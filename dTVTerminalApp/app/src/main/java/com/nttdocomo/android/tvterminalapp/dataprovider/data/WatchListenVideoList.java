/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WatchListenVideoList implements Serializable {

    private static final long serialVersionUID = 9013746905348284666L;

    private HashMap<String, String> mVcMap = new HashMap<>();
    private List<HashMap<String, String>> mVcList = new ArrayList<>();

    public HashMap getVcMap() {
        return mVcMap;
    }

    public void setVcMap(HashMap<String, String> vcMap) {
        this.mVcMap = vcMap;
    }

    public List getVcList() {
        return mVcList;
    }

    public void setVcList(List<HashMap<String, String>> vcList) {
        this.mVcList = vcList;
    }
}
