/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：DlnaからActivityに録画一覧を提供するクラス
 */
public class DlnaProvRecVideo {

    private String mCurrentDmsUdn;

    /**
     * 機能：DlnaProvRecVideoを構造
     */
    public DlnaProvRecVideo() {

    }

    /**
     * 機能：Listenを停止
     */
    public void stopListen(){
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            return;
        }
        di.setDlnaRecVideoBaseListener(null);
    }

    /**
     * 機能：DMSデバイスを取り始める
     * @param lis listener
     * @return 成功true
     */
    public boolean start(String udn, DlnaRecVideoListener lis){
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            return false;
        }
        if(!di.startDlna()){
            return false;
        }
        di.registerCurrentDms(udn);
        di.setDlnaRecVideoBaseListener(lis);
        return true;
    }

    /**
     * 機能：録画ヴィデオ一覧を発見
     * @param ctl ControlUrl
     * @return 成功true
     */
    public boolean browseRecVideoDms(String ctl){
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            return false;
        }
        return di.browseRecVideoDms(ctl);
    }
}
