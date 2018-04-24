/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;



import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchFilterType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchFilterTypeMappable;

import java.util.ArrayList;

/**
 * 検索条件クラス.
 */
public class SearchNarrowCondition {
    /**EnumOrdinal.*/
    private static int sEnumOrdinal = 1;
    /**EnumOrdinalNil.*/
    public static final int sEnumOrdinalNil = 0;

    /**
     * getNextOrdinal.
     * @return インデックス
     */
    public static synchronized int getNextOrdinal() {
        return ++sEnumOrdinal;
    }
    /**検索条件ArrayList.*/
    ArrayList<SearchFilterTypeMappable> conditionArray;

    /**
     * コンストラクタ.
     * @param conditionArray 検索条件
     */
    public SearchNarrowCondition(final ArrayList<SearchFilterTypeMappable> conditionArray) {
        this.conditionArray = conditionArray;
    }

    /**
     * 検索フィルタリストを取得.
     * @return  検索フィルタリスト
     */
    public ArrayList<SearchFilterType> searchFilterList() {
        ArrayList<SearchFilterType> ret = new ArrayList<SearchFilterType>();
        for (SearchFilterTypeMappable map: conditionArray) {
            ret.add(map.searchFilterType());
        }
        return ret;
    }
}
