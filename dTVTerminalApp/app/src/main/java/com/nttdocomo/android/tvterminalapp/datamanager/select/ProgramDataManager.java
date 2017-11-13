/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DataBaseManager;

import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_CHNO;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_CHNO;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_LINEAR_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_LINEAR_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE;


public class ProgramDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param mContext コンテスト
     */
    public ProgramDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * ホーム画面用CH一覧データを返却する
     *
     * @return list チャンネルデータ
     */
    public List<Map<String, String>> selectChannelListProgramData(String displayType) {
        //ホーム画面に必要な列を列挙する
        String[] columns = {CHANNEL_LIST_CHNO, CHANNEL_LIST_THUMB, CHANNEL_LIST_TITLE,
                CHANNEL_LIST_AVAIL_START_DATE, CHANNEL_LIST_AVAIL_END_DATE,
                CHANNEL_LIST_DISP_TYPE};

        //Daoクラス使用準備
        DBHelper channelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(channelListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        ChannelListDao channelListDao = new ChannelListDao(database);

        //ホーム画面用データ取得
        List<Map<String, String>> list = channelListDao.findByTypeAndDate(columns, displayType);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }

    /**
     * ホーム画面用CH毎番組表データを返却する
     *
     * @return list 番組データ
     */
    public List<Map<String, String>> selectTvScheduleListProgramData(String display_type, String update) {
        //ホーム画面に必要な列を列挙する
        String[] columns = {TV_SCHEDULE_LIST_THUMB, TV_SCHEDULE_LIST_TITLE,
                TV_SCHEDULE_LIST_LINEAR_START_DATE, TV_SCHEDULE_LIST_LINEAR_END_DATE,
                TV_SCHEDULE_LIST_CHNO};

        //Daoクラス使用準備
        DBHelper channelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(channelListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        TvScheduleListDao tvScheduleListDao = new TvScheduleListDao(database);

        //ホーム画面用データ取得
        List<Map<String, String>> list = tvScheduleListDao.findByTypeAndDate(columns, display_type, update);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }
}
