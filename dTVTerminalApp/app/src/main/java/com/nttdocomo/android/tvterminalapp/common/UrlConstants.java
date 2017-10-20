/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;



public class UrlConstants {

    public class WebApiUrl {
        //検索画面
        public static final String totalSearchUrl = "https://sea.dtv.dmkt-sp.jp/irengine/search/index.do";

        /**
         * レコメンド情報取得APIのURL
         */
        public static final String RECOMMEND_LIST_GET_URL =
                //"http://ve.m.support2.smt.docomo.ne.jp/srermd/recommend/index.do";
                "https://ve.m.support2.smt.docomo.ne.jp/srermd/recommend/index.do";
    }
}
