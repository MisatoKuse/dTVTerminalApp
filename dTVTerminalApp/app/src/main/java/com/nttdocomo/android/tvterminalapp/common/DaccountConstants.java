/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

import com.nttdocomo.android.tvterminalapp.BuildConfig;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;

/**
 * dアカウント連携用の固定値を格納する.
 */
public class DaccountConstants {
    /**
     * dアカウント設定アプリのクラス名.
     */
    public static final String D_ACCOUNT_ID_MANAGER = "com.nttdocomo.android.idmanager";

    /**
     * dアカウント設定アプリのクラス名.
     */
    public static final String D_ACCOUNT_ID_MANAGER_CLASS_NAME =
            "com.nttdocomo.android.idmanager.activity.DocomoIdTopActivity";

    /**
     * dアカウント設定アプリのサービス名.
     */
    public static final String D_ACCOUNT_SERVICE =
            "com.nttdocomo.android.idmanager.DimServiceAppService";

    /**
     * サービス登録専用の識別名.
     */
    public static final String REGIST_SERVICE_ACTION = "DimServiceAppServiceCustom";

    /**
     * DTVTアプリに割り当てられたサービス識別キー(OTP):商用署名向け(商用向きor検証向きどちらも同じ).
     */
    private static final String SERVICE_KEY_COM = "B3t02";
    /**
     * DTVTアプリに割り当てられたサービス識別キー(OTP):商用向き開発署名向け.
     */
    private static final String SERVICE_KEY_COM_DEBUG = "B3s02";
    /**
     * DTVTアプリに割り当てられたサービス識別キー(OTP):検証向き開発署名向け.
     */
    private static final String SERVICE_KEY_TEST_DEBUG = "B3d02";

    //ブロードキャスト登録名
    /**
     * デフォルトのdアカウントIDを知らせる場合.
     */
    public static final String SET_ID_RECEIVER =
            "com.nttdocomo.android.idmanager.action.DOCOMOID_SET_DEFAULT";
    /**
     * dアカウントユーザー認証が発生した事を知らせる場合.
     */
    public static final String USER_AUTH_RECEIVER =
            "com.nttdocomo.android.idmanager.action.DOCOMOID_AUTHENTICATED";
    /**
     * dアカウントユーザーの削除を知らせる場合.
     */
    public static final String DELETE_ID_RECEIVER =
            "com.nttdocomo.android.idmanager.action.DOCOMOID_REMOVED";
    /**
     * dアカウント無効化を知らせる場合.
     */
    public static final String INVALIDATE_ID_RECEIVER =
            "com.nttdocomo.android.idmanager.action.DOCOMOID_INVALIDATE";
    /**
     * 回線連携完了を知らせる場合.
     */
    public static final String LINKED_LINE_RECEIVER =
            "com.nttdocomo.android.idmanager.action.DOCOMOID_LINKED_LINE";
    /**
     * アプリ除外を知らせる場合.
     */
    public static final String SERVICEAPP_REMOVED_RECEIVER =
            "com.nttdocomo.android.idmanager.action.SERVICEAPP_REMOVED";
    /**
     * DTVTアプリに割り当てられたサービス識別キー(OTP)を返却する.
     * 商用/検証向き、また署名によって変わるのでメソッド化.
     *
     * @return dアカントサービスキー取得.
     */
    public static String getDaccountServiceKey() {
        if ("unsigned_off".equals(BuildConfig.BUILD_TYPE)
                || "unsigned_on".equals(BuildConfig.BUILD_TYPE)
                || "_unsigned_off".equals(BuildConfig.BUILD_TYPE)
                || "_unsigned_on".equals(BuildConfig.BUILD_TYPE)) {
            // 商用署名向け(商用向きor検証向きどちらも同じ).
            return SERVICE_KEY_COM;
        } else if ("signed_off".equals(BuildConfig.BUILD_TYPE)
                || "signed_on".equals(BuildConfig.BUILD_TYPE)) {

            String buildTarget = BuildConfig.FLAVOR + BuildConfig.BUILD_TYPE;
            if (ContentUtils.LOCAL_SIGNED_ON.equals(buildTarget)) {
                // 検証向き開発署名向け.
                return SERVICE_KEY_TEST_DEBUG;
            }
            // 商用向き開発署名向け.
            return SERVICE_KEY_COM_DEBUG;
        } else {
            // 検証向き開発署名向け.
            return SERVICE_KEY_TEST_DEBUG;
        }
    }
}
