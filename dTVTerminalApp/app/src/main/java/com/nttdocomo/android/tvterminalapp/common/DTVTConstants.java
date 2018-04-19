/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */


package com.nttdocomo.android.tvterminalapp.common;


/**
 * クラス機能：
 * プロジェクトにて共通データを定義するクラスである.
 */
public class DTVTConstants {
    //=================================== 定数定義 Begin ==================================
    /**
     * 一秒.
     */
    public static final int SEARCH_SERVER_TIMEOUT = 1000;
    /**
     * NG.
     */
    public static final String SEARCH_STATUS_NG = "ng";
    /**
     * サーチエラーID.
     */
    public static final String SEARCH_ERROR_ID_1 = "ERMD08001";
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
     * ロケーションキー.
     */
    public static final String LOCATION_KEY = "Location";

    /**
     * disp_type 一覧（まず、多階層用のみ宣言）.
     */
    public static final String DISP_TYPE_SERIES_SVOD = "series_svod",
            DISP_TYPE_WIZARD = "wizard",
            DISP_TYPE_VIDEO_PACKAGE = "video_package",
            DISP_TYPE_SUBSCRIPTION_PACKAGE = "subscription_package";

    /** limitがない場合の初期値.*/
    public static final int REQUEST_LIMIT_50 = 50;
    //==================================== 定数定義 End ===================================

    /**
     * 通信時エラー情報（WebAPI及びデータプロバイダー等で使用）.
     */
    public enum ERROR_TYPE {
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

    }

}


