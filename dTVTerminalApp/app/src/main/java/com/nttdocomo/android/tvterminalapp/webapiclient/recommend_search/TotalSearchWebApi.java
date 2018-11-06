/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;

import java.util.LinkedHashMap;

/**
 * 検索WebApi.
 */
public class TotalSearchWebApi extends WebApiBase implements WebApiCallback, SearchXmlParser.XMLParserFinishListener {

    /**
     * XMLのparse結果をコールバックする.
     */
    private TotalSearchWebApiDelegate mDelegate;

    /**
     * リクエストパラメータ"displayId"固定値.
     */
    private static final String SEARCH_WEBAPI_PARAM_DISPLAY_ID = "SEA0000001";

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
    public TotalSearchWebApi(final Context context) {
        //コンテキストの退避
        mContext = context;
    }

    /**
     * delegateの設定.
     *
     * @param delegate デリゲート
     */
    public void setDelegate(final TotalSearchWebApiDelegate delegate) {
        synchronized (this) {
            mDelegate = delegate;
        }
    }

    /**
     * 検索リクエスト.
     *
     * @param requestData リクエストデータ
     */
    public void request(final TotalSearchRequestData requestData) {
        DTVTLogger.debug("request");

        if (!mIsCancel) {
            LinkedHashMap<String, String> queryItems = new LinkedHashMap<>();
            queryItems.put(SearchRequestKey.kQuery, requestData.query);
            queryItems.put(SearchRequestKey.kStartIndex, String.valueOf(requestData.startIndex));
            queryItems.put(SearchRequestKey.kMaxResult, String.valueOf(requestData.maxResult));
            queryItems.put(SearchRequestKey.kServiceCategory, requestData.serviceCategory);

            int sortKind = requestData.sortKind;
            queryItems.put(SearchRequestKey.kSortKind, sortKind + "");

            //固定値のため直接指定する
            queryItems.put(SearchRequestKey.kDisplayId, SEARCH_WEBAPI_PARAM_DISPLAY_ID);
            getRecommendSearch(queryItems, this, mContext);
        } else {
            DTVTLogger.error("TotalSearchWebApi is stopping connection");
        }
    }

    /**
     *  コンテンツメタ情報の検索リクエスト.
     * @param requestData リクエストデータ
     */
    public void requestContentDetail(final TotalSearchRequestData requestData) {
        DTVTLogger.debug("request");

        if (!mIsCancel) {
            LinkedHashMap<String, String> queryItems = new LinkedHashMap<>();

            queryItems.put(SearchRequestKey.kQuery, requestData.query);

            String serviceId = requestData.serviceId;
            queryItems.put(SearchRequestKey.kServiceId, serviceId);
            queryItems.put(SearchRequestKey.kSearchFields, requestData.searchFields);

            queryItems.put(SearchRequestKey.kDisplayId, requestData.displayId);

            getNoPassword(queryItems, this, mContext);
        } else {
            DTVTLogger.error("TotalSearchWebApi is stopping connection");
        }
    }

    @Override
    public void onFinish(final String responseData) {
        String str = responseData;
        if (null == responseData || 0 == responseData.length()) {
            str = "";
        }
        new SearchXmlParser(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, str);
    }

    @Override
    public void onXMLParserFinish(final TotalSearchResponseData responseData) {
        if (null != mDelegate) {
            mDelegate.onSuccess(responseData);
        }
    }

    @Override
    public void onXMLParserError(final TotalSearchErrorData errorData) {
        if (null != mDelegate) {
            mDelegate.onFailure(errorData);
        }
    }

    /**
     * 通信を止める.
     * TotalSearchWebApiは検索の度にnewされているので通信可能状態にする処理は不要
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopHTTPConnection();
    }
}