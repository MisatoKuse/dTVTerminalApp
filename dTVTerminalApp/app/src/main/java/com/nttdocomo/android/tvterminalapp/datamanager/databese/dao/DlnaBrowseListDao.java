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
 * チャンネルリストData Access Object.
 */
public class DlnaBrowseListDao {

    /**
     * SQLデータベースクラス.
     */
    private final SQLiteDatabase mSQLiteDatabase;

    /**
     * コンストラクタ.
     *
     * @param db SQLデータベースクラス
     */
    public DlnaBrowseListDao(final SQLiteDatabase db) {
        this.mSQLiteDatabase = db;
    }

    /**
     * 配列で指定した列データをすべて取得.
     *
     * @param strings 取得したいテーブル名の配列
     * @return チャンネルリスト情報.
     */
    public List<Map<String, String>> findByContainerId(final String[] strings, String containerId) {
        //特定IDのデータ取得はしない方針
        List<Map<String, String>> list = new ArrayList<>();
        String selection = null;
        if (containerId != null && !containerId.isEmpty()) {
            String[] selectionStrings = {
                    DataBaseConstants.DLNA_BROWSE_COLUM_CONTAINER_ID,
                    "=? "
            };
            selection = StringUtils.getConnectString(selectionStrings);
        }
        Cursor cursor = mSQLiteDatabase.query(
                DataBaseConstants.DLNA_BROWSE_TABLE_NAME,
                strings,
                selection,
                new  String[]{containerId},
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
        mSQLiteDatabase.close();
        return list;
    }

    /**
     * データの登録.
     *
     * @param values 格納する値
     * @return SQLiteDatabaseクラスの戻り値(正常終了した場合はROWID,失敗した場合,マイナス1)
     */
    public long insert(final ContentValues values) {
        return mSQLiteDatabase.insert(DataBaseConstants.DLNA_BROWSE_TABLE_NAME, null, values);
    }

    /**
     * データの削除by containerId.
     *
     * @param containerId containerId
     * @return 削除リターン
     */
    public int deleteByContainerId(final String containerId) {
        String deleteSelection = StringUtils.getConnectStrings(
                DataBaseConstants.DLNA_BROWSE_COLUM_CONTAINER_ID, "=? ");
        return mSQLiteDatabase.delete(DataBaseConstants.DLNA_BROWSE_TABLE_NAME, deleteSelection, new String[]{containerId});
    }

}
