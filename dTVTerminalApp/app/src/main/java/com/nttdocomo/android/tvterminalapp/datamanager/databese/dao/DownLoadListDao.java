/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ダウンロードData Access Object.
 */
public class DownLoadListDao {
    /**
     * SQLiteDatabase.
     */
    private final SQLiteDatabase mSQLiteDatabase;

    /**
     * コンストラクタ.
     *
     * @param db SQLiteDatabase
     */
    public DownLoadListDao(final SQLiteDatabase db) {
        this.mSQLiteDatabase = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 指定列
     * @return リスト
     */
    public List<Map<String, String>> findDownLoadList(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor = mSQLiteDatabase.query(
                DataBaseConstants.DOWNLOAD_LIST_TABLE_NAME,
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
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 指定列
     * @return ダウンロードリスト
     */
    public List<Map<String, String>> findAllDownloadList(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query(
                DataBaseConstants.DOWNLOAD_LIST_TABLE_NAME,
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
     * 配列で指定した列データをすべて取得.
     *
     * @param itemId 項目ID
     * @return リスト
     */
    public List<Map<String, String>> findByItemId(final String itemId) {
        //特定IDのデータ取得はしない方針
        String[] selections = {DataBaseConstants.DOWNLOAD_LIST_COLUM_URL};
        List<Map<String, String>> list = new ArrayList<>();
        String selectSelection = StringUtils.getConnectStrings(
                DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID, "=? ");

        Cursor cursor = mSQLiteDatabase.query(
                DataBaseConstants.DOWNLOAD_LIST_TABLE_NAME,
                selections,
                selectSelection,
                new String[]{itemId},
                null,
                null,
                null);

        //参照先を一番始めに
        boolean isEof = cursor.moveToFirst();

        //データを一行ずつ格納する
        while (isEof) {
            HashMap<String, String> map = new HashMap<>();
            map.put(DataBaseConstants.DOWNLOAD_LIST_COLUM_URL, cursor.getString(cursor.getColumnIndex(DataBaseConstants.DOWNLOAD_LIST_COLUM_URL)));
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
     * @return 挿入リターン
     */
    public long insert(final ContentValues values) {
        return mSQLiteDatabase.insert(DataBaseConstants.DOWNLOAD_LIST_TABLE_NAME, null, values);
    }

    /**
     * データの登録.
     *
     * @param contentValues 更新column
     * @param itemId 更新条件
     * @return 更新リターン
     */
    public int updateByItemId(final ContentValues contentValues, final String itemId) {
        //基本的にデータの更新はしない予定
        String updateSelection = StringUtils.getConnectStrings(
                DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID, "=? ");
        return mSQLiteDatabase.update(DataBaseConstants.DOWNLOAD_LIST_TABLE_NAME, contentValues, updateSelection, new String[]{itemId});
    }

    /**
     * データの削除.
     *
     * @return 削除リターン
     */
    public int delete() {
        return mSQLiteDatabase.delete(DataBaseConstants.DOWNLOAD_LIST_TABLE_NAME, null, null);
    }

    /**
     * データの削除by mItemId.
     *
     * @param itemId mItemId
     * @return 削除リターン
     */
    public int deleteByItemId(final String itemId) {
        String deleteSelection = StringUtils.getConnectStrings(
                DataBaseConstants.DOWNLOAD_LIST_COLUM_ITEM_ID, "=? ");
        return mSQLiteDatabase.delete(DataBaseConstants.DOWNLOAD_LIST_TABLE_NAME, deleteSelection, new String[]{itemId});
    }
}
