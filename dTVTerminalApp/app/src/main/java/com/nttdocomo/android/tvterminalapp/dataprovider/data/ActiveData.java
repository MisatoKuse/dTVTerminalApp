/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;

/**
 * 有効期限一覧
 */
public class ActiveData implements Serializable {
    private static final long serialVersionUID = -4091200326941004981L;

    // ライセンスID
    private String licenseId;
    // 有効期限
    private long validEndDate;

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public long getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(long validEndDate) {
        this.validEndDate = validEndDate;
    }

    /**
     * コンストラクタ
     */
    public ActiveData() {
        licenseId = "";
        validEndDate = 0;
    }
}