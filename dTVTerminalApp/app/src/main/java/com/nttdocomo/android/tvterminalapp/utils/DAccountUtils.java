/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

/**
 * DAccountに関する共通処理を記載する.
 */
public class DAccountUtils {

    /**
     * Dアカウントアプリ Package名.
     */
    public static final String D_ACCOUNT_APP_PACKAGE_NAME = "com.nttdocomo.android.idmanager";
    /**
     * Dアカウントアプリ Activity名.
     */
    public static final String D_ACCOUNT_APP_ACTIVITY_NAME = ".activity.DocomoIdTopActivity";
    /**
     * DアカウントアプリURI.
     */
    public static final String D_ACCOUNT_APP_URI = "market://details?id=com.nttdocomo.android.idmanager";

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
     * Dアカウント設定を連携起動する.
     *
     * @param context コンテキストファイル
     */
    public static void startDAccountApplication(final Context context) {
        Intent intent = new Intent();
        intent.setClassName(D_ACCOUNT_APP_PACKAGE_NAME,
                StringUtils.getConnectStrings(D_ACCOUNT_APP_PACKAGE_NAME, D_ACCOUNT_APP_ACTIVITY_NAME));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //　アプリが無ければインストール画面に誘導
            CustomDialog dAccountUninstallDialog = new CustomDialog(context, CustomDialog.DialogType.CONFIRM);
            dAccountUninstallDialog.setContent(context.getString(R.string.main_setting_d_account_message));
            dAccountUninstallDialog.setConfirmText(R.string.positive_response);
            dAccountUninstallDialog.setCancelText(R.string.negative_response);
            dAccountUninstallDialog.setOkCallBack(new CustomDialog.ApiOKCallback() {
                @Override
                public void onOKCallback(final boolean isOK) {
                    Uri uri = Uri.parse(D_ACCOUNT_APP_URI);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
            dAccountUninstallDialog.showDialog();
        }
    }
}
