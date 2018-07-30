/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.jni.hikari;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;

/**
 * 多チャンネル
 * 機能：DlnaからActivityに多チャンネル一覧を提供するクラス.
 */
public class DlnaContentMultiChannelDataProvider implements DlnaManager.BrowseListener, DlnaManager.RemoteConnectStatusChangeListener {

    /**
     * callbackリスナー.
     */
    public interface OnMultiChCallbackListener {
        /**
         * 処理完了callback.
         * @param dlnaObject 多チャンネル再生情報
         */
        void multiChannelFindCallback(final DlnaObject dlnaObject);
        /**
         * ブラウズエラーコールバック.
         */
        void multiChannelErrorCallback();
        /**
         * 接続できない場合のエラーコールバック.
         * @param errorCode エラーコード
         */
        void onConnectErrorCallback(final int errorCode);
    }

    /** チャンネル番号. */
    private String mChannelNr = "";
    /** コンテキスト. */
    private final Context mContext;
    /** コールバック. */
    private final OnMultiChCallbackListener mOnMultiChCallbackListener;
    /** ページインデックス. */
    private int mRequestIndex;

    /**
     * 機能：DlnaProvHikariVideoを構造.
     *
     * @param mContext  コンテキスト
     * @param onMultiChCallbackListener  onMultiChCallbackListener
     */
    public DlnaContentMultiChannelDataProvider(final Context mContext, final OnMultiChCallbackListener onMultiChCallbackListener) {
        this.mContext = mContext;
        mOnMultiChCallbackListener = onMultiChCallbackListener;
    }

    /**
     * 機能：チャンネル情報を探す.
     * @param mChannelNr  チャンネル番号
     */
    public void findChannelByChannelNo(final String mChannelNr) {
        this.mChannelNr = mChannelNr;
        DlnaManager.shared().mBrowseListener = this;
        DlnaManager.shared().mRemoteConnectStatusChangeListener = this;
        DlnaManager.shared().clearQue();
        mRequestIndex = 0;
        DlnaManager.shared().BrowseContentWithContainerId(DlnaUtils.getContainerIdByImageQuality(mContext, DlnaUtils.DLNA_DMS_MULTI_CHANNEL), mRequestIndex);
    }

    @Override
    public void onContentBrowseCallback(final DlnaObject[] objs, final String containerId, final boolean isComplete) {
        for (DlnaObject obj: objs) {
            if (mChannelNr.equals(obj.mChannelNr)) {
                mOnMultiChCallbackListener.multiChannelFindCallback(obj);
                return;
            }
        }
        if (objs.length == 0) {
            mOnMultiChCallbackListener.multiChannelFindCallback(null);
        } else {
            if (isComplete) {
                mOnMultiChCallbackListener.multiChannelFindCallback(null);
                return;
            }
            mRequestIndex += objs.length;
            DlnaManager.shared().clearQue();
            DlnaManager.shared().BrowseContentWithContainerId(DlnaUtils.getContainerIdByImageQuality(mContext, DlnaUtils.DLNA_DMS_MULTI_CHANNEL), mRequestIndex);
        }
    }

    @Override
    public void onContentBrowseErrorCallback(final String containerId) {
        if (mOnMultiChCallbackListener != null) {
            mOnMultiChCallbackListener.multiChannelErrorCallback();
        }
    }

    @Override
    public void onRemoteConnectStatusCallBack(final int errorCode) {
        if (mOnMultiChCallbackListener != null) {
            mOnMultiChCallbackListener.onConnectErrorCallback(errorCode);
        }
    }
}
