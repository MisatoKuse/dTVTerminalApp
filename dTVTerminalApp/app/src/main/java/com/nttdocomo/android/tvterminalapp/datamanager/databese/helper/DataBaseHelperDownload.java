/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;

/**
 * 持ち出しリスト用データキャッシュ保存用DB.
 * ※持ち出しリストはローカルでの管理しかできないため、基本的にDBの更新は行わない.
 */
public class DataBaseHelperDownload extends SQLiteOpenHelper {

    /**
     * DBVersion.
     */
    private static final int DOWNLOAD_DATABASE_VERSION = 1;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public DataBaseHelperDownload(final Context context) {
        super(context, DataBaseConstants.DOWNLOAD_DATABASE_NAME, null, DOWNLOAD_DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(DataBaseConstants.CREATE_TABLE_DOWNLOAD_LIST_SQL);
        } catch (SQLiteException e) {
            DTVTLogger.debug("DataBaseHelperDownload::onCreate, create " + DataBaseConstants.CREATE_TABLE_DOWNLOAD_LIST_SQL + " table failed, cause=" + e.getCause());
        }
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        //DB更新時の処理を記載する
    }
}
