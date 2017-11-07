/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationMetaData;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RemoteRecordingReservationListWebClient;

import java.util.ArrayList;
import java.util.List;

// TODO implement WebApiコールバック実装
public class RecordingReservationListDataProvider
        implements RemoteRecordingReservationListWebClient.RemoteRecordingReservationListJsonParserCallback {

    private Context mContext;
    private ApiDataProviderCallback apiDataProviderCallback;
    // ソート完了リスト
    private List<ContentsData> mRecordingReservationList = null;
    // dリモートレスポンス　
    // TODO WebApi側が完成次第コメントアウトを外す
//    private RecordingReservationListResponse mStbResponse = null;
    // STBレスポンス
    private RemoteRecordingReservationListResponse mDRemoteResponse = null;
    // レスポンス突合後リスト
    private List<ContentsData> mBuffMatchList = null;

    // 録画予約ステータスの固定値
    public static final int RECORD_RESERVATION_SYNC_STATUS_REFLECTS_WAITING = 1; // チューナー反映待ち
    public static final int RECORD_RESERVATION_SYNC_STATUS_DURING_REFLECT = 2; // チューナー反映中
    public static final int RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT = 3; // チューナー反映済み
    public static final int RECORD_RESERVATION_SYNC_STATUS_REFLECT_FAILURE = 4; // チューナー反映失敗



    @Override
    public void onRemoteRecordingReservationListJsonParsed(RemoteRecordingReservationListResponse response) {
        if (response != null) {
            mDRemoteResponse = response;
            // リモート側との同期
            if (mDRemoteResponse != null) {
                mRecordingReservationList = buttRecordingReservationListData();
                apiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
            }
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    // TODO WebApi側が完成次第コメントアウトを外す
//    @Override
//    public void onRecordingReservationListJsonParsed(RecordingReservationListResponse response) {
//        if (response != null) {
//            mStbResponse = response;
//            // STB側との同期
//            if(mStbResponse != null) {
//                mRecordingReservationList = buttRecordingReservationListData();
//                apiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
//            }
//        } else {
//            //TODO:WEBAPIを取得できなかった時の処理を記載予定
//        }
//    }

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
     */
    public void requestRecordingReservationListData() {
        getRecordingReservationListData();
    }


    /**
     * 録画予約一覧を取得する
     */
    private void getRecordingReservationListData() {
        RemoteRecordingReservationListWebClient remoteWebClient = new RemoteRecordingReservationListWebClient();
        // TODO メソッド名の修正が入る
//        remoteWebClient.getRentalVodListApi(this);

        // TODO 通信クラスにデータ取得要求を出す(STB)
        // TODO WebApi側が完成次第コメントアウトを外す
//        RecordingReservationListWebClient stbWebClient = new RecordingReservationListWebClient();
//        stbWebClient.getListApi(this);
    }

    /**
     * ContentsDataを生成(STB)
     *
     * @param data
     * @return
     */
    private ContentsData createContentsData(RemoteRecordingReservationMetaData data) {
        ContentsData contentsData = new ContentsData();
//        contentsData.setTitle(data.getmTitle());
//        contentsData.setTime(data.getmStart_time());
//        contentsData.setRecordingReservationStatus(data.getmSync_status());
        return contentsData;
    }

//    /**
//     * ContentsDataを生成(dリモート)
//     *
//     * @param data
//     * @return
//     */
//    private ContentsData createContentsData(RecordingReservationMetaData data) {
//        ContentsData contentsData = new ContentsData();
////        contentsData.setTitle(data.getmTitle());
////        contentsData.setTime(data.getmStart_time());
////        contentsData.setRecordingReservationStatus(data.getmSync_status());
//        return contentsData;
//    }

    // TODO データの突合
    private List<ContentsData> buttRecordingReservationListData() {
        mBuffMatchList = new ArrayList<ContentsData>();
        ContentsData contentsData = null;
        List<RemoteRecordingReservationMetaData> remoteList = mDRemoteResponse.getRemoteRecordingReservationMetaData();
//        List<RecordingReservationMetaData> stbList = mStbResponse.getRecordingReservationMetaData();
//        for(int i=0 ; i < remoteList.size() ; i++) {
//            contentsData = null;
//            for(int j=0 ; j < stbList.size() ; j++) {
//                if(remoteList.get(i).getmService_id().equals(stbList.get(j).getServiceId())
//                        && remoteList.get(i).getmEvent_id().equals(stbList.get(j).getEventId())) {
//                    contentsData = createContentsData(remoteList.get(i));
//
//                    mBuffMatchList.add(contentsData);
//                    break;
//                }
//            }
//            if(j < stbList.size()) {
//                contentsData = createContentsData(remoteList.get(i));
//                mBuffMatchList.add(contentsData);
//            }
//        }
//        for(int i=0 ; i < stbList.size() ; i++) {
//            contentsData = null;
//            for(int j=0 ; j < remoteList.size() ; j++) {
//                if(remoteList.get(j).getmService_id().equals(stbList.get(i).getServiceId())
//                        && remoteList.get(j).getmEvent_id().equals(stbList.get(i).getEventId())) {
//                    break;
//                }
//            }
//            if(j < remoteList.size()) {
//                contentsData = createContentsData(stbList.get(i));
//                mBuffMatchList.add(contentsData);
//            }
//        }
        return null;
    }

    // TODO データのソート（開始時刻順）
    private List<ContentsData> sortRecordingReservationListData(List<ContentsData> beforeList) {
        mRecordingReservationList = new ArrayList<ContentsData>();
        mRecordingReservationList.add(beforeList.get(0));
        int i = 0;
        for(ContentsData data : beforeList) {
//            if(data.getTime() < mRecordingReservationList.get(i).getTime()) {
//
//            }
        }

        return null;
    }
}
