package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VodClipListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.VodClipListDBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class VodClipInsertDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public VodClipInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * VodClipAPIの解析結果をDBに格納する。
     *
     * @return
     */
    public void insertVodClipInsertList(VodClipList vodClipList) {

        //各種オブジェクト作成
        VodClipListDBHelper vodClipListDBHelper = new VodClipListDBHelper(mContext);
        SQLiteDatabase db = vodClipListDBHelper.getWritableDatabase();
        VodClipListDao vodClipListDao = new VodClipListDao(db);
        List<HashMap<String,String>> hashMaps = vodClipList.getVcList();

        //DB保存前に前回取得したデータは全消去する
        vodClipListDao.delete();

        //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
        for (int i = 0; i < hashMaps.size(); i++) {
            Iterator entries = hashMaps.get(i).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                vodClipListDao.insert(keyName, valName);
            }
        }
    }
}
