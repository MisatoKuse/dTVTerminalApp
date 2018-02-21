/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.DailyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RecommendChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RecommendVideoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RoleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VodClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WatchListenVideoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WeeklyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Home画面用DBManager.
 */
public class HomeDataManager {

    /**
     * コンテキスト.
     */
    private Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public HomeDataManager(final Context context) {
        this.mContext = context;
    }

    /**
     * ホーム画面用視聴中ビデオデータを返却する.
     *
     * @return list
     */
    public synchronized List<Map<String, String>> selectWatchingVideoHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.WATCH_LISTEN_VIDEO_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE,
                JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper DbHelper = new DBHelper(mContext);
        SQLiteDatabase database = DbHelper.getWritableDatabase();
        WatchListenVideoListDao watchListenVideoListDao = new WatchListenVideoListDao(database);

        //ホーム画面用データ取得
        list = watchListenVideoListDao.findById(columns);
        database.close();
        return list;
    }

    /**
     * ホーム画面用クリップ[テレビ]データを返却する.
     *
     * @return list
     */
    public synchronized List<Map<String, String>> selectTvClipHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.TVCLIP_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE,
                JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        TvClipListDao tvClipListDao = new TvClipListDao(database);

        //ホーム画面用データ取得
        list = tvClipListDao.findById(columns);
        database.close();
        return list;
    }

    /**
     * ホーム画面用クリップ[ビデオ]データを返却する.
     *
     * @return list
     */
    public synchronized List<Map<String, String>> selectVodClipHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.VODCLIP_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE,
                JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        VodClipListDao vodClipListDao = new VodClipListDao(database);

        //ホーム画面用データ取得
        list = vodClipListDao.findById(columns);
        database.close();
        return list;
    }

    /**
     * ホーム画面用CH一覧データを返却する.
     * TODO:NOW ON AIRでチャンネル名表示するために必要.ただしHOME画面に特化した物は不要.削除する事
     *
     * @return list
     */
    public synchronized List<Map<String, String>> selectChannelListHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.CHANNEL_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_CHNO, JsonConstants.META_RESPONSE_DEFAULT_THUMB,
                JsonConstants.META_RESPONSE_TITLE, JsonConstants.META_RESPONSE_AVAIL_START_DATE,
                JsonConstants.META_RESPONSE_AVAIL_END_DATE, JsonConstants.META_RESPONSE_DISP_TYPE,
                JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_CID};

        //Daoクラス使用準備
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ChannelListDao channelListDao = new ChannelListDao(database);

        //ホーム画面用データ取得
        list = channelListDao.findById(columns);
        database.close();
        return list;
    }

    /**
     * ホーム画面用おすすめ番組一覧データを返却する.
     *
     * @return list
     */
    public synchronized List<Map<String, String>> selectRecommendChListHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.RECOMMEND_CHANNEL_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_TITLE,
                RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_STARTVIEWING,
                RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CTPICURL1};

        //Daoクラス使用準備
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        RecommendChannelListDao recommendChannelListDao = new RecommendChannelListDao(database);

        //ホーム画面用データ取得
        list = recommendChannelListDao.findById(columns);
        database.close();
        return list;
    }

    /**
     * ホーム画面用おすすめビデオ一覧データを返却する.
     *
     * @return list
     */
    public synchronized List<Map<String, String>> selectRecommendVdListHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.RECOMMEND_VIDEO_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_TITLE,
                RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_STARTVIEWING,
                RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CTPICURL1};

        //Daoクラス使用準備
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        RecommendVideoListDao recommendVdListDao = new RecommendVideoListDao(database);

        //ホーム画面用データ取得
        list = recommendVdListDao.findById(columns);
        database.close();
        return list;
    }

    /**
     * ホーム画面用今日のランキング一覧データを返却する.
     *
     * @return list
     */
    public synchronized List<Map<String, String>> selectDailyRankListHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.DAILYRANK_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_DISPLAY_START_DATE, JsonConstants.META_RESPONSE_DISP_TYPE,
                JsonConstants.META_RESPONSE_SEARCH_OK, JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_R_VALUE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_CONTENT_TYPE,
                JsonConstants.META_RESPONSE_DTV, JsonConstants.META_RESPONSE_TV_SERVICE,
                JsonConstants.META_RESPONSE_DTV_TYPE, JsonConstants.META_RESPONSE_CID};

        //Daoクラス使用準備
        DBHelper DbHelper = new DBHelper(mContext);
        SQLiteDatabase database = DbHelper.getWritableDatabase();

        DailyRankListDao dailyRankListDao = new DailyRankListDao(database);

        //ホーム画面用データ取得
        list = dailyRankListDao.findById(columns);
        database.close();
        return list;
    }

    /**
     * ホーム画面用CH毎番組表データを返却する.
     *
     * @return list Now On Air データ
     */
    public synchronized List<Map<String, String>> selectTvScheduleListHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //DBデータ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.TV_SCHEDULE_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_CONTENT_TYPE,
                JsonConstants.META_RESPONSE_CID, JsonConstants.META_RESPONSE_SERVICE_ID};

        //Daoクラス使用準備
        DBHelper DbHelper = new DBHelper(mContext);
        SQLiteDatabase database = DbHelper.getWritableDatabase();

        TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);

        //ホーム画面用データ取得
        list = tvScheduleListDao.findById(columns);
        database.close();
        return list;
    }

    /**
     * ホーム画面用週間ランキングデータを返却する.
     *
     * @return list
     */
    public synchronized List<Map<String, String>> selectWeeklyRankListHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.WEEKLYRANK_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_DISPLAY_START_DATE, JsonConstants.META_RESPONSE_DISP_TYPE,
                JsonConstants.META_RESPONSE_SEARCH_OK, JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_R_VALUE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_CONTENT_TYPE,
                JsonConstants.META_RESPONSE_DTV, JsonConstants.META_RESPONSE_DTV_TYPE,
                JsonConstants.META_RESPONSE_TV_SERVICE, JsonConstants.META_RESPONSE_CID};

        //Daoクラス使用準備
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        WeeklyRankListDao weeklyRankListDao = new WeeklyRankListDao(database);

        //ホーム画面用データ取得
        list = weeklyRankListDao.findById(columns);
        database.close();
        return list;
    }

    /**
     * ロールリストデータを返却する.
     *
     * @return list ロールリスト
     */
    public synchronized List<Map<String, String>> selectRoleListData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.ROLE_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_CONTENTS_ID, JsonConstants.META_RESPONSE_CONTENTS_NAME};

        //Daoクラス使用準備
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        RoleListDao roleListDao = new RoleListDao(database);

        //ホーム画面用データ取得
        list = roleListDao.findById(columns);
        database.close();
        return list;
    }
}