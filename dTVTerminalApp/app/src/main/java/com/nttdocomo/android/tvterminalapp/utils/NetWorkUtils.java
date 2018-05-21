/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * ネットワーク関連のUtilsクラス.
 */
public class NetWorkUtils {

    /** ネットワーク状態（NONE）. */
    public static final int NETWORK_NONE = -1;
    /** ネットワーク状態（MOBILE）.*/
    public static final int NETWORK_MOBILE = 0;
    /** ネットワーク状態（WIFI）. */
    public static final int NETWORK_WIFI = 1;

    /**
     * 通信可能確認.
     *
     * @param context コンテキスト
     * @return 通信可能ならばtrue
     */
    public static boolean isOnline(final Context context) {
        DTVTLogger.start();
        if (context == null) {
            DTVTLogger.warning("context == null");
            return false;
        }
        //システムのネットワーク情報を取得する
        ConnectivityManager connectManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectManager != null) {
            networkInfo = connectManager.getActiveNetworkInfo();
        }

        DTVTLogger.end();

        //通信手段が無い場合は、networkInfoがヌルになる
        //手段があっても接続されていないときは、isConnected()がfalseになる
        //どちらの場合も通信は不可能なので、falseを返す
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 通信状態取得.
     *
     * @param context コンテキスト
     * @return WIFI、MOBILE、NONE
     */
    public static int getNetWorkState(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }
}
