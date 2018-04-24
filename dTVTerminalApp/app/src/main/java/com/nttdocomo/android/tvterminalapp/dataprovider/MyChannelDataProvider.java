/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelDeleteResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelRegisterResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelDeleteWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelRegisterWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelWebClient;

import java.util.ArrayList;


/**
 * My番組表用データプロバイダークラス.
 * 機能：　My番組表画面で使うデータを提供するクラスである
 */
public class MyChannelDataProvider implements MyChannelWebClient.MyChannelListJsonParserCallback,
        MyChannelDeleteWebClient.MyChannelDeleteJsonParserCallback,
        MyChannelRegisterWebClient.MyChannelRegisterJsonParserCallback {

    /**
     * コンテキスト.
     */
    private final Context mContext;
    /**
     * コールバック.
     */
    private ApiDataProviderCallback mApiDataProviderCallback = null;

    /**
     * マイ番組表ウェブクライアント.
     */
    private MyChannelWebClient mMyChannelListWebClient;
    /**
     * My番組表登録ウェブクライアント.
     */
    private MyChannelRegisterWebClient mMyChannelRegisterStatus;
    /**
     * My番組表解除ウェブクライアント.
     */
    private MyChannelDeleteWebClient mMyChannelDeleteStatus;

    /**
     * コンストラクタ.
     *
     * @param mContext 　My番組表
     */
    public MyChannelDataProvider(final Context mContext) {
        this.mContext = mContext;
        this.mApiDataProviderCallback = (MyChannelDataProvider.ApiDataProviderCallback) mContext;
    }

    @Override
    public void onMyChannelListJsonParsed(final MyChannelListResponse myChannelListResponse) {
        if (mApiDataProviderCallback == null) {
            return;
        }

        if (myChannelListResponse != null && myChannelListResponse.getMyChannelMetaData() != null) {
            ArrayList<MyChannelMetaData> myChannelMetaData = myChannelListResponse.getMyChannelMetaData();
            mApiDataProviderCallback.onMyChannelListCallback(myChannelMetaData);
        } else {
            mApiDataProviderCallback.onMyChannelListCallback(null);
        }
    }

    @Override
    public void onMyChannelDeleteJsonParsed(final MyChannelDeleteResponse myChannelDeleteResponse) {
        if (myChannelDeleteResponse != null) {
            mApiDataProviderCallback.onMyChannelDeleteCallback(myChannelDeleteResponse.getStatus());
        }
    }

    @Override
    public void onMyChannelRegisterJsonParsed(final MyChannelRegisterResponse myChannelRegisterResponse) {
        if (myChannelRegisterResponse != null) {
            mApiDataProviderCallback.onMyChannelRegisterCallback(myChannelRegisterResponse.getStatus());
        }
    }

    /**
     * My番組表チャンネル情報エラーステータス取得.
     * @return エラーステータス
     */
    public ErrorState getMyChannelListError() {
        return mMyChannelListWebClient.getError();
    }

    /**
     * 画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {

        /**
         * My番組表チャンネル情報を戻すコールバック.
         *
         * @param myChannelMetaData 画面に渡すチャンネル番組情報
         */
        void onMyChannelListCallback(ArrayList<MyChannelMetaData> myChannelMetaData);

        /**
         * 登録ステータスを戻すコールバック.
         *
         * @param result 登録状態
         */
        void onMyChannelRegisterCallback(String result);

        /**
         * 解除ステータスを戻すコールバック.
         *
         * @param result 解除状態
         */
        void onMyChannelDeleteCallback(String result);
    }

    /**
     * My番組表一覧取得.
     *
     * @param reqCode リクエストコード
     */
    public void getMyChannelList(final int reqCode) {
        switch (reqCode) {
            case R.layout.tv_program_list_main_layout:
            case R.layout.my_channel_edit_main_layout:
                //通信してデータ取得
                mMyChannelListWebClient = new MyChannelWebClient(mContext);
                mMyChannelListWebClient.getMyChanelListApi(this);
                break;
            default:
                break;
        }
    }

    /**
     * My番組表登録.
     *
     * @param service_id サービスID
     * @param title タイトル
     * @param r_value r_value
     * @param adult_type アダルトタイプ
     * @param index インデックス
     */
    public void getMyChannelRegisterStatus(final String service_id, final String title,
                                           final String r_value, final String adult_type, final int index) {
        mMyChannelRegisterStatus = new MyChannelRegisterWebClient(mContext);
        boolean result = mMyChannelRegisterStatus.getMyChanelRegisterApi(service_id, title, r_value, adult_type, index, this);
        if (!result) {
            mApiDataProviderCallback.onMyChannelRegisterCallback("パラメータ不正");
        }
    }

    /**
     * My番組表解除.
     *
     * @param service_id サービスID
     */
    public void getMyChannelDeleteStatus(final String service_id) {
        mMyChannelDeleteStatus = new MyChannelDeleteWebClient(mContext);
        boolean result = mMyChannelDeleteStatus.getMyChanelDeleteApi(service_id, this);
        if (!result) {
            mApiDataProviderCallback.onMyChannelDeleteCallback("パラメータ不正");
        }
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        //マイ番組表取得
        if (mMyChannelListWebClient != null) {
            mMyChannelListWebClient.stopConnection();
        }
        //マイ番組表登録
        if (mMyChannelRegisterStatus != null) {
            mMyChannelRegisterStatus.stopConnection();
        }
        //マイ番組表解除
        if (mMyChannelDeleteStatus != null) {
            mMyChannelDeleteStatus.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        //マイ番組表取得
        if (mMyChannelListWebClient != null) {
            mMyChannelListWebClient.enableConnection();
        }
        //マイ番組表登録
        if (mMyChannelRegisterStatus != null) {
            mMyChannelRegisterStatus.enableConnection();
        }
        //マイ番組表解除
        if (mMyChannelDeleteStatus != null) {
            mMyChannelDeleteStatus.enableConnection();
        }
    }
}
