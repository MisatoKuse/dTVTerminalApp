/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：DlnaからActivityに録画一覧を提供するクラス.
 */
public class DlnaProvRecVideo {

    /**
     * 機能：DlnaProvRecVideoを構造.
     */
    public DlnaProvRecVideo() {

    }

    /**
     * 機能：Listenを停止.
     */
    public void stopListen() {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return;
        }
        di.setDlnaRecVideoBaseListener(null);
    }

    /**
     * 機能：DMSデバイスを取り始める.
     * @param lis listener
     * @param item item
     * @return 成功true
     */
    public boolean start(final DlnaDmsItem item, final DlnaRecVideoListener lis) {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return false;
        }
        if (!isDlnaProRecVideoAvailable()) {
            return false;
        }
        boolean ret = di.registerCurrentDms(item);
        if (!ret) {
            return false;
        }
        di.setDlnaRecVideoBaseListener(lis);
        return true;
    }

    /**
     * 機能：録画ビデオ一覧を発見.
     * @return 成功true
     */
    public boolean browseRecVideoDms() {
        DlnaInterface di = DlnaInterface.getInstance();
//        if(null!=di){
//            return di.browseRecVideoDms();
//        }
//        return false;
        return null != di && di.browseRecVideoDms();
    }

    /**
     * isDlnaProRecVideoAvailable.
     * @return isDlnaRunning true
     */
    private boolean isDlnaProRecVideoAvailable() {
        DlnaInterface di = DlnaInterface.getInstance();
//        if(null!=di){
//            return di.isDlnaRunning();
//        }
//        return false;
        return null != di && di.isDlnaRunning();
    }
}
