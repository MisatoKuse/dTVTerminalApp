package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.VodClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class HomeDataProvider {

    private Context mContext;

    public HomeDataProvider(Context context) {
        mContext = context;
    }

    public void getHomeData() {
        //Activityからのデータ取得要求受付
    }

    public boolean checkHomeData() {
        //Home用構造体を確認する
        return false;
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
     * @param dailyRankList
     */
    public void setStructDB(DailyRankList dailyRankList) {
        //引数に渡された各オブジェクトに応じたDMに構造体を渡す
    }

    /**
     * Vodクリップ一覧データをDBに格納する
     * @param vodClipList
     */
    public void setStructDB(VodClipList vodClipList) {
        VodClipInsertDataManager dataManager = new VodClipInsertDataManager(mContext);
        dataManager.insertVodClipInsertList(vodClipList);
    }
}
