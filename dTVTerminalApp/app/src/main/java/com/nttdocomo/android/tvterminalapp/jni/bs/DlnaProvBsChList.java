/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.bs;


import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;

/**
 * BS
 * 機能：DlnaからActivityにBSデジタル一覧を提供するクラス.
 */
public class DlnaProvBsChList {

    /**
     * 機能：DlnaProvRecVideoを構造.
     */
    public DlnaProvBsChList() {

    }

    /**
     * 機能：Listenを停止.
     */
    public void stopListen() {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return;
        }
        di.setDlnaBsChListListener(null);
    }

    /**
     * 機能：DMSデバイスを取り始める.
     * @param lis listener
     * @param item item
     * @return 成功true
     */
    public boolean start(final DlnaDmsItem item, final DlnaBsChListListener lis) {
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
        di.setDlnaBsChListListener(lis);
        return true;
    }

    /**
     * 機能：録画ビデオ一覧を発見.
     * @return 成功true
     */
    public boolean browseChListDms() {
        DlnaInterface di = DlnaInterface.getInstance();
//        if(null != di){
//            return di.browseBsChListDms();
//        }
//        return false;
        return null != di && di.browseBsChListDms();
    }
}
