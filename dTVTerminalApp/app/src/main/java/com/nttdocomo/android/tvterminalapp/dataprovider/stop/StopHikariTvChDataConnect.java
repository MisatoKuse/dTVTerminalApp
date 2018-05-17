/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.HikariTvChannelDataProvider;

/**
 * おすすめ番組・ビデオ取得で行っている通信を止める.
 */

public class StopHikariTvChDataConnect extends AsyncTask<HikariTvChannelDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopHikariTvChDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final HikariTvChannelDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (HikariTvChannelDataProvider hikariTvChannelDataProvider : dataProviders) {
                if (hikariTvChannelDataProvider != null) {
                    hikariTvChannelDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
