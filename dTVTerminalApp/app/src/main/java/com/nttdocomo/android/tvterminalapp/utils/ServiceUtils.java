/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Service用のUtilクラス.
 */
public class ServiceUtils {

    /**
     * サービスの実行状態を返却する.
     *
     * @param context     コンテキスト
     * @param serviceName サービスクラス名
     * @return サービス実行状態
     */
    @SuppressWarnings("deprecation")
    public static boolean isRunningService(final Context context, final String serviceName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
        boolean found = false;
        for (ActivityManager.RunningServiceInfo curr : listServiceInfo) {
            // クラス名を比較
            if (curr.service.getClassName().equals(serviceName)) {
                // 実行中のサービスと一致
                found = true;
                break;
            }
        }
        return found;
    }
}
