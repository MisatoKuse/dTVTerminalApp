/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClipKeyListDao {

    // SQLiteDatabase
    private SQLiteDatabase db;
    public static final String META_DISP_TYPE_TV_PROGRAM = "tv_program";
    public static final String META_DTV_FLAG_FALSE = "0";
    public static final String META_DTV_FLAG_TRUE = "1";


    /**
     * コンテンツの種類
     */
    public enum CONTENT_TYPE {
        TV,
        VOD,
        DTV
    }

    /**
     * テーブルの種類
     */
    public enum TABLE_TYPE {
        TV,
        VOD,
    }

    /**
     * コンストラクタ
     *
     * @param db
     */
    public ClipKeyListDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得
     *
     * @param strings 列パラメータ
     * @return list クリップキー一覧
     */
    public List<Map<String, String>> findById(String[] strings, TABLE_TYPE type, String selection, String[] args) {
        DTVTLogger.start();
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    getTableName(type),
                    strings,
                    selection,
                    args,
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
            for (int i = 0; i < strings.length; i++) {
                map.put(strings[i], cursor.getString(cursor.getColumnIndex(strings[i])));
            }
            list.add(map);

            isEof = cursor.moveToNext();
        }
        cursor.close();

        DTVTLogger.end();
        return list;
    }

    /**
     * データの登録
     *
     * @param type
     * @param values
     */
    public long insert(TABLE_TYPE type, ContentValues values) {
        DTVTLogger.debug("Insert Data");
        return db.insert(getTableName(type), null, values);
    }

    public int update() {
        //基本的にデータの更新はしない予定
        return 0;
    }

    /**
     * データの削除(type指定)
     */
    public int delete(TABLE_TYPE type) {
        DTVTLogger.debug("Delete Data : " + type);
        return db.delete(getTableName(type), null, null);
    }

    /**
     * データの削除
     */
    public void delete() {
        DTVTLogger.debug("Delete All Data");
        delete(TABLE_TYPE.TV);
        delete(TABLE_TYPE.VOD);
    }

    private String getTableName(TABLE_TYPE type) {
        String tableName = null;
        switch (type) {
            case TV:
                tableName = DBConstants.TV_CLIP_KEY_LIST_TABLE_NAME;
                break;
            case VOD:
                tableName = DBConstants.VOD_CLIP_KEY_LIST_TABLE_NAME;
                break;
        }
        return tableName;
    }
}
