/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.hikari;


import android.support.annotation.Nullable;

import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsInfo;
import com.nttdocomo.android.tvterminalapp.jni.dms.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaInterface;

/**
 * 多チャンネル
 * 機能：DlnaからActivityに多チャンネル一覧を提供するクラス.
 */
public class DlnaProvHikariChList implements DlnaHikariChListListener{

    public interface OnApiCallbackListener {
        void itemFindCallback(@Nullable DlnaHikariChListItem resultItem);
    }

    private String mChannelNr = "";
    private OnApiCallbackListener mOnApiCallbackListener;

    /**
     * 機能：DlnaProvHikariVideoを構造.
     */
    public DlnaProvHikariChList(OnApiCallbackListener onApiCallbackListener) {
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
     * @return 成功true
     */
    public boolean start(final DlnaDmsItem item, final String channelNr) {
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
        return di.browseHikariChListDms();
    }

    /**
     * 機能：チャンネル一覧を発見.
     * @return 成功true
     */
    public boolean browseChListDms() {
        DlnaInterface di = DlnaInterface.getInstance();
        return null != di && di.browseHikariChListDms();
    }

    @Override
    public void onListUpdate(DlnaHikariChListInfo curInfo) {
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
    public void onDeviceLeave(DlnaDmsInfo curInfo, String leaveDmsUdn) {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public String getCurrentDmsUdn() {
        return null;
    }
}
