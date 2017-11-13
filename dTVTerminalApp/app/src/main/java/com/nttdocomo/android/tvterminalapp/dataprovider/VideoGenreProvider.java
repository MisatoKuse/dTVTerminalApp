/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreCountGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreListWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoGenreProvider implements
        GenreListWebClient.GenreListJsonParserCallback,
        GenreCountGetWebClient.GenreCountGetJsonParserCallback{
    private Context mContext;

    private apiGenreListDataProviderCallback mApiGenreListDataProviderCallback = null;
    private apiContentCountDataProviderCallback mApiContentCountDataProviderCallback = null;

    @Override
    public void onGenreListJsonParsed(GenreListResponse genreListResponse) {

    }

    @Override
    public void onGenreCountGetJsonParsed(GenreCountGetResponse genreCountGetResponse) {

    }

    /**
     * ジャンル一覧用データを返却するためのコールバック
     */
    public interface apiGenreListDataProviderCallback {
        /**
         * ジャンル一覧用コールバック
         *
         * @param genreHashMap
         */
        void genreListCallback(List<Map<String, String>> genreHashMap);
    }

    /**
     * コンテンツ数用データを返却するためのコールバック
     */
    public interface apiContentCountDataProviderCallback {
        /**
         * コンテンツ数用コールバック
         *
         * @param countHashMap
         */
        void contentCountCallback(List<Map<String, String>> countHashMap);
    }

    /**
     * ジャンル一覧
     * コンストラクタ
     */
    public VideoGenreProvider(Context mContext) {
        this.mContext = mContext;
        this.mApiGenreListDataProviderCallback = (apiGenreListDataProviderCallback) mContext;
    }

    /**
     * コンテンツ数
     * コンストラクタ
     */
    public VideoGenreProvider(Context mContext, String genreId) {
        this.mContext = mContext;
        this.mApiContentCountDataProviderCallback = (apiContentCountDataProviderCallback) mContext;
    }

    /**
     * ジャンル一覧をVideoTopActivityに送る
     *
     * @param list
     */
    public void sendGenreListData(List<Map<String, String>> list) {
        mApiGenreListDataProviderCallback.genreListCallback(list);
    }

    /**
     * コンテンツ数をVideoTopActivityに送る
     *
     * @param list
     */
    public void sendContentCountListData(List<Map<String, String>> list) {
        mApiContentCountDataProviderCallback.contentCountCallback(list);
    }

    /**
     * ジャンル一覧
     * VideoTopActivityからのデータ取得要求受付
     */
    public void getGenreListDataRequest() {
        List<Map<String, String>> genreList = getGenreListData();
        if (genreList != null && genreList.size() > 0) {
            sendGenreListData(genreList);
        }
    }

    /**
     * コンテンツ数
     * VideoTopActivityからのデータ取得要求受付
     */
    public void getContentCountDataRequest(String genreId) {
        List<Map<String, String>> contentCountList = getContentCountListData(genreId);
        if (contentCountList != null && contentCountList.size() > 0) {
            sendContentCountListData(contentCountList);
        }
    }





    /**
     * ジャンル一覧のデータ取得要求を行う
     *
     * @return
     */
    private List<Map<String, String>> getGenreListData() {
        List<Map<String, String>> list = new ArrayList<>();
        //通信クラスにデータ取得要求を出す
        // TODO WebAPI
        GenreListWebClient webClient = new GenreListWebClient();
        int limit = 1;
        int offset = 1;
        String filter = "";
        int ageReq = 1;
        webClient.getGenreListApi(this);
        return list;
    }

    /**
     * コンテンツ数のデータ取得要求を行う
     *
     * @param genreId
     * @return
     */
    private List getContentCountListData(String genreId) {
        List<Map<String, String>> list = new ArrayList<>();
        //通信クラスにデータ取得要求を出す
        // TODO WebAPI
        GenreCountGetWebClient webClient = new GenreCountGetWebClient();
        int limit = 1;
        int offset = 1;
        String filter = "";
        String type = "";
        int ageReq = 1;
        webClient.getGenreCountGetApi(filter, ageReq, genreId, type, this);
        return list;
    }

    /**
     * Mapリストの作成(Map<id, title>)
     *
     * @param genreList
     * @return titleList
     */
    public List<Map<String, String>> getContentTitleMap(List<Map<String, String>> genreList) {
        VideoGenreList videoGenreList = new VideoGenreList();
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> titleList = new ArrayList<>();
        for (int i = 0; i < genreList.size(); i++) {
            String id = genreList.get(i).get("genre_id");
            String title = genreList.get(i).get("title");
            map.put(id, title);
        }
        titleList.add(map);
//        videoGenreList.setTitleList(titleList);
        return titleList;
    }

    /**
     * Mapリストの作成(Map<id, count>)
     *
     * @param countList
     * @return titleList
     */
    public List<Map<String, String>> getContentCountMap(List<Map<String, String>> countList) {
        VideoGenreList videoGenreList = new VideoGenreList();
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> countLst = new ArrayList<>();
        int allCount = 0;
        for (int i = 0; i < countList.size(); i++) {
            String id = countList.get(i).get("genre_id");
            String count = countList.get(i).get("count");
            map.put(id, count);
            // [すべて]のコンテンツ数に追加していく
            allCount = allCount + Integer.parseInt(count);
        }
        countLst.add(map);
//        videoGenreList.setAllContentCount(String.valueOf(allCount));
//        videoGenreList.setCountList(countLst);
        return countLst;
    }
}