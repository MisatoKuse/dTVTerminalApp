/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Intent;

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
}
