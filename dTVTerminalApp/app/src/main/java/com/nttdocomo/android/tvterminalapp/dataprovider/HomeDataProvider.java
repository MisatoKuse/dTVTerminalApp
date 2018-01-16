/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VideoRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RankingTopDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ホーム画面用データプロバイダ.
 */
public class HomeDataProvider implements
        TvClipWebClient.TvClipJsonParserCallback,
        VodClipWebClient.VodClipJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback,
        DailyRankWebClient.DailyRankJsonParserCallback,
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback,
        RecommendDataProvider.RecommendApiDataProviderCallback {

    private Context mContext = null;

    private ApiDataProviderCallback mApiDataProviderCallback = null;

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

    /**
     * Home画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {
        /**
         * チャンネル一覧用コールバック.
         *
         * @param channelList
         */
        void tvScheduleListCallback(List<ContentsData> channelList);

        /**
         * デイリーランキング用コールバック.
         *
         * @param dailyList
         */
        void dailyRankListCallback(List<ContentsData> dailyList);

        /**
         * ユーザ情報用コールバック.
         *
         * @param userList
         */
        void userInfoCallback(List<Map<String, String>> userList);

        /**
         * クリップ[テレビ]リスト用コールバック.
         *
         * @param tvClipList
         */
        void tvClipListCallback(List<ContentsData> tvClipList);

        /**
         * クリップ[ビデオ]リスト用コールバック.
         *
         * @param vodClipList
         */
        void vodClipListCallback(List<ContentsData> vodClipList);

        /**
         * ビデオランキング用コールバック.
         *
         * @param videoRankList
         */
        void videoRankCallback(List<ContentsData> videoRankList);

        /**
         * おすすめ番組用コールバック.
         *
         * @param recChList
         */
        void recommendChannelCallback(List<ContentsData> recChList);

        /**
         * おすすめビデオ用コールバック.
         *
         * @param recVdList
         */
        void recommendVideoCallback(List<ContentsData> recVdList);
    }

    /**
     * コンストラクタ.
     *
     * @param mContext
     */
    public HomeDataProvider(final Context mContext) {
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
            List<Map<String, String>> VideoRankList = getVideoRankListData();
            if (VideoRankList != null && VideoRankList.size() > 0) {
                sendVideoRankListData(VideoRankList);
            }
            //クリップ[テレビ]
            List<Map<String, String>> tvClipList = getTvClipListData();
            if (tvClipList != null && tvClipList.size() > 0) {
                sendTvClipListData(tvClipList);
            }
            //クリップ[ビデオ]
            List<Map<String, String>> vodClipList = getVodClipListData();
            if (vodClipList != null && vodClipList.size() > 0) {
                sendVodClipListData(vodClipList);
            }
            return null;
        }
    };

    /**
     * NOW ON AIRをHomeActivityに送る.
     *
     * @param list
     */
    public void sendTvScheduleListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.tvScheduleListCallback(setHomeContentData(list));
    }

    /**
     * おすすめ番組をHomeActivityに送る.
     *
     * @param list おすすめ番組のコンテンツ情報
     */
    public void sendRecommendChListData(List<ContentsData> list) {
        mApiDataProviderCallback.recommendChannelCallback(list);
    }

    /**
     * おすすめビデオをHomeActivityに送る.
     *
     * @param list おすすめビデオのコンテンツ情報
     */
    public void sendRecommendVdListData(List<ContentsData> list) {
        mApiDataProviderCallback.recommendVideoCallback(list);
    }

    /**
     * 今日のランキングをHomeActivityに送る.
     *
     * @param list
     */
    public void sendDailyRankListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.dailyRankListCallback(setHomeContentData(list));
    }

    /**
     * ビデオランキングをHomeActivityに送る.
     *
     * @param list
     */
    public void sendVideoRankListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.videoRankCallback(setHomeContentData(list));
    }

    /**
     * クリップ[テレビ]リストをHomeActivityに送る.
     *
     * @param list
     */
    public void sendTvClipListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.tvClipListCallback(setHomeContentData(list));
    }

    /**
     * クリップ[ビデオ]リストをHomeActivityに送る.
     *
     * @param list
     */
    public void sendVodClipListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.vodClipListCallback(setHomeContentData(list));
    }

    /**
     * ユーザ情報をHomeActivityに送る.
     *
     * @param list
     */
    public void sendUserInfoListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.userInfoCallback(list);
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
            rankingContentInfo.setTime(mapList.get(i).get(JsonContents.META_RESPONSE_DISPLAY_START_DATE));
            rankingContentInfo.setTitle(mapList.get(i).get(JsonContents.META_RESPONSE_TITLE));
            rankingContentInfo.setThumURL(mapList.get(i).get(JsonContents.META_RESPONSE_THUMB_448));
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
     * @return
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
            TvScheduleWebClient webClient = new TvScheduleWebClient();
            int[] ageReq = {1};
            String[] upperPageLimit = {WebApiBasePlala.DATE_NOW};
            String lowerPageLimit = "";
            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
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
     * @return
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
            TvClipWebClient webClient = new TvClipWebClient();
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
     * @return
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
            VodClipWebClient webClient = new VodClipWebClient();
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
     * @return
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
            DailyRankWebClient webClient = new DailyRankWebClient();
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
     * @return ビデオランキング情報
     */
    private List<Map<String, String>> getVideoRankListData() {
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
            ContentsListPerGenreWebClient webClient = new ContentsListPerGenreWebClient();
            int limit = 1;
            int offset = 1;
            String filter = "";
            int ageReq = 1;
            String genreId = "";
            String type = "";
            String sort = "";
            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getContentsListPerGenreApi(limit, offset,
                    filter, ageReq, genreId, type, sort, this);
        }
        return list;
    }

    /**
     * チャンネル一覧データをDBに格納する.
     *
     * @param tvScheduleList
     */
    private void setStructDB(TvScheduleList tvScheduleList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.TV_SCHEDULE_LAST_INSERT);
        TvScheduleInsertDataManager dataManager = new TvScheduleInsertDataManager(mContext);
        dataManager.insertTvScheduleInsertList(tvScheduleList);
        sendTvScheduleListData(tvScheduleList.geTvsList());
    }

    /**
     * デイリーランキングデータをDBに格納する.
     *
     * @param dailyRankList
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
     * @param videoRankList
     */
    private void setStructDB(final VideoRankList videoRankList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.VIDEO_RANK_LAST_INSERT);
        VideoRankInsertDataManager dataManager = new VideoRankInsertDataManager(mContext);
        dataManager.insertVideoRankInsertList(videoRankList);
        sendVideoRankListData(videoRankList.getVrList());
    }

    /*
     * クリップ[テレビ]一覧データをDBに格納する
     *
     * @param tvClipList
     */
    public void setStructDB(TvClipList tvClipList) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//        DateUtils dateUtils = new DateUtils(mContext);
//        dateUtils.addLastDate(DateUtils.TV_LAST_INSERT);
//        TvClipInsertDataManager dataManager = new TvClipInsertDataManager(mContext);
//        dataManager.insertTvClipInsertList(tvClipList);
        sendTvClipListData(tvClipList.getVcList());
    }

    /*
     * クリップ[ビデオ]一覧データをDBに格納する
     *
     * @param vodClipList
     */
    public void setStructDB(VodClipList vodClipList) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//        DateUtils dateUtils = new DateUtils(mContext);
//        dateUtils.addLastDate(DateUtils.VOD_LAST_INSERT);
//        VodClipInsertDataManager dataManager = new VodClipInsertDataManager(mContext);
//        dataManager.insertVodClipInsertList(vodClipList);
        sendVodClipListData(vodClipList.getVcList());
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