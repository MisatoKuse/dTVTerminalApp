/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */


package com.nttdocomo.android.tvterminalapp.common;


/**
 * クラス機能：
 * プロジェクトにて共通データを定義するクラスである.
 */
public class DtvtConstants {
    //=================================== 定数定義 Begin ==================================
    /**
     * サーバー用のコネクションタイムアウト時間 30秒.
     */
    public static final int SERVER_CONNECT_TIMEOUT = 30000;
    /**
     * サーバー用の読み込みタイムアウト時間 30秒.
     */
    public static final int SERVER_READ_TIMEOUT = 30000;
    /**
     * 検索サーバーレスポンス・処理結果NG.
     */
    public static final int SEARCH_STATUS_NG = 1;
    /**
     * 検索サーバーレスポンス・処理結果OK.
     */
    public static final int SEARCH_STATUS_OK = 0;
    /**
     * サーチエラーID.
     */
    public static final String SEARCH_ERROR_ID_1 = "";
    /**
     * サーチパラメータnullリクエストエラー.
     */
    public static final String SEARCH_ERROR_PARAM_NULL_REQ = "リクエストエラー(リクエストは空である)";
    /**
     * グロバールメニュー起動.
     */
    public static final String GLOBAL_MENU_LAUNCH = "global_menu_launch";
    /**
     * ソーススクリーン.
     */
    public static final String SOURCE_SCREEN = "source_screen";

    /**
     * GETリクエストメソッド.
     */
    public static final String REQUEST_METHOD_GET = "GET";
    /**
     * ACCEPT_CHARSET.
     */
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    /**
     * コンテンツタイプ.
     */
    public static final String CONTENT_TYPE = "contentType";
    /**
     * UserAgent.
     */
    public static final String USER_AGENT = "User-Agent";
    /**
     * ロケーションキー.
     */
    public static final String LOCATION_KEY = "Location";
    /**
     * クリップ一覧パラメータ(取得方向).
     */
    public static final String CLIP_BODY_DIRECTION_NEXT = "next";
    /**
     * HTMLヘッダーキー：Last-Modified
     */
    public static final String LAST_MODIFIED_HEADER_KEY = "Last-Modified";

    /**
     * disp_type 一覧（まず、多階層用のみ宣言）.
     */
    public static final String DISP_TYPE_SERIES_SVOD = "series_svod",
            DISP_TYPE_WIZARD = "wizard",
            DISP_TYPE_VIDEO_PACKAGE = "video_package",
            DISP_TYPE_SUBSCRIPTION_PACKAGE = "subscription_package";

    /** limitがない場合のLowerPagerLimit初期値.*/
    public static final int REQUEST_LIMIT_1 = 1;
    /** limitがない場合の初期値.*/
    public static final int REQUEST_LIMIT_50 = 50;
    /** STBに対するDLNAデータ取得時のlimit. */
    public static final int REQUEST_DLNA_LIMIT_50 = 50;
    /** cache dataの最新バージョン */
    public static final int NEW_CACHE_DATA_VERDION = 1;
    /** アプリケーションプライバシーポリシー最新バージョン */
    public static final int NEW_APPLICATION_PRIVACY_POLICY_VERSION = 0;

    //==================================== 定数定義 End ===================================

    /**
     * 通信時エラー情報（WebAPI及びデータプロバイダー等で使用）.
     */
    public enum ErrorType {
        /**
         * 通信成功.
         */
        SUCCESS,

        /**
         * サーバーエラー：WebAPIやそのサーバーへのアクセスが失敗した場合のエラー.
         */
        SERVER_ERROR,

        /**
         * トークン取得エラー：各種トークンの取得に失敗した場合のエラー.
         */
        TOKEN_ERROR,

        /**
         * SSLエラー：証明書の期限切れ等、SSLライブラリの出すエラー.
         */
        SSL_ERROR,

        /**
         * ネットワークエラー:圏外等ネットワークに接続できない場合のエラー.
         */
        NETWORK_ERROR,

        /**
         * HTTPエラー:その他通信エラー.
         */
        HTTP_ERROR,

        /**
         * パースエラー(JSON, HTML)
         */
        PARSE_ERROR,
    }

}


