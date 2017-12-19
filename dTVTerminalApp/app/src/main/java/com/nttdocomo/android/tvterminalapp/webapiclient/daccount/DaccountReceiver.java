/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.daccount;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;

/**
 * dアカウントで状況の変化が発生した場合の通知を受け取るレシーバー
 * TODO: dアカウント用にマニフェストファイルの設定を行ったのに応じて生成された。中身はこれから
 */
public class DaccountReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //ブロードキャストの原因の取得
        String role = intent.getAction();

        //内容で処理を振り分ける
        if(role.equals(DaccountConstants.SET_ID_RECEIVER)) {
            //デフォルトIDの設定通知を受け取った。
        } else
        if(role.equals(DaccountConstants.USER_AUTH_RECEIVER)) {
            //ユーザー認証通知を受け取った。
        } else
        if(role.equals(DaccountConstants.DELETE_ID_RECEIVER)) {
            //ユーザー削除通知を受け取った。
        } else
        if(role.equals(DaccountConstants.INVALIDATE_ID_RECEIVER)) {
            //ユーザー無効化通知を受け取った。
        } else
        if(role.equals(DaccountConstants.LINKED_LINE_RECEIVER)) {
            //回線登録通知を受け取った。
        }
    }
}
