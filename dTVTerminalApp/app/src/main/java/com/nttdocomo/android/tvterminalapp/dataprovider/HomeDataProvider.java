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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        getVodClipListData();
    }

    public void getHomeBeen() {
        //Home用構造体を作成する
    }

    public void sendHomeData() {
        //HomeActivityにHomeBeenを返却する
    }

    /**
     * Vodクリップリストデータ取得開始
     */
    private void getVodClipListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(VOD_LAST_INSERT);

        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null || lastDate.length() > 0 || dateUtils.isBeforeLimitDate(lastDate)) {
            List<Map<String,String>> list = new ArrayList<>();
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
        }
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
