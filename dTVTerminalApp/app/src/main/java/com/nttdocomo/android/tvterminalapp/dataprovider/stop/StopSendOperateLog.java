/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.stop;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SendOperateLog;

/**
 * サムネイル取得で行っている通信を止める.
 */
public class StopSendOperateLog extends AsyncTask<SendOperateLog, Void, Void> {
    /**
     * コンストラクタ.
     */
    public StopSendOperateLog() {
        DTVTLogger.start();    }

    @Override
    protected Void doInBackground(final SendOperateLog... sendOperateLogs) {
        if (sendOperateLogs != null) {
            for (SendOperateLog sendOperateLog : sendOperateLogs) {
                if (sendOperateLog != null) {
                    sendOperateLog.stopConnection();
                }
            }
        }
        return null;
    }
}
