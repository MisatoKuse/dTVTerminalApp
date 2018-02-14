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

import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;

/**
 * チャンネルリストData Access Object.
 */
public class ChannelListDao {

    /**
     * SQLデータベースクラス.
     */
    private final SQLiteDatabase db;

    /**
     * コンストラクタ.
     *
     * @param db SQLデータベースクラス
     */
    public ChannelListDao(final SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 取得したいテーブル名の配列
     * @return チャンネルリスト情報.
     */
    public List<Map<String, String>> findById(final String[] strings) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();

        Cursor cursor = db.query(
                DBConstants.CHANNEL_LIST_TABLE_NAME,
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
     * @param strings 取得したいテーブル名の配列
     * @param service チャンネルメタのservice.ひかり or dch(空文字の場合は両方)
     * @return チャンネルリスト情報.
     */
    public List<Map<String, String>> findByService(final String[] strings, final String service) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        String selection = null;
        if (service != null && !service.isEmpty()) {
            String[] selectionStrings = {
                    JsonConstants.META_RESPONSE_SERVICE,
                    "=? "
            };
            selection = StringUtils.getConnectString(selectionStrings);
        }
        Cursor cursor = db.query(
                DBConstants.CHANNEL_LIST_TABLE_NAME,
                strings,
                selection,
                new  String[]{service},
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
     * データの登録.
     *
     * @param values 格納する値
     * @return SQLiteDatabaseクラスの戻り値(正常終了した場合はROWID,失敗した場合,マイナス1)
     */
    public long insert(final ContentValues values) {
        return db.insert(DBConstants.CHANNEL_LIST_TABLE_NAME, null, values);
    }

    /**
     * データの更新.
     * @return 常に0.
     */
    @SuppressWarnings("SameReturnValue")
    public int update() {
        //基本的にデータの更新はしない(取得したデータで全て置き換え)
        return 0;
    }

    /**
     * データの削除.
     *
     * @return SQLiteDatabaseクラスの戻り値(削除されたレコード数)
     */
    public int delete() {
        return db.delete(DBConstants.CHANNEL_LIST_TABLE_NAME, null, null);
    }

}
