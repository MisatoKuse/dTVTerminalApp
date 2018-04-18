/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;

/**
 * 購入済みVOD一覧取得：正常時レスポンスデータ.
 */
public class PurchasedVodListResponse {
    /**status.*/
    private String mStatus = null;
    /** 購入済みVOD一覧リスト.*/
    private ArrayList<VodMetaFullData> mVodMetaFullData = new ArrayList<>();
    /**有効期限一覧.*/
    private ArrayList<ActiveData> mActiveData = new ArrayList<>();
    /**
     * status取得.
     * @return status
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * status設定.
     * @param status status
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * 購入済みVOD一覧リスト取得.
     * @return 購入済みVOD一覧リスト
     */
    public ArrayList<VodMetaFullData> getVodMetaFullData() {
        return mVodMetaFullData;
    }

    /**
     * 購入済みVOD一覧リスト設定.
     * @param vodMetaFullData 購入済みVOD一覧リスト
     */
    public void setVodMetaFullData(final ArrayList<VodMetaFullData> vodMetaFullData) {
        mVodMetaFullData = vodMetaFullData;
    }

    /**
     * 有効期限一覧取得.
     * @return 有効期限一覧
     */
    public ArrayList<ActiveData> getVodActiveData() {
        return mActiveData;
    }

    /**
     * 有効期限一覧設定.
     * @param vodActiveData 有効期限一覧
     */
    public void setVodActiveData(final ArrayList<ActiveData> vodActiveData) {
        mActiveData = vodActiveData;
    }
    /**
     * コンストラクタ.
     */
    public PurchasedVodListResponse() {
        mStatus = "";     // OK 固定値
        mVodMetaFullData = new ArrayList<VodMetaFullData>();  // 購入済みVOD一覧
        mActiveData = new ArrayList<ActiveData>();
    }
}