/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.common;

/**
 * dアカウント連携用の固定値を格納する
 */
public class DaccountConstants {
    //dアカウント設定アプリのクラス名
    public static final String D_ACCOUNT_ID_MANAGER = "com.nttdocomo.android.idmanager";

    //dアカウント設定アプリのサービス名
    public static final String D_ACCOUNT_SERVICE =
            "com.nttdocomo.android.idmanager.DimServiceAppService";

    //サービス登録専用の識別名
    public static final String REGIST_SERVICE_ACTION = "DimServiceAppServiceCustom";

    //DTVTアプリに割り当てられたサービス識別キー(OTP)
    public static final String SERVICE_KEY = "B3d02";
}
