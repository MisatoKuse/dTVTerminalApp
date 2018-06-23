/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * レコメンド取得用パラメータ.
 */
public class RecommendRequestData {

    /**
     * 取得対象サービスID.
     */
    private final String mServiceId;

   /**
     * 取得対象サービスID:カテゴリーID.
     */
    private String mServiceCategoryId;

    /**
     * レコメンド取得ページ.
     */
    private final String mGetPage;

    /**
     * レコメンド取得開始位置.
     */
    private String mStartIndex;

    /**
     * レコメンド取得最大件数.
     */
    private String mMaxResult;

    /**
     * 画面ID.
     */
    private final String mPageId;
    /**
     * 放送時間考慮.
     */
    private final String mAirtime;

    /**
     * コンストラクタ.
     */
    public RecommendRequestData() {
        //初期化
        mServiceId = "";
        mServiceCategoryId = "";
        mGetPage = "";
        mStartIndex = "";
        mMaxResult = "";
        mPageId = "";
        mAirtime = "";
    }

    public String getServiceId() {
        return mServiceId;
    }

    public String getServiceCategoryId() {
        return mServiceCategoryId;
    }

    public void setStartIndex(String startIndex) {
        this.mStartIndex = startIndex;
    }

    public void setMaxResult(String maxResult) {
        this.mMaxResult = maxResult;
    }

    public void setServiceCategoryId(String serviceCategoryId) {
        this.mServiceCategoryId = serviceCategoryId;
    }

    public String getPageId() {
        return mPageId;
    }

    public String getMaxResult() {
        return mMaxResult;
    }

    public String getStartIndex() {
        return mStartIndex;
    }

    public String getGetPage() {
        return mGetPage;
    }

}
