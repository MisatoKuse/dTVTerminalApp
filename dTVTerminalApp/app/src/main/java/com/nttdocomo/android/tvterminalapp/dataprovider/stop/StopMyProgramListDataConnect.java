/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.MyChannelDataProvider;

/**
 * マイ番組表リスト取得の通信を止める.
 */
public class StopMyProgramListDataConnect extends AsyncTask<MyChannelDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopMyProgramListDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final MyChannelDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (MyChannelDataProvider myChannelDataProvider : dataProviders) {
                if (myChannelDataProvider != null) {
                    myChannelDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
