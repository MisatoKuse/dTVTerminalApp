/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.app.Activity;
import android.content.Intent;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;

/**
 * DAccountに関する共通処理を記載する.
 */
public class DAccountUtils {

    /**
     * Dアカウント設定アプリがインストールされているか判定を行う.
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
     * dアカウントの削除や変更後に、自アプリがフォアグラウンドならば、アプリの再起動を行う.
     *
     * @param activity アクティビティ
     */
    public static void reStartApplication(final Activity activity) {
        DTVTLogger.start();

        //再起動処理を行う
        Intent intent = new Intent();
        intent.setClass(activity, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseActivity baseActivity = (BaseActivity) activity;
        baseActivity.startActivity(intent);

        DTVTLogger.end();
    }
}
