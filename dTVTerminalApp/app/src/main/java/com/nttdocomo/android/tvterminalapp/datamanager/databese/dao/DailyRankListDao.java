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

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DAILYRANK_LIST_TABLE_NAME;

public class DailyRankListDao {
    // SQLiteDatabase
    private final SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db データベース
     */
    public DailyRankListDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得
     *
     * @param strings 欲しい列データの名前
     * @return 列データ結果
     */
    public List<Map<String, String>> findById(String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor = db.query(
                DAILYRANK_LIST_TABLE_NAME,
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
     * データの登録
     *
     * @param values 格納するデータ
     * @return 挿入データID
     */
    public long insert(ContentValues values) {
        return db.insert(DAILYRANK_LIST_TABLE_NAME, null, values);
    }

    public int update() {
        //基本的にデータの更新はしない予定
        return 0;
    }

    /**
     * データの削除
     *
     * @return deleteの第2パラメータがヌルなのでゼロとなる
     */
    public int delete() {
        return db.delete(DAILYRANK_LIST_TABLE_NAME, null, null);
    }
}
