/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RentalDataProvider;

/**
 * レンタルVODリスト取得の通信を止める.
 */
public class StopRentalDataConnect extends AsyncTask<RentalDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopRentalDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final RentalDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (RentalDataProvider rentalDataProvider : dataProviders) {
                if (rentalDataProvider != null) {
                    rentalDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
