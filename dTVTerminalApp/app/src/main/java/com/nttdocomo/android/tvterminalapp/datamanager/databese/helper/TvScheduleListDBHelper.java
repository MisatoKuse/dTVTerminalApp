/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UNDER_BAR_FOUR_K_FLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE_ID;

public class TvScheduleListDBHelper extends SQLiteOpenHelper{
    /**
     * DB定義用定数(最終的にはJsonParser側から入手する
     */
    public static final String TV_SCHEDULE_LIST_TABLE_NAME = "tv_schedule_list";
    public static final String TV_SCHEDULE_LIST_ID_COLUMN = "row_id";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + TV_SCHEDULE_LIST_TABLE_NAME + " (" +
            TV_SCHEDULE_LIST_ID_COLUMN + " integer primary key autoincrement, " +
            TV_SCHEDULE_LIST_CRID + " text, " +
            TV_SCHEDULE_LIST_CID + " text, " +
            TV_SCHEDULE_LIST_TITLE_ID + " text, " +
            TV_SCHEDULE_LIST_EPISODE_ID + " text, " +
            TV_SCHEDULE_LIST_TITLE + " text, " +
            TV_SCHEDULE_LIST_EPITITLE + " text, " +
            TV_SCHEDULE_LIST_DISP_TYPE + " text, " +
            TV_SCHEDULE_LIST_DISPLAY_START_DATE + " text, " +
            TV_SCHEDULE_LIST_DISPLAY_END_DATE + " text, " +
            TV_SCHEDULE_LIST_AVAIL_START_DATE + " text, " +
            TV_SCHEDULE_LIST_AVAIL_END_DATE + " text, " +
            TV_SCHEDULE_LIST_PUBLISH_START_DATE + " text, " +
            TV_SCHEDULE_LIST_PUBLISH_END_DATE + " text, " +
            TV_SCHEDULE_LIST_NEWA_START_DATE + " text, " +
            TV_SCHEDULE_LIST_NEWA_END_DATE + " text, " +
            TV_SCHEDULE_LIST_COPYRIGHT + " text, " +
            TV_SCHEDULE_LIST_THUMB + " text, " +
            TV_SCHEDULE_LIST_DUR + " text, " +
            TV_SCHEDULE_LIST_DEMONG + " text, " +
            TV_SCHEDULE_LIST_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            TV_SCHEDULE_LIST_HDRFLG + " text, " +
            TV_SCHEDULE_LIST_AVAIL_STATUS + " text, " +
            TV_SCHEDULE_LIST_DELIVERY + " text, " +
            TV_SCHEDULE_LIST_R_VALUE + " text, " +
            TV_SCHEDULE_LIST_ADULT + " text, " +
            TV_SCHEDULE_LIST_MS + " text, " +
            TV_SCHEDULE_LIST_NG_FUNC + " text, " +
            TV_SCHEDULE_LIST_GENRE_ID_ARRAY + " text, " +
            TV_SCHEDULE_LIST_DTV + " text " +
            ")";

    /**
     * 「tv_schedule_list」テーブルの削除用SQL
     */
    private static final String DROP_TABLE_SQL = "drop table if exists " + TV_SCHEDULE_LIST_TABLE_NAME;

    /**
     * コンストラクタ
     * @param context
     */
    public TvScheduleListDBHelper(Context context) {
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
