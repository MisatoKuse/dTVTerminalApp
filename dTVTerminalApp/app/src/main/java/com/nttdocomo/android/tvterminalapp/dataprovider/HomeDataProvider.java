package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.beans.HomeBean;
import com.nttdocomo.android.tvterminalapp.beans.HomeBeanContent;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VodClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VOD_LAST_INSERT;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class HomeDataProvider {

    private Context mContext;

    private List<Map<String, String>> HomeStructList = new ArrayList<>();

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
     * @param apiDataProviderCallback
     */
    public HomeDataProvider(Context mContext, ApiDataProviderCallback apiDataProviderCallback) {
        this.mContext = mContext;
        this.apiDataProviderCallback = apiDataProviderCallback;
    }

    public void getHomeData() {
        //Activityからのデータ取得要求受付
        List<Map<String, String>> vodClipList = getVodClipListData();
        makeHomeStruct(vodClipList);
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
     * @param homeBean
     */
    public void sendChannelListData(HomeBean homeBean) {
        apiDataProviderCallback.ChannelListCallback(homeBean);
    }

    /**
     * 今日のランキングをHomeActivityに送る
     *
     * @param homeBean
     */
    public void sendDailyRankListData(HomeBean homeBean) {
        apiDataProviderCallback.DailyRankListCallback(homeBean);
    }

    /**
     * CH毎番組表をHomeActivityに送る
     *
     * @param homeBean
     */
    public void sendTvScheduleListData(HomeBean homeBean) {
        apiDataProviderCallback.TvScheduleCallback(homeBean);
    }

    /**
     * ユーザ情報をHomeActivityに送る
     *
     * @param homeBean
     */
    public void sendUserInfoListData(HomeBean homeBean) {
        apiDataProviderCallback.UserInfoCallback(homeBean);
    }

    /**
     * VodクリップリストをHomeActivityに送る
     *
     * @param homeBean
     */
    public void sendVodClipListData(HomeBean homeBean) {
        apiDataProviderCallback.VodClipListCallback(homeBean);
    }

    /**
     * 週間ランキングリストをHomeActivityに送る
     *
     * @param homeBean
     */
    public void sendWeeklyRankListData(HomeBean homeBean) {
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
            list = homeDataManager.selectHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
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
        //引数に渡された各オブジェクトに応じたDMに構造体を渡す
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
    }
}
