/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;


public class BaseUrlConstants {

    /**
     * 検索サーバAPIのURL.
     */
    public static final String TOTAL_SEARCH_URL =
            "https://ve.m.service.smt.docomo.ne.jp/srermd/search/index.do";

    /**
     * STBメタデータ取得APIのURL.
     */
    public static final String STB_META_DATA_URL =
            "https://ve.m.service.smt.docomo.ne.jp/srermd/getMeta/index.do";

    /**
     * レコメンドサーバAPIのベースURL.
     */
    public static final String RECOMMEND_BASE_URL =
            "https://ve.m.service.smt.docomo.ne.jp/srermd/";

    /**
     * ワンタイムパスワードでレコメンドサーバーを認証するURL.
     */
    public static final String ONE_TIME_PASSWORD_AUTH_URL =
            "https://ve.m.cfg.smt.docomo.ne.jp/auth/cgi/aplpwdauth";

    /**
     * ぷららサーバーAPIアクセス用のベースURL.
     */
    // ローカルサーバ.
    public static final String PLALA_BASE_URL = "http://192.168.2.3/";

    /**
     * APIアクセスプロキシパス.
     */
    public static final String PLALA_C_CLIENT = PLALA_BASE_URL + "dtt_stub_mix/c_client/";
    public static final String PLALA_AUTH = PLALA_BASE_URL + "dtt_stub_mix/auth/";
    public static final String PLALA_CLIENT = PLALA_BASE_URL + "dtt_stub_mix/client/";

    /**
     * ぷららサーバー ID管理ファイルアクセス用のベースURL.
     */
    public static final String PLALA_ID_FILE_URL = "http://192.168.2.3/";

    /**
     * ぷららサーバー固定ファイル用ベースURL.
     *
     * (現状は、ジャンル一覧と設定ファイルで使用)
     */
    public static final String PLALA_FIXED_FILE_BASE_URL
            = "http://192.168.2.3/";
}