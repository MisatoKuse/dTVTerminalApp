/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.StbMetaInfoGetDataProvider;

/**
 * 検索結果取得で行っている通信を止める.
 */
public class StopRecommendDetailDataConnect extends AsyncTask<StbMetaInfoGetDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopRecommendDetailDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final StbMetaInfoGetDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (StbMetaInfoGetDataProvider stbMetaInfoGetDataProvider : dataProviders) {
                if (stbMetaInfoGetDataProvider != null) {
                    stbMetaInfoGetDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
