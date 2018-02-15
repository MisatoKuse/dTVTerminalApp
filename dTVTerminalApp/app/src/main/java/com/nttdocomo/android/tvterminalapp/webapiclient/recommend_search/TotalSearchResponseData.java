/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.struct.SearchContentInfo;

import java.util.ArrayList;

/**
 * 検索結果返却用データクラス.
 */
public class TotalSearchResponseData {

    /**
     * ServiceCount.
     */
    public class ServiceCount {
        /**
         * serviceId.
         */
        int serviceId;
        /**
         * contentCount.
         */
        int contentCount;
    }

    /**
     * コンテンツ情報.
     */
    public class Content {
        /**
         * ランク.
         */
        int rank;
        /**
         * コンテンツId.
         */
        String ctId;
        /**
         * サービスId.
         */
        int serviceId;
        /**
         * サムネイルURL1.
         */
        String ctPicURL1;
        /**
         * サムネイルURL2.
         */
        String ctPicURL2;
        /**
         * タイトル.
         */
        String title;
        /**
         * mobileViewingFlg モバイル視聴可否フラグ
         */
        String  mobileViewingFlg;
        //String person;    //iosソースより、保留
        //int titleKind;    //iosソースより、保留
    }

    /**
     * コンストラクタ.
     */
    public TotalSearchResponseData() {
        serviceCountList = new ArrayList<>();
        contentList = new ArrayList<>();
    }

    /**
     * 処理結果.
     */
    public String status;
    /**
     * 検索結果合計件数.
     */
    public int totalCount;
    /**
     * クエリ.
     */
    public String query;
    /**
     * 検索結果返却開始位置.
     */
    public int startIndex;
    /**
     * resultCount.
     */
    public int resultCount;

    /**
     * serviceCountの配列.
     */
    public ArrayList<ServiceCount> serviceCountList;
    /**
     * contentListの配列.
     */
    public ArrayList<Content> contentList;

    /**
     * 検索結果からコンテンツ情報をリストにセットする.
     *
     * @param searchContentInfoArray 検索結果リスト
     */
    public void map(ArrayList<SearchContentInfo> searchContentInfoArray) {
        if (null == searchContentInfoArray) {
            searchContentInfoArray = new ArrayList<>();
        } else {
            searchContentInfoArray.clear();
        }
        for (Content content: contentList) {
            SearchContentInfo info = new SearchContentInfo(false, content.ctId, content.serviceId,
                    content.ctPicURL1, content.ctPicURL2, content.title, content.rank, content.mobileViewingFlg);
            searchContentInfoArray.add(info);
        }
    }

    /**
     * serviceCountを増やす.
     */
    public void appendServiceCount() {
        serviceCountList.add(new ServiceCount());
    }

    /**
     * contentを増やす.
     */
    public void appendContent() {
        contentList.add(new Content());
    }
}
