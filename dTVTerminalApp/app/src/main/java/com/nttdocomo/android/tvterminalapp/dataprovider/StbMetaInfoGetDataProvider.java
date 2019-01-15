/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.struct.ResultType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.StbMetaInfoGetErrorData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.StbMetaInfoGetWebApi;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.StbMetaInfoGetWebApiDelegate;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.StbMetaInfoRequestData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.StbMetaInfoResponseData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchResultError;

/**
 * STBメタデータ取得データプロバイダ.
 */
public class StbMetaInfoGetDataProvider implements StbMetaInfoGetWebApiDelegate {

    /** STBメタデータ取得プロパイダ.*/
    private StbMetaInfoGetDataProviderListener mStbMetaInfoGetDataProviderListener = null;
    /** STBメタデータ取得用WebAPI.*/
    private StbMetaInfoGetWebApi mStbMetaInfoGetWebApi;
    /** 通信禁止判定フラグ.*/
    private boolean mIsCancel = false;
    /**コロン.*/
    private static final String COLON_SEPARATOR = ":";

    /**
     * STBメタデータ取得プロバイダリスナーインターフェース.
     */
    public interface StbMetaInfoGetDataProviderListener {

        /**
         * STBメタデータ取得成功コールバック.
         *
         * @param resultType 成功時データ取得結果
         */
        void onStbMetaInfoGetDataProviderFinishOk(ResultType<StbMetaInfoResponseData> resultType);

        /**
         * STBメタデータ取得失敗コールバック.
         *
         * @param resultType 失敗時データ取得結果
         */
        void onStbMetaInfoGetDataProviderFinishNg(ResultType<SearchResultError> resultType);
    }

    /**
     * 検索サーバからSTBメタ情報を取得.
     * @param contentsId コンテンツId
     * @param serviceId  サービスId
     * @param categoryId カテゴリId
     * @param context context
     */
    public void getStbMetaInfo(final String contentsId, final String serviceId, final String categoryId, final Context context) {
        if (!mIsCancel) {
            StbMetaInfoRequestData request = new StbMetaInfoRequestData();
            String serviceCategory = serviceId.concat(COLON_SEPARATOR).concat(categoryId);

            request.contentsId = contentsId;
            request.serviceCategory = serviceCategory;

            mStbMetaInfoGetWebApi = new StbMetaInfoGetWebApi(context);
            mStbMetaInfoGetWebApi.setDelegate(this);
            mStbMetaInfoGetDataProviderListener = (StbMetaInfoGetDataProviderListener) context;
            mStbMetaInfoGetWebApi.request(request);
        } else {
            DTVTLogger.error("getStbMetaInfo is stopping connection");
        }
    }

    @Override
    public void onSuccess(final StbMetaInfoResponseData result) {
        synchronized (this) {
            DTVTLogger.start();

            ResultType<StbMetaInfoResponseData> resultType = new ResultType<>();
            resultType.success(result);

            if (null != mStbMetaInfoGetDataProviderListener) {
                sendStbMetaInfoGetOkApiCallback(resultType);
            }

            DTVTLogger.end();
        }
    }

    @Override
    public void onFailure(final StbMetaInfoGetErrorData result) {
        DTVTLogger.start();
        SearchResultError error = SearchResultError.systemError;
        ErrorState errorState = getError();
        if (result.error.id.equals(SearchConstants.SearchResponseErrorId.requestError)) {
            error = SearchResultError.requestError;
            errorState.setErrorType(DtvtConstants.ErrorType.SERVER_ERROR);
        } else if (result.error.id.equals(DtvtConstants.SEARCH_ERROR_ID_1)) {
            error = SearchResultError.parseError;
            errorState.setErrorType(DtvtConstants.ErrorType.PARSE_ERROR);
        }

        ResultType<SearchResultError> resultType = new ResultType<>();
        resultType.failure(error);

        if (null != mStbMetaInfoGetDataProviderListener) {
            sendStbMetaInfoGetNgApiCallback(resultType);
        }
        DTVTLogger.end();
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        if (mStbMetaInfoGetWebApi != null) {
            mStbMetaInfoGetWebApi.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
    }

    /**
     * 通信エラーメッセージを取得する.
     * @return エラーメッセージ
     */
    public ErrorState getError() {
        ErrorState errorState = null;
        if (mStbMetaInfoGetWebApi != null) {
            errorState = mStbMetaInfoGetWebApi.getError();
        }
        return errorState;
    }

    /**
     * STBメタデータ取得成功結果を戻す.
     *
     * @param resultType STBメタデータ取得成功結果
     */
    private void sendStbMetaInfoGetOkApiCallback(final ResultType<StbMetaInfoResponseData> resultType) {
        if (mStbMetaInfoGetDataProviderListener != null) {
            mStbMetaInfoGetDataProviderListener.onStbMetaInfoGetDataProviderFinishOk(resultType);
        }
    }

    /**
     * STBメタデータ取得失敗結果を戻す.
     *
     * @param resultType STBメタデータ取得失敗結果
     */
    private void sendStbMetaInfoGetNgApiCallback(final ResultType<SearchResultError> resultType) {
        if (mStbMetaInfoGetDataProviderListener != null) {
            mStbMetaInfoGetDataProviderListener.onStbMetaInfoGetDataProviderFinishNg(resultType);
        }
    }
}