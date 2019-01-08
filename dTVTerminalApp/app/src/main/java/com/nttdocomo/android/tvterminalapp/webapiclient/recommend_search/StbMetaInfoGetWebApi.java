/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;

import java.util.LinkedHashMap;

/**
 * STBメタデータ取得WebApi.
 */
public class StbMetaInfoGetWebApi extends WebApiBase implements WebApiCallback, StbMetaInfoGetXmlParser.XMLParserFinishListener {

    /**
     * XMLのparse結果をコールバックする.
     */
    private StbMetaInfoGetWebApiDelegate mDelegate;

    /**
     * SSLチェック用コンテキスト.
     */
    private final Context mContext;

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public StbMetaInfoGetWebApi(final Context context) {
        //コンテキストの退避
        mContext = context;
    }

    /**
     * delegateの設定.
     *
     * @param delegate デリゲート
     */
    public void setDelegate(final StbMetaInfoGetWebApiDelegate delegate) {
        synchronized (this) {
            mDelegate = delegate;
        }
    }

    /**
     *  STBメタデータ取得のリクエスト.
     * @param requestData リクエストデータ
     */
    public void request(final StbMetaInfoRequestData requestData) {
        DTVTLogger.debug("request");

        if (!mIsCancel) {
            LinkedHashMap<String, String> queryItems = new LinkedHashMap<>();

            queryItems.put(StbMetainfoRequestKey.kServiceCategory, requestData.serviceCategory);
            queryItems.put(StbMetainfoRequestKey.kContentId, requestData.contentsId);
            if (requestData.episodeStartIndex > 0) {
                queryItems.put(StbMetainfoRequestKey.kEpisodeStartIndex, String.valueOf(requestData.episodeStartIndex));
            }
            if (requestData.episodeMaxResult > 0) {
                queryItems.put(StbMetainfoRequestKey.kEpisodeMaxResult, String.valueOf(requestData.episodeMaxResult));
            }

            getNoPassword(UrlConstants.WebApiUrl.STB_META_DATA_URL, queryItems, this, mContext);
        } else {
            DTVTLogger.error("StbMetaInfoGetWebApi is stopping connection");
        }
    }

    @Override
    public void onFinish(final String responseData) {
        String str = responseData;
        if (null == responseData || 0 == responseData.length()) {
            str = "";
        }
        new StbMetaInfoGetXmlParser(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, str);
    }

    @Override
    public void onXMLParserFinish(final StbMetaInfoResponseData responseData) {
        if (null != mDelegate) {
            mDelegate.onSuccess(responseData);
        }
    }

    @Override
    public void onXMLParserError(final StbMetaInfoGetErrorData errorData) {
        if (null != mDelegate) {
            mDelegate.onFailure(errorData);
        }
    }

    /**
     * 通信を止める.
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopHTTPConnection();
    }
}