package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UNDER_BAR_FOUR_K_FLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient_temp.jsonparser_temp.DailyRankJsonParser.DAILYRANK_LIST_TITLE_ID;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class DailyRankListDBHelper extends SQLiteOpenHelper {


    /**
     * DB定義用定数(最終的にはJsonParser側から入手する
     */
    public static final String DAILYRANK_LIST_TABLE_NAME = "daily_rank_list";
    public static final String DAILYRANK_LIST_ID_COLUMN = "row_id";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + DAILYRANK_LIST_TABLE_NAME + " (" +
            DAILYRANK_LIST_ID_COLUMN + " integer primary key autoincrement, " +
            DAILYRANK_LIST_CRID + " text, " +
            DAILYRANK_LIST_CID + " text, " +
            DAILYRANK_LIST_TITLE_ID + " text, " +
            DAILYRANK_LIST_EPISODE_ID + " text, " +
            DAILYRANK_LIST_TITLE + " text, " +
            DAILYRANK_LIST_EPITITLE + " text, " +
            DAILYRANK_LIST_DISP_TYPE + " text, " +
            DAILYRANK_LIST_DISPLAY_START_DATE + " text, " +
            DAILYRANK_LIST_DISPLAY_END_DATE + " text, " +
            DAILYRANK_LIST_AVAIL_START_DATE + " text, " +
            DAILYRANK_LIST_AVAIL_END_DATE + " text, " +
            DAILYRANK_LIST_PUBLISH_START_DATE + " text, " +
            DAILYRANK_LIST_PUBLISH_END_DATE + " text, " +
            DAILYRANK_LIST_NEWA_START_DATE + " text, " +
            DAILYRANK_LIST_NEWA_END_DATE + " text, " +
            DAILYRANK_LIST_COPYRIGHT + " text, " +
            DAILYRANK_LIST_THUMB + " text, " +
            DAILYRANK_LIST_DUR + " text, " +
            DAILYRANK_LIST_DEMONG + " text, " +
            DAILYRANK_LIST_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            DAILYRANK_LIST_HDRFLG + " text, " +
            DAILYRANK_LIST_AVAIL_STATUS + " text, " +
            DAILYRANK_LIST_DELIVERY + " text, " +
            DAILYRANK_LIST_R_VALUE + " text, " +
            DAILYRANK_LIST_ADULT + " text, " +
            DAILYRANK_LIST_MS + " text, " +
            DAILYRANK_LIST_NG_FUNC + " text, " +
            DAILYRANK_LIST_GENRE_ID_ARRAY + " text, " +
            DAILYRANK_LIST_DTV + " text" +
            ")";

    /**
     * 「daily_rank_list」テーブルの削除用SQL
     */
    private static final String DROP_TABLE_SQL = "drop table if exists " + DAILYRANK_LIST_TABLE_NAME;

    /**
     * コンストラクタ
     * @param context
     */
    public DailyRankListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_SQL);
    }
}
