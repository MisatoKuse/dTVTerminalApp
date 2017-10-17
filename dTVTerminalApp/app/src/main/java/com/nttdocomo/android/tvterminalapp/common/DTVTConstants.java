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

    //アプリ名前
    public static final String AppliName ="dリモート";

    //============================== 画面IDと画面タイトル定義 Begin ==============================
    //HOME(契約・ペアリング済み)
    public static final String HomeScreenID     = "dRM-01-001";
    public static final String HomeScreenTitle  = "HOME(契約・ペアリング済み)";

    //番組表（拡大版）
    public static final String ProgListScreenID     = "dRM-09-001";
    public static final String ProgListScreenTitle  = "番組表（拡大版）";

    //番組表（縮小版）
    public static final String ProgList2ScreenID     = "dRM-09-002";
    public static final String ProgList2ScreenTitle  = "番組表（縮小版）";

    //チャンネル詳細
    public static final String ChannelDetailScreenID     = "dRM-09-003";
    public static final String ChannelDetailScreenTitle  = "チャンネル詳細";


    //---------------------------------- Launch グループ Begin ----------------------------------
    //起動画面
    public static final String LaunchActivityScreenID= "";
    public static final String LaunchActivityScreenTitle = "起動画面";

    //----------------------------------- Launch グループ End -----------------------------------

    //=============================== 画面IDと画面タイトル定義 End ===============================


    //=================================== Logメセッジ定義 Begin ==================================
    public static final String LOG_DEF_TAG = "Common_Log";
    //==================================== Logメセッジ定義 End ===================================

    //=================================== 定数定義 Begin ==================================
    public static final int SEARCH_SERVER_TIMEOUT = 1000;   //一秒
    public static final String SEARCH_STATUS_NG = "ng";
    public static final String SEARCH_ERROR_ID_1 = "ERMD08001";
    public static final String SEARCH_ERROR_PARAM_NULL_REQ = "リクエストエラー(リクエストは空である)";
    //==================================== 定数定義 End ===================================

}


