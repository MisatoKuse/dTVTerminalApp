/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;

public class SharedPreferencesUtils {

    /**  SharedPreferences ペアリング情報保存キー 親キー */
    private static final String SHARED_KEY_PAIRING_INFOMATION = "pairing_info";
    /**  STB選択画面"次回以降表示しない" 選択保存キー */
    private static final String SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME = "unnecessary_next_time";
    /**  STB接続画面 接続成功 保存キー */
    private static final String SHARED_KEY_STB_CONNECT_SUCCESS = "connect_success";
    /**  ペアリング勧誘画面表示済み判定情報 保存キー */
    private static final String SHARED_KEY_IS_DISPLAYED_PARING_INVITATION = "is_displayed_paring_invitation";
    /**  ホーム画面ペアリング済み判定 保存キー */
    private static final String SHARED_KEY_DECISION_PARING_SETTLED = "decision_paring_settled";
    // 接続済み STB情報 保存キー
    /**  STB情報 親キー */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION = "stb_data_info";
    /**  UDN */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_UDN = "selected_stb_udn";
    /**  コントロールURL */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_CONTROL_URL = "selected_stb_control_url";
    /**  HTTP */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_HTTP = "selected_stb_http";
    /**  Friendly名 */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_FRIENDLY_NAME = "selected_stb_friendly_name";
    /**  IPv6アドレス */
    private static final String SHARED_KEY_SELECTED_STB_DATA_INFOMATION_IPADDRESS = "selected_stb_ipaddress";
    /**  getString 初期値 */
    private static final String SHARED_GET_STRING_DEFAULT = "";
    /**  画面情報保存 親キー */
    private static final String SHARED_KEY_SCREEN_INFORMATION = "screen_information";
    /**  LaunchActivity チュートリアル表示済み判定 保存キー */
    private static final String SHARED_KEY_IS_DISPLAYED_TUTORIAL = "is_displayed_tutorial";
    /**  SettingActivity 画質設定の設定値 */
    private static final String SHARED_KEY_IMAGE_QUALITY = "image_quality";
    /**  持ち出しコンテンツダウンロード先 ture:内部 false:外部 */
    private static final String SHARED_KEY_STORAGE_PATH = "storage_path";
    /** 最後に取得したdアカウントのID */
    private static final String LAST_D_ACCOUNT_ID = "BEFORE_D_ACCOUNT_ID";
    /** 最後に取得したワンタイムパスワード */
    private static final String LAST_ONE_TIME_PASSWORD = "LAST_ONE_TIME_PASSWORD";
    /** アプリ再起動フラグ */
    private static final String RESTART_FLAG = "RESTART_FLAG";
    /** ユーザ年齢情報キー */
    private static final String USER_AGE_REQ_SHARED_KEY = "USER_AGE_REQ_SHARED_KEY";
    /** ユーザー情報取得日時 */
    private static final String LAST_USER_INFO_DATE = "LAST_USER_INFO_DATE";

    private static final String CONTRACT_INFO_SHARED_KEY = "CONTRACT_INFO_SHARED_KEY";

    /**
     * 独自の削除メソッドがある接続済みSTB情報以外の、dアカウントユーザー切り替え時の削除対象
     * 新しい物を追加した場合は、基本的にこの配列に名前を追加してください。
     * (チュートリアル表示済み判定以外は全て消すことになった。アプリ再起動フラグは自動で消えるので対象外)
     */
    private final static String[] DELETE_PREFERENCES_NAME = {
            // SharedPreferences ペアリング情報保存キー 親キー
            SHARED_KEY_PAIRING_INFOMATION,
            // STB選択画面"次回以降表示しない" 選択保存キー
            SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME,
            // STB接続画面 接続成功 保存キー
            SHARED_KEY_STB_CONNECT_SUCCESS,
            // ペアリング勧誘画面表示済み判定情報 保存キー
            SHARED_KEY_IS_DISPLAYED_PARING_INVITATION,
            // ホーム画面ペアリング済み判定 保存キー
            SHARED_KEY_DECISION_PARING_SETTLED,
            // SettingActivity 画質設定の設定値
            SHARED_KEY_IMAGE_QUALITY,
            // 持ち出しコンテンツダウンロード先
            SHARED_KEY_STORAGE_PATH,
            //最後に取得したdアカウントのID
            LAST_D_ACCOUNT_ID,
            //最後に取得したワンタイムパスワード
            LAST_ONE_TIME_PASSWORD,
            //ユーザー情報取得日時
            LAST_USER_INFO_DATE,
    };


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
     * @param paringStatus ペアリング状態
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

    /**
     * 取得したdアカウントを保存
     *
     * @param context コンテキスト
     * @param id 保存するdアカウントID
     */
    public static void setSharedPreferencesDaccountId(Context context, String id) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_D_ACCOUNT_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(LAST_D_ACCOUNT_ID, id);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 最後に保存したdアカウントを取得
     */
    public static String getSharedPreferencesDaccountId(Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_D_ACCOUNT_ID, Context.MODE_PRIVATE);

        return data.getString(LAST_D_ACCOUNT_ID,"");
    }

    /**
     * 取得したワンタイムパスワードを保存
     *
     * @param context コンテキスト
     * @param pass 保存するワンタイムパスワード
     */
    public static void setSharedPreferencesOneTimePass(Context context, String pass) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_ONE_TIME_PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(LAST_ONE_TIME_PASSWORD, pass);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 最後に保存したワンタイムパスワードを取得
     */
    public static String getSharedPreferencesOneTimePass(Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_ONE_TIME_PASSWORD, Context.MODE_PRIVATE);

        return data.getString(LAST_ONE_TIME_PASSWORD,"");
    }

    /**
     * ユーザー情報の最終取得日時を保存
     *
     * @param context コンテキスト
     * @param getTime 取得日時
     */
    public static void setSharedPreferencesUserInfoDate(Context context, long getTime) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_USER_INFO_DATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putLong(LAST_USER_INFO_DATE, getTime);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * ユーザー情報の最終取得日時を取得
     *
     * @param context コンテキスト
     * @return 最終取得日時
     */
    public static long getSharedPreferencesUserInfoDate(Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                LAST_USER_INFO_DATE, Context.MODE_PRIVATE);
        long dateTime = data.getLong(LAST_USER_INFO_DATE,-1L);

        //日時が負数ならばまだユーザー情報は読み込まれていないので、最大値を入れてかならず読むようにする
        if(dateTime < 0) {
            dateTime = Long.MAX_VALUE;
        }

        return dateTime;
    }

    /**
     * 一部の設定値以外のプリファレンスを削除する
     */
    public static void clearAlmostSharedPreferences(Context context) {
        //接続済みSTB情報には削除処理があるので使用する
        resetSharedPreferencesStbInfo(context);

        //他の情報の削除を行う
        for(String deleteKey : DELETE_PREFERENCES_NAME) {
            SharedPreferences deleteData = context.getSharedPreferences(deleteKey,Context.MODE_PRIVATE);
            deleteData.edit().clear().apply();
        }
    }

    /**
     * 再起動フラグを設定
     *
     * @param context コンテキスト
     * @param restartFlag 再起動フラグの設定値
     */
    public static void setSharedPreferencesRestartFlag(Context context, boolean restartFlag) {
        DTVTLogger.start("set restart flag = " + restartFlag);
        SharedPreferences data = context.getSharedPreferences(
                RESTART_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(RESTART_FLAG, restartFlag);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 再起動フラグを取得
     */
    public static boolean getSharedPreferencesRestartFlag(Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                RESTART_FLAG, Context.MODE_PRIVATE);

        //デフォルト値はfalseで取得して返す
        return data.getBoolean(RESTART_FLAG,false);
    }

    /**
     * 取得したユーザ視聴制限情報を保存
     *
     * @param context コンテキスト
     * @param age 保存するユーザ年齢制限情報
     */
    public static void setSharedPreferencesAgeReq(Context context, int age) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                USER_AGE_REQ_SHARED_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt(USER_AGE_REQ_SHARED_KEY, age);
        editor.apply();
        DTVTLogger.end();
    }

    /**
     * 最後に保存した年齢情報を取得
     */
    public static int getSharedPreferencesAgeReq(Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                USER_AGE_REQ_SHARED_KEY, Context.MODE_PRIVATE);

        //保存した年齢情報がない場合はPG12を返却
        return data.getInt(USER_AGE_REQ_SHARED_KEY, StringUtil.USER_AGE_REQ_PG12);
    }

    /**
     * 取得したユーザーの契約情報を保存
     *
     * @param context コンテキスト
     * @param info 契約情報
     */
    public static void setSharedPreferencesContractInfo(Context context, String info) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                CONTRACT_INFO_SHARED_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(CONTRACT_INFO_SHARED_KEY, info);
        editor.apply();
        DTVTLogger.end();
    }

    public static String getSharedPreferencesContractInfo(Context context) {
        DTVTLogger.start();
        SharedPreferences data = context.getSharedPreferences(
                CONTRACT_INFO_SHARED_KEY, Context.MODE_PRIVATE);

        //保存した契約情報がない場合はnoneを返却する
        return data.getString(CONTRACT_INFO_SHARED_KEY, "none");
    }
}