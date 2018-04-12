/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.struct.Pager;

import java.util.ArrayList;

/**
 * 子コンテンツ一覧取得APIパース用
 */
public class ChildContentListGetResponse {

    private String mStatus;
    private Pager mPager;
    private ArrayList<VodMetaFullData> mVodMetaFullData;

    public ChildContentListGetResponse() {
        mStatus = "";
        mPager = new Pager();
        mVodMetaFullData = new ArrayList<>();
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(final String status) {
        mStatus = status;
    }

    public Pager getPager() {
        return mPager;
    }

    public void setPager(final int limit, final int offset, final int count, final int total) {
        mPager.setLimit(limit);
        mPager.setOffset(offset);
        mPager.setCount(count);
        mPager.setTotal(total);
    }

    public ArrayList<VodMetaFullData> getVodMetaFullData() {
        return mVodMetaFullData;
    }

    public void setVodMetaFullData(final ArrayList<VodMetaFullData> vodMetaFullData) {
        mVodMetaFullData = vodMetaFullData;
    }
}

