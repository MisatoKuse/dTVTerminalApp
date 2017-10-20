/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WeeklyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.HomeDBHelper;

import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_TITLE;

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
    public List<Map<String, String>> selectWeeklyRankListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {VODCLIP_LIST_THUMB, VODCLIP_LIST_TITLE,
                VODCLIP_LIST_DISPLAY_START_DATE, VODCLIP_LIST_DISP_TYPE};

        //Daoクラス使用準備
        HomeDBHelper weeklyRankListDBHelper = new HomeDBHelper(mContext);
        SQLiteDatabase db = weeklyRankListDBHelper.getWritableDatabase();
        WeeklyRankListDao weeklyRankListDao = new WeeklyRankListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = weeklyRankListDao.findById(columns);
        db.close();
        weeklyRankListDBHelper.close();
        return list;
    }
}
