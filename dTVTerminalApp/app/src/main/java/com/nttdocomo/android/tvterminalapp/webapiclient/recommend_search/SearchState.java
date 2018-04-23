/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


/**
 * サーチステータス.
 */
public enum SearchState {
    /**初期状態.*/
    inital,
    /**サーチ中.*/
    running,
    /**完了.*/
    finished,
    /**中止.*/
    canceled;
}
