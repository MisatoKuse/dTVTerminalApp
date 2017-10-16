package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.VodClipListDBHelper.VOD_CLIP_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.VodClipListDBHelper.VOD_CLIP_LIST_ID_COLUMN;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.VodClipListDBHelper.VOD_CLIP_LIST_TABLE_NAME;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class VodClipListDao {

    private static final String[] COLUMNS = {VOD_CLIP_LIST_ID_COLUMN, VOD_CLIP_LIST_CRID};

    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db
     */
    public VodClipListDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 特定IDのデータを取得
     *
     * @param rowId
     * @return
     */
    public VodClipList findById(int rowId) {
        //特定IDのデータ取得はしない方針
        String selection = VOD_CLIP_LIST_ID_COLUMN + "=" + rowId;
        Cursor cursor = db.query(
                VOD_CLIP_LIST_TABLE_NAME,
                COLUMNS,
                selection,
                null,
                null,
                null,
                null);

        cursor.moveToNext();
        VodClipList entity = new VodClipList();

        return entity;
    }

    /**
     * データの登録
     *
     * @param value
     * @return
     */
    public long insert(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(key, value);
        return db.insert(VOD_CLIP_LIST_TABLE_NAME, null, values);
    }

    public int update() {
        //基本的にデータの更新はしない予定
        return 0;
    }

    /**
     * データの削除
     *
     * @return
     */
    public int delete() {
        return db.delete(VOD_CLIP_LIST_TABLE_NAME, null, null);
    }
}
