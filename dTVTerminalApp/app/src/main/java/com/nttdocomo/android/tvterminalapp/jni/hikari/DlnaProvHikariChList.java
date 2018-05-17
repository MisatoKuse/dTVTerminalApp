/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.hikari;


import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsInfo;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;

/**
 * 多チャンネル
 * 機能：DlnaからActivityに多チャンネル一覧を提供するクラス.
 */
public class DlnaProvHikariChList implements DlnaHikariChListListener {
    /**
     * callbackリスナー.
     */
    public interface OnApiCallbackListener {
        /**
         * 処理完了callback.
         * @param resultItem resultItem
         */
        void itemFindCallback(@Nullable final DlnaHikariChListItem resultItem);
    }

    /**
     * チャンネル番号.
     */
    private String mChannelNr = "";
    /**
     * callback.
     */
    private OnApiCallbackListener mOnApiCallbackListener;

    /**
     * 機能：DlnaProvHikariVideoを構造.
     * @param onApiCallbackListener  onApiCallbackListener
     */
    public DlnaProvHikariChList(final OnApiCallbackListener onApiCallbackListener) {
        mOnApiCallbackListener = onApiCallbackListener;
    }

    /**
     * 機能：Listenを停止.
     */
    public void stopListen() {
        DlnaInterface di = DlnaInterface.getInstance();
        if (null == di) {
            return;
        }
        di.setDlnaHikariChListListener(null);
    }

    /**
     * 機能：DMSデバイスを取り始める.
     * @param item item
     * @param imageQuality 画質設定
     *@param channelNr チャンネル番号
     * @return 成功true
     */
    public boolean start(final DlnaDmsItem item, final String channelNr, final int imageQuality) {
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
        mChannelNr = channelNr;
        di.setDlnaHikariChListListener(this);
        return di.browseHikariChListDms(imageQuality);
    }

    /**
     * 機能：チャンネル一覧を発見.
     * @param imageQuality 画質設定
     * @return 成功true
     */
    public boolean browseChListDms(final int imageQuality) {
        DlnaInterface di = DlnaInterface.getInstance();
        return null != di && di.browseHikariChListDms(imageQuality);
    }

    @Override
    public void onListUpdate(final DlnaHikariChListInfo curInfo) {
        DlnaHikariChListItem resultItem = null;
        for (int i = 0; i < curInfo.size(); ++i) {
            DlnaHikariChListItem item = curInfo.get(i);
            if (mChannelNr.equals(item.mChannelNr)) {
                resultItem = item;
                break;
            }
        }
        mOnApiCallbackListener.itemFindCallback(resultItem);
    }

    @Override
    public void onDeviceLeave(final DlnaDmsInfo curInfo, final String leaveDmsUdn) {

    }

    @Override
    public void onError(final String msg) {

    }

    @Override
    public String getCurrentDmsUdn() {
        return null;
    }
}
