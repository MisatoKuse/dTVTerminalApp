/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * チャンネルリストデータ管理.
 */
public class ChannelInsertDataManager {

    /**
     * コンテキスト.
     */
    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public ChannelInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * チャンネル一覧の情報をDBに格納する.
     * @param channelList  チャンネルリスト情報
     */
    public void insertChannelInsertList(final ChannelList channelList) {

        //有効期限判定
        if (!DateUtils.getLastDate(mContext, DateUtils.CHANNEL_LAST_UPDATE)) {
            return;
        }

        //各種オブジェクト作成
        DBHelper channelListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(channelListDBHelper);
        DataBaseManager dbm = DataBaseManager.getInstance();
        SQLiteDatabase database = dbm.openDatabase();
        ChannelListDao channelListDao = new ChannelListDao(database);
        List<HashMap<String, String>> hashMaps = channelList.getChannelList();

        //DB保存前に前回取得したデータは全消去する
        //TODO 日付とチャンネルを管理し、それらが一致するデータだけを消す事.またキャッシュ期限もその単位で管理する必要があるのでDB再設計が必要
        channelListDao.delete();

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < hashMaps.size(); i++) {
            Iterator entries = hashMaps.get(i).entrySet().iterator();

            ContentValues values = new ContentValues();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                if (JsonConstants.META_RESPONSE_AVAIL_START_DATE.equals(keyName)) {
                    values.put(DBConstants.UPDATE_DATE, !TextUtils.isEmpty(valName) ? valName.substring(0, 10) : "");
                }
                values.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            channelListDao.insert(values);
        }
        //データ保存日時を格納
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.CHANNEL_LAST_UPDATE);

        dbm.closeDatabase();
    }
}