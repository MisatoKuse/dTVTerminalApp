/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvScheduleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DataBaseManager;

import java.util.List;
import java.util.Map;

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
        String[] columns = {JsonContents.META_RESPONSE_CHNO, JsonContents.META_RESPONSE_DEFAULT_THUMB, JsonContents.META_RESPONSE_TITLE,
                JsonContents.META_RESPONSE_AVAIL_START_DATE, JsonContents.META_RESPONSE_AVAIL_END_DATE,
                JsonContents.META_RESPONSE_DISP_TYPE,JsonContents.META_RESPONSE_SERVICE_ID};

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
        String[] columns = {JsonContents.META_RESPONSE_THUMB_448, JsonContents.META_RESPONSE_TITLE,
                JsonContents.META_RESPONSE_AVAIL_START_DATE, JsonContents.META_RESPONSE_AVAIL_END_DATE,
                JsonContents.META_RESPONSE_CHNO, JsonContents.META_RESPONSE_DISP_TYPE,
                JsonContents.META_RESPONSE_SEARCH_OK, JsonContents.META_RESPONSE_CRID,
                JsonContents.META_RESPONSE_SERVICE_ID, JsonContents.META_RESPONSE_EVENT_ID,
                JsonContents.META_RESPONSE_TITLE_ID, JsonContents.META_RESPONSE_R_VALUE,
                JsonContents.META_RESPONSE_CONTENT_TYPE, JsonContents.META_RESPONSE_DTV,
                JsonContents.META_RESPONSE_TV_SERVICE};

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
