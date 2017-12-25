/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;


import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.model.ResultType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.model.search.SearchContentInfo;
import com.nttdocomo.android.tvterminalapp.model.search.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.model.search.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.model.search.SearchSortKind;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchContentInfo;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchErrorData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchRequestData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchResponseData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchWebApi;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchWebApiDelegate;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchResultError;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchState;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchDataProvider implements TotalSearchWebApiDelegate {

    public interface SearchDataProviderListener {
        public void onSearchDataProviderFinishOk(ResultType<TotalSearchContentInfo> resultType);

        public void onSearchDataProviderFinishNg(ResultType<SearchResultError> resultType);
    }

    public static final String comma = ",";

    private SearchState _state = SearchState.inital;

    private SearchDataProviderListener mSearchDataProviderListener = null;


    public SearchState getSearchState() {
        synchronized (this) {
            return _state;
        }
    }

    private void setSearchState(SearchState newSearchState) {
        synchronized (this) {
            _state = newSearchState;
        }
    }

    private TotalSearchWebApi mTotalSearchWebApi = null;

    public void startSearchWith(String keyword,
                                ArrayList<SearchServiceType> serviceTypeArray,
                                SearchNarrowCondition condition,
                                SearchSortKind sortKind,
                                int pageNumber,
                                /*TotalSearchContentInfo handler, */
                                SearchDataProviderListener searchDataProviderListener) {

        //this.handler = handler;
        setSearchState(SearchState.running);
        TotalSearchRequestData request = new TotalSearchRequestData();
        try {
            request.query = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e) {
            DTVTLogger.debug(e);
        }

        request.serviceId = getMappedData(serviceTypeArray, SearchDataProvider.comma);
        request.sortKind = sortKind.searchWebSortType().ordinal();
        request.filterTypeList = condition.searchFilterList();
        request.maxResult = SearchConstants.Search.requestMaxResultCount;
        request.startIndex = pageNumber * SearchConstants.Search.requestMaxResultCount + 1;

        mTotalSearchWebApi = new TotalSearchWebApi();
        mTotalSearchWebApi.setDelegate(this);
        mSearchDataProviderListener = searchDataProviderListener;
        mTotalSearchWebApi.request(request);
    }

    public void cancelSearch() {
        setSearchState(SearchState.canceled);
        DTVTLogger.debug("SearchDataProvider::cancelSearch()");
    }

    private String getMappedData(ArrayList<SearchServiceType> serviceTypeArray, String comma) {
        StringBuilder ret = new StringBuilder("");
        for (int i = 0; i < serviceTypeArray.size(); ++i) {
            SearchServiceType sst = serviceTypeArray.get(i);
            ret.append(sst.serverRequestServiceIdString());
            if (i != serviceTypeArray.size() - 1) {
                ret.append(comma);
            }
        }
        return ret.toString();
    }

    @Override
    public void onSuccess(TotalSearchResponseData result) {
        /* //for test
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        DTVTLogger.debug("SearchDataProvider::onSuccess(), _state=" + _state.toString());
        synchronized (this) {
            if (_state != SearchState.canceled) {

                final ArrayList<SearchContentInfo> contentArray = new ArrayList<SearchContentInfo>();
                result.map(contentArray);

                ResultType<TotalSearchContentInfo> resultType = new ResultType<TotalSearchContentInfo>();
                TotalSearchContentInfo totalSearchContentInfo = new TotalSearchContentInfo();
                totalSearchContentInfo.init(result.totalCount, contentArray);
                resultType.success(totalSearchContentInfo);

                if (null != mSearchDataProviderListener) {
                    mSearchDataProviderListener.onSearchDataProviderFinishOk(onSetClipRequestData(resultType));
                }

                setSearchState(SearchState.finished);
            }
        }
    }

    /**
     * クリップ画面表示データ作成
     *
     * @param resultType サーバレスポンス
     * @return クリップ画面表示データ
     */
    private ResultType<TotalSearchContentInfo> onSetClipRequestData(ResultType<TotalSearchContentInfo> resultType) {
        TotalSearchContentInfo content = resultType.getResultType();

        List<ContentsData> contentsDataList = new ArrayList<>();

        //クリップフラグ設定用
        final String SEARCH_OK_TRUE = "1";
        final String SEARCH_OK_FALSE = "0";

        int thisTimeTotal = content.searchContentInfo.size();
        for (int i = 0; i < thisTimeTotal; ++i) {
            ContentsData contentsData = new ContentsData();
            SearchContentInfo ci = content.searchContentInfo.get(i);

            //画面表示用データ設定
            contentsData.setContentsId(ci.contentId);
            contentsData.setServiceId(String.valueOf(ci.serviceId));
            contentsData.setThumURL(ci.contentPictureUrl);
            contentsData.setTitle(ci.title);

            SearchContentInfo searchContentInfo = resultType.getResultType().searchContentInfo.get(i);

            //TODO:レスポンスパラメータがないため、仮データを設定
            String searchOk;
            if (searchContentInfo.clipFlag) {
                searchOk = SEARCH_OK_TRUE;
            } else {
                searchOk = SEARCH_OK_FALSE;
            }
            contentsData.setSearchOk(searchOk);
            ClipRequestData requestData = new ClipRequestData();
            requestData.setCrid("672017101601");
            requestData.setServiceId("672017101601");
            requestData.setEventId("14c2");
            requestData.setTitleId("");
            requestData.setTitle(searchContentInfo.title);
            requestData.setRValue("G");
            requestData.setLinearStartDate("1513071135");
            requestData.setLinearEndDate("1513306982");
            requestData.setSearchOk(searchOk);
            requestData.setClipTarget(searchContentInfo.title); //TODO:仕様確認中 現在はトーストにタイトル名を表示することとしています
            requestData.setIsNotify("disp_type", "content_type",
                    "display_end_date", "tv_service", "dtv");
            contentsData.setRequestData(requestData);
            contentsDataList.add(contentsData);
        }
        resultType.getResultType().setContentsDataList(contentsDataList);
        return resultType;
    }

    @Override
    public void onFailure(TotalSearchErrorData result) {
        if (SearchState.canceled != getSearchState()) {

            SearchResultError error = SearchResultError.systemError;
            ;
            if (result.error.id.equals(SearchConstants.SearchResponseErrorId.requestError)) {
                error = SearchResultError.requestError;
            } else if (result.error.id.equals(SearchConstants.SearchResponseErrorId.systemError)) {
                error = SearchResultError.systemError;
            }

            //if(null!=handler){

            ResultType<SearchResultError> resultType = new ResultType<SearchResultError>();
            resultType.failure(error);

            //handler.init(resultType);
            if (null != mSearchDataProviderListener) {
                mSearchDataProviderListener.onSearchDataProviderFinishNg(resultType);
            }
            //}

            setSearchState(SearchState.finished);
        }
    }
}
