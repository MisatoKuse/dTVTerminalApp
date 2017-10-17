/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.beans.HomeBean;
import com.nttdocomo.android.tvterminalapp.beans.HomeBeanContent;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendChInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendVdInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VodClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.WeeklyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVdList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.plala.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.plala.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.plala.RecommendChWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.plala.RecommendVdWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.plala.TvScheduleWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.plala.VodClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.plala.WeeklyRankWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.CHANNEL_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.DAILY_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.TvSchedule_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VOD_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.WEEKLY_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_TITLE;

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

    @Override
    public void RecommendVideoCallback(RecommendVdList mRecommendVdList) {
        if (mRecommendVdList != null && mRecommendVdList.getmRvList()!=null &&
                mRecommendVdList.getmRvList().size() > 0) {
//            setStructDB(mRecommendVdList);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void RecommendChannelCallback(RecommendChList mRecommendChList) {
        if (mRecommendChList != null && mRecommendChList.getmRcList()!=null &&
                mRecommendChList.getmRcList().size() > 0) {
//            setStructDB(mRecommendChList);
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
         * @param homeBean
         */
        void ChannelListCallback(HomeBean homeBean);

        /**
         * デイリーランキング用コールバック
         *
         * @param homeBean
         */
        void DailyRankListCallback(HomeBean homeBean);

        /**
         * CH毎番組表用コールバック
         *
         * @param homeBean
         */
        void TvScheduleCallback(HomeBean homeBean);

        /**
         * ユーザ情報用コールバック
         *
         * @param homeBean
         */
        void UserInfoCallback(HomeBean homeBean);

        /**
         * クリップリスト用コールバック
         *
         * @param homeBean
         */
        void VodClipListCallback(HomeBean homeBean);

        /**
         * 週間ランキング用コールバック
         *
         * @param homeBean
         */
        void WeeklyRankCallback(HomeBean homeBean);

        /**
         * おすすめ番組用コールバック
         *
         * @param homeBean
         */
        void RecommendChannelCallback(HomeBean homeBean);

        /**
         * おすすめビデオ用コールバック
         *
         * @param homeBean
         */
        void RecommemdVideoCallback(HomeBean homeBean);
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

    public void getHomeData() {
        //Activityからのデータ取得要求受付

        List<Map<String, String>> vodClipList = getVodClipListData();
        if(vodClipList != null && vodClipList.size() > 0){
            sendVodClipListData(vodClipList);
        }
        List<Map<String, String>> channelList = getChannelListData();
        if(channelList != null && channelList.size() > 0){
            sendChannelListData(channelList);
        }
        List<Map<String, String>> dailyRankList = getDailyRankListData();
        if(dailyRankList != null && dailyRankList.size() > 0){
            sendDailyRankListData(dailyRankList);
        }
        List<Map<String, String>> tvScheduleList = getTvScheduleData();
        if(tvScheduleList != null && tvScheduleList.size() > 0){
            sendTvScheduleListData(tvScheduleList);
        }
        List<Map<String, String>> weeklyRankList = getWeeklyRankListData();
        if(weeklyRankList != null && weeklyRankList.size() > 0){
            sendWeeklyRankListData(weeklyRankList);
        }

    }

    public HomeBean makeHomeStruct(List<Map<String, String>> list) {
        HomeBean homeBean = new HomeBean();
        HomeBeanContent homeBeanContent = new HomeBeanContent();
        //Home用構造体を作成する
        return homeBean;
    }

    /**
     * CH一覧をHomeActivityに送る
     *
     * @param list
     */
    public void sendChannelListData(List<Map<String, String>> list) {
        HomeBean homeBean = new HomeBean();
        homeBean.setContentTypeName(mContext.getResources().getString(R.string.nav_menu_item_channel_list));
        HomeBeanContent homeBeanContent = new HomeBeanContent();
        List<HomeBeanContent> contents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            homeBeanContent.setContentTime(VODCLIP_LIST_THUMB);
            homeBeanContent.setContentName(VODCLIP_LIST_TITLE);
            homeBeanContent.setContentTime(VODCLIP_LIST_DISPLAY_START_DATE);
            homeBeanContent.setContentId(VODCLIP_LIST_DISP_TYPE);
            contents.add(homeBeanContent);
        }
        homeBean.setContentList(contents);
        apiDataProviderCallback.ChannelListCallback(homeBean);
    }

    /**
     * 今日のランキングをHomeActivityに送る
     *
     * @param list
     */
    public void sendDailyRankListData(List<Map<String, String>> list) {
        HomeBean homeBean = new HomeBean();
        homeBean.setContentTypeName(mContext.getResources().getString(R.string.daily_tv_ranking_title));
        HomeBeanContent homeBeanContent = new HomeBeanContent();
        List<HomeBeanContent> contents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            homeBeanContent.setContentSrcURL("http:"+list.get(i).get(VODCLIP_LIST_THUMB));
            homeBeanContent.setContentName(list.get(i).get(VODCLIP_LIST_TITLE));
            homeBeanContent.setContentTime(list.get(i).get(VODCLIP_LIST_DISPLAY_START_DATE));
            homeBeanContent.setContentId(list.get(i).get(VODCLIP_LIST_DISP_TYPE));
            contents.add(homeBeanContent);
        }
        homeBean.setContentList(contents);
        apiDataProviderCallback.DailyRankListCallback(homeBean);
    }

    /**
     * CH毎番組表をHomeActivityに送る
     *
     * @param list
     */
    public void sendTvScheduleListData(List<Map<String, String>> list) {
        HomeBean homeBean = new HomeBean();
        homeBean.setContentTypeName(mContext.getResources().getString(R.string.nav_menu_item_channel_list));
        HomeBeanContent homeBeanContent = new HomeBeanContent();
        List<HomeBeanContent> contents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            homeBeanContent.setContentSrcURL("http:"+list.get(i).get(VODCLIP_LIST_THUMB));
            homeBeanContent.setContentName(list.get(i).get(VODCLIP_LIST_TITLE));
            homeBeanContent.setContentTime(list.get(i).get(VODCLIP_LIST_DISPLAY_START_DATE));
            homeBeanContent.setContentId(list.get(i).get(VODCLIP_LIST_DISP_TYPE));
            contents.add(homeBeanContent);
        }
        homeBean.setContentList(contents);
        apiDataProviderCallback.TvScheduleCallback(homeBean);
    }

    /**
     * ユーザ情報をHomeActivityに送る
     *
     * @param list
     */
    public void sendUserInfoListData(List<Map<String, String>> list) {
        HomeBean homeBean = new HomeBean();
        HomeBeanContent homeBeanContent = new HomeBeanContent();
        List<HomeBeanContent> contents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            homeBeanContent.setContentSrcURL("http:"+list.get(i).get(VODCLIP_LIST_THUMB));
            homeBeanContent.setContentName(list.get(i).get(VODCLIP_LIST_TITLE));
            homeBeanContent.setContentTime(list.get(i).get(VODCLIP_LIST_DISPLAY_START_DATE));
            homeBeanContent.setContentId(list.get(i).get(VODCLIP_LIST_DISP_TYPE));
            contents.add(homeBeanContent);
        }
        homeBean.setContentList(contents);
        apiDataProviderCallback.UserInfoCallback(homeBean);
    }

    /**
     * VodクリップリストをHomeActivityに送る
     *
     * @param list
     */
    public void sendVodClipListData(List<Map<String, String>> list) {
        HomeBean homeBean = new HomeBean();
        homeBean.setContentTypeName(mContext.getResources().getString(R.string.nav_menu_item_clip));
        HomeBeanContent homeBeanContent = new HomeBeanContent();
        List<HomeBeanContent> contents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            homeBeanContent.setContentSrcURL("http:"+list.get(i).get(VODCLIP_LIST_THUMB));
            homeBeanContent.setContentName(list.get(i).get(VODCLIP_LIST_TITLE));
            homeBeanContent.setContentTime(list.get(i).get(VODCLIP_LIST_DISPLAY_START_DATE));
            homeBeanContent.setContentId(list.get(i).get(VODCLIP_LIST_DISP_TYPE));
            contents.add(homeBeanContent);
        }
        homeBean.setContentList(contents);
        apiDataProviderCallback.VodClipListCallback(homeBean);
    }

    /**
     * 週間ランキングリストをHomeActivityに送る
     *
     * @param list
     */
    public void sendWeeklyRankListData(List<Map<String, String>> list) {
        HomeBean homeBean = new HomeBean();
        homeBean.setContentTypeName(mContext.getResources().getString(R.string.weekly_tv_ranking_title));
        HomeBeanContent homeBeanContent = new HomeBeanContent();
        List<HomeBeanContent> contents = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            homeBeanContent.setContentSrcURL("http:"+list.get(i).get(VODCLIP_LIST_THUMB));
            homeBeanContent.setContentName(list.get(i).get(VODCLIP_LIST_TITLE));
            homeBeanContent.setContentTime(list.get(i).get(VODCLIP_LIST_DISPLAY_START_DATE));
            homeBeanContent.setContentId(list.get(i).get(VODCLIP_LIST_DISP_TYPE));
            contents.add(homeBeanContent);
        }
        homeBean.setContentList(contents);
        apiDataProviderCallback.WeeklyRankCallback(homeBean);
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
            String lowerPageLimit = "1";
            String pagerOffset = "1";
            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getChannelApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, this);
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
            String lowerPageLimit = "1";
            int pagerOffset = 1;
            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getDailyRankApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, this);
        }
        return list;
    }

    private List<Map<String, String>> getTvScheduleData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(TvSchedule_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectTvScheduleListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            TvScheduleWebClient webClient = new TvScheduleWebClient();
            int []ageReq = {1};
            String [] upperPageLimit = {"1"};
            String lowerPageLimit = "1";
            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getTvScheduleApi(ageReq, upperPageLimit,
                    lowerPageLimit, this);
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
            String filter = "1";
            int ageReq = 1;
            String genreId = "1";

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
//        sendChannelListData(getChannelListData());
        sendChannelListData(channelList.getClList());

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
//       sendDailyRankListData(getDailyRankListData());
        sendDailyRankListData(dailyRankList.getDrList());

    }

    /**
     * CH毎チャンネルリストをDBに格納する
     *
     * @param tvScheduleList
     */
    public void setStructDB(TvScheduleList tvScheduleList) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(TvSchedule_LAST_INSERT);
        TvScheduleInsertDataManager dataManager = new TvScheduleInsertDataManager(mContext);
        dataManager.insertTvScheduleInsertList(tvScheduleList);
//        sendWeeklyRankListData(getWeeklyRankListData());
        sendWeeklyRankListData(tvScheduleList.geTvsList());

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
//        sendVodClipListData(getVodClipListData());
    }

    /**
     * 週間ランキングリストをDBに保存する
     *
     * @param weeklyRankList
     */
    public void setStructDB(WeeklyRankList weeklyRankList) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
        WeeklyRankInsertDataManager dataManager = new WeeklyRankInsertDataManager(mContext);
        dataManager.insertWeeklyRankInsertList(weeklyRankList);
//        sendWeeklyRankListData(getWeeklyRankListData());
        sendWeeklyRankListData(weeklyRankList.getWrList());
    }

    /**
     * おすすめ番組をDBに保存する
     *
     * @param recommendChList
     */
    public void setStructDB(RecommendChList recommendChList) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
        RecommendChInsertDataManager dataManager = new RecommendChInsertDataManager(mContext);
        dataManager.insertVodClipInsertList(recommendChList);
//        sendWeeklyRankListData(getWeeklyRankListData());
//        sendWeeklyRankListData(getWeeklyRankListData());
    }

    /**
     * おすすめビデオをDBに保存する
     *
     * @param recommendVdList
     */
    public void setStructDB(RecommendVdList recommendVdList) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
        RecommendVdInsertDataManager dataManager = new RecommendVdInsertDataManager(mContext);
        dataManager.insertVodClipInsertList(recommendVdList);
        sendWeeklyRankListData(getWeeklyRankListData());
    }
}
