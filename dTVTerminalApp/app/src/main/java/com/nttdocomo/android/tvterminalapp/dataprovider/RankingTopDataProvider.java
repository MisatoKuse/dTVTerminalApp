/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VideoRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.WeeklyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RankingTopDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WeeklyRankWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.DAILY_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VIDEO_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.WEEKLY_RANK_LAST_INSERT;

public class RankingTopDataProvider implements
        DailyRankWebClient.DailyRankJsonParserCallback,
        WeeklyRankWebClient.WeeklyRankJsonParserCallback{

    private Context mContext;

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
    public void onWeeklyRankJsonParsed(List<WeeklyRankList> weeklyRankLists) {
        if (weeklyRankLists != null && weeklyRankLists.size() > 0) {
            WeeklyRankList list = weeklyRankLists.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    /**
     * Ranking Top画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {

        /**
         * デイリーランキング用コールバック
         *
         * @param dailyHashMap
         */
        void DailyRankListCallback(List<Map<String, String>> dailyHashMap);

        /**
         * 週間ランキング用コールバック
         *
         * @param weeklyHashMap
         */
        void WeeklyRankCallback(List<Map<String, String>> weeklyHashMap);

        /**
         * ビデオランキング用コールバック
         *
         * @param videoHashMap
         */
        void VideoRankCallback(List<Map<String, String>> videoHashMap);
    }

    private ApiDataProviderCallback apiDataProviderCallback;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public RankingTopDataProvider(Context mContext) {
        this.mContext = mContext;
        this.apiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * Activityからのデータ取得要求受付
     */
    public void getRankingTopData() {
        //今日のランキング
        List<Map<String, String>> dailyRankList = getDailyRankListData();
        if(dailyRankList != null && dailyRankList.size() > 0){
            sendDailyRankListData(dailyRankList);
        }
        //週刊のランキング
        List<Map<String, String>> weeklyRankList = getWeeklyRankListData();
        if(weeklyRankList != null && weeklyRankList.size() > 0){
            sendWeeklyRankListData(weeklyRankList);
        }
        //ビデオのランキング
        List<Map<String, String>> videoRankList = getVideoRankListData();
        if(videoRankList != null && videoRankList.size() > 0){
            sendVideoRankListData(videoRankList);
        }
    }

    /**
     * 今日のランキングをRankingTopActivityに送る
     *
     * @param list
     */
    public void sendDailyRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.DailyRankListCallback(list);
    }

    /**
     * 週間ランキングリストをRankingTopActivityに送る
     *
     * @param list
     */
    public void sendWeeklyRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.WeeklyRankCallback(list);
    }

    /**
     * ビデオランキングリストをRankingTopActivityに送る
     *
     * @param list
     */
    public void sendVideoRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.WeeklyRankCallback(list);
    }

    /**
     * 今日のランキングデータを取得する
     *
     */
    private List<Map<String, String>> getDailyRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DAILY_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //今日のランキング一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
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

    private List<Map<String, String>> getWeeklyRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(WEEKLY_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectWeeklyRankListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            WeeklyRankWebClient webClient = new WeeklyRankWebClient();
            int limit = 1;
            int offset = 1;
            String filter = "";
            int ageReq = 1;
            String genreId = "";

            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getWeeklyRankApi(limit, offset,
                    filter, ageReq, genreId , this);
        }
        return list;
    }

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
            WeeklyRankWebClient webClient = new WeeklyRankWebClient();
            int limit = 1;
            int offset = 1;
            String filter = "";
            int ageReq = 1;
            String genreId = "";

            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getWeeklyRankApi(limit, offset,
                    filter, ageReq, genreId , this);
        }
        return list;
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
     * 週間ランキングリストをDBに格納する
     *
     * @param weeklyRankList
     */
    public void setStructDB(WeeklyRankList weeklyRankList) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
        WeeklyRankInsertDataManager dataManager = new WeeklyRankInsertDataManager(mContext);
        dataManager.insertWeeklyRankInsertList(weeklyRankList);
        sendWeeklyRankListData(weeklyRankList.getWrList());
    }

    /**
     * ビデオランキングリストをDBに格納する
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
}
