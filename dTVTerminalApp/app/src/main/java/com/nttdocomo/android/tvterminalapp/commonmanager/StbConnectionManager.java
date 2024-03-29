/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.commonmanager;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

/**
 * STB接続マネージャー.
 */
public class StbConnectionManager {

    /**
     * STB接続リスナー.
     */
    public interface ConnectionListener {
        /**
         * 宅内、宅外チェックコールバック.
         * @param isStbConnectedHomeNetwork 宅内、宅外チェック
         */
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
    private static final StbConnectionManager sInstance = new StbConnectionManager();
    /** StbConnectionManager. */
    private StbConnectionManager() {
    }

    /**
     * 接続ステータス.
     */
    public enum ConnectionStatus {
        /** ペアリングもしてない. */
        NONE_PAIRING,
        /** ペアリングしてるけどLRしてない. */
        NONE_LOCAL_REGISTRATION,
        /** 宅外状態でSTBとまだ接続してない. */
        HOME_OUT,
        /** 宅外+リモート接続中. */
        HOME_OUT_CONNECT,
        /** 宅内. */
        HOME_IN
    }


    // region variable
    /** 接続ステータス. */
    private ConnectionStatus connectionStatus = ConnectionStatus.NONE_PAIRING;
    /** コンテキスト. */
    private Context mContext;
    /** 接続リスナー. */
    private ConnectionListener mConnectionListener = null;
    // endregion variable

    // region method

    /**
     * launch.
     * @param context コンテキスト
     */
    public void launch(final Context context) {
        StbConnectionManager.shared().mContext = context;

        boolean connected = SharedPreferencesUtils.getSharedPreferencesStbConnect(context);
        if (connected) {
            StbConnectionManager.shared().connectionStatus = ConnectionStatus.HOME_OUT;
        }
    }

    /**
     * ホーム初回遷移時やwifi切り替え時にConnectionStatusを初期化し、再度チェックをする.
     */
    public void initializeState() {
        DTVTLogger.info("before connectionStatus = " + connectionStatus);
        StbConnectionManager.shared().connectionStatus = ConnectionStatus.NONE_PAIRING;
        boolean connected = SharedPreferencesUtils.getSharedPreferencesStbConnect(StbConnectionManager.shared().mContext);
        if (connected) { // ペアリング済みであればRLを確認
            String remoteDeviceExpireDate = SharedPreferencesUtils.getRemoteDeviceExpireDate(StbConnectionManager.shared().mContext);
            if (TextUtils.isEmpty(remoteDeviceExpireDate)) {
                StbConnectionManager.shared().connectionStatus = ConnectionStatus.NONE_LOCAL_REGISTRATION;
            } else {
                StbConnectionManager.shared().connectionStatus = ConnectionStatus.HOME_OUT;
            }
        }
        DTVTLogger.info("after connectionStatus = " + connectionStatus);
        setConnectionStatus(StbConnectionManager.shared().connectionStatus);
    }

    /**
     * 接続ステータスの取得.
     * @return 接続ステータス
     */
    public ConnectionStatus getConnectionStatus() {
        return StbConnectionManager.shared().connectionStatus;
    }

    /**
     * 接続ステータスの更新.
     * @param connectionStatus 接続ステータス
     */
    public void setConnectionStatus(final ConnectionStatus connectionStatus) {
        DTVTLogger.warning("before connectionStatus = " + this.connectionStatus + ", after connectionStatus = " + connectionStatus);
        StbConnectionManager.shared().connectionStatus = connectionStatus;
        if (StbConnectionManager.shared().mConnectionListener != null) {
            StbConnectionManager.shared().mConnectionListener.onConnectionChangeCallback(StbConnectionManager.shared().connectionStatus == ConnectionStatus.HOME_IN);
        } else {
            DTVTLogger.warning("mConnectionListener == null");
        }
    }

    public void setConnectionListener(final ConnectionListener listener) {
        StbConnectionManager.shared().mConnectionListener = listener;
    }
    // endregion method

}
