package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class TvClipList{
    private HashMap<String, String> mTvcMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mTvcList = new ArrayList<>();

    public HashMap getTvcMap() {
        return mTvcMap;
    }

    public void setTvcMap(HashMap tvcMap) {
        this.mTvcMap = tvcMap;
    }

    public List getTvcList() {
        return mTvcList;
    }

    public void setTvcList(List tvcList) {
        this.mTvcList = tvcList;
    }
}
