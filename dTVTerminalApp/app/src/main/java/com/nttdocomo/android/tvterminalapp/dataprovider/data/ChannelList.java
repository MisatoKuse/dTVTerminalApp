/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ChannelList implements Serializable {

    private HashMap<String, String> mClMap = new HashMap<>();
    private List<HashMap<String, String>> mClList = new ArrayList<>();

    public HashMap getClMap() {
        return mClMap;
    }

    public void setClMap(HashMap clMap) {
        this.mClMap = clMap;
    }

    public List getClList() {
        return mClList;
    }

    public void setClList(List clList) {
        this.mClList = clList;
    }

}
