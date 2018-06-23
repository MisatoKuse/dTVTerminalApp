/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient.security;


import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.relayclient.StbConnectRelayClient;
import com.nttdocomo.android.tvterminalapp.relayclient.TcpClient;

import java.security.NoSuchAlgorithmException;

/**
 * 暗号化API.
 */
public class CipherApi {
    /**
     * callback.
     */
    public interface CipherApiCallback {
       //STBがbusyの場合false.
        /**
         * 処理完了callback.
         * @param result result
         * @param data data
         */
        void apiCallback(boolean result, @Nullable String data);
    }
    /**callback.*/
    private final CipherApiCallback mCallback;

    /**
     * 構造体.
     * @param callback callback
     */
    public CipherApi(final CipherApiCallback callback) {
        mCallback = callback;
    }

    /**
     * 鍵交換処理.
     */
    public void requestSendPublicKey() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                syncronizeRequestSendPublicKey();
            }
        }).start();
    }

    /**
     * 鍵交換の同期処理.
     */
    private synchronized void syncronizeRequestSendPublicKey() {
        CipherData cipherData;
        try {
            cipherData = CipherUtil.generatePublicKey();
        } catch (NoSuchAlgorithmException e) {
            DTVTLogger.debug(e);
            mCallback.apiCallback(false, null);
            return;
        }

        byte[] exponent = cipherData.getExponentData();
        byte[] module = cipherData.getModuleData();

        StbConnectRelayClient stbConnection = StbConnectRelayClient.getInstance();
        boolean connectResult = stbConnection.connect(TcpClient.KEY_EXCHANGE_TIMEOUT);
        DTVTLogger.debug("connectResult = " + connectResult);

        byte[] actionBytes = new byte[CipherConfig.BYTE_LENGTH_ACTION];
        CipherUtil.writeInt(CipherConfig.ACTION_EXCHANGE_KEY, actionBytes);

        byte[] sum = new byte[actionBytes.length + exponent.length + module.length];
        System.arraycopy(actionBytes, 0, sum, 0, actionBytes.length);
        System.arraycopy(exponent, 0, sum, actionBytes.length, exponent.length);
        System.arraycopy(module, 0, sum, actionBytes.length + exponent.length, module.length);

        boolean sendResult = stbConnection.send(sum, sum.length);
        DTVTLogger.debug("sendResult = " + sendResult);
        if (sendResult) {
            boolean exchangeKeyResult = stbConnection.receiveExchangeKey();
            DTVTLogger.debug("exchangeKeyResult = " + exchangeKeyResult);
        }
        stbConnection.disconnect();
        mCallback.apiCallback(sendResult, null);
    }
}
