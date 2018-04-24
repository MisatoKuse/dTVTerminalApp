/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 *TotalSearchWebApiDelegateインタフェース.
 */
public interface TotalSearchWebApiDelegate {
    /**
     * 成功時のcallback.
     * @param result 検索結果レスポンスデータ
     */
    void onSuccess(TotalSearchResponseData result);

    /**
     * 失敗時のcallback.
     * @param result 検索エラー情報
     */
    void onFailure(TotalSearchErrorData result);
}
