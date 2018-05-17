/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * おすすめリストData Access Object.
 */
public class RecommendListDao {
    /**
     * SQLiteDatabase.
     */
    private final SQLiteDatabase mSQLiteDatabase;

    /**
     * コンストラクタ.
     *
     * @param db SQLiteDatabase
     */
    public RecommendListDao(final SQLiteDatabase db) {
        this.mSQLiteDatabase = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings カラム
     * @param tagPageNo タブ名のタグ番号
     * @return 取得データ
     */
    public List<Map<String, String>> findById(final String[] strings, final int tagPageNo) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        String tableName = DataBaseUtils.getRecommendTableName(tagPageNo);

        Cursor cursor = mSQLiteDatabase.query(
                tableName,
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
     * データの登録.
     *
     * @param values 登録するデータ
     * @param tagPageNo タブ名のタグ番号
     * @return 成功時:row ID 失敗時:-1
     */
    public long insert(final ContentValues values, final int tagPageNo) {
        String tableName = DataBaseUtils.getRecommendTableName(tagPageNo);
        return mSQLiteDatabase.insert(tableName, "", values);
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
     * @param tagPageNo タブ名のタグ番号
     * @return リターン値
     */
    public int delete(final int tagPageNo) {
        String tableName = DataBaseUtils.getRecommendTableName(tagPageNo);
        DTVTLogger.debug(String.format("RecommendListDao.delete [%s]", tableName));
        return mSQLiteDatabase.delete(tableName, null, null);
    }
}