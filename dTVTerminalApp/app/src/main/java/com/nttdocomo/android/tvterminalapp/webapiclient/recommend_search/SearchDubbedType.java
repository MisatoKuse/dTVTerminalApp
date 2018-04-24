/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.struct.SearchNarrowCondition;

/**
 * 検索絞込み条件:「吹替」を指定する（複数指定可）.
 */
public class SearchDubbedType extends SearchFilterTypeMappable {
    /**2：吹替のみ.*/
    public final int dubbed = SearchNarrowCondition.getNextOrdinal();
    /**1：字幕のみ.*/
    public final int subtitle = SearchNarrowCondition.getNextOrdinal();
    /**3：字幕/吹替両対応.*/
    public final int both = SearchNarrowCondition.getNextOrdinal();
    /**検索条件初期値.*/
    private int self = SearchNarrowCondition.sEnumOrdinalNil;

    /**
     *コンストラクタ.
     * @param name 絞込みの指定
     */
    public SearchDubbedType(final String name) {
        if ("SearchDubbedTypeDubbed".equals(name)) {
            self = dubbed;
        } else if ("SearchDubbedTypeSubtitle".equals(name)) {
            self = subtitle;
        } else if ("SearchDubbedTypeBoth".equals(name)) {
            self = both;
        }
    }

    @Override
    public SearchFilterType searchFilterType() {
        if (dubbed == self) {
            return SearchFilterType.dubbedDubbed;
        } else if (subtitle == self) {
            return SearchFilterType.dubbedText;
        } else if (both == self) {
            return SearchFilterType.dubbedTextAndDubbing;
        }

        return null;
    }
}
