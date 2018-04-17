/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;

/**
 * マイチャンネル解除取得：正常時レスポンスデータ.
 */
public class MyChannelDeleteResponse implements Serializable {
    private static final long serialVersionUID = -171253737402592759L;
    /**ステータス.*/
    private String mStatus;
    /**ステータス初期値.*/
    private static final String MY_CHANNEL_DELETE_FIXED_STATUS = "";

    /**
     * ステータス取得.
     * @return ステータス
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * ステータス設定.
     * @param status ステータス
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * コンストラクタ.
     */
    public MyChannelDeleteResponse() {
        mStatus = MY_CHANNEL_DELETE_FIXED_STATUS;     // OK 固定値
    }
}
