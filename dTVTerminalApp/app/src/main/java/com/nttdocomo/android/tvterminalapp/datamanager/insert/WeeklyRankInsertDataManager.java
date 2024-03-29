/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WeeklyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
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

        //取得データが空の場合は更新しないで、有効期限をクリアする
        if (weeklyRankList == null || weeklyRankList.getWeeklyRankList() == null
                || weeklyRankList.getWeeklyRankList().size() < 1) {
            DateUtils.clearLastProgramDate(mContext, DateUtils.WEEKLY_RANK_LAST_INSERT);
            return;
        } else {
            //HashMapが空の時も有効期限をクリアして何もしない
            HashMap<String, String> hashMap = (HashMap<String, String>) weeklyRankList.getWeeklyRankList().get(0);
            if (hashMap.isEmpty()) {
                DateUtils.clearLastProgramDate(mContext, DateUtils.WEEKLY_RANK_LAST_INSERT);
                return;
            }
        }

        //各種オブジェクト作成
        DataBaseHelper weeklyRankListDataBaseHelper = new DataBaseHelper(mContext);
        DataBaseManager.initializeInstance(weeklyRankListDataBaseHelper);
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        synchronized (dataBaseManager) {
            try {
                SQLiteDatabase database = dataBaseManager.openDatabase();
                database.acquireReference();
                WeeklyRankListDao weeklyRankListDao = new WeeklyRankListDao(database);
                @SuppressWarnings("unchecked")
                List<HashMap<String, String>> hashMaps = weeklyRankList.getWeeklyRankList();

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
                        values.put(DataBaseUtils.fourKFlgConversion(keyName), valName);
                    }
                    weeklyRankListDao.insert(values);
                }
                //DB保存日時格納
                DateUtils dateUtils = new DateUtils(mContext);
                dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
            } catch (SQLiteException e) {
                DTVTLogger.debug("WeeklyRankInsertDataManager::insertWeeklyRankInsertList, e.cause=" + e.getCause());
            } finally {
                DataBaseManager.getInstance().closeDatabase();
            }
        }
    }
}