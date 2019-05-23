/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.NoticeWebClient;

public class NoticeDataProvider implements NoticeWebClient.NoticeWebClientCallBack {

    private NoticeWebClient mNoticeWebClient;
    private final Context mContext;
    private final NoticeDataProviderCallback mNoticeDataProviderCallback;

    public interface NoticeDataProviderCallback {
        void noticeGetFinishCallBack(final boolean isNewly);
    }

    public NoticeDataProvider(Context mContext, NoticeDataProviderCallback mNoticeDataProviderCallback) {
        this.mContext = mContext;
        this.mNoticeDataProviderCallback = mNoticeDataProviderCallback;
        mNoticeWebClient = new NoticeWebClient(this.mContext, this);
    }

    @Override
    public void noticeGetFinishCallBack() {
        boolean isNewly = SharedPreferencesUtils.updateNewlyNoticeFlag(mContext, mNoticeWebClient.getLastModified());
        if (mNoticeDataProviderCallback != null) {
            mNoticeDataProviderCallback.noticeGetFinishCallBack(isNewly);
        }
    }

    public void requestNotice() {
        mNoticeWebClient.requestNotice();
    }
}
