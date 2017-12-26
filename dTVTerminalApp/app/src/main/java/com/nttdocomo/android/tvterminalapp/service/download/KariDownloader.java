/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


import android.os.Build;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * todo: このクラスを実現
 * 仮データ
 */
class KariDownloader extends DownloaderBase{
    private KariDownloadParam downloadParam;
    KariDownloader(DownloadParam param, DownloadListener downloadListener)throws Exception{
        super(param, downloadListener);
        this.downloadParam = (KariDownloadParam)param;
    }

    @Override
    protected int calculateTotalBytes() {
        return downloadParam.getFileSize();
    }

    @Override
    protected void download() {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            String downloadUrl = downloadParam.getUrl();
            String fileName = downloadParam.getSaveFileName();
            URL url = new URL(downloadUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            long len;
            int receivedBytes = 0;
            if (urlConnection.getResponseCode()==200) {
//                len = urlConnection.getContentLength();
                len = downloadParam.getFileSize();
                in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
                File file = new File(downloadParam.getSavePath(), fileName);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                byte[] buffer = new byte[10240];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                    receivedBytes += length;
                    setDownloadedBytes(receivedBytes);
                    onProgress(length);
                    if (receivedBytes == len) {
                        Thread.sleep(5000);
                        onSuccess();
                    }
                }
            }
        } catch (IOException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                DTVTLogger.debug(e);
            }
        }
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
