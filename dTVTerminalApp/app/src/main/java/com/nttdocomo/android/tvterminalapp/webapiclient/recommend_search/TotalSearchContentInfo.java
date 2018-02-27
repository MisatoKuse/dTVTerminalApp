/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ResultType;
import com.nttdocomo.android.tvterminalapp.struct.SearchContentInfo;

import java.util.ArrayList;
import java.util.List;

public class TotalSearchContentInfo {
    public int totalCount;
    public ArrayList<SearchContentInfo> searchContentInfo;

    private ResultType mResultType;

    private List<ContentsData> mContentsDataList = new ArrayList<>();

    public void init(final int totalCount, final ArrayList<SearchContentInfo> searchContentInfo) {
        this.totalCount = totalCount;
        this.searchContentInfo = searchContentInfo;
    }


    public void init(final ResultType resultType) {
        mResultType = resultType;
    }

    public List<ContentsData> getContentsDataList() {
        return mContentsDataList;
    }

    public void setContentsDataList(final List<ContentsData> mContentsDataList) {
        this.mContentsDataList = mContentsDataList;
    }
}
