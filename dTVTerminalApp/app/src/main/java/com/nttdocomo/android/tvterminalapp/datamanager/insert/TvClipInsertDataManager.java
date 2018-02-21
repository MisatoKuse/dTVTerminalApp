/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.TvClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TvClipInsertDataManager {

    /*
    public boolean insertTvClipList(){
        return false;
    }
    */

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public TvClipInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * TvClipAPIの解析結果をDBに格納する。
     *
     * @return
     */
    public void insertTvClipInsertList(TvClipList tvClipList) {

        //各種オブジェクト作成
        DBHelper tvClipListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(tvClipListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        TvClipListDao tvClipListDao = new TvClipListDao(database);
        List<HashMap<String, String>> hashMaps = tvClipList.getVcList();

        //DB保存前に前回取得したデータは全消去する
        try {
            tvClipListDao.delete();
        } catch (Exception e) {
            DTVTLogger.debug("TvClipInsertDataManager::insertTvClipInsertList, e.cause=" + e.getCause());
        }

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
            tvClipListDao.insert(values);
        }
        DataBaseManager.getInstance().closeDatabase();
    }
}
