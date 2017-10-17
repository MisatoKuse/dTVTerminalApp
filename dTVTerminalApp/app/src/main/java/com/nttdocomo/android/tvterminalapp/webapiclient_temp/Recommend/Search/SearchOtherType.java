package com.nttdocomo.android.tvterminalapp.webapiclient.recommend.search;


import com.nttdocomo.android.tvterminalapp.model.Search.SearchNarrowCondition;

public class SearchOtherType extends SearchFilterTypeMappable {
    public final int HDContent = SearchNarrowCondition.getNextOrdinal();

    private int self=SearchNarrowCondition.sEnumOrdinalNil;

    public SearchOtherType(String name){
        if("SearchOtherTypeHDContent".equals(name)){
            self=HDContent;
        }
    }

    @Override
    public SearchFilterType searchFilterType() {
        if(HDContent == self){
            return SearchFilterType.otherHdWork;
        }
        return null;
    }
}
