/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.daccount;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;
import com.nttdocomo.android.tvterminalapp.utils.DAccountUtils;

import java.util.Set;

/**
 * dアカウントで状況の変化が発生した場合の通知を受け取るレシーバー
 */
public class DaccountReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //ブロードキャストの原因の取得
        String role = intent.getAction();

        //送られてきたデータをログに表示
        Bundle intentBundle = intent.getExtras();
        Set<String> bundleLists = intentBundle.keySet();
        for (String key : bundleLists) {
            Object bundleData = intentBundle.get(key);
            if (bundleData != null) {
                String buffer = bundleData.toString();
                DTVTLogger.debug("in Data " + key + "=" + buffer);
            }
        }

        //内容で処理を振り分ける
        switch (role) {
            case DaccountConstants.SET_ID_RECEIVER:
                //デフォルトIDの設定通知を受け取った。
                DTVTLogger.debug("SET_ID_RECEIVER");

                //ユーザーが登録された場合は、キャッシュクリアを呼ぶ。その後は再起動
                DaccountControl.cacheClear(context);

                break;
            case DaccountConstants.USER_AUTH_RECEIVER:
                //ユーザー認証通知を受け取った。
                DTVTLogger.debug("USER_AUTH_RECEIVER");

                //ユーザーが登録された場合は、キャッシュクリアを呼ぶ。その後は再起動
                DaccountControl.cacheClear(context);

                break;
            case DaccountConstants.DELETE_ID_RECEIVER:
                //ユーザー削除通知を受け取った。
                DTVTLogger.debug("DELETE_ID_RECEIVER");

                //ユーザーが削除されていた場合は、キャッシュクリアを呼ぶ。その後は再起動
                DaccountControl.cacheClear(context);

                break;
            case DaccountConstants.INVALIDATE_ID_RECEIVER:
                //ユーザー無効化通知を受け取った。
                DTVTLogger.debug("INVALIDATE_ID_RECEIVER");

                //ユーザーが無効化されていた場合は、キャッシュクリアを呼ぶ。その後は再起動
                DaccountControl.cacheClear(context);

                break;
            case DaccountConstants.LINKED_LINE_RECEIVER:
                //回線登録通知を受け取った。
                DTVTLogger.debug("INVALIDATE_ID_RECEIVER");
                break;
            case DaccountConstants.SERVICEAPP_REMOVED_RECEIVER:
                //アプリ除外通知を受け取った。
                DTVTLogger.debug("SERVICE_APP_REMOVED_RECEIVER");
                break;
            default:
                //アナライザー対策なので無処理
        }
    }
}
