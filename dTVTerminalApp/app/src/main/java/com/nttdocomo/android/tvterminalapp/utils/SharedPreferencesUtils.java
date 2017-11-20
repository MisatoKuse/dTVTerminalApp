/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SharedPreferencesUtils {

    // SharedPreferences ペアリング情報保存キー 親キー
    private static final String SHARED_KEY_PAIRING_INFOMATION = "pairing_info";
    // STB選択画面"次回以降表示しない" 選択保存キー
    private static final String SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME = "unnecessary_next_time";
    // STB接続画面 接続成功 保存キー
    private static final String SHARED_KEY_STB_CONNECT_SUCCESS = "connect_success";
    // ペアリング STB情報 保存キー
    private static final String SHARED_KEY_STB_DATA_INFOMATION = "stb_data_info";
    // ペアリング勧誘画面表示済み判定情報 保存キー
    private static final String SHARED_KEY_PARING_INVITATION_IS_DISPLAYED = "paring_invitation_is_displayed";
    // ホーム画面ペアリング済み判定 保存キー
    private static final String SHARED_KEY_DECISION_PARING_SETTLED = "decision_paring_settled";
    public static final String STATE_TO_HOME_PAIRING_OK="ホーム画面（ペアリング済）";
    public static final String STATE_TO_HOME_PAIRING_NG="ホーム画面（未ペアリング）";

    /**
     * STB選択画面"次回以降表示しない" 状態を保存
     * @param context コンテキスト
     * @param selectedUnnecessary true:チェック済み false:チェックなし
     */
    public static void setSharedPreferencesStbSelect(Context context, boolean selectedUnnecessary) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME,selectedUnnecessary);
        editor.apply();
    }

    /**
     * STB接続完了状態を保存
     * @param context コンテキスト
     * @param stbConnectSuccess true:接続完了済み false:接続未完了
     */
    public static void setSharedPreferencesStbConnect(Context context, boolean stbConnectSuccess) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_STB_CONNECT_SUCCESS, stbConnectSuccess);
        editor.apply();
    }

    /**
     * ペアリング勧誘画面表示済み判定情報を保存
     * @param context コンテキスト
     * @param isDisplayed true:表示済み false:未表示
     */
    public static void setSharedPreferencesParingInvitationIsDisplayed(Context context, boolean isDisplayed) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putBoolean(SHARED_KEY_PARING_INVITATION_IS_DISPLAYED, isDisplayed);
        editor.apply();
    }

    /**
     * STBの情報を保存
     * @param context コンテキスト
     */
    public static void setSharedPreferencesStbInfo(Context context) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        // TODO STB情報クラスの値を参照
        // TODO 情報をJSONに変換して保存予定
//        Gson gson = new Gson();
//        editor.put(SHARED_KEY_STB_DATA_INFOMATION, )
        editor.apply();
    }

    /**
     * STB選択画面"次回以降表示しない" 状態を取得
     * @param context コンテキスト
     * @return  true:表示なし false:表示
     */
    public static boolean getSharedPreferencesStbSelect(Context context) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        return data.getBoolean(SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME,false);

    }

    /**
     * STB接続完了状態を取得
     * @param context コンテキスト
     * @return true:完了済み false:未完了
     */
    public static boolean getSharedPreferencesStbConnect(Context context) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        return data.getBoolean(SHARED_KEY_STB_CONNECT_SUCCESS,false);
    }

    /**
     * ペアリング勧誘画面表示済み判定情報を取得
     * @param context コンテキスト
     * @return true:表示済み false:未表示
     */
    public static boolean getSharedPreferencesParingInvitationIsDisplayed(Context context) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        return data.getBoolean(SHARED_KEY_PARING_INVITATION_IS_DISPLAYED,false);
    }

    /**
     *  ペアリング状態を保存(Home画面用)
     * @param paringStatus
     */
    public static void setSharedPreferencesDecisionParingSettled(Context context, String paringStatus) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putString(SHARED_KEY_DECISION_PARING_SETTLED, paringStatus);
        editor.apply();
    }

    /**
     *  ペアリング状態を取得(Home画面用)
     */
    public static String getSharedPreferencesDecisionParingSettled(Context context) {
        SharedPreferences data = context.getSharedPreferences(
                SHARED_KEY_PAIRING_INFOMATION, Context.MODE_PRIVATE);
        return data.getString(SHARED_KEY_DECISION_PARING_SETTLED, STATE_TO_HOME_PAIRING_NG);
    }
}