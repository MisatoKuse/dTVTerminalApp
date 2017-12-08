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
    private final static String NUMERICAL_DECISION = "^[0-9]*$";

    /**
     * Jsonのキー名の"4kflg"によるDBエラー回避用
     *
     * @param string 検査用文字列
     * @return 変換後文字列
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
     *
     * @param num 　判定したい数値
     * @return 数値の場合true
     */
    public static boolean isNumber(String num) {
        //ヌルや空欄ならば数値ではない
        if(num == null || num.isEmpty()) {
            return false;
        }

        Pattern p = Pattern.compile(NUMERICAL_DECISION);
        Matcher m = p.matcher(num);
        return m.find();
    }

    /**
     * 与えられたオブジェクトが小数を含む数値か、小数を表す文字列だった場合は、ダブル型で返す
     * @param num 返還対象のオブジェクト
     * @return 変換後の数値。変換に失敗した場合はゼロとなる
     */
    public static double getDecimal(Object num) {
        //例外に頼らずに済むものは、事前に排除する
        //ヌルならば即座に帰る
        if(num == null) {
            return 0;
        }

        //整数型ならばダブル型に変換して帰る
        if(num instanceof Integer) {
            return ((Integer) num).doubleValue();
        }

        //長い整数型ならばダブル型に変換して帰る
        if(num instanceof Long) {
            return ((Long) num).doubleValue();
        }

        double answer;
        try {
            answer = (double)num;
        } catch(ClassCastException classCastException) {
            //ダブルへのキャストに失敗するので、数値の類ではない
            try {
                answer = Double.parseDouble((String)num);
            } catch (NumberFormatException numberFormatException) {
                //ダブルへのパースに失敗するので、小数ではない
                answer=0;
            }
        }

        return answer;
    }

    /**
     * 数値取得
     *
     * @param data 　数値判定オブジェクト
     * @return 数値
     */
    public static int getNumeric(Object data) {
        int i = 0;
        if (data instanceof Integer) {
            i = ((Integer) data);
        } else if (data instanceof String) {
            i = Integer.parseInt(String.valueOf(data));
        }
        return i;
    }
    /**
     * long型数値取得
     *
     * @param data 　数値判定オブジェクト
     * @return long型数値
     */
    public static long getLong(Object data) {
        long i = 0;
        if (data instanceof Integer) {
            i = ((Integer) data).longValue();
        } else if (data instanceof Long) {
            i =  (Long) data;
        }
        return i;
    }
}
