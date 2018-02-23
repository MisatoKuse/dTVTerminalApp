/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * クリップキーリスト用DataManager.
 */
public class ClipKeyListDataManager {
    /**
     * コンテキスト.
     */
    private final Context mContext;


    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public ClipKeyListDataManager(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * クリップキー一覧を返却する.
     *
     * @param type      テーブルの種類
     * @param selection WHERE句
     * @param args param値(serviceId,eventId,type)
     * @return クリップキーリスト
     */
    private synchronized List<Map<String, String>> selectClipKeyListData(
            final ClipKeyListDao.TABLE_TYPE type, final String selection, final String[] args) {
        DTVTLogger.start();

        //データ存在チェック
        List<Map<String, String>> list = new ArrayList<>();
        if (!DBUtils.isCachingRecord(mContext, DBUtils.getClipKeyTableName(type))) {
            return list;
        }

        //必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID,
                JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TYPE,
                JsonConstants.META_RESPONSE_TITLE_ID};

        //Daoクラス使用準備
        DBHelper dbHelper = new DBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ClipKeyListDao clipKeyListDao = new ClipKeyListDao(database);

        //データ取得
        list = clipKeyListDao.findById(columns, type, selection, args);
        database.close();
        DTVTLogger.end();
        return list;
    }

    /**
     * コンテンツの種類によってWHERE句を変える.
     *
     * @param contentType コンテンツタイプ
     * @return Where句
     */
    private String getSQLWhereStr(final ClipKeyListDao.CONTENT_TYPE contentType) {
        StringBuilder strBuilder = new StringBuilder();

        switch (contentType) {
            case VOD:
                strBuilder.append(JsonConstants.META_RESPONSE_CRID)
                        .append(" = ? ");
                break;
            case TV:
                strBuilder.append(JsonConstants.META_RESPONSE_SERVICE_ID)
                        .append(" = ? AND ")
                        .append(JsonConstants.META_RESPONSE_EVENT_ID)
                        .append(" = ? AND ")
                        .append(JsonConstants.META_RESPONSE_TYPE)
                        .append(" = ? ");
                break;
            case DTV:
                strBuilder.append(JsonConstants.META_RESPONSE_TITLE_ID)
                        .append(" = ? ");
                break;
        }
        return strBuilder.toString();
    }

    /**
     * TVコンテンツのクリップキーをID指定取得.
     *
     * @param tableType テーブル種別
     * @param serviceId serviceId
     * @param eventId eventId
     * @param type type
     * @return クリップキーリスト
     */
    public List<Map<String, String>> selectClipKeyDbTvData(
            final ClipKeyListDao.TABLE_TYPE tableType, final String serviceId,
            final String eventId, final String type) {
        String[] args = {serviceId, eventId, type};
        String selection = getSQLWhereStr(ClipKeyListDao.CONTENT_TYPE.TV);
        return selectClipKeyListData(tableType, selection, args);
    }

    /**
     * dTVコンテンツのクリップキーをID指定取得.
     *
     * @param tableType テーブル種別
     * @param titleId titleId
     * @return クリップキーリスト
     */
    public List<Map<String, String>> selectClipKeyDbDtvData(
            final ClipKeyListDao.TABLE_TYPE tableType, final String titleId) {
        String[] args = {titleId};
        String selection = getSQLWhereStr(ClipKeyListDao.CONTENT_TYPE.DTV);

        return selectClipKeyListData(tableType, selection, args);
    }

    /**
     * VODコンテンツのクリップキーをID指定取得.
     *
     * @param tableType テーブル種別
     * @param crid crid
     * @return クリップキーリスト
     */
    public List<Map<String, String>> selectClipKeyDbVodData(
            final ClipKeyListDao.TABLE_TYPE tableType, final String crid) {
        String[] args = {crid};
        String selection = getSQLWhereStr(ClipKeyListDao.CONTENT_TYPE.VOD);
        return selectClipKeyListData(tableType, selection, args);
    }

    /**
     * 引数指定のクリップキーテーブルから一覧を取得.
     *
     * @param tableType テーブル種別
     * @return クリップキーリスト
     */
    public List<Map<String, String>> selectListData(
            final ClipKeyListDao.TABLE_TYPE tableType) {
        return selectClipKeyListData(tableType, null, null);
    }
}
