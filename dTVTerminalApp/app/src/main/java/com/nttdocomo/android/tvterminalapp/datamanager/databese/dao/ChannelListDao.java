package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.ChannelListDBHelper.CHANNEL_LIST_ID_COLUMN;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.ChannelListDBHelper.CHANNEL_LIST_SERVICE_ID;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.ChannelListDBHelper.CHANNEL_LIST_TABLE_NAME;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class ChannelListDao {
    // テーブルの定数
//    private static final String TABLE_NAME = "channel_table";
//    private static final String COLUMN_ID = "row_id";
//    private static final String COLUMN_DATA = "data";
    private static final String[] COLUMNS = {CHANNEL_LIST_ID_COLUMN, CHANNEL_LIST_SERVICE_ID};

    // SQLiteDatabase
    private SQLiteDatabase db;

    public ChannelListDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 特定IDのデータを取得
     *
     * @param rowId
     * @return
     */
    public ChannelList findById(int rowId) {
        String selection = CHANNEL_LIST_ID_COLUMN + "=" + rowId;
        Cursor cursor = db.query(
                CHANNEL_LIST_TABLE_NAME,
                COLUMNS,
                selection,
                null,
                null,
                null,
                null);

        cursor.moveToNext();
        ChannelList entity = new ChannelList();

        return entity;
    }

    /**
     * データの登録
     *
     * @param value
     * @return
     */
    public long insert(String value) {
        ContentValues values = new ContentValues();
        values.put(CHANNEL_LIST_SERVICE_ID, value);
        return db.insert(CHANNEL_LIST_TABLE_NAME, null, values);
    }

    /**
     * データを一括削除
     *
     * @return
     */
    public int delete() {
        //基本的には一括削除
        return db.delete(CHANNEL_LIST_TABLE_NAME, null, null);
    }
}
