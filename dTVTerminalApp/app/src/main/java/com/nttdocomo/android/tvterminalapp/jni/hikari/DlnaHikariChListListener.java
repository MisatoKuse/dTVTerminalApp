/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.hikari;


import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsInfo;

/**
 * 機能：多チャンネル一覧を提供するインターフェース.
 */
public interface DlnaHikariChListListener {
    /**
     * 機能：Listenerに、BSデジタル一覧情報が届く時、コールされる.
     * @param curInfo current recorded video list
     */
    void onListUpdate(final DlnaHikariChListInfo curInfo);

    /**
     * 機能：Listenerに、Dmsが消える時、コールされる.
     * @param curInfo 　カレントDlnaDmsInfo
     * @param leaveDmsUdn  消えるDmsのudn名
     */
    void onDeviceLeave(final DlnaDmsInfo curInfo, final String leaveDmsUdn);

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
