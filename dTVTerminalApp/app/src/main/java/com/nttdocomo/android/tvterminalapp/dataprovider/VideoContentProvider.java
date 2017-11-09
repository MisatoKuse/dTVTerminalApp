/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RankingTopDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingConstants;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static final String SUB_GENRE_KEY = "sub";

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
    public void getVideoContentData() {
            // コンテンツ数
            List<Map<String, String>> videoContentCount = getVideoContentListData(SUB_GENRE_KEY);
            if (videoContentCount != null && videoContentCount.size() > 0) {
                getVideoContentListData(SUB_GENRE_KEY);
            }
    }

    /**
     * ビデオコンテンツ一覧のデータ取得要求を行う
     * @return
     */
    private List<Map<String, String>> getVideoContentListData(String genreId) {
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
