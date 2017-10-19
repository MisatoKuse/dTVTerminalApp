/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ChannelListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.HomeDBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
     * @return
     */
    public void insertChannelInsertList(ChannelList channelList) {

        //各種オブジェクト作成
        HomeDBHelper channelListDBHelper = new HomeDBHelper(mContext);
        SQLiteDatabase db = channelListDBHelper.getWritableDatabase();
        ChannelListDao channelListDao = new ChannelListDao(db);
        List<HashMap<String,String>> hashMaps = channelList.getClList();

        //DB保存前に前回取得したデータは全消去する
        channelListDao.delete();

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < hashMaps.size(); i++) {
            Iterator entries = hashMaps.get(i).entrySet().iterator();
            ContentValues values = new ContentValues();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                values.put(DBUtils.fourKFlgConversion(keyName), valName);
            }
            channelListDao.insert(values);
        }
    }
}