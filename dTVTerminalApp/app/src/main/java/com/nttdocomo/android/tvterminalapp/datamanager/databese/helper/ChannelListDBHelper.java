package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_4KFLG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_TITLE_ID;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class ChannelListDBHelper extends SQLiteOpenHelper {

    /**
     * DB定義用定数(最終的にはJsonParser側から入手する
     */
    public static final String CHANNEL_LIST_TABLE_NAME = "channel_list";
    public static final String CHANNEL_LIST_ID_COLUMN = "row_id";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + CHANNEL_LIST_TABLE_NAME + " (" +
            CHANNEL_LIST_ID_COLUMN + " integer primary key autoincrement, " +
            CHANNEL_LIST_CRID + " text, " +
            CHANNEL_LIST_CID + " text, " +
            CHANNEL_LIST_TITLE_ID + " text, " +
            CHANNEL_LIST_EPISODE_ID + " text, " +
            CHANNEL_LIST_TITLE + " text, " +
            CHANNEL_LIST_EPITITLE + " text, " +
            CHANNEL_LIST_DISP_TYPE + " text, " +
            CHANNEL_LIST_DISPLAY_START_DATE + " text, " +
            CHANNEL_LIST_DISPLAY_END_DATE + " text, " +
            CHANNEL_LIST_AVAIL_START_DATE + " text, " +
            CHANNEL_LIST_AVAIL_END_DATE + " text, " +
            CHANNEL_LIST_PUBLISH_START_DATE + " text, " +
            CHANNEL_LIST_PUBLISH_END_DATE + " text, " +
            CHANNEL_LIST_NEWA_START_DATE + " text, " +
            CHANNEL_LIST_NEWA_END_DATE + " text, " +
            CHANNEL_LIST_COPYRIGHT + " text, " +
            CHANNEL_LIST_THUMB + " text, " +
            CHANNEL_LIST_DUR + " text, " +
            CHANNEL_LIST_DEMONG + " text, " +
            CHANNEL_LIST_BVFLG + " text, " +
            CHANNEL_LIST_4KFLG + " text, " +
            CHANNEL_LIST_HDRFLG + " text, " +
            CHANNEL_LIST_AVAIL_STATUS + " text, " +
            CHANNEL_LIST_DELIVERY + " text, " +
            CHANNEL_LIST_R_VALUE + " text, " +
            CHANNEL_LIST_ADULT + " text, " +
            CHANNEL_LIST_MS + " text, " +
            CHANNEL_LIST_NG_FUNC + " text, " +
            CHANNEL_LIST_GENRE_ID_ARRAY + " text, " +
            CHANNEL_LIST_DTV + " text " +
            ")";

    /**
     * 「channel_list」テーブルの削除用SQL
     */
    private static final String DROP_TABLE_SQL = "drop table if exists " + CHANNEL_LIST_TABLE_NAME;

    /**
     * コンストラクタ
     * @param context
     */
    public ChannelListDBHelper(Context context) {
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
