/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.USER_INFO_LIST_TABLE_NAME;

public class UserInfoListDao {
    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db データベース
     */
    public UserInfoListDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得
     *
     * @param strings 列データ名
     * @return 取得データ
     */
    public List<Map<String, String>> findById(String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor = db.query(
                USER_INFO_LIST_TABLE_NAME,
                strings,
                null,
                null,
                null,
                null,
                null);

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
     * データの登録・集合
     *
     * @param values 値の集合
     * @return 書き込み位置
     */
    public long insert(ContentValues values) {
        return db.insert(USER_INFO_LIST_TABLE_NAME, null, values);
    }

    /**
     * データの登録・単独
     *
     * @param key   　書き込みキー名
     * @param value 書き込み位置
     * @return 書き込み位置
     */
    public long insert(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(key, value);
        return db.insert(USER_INFO_LIST_TABLE_NAME, null, values);
    }

    public int update() {
        //基本的にデータの更新はしない予定
        return 0;
    }

    /**
     * データの削除
     *
     * @return 削除行数
     */
    public int delete() {
        return db.delete(USER_INFO_LIST_TABLE_NAME, null, null);
    }
}