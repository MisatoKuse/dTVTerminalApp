/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ClipKeyListResponse.
 */
public class ClipKeyListResponse {

    /**
     * response body.
     */
    private String mStatus = null;
    /**
     * アップデートフラグ.
     */
    private boolean mIsUpdate = false;
    /**
     * response body list.
     */
    private List<HashMap<String, String>> mCkList = new ArrayList<>();

    /**
     * ClipKeyListResponse構造.
     */
    public ClipKeyListResponse() {
    }

    /**
     * ステータス設定する.
     * @param status  ステータス
     */
    public void setStatus(final String status) {
        mStatus = status;
    }

    /**
     * ステータス取得する.
     * @return ステータス
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * アップデート要否設定.
     * @param isUpdate アップデート要否フラグ.
     */
    public void setIsUpdate(final boolean isUpdate) {
        mIsUpdate = isUpdate;
    }

    /**
     * アップデート要否取得.
     * @return アップデート要否
     */
    public boolean getIsUpdate() {
        return mIsUpdate;
    }

    /**
     * response body list取得 .
     * @return response body list
     */
    public List getCkList() {
        return mCkList;
    }

    /**
     * response body list設定 .
     * @param list レスポンスボディーリスト
     */
    public void setCkList(final List<HashMap<String, String>> list) {
        this.mCkList = list;
    }

}
