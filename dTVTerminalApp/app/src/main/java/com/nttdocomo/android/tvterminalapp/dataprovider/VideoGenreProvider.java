/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.WeeklyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.callback.VideoRankingApiDataProviderCallback;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
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
        GenreCountGetWebClient.GenreCountGetJsonParserCallback {

    private apiGenreListDataProviderCallback mApiGenreListDataProviderCallback = null;

    private static final String VIDEO_GENRE_KEY_IPTV = "IPTV";
    private static final String VIDEO_GENRE_KEY_NOD = "NOD";
    private static final String VIDEO_GENRE_KEY_ARIB = "ARIB";
    private Context mContext = null;

    private GenreListMapCallback genreListMapCallback;

    private RankGenreListCallback mRankGenreListCallback;

    private ContentsAdapter.ActivityTypeItem type;

    @Override
    public void onGenreListJsonParsed(GenreListResponse genreListResponse) {
        if(mRankGenreListCallback != null){
            setRankGenreListData(genreListResponse);
        } else {
            getGenreList(genreListResponse);
        }
    }

    @Override
    public void onGenreCountGetJsonParsed(GenreCountGetResponse genreCountGetResponse) {
        mApiGenreListDataProviderCallback.genreListCallback(genreCountGetResponse.getGenreCountGetMetaData());
    }

    /**
     * ビデオジャンル一覧画面用データを返却するためのコールバック
     */
    public interface apiGenreListDataProviderCallback {
        /**
         * Listデータコールバック
         *
         * @param listData ジャンルのコンテンツ数一覧
         */
        void genreListCallback(List<GenreCountGetMetaData> listData);
    }

    /**
     * ジャンルIDをキー値としたジャンルデータMapを返却するためのコールバック
     */
    public interface GenreListMapCallback {
        void genreListMapCallback(Map<String, VideoGenreList> map, List<String> firstGenreIdList);
    }

    /**
     * ジャンルリストデータを返却するためのコールバック
     */
    public interface RankGenreListCallback {
        void onRankGenreListCallback(ArrayList<GenreListMetaData> genreMetaDataList);
    }

    /**
     * ジャンル一覧
     * コンストラクタ
     */
    public VideoGenreProvider(Context context) {
        this.mApiGenreListDataProviderCallback = (apiGenreListDataProviderCallback) context;
        genreListMapCallback = (GenreListMapCallback) context;
        mContext = context;
    }

    public VideoGenreProvider(Context context, RankGenreListCallback mRankGenreListCallback, ContentsAdapter.ActivityTypeItem type) {
        this.type = type;
        this.mRankGenreListCallback = mRankGenreListCallback;
        mContext = context;
    }

    /**
     * ジャンル一覧
     * VideoTopActivityからのデータ取得要求受付
     */
    public void getGenreListDataRequest() {
        getGenreListData();
    }

    /**
     * ジャンル一覧のデータ取得要求を行う
     */
    private void getGenreListData() {
        // TODO 仮実装 IF仕様が固まったらパラメータを送るように修正
        GenreListWebClient webClient = new GenreListWebClient(mContext);
        int limit = 1;
        int offset = 1;
        String filter = "";
        int ageReq = 1;
        webClient.getGenreListApi(this);
    }

    /**
     * コンテンツ数のデータ取得要求を行う
     *
     * @param genreId ジャンルIDリスト
     */
    public void getContentCountListData(List<String> genreId) {
        //通信クラスにデータ取得要求を出す
        GenreCountGetWebClient webClient = new GenreCountGetWebClient(mContext);
        int limit = 1;
        int offset = 1;
        String filter = "";
        String type = "";
        int ageReq = 1;
        webClient.getGenreCountGetApi(filter, ageReq, genreId, type, this);
    }

    /**
     * ジャンル一覧データをリストに形成する
     *
     * @param genreListResponse ジャンル一覧APIからのレスポンス
     */
    private void setRankGenreListData(GenreListResponse genreListResponse){
        try {
            Map<String, ArrayList<GenreListMetaData>> listMap = genreListResponse.getTypeList();
            if(listMap == null){
                DTVTLogger.error("response is null");
                mRankGenreListCallback.onRankGenreListCallback(null);
            } else {
                if(mContext != null){
                    ArrayList<GenreListMetaData> genreMetaDataList = new ArrayList<>();
                    GenreListMetaData genreAll = new GenreListMetaData();
                    genreAll.setTitle(mContext.getResources().getString(R.string.common_ranking_tab_all));
                    genreAll.setId("");
                    genreMetaDataList.add(genreAll);
                    if (ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK.equals(type)) {
                        //IPTVコンテンツデータをすべて取得
                        if (listMap.get(VIDEO_GENRE_KEY_IPTV) != null) {
                            genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_IPTV));
                        } else {
                            DTVTLogger.error("IPTV is not found");
                        }
                    } else if (ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK.equals(type)) {
                        //ARIBコンテンツデータをすべて取得
                        if (listMap.get(VIDEO_GENRE_KEY_ARIB) != null) {
                            genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_ARIB));
                        } else {
                            DTVTLogger.error("ARIB is not found");
                        }
                    } else {
                        DTVTLogger.error("activity is not found");
                    }
                    mRankGenreListCallback.onRankGenreListCallback(genreMetaDataList);
                } else {
                    DTVTLogger.error("context is null");
                    mRankGenreListCallback.onRankGenreListCallback(null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            DTVTLogger.error("response genreListData error");
        }
    }

    /**
     * ジャンル一覧データをMapに形成する
     *
     * @param genreListResponse ジャンル一覧APIからのレスポンス
     */
    private void getGenreList(GenreListResponse genreListResponse) {
        Map<String, ArrayList<GenreListMetaData>> listMap = new HashMap<>();
        ArrayList<GenreListMetaData> genreMetaDataList = new ArrayList<>();
        try {
            listMap = genreListResponse.getTypeList();
        } catch (Exception e) {
            DTVTLogger.error("response is null");
        }
        //IPTVコンテンツデータをすべて取得
        if (listMap.get(VIDEO_GENRE_KEY_IPTV) != null) {
            GenreListMetaData genreAll = new GenreListMetaData();
            genreAll.setTitle(mContext.getResources().getString(R.string.video_list_genre_all));
            genreAll.setId(GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS);
            genreMetaDataList.add(genreAll);
            genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_IPTV));
        }

        //NODコンテンツデータをすべて取得
        if (listMap.get(VIDEO_GENRE_KEY_NOD) != null) {
            GenreListMetaData firstNodData = new GenreListMetaData();
            firstNodData.setTitle(mContext.getResources().getString(R.string.video_list_genre_nod));
            firstNodData.setId(GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD);
            firstNodData.setSubContentAll(listMap.get(VIDEO_GENRE_KEY_NOD));
            genreMetaDataList.add(firstNodData);
        }

        //ARIBコンテンツデータをすべて取得
        if (listMap.get(VIDEO_GENRE_KEY_ARIB) != null) {
            genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_ARIB));
        }

        // 初期画面のジャンルIDを設定
        List<String> firstPageGenreIdList = new ArrayList<String>();
        Map<String, VideoGenreList> videoGenreListMap = new HashMap<String, VideoGenreList>();
        for (int i = 0; i < genreMetaDataList.size(); i++) {
            firstPageGenreIdList.add(genreMetaDataList.get(i).getId());
            videoGenreListMap = setVideoGenreList(genreMetaDataList.get(i), videoGenreListMap);
        }
        genreListMapCallback.genreListMapCallback(videoGenreListMap, firstPageGenreIdList);
    }

    /**
     * ジャンルIDをKey値としたジャンルデータMapを生成
     * 親ジャンル/サブジャンルのデータをMapに設定する
     *
     * @param metaData
     * @param listMap
     * @return
     */
    private Map<String, VideoGenreList> setVideoGenreList(GenreListMetaData metaData, Map<String, VideoGenreList> listMap) {
        VideoGenreList videoGenreList = new VideoGenreList();
        videoGenreList.setTitle(metaData.getTitle());
        videoGenreList.setGenreId(metaData.getId());
        videoGenreList.setRValue(metaData.getRValue());

        // サブジャンルがあるかどうかの判定
        if (metaData.getSubContent() != null &&
                metaData.getSubContent().size() != 0) {
            // サブジャンルがある場合
            for (GenreListMetaData data : metaData.getSubContent()) {
                // サブジャンルのジャンルIDをリストに格納
                videoGenreList.addSubGenreList(data.getId());
                // サブジャンルのジャンルデータをMapへ設定する繰り返し処理
                listMap = setVideoGenreList(data, listMap);
            }
        }
        listMap.put(videoGenreList.getGenreId(), videoGenreList);

        return listMap;
    }
}