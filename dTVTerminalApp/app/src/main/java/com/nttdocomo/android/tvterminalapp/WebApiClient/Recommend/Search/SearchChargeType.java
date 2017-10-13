package com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search;


import com.nttdocomo.android.tvterminalapp.Model.Search.SearchNarrowCondition;

public class SearchChargeType extends SearchFilterTypeMappable {
    public final int free = SearchNarrowCondition.getNextOrdinal();
    public final int rental =SearchNarrowCondition.getNextOrdinal();

    private int self=SearchNarrowCondition.sEnumOrdinalNil;

    public SearchChargeType(String name){
        if("SearchChargeTypeFree".equals(name)){
            self=free;
        } else if("SearchChargeTypeRental".equals(name)){
            self=rental;
        }
    }

    @Override
    public SearchFilterType searchFilterType() {
        if(free==self){
            return SearchFilterType.chargeUnlimited;
        } else if(rental==self){
            return SearchFilterType.chargeRental;
        }

        return null;
    }
}
