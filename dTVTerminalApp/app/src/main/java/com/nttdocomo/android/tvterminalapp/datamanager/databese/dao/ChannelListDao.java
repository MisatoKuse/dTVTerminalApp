package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBTestEntity;

import java.util.ArrayList;
import java.util.List;

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
     * 全データの取得
     *
     * @return
     */
    public List<DBTestEntity> findAll() {
        List<DBTestEntity> entityList = new ArrayList<DBTestEntity>();
        Cursor cursor = db.query(
                CHANNEL_LIST_TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                CHANNEL_LIST_ID_COLUMN);

        while (cursor.moveToNext()) {
            DBTestEntity entity = new DBTestEntity();
            entityList.add(entity);
        }

        return entityList;
    }

    /**
     * 特定IDのデータを取得
     *
     * @param rowId
     * @return
     */
    public DBTestEntity findById(int rowId) {
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
        DBTestEntity entity = new DBTestEntity();

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
     * データの更新
     *
     * @param entity
     * @return
     */
    public int update(DBTestEntity entity) {
        //基本的にデータの更新はしない予定
        return 0;
    }

    /**
     * データの削除
     *
     * @param rowId
     * @return
     */
    public int delete(int rowId) {
        //基本的には一括削除
        String whereClause = CHANNEL_LIST_ID_COLUMN + "=" + rowId;
        return db.delete(CHANNEL_LIST_TABLE_NAME, whereClause, null);
    }
}
