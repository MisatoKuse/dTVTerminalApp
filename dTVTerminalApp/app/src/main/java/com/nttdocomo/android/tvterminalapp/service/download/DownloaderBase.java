/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;


import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.activation.NewEnvironmentUtil;

import java.io.File;

public abstract class DownloaderBase {

    private DownloadParam mDownloadParam;
    private int mDownloadedBytes;
    private int mTotalBytes;
    private DownloadListener.DLError mError;
    private DownloadListener mDownloadListener;
    public static final String sDlPrefix = "d_";
    private boolean mIsDownloading = false;

    protected void setDownloadedBytes(final int bytesDone) {
        mDownloadedBytes = bytesDone;
    }

    public DownloadParam getDownloadParam() {
        return mDownloadParam;
    }

    /**
     * Constructor.
     * @param param param
     * @param downloadListener リスナー
     */
    DownloaderBase(final DownloadParam param, final DownloadListener downloadListener) {
        if (null == param) {
            DTVTLogger.error("DownloaderBase.DownloaderBase, param is null");
            return;
        }
        mDownloadParam = param;
        mDownloadListener = downloadListener;
        reset();
    }

    /**
     * 新しいダウンロードする場合、使用する.
     * @param param param
     */
    public void reset(final DownloadParam param) {
        if (null == param) {
            DTVTLogger.error("DownloaderBase.reset, param is null");
            return;
        }
        mDownloadParam = param;
        reset();
    }

    /**
     * ダウンロードステータスをリセット.
     */
    protected void reset() {
        mDownloadedBytes = 0;
        mTotalBytes = 0;
        mError = DownloadListener.DLError.DLError_NoError;
        setDownloading(false);
    }

    /**
     * Totalダウンロードサイズを戻す.
     * @return int
     */
    protected abstract int calculateTotalBytes();

    /**
     * ダウンロード開始.
     */
    public void start() {
        mTotalBytes = calculateTotalBytes();
        if (isStorageSpaceLow()) {
            setLowStorageSpace();
            return;
        }
        if (!isDownloading()) {
            if (null != mDownloadListener) {
                mDownloadListener.onStart(mTotalBytes);
            }
            newDl();
        }
    }

    private void newDl() {
        try {
            DownLoadThread dt = new DownLoadThread();
            dt.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * 実際のダウンロードを実現.
     */
    protected abstract void download();

    /* Build.VERSION.SDK_INT >= 26の時、この関数を実現.
     * ref: https://developer.android.com/reference/android/content/Context.html#getCacheDir()
     *      getCacheQuotaBytes(java.util.UUID)
     */
    protected abstract boolean isStorageSpaceLow();

    /**
     * Sub Classでダウンロード成功したとき、この関数をコール.
     */
    protected void onSuccess() {
        if (null != mDownloadListener) {
            String path = getFullFilePath();
            mDownloadListener.onSuccess(path);
        }
    }

    protected void onProgress(final int everyTimeBytes) {
        mDownloadedBytes += everyTimeBytes;
        if (null != mDownloadListener && null != mDownloadParam) {
            int total = mDownloadParam.getTotalSizeToDl();
            if (0 == total) {
                return;
            }
            int ff = (int) (((float) mDownloadedBytes) / ((float) total) * 100);
            if (100 < ff) {
                ff = 100;
            }
            if (ff < 0) {
                ff = 0;
            }
            mDownloadListener.onProgress(mDownloadedBytes, ff);
        }
    }

    class DownLoadThread extends Thread {
        @Override
        public void run() {
            setDownloading(true);
            download();
            setDownloading(false);
        }
    }

    /**
     * ダウンロード進捗通知.
     */
    int getProgressBytes() {
        return mDownloadedBytes;
    }

    /**
     * ダウンロード進捗通知.
     */
    float getProgressPercent() {
        if (0 == mTotalBytes) {
            return 0.0f;
        }
        return ((float) mDownloadedBytes) / ((float) mTotalBytes);
    }

    /**
     * ダウンロードエラー発生の時、コールされる.
     */
    DownloadListener.DLError isError() {
        return mError;
    }

    /**
     * ダウンロードキャンセル.
     */
    void cancel() {
        synchronized (this) {
            cancelImpl();
        }
    }

    private String getFullFilePath() {
        if (null != mDownloadParam) {
            StringBuilder sb = new StringBuilder();
            String path = mDownloadParam.getSavePath();
            String name = mDownloadParam.getSaveFileName();
            if (null == path || null == name) {
                return null;
            }
            sb.append(path);
            sb.append(File.separator);
            sb.append(name);
            return sb.toString();
        }
        return null;
    }


    /**
     * Sub Classでダウンロード成功したとき、この関数をコール.
     */
    protected abstract void cancelImpl();

    /**
     * ダウンロード容量不足.
     */
    void setLowStorageSpace() {
        if (null != mDownloadListener) {
            if (null != mDownloadParam) {
                String path = getFullFilePath();
                if (null != path) {
                    mDownloadListener.onLowStorageSpace(path);
                }
            }
        }
    }

    /**
     * @return MB.
     */
    long getInnerStorageSafeSpaceMB(){
        return 300;
    }

    /**
     * Sub Classでダウンロード成功したとき、この関数をコール.
     */
    protected void onFail(final DownloadListener.DLError error) {
        setDownloading(false);
        if (null != mDownloadListener && null != mDownloadParam) {
            final String savePath = mDownloadParam.getSavePath() + File.separator + mDownloadParam.getSaveFileName();
            mDownloadListener.onFail(error, savePath);
        }
    }

    /**
     *
     */
    protected void onCancel() {
        setDownloading(false);
        if (null != mDownloadListener && null != mDownloadParam) {
            final String savePath = mDownloadParam.getSavePath() + File.separator + mDownloadParam.getSaveFileName();
            mDownloadListener.onCancel(savePath);
        }
    }

    /**
     * 機能：xml item idからストレージ名を戻す.
     * @param id xml item id
     * @return ret ret
     */
    public static String getFileNameById(final String id) {
        String ret = id;
        ret = ret.replaceAll("[^a-z^A-Z^0-9]", "_");
        return sDlPrefix + (new StringBuilder(ret)).toString();
    }

    /**
     * 機能：.
     *      １．Download Uiがなくなる場合、且サービスにqueueはない場合、必ずこれをコールする
     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
     */
    public abstract void stop();

    /**
     * Get save path.
     * @return
     */
    public static String getDownloadPath(final Context context) {
        String downLoadPath = null;
        if (null == context) {
            return null;
        }

        String dmp = getDmpFolderName(context);
        if (null == dmp || dmp.isEmpty()) {
            downLoadPath = NewEnvironmentUtil.getPrivateDataHome(context, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        } else {
            File[] files = ContextCompat.getExternalFilesDirs(context, null);
            if (files != null) {
                if (files.length > 0) {
                    for (int i = files.length - 1; i >= 0; i--) {
                        File file = files[i];
                        if (file != null) {
                            downLoadPath = file.getAbsolutePath() + File.separator + dmp;
                            File dmpFile = new File(downLoadPath);
                            if (!dmpFile.exists()) {
                                boolean r = dmpFile.mkdirs();
                                if (r) {
                                    break;
                                } else {
                                    downLoadPath = null;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return downLoadPath;
    }

    public static String getDmpFolderName(final Context context) {
        if (null == context) {
            return null;
        }
        String ret = EnvironmentUtil.getPrivateDataHome(context, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        String sp = File.separator;
        int i = ret.lastIndexOf(sp);
        int l = ret.length();
        if (0 > i || i >= l) {
            return "";
        }
        return ret.substring(i + 1, l);
    }

    private synchronized void setDownloading(final boolean yn) {
        mIsDownloading = yn;
    }

    public synchronized boolean isDownloading() {
        return mIsDownloading;
    }
}
