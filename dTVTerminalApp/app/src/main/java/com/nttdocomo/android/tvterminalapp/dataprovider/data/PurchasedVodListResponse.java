/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 購入済みVOD一覧取得：正常時レスポンスデータ
 */

public class PurchasedVodListResponse implements Serializable {

    private static final long serialVersionUID = 11185212718261137L;

    private String mStatus;                                 // status
    private ArrayList<VodMetaFullData> mVodMetaFullData;  // 購入済みVOD一覧リスト
    private ArrayList<ActiveData> mActiveData;             //有効期限一覧

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public ArrayList<VodMetaFullData> getVodMetaFullData() {
        return mVodMetaFullData;
    }

    public void setVodMetaFullData(ArrayList<VodMetaFullData> vodMetaFullData) {
        mVodMetaFullData = vodMetaFullData;
    }

    public ArrayList<ActiveData> getVodActiveData() {
        return mActiveData;
    }

    public void setVodActiveData(ArrayList<ActiveData> vodActiveData) {
        mActiveData = vodActiveData;
    }

    public PurchasedVodListResponse() {
        mStatus = "";     // OK 固定値
        mVodMetaFullData = new ArrayList<VodMetaFullData>();  // 購入済みVOD一覧
        mActiveData = new ArrayList<ActiveData>();
    }
}
