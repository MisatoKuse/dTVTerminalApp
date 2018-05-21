/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * 検索レコメンドAPI用リクエストキー.
 */
class SearchRequestKey {
    //request key
    /**?query.*/
    public static final String kQuery = "?query";
    /**&startIndex.*/
    public static final String kStartIndex = "&startIndex";
    /**&maxResult.*/
    public static final String kMaxResult = "&maxResult";
    /**&sortKind.*/
    public static final String kSortKind = "&sortKind";
    /**&serviceId.*/
    public static final String kServiceId = "&serviceId";
    /**&filterCategoryId.*/
    public static final String kCategoryId = "&filterCategoryId";
    /**&filterViewableAge.*/
    public static final String kFilterViewableAge = "&filterViewableAge";
    /**&displayId.*/
    public static final String kDisplayId = "&displayId";
}
