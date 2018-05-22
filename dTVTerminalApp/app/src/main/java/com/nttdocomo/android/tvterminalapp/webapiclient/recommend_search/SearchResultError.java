/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * 検索エラー原因種別.
 */
public enum SearchResultError {
    /**システムエラー(エラーID:ERMD08002).*/
    systemError,
    /**リクエストエラー(エラーID:ERMD08001).*/
    requestError
}
