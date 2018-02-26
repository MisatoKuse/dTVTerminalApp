/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClipKeyListResponse {

    /**
     * response body.
     */
    private String mStatus = null;
    private boolean mIsUpdate = false;
    /**
     * response body list.
     */
    private List<HashMap<String, String>> mCkList = new ArrayList<>();

    public ClipKeyListResponse() {
    }

    public void setStatus(final String status) {
        mStatus = status;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setIsUpdate(final boolean isUpdate) {
        mIsUpdate = isUpdate;
    }

    public boolean getIsUpdate() {
        return mIsUpdate;
    }

    public List getCkList() {
        return mCkList;
    }

    public void setCkList(final List<HashMap<String, String>> list) {
        this.mCkList = list;
    }

}
