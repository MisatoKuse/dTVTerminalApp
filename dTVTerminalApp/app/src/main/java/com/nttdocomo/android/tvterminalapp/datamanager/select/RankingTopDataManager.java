/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VideoRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WeeklyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser.VIDEORANK_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser.VIDEORANK_LIST_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser.VIDEORANK_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser.VIDEORANK_LIST_TITLE;

public class RankingTopDataManager {
    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public RankingTopDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * ランキングトップ画面用週間ランキングデータを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectVideoRankListData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {VIDEORANK_LIST_THUMB, VIDEORANK_LIST_TITLE,
                VIDEORANK_LIST_START_DATE, VIDEORANK_LIST_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper videoRankListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = videoRankListDBHelper.getWritableDatabase();
        VideoRankListDao videoRankListDao = new VideoRankListDao(db);

        //ビデオランクデータ取得
        List<Map<String, String>> list = videoRankListDao.findById(columns);
        db.close();
        videoRankListDBHelper.close();
        return list;
    }
}
