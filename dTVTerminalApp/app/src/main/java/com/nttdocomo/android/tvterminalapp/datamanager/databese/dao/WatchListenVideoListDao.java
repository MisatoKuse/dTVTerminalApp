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
 * 視聴中ビデオ Data Access Object.
 */
public class WatchListenVideoListDao {

    /**
     * SQLiteDatabase.
     */
    private final SQLiteDatabase db;

    /**
     * コンストラクタ.
     *
     * @param db データベース
     */
    public WatchListenVideoListDao(final SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 欲しい列データの名前
     * @return 列データ結果
     */
    public List<Map<String, String>> findById(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor = db.query(
                DBConstants.WATCH_LISTEN_VIDEO_TABLE_NAME,
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
            if (map.isEmpty()) {
                list = null;
            } else {
                if (list != null) {
                    list.add(map);
                }
            }
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
        return db.insert(DBConstants.WATCH_LISTEN_VIDEO_TABLE_NAME, null, values);
    }

    /**
     * データの削除.
     *
     * @return deleteの第2パラメータがヌルなのでゼロとなる
     */
    public int delete() {
        return db.delete(DBConstants.WATCH_LISTEN_VIDEO_TABLE_NAME, null, null);
    }
}