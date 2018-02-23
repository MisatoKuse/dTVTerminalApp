/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DailyRankList implements Serializable {

    private static final long serialVersionUID = -6794322997308686784L;
    private HashMap<String, String> mDrMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mDrList = new ArrayList<>();

    public HashMap getVcMap() {
        return mDrMap;
    }

    public void setDrMap(HashMap<String, String> drMap) {
        this.mDrMap = drMap;
    }

    public List getDrList() {
        return mDrList;
    }

    public void setDrList(List drList) {
        this.mDrList = drList;
    }


}
