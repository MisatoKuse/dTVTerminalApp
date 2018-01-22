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
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DBConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DownLoadListDataManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ダウンロードデータプロバイダー
 * Activityからこのクラスを利用する
 */
public class DlDataProvider implements ServiceConnection, DownloadServiceListener, DbThread.DbOperation {
    private DlDataProviderListener mDlDataProviderListener;
    private static DownloadService.Binder mBinder;
    private Activity mActivity;
    private DlData dlData;
    private String itemId;
    public boolean isRegistered;

    public DlDataProvider(Activity activity, DlDataProviderListener dlDataProviderListener) throws Exception {
        if (null == activity) {
            throw new Exception("DlDataProvider.DlDataProvider, null activity");
        }
        this.mActivity = activity;
        mDlDataProviderListener = dlDataProviderListener;
    }

    public DlDataProvider(Activity activity){
        this.mActivity = activity;
    }

    /**
     * DlDataProvider機能を有効
     */
    public void beginProvider() {
        if (null == mActivity) {
            return;
        }
        DownloadService.BINDSTATUS = DownloadService.BINDED;
        Intent intent = new Intent(mActivity, DownloadService.class);
        isRegistered = mActivity.bindService(intent, this, Context.BIND_AUTO_CREATE);
        startService();
    }

    /**
     * 再バインド
     */
    public void rebind() {
        if (null == mActivity) {
            return;
        }
        DownloadService.BINDSTATUS = DownloadService.BINDED;
        Intent intent = new Intent(mActivity, DownloadService.class);
        mActivity.bindService(intent, this, Context.BIND_AUTO_CREATE);
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
        if (null == mActivity || DownloadService.BINDSTATUS == DownloadService.UNBINED) {
            return;
        }
        DownloadService.BINDSTATUS = DownloadService.UNBINED;
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
    public void stopService() {
        DownloadService ds = getDownloadService();
        if (null != ds) {
            ds.stop();
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
        if (null != mDlDataProviderListener && DownloadService.BINDSTATUS == DownloadService.BINDED) {
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
        if (null != mDlDataProviderListener && DownloadService.BINDSTATUS == DownloadService.BINDED) {
            mDlDataProviderListener.onProgress(receivedBytes, percent);
        }
        if(DownloadService.BINDSTATUS == DownloadService.BACKGROUD){
            DownloadService ds = getDownloadService();
            if (null != ds) {
                Intent intent = new Intent();
                intent.setAction(DownloadService.DONWLOAD_UPDATE);
                intent.putExtra(DownloadService.DONWLOAD_UPDATE, percent);
                ds.sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onFail(final DLError error, final String savePath) {
        if (null != mDlDataProviderListener) {
            mDlDataProviderListener.onFail(error, savePath);
        }
    }

    @Override
    public void onSuccess(String fullPath) {
        if(!TextUtils.isEmpty(fullPath)){
            if(fullPath.contains(File.separator)){
                String paths[] = fullPath.split(File.separator);
                itemId = fullPath.split(File.separator)[paths.length - 1];
                if(!TextUtils.isEmpty(itemId)){
                    updateDownloadStatusToDb();
                }
            }
        }
        if (null != mDlDataProviderListener && DownloadService.BINDSTATUS == DownloadService.BINDED) {
            DTVTLogger.debug(">>>>>>>>>>>>>>>>>> dl ok");
            mDlDataProviderListener.onSuccess(fullPath);
        } else if(DownloadService.BINDSTATUS == DownloadService.BACKGROUD){
            DownloadService ds = getDownloadService();
            if (null != ds) {
                Intent intent = new Intent();
                intent.setAction(DownloadService.DONWLOAD_SUCCESS);
                intent.putExtra(DownloadService.DONWLOAD_PATH, fullPath);
                ds.sendBroadcast(intent);
            }
        } else {
            if (DownloadService.dlDataQue != null && DownloadService.dlDataQue.size() > 0) {
                DownloadService.dlDataQue.remove(0);
                DTVTLogger.debug(">>>>>>>>>>>>>>>>>> dl ok");
                if(0 == DownloadService.dlDataQue.size()){
                    isRegistered = false;
                    stopService();
                    return;
                }
                try {
                    Thread.sleep(300);
                    DTVTLogger.debug(">>>>>>>>>>>>>>>>>> new dl");
                    setDlParam(getDownLoadParam());
                    start();
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                stopService();
            }
        }
    }

    private DownloadParam getDownLoadParam(){
        DownloadParam downloadParam = null;
        if(DownloadService.dlDataQue != null && DownloadService.dlDataQue.size() > 0){
            DlData item = DownloadService.dlDataQue.get(0);
            Context context = null;
            if(getDownloadService() != null){
                context = getDownloadService().getApplicationContext();
            }
            downloadParam = new DtcpDownloadParam();
            DtcpDownloadParam dtcpDownloadParam = (DtcpDownloadParam) downloadParam;
            dtcpDownloadParam.setContext(context);
            dtcpDownloadParam.setSavePath(item.getSaveFile());
            dtcpDownloadParam.setSaveFileName(item.getTitle());
            dtcpDownloadParam.setDtcp1host(item.getHost());
            dtcpDownloadParam.setDtcp1port(Integer.parseInt(item.getPort()));
            dtcpDownloadParam.setUrl(item.getUrl());
            dtcpDownloadParam.setCleartextSize(Integer.parseInt(item.getTotalSize()));
            dtcpDownloadParam.setItemId(item.getItemId());
            dtcpDownloadParam.setPercentToNotify(Integer.parseInt(item.getPercentToNotify()));
            dtcpDownloadParam.setXmlToDl(item.getXmlToDl());
        }
        return downloadParam;
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

    private static final int DOWNLOAD_STATUS_SELECT = 1;
    private static final int DOWNLOAD_INSERT = 2;
    private static final int DOWNLOAD_UPDATE = 3;
    private static final int DOWNLOAD_TOTALSIZE_SELECT = 4;
    private static final int DOWNLOAD_DELETE_ALL = 5;

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
                            mDlDataProviderListener.onFail(DLError.DLError_Other, "");
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
                downLoadListDataManager.selectDownLoadList();
                break;
            case DOWNLOAD_INSERT:
                if(dlData != null){
                    downLoadListDataManager.insertDownload(dlData);
                }
                break;
            case DOWNLOAD_UPDATE:
                if(dlData != null) {
                    downLoadListDataManager.updateDownloadByItemId(itemId);
                }
                break;
            case DOWNLOAD_DELETE_ALL:
                downLoadListDataManager.deleteDownloadAllContents();
                break;
            default:
                break;
        }
        return resultSet;
    }

    public void cancelDownLoadStatus(String path){
        if(!TextUtils.isEmpty(path) && path.contains(File.separator)){
            String paths[] = path.split(File.separator);
            String itemId = path.split(File.separator)[paths.length - 1];
            if(!TextUtils.isEmpty(itemId)){
                DownLoadListDataManager downLoadListDataManager = new DownLoadListDataManager(mActivity);
                downLoadListDataManager.deleteDownloadContentByItemId(itemId);
                //ディスクからコンテンツを削除する
                File file = new File(path);
                if(file.exists()){
                    File files[] = file.listFiles();
                    for(File file1:files){
                        if(!file1.delete()){
                            DTVTLogger.debug("delete cacel file fail ");
                        }
                    }
                    if(file.exists()){
                        if(!file.delete()) {
                            DTVTLogger.debug("delete cacel directory fail ");
                        }
                    }
                }
            }
        }
    }

    public List<Map<String, String>> getDownloadListData(){
        DownLoadListDataManager downLoadListDataManager = new DownLoadListDataManager(mActivity);
        return downLoadListDataManager.selectDownLoadList();
    }

    public void deleteAllDownLoadContents(){
        DownLoadListDataManager downLoadListDataManager = new DownLoadListDataManager(mActivity);
        List<Map<String, String>> downLoadList = downLoadListDataManager.selectDownLoadList();
        if(downLoadList != null && downLoadList.size() > 0){
            for(int i=0; i < downLoadList.size(); i++){
                Map<String, String> hashMap = downLoadList.get(i);
                String path = hashMap.get(DBConstants.DOWNLOAD_LIST_COLUM_SAVE_URL);
                if(!TextUtils.isEmpty(path)){
                    deleteAllFiles(new File(path));
                }
            }
            downLoadListDataManager.deleteDownloadAllContents();
        }
    }

    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null){
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteAllFiles(f);
                    try {
                        if (!f.delete()) {
                            DTVTLogger.debug("delete download file fail ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (f.exists()) {
                        deleteAllFiles(f);
                        try {
                            if (!f.delete()) {
                                DTVTLogger.debug("delete download file fail ");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
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

    private void updateDownloadStatusToDb(){
        if(mActivity != null){
            DownLoadListDataManager downLoadListDataManager = new DownLoadListDataManager(mActivity);
            downLoadListDataManager.updateDownloadByItemId(itemId);
        }
    }

    public void setQue(List<DlData> dlData) {
        if(DownloadService.dlDataQue != null && DownloadService.dlDataQue.size() > 0){
            DownloadService.dlDataQue.clear();
        }
        DownloadService.dlDataQue = dlData;
        DownloadService.BINDSTATUS = DownloadService.UNBINED;
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

//    /**
//     * 機能：
//     *      １．Download Uiがなくなる場合、且サービスにqueueはない場合、必ずこれをコールする
//     *      ２．Download Uiがない場合、Serviceは閉じる時、必ずこれをコールする
//     */
//    public void stop(){
//        DownloadService ds = getDownloadService();
//        if (null != ds) {
//            ds.stop();
//        }
//    }
}
