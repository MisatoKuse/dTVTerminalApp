/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;

public class NoticeWebClient implements HttpThread.HttpThreadFinish {
    /**
     * コールバック
     */
    private final NoticeWebClientCallBack mNoticeWebClientCallBack;

    /**
     * HTTP通信スレッド.
     */
    private HttpThread mHttpThread = null;

    public NoticeWebClient(final Context context, final NoticeWebClientCallBack mNoticeWebClientCallBack) {
        this.mNoticeWebClientCallBack = mNoticeWebClientCallBack;
        this.mHttpThread = new HttpThread(UrlConstants.WebUrl.NOTICE_URL, this, context, null, null);
    }

    public interface NoticeWebClientCallBack {
        void noticeGetFinishCallBack();
    }

    public void requestNotice() {
        mHttpThread.start();
    }

    public String getLastModified() {
        return mHttpThread.getResponseHeaderField().lastModified;
    }

    @Override
    public void onHttpThreadFinish(String str, ErrorState errorStatus) {
        if (mNoticeWebClientCallBack != null) {
            mNoticeWebClientCallBack.noticeGetFinishCallBack();
        }
    }
}
