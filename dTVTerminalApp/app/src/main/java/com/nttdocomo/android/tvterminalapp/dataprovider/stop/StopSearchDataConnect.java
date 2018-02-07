/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.SearchDataProvider;

/**
 * 検索結果取得で行っている通信を止める.
 */
public class StopSearchDataConnect extends AsyncTask<SearchDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopSearchDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final SearchDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (SearchDataProvider searchDataProvider : dataProviders) {
                searchDataProvider.stopConnect();
            }
        }
        return null;
    }
}
