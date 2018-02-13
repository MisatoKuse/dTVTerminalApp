/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoGenreProvider;

/**
 * ジャンル取得の通信を止める.
 */
public class StopVideoGenreConnect extends AsyncTask<VideoGenreProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopVideoGenreConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final VideoGenreProvider... providers) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (providers != null) {
            for (VideoGenreProvider videoGenreProvider : providers) {
                videoGenreProvider.stopConnect();
            }
        }
        return null;
    }
}
