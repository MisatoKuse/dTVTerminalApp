/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;

/**
 * マイチャンネル登録取得：正常時レスポンスデータ.
 */
public class MyChannelRegisterResponse implements Serializable {
    private static final long serialVersionUID = -171253737402592759L;

    private String mStatus; // status

    private static final String MY_CHANNEL_FIXED_STATUS = "";

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * コンストラクタ.
     */
    public MyChannelRegisterResponse() {
        mStatus = MY_CHANNEL_FIXED_STATUS;     // OK 固定値
    }
}
