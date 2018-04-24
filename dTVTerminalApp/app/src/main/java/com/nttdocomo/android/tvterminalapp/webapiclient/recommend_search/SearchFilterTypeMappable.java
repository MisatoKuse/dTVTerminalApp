/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

/**
 * SearchFilterTypeMappableクラス.
 */
public abstract class SearchFilterTypeMappable {

    //must be overrode

    /**
     * サーチフィルタータイプ取得.
     * @return null
     */
    public SearchFilterType searchFilterType() {
        return null;
    }
}
