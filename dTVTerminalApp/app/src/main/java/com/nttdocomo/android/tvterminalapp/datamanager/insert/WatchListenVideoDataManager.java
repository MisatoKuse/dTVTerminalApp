/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WatchListenVideoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoList;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 視聴中ビデオ一覧情報をDB保存するクラス.
 */
public class WatchListenVideoDataManager {

    /**
     * コンテキスト.
     */
    private Context mContext = null;

    /**
     * コンストラクタ.
     *
     * @param mContext Activity
     */
    public WatchListenVideoDataManager(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 取得した視聴中一覧情報をすべてDBに保存する.
     *
     * @param watchListenVideoList 視聴中ビデオ一覧情報
     */
    public void insertWatchListenVideoInsertList(final WatchListenVideoList watchListenVideoList) {

        //取得データが空の場合は更新しないで、有効期限をクリアする
        if (watchListenVideoList == null || watchListenVideoList.getVcList() == null
                || watchListenVideoList.getVcList().size() < 1) {
            DateUtils.clearLastProgramDate(mContext, DateUtils.WATCHING_VIDEO_LIST_LAST_INSERT);
            return;
        } else {
            //HashMapが空の時も有効期限をクリアして何もしない
            HashMap<String, String> hashMap = (HashMap<String, String>) watchListenVideoList.getVcList().get(0);
            if (hashMap.isEmpty()) {
                DateUtils.clearLastProgramDate(mContext, DateUtils.WATCHING_VIDEO_LIST_LAST_INSERT);
                return;
            }
        }

        try {
            //各種オブジェクト作成
            DataBaseHelper watchListenVideoDataBaseHelper = new DataBaseHelper(mContext);
            DataBaseManager.initializeInstance(watchListenVideoDataBaseHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();
            WatchListenVideoListDao watchListenVideoListDao = new WatchListenVideoListDao(database);
            @SuppressWarnings("unchecked")
            List<HashMap<String, String>> hashMaps = watchListenVideoList.getVcList();

            //DB保存前に前回取得したデータは全消去する
            watchListenVideoListDao.delete();

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
                watchListenVideoListDao.insert(values);
            }
            //データ保存日時を格納
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastDate(DateUtils.WATCHING_VIDEO_LIST_LAST_INSERT);
        } catch (SQLiteException e) {
            DTVTLogger.debug("WatchListenVideoDataManager::insertWatchListenVideoInsertList, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }
    }
}