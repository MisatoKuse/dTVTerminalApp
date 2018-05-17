/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


import android.content.Context;
import android.os.Build;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;

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
    /**
     * エラータイプ:OVER.
     */
    private static final int LOCAL_REGISTRATION_ERROR_TYPE_OVER = 1;
    /**
     * DlnaManagerListener.
     */
    public DlnaManagerListener mDlnaManagerListener = null;
    /**
     * LocalRegisterListener.
     */
    public LocalRegisterListener mLocalRegisterListener = null;
    /**
     * Context.
     */
    private Context mContext;

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
     * RequestLocalRegistration.
     * @param udn udn
     */
    public void RequestLocalRegistration(final String udn) {
        requestLocalRegistration(udn, Build.MODEL);
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

    // call from jni

    /**
     * getUniqueId.
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
     *  callback.
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
     * DmsLeaveCallback.
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
     * RegistResultCallBack.
     * @param result result
     * @param errorType errorType
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
     * restartDirag.
     */
    private native void restartDirag();

    /**
     * requestLocalRegistration.
     * @param udn udn
     * @param deviceName deviceName
     */
    private native void requestLocalRegistration(final String udn, final String deviceName);

    /**
     * getRemoteDeviceExpireDate.
     * @param udn udn
     * @return DeviceExpireDate
     */
    private native String getRemoteDeviceExpireDate(final String udn);
    // endregion native method
}
