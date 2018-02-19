/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.VideoRankListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VideoRankInsertDataManager {

    final private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public VideoRankInsertDataManager(Context context) {
        mContext = context;
    }

    /**
     * WeeklyRankAPIの解析結果をDBに格納する。
     */
    public void insertVideoRankInsertList(VideoRankList videoRankList) {

        //各種オブジェクト作成
        DBHelper videoRankListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = videoRankListDBHelper.getWritableDatabase();
        VideoRankListDao weeklyRankListDao = new VideoRankListDao(db);
        List<HashMap<String, String>> hashMaps = videoRankList.getVrList();

        //DB保存前に前回取得したデータは全消去する
        weeklyRankListDao.delete();

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
            weeklyRankListDao.insert(values);
        }
        db.close();
    }
}