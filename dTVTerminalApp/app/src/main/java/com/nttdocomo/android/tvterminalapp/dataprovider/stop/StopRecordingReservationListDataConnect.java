/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;

/**
 * 録画予約リスト取得の通信を止める.
 */
public class StopRecordingReservationListDataConnect
        extends AsyncTask<RecordingReservationListDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopRecordingReservationListDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final RecordingReservationListDataProvider... dataProviders) {
        //通信を行っている処理を止める
        if (dataProviders != null) {
            for (RecordingReservationListDataProvider recordingReservationListDataProvider : dataProviders) {
                if (recordingReservationListDataProvider != null) {
                    recordingReservationListDataProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
