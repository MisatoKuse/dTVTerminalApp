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

    //ブロードキャスト登録名
    //デフォルトのdアカウントIDを知らせる場合
    public static final String SET_ID_RECEIVER = //"com.nttdocomo.android.tvterminalapp.DOCOMOID_SET_DEFAULT";
                                                    "com.nttdocomo.android.idmanager.action.DOCOMOID_SET_DEFAULT";
    //dアカウントユーザー認証が発生した事を知らせる場合
    public static final String USER_AUTH_RECEIVER =//"com.nttdocomo.android.tvterminalapp.DOCOMOID_AUTHENTICATED";
                                                        "com.nttdocomo.android.idmanager.action.DOCOMOID_AUTHENTICATED";
    //dアカウントユーザーの削除を知らせる場合
    public static final String DELETE_ID_RECEIVER =//"com.nttdocomo.android.tvterminalapp.DOCOMOID_RECEIVER";
                                                         "com.nttdocomo.android.idmanager.action.DOCOMOID_REMOVED";
    //dアカウント無効化を知らせる場合
    public static final String INVALIDATE_ID_RECEIVER =//"com.nttdocomo.android.tvterminalapp.DOCOMOID_INVALIDATE";
                                                            "com.nttdocomo.android.idmanager.action.DOCOMOID_INVALIDATE";
    //回線連携完了を知らせる場合
    public static final String LINKED_LINE_RECEIVER = //"com.nttdocomo.android.tvterminalapp.DOCOMOID_LINKED_LINE";
                                                            "com.nttdocomo.android.idmanager.action.DOCOMOID_LINKED_LINE";
    //アプリ除外を知らせる場合
    public static final String SERVICEAPP_REMOVED_RECEIVER = "com.nttdocomo.android.idmanager.action.SERVICEAPP_REMOVED";


}
