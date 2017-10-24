/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

public class SearchConstants {

    public static class Search {
        public static final int requestMaxResultCount = 20;
        public static final int startPageIndex = 1;
    }

    public static class SearchResponseErrorId {
        public static final String requestError = "ERMD08001";
        public static final String systemError = "ERMD08002";
    }

    public static class RecommendList {
        public static final int requestMaxCount_Recommend = 20;
    }

    public static class RecommendResponseErrorId {
        public static final String requestError_Recommend = "ERMD08001";
        public static final String systemError_Recommend = "ERMD08002";
    }

    public static class RecommendTabPageNo {
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_TELEBI = 0;                                                     // テレビ
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO = RECOMMEND_PAGE_NO_OF_SERVICE_TELEBI + 1;          // ビデオ
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL = RECOMMEND_PAGE_NO_OF_SERVICE_TELEBI + 2;   // dチャンネル
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_DTV = RECOMMEND_PAGE_NO_OF_SERVICE_TELEBI + 3;            // dTV
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_DANIME = RECOMMEND_PAGE_NO_OF_SERVICE_TELEBI + 4;         //  dアニメ
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_UNKNOWN = RECOMMEND_PAGE_NO_OF_SERVICE_TELEBI + 99;       // その他

    }
}



