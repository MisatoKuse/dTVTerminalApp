/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;


public class BaseUrlConstants {

    /**
     * 検索サーバAPIのURL.
     */
    public static final String TOTAL_SEARCH_URL =
            "https://sea2.stg.dtv.dmkt-sp.jp/irengine/search/index.do";

    /**
     * レコメンド情報取得APIのURL.
     */
    public static final String RECOMMEND_LIST_GET_URL =
            "https://ve.m.support2.smt.docomo.ne.jp/srermd/recommend/index.do";

    /**
     * ワンタイムパスワードでレコメンドサーバーを認証するURL.
     */
    public static final String ONE_TIME_PASSWORD_AUTH_URL =
            "https://ve.m.cfg.smt.docomo.ne.jp/auth/cgi/aplpwdauth";
    //"https://ve.m.service.smt.docomo.ne.jp/auth/cgi/aplpwdauth";

    /**
     * ぷららサーバーAPIアクセス用のベースURL.
     */
    // ステージング
//        private static final String PLALA_BASE_URL = "https://zif.hikaritv-docomo.jp/";
    // ラボ
    public static final String PLALA_BASE_URL = "https://tif.hikaritv-docomo.jp/";

    /**
     * APIアクセスプロキシパス・現在のところPLALA_AUTHを使用する物は無いが、将来の為に残しておく.
     */
    public static final String PLALA_C_CLIENT = PLALA_BASE_URL + "dtt_stub_mix/c_client/";
    public static final String PLALA_AUTH = PLALA_BASE_URL + "dtt_stub_mix/auth/";
    public static final String PLALA_CLIENT = PLALA_BASE_URL + "dtt_stub_mix/client/";

    /**
     * ぷららサーバー ID管理ファイルアクセス用のベースURL.
     */
    public static final String PLALA_ID_FILE_URL = "https://dev-conf.dch.dmkt-sp.jp/common/client/";

}