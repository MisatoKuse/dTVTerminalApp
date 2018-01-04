/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserInfoList {
    private HashMap<String, String> mUiMap = new HashMap<>();
    private List<HashMap<String, String>> mLoggedinAccountList = new ArrayList<>();
    private List<HashMap<String, String>> mH4dAccountList = new ArrayList<>();

    public List getLoggedinAccountList() {
        return mLoggedinAccountList;
    }

    public void setLoggedinAccountList(List loggedinAccountList) {
        this.mLoggedinAccountList = loggedinAccountList;
    }

    public List getH4dAccountList() {
        return mH4dAccountList;
    }

    public void setH4dAccountList(List h4dAccountList) {
        this.mH4dAccountList = h4dAccountList;
    }
}
