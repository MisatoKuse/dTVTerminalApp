package com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search;


import com.nttdocomo.android.tvterminalapp.Model.Search.SearchNarrowCondition;

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
