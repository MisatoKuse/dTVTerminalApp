/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * STBメタデータ取得・リクエストデータクラス
 */
public class StbMetaInfoRequestData {
    //required
    /**サービスカテゴリ*/
    public String serviceCategory = null;

    /**コンテンツID*/
    public String contentsId = null;

    //optional
    /**エピソード取得開始位置*/
    int episodeStartIndex = 0;

    /**エピソード取得最大件数*/
    int episodeMaxResult = 0;
}
