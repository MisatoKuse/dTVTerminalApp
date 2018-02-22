/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RecommendChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * RecommendChannelInsertDataManager.
 */
public class RecommendChannelInsertDataManager {

    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public RecommendChannelInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * VodClipAPIの解析結果をDBに格納する.
     * @param redChList レコメンドチャンネルリスト
     */
    public void insertRecommendChInsertList(final RecommendChList redChList) {

        //各種オブジェクト作成
        DBHelper recommendChListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(recommendChListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        RecommendChannelListDao redChListDao = new RecommendChannelListDao(database);
        List<Map<String, String>> hashMaps = redChList.getmRcList();

        //DB保存前に前回取得したデータは全消去する
        redChListDao.delete();

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
            redChListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }
}
