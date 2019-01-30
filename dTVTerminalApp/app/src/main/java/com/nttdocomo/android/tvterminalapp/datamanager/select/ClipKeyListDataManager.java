/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;

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
            final ClipKeyListDao.TableTypeEnum type, final String selection, final String[] args) {
//        DTVTLogger.start(); //必要な時にコメントを解除して使用

        //データ存在チェック
        List<Map<String, String>> list = new ArrayList<>();
        if (!DataBaseUtils.isCachingRecord(mContext, DataBaseUtils.getClipKeyTableName(type))) {
            return list;
        }

        //必要な列を列挙する
        String[] columns = {JsonConstants.META_RESPONSE_CRID,
                JsonConstants.META_RESPONSE_SERVICE_ID,
                JsonConstants.META_RESPONSE_EVENT_ID,
                JsonConstants.META_RESPONSE_TITLE_ID};

        //Daoクラス使用準備
        DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
        try (SQLiteDatabase database = dataBaseHelper.getWritableDatabase()) {
            ClipKeyListDao clipKeyListDao = new ClipKeyListDao(database);

            //データ取得
            list = clipKeyListDao.findById(columns, type, selection, args);
        } catch (SQLiteException e) {
            DTVTLogger.debug("ClipKeyListDataManager::selectClipKeyListData, e.cause=" + e.getCause());
        }
//        DTVTLogger.end();//必要な時にコメントを解除して使用
        return list;
    }

    /**
     * コンテンツの種類によってWHERE句を変える.
     *
     * @param contentType コンテンツタイプ
     * @return Where句
     */
    private String getSQLWhereString(final ClipKeyListDao.ContentTypeEnum contentType) {
        StringBuilder strBuilder = new StringBuilder();

        switch (contentType) {
            case CRID_REFERENCE:
                strBuilder.append(JsonConstants.META_RESPONSE_CRID)
                        .append(" = ? ");
                break;
            case SERVICE_ID_AND_EVENT_ID_REFERENCE:
                strBuilder.append(JsonConstants.META_RESPONSE_SERVICE_ID)
                        .append(" = ? AND ")
                        .append(JsonConstants.META_RESPONSE_EVENT_ID)
                        .append(" = ? ");
                break;
            case TITLE_ID_REFERENCE:
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
     * @return クリップキーリスト
     */
    public List<Map<String, String>> selectClipKeyDbTvData(
            final ClipKeyListDao.TableTypeEnum tableType, final String serviceId,
            final String eventId) {
        String[] args = {serviceId, eventId};
        String selection = getSQLWhereString(ClipKeyListDao.ContentTypeEnum.SERVICE_ID_AND_EVENT_ID_REFERENCE);
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
            final ClipKeyListDao.TableTypeEnum tableType, final String titleId) {
        String[] args = {titleId};
        String selection = getSQLWhereString(ClipKeyListDao.ContentTypeEnum.TITLE_ID_REFERENCE);

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
            final ClipKeyListDao.TableTypeEnum tableType, final String crid) {
        String[] args = {crid};
        String selection = getSQLWhereString(ClipKeyListDao.ContentTypeEnum.CRID_REFERENCE);
        return selectClipKeyListData(tableType, selection, args);
    }

    /**
     * 引数指定のクリップキーテーブルから一覧を取得.
     *
     * @param tableType テーブル種別
     * @return クリップキーリスト
     */
    private List<Map<String, String>> selectListData(
            final ClipKeyListDao.TableTypeEnum tableType) {
        return selectClipKeyListData(tableType, null, null);
    }

    /**
     * 全てのクリップキーリストを取得.
     *
     * @return クリップキーリスト
     */
    public List<Map<String, String>> selectClipAllList() {
        DTVTLogger.start();
        List<Map<String, String>> list = selectListData(ClipKeyListDao.TableTypeEnum.TV);
        list.addAll(selectListData(ClipKeyListDao.TableTypeEnum.VOD));
        DTVTLogger.end();
        return list;
    }
}
