/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;

public class RoleListResponse implements Serializable {

    private static final long serialVersionUID = -1211331811012005279L;

    //複数のジャンルの種別を収めるハッシュマップ
    private ArrayList<RoleListMetaData> mRoleList = new ArrayList<>();

    public ArrayList<RoleListMetaData> getRoleList() {
        return mRoleList;
    }

    public void setRoleList(final ArrayList<RoleListMetaData> mRoleList) {
        this.mRoleList = mRoleList;
    }
}