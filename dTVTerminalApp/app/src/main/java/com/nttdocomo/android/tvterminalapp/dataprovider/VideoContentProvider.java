/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;

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
        void videoContentCallback(List<Map<String, String>> videoHashMap);
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
        mApiVideoContentDataProviderCallback.videoContentCallback(list);
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
