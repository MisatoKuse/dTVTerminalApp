/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.DailyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RecommendChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RecommendVideolListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RoleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VodClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WeeklyRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_TITLE;


public class HomeDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public HomeDataManager(Context context) {
        this.mContext = context;
    }

    /**
     * ホーム画面用クリップデータを返却する
     *
     * @return list
     */
    public List<Map<String, String>> selectClipHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_THUMB_448, JsonContents.META_RESPONSE_TITLE,
                JsonContents.META_RESPONSE_DISPLAY_START_DATE, JsonContents.META_RESPONSE_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper vodClipListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = vodClipListDBHelper.getWritableDatabase();
        VodClipListDao vodClipListDao = new VodClipListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = vodClipListDao.findById(columns);
        db.close();
        vodClipListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用CH一覧データを返却する
     *
     * @return list
     */
    public List<Map<String, String>> selectChannelListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_CHNO, JsonContents.META_RESPONSE_DEFAULT_THUMB,
                JsonContents.META_RESPONSE_TITLE, JsonContents.META_RESPONSE_AVAIL_START_DATE,
                JsonContents.META_RESPONSE_AVAIL_END_DATE, JsonContents.META_RESPONSE_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper channelListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = channelListDBHelper.getWritableDatabase();
        ChannelListDao channelListDao = new ChannelListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = channelListDao.findById(columns);
        db.close();
        channelListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用おすすめ番組一覧データを返却する
     *
     * @return list
     */
    public List<Map<String, String>> selectRecommendChListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {RECOMMENDVIDEO_LIST_TITLE, RECOMMENDVIDEO_LIST_CTPICURL1};

        //Daoクラス使用準備
        DBHelper recommendChListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = recommendChListDBHelper.getWritableDatabase();
        RecommendChannelListDao recommendChannelListDao = new RecommendChannelListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = recommendChannelListDao.findById(columns);
        db.close();
        recommendChListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用おすすめビデオ一覧データを返却する
     *
     * @return list
     */
    public List<Map<String, String>> selectRecommendVdListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {RECOMMENDVIDEO_LIST_TITLE, RECOMMENDVIDEO_LIST_CTPICURL1};

        //Daoクラス使用準備
        DBHelper recommendVdListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = recommendVdListDBHelper.getWritableDatabase();
        RecommendVideolListDao recommendVdListDao = new RecommendVideolListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = recommendVdListDao.findById(columns);
        db.close();
        recommendVdListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用今日のランキング一覧データを返却する
     *
     * @return list
     */
    public List<Map<String, String>> selectDailyRankListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_THUMB_448, JsonContents.META_RESPONSE_TITLE,
                JsonContents.META_RESPONSE_DISPLAY_START_DATE, JsonContents.META_RESPONSE_DISP_TYPE,
                JsonContents.META_RESPONSE_SEARCH_OK, JsonContents.META_RESPONSE_TYPE,
                JsonContents.META_RESPONSE_CRID, JsonContents.META_RESPONSE_SERVICE_ID,
                JsonContents.META_RESPONSE_EVENT_ID, JsonContents.META_RESPONSE_TITLE_ID,
                JsonContents.META_RESPONSE_R_VALUE, JsonContents.META_RESPONSE_AVAIL_START_DATE,
                JsonContents.META_RESPONSE_AVAIL_END_DATE, JsonContents.META_RESPONSE_DTV};

        //Daoクラス使用準備
        DBHelper dailyRankListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = dailyRankListDBHelper.getWritableDatabase();
        DailyRankListDao dailyRankListDao = new DailyRankListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = dailyRankListDao.findById(columns);
        db.close();
        dailyRankListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用CH毎番組表データを返却する
     *
     * @return list
     */
    public List<Map<String, String>> selectTvScheduleListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_THUMB_448, JsonContents.META_RESPONSE_TITLE,
                JsonContents.META_RESPONSE_AVAIL_START_DATE, JsonContents.META_RESPONSE_AVAIL_END_DATE};

        //Daoクラス使用準備
        DBHelper tvScheduleListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = tvScheduleListDBHelper.getWritableDatabase();
        TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = tvScheduleListDao.findById(columns);
        db.close();
        tvScheduleListDBHelper.close();
        return list;
    }

    /**
     * ホーム画面用週間ランキングデータを返却する
     *
     * @return list
     */
    public List<Map<String, String>> selectWeeklyRankListHomeData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_THUMB_448, JsonContents.META_RESPONSE_TITLE,
                JsonContents.META_RESPONSE_AVAIL_START_DATE, JsonContents.META_RESPONSE_SEARCH_OK,
                JsonContents.META_RESPONSE_TYPE, JsonContents.META_RESPONSE_CRID,
                JsonContents.META_RESPONSE_SERVICE_ID, JsonContents.META_RESPONSE_EVENT_ID,
                JsonContents.META_RESPONSE_TITLE_ID, JsonContents.META_RESPONSE_R_VALUE,
                JsonContents.META_RESPONSE_AVAIL_START_DATE, JsonContents.META_RESPONSE_AVAIL_END_DATE,
                JsonContents.META_RESPONSE_DTV};

        //Daoクラス使用準備
        DBHelper weeklyRankListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = weeklyRankListDBHelper.getWritableDatabase();
        WeeklyRankListDao weeklyRankListDao = new WeeklyRankListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = weeklyRankListDao.findById(columns);
        db.close();
        weeklyRankListDBHelper.close();
        return list;
    }

    /**
     * ロールリストデータを返却する
     *
     * @return list ロールリスト
     */
    public List<Map<String, String>> selectRoleListData() {
        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_CONTENTS_ID, JsonContents.META_RESPONSE_CONTENTS_NAME};

        //Daoクラス使用準備
        DBHelper roleListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = roleListDBHelper.getWritableDatabase();
        RoleListDao roleListDao = new RoleListDao(db);

        //ホーム画面用データ取得
        List<Map<String, String>> list = roleListDao.findById(columns);
        db.close();
        roleListDBHelper.close();
        return list;
    }
}
