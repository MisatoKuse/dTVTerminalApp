/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.UserInfoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ユーザー情報DataManager.
 */

public class UserInfoDataManager {
    /**
     *
     * コンテキスト.
     */
    private final  Context mContext;

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public UserInfoDataManager(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * ユーザ情報取得.
     *
     * @return ユーザ年齢情報
     */
    public synchronized List<Map<String, String>> selectUserAgeInfo() {

        //データ存在チェック
        List<Map<String, String>> list = new ArrayList<>();
        if (!DBUtils.isCachingRecord(mContext, DBConstants.USER_INFO_LIST_TABLE_NAME)) {
            return list;
        }

        //ユーザ情報取得に必要な列を列挙する
        String[] columns = {UserInfoJsonParser.USER_INFO_LIST_LOGGEDIN_ACCOUNT,
                UserInfoJsonParser.USER_INFO_LIST_LOGGEDIN_ACCOUNT,
                UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS,
                UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ,
                UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ};

        //Daoクラス使用準備
        DBHelper homeDBHelper = new DBHelper(mContext);
        SQLiteDatabase database = homeDBHelper.getWritableDatabase();
        UserInfoListDao userInfoListDao = new UserInfoListDao(database);

        //ホーム画面用データ取得
        list = userInfoListDao.findById(columns);
        database.close();

        return list;
    }
}
