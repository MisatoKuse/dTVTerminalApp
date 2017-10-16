package com.nttdocomo.android.tvterminalapp.activity.common;

/**
 *  機能
 *      ユーザ状態を定義
 */
public enum UserState {
    LOGIN_NG,                   //未ログイン
    LOGIN_OK_CONTRACT_NG,     //未契約ログイン
    CONTRACT_OK_PAIRING_NG,   //契約・ペアリング未
    CONTRACT_OK_PARING_OK;    //契約・ペアリング済み
}
