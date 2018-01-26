/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VideoRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankingTopDataManager {
    private Context mContext;

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
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.RANKING_VIDEO_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_DISPLAY_START_DATE, JsonConstants.META_RESPONSE_DISP_TYPE,
                JsonConstants.META_RESPONSE_RATING, JsonConstants.META_RESPONSE_CONTENT_TYPE,
                JsonConstants.META_RESPONSE_SEARCH_OK, JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_R_VALUE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_DTV, JsonConstants.META_RESPONSE_TV_SERVICE,
                JsonConstants.META_RESPONSE_DTV_TYPE};

        //Daoクラス使用準備
        DBHelper videoRankListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = videoRankListDBHelper.getWritableDatabase();
        VideoRankListDao videoRankListDao = new VideoRankListDao(db);

        //ビデオランクデータ取得
        list = videoRankListDao.findById(columns);
        db.close();
        videoRankListDBHelper.close();
        return list;
    }
}
