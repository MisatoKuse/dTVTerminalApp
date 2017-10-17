package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.DailyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DailyRankListDBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class DailyRankInsertDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public DailyRankInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     *  DailyRankAPIの解析結果をDBに格納する。
     *
     * @return
     */
    public void insertDailyRankInsertList( DailyRankList dailyRankList) {

        //各種オブジェクト作成
        DailyRankListDBHelper dailyRankListDBHelper = new DailyRankListDBHelper(mContext);
        SQLiteDatabase db = dailyRankListDBHelper.getWritableDatabase();
        DailyRankListDao dailyRankListDao = new DailyRankListDao(db);
        List<HashMap<String,String>> hashMaps = dailyRankList.getDrList();

        //DB保存前に前回取得したデータは全消去する
        dailyRankListDao.delete();

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
            dailyRankListDao.insert(values);
        }
    }
}