/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


import android.content.Context;
import android.os.Build;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.net.MalformedURLException;
import java.net.URL;

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
    public enum RemoteConnectStatus {
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
         * @param connectStatus 接続状態
         */
        void onRemoteConnectStatusCallBack(RemoteConnectStatus connectStatus);
    }
    // endregion Listener declaration

    /**
     * singletone.
     */
    private static DlnaManager sInstance = new DlnaManager();

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
    /** dms検出リスナー. */
    private DlnaManagerListener mDlnaManagerListener = null;
    /** ローカルレジストレーションリスナー. */
    public LocalRegisterListener mLocalRegisterListener = null;
    /** リモート接続リスナー. */
    public RemoteConnectStatusChangeListener mRemoteConnectStatusChangeListener = null;
    /** 接続ステータス. */
    public RemoteConnectStatus remoteConnectStatus = RemoteConnectStatus.OTHER;
    /** コンテキスト. */
    public Context mContext;

    /**
     * launch.
     * @param context コンテキスト
     */
    public void launch(final Context context) {
        mContext = context;
        DTVTLogger.start();

        DlnaUtils.copyDiragConfigFile(context);

        // DTCP+ ムーブやローカル再生機能を使うためにアプリ起動時に下記初期化処理をしておく
        secureIoGlobalCreate();
        String privateDataPath = DlnaUtils.getPrivateDataHomePath(context);
        cipherFileContextGlobalCreate(privateDataPath);
        initDmp(privateDataPath);

        String diragConfigFilePath = DlnaUtils.getDiragConfileFilePath(context);
        initDirag(diragConfigFilePath);

        DTVTLogger.end();
    }

    /**
     * StartDmp.
     */
    public void StartDmp() {
        startDmp();
    }

    /**
     * StopDmp.
     */
    public void StopDmp() {
        stopDmp();
    }

    /**
     * ConnectDmsWithUdn.
     * @param udn udn
     */
    public void ConnectDmsWithUdn(final String udn) {
        connectDmsWithUdn(udn);
    }

    /**
     * BrowseContentWithContainerId.
     * @param offset offset
     * @param limit limit
     * @param containerId containerId
     */
    public void BrowseContentWithContainerId(final int offset, final int limit, final String containerId) {
        browseContentWithContainerId(offset, limit, containerId);
    }

    /**
     * StartDtcp.
     */
    public void StartDtcp() {
        startDtcp();
    }

    /**
     * RestartDirag.
     */
    public void RestartDirag() {
        restartDirag();
    }

    /**
     * Diragを停止.
     */
    private void StopDirag() {
        stopDirag();
    }

    /**
     * RequestLocalRegistration.
     * @param udn udn
     */
    public void RequestLocalRegistration(final String udn) {
        requestLocalRegistration(udn, Build.MODEL);
    }

    /**
     * リモート接続を行う.
     * @param udn udn
     */
    public void RequestRemoteConnect(final String udn) {
        requestRemoteConnect(udn);
    }

    /**
     * GetRemoteDeviceExpireDate.
     * @param udn udn
     * @return RemoteDeviceExpireDate
     */
    public String GetRemoteDeviceExpireDate(final String udn) {
        String result = getRemoteDeviceExpireDate(udn);
        return result;
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
        //TODO 宅外、宅内の実装が必要があるため、いったんコメントアウトして、次回のスプリントでやるつもりです
//        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(DlnaManager.shared().mContext);
//        if (dlnaDmsItem != null) {
//            if (dlnaDmsItem.mUdn.equals(udn)) {
//                updateConnectStatus();
//            }
//        }
        DlnaManagerListener listener = DlnaManager.shared().mDlnaManagerListener;
        if (listener != null) {
            URL hostUrl = null;
            String hostString = "";
            try {
                hostUrl = new URL(location);
                hostString = hostUrl.getHost();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            listener.joinDms(friendlyName, hostString, udn, controlUrl, eventSubscriptionUrl);
        } else {
            DTVTLogger.error("no callback");
        }
    }

    /**
     * dms消えた(call from jni).
     * @param udn  udn
     */
    public void DmsLeaveCallback(final String udn) {
        DTVTLogger.warning("udn = " + udn);
        DlnaManagerListener listener = DlnaManager.shared().mDlnaManagerListener;
        if (listener != null) {
            listener.leaveDms(udn);
        } else {
            DTVTLogger.error("no callback");
        }
    }

    /**
     * ローカルレジストレーション結果コールバック（call from jni）.
     * @param result 結果
     * @param errorType エラータイプ
     */
    public void RegistResultCallBack(final boolean result, final int errorType) {
        DTVTLogger.warning("result = " + result);
        LocalRegisterListener listener = DlnaManager.shared().mLocalRegisterListener;
        if (listener != null) {
            LocalRegistrationErrorType localRegistrationErrorType = LocalRegistrationErrorType.NONE;
            if (errorType == LOCAL_REGISTRATION_ERROR_TYPE_OVER) {
                localRegistrationErrorType = LocalRegistrationErrorType.OVER;
            }
            listener.onRegisterCallBack(result, localRegistrationErrorType);
        } else {
            DTVTLogger.error("no callback");
        }
    }

    /**
     * リモート接続コールバック.
     * @param connectStatus 接続ステータス
     */
    public void RemoteConnectStatusCallBack(final int connectStatus) {
        DTVTLogger.warning("connectStatus = " + connectStatus);
        RemoteConnectStatusChangeListener listener = DlnaManager.shared().mRemoteConnectStatusChangeListener;
        if (listener != null) {
            RemoteConnectStatus status;
            switch (connectStatus) {
                case REMOTE_CONNECT_STATUS_UNKOWN:
                default:
                    status = RemoteConnectStatus.UNKNOWN;
                    break;
                case REMOTE_CONNECT_STATUS_READY:
                    status = RemoteConnectStatus.READY;
                    break;
                case REMOTE_CONNECT_STATUS_CONNECTED:
                    status = RemoteConnectStatus.CONNECTED;
                    break;
                case REMOTE_CONNECT_STATUS_DISCONNECTION:
                    status = RemoteConnectStatus.DETECTED_DISCONNECTION;
                    break;
                case REMOTE_CONNECT_STATUS_RECONNECTION:
                    status = RemoteConnectStatus.GAVEUP_RECONNECTION;
                    break;
            }
            DlnaManager.shared().remoteConnectStatus = status;
            listener.onRemoteConnectStatusCallBack(status);
        } else {
            DTVTLogger.error("no callback");
        }
    }

    // endregion call from jni

    /**
     * リモート接続のリセット.
     */
    private void updateConnectStatus() {
        StopDirag();
        DlnaManager.shared().remoteConnectStatus = RemoteConnectStatus.OTHER;
    }

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
     * connectDmsWithUdn.
     * @param udn udn
     */
    private native void connectDmsWithUdn(String udn);

    /**
     * browseContentWithContainerId.
     * @param offset offset
     * @param limit limit
     * @param containerId containerId
     */
    private native void browseContentWithContainerId(final int offset, final int limit, final String containerId);

    /**
     * startDtcp.
     */
    private native void startDtcp();

    /**
     * Diragを再起動.
     */
    private native void restartDirag();

    /**
     * Diragを停止.
     */
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
     * ローカルレジストレーションの開始時間取得.
     * @param udn udn
     * @return DeviceExpireDate
     */
    private native String getRemoteDeviceExpireDate(final String udn);
    // endregion native method
}
