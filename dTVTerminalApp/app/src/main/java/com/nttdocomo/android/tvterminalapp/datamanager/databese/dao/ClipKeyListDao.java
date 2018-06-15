/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * クリップキーリストData Access Object.
 */
public class ClipKeyListDao {

    /**
     * SQLデータベースクラス.
     */
    private final SQLiteDatabase mSQLiteDatabase;

    /**
     * SQLデータベースクラス.
     */
    public static final String META_DISPLAY_TYPE_TV_PROGRAM = "tv_program";
    /**
     * dtvフラグ値(FALSE=DTVコンテンツではない).
     */
    public static final String META_DTV_FLAG_FALSE = "0";

    /**
     * コンテンツの種類.それぞれでキーになる情報が異なる.
     */
    public enum ContentTypeEnum {
        /** 多チャンネル放送. */
        TV,
        /** ひかりTVビデオ、dTVチャンネル. */
        VOD,
        /** DTV. */
        DTV
    }

    /**
     * テーブルの種類.それぞれWebAPIコールして別々に取得している.
     */
    public enum TableTypeEnum {
        /** EPGコンテンツ. */
        TV,
        /** VODコンテンツ. */
        VOD
    }

    /**
     * コンストラクタ.
     *
     * @param db  SQLデータベースクラス
     */
    public ClipKeyListDao(final SQLiteDatabase db) {
        this.mSQLiteDatabase = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 列パラメータ
     * @param type テーブルの種類(TV or VOD)
     * @param selection WHERE句
     * @param args ?パラメータに埋め込む値
     * @return list クリップキー一覧
     */
    public List<Map<String, String>> findById(final String[] strings, final TableTypeEnum type, final String selection, final String[] args) {
        DTVTLogger.start();
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor;
        try {
            cursor = mSQLiteDatabase.query(
                    DataBaseUtils.getClipKeyTableName(type),
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
     * @param type 格納する値
     * @param values 格納する値
     * @return SQLiteDatabaseクラスの戻り値(正常終了した場合はROWID,失敗した場合,マイナス1)
     */
    public long insert(final TableTypeEnum type, final ContentValues values) {
        DTVTLogger.debug("Insert Data");
        return mSQLiteDatabase.insert(DataBaseUtils.getClipKeyTableName(type), null, values);
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
     * @param type テーブルの種類(TV or VOD)
     * @return SQLiteDatabaseクラスの戻り値(削除されたレコード数)
     */
    public int delete(final TableTypeEnum type) {
        DTVTLogger.debug("Delete Data : " + type);
        return mSQLiteDatabase.delete(DataBaseUtils.getClipKeyTableName(type), null, null);
    }

    /**
     * データの削除.
     */
    @SuppressWarnings("unused")
    public void delete() {
        DTVTLogger.debug("Delete All Data");
        delete(TableTypeEnum.TV);
        delete(TableTypeEnum.VOD);
    }

    /**
     * 条件に一致する行を削除する.
     *
     * @param tableType テーブル種別(TV/VOD)
     * @param query クエリ
     * @param columns   対象列名
     * @return SQLiteDatabaseクラスの戻り値(削除されたレコード数)
     */
    public int deleteRowData(final TableTypeEnum tableType, final String query, final String[] columns) {
        DTVTLogger.debug("Delete Row : " + tableType);
        return mSQLiteDatabase.delete(DataBaseUtils.getClipKeyTableName(tableType), query, columns);
    }
}
