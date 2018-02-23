/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RecommendVideoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVdList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * RecommendVideoInsertDataManager.
 */
public class RecommendVideoInsertDataManager {

    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public RecommendVideoInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * VodClipAPIの解析結果をDBに格納する.
     *
     * @param redVdList レコメンドビデオリスト
     */
    public void insertRecommendVdInsertList(final RecommendVdList redVdList) {

        //各種オブジェクト作成
        DBHelper redVdListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(redVdListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        RecommendVideoListDao redVdListDao = new RecommendVideoListDao(database);
        List<Map<String, String>> hashMaps = redVdList.getmRvList();

        //DB保存前に前回取得したデータは全消去する
        redVdListDao.delete();

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < hashMaps.size(); i++) {
            Iterator entries = hashMaps.get(i).entrySet().iterator();
            ContentValues values = new ContentValues();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                values.put(keyName, valName);
            }
            redVdListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }
}