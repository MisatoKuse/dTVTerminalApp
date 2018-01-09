/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClipKeyListDao {

    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db
     */
    public ClipKeyListDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得
     *
     * @param strings 列パラメータ
     * @return list クリップキー一覧
     */
    public List<Map<String, String>> findById(String[] strings) {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    DBConstants.CLIP_KEY_LIST_TABLE_NAME,
                    strings,
                    null,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            DTVTLogger.debug(e);
            return null;
        }

        //参照先を一番始めに
        boolean isEof = cursor.moveToFirst();

        //データを一行ずつ格納する
        while (isEof) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < strings.length; i++) {
                map.put(strings[i], cursor.getString(cursor.getColumnIndex(strings[i])));
            }
            list.add(map);

            isEof = cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    /**
     * データの登録
     *
     * @param values
     */
    public long insert(ContentValues values) {
        return db.insert(DBConstants.CLIP_KEY_LIST_TABLE_NAME, null, values);
    }

    public int update() {
        //基本的にデータの更新はしない予定
        return 0;
    }

    /**
     * データの削除
     */
    public int delete() {
        return db.delete(DBConstants.CLIP_KEY_LIST_TABLE_NAME, null, null);
    }
}
