/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.insert;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.RecommendListDao;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.DBHelper;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

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
     */
    public void insertRecommendInsertList(final RecommendChList redChList, final boolean addFlag, final int tagPageNo) {

        //各種オブジェクト作成
        List<Map<String, String>> hashMaps = redChList.getmRcList();
        DBHelper deHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(deHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        RecommendListDao redListDao = new RecommendListDao(database);

        //DB保存前に前回取得したデータは全消去する
        if (!addFlag) { // ページングの場合、削除しない
            switch (tagPageNo) {
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
        DataBaseManager.getInstance().closeDatabase();
    }

    /**
     * キャッシュからリストデータを表示件数分取得する.
     * @param tagPageNo タブNo
     * @param startIndex 取得開始位置
     * @param maxResult 最大取得件数
     * @return recommendContentInfoList
     */
    public List<ContentsData> selectRecommendList(final int tagPageNo, final int startIndex, final int maxResult) {

        DBHelper deHelper = new DBHelper(mContext);
        DataBaseManager.initializeInstance(deHelper);
        SQLiteDatabase database = DataBaseManager.getInstance().openDatabase();
        RecommendListDao redListDao = new RecommendListDao(database);

        String[] columns = {
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_PAGEID,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_GROUPID,
                RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID
        };
        int maxResultData = startIndex + maxResult - 1;
        List<Map<String, String>> resultList
                = redListDao.findById(columns, tagPageNo, String.valueOf(maxResultData));
        List<ContentsData> recommendContentInfoList = new ArrayList<>();
        if (resultList.size() == 0) {
            return recommendContentInfoList;
        }
        for (int i = startIndex - 1; i <= resultList.size() - 1; i++) {
            Map<String, String> map = resultList.get(i);
            ContentsData contentsData = new ContentsData();
            contentsData.setContentsId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID));
            contentsData.setCategoryId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID));
            contentsData.setServiceId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID));
            contentsData.setThumURL(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1));
            contentsData.setTitle(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE));
            contentsData.setStartViewing(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING));
            contentsData.setEndViewing(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING));
            contentsData.setReserved1(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED1));
            contentsData.setReserved2(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED2));
            contentsData.setReserved4(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4));
            contentsData.setChannelId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID));
            contentsData.setRecommendOrder(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER));
            contentsData.setPageId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_PAGEID));
            contentsData.setGroupId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_GROUPID));
            contentsData.setRecommendMethodId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID));
            recommendContentInfoList.add(contentsData);
        }
        DataBaseManager.getInstance().closeDatabase();
        return recommendContentInfoList;
    }
}