/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
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
    private List<VideoGenreList> mVideoGenreList = new ArrayList<>();

    private static final String VIDEO_GENRE_KEY_PLALA = "PLALA";
    private static final String VIDEO_GENRE_KEY_NOD = "NOD";
    private static final String VIDEO_GENRE_KEY_ARIB = "ARIB";

    private GenreListMapCallback genreListMapCallback;

    @Override
    public void onGenreListJsonParsed(GenreListResponse genreListResponse) {
        getGenreList(genreListResponse);
    }

    @Override
    public void onGenreCountGetJsonParsed(GenreCountGetResponse genreCountGetResponse) {
//        getContent(genreCountGetResponse);
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
//        void genreListCallback(List<VideoGenreList> listData);
        void genreListCallback(List<GenreCountGetMetaData> listData);
    }

    /**
     * ジャンルIDをキー値としたジャンルデータMapを返却するためのコールバック
     */
    public interface GenreListMapCallback {
        void genreListMapCallback(Map<String, VideoGenreList> map, List<String> firstGenreIdList);
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
     * @param listData ジャンル一覧
     */
    private void sendGenreListData(List<VideoGenreList> listData) {
//        mApiGenreListDataProviderCallback.genreListCallback(listData);
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
    public void getContentCountDataRequest(List<String> genreId) {
        getContentCountListData(genreId);
    }

    /**
     * ジャンル一覧のデータ取得要求を行う
     */
    private void getGenreListData() {
        // TODO 仮実装 IF仕様が固まったらパラメータを送るように修正
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
     * @param genreId ジャンルIDリスト
     */
    public void getContentCountListData(List<String> genreId) {
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
     * @param genreListResponse ジャンル一覧APIからのレスポンス
     */
    private void getGenreList(GenreListResponse genreListResponse) {
        Map<String, ArrayList<GenreListMetaData>> listMap = new HashMap<>();
        ArrayList<GenreListMetaData> genreMetaDataList = new ArrayList<>();
        try {
            listMap = genreListResponse.getTypeList();
        } catch (Exception e){
            DTVTLogger.error("response is null");
        }
        //PLALAコンテンツデータをすべて取得
        if (listMap.get(VIDEO_GENRE_KEY_PLALA) != null) {
            genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_PLALA));
        }

        //NODコンテンツデータをすべて取得
        if (listMap.get(VIDEO_GENRE_KEY_NOD) != null) {
            GenreListMetaData firstNodData = new GenreListMetaData();
            firstNodData.setTitle(GenreListMetaData.VIDEO_LIST_TITLE_NOD);
            firstNodData.setId(GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD);
            firstNodData.setSubContentAll(listMap.get(VIDEO_GENRE_KEY_NOD));
            genreMetaDataList.add(firstNodData);
        }

        //ARIBコンテンツデータをすべて取得
        if (listMap.get(VIDEO_GENRE_KEY_ARIB) != null) {
            genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_ARIB));
        }

//        HashMap<String, String> map = new HashMap<>();
//        GenreListMetaData metaData;
//        GenreListMetaData subContent;
//        for (int i = 0; i < genreMetaDataList.size(); i++) {
//            // i番目のジャンルのリストを取得する
//            metaData = genreMetaDataList.get(i);
//            // ジャンルのMap<id, title>を生成する
//            map.put(metaData.getId(), metaData.getTitle());
//            for (int j = 0; j < metaData.getSubContent().size(); j++) {
//                // i番目のサブジャンルのリストを取得する
//                subContent = metaData.getSubContent().get(j);
//                // サブジャンルのMap<id, title>を生成する
//                map.put(subContent.getId(), subContent.getTitle());
//            }
//        }
//        VideoGenreListData videoGenreListData = new VideoGenreListData(map);
//        genreListMapCallback.genreListMapCallback(videoGenreListData);
        // 初期画面のジャンルIDを設定
        List<String> firstPageGenreIdList = new ArrayList<String>();
        Map<String, VideoGenreList> videoGenreListMap = new HashMap<String, VideoGenreList>();
        for(int i=0; i < genreMetaDataList.size(); i++) {
            firstPageGenreIdList.add(genreMetaDataList.get(i).getId());
            videoGenreListMap = setVideoGenreList(genreMetaDataList.get(i), videoGenreListMap);
        }
        genreListMapCallback.genreListMapCallback(videoGenreListMap, firstPageGenreIdList);
//        mVideoGenreList = setVideoGenreList(genreMetaDataList);
//        contentsCountRequest(genreMetaDataList);
    }

    /**
     * サブジャンルあり/なしそれぞれの場合でのデータを作成する
     * ジャンルごとコンテンツ数一覧へのリクエストを実施
     *
     * @param genreMetaDataList 　ジャンル一覧
     */
    private void contentsCountRequest(ArrayList<GenreListMetaData> genreMetaDataList) {
//        if (genreMetaDataList.size() > 1) {
            //コンテンツデータが複数ある時は、ジャンルIDとサブジャンルIDを関連付けて保存
//            for (int i = 0; i < genreMetaDataList.size(); i++) {
//                VideoGenreList genreList = new VideoGenreList();
//                genreList.setGenreId(genreMetaDataList.get(i).getId());
//                genreList.setTitle(genreMetaDataList.get(i).getTitle());
//                genreList.setSubGenre(genreMetaDataList.get(i).getSubContent());
//                mVideoGenreList.add(genreList);
//            }

//        } else if (genreMetaDataList.size() > 0) {
//            //コンテンツデータが一つの時はジャンルIDとサブジャンルIDを同列に扱う
//            VideoGenreList genreList = new VideoGenreList();
//            genreList.setGenreId(genreMetaDataList.get(0).getId());
//            genreList.setTitle(mContext.getString(R.string.video_content_all_title));
//            mVideoGenreList.add(genreList);
//            ArrayList<GenreListMetaData> list = genreMetaDataList.get(0).getSubContent();
//            for (int i = 0; i < list.size(); i++) {
//                genreList = new VideoGenreList();
//                genreList.setTitle(list.get(i).getTitle());
//                genreList.setGenreId(list.get(i).getId());
//                mVideoGenreList.add(genreList);
//            }
//        }


        //ジャンル毎コンテンツ数一覧取得用のリストを作成する
        List<String> arrayList = new ArrayList<>();
//        for (int i = 0; i < mVideoGenreList.size(); i++) {
//            arrayList.add(mVideoGenreList.get(i).getGenreId());
//        }
//        arrayList = getGenreIdList(mVideoGenreList);

        getContentCountDataRequest(arrayList);
    }

    /**
     * Activityへ送信するListDataを作成する
     *
     * @param genreCountGetResponse ジャンルごとコンテンツ数一覧レスポンス
     */
    private void getContent(GenreCountGetResponse genreCountGetResponse) {
//        ArrayList<GenreCountGetMetaData> dataArrayList = genreCountGetResponse.getGenreCountGetMetaData();
//        List<VideoGenreList> videoGenreList = new ArrayList<>();
//        if (mVideoGenreList != null && mVideoGenreList.size() > 0) {
//            // ジャンル一覧データ作成
//            for (int i = 0; i < mVideoGenreList.size(); i++) {
//                String titleGenreId = mVideoGenreList.get(i).getGenreId();
//                for (int j = 0; j < dataArrayList.size(); j++) {
//                    String contentGenreId = dataArrayList.get(j).getGenreId();
//                    if (contentGenreId.equals(titleGenreId)) {
//                        mVideoGenreList.get(i).setContentCount(String.valueOf(dataArrayList.get(j).getCount()));
//                        break;
//                    }
//                }
//            }
//
//            //コンテンツ数が0のリストを削除する
//            for (int i = 0; i < mVideoGenreList.size(); i++) {
//                if (!GenreListMetaData.VIDEO_LIST_TITLE_NOD.equals(mVideoGenreList.get(i).getTitle())) {
//                    if (Integer.parseInt(mVideoGenreList.get(i).getContentCount()) > 0) {
//                        videoGenreList.add(mVideoGenreList.get(i));
//                    }
//                } else {
//                    videoGenreList.add(mVideoGenreList.get(i));
//                }
//            }
//        }
//        sendGenreListData(videoGenreList);
            }

    /**
     *
     * @param metaDataList
     * @return
     */
    private Map<String, VideoGenreList> setVideoGenreList(List<GenreListMetaData> metaDataList) {
        Map<String, VideoGenreList> videoGenreListMap = new HashMap<String, VideoGenreList>();
        for(int i=0; i < metaDataList.size(); i++) {
            videoGenreListMap = setVideoGenreList(metaDataList.get(i), videoGenreListMap);
        }
        return videoGenreListMap;
    }

    /**
     * ジャンルIDをKey値としたジャンルデータMapを生成
     * 親ジャンル/サブジャンルのデータをMapに設定する
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
        if(metaData.getSubContent() != null &&
                metaData.getSubContent().size() != 0) {
            // サブジャンルがある場合
            for(GenreListMetaData data : metaData.getSubContent()) {
                // サブジャンルのジャンルIDをリストに格納
                videoGenreList.addSubGenreList(data.getId());
                // サブジャンルのジャンルデータをMapへ設定する繰り返し処理
                listMap = setVideoGenreList(data, listMap);
            }
        }
        listMap.put(videoGenreList.getGenreId(), videoGenreList);

        return listMap;
    }


//
//
//    /**
//     * ジャンルIDリストを生成
//     * @param list
//     * @return
//     */
//    private Map<String, VideoGenreList> getGenreIdList(List<VideoGenreList> list) {
//        Map<String, VideoGenreList> listMap = new HashMap<String, VideoGenreList>();
//        for (int i=0; i < list.size(); i++) {
//            listMap = getGenreId(list.get(i), listMap);
//        }
//
//        return listMap;
//    }
//
//    /**
//     * 引数のvideoGenreListに紐づくジャンルIDをMapに追加
//     * @param videoGenreList
//     * @param listMap
//     * @return
//     */
//    private Map<String, VideoGenreList> getGenreId(VideoGenreList videoGenreList, Map<String, VideoGenreList> listMap) {
//        if(videoGenreList.getSubGenre() != null &&
//                videoGenreList.getSubGenre().size() != 0) {
//            for (VideoGenreList subList : videoGenreList.getSubGenre()) {
//                listMap = getGenreId(subList, listMap);
//            }
//        }
//        listMap.put(videoGenreList.getGenreId(), videoGenreList);
//        return listMap;
//    }
}