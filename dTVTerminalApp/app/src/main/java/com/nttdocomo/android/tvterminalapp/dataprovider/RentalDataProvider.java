/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;

import java.util.ArrayList;
import java.util.List;

public class RentalDataProvider {

    private Context mContext;

    /**
     * Ranking Top画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {

        /**
         * レンタル一覧用コールバック
         *
         * @param contentsData
         */
        void rentalListCallback(List<ContentsData> contentsData);
    }

    private ApiDataProviderCallback apiDataProviderCallback;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public RentalDataProvider(Context mContext) {
        this.mContext = mContext;
        this.apiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * RankingTopActivityからのデータ取得要求受付
     */
    public void getRentalData(int offSet) {
        //レンタル一覧
        List<ContentsData> rentalList = getRentalListData(offSet);
        if (rentalList != null && rentalList.size() > 0) {
            sendDailyRankListData(rentalList);
        }
    }

    /**
     * レンタル一覧データをRentalListActivityに送る
     *
     * @param list
     */
    public void sendDailyRankListData(List<ContentsData> list) {
        //TODO:レンタル一覧を送る
        apiDataProviderCallback.rentalListCallback(list);
    }

    /**
     * レンタル一覧を取得する
     */
    private List<ContentsData> getRentalListData(int pageOffset) {
        //TODO:データマネージャからレンタルリストを取得する
        List<ContentsData> list = new ArrayList<>();

        //Display用ダミーデータ
        for (int i = 0; i < 30; i++) {
            ContentsData contentsData = new ContentsData();
            contentsData.setRatStar(String.valueOf(i));
            contentsData.setThumURL("https://www.nhk.or.jp/prog/img/944/g944.jpg");
            contentsData.setTime("2017/12/01");
            contentsData.setTitle("aaa" + i);
            list.add(contentsData);
        }
        return list;
    }
}
