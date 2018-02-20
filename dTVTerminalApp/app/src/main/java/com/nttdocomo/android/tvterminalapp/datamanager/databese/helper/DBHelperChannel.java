/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;

/**
 * 番組情報保存用DBHelper.
 */
public class DBHelperChannel extends SQLiteOpenHelper {

    /**
     * DBVersion.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     * @param name DB名
     */
    public DBHelperChannel(final Context context, final String name) {
        super(context, name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_TV_SCHEDULE_SQL);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_TV_SCHEDULE_SQL);
    }
}
