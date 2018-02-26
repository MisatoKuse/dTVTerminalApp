/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * マイチャンネル登録取得：正常時レスポンスデータ.
 */
public class MyChannelListResponse implements Serializable {
    private static final long serialVersionUID = -171253737402592759L;

    private String mStatus; // status
    private int mCount; //count
    private ArrayList<MyChannelMetaData> myChannelMetaData;  // マイチャンネル一覧リスト
    private static final int MY_CHANNEL_INFO_INIT_COUNT = 0;
    private static final String MY_CHANNEL_FIXED_STATUS = "";

    public String getStatus() {
        return mStatus;
    }

    public int getCount() {
        return mCount;
    }

    public void setStatus(final String status) {
        mStatus = status;
    }

    public void setCount(final int count) {
        mCount = count;
    }

    public ArrayList<MyChannelMetaData> getMyChannelMetaData() {
        return myChannelMetaData;
    }

    /**
     * コンストラクタ.
     * @param myChannelMetaData マイチャンネル一覧メタデータ
     */
    public void setMyChannelMetaData(final ArrayList<MyChannelMetaData> myChannelMetaData) {
        this.myChannelMetaData = myChannelMetaData;
    }

    /**
     * コンストラクタ.
     */
    public MyChannelListResponse() {
        mStatus = MY_CHANNEL_FIXED_STATUS;     // OK 固定値
        mCount = MY_CHANNEL_INFO_INIT_COUNT;      //取得した予約情報の件数
        myChannelMetaData = new ArrayList<>();  // マイチャンネル一覧
    }
}
