/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RentalListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;

import java.util.ArrayList;
import java.util.List;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RENTAL_LIST_LAST_INSERT;

public class RentalDataProvider implements RentalVodListWebClient.RentalVodListJsonParserCallback {

    private Context mContext;
    private boolean mSetDB;

    @Override
    public void onRentalVodListJsonParsed(PurchasedVodListResponse response) {
        if (response != null) {
            setStructDB(response);
            sendRentalListData(response);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    /**
     * Ranking Top画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {

        /**
         * レンタル一覧用コールバック
         *
         * @param list
         */
        void rentalListCallback(List<ContentsData> list);
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
        this.mSetDB = false;
    }

    /**
     * RankingTopActivityからのデータ取得要求受付
     *
     * @param flg 初回取得時のみDB保存する
     */
    public void getRentalData(boolean flg) {
        mSetDB = flg;
        //レンタル一覧取得
        getRentalListData();
    }

    /**
     * レンタル一覧データをRentalListActivityに送る
     *
     * @param response
     */
    public void sendRentalListData(PurchasedVodListResponse response) {

        //ContentsList生成
        List<ContentsData> list = makeContentsData(response);

        //レンタル一覧を送る
        apiDataProviderCallback.rentalListCallback(list);
    }

    /**
     * レンタル一覧を取得する
     */
    private void getRentalListData() {
        PurchasedVodListResponse response = new PurchasedVodListResponse();

        //通信クラスにデータ取得要求を出す
        RentalVodListWebClient webClient = new RentalVodListWebClient();
        webClient.getRentalVodListApi(this);

        //Display用ダミーデータ(消去予定)ここから
        ArrayList<VodMetaFullData> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            VodMetaFullData vodMetaFullData = new VodMetaFullData();
            vodMetaFullData.setTitle("title" + i);
            vodMetaFullData.setThumb("https://www.nhk.or.jp/prog/img/944/g944.jpg");
            vodMetaFullData.setAvail_end_date("2017/12/01");
            vodMetaFullData.setRating(String.valueOf(i));
            list.add(vodMetaFullData);
        }
        response.setVodMetaFullData(list);
        sendRentalListData(response);
        //Display用ダミーデータ(消去予定)ここまで
    }

    /**
     * DB保存
     *
     * @param response
     */
    public void setStructDB(PurchasedVodListResponse response) {
        if (mSetDB) {
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastDate(RENTAL_LIST_LAST_INSERT);
            RentalListInsertDataManager dataManager = new RentalListInsertDataManager(mContext);
            dataManager.insertRentalListInsertList(response);
            sendRentalListData(response);
        }
    }

    /**
     * ContentsData生成
     *
     * @param response
     * @return
     */
    private List<ContentsData> makeContentsData(PurchasedVodListResponse response) {
        List<ContentsData> list = new ArrayList<>();
        ArrayList<VodMetaFullData> metaFullData = response.getVodMetaFullData();
        for (int i = 0; i < response.getVodMetaFullData().size(); i++) {
            ContentsData data = new ContentsData();
            data.setTitle(metaFullData.get(i).getTitle());
            data.setTime(metaFullData.get(i).getAvail_end_date());
            data.setRatStar(metaFullData.get(i).getRating());
            data.setThumURL(metaFullData.get(i).getThumb());
            list.add(data);
        }
        return list;
    }
}
