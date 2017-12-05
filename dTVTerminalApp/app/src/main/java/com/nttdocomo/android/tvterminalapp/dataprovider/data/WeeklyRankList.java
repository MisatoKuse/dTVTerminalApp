/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeeklyRankList {
    private HashMap<String, String> mWrMap = new HashMap<>();
    private List<HashMap<String, String>> mWrList = new ArrayList<>();

    /**
     * 拡張情報
     **/
    private Bundle extraData = null;

    public HashMap getWrMap() {
        return mWrMap;
    }

    public void setWrMap(HashMap wrMap) {
        this.mWrMap = wrMap;
    }

    public List getWrList() {
        return mWrList;
    }

    public void setWrList(List wrList) {
        this.mWrList = wrList;
    }

    /**
     * 拡張情報のセッター
     *
     * @param extraDataSrc 代入する拡張情報
     */
    public void setExtraData(Bundle extraDataSrc) {
        extraData = extraDataSrc;
    }

    /**
     * 拡張情報のセッター
     *
     * @return 拡張情報
     */
    public Bundle getExtraData() {
        return extraData;
    }
}