package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_4KFLG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_TITLE_ID;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class VodClipListDBHelper extends SQLiteOpenHelper {

    /**
     * DB定義用定数(最終的にはJsonParser側から入手する
     */
    public static final String VODCLIP_LIST_TABLE_NAME = "vod_clip_list";
    public static final String VODCLIP_LIST_ID_COLUMN = "row_id";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + VODCLIP_LIST_TABLE_NAME + " (" +
            VODCLIP_LIST_ID_COLUMN + " integer primary key autoincrement, " +
            VODCLIP_LIST_CRID + " text, " +
            VODCLIP_LIST_CID + " text, " +
            VODCLIP_LIST_TITLE_ID + " text, " +
            VODCLIP_LIST_EPISODE_ID + " text, " +
            VODCLIP_LIST_TITLE + " text, " +
            VODCLIP_LIST_EPITITLE + " text, " +
            VODCLIP_LIST_DISP_TYPE + " text, " +
            VODCLIP_LIST_DISPLAY_START_DATE + " text, " +
            VODCLIP_LIST_DISPLAY_END_DATE + " text, " +
            VODCLIP_LIST_AVAIL_START_DATE + " text, " +
            VODCLIP_LIST_AVAIL_END_DATE + " text, " +
            VODCLIP_LIST_PUBLISH_START_DATE + " text, " +
            VODCLIP_LIST_PUBLISH_END_DATE + " text, " +
            VODCLIP_LIST_NEWA_START_DATE + " text, " +
            VODCLIP_LIST_NEWA_END_DATE + " text, " +
            VODCLIP_LIST_COPYRIGHT + " text, " +
            VODCLIP_LIST_THUMB + " text, " +
            VODCLIP_LIST_DUR + " text, " +
            VODCLIP_LIST_DEMONG + " text, " +
            VODCLIP_LIST_BVFLG + " text, " +
            VODCLIP_LIST_4KFLG + " text, " +
            VODCLIP_LIST_HDRFLG + " text, " +
            VODCLIP_LIST_AVAIL_STATUS + " text, " +
            VODCLIP_LIST_DELIVERY + " text, " +
            VODCLIP_LIST_R_VALUE + " text, " +
            VODCLIP_LIST_ADULT + " text, " +
            VODCLIP_LIST_MS + " text, " +
            VODCLIP_LIST_NG_FUNC + " text, " +
            VODCLIP_LIST_GENRE_ID_ARRAY + " text, " +
            VODCLIP_LIST_DTV + " text " +
            ")";

    /**
     * 「vod_clip_list」テーブルの削除用SQL
     */
    private static final String DROP_TABLE_SQL = "drop table if exists " + VODCLIP_LIST_TABLE_NAME;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public VodClipListDBHelper(Context context) {
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
