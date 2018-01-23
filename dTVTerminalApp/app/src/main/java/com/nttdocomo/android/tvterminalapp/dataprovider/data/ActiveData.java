/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;

/**
 * 購入情報.
 */
public class ActiveData implements Serializable {
    private static final long serialVersionUID = -4091200326941004981L;

    /**
     * ライセンスID.
     */
    private String licenseId;

    /**
     * 有効期限.
     */
    private long validEndDate;

    /**
     * コンストラクタ.
     */
    public ActiveData() {
        licenseId = "";
        validEndDate = 0;
    }

    /**
     * ライセンスID取得.
     * @return ライセンスID
     */
    public String getLicenseId() {
        return licenseId;
    }

    /**
     * ライセンスID設定.
     * @param licenseId  ライセンスID
     */
    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    /**
     * 有効期限取得.
     * @return 有効期限
     */
    public long getValidEndDate() {
        return validEndDate;
    }

    /**
     * 有効期限設定.
     * @param validEndDate  有効期限
     */
    public void setValidEndDate(long validEndDate) {
        this.validEndDate = validEndDate;
    }

}