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
 * 録画データプロバイダー.
 */
public class DlnaContentRecordedDataProvider implements DlnaManager.BrowseListener, DlnaManager.RemoteConnectStatusChangeListener {

    /**
     * コールバック.
     */
    public interface CallbackListener {
        /**
         * コンテンツブラウズコールバック.
         * @param objs コンテンツリスト
         * @param isComplete ページング終了フラグ
         */
        void onBrowseCallback(DlnaObject[] objs, boolean isComplete);
        /**
         * コンテンツブラウズエラーコールバック.
         */
        void onBrowseErrorCallback();
        /**
         * 接続できない場合のエラーコールバック.
         * @param errorCode エラーコード
         */
        void onConnectErrorCallback(final int errorCode);
    }
    /**コールバック.*/
    private CallbackListener mCallbackListener = null;

    /**
     * コールバックリスナーを設定.
     * @param listener リスナー
     */
    public void listen(final CallbackListener listener) {
        mCallbackListener = listener;
    }

    /**
     * コールバックリスナーを停止.
     */
    public void stopListen() {
        mCallbackListener = null;
    }

    /**
     * ブラウズ処理.
     * @param context コンテキスト
     * @param requestIndex ページングインデックス
     */
    public boolean browse(final Context context, final int requestIndex) {
        String containerId = DlnaUtils.getContainerIdByImageQuality(context, DlnaUtils.DLNA_DMS_RECORD_LIST);
        DTVTLogger.warning(">>> containerId = " + containerId);
        DlnaManager.shared().mBrowseListener = this;
        DlnaManager.shared().mRemoteConnectStatusChangeListener = this;
        DlnaManager.shared().clearQue();
        DlnaManager.shared().BrowseContentWithContainerId(containerId, requestIndex);
        return true;
    }

    @Override
    public void onContentBrowseCallback(final DlnaObject[] objs, final String containerId, final boolean isComplete) {
        if (mCallbackListener != null) {
            mCallbackListener.onBrowseCallback(objs, isComplete);
        }
    }

    @Override
    public void onContentBrowseErrorCallback(final String containerId) {
        if (mCallbackListener != null) {
            mCallbackListener.onBrowseErrorCallback();
        }
    }

    @Override
    public void onRemoteConnectStatusCallBack(final int errorCode) {
        if (mCallbackListener != null) {
            mCallbackListener.onConnectErrorCallback(errorCode);
        }
    }
}
