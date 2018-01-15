/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.select;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;

import java.util.List;
import java.util.Map;

public class ClipKeyListDataManager {

    private Context mContext;


    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public ClipKeyListDataManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * クリップキー一覧を返却する
     *
     * @param type      テーブルの種類
     * @param selection WHERE句
     * @param args
     * @return
     */
    public List<Map<String, String>> selectClipKeyListData(ClipKeyListDao.TABLE_TYPE type, String selection, String[] args) {
        DTVTLogger.start();
        //必要な列を列挙する
        String[] columns = {JsonContents.META_RESPONSE_CRID,
                JsonContents.META_RESPONSE_SERVICE_ID,
                JsonContents.META_RESPONSE_EVENT_ID,
                JsonContents.META_RESPONSE_TYPE,
                JsonContents.META_RESPONSE_TITLE_ID};

        //Daoクラス使用準備
        DBHelper dBHelper = new DBHelper(mContext);
        SQLiteDatabase db = dBHelper.getWritableDatabase();
        ClipKeyListDao clipKeyListDao = new ClipKeyListDao(db);

        //データ取得
        List<Map<String, String>> list = clipKeyListDao.findById(columns, type, selection, args);
        db.close();
        dBHelper.close();

        DTVTLogger.end();
        return list;
    }

    /**
     * コンテンツの種類によってWHERE句を変える
     *
     * @param contentType
     * @return
     */
    private String getSQLWhereStr(ClipKeyListDao.CONTENT_TYPE contentType) {
        StringBuilder strBuilder = new StringBuilder();

        switch (contentType) {
            case VOD:
                strBuilder.append(JsonContents.META_RESPONSE_CRID)
                        .append(" = ? ");
                break;
            case TV:
                strBuilder.append(JsonContents.META_RESPONSE_SERVICE_ID)
                        .append(" = ? ,")
                        .append(JsonContents.META_RESPONSE_EVENT_ID)
                        .append(" = ? ,")
                        .append(JsonContents.META_RESPONSE_TYPE)
                        .append(" = ? ");
                break;
            case DTV:
                strBuilder.append(JsonContents.META_RESPONSE_TITLE_ID)
                        .append(" = ? ");
                break;
        }
        return strBuilder.toString();
    }

    /**
     * TVコンテンツのクリップキーをID指定取得
     *
     * @param tableType
     * @param serviceId
     * @param eventId
     * @param type
     * @return
     */
    public List<Map<String, String>> selectClipKeyDbTvData(ClipKeyListDao.TABLE_TYPE tableType, String serviceId, String eventId, String type) {
        String[] args = {serviceId, eventId, type};
        String selection = getSQLWhereStr(ClipKeyListDao.CONTENT_TYPE.TV);
        return selectClipKeyListData(tableType, selection, args);
    }

    /**
     * dTVコンテンツのクリップキーをID指定取得
     *
     * @param tableType
     * @param titleId
     * @return
     */
    public List<Map<String, String>> selectClipKeyDbDtvData(ClipKeyListDao.TABLE_TYPE tableType, String titleId) {
        String[] args = {titleId};
        String selection = getSQLWhereStr(ClipKeyListDao.CONTENT_TYPE.DTV);

        return selectClipKeyListData(tableType, selection, args);
    }

    /**
     * VODコンテンツのクリップキーをID指定取得
     *
     * @param tableType
     * @param crid
     * @return
     */
    public List<Map<String, String>> selectClipKeyDbVodData(ClipKeyListDao.TABLE_TYPE tableType, String crid) {
        String[] args = {crid};
        String selection = getSQLWhereStr(ClipKeyListDao.CONTENT_TYPE.VOD);
        return selectClipKeyListData(tableType, selection, args);
    }

    /**
     * 引数指定のクリップキーテーブルから一覧を取得
     *
     * @param tableType
     * @return
     */
    public List<Map<String, String>> selectListData(ClipKeyListDao.TABLE_TYPE tableType) {
        return selectClipKeyListData(tableType, null, null);
    }
}
