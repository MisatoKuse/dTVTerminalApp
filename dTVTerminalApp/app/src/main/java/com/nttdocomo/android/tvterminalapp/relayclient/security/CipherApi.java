/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.relayclient.security;


import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.relayclient.RemoteControlRelayClient;
import com.nttdocomo.android.tvterminalapp.relayclient.StbConnectRelayClient;


public class CipherApi {
    public interface CipherApiCallback {
        /** STBがbusyの場合false. */
        void apiCallback(boolean result, @Nullable String data);
    }
    private CipherApiCallback mCallback;

    public CipherApi(CipherApiCallback callback) {
        mCallback = callback;
    }

    public void requestSendPublicKey() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CipherData cipherData;
                try {
                    cipherData = CipherUtil.generatePublicKey();
                } catch (Exception e) {
                    DTVTLogger.debug(e);
                    mCallback.apiCallback(false, null);
                    return;
                }

                byte[] exponent = cipherData.getExponentData();
                byte[] module = cipherData.getModuleData();

                StbConnectRelayClient stbConnection = StbConnectRelayClient.getInstance();
                boolean connectResult = stbConnection.connect();
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
        }).start();
    }

}
