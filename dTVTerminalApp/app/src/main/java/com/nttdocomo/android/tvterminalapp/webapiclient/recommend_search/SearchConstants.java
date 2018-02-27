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

        /**
         * レコメンド情報を先読みする場合の個数.
         */
        public static final int RECOMMEND_PRELOAD_COUNT = 100;

        /**
         * レコメンド情報を先読みする場合の先頭位置.
         */
        public static final int FIRST_POSITION = 1;
    }

    public static class RecommendResponseErrorId {
        public static final String requestError_Recommend = "ERMD08001";
        public static final String systemError_Recommend = "ERMD08002";
    }

    public static class RecommendTabPageNo {
        /** テレビ. */
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_TV = 0;
        /** ビデオ. */
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO =
                RECOMMEND_PAGE_NO_OF_SERVICE_TV + 1;
        /** dTV. */
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_DTV =
                RECOMMEND_PAGE_NO_OF_SERVICE_TV + 2;
        /** dチャンネル. */
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL =
                RECOMMEND_PAGE_NO_OF_SERVICE_TV + 3;
        /**  dアニメ. */
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_DANIME =
                RECOMMEND_PAGE_NO_OF_SERVICE_TV + 4;
        /** その他. */
        public static final int RECOMMEND_PAGE_NO_OF_SERVICE_UNKNOWN =
                RECOMMEND_PAGE_NO_OF_SERVICE_TV + 99;
    }
}



