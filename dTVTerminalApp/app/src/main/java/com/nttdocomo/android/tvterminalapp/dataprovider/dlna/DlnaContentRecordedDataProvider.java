/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.dlna;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.jni.DlnaManager;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;

public class DlnaContentRecordedDataProvider implements DlnaManager.BrowseListener{

    public interface CallbackListener {
        void callback(DlnaObject[] objs);
    }
    private CallbackListener mCallbackListener = null;

    public void listen(CallbackListener listener) {
        mCallbackListener = listener;
    }
    public void stopListen() {
        mCallbackListener = null;
    }

    public boolean browse(Context context) {
        String containerId = DlnaUtils.getContainerIdByImageQuality(context, DlnaUtils.DLNA_DMS_RECORD_LIST);
        DTVTLogger.warning(">>> containerId = " + containerId);
        DlnaManager.shared().mBrowseListener = this;
        DlnaManager.shared().BrowseContentWithContainerId(DlnaUtils.getContainerIdByImageQuality(context, DlnaUtils.DLNA_DMS_RECORD_LIST));
        return true;
    }

    @Override
    public void onContentBrowseCallback(DlnaObject[] objs) {
        if (mCallbackListener != null) {
            mCallbackListener.callback(objs);
        }
    }
}
