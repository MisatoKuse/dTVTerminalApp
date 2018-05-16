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

import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;

/**
 * 今日のテレビランキングData Access Object.
 */
public class DailyRankListDao {

    /**
     * SQLデータベースクラス.
     */
    private final SQLiteDatabase mSQLiteDatabase;

    /**
     * コンストラクタ.
     *
     * @param db SQLデータベースクラス
     */
    public DailyRankListDao(final SQLiteDatabase db) {
        this.mSQLiteDatabase = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 取得したいテーブル名の配列
     * @return 今日のテレビランキング情報
     */
    public List<Map<String, String>> findById(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor = mSQLiteDatabase.query(
                DataBaseConstants.DAILYRANK_LIST_TABLE_NAME,
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
            for (String stringBuffer : strings) {
                map.put(stringBuffer, cursor.getString(cursor.getColumnIndex(stringBuffer)));
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
     * @param values 格納するデータ
     * @return 挿入データID
     */
    public long insert(final ContentValues values) {
        return mSQLiteDatabase.insert(DataBaseConstants.DAILYRANK_LIST_TABLE_NAME, null, values);
    }

    /**
     * データの更新.
     * @return 常に0.
     */
    @SuppressWarnings({"SameReturnValue", "unused"})
    public int update() {
        //基本的にデータの更新はしない(取得したデータで全て置き換え)
        return 0;
    }

    /**
     * データの削除.
     *
     * @return deleteの第2パラメータがヌルなのでゼロとなる
     */
    public int delete() {
        return mSQLiteDatabase.delete(DataBaseConstants.DAILYRANK_LIST_TABLE_NAME, null, null);
    }
}
