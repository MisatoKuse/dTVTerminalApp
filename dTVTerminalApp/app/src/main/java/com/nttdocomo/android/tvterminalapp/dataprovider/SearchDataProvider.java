package com.nttdocomo.android.tvterminalapp.dataprovider;


import android.util.Log;

import com.nttdocomo.android.tvterminalapp.common.DCommon;
import com.nttdocomo.android.tvterminalapp.model.ResultType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.model.search_temp.SearchContentInfo;
import com.nttdocomo.android.tvterminalapp.model.search_temp.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.model.search_temp.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.model.search_temp.SearchSortKind;
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

public class SearchDataProvider implements TotalSearchWebApiDelegate {

    public interface SearchDataProviderListener{
        public void onSearchDataProviderFinishOk(ResultType<TotalSearchContentInfo> resultType);
        public void onSearchDataProviderFinishNg(ResultType<SearchResultError> resultType);
    }

    public static final String comma = ",";

    private SearchState _state = SearchState.inital;

    private SearchDataProviderListener mSearchDataProviderListener=null;


    public SearchState getSearchState(){
        synchronized (this) {
            return _state;
        }
    }

    private void setSearchState(SearchState newSearchState){
        synchronized (this) {
            _state = newSearchState;
        }
    }

    private TotalSearchWebApi mTotalSearchWebApi=null;

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
            e.printStackTrace();
        }

        request.serviceId = getMappedData(serviceTypeArray, SearchDataProvider.comma);
        request.sortKind = sortKind.searchWebSortType().ordinal();
        request.filterTypeList = condition.searchFilterList();
        request.maxResult = SearchConstants.Search.requestMaxResultCount;
        request.startIndex = pageNumber * SearchConstants.Search.requestMaxResultCount + 1;

        mTotalSearchWebApi = new TotalSearchWebApi();
        mTotalSearchWebApi.setDelegate(this);
        mSearchDataProviderListener= searchDataProviderListener;
        mTotalSearchWebApi.request(request);
    }

    public void cancelSearch() {
        setSearchState(SearchState.canceled);
        Log.d(DCommon.LOG_DEF_TAG, "SearchDataProvider::cancelSearch()");
    }

    private String getMappedData(ArrayList<SearchServiceType> serviceTypeArray, String comma){
        StringBuilder ret=new StringBuilder("");
        for(int i=0;i<serviceTypeArray.size();++i){
            SearchServiceType sst=serviceTypeArray.get(i);
            ret.append(sst.serverRequestServiceIdString());
            if(i!= serviceTypeArray.size() -1){
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
        Log.d(DCommon.LOG_DEF_TAG, "SearchDataProvider::onSuccess(), _state=" + _state.toString());
        synchronized (this) {
            if (_state != SearchState.canceled) {

                final ArrayList<SearchContentInfo> contentArray = new ArrayList<SearchContentInfo>();
                result.map(contentArray);

                ResultType<TotalSearchContentInfo> resultType = new ResultType<TotalSearchContentInfo>();
                TotalSearchContentInfo totalSearchContentInfo = new TotalSearchContentInfo();
                totalSearchContentInfo.init(result.totalCount, contentArray);
                resultType.success(totalSearchContentInfo);

                if (null != mSearchDataProviderListener) {
                    mSearchDataProviderListener.onSearchDataProviderFinishOk(resultType);
                }

                setSearchState(SearchState.finished);
            }
        }
    }

    @Override
    public void onFailure(TotalSearchErrorData result) {
        if(SearchState.canceled != getSearchState()){

            SearchResultError error = SearchResultError.systemError;;
            if(result.error.id.equals(SearchConstants.SearchResponseErrorId.requestError)){
                error = SearchResultError.requestError;
            } else if (result.error.id.equals(SearchConstants.SearchResponseErrorId.systemError)){
                error = SearchResultError.systemError;
            }

            //if(null!=handler){

            ResultType<SearchResultError> resultType= new ResultType<SearchResultError>();
            resultType.failure(error);

            //handler.init(resultType);
            if(null!=mSearchDataProviderListener){
                mSearchDataProviderListener.onSearchDataProviderFinishNg(resultType);
            }
            //}

            setSearchState(SearchState.finished);
        }
    }
}
