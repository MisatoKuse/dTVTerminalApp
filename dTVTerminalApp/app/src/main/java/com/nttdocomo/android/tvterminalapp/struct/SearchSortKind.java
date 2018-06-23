/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;


/**
 * サーチ時のソート種別クラス.
 */
public class SearchSortKind {

    /**ソート種別.*/
    public enum SearchSortKindEnum {
        /**なし.*/
        SEARCH_SORT_KIND_NONE,
        /**人気.*/
        SEARCH_SORT_KIND_POPULARITY,
        /**新着.*/
        SEARCH_SORT_KIND_NEW_ARRIVAL,
        /**タイトル.*/
        SEARCH_SORT_KIND_TITLE
    }
    /**インスタンス.*/
    private final SearchSortKindEnum mSearchSortKindEnum;

    /**
     * コンストラクタ.
     * @param kind ソート種別
     */
    public SearchSortKind(final SearchSortKindEnum kind) {
        mSearchSortKindEnum = kind;
    }

    /**
     * ソート種別取得.
     * @return ソート種別
     */
    public SearchSortKindEnum searchWebSortType() {
        return mSearchSortKindEnum;
    }
}
