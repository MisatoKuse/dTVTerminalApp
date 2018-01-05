/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import org.json.JSONArray;


/**
 * 文字列加工に関する処理を記載する
 */
public class StringUtil {

    private Context mContext;

    public StringUtil(Context context) {
        mContext = context;
    }

    /**
     * サービスID(dTV関連)に応じたサービス名を返す
     *
     * @param id サービスID
     * @return サービス名
     */
    public String getContentsServiceName(int id) {
        switch (id) {
            case DtvContentsDetailActivity.DTV_CONTENTS_SERVICE_ID:
                return mContext.getString(R.string.dtv_contents_service_name);
            case DtvContentsDetailActivity.DTV_CHANNEL_CONTENTS_SERVICE_ID:
                return mContext.getString(R.string.dtv_channel_contents_service_name);
            case DtvContentsDetailActivity.D_ANIMATION_CONTENTS_SERVICE_ID:
                return mContext.getString(R.string.d_animation_contents_service_name);
        }
        return mContext.getString(R.string.hikari_tv_contents_service_name);
    }

    /**
     * 文字列を連結する
     *
     * @param strings 連結したい文字列配列
     * @return 連結後の文字列
     */
    public static String getConnectString(String[] strings) {
        StringBuilder builder = new StringBuilder();
        String conString;
        for (String string : strings) {
            builder.append(string);
        }
        conString = builder.toString();
        return conString;
    }

    /**
     * 文字列を連結する・可変長引数版（言語仕様の都合で、オーバーロードの同じ名前にはできない）
     *
     * @param strings 連結したい文字列を必要な数だけ指定する。
     * @return 連結後の文字列
     */
    public static String getConnectStrings(String... strings) {
        //同じ名前にはできないが、処理は委譲する。
        return getConnectString(strings);
    }

    /**
     * JSonArrayを文字列配列に変換する
     *
     * @param jsonArray JsonArray
     * @return 文字列配列
     */
    public static String[] JSonArray2StringArray(JSONArray jsonArray) {
        //出力文字列
        String[] stringArr;

        //JsonArrayがヌルの場合やJSONArrayではない場合は、長さゼロの配列を返す
        if (jsonArray == null) {
            return new String[0];
        }

        //jsonArrayの要素数で配列を宣言
        stringArr = new String[jsonArray.length()];

        //jsonArrayの要素数だけ回る
        for (int i = 0; i < stringArr.length; i++) {
            if (jsonArray.optString(i) == null) {
                //無いはずだが、ヌルならば空文字にする
                stringArr[i] = "";
            } else {
                //JsonArrayの中身を移す
                stringArr[i] = jsonArray.optString(i);
            }
        }

        return stringArr;
    }

    /**
     * 与えられたオブジェクトをチェックし、長整数に変換する
     *
     * @param data オブジェクト
     * @return 長整数変換後の値。変換できなければゼロ
     */
    public static long changeString2Long(Object data) {
        //既に数値かどうかを判定
        if (data instanceof Long) {
            //長整数なのでそのまま返す
            return (long) data;
        }

        if(data instanceof Integer) {
            //整数なので長整数に変換
            return ((Integer)data).longValue();
        }

        //数字の文字列かどうかの判定
        if (data != null && data instanceof String && DBUtils.isNumber((String) data)) {
            //数字文字列だったので、変換して返す
            return Long.parseLong((String) data);
        }

        //変換できなかったのでゼロ
        return 0;
    }

    /**
     * 与えられたオブジェクトをチェックし、整数に変換する
     *
     * @param data オブジェクト
     * @return 整数変換後の値。変換できなければゼロ
     */
    public static int changeString2Int(Object data) {
        //既に数値かどうかを判定
        if(data instanceof Integer) {
            //整数なのでそのまま返す
            return (Integer)data;
        }

        if (data instanceof Long) {
            //長整数なので整数に変換
            return ((Long)data).intValue();
        }

        //数字の文字列かどうかの判定
        if (data != null && data instanceof String && DBUtils.isNumber((String) data)) {
            //数字文字列だったので、変換して返す
            return Integer.parseInt((String) data);
        }

        //変換できなかったのでゼロ
        return 0;
    }

    /**
     * 与えられたオブジェクトをチェックし、文字列に変換する
     *
     * @param data オブジェクト
     * @return 変換後の文字列。変換できなければ空文字
     */
    public static String changeObject2String(Object data) {
        //Stringかどうかを見る
        if (data instanceof String) {
            //そのまま返す
            return (String)data;
        }

        //intかどうかを判定
        if(data instanceof Integer) {
            //intを文字に変換して返す
            return String.valueOf((int)data);
        }

        //longかどうかを判定
        if(data instanceof Long) {
            //longを文字に変換して返す
            return String.valueOf((long)data);
        }

        //doubleかどうかを判定
        if(data instanceof Double) {
            //doubleを文字に変換して返す
            return String.valueOf((double)data);
        }

        //変換できなかったので空文字
        return "";
    }

    /**
     * ひかりコンテンツ判定(ひかり内DTVのみ)
     *
     * @param type
     * @return
     */
    public static boolean isHikariInDtvContents(String type) {
        boolean isHikariIn = false;
        if (type != null && (type.equals(WebApiBasePlala.CLIP_TYPE_DTV_VOD))) {
            isHikariIn = true;
        }
        return isHikariIn;
    }

    /**
     * ひかりコンテンツ判定
     *
     * @param type
     * @return
     */
    public static boolean isHikariContents(String type) {
        boolean isHikari = false;
        if (type != null && (type.equals(WebApiBasePlala.CLIP_TYPE_H4D_IPTV)
                || type.equals(WebApiBasePlala.CLIP_TYPE_H4D_VOD)
                || type.equals(WebApiBasePlala.CLIP_TYPE_DCH))) {
            isHikari = true;
        }
        return isHikari;
    }
}
