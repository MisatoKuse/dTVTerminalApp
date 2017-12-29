/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;



public class SearchRequestKey {
    //request key
    public static final String kUserId = "?userId";
    public static final String kFunction = "&function";
    public static final String kResponseType = "&responseType";
    public static final String kQuery = "&query";
    public static final String kStartIndex = "&startIndex";
    public static final String kMaxResult = "&maxResult";
    public static final String kSortKind = "&sortKind";
    public static final String kServiceId = "&serviceId";
    public static final String kServiceCategoryId = "&serviceCategoryId";
//    public static final String kCategoryId = "&filterCategoryId";TODO:仕様確認後削除
    public static final String kCondition = "&condition";
}
