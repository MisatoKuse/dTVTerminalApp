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
     * レコメンドサーバAPIのベースURL.
     */
    public static final String RECOMMEND_BASE_URL =
            "https://ve.m.service.smt.docomo.ne.jp/srermd/";

    /**
     * レコメンドサーバー用のCIRCUS認証URL.
     */
    public static final String ONE_TIME_PASSWORD_AUTH_URL =
            "https://ve.m.cfg.smt.docomo.ne.jp/auth/cgi/aplpwdauth";

    /**
     * ぷららサーバーAPIアクセス用のベースURL.
     */
    // ステージング
//        private static final String PLALA_BASE_URL = "https://zif.hikaritv-docomo.jp/";
    // ラボ
    public static final String PLALA_BASE_URL = "https://tif.hikaritv-docomo.jp/";

    /**
     * APIアクセスプロキシパス.
     */
    public static final String PLALA_C_CLIENT = PLALA_BASE_URL + "dtt/c_client/";
    public static final String PLALA_AUTH = PLALA_BASE_URL + "dtt/auth/";
    public static final String PLALA_CLIENT = PLALA_BASE_URL + "dtt/client/";

    /**
     * ぷららサーバー ID管理ファイルアクセス用のベースURL.
     */
    public static final String PLALA_ID_FILE_URL = "https://dev-conf.dch.dmkt-sp.jp/common/client/";

}