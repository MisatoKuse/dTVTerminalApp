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
    private static DlnaInterface sDlnaInterface= new DlnaInterface();

    //Browseコンテンツ
    private static final int DLNA_MSG_ID_BROWSE_SOAP=0;
    //デバイスjoin
    private static final int DLNA_MSG_ID_DEV_DISP_JOIN= DLNA_MSG_ID_BROWSE_SOAP + 1;
    //デバイスleave
    private static final int DLNA_MSG_ID_DEV_DISP_LEAVE=DLNA_MSG_ID_BROWSE_SOAP + 2;

    //DMS情報
    private DlnaDMSInfo mDMSInfo=new DlnaDMSInfo();

    //current dms udn
    private String mCurrentDmsUdn;

    private DlnaRecVideoListener mDlnaRecVideoListener;

    private boolean mIsDlnaRunning = false;

    /**
     * 機能：デフォールト構造を禁止
     */
    private DlnaInterface(){}

    /**
     * 機能：インスタンスを戻す
     * @return インスタンス
     */
    public static DlnaInterface getInstance(){
        return sDlnaInterface;
    }

//下記のListenerは将来使う可能ですので、一時保留している。
//    public interface DlnaListener{
//        /**
//         * 機能：Listenerに、録画一覧情報が届く時、コールされる
//         * @param content content
//         */
//        void onVideoBrows(String content);
//
//        /**
//         * 機能：Listenerに、新しいDms情報が届く時、コールされる
//         * @param curInfo カレントDlnaDMSInfo
//         * @param newItem 新しいDms情報
//         */
//        void onDeviceJoin(DlnaDMSInfo curInfo, DlnaDmsItem newItem);
//
//        /**
//         * 機能：Listenerに、Dmsが消える時、コールされる
//         * @param curInfo　　　カレントDlnaDMSInfo
//         * @param leaveDmsUdn　消えるDmsのudn名
//         */
//        void onDeviceLeave(DlnaDMSInfo curInfo, String leaveDmsUdn);
//
//        /**
//         * 機能：Listenerに、エラーメセッジを送信
//         * @param msgId  エラー
//         * @param msg     エラー情報
//         */
//        void onError(int msgId, String msg);
//
//        /**
//         * 機能：各DlnaProviderに、使用しているDmsを戻す
//         * 　　　この設計の理由は使用しているDms以外のDmsは、ネットワークに加入と消える時、
//         * 　　　DlnaProviderにイベントを通知しないよう
//         * 　　　例外はDms一覧用のDlnaProviderである。
//         * @return 使用しているDmsのudn名
//         */
//         String getCurrentDmsUdn();
//    }

    public boolean isDmsAvailable(String udn){
        return mDMSInfo.exists(udn);
    }

    private DlnaDevListListener mDlnaDevListListener=null;
    private long mNativeDlna=0;

    /**
     * 機能：DlnaListenerを設定
     * @param lis listener
     */
    public void setDlnaDevListListener(DlnaDevListListener lis){
        synchronized (this) {
            mDlnaDevListListener = lis;
        }
    }

    /**
     * 機能：DlnaListenerを設定
     * @param lis listener
     */
    public void setDlnaRecVideoBaseListener(DlnaRecVideoListener lis){
        synchronized (this) {
            mDlnaRecVideoListener = lis;
        }
    }

    /**
     * 機能：Dlna機能を開始
     * @return
     */
    public boolean startDlna(){
        synchronized (this) {
            if(mIsDlnaRunning){
                return true;
            }

            if(0==mNativeDlna){
                mNativeDlna = nativeCreateDlnaObject();
            }
            if(0==mNativeDlna){
                return false;
            }

            mDMSInfo.clear();
            mIsDlnaRunning = nativeStartDlna(mNativeDlna);
            return mIsDlnaRunning;
        }
    }

    public void stopDlna(){
        synchronized (this) {
            if(!mIsDlnaRunning){
                return;
            }

            nativeStopDlna(mNativeDlna);
            mDMSInfo.clear();
            mNativeDlna=0;
            mIsDlnaRunning=false;
        }
    }

    /**
     * 機能：録画ヴィデオ一覧を発見
     * @param ctl ControlUrl
     * @return 成功true
     */
    public boolean browseRecVideoDms(String ctl){
        return browseRecVideoDms(mNativeDlna, ctl);
    }

    public void notifyFromNative(int msg, String content){
        Log.d("", "msg=" + msg + ", content=" + content);
        switch(msg) {
            case DLNA_MSG_ID_DEV_DISP_LEAVE:
                removeDms(content);
                break;
        }
    }

    public void notifyObjFromNative(int msg, ArrayList<Object> content){
        Log.d("", "msg=" + msg + ", content=" + content);
        switch(msg) {
            case DLNA_MSG_ID_BROWSE_SOAP:
                onRecVideo(content);
                break;
            case DLNA_MSG_ID_DEV_DISP_JOIN:
                onDeviceJoin(content);
                //addDms(content);
                break;
        }
    }

    private void onRecVideo(ArrayList<Object> content){
        if(null!=mDlnaRecVideoListener){
            DlnaRecVideoInfo info=DlnaRecVideoInfo.fromArrayList(content);
            if(null!=info){
                mDlnaRecVideoListener.onVideoBrows(info);
            }
        }
    }

    private void onDeviceJoin(ArrayList<Object> content) {
        if(null==content || 0==content.size()){
            return;
        }

        DlnaDmsItem item= (DlnaDmsItem)content.get(0);
        mDMSInfo.add(item);

        if(null!=mDlnaDevListListener){
            mDlnaDevListListener.onDeviceJoin(mDMSInfo, item);
        }
    }

    private void removeDms(String content){
        mDMSInfo.remove(content);
        if(null!=mDlnaDevListListener){
            mDlnaDevListListener.onDeviceLeave(mDMSInfo, content);
        }
    }

    /**
     * 機能：使用しているDmsをDlaninterfaceクラスに通知し、
     * 　　　Dlaninterfaceクラスはそのdms以外のdms変動情報をDlnaProviderRecoredVideoに通知しない
     * @param curDmsUdn 使用しているDmsのudn
     */
    public void registerCurrentDms(String curDmsUdn){
        if(null!=mDMSInfo && mDMSInfo.exists(curDmsUdn)){
            mCurrentDmsUdn=curDmsUdn;
        }
    }

    private synchronized void setDlnaStatus(boolean status){
        mIsDlnaRunning = status;
    }

    //jni関数
    private native long nativeCreateDlnaObject();
    private native boolean nativeStartDlna(long prt);
    private native boolean nativeStopDlna(long prt);
    private native boolean browseRecVideoDms(long prt, String ctl);
}