/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


import android.os.Build;

/**
 * todo: このクラスを実現
 * 仮データ
 */
class KariDownloader extends DownloaderBase{
    KariDownloader(DownloadParam param, DownloadListener downloadListener)throws Exception{
        super(param, downloadListener);
    }

    @Override
    protected int calculateTotalBytes() {
        return 0;
    }

    @Override
    protected void download() {
        //todo
    }

    /* Build.VERSION.SDK_INT >= 26の時、この関数を実現
     * ref: https://developer.android.com/reference/android/content/Context.html#getCacheDir()
     *      getCacheQuotaBytes(java.util.UUID)
     */
    @Override
    protected boolean isStorageSpaceLow() {
        if(Build.VERSION.SDK_INT >= 26){
            //todo
        }
        return false;
    }
}
