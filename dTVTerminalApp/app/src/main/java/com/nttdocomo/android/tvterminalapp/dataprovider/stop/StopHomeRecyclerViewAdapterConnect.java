/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.adapter.HomeRecyclerViewAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * ホーム画面とランキングトップ画面で使用しているアダプタの通信を止める.
 */
public class StopHomeRecyclerViewAdapterConnect extends AsyncTask<HomeRecyclerViewAdapter, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopHomeRecyclerViewAdapterConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final HomeRecyclerViewAdapter... adapters) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (adapters != null) {
            for (HomeRecyclerViewAdapter homeRecyclerViewAdapter : adapters) {
                if (homeRecyclerViewAdapter != null) {
                    homeRecyclerViewAdapter.stopConnect();
                }
            }
        }
        return null;
    }
}
