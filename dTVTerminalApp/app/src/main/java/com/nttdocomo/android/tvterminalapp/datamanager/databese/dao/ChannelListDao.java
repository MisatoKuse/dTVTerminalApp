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

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.CHANNEL_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATE_TYPE;


public class ChannelListDao {
    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db
     */
    public ChannelListDao(SQLiteDatabase db) {
        this.db = db;
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

        Cursor cursor = db.query(
                CHANNEL_LIST_TABLE_NAME,
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
        db.close();
        return list;
    }

    /**
     * 配列で指定した列データをすべて取得
     *
     * @param strings
     * @return
     */
    public List<Map<String, String>> findByTypeAndDate(String[] strings, String type) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        StringBuilder selection = new StringBuilder();
        selection.append(CHANNEL_LIST_DISP_TYPE);
        selection.append("=? AND ");
        selection.append(DATE_TYPE);
        selection.append("=? ");
        Cursor cursor = db.query(
                CHANNEL_LIST_TABLE_NAME,
                strings,
                selection.toString(),
                new  String[]{type, "program"},
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
     * データの登録
     *
     * @param values
     * @return
     */
    public long insert(ContentValues values) {
        return db.insert(CHANNEL_LIST_TABLE_NAME, null, values);
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
        StringBuilder deleteSelection = new StringBuilder();
        deleteSelection.append(DATE_TYPE);
        deleteSelection.append("=?");
        return db.delete(CHANNEL_LIST_TABLE_NAME, deleteSelection.toString(), new String[]{"home"});
    }

    /**
     * データの削除
     *
     * @return
     */
    public int deleteByType(String type) {
        StringBuilder deleteSelection = new StringBuilder();
        deleteSelection.append(CHANNEL_LIST_DISP_TYPE);
        deleteSelection.append("=? AND ");
        deleteSelection.append(DATE_TYPE);
        deleteSelection.append("=?");
        return db.delete(CHANNEL_LIST_TABLE_NAME, deleteSelection.toString(), new String[]{type, "program"});
    }
}
