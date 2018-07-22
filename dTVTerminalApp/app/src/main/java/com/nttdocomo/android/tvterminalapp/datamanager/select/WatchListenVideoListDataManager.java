/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WatchListenVideoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DataBaseManager;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 視聴中ビデオ一覧用DataManager.
 */
public class WatchListenVideoListDataManager {

    /**
     * コンテキストファイル.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public WatchListenVideoListDataManager(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 視聴中ビデオ一覧データを返却する.
     *
     * @return 視聴中ビデオ一覧データ
     */
    public synchronized List<Map<String, String>> selectWatchListenVideoData() {

        List<Map<String, String>> list = new ArrayList<>();

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_DTV_THUMB_448, JsonConstants.META_RESPONSE_DTV_THUMB_640,
                JsonConstants.META_RESPONSE_DISPLAY_START_DATE, JsonConstants.META_RESPONSE_RATING,
                JsonConstants.META_RESPONSE_SEARCH_OK, JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_R_VALUE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_CONTENT_TYPE,
                JsonConstants.META_RESPONSE_DTV, JsonConstants.META_RESPONSE_TV_SERVICE,
                JsonConstants.META_RESPONSE_DTV_TYPE, JsonConstants.META_RESPONSE_THUMB_640,
                JsonConstants.META_RESPONSE_PUBLISH_START_DATE, JsonConstants.META_RESPONSE_PUBLISH_END_DATE,
                JsonConstants.META_RESPONSE_CID, JsonConstants.META_RESPONSE_CHNO,
                JsonConstants.META_RESPONSE_DUR};

        //Daoクラス使用準備
        DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
        DataBaseManager.initializeInstance(dataBaseHelper);
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        synchronized (dataBaseManager) {
            try {
                SQLiteDatabase database = dataBaseManager.openDatabase();
                database.acquireReference();

                //データ存在チェック
                if (!DataBaseUtils.isCachingRecord(database, DataBaseConstants.WATCH_LISTEN_VIDEO_TABLE_NAME)) {
                    DataBaseManager.getInstance().closeDatabase();
                    return list;
                }

                WatchListenVideoListDao watchListenVideoListDao = new WatchListenVideoListDao(database);

                //ホーム画面用データ取得
                list = watchListenVideoListDao.findById(columns);
            } catch (SQLiteException e) {
                DTVTLogger.debug("WatchListenVideoListDataManager::selectWatchListenVideoData, e.cause=" + e.getCause());
            } finally {
                DataBaseManager.getInstance().closeDatabase();
            }
        }
        return list;
    }
}
