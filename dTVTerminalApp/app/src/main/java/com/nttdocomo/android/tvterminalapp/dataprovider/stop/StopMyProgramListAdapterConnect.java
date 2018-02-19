/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.adapter.TvProgramListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * アダプタで行っている通信を止める.
 */
public class StopMyProgramListAdapterConnect extends AsyncTask<TvProgramListAdapter, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopMyProgramListAdapterConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final TvProgramListAdapter... adapters) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (adapters != null) {
            for (TvProgramListAdapter contentsAdapter : adapters) {
                if (contentsAdapter != null) {
                    contentsAdapter.stopConnect();
                }
            }
        }
        return null;
    }
}
