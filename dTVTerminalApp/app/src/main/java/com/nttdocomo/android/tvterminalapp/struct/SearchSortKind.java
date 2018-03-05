/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

public class SearchSortKind {


    public enum SearchSortKindEnum {
        SEARCH_SORT_KIND_NONE,
        SEARCH_SORT_KIND_POPULARITY,
        SEARCH_SORT_KIND_NEW_ARRIVAL,
        SEARCH_SORT_KIND_TITLE
    }

    private SearchSortKindEnum mSearchSortKindEnum;

    public SearchSortKind(final SearchSortKindEnum kind) {
        mSearchSortKindEnum = kind;
    }

    public SearchSortKindEnum searchWebSortType() {
        return mSearchSortKindEnum;
    }
}
