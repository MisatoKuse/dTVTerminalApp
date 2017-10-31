/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoRankList {
    private HashMap<String, String> mVrMap = new HashMap<String, String>();
    private List<HashMap<String, String>> mVrList = new ArrayList<>();
    private List<HashMap<String, String>> mVrPLITList = new ArrayList<>();
    private List<HashMap<String, String>> mVrPlicenseList = new ArrayList<>();

    /**
     * 拡張情報
     **/
    private Bundle mExtraData = null;

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

    /**
     * 拡張情報のセッター
     *
     * @param extraDataSrc 代入する拡張情報
     */
    public void setExtraData(Bundle extraDataSrc) {
        mExtraData = extraDataSrc;
    }

    /**
     * 拡張情報のゲッター
     *
     * @return 拡張情報
     */
    public Bundle getExtraData() {
        return mExtraData;
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
