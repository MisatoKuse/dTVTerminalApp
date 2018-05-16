/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;

/**
 * 購入済みCh一覧取得：正常時レスポンスデータ.
 */
public class PurchasedChannelListResponse {
    /**
     * status.
     */
    private String mStatus = null;

    /**
     * 購入済みCh一覧リスト.
     */
    private ChannelList mChannelListData;

    /**
     * 有効期限一覧.
     */
    private ArrayList<ActiveData> mActiveData = new ArrayList<>();

    /**
     * statusの取得.
     *
     * @return status
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * statusの保存.
     *
     * @param status status
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * 購入済みCHリストの取得.
     *
     * @return 購入済みCHリスト
     */
    public ChannelList getChannelListData() {
        return mChannelListData;
    }

    /**
     * 購入済みCHリストの保存.
     *
     * @param channelListData 購入済みCHリスト
     */
    public void setChannelListData(final ChannelList channelListData) {
        mChannelListData = channelListData;
    }

    /**
     * 有効期限一覧情報を取得.
     *
     * @return 有効期限一覧情報
     */
    public ArrayList<ActiveData> getChActiveData() {
        return mActiveData;
    }

    /**
     * 有効期限一覧情報を保存.
     *
     * @param vodActiveData 有効期限一覧情報
     */
    public void setChActiveData(final ArrayList<ActiveData> vodActiveData) {
        mActiveData = vodActiveData;
    }

    /**
     * コンストラクタ.
     */
    public PurchasedChannelListResponse() {
        mStatus = ""; //OK 固定値
        mChannelListData = new ChannelList(); //購入済みCH一覧
        mActiveData = new ArrayList<>(); //有効期限一覧
    }
}