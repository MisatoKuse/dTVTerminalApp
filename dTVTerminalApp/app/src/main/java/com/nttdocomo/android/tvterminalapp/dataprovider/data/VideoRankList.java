/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoRankList {
    private HashMap<String, String> mVrMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mVrList = new ArrayList<>();
    private List<HashMap<String, String>> mVrPLITList = new ArrayList<>();
    private List<HashMap<String, String>> mVrPlicenseList = new ArrayList<>();

    public HashMap getVrMap() {
        return mVrMap;
    }

    public void setVrMap(HashMap vrMap) {
        this.mVrMap = vrMap;
    }

    public List getVrList() {
        return mVrList;
    }

    public void setVrList(List vrList) {
        this.mVrList = vrList;
    }

//    public List getVrPLITList() {
//        return mVrPLITList;
//    }
//
//    public void setVrPLITList(List rPLITList) {
//        this.mVrPLITList = rPLITList;
//    }
//
//    public List getVrPlicenseList() {
//        return mVrPlicenseList;
//    }
//
//    public void setVrPlicenseList(List vrPlicenseList) {
//        this.mVrPlicenseList = vrPlicenseList;
//    }

}
