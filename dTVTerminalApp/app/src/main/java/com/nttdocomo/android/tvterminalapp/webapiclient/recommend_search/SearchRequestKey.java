/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * 検索レコメンドAPI用リクエストキー.
 */
public class SearchRequestKey {
    //request key
    public static final String kQuery = "?query";
    public static final String kStartIndex = "&startIndex";
    public static final String kMaxResult = "&maxResult";
    public static final String kSortKind = "&sortKind";
    public static final String kServiceId = "&serviceId";
    public static final String kCategoryId = "&filterCategoryId";
    public static final String kFilterViewableAge = "&filterViewableAge";
    public static final String kDisplayId = "&displayId";
}
