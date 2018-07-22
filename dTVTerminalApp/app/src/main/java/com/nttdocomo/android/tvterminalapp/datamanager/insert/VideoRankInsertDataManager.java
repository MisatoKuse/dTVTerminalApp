/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VideoRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VIDEO_RANK_LAST_INSERT;

/**
 * ビデオランキングInsertDataManager.
 */
public class VideoRankInsertDataManager {
    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public VideoRankInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * WeeklyRankAPIの解析結果をDBに格納する.
     * @param videoRankList ビデオランキングリスト
     */
    public void insertVideoRankInsertList(final VideoRankList videoRankList) {
        //取得データが空の場合は更新しないで、有効期限をクリアする
        if (videoRankList == null || videoRankList.getVrList() == null
                || videoRankList.getVrList().size() < 1) {
            DateUtils.clearLastProgramDate(mContext, DateUtils.VIDEO_RANK_LAST_INSERT);
            return;
        } else {
            //HashMapが空の時も有効期限をクリアして何もしない
            HashMap<String, String> hashMap = (HashMap<String, String>) videoRankList.getVrList().get(0);
            if (hashMap.isEmpty()) {
                DateUtils.clearLastProgramDate(mContext, DateUtils.VIDEO_RANK_LAST_INSERT);
                return;
            }
        }

        //各種オブジェクト作成
        DataBaseHelper videoRankListDataBaseHelper = new DataBaseHelper(mContext);
        DataBaseManager.initializeInstance(videoRankListDataBaseHelper);
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        synchronized (dataBaseManager) {
            try {
                SQLiteDatabase database = dataBaseManager.openDatabase();
                database.acquireReference();
                VideoRankListDao videoRankListDao = new VideoRankListDao(database);
                @SuppressWarnings("unchecked")
                List<HashMap<String, String>> hashMaps = videoRankList.getVrList();

                //DB保存前に前回取得したデータは全消去する
                videoRankListDao.delete();

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
                    videoRankListDao.insert(values);
                }
                //DB保存日時格納
                DateUtils dateUtils = new DateUtils(mContext);
                dateUtils.addLastDate(VIDEO_RANK_LAST_INSERT);
            } catch (SQLiteException e) {
                DTVTLogger.debug("VideoRankInsertDataManager::insertVideoRankInsertList, e.cause=" + e.getCause());
            } finally {
                DataBaseManager.getInstance().closeDatabase();
            }
        }
    }
}