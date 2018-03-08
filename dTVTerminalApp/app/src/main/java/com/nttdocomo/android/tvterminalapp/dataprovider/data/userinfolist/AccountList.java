/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist;

import java.io.Serializable;

/**
 * 契約情報構造体.
 */
public class AccountList implements Serializable {

    /**
     * バージョン.
     */
    private static final long serialVersionUID = 5253124266346397317L;
    /**
     * 契約状態.
     */
    private String mContractStatus;
    /**
     * dTVチャンネル視聴年齢値.
     */
    private String mDchAgeReq;
    /**
     * ひかりTVfordocomo視聴年齢値.
     */
    private String mH4dAgeReq;

    /**
     * 初期化用コンストラクタ.
     */
    public AccountList() {
        this.mContractStatus = "";
        this.mDchAgeReq = "";
        this.mH4dAgeReq = "";
    }

    public String getContractStatus() {
        return mContractStatus;
    }

    public void setContractStatus(final String contractStatus) {
        if (contractStatus != null) {
            this.mContractStatus = contractStatus;
        }
    }

    public String getDchAgeReq() {
        return mDchAgeReq;
    }

    public void setDchAgeReq(final String dchAgeReq) {
        if (dchAgeReq != null) {
            this.mDchAgeReq = dchAgeReq;
        }
    }

    public String getH4dAgeReq() {
        return mH4dAgeReq;
    }

    public void setH4dAgeReq(final String h4dAgeReq) {
        if (h4dAgeReq != null) {
            this.mH4dAgeReq = h4dAgeReq;
        }
    }
}
