/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


/**
 * 絞り込み条件：「ジャンル」を指定する（複数指定可）.
 */
public class SearchGenreType extends SearchFilterTypeMappable {
    /**各絞込み条件における設定内容：映画.*/
    public static final String active_001 = "映画";  //TODO : ジャンルタイプリスト仕様が決まり次第、追加する
    /**タイプ.*/
    private String mType = "";

    /**
     * コンストラクタ.
     * @param name 設定内容
     */
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