/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;

/**
 * おすすめ番組・ビデオ取得で行っている通信を止める.
 */
public class StopRecommendDataConnect extends AsyncTask<RecommendDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopRecommendDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final RecommendDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (RecommendDataProvider recommendDataProvider : dataProviders) {
                if (recommendDataProvider != null) {
                    recommendDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}

