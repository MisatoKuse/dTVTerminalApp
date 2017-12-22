/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.model.ResultType;
import com.nttdocomo.android.tvterminalapp.model.search.SearchContentInfo;

import java.util.ArrayList;
import java.util.List;

public class TotalSearchContentInfo {
    public int totalCount;
    public ArrayList<SearchContentInfo> searchContentInfo;

    private ResultType mResultType;

    private List<ContentsData> mContentsDataList = new ArrayList<>();

    public void init(int totalCount, ArrayList<SearchContentInfo> searchContentInfo){
        this.totalCount=totalCount;
        this.searchContentInfo = searchContentInfo;
    }


    public void init(ResultType resultType) {
        mResultType=resultType;
    }

    public List<ContentsData> getContentsDataList() {
        return mContentsDataList;
    }

    public void setContentsDataList(List<ContentsData> mContentsDataList) {
        this.mContentsDataList = mContentsDataList;
    }
}
