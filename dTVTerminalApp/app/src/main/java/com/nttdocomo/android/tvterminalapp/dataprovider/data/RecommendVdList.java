package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class RecommendVdList {
    private HashMap<String, String> mRvMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mRvList = new ArrayList<>();

    public HashMap<String, String> getmRvMap() {
        return mRvMap;
    }

    public void setmRvMap(HashMap<String, String> mRvMap) {
        this.mRvMap = mRvMap;
    }

    public List<HashMap<String, String>> getmRvList() {
        return mRvList;
    }

    public void setmRvList(List<HashMap<String, String>> mRvList) {
        this.mRvList = mRvList;
    }
}

