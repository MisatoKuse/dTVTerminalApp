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
    /**ステータス.*/
    private String mStatus; // status
    /**ステータス.*/
    private static final String MY_CHANNEL_FIXED_STATUS = "";
    /**
     * ステータス初期値取得.
     * @return ステータス
     */
    public String getStatus() {
        return mStatus;
    }
    /**
     * ステータス設定.
     * @param  status ステータス
     */
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
