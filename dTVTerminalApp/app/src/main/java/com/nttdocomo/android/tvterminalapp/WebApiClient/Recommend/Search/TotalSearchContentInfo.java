package com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search;


import com.nttdocomo.android.tvterminalapp.Model.ResultType;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchContentInfo;

import java.util.ArrayList;

public class TotalSearchContentInfo {
    public int totalCount;
    public ArrayList<SearchContentInfo> searchContentInfo;

    private ResultType mResultType;


    public void init(int totalCount, ArrayList<SearchContentInfo> searchContentInfo){
        this.totalCount=totalCount;
        this.searchContentInfo = searchContentInfo;
    }


    public void init(ResultType resultType) {
        mResultType=resultType;
    }

}
