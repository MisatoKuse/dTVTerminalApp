/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

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
    //Download progress
    public static final int DLNA_MSG_ID_DL_PROGRESS = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 6;
    //Download status
    public static final int DLNA_MSG_ID_DL_STATUS = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 7;
    //Download param
    private static final int DLNA_MSG_ID_DL_XMLPARAM = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 8;

    //DMS情報
    private DlnaDMSInfo mDMSInfo = new DlnaDMSInfo();

    private DlnaRecVideoListener mDlnaRecVideoListener;
    private DlnaBsChListListener mDlnaBsChListListener;
    private DlnaTerChListListener mDlnaTerChListListener;
    private DlnaHikariChListListener mDlnaHikariChListListener;
    private boolean mIsDlnaRunning = false;
    private DlnaDevListListener mDlnaDevListListener = null;
    private long mNativeDlna = 0;
    private DlnaDmsItem mCurrentDmsItem;

    //Dlna info
    private DlnaRecVideoInfo mDlnaRecVideoInfo = new DlnaRecVideoInfo();
    private DlnaBsChListInfo mDlnaBsChListInfo;
    private DlnaTerChListInfo mDlnaTerChListInfo;
    private Handler mHandler= new Handler();

//    //Download
//    private DlnaDlListener mDlnaDlListener;
//    private Context mContext;

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
     * @return boolean
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

    private final int DLNA_IF_THREAD_DELAY=200;

    /**
     * 機能：録画ヴィデオ一覧を発見
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseRecVideoDms() {
        //return browseRecVideoDms(mNativeDlna, ctl);
        DTVTLogger.start();
        if(null== mCurrentDmsItem || null== mCurrentDmsItem.mControlUrl || 1> mCurrentDmsItem.mControlUrl.length()){
            return false;
        }
        if(null!=mDlnaRecVideoInfo && 0<mDlnaRecVideoInfo.size()){
            final ArrayList<Object> content= DlnaRecVideoInfo.toArrayList(mDlnaRecVideoInfo);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyObjFromNative(DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST, content);
                }
            }, DLNA_IF_THREAD_DELAY);
            DTVTLogger.end();
            return true;
        }
        boolean ret= browseRecVideoDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
        DTVTLogger.debug("call c++ browseRecVideoDms");
        DTVTLogger.end();
        return ret;
    }

    /**
     * 機能：BSデジタルに関して、チャンネルリストを発見
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseBsChListDms() {
        //boolean ret= browseBsChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
        if(null== mCurrentDmsItem || null== mCurrentDmsItem.mControlUrl || 1> mCurrentDmsItem.mControlUrl.length()){
            return false;
        }
        if(null!=mDlnaBsChListInfo && 0<mDlnaBsChListInfo.size()){
            final ArrayList<Object> content= DlnaBsChListInfo.toArrayList(mDlnaBsChListInfo);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyObjFromNative(DLNA_MSG_ID_BS_CHANNEL_LIST, content);
                }
            }, DLNA_IF_THREAD_DELAY);
            DTVTLogger.end();
            return true;
        }
        boolean ret= browseBsChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
        DTVTLogger.debug("call c++ browseBsChListDms");
        DTVTLogger.end();

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
        if(null== mCurrentDmsItem || null== mCurrentDmsItem.mControlUrl || 1> mCurrentDmsItem.mControlUrl.length()){
            return false;
        }
        if(null!=mDlnaTerChListInfo && 0<mDlnaBsChListInfo.size()){
            final ArrayList<Object> content= DlnaTerChListInfo.toArrayList(mDlnaTerChListInfo);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyObjFromNative(DLNA_MSG_ID_TER_CHANNEL_LIST, content);
                }
            }, DLNA_IF_THREAD_DELAY);
            DTVTLogger.end();
            return true;
        }
        boolean ret= browseTerChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
        DTVTLogger.debug("call c++ browseTerChListDms");
        DTVTLogger.end();

        return ret;
    }

    /**
     * 機能：ひかりTVに関して、チャンネルリストを発見
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseHikariChListDms() {
        //return browseHikariChListDms(mNativeDlna, ctl);
        if(null== mCurrentDmsItem || null== mCurrentDmsItem.mControlUrl || 1> mCurrentDmsItem.mControlUrl.length()){
            return false;
        }
        return browseHikariChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
    }

    /**
     * 機能：jni c/c++からの通知を処理
     * @param msg msg
     * @param content content
     */
    public void notifyFromNative(int msg, String content) {
        DTVTLogger.debug("msg=" + msg + ", content=" + content);
        switch (msg) {
            case DLNA_MSG_ID_DEV_DISP_LEAVE:
                removeDms(content);
                break;
            default:
                break;
        }
    }

    /**
     * 機能：jni c/c++からのobj通知を処理
     * @param msg msg
     * @param content content
     */
    public void notifyObjFromNative(int msg, ArrayList<Object> content) {
        DTVTLogger.debug("msg=" + msg + ", content=" + content);
        if(onError(msg, content)){
            return;
        }
        switch (msg) {
            case DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST:
                onRecVideo(content);
                notifyDeviceJoinInv(content, mCurrentDmsItem);
                break;
            case DLNA_MSG_ID_DEV_DISP_JOIN:
                onDeviceJoin(content);
                break;
            case DLNA_MSG_ID_BS_CHANNEL_LIST:
                onBsChList(content);
                notifyDeviceJoinInv(content, mCurrentDmsItem);
                break;
            case DLNA_MSG_ID_TER_CHANNEL_LIST:
                onTerChList(content);
                notifyDeviceJoinInv(content, mCurrentDmsItem);
                break;
            case DLNA_MSG_ID_HIKARI_CHANNEL_LIST:
                onHikariChList(content);
                notifyDeviceJoinInv(content, mCurrentDmsItem);
                break;
            default:
                break;
        }
    }

    private final String sErrorMsgDMS="DMS Error";
    private final String sErrorMsgRecordVideo="録画一覧データ取得に失敗しました";
    private final String sErrorMsgTerChannelList="地上波チャンネルリスト取得に失敗しました";
    private final String sErrorMsgBsChannelList="BSチャンネルリスト取得に失敗しました";
    private final String sErrorMsgHikariChannelList="ひかりチャンネルリスト取得に失敗しました";

    private boolean onError(int msg, ArrayList<Object> content){
        boolean ret=false;
        switch (msg) {
            case DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST:
                if(null!=mDlnaRecVideoListener && null!=content && 0==content.size()){
                    mDlnaRecVideoListener.onError(sErrorMsgRecordVideo);
                    ret=true;
                }
                break;
            case DLNA_MSG_ID_DEV_DISP_JOIN:
                if(null!=mDlnaDevListListener && null!=content && 0==content.size()){
                    mDlnaDevListListener.onError(sErrorMsgDMS);
                    ret=true;
                }
                break;
            case DLNA_MSG_ID_BS_CHANNEL_LIST:
                if(null!=mDlnaBsChListListener && null!=content && 0==content.size()){
                    mDlnaBsChListListener.onError(sErrorMsgBsChannelList);
                    ret=true;
                }
                break;
            case DLNA_MSG_ID_TER_CHANNEL_LIST:
                if(null!=mDlnaTerChListListener && null!=content && 0==content.size()){
                    mDlnaTerChListListener.onError(sErrorMsgTerChannelList);
                    ret=true;
                }
                break;
            case DLNA_MSG_ID_HIKARI_CHANNEL_LIST:
                if(null!=mDlnaHikariChListListener && null!=content && 0==content.size()){
                    mDlnaHikariChListListener.onError(sErrorMsgHikariChannelList);
                    ret=true;
                }
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * 機能：jni c/c++からの録画情報を処理
     * @param content content
     */
    private void onRecVideo(ArrayList<Object> content) {
        if (null != mDlnaRecVideoListener) {
            DlnaRecVideoInfo info = DlnaRecVideoInfo.fromArrayList(content);
            if (null != info) {
                mDlnaRecVideoInfo=info;
                mDlnaRecVideoListener.onVideoBrows(mDlnaRecVideoInfo);
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
            if(null!=mDlnaDevListListener ){
                mDlnaDevListListener.onError(sErrorMsgDMS);
            }
            return;
        }

        DlnaDmsItem item = (DlnaDmsItem) content.get(0);
        //if(null==item || null==item.mUdn){
        if(!DlnaDmsItem.isDmsItemValid(item)){
            DTVTLogger.debug("onDeviceJoin(), DlnaDmsItem invallid so skip");
            return;
        }
        mDMSInfo.add(item);

        if (null != mDlnaDevListListener) {
            mDlnaDevListListener.onDeviceJoin(mDMSInfo, item);
        }
        if(null!=mCurrentDmsItem && null!=mCurrentDmsItem.mUdn && mCurrentDmsItem.mUdn.equals(item.mUdn)){
            mCurrentDmsItem=item;
            //本番ソース begin
            browseBsChListDms();
            //browseTerChListDms();
            //本番ソース end
            //browseRecVideoDms();    //test
        }
    }

    /**
     * 機能：dmsを削除
     * @param content content to remove
     */
    private void removeDms(String content) {
        if(null!=mCurrentDmsItem && 0<mCurrentDmsItem.mUdn.length() && null!=content && 0<content.length()){
            if(mCurrentDmsItem.mUdn.equals(content)){
                mDlnaRecVideoInfo.clearAll();
            }
        }
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
        if (null != mDMSInfo && DlnaDmsItem.isDmsItemValid(item) ) {
            mCurrentDmsItem = item;
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

    /**
     * 機能：カレントDMSを削除
     */
    public void dmsRemove(){
        if(null!=mDlnaDevListListener){
            String udn="";
            if(null!=mCurrentDmsItem){
                udn= mCurrentDmsItem.mUdn;
            }
            mDlnaDevListListener.onDeviceLeave(mDMSInfo, udn);
        }
        mCurrentDmsItem=null;
    }

    /**
     * todo: WiFiは切ると、対応する
     */
    public void toDoWithWiFiLost(){

    }

    /**
     * 機能：download
     * @param itemId itemId
     * @return xmlToDl xmlToDl
     */
    public String getDlParam(final String itemId) {
        DTVTLogger.start();
        DTVTLogger.end();
        return getDlParam(mNativeDlna, itemId);
    }

    private native String getDlParam(long prt, String itemId);

    public static String getXmlToDl(String itemId){
        if(null==sDlnaInterface || null==itemId || itemId.isEmpty()) {
            return null;
        }

        return sDlnaInterface.getDlParam(itemId);
    }
}