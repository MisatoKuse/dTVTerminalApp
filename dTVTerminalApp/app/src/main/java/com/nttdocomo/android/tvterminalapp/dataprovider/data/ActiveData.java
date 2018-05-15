/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.tvterminalapp.dataprovider.data;

/**
 * 購入情報.
 */
public class ActiveData {

    /**
     * ライセンスID.
     */
    private String mLicenseId;

    /**
     * 有効期限.
     */
    private long mValidEndDate;

    /**
     * コンストラクタ.
     */
    public ActiveData() {
        mLicenseId = "";
        mValidEndDate = 0;
    }

    /**
     * ライセンスID取得.
     * @return ライセンスID
     */
    public String getLicenseId() {
        return mLicenseId;
    }

    /**
     * ライセンスID設定.
     * @param licenseId  ライセンスID
     */
    public void setLicenseId(final String licenseId) {
        this.mLicenseId = licenseId;
    }

    /**
     * 有効期限取得.
     * @return 有効期限
     */
    public long getValidEndDate() {
        return mValidEndDate;
    }

    /**
     * 有効期限設定.
     * @param validEndDate  有効期限
     */
    public void setValidEndDate(final long validEndDate) {
        this.mValidEndDate = validEndDate;
    }

}