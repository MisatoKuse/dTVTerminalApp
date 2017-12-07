/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：DlnaからActivityにBSデジタル一覧を提供するクラス
 */
public class DlnaProvHikariChList {

    private String mCurrentDmsUdn;

    /**
     * 機能：DlnaProvRecVideoを構造
     */
    public DlnaProvHikariChList() {

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
    public boolean start(DlnaDmsItem item, DlnaHikariChListListener lis){
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            return false;
        }
        if(!di.startDlna()){
            return false;
        }
        boolean ret = di.registerCurrentDms(item);
        if(!ret){
            return false;
        }
        di.setDlnaHikariChListListener(lis);
        return true;
    }

    /**
     * 機能：録画ヴィデオ一覧を発見
     * @return 成功true
     */
    public boolean browseBsChListDms(){
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            return false;
        }
        return di.browseBsChListDms();
    }
}
