/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.WatchListenVideoDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RoleListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListResponse;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RentalListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VideoRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RankingTopDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalChListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RoleListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WatchListenVideoWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ホーム画面用データプロバイダ.
 */
public class HomeDataProvider extends ClipKeyListDataProvider implements
        TvClipWebClient.TvClipJsonParserCallback,
        VodClipWebClient.VodClipJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback,
        DailyRankWebClient.DailyRankJsonParserCallback,
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback,
        WatchListenVideoWebClient.WatchListenVideoJsonParserCallback,
        RentalVodListWebClient.RentalVodListJsonParserCallback,
        RentalChListWebClient.RentalChListJsonParserCallback,
        ChannelWebClient.ChannelJsonParserCallback,
        RoleListWebClient.RoleListJsonParserCallback,
        GenreListWebClient.GenreListJsonParserCallback,
        DbThread.DbOperation,
        RecommendDataProvider.RecommendApiDataProviderCallback {

    /**
     * Context.
     */
    private Context mContext = null;

    /**
     * Home画面用データを返却するためのコールバック.
     */
    private ApiDataProviderCallback mApiDataProviderCallback = null;

    /**
     * Home画面ではチャンネルリストを使用しないため"全て"を]設定する.
     */
    private static final int DEFAULT_CHANNEL_DISPLAY_TYPE = 0;

    /**
     * ロールリストID.
     */
    private static final int ROLE_ID_LIST = 0;
    /**
     * チャンネルリストID.
     */
    private static final int CHANNEL_LIST = 1;
    /**
     * 番組一覧ID.
     */
    private static final int TV_SCHEDULE_LIST = 2;
    /**
     * 今日のテレビランキングリストID.
     */
    private static final int DAILY_RANK_LIST = 3;
    /**
     * ビデオランキングリストID.
     */
    private static final int VIDEO_RANK_LIST = 4;
    /**
     * クリップ(テレビ)リストID.
     */
    private static final int TV_CLIP_LIST = 5;
    /**
     * クリップ(ビデオ)リストID.
     */
    private static final int VOD_CLIP_LIST = 6;
    /**
     * 視聴中ビデオ一覧ID.
     */
    private static final int WATCH_LISTEN_VIDEO_LIST = 7;
    /**
     * 購入済みCh一覧ID.
     */
    private static final int RENTAL_CH_LIST = 8;
    /**
     * 購入済みVod一覧ID.
     */
    private static final int RENTAL_VOD_LIST = 9;

    /**
     * 購入済みCh一覧取得クラス.
     */
    private PurchasedChListResponse mPurchasedChListResponse = null;

    /**
     * ロールリスト.
     */
    private ArrayList<RoleListMetaData> mRoleListMetaDataList = null;

    /**
     * 購入済みVOD一覧取得クラス.
     */
    private PurchasedVodListResponse mPurchasedVodListResponse = null;

    /**
     * 視聴中ビデオListクラス.
     */
    private WatchListenVideoList mWatchListenVideoList = null;

    /**
     * クリップ(ビデオ)Listクラス.
     */
    private VodClipList mVodClipList = null;

    /**
     * クリップ(テレビ)Listクラス.
     */
    private TvClipList mTvClipList = null;

    /**
     * チャンネルリストListクラス.
     */
    private ChannelList mChannelList = null;

    /**
     *ビデオリスト Listクラス.
     */
    private VideoRankList mVideoRankList = null;

    /**
     * 今日テレビランキングListクラス.
     */
    private DailyRankList mDailyRankList = null;

    /**
     * 番組Listクラス.
     */
    private TvScheduleList mTvScheduleList = null;

    @Override
    public void onTvClipJsonParsed(final List<TvClipList> tvClipLists) {
        if (tvClipLists != null && tvClipLists.size() > 0) {
            TvClipList list = tvClipLists.get(0);
            setStructDB(list);
        } else {
            //TODO:Sprint10でDB使用を一時停止
            //WEBAPIを取得できなかった時はDBのデータを使用
//            List<Map<String, String>> tvClipList = new ArrayList<>();
//            HomeDataManager homeDataManager = new HomeDataManager(mContext);
//            tvClipList = homeDataManager.selectTvClipHomeData();
//            sendTvClipListData(tvClipList);
        }
    }

    @Override
    public void onVodClipJsonParsed(final List<VodClipList> vodClipLists) {
        if (vodClipLists != null && vodClipLists.size() > 0) {
            VodClipList list = vodClipLists.get(0);
            setStructDB(list);
        } else {
            //TODO:Sprint10でDB使用を一時停止
            //WEBAPIを取得できなかった時はDBのデータを使用
//            List<Map<String, String>> vodClipList = new ArrayList<>();
//            HomeDataManager homeDataManager = new HomeDataManager(mContext);
//            vodClipList = homeDataManager.selectVodClipHomeData();
//            sendVodClipListData(vodClipList);
        }
    }

    @Override
    public void onTvScheduleJsonParsed(final List<TvScheduleList> tvScheduleList) {
        if (tvScheduleList != null && tvScheduleList.size() > 0) {
            TvScheduleList list = tvScheduleList.get(0);
            setStructDB(list);
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            List<Map<String, String>> scheduleList = new ArrayList<>();
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            scheduleList = homeDataManager.selectTvScheduleListHomeData();
            sendTvScheduleListData(scheduleList);
        }
    }

    @Override
    public void onDailyRankJsonParsed(final List<DailyRankList> dailyRankLists) {
        if (dailyRankLists != null && dailyRankLists.size() > 0) {
            DailyRankList list = dailyRankLists.get(0);
            setStructDB(list);
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            List<Map<String, String>> dailyRankList = new ArrayList<>();
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            dailyRankList = homeDataManager.selectDailyRankListHomeData();
            sendDailyRankListData(dailyRankList);
        }
    }

    @Override
    public void onContentsListPerGenreJsonParsed(final List<VideoRankList> contentsListPerGenre) {
        if (contentsListPerGenre != null && contentsListPerGenre.size() > 0) {
            VideoRankList list = contentsListPerGenre.get(0);
            setStructDB(list);
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            List<Map<String, String>> videoRankList = new ArrayList<>();
            RankingTopDataManager rankingTopDataManager = new RankingTopDataManager(mContext);
            videoRankList = rankingTopDataManager.selectVideoRankListData();
            sendVideoRankListData(videoRankList);
        }
    }

    @Override
    public void onWatchListenVideoJsonParsed(final List<WatchListenVideoList> watchListenVideoList) {
        if (watchListenVideoList != null && watchListenVideoList.size() > 0) {
            WatchListenVideoList list = watchListenVideoList.get(0);
            setStructDB(list);
            //TODO:取得したデータをHome画面で使用する場合はここに記載
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定(不要な場合は削除)
        }
    }

    @Override
    public void onRentalVodListJsonParsed(final PurchasedVodListResponse RentalVodListResponse) {
        if (RentalVodListResponse != null) {
            setStructDB(RentalVodListResponse);
            //TODO:取得したデータをHome画面で使用する場合はここに記載
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定(不要な場合は削除)
        }
    }

    @Override
    public void onRentalChListJsonParsed(final PurchasedChListResponse RentalChListResponse) {
        if (RentalChListResponse != null) {
            setStructDB(RentalChListResponse);
            //TODO:取得したデータをHome画面で使用する場合はここに記載
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定(不要な場合は削除)
        }
    }

    @Override
    public void onChannelJsonParsed(final List<ChannelList> channelLists) {
        if (channelLists != null && channelLists.size() > 0) {
            ChannelList list = channelLists.get(0);
            setStructDB(list);
            //TODO:取得したデータをHome画面で使用する場合はここに記載
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定(不要な場合は削除)
        }
    }

    @Override
    public void onRoleListJsonParsed(final RoleListResponse roleListResponse) {
        if (roleListResponse != null) {
            setStructDB(roleListResponse);
            //TODO:取得したデータをHome画面で使用する場合はここに記載
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定(不要な場合は削除)
        }
    }

    @Override
    public void onGenreListJsonParsed(final GenreListResponse genreListResponse) {
        if (genreListResponse != null) {
            //取得した情報を保存する
            DateUtils dateUtils = new DateUtils(mContext);
            String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
            if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
                dateUtils.addLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
                dateUtils.addLastProgramDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
                SharedPreferencesUtils.setSharedPreferencesVideoGenreData(mContext,
                        StringUtils.toGenreListResponseBase64(genreListResponse));
            }
        }
    }

    /**
     * Home画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {
        /**
         * チャンネル一覧用コールバック.
         *
         * @param channelList チャンネルリスト
         */
        void tvScheduleListCallback(List<ContentsData> channelList);

        /**
         * デイリーランキング用コールバック.
         *
         * @param dailyList デイリーランクリスト
         */
        void dailyRankListCallback(List<ContentsData> dailyList);

        /**
         * クリップ[テレビ]リスト用コールバック.
         *
         * @param tvClipList クリップ(ビデオ)リスト
         */
        void tvClipListCallback(List<ContentsData> tvClipList);

        /**
         * クリップ[ビデオ]リスト用コールバック.
         *
         * @param vodClipList クリップ(ビデオ)リスト
         */
        void vodClipListCallback(List<ContentsData> vodClipList);

        /**
         * ビデオランキング用コールバック.
         *
         * @param videoRankList ビデオランクリスト
         */
        void videoRankCallback(List<ContentsData> videoRankList);

        /**
         * おすすめ番組用コールバック.
         *
         * @param recChList おすすめ番組リスト
         */
        void recommendChannelCallback(List<ContentsData> recChList);

        /**
         * おすすめビデオ用コールバック.
         *
         * @param recVdList おすすめビデオリスト
         */
        void recommendVideoCallback(List<ContentsData> recVdList);
    }

    /**
     * コンストラクタ.
     *
     * @param mContext コンテクスト
     */
    public HomeDataProvider(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * Activityからのデータ取得要求受付.
     */
    public void getHomeData() {
        //非同期処理でデータの取得を行う
        homeDataDownloadTask.execute();
    }

    /**
     * 非同期処理でHOME画面に表示するデータの取得を行う.
     */
    private AsyncTask<Void, Void, Void> homeDataDownloadTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(final Void... voids) {
            //NOW ON AIR
            List<Map<String, String>> tvScheduleListData = getTvScheduleListData();
            if (tvScheduleListData != null && tvScheduleListData.size() > 0) {
                sendTvScheduleListData(tvScheduleListData);
            }
            //おすすめ番組・レコメンド情報は最初からContentsDataのリストなので、そのまま使用する
            List<ContentsData> recommendChListData = getRecommendChListData();
            if (recommendChListData != null && recommendChListData.size() > 0) {
                sendRecommendChListData(recommendChListData);
            }
            //おすすめビデオ・レコメンド情報は最初からContentsDataのリストなので、そのまま使用する
            List<ContentsData> recommendVdListData = getRecommendVdListData();
            if (recommendVdListData != null && recommendVdListData.size() > 0) {
                sendRecommendVdListData(recommendVdListData);
            }
            //今日のテレビランキング
            List<Map<String, String>> dailyRankList = getDailyRankListData();
            if (dailyRankList != null && dailyRankList.size() > 0) {
                sendDailyRankListData(dailyRankList);
            }
            //ビデオランキング
            //TODO：Sprint11では視聴年齢値、ソート順のみの指定のためその他の値は固定値
            int ageReq = SharedPreferencesUtils.getSharedPreferencesAgeReq(mContext);
            List<Map<String, String>> VideoRankList = getVideoRankListData(1, 1, "", ageReq, "", "",
                    JsonConstants.GENRE_PER_CONTENTS_SORT_PLAY_COUNT_DESC);
            if (VideoRankList != null && VideoRankList.size() > 0) {
                sendVideoRankListData(VideoRankList);
            }
            //TODO:生データ保存のみ(DB保存までの処理を新設するか検討中)
            //ジャンルID(ビデオ一覧)一覧取得
            getGenreListDataRequest();

            //ロールID一覧取得
            getRoleListData();

            //チャンネルリスト取得
            getChannelList(1, 1, "", DEFAULT_CHANNEL_DISPLAY_TYPE);

            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
            if (userInfoDataProvider.isH4dUser()) {
                //H4dユーザに必要なデータ取得開始
                //クリップキー一覧(今日のテレビランキングにまとめられてるので省略するか検討中)
                //TVクリップ一覧
                List<Map<String, String>> tvClipList = getTvClipListData();
                if (tvClipList != null && tvClipList.size() > 0) {
                    sendTvClipListData(tvClipList);
                }

                //VODクリップ一覧
                List<Map<String, String>> vodClipList = getVodClipListData();
                if (vodClipList != null && vodClipList.size() > 0) {
                    sendVodClipListData(vodClipList);
                }

                //視聴中ビデオ一覧
                getWatchListenVideoData(WatchListenVideoListDataProvider.DEFAULT_PAGE_OFFSET);

                //購入済チャンネル一覧(レンタルCh)
                getChListData();

                //購入済VOD一覧
                getRentalData();
            }
            return null;
        }
    };

    /**
     * NOW ON AIRをHomeActivityに送る.
     *
     * @param list 番組リスト
     */
    private void sendTvScheduleListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.tvScheduleListCallback(setHomeContentData(list, false));
    }

    /**
     * おすすめ番組をHomeActivityに送る.
     *
     * @param list おすすめ番組のコンテンツ情報
     */
    private void sendRecommendChListData(final List<ContentsData> list) {
        mApiDataProviderCallback.recommendChannelCallback(list);
    }

    /**
     * おすすめビデオをHomeActivityに送る.
     *
     * @param list おすすめビデオのコンテンツ情報
     */
    private void sendRecommendVdListData(final List<ContentsData> list) {
        mApiDataProviderCallback.recommendVideoCallback(list);
    }

    /**
     * 今日のランキングをHomeActivityに送る.
     *
     * @param list デイリーランクリスト
     */
    private void sendDailyRankListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.dailyRankListCallback(setHomeContentData(list, true));
    }

    /**
     * ビデオランキングをHomeActivityに送る.
     *
     * @param list ビデオランキング
     */
    private void sendVideoRankListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.videoRankCallback(setHomeContentData(list, true));
    }

    /**
     * クリップ[テレビ]リストをHomeActivityに送る.
     *
     * @param list クリップ[テレビ]リスト
     */
    private void sendTvClipListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.tvClipListCallback(setHomeContentData(list, false));
    }

    /**
     * クリップ[ビデオ]リストをHomeActivityに送る.
     *
     * @param list クリップ[ビデオ]リスト
     */
    private void sendVodClipListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.vodClipListCallback(setHomeContentData(list, false));
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     * @param mapList コンテンツリストデータ
     * @param rankFlag ランキングのコンテンツか否か
     * @return dataList ListView表示用データ
     */
    private List<ContentsData> setHomeContentData(
            final List<Map<String, String>> mapList, final boolean rankFlag) {
        List<ContentsData> contentsDataList = new ArrayList<>();

        ContentsData contentInfo;

        for (int i = 0; i < mapList.size(); i++) {
            contentInfo = new ContentsData();
            if (rankFlag) {
                contentInfo.setRank(String.valueOf(i + 1));
            }
            contentInfo.setTime(mapList.get(i).get(JsonConstants.META_RESPONSE_DISPLAY_START_DATE));
            contentInfo.setTitle(mapList.get(i).get(JsonConstants.META_RESPONSE_TITLE));
            contentInfo.setThumURL(mapList.get(i).get(JsonConstants.META_RESPONSE_THUMB_448));
            String thumbUrl = contentInfo.getThumURL();
            String title = contentInfo.getTitle();
            if (title == null || title.length() < 1) {
                contentInfo.setTitle(mapList.get(i).get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE));
            }
            if (thumbUrl == null || thumbUrl.length() < 1) {
                //レコメンドからのデータはキーが違うため再取得する
                contentInfo.setThumURL(mapList.get(i).get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1));
            }

            contentsDataList.add(contentInfo);
        }

        return contentsDataList;
    }

    /**
     * NOW ON AIR 情報取得.
     *
     * @return 番組情報
     */
    private List<Map<String, String>> getTvScheduleListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.TV_SCHEDULE_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //NO ON AIR一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            //TODO データがDBに無い場合や壊れていた場合の処理が必要
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectTvScheduleListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            TvScheduleWebClient webClient = new TvScheduleWebClient(mContext);
            int[] ageReq = {1};
            String[] upperPageLimit = {WebApiBasePlala.DATE_NOW};
            String lowerPageLimit = "";
            webClient.getTvScheduleApi(ageReq, upperPageLimit,
                    lowerPageLimit, this);
        }
        return list;
    }

    /**
     * おすすめ番組情報取得.
     * 認証処理が必要になったので、レコメンド情報はレコメンドデータプロバイダ経由で取得するように変更
     *
     * @return おすすめ番組情報
     */
    private List<ContentsData> getRecommendChListData() {
        RecommendDataProvider recommendDataProvider = new RecommendDataProvider(
                mContext.getApplicationContext(), this);

        //レコメンドデータプロバイダーからおすすめ番組情報を取得する・DBに既に入っていた場合はその値を使用するので、trueを指定する
        List<ContentsData> recommendTvData =
                recommendDataProvider.startGetRecommendData(RecommendDataProvider.TV_NO,
                        SearchConstants.RecommendList.FIRST_POSITION,
                        SearchConstants.RecommendList.RECOMMEND_PRELOAD_COUNT, true);

        //取得したデータを渡す
        return recommendTvData;
    }

    /**
     * おすすめビデオ情報取得.
     * 認証処理が必要になったので、レコメンド情報はレコメンドデータプロバイダ経由で取得するように変更
     *
     * @return おすすめビデオ情報
     */
    private List<ContentsData> getRecommendVdListData() {
        RecommendDataProvider recommendDataProvider = new RecommendDataProvider(
                mContext.getApplicationContext(), this);

        //レコメンドデータプロバイダーからおすすめビデオ情報を取得する・DBに既に入っていた場合はその値を使用するので、trueを指定する
        List<ContentsData> recommendVideoData =
                recommendDataProvider.startGetRecommendData(RecommendDataProvider.VIDEO_NO,
                        SearchConstants.RecommendList.FIRST_POSITION,
                        SearchConstants.RecommendList.RECOMMEND_PRELOAD_COUNT, true);

        //取得したデータを渡す
        return recommendVideoData;
    }

    /**
     * クリップ[テレビ]リストデータ取得開始.
     *
     * @return クリップ[テレビ]リスト
     */
    private List<Map<String, String>> getTvClipListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.TV_LAST_INSERT);
        List<Map<String, String>> list = new ArrayList<>();
        //クリップ[テレビ]一覧のDB保存履歴と、有効期間を確認
//        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
        //TODO:Sprint10でDB使用を一時停止
        //データをDBから取得する
//            HomeDataManager homeDataManager = new HomeDataManager(mContext);
//            list = homeDataManager.selectTvClipHomeData();
//        } else {
        //通信クラスにデータ取得要求を出す
        TvClipWebClient webClient = new TvClipWebClient(mContext);
        int ageReq = 1;
        int upperPageLimit = 1;
        int lowerPageLimit = 1;
        int pagerOffset = 1;
        String pagerDirection = "";

        webClient.getTvClipApi(ageReq, upperPageLimit,
                lowerPageLimit, pagerOffset, pagerDirection, this);
//        }
        return list;
    }

    /**
     * クリップ[ビデオ]リストデータ取得開始.
     *
     * @return クリップ[ビデオ]リスト
     */
    private List<Map<String, String>> getVodClipListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.VOD_LAST_INSERT);
        List<Map<String, String>> list = new ArrayList<>();
        //クリップ[ビデオ]一覧のDB保存履歴と、有効期間を確認
//        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
        //TODO:Sprint10でDB使用を一時停止
        //データをDBから取得する
//            HomeDataManager homeDataManager = new HomeDataManager(mContext);
//            list = homeDataManager.selectVodClipHomeData();
//        } else {
        //通信クラスにデータ取得要求を出す
        VodClipWebClient webClient = new VodClipWebClient(mContext);
        int ageReq = 1;
        int upperPageLimit = 1;
        int lowerPageLimit = 1;
        int pagerOffset = 1;
        String pagerDirection = "";

        webClient.getVodClipApi(ageReq, upperPageLimit,
                lowerPageLimit, pagerOffset, pagerDirection, this);
//        }
        return list;
    }

    /**
     * 今日のテレビランキング情報取得.
     *
     * @return 今日のテレビランキング
     */
    private List<Map<String, String>> getDailyRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.DAILY_RANK_LAST_INSERT);
        List<Map<String, String>> list = new ArrayList<>();
        //今日のテレビランキング一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectDailyRankListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            DailyRankWebClient webClient = new DailyRankWebClient(mContext);
            int ageReq = 1;
            int upperPageLimit = 1;
            String lowerPageLimit = "";
            int pagerOffset = 1;
            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getDailyRankApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, this);
        }
        return list;
    }

    /**
     * ビデオランキング情報取得.
     *
     * @param limit   レスポンス最大件数
     * @param offset  取得位置
     * @param filter  フィルター
     * @param ageReq  年齢設定値
     * @param genreId ジャンルID
     * @param type    コンテンツタイプ
     * @param sort    ソート指定
     * @return ビデオランキング情報
     */
    private List<Map<String, String>> getVideoRankListData(final int limit, final int offset,
                                                           final String filter, final int ageReq,
                                                           final String genreId, final String type,
                                                           final String sort) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_RANK_LAST_INSERT);
        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            RankingTopDataManager rankingTopDataManager = new RankingTopDataManager(mContext);
            list = rankingTopDataManager.selectVideoRankListData();
        } else {
            //通信クラスにデータ取得要求を出す
            ContentsListPerGenreWebClient webClient = new ContentsListPerGenreWebClient(mContext);
            webClient.getContentsListPerGenreApi(limit, offset,
                    filter, ageReq, genreId, type, sort, this);
        }
        return list;
    }

    /**
     * CH一覧取得.
     *
     * @param limit  レスポンスの最大件数
     * @param offset 取得位置(1～)
     * @param filter release、testa、demo ※指定なしの場合release
     * @param type   dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void getChannelList(final int limit, final int offset, final String filter, final int type) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.CHANNEL_LAST_UPDATE);
        if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
            dateUtils.addLastProgramDate(DateUtils.CHANNEL_LAST_UPDATE);
            ChannelWebClient mChannelList = new ChannelWebClient(mContext);
            mChannelList.getChannelApi(limit, offset, filter, JsonConstants.DISPLAY_TYPE[type], (ChannelWebClient.ChannelJsonParserCallback) this);
        }
    }

    /**
     * 視聴中ビデオ一覧取得.
     *
     * @param pagerOffset 取得位置
     */
    private void getWatchListenVideoData(final int pagerOffset) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.WATCHING_VIDEO_LIST_LAST_INSERT);

        // クリップキー一覧を取得
        if (mRequiredClipKeyList) {
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
        }

        //視聴中ビデオ一覧のDB保存履歴と、有効期間を確認
        if (lastDate == null || lastDate.length() < 1 || dateUtils.isBeforeLimitDate(lastDate)) {
            WatchListenVideoWebClient webClient = new WatchListenVideoWebClient(mContext);
            //TODO：仮設定値
            int ageReq = 1;
            int upperPageLimit = 1;
            int lowerPageLimit = 1;
            String pagerDirection = "";

            webClient.getWatchListenVideoApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, pagerDirection, (WatchListenVideoWebClient.WatchListenVideoJsonParserCallback) this);
        }
    }

    /**
     * 購入済みVod一覧取得.
     */
    private void getRentalData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_RANK_LAST_INSERT);
        // クリップキー一覧を取得
        if (mRequiredClipKeyList) {
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
        }
        if (lastDate == null || lastDate.length() < 1 || dateUtils.isBeforeLimitDate(lastDate)) {
            //レンタル一覧取得
            RentalVodListWebClient webClient = new RentalVodListWebClient(mContext);
            webClient.getRentalVodListApi(this);
        }
    }

    /**
     * 購入済みCH一覧取得.
     */
    private void getChListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RENTAL_CHANNEL_LAST_UPDATE);
        if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
            RentalChListWebClient rentalChListWebClient = new RentalChListWebClient(mContext);
            rentalChListWebClient.getRentalChListApi((RentalChListWebClient.RentalChListJsonParserCallback) this);
        }
    }

    /**
     * ロールリスト取得.
     */
    private void getRoleListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.ROLELIST_LAST_UPDATE);
        if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
            dateUtils.addLastProgramDate(DateUtils.ROLELIST_LAST_UPDATE);
            RoleListWebClient roleListWebClient = new RoleListWebClient(mContext);
            roleListWebClient.getRoleListApi((RoleListWebClient.RoleListJsonParserCallback) this);
        }
    }

    /**
     * ジャンル一覧取得.
     */
    private void getGenreListDataRequest() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
        if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データの有効期限切れなら通信で取得
            GenreListWebClient webClient = new GenreListWebClient(mContext);
            webClient.getGenreListApi(this);
        }
    }

    /**
     * 番組一覧データをDBに格納する.
     *
     * @param tvScheduleList 番組一覧データ
     */
    private void setStructDB(final TvScheduleList tvScheduleList) {
        mTvScheduleList = tvScheduleList;
        sendTvScheduleListData(mTvScheduleList.geTvsList());
        //DB保存
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, TV_SCHEDULE_LIST);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * デイリーランキングデータをDBに格納する.
     *
     * @param dailyRankList デイリーランキングデータ
     */
    private void setStructDB(final DailyRankList dailyRankList) {
        mDailyRankList = dailyRankList;
        sendDailyRankListData(mDailyRankList.getDrList());
        //DB保存
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, DAILY_RANK_LIST);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * ビデオランキングデータをDBに格納する.
     *
     * @param videoRankList ビデオランキングデータ
     */
    private void setStructDB(final VideoRankList videoRankList) {
        mVideoRankList = videoRankList;
        sendVideoRankListData(mVideoRankList.getVrList());
        //DB保存
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, VIDEO_RANK_LIST);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * チャンネルリストデータをDBに格納する.
     *
     * @param channelList チャンネルリストデータ
     */
    private void setStructDB(final ChannelList channelList) {
        mChannelList = channelList;
        //DB保存
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, CHANNEL_LIST);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * クリップ[テレビ]一覧データをDBに格納する.
     *
     * @param tvClipList クリップ[テレビ]一覧データ
     */
    private void setStructDB(final TvClipList tvClipList) {
        mTvClipList = tvClipList;
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
        sendTvClipListData(mTvClipList.getVcList());
        //DB保存
//        Handler handler = new Handler();
//        try {
//            DbThread t = new DbThread(handler, this, TV_CLIP_LIST);
//            t.start();
//        } catch (Exception e) {
//            DTVTLogger.debug(e);
//        }
    }

    /**
     * クリップ[ビデオ]一覧データをDBに格納する.
     *
     * @param vodClipList クリップ[ビデオ]一覧データ
     */
    private void setStructDB(final VodClipList vodClipList) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
        mVodClipList = vodClipList;
        sendVodClipListData(mVodClipList.getVcList());
        //DB保存
//        Handler handler = new Handler();
//        try {
//            DbThread t = new DbThread(handler, this, VOD_CLIP_LIST);
//            t.start();
//        } catch (Exception e) {
//            DTVTLogger.debug(e);
//        }
    }

    /**
     * 視聴中ビデオ一覧データをDBに格納する.
     *
     * @param watchListenVideoList 視聴中ビデオ一覧用データ
     */
    private void setStructDB(final WatchListenVideoList watchListenVideoList) {
        mWatchListenVideoList = watchListenVideoList;
        //DB保存
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, WATCH_LISTEN_VIDEO_LIST);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * 購入済みVod一覧データをDBに格納する.
     *
     * @param purchasedVodListResponse 視聴中ビデオ一覧用データ
     */
    private void setStructDB(final PurchasedVodListResponse purchasedVodListResponse) {
        mPurchasedVodListResponse = purchasedVodListResponse;
        //DB保存
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, RENTAL_VOD_LIST);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * 購入済みCh一覧データをDBに格納する.
     *
     * @param purchasedChListResponse 視聴中ビデオ一覧用データ
     */
    private void setStructDB(final PurchasedChListResponse purchasedChListResponse) {
        mPurchasedChListResponse = purchasedChListResponse;
        //DB保存
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, RENTAL_CH_LIST);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * ロールリストをDBに格納する.
     *
     * @param roleListResponse ロールリスト
     */
    private void setStructDB(final RoleListResponse roleListResponse) {
        mRoleListMetaDataList = roleListResponse.getRoleList();
        //DB保存
        Handler handler = new Handler();
        try {
            DbThread t = new DbThread(handler, this, ROLE_ID_LIST);
            t.start();
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * レコメンドのテレビ情報のコールバック.
     *
     * @param recommendContentInfoList テレビレコメンド情報
     */
    @Override
    public void RecommendChannelCallback(final List<ContentsData> recommendContentInfoList) {
        if (recommendContentInfoList != null && recommendContentInfoList.size() > 0) {
            //送られてきたデータをアクティビティに渡す
            sendRecommendChListData(recommendContentInfoList);
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            List<ContentsData> recommendChannelInfoList;
            RecommendDataProvider recommendDataProvider = new RecommendDataProvider(mContext);
            recommendChannelInfoList = recommendDataProvider.getRecommendListDataCache(
                    DateUtils.RECOMMEND_CH_LAST_INSERT, RecommendDataProvider.TV_NO,
                    SearchConstants.RecommendList.FIRST_POSITION,
                    SearchConstants.RecommendList.RECOMMEND_PRELOAD_COUNT);
            sendRecommendChListData(recommendChannelInfoList);
        }
    }

    /**
     * レコメンドのビデオ情報のコールバック.
     *
     * @param recommendContentInfoList ビデオレコメンド情報
     */
    @Override
    public void RecommendVideoCallback(final List<ContentsData> recommendContentInfoList) {
        if (recommendContentInfoList != null && recommendContentInfoList.size() > 0) {
            //送られてきたデータをアクティビティに渡す
            sendRecommendVdListData(recommendContentInfoList);
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            List<ContentsData> recommendVodInfoList;
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            RecommendDataProvider recommendDataProvider = new RecommendDataProvider(mContext);
            recommendVodInfoList = recommendDataProvider.getRecommendListDataCache(
                    DateUtils.RECOMMEND_VD_LAST_INSERT, RecommendDataProvider.VIDEO_NO,
                    SearchConstants.RecommendList.FIRST_POSITION,
                    SearchConstants.RecommendList.RECOMMEND_PRELOAD_COUNT);
            sendRecommendVdListData(recommendVodInfoList);
        }
    }

    /**
     * レコメンドのDTV情報のコールバック.
     *
     * @param recommendContentInfoList DTVレコメンド情報
     */
    @Override
    public void RecommendDTVCallback(final List<ContentsData> recommendContentInfoList) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    /**
     * レコメンドのdアニメ情報のコールバック.
     *
     * @param recommendContentInfoList dアニメレコメンド情報
     */
    @Override
    public void RecommendDAnimeCallback(final List<ContentsData> recommendContentInfoList) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    /**
     * レコメンドのDチャンネル情報のコールバック.
     *
     * @param recommendContentInfoList Dチャンネルのレコメンド情報
     */
    @Override
    public void RecommendDChannelCallback(final List<ContentsData> recommendContentInfoList) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    /**
     * レコメンドのエラーのコールバック.
     */
    @Override
    public void RecommendNGCallback() {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @Override
    public List<Map<String, String>> dbOperation(final int operationId) throws Exception {
        DateUtils dateUtils = new DateUtils(mContext);
        switch (operationId) {
            case ROLE_ID_LIST:
                dateUtils.addLastDate(DateUtils.ROLELIST_LAST_UPDATE);
                RoleListInsertDataManager roleListInsertDataManager = new RoleListInsertDataManager(mContext);
                roleListInsertDataManager.insertRoleList(mRoleListMetaDataList);
                break;
            case CHANNEL_LIST:
                dateUtils.addLastDate(DateUtils.CHANNEL_LAST_UPDATE);
                ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(mContext);
                channelInsertDataManager.insertChannelInsertList(mChannelList, JsonConstants.DISPLAY_TYPE[DEFAULT_CHANNEL_DISPLAY_TYPE]);
                break;
            case TV_SCHEDULE_LIST:
                dateUtils.addLastDate(DateUtils.TV_SCHEDULE_LAST_INSERT);
                TvScheduleInsertDataManager tvScheduleInsertDataManager = new TvScheduleInsertDataManager(mContext);
                tvScheduleInsertDataManager.insertTvScheduleInsertList(mTvScheduleList);
                break;
            case DAILY_RANK_LIST:
                dateUtils.addLastDate(DateUtils.DAILY_RANK_LAST_INSERT);
                DailyRankInsertDataManager dailyRankInsertDataManager = new DailyRankInsertDataManager(mContext);
                dailyRankInsertDataManager.insertDailyRankInsertList(mDailyRankList);
                break;
            case VIDEO_RANK_LIST:
                dateUtils.addLastDate(DateUtils.VIDEO_RANK_LAST_INSERT);
                VideoRankInsertDataManager videoRankInsertDataManager = new VideoRankInsertDataManager(mContext);
                videoRankInsertDataManager.insertVideoRankInsertList(mVideoRankList);
                break;
            case TV_CLIP_LIST:
//                dateUtils.addLastDate(DateUtils.TV_LAST_INSERT);
//                TvClipInsertDataManager tvClipInsertDataManager = new TvClipInsertDataManager(mContext);
//                tvClipInsertDataManager.insertTvClipInsertList(tvClipList);
                break;
            case VOD_CLIP_LIST:
//                dateUtils.addLastDate(DateUtils.VOD_LAST_INSERT);
//                VodClipInsertDataManager vodClipInsertDataManager = new VodClipInsertDataManager(mContext);
//                vodClipInsertDataManager.insertVodClipInsertList(mVodClipList);
                break;
            case WATCH_LISTEN_VIDEO_LIST:
                dateUtils.addLastDate(DateUtils.WATCHING_VIDEO_LIST_LAST_INSERT);
                WatchListenVideoDataManager watchListenVideoDataManager = new WatchListenVideoDataManager(mContext);
                watchListenVideoDataManager.insertWatchListenVideoInsertList(mWatchListenVideoList);
                break;
            case RENTAL_CH_LIST:
                dateUtils.addLastDate(DateUtils.RENTAL_CHANNEL_LAST_UPDATE);
                RentalListInsertDataManager rentalChListInsertDataManager = new RentalListInsertDataManager(mContext);
                rentalChListInsertDataManager.insertChRentalListInsertList(mPurchasedChListResponse);
                break;
            case RENTAL_VOD_LIST:
                dateUtils.addLastDate(DateUtils.VIDEO_RANK_LAST_INSERT);
                RentalListInsertDataManager rentalListInsertDataManager = new RentalListInsertDataManager(mContext);
                rentalListInsertDataManager.insertRentalListInsertList(mPurchasedVodListResponse);
                break;
            default:
                break;
        }
        return null;
    }
}