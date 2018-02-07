/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * アダプタで行っている通信を止める.
 */
public class StopContentsAdapterConnect extends AsyncTask<ContentsAdapter, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopContentsAdapterConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final ContentsAdapter... adapters) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (adapters != null) {
            for (ContentsAdapter contentsAdapter : adapters) {
                contentsAdapter.stopConnect();
            }
        }
        return null;
    }
}
