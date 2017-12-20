/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * DAccountに関する処理を記載する
 */
public class DAccountUtils {

    //Dアカウントアプリpackage名
    private static final String D_ACCOUNT_PACKAGE_ID = "com.nttdocomo.android.idmanager";

    //DアカウントアプリのPlayストア用URI
    private static final String D_ACCOUNT_APP_URI =
            "market://details?id=com.nttdocomo.android.idmanager";

    //dアカウント登録アプリのパッケージ名
    private static final String D_ACCOUNT_ID_MANAGER = "com.nttdocomo.android.idmanager";

    //dアカウント登録アプリの設定画面アクティビティ名
    private static final String D_ACCOUNT_ID_ACTIVITY =
            "com.nttdocomo.android.idmanager.activity.DocomoIdTopActivity";

    /**
     * Dアカウント設定アプリがインストールされているか判定を行う
     *
     * @return intent:インストールされている null:インストールされていない
     */
    public static Intent checkDAccountIsExist(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getLaunchIntentForPackage(D_ACCOUNT_PACKAGE_ID);
    }

    /**
     * Dアカウント設定を連携起動する
     * TODO: ひとまずSettingActivityから流用・後ほど統合を行う予定
     */
    public static void startDAccountSetting(final Context context) {
        DTVTLogger.start();

        Intent intent = new Intent();
        intent.setClassName(
                D_ACCOUNT_ID_MANAGER,
                D_ACCOUNT_ID_ACTIVITY);

        if (intent != null) {
            context.startActivity(intent);
        } else {
            //　アプリが無ければインストール画面に誘導
            //TODO 独自ダイアログの使用。現在はAlertDialogを使用している。
            new AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.main_setting_d_account_message))
                    .setPositiveButton(context.getString(R.string.positive_response), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse(D_ACCOUNT_APP_URI);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton(context.getString(R.string.negative_response), null)
                    .show();
        }

        DTVTLogger.end();
    }

    /**
     * アプリの再起動を行う
     *
     * @param context コンテキスト
     */
    public static void reStartApli(Context context) {
        DTVTLogger.start();
        //TODO: 処理は後ほど
        DTVTLogger.end();
    }
}
