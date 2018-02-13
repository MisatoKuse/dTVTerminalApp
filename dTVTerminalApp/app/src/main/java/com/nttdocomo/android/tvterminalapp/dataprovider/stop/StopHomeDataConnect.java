/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.HomeDataProvider;

/**
 * HomeDataProviderの通信を止める.
 */
public class StopHomeDataConnect extends AsyncTask<HomeDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopHomeDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final HomeDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (HomeDataProvider homeDataProvider : dataProviders) {
                if (homeDataProvider != null) {
                    homeDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
