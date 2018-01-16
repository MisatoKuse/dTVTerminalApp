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

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DOWNLOAD_LIST_TABLE_NAME;


public class DownLoadListDao {
    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db
     */
    public DownLoadListDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得
     *
     * @param strings
     * @return リスト
     */
    public List<Map<String, String>> findDownLoadList(String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor = db.query(
                DOWNLOAD_LIST_TABLE_NAME,
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
     * 配列で指定した列データをすべて取得
     *
     * @param strings
     * @return ダウンロードリスト
     */
    public List<Map<String, String>> findAllDowloadList(String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = db.query(
                DOWNLOAD_LIST_TABLE_NAME,
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
     * 配列で指定した列データをすべて取得
     *
     * @param itemId 項目ID
     * @return リスト
     */
    public List<Map<String, String>> findByItemId(String itemId) {
        //特定IDのデータ取得はしない方針
        String []selections = {DBConstants.DOWNLOAD_LIST_COLUM_URL};
        List<Map<String, String>> list = new ArrayList<>();
        StringBuilder selectSelection = new StringBuilder();
        /*selectSelection.append(DBConstants.DOWNLOAD_LIST_COLUM_USER_ID);
        selectSelection.append("=? AND ");*/
        selectSelection.append(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
        selectSelection.append("=? ");

        Cursor cursor = db.query(
                DOWNLOAD_LIST_TABLE_NAME,
                selections,
                selectSelection.toString(),
                new String[]{itemId},
                null,
                null,
                null);

        //参照先を一番始めに
        boolean isEof = cursor.moveToFirst();

        //データを一行ずつ格納する
        while (isEof) {
            HashMap<String, String> map = new HashMap<>();
            map.put(DBConstants.DOWNLOAD_LIST_COLUM_URL, cursor.getString(cursor.getColumnIndex(DBConstants.DOWNLOAD_LIST_COLUM_URL)));
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
     * @return 挿入リターン
     */
    public long insert(ContentValues values) {
        return db.insert(DOWNLOAD_LIST_TABLE_NAME, null, values);
    }

    /**
     * データの登録
     *
     * @param contentValues 更新colum
     * @param itemId 更新条件
     * @return 更新リターン
     */
    public int updatebyItemId(ContentValues contentValues, String itemId) {
        //基本的にデータの更新はしない予定
        StringBuilder updateSelection = new StringBuilder();
        updateSelection.append(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
        updateSelection.append("=? ");
        return db.update(DOWNLOAD_LIST_TABLE_NAME, contentValues , updateSelection.toString(), new String[]{itemId});
    }

    /**
     * データの削除
     *
     * @return 削除リターン
     */
    public int delete() {
        return db.delete(DOWNLOAD_LIST_TABLE_NAME, null, null);
    }

    /**
     * データの削除by itemId
     *
     * @return 削除リターン
     */
    public int deleteByItemId(String itemId) {
        StringBuilder deleteSelection = new StringBuilder();
        deleteSelection.append(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
        deleteSelection.append("=? ");
        return db.delete(DOWNLOAD_LIST_TABLE_NAME, deleteSelection.toString(), new String[]{itemId});
    }
}
