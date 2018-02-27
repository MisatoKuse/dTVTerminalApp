/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationMetaData;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RecordingReservationListWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecordingReservationListJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_RESPONSE = ".sendRecordingReservationListResponse";
    private static final String SEND_STATUS = ".sendStatus";
    private static final String RESPONSE = ".RecordingReservationListResponse";
    private static final String JSON_OBJECT = ".JSONObject";

    private RecordingReservationListWebClient.RecordingReservationListJsonParserCallback
            mRecordingReservationListJsonParserCallback;
    // オブジェクトクラスの定義　
    private RecordingReservationListResponse mRecordingReservationListResponse;

    /**
     * コンストラクタ.
     * <p>
     * //     * @param recordingReservationListJsonParserCallback
     */
    public RecordingReservationListJsonParser(final RecordingReservationListWebClient.RecordingReservationListJsonParserCallback recordingReservationListJsonParserCallback) {
        mRecordingReservationListJsonParserCallback =
                recordingReservationListJsonParserCallback;
        mRecordingReservationListResponse = new RecordingReservationListResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        mRecordingReservationListJsonParserCallback.
            onRecordingReservationListJsonParsed(mRecordingReservationListResponse);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        RecordingReservationListResponse response = recordingReservationListSender(result);
        return response;
    }

    /**
     * 録画予約一覧Jsonデータを解析する.
     *
     * @param jsonStr 録画予約一覧Jsonデータ
     * @return 録画予約一覧取得：正常時レスポンスデータ
     */
    public RecordingReservationListResponse recordingReservationListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mRecordingReservationListResponse = new RecordingReservationListResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj != null) {
                    sendStatus(jsonObj);
                    sendRecordingReservationListResponse(jsonObj);
                    return mRecordingReservationListResponse;
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(CLASS_NAME + JSON_OBJECT, e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + RESPONSE, e);
        }
        return null;
    }

    /**
     * statusとpagerの値を録画予約一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                if (mRecordingReservationListResponse != null) {
                    mRecordingReservationListResponse.setStatus(status);
                }
            }

            // 録画予約情報受信時刻を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_RESERVATION)) {
                String reservation = jsonObj.getString(JsonConstants.META_RESPONSE_RESERVATION);
                if (mRecordingReservationListResponse != null) {
                    mRecordingReservationListResponse.setReservation(reservation);
                }
            }

            //pagerの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonConstants.META_RESPONSE_PAGER);
                mRecordingReservationListResponse.setPager(pager);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + SEND_STATUS, e);
        }
    }

    /**
     * 録画予約一覧コンテンツのListを録画予約一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendRecordingReservationListResponse(final JSONObject jsonObj) {
        try {
            ArrayList<RecordingReservationMetaData> recordingReservationMetaDataList =
                    new ArrayList<RecordingReservationMetaData>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_RESERVATION_LIST)) {
                // 録画予約一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(
                        JsonConstants.META_RESPONSE_RESERVATION_LIST);
                if (lists.length() == 0) {
                    return;
                }
                //録画予約一覧のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    RecordingReservationMetaData recordingReservationMetaData =
                            new RecordingReservationMetaData();
                    recordingReservationMetaData.setData(lists.getJSONObject(i));
                    recordingReservationMetaDataList.add(recordingReservationMetaData);
                }
                if (mRecordingReservationListResponse != null) {
                    // 録画予約一覧リストをセットする
                    mRecordingReservationListResponse.setRecordingReservationMetaData(
                            recordingReservationMetaDataList);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + SEND_RESPONSE, e);
        }
    }
}