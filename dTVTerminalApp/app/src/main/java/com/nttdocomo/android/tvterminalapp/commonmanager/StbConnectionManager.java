/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.commonmanager;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

public class StbConnectionManager {

    public interface ConnectionListener {
        void onConnectionChangeCallback(boolean isStbConnectedHomeNetwork);
    }

    /**
     * shared.
     * @return Instance
     */
    public static StbConnectionManager shared() {
        return sInstance;
    }

    /** singletone. */
    private static StbConnectionManager sInstance = new StbConnectionManager();
    /** StbConnectionManager. */
    private StbConnectionManager() {
    }

    public enum ConnectionStatus {
        NONE_PAIRING, // ペアリングもしてない

        NONE_LOCAL_REGISTRATION, //ペアリングしてるけどLRしてない

        HOME_OUT, // 宅外状態でSTBとまだ接続してない

        HOME_OUT_CONNECT, // 宅外+リモート接続中

        HOME_IN // 宅内
    }


    // region variable
    private ConnectionStatus connectionStatus = ConnectionStatus.NONE_PAIRING;
    private Context mContext;

    public ConnectionListener mConnectionListener = null;
    // endregion variable

    // region method

    public void launch(Context context) {
        mContext = context;

        boolean connected = SharedPreferencesUtils.getSharedPreferencesStbConnect(context);
        if (connected) {
            connectionStatus = ConnectionStatus.HOME_OUT;
        }
    }

    /**
     * ホーム初回遷移時やwifi切り替え時にConnectionStatusを初期化し、再度チェックをする
     */
    public void initializeState() {
        DTVTLogger.info("before connectionStatus = " + connectionStatus);
        connectionStatus = ConnectionStatus.NONE_PAIRING;
        boolean connected = SharedPreferencesUtils.getSharedPreferencesStbConnect(mContext);
        if (connected) { // ペアリング済みであればRLを確認
            DlnaDmsItem item = SharedPreferencesUtils.getSharedPreferencesStbInfo(mContext);
            String remoteDeviceExpireDate = DlnaManager.shared().GetRemoteDeviceExpireDate(item.mUdn);
            if (TextUtils.isEmpty(remoteDeviceExpireDate)) {
                connectionStatus = ConnectionStatus.NONE_LOCAL_REGISTRATION;
            } else {
                connectionStatus = ConnectionStatus.HOME_OUT;
            }
        }
        DTVTLogger.info("after connectionStatus = " + connectionStatus);
        setConnectionStatus(connectionStatus);
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        DTVTLogger.warning("before connectionStatus = " + this.connectionStatus + ", after connectionStatus = " + connectionStatus);
        this.connectionStatus = connectionStatus;
        if (mConnectionListener != null) {
            mConnectionListener.onConnectionChangeCallback(connectionStatus == ConnectionStatus.HOME_IN);
        } else {
            DTVTLogger.warning("mConnectionListener == null");
        }
    }


    public void networkStatusChanged(NetWorkUtils.NetworkState networkState) {
        DTVTLogger.warning("networkState = " + networkState);
        switch (networkState) {
            case NONE:

              break;
            case MOBILE:

                break;
            case WIFI:

                break;
        }
        if (connectionStatus != ConnectionStatus.NONE_PAIRING) {
            connectionStatus = ConnectionStatus.HOME_OUT;
        }
    }
    // endregion method

}
