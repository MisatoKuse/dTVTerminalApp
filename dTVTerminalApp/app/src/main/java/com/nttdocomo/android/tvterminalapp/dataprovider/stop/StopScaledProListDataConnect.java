/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;

/**
 * チャンネル番組取得で行っている通信を止める.
 */
public class StopScaledProListDataConnect extends AsyncTask<ScaledDownProgramListDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopScaledProListDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final ScaledDownProgramListDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (ScaledDownProgramListDataProvider scaledDownProgramListDataProvider : dataProviders) {
                scaledDownProgramListDataProvider.stopConnect();
            }
        }
        return null;
    }
}
