/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;

import java.util.List;

public class UserInfoList {
    /**
     * コンストラクタ
     */
    public UserInfoList() {
        //ステータスはOKで初期化
        mStatus = JsonContents.META_RESPONSE_STATUS_OK;
        mLoggedinAccount = null;
        mH4dContractedAccount = null;
    }

    /**
     * 契約情報構造体
     */
    public static class AccountList {
        /**
         * 契約状態
         */
        private String mContractStatus;
        /**
         * dTVチャンネル視聴年齢値
         */
        private String mDchAgeReq;
        /**
         * ひかりTVfordocomo視聴年齢値
         */
        private String mH4dAgeReq;

        /**
         * 初期化用コンストラクタ
         */
        public AccountList() {
            this.mContractStatus = "";
            this.mDchAgeReq = "";
            this.mH4dAgeReq = "";
        }

        public String getContractStatus() {
            return mContractStatus;
        }

        public void setContractStatus(String contractStatus) {
            if (contractStatus != null) {
                this.mContractStatus = contractStatus;
            }
        }

        public String getDchAgeReq() {
            return mDchAgeReq;
        }

        public void setDchAgeReq(String dchAgeReq) {
            if (dchAgeReq != null) {
                this.mDchAgeReq = dchAgeReq;
            }
        }

        public String getH4dAgeReq() {
            return mH4dAgeReq;
        }

        public void setH4dAgeReq(String h4dAgeReq) {
            if (h4dAgeReq != null) {
                this.mH4dAgeReq = h4dAgeReq;
            }
        }
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
