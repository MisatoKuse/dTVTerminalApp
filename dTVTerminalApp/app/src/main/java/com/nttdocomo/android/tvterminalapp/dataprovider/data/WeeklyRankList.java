package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class WeeklyRankList {
    private HashMap<String, String> mWrMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mWrList = new ArrayList<>();

    public HashMap geWrMap() {
        return mWrMap;
    }

    public void setWrMap(HashMap wrMap) {
        this.mWrMap = wrMap;
    }

    public List getWrList() {
        return mWrList;
    }

    public void setWrList(List wrList) {
        this.mWrList = wrList;
    }
}

