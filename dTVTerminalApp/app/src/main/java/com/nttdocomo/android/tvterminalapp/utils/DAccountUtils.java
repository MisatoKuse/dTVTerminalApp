/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * DAccountに関する処理を記載する
 */
public class DAccountUtils {

    //Dアカウントアプリpackage名
    private static final String D_ACCOUNT_PACKAGE_ID = "com.nttdocomo.android.idmanager";

    /**
     * Dアカウント設定アプリがインストールされているか判定を行う
     *
     * @return intent:インストールされている null:インストールされていない
     */
    public static Intent checkDAccountIsExist(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getLaunchIntentForPackage(D_ACCOUNT_PACKAGE_ID);
    }
}
