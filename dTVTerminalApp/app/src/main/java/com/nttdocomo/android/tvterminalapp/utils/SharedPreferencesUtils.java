/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;

public class SharedPreferencesUtils {

    // SharedPreferences ペアリング情報保存キー 親キー
    private static final String SHARED_KEY_PAIRING_INFOMATION = "pairing_info";
    // STB選択画面"次回以降表示しない" 選択保存キー
    private static final String SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME = "unnecessary_next_time";
    // STB接続画面 接続成功 保存キー
    private static final String SHARED_KEY_STB_CONNECT_SUCCESS = "connect_success";
    // ペアリング勧誘画面表示済み判定情報 保存キー
    private static final String SHARED_KEY_IS_DISPLAYED_PARING_INVITATION = "is_displayed_paring_invitation";
    // ホーム画面ペアリング済み判定 保存キー
    private static final String SHARED_KEY_DECISION_PARING_SETTLED = "decision_paring_settled";
    // 接続済み STB情報 保存キー
    // STB情報 親キー
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION = "stb_data_info";
    // UDN
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_UDN = "selected_stb_udn";
    // コントロールURL
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_CONTROL_URL = "selected_stb_control_url";
    // HTTP
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_HTTP = "selected_stb_http";
    // Friendly名
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_FRIENDLY_NAME = "selected_stb_friendly_name";
    // IPv6アドレス
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_IPADDRESS = "selected_stb_ipaddress";
    // getString 初期値
    private static final String SHARED_GET_STRING_DEFAULT = "";
    // 画面情報保存 親キー
    private static final String SHARED_KEY_SCREEN_INFORMATION = "screen_information";
    // LaunchActivity チュートリアル表示済み判定 保存キー
    private static final String SHARED_KEY_IS_DISPLAYED_TUTORIAL = "is_displayed_tutorial";
    // SettinActivity 画質設定の設定値
    private static final String SHARED_KEY_IMAGE_QUALITY = "image_quality";
    // 持ち出しコンテンツダウンロード先 ture:内部 false:外部
    private static final String SHARED_KEY_STORAGE_PATH = "storage_path";


    /**
     * STB選択画面"次回以降表示しない" 状態を保存
     *
     * @param context             コンテキスト
     * @param selectedUnnecessary true:チェック済み false:チェックなし
     */
    public static void setSharedPreferencesStbSelect(Context context, boolean selectedUnnecessary) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME, selectedUnnecessary);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * STB接続完了状態を保存
     *
     * @param context           コンテキスト
     * @param stbConnectSuccess true:接続完了済み false:接続未完了
     */
    public static void setSharedPreferencesStbConnect(Context context, boolean stbConnectSuccess) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_STB_CONNECT_SUCCESS, stbConnectSuccess);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * ペアリング勧誘画面表示済み判定情報を保存
     *
     * @param context     コンテキスト
     * @param isDisplayed true:表示済み false:未表示
     */
    public static void setSharedPreferencesIsDisplayedParingInvitation(Context context, boolean isDisplayed) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_IS_DISPLAYED_PARING_INVITATION, isDisplayed);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 接続済みSTBの情報を保存
     *
     * @param context コンテキスト
     */
    public static void setSharedPreferencesStbInfo(Context context, DlnaDmsItem item) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SELECTED_STB_DATA_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_UDN, item.mUdn);
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_CONTROL_URL, item.mControlUrl);
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_HTTP, item.mHttp);
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_FRIENDLY_NAME, item.mFriendlyName);
        editor.putString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_IPADDRESS, item.mIPAddress);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * STB選択画面"次回以降表示しない" 状態を取得
     *
     * @param context コンテキスト
     * @return true:表示なし false:表示
     */
    public static boolean getSharedPreferencesStbSelect(Context context) {
        DTVTLogger.debug("getSharedPreferencesStbSelect");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME, false);

    }

    /**
     * STB接続完了状態を取得
     *
     * @param context コンテキスト
     * @return true:完了済み false:未完了
     */
    public static boolean getSharedPreferencesStbConnect(Context context) {
        DTVTLogger.debug("getSharedPreferencesStbConnect");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_STB_CONNECT_SUCCESS, false);
    }

    /**
     * ペアリング勧誘画面表示済み判定情報を取得
     *
     * @param context コンテキスト
     * @return true:表示済み false:未表示
     */
    public static boolean getSharedPreferencesIsDisplayedParingInvitation(Context context) {
        DTVTLogger.debug("getSharedPreferencesParingInvitationIsDisplayed");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_IS_DISPLAYED_PARING_INVITATION, false);
    }

    /**
     * ペアリング状態を保存(Home画面用)
     *
     * @param paringStatus
     */
    public static void setSharedPreferencesDecisionParingSettled(Context context, boolean paringStatus) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_DECISION_PARING_SETTLED, paringStatus);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * ペアリング状態を取得(Home画面用)
     */
    public static boolean getSharedPreferencesDecisionParingSettled(Context context) {
        DTVTLogger.debug("getSharedPreferencesDecisionParingSettled");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_DECISION_PARING_SETTLED, false);
    }

    /**
     * 接続済みSTB情報を取得
     */
    public static DlnaDmsItem getSharedPreferencesStbInfo(Context context) {
        DTVTLogger.debug("getSharedPreferencesStbInfo");
        DlnaDmsItem item = new DlnaDmsItem();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SELECTED_STB_DATA_INFOMATION, Context.MODE_PRIVATE);

        item.mUdn = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_UDN, SHARED_GET_STRING_DEFAULT);
        item.mControlUrl = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_CONTROL_URL, SHARED_GET_STRING_DEFAULT);
        item.mHttp = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_HTTP, SHARED_GET_STRING_DEFAULT);
        item.mFriendlyName = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_FRIENDLY_NAME, SHARED_GET_STRING_DEFAULT);
        item.mIPAddress = data.getString(SHARED_KEY_SELECTED_STB_DATA_INFOMATION_IPADDRESS, SHARED_GET_STRING_DEFAULT);

        return item;
    }

    /**
     * チュートリアル画面表示済み情報を設定
     *
     * @param context     コンテキスト
     * @param isDisplayed true:表示済み false:未表示
     */
    public static void setSharedPreferencesIsDisplayedTutorial(Context context, boolean isDisplayed) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SCREEN_INFORMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_IS_DISPLAYED_TUTORIAL, isDisplayed);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * チュートリアル画面表示済み情報を取得
     */
    public static boolean getSharedPreferencesIsDisplayedTutorial(Context context) {
        DTVTLogger.debug("getSharedPreferencesIsDisplayedTutorial");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SCREEN_INFORMATION, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_IS_DISPLAYED_TUTORIAL, false);
    }

    /**
     * SharedPreferences内の接続済みSTBのデータをクリア
     */
    public static void resetSharedPreferencesStbInfo(Context context) {
        DTVTLogger.debug("resetSharedPreferencesStbInfo");
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_SELECTED_STB_DATA_INFOMATION, Context.MODE_PRIVATE);
        data.edit().clear().apply();
    }

    /**
     * 設定画面の"外出先視聴時の画質設定"の設定値を保存
     *
     * @param context コンテキスト
     * @param quality 画質の設定値
     */
    public static void setSharedPreferencesImageQuality(Context context, String quality) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_IMAGE_QUALITY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(SHARED_KEY_IMAGE_QUALITY, quality);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 設定画面の"外出先視聴時の画質設定"の設定値を取得
     */
    public static String getSharedPreferencesImageQuality(Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_IMAGE_QUALITY, Context.MODE_PRIVATE);

        return data.getString(SHARED_KEY_IMAGE_QUALITY, "");
    }

    /**
     * 持ち出しコンテンツのダウンロード先を保存
     *
     * @param context コンテキスト
     * @param path ダウンロード先 true:内部 false:外部
     */
    public static void setSharedPreferencesStoragePath(Context context, Boolean path) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_STORAGE_PATH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_STORAGE_PATH, path);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 持ち出しコンテンツのダウンロード先を取得
     */
    public static Boolean getSharedPreferencesStoragePath(Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_STORAGE_PATH, Context.MODE_PRIVATE);

        return data.getBoolean(SHARED_KEY_STORAGE_PATH, true);
    }
}