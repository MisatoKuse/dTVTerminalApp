/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni;


/**
 * 機能：地上波一覧を提供するインターフェース
 */
public interface DlnaTerChListListener {
    /**
     * 機能：Listenerに、地上波一覧情報が届く時、コールされる
     * @param curInfo current recorded video list
     */
    void onListUpdate(DlnaTerChListInfo curInfo);

    /**
     * 機能：Listenerに、Dmsが消える時、コールされる
     * @param curInfo　　　カレントDlnaDMSInfo
     * @param leaveDmsUdn　消えるDmsのudn名
     */
    void onDeviceLeave(DlnaDMSInfo curInfo, String leaveDmsUdn);

    /**
     * 機能：Listenerに、エラーメセッジを送信
     * @param msg     エラー情報
     */
    void onError(String msg);

    /**
     * 機能：各DlnaProviderに、使用しているDmsを戻す
     * 　　　この設計の理由は使用しているDms以外のDmsは、ネットワークに加入と消える時、
     * 　　　DlnaProviderにイベントを通知しないよう
     * 　　　例外はDms一覧用のDlnaProviderである。
     * @return 使用しているDmsのudn名
     */
    String getCurrentDmsUdn();
}
