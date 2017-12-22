/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.Intent;

import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;
import com.nttdocomo.android.tvterminalapp.common.DtvtApplication;

/**
 * DAccountに関する共通処理を記載する
 */
public class DAccountUtils {

    /**
     * Dアカウント設定アプリがインストールされているか判定を行う
     *
     * @return intent:インストールされている null:インストールされていない
     */
    public static Intent checkDAccountIsExist() {
        Intent intent = new Intent();
        intent.setClassName(
                DaccountConstants.D_ACCOUNT_ID_MANAGER,
                DaccountConstants.D_ACCOUNT_ID_MANAGER_CLASS_NAME);
        return intent;
    }

    /**
     * dアカウントの削除や変更後に、自アプリがフォアグラウンドならば、アプリの再起動を行う
     *
     * @param context コンテキスト
     */
    public static void reStartApplication(Context context) {
        DTVTLogger.start();

        //実際に再起動を行うか判定を行う
        DtvtApplication dtvtApplication = (DtvtApplication) context.getApplicationContext();
        if (!dtvtApplication.isApplicationInForeground()) {
            //アプリはバックグラウンドだったので、再起動は行わずに帰る
            DTVTLogger.end("Application background");
            return;
        }

        DTVTLogger.debug("Application foreground");

        //再起動処理を行う
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        DTVTLogger.end();
    }
}
