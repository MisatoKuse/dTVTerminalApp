package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class ChannelListDBHelper extends SQLiteOpenHelper {

    /**　テーブル作成用定数(最終的にはJsonPerser側から入手) **/
    public static final String CHANNEL_LIST_TABLE_NAME = "channel_list";
    public static final String CHANNEL_LIST_ID_COLUMN = "row_id";
    public static final String CHANNEL_LIST_VALUE_NAME_COLUMN = "value_name";
    public static final String CHANNEL_LIST_VALUE_COLUMN = "value";
    public static final String CHANNEL_LIST_STATUS = "status";
    public static final String CHANNEL_LIST_COUNT = "count";
    public static final String CHANNEL_LIST_SERVICE_ID = "service_id";
    public static final String CHANNEL_LIST_TITLE = "title";
    public static final String CHANNEL_LIST_R_VALUE = "r_value";
    public static final String CHANNEL_LIST_ADULT_TYPE = "adult_type";
    public static final String CHANNEL_LIST_INDEX = "index";

    //Homeキャッシュデータ格納用テーブル
    private static final String CREATE_TABLE_SQL = "" +
            "create table " + CHANNEL_LIST_TABLE_NAME + " (" +
            CHANNEL_LIST_ID_COLUMN + " integer primary key, " +
//            CHANNEL_LIST_VALUE_NAME_COLUMN + " text not null " +
            CHANNEL_LIST_SERVICE_ID + " text not null " +
            CHANNEL_LIST_TITLE + " text not null " +
            CHANNEL_LIST_R_VALUE + " text not null " +
            CHANNEL_LIST_ADULT_TYPE + " text not null " +
            CHANNEL_LIST_INDEX + " text not null " +
            ")";

    /** 「channel_list」テーブルの削除用SQL */
    private static final String DROP_TABLE_SQL = "drop table if exists " + CHANNEL_LIST_TABLE_NAME;

    /**
     * コンストラクタ
     *
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
