package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.DailyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VodClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WeeklyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.ChannelListDBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DailyRankListDBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.TvScheduleListDBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.VodClipListDBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.WeeklyRankListDBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.ChannelJsonParser.CHANNEL_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.DailyRankJsonParser.DAILYRANK_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.DailyRankJsonParser.DAILYRANK_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.DailyRankJsonParser.DAILYRANK_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.DailyRankJsonParser.DAILYRANK_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.TvScheduleJsonParser.TV_SCHEDULE_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.TvScheduleJsonParser.TV_SCHEDULE_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_TITLE;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class HomeDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public HomeDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * ホーム画面用CH一覧データを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectChannelListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {CHANNEL_LIST_THUMB, CHANNEL_LIST_TITLE,
                CHANNEL_LIST_DISPLAY_START_DATE, CHANNEL_LIST_DISP_TYPE};

        //Daoクラス使用準備
        ChannelListDBHelper channelListDBHelper = new ChannelListDBHelper(mContext);
        SQLiteDatabase db = channelListDBHelper.getWritableDatabase();
        ChannelListDao channelListDao = new ChannelListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = channelListDao.findById(columns);

        return list;
    }

    /**
     * ホーム画面用今日のランキング一覧データを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectDailyRankListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {DAILYRANK_LIST_THUMB, DAILYRANK_LIST_TITLE,
                DAILYRANK_LIST_DISPLAY_START_DATE, DAILYRANK_LIST_DISP_TYPE};

        //Daoクラス使用準備
        DailyRankListDBHelper dailyRankListDBHelper = new DailyRankListDBHelper(mContext);
        SQLiteDatabase db = dailyRankListDBHelper.getWritableDatabase();
        DailyRankListDao dailyRankListDao = new DailyRankListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = dailyRankListDao.findById(columns);

        return list;
    }

    /**
     * ホーム画面用CH毎番組表データを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectTvScheduleListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {TV_SCHEDULE_LIST_THUMB, TV_SCHEDULE_LIST_TITLE,
                TV_SCHEDULE_LIST_DISPLAY_START_DATE, TV_SCHEDULE_LIST_DISP_TYPE};

        //Daoクラス使用準備
        TvScheduleListDBHelper tvScheduleListDBHelper = new TvScheduleListDBHelper(mContext);
        SQLiteDatabase db = tvScheduleListDBHelper.getWritableDatabase();
        TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = tvScheduleListDao.findById(columns);

        return list;
    }

    /**
     * ホーム画面用クリップデータを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectClipHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {VODCLIP_LIST_THUMB, VODCLIP_LIST_TITLE,
                VODCLIP_LIST_DISPLAY_START_DATE, VODCLIP_LIST_DISP_TYPE};

        //Daoクラス使用準備
        VodClipListDBHelper vodClipListDBHelper = new VodClipListDBHelper(mContext);
        SQLiteDatabase db = vodClipListDBHelper.getWritableDatabase();
        VodClipListDao vodClipListDao = new VodClipListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = vodClipListDao.findById(columns);

        return list;
    }

    /**
     * ホーム画面用週間ランキングデータを返却する
     *
     * @return
     */
    public List<Map<String, String>> selectWeeklyRankListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {VODCLIP_LIST_THUMB, VODCLIP_LIST_TITLE,
                VODCLIP_LIST_DISPLAY_START_DATE, VODCLIP_LIST_DISP_TYPE};

        //Daoクラス使用準備
        WeeklyRankListDBHelper weeklyRankListDBHelper = new WeeklyRankListDBHelper(mContext);
        SQLiteDatabase db = weeklyRankListDBHelper.getWritableDatabase();
        WeeklyRankListDao weeklyRankListDao = new WeeklyRankListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = weeklyRankListDao.findById(columns);

        return list;
    }
}
