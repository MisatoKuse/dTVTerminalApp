/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.dms;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

/**
 * 機能：一台のDMS設備を表示するクラス.
 */
public class DlnaDmsItem {
    //このクラスにて、フィールドは「public」に設定している理由は,
    // １．JNIのC側で操作便利
    // ２．フィールドは追加すると、setとget方法をjavaとc側両方で増やさないよう
    /**デバイスのudn名.*/
    public String mUdn = "";
    /**デバイスのコントロールurl.*/
    public String mControlUrl = "";
    /**デバイスのhttp.*/
    public String mHttp = "";
    /**Friendly名.*/
    public String mFriendlyName = "";
    /**IPアドレス.*/
    public String mIPAddress = "";

    //to do: 使用する必要があれば、新しいフィールドをここで追加

    /**
     * 機能：DMS情報クラスを構造.
     */
    public DlnaDmsItem() {
    }

    /**
     * getPortFromProtocal.
     * @param portStr portStr
     * @return port
     */
    public static int getPortFromProtocal(final String portStr) {
        final String head = "DTCP1PORT=";
        final String tail = ";CONTENTFORMAT";
        int p1 = portStr.indexOf(head) + head.length();
        int p2 = portStr.lastIndexOf(tail);
        if (p1 >= p2) {
            return -1;
        }
        String ret = portStr.substring(p1, p2);
        int retI = -1;
        try {
            retI = Integer.parseInt(ret);
        } catch (Exception e) {
            DTVTLogger.debug("DlnaDmsItem.getPortFromProtocal, " + e.getCause());
        }
        return retI;
    }

    /**
     * isDmsItemValid.
     * @param item item
     * @return true or false
     */
    public static boolean isDmsItemValid(final DlnaDmsItem item) {
        boolean ret = false;
        if (null != item
                && null != item.mUdn && 0 < item.mUdn.length()
                && null != item.mControlUrl && 6 < item.mControlUrl.length()
                && null != item.mIPAddress && 7 < item.mIPAddress.length()) {
            ret = true;
        }
        return ret;
    }
}
