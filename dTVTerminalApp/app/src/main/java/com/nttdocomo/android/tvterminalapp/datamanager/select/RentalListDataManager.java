/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RentalListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DataBaseManager;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 購入済みコンテンツ情報を取得するDataManager.
 */
public class RentalListDataManager {

    /**
     * コンテキスト.
     */
    private Context mContext;

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public RentalListDataManager(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * レンタル一覧画面用クリップデータを返却する.
     *
     * @return クリップデータリスト
     */
    public List<Map<String, String>> selectRentalListData() {

        //データ存在チェック
        List<Map<String, String>> list = new ArrayList<>();
        if (!DBUtils.isCachingRecord(mContext, DBConstants.RENTAL_LIST_TABLE_NAME)) {
            return list;
        }

        //ホーム画面に必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_THUMB_448, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_PUBLISH_END_DATE, JsonConstants.META_RESPONSE_DISP_TYPE,
                JsonConstants.META_RESPONSE_SEARCH_OK, JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_R_VALUE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_DTV, JsonConstants.META_RESPONSE_TV_SERVICE,
                JsonConstants.META_RESPONSE_CONTENT_TYPE, JsonConstants.META_RESPONSE_DTV_TYPE,
                JsonConstants.META_RESPONSE_RATING, JsonConstants.META_RESPONSE_EPISODE_ID,
                JsonConstants.META_RESPONSE_EST_FLAG, JsonConstants.META_RESPONSE_CID
        };

        //Daoクラス使用準備
        DBHelper homeDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(homeDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        RentalListDao rentalListDao = new RentalListDao(database);

        //ホーム画面用データ取得
        list = rentalListDao.findById(columns);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }

    /**
     * レンタル一覧のactive_listデータを返却する.
     *
     * @return 購入済みCH一覧データ
     */
    public List<Map<String, String>> selectRentalActiveListData() {
        //必要な列を列挙する
        String[] activeColumns = {JsonConstants.META_RESPONSE_ACTIVE_LIST + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_LICENSE_ID,
                JsonConstants.META_RESPONSE_ACTIVE_LIST + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_VAILD_END_DATE
        };

        //Daoクラス使用準備
        DBHelper homeDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(homeDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        RentalListDao rentalListDao = new RentalListDao(database);

        //データ取得
        List<Map<String, String>> list = rentalListDao.activeListFindById(activeColumns);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }

    /**
     * 購入済みCH一覧データを返却する.
     *
     * @return 購入済みCH一覧データ
     */
    public List<Map<String, String>> selectRentalChListData() {
        //必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_CHNO, JsonConstants.META_RESPONSE_DEFAULT_THUMB, JsonConstants.META_RESPONSE_TITLE,
                JsonConstants.META_RESPONSE_AVAIL_START_DATE, JsonConstants.META_RESPONSE_AVAIL_END_DATE,
                JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_SERVICE_ID,
                JsonConstants.META_RESPONSE_CH_TYPE, JsonConstants.META_RESPONSE_PUID, JsonConstants.META_RESPONSE_SUB_PUID
        };

        //Daoクラス使用準備
        DBHelper dbHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(dbHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        RentalListDao rentalListDao = new RentalListDao(database);

        //データ取得
        List<Map<String, String>> list = rentalListDao.chFindById(columns);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }

    /**
     * 購入済みCH一覧のactive_listデータを返却する.
     *
     * @return 購入済みCH一覧データ
     */
    public List<Map<String, String>> selectRentalChActiveListData() {
        //必要な列を列挙する
        String[] activeColumns = {JsonConstants.META_RESPONSE_ACTIVE_LIST + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_LICENSE_ID,
                JsonConstants.META_RESPONSE_ACTIVE_LIST + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_VAILD_END_DATE
        };

        //Daoクラス使用準備
        DBHelper homeDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(homeDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        RentalListDao rentalListDao = new RentalListDao(database);

        //データ取得
        List<Map<String, String>> list = rentalListDao.chActiveListFindById(activeColumns);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }
}
