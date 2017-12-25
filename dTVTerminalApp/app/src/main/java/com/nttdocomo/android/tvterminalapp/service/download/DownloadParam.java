/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

import android.content.Context;

/**
 * このクラスから継承して、DownloaderBaseおよびSubClass用パラメータクラウスである
 */
class DownloadParam {
    private Context mContext;
    private String mSavePath;
    private String mSaveFileName;
    //通知の頻度
    private int mPercentToNotify;

    /**
     * Getter
     * @return mContext
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Setter
     * @param context context
     */
    public void setContext(Context context) {
        this.mContext = context;
    }

    /**
     * Getter
     * @return mSavePath
     */
    String getSavePath() {
        return mSavePath;
    }

    /**
     * Setter
     * @param savePath savePath
     */
    public void setSavePath(String savePath) {
        this.mSavePath = savePath;
    }

    /**
     * Getter
     * @return mSaveFileName
     */
    String getSaveFileName() {
        return mSaveFileName;
    }

    /**
     * Setter
     * @param saveFileName saveFileName
     */
    public void setSaveFileName(String saveFileName) {
        this.mSaveFileName = saveFileName;
    }
}
