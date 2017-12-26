/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RENTAL_LIST_TABLE_NAME;


public class RentalListDao {

    // SQLiteDatabase
    private SQLiteDatabase mSQLiteDatabase = null;

    /**
     * コンストラクタ
     *
     * @param db
     */
    public RentalListDao(SQLiteDatabase db) {
        this.mSQLiteDatabase = db;
    }

    /**
     * 配列で指定した列データをすべて取得
     *
     * @param strings
     * @return
     */
    public List<Map<String, String>> findById(String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor =null;
        try {
            cursor = mSQLiteDatabase.query(
                    RENTAL_LIST_TABLE_NAME,
                    strings,
                    null,
                    null,
                    null,
                    null,
                    null);
        }catch(Exception e){
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
     * @return
     */
    public long insert(ContentValues values) {
        return mSQLiteDatabase.insert(RENTAL_LIST_TABLE_NAME, null, values);
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
        return mSQLiteDatabase.delete(RENTAL_LIST_TABLE_NAME, null, null);
    }
}