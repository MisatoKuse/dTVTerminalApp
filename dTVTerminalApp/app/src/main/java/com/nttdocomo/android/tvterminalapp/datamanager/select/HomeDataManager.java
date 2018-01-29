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
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RecommendVideolListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RoleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VodClipListDao;
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
     * ホーム画面用クリップ[テレビ]データを返却する.
     *
     * @return list
     */
    public List<Map<String, String>> selectTvClipHomeData() {

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
        DBHelper tvClipListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = tvClipListDBHelper.getWritableDatabase();
        TvClipListDao tvClipListDao = new TvClipListDao(db);

        //ホーム画面用データ取得
        list = tvClipListDao.findById(columns);
        db.close();
        tvClipListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用クリップ[ビデオ]データを返却する.
     *
     * @return list
     */
    public List<Map<String, String>> selectVodClipHomeData() {

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
        DBHelper vodClipListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = vodClipListDBHelper.getWritableDatabase();
        VodClipListDao vodClipListDao = new VodClipListDao(db);

        //ホーム画面用データ取得
        list = vodClipListDao.findById(columns);
        db.close();
        vodClipListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用CH一覧データを返却する.
     * TODO:NOW ON AIRでチャンネル名表示するために必要.ただしHOME画面に特化した物は不要.削除する事
     *
     * @return list
     */
    public List<Map<String, String>> selectChannelListHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.CHANNEL_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_CHNO, JsonConstants.META_RESPONSE_DEFAULT_THUMB,
                JsonConstants.META_RESPONSE_TITLE, JsonConstants.META_RESPONSE_AVAIL_START_DATE,
                JsonConstants.META_RESPONSE_AVAIL_END_DATE, JsonConstants.META_RESPONSE_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper channelListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = channelListDBHelper.getWritableDatabase();
        ChannelListDao channelListDao = new ChannelListDao(db);

        //ホーム画面用データ取得
        list = channelListDao.findById(columns);
        db.close();
        channelListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用おすすめ番組一覧データを返却する.
     *
     * @return list
     */
    public List<Map<String, String>> selectRecommendChListHomeData() {

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
        DBHelper recommendChListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = recommendChListDBHelper.getWritableDatabase();
        RecommendChannelListDao recommendChannelListDao = new RecommendChannelListDao(db);

        //ホーム画面用データ取得
        list = recommendChannelListDao.findById(columns);
        db.close();
        recommendChListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用おすすめビデオ一覧データを返却する.
     *
     * @return list
     */
    public List<Map<String, String>> selectRecommendVdListHomeData() {

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
        DBHelper recommendVdListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = recommendVdListDBHelper.getWritableDatabase();
        RecommendVideolListDao recommendVdListDao = new RecommendVideolListDao(db);

        //ホーム画面用データ取得
        list = recommendVdListDao.findById(columns);
        db.close();
        recommendVdListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用今日のランキング一覧データを返却する.
     *
     * @return list
     */
    public List<Map<String, String>> selectDailyRankListHomeData() {

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
                JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_CONTENT_TYPE,
                JsonConstants.META_RESPONSE_DTV, JsonConstants.META_RESPONSE_TV_SERVICE,
                JsonConstants.META_RESPONSE_DTV_TYPE};

        //Daoクラス使用準備
        DBHelper dailyRankListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = dailyRankListDBHelper.getWritableDatabase();
        DailyRankListDao dailyRankListDao = new DailyRankListDao(db);

        //ホーム画面用データ取得
        list = dailyRankListDao.findById(columns);
        db.close();
        dailyRankListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用CH毎番組表データを返却する.
     *
     * @return list Now On Air データ
     */
    public List<Map<String, String>> selectTvScheduleListHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //DBデータ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.TV_SCHEDULE_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE};

        //Daoクラス使用準備
        DBHelper tvScheduleListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = tvScheduleListDBHelper.getWritableDatabase();
        TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(db);

        //ホーム画面用データ取得
        list = tvScheduleListDao.findById(columns);
        db.close();
        tvScheduleListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用週間ランキングデータを返却する.
     *
     * @return list
     */
    public List<Map<String, String>> selectWeeklyRankListHomeData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.WEEKLYRANK_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_DISP_TYPE,
                JsonConstants.META_RESPONSE_SEARCH_OK, JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_R_VALUE,
                JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_CONTENT_TYPE,
                JsonConstants.META_RESPONSE_DTV, JsonConstants.META_RESPONSE_DTV_TYPE,
                JsonConstants.META_RESPONSE_TV_SERVICE};

        //Daoクラス使用準備
        DBHelper weeklyRankListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = weeklyRankListDBHelper.getWritableDatabase();
        WeeklyRankListDao weeklyRankListDao = new WeeklyRankListDao(db);

        //ホーム画面用データ取得
        list = weeklyRankListDao.findById(columns);
        db.close();
        weeklyRankListDBHelper.close();
        return list;
    }

    /**
     * ロールリストデータを返却する.
     *
     * @return list ロールリスト
     */
    public List<Map<String, String>> selectRoleListData() {

        List<Map<String, String>> list = new ArrayList<>();
        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBConstants.ROLE_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_CONTENTS_ID, JsonConstants.META_RESPONSE_CONTENTS_NAME};

        //Daoクラス使用準備
        DBHelper roleListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = roleListDBHelper.getWritableDatabase();
        RoleListDao roleListDao = new RoleListDao(db);

        //ホーム画面用データ取得
        list = roleListDao.findById(columns);
        db.close();
        roleListDBHelper.close();
        return list;
    }
}