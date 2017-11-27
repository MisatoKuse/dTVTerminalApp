/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreListData;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreCountGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreListWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoGenreProvider implements
        GenreListWebClient.GenreListJsonParserCallback,
        GenreCountGetWebClient.GenreCountGetJsonParserCallback {
    private Context mContext;

    private apiGenreListDataProviderCallback mApiGenreListDataProviderCallback = null;

    private VideoGenreListData mVideoGenreListData;

    private List<VideoGenreList> mVideoGenreList = new ArrayList<>();

    public static final String VIDEO_GENRE_KEY_PLALA = "PLALA";
    public static final String VIDEO_GENRE_KEY_NOD = "NOD";
    public static final String VIDEO_GENRE_KEY_ARIB = "ARIB";

    private GenreListMapCallback genreListMapCallback;

    @Override
    public void onGenreListJsonParsed(GenreListResponse genreListResponse) {
        getGenreList(genreListResponse);
    }

    @Override
    public void onGenreCountGetJsonParsed(GenreCountGetResponse genreCountGetResponse) {
        getContent(genreCountGetResponse);
    }

    /**
     * ビデオジャンル一覧画面用データを返却するためのコールバック
     */
    public interface apiGenreListDataProviderCallback {
        /**
         * Listデータコールバック
         *
         * @param listData
         */
        void genreListCallback(List<VideoGenreList> listData);
    }

    /**
     * VideoGenreListDataを返却するためのコールバック
     */
    public interface GenreListMapCallback {
        void genreListMapCallback(VideoGenreListData listData);
    }

    /**
     * ジャンル一覧
     * コンストラクタ
     */
    public VideoGenreProvider(Context mContext) {
        this.mContext = mContext;
        this.mApiGenreListDataProviderCallback = (apiGenreListDataProviderCallback) mContext;
        genreListMapCallback = (GenreListMapCallback) mContext;
    }

    /**
     * ジャンル一覧をVideoTopActivityに送る
     *
     * @param listData
     */
    public void sendGenreListData(List<VideoGenreList> listData) {
        mApiGenreListDataProviderCallback.genreListCallback(listData);
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
     */
    private void getContentCountListData(String genreId) {
        //通信クラスにデータ取得要求を出す
        GenreCountGetWebClient webClient = new GenreCountGetWebClient();
        int limit = 1;
        int offset = 1;
        String filter = "";
        String type = "";
        int ageReq = 1;
        webClient.getGenreCountGetApi(filter, ageReq, genreId, type, this);
    }

    /**
     * ジャンル一覧データをMapに形成する
     *
     * @param genreListResponse
     */
    public void getGenreList(GenreListResponse genreListResponse) {
        Map<String, ArrayList<GenreListMetaData>> listMap = genreListResponse.getTypeList();
        ArrayList<GenreListMetaData> genreMetaDataList = new ArrayList<>();

        //PLALAコンテンツデータをすべて取得
        if (listMap.get(VIDEO_GENRE_KEY_PLALA) != null) {
            genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_PLALA));
        }

        //NODコンテンツデータをすべて取得
        if (listMap.get(VIDEO_GENRE_KEY_NOD) != null) {
            genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_NOD));
        }

        //ARIBコンテンツデータをすべて取得
        if (listMap.get(VIDEO_GENRE_KEY_ARIB) != null) {
            genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_ARIB));
        }

        HashMap<String, String> map = new HashMap<>();
        GenreListMetaData metaData;
        GenreListMetaData.SubContent subContent;
        for (int i = 0; i < genreMetaDataList.size(); i++) {
            // i番目のジャンルのリストを取得する
            metaData = genreMetaDataList.get(i);
            // ジャンルのMap<id, title>を生成する
            map.put(metaData.getId(), metaData.getTitle());
            for (int j = 0; j < metaData.getSubContent().size(); j++) {
                // i番目のサブジャンルのリストを取得する
                subContent = metaData.getSubContent().get(j);
                // サブジャンルのMap<id, title>を生成する
                map.put(subContent.getId(), subContent.getTitle());
            }
        }
        VideoGenreListData videoGenreListData = new VideoGenreListData(map, map);
        genreListMapCallback.genreListMapCallback(videoGenreListData);
        if (genreMetaDataList.size() > 1) {
            //コンテンツデータが複数ある時は、ジャンルIDとサブジャンルIDを関連付けて保存
            for (int i = 0; i < genreMetaDataList.size(); i++) {
                VideoGenreList genreList = new VideoGenreList();
                genreList.setGenreId(genreMetaDataList.get(i).getId());
                genreList.setTitle(genreMetaDataList.get(i).getTitle());
                genreList.setSubGenre(genreMetaDataList.get(i).getSubContent());
                mVideoGenreList.add(genreList);
            }
        } else if (genreMetaDataList.size() > 0) {
            //コンテンツデータが一つの時はジャンルIDとサブジャンルIDを同列に扱う
            VideoGenreList genreList = new VideoGenreList();
            genreList.setGenreId(genreMetaDataList.get(0).getId());
            genreList.setTitle(mContext.getString(R.string.video_content_all_title));
            mVideoGenreList.add(genreList);
            ArrayList<GenreListMetaData.SubContent> list = genreMetaDataList.get(0).getSubContent();
            for (int i = 0; i < list.size(); i++) {
                genreList = new VideoGenreList();
                genreList.setTitle(list.get(i).getTitle());
                genreList.setGenreId(list.get(i).getId());
                mVideoGenreList.add(genreList);
            }
        }

        //TODO:ジャンル毎コンテンツ数取得のリクエストは1回のみ(引数はGenreIdのリストとする
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < mVideoGenreList.size(); i++) {
            arrayList.add(mVideoGenreList.get(i).getGenreId());
            getContentCountDataRequest(mVideoGenreList.get(i).getGenreId());
        }
    }

    /**
     * Activityへ送信するListDataを作成する
     *
     * @param genreCountGetResponse
     */
    public void getContent(GenreCountGetResponse genreCountGetResponse) {
        //TODO:ジャンル、サブジャンル関連データのみのデータクラスを作成し、Activityに送る
        //TODO:ここでは既に取得したジャンル一覧とジャンルごとのコンテンツ数を合わせてActivityに送るのみとする
        //TODO:詳細はVideoGenreListDataクラス参照
        ArrayList<GenreCountGetMetaData> dataArrayList = genreCountGetResponse.getGenreCountGetMetaData();
        if (mVideoGenreList != null && mVideoGenreList.size() > 0) {
            // サブジャンル一覧表示の時
            for (int i = 0; i < mVideoGenreList.size(); i++) {
                String titleGenreId = mVideoGenreList.get(i).getGenreId();
                for (int j = 0; j < dataArrayList.size(); j++) {
                    String contentGenreId = dataArrayList.get(j).getGenreId();
                    if (contentGenreId.equals(titleGenreId)) {
                        mVideoGenreList.get(i).setContentCount(String.valueOf(dataArrayList.get(j).getCount()));
                        break;
                    }
                }
            }
        } else if (mVideoGenreListData != null && dataArrayList != null && dataArrayList.size() > 0) {
            // ジャンル一覧表示の時
            GenreCountGetMetaData metaData;
            HashMap<String, String> contentMap = mVideoGenreListData.getTitleMap();
            String strId = null;
            for (int i = 0; i < dataArrayList.size(); i++) {
                VideoGenreList genreList = new VideoGenreList();
                metaData = dataArrayList.get(i);
                // コンテンツ数を構造体にset
                genreList.setContentCount(String.valueOf(metaData.getCount()));
                strId = metaData.getGenreId();
                // タイトル、ジャンルIDを構造体にset
                genreList.setGenreId(strId);
                genreList.setTitle(contentMap.get(strId));
                mVideoGenreList.add(genreList);
            }
        }
        sendGenreListData(mVideoGenreList);
    }

    /**
     * mVideoGenreListDataの保持
     *
     * @param mVideoGenreListData
     */
    public void setVideoGenreListData(VideoGenreListData mVideoGenreListData) {
        this.mVideoGenreListData = mVideoGenreListData;
    }
}