/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RecommendListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DataBaseHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChannelList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendWebXmlParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * レコメンドリストDataManager.
 */
public class RecommendListDataManager {
    /**
     * コンテキスト.
     */

    private final Context mContext;

    /**
     * コンストラクタ.
     *
     * @param context Activity
     */
    public RecommendListDataManager(final Context context) {
        mContext = context;

    }

    /**
     * VodClipAPIの解析結果をDBに格納する.
     * @param redChList レコメンド(TV)データ
     * @param addFlag ページングフラグ
     * @param tagPageNo ページ番号
     * @param cacheDateKey キャッシュ対象ごとのキー
     */
    @SuppressWarnings("OverlyLongMethod")
    public synchronized void insertRecommendInsertList(
            final RecommendChannelList redChList, final boolean addFlag, final int tagPageNo,
            final String cacheDateKey) {

        //取得データが空の場合は更新しないで、有効期限をクリアする
        if (redChList == null || redChList.getmRcList().size() < 1
                || redChList.getmRcList().get(0).isEmpty()) {
            DateUtils.clearLastProgramDate(mContext, cacheDateKey);
            return;
        }

        DataBaseHelper deHelper = new DataBaseHelper(mContext);
        DataBaseManager.initializeInstance(deHelper);
        DataBaseManager databaseManager = DataBaseManager.getInstance();

        synchronized (databaseManager) {
            try {
                SQLiteDatabase database = databaseManager.openDatabase();
                //各種オブジェクト作成
                List<Map<String, String>> hashMaps = redChList.getmRcList();
                database.acquireReference();
                RecommendListDao redListDao = new RecommendListDao(database);

                //DB保存前に前回取得したデータは全消去する
                if (!addFlag) { // ページングの場合、削除しない
                    switch (tagPageNo) {
                        case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_TV:
                        case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_VIDEO:
                        case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV:
                        case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO:
                        case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL:
                        case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV:
                        case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME:
                            redListDao.delete(tagPageNo);
                            break;
                        default:
                            break;
                    }
                }

                //HashMapの要素とキーを一行ずつ取り出し、DBに格納する
                for (int i = 0; i < hashMaps.size(); i++) {
                    Iterator entries = hashMaps.get(i).entrySet().iterator();
                    ContentValues values = new ContentValues();
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                        String keyName = (String) entry.getKey();
                        String valName = (String) entry.getValue();
                        values.put(keyName, valName);
                    }
                    redListDao.insert(values, tagPageNo);
                }
                DTVTLogger.debug(String.format("RecommendListDao.insert [%s] size[%s]", DataBaseUtils.getRecommendTableName(tagPageNo), hashMaps.size()));
                DateUtils dateUtils = new DateUtils(mContext);
                dateUtils.addLastDate(cacheDateKey);
            } catch (SQLiteException e) {
                DTVTLogger.debug("RecommendListDataManager::insertRecommendInsertList, e.cause=" + e.getCause());
            } finally {
                DataBaseManager.getInstance().closeDatabase();
            }
        }
    }

    /**
     * キャッシュからリストデータを表示件数分取得する.
     * @param tagPageNo タブNo
     * @return recommendContentInfoList
     */
    @SuppressWarnings("OverlyLongMethod")
    public synchronized List<ContentsData> selectRecommendList(final int tagPageNo) {

        DTVTLogger.start();
        List<ContentsData> recommendContentInfoList = new ArrayList<>();
        DataBaseHelper deHelper = new DataBaseHelper(mContext);
        DataBaseManager.initializeInstance(deHelper);
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        synchronized (dataBaseManager) {
            try {
                SQLiteDatabase database = dataBaseManager.openDatabase();
                database.acquireReference();

                //データテーブル存在チェック
                if (!DataBaseUtils.isCachingRecord(database, DataBaseUtils.getRecommendTableName(tagPageNo))) {
                    DTVTLogger.debug(String.format("Database table [%s] data not exist", DataBaseUtils.getRecommendTableName(tagPageNo)));
                    DataBaseManager.getInstance().closeDatabase();
                    return null;
                }
                RecommendListDao redListDao = new RecommendListDao(database);

                //データテーブルに依らずカラム名は同一
                String[] columns = {
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_TITLE,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED1,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED2,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_PAGEID,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_GROUPID,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID,
                        RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_COPYRIGHT
                };
                List<Map<String, String>> resultList
                        = redListDao.findById(columns, tagPageNo);
                recommendContentInfoList = new ArrayList<>();
                if (resultList.size() == 0) {
                    DTVTLogger.debug(String.format("Database table [%s] data size 0", DataBaseUtils.getRecommendTableName(tagPageNo)));
                    DataBaseManager.getInstance().closeDatabase();
                    return recommendContentInfoList;
                }
                DTVTLogger.debug(String.format("Database table [%s] resultList size[%s]", DataBaseUtils.getRecommendTableName(tagPageNo), resultList.size()));
                for (int i = 0; i <= resultList.size() - 1; i++) {
                    Map<String, String> map = resultList.get(i);
                    ContentsData contentsData = new ContentsData();
                    contentsData.setContentsId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID));
                    contentsData.setCategoryId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID));
                    contentsData.setServiceId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID));
                    contentsData.setThumURL(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1));
                    contentsData.setTitle(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_TITLE));
                    contentsData.setStartViewing(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING));
                    contentsData.setEndViewing(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING));
                    contentsData.setReserved1(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED1));
                    contentsData.setReserved2(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED2));
                    contentsData.setReserved4(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4));
                    contentsData.setChannelId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID));
                    contentsData.setRecommendOrder(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER));
                    contentsData.setPageId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_PAGEID));
                    contentsData.setGroupId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_GROUPID));
                    contentsData.setRecommendMethodId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID));
                    contentsData.setCopyright(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_COPYRIGHT));
                    recommendContentInfoList.add(contentsData);
                }
            } catch (SQLiteException e) {
                DTVTLogger.debug("RecommendListDataManager::selectRecommendList, e.cause=" + e.getCause());
            } finally {
                DataBaseManager.getInstance().closeDatabase();
            }
        }
        DTVTLogger.end();
        return recommendContentInfoList;
    }
}