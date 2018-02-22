/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 購入済みVOD一覧取得：正常時レスポンスデータ
 */
public class PurchasedVodListResponse {
    // status
    private String mStatus = null;
    // 購入済みVOD一覧リスト
    private ArrayList<VodMetaFullData> mVodMetaFullData = new ArrayList<>();
    //有効期限一覧
    private ArrayList<ActiveData> mActiveData = new ArrayList<>();

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