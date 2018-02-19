/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoContentProvider;

/**
 * ビデオ一覧取得の通信を止める.
 */
public class StopVideoContentConnect extends AsyncTask<VideoContentProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopVideoContentConnect() {
        DTVTLogger.start();
    }

    @Override
    protected Void doInBackground(final VideoContentProvider... providers) {
        DTVTLogger.start();
        //通信を行っている処理を止める
        if (providers != null) {
            for (VideoContentProvider videoContentProvider : providers) {
                if (videoContentProvider != null) {
                    videoContentProvider.stopConnect();
                }
            }
        }
        return null;
    }
}
