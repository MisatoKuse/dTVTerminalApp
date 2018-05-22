/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

/**
 * ネットワーク監視ブロードキャスト.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    /**
     * 監視リスナー.
     */
    public NetworkChangeListener mNetworkChangeListener;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetWorkUtils.getNetworkState(context);
            if (mNetworkChangeListener != null) {
                mNetworkChangeListener.onNetworkStateChangeCallBack(netWorkState);
            }
        }
    }

    /**
     * ネットワーク監視リスナー.
     */
    public interface NetworkChangeListener {
        /**
         * ネットワークステータス監視コールバック.
         * @param netWorkState netWorkState
         */
        void onNetworkStateChangeCallBack(int netWorkState);
    }
}
