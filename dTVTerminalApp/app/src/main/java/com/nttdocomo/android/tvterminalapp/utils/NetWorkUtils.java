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
}
