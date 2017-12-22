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
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendChInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendVdInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VideoRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VodClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RankingTopDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVdList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendChWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendVdWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.DAILY_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RECOMMEND_CH_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RECOMMEND_VD_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.TvSchedule_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VOD_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VIDEO_RANK_LAST_INSERT;

public class HomeDataProvider implements VodClipWebClient.VodClipJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback,
        DailyRankWebClient.DailyRankJsonParserCallback,
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback,
        RecommendChWebClient.RecommendChannelCallback,
        RecommendVdWebClient.RecommendVideoCallback {

    private Context mContext;

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
    public void RecommendChannelCallback(RecommendChList mRecommendChList) {
        if (mRecommendChList != null && mRecommendChList.getmRcList() != null &&
                mRecommendChList.getmRcList().size() > 0) {
            setStructDB(mRecommendChList);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void RecommendVideoCallback(RecommendVdList mRecommendVdList) {
        if (mRecommendVdList != null && mRecommendVdList.getmRvList() != null &&
                mRecommendVdList.getmRvList().size() > 0) {
            setStructDB(mRecommendVdList);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    /**
     * Home画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {
        /**
         * チャンネル一覧用コールバック
         *
         * @param channelList
         */
        void tvScheduleListCallback(List<ContentsData> channelList);

        /**
         * デイリーランキング用コールバック
         *
         * @param dailyList
         */
        void dailyRankListCallback(List<ContentsData> dailyList);

        /**
         * ユーザ情報用コールバック
         *
         * @param userList
         */
        void userInfoCallback(List<Map<String, String>> userList);

        /**
         * クリップリスト用コールバック
         *
         * @param clipList
         */
        void vodClipListCallback(List<ContentsData> clipList);

        /**
         * ビデオランキング用コールバック
         *
         * @param videoRankList
         */
        void videoRankCallback(List<ContentsData> videoRankList);

        /**
         * おすすめ番組用コールバック
         *
         * @param recChList
         */
        void recommendChannelCallback(List<ContentsData> recChList);

        /**
         * おすすめビデオ用コールバック
         *
         * @param recVdList
         */
        void recommendVideoCallback(List<ContentsData> recVdList);
    }

    private ApiDataProviderCallback apiDataProviderCallback;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public HomeDataProvider(Context mContext) {
        this.mContext = mContext;
        this.apiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * Activityからのデータ取得要求受付
     */
    public void getHomeData() {
        //非同期処理でデータの取得を行う
        homeDataDownloadTask.execute();
    }

    /**
     * 非同期処理でHOME画面に表示するデータの取得を行う
     */
    private AsyncTask<Void, Void, Void> homeDataDownloadTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            //NOW ON AIR
            List<Map<String, String>> tvScheduleListData = getTvScheduleListData();
            if (tvScheduleListData != null && tvScheduleListData.size() > 0) {
                sendTvScheduleListData(tvScheduleListData);
            }
            //おすすめ番組
            List<Map<String, String>> recommendChListData = getRecommendChListData();
            if (recommendChListData != null && recommendChListData.size() > 0) {
                sendRecommendChListData(recommendChListData);
            }
            //おすすめビデオ
            List<Map<String, String>> recommendVdListData = getRecommendVdListData();
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
            //クリップ
            List<Map<String, String>> vodClipList = getVodClipListData();
            if (vodClipList != null && vodClipList.size() > 0) {
                sendVodClipListData(vodClipList);
            }
            return null;
        }
    };

    /**
     * NOW ON AIRをHomeActivityに送る
     *
     * @param list
     */
    public void sendTvScheduleListData(List<Map<String, String>> list) {
        apiDataProviderCallback.tvScheduleListCallback(setHomeContentData(list));
    }

    /**
     * おすすめ番組をHomeActivityに送る
     *
     * @param list
     */
    public void sendRecommendChListData(List<Map<String, String>> list) {
        apiDataProviderCallback.recommendChannelCallback(setHomeContentData(list));
    }

    /**
     * おすすめビデオをHomeActivityに送る
     *
     * @param list
     */
    public void sendRecommendVdListData(List<Map<String, String>> list) {
        apiDataProviderCallback.recommendVideoCallback(setHomeContentData(list));
    }

    /**
     * 今日のランキングをHomeActivityに送る
     *
     * @param list
     */
    public void sendDailyRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.dailyRankListCallback(setHomeContentData(list));
    }

    /**
     * ビデオランキングをHomeActivityに送る
     *
     * @param list
     */
    public void sendVideoRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.videoRankCallback(setHomeContentData(list));
    }

    /**
     * VodクリップリストをHomeActivityに送る
     *
     * @param list
     */
    public void sendVodClipListData(List<Map<String, String>> list) {
        apiDataProviderCallback.vodClipListCallback(setHomeContentData(list));
    }

    /**
     * ユーザ情報をHomeActivityに送る
     *
     * @param list
     */
    public void sendUserInfoListData(List<Map<String, String>> list) {
        apiDataProviderCallback.userInfoCallback(list);
    }


    /**
     * 取得したリストマップをContentsDataクラスへ入れる
     *
     * @param mapList コンテンツリストデータ
     * @return dataList ListView表示用データ
     */
    private List<ContentsData> setHomeContentData(
            List<Map<String, String>> mapList) {
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
     * NOW ON AIR 情報取得
     */
    private List<Map<String, String>> getTvScheduleListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(TvSchedule_LAST_INSERT);

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
     * おすすめ番組情報取得
     */
    private List<Map<String, String>> getRecommendChListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(RECOMMEND_CH_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //おすすめ番組一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectRecommendChListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            RecommendChWebClient mRecommendChWebClient = new RecommendChWebClient(this);
            mRecommendChWebClient.getRecommendChannelApi();
        }
        return list;
    }

    /**
     * おすすめビデオ情報取得
     */
    private List<Map<String, String>> getRecommendVdListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(RECOMMEND_VD_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //おすすめビデオのDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectRecommendVdListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            RecommendVdWebClient mRecommendChWebClient = new RecommendVdWebClient(this);
            mRecommendChWebClient.getRecommendVideoApi();
        }
        return list;
    }

    /**
     * Vodクリップリストデータ取得開始
     */
    private List<Map<String, String>> getVodClipListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(VOD_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectClipHomeData();
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
     * 今日のテレビランキング情報取得
     */
    private List<Map<String, String>> getDailyRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DAILY_RANK_LAST_INSERT);

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
     * ビデオランキング情報取得
     */
    private List<Map<String, String>> getVideoRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(VIDEO_RANK_LAST_INSERT);

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
     * チャンネル一覧データをDBに格納する
     *
     * @param tvScheduleList
     */
    public void setStructDB(TvScheduleList tvScheduleList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(TvSchedule_LAST_INSERT);
        TvScheduleInsertDataManager dataManager = new TvScheduleInsertDataManager(mContext);
        dataManager.insertTvScheduleInsertList(tvScheduleList);
        sendTvScheduleListData(tvScheduleList.geTvsList());
    }

    /**
     * おすすめ番組をDBに保存する
     *
     * @param recommendChList
     */
    public void setStructDB(RecommendChList recommendChList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(RECOMMEND_CH_LAST_INSERT);
        RecommendChInsertDataManager dataManager = new RecommendChInsertDataManager(mContext);
        dataManager.insertRecommendChInsertList(recommendChList);
        sendRecommendChListData(recommendChList.getmRcList());
    }

    /**
     * おすすめビデオをDBに保存する
     *
     * @param recommendVdList
     */
    public void setStructDB(RecommendVdList recommendVdList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(RECOMMEND_VD_LAST_INSERT);
        RecommendVdInsertDataManager dataManager = new RecommendVdInsertDataManager(mContext);
        dataManager.insertRecommendVdInsertList(recommendVdList);
        sendRecommendVdListData(recommendVdList.getmRvList());
    }

    /**
     * デーリーランキングデータをDBに格納する
     *
     * @param dailyRankList
     */
    public void setStructDB(DailyRankList dailyRankList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DAILY_RANK_LAST_INSERT);
        DailyRankInsertDataManager dataManager = new DailyRankInsertDataManager(mContext);
        dataManager.insertDailyRankInsertList(dailyRankList);
        sendDailyRankListData(dailyRankList.getDrList());
    }

    /**
     * ビデオランキングデータをDBに格納する
     *
     * @param videoRankList
     */
    public void setStructDB(VideoRankList videoRankList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(VIDEO_RANK_LAST_INSERT);
        VideoRankInsertDataManager dataManager = new VideoRankInsertDataManager(mContext);
        dataManager.insertVideoRankInsertList(videoRankList);
        sendVideoRankListData(videoRankList.getVrList());
    }

    /**
     * Vodクリップ一覧データをDBに格納する
     *
     * @param vodClipList
     */
    public void setStructDB(VodClipList vodClipList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(VOD_LAST_INSERT);
        VodClipInsertDataManager dataManager = new VodClipInsertDataManager(mContext);
        dataManager.insertVodClipInsertList(vodClipList);
        sendVodClipListData(vodClipList.getVcList());
    }
}