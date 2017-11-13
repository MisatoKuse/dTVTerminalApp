/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.DATE_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants.UPDATE_DATE;

public class ChannelInsertDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public ChannelInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * ChannelAPIの解析結果をDBに格納する。
     *
     */
    public void insertChannelInsertList(ChannelList channelList, String display_type) {

        //各種オブジェクト作成
        DBHelper channelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(channelListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        ChannelListDao channelListDao = new ChannelListDao(database);
        List<HashMap<String,String>> hashMaps = channelList.getClList();

        //DB保存前に前回取得したデータは全消去する
        channelListDao.deleteByType(display_type);

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < hashMaps.size(); i++) {
            Iterator entries = hashMaps.get(i).entrySet().iterator();
            ContentValues values = new ContentValues();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                if(CHANNEL_LIST_AVAIL_START_DATE.equals(keyName)){
                    values.put(UPDATE_DATE, !TextUtils.isEmpty(valName)?valName.substring(0,10):"");
                }
                values.put(DBUtils.fourKFlgConversion(keyName), valName);
                values.put(DATE_TYPE, "program");
            }
            channelListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }
}