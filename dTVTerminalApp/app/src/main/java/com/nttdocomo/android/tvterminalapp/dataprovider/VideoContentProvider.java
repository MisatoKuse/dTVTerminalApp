/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ビデオ一覧専用DPクラス
 */
public class VideoContentProvider  extends  ClipKeyListDataProvider implements
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback {

    private Context mContext = null;

    // ビデオコンテンツ画面用コールバック
    private apiVideoContentDataProviderCallback mApiVideoContentDataProviderCallback = null;
    private List<Map<String, String>> videoContentMapList;
    private VideoRankList mVideoRankList = null;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public VideoContentProvider(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mApiVideoContentDataProviderCallback = (apiVideoContentDataProviderCallback) mContext;
    }

    /**
     * ビデオコンテンツ一覧画面用データを返却するためのコールバック
     */
    public interface apiVideoContentDataProviderCallback {
        /**
         * ビデオコンテンツ一覧用コールバック
         *
         * @param videoHashMap
         */
        void videoContentCallback(List<ContentsData> videoHashMap);
    }

    /**
     * VideoContentListActivityからのデータ取得要求受付
     */
    public void getVideoContentData(String genreId) {
        mVideoRankList = null;
        if(mRequiredClipKeyList) {
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
        }
        // コンテンツ数
        getVideoContentListData(genreId);
    }

    /**
     * ビデオコンテンツ一覧のデータ取得要求を行う
     *
     * @return
     */
    private void getVideoContentListData(String genreId) {
        //通信クラスにデータ取得要求を出す
        ContentsListPerGenreWebClient webClient = new ContentsListPerGenreWebClient();
        int limit = 1;
        int offset = 1;
        String filter = "";
        int ageReq = 1;
        String type = "";
        String sort = "";

        //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
        webClient.getContentsListPerGenreApi(limit, offset,
                filter, ageReq, genreId, type, sort, this);
    }

    /**
     * ビデオコンテンツ一覧をVideoContentListActivityに送る
     *
     * @param list
     */
    public void sendContentListData(List<Map<String, String>> list) {
        List<ContentsData> contentsDataList = setVideoContentData(list);
        mApiVideoContentDataProviderCallback.videoContentCallback(contentsDataList);
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる
     *
     * @param videoContentMapList コンテンツリストデータ
     * @return dataList 読み込み表示フラグ
     */
    private List<ContentsData> setVideoContentData(List<Map<String, String>> videoContentMapList) {
        this.videoContentMapList = videoContentMapList;
        List<ContentsData> videoContentsDataList = new ArrayList<>();

        ContentsData contentsData;

        for (int i = 0; i < videoContentMapList.size(); i++) {
            contentsData = new ContentsData();

            Map<String, String> map = videoContentMapList.get(i);
            String title = map.get(JsonContents.META_RESPONSE_TITLE);
            String searchOk = map.get(JsonContents.META_RESPONSE_SEARCH_OK);
            String linearEndDate = map.get(JsonContents.META_RESPONSE_AVAIL_END_DATE);
            String dispType = map.get(JsonContents.META_RESPONSE_DISP_TYPE);
            String dtv = map.get(JsonContents.META_RESPONSE_DTV);
            String dtvType = map.get(JsonContents.META_RESPONSE_DTV_TYPE);

            contentsData.setThumURL(map.get(JsonContents.META_RESPONSE_THUMB_448));
            contentsData.setTitle(map.get(JsonContents.META_RESPONSE_TITLE));
            contentsData.setRatStar(map.get(JsonContents.META_RESPONSE_RATING));
            contentsData.setContentsType(map.get(JsonContents.META_RESPONSE_CONTENT_TYPE));
            contentsData.setDtv(dtv);
            contentsData.setDispType(dispType);
            contentsData.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
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
            contentsData.setRequestData(requestData);

            if(mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                contentsData.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        contentsData.getCrid(), contentsData.getServiceId(),
                        contentsData.getEventId(), contentsData.getContentsType(),
                        contentsData.getTitleId()));
            }

            videoContentsDataList.add(contentsData);
        }

        return videoContentsDataList;
    }

    @Override
    public void onContentsListPerGenreJsonParsed(List<VideoRankList> contentsListPerGenre) {
        if (contentsListPerGenre != null && contentsListPerGenre.size() > 0) {
            VideoRankList list = contentsListPerGenre.get(0);
            if (!mRequiredClipKeyList
                    || mResponse != null) {
                sendContentListData(list.getVrList());
            } else {
                mVideoRankList = list;
            }
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onTvClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onTvClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        if(mVideoRankList != null) {
            sendContentListData(mVideoRankList.getVrList());
        }
        DTVTLogger.end();
    }
}