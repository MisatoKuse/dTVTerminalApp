/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


import android.content.Context;

import com.digion.dixim.android.util.EnvironmentUtil;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class DlnaManager {
    // region Listener declaration
    public interface DlnaManagerListener {
        void joinDms(String name, String host, String udn, String controlUrl, String eventSubscriptionUrl);
        void leaveDms(String udn);
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

    public DlnaManagerListener dlnaManagerListener = null;
    private Context mContext;

    public void launch(Context context) {
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
    public void RequestLocalRegistration(String udn) {
        requestLocalRegistration(udn);
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
        DlnaManagerListener listener = DlnaManager.shared().dlnaManagerListener;
        if (listener != null) {
            URL hostUrl = null;
            String hostString = "";
            try {
                hostUrl = new URL(location);
                hostString = hostUrl.getHost();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            DTVTLogger.warning("callback");
            listener.joinDms(friendlyName, hostString, udn, controlUrl, eventSubscriptionUrl);
        } else {
            DTVTLogger.error("no callback");
        }
    }

    public void DmsLeaveCallback(String udn) {
        DTVTLogger.warning("udn = " + udn);
        DlnaManagerListener listener = DlnaManager.shared().dlnaManagerListener;
        if (listener != null) {
            DTVTLogger.warning("callback");
            listener.leaveDms(udn);
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
    private native void requestLocalRegistration(String udn);
    // endregion native method

}
