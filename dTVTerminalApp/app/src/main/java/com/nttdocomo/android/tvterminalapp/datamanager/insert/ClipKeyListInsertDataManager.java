/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.ClipKeyListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * クリップキー用InsertDataManager.
 */
public class ClipKeyListInsertDataManager {

    /**
     * コンテキストファイル.
     */
    private Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context コンテキストファイル
     */
    public ClipKeyListInsertDataManager(final Context context) {
        mContext = context;
    }

    /**
     * TvClipAPIの解析結果をDBに格納する.
     *
     * @param type                テーブル種別
     * @param clipKeyListResponse クリップキー一覧 レスポンスデータ
     */
    public synchronized void insertClipKeyListInsert(final ClipKeyListDao.TABLE_TYPE type, final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        //各種オブジェクト作成
        DBHelper DbHelper = new DBHelper(mContext);
        SQLiteDatabase database = DbHelper.getWritableDatabase();

        ClipKeyListDao ClipKeyListDao = new ClipKeyListDao(database);
        List<HashMap<String, String>> hashMaps = clipKeyListResponse.getCkList();

        //DB保存前に前回取得したデータを消去する
        try {
            ClipKeyListDao.delete(type);
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
            ClipKeyListDao.insert(type, values);
        }
        database.close();
        DTVTLogger.end();
    }

    /**
     * クリップ登録成功後の行データ挿入.
     *
     * @param tableType テーブル種別(TV/VOD)
     * @param crId      コンテンツ識別子
     * @param serviceId サービスID
     * @param eventId   イベントID
     * @param titleId   タイトルID
     */
    public void insertRowSqlStart(
            final ClipKeyListDao.TABLE_TYPE tableType, final String crId, final String serviceId,
            final String eventId, final String titleId) {
        DTVTLogger.start();
        //各種オブジェクト作成
        DBHelper ClipKeyListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(ClipKeyListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        ClipKeyListDao clipKeyListDao = new ClipKeyListDao(database);

        //コンテンツデータ作成
        ContentValues values = new ContentValues();
        values.put(JsonConstants.META_RESPONSE_CRID, crId);
        values.put(JsonConstants.META_RESPONSE_SERVICE_ID, serviceId);
        values.put(JsonConstants.META_RESPONSE_EVENT_ID, eventId);
        values.put(JsonConstants.META_RESPONSE_TYPE, ClipKeyListDataProvider.CLIP_KEY_LIST_TYPE_OTHER_CHANNEL);
        values.put(JsonConstants.META_RESPONSE_TITLE_ID, titleId);
        clipKeyListDao.insert(tableType, values);
        DataBaseManager.getInstance().closeDatabase();
        DTVTLogger.end();
    }

    /**
     * 条件に一致する行を削除する.
     *
     * @param tableType テーブル種別(TV/VOD)
     * @param crId      コンテンツ識別子
     * @param serviceId サービスID
     * @param eventId   イベントID
     * @param titleId   タイトルID
     */
    public void deleteRowSqlStart(
            final ClipKeyListDao.TABLE_TYPE tableType, final String crId, final String serviceId,
            final String eventId, final String titleId) {
        DTVTLogger.start();
        //各種オブジェクト作成
        DBHelper clipKeyListDBHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(clipKeyListDBHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        ClipKeyListDao clipKeyListDao = new ClipKeyListDao(database);

        String query = StringUtils.getConnectStrings(JsonConstants.META_RESPONSE_CRID, "=? OR ",
                JsonConstants.META_RESPONSE_SERVICE_ID, "=? AND ",
                JsonConstants.META_RESPONSE_EVENT_ID, "=? AND ",
                JsonConstants.META_RESPONSE_TYPE, "=? OR ",
                JsonConstants.META_RESPONSE_TITLE_ID, "=?");

        String[] columns = {crId, serviceId, eventId, ClipKeyListDataProvider.CLIP_KEY_LIST_TYPE_OTHER_CHANNEL, titleId};
        clipKeyListDao.deleteRowData(tableType, query, columns);
        DataBaseManager.getInstance().closeDatabase();
        DTVTLogger.end();
    }
}
