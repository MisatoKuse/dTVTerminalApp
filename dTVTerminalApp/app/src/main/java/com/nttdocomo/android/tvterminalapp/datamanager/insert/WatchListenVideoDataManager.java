/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.WatchListenVideoListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 視聴中ビデオ一覧情報をDB保存するクラス.
 */
public class WatchListenVideoDataManager {
    private Context mContext = null;

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキストファイル
     */
    public WatchListenVideoDataManager(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 取得した視聴中一覧情報をすべてDBに保存する.
     *
     * @param watchListenVideoList 視聴中ビデオ一覧情報
     */
    public void insertWatchListenVideoInsertList(final WatchListenVideoList watchListenVideoList) {

        //各種オブジェクト作成
        DBHelper watchListenVideoDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = watchListenVideoDBHelper.getWritableDatabase();
        WatchListenVideoListDao watchListenVideoListDao = new WatchListenVideoListDao(db);

        List<HashMap<String, String>> hashMaps = watchListenVideoList.getVcList();

        //DB保存前に前回取得したデータは全消去する
        watchListenVideoListDao.delete();

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
            watchListenVideoListDao.insert(values);
        }
        db.close();
    }
}