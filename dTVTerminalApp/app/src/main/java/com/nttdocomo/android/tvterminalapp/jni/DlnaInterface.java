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

    //current dms udn
    private String mCurrentDmsUdn;
    private DlnaRecVideoListener mDlnaRecVideoListener;
    private DlnaBsChListListener mDlnaBsChListListener;
    private DlnaTerChListListener mDlnaTerChListListener;
    private DlnaHikariChListListener mDlnaHikariChListListener;
    private boolean mIsDlnaRunning = false;
    private DlnaDevListListener mDlnaDevListListener = null;
    private long mNativeDlna = 0;

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
     * 機能：DlnaListenerを設定
     *
     * @param lis listener
     */
    public void setDlnaDevListListener(DlnaDevListListener lis) {
        synchronized (this) {
            mDlnaDevListListener = lis;
        }
    }

    public void setDlnaBsChListListener(DlnaBsChListListener lis) {
        synchronized (this) {
            mDlnaBsChListListener = lis;
        }
    }

    public void setDlnaTerChListListener(DlnaTerChListListener lis) {
        synchronized (this) {
            mDlnaTerChListListener = lis;
        }
    }

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
     * @param ctl ControlUrl
     * @return 成功true
     */
    public boolean browseRecVideoDms(String ctl) {
        return browseRecVideoDms(mNativeDlna, ctl);
    }

    /**
     * 機能：BSデジタルに関して、チャンネルリストを発見
     *
     * @param ctl ControlUrl
     * @return 成功true
     */
    public boolean browseBsChListDms(String ctl) {
        return browseBsChListDms(mNativeDlna, ctl);
    }

    public boolean browseTerChListDms(String ctl) {
        return browseTerChListDms(mNativeDlna, ctl);
    }

    public boolean browseHikariChListDms(String ctl) {
        return browseHikariChListDms(mNativeDlna, ctl);
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
                break;
            case DLNA_MSG_ID_DEV_DISP_JOIN:
                onDeviceJoin(content);
                //addDms(content);
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

    private void onBsChList(ArrayList<Object> content) {
        if (null != mDlnaBsChListListener) {
            DlnaBsChListInfo info = DlnaBsChListInfo.fromArrayList(content);
            if (null != info) {
                mDlnaBsChListListener.onListUpdate(info);
            }
        }
    }

    private void onTerChList(ArrayList<Object> content) {
        if (null != mDlnaTerChListListener) {
            DlnaTerChListInfo info = DlnaTerChListInfo.fromArrayList(content);
            if (null != info) {
                mDlnaTerChListListener.onListUpdate(info);
            }
        }
    }

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
     * @param curDmsUdn 使用しているDmsのudn
     */
    public void registerCurrentDms(String curDmsUdn) {
        if (null != mDMSInfo && mDMSInfo.exists(curDmsUdn)) {
            mCurrentDmsUdn = curDmsUdn;
        }
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