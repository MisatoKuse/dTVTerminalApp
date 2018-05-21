/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.struct.SearchNarrowCondition;

/**
 *「その他絞込み条件」を指定 … 1：HD作品.
 */
public class SearchOtherType extends SearchFilterTypeMappable {
    /**HD作品.*/
    private final int HDContent = SearchNarrowCondition.getNextOrdinal();
    /**検索条件初期値.*/
    private int self = SearchNarrowCondition.sEnumOrdinalNil;

    /**
     * コンストラクタ.
     * @param name 検索絞込み条件
     */
    public SearchOtherType(final String name) {
        if ("SearchOtherTypeHDContent".equals(name)) {
            self = HDContent;
        }
    }

    @Override
    public SearchFilterType searchFilterType() {
        if (HDContent == self) {
            return SearchFilterType.otherHdWork;
        }
        return null;
    }
}
