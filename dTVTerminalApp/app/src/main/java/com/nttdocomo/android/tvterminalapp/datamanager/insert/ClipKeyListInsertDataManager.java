/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClipKeyListInsertDataManager {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public ClipKeyListInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * TvClipAPIの解析結果をDBに格納する。
     *
     * @param clipKeyListResponse クリップキー一覧 レスポンスデータ
     */
    public void insertClipKeyListInsert(ClipKeyListResponse clipKeyListResponse) {

        //各種オブジェクト作成
        DBHelper ClipKeyListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = ClipKeyListDBHelper.getWritableDatabase();
        ClipKeyListDao ClipKeyListDao = new ClipKeyListDao(db);
        List<HashMap<String, String>> hashMaps = clipKeyListResponse.getCkList();

        //DB保存前に前回取得したデータは全消去する
        try {
            ClipKeyListDao.delete();
        } catch (Exception e) {
            DTVTLogger.debug("ClipKeyListInsertDataManager::insertClipKeyListInsert, e.cause=" + e.getCause());
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
            ClipKeyListDao.insert(values);
        }
    }
}
