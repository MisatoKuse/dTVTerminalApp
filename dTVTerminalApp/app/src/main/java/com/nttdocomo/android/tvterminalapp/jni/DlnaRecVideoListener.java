/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：録画一覧を提供するインターフェース.
 */
public interface DlnaRecVideoListener {
    /**
     * 機能：Listenerに、録画一覧情報が届く時、コールされる.
     * @param curInfo current recorded video list
     */
    void onVideoBrows(DlnaRecVideoInfo curInfo);

    /**
     * 機能：Listenerに、Dmsが消える時、コールされる.
     * @param curInfo  カレントDlnaDMSInfo
     * @param leaveDmsUdn  　消えるDmsのudn名
     */
    void onDeviceLeave(final DlnaDMSInfo curInfo, final String leaveDmsUdn);

    /**
     * 機能：Listenerに、エラーメセッジを送信.
     * @param msg     エラー情報
     */
    void onError(final String msg);

    /**
     * 機能：各DlnaProviderに、使用しているDmsを戻す.
     * 　　　この設計の理由は使用しているDms以外のDmsは、ネットワークに加入と消える時、
     * 　　　DlnaProviderにイベントを通知しないよう
     * 　　　例外はDms一覧用のDlnaProviderである。
     * @return 使用しているDmsのudn名
     */
    String getCurrentDmsUdn();
}
