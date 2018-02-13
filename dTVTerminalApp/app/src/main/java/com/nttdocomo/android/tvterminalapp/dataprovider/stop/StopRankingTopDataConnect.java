/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;

/**
 * ランキングトップ画面のデータ取得の通信を止める.
 */
public class StopRankingTopDataConnect extends AsyncTask<RankingTopDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopRankingTopDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final RankingTopDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (RankingTopDataProvider rankingTopDataProvider : dataProviders) {
                if (rankingTopDataProvider != null) {
                    rankingTopDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
