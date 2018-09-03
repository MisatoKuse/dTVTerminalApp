/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.dlna;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;

/**
 * BSデータプロバイダー.
 */
public class DlnaContentBsChannelDataProvider implements DlnaManager.BrowseListener, DlnaManager.RemoteConnectStatusChangeListener {

    /**
     * DataProviderコールバック.
     */
    public interface ContentsDataCallback {
        /**
         * コンテンツブラウズコールバック.
         * @param objs コンテンツリスト
         * @param containerId パス
         * @param isComplete ページング終了フラグ
         */
        void onContentBrowseCallback(final DlnaObject[] objs, final String containerId, final boolean isComplete);
        /**
         * コンテンツブラウズエラーコールバック.
         * @param containerId パス
         */
        void onContentBrowseErrorCallback(final String containerId);
        /**
         * 接続できない場合のエラーコールバック.
         * @param errorCode エラーコード
         */
        void onConnectErrorCallback(final int errorCode);
        /**
         * 接続タイムアウト場合のエラーコールバック.
         */
        void onRemoteConnectTimeOutCallback();
    }

    /**コールバック.*/
    private ContentsDataCallback mCallback = null;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public DlnaContentBsChannelDataProvider(final Context context) {
        mCallback = (ContentsDataCallback) context;
    }

    @Override
    public void onContentBrowseCallback(final DlnaObject[] objs, final String containerId, final boolean isComplete) {
        if (objs != null) {
            if (mCallback != null) {
                mCallback.onContentBrowseCallback(objs, containerId, isComplete);
            }
        } else {
            DTVTLogger.error("no ContentBrowseCallback");
        }
    }

    @Override
    public void onContentBrowseErrorCallback(final String containerId) {
        if (mCallback != null) {
            mCallback.onContentBrowseErrorCallback(containerId);
        }
    }

    @Override
    public void onRemoteConnectStatusCallBack(final int errorCode) {
        if (mCallback != null) {
            mCallback.onConnectErrorCallback(errorCode);
        }
    }

    /**
     * BrowseContentWithContainerId.
     * @param containerId パス
     * @param requestIndex 請求ポジション
     */
    public void browseContentWithContainerId(final String containerId, final int requestIndex) {
        DlnaManager.shared().mBrowseListener = null;
        DlnaManager.shared().mBrowseListener = this;
        DlnaManager.shared().mRemoteConnectStatusChangeListener = this;
        DlnaManager.shared().BrowseContentWithContainerId(containerId, requestIndex);
    }

    @Override
    public void onContentBrowseRemoteTimeOut() {
        if (mCallback != null) {
            mCallback.onRemoteConnectTimeOutCallback();
        }
    }
}
