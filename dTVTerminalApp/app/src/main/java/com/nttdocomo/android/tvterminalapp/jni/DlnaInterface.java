/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;

import com.digion.dixim.android.util.AribExternalCharConverter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsInfo;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaHikariChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaHikariChListItem;
import com.nttdocomo.android.tvterminalapp.jni.hikari.DlnaHikariChListListener;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.rec.DlnaRecVideoListener;

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
    /**ひかりチャンネルリスナー.*/
    private DlnaHikariChListListener mDlnaHikariChListListener;
    /**Dlna稼働フラグ.*/
    private boolean mIsDlnaRunning = false;

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
     * @param imageQuality 画質設定
     * @return 成功:true 失敗: false
     */
    public boolean browseRecVideoDms(final int imageQuality) {
        DTVTLogger.start();
        if (null == mCurrentDmsItem || null == mCurrentDmsItem.mControlUrl || 1 > mCurrentDmsItem.mControlUrl.length()) {
            return false;
        }
        synchronized (this) {
            boolean ret = browseRecVideoDms(mNativeDlna, mCurrentDmsItem.mControlUrl, imageQuality);
            DTVTLogger.debug("call c++ browseRecVideoDms");
            DTVTLogger.end();
            return ret;
        }
    }

    /**
     * 機能：BSデジタルに関して、チャンネルリストを発見.
     * @param imageQuality 画質設定
     * @return 成功:true 失敗: false
     */
    public boolean browseBsChListDms(final int imageQuality) {
        //boolean ret= browseBsChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl);
        if (null == mCurrentDmsItem || null == mCurrentDmsItem.mControlUrl || 1 > mCurrentDmsItem.mControlUrl.length()) {
            return false;
        }
        synchronized (this) {
            boolean ret = browseBsChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl, imageQuality);
            DTVTLogger.debug("call c++ browseBsChListDms");
            DTVTLogger.end();
            return ret;
        }
    }
    /**
     * 機能：多チャンネルに関して、チャンネルリストを発見.
     * @param imageQuality 画質設定
     * @return 成功:true 失敗: false
     */
    public boolean browseHikariChListDms(final int imageQuality) {
        if (null == mCurrentDmsItem || null == mCurrentDmsItem.mControlUrl || 1 > mCurrentDmsItem.mControlUrl.length()) {
            return false;
        }
        synchronized (this) {
            boolean ret = browseHikariChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl, imageQuality);
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
//            onDeviceJoin(dmss);
        }
    }

    /**
     * 機能：地上波に関して、チャンネルリストを発見.
     * @param imageQuality 画質設定
     * @return 成功:true 失敗: false
     */
    public boolean browseTerChListDms(final int imageQuality) {
        //return browseTerChListDms(mNativeDlna, ctl);
        if (null == mCurrentDmsItem || null == mCurrentDmsItem.mControlUrl || 1 > mCurrentDmsItem.mControlUrl.length()) {
            return false;
        }
        synchronized (this) {
            boolean ret = browseTerChListDms(mNativeDlna, mCurrentDmsItem.mControlUrl, imageQuality);
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
//                removeDms(content);
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
//                onDeviceJoin(content);
                break;
            case DLNA_MSG_ID_BS_CHANNEL_LIST:
//                onBsChList(content);
                notifyDeviceJoinInv(content, mCurrentDmsItem);
                break;
            case DLNA_MSG_ID_TER_CHANNEL_LIST:
//                onTerChList(content);
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

                break;
            case DLNA_MSG_ID_BS_CHANNEL_LIST:

                break;
            case DLNA_MSG_ID_TER_CHANNEL_LIST:

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
                item.mTitle = AribExternalCharConverter.getConverted(item.mTitle);
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
     * @param imageQuality 画質設定
     * @return 操作結果
     */
    private native boolean browseRecVideoDms(long prt, String ctl, int imageQuality);

    /**
     * 機能：jni関数.
     *@param prt prt
     * @param ctl ctl
     * @param imageQuality 画質設定
     * @return 操作結果
     */
    private native boolean browseBsChListDms(long prt, String ctl, int imageQuality);

    /**
     * 機能：jni関数.
     * @param prt prt
     * @param ctl ctl
     * @param imageQuality 画質設定
     * @return 操作結果
     */
    private native boolean browseTerChListDms(long prt, String ctl, int imageQuality);

    /**
     * 機能：jni関数.
     * @param prt prt
     * @param ctl ctl
     * @param imageQuality 画質設定
     * @return 操作結果
     */
    private native boolean browseHikariChListDms(long prt, String ctl, int imageQuality);

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