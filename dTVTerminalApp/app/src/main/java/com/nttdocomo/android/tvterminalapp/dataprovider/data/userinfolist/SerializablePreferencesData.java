/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;

import java.io.Serializable;
import java.util.List;

/**
 * ユーザ情報永続化用構造体.
 */
public class SerializablePreferencesData implements Serializable {

    /**
     * Serializableバージョン.
     */
    private static final long serialVersionUID = 730336406251335293L;
    /**
     * ユーザ情報リスト.
     */
    private List<UserInfoList> mUserInfoList;

    /**
     * ユーザ情報リスト取得.
     *
     * @return ユーザ情報リスト
     */
    public List<UserInfoList> getUserInfoList() {
        return mUserInfoList;
    }

    /**
     * ユーザ情報リスト保存.
     *
     * @param mUserInfoList ユーザ情報リスト
     */
    public void setUserInfoList(final List<UserInfoList> mUserInfoList) {
        this.mUserInfoList = mUserInfoList;
    }
}
