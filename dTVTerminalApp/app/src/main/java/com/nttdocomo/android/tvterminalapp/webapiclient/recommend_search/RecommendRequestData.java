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
    private String mPageId;

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
    }

    public String getServiceId() {
        return mServiceId;
    }

    public String getServiceCategoryId() {
        return mServiceCategoryId;
    }

    public void setStartIndex(final String startIndex) {
        this.mStartIndex = startIndex;
    }

    public void setMaxResult(final String maxResult) {
        this.mMaxResult = maxResult;
    }

    public void setServiceCategoryId(final String serviceCategoryId) {
        this.mServiceCategoryId = serviceCategoryId;
    }

    public void setPageId(String pageId) {
        this.mPageId = pageId;
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
