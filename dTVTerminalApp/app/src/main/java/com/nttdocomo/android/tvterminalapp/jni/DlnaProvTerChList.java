/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：DlnaからActivityに地上波一覧を提供するクラス.
 */
public class DlnaProvTerChList {

    /**
     * 機能：DlnaProvRecVideoを構造.
     */
    public DlnaProvTerChList() {

    }

    /**
     * 機能：Listenを停止.
     */
    public void stopListen() {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return;
        }
        di.setDlnaTerChListListener(null);
    }

    /**
     * 機能：DMSデバイスを取り始める.
     * @param lis listener
     * @return 成功true
     */
    public boolean start(final DlnaDmsItem item, final DlnaTerChListListener lis) {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return false;
        }
        if (!di.startDlna()) {
            return false;
        }
        boolean ret = di.registerCurrentDms(item);
        if (!ret) {
            return false;
        }
        di.setDlnaTerChListListener(lis);
        return true;
    }

    /**
     * 機能：録画ビデオ一覧を発見.
     * @return 成功true
     */
    public boolean browseChListDms() {
        DlnaInterface di = DlnaInterface.getInstance();
//        if(null != di){
//            return di.browseTerChListDms();
//        }
//        return false;
        return null != di && di.browseTerChListDms();
    }
}
