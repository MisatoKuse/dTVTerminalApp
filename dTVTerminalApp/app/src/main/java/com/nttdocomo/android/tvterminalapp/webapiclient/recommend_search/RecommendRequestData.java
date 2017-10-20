/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * レコメンド取得用パラメータ
 */
public class RecommendRequestData {
    /**
     * 取得対象サービスID
     */
    public String serviceId;
    /**
     * 取得対象サービスID:カテゴリーID
     */
    public String serviceCategoryId;
    /**
     * レコメンド取得ページ
     */
    public String getPage;
    /**
     * レコメンド取得開始位置
     */
    public String startIndex;
    /**
     * レコメンド取得最大件数
     */
    public String maxResult;
    /**
     * 画面ID
     */
    public String pageId;
    /**
     * 放送時間考慮
     */
    public String airtime;

    /**
     * コンストラクタ
     */
    public RecommendRequestData() {
        //初期化
        serviceId = "";
        serviceCategoryId = "";
        getPage = "";
        startIndex = "";
        maxResult = "";
        pageId = "";
        airtime = "";
    }
}
