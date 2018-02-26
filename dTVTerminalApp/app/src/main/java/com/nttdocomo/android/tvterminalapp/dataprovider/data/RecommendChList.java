/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendChList {
    private HashMap<String, String> mRcMap = new HashMap<String, String>();
    private List<Map<String, String>> mRcList = new ArrayList<>();

    public HashMap<String, String> getmRcMap() {
        return mRcMap;
    }

    public void setmRcMap(final HashMap<String, String> mRcMap) {
        this.mRcMap = mRcMap;
    }

    public List<Map<String, String>> getmRcList() {
        return mRcList;
    }

    public void setmRcList(final List<Map<String, String>> mRcList) {
        this.mRcList = mRcList;
    }

}

