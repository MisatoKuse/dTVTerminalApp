/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RoleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.util.List;

/**
 * RoleListDataManager.
 */
public class RoleListInsertDataManager {

    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public RoleListInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * ChannelAPIの解析結果をDBに格納する.
     * @param roleList ロールリスト
     */
    public void insertRoleList(final List<RoleListMetaData> roleList) {

        //有効期限判定
        if (!DateUtils.getLastDate(mContext, DateUtils.ROLELIST_LAST_UPDATE)) {
            return;
        }

        //各種オブジェクト作成
        DBHelper channelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(channelListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        RoleListDao roleListDao = new RoleListDao(database);

        //DB保存前に前回取得したデータは全消去する
        roleListDao.delete();

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < roleList.size(); i++) {
            RoleListMetaData roleData = roleList.get(i);
            ContentValues values = new ContentValues();
            values.put(JsonConstants.META_RESPONSE_CONTENTS_ID, roleData.getId());
            values.put(JsonConstants.META_RESPONSE_CONTENTS_NAME, roleData.getName());
            roleListDao.insert(values);
        }
        //データ保存日時を格納
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.ROLELIST_LAST_UPDATE);

        DataBaseManager.getInstance().closeDatabase();
    }
}