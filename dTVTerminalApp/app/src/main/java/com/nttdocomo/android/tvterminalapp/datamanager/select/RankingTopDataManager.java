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
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VideoRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DataBaseManager;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ランキングトップデータマネージャー.
 *
 */
public class RankingTopDataManager {
    /**
     *コンテキスト.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public RankingTopDataManager(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * ランキングトップ画面用週間ランキングデータを返却する.
     *
     * @return ランキングデータ
     */
    public List<Map<String, String>> selectVideoRankListData() {

        List<Map<String, String>> list = new ArrayList<>();

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_DTV_THUMB_448, JsonConstants.META_RESPONSE_DTV_THUMB_640,
                JsonConstants.META_RESPONSE_DISPLAY_START_DATE, JsonConstants.META_RESPONSE_DISP_TYPE,
                JsonConstants.META_RESPONSE_RATING, JsonConstants.META_RESPONSE_CONTENT_TYPE,
                JsonConstants.META_RESPONSE_SEARCH_OK, JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_R_VALUE,
                JsonConstants.META_RESPONSE_DTV, JsonConstants.META_RESPONSE_TV_SERVICE,
                JsonConstants.META_RESPONSE_DTV_TYPE, JsonConstants.META_RESPONSE_CID,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_THUMB_640,
                JsonConstants.META_RESPONSE_AVAIL_END_DATE, JsonConstants.META_RESPONSE_DUR,
                JsonConstants.META_RESPONSE_CHNO, JsonConstants.META_RESPONSE_PUBLISH_START_DATE,
                JsonConstants.META_RESPONSE_PUBLISH_END_DATE};

        //Daoクラス使用準備
        DataBaseHelper videoRankListDataBaseHelper = new DataBaseHelper(mContext);
        DataBaseManager.initializeInstance(videoRankListDataBaseHelper);
        DataBaseManager databaseManager = DataBaseManager.getInstance();
        synchronized (databaseManager) {
            try {
                //Daoクラス使用準備
                SQLiteDatabase database = databaseManager.openDatabase();
                database.acquireReference();

                //データ存在チェック
                if (!DataBaseUtils.isCachingRecord(database, DataBaseConstants.RANKING_VIDEO_LIST_TABLE_NAME)) {
                    DataBaseManager.getInstance().closeDatabase();
                    return list;
                }

                VideoRankListDao videoRankListDao = new VideoRankListDao(database);

                //ビデオランクデータ取得
                list = videoRankListDao.findById(columns);
            } catch (SQLiteException e) {
                DTVTLogger.debug("RankingTopDataManager::selectVideoRankListData, e.cause=" + e.getCause());
            } finally {
                DataBaseManager.getInstance().closeDatabase();
            }
        }
        return list;
    }
}
