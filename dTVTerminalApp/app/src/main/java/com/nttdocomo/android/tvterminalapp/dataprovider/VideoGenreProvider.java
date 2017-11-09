/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoGenreProvider
//        implements GenreListWebClient.GenreListJsonParserCallback
{
    private Context mContext;

    private apiVideoGenreDataProviderCallback mApiVideoGenreDataProviderCallback = null;

    public static final String GENRE_ID_KEY = "genreId";
    public static final String TITLE_ID_KEY = "title";
    public static final String COUNT_ID_KEY = "count";

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public VideoGenreProvider(Context mContext) {
        this.mContext = mContext;
        this.mApiVideoGenreDataProviderCallback = (apiVideoGenreDataProviderCallback) mContext;
    }

//    @Override
//    public void onGenreListJsonParsed(RemoteRecordingReservationListResponse genreListResponse) {
//        if (genreListResponse != null && genreListResponse.size() > 0) {
//            VideoRankList list = genreListResponse.get(0);
//            sendContentCountData(list.getVrList());
//        } else {
//            //TODO:WEBAPIを取得できなかった時の処理を記載予定
//        }
//    }

    /**
     * ビデオコンテンツ一覧画面用データを返却するためのコールバック
     */
    public interface apiVideoGenreDataProviderCallback {
        /**
         * ジャンル一覧用コールバック
         *
         * @param genreMap
         */
        void videoGenreCallback(List<Map<String, String>> genreMap);

        /**
         * コンテンツ数用コールバック
         *
         * @param countMap
         */
        void videoContentCountCallback(List<Map<String, String>> countMap);
    }

    /**
     * ジャンル一覧用
     * VideoContentListActivityからのデータ取得要求受付
     */
    public void getVideoGenreData() {
        // ジャンルリスト
        List<Map<String, String>> videoGenreList = getVideoGenreListData();
        if (videoGenreList != null && videoGenreList.size() > 0) {
            sendGanreListData(videoGenreList);
        }
        // コンテンツ数
        List<Map<String, String>> videoContentCount = getVideoContentCountData();
        if (videoContentCount != null && videoContentCount.size() > 0) {
            sendContentCountData(videoContentCount);
        }
    }

    /**
     * サブジャンル一覧用
     * VideoContentListActivityからのデータ取得要求受付
     */
    public void getVideoGenreData(String genreId) {
        // コンテンツ数
        List<Map<String, String>> videoContentCount = getVideoContentCountData();
        if (videoContentCount != null && videoContentCount.size() > 0) {
            sendContentCountData(videoContentCount);
        }
    }

    /**
     * ビデオジャンル/サブジャンル一覧のデータ取得要求を行う
     *
     * @return
     */
    private List<Map<String, String>> getVideoGenreListData() {
        List<Map<String, String>> list = new ArrayList<>();
        //通信クラスにデータ取得要求を出す
//        GenreListWebClient webClient = new GenreListWebClient();
        int limit = 1;
        int offset = 1;
        String filter = "";
        int ageReq = 1;
        String type = "";
        String sort = "";

//        webClient.getGenreListApi(limit, offset,
//                filter, ageReq, genreId, type, sort, this);

        return list;
    }

    /**
     * コンテンツ数のデータ取得要求を行う
     *
     * @return
     */
    private List<Map<String, String>> getVideoContentCountData() {
        List<Map<String, String>> list = new ArrayList<>();
        // TODO まだサーバーから情報がとれない
        //通信クラスにデータ取得要求を出す
//        ContentsListPerGenreWebClient webClient = new ContentsListPerGenreWebClient();
        int limit = 1;
        int offset = 1;
        String filter = "";
        int ageReq = 1;
        String type = "";
        String sort = "";

//        //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
//        webClient.getContentsListPerGenreApi(limit, offset,
//                filter, ageReq, genreId, type, sort, this);

        return list;
    }

    /**
     * ジャンル一覧をVideoContentListActivityに送る
     *
     * @param list
     */
    public void sendGanreListData(List<Map<String, String>> list) {
        mApiVideoGenreDataProviderCallback.videoGenreCallback(list);
    }

    /**
     * コンテンツ数をVideoContentListActivityに送る
     *
     * @param list
     */
    public void sendContentCountData(List<Map<String, String>> list) {
        mApiVideoGenreDataProviderCallback.videoContentCountCallback(list);
    }

    /**
     * Mapリストの作成(Map<id, title>)
     * @param list
     * @return titleList
     */
    public List<Map<String, String>> getContentTitleMap(List<Map<String, String>> list) {
        // TODO 構造体とMap作成してシングルトンインスタンスに保持

        Map<String, String> mapData = new HashMap<>();

        List<Map<String, String>> titleList = new ArrayList<>();

        int allCount = 0;

        for (int i = 0; i < list.size(); i++) {
            Map<String, String> listMap = list.get(i);
            mapData.put(listMap.get(GENRE_ID_KEY), listMap.get(TITLE_ID_KEY));
        }
        titleList.add(mapData);

        return titleList;
    }

    /**
     * Mapリストの作成(Map<id, count>)
     * @param list
     * @return titleList
     */
    public void getContentCountMap(List<Map<String, String>> list) {
        // TODO 構造体とMap作成してシングルトンインスタンスに保持
        Map<String, String> mapData = new HashMap<>();
        VideoGenreList videoGenreList = new VideoGenreList();
        int count = 0;
        int allCount = 0;

        for (int i = 0; i < list.size(); i++) {
            count = 0;
            Map<String, String> listMap = list.get(i);
            count = Integer.parseInt(listMap.get(COUNT_ID_KEY));
            mapData.put(listMap.get(GENRE_ID_KEY), listMap.get(COUNT_ID_KEY));
            allCount = allCount + count;
        }

        videoGenreList.setMapCountList(mapData);
        // for文を抜けたらすべてのコンテンツ数が加算され終えているのでオブジェクトに格納する。
        // TODO まだ見直し必要
        videoGenreList.setAllContentCount(allCount);
    }
}
