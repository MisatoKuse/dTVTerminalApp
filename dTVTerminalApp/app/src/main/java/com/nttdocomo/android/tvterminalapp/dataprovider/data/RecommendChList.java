/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecommendChList {
    private HashMap<String, String> mRcMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mRcList = new ArrayList<>();

    public HashMap<String, String> getmRcMap() {
        return mRcMap;
    }

    public void setmRcMap(HashMap<String, String> mRcMap) {
        this.mRcMap = mRcMap;
    }

    public List<HashMap<String, String>> getmRcList() {
        return mRcList;
    }

    public void setmRcList(List<HashMap<String, String>> mRcList) {
        this.mRcList = mRcList;
    }

}

