/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */


package com.nttdocomo.android.tvterminalapp.common;


/**
 *
 * クラス機能：
 *      プロジェクトにて共通データを定義するクラスである
 *
 */
public class DTVTConstants {
    //=================================== 定数定義 Begin ==================================
    public static final int SEARCH_SERVER_TIMEOUT = 1000;   //一秒
    public static final String SEARCH_STATUS_NG = "ng";
    public static final String SEARCH_ERROR_ID_1 = "ERMD08001";
    public static final String SEARCH_ERROR_PARAM_NULL_REQ = "リクエストエラー(リクエストは空である)";
    public static final String GLOBAL_MENU_LAUNCH = "global_menu_launch";
    public static final String SOURCE_SCREEN = "source_screen";
    //==================================== 定数定義 End ===================================

    /**
     * 通信時エラー情報（WebAPI及びデータプロバイダー等で使用）
     */
    public enum ERROR_TYPE {
        /**
         * 通信成功
         */
        SUCCESS,

        /**
         * HTTPエラー
         */
        HTTP_ERROR,

        /**
         * その他通信エラー
         */
        COMMUNICATION_ERROR,
        /**
         * 証明書の問題等、SSLのエラー
         */
        SSL_ERROR,

        /**
         * データなし
         */
        NO_DATA,

        /**
         * レスポンス解析エラー
         */
        ANALYSIS_ERROR,

        /**
         * その他エラー
         */
        OTHER_ERROR,
    }

}


