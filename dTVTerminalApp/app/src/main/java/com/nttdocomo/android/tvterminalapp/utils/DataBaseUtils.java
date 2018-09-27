/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.utils;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelperChannel;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DB関連のUtilクラス.
 */
public class DataBaseUtils {
    /**
     * 整数の正規表現.
     */
    private final static String NUMERICAL_DECISION = "^[0-9]*$";

    /**
     * 日付用パラメータの識別用.
     */
    private static final String[] DATE_PARA = {
            JsonConstants.META_RESPONSE_DISPLAY_START_DATE,
            JsonConstants.META_RESPONSE_DISPLAY_END_DATE,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE,
            JsonConstants.META_RESPONSE_PUBLISH_START_DATE,
            JsonConstants.META_RESPONSE_PUBLISH_END_DATE,
            JsonConstants.META_RESPONSE_NEWA_START_DATE,
            JsonConstants.META_RESPONSE_NEWA_END_DATE,
            JsonConstants.META_RESPONSE_PU_START_DATE,
            JsonConstants.META_RESPONSE_PU_END_DATE,
            JsonConstants.META_RESPONSE_VOD_START_DATE,
            JsonConstants.META_RESPONSE_VOD_END_DATE,
    };

    /**
     * Jsonのキー名の"4kflg"によるDBエラー回避用.
     *
     * @param string 検査用文字列
     * @return 変換後文字列
     */
    public static String fourKFlgConversion(final String string) {
        String s = string;
        if (string.equals(DataBaseConstants.FOUR_K_FLG)) {
            s = DataBaseConstants.UNDER_BAR_FOUR_K_FLG;
        }
        return s;
    }

    /**
     * 数値なのかを判定.
     *
     * @param num 　判定したい数値
     * @return 数値の場合true
     */
    public static boolean isNumber(final String num) {
        //ヌルや空欄ならば数値ではない
        if (num == null || num.isEmpty()) {
            return false;
        }

        Pattern p = Pattern.compile(NUMERICAL_DECISION);
        Matcher m = p.matcher(num);
        return m.find();
    }

    /**
     * 数値なのかを判定(Float).
     *
     * @param num 　判定したい数値
     * @return 数値の場合true
     */
    public static boolean isFloat(final String num) {
        //ヌルや空欄ならば数値ではない
        if (num == null || num.isEmpty()) {
            return false;
        }
        String regex = "^-?[0-9]*.?[0-9]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(num);
        return m.find();
    }

    /**
     * 与えられたオブジェクトが小数を含む数値か、小数を表す文字列だった場合は、ダブル型で返す.
     *
     * @param num 返還対象のオブジェクト
     * @return 変換後の数値。変換に失敗した場合はゼロとなる
     */
    public static double getDecimal(final Object num) {
        //例外に頼らずに済むものは、事前に排除する
        //ヌルならば即座に帰る
        if (num == null) {
            return 0;
        }

        //整数型ならばダブル型に変換して帰る
        if (num instanceof Integer) {
            return ((Integer) num).doubleValue();
        }

        //長い整数型ならばダブル型に変換して帰る
        if (num instanceof Long) {
            return ((Long) num).doubleValue();
        }

        double answer;
        try {
            answer = (double) num;
        } catch (ClassCastException classCastException) {
            //ダブルへのキャストに失敗するので、数値の類ではない
            try {
                answer = Double.parseDouble((String) num);
            } catch (NumberFormatException numberFormatException) {
                //ダブルへのパースに失敗するので、小数ではない
                answer = 0;
            }
        }

        return answer;
    }

    /**
     * 数値取得.
     *
     * @param data 　数値判定オブジェクト
     * @return 数値
     */
    public static int getNumeric(final Object data) {
        int i = 0;
        if (data instanceof Integer) {
            i = ((Integer) data);
        } else if (data instanceof String) {
            i = Integer.parseInt(String.valueOf(data));
        }
        return i;
    }

    /**
     * String取得.
     * @param data　String判定オブジェクト
     * @return String
     */
    public static String getString(final Object data) {
        String s = "";
        if (data instanceof String) {
            s = ((String) data);
        } else if (data instanceof Integer) {
            s = String.valueOf(data);
        }
        return s;
    }

    /**
     * long型数値取得.
     *
     * @param data 　数値判定オブジェクト
     * @return long型数値
     */
    public static long getLong(final Object data) {
        long i = 0;
        if (data instanceof Integer) {
            i = ((Integer) data).longValue();
        } else if (data instanceof Long) {
            i = (Long) data;
        }
        return i;
    }

    /**
     * Jsonの項目名が日付関連か同課を見る.
     *
     * @param parameterName 調べたい項目名
     * @return 日付関連の項目名ならばtrue
     */
    public static boolean isDateItem(final String parameterName) {
        boolean answer = false;

        //日付関連項目が含まれるかどうかを見る
        if (Arrays.asList(DATE_PARA).contains(parameterName)) {
            //日付関連項目なので、true
            answer = true;
        }

        return answer;
    }

    /**
     * 引数指定されたテーブルにレコードが存在するかを返す.
     *
     * @param context   コンテキストファイル
     * @param tableName テーブル名
     * @return データ存在チェック結果
     */
    public static synchronized boolean isCachingRecord(final Context context, final String tableName) {
        long recordCount = 0;
        DataBaseHelper dBHelper = new DataBaseHelper(context);
        try (SQLiteDatabase database = dBHelper.getWritableDatabase()) {
            recordCount = DatabaseUtils.queryNumEntries(database, tableName);
        } catch (SQLiteException e) {
            DTVTLogger.debug(e);
        }
        return recordCount > 0;
    }

    /**
     * 引数指定されたテーブルにレコードが存在するかを返す.
     *
     * @param database  データベース
     * @param tableName テーブル名
     * @return データ存在チェック結果
     */
    public static boolean isCachingRecord(final SQLiteDatabase database, final String tableName) {
        long recordCount = DatabaseUtils.queryNumEntries(database, tableName);
        return recordCount > 0;
    }

    /**
     * 引数指定されたテーブルにレコードが存在するかを返す(番組詳細情報用).
     *
     * @param context コンテキスト
     * @param tableName テーブル名
     * @param chNo チャンネル番号
     * @return データ存在チェック結果
     */
    public static synchronized boolean isChCachingRecord(final Context context, final String tableName,
                                                         final String chNo) {
        DataBaseHelperChannel dataBaseHelperChannel = new DataBaseHelperChannel(context, chNo);
        SQLiteDatabase database = dataBaseHelperChannel.getWritableDatabase();
        long recordCount = DatabaseUtils.queryNumEntries(database, tableName);
        database.close();
        return recordCount > 0;
    }

    /**
     * 引数指定されたテーブルにレコードが存在するかを返す.
     *
     * @param context コンテキスト
     * @param tableName テーブル名
     * @param selection WHERE句の内容
     * @param args      selectionに"?" が入っている場合のパラメータ
     * @return データ存在チェック結果
     */
    public static synchronized boolean isCachingRecord(
            final Context context, final String tableName, final String selection, final String[] args) {
        DataBaseHelper dBHelper = new DataBaseHelper(context);
        SQLiteDatabase database = dBHelper.getWritableDatabase();
        long recordCount = DatabaseUtils.queryNumEntries(database, tableName, selection, args);
        database.close();
        return recordCount > 0;
    }

    /**
     * 操作するレコメンドテーブル名を取得.
     *
     * @param tagPageNo タグ名
     * @return tableName
     */
    public static String getRecommendTableName(final int tagPageNo) {
        String tableName = null;
        switch (tagPageNo) {
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_TV:
                tableName = DataBaseConstants.RECOMMEND_HOME_CHANNEL_LIST_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_VIDEO:
                tableName = DataBaseConstants.RECOMMEND_HOME_VIDEO_LIST_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV:
                tableName = DataBaseConstants.RECOMMEND_CHANNEL_LIST_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO:
                tableName = DataBaseConstants.RECOMMEND_VIDEO_LIST_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV:
                tableName = DataBaseConstants.RECOMMEND_LIST_DTV_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL:
                tableName = DataBaseConstants.RECOMMEND_LIST_DCHANNEL_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME:
                tableName = DataBaseConstants.RECOMMEND_LIST_DANIME_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }

    /**
     * テーブル名取得(type指定).
     *
     * @param type テーブルの種類(TV or VOD)
     * @return SQLiteDatabaseクラスの戻り値(削除されたレコード数)
     */
    public static String getClipKeyTableName(final ClipKeyListDao.TableTypeEnum type) {
        String tableName = null;
        switch (type) {
            case TV:
                tableName = DataBaseConstants.TV_CLIP_KEY_LIST_TABLE_NAME;
                break;
            case VOD:
                tableName = DataBaseConstants.VOD_CLIP_KEY_LIST_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
