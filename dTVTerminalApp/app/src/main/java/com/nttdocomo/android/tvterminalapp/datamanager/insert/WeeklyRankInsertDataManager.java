/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WeeklyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.WEEKLY_RANK_LAST_INSERT;

/**
 * WeeklyRankInsertDataManager.
 */
public class WeeklyRankInsertDataManager {

    /**
     * コンテキスト.
     */
    final private Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public WeeklyRankInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * WeeklyRankAPIの解析結果をDBに格納する.
     * @param weeklyRankList  週間ランキングリスト
     */
    public void insertWeeklyRankInsertList(final WeeklyRankList weeklyRankList) {

        //有効期限判定
        if (!DateUtils.getLastDate(mContext, DateUtils.WEEKLY_RANK_LAST_INSERT)) {
            return;
        }

        //各種オブジェクト作成
        DBHelper weeklyRankListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(weeklyRankListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        WeeklyRankListDao weeklyRankListDao = new WeeklyRankListDao(database);
        @SuppressWarnings("unchecked")
        List<HashMap<String, String>> hashMaps = weeklyRankList.getWrList();

        //DB保存前に前回取得したデータは全消去する
        weeklyRankListDao.delete();

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
            weeklyRankListDao.insert(values);
        }
        //DB保存日時格納
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);

        DataBaseManager.getInstance().closeDatabase();
    }
}