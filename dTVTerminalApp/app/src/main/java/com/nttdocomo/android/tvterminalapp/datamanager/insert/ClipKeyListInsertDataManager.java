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
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;

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
    public void insertClipKeyListInsert(ClipKeyListDao.TABLE_TYPE type, ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        //各種オブジェクト作成
        DBHelper ClipKeyListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = ClipKeyListDBHelper.getWritableDatabase();
        ClipKeyListDao ClipKeyListDao = new ClipKeyListDao(db);
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
        DTVTLogger.end();
    }

    /**
     * クリップ登録成功後の行データ挿入.
     *
     * @param tableType テーブル種別(TV/VOD)
     * @param serviceId サービスID
     * @param eventId   イベントID
     * @param type      クリップ種別
     * @param titleId   タイトルID
     */
    public void insertRowSqlStart(final ClipKeyListDao.TABLE_TYPE tableType, final String serviceId,
                              final String eventId, final String type, final String titleId) {
        DTVTLogger.start();
        //各種オブジェクト作成
        DBHelper ClipKeyListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = ClipKeyListDBHelper.getWritableDatabase();
        ClipKeyListDao clipKeyListDao = new ClipKeyListDao(db);

        //コンテンツデータ作成
        ContentValues values = new ContentValues();
        values.put(JsonConstants.META_RESPONSE_SERVICE_ID, serviceId);
        values.put(JsonConstants.META_RESPONSE_EVENT_ID, eventId);
        values.put(JsonConstants.META_RESPONSE_TYPE, type);
        values.put(JsonConstants.META_RESPONSE_TITLE_ID, titleId);
        clipKeyListDao.insert(tableType, values);
        DTVTLogger.end();
    }

    /**
     * 条件に一致する行を削除する.
     *
     * @param tableType テーブル種別(TV/VOD)
     * @param serviceId サービスID
     * @param eventId   イベントID
     * @param titleId   タイトルID
     */
    public void deleteRowSqlStart(final ClipKeyListDao.TABLE_TYPE tableType, final String serviceId,
                                 final String eventId, final String titleId) {
        DTVTLogger.start();
        //各種オブジェクト作成
        DBHelper ClipKeyListDBHelper = new DBHelper(mContext);
        SQLiteDatabase db = ClipKeyListDBHelper.getWritableDatabase();
        ClipKeyListDao clipKeyListDao = new ClipKeyListDao(db);

        String query = StringUtil.getConnectStrings(JsonConstants.META_RESPONSE_CRID, "=? OR",
                JsonConstants.META_RESPONSE_SERVICE_ID, "=? AND",
                JsonConstants.META_RESPONSE_EVENT_ID, "=? AND",
                JsonConstants.META_RESPONSE_TYPE, "=h4d_iptv OR",
                JsonConstants.META_RESPONSE_TITLE_ID, "=?");

        String[] columns = {serviceId, eventId, titleId};
        clipKeyListDao.deleteRowData(tableType, query, columns);
    }
}
