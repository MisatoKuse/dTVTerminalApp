/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * サーチコンテンツクラス.
 */
public class SearchConstants {
    /**
     * Search.
     */
    public static class Search {
        /**検索結果返却数.*/
        public static final int requestMaxResultCount = 20;
        /**検索結果返却開始位置.*/
        public static final int startPageIndex = 1;
    }

    /**
     * 検索レスポンスエラーID.
     */
    public static class SearchResponseErrorId {
        /**個々のサービスIDの桁数が不一致の場合はERMD08001（リクエストエラー）.*/
        public static final String requestError = "ERMD08001";
        /**範囲を超えている場合のERMD08002（システムエラー）.*/
        public static final String systemError = "ERMD08002";
    }

    /**
     * レコメンド情報.
     */
    private static class RecommendList {
        /**レコメンド情報検索結果返却数.*/
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

    /**
     * レコメンド情報レスポンスエラーID.
     */
    private static class RecommendResponseErrorId {
        /**個々のサービスIDの桁数が不一致の場合はERMD08001（リクエストエラー）.*/
        public static final String requestError_Recommend = "ERMD08001";
        /**範囲を超えている場合はERMD08002（システムエラー）.*/
        public static final String systemError_Recommend = "ERMD08002";
    }

    /**
     * おすすめタブページ番号.
     */
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



