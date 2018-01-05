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

        public String getmContractStatus() {
            return mContractStatus;
        }

        public void setmContractStatus(String contractStatus) {
            if (contractStatus != null) {
                this.mContractStatus = contractStatus;
            }
        }

        public String getmDchAgeReq() {
            return mDchAgeReq;
        }

        public void setmDchAgeReq(String dchAgeReq) {
            if (dchAgeReq != null) {
                this.mDchAgeReq = dchAgeReq;
            }
        }

        public String getmH4dAgeReq() {
            return mH4dAgeReq;
        }

        public void setmH4dAgeReq(String h4dAgeReq) {
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

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public List<AccountList> getmLoggedinAccount() {
        //リクエストユーザデータの取得
        return mLoggedinAccount;
    }

    public void setmLoggedinAccount(List<AccountList> mLoggedinAccount) {
        //リクエストユーザデータのセット
        this.mLoggedinAccount = mLoggedinAccount;
    }

    public List<AccountList> getmH4dContractedAccount() {
        //H4D契約ユーザデータの取得
        return mH4dContractedAccount;
    }

    public void setmH4dContractedAccount(List<AccountList> mH4dContractedAccount) {
        //H4D契約ユーザデータのセット
        this.mH4dContractedAccount = mH4dContractedAccount;
    }
}
