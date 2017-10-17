package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class TvScheduleList{
    private HashMap<String, String> mTvsMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mTvsList = new ArrayList<>();

    public HashMap getTvsMap() {
        return mTvsMap;
    }

    public void setTvsMap(HashMap tvsMap) {
        this.mTvsMap = tvsMap;
    }

    public List geTvsList() {
        return mTvsList;
    }

    public void setTvsList(List tvsList) {
        this.mTvsList = tvsList;
    }
}
