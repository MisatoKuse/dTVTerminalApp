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
    /**ステータス.*/
    private String mStatus;
    /**マイチャンネル件数.*/
    private int mCount;
    /**マイチャンネル一覧リスト.*/
    private ArrayList<MyChannelMetaData> myChannelMetaData;
    /**マイチャンネル件数初期値.*/
    private static final int MY_CHANNEL_INFO_INIT_COUNT = 0;
    /**ステータス初期値.*/
    private static final String MY_CHANNEL_FIXED_STATUS = "";
    /**
     * ステータス取得.
     * @return ステータス
     */
    public String getStatus() {
        return mStatus;
    }
    /**
     * マイチャンネル件数取得.
     * @return マイチャンネル件数
     */
    public int getCount() {
        return mCount;
    }
    /**
     * ステータス設定.
     * @param status ステータス
     */
    public void setStatus(final String status) {
        mStatus = status;
    }
    /**
     * マイチャンネル件数設定.
     *@param count マイチャンネル件数
     */
    public void setCount(final int count) {
        mCount = count;
    }
    /**
     * マイチャンネルメタデータ取得.
     * @return  myChannelMetaData マイチャンネルメタデータ
     */
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
