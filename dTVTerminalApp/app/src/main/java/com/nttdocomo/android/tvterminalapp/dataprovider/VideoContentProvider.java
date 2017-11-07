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

    // ビデオコンテンツ用コールバック
    private videoContentApiDataProviderCallback mVideoContentApiDataProviderCallback = null;

    // ジャンル一覧用コールバック
    private videoGerneApiDataProviderCallback mVideoGerneApiDataProviderCallback = null;

//    // コンテンツ数用コールバック
//    private videoContentCountProviderCallback mVideoContentCountProviderCallback = null;

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
         * @param videoContentList
         */
        void videoContentApiDataProviderCallback(List<Map<String, String>> videoContentList);
    }

    /**
     * ジャンル一覧用コールバック
     */
    public interface videoGerneApiDataProviderCallback {
        /**
         * ジャンル一覧用コールバック
         *
         * @param genreList
         */
        void videoGerneApiDataProviderCallback(List<Map<String, String>> genreList);
    }
//
//    /**
//     * コンテンツ数用コールバック
//     */
//    public interface videoContentCountProviderCallback {
//        /**
//         * コンテンツ数用コールバック
//         *
//         * @param contentCountList
//         */
//        void videoContentCountProviderCallback(List<Map<String, String>> contentCountList);
//    }

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
     * VideoContentListActivityからのデータ取得要求
     *
     * @param genreId
     */
    public void getVideoData(String genreId) {
        // ビデオコンテンツ一覧
        List<Map<String, String>> contentList = getVideoContentListData(genreId);
        if (contentList != null && contentList.size() > 0) {
            sendVideoContentListData(contentList, genreId);
        }
        // ビデオジャンル一覧
        List<Map<String, String>> videoGenreList = getVideoContentListData(genreId);
        if (contentList != null && contentList.size() > 0) {
            sendVideoContentListData(contentList, genreId);
        }

        // ビデオコンテンツ数
        List<Map<String, String>> contentCount = getVideoContentListData(genreId);
        if (contentList != null && contentList.size() > 0) {
            sendVideoContentListData(contentList, genreId);
        }
    }

    /**
     * ビデオランキングリストをVideoRankingActivityに送る
     */
    public void sendVideoContentListData(List<Map<String, String>> list, String genreId) {
        DTVTLogger.start("response genreId : " + genreId);
        // TODO ジャンルID毎にコールバックを返す
        switch (genreId) {
            case "":
                mVideoContentApiDataProviderCallback.videoContentApiDataProviderCallback(list);
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }


    /**
     * ビデオラコンテンツ一覧データ取得要求を行う
     *
     * @return
     */
    private List<Map<String, String>> getVideoContentListData(String genreId) {
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

    /**
     * ビデオジャンル一覧のデータ取得要求を行う
     *
     * @return
     */
    private List<Map<String, String>> getVideoGenreListData(String genreId) {
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

    /**
     * ビデオコンテンツ数データ取得要求を行う
     *
     * @return
     */
    private List<Map<String, String>> getVideoContentCountListData(String genreId) {
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
