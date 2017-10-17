package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class UserInfoList {
    private HashMap<String, String> mUiMap = new HashMap<>();
    private List<HashMap<String, String>> mUiList = new ArrayList<>();

    public HashMap getUiMap() {
        return mUiMap;
    }

    public void setUiMap(HashMap uiMap) {
        this.mUiMap = uiMap;
    }

    public List getUiList() {
        return mUiList;
    }

    public void setUiList(List uiList) {
        this.mUiList = uiList;
    }
}
