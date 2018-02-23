/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.HashMap;
import java.util.List;

/**
 * サービストークン情報.
 */
class ServiceTokenData {
    /**
     * WebAPIレスポンス情報.
     */
    private HashMap<String, String> mResponseInfoMap = null;

    /**
     * トークン情報.
     */
    private List<HashMap<String, String>> mToken = null;

    /**
     * WebAPIレスポンス情報取得.
     *
     * @return レスポンス情報
     */
    public HashMap getResponseInfoMap() {
        return mResponseInfoMap;
    }

    /**
     * WebAPIレスポンス情報設定.
     *
     * @param responseInfoMap レスポンス情報
     */
    public void setResponseInfoMap(HashMap<String, String> responseInfoMap) {
        this.mResponseInfoMap = responseInfoMap;
    }

    /**
     * WebAPIレスポンス情報情報取得.
     *
     * @return トークン情報
     */
    public List<HashMap<String, String>> getToken() {
        return mToken;
    }

    /**
     * トークン情報設定.
     *
     * @param tokenData トークンリスト情報
     */
    public void setToken(List<HashMap<String, String>> tokenData) {
        mToken = tokenData;
    }
}
