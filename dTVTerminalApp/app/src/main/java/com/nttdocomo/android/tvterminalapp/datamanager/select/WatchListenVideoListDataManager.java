/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WatchListenVideoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.List;
import java.util.Map;

/**
 * 視聴中ビデオ一覧用DataManager.
 */
public class WatchListenVideoListDataManager {

    /**
     * コンテキストファイル.
     */
    private Context mContext;

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public WatchListenVideoListDataManager(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * クリップ一覧画面用クリップデータを返却する.
     *
     * @return クリップ一覧画面用クリップデータ
     */
    public List<Map<String, String>> selectWatchListenVideoData() {

        //データ存在チェック
        List<Map<String, String>> list;
        if (!DBUtils.isCachingRecord(mContext, DBConstants.VODCLIP_LIST_TABLE_NAME)) {
            return null;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_DISPLAY_START_DATE, JsonConstants.META_RESPONSE_RATING,
                JsonConstants.META_RESPONSE_SEARCH_OK, JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_R_VALUE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_CONTENT_TYPE,
                JsonConstants.META_RESPONSE_DTV, JsonConstants.META_RESPONSE_TV_SERVICE,
                JsonConstants.META_RESPONSE_DTV_TYPE};

        //Daoクラス使用準備
        DBHelper homeDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = homeDBHelper.getWritableDatabase();
        WatchListenVideoListDao watchListenVideoListDao = new WatchListenVideoListDao(db);

        //ホーム画面用データ取得
        list = watchListenVideoListDao.findById(columns);
        db.close();
        homeDBHelper.close();
        return list;
    }
}
