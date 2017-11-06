/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;

import java.util.List;

// TODO implement WebApiコールバック実装
public class RecordingReservationListDataProvider {

    private Context mContext;
//    private mDRemoteRecordingReservationList = null;
//    private mSTBRecordingReservationList = null;

    // TODO コールバック関数(dリモート)
    public void onDRemoteRecordingReservationListJsonParsed() {
//        if (response != null) {
              // TODO メンバ変数に値を格納
//            sendRecordingReservationListData(response);
                // TODO STB側のコールバックと同期
//                if(mSTBRecordingReservationList != null) {
//
//                }
//        } else {
//            //TODO:WEBAPIを取得できなかった時の処理を記載予定
//        }
    }

    // TODO コールバック関数(STB)
    public void onSTBRecordingReservationListJsonParsed() {
        //        if (response != null) {
        // TODO メンバ変数に値を格納
//            sendRecordingReservationListData(response);
        // TODO dリモート側のコールバックと同期
//                if(mSTBRecordingReservationList != null) {
//
//                }
//        } else {
//            //TODO:WEBAPIを取得できなかった時の処理を記載予定
//        }
    }

    /**
     * WebApiからのコールバックデータを返却するためのActivity実装用コールバック
     */
    public interface ApiDataProviderCallback {

        /**
         * 録画予約一覧返却用コールバック
         *
         * @param list
         */
        void recordingReservationListCallback(List<ContentsData> list);
    }

    private ApiDataProviderCallback apiDataProviderCallback;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public RecordingReservationListDataProvider(Context mContext) {
        this.mContext = mContext;
        this.apiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * Activityからのデータ取得要求受付
     *
     */
    public void requestRecordingReservationListData() {


    }

    /**
     * 一覧データをActivityに送る
     *
     * @param
     */
    public void sendRentalListData( ) {

//        //ContentsList生成
//        List<ContentsData> list = makeContentsData();
//
//        //レンタル一覧を送る
//        apiDataProviderCallback.recordingReservationListCallback(list);
    }

    /**
     * 録画予約一覧を取得する
     */
    private void getRecordingReservationListData() {
        // TODO 通信クラスにデータ取得要求を出す(dリモート)
//        WebClient webClient = new WebClient();
//        webClient.getListApi(this);

        // TODO 通信クラスにデータ取得要求を出す(STB)
//        WebClient webClient = new WebClient();
//        webClient.getListApi(this);
    }

    // TODO データの突合
    private List<ContentsData> buttRecordingReservationListData() {
        return null;
    }

    // TODO データのソート（開始時刻順）
    private List<ContentsData> sortRecordingReservationListData() {
        return null;
    }
}
