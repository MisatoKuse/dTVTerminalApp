/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;


public class BaseUrlConstants {

    /**
     * 検索サーバAPIのURL.
     */
    public static final String TOTAL_SEARCH_URL =
            "https://sea2.dtv.dmkt-sp.jp/irengine/search/index.do";

    /**
     * レコメンド情報取得APIのURL.
     */
    public static final String RECOMMEND_LIST_GET_URL =
            "https://service.smt.docomo.ne.jp/srermd/recommend/index.do";

    /**
     * ワンタイムパスワードでレコメンドサーバーを認証するURL.
     */
    public static final String ONE_TIME_PASSWORD_AUTH_URL =
            "https://cfg.smt.docomo.ne.jp/auth/cgi/aplpwdauth";

    /**
     * ぷららサーバーAPIアクセス用のベースURL.
     */
    // 商用
    public static final String PLALA_BASE_URL = "https://if.hikaritv-docomo.jp/";

    /**
     * APIアクセスプロキシパス・現在のところPLALA_AUTHを使用する物は無いが、将来の為に残しておく.
     */
    public static final String PLALA_C_CLIENT = PLALA_BASE_URL + "dtt/c_client/";
    public static final String PLALA_AUTH = PLALA_BASE_URL + "dtt/auth/";
    public static final String PLALA_CLIENT = PLALA_BASE_URL + "dtt/client/";

    /**
     * ぷららサーバー IDファイルアクセス用のベースURL.
     */
    public static final String PLALA_ID_FILE_URL = "https://conf.dch.dmkt-sp.jp/common/client/";
}