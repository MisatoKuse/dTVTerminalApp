/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ユーザー情報リスト Data Access Object.
 */
public class UserInfoListDao {
    /**
     * SQLiteDatabase.
     */
    private final SQLiteDatabase db;

    /**
     * コンストラクタ.
     *
     * @param db データベース
     */
    public UserInfoListDao(final SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 列データ名
     * @return 取得データ
     */
    public List<Map<String, String>> findById(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor = db.query(
                DBConstants.USER_INFO_LIST_TABLE_NAME,
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
            for (String string : strings) {
                map.put(string, cursor.getString(cursor.getColumnIndex(string)));
            }
            list.add(map);

            isEof = cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    /**
     * データの登録・集合.
     *
     * @param values 値の集合
     * @return 書き込み位置
     */
    public long insert(final ContentValues values) {
        return db.insert(DBConstants.USER_INFO_LIST_TABLE_NAME, null, values);
    }

    /**
     * データの登録・単独.
     *
     * @param key   　書き込みキー名
     * @param value 書き込み位置
     * @return 書き込み位置
     */
    @SuppressWarnings("unused")
    public long insert(final String key, final String value) {
        ContentValues values = new ContentValues();
        values.put(key, value);
        return db.insert(DBConstants.USER_INFO_LIST_TABLE_NAME, null, values);
    }

    /**
     * データの更新.
     *
     * @return 更新リターン
     */
    @SuppressWarnings({"SameReturnValue", "unused"})
    public int update() {
        //基本的にデータの更新はしない予定
        return 0;
    }

    /**
     * データの削除.
     *
     * @return 削除行数
     */
    public int delete() {
        return db.delete(DBConstants.USER_INFO_LIST_TABLE_NAME, null, null);
    }
}