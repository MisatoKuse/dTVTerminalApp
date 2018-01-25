/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.datamanager.WatchListenVideoDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RoleListInsertDataManager;
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
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
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
        RecommendDataProvider.RecommendApiDataProviderCallback {

    private Context mContext = null;

    private ApiDataProviderCallback mApiDataProviderCallback = null;

    //Home画面ではチャンネルリストを使用しないため"全て"を]設定する
    private static final int DEFAULT_CHANNEL_DISPLAY_TYPE = 0;

    @Override
    public void onTvClipJsonParsed(List<TvClipList> tvClipLists) {
        if (tvClipLists != null && tvClipLists.size() > 0) {
            TvClipList list = tvClipLists.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onVodClipJsonParsed(List<VodClipList> vodClipLists) {
        if (vodClipLists != null && vodClipLists.size() > 0) {
            VodClipList list = vodClipLists.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onTvScheduleJsonParsed(List<TvScheduleList> tvScheduleList) {
        if (tvScheduleList != null && tvScheduleList.size() > 0) {
            TvScheduleList list = tvScheduleList.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onDailyRankJsonParsed(List<DailyRankList> dailyRankLists) {
        if (dailyRankLists != null && dailyRankLists.size() > 0) {
            DailyRankList list = dailyRankLists.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onContentsListPerGenreJsonParsed(List<VideoRankList> contentsListPerGenre) {
        if (contentsListPerGenre != null && contentsListPerGenre.size() > 0) {
            VideoRankList list = contentsListPerGenre.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onWatchListenVideoJsonParsed(List<WatchListenVideoList> watchListenVideoList) {
        if (watchListenVideoList != null && watchListenVideoList.size() > 0) {
            WatchListenVideoList list = watchListenVideoList.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onRentalVodListJsonParsed(PurchasedVodListResponse RentalVodListResponse) {
        if (RentalVodListResponse != null) {
            setStructDB(RentalVodListResponse);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onRentalChListJsonParsed(PurchasedChListResponse RentalChListResponse) {
        if (RentalChListResponse != null) {
            setStructDB(RentalChListResponse);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onChannelJsonParsed(List<ChannelList> channelLists) {
        if (channelLists != null && channelLists.size() > 0) {
            ChannelList list = channelLists.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onRoleListJsonParsed(RoleListResponse roleListResponse) {
        if (roleListResponse != null) {
            setStructDB(roleListResponse);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
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
        protected Void doInBackground(Void... voids) {
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
            //データ取得のみ(API取得～DB保存までの処理を新設するか検討中)
            //ジャンルID(ビデオ一覧)一覧取得
            VideoGenreProvider videoGenreProvider = new VideoGenreProvider(mContext);
            videoGenreProvider.getGenreListDataRequest();

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
        mApiDataProviderCallback.tvScheduleListCallback(setHomeContentData(list));
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
        mApiDataProviderCallback.dailyRankListCallback(setHomeContentData(list));
    }

    /**
     * ビデオランキングをHomeActivityに送る.
     *
     * @param list ビデオランキング
     */
    private void sendVideoRankListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.videoRankCallback(setHomeContentData(list));
    }

    /**
     * クリップ[テレビ]リストをHomeActivityに送る.
     *
     * @param list クリップ[テレビ]リスト
     */
    private void sendTvClipListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.tvClipListCallback(setHomeContentData(list));
    }

    /**
     * クリップ[ビデオ]リストをHomeActivityに送る.
     *
     * @param list クリップ[ビデオ]リスト
     */
    private void sendVodClipListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.vodClipListCallback(setHomeContentData(list));
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     *
     * @param mapList コンテンツリストデータ
     * @return dataList ListView表示用データ
     */
    private List<ContentsData> setHomeContentData(
            final List<Map<String, String>> mapList) {
        List<ContentsData> rankingContentsDataList = new ArrayList<>();

        ContentsData rankingContentInfo;

        for (int i = 0; i < mapList.size(); i++) {
            rankingContentInfo = new ContentsData();
            rankingContentInfo.setTime(mapList.get(i).get(JsonConstants.META_RESPONSE_DISPLAY_START_DATE));
            rankingContentInfo.setTitle(mapList.get(i).get(JsonConstants.META_RESPONSE_TITLE));
            rankingContentInfo.setThumURL(mapList.get(i).get(JsonConstants.META_RESPONSE_THUMB_448));
            String thumbUrl = rankingContentInfo.getThumURL();
            String title = rankingContentInfo.getTitle();
            if (title == null || title.length() < 1) {
                rankingContentInfo.setTitle(mapList.get(i).get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE));
            }
            if (thumbUrl == null || thumbUrl.length() < 1) {
                //レコメンドからのデータはキーが違うため再取得する
                rankingContentInfo.setThumURL(mapList.get(i).get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1));
            }

            rankingContentsDataList.add(rankingContentInfo);
            DTVTLogger.info("RankingContentInfo " + rankingContentInfo.getRank());
        }

        return rankingContentsDataList;
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
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectTvClipHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            TvClipWebClient webClient = new TvClipWebClient(mContext);
            int ageReq = 1;
            int upperPageLimit = 1;
            int lowerPageLimit = 1;
            int pagerOffset = 1;
            String pagerDirection = "";

            webClient.getTvClipApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, pagerDirection, this);
        }
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
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectVodClipHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            VodClipWebClient webClient = new VodClipWebClient(mContext);
            int ageReq = 1;
            int upperPageLimit = 1;
            int lowerPageLimit = 1;
            int pagerOffset = 1;
            String pagerDirection = "";

            webClient.getVodClipApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, pagerDirection, this);
        }
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
    public void getRoleListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.ROLELIST_LAST_UPDATE);
        if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
            dateUtils.addLastProgramDate(DateUtils.ROLELIST_LAST_UPDATE);
            RoleListWebClient roleListWebClient = new RoleListWebClient(mContext);
            roleListWebClient.getRoleListApi((RoleListWebClient.RoleListJsonParserCallback) this);
        }
    }

    /**
     * 番組一覧データをDBに格納する.
     *
     * @param tvScheduleList 番組一覧データ
     */
    private void setStructDB(final TvScheduleList tvScheduleList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.TV_SCHEDULE_LAST_INSERT);
        TvScheduleInsertDataManager dataManager = new TvScheduleInsertDataManager(mContext);
        dataManager.insertTvScheduleInsertList(tvScheduleList);
        sendTvScheduleListData(tvScheduleList.geTvsList());
    }

    /**
     * デイリーランキングデータをDBに格納する.
     *
     * @param dailyRankList デイリーランキングデータ
     */
    private void setStructDB(final DailyRankList dailyRankList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.DAILY_RANK_LAST_INSERT);
        DailyRankInsertDataManager dataManager = new DailyRankInsertDataManager(mContext);
        dataManager.insertDailyRankInsertList(dailyRankList);
        sendDailyRankListData(dailyRankList.getDrList());
    }

    /**
     * ビデオランキングデータをDBに格納する.
     *
     * @param videoRankList ビデオランキングデータ
     */
    private void setStructDB(final VideoRankList videoRankList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.VIDEO_RANK_LAST_INSERT);
        VideoRankInsertDataManager dataManager = new VideoRankInsertDataManager(mContext);
        dataManager.insertVideoRankInsertList(videoRankList);
        sendVideoRankListData(videoRankList.getVrList());
    }

    /**
     * チャンネルリストデータをDBに格納する.
     *
     * @param channelList 視聴中ビデオ一覧用データ
     */
    private void setStructDB(final ChannelList channelList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.CHANNEL_LAST_UPDATE);

        ChannelInsertDataManager dataManager = new ChannelInsertDataManager(mContext);
        dataManager.insertChannelInsertList(channelList, JsonConstants.DISPLAY_TYPE[DEFAULT_CHANNEL_DISPLAY_TYPE]);
    }

    /**
     * クリップ[テレビ]一覧データをDBに格納する
     *
     * @param tvClipList クリップ[テレビ]一覧データ
     */
    private void setStructDB(TvClipList tvClipList) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//        DateUtils dateUtils = new DateUtils(mContext);
//        dateUtils.addLastDate(DateUtils.TV_LAST_INSERT);
//        TvClipInsertDataManager dataManager = new TvClipInsertDataManager(mContext);
//        dataManager.insertTvClipInsertList(tvClipList);
        sendTvClipListData(tvClipList.getVcList());
    }

    /**
     * クリップ[ビデオ]一覧データをDBに格納する.
     *
     * @param vodClipList クリップ[ビデオ]一覧データ
     */
    private void setStructDB(final VodClipList vodClipList) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//        DateUtils dateUtils = new DateUtils(mContext);
//        dateUtils.addLastDate(DateUtils.VOD_LAST_INSERT);
//        VodClipInsertDataManager dataManager = new VodClipInsertDataManager(mContext);
//        dataManager.insertVodClipInsertList(vodClipList);
        sendVodClipListData(vodClipList.getVcList());
    }

    /**
     * 視聴中ビデオ一覧データをDBに格納する.
     *
     * @param watchListenVideoList 視聴中ビデオ一覧用データ
     */
    private void setStructDB(final WatchListenVideoList watchListenVideoList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.WATCHING_VIDEO_LIST_LAST_INSERT);
        WatchListenVideoDataManager dataManager = new WatchListenVideoDataManager(mContext);
        dataManager.insertWatchListenVideoInsertList(watchListenVideoList);
    }

    /**
     * 購入済みVod一覧データをDBに格納する.
     *
     * @param purchasedVodListResponse 視聴中ビデオ一覧用データ
     */
    private void setStructDB(final PurchasedVodListResponse purchasedVodListResponse) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.VIDEO_RANK_LAST_INSERT);
        RentalListInsertDataManager dataManager = new RentalListInsertDataManager(mContext);
        dataManager.insertRentalListInsertList(purchasedVodListResponse);
    }

    /**
     * 購入済みCh一覧データをDBに格納する.
     *
     * @param purchasedChListResponse 視聴中ビデオ一覧用データ
     */
    private void setStructDB(final PurchasedChListResponse purchasedChListResponse) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.RENTAL_CHANNEL_LAST_UPDATE);
        RentalListInsertDataManager rentalChListInsertDataManager = new RentalListInsertDataManager(mContext);
        rentalChListInsertDataManager.insertChRentalListInsertList(purchasedChListResponse);
        //DB保存をThread化するためのサンプルとしてコピー　※後でまとめてThread化する
//            Handler handler = new Handler(); //チャンネル情報更新
//            try {
//                DbThread t = new DbThread(handler, this, CHANNEL_SELECT);
//                t.start();
//            } catch (Exception e) {
//                DTVTLogger.debug(e);
//            }
    }

    /**
     * ロールリストをDBに格納する.
     *
     * @param roleListResponse ロールリスト
     */
    private void setStructDB(final RoleListResponse roleListResponse) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.ROLELIST_LAST_UPDATE);
        ArrayList<RoleListMetaData> list = roleListResponse.getRoleList();
        RoleListInsertDataManager dataManager = new RoleListInsertDataManager(mContext);
        dataManager.insertRoleList(list);
    }

    /**
     * レコメンドのテレビ情報のコールバック.
     *
     * @param recommendContentInfoList テレビレコメンド情報
     */
    @Override
    public void RecommendChannelCallback(List<ContentsData> recommendContentInfoList) {
        //送られてきたデータをアクティビティに渡す
        sendRecommendChListData(recommendContentInfoList);
    }

    /**
     * レコメンドのビデオ情報のコールバック.
     *
     * @param recommendContentInfoList ビデオレコメンド情報
     */
    @Override
    public void RecommendVideoCallback(List<ContentsData> recommendContentInfoList) {
        //送られてきたデータをアクティビティに渡す
        sendRecommendVdListData(recommendContentInfoList);
    }

    /**
     * レコメンドのDTV情報のコールバック.
     *
     * @param recommendContentInfoList DTVレコメンド情報
     */
    @Override
    public void RecommendDTVCallback(List<ContentsData> recommendContentInfoList) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    /**
     * レコメンドのdアニメ情報のコールバック.
     *
     * @param recommendContentInfoList dアニメレコメンド情報
     */
    @Override
    public void RecommendDAnimeCallback(List<ContentsData> recommendContentInfoList) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    /**
     * レコメンドのDチャンネル情報のコールバック.
     *
     * @param recommendContentInfoList Dチャンネルのレコメンド情報
     */
    @Override
    public void RecommendDChannelCallback(List<ContentsData> recommendContentInfoList) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    /**
     * レコメンドのエラーのコールバック.
     */
    @Override
    public void RecommendNGCallback() {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }
}