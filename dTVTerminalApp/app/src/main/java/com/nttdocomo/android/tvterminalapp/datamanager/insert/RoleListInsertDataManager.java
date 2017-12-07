/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RoleListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATE_TYPE;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UPDATE_DATE;

public class RoleListInsertDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public RoleListInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * ChannelAPIの解析結果をDBに格納する。
     *
     */
    public void insertRoleList(List<RoleListMetaData> roleList) {

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
            values.put(JsonContents.META_RESPONSE_CONTENTS_ID, roleData.getId());
            values.put(JsonContents.META_RESPONSE_CONTENTS_NAME, roleData.getName());
            roleListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }
}