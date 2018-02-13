/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.WatchListenVideoListDataProvider;

/**
 * 視聴中ビデオリスト取得の通信を止める.
 */
public class StopWatchListenVideoListDataConnect extends AsyncTask<WatchListenVideoListDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopWatchListenVideoListDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final WatchListenVideoListDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (WatchListenVideoListDataProvider watchListenVideoListDataProvider : dataProviders) {
                watchListenVideoListDataProvider.stopConnect();
            }
        }
        return null;
    }
}
