/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.UserInfoDataProvider;

/**
 * ユーザ情報中断処理.
 */
public class StopUserInfoDataConnect extends AsyncTask<UserInfoDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopUserInfoDataConnect() {
    }

    @Override
    protected Void doInBackground(final UserInfoDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (UserInfoDataProvider userInfoDataProvider : dataProviders) {
                if (userInfoDataProvider != null) {
                    userInfoDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
