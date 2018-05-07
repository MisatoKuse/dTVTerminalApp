/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import android.os.Handler;

import com.digion.dixim.android.util.AribExternalCharConverter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.bs.DlnaBsChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.bs.DlnaBsChListItem;
import com.nttdocomo.android.tvterminalapp.jni.bs.DlnaBsChListListener;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDevListListener;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsInfo;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaHikariChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaHikariChListItem;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaHikariChListListener;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.jni.ter.DlnaTerChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.ter.DlnaTerChListItem;
import com.nttdocomo.android.tvterminalapp.jni.ter.DlnaTerChListListener;

import java.util.ArrayList;

/**
 * 機能：Singletonで実現され、Dlnaに関する機能を纏めるベースクラス.
 */
public class DlnaInterface {

    /**Singleton.*/
    private static DlnaInterface sDlnaInterface = new DlnaInterface();

    /**Browseコンテンツ.*/
    private static final int DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST = 0;
    /**デバイスjoin.*/
    private static final int DLNA_MSG_ID_DEV_DISP_JOIN = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 1;
    /**デバイスleave.*/
    private static final int DLNA_MSG_ID_DEV_DISP_LEAVE = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 2;
    /**BSデジタルに関して、チャンネルリストを発見.*/
    private static final int DLNA_MSG_ID_BS_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 3;
    /**地上波(terrestrial)に関して、チャンネルリストを発見.*/
    private static final int DLNA_MSG_ID_TER_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 4;
    /**ひかりTVに関して、チャンネルリストを発見.*/
    private static final int DLNA_MSG_ID_HIKARI_CHANNEL_LIST = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 5;
    /**Download progress.*/
    public static final int DLNA_MSG_ID_DL_PROGRESS = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 6;
    /**Download status.*/
    public static final int DLNA_MSG_ID_DL_STATUS = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 7;
    /**Download param.*/
    private static final int DLNA_MSG_ID_DL_XMLPARAM = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 8;
    /** ローカルレジストレーションステータス.*/
    public static final int DLNA_MSG_ID_RM_STATUS = DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST + 9;

    /**DMS情報.*/
    private DlnaDmsInfo mDMSInfo = new DlnaDmsInfo();
    /**録画ビデオリスナー.*/
    private DlnaRecVideoListener mDlnaRecVideoListener;
    /**BSチャンネルリスナー.*/
    private DlnaBsChListListener mDlnaBsChListListener;
    /**地上波チャンネルリスナー.*/
    private DlnaTerChListListener mDlnaTerChListListener;
    /**ひかりチャンネルリスナー.*/
    private DlnaHikariChListListener mDlnaHikariChListListener;
    /**Dlna稼働フラグ.*/
    private boolean mIsDlnaRunning = false;
    /**デバイスリストリスナー.*/
    private DlnaDevListListener mDlnaDevListListener = null;
    /**NativeDlna.*/
    private long mNativeDlna = 0;
    /**現在のDMS Item.*/
    private DlnaDmsItem mCurrentDmsItem;

    // TODO　:エラー表示仕様によって削除（コード上に文字列定義しない
    /**DMS Errorエラーメッセージ.*/
    private static final String sErrorMsgDMS = "DMS Error";
    /**録画一覧データ取得失敗エラーメッセージ.*/
    private static final String sErrorMsgRecordVideo = "録画一覧データ取得に失敗しました";
    /**地上波チャンネルリスト取得失敗エラーメッセージ.*/
    private static final String sErrorMsgTerChannelList = "地上波チャンネルリスト取得に失敗しました";
    /**BSチャンネルリスト取得失敗エラーメッセージ.*/
    private static final String sErrorMsgBsChannelList = "BSチャンネルリスト取得に失敗しました";
    /**ひかりチャンネルリスト取得失敗エラーメッセージ.*/
    private static final String sErrorMsgHikariChannelList = "ひかりチャンネルリスト取得に失敗しました";

    //Dlna info
    /**BSチャンネルリスト情報.*/
    private DlnaBsChListInfo mDlnaBsChListInfo;
    /**地上波チャンネルリスト情報.*/
    private DlnaTerChListInfo mDlnaTerChListInfo;
    /**ハンドラー.*/
    private Handler mHandler = new Handler();

    /**
     * 機能：デフォールト構造を禁止.
     */
    private DlnaInterface() {
    }

    /**
     * 機能：インスタンスを戻す.
     *
     * @return インスタンス
     */
    public static DlnaInterface getInstance() {
        return sDlnaInterface;
    }

    /**
     * 機能：指定するudnのdmsが存在しるか.
     * @param udn udn
     * @return 存在しるか
     */
    public boolean isDmsAvailable(final String udn) {
        return mDMSInfo.exists(udn);
    }

    /**
     * 機能：Listenerを設定.
     *
     * @param lis listener
     */
    public void setDlnaDevListListener(final DlnaDevListListener lis) {
        synchronized (this) {
            mDlnaDevListListener = lis;
        }
    }

    /**
     * 機能：Listenerを設定.
     *
     * @param lis listener
     */
    public void setDlnaBsChListListener(final DlnaBsChListListener lis) {
        synchronized (this) {
            mDlnaBsChListListener = lis;
        }
    }

    /**
     * 機能：Listenerを設定.
     *
     * @param lis listener
     */
    public void setDlnaTerChListListener(final DlnaTerChListListener lis) {
        synchronized (this) {
            mDlnaTerChListListener = lis;
        }
    }

    /**
     * 機能：Listenerを設定.
     *
     * @param lis listener
     */
    public void setDlnaHikariChListListener(final DlnaHikariChListListener lis) {
        synchronized (this) {
            mDlnaHikariChListListener = lis;
        }
    }

    /**
     * 機能：カレントDMSInfoを戻す.
     * @return カレントDMSInfo
     */
    public DlnaDmsInfo getDlnaDmsInfo() {
        return mDMSInfo;
    }

    /**
     * 機能：DlnaListenerを設定.
     *
     * @param lis listener
     */
    public void setDlnaRecVideoBaseListener(final DlnaRecVideoListener lis) {
        synchronized (this) {
            mDlnaRecVideoListener = lis;
        }
    }

    /**
     * 機能：Dlna機能を開始.
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
     * 機能：Dlna機能を停止.
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
     * 機能：録画ビデオ一覧を発見.
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseRecVideoDms() {
        DTVTLogger.start();
        if (null == mCurrentDmsItem || null == mCurrentDmsItem.mControlUrl || 1 > mCurrentDmsItem.mControlUrl.length()) {
            return false;
        }
        synchronized (this) {
            boolean ret = browseRecVideoDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
            DTVTLogger.debug("call c++ browseRecVideoDms");
            DTVTLogger.end();
            return ret;
        }
    }

    /**
     * 機能：BSデジタルに関して、チャンネルリストを発見.
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseBsChListDms() {
        //boolean ret= browseBsChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
        if (null == mCurrentDmsItem || null == mCurrentDmsItem.mControlUrl || 1 > mCurrentDmsItem.mControlUrl.length()) {
            return false;
        }
        synchronized (this) {
            boolean ret = browseBsChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
            DTVTLogger.debug("call c++ browseBsChListDms");
            DTVTLogger.end();
            return ret;
        }
    }
    /**
     * 機能：多チャンネルに関して、チャンネルリストを発見.
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseHikariChListDms() {
        if (null == mCurrentDmsItem || null == mCurrentDmsItem.mControlUrl || 1 > mCurrentDmsItem.mControlUrl.length()) {
            return false;
        }
        synchronized (this) {
            boolean ret = browseHikariChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
            DTVTLogger.debug("call c++ browseBsChListDms");
            DTVTLogger.end();
            return ret;
        }
    }
    /**
     * 機能：dmsが存在すると、画面に通知する.
     * @param content content
     * @param item dms item
     */
    private void notifyDeviceJoinInv(final ArrayList<Object> content, final DlnaDmsItem item) {
        if (null == content || 0 == content.size() || null == item || null == item.mUdn || 1 > item.mUdn.length()) {
            return;
        }
        boolean has = mDMSInfo.exists(item.mUdn);
        if (!has) {
            mDMSInfo.add(item);
            ArrayList<Object> dmss = new ArrayList<>();
            dmss.add(item);
            onDeviceJoin(dmss);
        }
    }

    /**
     * 機能：地上波に関して、チャンネルリストを発見.
     *
     * @return 成功:true 失敗: false
     */
    public boolean browseTerChListDms() {
        //return browseTerChListDms(mNativeDlna, ctl);
        if (null == mCurrentDmsItem || null == mCurrentDmsItem.mControlUrl || 1 > mCurrentDmsItem.mControlUrl.length()) {
            return false;
        }
        synchronized (this) {
            boolean ret = browseTerChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
            DTVTLogger.debug("call c++ browseTerChListDms");
            DTVTLogger.end();

            return ret;
        }
    }

    /**
     * 機能：jni c/c++からの通知を処理.
     * @param msg msg
     * @param content content
     */
    public void notifyFromNative(final int msg, final String content) {
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
     * 機能：jni c/c++からのobj通知を処理.
     * @param msg msg
     * @param content content
     */
    public void notifyObjFromNative(final int msg, final ArrayList<Object> content) {
        DTVTLogger.debug("msg=" + msg + ", content=" + content);
        if (onError(msg, content)) {
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

    /**
     * onError.
     * @param msg msg
     * @param content content
     * @return true or false
     */
    private boolean onError(final int msg, final ArrayList<Object> content) {
        boolean ret = false;
        if (null != content && 0 != content.size()) {
            return false;
        }
        switch (msg) {
            case DLNA_MSG_ID_BROWSE_REC_VIDEO_LIST:
                if (null != mDlnaRecVideoListener) {
                    mDlnaRecVideoListener.onError(sErrorMsgRecordVideo);
                    ret = true;
                }
                break;
            case DLNA_MSG_ID_DEV_DISP_JOIN:
                if (null != mDlnaDevListListener) {
                    mDlnaDevListListener.onError(sErrorMsgDMS);
                    ret = true;
                }
                break;
            case DLNA_MSG_ID_BS_CHANNEL_LIST:
                if (null != mDlnaBsChListListener) {
                    mDlnaBsChListListener.onError(sErrorMsgBsChannelList);
                    ret = true;
                }
                break;
            case DLNA_MSG_ID_TER_CHANNEL_LIST:
                if (null != mDlnaTerChListListener) {
                    mDlnaTerChListListener.onError(sErrorMsgTerChannelList);
                    ret = true;
                }
                break;
            case DLNA_MSG_ID_HIKARI_CHANNEL_LIST:
                if (null != mDlnaHikariChListListener) {
                    mDlnaHikariChListListener.onError(sErrorMsgHikariChannelList);
                    ret = true;
                }
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * 機能：jni c/c++からの録画情報を処理.
     * @param content content
     */
    private void onRecVideo(final ArrayList<Object> content) {
        if (null != mDlnaRecVideoListener) {
            DlnaRecVideoInfo info = DlnaRecVideoInfo.fromArrayList(content);
            if (null != info) {
                aribConvertRecorded(info);
                mDlnaRecVideoListener.onVideoBrows(info);
            }
        }
    }

    /**
     * ARIB変換.
     * @param info info
     */
    private void aribConvertRecorded(final DlnaRecVideoInfo info) {
        AribExternalCharConverter aribExternalCharConverter = AribExternalCharConverter.getInstance();
        if (null == aribExternalCharConverter) {
            DTVTLogger.debug("get AribExternalCharConverter instance failed");
            return;
        }
        if (null == info || 0 == info.size()) {
            return;
        }

        for (DlnaRecVideoItem item: info.getRecordVideoLists()) {
            if (null != item) {
                item.mTitle = aribExternalCharConverter.getConverted(item.mTitle);
            }
        }
    }

    /**
     * ARIB変換.
     * @param info info
     */
    private void aribConvertBs(final DlnaBsChListInfo info) {
        AribExternalCharConverter aribExternalCharConverter = AribExternalCharConverter.getInstance();
        if (null == aribExternalCharConverter) {
            DTVTLogger.debug("get AribExternalCharConverter instance failed");
            return;
        }
        if (null == info || 0 == info.size()) {
            return;
        }

        for (DlnaBsChListItem item: info.getBsChLists()) {
            if (null != item) {
                item.mChannelName = AribExternalCharConverter.getConverted(item.mChannelName);
            }
        }
    }

    /**
     * ARIB変換.
     * @param info info
     */
    private void aribConvertTer(final DlnaTerChListInfo info) {
        AribExternalCharConverter aribExternalCharConverter = AribExternalCharConverter.getInstance();
        if (null == aribExternalCharConverter) {
            DTVTLogger.debug("get AribExternalCharConverter instance failed");
            return;
        }
        if (null == info || 0 == info.size()) {
            return;
        }

        for (DlnaTerChListItem item: info.getTerChLists()) {
            if (null != item) {
                item.mChannelName = AribExternalCharConverter.getConverted(item.mChannelName);
            }
        }
    }

    /**
     * ARIB変換.
     * @param info info
     */
    private void aribConvertHikari(final DlnaHikariChListInfo info) {
        AribExternalCharConverter aribExternalCharConverter = AribExternalCharConverter.getInstance();
        if (null == aribExternalCharConverter) {
            DTVTLogger.debug("get AribExternalCharConverter instance failed");
            return;
        }
        if (null == info || 0 == info.size()) {
            return;
        }

        for (DlnaHikariChListItem item: info.getHikariChLists()) {
            if (null != item) {
                item.mTitle = AribExternalCharConverter.getConverted(item.mTitle);
            }
        }
    }

    /**
     * 機能：jni c/c++からのチャンネルを処理.
     * @param content content
     */
    private void onBsChList(final ArrayList<Object> content) {
        if (null != mDlnaBsChListListener) {
            DlnaBsChListInfo info = DlnaBsChListInfo.fromArrayList(content);
            if (null != info) {
                aribConvertBs(info);
                mDlnaBsChListListener.onListUpdate(info);
            }
        }
    }

    /**
     * 機能：jni c/c++からのチャンネルを処理.
     * @param content content
     */
    private void onTerChList(final ArrayList<Object> content) {
        if (null != mDlnaTerChListListener) {
            DlnaTerChListInfo info = DlnaTerChListInfo.fromArrayList(content);
            if (null != info) {
                aribConvertTer(info);
                mDlnaTerChListListener.onListUpdate(info);
            }
        }
    }

    /**
     * 機能：jni c/c++からのチャンネルを処理.
     * @param content content
     */
    private void onHikariChList(final ArrayList<Object> content) {
        if (null != mDlnaHikariChListListener) {
            DlnaHikariChListInfo info = DlnaHikariChListInfo.fromArrayList(content);
            if (null != info) {
                aribConvertHikari(info);
                mDlnaHikariChListListener.onListUpdate(info);
            }
        }
    }

    /**
     * 機能：jni c/c++からの新しいdms加入を処理.
     * @param content content
     */
    private void onDeviceJoin(final ArrayList<Object> content) {
        if (null == content || 0 == content.size()) {
            if (null != mDlnaDevListListener) {
                mDlnaDevListListener.onError(sErrorMsgDMS);
            }
            return;
        }

        DlnaDmsItem item = (DlnaDmsItem) content.get(0);
        //if(null==item || null==item.mUdn){
        if (!DlnaDmsItem.isDmsItemValid(item)) {
            DTVTLogger.debug("onDeviceJoin(), DlnaDmsItem invallid so skip");
            return;
        }
        mDMSInfo.add(item);

        if (null != mDlnaDevListListener) {
            mDlnaDevListListener.onDeviceJoin(mDMSInfo, item);
        }
        if (null != mCurrentDmsItem && null != mCurrentDmsItem.mUdn && mCurrentDmsItem.mUdn.equals(item.mUdn)) {
            mCurrentDmsItem = item;
        }
    }

    /**
     * 機能：dmsを削除.
     * @param content content to remove
     */
    private void removeDms(final String content) {
        mDMSInfo.remove(content);
        if (null != mDlnaDevListListener) {
            mDlnaDevListListener.onDeviceLeave(mDMSInfo, content);
        }
    }

    /**
     * 機能：使用しているDmsをDlaninterfaceクラスに通知し、.
     * 　　　Dlaninterfaceクラスはそのdms以外のdms変動情報をDlnaProviderRecoredVideoに通知しない
     *
     * @param item 使用しているDlnaDmsItem
     * @return  true or false
     */
    public boolean registerCurrentDms(final DlnaDmsItem item) {
        if (null != mDMSInfo && DlnaDmsItem.isDmsItemValid(item)) {
            mCurrentDmsItem = item;
            return true;
        }
        return false;
    }

    /**
     * 機能：dlna statusを設定する.
     * @param status  status
     */
    private synchronized void setDlnaStatus(final boolean status) {
        mIsDlnaRunning = status;
    }

    /*
     * 機能：libをロードする.
     */
    static {
        System.loadLibrary("dtvtlib");
    }

    /**
     * 機能：jni関数.
     * @return c++ dlna object
     */
    private native long nativeCreateDlnaObject();

    /**
     * 機能：jni関数.
     * @param prt prt
     * @return 操作結果
     */
    private native boolean nativeStartDlna(long prt);

    /**
     * 機能：jni関数.
     * @param prt prt
     * @return 操作結果
     */
    private native boolean nativeStopDlna(long prt);

    /**
     * 機能：jni関数.
     * @param prt prt
     * @param ctl ctl
     * @return 操作結果
     */
    private native boolean browseRecVideoDms(long prt, String ctl);

    /**
     * 機能：jni関数.
     *@param prt prt
     * @param ctl ctl
     * @return 操作結果
     */
    private native boolean browseBsChListDms(long prt, String ctl);

    /**
     * 機能：jni関数.
     * @param prt prt
     * @param ctl ctl
     * @return 操作結果
     */
    private native boolean browseTerChListDms(long prt, String ctl);

    /**
     * 機能：jni関数.
     * @param prt prt
     * @param ctl ctl
     * @return 操作結果
     */
    private native boolean browseHikariChListDms(long prt, String ctl);

    /**
     * 機能：カレントDMSを削除.
     */
    public void dmsRemove() {
        if (null != mDlnaDevListListener) {
            String udn = "";
            if (null != mCurrentDmsItem) {
                udn = mCurrentDmsItem.mUdn;
            }
            mDlnaDevListListener.onDeviceLeave(mDMSInfo, udn);
        }
        mCurrentDmsItem = null;
    }

    /**
     * todo: WiFiは切ると、対応する.
     */
    public void toDoWithWiFiLost() {

    }

    /**
     * 機能：download.
     * @param itemId itemId
     * @return xmlToDl xmlToDl
     */
    private String getDlParam(final String itemId) {
        DTVTLogger.start();
        DTVTLogger.end();
        synchronized (this) {
            return getDlParam(mNativeDlna, itemId);
        }
    }

    /**
     * getDlParam.
     * @param prt prt
     * @param itemId itemId
     * @return param
     */
    private native String getDlParam(long prt, String itemId);

    /**
     * getXmlToDl.
     * @param itemId  itemId
     * @return xmlToDl
     */
    public static String getXmlToDl(final String itemId) {
        if (null == sDlnaInterface || null == itemId || itemId.isEmpty()) {
            return null;
        }

        return sDlnaInterface.getDlParam(itemId);
    }

    /**
     * dlnaOnResume.
     * @return true or false
     */
    public static boolean dlnaOnResume() {
//        if(null==sDlnaInterface){
//            return false;
//        }
//
//        return sDlnaInterface.startDlna();
        return null != sDlnaInterface && sDlnaInterface.startDlna();
    }

    /**
     * dlnaOnStop.
     */
    public static void dlnaOnStop() {
        if (null == sDlnaInterface) {
            return;
        }

        synchronized (sDlnaInterface) {
            if (!sDlnaInterface.mIsDlnaRunning) {
                return;
            }

            sDlnaInterface.nativeStopDlna(sDlnaInterface.mNativeDlna);
            sDlnaInterface.setDlnaStatus(false);
        }
    }

    /**
     * isDlnaRunning.
     * @return true or false
     */
    public boolean isDlnaRunning() {
        if (null == sDlnaInterface) {
            return false;
        }
        synchronized (sDlnaInterface) {
            return sDlnaInterface.mIsDlnaRunning;
        }
    }
}