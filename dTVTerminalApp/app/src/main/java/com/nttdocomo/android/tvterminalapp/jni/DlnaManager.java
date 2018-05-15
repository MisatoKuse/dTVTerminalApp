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

public class DlnaManager {
    public enum LocalRegistrationErrorType {
        NONE,
        OVER,
        UNKNOWN
    }

    // region Listener declaration
    public interface DlnaManagerListener {
        void joinDms(String name, String host, String udn, String controlUrl, String eventSubscriptionUrl);
        void leaveDms(String udn);
    }
    public interface LocalRegisterListener {
        void onRegisterCallBack(boolean result, LocalRegistrationErrorType errorType);
    }
    // endregion Listener declaration

    // singletone
    private static DlnaManager sInstance = new DlnaManager();

    private DlnaManager() {
    }

    public static DlnaManager shared() {
        return sInstance;
    }

    static {
        System.loadLibrary("dtvtlib");
    }

    private static final int LOCAL_REGISTRATION_ERROR_TYPE_OVER = 1;

    public DlnaManagerListener mDlnaManagerListener = null;
    public LocalRegisterListener mLocalRegisterListener = null;
    private Context mContext;

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

    public void StartDmp() {
        startDmp();
    }

    public void StopDmp() {
        stopDmp();
    }

    public void ConnectDmsWithUdn(String udn) {
        connectDmsWithUdn(udn);
    }

    public void BrowseContentWithContainerId(int offset, int limit, String containerId) {
        browseContentWithContainerId(offset, limit, containerId);
    }

    public void StartDtcp() {
        startDtcp();
    }

    public void RestartDirag() {
        restartDirag();
    }

    public void RequestLocalRegistration(final String udn) {
        requestLocalRegistration(udn, Build.MODEL);
    }

    public String GetRemoteDeviceExpireDate(String udn) {
        String result = getRemoteDeviceExpireDate(udn);
        return result;
    }

    // call from jni
    public String getUniqueId() {
        if (null == mContext) {
            DTVTLogger.warning("mContext is null!");
            return "";
        }
        String uniqueId = EnvironmentUtil.getCalculatedUniqueId(mContext, EnvironmentUtil.ACTIVATE_DATA_HOME.DMP);
        DTVTLogger.warning("uniqueId = " + uniqueId);
        return uniqueId;
    }

    // callback
    public void DmsFoundCallback(String friendlyName, String udn, String location, String controlUrl, String eventSubscriptionUrl) {
        DTVTLogger.warning("friendlyName = " + friendlyName + ", udn = " + udn + ", location = " + location + ", controlUrl = " + controlUrl + ", eventSubscriptionUrl = " + eventSubscriptionUrl);
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

    public void DmsLeaveCallback(String udn) {
        DTVTLogger.warning("udn = " + udn);
        DlnaManagerListener listener = DlnaManager.shared().mDlnaManagerListener;
        if (listener != null) {
            listener.leaveDms(udn);
        } else {
            DTVTLogger.error("no callback");
        }
    }

    public void RegistResultCallBack(boolean result, int errorType) {
        DTVTLogger.warning("result = " + result);
        LocalRegisterListener listener = DlnaManager.shared().mLocalRegisterListener;
        if (listener != null) {
            LocalRegistrationErrorType localRegistrationErrorType = LocalRegistrationErrorType.NONE;
            if (errorType == LOCAL_REGISTRATION_ERROR_TYPE_OVER){
                localRegistrationErrorType = LocalRegistrationErrorType.OVER;
            }
            listener.onRegisterCallBack(result, localRegistrationErrorType);
        } else {
            DTVTLogger.error("no callback");
        }
    }

    // region native method
    private native void secureIoGlobalCreate();
    private native void cipherFileContextGlobalCreate(String privateDataPath);
    private native void initDmp(String configFilePath);
    private native void initDirag(String configFilePath);

    private native void startDmp();
    private native void stopDmp();

    private native void connectDmsWithUdn(String udn);
    private native void browseContentWithContainerId(int offset, int limit, String containerId);

    private native void startDtcp();
    private native void restartDirag();
    private native void requestLocalRegistration(String udn, String deviceName);
    private native String getRemoteDeviceExpireDate(String udn);
    // endregion native method

}
