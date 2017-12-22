/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VIDEO_RANK_LAST_INSERT;

/**
 * ビデオ一覧専用DPクラス
 */
public class VideoContentProvider implements
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback {
    private Context mContext;

    // ビデオコンテンツ画面用コールバック
    private apiVideoContentDataProviderCallback mApiVideoContentDataProviderCallback = null;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public VideoContentProvider(Context mContext) {
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
        mApiVideoContentDataProviderCallback.videoContentCallback(setVideoContentData(list));
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる
     *
     * @param videoContentMapList コンテンツリストデータ
     * @return dataList 読み込み表示フラグ
     */
    private List<ContentsData> setVideoContentData(
            List<Map<String, String>> videoContentMapList) {
        List<ContentsData> videoContentsDataList = new ArrayList<>();

        ContentsData contentsData;

        for (int i = 0; i < videoContentMapList.size(); i++) {
            contentsData = new ContentsData();

            String title = videoContentMapList.get(i).get(JsonContents.META_RESPONSE_TITLE);
            String search = videoContentMapList.get(i).get(JsonContents.META_RESPONSE_SEARCH_OK);
            String linearEndDate = videoContentMapList.get(i).get(JsonContents.META_RESPONSE_AVAIL_END_DATE);

            contentsData.setThumURL(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_THUMB_448));
            contentsData.setTitle(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_TITLE));
            contentsData.setRatStar(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_RATING));

            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();

            requestData.setCrid(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_CRID));
            requestData.setServiceId(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_SERVICE_ID));
            requestData.setEventId(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_EVENT_ID));
            requestData.setTitleId(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_TITLE_ID));
            requestData.setTitle(title);
            requestData.setRValue(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_R_VALUE));
            requestData.setLinearStartDate(videoContentMapList.get(i).get(JsonContents.META_RESPONSE_AVAIL_START_DATE));
            requestData.setLinearEndDate(linearEndDate);
            requestData.setSearchOk(search);
            requestData.setClipTarget(title); //TODO:仕様確認中 現在はトーストにタイトル名を表示することとしています

            //視聴通知判定生成
            String dispType = videoContentMapList.get(i).get(JsonContents.META_RESPONSE_DISP_TYPE);
            String contentsType = videoContentMapList.get(i).get(JsonContents.META_RESPONSE_CONTENT_TYPE);
            String tvService = videoContentMapList.get(i).get(JsonContents.META_RESPONSE_TV_SERVICE);
            String dTv = videoContentMapList.get(i).get(JsonContents.META_RESPONSE_DTV);
            requestData.setIsNotify(dispType, contentsType, linearEndDate, tvService, dTv);

            videoContentsDataList.add(contentsData);
        }

        return videoContentsDataList;
    }

    @Override
    public void onContentsListPerGenreJsonParsed(List<VideoRankList> contentsListPerGenre) {
        if (contentsListPerGenre != null && contentsListPerGenre.size() > 0) {
            VideoRankList list = contentsListPerGenre.get(0);
            sendContentListData(list.getVrList());
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }
}
