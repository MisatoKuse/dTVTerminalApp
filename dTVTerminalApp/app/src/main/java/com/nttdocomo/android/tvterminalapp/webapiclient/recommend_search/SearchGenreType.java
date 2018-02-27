/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;




public class SearchGenreType extends SearchFilterTypeMappable {
    public static final String active_001 = "映画";  //TODO: ジャンルタイプリスト仕様が決まり次第、追加する

    private String mType = "";

    public SearchGenreType(final String name) {
        if ("SearchGenreTypeActive_001".equals(name)) {
            mType = active_001;
        }
    }

    @Override
    public SearchFilterType searchFilterType() {
        if (active_001.equals(mType)) {
            return SearchFilterType.genreMovie;
        }
        return null;
    }
}