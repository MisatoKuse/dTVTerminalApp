/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

import android.content.Context;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.activation.NewEnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.jni.download.DlnaDownloadRet;

import java.io.File;

/**
 * tcpダウンローダークラス.
 */
public class DtcpDownloader extends DownloaderBase implements DlnaManager.DownloadStatusListener {

    /**
     * Dtcpダウンローダー.
     *
     * @param param ダウンロードパラメータ
     * @param downloadListener リスナー
     */
    DtcpDownloader(final DownloadParam param, final DownloadListener downloadListener) {
        super(param, downloadListener);
    }

    @Override
    protected long calculateTotalBytes() {
        long ret = 0;
        DtcpDownloadParam param;
        try {
            param = (DtcpDownloadParam) getDownloadParam();
            ret = param.getCleartextSize();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return ret;
    }

    @Override
    protected void download() {
        DTVTLogger.debug("dtcp download begin, files");
        printDlPathFiles();
        //mFinishedBytes=0;
        DtcpDownloadParam param = (DtcpDownloadParam) getDownloadParam();
        if (!param.isParamValid()) {
            onFail(DownloadListener.DownLoadError.DLError_ParamError);
            return;
        }

        Context context = param.getContext();
        if (null == context) {
            onFail(DownloadListener.DownLoadError.DLError_Other);
            return;
        }

        DlnaManager.shared().mDownloadStatusListener = this;
        if (DlnaManager.shared().initDownload()) {
            String homeParent = getParentDir(param.getSavePath());
            int ret = NewEnvironmentUtil.copyDeviceKeyFromOtherCMWork(context, homeParent, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
            if (1 != ret && 3 != ret) {
                onFail(DownloadListener.DownLoadError.DLError_CopyKeyFileFailed);
                DlnaManager.shared().setCanceledStatus(false);
                return;
            }
            if (!DlnaManager.shared().getCanceled()) {
                DlnaManager.shared().downloadStart(param);
            } else {
                DlnaManager.shared().setCanceledStatus(false);
                onDownloadStatusCallBack(DlnaManager.DownLoadStatus.DOWNLOADER_STATUS_CANCELLED);
            }
        } else {
            onFail(DownloadListener.DownLoadError.DLError_Other);
        }
    }

    /**
     * getParentDir.
     * @param dir dir
     * @return Parent path
     */
    private String getParentDir(final String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            boolean r = f.mkdirs();
            if (!r) {
                return "";
            }
        }
        return f.getParent();
    }

    /**
     * エラー.
     * @param res ダウンロード結果
     */
    private void errors(final DlnaDownloadRet res) {
        switch (res) {
            case DownloadRet_CopyKeyFileFailed:
                onFail(DownloadListener.DownLoadError.DLError_CopyKeyFileFailed);
                break;
            case DownloadRet_ParamError:
                onFail(DownloadListener.DownLoadError.DLError_ParamError);
                break;
            case DownloadRet_Unactivated:
                onFail(DownloadListener.DownLoadError.DLError_Unactivated);
                break;
            case DownloadRet_OtherError:
                onFail(DownloadListener.DownLoadError.DLError_Other);
                break;
            case DownloadRet_Succeed:
                break;
        }
    }

    @Override
    protected boolean isStorageSpaceLow() {
        DownloadParam param =  getDownloadParam();
        if (null == param) {
            return true;
        }
        DtcpDownloadParam dp = (DtcpDownloadParam) param;
        String path = dp.getSavePath();
        if (null == path || path.isEmpty()) {
            return true;
        }
        File f = new File(path);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                return true;
            }
        }
        long usableSpace = (f.getUsableSpace() / 1024) / 1024;  //-->MB
        long safeSpace = getInnerStorageSafeSpaceMB();
        long dlSize = (dp.getCleartextSize() / 1024) / 1024; //-->MB
        return (usableSpace - dlSize) < safeSpace;
    }

    @Override
    public void onDownloadStatusCallBack(final DlnaManager.DownLoadStatus status) {
        switch (status) {
            case DOWNLOADER_STATUS_COMPLETED:
                onSuccess();
                onStopIt();
                break;
            case DOWNLOADER_STATUS_ERROR_OCCURED:
                onFail(DownloadListener.DownLoadError.DLError_Download);
                onStopIt();
                break;
            case DOWNLOADER_STATUS_CANCELLED:
                onCancel();
                onStopIt();
                break;
            case DOWNLOADER_STATUS_UNKNOWN:
                break;
            case DOWNLOADER_STATUS_MOVING:
                break;
            default:
                break;
        }
    }

    @Override
    public void onDownloadProgressCallBack(final int progress) {
        onProgress(progress);
    }

    /**
     *ダウンロードパスーを出力する.
     */
    private void printDlPathFiles() {
        DtcpDownloadParam dp = (DtcpDownloadParam) getDownloadParam();
        if (null != dp) {
            String path = dp.getSavePath();
            if (null != path) {
                File f = new File(path);
                File[] files = f.listFiles();
                if (null != files) {
                    for (File f2 : files) {
                        if (null != f2) {
                            DTVTLogger.debug(f2.getAbsolutePath() + ", mSize=" + f2.length());
                        }
                    }
                }
            }
        }
    }

    /**
     * ダウンロード停止.
     */
    private void onStopIt() {
        DTVTLogger.debug("dtcp download end, files");
        printDlPathFiles();
    }

    @Override
    public void stop() {
        DlnaManager.shared().DownloadStop();
    }
}
