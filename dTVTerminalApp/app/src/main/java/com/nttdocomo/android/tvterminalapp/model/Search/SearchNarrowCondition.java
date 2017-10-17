package com.nttdocomo.android.tvterminalapp.model.Search;



import com.nttdocomo.android.tvterminalapp.webapiclient.recommend.search.SearchFilterType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend.search.SearchFilterTypeMappable;

import java.util.ArrayList;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class SearchNarrowCondition {

    private static int sEnumOrdinal = 1;
    public static final int sEnumOrdinalNil = 0;
    public static synchronized int getNextOrdinal() {
        return ++sEnumOrdinal;
    }

    ArrayList<SearchFilterTypeMappable> conditionArray;

    public SearchNarrowCondition(ArrayList<SearchFilterTypeMappable> conditionArray){
        this.conditionArray= conditionArray;
    }

    public ArrayList<SearchFilterType> searchFilterList() {
        ArrayList<SearchFilterType> ret=new ArrayList<SearchFilterType>();
        for(SearchFilterTypeMappable map:conditionArray){
            ret.add(map.searchFilterType());
        }
        return ret;
    }
}
