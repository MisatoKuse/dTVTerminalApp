/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.MyChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelDeleteResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelRegisterResponse;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelDeleteWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelRegisterWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.MY_CHANNEL_LIST_LAST_INSERT;


/**
 * My番組表用データプロバイダークラス
 * 機能：　My番組表画面で使うデータを提供するクラスである
 */
public class MyChannelDataProvider implements MyChannelWebClient.MyChannelListJsonParserCallback,
        MyChannelDeleteWebClient.MyChannelDeleteJsonParserCallback,
        MyChannelRegisterWebClient.MyChannelRegisterJsonParserCallback,
        DbThread.DbOperation {

    //共通スレッド使う
    private static final int MY_CHANNEL_UPDATE = 0;//マイ番組表更新
    private static final int MY_CHANNEL_SELECT = 1;//マイ番組表表示
    private final Context mContext;

    private ApiDataProviderCallback mApiDataProviderCallback = null;
    private ArrayList<MyChannelMetaData> mMyChannelMetaDataList;
    private MyChannelInsertDataManager mMyChannelInsertDataManager;

    /**
     * マイ番組表ウェブクライアント
     */
    private MyChannelWebClient mMyChannelListWebClient;
    /**
     * My番組表登録ウェブクライアント
     */
    private MyChannelRegisterWebClient mMyChannelRegisterStatus;
    /**
     * My番組表解除ウェブクライアント
     */
    private MyChannelDeleteWebClient mMyChannelDeleteStatus;

    @Override
    public void onMyChannelListJsonParsed(MyChannelListResponse myChannelListResponse) {
        if (myChannelListResponse != null && myChannelListResponse.getMyChannelMetaData() != null) {
            mMyChannelMetaDataList = myChannelListResponse.getMyChannelMetaData();
            if (mMyChannelMetaDataList != null) {
                Handler handler = new Handler();
                try {
                    DbThread t = new DbThread(handler, this, MY_CHANNEL_UPDATE);
                    t.start();//データをDBに入れる
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mApiDataProviderCallback != null) {
                mApiDataProviderCallback.onMyChannelListCallback(mMyChannelMetaDataList);
            }
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
        this.mContext = mContext;
        this.mApiDataProviderCallback = (MyChannelDataProvider.ApiDataProviderCallback) mContext;
    }

    @Override
    public void onDbOperationFinished(boolean isSuccessful, List<Map<String, String>> resultSet, int operationId) {
        if (isSuccessful) {
            switch (operationId) {
                case MY_CHANNEL_SELECT:
                    ArrayList<MyChannelMetaData> channels = new ArrayList<>();
                    for (int i = 0; i < resultSet.size(); i++) {
                        Map<String, String> hashMap = resultSet.get(i);
                        String serviceId = hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID);
                        String title = hashMap.get(JsonConstants.META_RESPONSE_TITLE);
                        String rValue = hashMap.get(JsonConstants.META_RESPONSE_R_VALUE);
                        String adultType = hashMap.get(JsonConstants.META_RESPONSE_ADULT_TYPE);
                        String index = hashMap.get(JsonConstants.META_RESPONSE_INDEX);
                        if (!TextUtils.isEmpty(serviceId)) {
                            MyChannelMetaData channel = new MyChannelMetaData();
                            channel.setServiceId(serviceId);
                            channel.setTitle(title);
                            channel.setRValue(rValue);
                            channel.setAdultType(adultType);
                            channel.setIndex(index);
                            channels.add(channel);
                        }
                    }
                    if (null != mApiDataProviderCallback) {
                        mApiDataProviderCallback.onMyChannelListCallback(channels);
                    }
                    break;
            }
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(int operationId) throws Exception {
        List<Map<String, String>> resultSet = null;
        mMyChannelInsertDataManager = new MyChannelInsertDataManager(mContext);
        switch (operationId) {
            case MY_CHANNEL_UPDATE://サーバーから取得したチャンネルデータをDBに保存する
                mMyChannelInsertDataManager.insertIntoMyChannelList(mMyChannelMetaDataList);
                DateUtils dateUtils = new DateUtils(mContext);
                dateUtils.addLastDate(MY_CHANNEL_LIST_LAST_INSERT);
                break;
            case MY_CHANNEL_SELECT://DBから番組データを取得して、画面に返却する
                resultSet = mMyChannelInsertDataManager.selectFromMyChannelList();
                break;
            default:
                break;
        }
        return resultSet;
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
    public void getMyChannelList(int reqCode) {
        switch (reqCode) {
            case R.layout.tv_program_list_main_layout:
                DateUtils dateUtils = new DateUtils(mContext);
                String lastDate = dateUtils.getLastDate(MY_CHANNEL_LIST_LAST_INSERT);
                if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
                    Handler handler = new Handler();//チャンネル情報更新
                    try {
                        DbThread t = new DbThread(handler, this, MY_CHANNEL_SELECT);
                        t.start();//データをDBから取得する
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }//キャシューないなら通信のほうに進む
            case R.layout.my_channel_edit_main_layout://通信してデータ取得
                mMyChannelListWebClient = new MyChannelWebClient(mContext);
                mMyChannelListWebClient.getMyChanelListApi(this);
                break;
            default:
                break;
        }
    }

    /**
     * My番組表登録
     */
    public void getMyChannelRegisterStatus(String service_id, String title, String r_value, String adult_type, int index) {
        mMyChannelRegisterStatus = new MyChannelRegisterWebClient(mContext);
        boolean result = mMyChannelRegisterStatus.getMyChanelRegisterApi(service_id, title, r_value, adult_type, index, this);
        if (!result) {
            mApiDataProviderCallback.onMyChannelRegisterCallback("パラメータ不正");
        }
    }

    /**
     * My番組表解除
     */
    public void getMyChannelDeleteStatus(String service_id) {
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
