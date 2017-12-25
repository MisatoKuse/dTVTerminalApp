/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


/**
 * todo 作成中
 */
public class DtcpDownloader extends DownloaderBase {

    public DtcpDownloader(DownloadParam param, DownloadListener downloadListener)throws Exception{
        super(param, downloadListener);
    }

    @Override
    protected int calculateTotalBytes() {
        return 0;
    }

    @Override
    protected void download() {

    }

    @Override
    protected boolean isStorageSpaceLow() {
        return false;
    }
}
