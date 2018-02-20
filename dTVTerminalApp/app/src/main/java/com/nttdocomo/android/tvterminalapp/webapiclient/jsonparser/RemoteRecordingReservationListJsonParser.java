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

public class RemoteRecordingReservationListJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_RESPONSE = ".sendRemoteRecordingReservationListResponse";
    private static final String SEND_STATUS = ".sendStatus";
    private static final String IS_NUMBER = ".isNumber";
    private static final String RESPONSE = ".RemoteRecordingReservationListResponse";
    private static final String JSON_OBJECT = ".JSONObject";

    private RemoteRecordingReservationListWebClient.
            RemoteRecordingReservationListJsonParserCallback
            mRemoteRecordingReservationListJsonParserCallback;

    // オブジェクトクラスの定義　
    private RemoteRecordingReservationListResponse mRemoteRecordingReservationListResponse
            = new RemoteRecordingReservationListResponse();

    /**
     * コンストラクタ
     * <p>
     * //     * @param remoteRecordingReservationListJsonParserCallback
     */
    public RemoteRecordingReservationListJsonParser(RemoteRecordingReservationListWebClient.
                                                            RemoteRecordingReservationListJsonParserCallback
                                                            remoteRecordingReservationListJsonParserCallback) {
        mRemoteRecordingReservationListJsonParserCallback =
                remoteRecordingReservationListJsonParserCallback;
        mRemoteRecordingReservationListResponse = new RemoteRecordingReservationListResponse();
    }

    @Override
    protected void onPostExecute(Object s) {
        mRemoteRecordingReservationListJsonParserCallback
                .onRemoteRecordingReservationListJsonParsed(
                        mRemoteRecordingReservationListResponse);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        RemoteRecordingReservationListResponse response =
                remoteRecordingReservationListSender(result);
        return response;
    }

    /**
     * リモート録画予約一覧Jsonデータを解析する
     *
     * @param jsonStr リモート録画予約一覧Jsonデータ
     * @return リモート録画予約一覧取得：正常時レスポンスデータ
     */
    public RemoteRecordingReservationListResponse remoteRecordingReservationListSender(String jsonStr) {

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
            DTVTLogger.debug(CLASS_NAME + JSON_OBJECT, e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + RESPONSE, e);
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
                    DTVTLogger.debug(CLASS_NAME + IS_NUMBER, e);
                }
                if (mRemoteRecordingReservationListResponse != null) {
                    mRemoteRecordingReservationListResponse.setCount(count);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + SEND_STATUS, e);
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
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + SEND_RESPONSE, e);
        }
    }
}