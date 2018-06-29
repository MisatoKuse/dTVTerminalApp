/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelDeleteResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelRegisterResponse;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelDeleteWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelRegisterWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * My番組表用データプロバイダークラス.
 * 機能：　My番組表画面で使うデータを提供するクラスである
 */
public class MyChannelDataProvider implements MyChannelWebClient.MyChannelListJsonParserCallback,
        MyChannelDeleteWebClient.MyChannelDeleteJsonParserCallback,
        DataBaseThread.DataBaseOperation,
        MyChannelRegisterWebClient.MyChannelRegisterJsonParserCallback {

    /** コンテキスト. */
    private final Context mContext;
    /** 通信許可フラグ. */
    private boolean mIsStop = false;
    /** マイチャンネルリスト. */
    private List<Map<String, String>> mMyChanelMapList = null;
    /** マイチャンネルメタデータリスト. */
    private ArrayList<MyChannelMetaData> mMyChannelMetaDataList = null;
    /** コールバック. */
    private ApiDataProviderCallback mApiDataProviderCallback = null;

    /** マイ番組表ウェブクライアント. */
    private MyChannelWebClient mMyChannelListWebClient = null;
    /** My番組表登録ウェブクライアント. */
    private MyChannelRegisterWebClient mMyChannelRegisterStatus;
    /** My番組表解除ウェブクライアント. */
    private MyChannelDeleteWebClient mMyChannelDeleteStatus;

    /** My番組表DB保存. */
    private static final int MY_CHANNEL_INSERT = 0;
    /** My番組表DB取得. */
    private static final int MY_CHANNEL_SELECT = 1;
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

        if (myChannelListResponse != null && myChannelListResponse.getMyChannelMetaData() != null
                && myChannelListResponse.getStatus().equals(JsonConstants.META_RESPONSE_STATUS_OK)) {
            ArrayList<MyChannelMetaData> myChannelMetaData = myChannelListResponse.getMyChannelMetaData();
            if (myChannelMetaData == null || myChannelMetaData.size() < 1) {
                //データをDBから取得する
                Handler handler = new Handler(Looper.getMainLooper());
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, MY_CHANNEL_SELECT);
                dataBaseThread.start();
            } else {
                //データをDBに入れる
                mMyChannelMetaDataList = myChannelMetaData;
                if (mMyChannelMetaDataList != null && mMyChannelMetaDataList.size() > 0) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    DataBaseThread dataBaseThread = new DataBaseThread(handler, this, MY_CHANNEL_INSERT);
                    dataBaseThread.start();
                }
                sendMyChannelList(myChannelMetaData);
            }
        } else {
            sendMyChannelList(null);
        }
    }

    @Override
    public void onMyChannelDeleteJsonParsed(final MyChannelDeleteResponse myChannelDeleteResponse) {
        if (myChannelDeleteResponse != null) {
            mApiDataProviderCallback.onMyChannelDeleteCallback(myChannelDeleteResponse.getStatus());
        } else {
            mApiDataProviderCallback.onMyChannelDeleteCallback(null);
        }
    }

    @Override
    public void onMyChannelRegisterJsonParsed(final MyChannelRegisterResponse myChannelRegisterResponse) {
        if (myChannelRegisterResponse != null) {
            mApiDataProviderCallback.onMyChannelRegisterCallback(myChannelRegisterResponse.getStatus());
        } else {
            mApiDataProviderCallback.onMyChannelRegisterCallback(null);
        }
    }

    /**
     * My番組表チャンネル情報エラーステータス取得.
     * @return エラーステータス
     */
    public ErrorState getMyChannelListError() {
        ErrorState errorState = null;
        if(mMyChannelListWebClient != null) {
            errorState = mMyChannelListWebClient.getError();
        }
        return errorState;
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
                //作業
                //日付チェックして一時間以内ならDBキャッシュからチャンネル一覧を直接取得してCallback
                DateUtils dateUtils = new DateUtils(mContext);
                String lastDate = dateUtils.getLastDate(DateUtils.MY_CHANNEL_LIST_LAST_INSERT);
                //マイチャンネル一覧のDB保存履歴と、有効期間を確認
                if ((!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate))
                        || !NetWorkUtils.isOnline(mContext)) {
                    //データをDBから取得する
                    Handler handler = new Handler(Looper.getMainLooper());
                    DataBaseThread dataBaseThread = new DataBaseThread(handler, this, MY_CHANNEL_SELECT);
                    dataBaseThread.start();
                } else {
                    if (!mIsStop) {
                        //通信してデータ取得
                        mMyChannelListWebClient = new MyChannelWebClient(mContext);
                        mMyChannelListWebClient.getMyChanelListApi(this);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDbOperationFinished(final boolean isSuccessful, final List<Map<String, String>> resultSet, final int operationId) {
        switch (operationId) {
            case MY_CHANNEL_INSERT:
                break;
            case MY_CHANNEL_SELECT:
                ArrayList<MyChannelMetaData> myChannelMetaData = getMyChannelMetaData(mMyChanelMapList);
                sendMyChannelList(myChannelMetaData);
                break;
            default:
                break;
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(final int operationId) {
        switch (operationId) {
            case MY_CHANNEL_INSERT:
                try {
                    ChannelInsertDataManager insertDataManager = new ChannelInsertDataManager(mContext);
                    insertDataManager.insertMyChannelListInsert(mMyChannelMetaDataList);
                } catch (SQLiteException e) {
                    DTVTLogger.debug("MyChannel insert failed = " + e);
                }
                break;
            case MY_CHANNEL_SELECT:
                try {
                    ProgramDataManager selectDataManager = new ProgramDataManager(mContext);
                    mMyChanelMapList = selectDataManager.selectMyChannelListData();
                } catch (Exception e) {
                    DTVTLogger.debug("MyChannel select failed = " + e);
                    mMyChanelMapList = null;
                }
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * マイチャンネルデータに変換.
     *
     * @param mapList DB取得データ
     * @return マイチャンネルメタデータ
     */
    private ArrayList<MyChannelMetaData> getMyChannelMetaData(final List<Map<String, String>> mapList) {
        DTVTLogger.start();
        ArrayList<MyChannelMetaData> arrayList = new ArrayList<>();
        //DBデータがない場合はnullを返す
        if (mapList == null || mapList.size() < 1) {
            return null;
        }
        for (int i = 0; i < mapList.size(); i++) {
            MyChannelMetaData metaData = new MyChannelMetaData();
            metaData.setServiceId(mapList.get(i).get(JsonConstants.META_RESPONSE_SERVICE_ID));
            metaData.setRValue(mapList.get(i).get(JsonConstants.META_RESPONSE_R_VALUE));
            metaData.setAdultType(mapList.get(i).get(JsonConstants.META_RESPONSE_ADULT_TYPE));
            //カラム名に"index"は使えないため"_index"に変換
            metaData.setIndex(mapList.get(i).get(DataBaseUtils.indexConversion(JsonConstants.META_RESPONSE_INDEX)));
            metaData.setTitle(mapList.get(i).get(JsonConstants.META_RESPONSE_TITLE));
            arrayList.add(metaData);
        }
        DTVTLogger.end();
        return arrayList;
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
        mIsStop = true;
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
        mIsStop = false;
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

    /**
     * マイチャンネル一覧送信.
     *
     * @param myChannelMetaData マイチャンネル一覧
     */
    private void sendMyChannelList(final ArrayList<MyChannelMetaData> myChannelMetaData) {
        if (mApiDataProviderCallback != null) {
            mApiDataProviderCallback.onMyChannelListCallback(myChannelMetaData);
        }
    }

    /**
     * callbackキャンセル用.
     *
     * @param providerCallback callback(nullを設定)
     */
    public void setApiDataProviderCallback(final ApiDataProviderCallback providerCallback) {
        this.mApiDataProviderCallback = providerCallback;
    }
}
