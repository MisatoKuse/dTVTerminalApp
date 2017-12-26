/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RentalListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;

import java.util.ArrayList;
import java.util.List;

public class RentalDataProvider implements RentalVodListWebClient.RentalVodListJsonParserCallback {

    private Context mContext = null;
    private boolean mSetDB = false;

    private ApiDataProviderCallback mApiDataProviderCallback = null;

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
         * @param list コンテンツリスト
         */
        void rentalListCallback(List<ContentsData> list);
    }

    /**
     * コンストラクタ
     *
     * @param mContext コンテキスト
     */
    public RentalDataProvider(Context mContext) {
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
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
     * @param response 購入済みVOD一覧データ
     */
    public void sendRentalListData(PurchasedVodListResponse response) {

        //ContentsList生成
        List<ContentsData> list = makeContentsData(response);

        //レンタル一覧を送る
        mApiDataProviderCallback.rentalListCallback(list);
    }

    /**
     * レンタル一覧を取得する
     */
    private void getRentalListData() {
        PurchasedVodListResponse response = new PurchasedVodListResponse();

        //通信クラスにデータ取得要求を出す
        RentalVodListWebClient webClient = new RentalVodListWebClient();
        webClient.getRentalVodListApi(this);

        //TODO: Display用ダミーデータ(消去予定)ここから
//        ArrayList<VodMetaFullData> list = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            VodMetaFullData vodMetaFullData = new VodMetaFullData();
//            vodMetaFullData.setTitle("title" + i);
//            vodMetaFullData.setmThumb_448_252("https://www.nhk.or.jp/prog/img/944/g944.jpg");
//            vodMetaFullData.setAvail_end_date(1512054000);//"2017/12/01"
//            vodMetaFullData.setRating(i);
//            list.add(vodMetaFullData);
//        }
//        response.setVodMetaFullData(list);
//        sendRentalListData(response);
        //Display用ダミーデータ(消去予定)ここまで
    }

    /**
     * DB保存
     *
     * @param response 購入済みVOD一覧データ
     */
    public void setStructDB(PurchasedVodListResponse response) {
        if (mSetDB) {
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastDate(DateUtils.RENTAL_LIST_LAST_INSERT);
            RentalListInsertDataManager dataManager = new RentalListInsertDataManager(mContext);
            dataManager.insertRentalListInsertList(response);
            sendRentalListData(response);
        }
    }

    /**
     * ContentsData生成
     *
     * @param response 購入済みVOD一覧データ
     * @return コンテンツリスト
     */
    private List<ContentsData> makeContentsData(PurchasedVodListResponse response) {
        List<ContentsData> list = new ArrayList<>();
        ArrayList<VodMetaFullData> metaFullData = response.getVodMetaFullData();
        for (int i = 0; i < response.getVodMetaFullData().size(); i++) {
            ContentsData data = new ContentsData();

            VodMetaFullData vodMetaFullData = metaFullData.get(i);

            String title = vodMetaFullData.getTitle();
            String search = vodMetaFullData.getmSearch_ok();
            data.setTitle(title);
            //エポック秒から文字に変換
            data.setTime(DateUtils.formatEpochToString(vodMetaFullData.getAvail_end_date()));
            data.setRatStar(String.valueOf(vodMetaFullData.getRating()));
            data.setThumURL(vodMetaFullData.getmThumb_448_252());
            data.setSearchOk(search);

            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();

            String linearEndDate = String.valueOf(vodMetaFullData.getAvail_end_date());

            requestData.setCrid(vodMetaFullData.getCrid());
            requestData.setServiceId(vodMetaFullData.getmService_id());
            requestData.setEventId(vodMetaFullData.getmEvent_id());
            requestData.setTitleId(vodMetaFullData.getEpisode_id());
            requestData.setTitle(title);
            requestData.setClipTarget(title);
            requestData.setRValue(vodMetaFullData.getR_value());
            requestData.setLinearStartDate(String.valueOf(vodMetaFullData.getAvail_start_date()));
            requestData.setLinearEndDate(linearEndDate);
            requestData.setSearchOk(search);
            requestData.setClipTarget(title); //TODO:仕様確認中 現在はトーストにタイトル名を表示することとしています

            //視聴通知判定生成
            String dispType = vodMetaFullData.getDisp_type();
            String contentsType = vodMetaFullData.getmContent_type();
            String tvService = vodMetaFullData.getmTv_service();
            String dTv = vodMetaFullData.getDtv();
            requestData.setIsNotify(dispType, contentsType, linearEndDate, tvService, dTv);
            data.setRequestData(requestData);

            list.add(data);
        }
        return list;
    }
}