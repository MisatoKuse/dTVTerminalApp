/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient.security;


import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.CountDownLatch;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 暗号化CipherUtilクラス.
 */
public class CipherUtil {
    /** Share Key. */
    private static SecretKey sShareKey = null;

    /** Digest Key. */
    private static byte[] sSecureDigest = null;

    /** Public Key. */
    private static RSAPublicKey sPublicKey = null;

    /** Private Key. */
    private static RSAPrivateKey sPrivateKey = null;

    /** ivのサイズ. */
    private static final int IV_SIZE = 16;
    /** LockObject.*/
    private static final Object sLockObject = new Object();

    /** 暗号化処理の鍵交換を同期処理で実行する. */
    private static CountDownLatch mLatch = null;
    /** 暗号化処理の鍵交換の同期カウンター. */
    private static int LATCH_COUNT_MAX = 1;

    /**
     * 共通鍵あるかをチェック.
     * @return true or false
     */
    public static boolean hasShareKey() {
        boolean result = sShareKey != null;
        DTVTLogger.warning("result = " + result);
        return result;
    }
    /**
     * 公開鍵生成. 公開鍵生成済みであれば、生成のし直しは行わず、生成済みの公開鍵を返す. 再生成が必要な場合には、clearPublicKey()
     * を呼び出し鍵情報をクリアすること.
     *
     * @return 鍵生成結果.
     * @throws NoSuchAlgorithmException 例外
     */
    public static CipherData generatePublicKey() throws NoSuchAlgorithmException {
        byte[] module = null;
        byte[] exponent = null;
        synchronized (sLockObject) {
            if (sPublicKey == null) {
                KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
                keygen.initialize(2048);
                KeyPair keyPair = keygen.generateKeyPair();

                // 秘密キー
                sPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
                // 公開キー
                sPublicKey = (RSAPublicKey) keyPair.getPublic();
            }

            module = sPublicKey.getModulus().toByteArray();
            exponent = sPublicKey.getPublicExponent().toByteArray();
        }
        return new CipherData(module, exponent);
    }

    /**
     * 共通鍵を設定する.
     * @param shareKey shareKey
     * @return 設定結果
     */
    public static boolean setShareKey(final byte[] shareKey) {
        synchronized (sLockObject) {
            try {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, sPrivateKey);
                sSecureDigest = cipher.doFinal(shareKey);
                sShareKey = new SecretKeySpec(sSecureDigest, "AES");
                return true;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
                return false;
            }
        }
    }

    /**
     * Stringを暗号化してByte配列に変換する.
     *
     * @param srcDataString
     *            変換元文字列
     * @return 変換結果Byte配列 or null
     */
    public static @Nullable byte[] encodeData(final String srcDataString) {
        byte[] encodeByteStream = null;
        byte[] resultByteStream = null;
        byte[] ivCode = null;

        synchronized (sLockObject) {
            if (sShareKey == null) {
                DTVTLogger.warning("Key has not been generated.");
                return null;
            }
            try {
                byte[] srcDataBytes = srcDataString.getBytes("UTF-8");
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                byte[] ivSourceCode = random.generateSeed(IV_SIZE);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, sShareKey, new IvParameterSpec(ivSourceCode));
                encodeByteStream = cipher.doFinal(srcDataBytes);
                ivCode = cipher.getIV();
            } catch (UnsupportedEncodingException | IllegalBlockSizeException | NoSuchAlgorithmException
                    | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException
                    | NoSuchPaddingException e) {
                DTVTLogger.error(e.getMessage());
                return null;
            }
        }
        resultByteStream = new byte[(ivCode.length + encodeByteStream.length)];
        System.arraycopy(ivCode, 0, resultByteStream, 0, ivCode.length);
        System.arraycopy(encodeByteStream, 0, resultByteStream, ivCode.length, encodeByteStream.length);
        return resultByteStream;
    }

    /**
     * ディコードデータ.
     * @param srcData 変換前srcData
     * @return 変換後データ
     */
    public static String decodeData(final byte[] srcData) {
        byte[] encodeByteStream = new byte[srcData.length - IV_SIZE];
        byte[] decodeByteStream;

        byte[] ivCode = new byte[IV_SIZE];
        System.arraycopy(srcData, 0, ivCode, 0, IV_SIZE);
        System.arraycopy(srcData, IV_SIZE, encodeByteStream, 0, encodeByteStream.length);

        String decodeString = null;
        DTVTLogger.debug(" >>>");
        synchronized (sLockObject) {
            if (sShareKey == null) {
                DTVTLogger.warning("mShareKey == null");
                return null;
            }
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, sShareKey, new IvParameterSpec(ivCode));
                decodeByteStream = cipher.doFinal(encodeByteStream);
                decodeString = new String(decodeByteStream, "UTF-8");
                return decodeString;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException
                    | InvalidAlgorithmParameterException | IllegalBlockSizeException | UnsupportedEncodingException e) {
                DTVTLogger.error(e.getMessage());
                return null;
            }
        }
    }
    /**
     * writeInt.
     * @param value value
     * @param writeBuffer writeBuffer
     */
    public static void writeInt(final int value, final byte[] writeBuffer) {
        int tempValue = value;
        writeBuffer[3] = (byte) tempValue;
        tempValue >>>= 8;
        writeBuffer[2] = (byte) tempValue;
        tempValue >>>= 8;
        writeBuffer[1] = (byte) tempValue;
        tempValue >>>= 8;
        writeBuffer[0] = (byte) tempValue;
    }

    /**
     * 鍵交換処理を同期処理で実行する.
     */
    public static void syncRequestPublicKey() {
        CipherApi api = new CipherApi(new CipherApi.CipherApiCallback() {
            @Override
            public void apiCallback(final boolean result, final String data) {
                // 鍵交換処理同期ラッチカウンターを解除する
                mLatch.countDown();
            }
        });
        DTVTLogger.debug("sending public key");
        api.requestSendPublicKey();
        // 鍵交換処理が終わるまで待機する.
        mLatch = new CountDownLatch(LATCH_COUNT_MAX);
        try {
            DTVTLogger.debug("sync to completion of public key transmission");
            mLatch.await();
            DTVTLogger.debug("completion of public key transmission");
        } catch (InterruptedException e) {
            DTVTLogger.debug(e);
            return;
        }
    }

}
