/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClipKeyListResponse {

    /**
     * リクエスト
     */
    private ClipKeyListRequest mRequest = null;

    /**
     * response body
     */
    private HashMap<String, String> mCkRespMap = new HashMap<String, String>();
    /**
     * response body list
     */
    private List<HashMap<String, String>> mCkList = new ArrayList<>();

    public ClipKeyListResponse() {
    }

    public Map getCkRespMap() {
        return mCkRespMap;
    }

    public void setCkRespMap(HashMap map) {
        this.mCkRespMap = map;
    }

    public List getCkList() {
        return mCkList;
    }

    public void setCkList(List list) {
        this.mCkList = list;
    }

    public ClipKeyListRequest getRequest() {
        return mRequest;
    }

    public void setRequest(ClipKeyListRequest request) {
        mRequest = request;
    }
}
