/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_CHANNEL_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_LIST_DANIME_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_LIST_DCHANNEL_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_LIST_DTV_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.RECOMMEND_VIDEO_LIST_TABLE_NAME;


public class RecommendListDao {
    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db
     */
    public RecommendListDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得
     *
     * @param strings カラム
     * @return
     */
    public List<Map<String, String>> findById(String[] strings, int tagPageNo, String limitData) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        String tableName = getTableName(tagPageNo);

        Cursor cursor = db.query(
                tableName,
                strings,
                null,
                null,
                null,
                null,
                null,
                limitData);


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
     * @return
     */
    public long insert(ContentValues values, int tagPageNo) {
        String tableName = getTableName(tagPageNo);
        return db.insert(tableName, "", values);
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
    public int delete(int tagPageNo) {
        String tableName = getTableName(tagPageNo);
        return db.delete(tableName, null, null);
    }

    /**
     * 操作するテーブル名を取得
     *
     * @param tagPageNo タグ名
     * @return tableName
     */
    private String getTableName(int tagPageNo) {
        String tableName = null;
        switch (tagPageNo) {
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV:
                tableName = RECOMMEND_CHANNEL_LIST_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO:
                tableName = RECOMMEND_VIDEO_LIST_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL:
                tableName = RECOMMEND_LIST_DCHANNEL_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV:
                tableName = RECOMMEND_LIST_DTV_TABLE_NAME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME:
                tableName = RECOMMEND_LIST_DANIME_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}