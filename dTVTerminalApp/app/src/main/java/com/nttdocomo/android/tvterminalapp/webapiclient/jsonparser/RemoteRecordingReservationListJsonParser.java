/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationMetaData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse.REMOTE_RECORDING_RESERVATION_META_RESPONSE_COUNT;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse.REMOTE_RECORDING_RESERVATION_META_RESPONSE_LIST;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse.REMOTE_RECORDING_RESERVATION_META_RESPONSE_STATUS;

public class RemoteRecordingReservationListJsonParser extends AsyncTask<Object, Object, Object> {
    //    private RemoteRecordingReservationListWebClient.RemoteRecordingReservationListJsonParserCallback mRemoteRecordingReservationListJsonParserCallback;
    // オブジェクトクラスの定義　
    private RemoteRecordingReservationListResponse mRemoteRecordingReservationListResponse;

    /**
     * コンストラクタ
     * <p>
     * //     * @param remoteRecordingReservationListJsonParserCallback
     */
//    public RemoteRecordingReservationListJsonParser(RemoteRecordingReservationListWebClient.RemoteRecordingReservationListJsonParserCallback remoteRecordingReservationListJsonParserCallback) {
//        mRemoteRecordingReservationListJsonParserCallback = remoteRecordingReservationListJsonParserCallback;
//        mRemoteRecordingReservationListResponse = new RemoteRecordingReservationListResponse();
//    }
//
//    @Override
//    protected void onPostExecute(Object s) {
//        mRemoteRecordingReservationListJsonParserCallback.onRemoteRecordingReservationListJsonParsed(mRemoteRecordingReservationListResponse);
//    }
    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        RemoteRecordingReservationListResponse response = remoteRecordingReservationListSender(result);
        return response;
    }

    /**
     * リモート録画予約一覧Jsonデータを解析する
     *
     * @param jsonStr リモート録画予約一覧Jsonデータ
     * @return リモート録画予約一覧取得：正常時レスポンスデータ
     */
    public RemoteRecordingReservationListResponse remoteRecordingReservationListSender(String jsonStr) {

        mRemoteRecordingReservationListResponse = new RemoteRecordingReservationListResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj != null) {
                    sendStatus(jsonObj);
                    sendRemoteRecordingReservationListResponse(jsonObj);

                    return mRemoteRecordingReservationListResponse;
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * statusとcountの値をリモート録画予約一覧取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(REMOTE_RECORDING_RESERVATION_META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(REMOTE_RECORDING_RESERVATION_META_RESPONSE_STATUS);
                if (mRemoteRecordingReservationListResponse != null) {
                    mRemoteRecordingReservationListResponse.setStatus(status);
                }
            }
            // countの値を取得しセットする
            if (!jsonObj.isNull(REMOTE_RECORDING_RESERVATION_META_RESPONSE_COUNT)) {
                String count = null;
                try {
                    count = jsonObj.getString(REMOTE_RECORDING_RESERVATION_META_RESPONSE_COUNT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mRemoteRecordingReservationListResponse != null) {
                    mRemoteRecordingReservationListResponse.setCount(count);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * リモート録画予約一覧コンテンツのListをリモート録画予約一覧取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendRemoteRecordingReservationListResponse(JSONObject jsonObj) {
        try {
            ArrayList<RemoteRecordingReservationMetaData> remoteRecordingReservationMetaDataList = new ArrayList<RemoteRecordingReservationMetaData>();
            if (!jsonObj.isNull(REMOTE_RECORDING_RESERVATION_META_RESPONSE_LIST)) {
                // リモート録画予約一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(REMOTE_RECORDING_RESERVATION_META_RESPONSE_LIST);
                if (lists.length() == 0) {
                    return;
                }
                //リモート録画予約一覧のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    RemoteRecordingReservationMetaData remoteRecordingReservationMetaData = new RemoteRecordingReservationMetaData();
                    remoteRecordingReservationMetaData.setData(lists.getJSONObject(i));
                    remoteRecordingReservationMetaDataList.add(remoteRecordingReservationMetaData);
                }
                if (mRemoteRecordingReservationListResponse != null) {
                    // リモート録画予約一覧リストをセットする
                    mRemoteRecordingReservationListResponse.setRemoteRecordingReservationMetaData(remoteRecordingReservationMetaDataList);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
