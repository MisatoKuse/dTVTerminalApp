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
         * HTTPエラー.
         */
        HTTP_ERROR,

        /**
         * その他通信エラー.
         */
        COMMUNICATION_ERROR,
        /**
         * 証明書の問題等、SSLのエラー.
         */
        SSL_ERROR,

        /**
         * データなし.
         */
        NO_DATA,

        /**
         * レスポンス解析エラー.
         */
        ANALYSIS_ERROR,

        /**
         * その他エラー.
         */
        OTHER_ERROR,
    }

}


