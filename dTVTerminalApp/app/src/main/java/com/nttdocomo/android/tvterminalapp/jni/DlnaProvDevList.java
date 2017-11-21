/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：DlnaからActivityにDmsデバイス一覧を提供するクラス
 */
public class DlnaProvDevList {

    /**
     * 機能：DlnaProvDevListを構造
     */
    public DlnaProvDevList()  {

    }

    /**
     * 機能：Listenを停止
     */
    public void stopListen(){
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            return;
        }
        di.setDlnaDevListListener(null);
    }

    /**
     * 機能：DMSデバイスを取り始める
     * @param lis listener
     * @return 成功 true
     */
    public boolean start(DlnaDevListListener lis){
        DlnaInterface di= DlnaInterface.getInstance();
        if(null==di){
            return false;
        }
        if(!di.startDlna()){
            return false;
        }
        di.setDlnaDevListListener(lis);
        return true;
    }
}
