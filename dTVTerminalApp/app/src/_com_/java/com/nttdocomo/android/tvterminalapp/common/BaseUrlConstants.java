/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

/**
 * BaseUrlConstants.
 */
class BaseUrlConstants {

    /**
     * 検索サーバAPIのURL.
     */
    public static final String TOTAL_SEARCH_URL =
            "https://service.smt.docomo.ne.jp/srermd/search/index.do";

    /**
     * STBメタデータ取得APIのURL.
     */
    public static final String STB_META_DATA_URL =
            "https://service.smt.docomo.ne.jp/srermd/getMeta/index.do";

    /**
     * レコメンドサーバAPIのベースURL.
     */
    public static final String RECOMMEND_BASE_URL =
            "https://service.smt.docomo.ne.jp/srermd/";

    /**
     * レコメンドサーバー用のCIRCUS認証URL.
     */
    public static final String ONE_TIME_PASSWORD_AUTH_URL =
            "https://cfg.smt.docomo.ne.jp/auth/cgi/aplpwdauth";

    /**
     * ぷららサーバーAPIアクセス用のベースURL.
     */
    // 商用
    private static final String PLALA_BASE_URL = "https://if.hikaritv-docomo.jp/";

    /**
     * APIアクセスプロキシパス.
     */
    public static final String PLALA_C_CLIENT = PLALA_BASE_URL + "dtt/c_client/";
    /**
     * PLALA_AUTH.
     */
    public static final String PLALA_AUTH = PLALA_BASE_URL + "dtt/auth/";
    /**
     * PLALA_CLIENT.
     */
    public static final String PLALA_CLIENT = PLALA_BASE_URL + "dtt/client/";

    /**
     * ぷららサーバー IDファイルアクセス用のベースURL.
     */
    public static final String PLALA_ID_FILE_URL = "https://conf.dch.dmkt-sp.jp/common/client/";

    /**
     * ぷららサーバー固定ファイル用ベースURL.
     *
     * (現状は、ジャンル一覧と設定ファイルで使用)
     */
    public static final String PLALA_FIXED_FILE_BASE_URL
            = "https://conf.hikaritv-docomo.jp/";
}