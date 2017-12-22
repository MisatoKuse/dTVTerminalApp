/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATE_TYPE;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.TV_SCHEDULE_LIST_TABLE_NAME;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UPDATE_DATE;

public class TvScheduleListDao {
    // SQLiteDatabase
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     *
     * @param db
     */
    public TvScheduleListDao(SQLiteDatabase db) {
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
     * @param colomuStr
     * @return
     */
    public List<Map<String, String>> findByTypeAndDate(String[] colomuStr, String type, String date) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        //TODO DB日付検索条件除く、将来は対応必要
        /*StringBuilder selection = new StringBuilder();
        selection.append(JsonContents.META_RESPONSE_DISP_TYPE);
        selection.append("=? AND ");
        selection.append(UPDATE_DATE);
        selection.append("=? AND ");
        selection.append(DATE_TYPE);
        selection.append("=? ");*/
        Cursor cursor = db.query(
                TV_SCHEDULE_LIST_TABLE_NAME,
                colomuStr,
//                selection.toString(),
                null,
//                new String[]{type, date, "program"},
                null,
                null,
                null,
                null);
        //TODO DB日付検索条件除く、将来は対応必要
        //参照先を一番始めに
        boolean isEof = cursor.moveToFirst();

        //データを一行ずつ格納する
        while (isEof) {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < colomuStr.length; i++) {
                map.put(colomuStr[i], cursor.getString(cursor.getColumnIndex(colomuStr[i])));
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
        return db.insert(TV_SCHEDULE_LIST_TABLE_NAME, null, values);
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
        return db.delete(TV_SCHEDULE_LIST_TABLE_NAME, null, null);
    }

    /**
     * データの削除
     *
     * @return
     */
    public int deleteByType(String type) {
        StringBuilder deleteSelection = new StringBuilder();
        deleteSelection.append(JsonContents.META_RESPONSE_DISP_TYPE);
        deleteSelection.append("=? AND ");
        deleteSelection.append(DATE_TYPE);
        deleteSelection.append("=?");
        return db.delete(TV_SCHEDULE_LIST_TABLE_NAME, deleteSelection.toString(), new String[]{type, "program"});
    }
}

