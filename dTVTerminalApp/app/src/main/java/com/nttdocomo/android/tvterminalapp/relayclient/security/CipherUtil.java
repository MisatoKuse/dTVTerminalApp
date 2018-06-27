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

    /** 暗号化処理の鍵交換の同期カウンター. */
    private static final int LATCH_COUNT_MAX = 1;

    /**
     * CipherUtil.decodeData の例外状態取得.
     * ※デコード実行時に発生する error:1e00007b:Cipher functions:OPENSSL_internal:WRONG_FINAL_BLOCK_LENGTH
     */
    public static boolean isCipherDecodeError() {
        return mCipherDecodeError;
    }

    /**CipherUtil.decodeData の例外状態設定.*/
    private static void setCipherDecodeError(final boolean cipherDecodeError) {
        mCipherDecodeError = cipherDecodeError;
    }

    /**CipherUtil.decodeData の例外.*/
    private static boolean mCipherDecodeError = false;

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
     * 公開鍵生成. 公開鍵生成済みであれば、生成のし直しは行わず、生成済みの公開鍵を返す.
     * 再生成が必要な場合には、clearPublicKey() を呼び出し鍵情報をクリアすること.
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
        DTVTLogger.start();
        synchronized (sLockObject) {
            try {
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, sPrivateKey);
                sSecureDigest = cipher.doFinal(shareKey);
                sShareKey = new SecretKeySpec(sSecureDigest, "AES");
                DTVTLogger.end();
                return true;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                    | InvalidKeyException | BadPaddingException e) {
                DTVTLogger.error(e.getMessage());
                return false;
            }
        }
    }

    /**
     * 電文を暗号化してByte配列に変換する.
     *
     * @param srcDataString　平文文字列
     * @return 暗号化データ or null
     */
    public static @Nullable byte[] encodeData(final String srcDataString) {
        DTVTLogger.start();
        byte[] encodeByteStream = null;
        byte[] resultByteStream = null;
        byte[] ivCode = null;

        synchronized (sLockObject) {
            if (sShareKey == null) {
                DTVTLogger.warning("ShareKey has not been generated!");
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
        DTVTLogger.debug(String.format("encoded byte stream length = %s", resultByteStream.length));
        DTVTLogger.end();
        return resultByteStream;
    }

    /**
     * 暗号化データを復号して平文を返す.
     * @param srcData 暗号化データ srcData
     * @return 復号された平文 or null
     */
    public static String decodeData(final byte[] srcData) {
        DTVTLogger.start();
        byte[] encodeByteStream = new byte[srcData.length - IV_SIZE];
        byte[] decodeByteStream;

        byte[] ivCode = new byte[IV_SIZE];
        System.arraycopy(srcData, 0, ivCode, 0, IV_SIZE);
        System.arraycopy(srcData, IV_SIZE, encodeByteStream, 0, encodeByteStream.length);
        DTVTLogger.debug(String.format("encoded byte stream length = %s", encodeByteStream.length));

        String decodeString = null;
        synchronized (sLockObject) {
            if (sShareKey == null) {
                setCipherDecodeError(true);
                DTVTLogger.warning("ShareKey is null!");
                return null;
            }
            try {
                setCipherDecodeError(false);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, sShareKey, new IvParameterSpec(ivCode));
                decodeByteStream = cipher.doFinal(encodeByteStream);
                decodeString = new String(decodeByteStream, "UTF-8");
                DTVTLogger.end();
                return decodeString;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException
                    | InvalidAlgorithmParameterException | IllegalBlockSizeException | UnsupportedEncodingException e) {
                DTVTLogger.debug("decoding failed.");
                setCipherDecodeError(true);
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
        DTVTLogger.start();
        // 暗号化処理の鍵交換を同期処理で実行する.
        final CountDownLatch latch = new CountDownLatch(LATCH_COUNT_MAX);

        CipherApi api = new CipherApi(new CipherApi.CipherApiCallback() {
            @Override
            public void apiCallback(final boolean result, final String data) {
                // 鍵交換処理に同期ラッチカウンターを使用するためコールバック処理は不要
            }
        }, latch);
        DTVTLogger.debug("sending public key");
        api.requestSendPublicKey();
        try {
            DTVTLogger.debug("sync to completion of public key transmission");
            latch.await(); // 同期ラッチの待ち合わせ
            DTVTLogger.debug("completion of public key transmission");
        } catch (InterruptedException e) {
            DTVTLogger.debug(e);
            return;
        }
        DTVTLogger.end();
    }

    /**
     * 鍵交換処理を行う.
     * 他のドコテレアプリによる鍵交換が行われていた場合に交換済みの鍵が失効する場合があるため
     * リモコン使用時は鍵交換を行う
     */
    public static void exchangeKey() {
        DTVTLogger.start();
        // 鍵交換処理の同期処理のためにリモコン表示が停止しないようにスレッドを使用する
        new Thread(new Runnable() {
            @Override
            public void run() {
                syncRequestPublicKey();
                DTVTLogger.debug(String.format("public key exchange processing result = %s",
                        CipherUtil.hasShareKey())); // 鍵交換処理結果
            }
        }).start();
        DTVTLogger.end();
    }

}
