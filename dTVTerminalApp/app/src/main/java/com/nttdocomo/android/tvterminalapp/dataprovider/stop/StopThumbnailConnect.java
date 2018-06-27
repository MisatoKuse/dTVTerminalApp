/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.ThumbnailProvider;

/**
 * サムネイル取得で行っている通信を止める.
 */
public class StopThumbnailConnect extends AsyncTask<ThumbnailProvider, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopThumbnailConnect() {
        DTVTLogger.start();    }

    @Override
    protected Void doInBackground(final ThumbnailProvider... thumbnailProviders) {
        if (thumbnailProviders != null) {
            for (ThumbnailProvider thumbnailProvide : thumbnailProviders) {
                if (thumbnailProvide != null) {
                    thumbnailProvide.stopConnect();
                }
            }
        }
        return null;
    }
}
