/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.service.download.DtcpDownloadParam;
import com.nttdocomo.android.tvterminalapp.utils.AribUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * DLNAマネージャー.
 */
public class DlnaManager {
    /**エラータイプ.*/
    public enum LocalRegistrationErrorType {
        /**NONE.*/
        NONE,
        /**OVER.*/
        OVER,
        /**UNKNOWN.*/
        UNKNOWN
    }

    /**リモート接続ステータス.*/
    private enum RemoteConnectStatus {
        /** 不明. */
        UNKNOWN,
        /** 接続準備環境. */
        READY,
        /** 接続中. */
        CONNECTED,
        /** 切断. */
        DETECTED_DISCONNECTION,
        /** 再接続失敗. */
        GAVEUP_RECONNECTION,
        /** 其の他. */
        OTHER
    }

    /**ダウンロードステータ.*/
    public enum DownLoadStatus {
        /**UNKNOWN.*/
        DOWNLOADER_STATUS_UNKNOWN,
        /**MOVING.*/
        DOWNLOADER_STATUS_MOVING,
        /**COMPLETED.*/
        DOWNLOADER_STATUS_COMPLETED,
        /**CANCELLED.*/
        DOWNLOADER_STATUS_CANCELLED,
        /**ERROR_OCCURED.*/
        DOWNLOADER_STATUS_ERROR_OCCURED
    }

    // region Listener declaration

    /**
     * DlnaManagerListener.
     */
    public interface DlnaManagerListener {
        /**
         * joinDms.
         * @param name name
         * @param host host
         * @param udn udn
         * @param controlUrl controlUrl
         * @param eventSubscriptionUrl eventSubscriptionUrl
         */
        void joinDms(final String name, final String host, final String udn, final String controlUrl,
                     final String eventSubscriptionUrl);

        /**
         * leaveDms.
         * @param udn udn
         */
        void leaveDms(String udn);
    }

    /**
     * LocalRegisterListener.
     */
    public interface LocalRegisterListener {
        /**
         * onRegisterCallBack.
         * @param result result
         * @param errorType errorType
         */
        void onRegisterCallBack(final boolean result, final LocalRegistrationErrorType errorType);
    }

    /**
     * リモート接続リスナー.
     */
    public interface RemoteConnectStatusChangeListener {
        /**
         * リモート接続コールバック.
         * @param errorCode エラーコード
         */
        void onRemoteConnectStatusCallBack(int errorCode);
    }

    /**
     * リモート接続リスナー.
     */
    public interface DownloadStatusListener {
        /**
         * ダウンロード接続コールバック.
         * @param status ダウンロードステータ
         */
        void onDownloadStatusCallBack(DownLoadStatus status);
        /**
         * ダウンロード進捗コールバック.
         * @param progress ダウンロード進捗
         */
        void onDownloadProgressCallBack(int progress);
    }
    // endregion Listener declaration
    /**
     * コンテンツブラウズリスナー.
     */
    public interface BrowseListener {
        /**
         * コンテンツブラウズコールバック.
         * @param objs コンテンツリスト
         */
        void onContentBrowseCallback(final DlnaObject[] objs);
    }
    /**
     * singletone.
     */
    private static final DlnaManager sInstance = new DlnaManager();

    /**
     * DlnaManager.
     */
    private DlnaManager() {
    }

    /**
     * shared.
     * @return Instance
     */
    public static DlnaManager shared() {
        return sInstance;
    }

    static {
        System.loadLibrary("dtvtlib");
    }

    /**　エラータイプ:OVER.*/
    private static final int LOCAL_REGISTRATION_ERROR_TYPE_OVER = 1;
    /** 不明. */
    private static final int REMOTE_CONNECT_STATUS_UNKOWN = 2;
    /** 接続準備環境. */
    private static final int REMOTE_CONNECT_STATUS_READY = 3;
    /** 接続中. */
    private static final int REMOTE_CONNECT_STATUS_CONNECTED = 4;
    /** 切断. */
    private static final int REMOTE_CONNECT_STATUS_DISCONNECTION = 5;
    /** 再接続失敗. */
    private static final int REMOTE_CONNECT_STATUS_RECONNECTION = 6;
    /** UNKNOWN. */
    private static final int DOWNLOADER_STATUS_UNKNOWN = 11;
    /** MOVING. */
    private static final int DOWNLOADER_STATUS_MOVING = 12;
    /** COMPLETED. */
    private static final int DOWNLOADER_STATUS_COMPLETED = 13;
    /** CANCELLED. */
    private static final int DOWNLOADER_STATUS_CANCELLED = 14;
    /** ERROR_OCCURED. */
    private static final int DOWNLOADER_STATUS_ERROR_OCCURED = 15;
    /** dms検出リスナー. */
    public DlnaManagerListener mDlnaManagerListener = null;

    /** ローカルレジストレーションリスナー. */
    public LocalRegisterListener mLocalRegisterListener = null;
    /** リモート接続リスナー. */
    public RemoteConnectStatusChangeListener mRemoteConnectStatusChangeListener = null;
    /** コンテンツブラウズリスナー. */
    public BrowseListener mBrowseListener = null;
    /** ダウンロードリスナー. */
    public DownloadStatusListener mDownloadStatusListener = null;
    /** 接続ステータス. */
    private RemoteConnectStatus remoteConnectStatus = RemoteConnectStatus.OTHER;
    /** コンテキスト. */
    public Context mContext;
    /** 接続スタートフラグ. */
    public boolean isStarted = false;
    /** udn. */
    private String mUdn = "";
    /** 接続（ready）フラグ. */
    private boolean waitForReady = false;
    /** ブラウズパス指定. */
    private String requestContainerId = "";
    /** ページングインデックス. */
    private int mPageIndex = 0;
    /** dmp開始フラグ. */
    private boolean startedDmp = false;
    /** dtcp開始フラグ. */
    private boolean startedDtcp = false;
    /** dtcp開始フラグ. */
    private String mHomeOutControlUrl = null;
    /** ARIB外字変換クラス. */
    private AribUtils mAribUtils = null;

    /**
     * launch.
     * @param context コンテキスト
     */
    public void launch(final Context context) {
        DlnaManager.shared().isStarted = false;
        DlnaManager.shared().startedDmp = false;
        mContext = context;
        DTVTLogger.start();

        DlnaUtils.copyDiragConfigFile(context);

        // DTCP+ ムーブやローカル再生機能を使うためにアプリ起動時に下記初期化処理をしておく
        secureIoGlobalCreate();
        String privateDataPath = DlnaUtils.getPrivateDataHomePath(context);
        cipherFileContextGlobalCreate(privateDataPath);
        // initDmp(privateDataPath);
        String diragConfigFilePath = DlnaUtils.getDiragConfileFilePath(context);
        initDirag(diragConfigFilePath);

        //ARIB外字変換クラスの宣言
        mAribUtils = new AribUtils();

        DTVTLogger.end();
    }

    /**
     * NW変更時などにコールする.
     * @param context コンテキスト
     */
    public void Start(final Context context) {
        DlnaManager.shared().mContext = context;
        DTVTLogger.warning("isStarted = " + isStarted);
        //if start then stop
        if (!DlnaManager.shared().isStarted) {
            DlnaManager.shared().isStarted = true;
            StbConnectionManager.ConnectionStatus connectionStatus = StbConnectionManager.shared().getConnectionStatus();
            DTVTLogger.warning("connectionStatus = " + connectionStatus);
            switch (connectionStatus) {
                case HOME_OUT:
                    DlnaDmsItem item = SharedPreferencesUtils.getSharedPreferencesStbInfo(DlnaManager.shared().mContext);
                    mUdn = item.mUdn;
                    //1.dアカウントチェックしたかを確認してなかったらチェック
                    DlnaManager.shared().StartDmp();
                    DlnaManager.shared().StartDtcp();
                    DlnaManager.shared().RestartDirag();
                    break;
                case HOME_IN:
                    DlnaManager.shared().stopDirag();
                    break;
                case NONE_PAIRING:
                    DlnaManager.shared().StopDmp();
                    break;
                default:
                    DlnaManager.shared().StartDmp();
                    break;
            }
        }
    }

    /**
     * StartDmp.
     */
    public void StartDmp() {
        if (!DlnaManager.shared().startedDmp) {
            String privateDataPath = DlnaUtils.getPrivateDataHomePath(DlnaManager.shared().mContext);
            DTVTLogger.warning("native call >>>> privateDataPath" + privateDataPath);
            initDmp(privateDataPath);
            startDmp();
            DTVTLogger.warning("native call >>>> StartDmp");
            DlnaManager.shared().startedDmp = true;
        }
    }

    /**
     * StopDmp.
     */
    public void StopDmp() {
        if (DlnaManager.shared().startedDmp) {
            stopDmp();
            freeDmp();
            DTVTLogger.warning("native call >>>> StopDmp");
            DlnaManager.shared().startedDmp = false;
        }
    }

    /**
     * BrowseContentWithContainerId.
     * @param containerId containerId
     * @param pageIndex ページングインデックス
     */
    public void BrowseContentWithContainerId(final String containerId, final int pageIndex) {
        DTVTLogger.warning("StbConnectionManager.shared().getConnectionStatus() = " + StbConnectionManager.shared().getConnectionStatus());
        switch (StbConnectionManager.shared().getConnectionStatus()) {
            case HOME_OUT:
                DTVTLogger.warning("remoteConnectStatus = " + DlnaManager.shared().remoteConnectStatus);
                if (TextUtils.isEmpty(DlnaManager.shared().mUdn)) {
                    DlnaDmsItem item = SharedPreferencesUtils.getSharedPreferencesStbInfo(DlnaManager.shared().mContext);
                    DlnaManager.shared().mUdn = item.mUdn;
                }
                if (DlnaManager.shared().remoteConnectStatus == RemoteConnectStatus.READY) {
                    requestRemoteConnect(DlnaManager.shared().mUdn);
                    DlnaManager.shared().requestContainerId = containerId;
                    DlnaManager.shared().mPageIndex = pageIndex;
                } else {
                    DlnaManager.shared().waitForReady = true;
                    DlnaManager.shared().requestContainerId = containerId;
                    DlnaManager.shared().mPageIndex = pageIndex;
                    StartDtcp();
                    RestartDirag();
                }

                break;
            case HOME_IN:
                DlnaDmsItem item = SharedPreferencesUtils.getSharedPreferencesStbInfo(DlnaManager.shared().mContext);
                browseContentWithContainerId(pageIndex * DtvtConstants.REQUEST_DLNA_LIMIT_50, DtvtConstants.REQUEST_DLNA_LIMIT_50, containerId, item.mControlUrl);
                break;
            case HOME_OUT_CONNECT:
                browseContentWithContainerId(pageIndex * DtvtConstants.REQUEST_DLNA_LIMIT_50, DtvtConstants.REQUEST_DLNA_LIMIT_50, containerId, DlnaManager.shared().mHomeOutControlUrl);
                break;
            default:
                DTVTLogger.warning("default");
                break;
        }
        DTVTLogger.warning("containerId = " + containerId);

    }
    /**
     * StartDtcp.
     */
    public void StartDtcp() {
        if (!startedDtcp) {
            startedDtcp = true;
            startDtcp();
        }
    }

    /**
     * RestartDirag.
     */
    public void RestartDirag() {
        restartDirag();
    }

    /**
     * Diragを開始.
     */
    public void StartDirag() {
        startDirag();
    }
    /**
     * Diragを停止.
     */
    private void StopDirag() {
        stopDirag();
    }

    /**
     * Dtcpを停止.
     */
    public void StopDtcp() {
        if (startedDtcp) {
            startedDtcp = false;
            stopDtcp();
        }

    }

    /**
     * ローカルレジストレーションの依頼を行う.
     *
     * @param udn udn
     * @param context コンテキスト
     */
    public void RequestLocalRegistration(final String udn, final Context context) {
        //現在の年月日の文字列を取得
        String nowDate = DateUtils.formatEpochToString(DateUtils.getNowTimeFormatEpoch()
                , DateUtils.DATE_YYYY_MM_DD_J);

        //登録名にはOSバージョンや年月日も含めた完成状態で送り出す。（代わりにC言語側では無編集に変更)
        String storeName = StringUtils.getConnectStrings(Build.MODEL," "
                , context.getString(R.string.str_stb_registration_device_name_os)
                , Build.VERSION.RELEASE
                , " ", context.getString(R.string.home_contents_slash), " ", nowDate
                , context.getString(R.string.str_stb_registration_device_name_regist));

        //ローカルレジストレーションの呼び出し
        requestLocalRegistration(udn, storeName);
    }

    /**
     * リモート接続を行う.
     * @param udn udn
     */
    private void RequestRemoteConnect(final String udn) {
        requestRemoteConnect(udn);
    }

    /**
     * GetRemoteDeviceExpireDate.
     * @param udn udn
     * @return RemoteDeviceExpireDate
     */
    public String GetRemoteDeviceExpireDate(final String udn) {
        return getRemoteDeviceExpireDate(udn);
    }

    /**
     * ダウンロードdtcp起動.
     */
    public boolean initDownload() {
        return downLoadStartDtcp();
    }

    /**
     * ダウンロードをキャンセル.
     */
    public void DownloadCancel() {
        downloadCancel();
    }

    /**
     * ダウンロードをストップ.
     */
    public void DownloadStop() {
        downloadStop();
    }

    /**
     * ダウンロード.
     * @param param ダウンロードパラメータ
     */
    public void downloadStart(final DtcpDownloadParam param) {
        String path = param.getSavePath() + File.separator + param.getSaveFileName();
        download(path, param.getDtcp1host(), param.getDtcp1port(),
                param.getUrl(), param.getCleartextSize(), param.getXmlToDownLoad());
    }

    // region call from jni
    /**
     * getUniqueId(call from jni).
     * @return uniqueId
     */
    public String getUniqueId() {
        if (null == mContext) {
            DTVTLogger.warning("mContext is null!");
            return "";
        }
        String uniqueId = EnvironmentUtil.getCalculatedUniqueId(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        DTVTLogger.warning("uniqueId = " + uniqueId);
        return uniqueId;
    }

    /**
     *  dms検出(call from jni).
     * @param friendlyName friendlyName
     * @param udn udn
     * @param location location
     * @param controlUrl controlUrl
     * @param eventSubscriptionUrl eventSubscriptionUrl
     */
    public void DmsFoundCallback(final String friendlyName, final  String udn, final String location,
                                 final String controlUrl, final String eventSubscriptionUrl) {
        DTVTLogger.warning("friendlyName = " + friendlyName + ", udn = " + udn + ", location = " + location
                + ", controlUrl = " + controlUrl + ", eventSubscriptionUrl = " + eventSubscriptionUrl);

        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(DlnaManager.shared().mContext);
        updateJoinConnectStatus(dlnaDmsItem, friendlyName, udn, location, controlUrl);
        DlnaManagerListener listener = DlnaManager.shared().mDlnaManagerListener;
        if (listener != null) {
            listener.joinDms(friendlyName, DlnaUtils.getHost(location), udn, controlUrl, eventSubscriptionUrl);
        } else {
            DTVTLogger.warning("no dms callback");
        }
    }

    /**
     * dms消えた(call from jni).
     * @param udn  udn
     */
    public void DmsLeaveCallback(final String udn) {
        DTVTLogger.warning("udn = " + udn);
        DlnaManagerListener listener = DlnaManager.shared().mDlnaManagerListener;

        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(DlnaManager.shared().mContext);
        updateLeaveConnectStatus(dlnaDmsItem, udn);

        if (listener != null) {
            listener.leaveDms(udn);
        } else {
            DTVTLogger.error("no callback");
        }
    }

    /**
     *  コンテンツブラウズコールバック.
     * @param containerId コンテンツリスト
     * @param objs コンテンツリスト
     */
    public void ContentBrowseCallback(@NonNull final String containerId, @NonNull final DlnaObject[] objs) {
        DTVTLogger.warning("containerId = " + containerId + ", objs.length = " + objs.length);
        BrowseListener listener = DlnaManager.shared().mBrowseListener;
        if (listener != null) {
            aribConvertBs(objs);
            listener.onContentBrowseCallback(objs);
        }
    }

    /**
     * ローカルレジストレーション結果コールバック（call from jni）.
     * @param result 結果
     * @param errorType エラータイプ
     */
    public void RegistResultCallBack(final boolean result, final int errorType) {
        DTVTLogger.warning("result = " + result);

        if (result) {
            //ローカルレジストレーションが成功したので日時を蓄積する
            SharedPreferencesUtils.setRegistTime(DlnaManager.shared().mContext);
        }

        LocalRegisterListener listener = DlnaManager.shared().mLocalRegisterListener;
        if (listener != null) {
            LocalRegistrationErrorType localRegistrationErrorType = LocalRegistrationErrorType.NONE;
            if (errorType == LOCAL_REGISTRATION_ERROR_TYPE_OVER) {
                localRegistrationErrorType = LocalRegistrationErrorType.OVER;
            }
            if (result) {
                //リモート視聴設定期限ダイアログ表示フラグをリセットする
                SharedPreferencesUtils.setRegisterExpiredateDialogFlg(DlnaManager.shared().mContext, 0);
            }
            listener.onRegisterCallBack(result, localRegistrationErrorType);
        } else {
            DTVTLogger.error("no callback");
        }
    }

    /**
     * リモート接続コールバック.
     * @param connectStatus 接続ステータス
     * @param errorCode エラーコード
     */
    public void RemoteConnectStatusCallBack(final int connectStatus, final int errorCode) {
        DTVTLogger.warning("connectStatus = " + connectStatus);
        DlnaManager manager = DlnaManager.shared();
        RemoteConnectStatusChangeListener listener = manager.mRemoteConnectStatusChangeListener;
        RemoteConnectStatus status;
        switch (connectStatus) {
            case REMOTE_CONNECT_STATUS_UNKOWN:
            default:
                status = RemoteConnectStatus.UNKNOWN;
                break;
            case REMOTE_CONNECT_STATUS_READY:
                status = RemoteConnectStatus.READY;
                if (manager.waitForReady) {
                    manager.waitForReady = false;
                    manager.RequestRemoteConnect(manager.mUdn);
                }
                break;
            case REMOTE_CONNECT_STATUS_CONNECTED:
                status = RemoteConnectStatus.CONNECTED;
                break;
            case REMOTE_CONNECT_STATUS_DISCONNECTION:
                status = RemoteConnectStatus.DETECTED_DISCONNECTION;
                break;
            case REMOTE_CONNECT_STATUS_RECONNECTION:
                status = RemoteConnectStatus.GAVEUP_RECONNECTION;
                if (listener != null && StbConnectionManager.shared().getConnectionStatus() != StbConnectionManager.ConnectionStatus.HOME_IN) {
                    listener.onRemoteConnectStatusCallBack(errorCode);
                }
                break;
        }
        manager.remoteConnectStatus = status;
        DTVTLogger.warning("status = " + status);
    }

    /**
     * ダウンロードコールバック.
     * @param downloadStatus ダウンロードステータス
     */
    public void DownloadStatusCallBack(final int downloadStatus) {
        DownLoadStatus downLoadStatus = null;
        switch (downloadStatus) {
            case DOWNLOADER_STATUS_UNKNOWN:
                downLoadStatus = DownLoadStatus.DOWNLOADER_STATUS_UNKNOWN;
                break;
            case DOWNLOADER_STATUS_MOVING:
                downLoadStatus = DownLoadStatus.DOWNLOADER_STATUS_MOVING;
                break;
            case DOWNLOADER_STATUS_COMPLETED:
                downLoadStatus = DownLoadStatus.DOWNLOADER_STATUS_COMPLETED;
                break;
            case DOWNLOADER_STATUS_CANCELLED:
                downLoadStatus = DownLoadStatus.DOWNLOADER_STATUS_CANCELLED;
                break;
            case DOWNLOADER_STATUS_ERROR_OCCURED:
                downLoadStatus = DownLoadStatus.DOWNLOADER_STATUS_ERROR_OCCURED;
                break;
            default:
                break;
        }
        DownloadStatusListener listener = DlnaManager.shared().mDownloadStatusListener;
        if (listener != null) {
            listener.onDownloadStatusCallBack(downLoadStatus);
        }
    }

    /**
     * ダウンロードコールバック.
     * @param progress ダウンロード進捗
     */
    public void DownloadProgressCallBack(final int progress) {
        DownloadStatusListener listener = DlnaManager.shared().mDownloadStatusListener;
        if (listener != null) {
            listener.onDownloadProgressCallBack(progress);
        }
    }
    // endregion call from jni

    // region priavte method
    /**
     * ARIB変換.
     * @param info info
     */
    private void aribConvertBs(@NonNull final DlnaObject[] info) {

        if (null == DlnaManager.shared().mAribUtils) {
            DTVTLogger.debug("get AribUtils instance failed");
            return;
        }

        for (DlnaObject obj: info) {
            obj.mChannelName = DlnaManager.shared().mAribUtils.convertAribGaiji(
                    obj.mChannelName);
            try {
                Integer.parseInt(obj.mSize);
            } catch (NumberFormatException e) {
                obj.mSize = "0";
            }
        }
    }

    private void updateJoinConnectStatus(@Nullable final DlnaDmsItem item, final @NonNull String friendName, final @NonNull String udn, final @NonNull String location,
                                         final @NonNull String controlUrl) {
        if (item == null || !udn.equals(item.mUdn)) {
            return;
        }
        DTVTLogger.error(">>> STB接続");
        connectDmsWithUdn(udn);

        String localHostAddress = null;
        try {
            localHostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            DTVTLogger.debug(e);
        }
        URL locationHostUrl;
        String locationHostString = "";
        try {
            locationHostUrl = new URL(location);
            locationHostString = locationHostUrl.getHost();
        } catch (MalformedURLException e) {
            DTVTLogger.debug(e);
        }
        if (!TextUtils.isEmpty(localHostAddress) && localHostAddress.equals(locationHostString)) {
            StbConnectionManager.shared().setConnectionStatus(StbConnectionManager.ConnectionStatus.HOME_OUT_CONNECT);
            DlnaManager manager = DlnaManager.shared();
            manager.mHomeOutControlUrl = controlUrl;
            DTVTLogger.warning("requestContainerId = " + manager.requestContainerId);
            DTVTLogger.warning("pageIndex = " + manager.mPageIndex);
            if (!TextUtils.isEmpty(manager.requestContainerId)) {
                manager.BrowseContentWithContainerId(manager.requestContainerId, manager.mPageIndex);
                manager.requestContainerId = "";
                manager.mPageIndex = 0;
            }

        } else {
            if (StbConnectionManager.shared().getConnectionStatus() == StbConnectionManager.ConnectionStatus.HOME_OUT_CONNECT) {
                DlnaManager.shared().StopDirag();
            }
            StbConnectionManager.shared().setConnectionStatus(StbConnectionManager.ConnectionStatus.HOME_IN);
            String host = DlnaUtils.getHost(location);
            if (!item.mIPAddress.equals(host)) {
                DlnaDmsItem newItem = new DlnaDmsItem();
                newItem.mFriendlyName = friendName;
                newItem.mIPAddress = host;
                newItem.mUdn = udn;
                newItem.mControlUrl = controlUrl;
                newItem.mHttp = host;
                SharedPreferencesUtils.setSharedPreferencesStbInfo(DlnaManager.shared().mContext, newItem);
            }
        }
    }

    private void updateLeaveConnectStatus(@Nullable DlnaDmsItem item, final @NonNull String udn) {
        if (item == null || !udn.equals(item.mUdn)) {
            return;
        }
        DTVTLogger.warning("STB切断 <<<");
        switch (StbConnectionManager.shared().getConnectionStatus()) {
            case HOME_IN:
                StbConnectionManager.shared().initializeState();
                break;
            case HOME_OUT_CONNECT:
                StbConnectionManager.shared().setConnectionStatus(StbConnectionManager.ConnectionStatus.HOME_OUT);
                break;
        }
    }

    // endregion priavte method

    // region native method

    /**
     * secureIoGlobalCreate.
     */
    private native void secureIoGlobalCreate();

    /**
     * cipherFileContextGlobalCreate.
     * @param privateDataPath privateDataPath
     */
    private native void cipherFileContextGlobalCreate(String privateDataPath);

    /**
     * initDmp.
     * @param configFilePath configFilePath
     */
    private native void initDmp(String configFilePath);

    /**
     * initDirag.
     * @param configFilePath configFilePath
     */
    private native void initDirag(String configFilePath);

    /**
     * startDmp.
     */
    private native void startDmp();

    /**
     * stopDmp.
     */
    private native void stopDmp();

    /**
     * freeDmp.
     */
    private native void freeDmp();

    /**
     * connectDmsWithUdn.
     * @param udn udn
     */
    private native void connectDmsWithUdn(String udn);

    /**
     * browseContentWithContainerId.
     * @param offset offset
     * @param limit limit
     * @param containerId containerId
     * @param controlUrl controlUrl
     */
    private native void browseContentWithContainerId(final int offset, final int limit, final String containerId, final String controlUrl);
    /** dtcpを開始.*/
    private native void startDtcp();
    /** dtcpを停止.*/
    private native void stopDtcp();
    /** Diragを再起動.*/
    private native void restartDirag();
    /** Diragを起動.*/
    private native void startDirag();
    /** Diragを停止.*/
    private native void stopDirag();
    /**
     * requestLocalRegistration.
     * @param udn udn
     * @param deviceName deviceName
     */
    private native void requestLocalRegistration(String udn, String deviceName);

    /**
     * リモート接続処理を行う.
     * @param udn udn
     */
    private native void requestRemoteConnect(String udn);
    /**
     * ローカルレジストレーションの有効期限日.
     *
     * @param udn udn
     * @return DeviceExpireDate 有効期限日
     */
    private native String getRemoteDeviceExpireDate(final String udn);
    /** ダウンロード開始前のddtcpスタート.*/
    private native boolean downLoadStartDtcp();
    /** ダウンロードをキャンセル.*/
    private native boolean downloadCancel();
    /** ダウンロードをキャンセル.*/
    private native boolean downloadStop();
    /**
     * ダウンロード.
     * @param savePath 保存先
     * @param dtcp1host host
     * @param dtcp1port port
     * @param url url
     * @param cleartextSize サイズ
     * @param itemId itemId
     */
    private native void download(String savePath, String dtcp1host, int dtcp1port, String url, int cleartextSize, String itemId);

    // endregion native method
}

