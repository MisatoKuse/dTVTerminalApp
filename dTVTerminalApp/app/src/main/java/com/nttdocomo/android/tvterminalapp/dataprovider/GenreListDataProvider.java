/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
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
 * ジャンル一覧データプロバイダ.
 */
public class GenreListDataProvider implements
        GenreListWebClient.GenreListJsonParserCallback,
        GenreCountGetWebClient.GenreCountGetJsonParserCallback {
    // region declaration
    /**
     * ビデオジャンル一覧画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {
        /**
         * Listデータコールバック.
         *
         * @param listData ジャンルのコンテンツ数一覧
         */
        void genreListCallback(@Nullable List<GenreCountGetMetaData> listData);
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
    // endregion

    // callback
    /** ジャンルデータ取得プロパイダのコールバック. */
    private ApiDataProviderCallback mApiDataProviderCallback = null;
    /** ジャンルデータMapを返却するためのコールバック. */
    private GenreListMapCallback genreListMapCallback;
    /** ジャンルデータ取得のコールバック. */
    private RankGenreListCallback mRankGenreListCallback;

    // static final
    /** ビデオジャンル;NOD. */
    private static final String VIDEO_GENRE_KEY_NOD = "NOD";
    /** ビデオジャンル:ARIB. */
    private static final String VIDEO_GENRE_KEY_ARIB = "ARIB";
    /** ビデオジャンル:VOD.*/
    private static final String VIDEO_GENRE_KEY_VOD = "VOD";
    /** ビデオジャンル:dTV.*/
    private static final String VIDEO_GENRE_KEY_DTV = "dTV";
    /** コンテキスト. */
    private Context mContext = null;
    /** コンテンツタイプ. */
    private ContentsAdapter.ActivityTypeItem mType;

    /** ジャンルリスト取得用webクライアント. */
    private GenreListWebClient mGenreListWebClient = null;
    /** ジャンルコンテンツ数取得用webクライアント. */
    private GenreCountGetWebClient mWebClient = null;
    /** 通信禁止判定フラグ. */
    private boolean mIsCancel = false;
    /** ジャンルリスト用エラー情報バッファ. */
    private ErrorState mGenreListError = null;
    /** ジャンルコンテンツ数用エラー情報バッファ. */
    private ErrorState mGenreCountError = null;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public GenreListDataProvider(final Context context) {
        mApiDataProviderCallback = (ApiDataProviderCallback) context;
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
    public GenreListDataProvider(final Context context, final RankGenreListCallback mRankGenreListCallback,
                                 final ContentsAdapter.ActivityTypeItem type) {
        this.mType = type;
        this.mRankGenreListCallback = mRankGenreListCallback;
        mContext = context;
    }

    // region implement method
    @Override
    public void onGenreListJsonParsed(@Nullable final GenreListResponse genreListResponse) {

        GenreListResponse response = genreListResponse;
        if (genreListResponse != null && !genreListResponse.getTypeList().isEmpty()) {
            //取得した情報を保存する
            DateUtils dateUtils = new DateUtils(mContext);
            String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
            if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
                dateUtils.addLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
                dateUtils.addLastProgramDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
                SharedPreferencesUtils.setSharedPreferencesVideoGenreData(mContext,
                        StringUtils.toGenreListResponseBase64(genreListResponse));
            }
        } else {
            response = StringUtils.toGenreListResponse(SharedPreferencesUtils.getSharedPreferencesVideoGenreData(mContext));
        }

        if (mRankGenreListCallback != null) {
            setRankGenreListData(response);
        } else {
            getGenreList(response);
        }
    }

    @Override
    public void onGenreCountGetJsonParsed(@Nullable final GenreCountGetResponse genreCountGetResponse) {
        if (mApiDataProviderCallback == null) {
            return;
        }

        if (genreCountGetResponse != null) {
            mApiDataProviderCallback.genreListCallback(genreCountGetResponse.getGenreCountGetMetaData());
        } else {
            mGenreCountError = mWebClient.getError();
            mApiDataProviderCallback.genreListCallback(null);
        }
    }
    // endregion

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
                if (!mGenreListWebClient.getGenreListApi(this)) {
                    sendRankGenreList(null);
                }
            } else {
                DTVTLogger.error("GenreListDataProvider is stopping connection");
                //nullデータを返却する
                sendRankGenreList(null);
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
                    if (!mGenreListWebClient.getGenreListApi(this)) {
                        sendRankGenreList(null);
                    }
                } else {
                    DTVTLogger.error("GenreListDataProvider is stopping connection");
                    //nullデータを返却する
                    sendRankGenreList(null);
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
            String filter = "";
            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
            int ageReq = userInfoDataProvider.getUserAge();
            if (!mWebClient.getGenreCountGetApi(filter, ageReq, genreId, this)) {
                mApiDataProviderCallback.genreListCallback(null);
            }
        } else {
            DTVTLogger.error("GenreListDataProvider is stopping connection");
            mApiDataProviderCallback.genreListCallback(null);
        }
    }

    /**
     * ジャンル一覧データをリストに形成する.
     *
     * @param genreListResponse ジャンル一覧APIからのレスポンス
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void setRankGenreListData(@Nullable final GenreListResponse genreListResponse) {
        if (genreListResponse == null) {
            mGenreListError = mGenreListWebClient.getError();
            sendRankGenreList(null);
        } else {
            Map<String, ArrayList<GenreListMetaData>> listMap = genreListResponse.getTypeList();
            if (listMap == null) {
                DTVTLogger.error("response is null");
                sendRankGenreList(null);
            } else {
                if (mContext != null) {
                    ArrayList<GenreListMetaData> genreMetaDataList = new ArrayList<>();
                    GenreListMetaData genreAll = new GenreListMetaData();
                    genreAll.setTitle(mContext.getResources().getString(R.string.common_ranking_tab_all));
                    genreAll.setId("");
                    genreMetaDataList.add(genreAll);
                    switch (mType) {
                        case TYPE_VIDEO_RANK:
                            if (listMap.get(VIDEO_GENRE_KEY_VOD) != null) {
                                genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_VOD));
                            } else {
                                DTVTLogger.error("VOD listMap is not found");
                            }
                            break;
                        case TYPE_WEEKLY_RANK:
                            if (listMap.get(VIDEO_GENRE_KEY_ARIB) != null) {
                                genreMetaDataList.addAll(listMap.get(VIDEO_GENRE_KEY_ARIB));
                            } else {
                                DTVTLogger.error("ARIB listMap is not found");
                            }
                            break;
                       default:
                           DTVTLogger.error("activity is not found");
                    }
                    sendRankGenreList(genreMetaDataList);
                } else {
                    DTVTLogger.error("context is null");
                    sendRankGenreList(null);
                }
            }
        }
    }

    /**
     * ジャンル一覧データをMapに形成する.
     *
     * @param genreListResponse ジャンル一覧APIからのレスポンス
     */
    private void getGenreList(@Nullable final GenreListResponse genreListResponse) {
        if (genreListResponse == null) {
            mGenreListError = mGenreListWebClient.getError();
            genreListMapCallback.genreListMapCallback(null, null);
            DTVTLogger.error("response is null");
            return;
        }

        Map<String, ArrayList<GenreListMetaData>> listMap = genreListResponse.getTypeList();
        ArrayList<GenreListMetaData> genreMetaDataList = new ArrayList<>();

        // すべて(VOD)
        GenreListMetaData genreAll = new GenreListMetaData();
        genreAll.setTitle(mContext.getResources().getString(R.string.video_list_genre_all));
        genreAll.setId(GenreListMetaData.VIDEO_LIST_GENRE_ID_ALL_CONTENTS);
        genreMetaDataList.add(genreAll);

        // VOD親ジャンルを追加
        ArrayList<GenreListMetaData> vodList = listMap.get(VIDEO_GENRE_KEY_VOD);
        if (vodList != null) {
            genreMetaDataList.addAll(vodList);
        }

        // NODを追加
        ArrayList<GenreListMetaData> nodList = listMap.get(VIDEO_GENRE_KEY_NOD);
        if (nodList != null) {
            GenreListMetaData firstNodData = new GenreListMetaData();
            firstNodData.setTitle(mContext.getResources().getString(R.string.video_list_genre_nod));
            firstNodData.setId(GenreListMetaData.VIDEO_LIST_GENRE_ID_NOD);
            firstNodData.setSubContentAll(nodList);
            genreMetaDataList.add(firstNodData);
        }

        //dTVを追加
        ArrayList<GenreListMetaData> dtvList = listMap.get(VIDEO_GENRE_KEY_DTV);
        if (dtvList != null) {
            GenreListMetaData firstDtvData = new GenreListMetaData();
            firstDtvData.setTitle(mContext.getResources().getString(R.string.video_list_genre_dtv));
            firstDtvData.setId(GenreListMetaData.VIDEO_LIST_GENRE_ID_DTV);
            firstDtvData.setSubContentAll(dtvList);
            genreMetaDataList.add(firstDtvData);
        }

        // 初期画面のジャンル一覧データを生成
        List<String> firstPageGenreIdList = new ArrayList<>();
        Map<String, VideoGenreList> videoGenreListMap = new HashMap<>();

        for (GenreListMetaData genreMetaData: genreMetaDataList) {
            firstPageGenreIdList.add(genreMetaData.getId());
            videoGenreListMap = setVideoGenreList(genreMetaData, videoGenreListMap);
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
                && metaData.getSubContent().size() > 0) {
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

    /**
     * ジャンルリスト用エラーのクラスを返すゲッター.
     *
     * @return ジャンル情報取得エラーのクラス
     */
    public ErrorState getGenreListError() {
        //ジャンルリストAPIが正常動作ならば、コンテンツ数取得エラーの値を返す
        if (mGenreListError == null
                || mGenreListError.getErrorType() == DtvtConstants.ErrorType.SUCCESS) {
            return mGenreCountError;
        }

        //ジャンルリストはエラーなので、それを返す
        return mGenreListError;
    }

    /**
     * ジャンルリストcallbackを返す.
     *
     * @param genreMetaDataList ジャンルリスト
     */
    private void sendRankGenreList(final ArrayList<GenreListMetaData> genreMetaDataList) {
        if (mRankGenreListCallback != null) {
            mRankGenreListCallback.onRankGenreListCallback(genreMetaDataList);
        }
    }

    /**
     * callbackキャンセル用.
     *
     * @param listCallback callback(nullを設定)
     */
    public void setRankGenreListCallback(final RankGenreListCallback listCallback) {
        this.mRankGenreListCallback = listCallback;
    }
}