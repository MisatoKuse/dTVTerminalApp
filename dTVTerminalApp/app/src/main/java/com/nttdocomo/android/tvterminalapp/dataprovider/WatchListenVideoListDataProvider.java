/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoList;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WatchListenVideoWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WatchListenVideoListDataProvider implements WatchListenVideoWebClient.WatchListenVideoJsonParserCallback {

    private Context mContext = null;

    @Override
    public void onWatchListenVideoJsonParsed(List<WatchListenVideoList> watchListenVideoList) {
        if (watchListenVideoList != null && watchListenVideoList.size() > 0) {
            WatchListenVideoList list = watchListenVideoList.get(0);
            sendTvClipListData(list.getVcList());
        } else {
            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.watchListenVideoListCallback(null);
            }
        }
    }

    /**
     * 画面用データを返却するためのコールバック
     */
    public interface WatchListenVideoListProviderCallback {
        /**
         * クリップリスト用コールバック
         *
         * @param clipContentInfo
         */
        void watchListenVideoListCallback(List<ContentsData> clipContentInfo);
    }

    private WatchListenVideoListProviderCallback mApiDataProviderCallback;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public WatchListenVideoListDataProvider(Context context) {
        this.mContext = context;
        this.mApiDataProviderCallback = (WatchListenVideoListProviderCallback) context;
    }

    /**
     * Activityからのデータ取得要求受付
     */
    public void getWatchListenVideoData(int pagerOffset) {
        WatchListenVideoWebClient webClient = new WatchListenVideoWebClient();
        int ageReq = 1;
        int upperPageLimit = 1;
        int lowerPageLimit = 1;
        //int pagerOffset = 1;
        String pagerDirection = "";

        webClient.getWatchListenVideoApi(ageReq, upperPageLimit,
                lowerPageLimit, pagerOffset, pagerDirection, this);
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     *
     * @param mapList コンテンツリストデータ
     */
    private void sendTvClipListData(final List<Map<String, String>> mapList) {
        List<ContentsData> rankingContentsDataList = new ArrayList<>();

        ContentsData rankingContentInfo;

        for (int i = 0; i < mapList.size(); i++) {
            rankingContentInfo = new ContentsData();

            Map<String, String> map = mapList.get(i);
            String title = map.get(JsonContents.META_RESPONSE_TITLE);
            String searchOk = map.get(JsonContents.META_RESPONSE_SEARCH_OK);
            String linearEndDate = map.get(JsonContents.META_RESPONSE_AVAIL_END_DATE);
            String dispType = map.get(JsonContents.META_RESPONSE_DISP_TYPE);
            String dtv = map.get(JsonContents.META_RESPONSE_DTV);
            String dtvType = map.get(JsonContents.META_RESPONSE_DTV_TYPE);

            rankingContentInfo.setRank(String.valueOf(i + 1));
            rankingContentInfo.setThumURL(map.get(JsonContents.META_RESPONSE_THUMB_448));
            rankingContentInfo.setTitle(title);
            rankingContentInfo.setTime(map.get(JsonContents.META_RESPONSE_DISPLAY_START_DATE));
            rankingContentInfo.setSearchOk(searchOk);
            rankingContentInfo.setRank(String.valueOf(i + 1));
            rankingContentInfo.setRatStar(map.get(JsonContents.META_RESPONSE_RATING));
            rankingContentInfo.setContentsType(map.get(JsonContents.META_RESPONSE_CONTENT_TYPE));
            rankingContentInfo.setDtv(dtv);
            rankingContentInfo.setDispType(dispType);
            rankingContentInfo.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();
            requestData.setCrid(map.get(JsonContents.META_RESPONSE_CRID));
            requestData.setServiceId(map.get(JsonContents.META_RESPONSE_SERVICE_ID));
            requestData.setEventId(map.get(JsonContents.META_RESPONSE_EVENT_ID));
            requestData.setTitleId(map.get(JsonContents.META_RESPONSE_TITLE_ID));
            requestData.setTitle(title);
            requestData.setRValue(map.get(JsonContents.META_RESPONSE_R_VALUE));
            requestData.setLinearStartDate(map.get(JsonContents.META_RESPONSE_AVAIL_START_DATE));
            requestData.setLinearEndDate(linearEndDate);
            requestData.setSearchOk(searchOk);

            //視聴通知判定生成
            String contentsType = map.get(JsonContents.META_RESPONSE_CONTENT_TYPE);
            String tvService = map.get(JsonContents.META_RESPONSE_TV_SERVICE);
            String dTv = map.get(JsonContents.META_RESPONSE_DTV);
            requestData.setIsNotify(dispType, contentsType, linearEndDate, tvService, dTv);
            rankingContentInfo.setRequestData(requestData);

            rankingContentsDataList.add(rankingContentInfo);
            DTVTLogger.info("RankingContentInfo " + rankingContentInfo.getRank());
        }

        mApiDataProviderCallback.watchListenVideoListCallback(rankingContentsDataList);
    }
}