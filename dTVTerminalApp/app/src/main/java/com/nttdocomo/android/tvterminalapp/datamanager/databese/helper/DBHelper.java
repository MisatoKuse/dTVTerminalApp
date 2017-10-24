/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CHANNEL_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_CHANNEL_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_DAILY_RANK_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_RECOMMEND_CHANNEL_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_RECOMMEND_DANIME_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_RECOMMEND_DCHANNEL_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_RECOMMEND_DTV_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_RECOMMEND_VIDEO_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_TV_SCHEDULE_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_USER_INFO_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_VODCLIP_LIST_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_WEEKLYRANK_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CREATE_TABLE_RANKING_VIDEO_SQL;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DAILYRANK_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATABASE_VERSION;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_CHANNEL_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_LIST_DANIME_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_LIST_DTV_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_LIST_DCHANNEL_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_VIDEO_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.TV_SCHEDULE_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.USER_INFO_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.VODCLIP_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.WEEKLYRANK_LIST_TABLE_NAME;

public class DBHelper extends SQLiteOpenHelper {

    /**
     * 「channel_list」テーブルの削除用SQL
     */
    private static final String DROP_CHANNEL_TABLE_SQL = "drop table if exists " + CHANNEL_LIST_TABLE_NAME;
    private static final String DROP_DAILYRANK_TABLE_SQL = "drop table if exists " + DAILYRANK_LIST_TABLE_NAME;
    private static final String DROP_RECOMMEND_CHANNEL_TABLE_SQL = "drop table if exists " + RECOMMEND_CHANNEL_LIST_TABLE_NAME;
    private static final String DROP_RECOMMEND_VIDEO_TABLE_SQL = "drop table if exists " + RECOMMEND_VIDEO_LIST_TABLE_NAME;
    private static final String DROP_TV_SCHEDULE_TABLE_SQL = "drop table if exists " + TV_SCHEDULE_LIST_TABLE_NAME;
    private static final String USER_INFO_TABLE_SQL = "drop table if exists " + USER_INFO_LIST_TABLE_NAME;
    private static final String DROP_VODCLIP_TABLE_SQL = "drop table if exists " + VODCLIP_LIST_TABLE_NAME;
    private static final String DROP_WEEKLYRANK_TABLE_SQL = "drop table if exists " + WEEKLYRANK_LIST_TABLE_NAME;
    private static final String DROP_VIDEORANK_TABLE_SQL = "drop table if exists " + CREATE_TABLE_RANKING_VIDEO_SQL;
    private static final String DROP_RECOMMEND_DCHANNEL_TABLE_SQL = "drop table if exists " + RECOMMEND_LIST_DCHANNEL_TABLE_NAME;
    private static final String DROP_RECOMMEND_DTV_TABLE_SQL = "drop table if exists " + RECOMMEND_LIST_DTV_TABLE_NAME;
    private static final String DROP_RECOMMEND_DANIME_TABLE_SQL = "drop table if exists " + RECOMMEND_LIST_DANIME_TABLE_NAME;

    /**
     * コンストラクタ
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CHANNEL_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_DAILY_RANK_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_TV_SCHEDULE_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_USER_INFO_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_WEEKLYRANK_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_RECOMMEND_CHANNEL_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_RECOMMEND_VIDEO_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_VODCLIP_LIST_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_RANKING_VIDEO_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_RECOMMEND_DCHANNEL_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_RECOMMEND_DTV_SQL);
        sqLiteDatabase.execSQL(CREATE_TABLE_RECOMMEND_DANIME_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_CHANNEL_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_DAILYRANK_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_RECOMMEND_CHANNEL_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_RECOMMEND_VIDEO_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_TV_SCHEDULE_TABLE_SQL);
        sqLiteDatabase.execSQL(USER_INFO_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_VODCLIP_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_WEEKLYRANK_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_VIDEORANK_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_RECOMMEND_DCHANNEL_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_RECOMMEND_DTV_TABLE_SQL);
        sqLiteDatabase.execSQL(DROP_RECOMMEND_DANIME_TABLE_SQL);
    }
}