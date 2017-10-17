package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.beans.HomeBean;
import com.nttdocomo.android.tvterminalapp.beans.HomeBeanContent;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VodClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webApiClient.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webApiClient.VodClipWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.CHANNEL_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.DAILY_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VOD_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser.VodClipJsonParser.VODCLIP_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.WebApiBasePlala.FILTER_RELEASE;
import static com.nttdocomo.android.tvterminalapp.webApiClient.WebApiBasePlala.TYPE_D_CHANNEL;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class HomeDataProvider {

    private Context mContext;

    private List<Map<String, String>> HomeStructList = new ArrayList<>();

    /**
     * Home画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {
        void ChannelListCallback(HomeBean homeBean);

        void DailyRankListCallback(HomeBean homeBean);

        void TvScheduleCallback(HomeBean homeBean);

        void UserInfoCallback(HomeBean homeBean);

        void VodClipListCallback(HomeBean homeBean);

        void WeeklyRankCallback(HomeBean homeBean);
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
            homeBeanContent.setContentTime(VODCLIP_LIST_THUMB);
            homeBeanContent.setContentName(VODCLIP_LIST_TITLE);
            homeBeanContent.setContentTime(VODCLIP_LIST_DISPLAY_START_DATE);
            homeBeanContent.setContentId(VODCLIP_LIST_DISP_TYPE);
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
            homeBeanContent.setContentTime(VODCLIP_LIST_THUMB);
            homeBeanContent.setContentName(VODCLIP_LIST_TITLE);
            homeBeanContent.setContentTime(VODCLIP_LIST_DISPLAY_START_DATE);
            homeBeanContent.setContentId(VODCLIP_LIST_DISP_TYPE);
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
            homeBeanContent.setContentTime(VODCLIP_LIST_THUMB);
            homeBeanContent.setContentName(VODCLIP_LIST_TITLE);
            homeBeanContent.setContentTime(VODCLIP_LIST_DISPLAY_START_DATE);
            homeBeanContent.setContentId(VODCLIP_LIST_DISP_TYPE);
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
            homeBeanContent.setContentTime(VODCLIP_LIST_THUMB);
            homeBeanContent.setContentName(VODCLIP_LIST_TITLE);
            homeBeanContent.setContentTime(VODCLIP_LIST_DISPLAY_START_DATE);
            homeBeanContent.setContentId(VODCLIP_LIST_DISP_TYPE);
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
            homeBeanContent.setContentTime(VODCLIP_LIST_THUMB);
            homeBeanContent.setContentName(VODCLIP_LIST_TITLE);
            homeBeanContent.setContentTime(VODCLIP_LIST_DISPLAY_START_DATE);
            homeBeanContent.setContentId(VODCLIP_LIST_DISP_TYPE);
            contents.add(homeBeanContent);
        }
        homeBean.setContentList(contents);
        apiDataProviderCallback.WeeklyRankCallback(homeBean);
    }

    /**
     * Vodクリップリストデータ取得開始
     */
    private List<Map<String, String>> getVodClipListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(VOD_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null || lastDate.length() > 0 || dateUtils.isBeforeLimitDate(lastDate)) {
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
                    lowerPageLimit, pagerOffset, (VodClipWebClient.VodClipJsonParserCallback) mContext);
//            for (int i = 0; i < clipLists.size(); i++) {
//                list = clipLists.get(i).getVcList();
//            }
        }
        return list;
    }

    private List<Map<String, String>> getChannelListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(CHANNEL_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null || lastDate.length() > 0 || dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectClipHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            ChannelWebClient webClient = new ChannelWebClient();
            int pageLimit = 1;
            int pagerOffset = 1;
            String filter = FILTER_RELEASE;
            String type = TYPE_D_CHANNEL;
            List<VodClipList> clipLists = webClient.getChannelApi(pageLimit, pagerOffset, filter,
                    type);
            for (int i = 0; i < clipLists.size(); i++) {
                list = clipLists.get(i).getVcList();
            }
        }
        return list;
    }

    /**
     * チャンネル一覧データをDBに格納する
     *
     * @param channelList
     */
    public void setStructDB(ChannelList channelList) {
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
//        dataManager.insertVodClipInsertList(dailyRankList);

        sendVodClipListData(getVodClipListData());

    }

    /**
     * CH毎チャンネルリストをDBに格納する
     *
     * @param tvScheduleList
     */
    public void setStructDB(TvScheduleList tvScheduleList) {
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

        sendVodClipListData(getVodClipListData());
    }

    /**
     * 週間ランキングリストをDBに保存する
     *
     * @param weeklyRankList
     */
    public void setStructDB(WeeklyRankList weeklyRankList) {
    }
}
