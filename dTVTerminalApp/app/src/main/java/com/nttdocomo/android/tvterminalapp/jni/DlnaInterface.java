/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import android.util.Log;

import java.util.ArrayList;

/**
 * 機能：Singletonで実現され、Dlnaに関する機能を纏めるベースクラス
 */
public class DlnaInterface {

    //Singleton
    private static DlnaInterface sDlnaInterface = new DlnaInterface();

    //Browseコンテンツ
    private static final int DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST = 0;
    //デバイスjoin
    private static final int DLNA_MSG_ID_DEV_DISP_JOIN = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 1;
    //デバイスleave
    private static final int DLNA_MSG_ID_DEV_DISP_LEAVE = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 2;
    //BSデジタルに関して、チャンネルリストを発見
    private static final int DLNA_MSG_ID_BS_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 3;
    //地上波(terrestrial)に関して、チャンネルリストを発見
    private static final int DLNA_MSG_ID_TER_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 4;
    //ひかりTVに関して、チャンネルリストを発見
    private static final int DLNA_MSG_ID_HIKARI_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 5;

    //DMS情報
    private DlnaDMSInfo mDMSInfo = new DlnaDMSInfo();

    private DlnaRecVideoListener mDlnaRecVideoListener;
    private DlnaBsChListListener mDlnaBsChListListener;
    private DlnaTerChListListener mDlnaTerChListListener;
    private DlnaHikariChListListener mDlnaHikariChListListener;
    private boolean mIsDlnaRunning = false;
    private DlnaDevListListener mDlnaDevListListener = null;
    private long mNativeDlna = 0;
    private DlnaDmsItem mCurrentDlnaDmsItem;

    /**
     * 機能：デフォールト構造を禁止
     */
    private DlnaInterface() {
    }

    /**
     * 機能：インスタンスを戻す
     *
     * @return インスタンス
     */
    public static DlnaInterface getInstance() {
        return sDlnaInterface;
    }

    /**
     * 機能：指定するudnのdmsが存在しるか
     * @param udn udn
     * @return 存在しるか
     */
    public boolean isDmsAvailable(String udn) {
        return mDMSInfo.exists(udn);
    }

    /**
     * 機能：Listenerを設定
     *
     * @param lis listener
     */
    public void setDlnaDevListListener(DlnaDevListListener lis) {
        synchronized (this) {
            mDlnaDevListListener = lis;
        }
    }

    /**
     * 機能：Listenerを設定
     *
     * @param lis listener
     */
    public void setDlnaBsChListListener(DlnaBsChListListener lis) {
        synchronized (this) {
            mDlnaBsChListListener = lis;
        }
    }

    /**
     * 機能：Listenerを設定
     *
     * @param lis listener
     */
    public void setDlnaTerChListListener(DlnaTerChListListener lis) {
        synchronized (this) {
            mDlnaTerChListListener = lis;
        }
    }

    /**
     * 機能：Listenerを設定
     *
     * @param lis listener
     */
    public void setDlnaHikariChListListener(DlnaHikariChListListener lis) {
        synchronized (this) {
            mDlnaHikariChListListener = lis;
        }
    }

    /**
     * 機能：カレントDMSInfoを戻す
     * @return カレントDMSInfo
     */
    public DlnaDMSInfo getDlnaDMSInfo() {
        return mDMSInfo;
    }

    /**
     * 機能：DlnaListenerを設定
     *
     * @param lis listener
     */
    public void setDlnaRecVideoBaseListener(DlnaRecVideoListener lis) {
        synchronized (this) {
            mDlnaRecVideoListener = lis;
        }
    }

    /**
     * 機能：Dlna機能を開始
     *
     * @return
     */
    public boolean startDlna() {
        synchronized (this) {
            if (mIsDlnaRunning) {
                return true;
            }

            if (0 == mNativeDlna) {
                mNativeDlna = nativeCreateDlnaObject();
            }
            if (0 == mNativeDlna) {
                return false;
            }

            mDMSInfo.clear();
            mIsDlnaRunning = nativeStartDlna(mNativeDlna);
            return mIsDlnaRunning;
        }
    }

    /**
     * 機能：Dlna機能を停止
     */
    public void stopDlna() {
        synchronized (this) {
            if (!mIsDlnaRunning) {
                return;
            }

            nativeStopDlna(mNativeDlna);
            mDMSInfo.clear();
            mNativeDlna = 0;
            setDlnaStatus(false);
        }
    }

    /**
     * 機能：録画ヴィデオ一覧を発見
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseRecVideoDms() {
        //return browseRecVideoDms(mNativeDlna, ctl);
        if(null==mCurrentDlnaDmsItem || null==mCurrentDlnaDmsItem.mControlUrl || 1>mCurrentDlnaDmsItem.mControlUrl.length()){
            return false;
        }
        boolean ret= browseRecVideoDms(mNativeDlna, mCurrentDlnaDmsItem.mControlUrl);
        return ret;
    }

    /**
     * 機能：BSデジタルに関して、チャンネルリストを発見
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseBsChListDms() {
        if(null==mCurrentDlnaDmsItem || null==mCurrentDlnaDmsItem.mControlUrl || 1>mCurrentDlnaDmsItem.mControlUrl.length()){
            return false;
        }
        boolean ret= browseBsChListDms(mNativeDlna, mCurrentDlnaDmsItem.mControlUrl);
        return ret;
    }

    /**
     * 機能：dmsが存在すると、画面に通知する
     * @param content content
     * @param item dms item
     */
    private void notifyDeviceJoinInv(ArrayList<Object> content, DlnaDmsItem item){
        if(null==content || 0==content.size() || null==item || null==item.mUdn || 1>item.mUdn.length()){
            return;
        }
        boolean has= mDMSInfo.exists(item.mUdn);
        if(!has){
            mDMSInfo.add(item);
            ArrayList<Object> dmss=new ArrayList<>();
            dmss.add(item);
            onDeviceJoin(dmss);
        }
    }

    /**
     * 機能：地上波に関して、チャンネルリストを発見
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseTerChListDms() {
        //return browseTerChListDms(mNativeDlna, ctl);
        if(null==mCurrentDlnaDmsItem || null==mCurrentDlnaDmsItem.mControlUrl || 1>mCurrentDlnaDmsItem.mControlUrl.length()){
            return false;
        }
        boolean ret= browseTerChListDms(mNativeDlna, mCurrentDlnaDmsItem.mControlUrl);
        return ret;
    }

    /**
     * 機能：ひかりTVに関して、チャンネルリストを発見
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseHikariChListDms() {
        //return browseHikariChListDms(mNativeDlna, ctl);
        if(null==mCurrentDlnaDmsItem || null==mCurrentDlnaDmsItem.mControlUrl || 1>mCurrentDlnaDmsItem.mControlUrl.length()){
            return false;
        }
        boolean ret= browseHikariChListDms(mNativeDlna, mCurrentDlnaDmsItem.mControlUrl);
        return ret;
    }

    /**
     * 機能：jni c/c++からの通知を処理
     * @param msg msg
     * @param content content
     */
    public void notifyFromNative(int msg, String content) {
        Log.d("", "msg=" + msg + ", content=" + content);
        switch (msg) {
            case DLNA_MSG_ID_DEV_DISP_LEAVE:
                removeDms(content);
                break;
        }
    }

    /**
     * 機能：jni c/c++からのobj通知を処理
     * @param msg msg
     * @param content content
     */
    public void notifyObjFromNative(int msg, ArrayList<Object> content) {
        Log.d("", "msg=" + msg + ", content=" + content);
        switch (msg) {
            case DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST:
                onRecVideo(content);
                notifyDeviceJoinInv(content, mCurrentDlnaDmsItem);
                break;
            case DLNA_MSG_ID_DEV_DISP_JOIN:
                onDeviceJoin(content);
                break;
            case DLNA_MSG_ID_BS_CHANNEL_LIST:
                onBsChList(content);
                notifyDeviceJoinInv(content, mCurrentDlnaDmsItem);
                break;
            case DLNA_MSG_ID_TER_CHANNEL_LIST:
                onTerChList(content);
                notifyDeviceJoinInv(content, mCurrentDlnaDmsItem);
                break;
            case DLNA_MSG_ID_HIKARI_CHANNEL_LIST:
                onHikariChList(content);
                notifyDeviceJoinInv(content, mCurrentDlnaDmsItem);
                break;
        }
    }

    /**
     * 機能：jni c/c++からの録画情報を処理
     * @param content content
     */
    private void onRecVideo(ArrayList<Object> content) {
        if (null != mDlnaRecVideoListener) {
            DlnaRecVideoInfo info = DlnaRecVideoInfo.fromArrayList(content);
            if (null != info) {
                mDlnaRecVideoListener.onVideoBrows(info);
            }
        }
    }

    /**
     * 機能：jni c/c++からのチャンネルを処理
     * @param content content
     */
    private void onBsChList(ArrayList<Object> content) {
        if (null != mDlnaBsChListListener) {
            DlnaBsChListInfo info = DlnaBsChListInfo.fromArrayList(content);
            if (null != info) {
                mDlnaBsChListListener.onListUpdate(info);
            }
        }
    }

    /**
     * 機能：jni c/c++からのチャンネルを処理
     * @param content content
     */
    private void onTerChList(ArrayList<Object> content) {
        if (null != mDlnaTerChListListener) {
            DlnaTerChListInfo info = DlnaTerChListInfo.fromArrayList(content);
            if (null != info) {
                mDlnaTerChListListener.onListUpdate(info);
            }
        }
    }

    /**
     * 機能：jni c/c++からのチャンネルを処理
     * @param content content
     */
    private void onHikariChList(ArrayList<Object> content) {
        if (null != mDlnaHikariChListListener) {
            DlnaHikariChListInfo info = DlnaHikariChListInfo.fromArrayList(content);
            if (null != info) {
                mDlnaHikariChListListener.onListUpdate(info);
            }
        }
    }

    /**
     * 機能：jni c/c++からの新しいdms加入を処理
     * @param content content
     */
    private void onDeviceJoin(ArrayList<Object> content) {
        if (null == content || 0 == content.size()) {
            return;
        }

        DlnaDmsItem item = (DlnaDmsItem) content.get(0);
        mDMSInfo.add(item);

        if (null != mDlnaDevListListener) {
            mDlnaDevListListener.onDeviceJoin(mDMSInfo, item);
        }
    }

    /**
     * 機能：dmsを削除
     * @param content content to remove
     */
    private void removeDms(String content) {
        mDMSInfo.remove(content);
        if (null != mDlnaDevListListener) {
            mDlnaDevListListener.onDeviceLeave(mDMSInfo, content);
        }
    }

    /**
     * 機能：使用しているDmsをDlaninterfaceクラスに通知し、
     * 　　　Dlaninterfaceクラスはそのdms以外のdms変動情報をDlnaProviderRecoredVideoに通知しない
     *
     * @param item 使用しているDlnaDmsItem
     */
    public boolean registerCurrentDms(DlnaDmsItem item) {
        if (null != mDMSInfo && null!=item) {
            mCurrentDlnaDmsItem = item;
            return true;
        }
        return false;
    }

    /**
     * 機能：dlna statusを設定する
     * @param status　status
     */
    private synchronized void setDlnaStatus(boolean status) {
        mIsDlnaRunning = status;
    }

    /*
     * 機能：libをロードする
     */
    static {
        System.loadLibrary("dtvtlib");
    }

    /**
     * 機能：jni関数
     * @return c++ dlna object
     */
    private native long nativeCreateDlnaObject();

    /**
     * 機能：jni関数
     * @return 操作結果
     */
    private native boolean nativeStartDlna(long prt);

    /**
     * 機能：jni関数
     * @return 操作結果
     */
    private native boolean nativeStopDlna(long prt);

    /**
     * 機能：jni関数
     * @return 操作結果
     */
    private native boolean browseRecVideoDms(long prt, String ctl);

    /**
     * 機能：jni関数
     * @return 操作結果
     */
    private native boolean browseBsChListDms(long prt, String ctl);

    /**
     * 機能：jni関数
     * @return 操作結果
     */
    private native boolean browseTerChListDms(long prt, String ctl);

    /**
     * 機能：jni関数
     * @return 操作結果
     */
    private native boolean browseHikariChListDms(long prt, String ctl);
}