/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * マイチャンネル解除取得：正常時レスポンスデータ
 */
public class MyChannelDeleteResponse implements Serializable {
    private static final long serialVersionUID = -171253737402592759L;

    private String mStatus;// status
    private static final String MY_CHANNEL_DELETE_FIXED_STATUS = "";

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    /**
     * コンストラクタ
     */
    public MyChannelDeleteResponse() {
        mStatus = MY_CHANNEL_DELETE_FIXED_STATUS;     // OK 固定値
    }
}
