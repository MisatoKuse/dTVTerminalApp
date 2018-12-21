/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 *StbMetaInfoGetWebApiDelegateインタフェース.
 */
public interface StbMetaInfoGetWebApiDelegate {
    /**
     * 成功時のcallback.
     * @param result STBメタデータ取得結果レスポンスデータ
     */
    void onSuccess(StbMetaInfoResponseData result);

    /**
     * 失敗時のcallback.
     * @param result STBメタデータ取得エラー情報
     */
    void onFailure(StbMetaInfoGetErrorData result);
}
