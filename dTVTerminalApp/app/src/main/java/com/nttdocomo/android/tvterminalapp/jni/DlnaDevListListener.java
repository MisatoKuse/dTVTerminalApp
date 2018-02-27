/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：Dmsデバイス状況を監視するインターフェース.
 */
public interface DlnaDevListListener {
    /**
     * 機能：Listenerに、新しいDms情報が届く時、コールされる.
     * @param curInfo カレントDlnaDMSInfo
     * @param newItem 新しいDms情報
     */
    void onDeviceJoin(final DlnaDMSInfo curInfo, final DlnaDmsItem newItem);

    /**
     * 機能：Listenerに、Dmsが消える時、コールされる.
     * @param curInfo　　　カレントDlnaDMSInfo
     * @param leaveDmsUdn　消えるDmsのudn名
     */
    void onDeviceLeave(final DlnaDMSInfo curInfo, final String leaveDmsUdn);

    /**
     * 機能：Listenerに、エラーメセッジを送信.
     * @param msg     エラー情報
     */
    void onError(final String msg);
}
