/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VodClipList implements Serializable {

    private static final long serialVersionUID = -1638810113668563728L;
    private HashMap<String, String> mVcMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mVcList = new ArrayList<>();

    public HashMap getVcMap() {
        return mVcMap;
    }

    public void setVcMap(final HashMap<String, String> vcMap) {
        this.mVcMap = vcMap;
    }

    public List getVcList() {
        return mVcList;
    }

    public void setVcList(final List<HashMap<String, String>> vcList) {
        this.mVcList = vcList;
    }
}
