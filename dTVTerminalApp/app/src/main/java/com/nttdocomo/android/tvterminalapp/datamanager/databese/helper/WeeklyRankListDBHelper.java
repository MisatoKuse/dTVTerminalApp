package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_4KFLG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.WeeklyRankJsonParser.WEEKLYRANK_LIST_TITLE_ID;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class WeeklyRankListDBHelper extends SQLiteOpenHelper{
    /**
     * DB定義用定数(最終的にはJsonParser側から入手する
     */
    public static final String WEEKLYRANK_LIST_TABLE_NAME = "weekly_rank_list";
    public static final String WEEKLYRANK_LIST_ID_COLUMN = "row_id";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + WEEKLYRANK_LIST_TABLE_NAME + " (" +
            WEEKLYRANK_LIST_ID_COLUMN + " integer primary key, " +
            WEEKLYRANK_LIST_CRID + " text not null " +
            WEEKLYRANK_LIST_CID + " text not null " +
            WEEKLYRANK_LIST_TITLE_ID + " text not null " +
            WEEKLYRANK_LIST_EPISODE_ID + " text not null " +
            WEEKLYRANK_LIST_TITLE + " text not null " +
            WEEKLYRANK_LIST_EPITITLE + " text not null " +
            WEEKLYRANK_LIST_DISP_TYPE + " text not null " +
            WEEKLYRANK_LIST_DISPLAY_START_DATE + " text not null " +
            WEEKLYRANK_LIST_DISPLAY_END_DATE + " text not null " +
            WEEKLYRANK_LIST_AVAIL_START_DATE + " text not null " +
            WEEKLYRANK_LIST_AVAIL_END_DATE + " text not null " +
            WEEKLYRANK_LIST_PUBLISH_START_DATE + " text not null " +
            WEEKLYRANK_LIST_PUBLISH_END_DATE + " text not null " +
            WEEKLYRANK_LIST_NEWA_START_DATE + " text not null " +
            WEEKLYRANK_LIST_NEWA_END_DATE + " text not null " +
            WEEKLYRANK_LIST_COPYRIGHT + " text not null " +
            WEEKLYRANK_LIST_THUMB + " text not null " +
            WEEKLYRANK_LIST_DUR + " text not null " +
            WEEKLYRANK_LIST_DEMONG + " text not null " +
            WEEKLYRANK_LIST_BVFLG + " text not null " +
            WEEKLYRANK_LIST_4KFLG + " text not null " +
            WEEKLYRANK_LIST_HDRFLG + " text not null " +
            WEEKLYRANK_LIST_AVAIL_STATUS + " text not null " +
            WEEKLYRANK_LIST_DELIVERY + " text not null " +
            WEEKLYRANK_LIST_R_VALUE + " text not null " +
            WEEKLYRANK_LIST_ADULT + " text not null " +
            WEEKLYRANK_LIST_MS + " text not null " +
            WEEKLYRANK_LIST_NG_FUNC + " text not null " +
            WEEKLYRANK_LIST_GENRE_ID_ARRAY + " text not null " +
            WEEKLYRANK_LIST_DTV + " text not null " +
            ")";

    /**
     * 「weekly_rank_list」テーブルの削除用SQL
     */
    private static final String DROP_TABLE_SQL = "drop table if exists " + WEEKLYRANK_LIST_TABLE_NAME;

    /**
     * コンストラクタ
     * @param context
     */
    public WeeklyRankListDBHelper(Context context) {
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
