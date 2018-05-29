/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;

/**
 * ネットワーク監視ブロードキャスト.
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetWorkUtils.NetworkState netWorkState = NetWorkUtils.getNetworkState(context);
            StbConnectionManager.shared().networkStatusChanged(netWorkState);
        }
    }
}
