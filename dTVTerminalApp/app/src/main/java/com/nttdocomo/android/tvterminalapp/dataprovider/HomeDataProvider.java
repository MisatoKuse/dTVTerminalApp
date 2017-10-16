package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.content.SharedPreferences;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.helper.VodClipListDBHelper;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VodClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VOD_LAST_INSERT;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class HomeDataProvider {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public HomeDataProvider(Context context) {
        mContext = context;
    }

    public void getHomeData() {
        //Activityからのデータ取得要求受付
        HomeDataManager homeDataManager = new HomeDataManager(mContext);
        homeDataManager.selectHomeData();
    }

    public void getHomeBeen() {
        //Home用構造体を作成する
    }

    public boolean checkLastDate() {
        //前回DB更新日時から一定時間が経過していたらfalseを返却
        return false;
    }

    public void sendHomeData() {
        //HomeActivityにHomeBeenを返却する
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
