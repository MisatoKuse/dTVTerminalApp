/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DownLoadListDataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ダウンロードデータプロバイダー
 * Activityからこのクラスを利用する
 */
public class DlDataProvider implements ServiceConnection, DownloadServiceListener, DbThread.DbOperation {
    private DlDataProviderListener mDlDataProviderListener;
    private DownloadService.Binder mBinder;
    private Activity mActivity;
    private DlData dlData;
    private List<DlData> dlDataQue;
    private static boolean isBinded = false;

    public DlDataProvider(Activity activity, DlDataProviderListener dlDataProviderListener) throws Exception {
        if (null == activity) {
            throw new Exception("DlDataProvider.DlDataProvider, null activity");
        }
        mActivity = activity;
        mDlDataProviderListener = dlDataProviderListener;
    }

    /**
     * DlDataProvider機能を有効
     */
    public void beginProvider() {
        if (null == mActivity) {
            return;
        }
        isBinded = true;
        Intent intent = new Intent(mActivity, DownloadService.class);
        mActivity.bindService(intent, this, Context.BIND_AUTO_CREATE);
        startService();
    }

    /**
     * サービス起動する
     */
    public void startService() {
        if (null == mActivity) {
            return;
        }
        Intent intent = new Intent(mActivity, DownloadService.class);
        mActivity.startService(intent);
    }

    /**
     * DlDataProvider機能を無効
     */
    public void endProvider() {
        if (null == mActivity || !isBinded) {
            return;
        }
        isBinded = false;
        mActivity.unbindService(this);
    }

    public void setDlParam(DownloadParam param) throws Exception {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            ds.setDlParam(param);
        }
    }

    private DownloadService getDownloadService() {
        if (null == mBinder) {
            return null;
        }
        return mBinder.getDownloadService();
    }

    /**
     * ダウンロード開始
     */
    public void start() {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            ds.start();
        }
    }

    /**
     * ダウンロード一時停止
     */
    public void pause() {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            ds.pause();
        }
    }

    /**
     * ダウンロード再開
     */
    public void resume() {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            ds.resume();
        }
    }

    /**
     * ダウンロード停止
     */
    public void stop() {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            ds.stopService();
        }
    }

    /**
     * ダウンロード進捗通知
     */
    public int getProgressBytes() {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            return ds.getProgressBytes();
        }
        return 0;
    }

    /**
     * ダウンロード進捗通知
     */
    public float getProgressPercent() {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            return ds.getProgressPercent();
        }
        return 0.0f;
    }

    /**
     * ダウンロードエラー発生の時、コールされる
     */
    public DownloadListener.DLError isError() {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            return ds.isError();
        }
        return DownloadListener.DLError.DLError_NoError;
    }

    /**
     * ダウンロードキャンセル
     */
    public void cancel() {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            ds.cancel();
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mBinder = (DownloadService.Binder) iBinder;
        DownloadService ds = getDownloadService();
        if (null != ds) {
            ds.setDownloadServiceListener(this);
        }
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.dlDataProviderAvailable();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBinder = null;
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.dlDataProviderUnavailable();
        }
    }

    @Override
    public void onStart(int totalFileByteSize) {
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.onStart(totalFileByteSize);
            saveDownLoad(totalFileByteSize);
        }
    }

    @Override
    public void onPause() {
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.onPause();
        }
    }

    @Override
    public void onResume() {
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.onResume();
        }
    }

    @Override
    public void onProgress(int receivedBytes, int percent) {
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.onProgress(receivedBytes, percent);
        }
    }

    @Override
    public void onFail(final DLError error) {
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.onFail(error);
        }
    }

    @Override
    public void onSuccess(String fullPath) {
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.onSuccess(fullPath);
            if (!isBinded) {
                if (dlDataQue != null && dlDataQue.size() > 0) {
                    endProvider();
                    prepareDownLoad(dlDataQue.get(0));
                    start();
                } else {
                    stop();
                }
            }else{
                endProvider();
                stop();
            }
        }
    }

    @Override
    public void onCancel() {
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.onCancel();
        }
    }

    @Override
    public void onLowStorageSpace() {
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.onLowStorageSpace();
        }
    }

    private void prepareDownLoad(DlData dlData) {

    }

    private static final int DOWNLOAD_STATUS_SELECT = 1;
    private static final int DOWNLOAD_INSERT = 2;
    private static final int DOWNLOAD_UPDATE = 3;
    private static final int DOWNLOAD_TOTALSIZE_SELECT = 4;

    public void getDownLoadStatus() {
        dbOperationByThread(DOWNLOAD_STATUS_SELECT);
    }

    @Override
    public void onDbOperationFinished(boolean isSuccessful, List<Map<String, String>> resultSet, int operationId) {
        if (isSuccessful) {
            switch (operationId) {
                case DOWNLOAD_STATUS_SELECT:
                    List<DlData> statusList = new ArrayList<>();
                    for (int i = 0; i < resultSet.size(); i++) {
                        Map<String, String> hashMap = resultSet.get(i);
                        String downloadStatus = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS);
                        String itemId = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_ITEM_ID);
                        String saveURL = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_SAVE_URL);
                        String fileName = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_TITLE);
                        DlData mDlData = new DlData();
                        mDlData.setItemId(itemId);
                        mDlData.setSaveFile(saveURL);
                        mDlData.setTitle(fileName);
                        mDlData.setDownLoadStatus(downloadStatus);
                        statusList.add(mDlData);
                    }
                    if (null != mDlDataProviderListener) {
                        mDlDataProviderListener.onDownLoadListCallBack(statusList);
                    }
                    break;
                case DOWNLOAD_TOTALSIZE_SELECT:
                    if (null != mDlDataProviderListener) {
                        if (resultSet != null && resultSet.size() > 5) {
                            mDlDataProviderListener.onFail(DLError.DLError_Other);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(int operationId) throws Exception {
        List<Map<String, String>> resultSet = null;
        DownLoadListDataManager downLoadListDataManager = new DownLoadListDataManager(mActivity);
        switch (operationId) {
            case DOWNLOAD_STATUS_SELECT:
                resultSet = downLoadListDataManager.selectDownLoadListVideoData();
                break;
            case DOWNLOAD_TOTALSIZE_SELECT:
                downLoadListDataManager.selectDownLoadListByTotal();
                break;
            case DOWNLOAD_INSERT:
                downLoadListDataManager.insertDownload(dlData);
                break;
            case DOWNLOAD_UPDATE:
                downLoadListDataManager.updateDownload(dlData);
                break;
            default:
                break;
        }
        return resultSet;
    }

    private void saveDownLoad(int totalFileByteSize) {
        if (dlData != null) {
            dlData.setTotalSize(String.valueOf(totalFileByteSize));
        }
    }

    public void setDlData(DlData dlData) {
        this.dlData = dlData;
        dbOperationByThread(DOWNLOAD_INSERT);
    }

    public void setQue(List<DlData> dlData) {
        this.dlDataQue = dlData;
    }

    private void dbOperationByThread(int operationId) {
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, operationId);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
