/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.DtvContentsDetailDataProvider;

/**
 * コンテンツ詳細取得で行っている通信を止める.
 */
public class StopContentDetailDataConnect extends AsyncTask<DtvContentsDetailDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopContentDetailDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final DtvContentsDetailDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (DtvContentsDetailDataProvider contentsDetailDataProvider : dataProviders) {
                contentsDetailDataProvider.stopConnect();
            }
        }
        return null;
    }
}
