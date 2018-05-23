/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.struct.SearchNarrowCondition;

/**
 * 検索絞込み条件:「課金方法」を指定する（複数指定可）.
 */
public class SearchChargeType extends SearchFilterTypeMappable {
    /**0：見放題.*/
    private final int free = SearchNarrowCondition.getNextOrdinal();
    /**1：レンタル.*/
    private final int rental = SearchNarrowCondition.getNextOrdinal();
    /**検索条件初期値.*/
    private int self = SearchNarrowCondition.sEnumOrdinalNil;

    /**
     * コンストラクタ.
     * @param name 検索絞込み条件
     */
    public SearchChargeType(final String name) {
        if ("SearchChargeTypeFree".equals(name)) {
            self = free;
        } else if ("SearchChargeTypeRental".equals(name)) {
            self = rental;
        }
    }

    @Override
    public SearchFilterType searchFilterType() {
        if (free == self) {
            return SearchFilterType.chargeUnlimited;
        } else if (rental == self) {
            return SearchFilterType.chargeRental;
        }

        return null;
    }
}
