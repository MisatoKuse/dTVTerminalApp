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
    private DownloadService.Binder mBinder;
    private Activity mActivity;
    private DlData dlData;
    private String itemId;
    private boolean isRegistered;
    private static DlDataProvider sDlDataProvider= new DlDataProvider();

    private DlDataProvider(){

    }

    public static DlDataProvider getInstance(Activity activity) throws Exception {
        if (null == activity) {
            throw new Exception("DlDataProvider.DlDataProvider, null activity");
        }
        if(null==sDlDataProvider){
            sDlDataProvider = new DlDataProvider();
        }
        sDlDataProvider.mActivity = activity;
        return sDlDataProvider;
    }

    static void releaseInstance() {
        if(null == sDlDataProvider) {
            return;
        }
        sDlDataProvider = null;
    }

    public boolean getIsRegistered(){
        return isRegistered;
    }

    public DlDataProvider(Activity activity){
        this.mActivity = activity;
    }

    /**
     * DlDataProvider機能を有効
     */
    public void beginProvider(Activity act) {
        if (null == act) {
            start();
            return;
        }
        mActivity = act;
        Intent intent = new Intent(mActivity, DownloadService.class);
        isRegistered = mActivity.bindService(intent, this, Context.BIND_AUTO_CREATE);
        startService();
    }

    /**
     * サービス起動する
     */
    private void startService() {
        if(DownloadService.isDownloadServiceRunning()) {
            return;
        }
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
        if (null == mActivity) {
            return;
        }
        try {
            mActivity.unbindService(this);
        } catch (Exception e){
            DTVTLogger.debug(e);
        }
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
        sendBroadcast(DownloadService.DONWLOAD_DlDataProviderAvailable);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mBinder = null;
        sendBroadcast(DownloadService.DONWLOAD_DlDataProviderUnavailable);
    }

    @Override
    public void onStart(int totalFileByteSize) {
        sendBroadcast(DownloadService.DONWLOAD_OnStart, DownloadService.DONWLOAD_ParamInt, totalFileByteSize);
    }

    @Override
    public void onProgress(int receivedBytes, int percent) {
        sendBroadcast(DownloadService.DONWLOAD_OnProgress, DownloadService.DONWLOAD_ParamInt, percent);
    }

    @Override
    public void onFail(final DLError error, final String savePath) {
        int paramInt = error.ordinal();
        sendBroadcast(DownloadService.DONWLOAD_OnFail, DownloadService.DONWLOAD_ParamString, savePath, DownloadService.DONWLOAD_ParamInt, paramInt);
        DownloadService ds = getDownloadService();
        if(null == ds){
            return;
        }
        if(!ds.isUiRunning()){
            setNextDownLoad();
        }
    }

    private void sendBroadcast(String broad, String paramName, String param){
        DownloadService ds = getDownloadService();
        if (null != ds) {
            Intent intent = new Intent();
            intent.setAction(broad);
            intent.putExtra(paramName, param);
            ds.sendBroadcast(intent);
        }
    }

    private void sendBroadcast(String broad, String paramName, String param, String paramName2, int intParam){
        DownloadService ds = getDownloadService();
        if (null != ds) {
            Intent intent = new Intent();
            intent.setAction(broad);
            intent.putExtra(paramName, param);
            intent.putExtra(paramName2, intParam);
            ds.sendBroadcast(intent);
        }
    }



    private void sendBroadcast(String broad){
        DownloadService ds = getDownloadService();
        if (null != ds) {
            Intent intent = new Intent();
            intent.setAction(broad);
            ds.sendBroadcast(intent);
        }
    }

    private void sendBroadcast(String broad, String paramName, int param){
        DownloadService ds = getDownloadService();
        if (null != ds) {
            Intent intent = new Intent();
            intent.setAction(broad);
            intent.putExtra(paramName, param);
            ds.sendBroadcast(intent);
        }
    }

    private static final String sSeparator = File.separator + "";

    @Override
    public void onSuccess(String fullPath) {
        if(!TextUtils.isEmpty(fullPath)){
            if(fullPath.contains(sSeparator)){
                String paths[] = fullPath.split(sSeparator);
                String ids[] = fullPath.split(sSeparator);
                itemId = ids[paths.length - 1];
                if(!TextUtils.isEmpty(itemId)){
                    updateDownloadStatusToDb();
                }
            }
        }
        sendBroadcast(DownloadService.DONWLOAD_OnSuccess, DownloadService.DONWLOAD_ParamString, fullPath);
        DownloadService ds = getDownloadService();
        if(null == ds) {
            return;
        }
        if(!ds.isUiRunning()){
            setNextDownLoad();
        }
    }

    private void setNextDownLoad(){
        if (DownloadService.getDlDataQue() != null && DownloadService.getDlDataQue().size() > 0) {
            DownloadService.setDlDataQueRemove0();
            DTVTLogger.debug(">>>>>>>>>>>>>>>>>> dl ok 3");
            if(0 == DownloadService.getDlDataQue().size()){
                isRegistered = false;
                stopService();
                return;
            }
            try {
                Thread.sleep(1000*1);
                DTVTLogger.debug(">>>>>>>>>>>>>>>>>> new dl");
                setDlParam(getDownLoadParam());
                start();
            } catch (Exception e){
                DTVTLogger.debug(e);
            }
        } else {
            stopService();
        }
    }

    private DownloadParam getDownLoadParam(){
        DownloadParam downloadParam = null;
        if(DownloadService.getDlDataQue() != null && DownloadService.getDlDataQue().size() > 0){
            DlData item = DownloadService.getDlDataQue().get(0);
            Context context = null;
            if(getDownloadService() != null){
                context = getDownloadService().getApplicationContext();
            }
            downloadParam = new DtcpDownloadParam();
            DtcpDownloadParam dtcpDownloadParam = (DtcpDownloadParam) downloadParam;
            dtcpDownloadParam.setContext(context);
            dtcpDownloadParam.setSavePath(item.getSaveFile());
            dtcpDownloadParam.setSaveFileName(item.getItemId());
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
    public void onCancel(final String filePath) {
        sendBroadcast(DownloadService.DONWLOAD_OnCancel, DownloadService.DONWLOAD_ParamString, filePath);
        DownloadService ds = getDownloadService();
        if(null == ds) {
            return;
        }
        if(!ds.isUiRunning()){
            setNextDownLoad();
        }
    }

    @Override
    public void onLowStorageSpace(final String fullPath) {
        sendBroadcast(DownloadService.DONWLOAD_OnLowStorageSpace, DownloadService.DONWLOAD_ParamString, fullPath);
        DownloadService ds = getDownloadService();
        if(null == ds) {
            return;
        }
        if(!ds.isUiRunning()){
            setNextDownLoad();
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
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(int operationId) {
        List<Map<String, String>> resultSet = null;
        DownLoadListDataManager downLoadListDataManager = new DownLoadListDataManager(mActivity);
        switch (operationId) {
            case DOWNLOAD_STATUS_SELECT:
                resultSet = downLoadListDataManager.selectDownLoadListVideoData();
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
        if(null == path || path.isEmpty()){
            return;
        }
        if(!TextUtils.isEmpty(path) && path.contains(sSeparator)){
            String paths[] = path.split(sSeparator);
            String ids[] = path.split(sSeparator);
            String itemId = ids[paths.length - 1];
            if(!TextUtils.isEmpty(itemId)){
                DownLoadListDataManager downLoadListDataManager = new DownLoadListDataManager(mActivity);
                downLoadListDataManager.deleteDownloadContentByItemId(itemId);
                //ディスクからコンテンツを削除する
                File file = new File(path);
                if(file.exists()){
                    File files[] = file.listFiles();
                    if(null != files) {
                        for (File file1 : files) {
                            if (null != file1) {
                                if (!file1.delete()) {
                                    DTVTLogger.debug("delete cacel file fail path:" + path);
                                }
                            }
                        }
                    }
                    if(file.exists()){
                        if(!file.delete()) {
                            DTVTLogger.debug("delete cacel directory fail path:" + path);
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
                        DTVTLogger.debug(e);
                    }
                } else {
                    if (f.exists()) {
                        deleteAllFiles(f);
                        try {
                            if (!f.delete()) {
                                DTVTLogger.debug("delete download file fail ");
                            }
                        } catch (Exception e) {
                            DTVTLogger.debug(e);
                        }
                    }
                }
            }
        }
    }

    public void setDlData(DlData dlData) throws Exception{
        try {
            this.dlData = dlData;
            dbOperationByThread(DOWNLOAD_INSERT);
        } catch (Exception e) {
            throw new Exception("DlDataProvider.DlDataProvider, DB insert Fail ");
        }
    }

    private void updateDownloadStatusToDb(){
        DTVTLogger.start();
        if(mActivity != null){
            DTVTLogger.debug("writing db");
            DownLoadListDataManager downLoadListDataManager = new DownLoadListDataManager(mActivity);
            downLoadListDataManager.updateDownloadByItemId(itemId);
        }
        DTVTLogger.end();
    }

    public void setQue(List<DlData> dlData) {
        if(DownloadService.getDlDataQue() != null && DownloadService.getDlDataQue().size() > 0){
            DownloadService.setDlDataQueClear();
        }
        DownloadService.setDlDataQue(dlData);
    }

    private void dbOperationByThread(int operationId) {
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, operationId);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * Ui runningを設定
     */
    public void setUiRunning(boolean yn){
        DownloadService ds = getDownloadService();
        if (null != ds) {
            ds.setUiRunning(yn);
        }
    }

    /**
     * DlDataからfullpathを戻す
     * @param data data
     * @return fullpath fullpath
     */
    private static String getCurrentDlFullPath(DlData data){
        if(null==data ){
            return null;
        }
        StringBuilder path=new StringBuilder();
        path.append(data.getSaveFile());
        path.append(File.separator);
        path.append(data.getItemId());
        return path.toString();
    }

    /**
     * 機能：海外になる時、すべてのDLをキャンセル
     */
    public static synchronized void cancelAll() {
        if(null == sDlDataProvider){
            return;
        }
        DownloadService ds = sDlDataProvider.getDownloadService();
        if (null != ds) {
            if(ds.isUiRunning()){
                sDlDataProvider.sendBroadcast(DownloadService.DONWLOAD_OnCancelAll);
            } else {
                List<DlData> dlDataQue = ds.getDlDataQue();
                for(DlData d : dlDataQue){
                    String path = getCurrentDlFullPath(d);
                    if(null != path){
                        sDlDataProvider.cancelDownLoadStatus(path);
                    }
                }
                for(int i = dlDataQue.size() - 1; i > -1; --i){
                    dlDataQue.remove(i);
                }
                ds.cancel();
            }
        }
    }

    public synchronized boolean isDownloading(){
        DownloadService ds = getDownloadService();
        if (null != ds) {
            return ds.isDownloading();
        }
        return false;
    }
}
