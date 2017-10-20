/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendChInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendVdInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VodClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVdList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendChWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendVdWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WeeklyRankWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.CHANNEL_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.DAILY_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RECOMMEND_CH_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RECOMMEND_VD_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VOD_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.WEEKLY_RANK_LAST_INSERT;

public class HomeDataProvider implements VodClipWebClient.VodClipJsonParserCallback,
        ChannelWebClient.ChannelJsonParserCallback,
        DailyRankWebClient.DailyRankJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback,
        WeeklyRankWebClient.WeeklyRankJsonParserCallback,
        RecommendChWebClient.RecommendChannelCallback,
        RecommendVdWebClient.RecommendVideoCallback
{

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
    public void onChannelJsonParsed(List<ChannelList> channelLists) {
        if (channelLists != null && channelLists.size() > 0) {
            ChannelList list = channelLists.get(0);
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
    public void onTvScheduleJsonParsed(List<TvScheduleList> tvScheduleList) {
        if (tvScheduleList != null && tvScheduleList.size() > 0) {
            TvScheduleList list = tvScheduleList.get(0);
//            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onWeeklyRankJsonParsed(List<WeeklyRankList> weeklyRankLists) {
        if (weeklyRankLists != null && weeklyRankLists.size() > 0) {
            WeeklyRankList list = weeklyRankLists.get(0);
//            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void RecommendChannelCallback(RecommendChList mRecommendChList) {
        if (mRecommendChList != null && mRecommendChList.getmRcList()!=null &&
                mRecommendChList.getmRcList().size() > 0) {
            setStructDB(mRecommendChList);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void RecommendVideoCallback(RecommendVdList mRecommendVdList) {
        if (mRecommendVdList != null && mRecommendVdList.getmRvList()!=null &&
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
        void ChannelListCallback(List<Map<String,String>> channelList);

        /**
         * デイリーランキング用コールバック
         *
         * @param dailyList
         */
        void DailyRankListCallback(List<Map<String,String>> dailyList);

        /**
         * ユーザ情報用コールバック
         *
         * @param userList
         */
        void UserInfoCallback(List<Map<String,String>> userList);

        /**
         * クリップリスト用コールバック
         *
         * @param clipList
         */
        void VodClipListCallback(List<Map<String,String>> clipList);

        /**
         * ビデオランキング用コールバック
         *
         * @param weeklyList
         */
        void VideoRankCallback(List<Map<String,String>> weeklyList);

        /**
         * おすすめ番組用コールバック
         *
         * @param recChList
         */
        void RecommendChannelCallback(List<Map<String,String>> recChList);

        /**
         * おすすめビデオ用コールバック
         *
         * @param recVdList
         */
        void RecommemdVideoCallback(List<Map<String,String>> recVdList);
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
        //NOW ON AIR
        List<Map<String, String>> channelListData = getChannelListData();
        if(channelListData != null && channelListData.size() > 0){
            sendChannelListData(channelListData);
        }
        //おすすめ番組
        List<Map<String, String>> recommendChListData = getRecommendChListData();
        if(recommendChListData != null && recommendChListData.size() > 0){
            sendRecommendChListData(recommendChListData);
        }
        //おすすめビデオ
        List<Map<String, String>> recommendVdListData = getRecommendVdListData();
        if(recommendVdListData != null && recommendVdListData.size() > 0){
            sendRecommendVdListData(recommendVdListData);
        }
        //今日のテレビランキング
        List<Map<String, String>> dailyRankList = getDailyRankListData();
        if(dailyRankList != null && dailyRankList.size() > 0){
            sendDailyRankListData(dailyRankList);
        }
        /*//ビデオランキング
        List<Map<String, String>> VideoRankList = getDailyRankListData();
        if(VideoRankList != null && VideoRankList.size() > 0){
            sendVideoRankListData(VideoRankList);
        }*/
        //クリップ
        List<Map<String, String>> vodClipList = getVodClipListData();
        if(vodClipList != null && vodClipList.size() > 0){
            sendVodClipListData(vodClipList);
        }
    }

    /**
     * NOW ON AIRをHomeActivityに送る
     *
     * @param list
     */
    public void sendChannelListData(List<Map<String, String>> list) {
        apiDataProviderCallback.ChannelListCallback(list);
    }

    /**
     * おすすめ番組をHomeActivityに送る
     *
     * @param list
     */
    public void sendRecommendChListData(List<Map<String, String>> list) {
        apiDataProviderCallback.RecommendChannelCallback(list);
    }

    /**
     * おすすめビデオをHomeActivityに送る
     *
     * @param list
     */
    public void sendRecommendVdListData(List<Map<String, String>> list) {
        apiDataProviderCallback.RecommemdVideoCallback(list);
    }

    /**
     * 今日のランキングをHomeActivityに送る
     *
     * @param list
     */
    public void sendDailyRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.DailyRankListCallback(list);
    }

    /**
     * ビデオランキングをHomeActivityに送る
     *
     * @param list
     */
    public void sendVideoRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.VideoRankCallback(list);
    }

    /**
     * VodクリップリストをHomeActivityに送る
     *
     * @param list
     */
    public void sendVodClipListData(List<Map<String, String>> list) {
        apiDataProviderCallback.VodClipListCallback(list);
    }

    /**
     * ユーザ情報をHomeActivityに送る
     *
     * @param list
     */
    public void sendUserInfoListData(List<Map<String, String>> list) {
        apiDataProviderCallback.UserInfoCallback(list);
    }

    private List<Map<String, String>> getChannelListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(CHANNEL_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectChannelListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            ChannelWebClient webClient = new ChannelWebClient();
            int ageReq = 1;
            int upperPageLimit = 1;
            String lowerPageLimit = "";
            String pagerOffset = "";
            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getChannelApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, this);
        }
        return list;
    }

    private List<Map<String, String>> getRecommendChListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(RECOMMEND_CH_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
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

    private List<Map<String, String>> getRecommendVdListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(RECOMMEND_VD_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectRecommendVdListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            RecommendVdWebClient mRecommendChWebClient = new RecommendVdWebClient(this);
            mRecommendChWebClient.getRecommendChannelApi();
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
            webClient.getVodClipApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, this);
        }
        return list;
    }

    private List<Map<String, String>> getDailyRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DAILY_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
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

    private List<Map<String, String>> getWeeklyRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(WEEKLY_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
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


    /**
     * チャンネル一覧データをDBに格納する
     *
     * @param channelList
     */
    public void setStructDB(ChannelList channelList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(CHANNEL_LAST_INSERT);
        ChannelInsertDataManager dataManager = new ChannelInsertDataManager(mContext);
        dataManager.insertChannelInsertList(channelList);
        sendChannelListData(channelList.getClList());
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
        sendRecommendChListData(recommendVdList.getmRvList());
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
