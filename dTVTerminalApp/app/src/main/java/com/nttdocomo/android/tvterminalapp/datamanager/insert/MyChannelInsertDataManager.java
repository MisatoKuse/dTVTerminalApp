/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.MyChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyChannelInsertDataManager {

    private static final String SERVICE_ID_MY_CHANNEL_LIST = "service_id";
    private static final String TITLE_MY_CHANNEL_LIST = "title";
    private static final String RVALUE_MY_CHANNEL_LIST = "r_value";
    private static final String ADULT_TYPE_MY_CHANNEL_LIST = "adult_type";
    private static final String INDEX_MY_CHANNEL_LIST = "index_no";
    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public MyChannelInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * MyChannelAPIの解析結果をDBに格納する。
     */
    public void insertIntoMyChannelList(ArrayList<MyChannelMetaData> mMyChannelMetaDataList) {
        //各種オブジェクト作成
        DBHelper myChannelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(myChannelListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        MyChannelListDao myChannelListDao = new MyChannelListDao(database);
        //DB保存前に前回取得したデータは全消去する
        myChannelListDao.deleteAll();
        for (int i = 0; i < mMyChannelMetaDataList.size(); i++) {
            ContentValues values = new ContentValues();
            MyChannelMetaData myChannelMetaData = mMyChannelMetaDataList.get(i);
            values.put(SERVICE_ID_MY_CHANNEL_LIST,myChannelMetaData.getServiceId());
            values.put(TITLE_MY_CHANNEL_LIST,myChannelMetaData.getTitle());
            values.put(RVALUE_MY_CHANNEL_LIST,myChannelMetaData.getRValue());
            values.put(ADULT_TYPE_MY_CHANNEL_LIST,myChannelMetaData.getAdultType());
            values.put(INDEX_MY_CHANNEL_LIST,myChannelMetaData.getIndex());
            myChannelListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }

    public List<Map<String, String>> selectFromMyChannelList(){
        //各種オブジェクト作成
        DBHelper myChannelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(myChannelListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        MyChannelListDao myChannelListDao = new MyChannelListDao(database);
        //My番組表に必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_SERVICE_ID, JsonContents.META_RESPONSE_TITLE,
                JsonContents.META_RESPONSE_R_VALUE, JsonContents.META_RESPONSE_ADULT_TYPE,
                JsonContents.META_RESPONSE_INDEX};
        //My番組表画面用データ取得
        List<Map<String, String>> list = myChannelListDao.findById(columns);
        DataBaseManager.getInstance().closeDatabase();
        return list;
    }
}