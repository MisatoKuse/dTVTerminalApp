/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingConstants;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideoContentProvider implements
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback {

    private Context mContext;
    // ビデオコンテンツリスト用コールバック
    private videoContentApiDataProviderCallback mVideoContentApiDataProviderCallback = null;

    @Override
    public void onContentsListPerGenreJsonParsed(List<VideoRankList> contentsListPerGenre) {
        if (contentsListPerGenre != null && contentsListPerGenre.size() > 0) {
            VideoRankList list = contentsListPerGenre.get(0);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    /**
     * ビデオコンテンツリスト用のコールバック
     */
    public interface videoContentApiDataProviderCallback {
        /**
         * ビデオコンテンツ一覧用コールバック
         *
         * @param videoContentCallback
         */
        void videoContentCallback(List<Map<String, String>> videoContentCallback);
    }

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public VideoContentProvider(Context mContext) {
        this.mContext = mContext;
        this.mVideoContentApiDataProviderCallback = (videoContentApiDataProviderCallback) mContext;
    }

    /**
     * RankingTopActivityからのデータ取得要求受付
     */
    public void getRankingTopData(String genreId) {
        //ビデオのランキング
        List<Map<String, String>> videoRankList = getVideoRankListData(RankingConstants.RANKING_GENRE_ID_SYNTHESIS);
        if (videoRankList != null && videoRankList.size() > 0) {
            sendVideoGenreRankListData(videoRankList, genreId);
        }
    }

    /**
     * VideoContentListActivityからのデータ取得要求
     * @param genreId
     */
    public void getVideoRankingData(String genreId) {
        List<Map<String, String>> videoRankList = getVideoRankListData(genreId);
        if (videoRankList != null && videoRankList.size() > 0) {
            sendVideoGenreRankListData(videoRankList, genreId);
        }
    }

    /**
     * ビデオランキングリストをVideoRankingActivityに送る
     */
    public void sendVideoGenreRankListData(List<Map<String, String>> list, String genreId) {
        DTVTLogger.start("response genreId : " + genreId);
        // TODO ジャンルID毎にコールバックを返す
        switch (genreId) {
            case "":
                mVideoContentApiDataProviderCallback.videoContentCallback(list);
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }


    /**
     * ビデオランキングのデータ取得要求を行う
     * @return
     */
    private List<Map<String, String>> getVideoRankListData(String genreId) {
        DateUtils dateUtils = new DateUtils(mContext);
        List<Map<String, String>> list = new ArrayList<>();
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
        return list;
    }

}
