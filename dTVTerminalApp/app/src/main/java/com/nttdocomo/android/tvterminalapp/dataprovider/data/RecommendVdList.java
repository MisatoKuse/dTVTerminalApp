/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendVdList {
    private HashMap<String, String> mRvMap = new HashMap<String, String>();
    private List<Map<String, String>> mRvList = new ArrayList<>();

    public HashMap<String, String> getmRvMap() {
        return mRvMap;
    }

    public void setmRvMap(HashMap<String, String> mRvMap) {
        this.mRvMap = mRvMap;
    }

    public List<Map<String, String>> getmRvList() {
        return mRvList;
    }

    public void setmRvList(List<Map<String, String>> mRvList) {
        this.mRvList = mRvList;
    }
}

