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

    private static final String SHARED_KEY_PAIRING_INFOMATION = "pairing_info";
    // STB選択画面"次回以降表示しない" 選択保存キー
    private static final String SHARED_KEY_STB_SELECT_UNNECESSARY_NEXT_TIME = "unnecessary_next_time";
    // STB接続画面 接続成功 保存キー
    private static final String SHARED_KEY_STB_CONNECT_SUCCESS = "connect_success";
    // ペアリング STB情報 保存キー
    private static final String SHARED_KEY_STB_DATA_INFOMATION = "stb_data_info";

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
        editor.commit();
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
        editor.commit();

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
        editor.commit();
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
}