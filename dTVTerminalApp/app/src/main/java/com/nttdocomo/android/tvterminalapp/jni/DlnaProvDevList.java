/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：DlnaからActivityにDmsデバイス一覧を提供するクラス
 */
public class DlnaProvDevList {

    /**
     * 機能：DlnaProvDevListのコンストラクタ
     */
    public DlnaProvDevList() {

    }

    /**
     * 機能：Listenを停止
     */
    public void stopListen() {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return;
        }
        di.setDlnaDevListListener(null);
    }

    /**
     * 機能：指定するudnのdmsが存在するか
     * @param udn udn
     * @return 存在しるか
     */
    public boolean isDmsAvailable(String udn) {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return false;
        }
        return di.isDmsAvailable(udn);
    }

    /**
     * 機能：カレントDMSInfoを戻す
     * @return カレントDMSInfo
     */
    public DlnaDMSInfo getDlnaDMSInfo() {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return null;
        }
        return di.getDlnaDMSInfo();
    }

    /**
     * 機能：DMSデバイスを取り始める
     *
     * @param lis listener
     * @return 成功 true
     */
    public boolean start(DlnaDevListListener lis) {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return false;
        }
        if (!di.startDlna()) {
            return false;
        }
        di.setDlnaDevListListener(lis);
        return true;
    }

    /**
     * 機能：カレントDMSを削除
     */
    public void dmsRemove(){
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return;
        }
        di.dmsRemove();
    }
}
