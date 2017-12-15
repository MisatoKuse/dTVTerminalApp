/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelDeleteResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelRegisterResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelDeleteWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelRegisterWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelWebClient;

import java.util.ArrayList;


/**
 * My番組表用データプロバイダークラス
 * 機能：　My番組表画面で使うデータを提供するクラスである
 */
public class MyChannelDataProvider implements MyChannelWebClient.MyChannelListJsonParserCallback,
        MyChannelDeleteWebClient.MyChannelDeleteJsonParserCallback,
        MyChannelRegisterWebClient.MyChannelRegisterJsonParserCallback {

    private ApiDataProviderCallback mApiDataProviderCallback = null;

    @Override
    public void onMyChannelListJsonParsed(MyChannelListResponse myChannelListResponse) {
        if (myChannelListResponse != null && myChannelListResponse.getMyChannelMetaData() != null) {
            mApiDataProviderCallback.onMyChannelListCallback(myChannelListResponse.getMyChannelMetaData());
        }
    }

    @Override
    public void onMyChannelDeleteJsonParsed(MyChannelDeleteResponse myChannelDeleteResponse) {
        if (myChannelDeleteResponse != null) {
            mApiDataProviderCallback.onMyChannelDeleteCallback(myChannelDeleteResponse.getStatus());
        }
    }

    @Override
    public void onMyChannelRegisterJsonParsed(MyChannelRegisterResponse myChannelRegisterResponse) {
        if (myChannelRegisterResponse != null) {
            mApiDataProviderCallback.onMyChannelRegisterCallback(myChannelRegisterResponse.getStatus());
        }
    }

    /**
     * コンストラクタ
     *
     * @param mContext 　My番組表
     */
    public MyChannelDataProvider(Context mContext) {
        this.mApiDataProviderCallback = (MyChannelDataProvider.ApiDataProviderCallback) mContext;
    }

    /**
     * 画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {

        /**
         * My番組表チャンネル情報を戻すコールバック
         *
         * @param myChannelMetaData 画面に渡すチャンネル番組情報
         */
        void onMyChannelListCallback(ArrayList<MyChannelMetaData> myChannelMetaData);

        /**
         * 登録ステータスを戻すコールバック
         *
         * @param result 登録状態
         */
        void onMyChannelRegisterCallback(String result);

        /**
         * 解除ステータスを戻すコールバック
         *
         * @param result 解除状態
         */
        void onMyChannelDeleteCallback(String result);
    }

    /**
     * My番組表一覧取得
     */
    public void getMyChannelList() {
        MyChannelWebClient myChannelList = new MyChannelWebClient();
        myChannelList.getMyChanelListApi(this);
    }

    /**
     * My番組表登録
     */
    public void getMyChannelRegisterStatus(String service_id, String title, String r_value, String adult_type, int index) {
        MyChannelRegisterWebClient myChannelRegisterStatus = new MyChannelRegisterWebClient();
        boolean result = myChannelRegisterStatus.getMyChanelRegisterApi(service_id, title, r_value, adult_type, index, this);
        if(!result){
            mApiDataProviderCallback.onMyChannelRegisterCallback("パラメータ不正");
        }
    }

    /**
     * My番組表解除
     */
    public void getMyChannelDeleteStatus(String service_id) {
        MyChannelDeleteWebClient myChannelDeleteStatus = new MyChannelDeleteWebClient();
        boolean result = myChannelDeleteStatus.getMyChanelDeleteApi(service_id, this);
        if(!result){
            mApiDataProviderCallback.onMyChannelDeleteCallback("パラメータ不正");
        }
    }

}
