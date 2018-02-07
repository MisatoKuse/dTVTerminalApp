/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;


import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.struct.ResultType;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.struct.SearchContentInfo;
import com.nttdocomo.android.tvterminalapp.struct.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.common.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.struct.SearchSortKind;
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

/**
 * 検索画面用データプロバイダ.
 */
public class SearchDataProvider implements TotalSearchWebApiDelegate {

    /**
     * 検索の状態.
     */
    private SearchState mState = SearchState.inital;
    /**
     * 検索プロパイダ.
     */
    private SearchDataProviderListener mSearchDataProviderListener = null;
    /**
     * 検索用WebAPI.
     */
    private TotalSearchWebApi mTotalSearchWebApi;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * テレビtab.
     */
    private static final int PAGE_NO_OF_SERVICE_TELEVISION = 0;
    /**
     * ビデオtab.
     */
    private static final int PAGE_NO_OF_SERVICE_VIDEO = PAGE_NO_OF_SERVICE_TELEVISION + 1;
    /**
     * dTVチャンネルtab.
     */
    private static final int PAGE_NO_OF_SERVICE_DTV_CHANNEL = PAGE_NO_OF_SERVICE_TELEVISION + 2;
    /**
     * dTVtab.
     */
    private static final int PAGE_NO_OF_SERVICE_DTV = PAGE_NO_OF_SERVICE_TELEVISION + 3;
    /**
     * dアニメtab.
     */
    private static final int PAGE_NO_OF_SERVICE_DANIME = PAGE_NO_OF_SERVICE_TELEVISION + 4;

    /**
     * 検索データプロバイダリスナーインターフェース.
     */
    public interface SearchDataProviderListener {

        /**
         * 検索リクエスト成功コールバック.
         *
         * @param resultType 検索結果
         */
        void onSearchDataProviderFinishOk(ResultType<TotalSearchContentInfo> resultType);

        /**
         * 検索リクエスト失敗コールバック.
         *
         * @param resultType 検索結果
         */
        void onSearchDataProviderFinishNg(ResultType<SearchResultError> resultType);
    }

    /**
     * 検索状態取得.
     *
     * @return 検索状態
     */
    private SearchState getSearchState() {
        synchronized (this) {
            return mState;
        }
    }

    /**
     * 検索状態設定.
     *
     * @param newSearchState 検索状態
     */
    private void setSearchState(final SearchState newSearchState) {
        synchronized (this) {
            mState = newSearchState;
        }
    }

    /**
     * 検索リクエスト開始.
     *
     * @param keyword    検索キーザード
     * @param condition  フィルタタイプ設定
     * @param pageIndex  ページ位置
     * @param sortKind   ソート設定
     * @param pageNumber 検索結果返却開始位置
     * @param context    コンテクストファイル
     */
    public void startSearchWith(final String keyword,
                                final SearchNarrowCondition condition,
                                final int pageIndex,
                                final SearchSortKind sortKind,
                                final int pageNumber,
                                final Context context) {
        if (!mIsCancel) {
            setSearchState(SearchState.running);
            TotalSearchRequestData request = new TotalSearchRequestData();
            try {
                request.query = URLEncoder.encode(keyword, "utf-8");
            } catch (UnsupportedEncodingException e) {
                DTVTLogger.debug(e);
            }

            request.serviceId = StringUtils.setCommaSeparator(getCurrentSearchServiceTypeArray(pageIndex));
            request.categoryId = StringUtils.setCommaSeparator(getCurrentSearchCategoryTypeArray(pageIndex));
            request.sortKind = sortKind.searchWebSortType().ordinal();
            request.filterTypeList = condition.searchFilterList();
            request.maxResult = SearchConstants.Search.requestMaxResultCount;
            request.startIndex = pageNumber * SearchConstants.Search.requestMaxResultCount + 1;
            request.filterViewableAge = UserInfoUtils.getRecommendUserAge(getUserAgeInfo(context, pageIndex));

            mTotalSearchWebApi = new TotalSearchWebApi(context);
            mTotalSearchWebApi.setDelegate(this);
            mSearchDataProviderListener = (SearchDataProviderListener) context;
            mTotalSearchWebApi.request(request);
        }
    }

    /**
     * TODO:検索中止処理開始用.
     */
    public void cancelSearch() {
        setSearchState(SearchState.canceled);
        DTVTLogger.debug("SearchDataProvider::cancelSearch()");
    }

    @Override
    public void onSuccess(final TotalSearchResponseData result) {
        synchronized (this) {
            DTVTLogger.debug("SearchDataProvider::onSuccess(), _state=" + mState.toString());
            if (mState != SearchState.canceled || !mIsCancel) {

                final ArrayList<SearchContentInfo> contentArray = new ArrayList<>();
                result.map(contentArray);

                ResultType<TotalSearchContentInfo> resultType = new ResultType<>();
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
     * クリップ画面表示データ作成.
     *
     * @param resultType サーバレスポンス
     * @return クリップ画面表示データ
     */
    private ResultType<TotalSearchContentInfo> onSetClipRequestData(final ResultType<TotalSearchContentInfo> resultType) {
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
            if (!TextUtils.isEmpty(ci.contentPictureUrl1)) {
                contentsData.setThumURL(ci.contentPictureUrl1);
            } else {
                contentsData.setThumURL(ci.contentPictureUrl2);
            }
            contentsData.setTitle(ci.title);
            contentsData.setRecommendOrder(ci.rank);

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
            requestData.setIsNotify("disp_type", "content_type",
                    "1513306982", "tv_service", "dtv");
            contentsData.setRequestData(requestData);
            contentsDataList.add(contentsData);
        }
        resultType.getResultType().setContentsDataList(contentsDataList);
        return resultType;
    }

    @Override
    public void onFailure(final TotalSearchErrorData result) {
        if (SearchState.canceled != getSearchState() || !mIsCancel) {

            SearchResultError error = SearchResultError.systemError;
            if (result.error.id.equals(SearchConstants.SearchResponseErrorId.requestError)) {
                error = SearchResultError.requestError;
            } else if (result.error.id.equals(SearchConstants.SearchResponseErrorId.systemError)) {
                error = SearchResultError.systemError;
            }

            //if(null!=handler){

            ResultType<SearchResultError> resultType = new ResultType<>();
            resultType.failure(error);

            //handler.init(resultType);
            if (null != mSearchDataProviderListener) {
                mSearchDataProviderListener.onSearchDataProviderFinishNg(resultType);
            }
            //}

            setSearchState(SearchState.finished);
        }
    }

    /**
     * リクエスト用カテゴリIDを設定する.
     *
     * @param pageIndex ページ位置
     * @return カテゴリID
     */
    private ArrayList<String> getCurrentSearchCategoryTypeArray(final int pageIndex) {
        ArrayList<String> ret = new ArrayList<>();

        switch (pageIndex) {
            case PAGE_NO_OF_SERVICE_TELEVISION: //テレビ
                ret.add(SearchServiceType.CategoryId.H4D_CATEGORY_TERRESTRIAL_DIGITAL);
                ret.add(SearchServiceType.CategoryId.H4D_CATEGORY_SATELLITE_BROADCASTING);
                ret.add(SearchServiceType.CategoryId.H4D_CATEGORY_IP_TV);
                ret.add(SearchServiceType.CategoryId.H4D_CATEGORY_DTV_CHANNEL_BROADCAST);
                ret.add(SearchServiceType.CategoryId.H4D_CATEGORY_RECORDED_CONTENTS);
                break;
            case PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
                ret.add(SearchServiceType.CategoryId.H4D_CATEGORY_DTV_CHANNEL_WATCH_AGAIN);
                ret.add(SearchServiceType.CategoryId.H4D_CATEGORY_DTV_CHANNEL_RELATION);
                ret.add(SearchServiceType.CategoryId.H4D_CATEGORY_HIKARI_TV_VOD);
                ret.add(SearchServiceType.CategoryId.H4D_CATEGORY_DTV_SVOD);
                break;
            case PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
            case PAGE_NO_OF_SERVICE_DTV: //DTV_CONTENTS
            case PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * リクエスト用サービスIDを設定する.
     *
     * @param pageIndex ページ位置
     * @return サービスID
     */
    private ArrayList<String> getCurrentSearchServiceTypeArray(final int pageIndex) {
        ArrayList<String> ret = new ArrayList<>();

        switch (pageIndex) {
            case PAGE_NO_OF_SERVICE_TELEVISION: //テレビ
            case PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
                ret.add(SearchServiceType.ServiceId.HIKARI_TV_FOR_DOCOMO);
                break;
            case PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
                ret.add(SearchServiceType.ServiceId.DTV_CHANNEL_CONTENTS);
                break;
            case PAGE_NO_OF_SERVICE_DTV: //DTV_CONTENTS
                ret.add(SearchServiceType.ServiceId.DTV_CONTENTS);
                break;
            case PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                ret.add(SearchServiceType.ServiceId.D_ANIMATION_CONTENTS);
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * リクエスト用ユーザ年齢情報を設定する.
     *
     * @param context   コンテクストファイル
     * @param pageIndex ページ番号
     * @return 年齢情報
     */
    private int getUserAgeInfo(final Context context, final int pageIndex) {
        UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(context);
        int userAge = -1;

        switch (pageIndex) {
            case PAGE_NO_OF_SERVICE_TELEVISION: //テレビ
            case PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
            case PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
                userAge = userInfoDataProvider.getUserAge();
                break;
            case PAGE_NO_OF_SERVICE_DTV: //DTV_CONTENTS
            case PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                break;
            default:
                break;
        }
        return userAge;
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        cancelSearch();
        if (mTotalSearchWebApi != null) {
            mTotalSearchWebApi.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
    }
}