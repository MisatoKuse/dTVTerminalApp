package com.nttdocomo.android.tvterminalapp.DataProvider;


import com.nttdocomo.android.tvterminalapp.Model.ResultType;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchContentInfo;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchSortKind;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.TotalSearchContentInfo;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.TotalSearchErrorData;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.TotalSearchRequestData;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.TotalSearchResponseData;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.TotalSearchWebApi;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.TotalSearchWebApiDelegate;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.XMLParser;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.SearchResultError;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.SearchState;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchDataProvider implements TotalSearchWebApiDelegate {

    public interface SearchDataProviderListener{
        public void onSearchDataProviderFinishOk(ResultType<TotalSearchContentInfo> resultType);
        public void onSearchDataProviderFinishNg(ResultType<SearchResultError> resultType);
    }

    public static final String comma = ",";
    //private Lock lock = new ReentrantLock();    //private let lock = NSLock()
    private SearchState _state = SearchState.inital;
    private TotalSearchContentInfo handler=null;    //private var handler: ((ResultType<TotalSearchContentInfo>) -> Void)?

    private SearchDataProviderListener mSearchDataProviderListener=null;


    public SearchState getSearchState(){
        synchronized (this) {
            return _state;
        }
    }

    public void setSearchState(SearchState newSearchState){
        synchronized (this) {
            _state = newSearchState;
        }
    }

    private TotalSearchWebApi mTotalSearchWebApi=null;
    XMLParser.XMLParserFinishListener mXMLParserFinishListener=null;
    public void startSearchWith(String keyword,
                                ArrayList<SearchServiceType> serviceTypeArray,
                                SearchNarrowCondition condition,
                                SearchSortKind sortKind,
                                int pageNumber,
                                /*TotalSearchContentInfo handler, */
                                SearchDataProviderListener searchDataProviderListener) {

        this.handler = handler;
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
        int pageIndex = ( 0==pageNumber ? 1: pageNumber);
        request.startIndex = (pageIndex - 1) * SearchConstants.Search.requestMaxResultCount + 1;

        mTotalSearchWebApi = new TotalSearchWebApi();
        mTotalSearchWebApi.setDelegate(this);
        mSearchDataProviderListener= searchDataProviderListener;
        mTotalSearchWebApi.request(request);
    }

    public void cancelSearch() {
        synchronized (this) {
            _state = SearchState.canceled;
        }
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
        if(_state != SearchState.canceled ) {

            final ArrayList<SearchContentInfo> contentArray=new ArrayList<SearchContentInfo>();
            result.map(contentArray);

            ResultType<TotalSearchContentInfo> resultType= new ResultType<TotalSearchContentInfo>();
            TotalSearchContentInfo totalSearchContentInfo=new TotalSearchContentInfo();
            totalSearchContentInfo.init(result.totalCount, contentArray);
            resultType.success(totalSearchContentInfo);

            if(null!=mSearchDataProviderListener){
                mSearchDataProviderListener.onSearchDataProviderFinishOk(resultType);
            }

            setSearchState(SearchState.finished);
        }
    }

    @Override
    public void onFailure(TotalSearchErrorData result) {
        if(SearchState.canceled != getSearchState()){

            SearchResultError error;
            if(result.error.id.equals(SearchConstants.SearchResponseErrorId.requestError)){
                error = SearchResultError.requestError;
            } else if (result.error.id.equals(SearchConstants.SearchResponseErrorId.systemError)){
                error = SearchResultError.systemError;
            } else {
                error = SearchResultError.systemError;
            }

            if(null!=handler){

                ResultType<SearchResultError> resultType= new ResultType<SearchResultError>();
                resultType.failure(error);

                //handler.init(resultType);
                if(null!=mSearchDataProviderListener){
                    mSearchDataProviderListener.onSearchDataProviderFinishNg(resultType);
                }
            }

            setSearchState(SearchState.finished);
        }
    }
}
