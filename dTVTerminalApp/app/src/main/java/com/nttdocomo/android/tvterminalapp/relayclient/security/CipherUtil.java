/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient.security;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {
    /** Share Key. */
    private static SecretKey mShareKey = null;

    /** Digest Key. */
    private static byte[] mSecureDigest = null;

    /** Public Key. */
    private static RSAPublicKey mPublicKey = null;

    /** Private Key. */
    private static RSAPrivateKey mPrivateKey = null;

    /** ivのサイズ. */
    private static final int IV_SIZE = 16;

    private static final Object mLockObject = new Object();

    public static boolean hasShareKey() {
        boolean result = mShareKey != null;
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
    public static CipherData generatePublicKey() throws Exception {
        byte[] module = null;
        byte[] exponent = null;
        synchronized (mLockObject) {
            if (mPublicKey == null) {
                KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
                keygen.initialize(2048);
                KeyPair keyPair = keygen.generateKeyPair();

                // 秘密キー
                mPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
                // 公開キー
                mPublicKey = (RSAPublicKey) keyPair.getPublic();
            }

            module = mPublicKey.getModulus().toByteArray();
            exponent = mPublicKey.getPublicExponent().toByteArray();
        }
        return new CipherData(module, exponent);
    }

    /**
     * 共通鍵を設定する.
     *
     * @param shareKey 公開鍵で暗号化された共通鍵の鍵データ
     * @throws NoSuchAlgorithmException 例外
     * @throws NoSuchPaddingException 例外
     * @throws InvalidKeyException 例外
     * @throws IllegalBlockSizeException 例外
     * @throws BadPaddingException 例外
     */
    public static void setShareKey(final byte[] shareKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        synchronized (mLockObject) {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, mPrivateKey);
            mSecureDigest = cipher.doFinal(shareKey);
            mShareKey = new SecretKeySpec(mSecureDigest, "AES");
        }
    }

    /**
     * Stringを暗号化してByte配列に変換する.
     *
     * @param srcDataString
     *            変換元文字列
     * @return 変換結果Byte配列
     * @throws Exception
     */
    public static byte[] encodeData(final String srcDataString)
            throws Exception {
        byte[] encodeByteStream = null;
        byte[] resultByteStream = null;
        byte[] ivCode = null;

        synchronized (mLockObject) {
            if (mShareKey == null) {
                DTVTLogger.warning("mShareKey == null");
                throw new Exception("Key has not been generated.");
            }

            byte[] srcDataBytes = srcDataString.getBytes("UTF-8");

            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] ivSourceCode = random.generateSeed(IV_SIZE);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, mShareKey, new IvParameterSpec(ivSourceCode));
            encodeByteStream = cipher.doFinal(srcDataBytes);
            ivCode = cipher.getIV();
        }

        resultByteStream = new byte[(ivCode.length + encodeByteStream.length)];
        System.arraycopy(ivCode, 0, resultByteStream, 0, ivCode.length);
        System.arraycopy(encodeByteStream, 0, resultByteStream, ivCode.length, encodeByteStream.length);
        return resultByteStream;
    }

    public static String decodeData(final byte[] srcData) throws Exception {
        byte[] encodeByteStream = new byte[srcData.length - IV_SIZE];
        byte[] decodeByteStream;

        byte[] ivCode = new byte[IV_SIZE];
        System.arraycopy(srcData, 0, ivCode, 0, IV_SIZE);
        System.arraycopy(srcData, IV_SIZE, encodeByteStream, 0, encodeByteStream.length);

        String decodeString = null;
        DTVTLogger.debug(" >>>");
        synchronized (mLockObject) {
            if (mShareKey == null) {
                DTVTLogger.warning("mShareKey == null");
                return null;
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, mShareKey, new IvParameterSpec(ivCode));
            decodeByteStream = cipher.doFinal(encodeByteStream);
        }
        decodeString = new String(decodeByteStream, "UTF-8");
        return decodeString;
    }

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
}
