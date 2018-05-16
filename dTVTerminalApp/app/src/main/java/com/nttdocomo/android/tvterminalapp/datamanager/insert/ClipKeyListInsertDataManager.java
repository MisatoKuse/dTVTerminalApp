/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.ClipKeyListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
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
     * コンテキスト.
     */
    private final  Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
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
    public synchronized void insertClipKeyListInsert(final ClipKeyListDao.TableTypeEnum type, final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        try {
            //各種オブジェクト作成
            DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
            DataBaseManager.initializeInstance(dataBaseHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();

            ClipKeyListDao clipKeyListDao = new ClipKeyListDao(database);
            @SuppressWarnings("unchecked")
            List<HashMap<String, String>> hashMaps = clipKeyListResponse.getCkList();

            //DB保存前に前回取得したデータを消去する
            try {
                clipKeyListDao.delete(type);
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
                    values.put(DataBaseUtils.fourKFlgConversion(keyName), valName);
                }
                clipKeyListDao.insert(type, values);
            }
        } catch (SQLiteException e) {
            DTVTLogger.debug("ClipKeyListInsertDataManager::insertClipKeyListInsert, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }
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
            final ClipKeyListDao.TableTypeEnum tableType, final String crId, final String serviceId,
            final String eventId, final String titleId) {
        DTVTLogger.start();
        try {
            //各種オブジェクト作成
            DataBaseHelper clipKeyListDataBaseHelper = new DataBaseHelper(mContext);
            DataBaseManager.initializeInstance(clipKeyListDataBaseHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();
            ClipKeyListDao clipKeyListDao = new ClipKeyListDao(database);

            //コンテンツデータ作成
            ContentValues values = new ContentValues();
            values.put(JsonConstants.META_RESPONSE_CRID, crId);
            values.put(JsonConstants.META_RESPONSE_SERVICE_ID, serviceId);
            values.put(JsonConstants.META_RESPONSE_EVENT_ID, eventId);
            values.put(JsonConstants.META_RESPONSE_TYPE, ClipKeyListDataProvider.CLIP_KEY_LIST_TYPE_OTHER_CHANNEL);
            values.put(JsonConstants.META_RESPONSE_TITLE_ID, titleId);
            clipKeyListDao.insert(tableType, values);
        } catch (SQLiteException e) {
            DTVTLogger.debug("ClipKeyListInsertDataManager::insertRowSqlStart, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }
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
            final ClipKeyListDao.TableTypeEnum tableType, final String crId, final String serviceId,
            final String eventId, final String titleId) {
        DTVTLogger.start();
        try {
            //各種オブジェクト作成
            DataBaseHelper clipKeyListDataBaseHelper = new DataBaseHelper(mContext);
            DataBaseManager.initializeInstance(clipKeyListDataBaseHelper);
            SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
            database.acquireReference();
            ClipKeyListDao clipKeyListDao = new ClipKeyListDao(database);

            String query = StringUtils.getConnectStrings(JsonConstants.META_RESPONSE_CRID, "=? OR ",
                    JsonConstants.META_RESPONSE_SERVICE_ID, "=? AND ",
                    JsonConstants.META_RESPONSE_EVENT_ID, "=? AND ",
                    JsonConstants.META_RESPONSE_TYPE, "=? OR ",
                    JsonConstants.META_RESPONSE_TITLE_ID, "=?");

            String[] columns = {crId, serviceId, eventId, ClipKeyListDataProvider.CLIP_KEY_LIST_TYPE_OTHER_CHANNEL, titleId};
            clipKeyListDao.deleteRowData(tableType, query, columns);
        } catch (SQLiteException e) {
            DTVTLogger.debug("ClipKeyListInsertDataManager::deleteRowSqlStart, e.cause=" + e.getCause());
        } finally {
            DataBaseManager.getInstance().closeDatabase();
        }
        DTVTLogger.end();
    }
}
