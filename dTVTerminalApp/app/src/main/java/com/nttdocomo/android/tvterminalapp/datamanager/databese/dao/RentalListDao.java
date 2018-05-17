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
 * レンタルビデオData Access Object.
 */
public class RentalListDao {

    /**
     * SQLiteDatabase.
     */
    private SQLiteDatabase mSQLiteDatabase = null;

    /**
     * コンストラクタ.
     *
     * @param db データベース
     */
    public RentalListDao(final SQLiteDatabase db) {
        this.mSQLiteDatabase = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings columns
     * @return 取得結果
     */
    public List<Map<String, String>> findById(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor;
        try {
            cursor = mSQLiteDatabase.query(
                    DataBaseConstants.RENTAL_LIST_TABLE_NAME,
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

        return list;
    }

    /**
     * 配列で指定した列データをすべて取得(active_list).
     *
     * @param strings columns
     * @return 取得結果
     */
    public List<Map<String, String>> activeListFindById(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor;
        try {
            cursor = mSQLiteDatabase.query(
                    DataBaseConstants.RENTAL_ACTIVE_LIST_TABLE_NAME,
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

        return list;
    }

    /**
     * 配列で指定した列データをすべて取得(CH).
     *
     * @param strings columns
     * @return 取得結果
     */
    public List<Map<String, String>> chFindById(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor;
        try {
            cursor = mSQLiteDatabase.query(
                    DataBaseConstants.RENTAL_CHANNEL_LIST_TABLE_NAME,
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

        return list;
    }

    /**
     * 配列で指定した列データをすべて取得(CHのactive_list).
     *
     * @param strings columns
     * @return 取得結果
     */
    public List<Map<String, String>> chActiveListFindById(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor;
        try {
            cursor = mSQLiteDatabase.query(
                    DataBaseConstants.RENTAL_CHANNEL_ACTIVE_LIST_TABLE_NAME,
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

        return list;
    }

    /**
     * データの登録.
     *
     * @param values 保存するデータ
     * @return 結果
     */
    public long insert(final ContentValues values) {
        return mSQLiteDatabase.insert(DataBaseConstants.RENTAL_LIST_TABLE_NAME, null, values);
    }

    /**
     * レンタル一覧のactive_listのデータ登録.
     *
     * @param values 保存するデータ
     * @return 結果
     */
    public long insertActiveList(final ContentValues values) {
        return mSQLiteDatabase.insert(DataBaseConstants.RENTAL_ACTIVE_LIST_TABLE_NAME, null, values);
    }


    /**
     * 購入済みチャンネル一覧のデータ登録.
     *
     * @param values 保存する値
     * @return 結果
     */
    public long insertCh(final ContentValues values) {
        return mSQLiteDatabase.insert(DataBaseConstants.RENTAL_CHANNEL_LIST_TABLE_NAME, null, values);
    }

    /**
     * 購入済みチャンネル一覧のactive_listのデータ登録.
     *
     * @param values 保存するデータ
     * @return 結果
     */
    public long insertChActiveList(final ContentValues values) {
        return mSQLiteDatabase.insert(DataBaseConstants.RENTAL_CHANNEL_ACTIVE_LIST_TABLE_NAME, null, values);
    }

    /**
     * アップデート.
     *
     * @return 結果
     */
    @SuppressWarnings({"SameReturnValue", "unused"})
    public int update() {
        //基本的にデータの更新はしない予定
        return 0;
    }

    /**
     * データの削除.
     *
     * @return 結果
     */
    public int delete() {
        return mSQLiteDatabase.delete(DataBaseConstants.RENTAL_LIST_TABLE_NAME, null, null);
    }

    /**
     * レンタル一覧のactive_listのデータ削除.
     *
     * @return 結果
     */
    @SuppressWarnings("unused")
    public int deleteActiveList() {
        return mSQLiteDatabase.delete(DataBaseConstants.RENTAL_ACTIVE_LIST_TABLE_NAME, null, null);
    }

    /**
     * 購入済みチャンネル一覧のデータ削除.
     *
     * @return 結果
     */
    public int deleteCh() {
        return mSQLiteDatabase.delete(DataBaseConstants.RENTAL_CHANNEL_LIST_TABLE_NAME, null, null);
    }

    /**
     * レンタル一覧のactive_listのデータ削除.
     *
     * @return 結果
     */
    @SuppressWarnings("unused")
    public int deleteChActiveList() {
        return mSQLiteDatabase.delete(DataBaseConstants.RENTAL_CHANNEL_ACTIVE_LIST_TABLE_NAME, null, null);
    }
}