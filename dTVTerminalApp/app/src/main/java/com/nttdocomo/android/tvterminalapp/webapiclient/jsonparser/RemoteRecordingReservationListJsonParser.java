/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationMetaData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RemoteRecordingReservationListWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * リモート録画予約一覧用JsonParserクラス.
 */
public class RemoteRecordingReservationListJsonParser extends AsyncTask<Object, Object, Object> {
    /**callback.*/
    private RemoteRecordingReservationListWebClient.RemoteRecordingReservationListJsonParserCallback
                                     mRemoteRecordingReservationListJsonParserCallback;

    /**オブジェクトクラスの定義.*/
    private RemoteRecordingReservationListResponse mRemoteRecordingReservationListResponse
            = new RemoteRecordingReservationListResponse();

    /**
     * コンストラクタ.
     * <p>
     * @param remoteRecordingReservationListJsonParserCallback remoteRecordingReservationListJsonParserCallback
     */
    public RemoteRecordingReservationListJsonParser(final RemoteRecordingReservationListWebClient.RemoteRecordingReservationListJsonParserCallback
                                           remoteRecordingReservationListJsonParserCallback) {
        mRemoteRecordingReservationListJsonParserCallback =
                remoteRecordingReservationListJsonParserCallback;
        mRemoteRecordingReservationListResponse = new RemoteRecordingReservationListResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        mRemoteRecordingReservationListJsonParserCallback
                .onRemoteRecordingReservationListJsonParsed(
                        mRemoteRecordingReservationListResponse);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        RemoteRecordingReservationListResponse response =
                remoteRecordingReservationListSender(result);
        return response;
    }

    /**
     * リモート録画予約一覧Jsonデータを解析する.
     *
     * @param jsonStr リモート録画予約一覧Jsonデータ
     * @return リモート録画予約一覧取得：正常時レスポンスデータ
     */
    public RemoteRecordingReservationListResponse remoteRecordingReservationListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
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
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusとcountの値をリモート録画予約一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                if (mRemoteRecordingReservationListResponse != null) {
                    mRemoteRecordingReservationListResponse.setStatus(status);
                }
            }
            // countの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_COUNT)) {
                int count = 0;
                try {
                    //数字の場合のみ、値を採用する
                    String stringBuffer = jsonObj.getString(JsonConstants.META_RESPONSE_COUNT);
                    if (DBUtils.isNumber(stringBuffer)) {
                        count = Integer.parseInt(stringBuffer);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (JSONException e) {
                    DTVTLogger.debug(e);
                }
                if (mRemoteRecordingReservationListResponse != null) {
                    mRemoteRecordingReservationListResponse.setCount(count);
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * リモート録画予約一覧コンテンツのListをリモート録画予約一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendRemoteRecordingReservationListResponse(final JSONObject jsonObj) {
        try {
            ArrayList<RemoteRecordingReservationMetaData> remoteRecordingReservationMetaDataList = new ArrayList<RemoteRecordingReservationMetaData>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                // リモート録画予約一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
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
            DTVTLogger.debug(e);
        }
    }
}