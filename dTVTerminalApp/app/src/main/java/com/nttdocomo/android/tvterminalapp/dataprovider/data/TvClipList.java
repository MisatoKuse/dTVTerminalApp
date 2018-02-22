/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TvClipList implements Serializable {

    private static final long serialVersionUID = 3389140211376432479L;
    private HashMap<String, String> mTcMap = new HashMap<>();
    private List<HashMap<String, String>> mTcList = new ArrayList<>();

    public HashMap getVcMap() {
        return mTcMap;
    }

    public void setVcMap(HashMap tcMap) {
        this.mTcMap = tcMap;
    }

    public List getVcList() {
        return mTcList;
    }

    public void setVcList(List tcList) {
        this.mTcList = tcList;
    }
}
