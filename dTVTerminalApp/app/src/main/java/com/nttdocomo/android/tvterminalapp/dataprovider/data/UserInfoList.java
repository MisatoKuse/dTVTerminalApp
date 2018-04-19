/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist.AccountList;

import java.io.Serializable;
import java.util.List;

/**
 * ユーザ情報クラス.
 */
public class UserInfoList implements Serializable {

    /**
     * バージョンUID.
     */
    private static final long serialVersionUID = 1279582890842645545L;

    /**
     * コンストラクタ.
     */
    public UserInfoList() {
        //ステータスはOKで初期化
        mStatus = JsonConstants.META_RESPONSE_STATUS_OK;
        mLoggedinAccount = null;
        mH4dContractedAccount = null;
    }

    /**
     * ステータス.
     */
    private String mStatus;

    /**
     * リクエストユーザデータのマップ.
     */
    private List<AccountList> mLoggedinAccount;

    /**
     * リクエストユーザがH4Dサブアカウントである場合のH4D契約ユーザデータ.
     */
    private List<AccountList> mH4dContractedAccount;

    /**
     * ステータス取得.
     * @return ステータス
     */
    public String getStatus() {
        return mStatus;
    }

    /**
     * ステータス設定.
     * @param mStatus ステータス
     */
    public void setStatus(final String mStatus) {
        this.mStatus = mStatus;
    }

    /**
     * リクエストユーザデータのマップ取得.
     * @return リクエストユーザデータのマップ
     */
    public List<AccountList> getLoggedinAccount() {
        //リクエストユーザデータの取得
        return mLoggedinAccount;
    }

    /**
     * リクエストユーザデータのマップ設定.
     * @param mLoggedinAccount リクエストユーザデータのマップ
     */
    public void setLoggedinAccount(final List<AccountList> mLoggedinAccount) {
        //リクエストユーザデータのセット
        this.mLoggedinAccount = mLoggedinAccount;
    }

    /**
     * リクエストユーザがH4Dサブアカウントである場合のH4D契約ユーザデータ取得.
     * @return リクエストユーザがH4Dサブアカウントである場合のH4D契約ユーザデータ
     */
    public List<AccountList> getH4dContractedAccount() {
        //H4D契約ユーザデータの取得
        return mH4dContractedAccount;
    }

    /**
     * リクエストユーザがH4Dサブアカウントである場合のH4D契約ユーザデータ設定.
     * @param mH4dContractedAccount リクエストユーザがH4Dサブアカウントである場合のH4D契約ユーザデータ
     */
    public void setH4dContractedAccount(final List<AccountList> mH4dContractedAccount) {
        //H4D契約ユーザデータのセット
        this.mH4dContractedAccount = mH4dContractedAccount;
    }
}
