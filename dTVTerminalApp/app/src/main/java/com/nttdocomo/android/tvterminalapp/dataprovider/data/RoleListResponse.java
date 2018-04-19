/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ロールデータレスポンス.
 */
public class RoleListResponse implements Serializable {

    private static final long serialVersionUID = -1211331811012005279L;

    /**ロールデータを収めるハッシュマップ.*/
    private ArrayList<RoleListMetaData> mRoleList = new ArrayList<>();
    /**
     * ロールデータ取得.
     * @return  ロールデータ
     */
    public ArrayList<RoleListMetaData> getRoleList() {
        return mRoleList;
    }
    /**
     * ロールデータ設定.
     * @param mRoleList ロールデータ
     */
    public void setRoleList(final ArrayList<RoleListMetaData> mRoleList) {
        this.mRoleList = mRoleList;
    }
}