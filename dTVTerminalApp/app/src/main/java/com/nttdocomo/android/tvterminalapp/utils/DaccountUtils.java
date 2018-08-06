/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.nttdocomo.android.ocsplib.bouncycastle.operator.RuntimeOperatorException;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DaccountConstants;
import com.nttdocomo.android.tvterminalapp.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * DAccountに関する共通処理を記載する.
 */
public class DaccountUtils {

    /**
     * Dアカウントアプリ Package名.
     */
    public static final String D_ACCOUNT_APP_PACKAGE_NAME = "com.nttdocomo.android.idmanager";
    /**
     * Dアカウントアプリ Activity名.
     */
    private static final String D_ACCOUNT_APP_ACTIVITY_NAME = ".activity.DocomoIdTopActivity";
    /**
     * DアカウントアプリURI.
     */
    private static final String D_ACCOUNT_APP_URI = "https://play.google.com/store/apps/details?id=com.nttdocomo.android.idmanager";
    /**
     * dアカウント設定アプリが見つからない場合のエラーコード.
     * 必ず、IDimDefinesの値と重複しない物とする
     */
    public static final int D_ACCOUNT_APP_NOT_FOUND_ERROR_CODE = -999;

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

    /**
     * 指定されたアプリがインストール済みかどうかのチェック.
     *
     * @param context コンテキスト
     * @param packageName アプリのパッケージ名
     * @return インストールされているならばtrue
     */
    public static boolean checkInstalled(final Context context, final String packageName) {
        PackageManager packageManager = null;
        List<PackageInfo> packageInfos = null;
        try {
            //アプリ一覧の取得
            packageManager = context.getPackageManager();
            packageInfos = packageManager.getInstalledPackages(0);
        } catch (RuntimeException exception) {
            //Androidのバグと思われる原因により、稀に本例外が発生する。情報が取得できないので、アプリ有りの扱いとする
            //本メソッドは現状dアカウントアプリの有無のみ使用している。本当にdアカウントアプリが存在しなければ、後のバインドでエラーとなるので許容する
            DTVTLogger.debug(exception);
            return true;
        }

        //情報数だけ回る
        for (PackageInfo individualInfo : packageInfos) {
            //パッケージ名が含まれるかどうかの確認
            if (individualInfo.packageName.equals(packageName)) {
                //該当パッケージが有効かどうかの確認
                int applicationStatus = packageManager.getApplicationEnabledSetting(packageName);
                if (applicationStatus == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT ||
                    applicationStatus == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                    //指定された名前が見つかり、かつ有効なので、trueで帰る
                    return true;
                }
            }
        }

        //見つからなかったか、見つかっても無効化されていたのでfalseで帰る
        return false;
    }
}
