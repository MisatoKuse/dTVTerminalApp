/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;


public class BaseUrlConstants {
    // stubサーバーwifiのIPアドレス設定現在はG-FFF8
    public static final String STUB_HOST = "http://192.168.11.32:5001";
    /**
     * 検索サーバAPIのURL.
     */
    public static final String TOTAL_SEARCH_URL = STUB_HOST + "/srermd/search/index.do";

    /**
     * STBメタデータ取得APIのURL.
     */
    public static final String STB_META_DATA_URL = STUB_HOST + "/srermd/getMeta/index.do";

    /**
     * レコメンドサーバAPIのベースURL.
     */
    public static final String RECOMMEND_BASE_URL = STUB_HOST +"/srermd/";


    /**
     * APIアクセスプロキシパス.
     */
    public static final String PLALA_C_CLIENT = STUB_HOST + "/dtt/c_client/";
    /**
     * plala_auth.
     */
    public static final String PLALA_AUTH = STUB_HOST + "/dtt/auth/";
    /**
     * plala_client.
     */
    public static final String PLALA_CLIENT = STUB_HOST + "/dtt/client/";

    //開発の環境と同じ
    /**
     * レコメンドサーバー用のCIRCUS認証URL.
     */
    public static final String ONE_TIME_PASSWORD_AUTH_URL = "https://ve.m.cfg.smt.docomo.ne.jp/auth/cgi/aplpwdauth";

    /**
     * ぷららサーバー ID管理ファイルアクセス用のベースURL.
     */
    public static final String PLALA_ID_FILE_URL = "https://dev-conf.dch.dmkt-sp.jp/common/client/";

    /**
     * ぷららサーバー固定ファイル用ベースURL.
     *
     * (現状は、ジャンル一覧と設定ファイルで使用)
     */
    public static final String PLALA_FIXED_FILE_BASE_URL = "https://tconf.hikaritv-docomo.jp/";
}