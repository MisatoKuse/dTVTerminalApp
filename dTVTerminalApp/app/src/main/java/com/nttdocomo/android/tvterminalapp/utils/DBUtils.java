/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import org.json.JSONArray;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.FOUR_K_FLG;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UNDER_BAR_FOUR_K_FLG;

public class DBUtils {
    final static  String NUMERICAL_DECISION = "^[0-9]*$";

    /**
     * Jsonのキー名の"4kflg"によるDBエラー回避用
     *
     * @param string
     * @return
     */
    public static String fourKFlgConversion(String string) {
        String s = string;
        if (string.equals(FOUR_K_FLG)) {
            s = UNDER_BAR_FOUR_K_FLG;
        }
        return s;
    }

    /**
     * JSONArray to String[]
     *
     * @param array 読み込みJSON ARRAY
     * @return 文字配列
     */
    public static String[] toStringArray(JSONArray array) {

        String[] arr = null;
        if (array == null) {
            return null;
        }

        arr = new String[array.length()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = array.optString(i);
        }
        return arr;
    }

    /**
     * 数値なのかを判定
     * @param num　判定したい数値
     * @return 数値の場合true
     */

    public static boolean isNumber(String num) {

        Pattern p = Pattern.compile(NUMERICAL_DECISION);
        Matcher m = p.matcher(num);
        return m.find();
    }
}
