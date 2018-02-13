/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoGenreList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreCountGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreListWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ビデオジャンル一覧.
 */
public class VideoGenreProvider implements
        GenreListWebClient.GenreListJsonParserCallback,
        GenreCountGetWebClient.GenreCountGetJsonParserCallback {
    /**
     * ジャンルデータ取得プロパイダのコールバック.
     */
    private apiGenreListDataProviderCallback mApiGenreListDataProviderCallback = null;
    /**
     * ビデオジャンル:IPTV.
     */
    private static final String VIDEO_GENRE_KEY_IPTV = "IPTV";
    /**
     * ビデオジャンル;NOD.
     */
    private static final String VIDEO_GENRE_KEY_NOD = "NOD";
    /**
     * ビデオジャンル:ARIB.
     */
    private static final String VIDEO_GENRE_KEY_ARIB = "ARIB";
    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * ジャンルデータMapを返却するためのコールバック.
     */
    private GenreListMapCallback genreListMapCallback;
    /**
     * ジャンルデータ取得のコールバック.
     */
    private RankGenreListCallback mRankGenreListCallback;
    /**
     * コンテンツタイプ.
     */
    private ContentsAdapter.ActivityTypeItem type;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * ジャンルリスト取得用webクライアント.
     */
    private GenreListWebClient mGenreListWebClient = null;
    /**
     * ジャンルコンテンツ数取得用webクライアント.
     */
    private GenreCountGetWebClient mWebClient = null;

    @Override
    public void onGenreListJsonParsed(final GenreListResponse genreListResponse) {

        //取得した情報を保存する
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
        if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
            dateUtils.addLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
            dateUtils.addLastProgramDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
            SharedPreferencesUtils.setSharedPreferencesVideoGenreData(mContext,
                    StringUtils.toGenreListResponseBase64(genreListResponse));
        }
        if (mRankGenreListCallback != null) {
            setRankGenreListData(genreListResponse);
        } else {
            getGenreList(genreListResponse);
        }
    }

    @Override
    public void onGenreCountGetJsonParsed(final GenreCountGetResponse genreCountGetResponse) {
        if (mApiGenreListDataProviderCallback != null) {
            mApiGenreListDataProviderCallback.genreListCallback(genreCountGetResponse.getGenreCountGetMetaData());
        }
    }

    /**
     * ビデオジャンル一覧画面用データを返却するためのコールバック.
     */
    public interface apiGenreListDataProviderCallback {
        /**
         * Listデータコールバック.
         *
         * @param listData ジャンルのコンテンツ数一覧
         */
        void genreListCallback(List<GenreCountGetMetaData> listData);
    }

    /**
     * ジャンルIDをキー値としたジャンルデータMapを返却するためのコールバック.
     */
    public interface GenreListMapCallback {
        /**
         * ジャンルデータMapを返却する.
         *
         * @param map ジャンルデータmap
         * @param firstGenreIdList 初期画面のジャンルID
         */
        void genreListMapCallback(Map<String, VideoGenreList> map, List<String> firstGenreIdList);
    }

    /**
     * ジャンルリストデータを返却するためのコールバック.
     */
    public interface RankGenreListCallback {
        /**
         * ジャンルリストデータを返却する.
         *
         * @param genreMetaDataList ジャンルリスト
         */
        void onRankGenreListCallback(ArrayList<GenreListMetaData> genreMetaDataList);
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public VideoGenreProvider(final Context context) {
        this.mApiGenreListDataProviderCallback = (apiGenreListDataProviderCallback) context;
        genreListMapCallback = (GenreListMapCallback) context;
        mContext = context;
    }

    /**
     * コンストラクタ.
     *
     * @param context                コンテキスト
     * @param mRankGenreListCallback コールバック
     * @param type                   コンテンツタイプ
     */
    public VideoGenreProvider(final Context context, final RankGenreListCallback mRankGenreListCallback,
                              final ContentsAdapter.ActivityTypeItem type) {
        this.type = type;
        this.mRankGenreListCallback = mRankGenreListCallback;
        mContext = context;
    }

    /**
     * ジャンル一覧.
     * VideoTopActivityからのデータ取得要求受付
     */
    public void getGenreListDataRequest() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
        if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
            if (!mIsCancel) {
                //データの有効期限切れなら通信で取得
                mGenreListWebClient = new GenreListWebClient(mContext);
                mGenreListWebClient.getGenreListApi(this);
            } else {
                DTVTLogger.error("VideoGenreProvider is stopping connection");
                if (mRankGenreListCallback != null) {
                    //nullデータを返却する
                    mRankGenreListCallback.onRankGenreListCallback(null);
                }
            }
        } else {
            GenreListResponse genreListResponse = StringUtils.toGenreListResponse(
                    SharedPreferencesUtils.getSharedPreferencesVideoGenreData(mContext));
            if (genreListResponse != null) {
                //データ取得成功ならデータ作成開始
                if (mRankGenreListCallback != null) {
                    setRankGenreListData(genreListResponse);
                } else {
                    getGenreList(genreListResponse);
                }
            } else {
                if (!mIsCancel) {
                    //SharedPreferences取得失敗時には通信で取得
                    mGenreListWebClient = new GenreListWebClient(mContext);
                    mGenreListWebClient.getGenreListApi(this);
                } else {
                    DTVTLogger.error("VideoGenreProvider is stopping connection");
                    if (mRankGenreListCallback != null) {
                        //nullデータを返却する
                        mRankGenreListCallback.onRankGenreListCallback(null);
                    }
                }
            }
        }
    }

    /**
     * コンテンツ数のデータ取得要求を行う.
     *
     * @param genreId ジャンルIDリスト
     */
    public void getContentCountListData(final List<String> genreId) {
        if (!mIsCancel) {
            //通信クラスにデータ取得要求を出す
            mWebClient = new GenreCountGetWebClient(mContext);
            int limit = 1;
            int offset = 1;
            String filter = "";
            int ageReq = 1;
            mWebClient.getGenreCountGetApi(filter, ageReq, genreId, this);
        } else {
            DTVTLogger.error("VideoGenreProvider is stopping connection");
            mApiGenreListDataProviderCallback.genreListCallback(null);
        }
    }

    /**
     * ジャンル一覧データをリストに形成する.
     *
     * @param genreListResponse ジャンル一覧APIからのレスポンス
     */
    private void setRankGenreListData(final GenreListResponse genreListResponse) {
        try {
            Map<String, ArrayList<GenreListMetaData>> listMap = genreListResponse.getTypeList();
            if (listMap == null) {
                DTVTLogger.error("response is null");
                mRankGenreListCallback.onRankGenreListCallback(null);
            } else {
                if (mContext != null) {
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
     * ジャンル一覧データをMapに形成する.
     *
     * @param genreListResponse ジャンル一覧APIからのレスポンス
     */
    private void getGenreList(final GenreListResponse genreListResponse) {
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
        List<String> firstPageGenreIdList = new ArrayList<>();
        Map<String, VideoGenreList> videoGenreListMap = new HashMap<>();
        for (int i = 0; i < genreMetaDataList.size(); i++) {
            firstPageGenreIdList.add(genreMetaDataList.get(i).getId());
            videoGenreListMap = setVideoGenreList(genreMetaDataList.get(i), videoGenreListMap);
        }
        genreListMapCallback.genreListMapCallback(videoGenreListMap, firstPageGenreIdList);
    }

    /**
     * ジャンルIDをKey値としたジャンルデータMapを生成.
     * 親ジャンル/サブジャンルのデータをMapに設定する
     *
     * @param metaData ジャンルリスト
     * @param listMap  加工済みジャンルリスト
     * @return listMap 加工済みジャンルリスト
     */
    private Map<String, VideoGenreList> setVideoGenreList(final GenreListMetaData metaData, final Map<String, VideoGenreList> listMap) {
        VideoGenreList videoGenreList = new VideoGenreList();
        videoGenreList.setTitle(metaData.getTitle());
        videoGenreList.setGenreId(metaData.getId());
        videoGenreList.setRValue(metaData.getRValue());

        Map<String, VideoGenreList> videoGenreListMap = listMap;
        // サブジャンルがあるかどうかの判定
        if (metaData.getSubContent() != null
                && metaData.getSubContent().size() != 0) {
            // サブジャンルがある場合
            for (GenreListMetaData data : metaData.getSubContent()) {
                // サブジャンルのジャンルIDをリストに格納
                videoGenreList.addSubGenreList(data.getId());
                // サブジャンルのジャンルデータをMapへ設定する繰り返し処理
                videoGenreListMap = setVideoGenreList(data, listMap);
            }
        }
        videoGenreListMap.put(videoGenreList.getGenreId(), videoGenreList);

        return videoGenreListMap;
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        if (mGenreListWebClient != null) {
            mGenreListWebClient.stopConnect();
        }
        if (mWebClient != null) {
            mWebClient.stopConnect();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
        if (mGenreListWebClient != null) {
            mGenreListWebClient.enableConnect();
        }
        if (mWebClient != null) {
            mWebClient.enableConnect();
        }
    }
}