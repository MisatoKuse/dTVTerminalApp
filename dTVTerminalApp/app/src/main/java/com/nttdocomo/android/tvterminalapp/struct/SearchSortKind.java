/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchSortType;

public class SearchSortKind {


    public enum SearchSortKindEnum{
        none,
        popularity,
        newArrivalOrder;
    }

    private SearchSortKindEnum mSearchSortKindEnum;

    public SearchSortKind(String name){
        if("SearchSortKindNone".equals(name)){
            mSearchSortKindEnum=SearchSortKindEnum.none;
        } else if("SearchSortKindPopularity".equals(name)){
            mSearchSortKindEnum=SearchSortKindEnum.popularity;
        } else if("SearchSortKindNewArrivalOrder".equals(name)){
            mSearchSortKindEnum=SearchSortKindEnum.newArrivalOrder;
        }
        /*
        switch name {
            case NSLocalizedString("SearchSortKindNone", comment: ""):
                self = .none

            case NSLocalizedString("SearchSortKindPopularity", comment: ""):
                self = .popularity

            case NSLocalizedString("SearchSortKindNewArrivalOrder", comment: ""):
                self = .newArrivalOrder

            default:
                return nil
        }
        */
        //mSearchSortKindEnum=
    }

    public SearchSortType searchWebSortType() {
        switch (mSearchSortKindEnum) {
            case none:
                return SearchSortType.none;
            case popularity:
                return SearchSortType.populatiry;
            case newArrivalOrder:
                return SearchSortType.newest;
            default:
                DTVTLogger.error("SortKind is not exist. mSearchSortKindEnum=" + mSearchSortKindEnum);
                return SearchSortType.none;
        }
    }
}