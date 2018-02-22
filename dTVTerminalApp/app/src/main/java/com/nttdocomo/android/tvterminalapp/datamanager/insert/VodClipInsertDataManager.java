/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VodClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * VodClipInsertDataManager.
 */
 public class VodClipInsertDataManager {

    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
   public VodClipInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * VodClipAPIの解析結果をDBに格納する.
     *
     * @param vodClipList Vodクリップリスト
     */
    public void insertVodClipInsertList(final VodClipList vodClipList) {

        //各種オブジェクト作成
        DBHelper vodClipListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(vodClipListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        VodClipListDao vodClipListDao = new VodClipListDao(database);
        @SuppressWarnings("unchecked")
        List<HashMap<String, String>> hashMaps = vodClipList.getVcList();

        //DB保存前に前回取得したデータは全消去する
        vodClipListDao.delete();

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < hashMaps.size(); i++) {
            Iterator entries = hashMaps.get(i).entrySet().iterator();
            ContentValues values = new ContentValues();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                values.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            vodClipListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }
}
