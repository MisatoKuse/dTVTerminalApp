package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class TvClipListDBHelper extends SQLiteOpenHelper {

    /**
     * DB定義用定数(最終的にはJsonParser側から入手する
     */
    public static final String TV_CLIP_LIST_TABLE_NAME = "tv_clip_list";
    public static final String TV_CLIP_LIST_ID_COLUMN = "row_id";
    //    public static final String VOD_CLIP_LIST_VALUE_NAME_COLUMN = "value_name";
    public static final String VOD_CLIP_LIST_VALUE_COLUMN = "value";
    public static final String VOD_CLIP_LIST_STATUS = "status";
    //    public static final String VOD_CLIP_LIST_COUNT = "count";
    public static final String TV_CLIP_LIST_CRID = "crid";
    public static final String TV_CLIP_LIST_CID = "cid";
    public static final String TV_CLIP_LIST_TITLE_ID = "title_id";
    public static final String TV_CLIP_LIST_EPISODE_ID = "episode_id";
    public static final String TV_CLIP_LIST_TITLE = "title";
    public static final String TV_CLIP_LIST_EPITITLE = "epititle";
    public static final String TV_CLIP_LIST_DISP_TYPE = "disp_type";
    public static final String TV_CLIP_LIST_DISPLAY_START_DATE = "display_start_date";
    public static final String TV_CLIP_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String TV_CLIP_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String TV_CLIP_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String TV_CLIP_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String TV_CLIP_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String TV_CLIP_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String TV_CLIP_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String TV_CLIP_LIST_COPYRIGHT = "copyright";
    public static final String TV_CLIP_LIST_THUMB = "thumb";
    public static final String TV_CLIP_LIST_DUR = "dur";
    public static final String TV_CLIP_LIST_DEMONG = "demong";
    public static final String TV_CLIP_LIST_BVFLG = "bvflg";
    public static final String TV_CLIP_LIST_4KFLG = "4kflg";
    public static final String TV_CLIP_LIST_HDRFLG = "hdrflg";
    public static final String TV_CLIP_LIST_AVAIL_STATUS = "avail_status";
    public static final String TV_CLIP_LIST_DELIVERY = "delivery";
    public static final String TV_CLIP_LIST_R_VALUE = "r_value";
    public static final String TV_CLIP_LIST_ADULT = "adult";
    public static final String TV_CLIP_LIST_MS = "ms";
    public static final String TV_CLIP_LIST_NG_FUNC = "ng_func";
    public static final String TV_CLIP_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String TV_CLIP_LIST_DTV = "dtv";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + TV_CLIP_LIST_TABLE_NAME + " (" +
            TV_CLIP_LIST_ID_COLUMN + " integer primary key, " +
            TV_CLIP_LIST_CRID + " text not null " +
            TV_CLIP_LIST_CID + " text not null " +
            TV_CLIP_LIST_TITLE_ID + " text not null " +
            TV_CLIP_LIST_EPISODE_ID + " text not null " +
            TV_CLIP_LIST_TITLE + " text not null " +
            TV_CLIP_LIST_EPITITLE + " text not null " +
            TV_CLIP_LIST_DISP_TYPE + " text not null " +
            TV_CLIP_LIST_DISPLAY_START_DATE + " text not null " +
            TV_CLIP_LIST_DISPLAY_END_DATE + " text not null " +
            TV_CLIP_LIST_AVAIL_START_DATE + " text not null " +
            TV_CLIP_LIST_AVAIL_END_DATE + " text not null " +
            TV_CLIP_LIST_PUBLISH_START_DATE + " text not null " +
            TV_CLIP_LIST_PUBLISH_END_DATE + " text not null " +
            TV_CLIP_LIST_NEWA_START_DATE + " text not null " +
            TV_CLIP_LIST_NEWA_END_DATE + " text not null " +
            TV_CLIP_LIST_COPYRIGHT + " text not null " +
            TV_CLIP_LIST_THUMB + " text not null " +
            TV_CLIP_LIST_DUR + " text not null " +
            TV_CLIP_LIST_DEMONG + " text not null " +
            TV_CLIP_LIST_BVFLG + " text not null " +
            TV_CLIP_LIST_4KFLG + " text not null " +
            TV_CLIP_LIST_HDRFLG + " text not null " +
            TV_CLIP_LIST_AVAIL_STATUS + " text not null " +
            TV_CLIP_LIST_DELIVERY + " text not null " +
            TV_CLIP_LIST_R_VALUE + " text not null " +
            TV_CLIP_LIST_ADULT + " text not null " +
            TV_CLIP_LIST_MS + " text not null " +
            TV_CLIP_LIST_NG_FUNC + " text not null " +
            TV_CLIP_LIST_GENRE_ID_ARRAY + " text not null " +
            TV_CLIP_LIST_DTV + " text not null " +
            ")";

    /**
     * 「tv_clip_list」テーブルの削除用SQL
     */
    private static final String DROP_TABLE_SQL = "drop table if exists " + TV_CLIP_LIST_TABLE_NAME;

    /**
     * コンストラクタ
     * @param context
     */
    public TvClipListDBHelper(Context context) {
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
