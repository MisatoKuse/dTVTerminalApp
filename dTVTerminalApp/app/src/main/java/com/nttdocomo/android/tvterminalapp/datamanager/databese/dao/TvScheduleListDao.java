/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.TV_SCHEDULE_LIST_TABLE_NAME;

/**
 * 番組情報取得用Dao.
 */
public class TvScheduleListDao {
    /**
     * SQLiteDatabase.
     */
    private final SQLiteDatabase db;

    /**
     * コンストラクタ.
     *
     * @param db SQLiteDatabase
     */
    public TvScheduleListDao(final SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 指定列
     * @return 取得データ
     */
    public List<Map<String, String>> findById(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = db.query(
                TV_SCHEDULE_LIST_TABLE_NAME,
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
        db.close();
        return list;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param columnStr 指定列
     * @return 取得データ
     */
    public List<Map<String, String>> findByTypeAndDate(final String[] columnStr) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = db.query(
                TV_SCHEDULE_LIST_TABLE_NAME,
                columnStr,
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
            for (String aColumnStr : columnStr) {
                map.put(aColumnStr, cursor.getString(cursor.getColumnIndex(aColumnStr)));
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
     * @param values 保存する値
     * @return 保存結果
     */
    public long insert(final ContentValues values) {
        return db.insert(TV_SCHEDULE_LIST_TABLE_NAME, null, values);
    }

    /**
     * update.
     *
     * @return update結果(0)
     */
    @SuppressWarnings({"SameReturnValue", "unused"})
    public int update() {
        //基本的にデータの更新はしない予定
        return 0;
    }

    /**
     * データの削除.
     *
     * @return 削除結果.
     */
    public int delete() {
        return db.delete(TV_SCHEDULE_LIST_TABLE_NAME, null, null);
    }

    /**
     * データの削除.
     *
     * @param type 削除するデータタイプ
     * @return 削除結果
     */
    @SuppressWarnings("unused")
    public int deleteByType(final String type) {
        String deleteSelection = StringUtils.getConnectStrings(JsonConstants.META_RESPONSE_DISP_TYPE, "=? ");
        return db.delete(TV_SCHEDULE_LIST_TABLE_NAME, deleteSelection, new String[]{type});
    }
}

