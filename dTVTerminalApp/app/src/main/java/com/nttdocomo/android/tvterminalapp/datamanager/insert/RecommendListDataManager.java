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
import com.nttdocomo.android.tvterminalapp.model.recommend.RecommendContentInfo;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE;

public class RecommendListDataManager {

    private Context mContext;
    private DBHelper mRecommendListDBHelper = null;
    private SQLiteDatabase mDb = null;
    private RecommendListDao mRecommendListDao = null;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public RecommendListDataManager(Context context) {
        mContext = context;
        mRecommendListDBHelper = new DBHelper(mContext);
        mDb = mRecommendListDBHelper.getWritableDatabase();
        mRecommendListDao = new RecommendListDao(mDb);

    }

    /**
     * VodClipAPIの解析結果をDBに格納する。
     *
     * @return
     */
    public void insertRecommendInsertList(RecommendChList redChList, boolean addFlag, int tagPageNo) {

        //各種オブジェクト作成
        List<Map<String, String>> hashMaps = redChList.getmRcList();

        //DB保存前に前回取得したデータは全消去する
        if (!addFlag) { // ページングの場合、削除しない
            switch (tagPageNo) {
                case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV:
                    mRecommendListDao.delete(tagPageNo);
                    break;
                case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO:
                    mRecommendListDao.delete(tagPageNo);
                    break;
                case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL:
                    mRecommendListDao.delete(tagPageNo);
                    break;
                case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV:
                    mRecommendListDao.delete(tagPageNo);
                    break;
                case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME:
                    mRecommendListDao.delete(tagPageNo);
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
            mRecommendListDao.insert(values, tagPageNo);
        }
        mDb.close();
        mRecommendListDBHelper.close();
    }

    /**
     * キャッシュからリストデータを表示件数分取得する
     */
    public List<RecommendContentInfo> selectRecommendList(int tagPageNo, int startIndex, int maxResult) {

        String[] columns = {
                RECOMMENDCHANNEL_LIST_CONTENTSID,
                RECOMMENDCHANNEL_LIST_CATEGORYID,
                RECOMMENDCHANNEL_LIST_SERVICEID,
                RECOMMENDCHANNEL_LIST_CTPICURL1,
                RECOMMENDCHANNEL_LIST_TITLE,
                RECOMMENDCHANNEL_LIST_STARTVIEWING
        };
        int maxResultData = startIndex + maxResult - 1;
        List<Map<String, String>> resultList
                = mRecommendListDao.findById(columns, tagPageNo, String.valueOf(maxResultData));
        List<RecommendContentInfo> recommendContentInfoList = new ArrayList<RecommendContentInfo>();
        if(resultList.size() == 0) {
            return recommendContentInfoList;
        }
        for (int i = startIndex - 1; i <= resultList.size()-1; i++) {
            Map<String, String> map = resultList.get(i);
            RecommendContentInfo info = new RecommendContentInfo(
                    map.get(RECOMMENDCHANNEL_LIST_CONTENTSID),
                    Integer.parseInt(map.get(RECOMMENDCHANNEL_LIST_CATEGORYID)),
                    Integer.parseInt(map.get(RECOMMENDCHANNEL_LIST_SERVICEID)),
                    map.get(RECOMMENDCHANNEL_LIST_CTPICURL1),
                    map.get(RECOMMENDCHANNEL_LIST_TITLE),
                    map.get(RECOMMENDCHANNEL_LIST_STARTVIEWING)
            );
            recommendContentInfoList.add(info);
        }
        return recommendContentInfoList;
    }
}