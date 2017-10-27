/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScaledDownProgramChannelList implements Serializable {

    private HashMap<String, String> mVcMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mVcList = new ArrayList<>();

    public HashMap getVcMap() {
        return mVcMap;
    }

    public void setVcMap(HashMap vcMap) {
        this.mVcMap = vcMap;
    }

    public List getVcList() {
        return mVcList;
    }

    public void setVcList(List vcList) {
        this.mVcList = vcList;
    }
}
