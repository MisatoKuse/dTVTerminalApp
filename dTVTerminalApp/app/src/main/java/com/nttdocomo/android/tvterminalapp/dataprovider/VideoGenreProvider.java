/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreCountGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreListWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoGenreProvider implements
        GenreListWebClient.GenreListJsonParserCallback,
        GenreCountGetWebClient.GenreCountGetJsonParserCallback {
    private Context mContext;

    private apiGenreListDataProviderCallback mApiGenreListDataProviderCallback = null;
    private apiContentCountDataProviderCallback mApiContentCountDataProviderCallback = null;

    @Override
    public void onGenreListJsonParsed(GenreListResponse genreListResponse) {
        getContentTitleMap(genreListResponse);
    }

    @Override
    public void onGenreCountGetJsonParsed(GenreCountGetResponse genreCountGetResponse) {
        getContentCountMap(genreCountGetResponse);
    }

    /**
     * ジャンル一覧用データを返却するためのコールバック
     */
    public interface apiGenreListDataProviderCallback {
        /**
         * ジャンル一覧用コールバック
         *
         * @param titleMap, subTitleMap
         */
        void genreListCallback(Map<String, String> titleMap, Map<String, String> subTitleMap);
    }

    /**
     * コンテンツ数用データを返却するためのコールバック
     */
    public interface apiContentCountDataProviderCallback {
        /**
         * コンテンツ数用コールバック
         *
         * @param mapData
         */
        void contentCountCallback(Map<String, String> mapData);
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
     * @param titleMap, subTitleMap
     */
    public void sendGenreListData(Map<String, String> titleMap, Map<String, String> subTitleMap) {
        mApiGenreListDataProviderCallback.genreListCallback(titleMap, subTitleMap);
    }

    /**
     * コンテンツ数をVideoTopActivityに送る
     *
     * @param mapData
     */
    public void sendContentCountListData(Map<String, String> mapData) {
        mApiContentCountDataProviderCallback.contentCountCallback(mapData);
    }

    /**
     * ジャンル一覧
     * VideoTopActivityからのデータ取得要求受付
     */
    public void getGenreListDataRequest() {
        getGenreListData();
    }

    /**
     * コンテンツ数
     * VideoTopActivityからのデータ取得要求受付
     */
    public void getContentCountDataRequest(String genreId) {
        getContentCountListData(genreId);
    }

    /**
     * ジャンル一覧のデータ取得要求を行う
     *
     * @return
     */
    private void getGenreListData() {
        GenreListWebClient webClient = new GenreListWebClient();
        int limit = 1;
        int offset = 1;
        String filter = "";
        int ageReq = 1;
        webClient.getGenreListApi(this);
    }

    /**
     * コンテンツ数のデータ取得要求を行う
     *
     * @param genreId
     * @return
     */
    private void getContentCountListData(String genreId) {
        //通信クラスにデータ取得要求を出す
        GenreCountGetWebClient webClient = new GenreCountGetWebClient();
        String filter = "release";
        String type = "";
        int ageReq = 1;
        webClient.getGenreCountGetApi(filter, ageReq, genreId, type, this);
    }

    /**
     * Mapリストの作成(Map<id, title>)
     *
     * @param genreListResponse
     */
    public void getContentTitleMap(GenreListResponse genreListResponse) {
        Map<String, String> titleMap = new HashMap<>();
        Map<String, String> subMap = new HashMap<>();
        GenreListMetaData genreListMetaData = new GenreListMetaData();
        ArrayList<GenreListMetaData> listData = genreListResponse.getTypeList().get("PLALA");

        for (int i = 0; i > listData.size(); i++) {
            String genreId = String.valueOf(listData.get(i).getMember(genreListMetaData.GENRE_LIST_META_DATA_ID));
            String title = String.valueOf(listData.get(i).getMember(genreListMetaData.GENRE_LIST_META_DATA_TITLE));
            titleMap.put(genreId, title);

//            ArrayList<GenreListMetaData.SubContent> sub = (ArrayList<GenreListMetaData.SubContent>)
//                    listData.get(i).getMember(genreListMetaData.GENRE_LIST_META_DATA_SUB);
//
//            String subGenreId = sub.get(i).getId();
//            String subTitle = sub.get(i).getTitle();
//
//            subMap.put(subGenreId, subTitle);
        }
        sendGenreListData(titleMap, subMap);
    }

    /**
     * Mapリストの作成(Map<id, count>)
     *
     * @param genreCountGetResponse
     */
    public void getContentCountMap(GenreCountGetResponse genreCountGetResponse) {
        Map<String, String> map = new HashMap<>();
        ArrayList<GenreCountGetMetaData> responseData = genreCountGetResponse.getGenreCountGetMetaData();
        for (int i = 0; i > responseData.size(); i++) {
            String genreId = responseData.get(i).getGenreId();

            // TODO IDが同じものならmapにPut
//            if (genreId == ) {
                String count = String.valueOf(responseData.get(i).getCount());
                map.put(genreId, count);
//            } else {
                // IDが違うならmapに格納しない
//            }
        }
        sendContentCountListData(map);
    }
}