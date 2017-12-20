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
 * TODO: dアカウント用にマニフェストファイルの設定を行ったのに応じて生成された。中身はこれから
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
            String buffer = intentBundle.get(key).toString();
            if (buffer == null) {
                buffer = "";
            }

            DTVTLogger.debug("in Data " + key + "=" + buffer);
        }

        //内容で処理を振り分ける
        if (role.equals(DaccountConstants.SET_ID_RECEIVER)) {
            //デフォルトIDの設定通知を受け取った。
            DTVTLogger.debug("SET_ID_RECEIVER");

            //アプリの再起動を行う
            DAccountUtils.reStartApli(context);

        } else if (role.equals(DaccountConstants.USER_AUTH_RECEIVER)) {
            //ユーザー認証通知を受け取った。
            DTVTLogger.debug("USER_AUTH_RECEIVER");
        } else if (role.equals(DaccountConstants.DELETE_ID_RECEIVER)) {
            //ユーザー削除通知を受け取った。
            DTVTLogger.debug("DELETE_ID_RECEIVER");

            //ユーザーが削除されていた場合は、キャッシュクリアを呼ぶ
            DaccountControl.cacheClear(context);

        } else if (role.equals(DaccountConstants.INVALIDATE_ID_RECEIVER)) {
            //ユーザー無効化通知を受け取った。
            DTVTLogger.debug("INVALIDATE_ID_RECEIVER");

            //ユーザーが無効化されていた場合は、キャッシュクリアを呼ぶ
            DaccountControl.cacheClear(context);

        } else if (role.equals(DaccountConstants.LINKED_LINE_RECEIVER)) {
            //回線登録通知を受け取った。
            DTVTLogger.debug("INVALIDATE_ID_RECEIVER");
        } else if (role.equals(DaccountConstants.SERVICEAPP_REMOVED_RECEIVER)) {
            //アプリ除外通知を受け取った。
            DTVTLogger.debug("SERVICEAPP_REMOVED_RECEIVER");
        }
    }
}
