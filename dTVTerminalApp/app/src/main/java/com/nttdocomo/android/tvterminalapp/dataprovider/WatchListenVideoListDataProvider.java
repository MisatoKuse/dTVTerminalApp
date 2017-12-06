/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoContentInfo;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WatchListenVideoWebClient;

import java.util.List;
import java.util.Map;


public class WatchListenVideoListDataProvider implements WatchListenVideoWebClient.WatchListenVideoJsonParserCallback {
    private Context mContext;

    @Override
    public void onWatchListenVideoJsonParsed(List<WatchListenVideoList> watchListenVideoList) {
        if (watchListenVideoList != null && watchListenVideoList.size() > 0) {
            WatchListenVideoList list = watchListenVideoList.get(0);
            sendTvClipListData(list.getVcList());
        } else {
            if ( null != mApiDataProviderCallback){
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
        void watchListenVideoListCallback(WatchListenVideoContentInfo clipContentInfo);
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
     * TvクリップリストをActivityに送る
     *
     * @param list
     */
    public void sendTvClipListData(List<Map<String, String>> list) {

        WatchListenVideoContentInfo clipContentInfo = new WatchListenVideoContentInfo();
        String title = "";
        String ratingValue = "";
        String picUrl = "";
        WatchListenVideoContentInfo tmpClipContentInfo = new WatchListenVideoContentInfo();

        for (int i = 0; i < list.size(); i++) {
            title = list.get(i).get(JsonContents.META_RESPONSE_TITLE);
            ratingValue = list.get(i).get(JsonContents.META_RESPONSE_R_VALUE);
            picUrl = list.get(i).get(JsonContents.META_RESPONSE_THUMB_448);

            WatchListenVideoContentInfo.WatchListenVideoContentInfoItem item=tmpClipContentInfo.new WatchListenVideoContentInfoItem(picUrl, title, ratingValue);
            clipContentInfo.add(item);
        }

        mApiDataProviderCallback.watchListenVideoListCallback(clipContentInfo);
    }

}
