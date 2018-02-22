/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist.AccountList;

import java.util.List;

public class UserInfoList {
    /**
     * コンストラクタ
     */
    public UserInfoList() {
        //ステータスはOKで初期化
        mStatus = JsonConstants.META_RESPONSE_STATUS_OK;
        mLoggedinAccount = null;
        mH4dContractedAccount = null;
    }

    /**
     * ステータス
     */
    private String mStatus;

    /**
     * リクエストユーザデータのマップ
     */
    private List<AccountList> mLoggedinAccount;

    /**
     * リクエストユーザがH4Dサブアカウントである場合のH4D契約ユーザデータ
     */
    private List<AccountList> mH4dContractedAccount;

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public List<AccountList> getLoggedinAccount() {
        //リクエストユーザデータの取得
        return mLoggedinAccount;
    }

    public void setLoggedinAccount(List<AccountList> mLoggedinAccount) {
        //リクエストユーザデータのセット
        this.mLoggedinAccount = mLoggedinAccount;
    }

    public List<AccountList> getH4dContractedAccount() {
        //H4D契約ユーザデータの取得
        return mH4dContractedAccount;
    }

    public void setH4dContractedAccount(List<AccountList> mH4dContractedAccount) {
        //H4D契約ユーザデータのセット
        this.mH4dContractedAccount = mH4dContractedAccount;
    }
}
