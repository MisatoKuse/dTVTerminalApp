/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.VodClipDataProvider;

/**
 * 視聴中ビデオリスト取得の通信を止める.
 */
public class StopVodClipDataConnect extends AsyncTask<VodClipDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopVodClipDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final VodClipDataProvider... dataProviders) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (VodClipDataProvider vodClipDataProvider : dataProviders) {
                if (vodClipDataProvider != null) {
                    vodClipDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
