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
        int mServiceId;
        /**
         * contentCount.
         */
        int mContentCount;
    }

    /**
     * コンテンツ情報.
     */
    public class Content {
        /**
         * ランク.
         */
        int mRank;
        /**
         * コンテンツId.
         */
        String mContentsId;
        /**
         * サービスId.
         */
        int mServiceId;
        /**
         * サムネイルURL1.
         */
        String mCtPicURL1;
        /**
         * サムネイルURL2.
         */
        String mCtPicURL2;
        /**
         * タイトル.
         */
        String mTitle;
        /**
         * mobileViewingFlg モバイル視聴可否フラグ.
         */
        String mMobileViewingFlg;
        /**
         *  開始時刻.
         */
        String mStartViewing;
        /**
         *  終了時刻.
         */
        String mEndViewing;
        /**
         *  チャンネル名.
         */
        String mChannelName;
        /**
         *  種類名.
         */
        String mGenreName;
        /**
         *  チャンネルID.
         */
        String mChannelId;
        /**
         *  カテゴリーID.
         */
        String mCategoryId;
        /**
         *  年齢.
         */
        String mViewableAge;
        /**
         *  cast.
         */
        String mCast;
        /**
         *  reserved1.
         */
        String mReserved1;
        /**
         *  reserved2.
         */
        String mReserved2;
        /**
         *  reserved3.
         */
        String mReserved3;
        /**
         *  reserved4.
         */
        String mReserved4;
        /**
         *  reserved5.
         */
        String mReserved5;
        /**
         *  description1.
         */
        String mDescription1;
        /**
         *  description2.
         */
        String mDescription2;
        /**
         *  description3.
         */
        String mDescription3;
        /**
         *  paymentFlg.
         */
        String mPaymentFlg;
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
     * @return tmpSearchContentInfoArray 検索結果リスト
     */
    public ArrayList<SearchContentInfo> map(final ArrayList<SearchContentInfo> searchContentInfoArray) {
        ArrayList<SearchContentInfo> tmpSearchContentInfoArray  = searchContentInfoArray;
        if (null == tmpSearchContentInfoArray) {
            tmpSearchContentInfoArray = new ArrayList<>();
        } else {
            tmpSearchContentInfoArray.clear();
        }
        for (Content content: contentList) {
            SearchContentInfo info = new SearchContentInfo(false, content.mContentsId, content.mServiceId, content.mCategoryId,
                    content.mCtPicURL1, content.mCtPicURL2, content.mTitle, content.mRank, content.mMobileViewingFlg,
                    content.mStartViewing, content.mEndViewing, content.mChannelName, content.mChannelId,
                    content.mGenreName, content.mDescription1, content.mDescription2, content.mDescription3,
                    content.mViewableAge, content.mReserved1, content.mReserved2, content.mReserved3,
                    content.mReserved4, content.mReserved5);
            tmpSearchContentInfoArray.add(info);
        }
        return tmpSearchContentInfoArray;
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
