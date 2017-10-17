package com.nttdocomo.android.tvterminalapp.webapiclient.recommend.search;


import com.nttdocomo.android.tvterminalapp.model.Search.SearchNarrowCondition;

public class SearchDubbedType extends SearchFilterTypeMappable {

    public final int dubbed = SearchNarrowCondition.getNextOrdinal();
    public final int subtitle =SearchNarrowCondition.getNextOrdinal();
    public final int both =SearchNarrowCondition.getNextOrdinal();

    private int self=SearchNarrowCondition.sEnumOrdinalNil;

    public SearchDubbedType(String name) {
        if("SearchDubbedTypeDubbed".equals(name)){
            self=dubbed;
        } else if("SearchDubbedTypeSubtitle".equals(name)){
            self=subtitle;
        } else if("SearchDubbedTypeBoth".equals(name)){
            self=both;
        }
    }

    @Override
    public SearchFilterType searchFilterType() {
        if(dubbed == self){
            return SearchFilterType.dubbedDubbed;
        } else if(subtitle == self){
            return SearchFilterType.dubbedText;
        } else if( both == self){
            return SearchFilterType.dubbedTextAndDubbing;
        }

        return null;
    }
}
