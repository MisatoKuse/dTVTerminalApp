/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.GenreListDataProvider;

/**
 * ジャンル取得の通信を止める.
 */
public class StopGenreListDataConnect extends AsyncTask<GenreListDataProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopGenreListDataConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final GenreListDataProvider... providers) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (providers != null) {
            for (GenreListDataProvider videoGenreProvider : providers) {
                if (videoGenreProvider != null) {
                    videoGenreProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
