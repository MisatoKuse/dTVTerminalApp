/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * クリップキーリストData Access Object.
 */
public class MyChannelListDao {

    /**
     * SQLデータベースクラス.
     */
    private final SQLiteDatabase mSQLiteDatabase;

    /**
     * コンストラクタ.
     *
     * @param db  SQLデータベースクラス
     */
    public MyChannelListDao(final SQLiteDatabase db) {
        this.mSQLiteDatabase = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 列パラメータ
     * @return list クリップキー一覧
     */
    public List<Map<String, String>> findById(final String[] strings) {
        DTVTLogger.start();
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor;
        try {
            cursor = mSQLiteDatabase.query(
                    DataBaseConstants.MY_CHANNEL_LIST_TABLE_NAME,
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
            for (String string : strings) {
                map.put(string, cursor.getString(cursor.getColumnIndex(string)));
            }
            list.add(map);

            isEof = cursor.moveToNext();
        }
        cursor.close();

        DTVTLogger.end();
        return list;
    }

    /**
     * データの登録.
     * @param values 格納する値
     * @return SQLiteDatabaseクラスの戻り値(正常終了した場合はROWID,失敗した場合,マイナス1)
     */
    public long insert(final ContentValues values) {
        DTVTLogger.debug("Insert Data");
        return mSQLiteDatabase.insert(DataBaseConstants.MY_CHANNEL_LIST_TABLE_NAME, null, values);
    }

    /**
     * データの更新.
     * @return 常に0.
     */
    @SuppressWarnings({"unused", "SameReturnValue"})
    public int update() {
        //基本的にデータの更新はしない(取得したデータで全て置き換え)
        return 0;
    }

    /**
     * データの削除(type指定).
     * @return SQLiteDatabaseクラスの戻り値(削除されたレコード数)
     */
    public int delete() {
        DTVTLogger.debug("Delete Data");
        return mSQLiteDatabase.delete(DataBaseConstants.MY_CHANNEL_LIST_TABLE_NAME, null, null);
    }
}
