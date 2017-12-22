/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RemoteRecordingReservationClient;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoteRecordingReservationJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_STATUS = ".sendStatus";
    private static final String RESPONSE = ".RemoteRecordingReservationResultResponse";
    private static final String JSON_OBJECT = ".JSONObject";

    private RemoteRecordingReservationClient.RemoteRecordingReservationJsonParserCallback
            mRemoteRecordingReservationJsonParserCallback;
    // オブジェクトクラスの定義　
    private RemoteRecordingReservationResultResponse mRemoteRecordingReservationResultResponse;

    /**
     * コンストラクタ
     */
    public RemoteRecordingReservationJsonParser(RemoteRecordingReservationClient.
                                                        RemoteRecordingReservationJsonParserCallback recordingReservationListJsonParserCallback) {
        mRemoteRecordingReservationJsonParserCallback =
                recordingReservationListJsonParserCallback;
        mRemoteRecordingReservationResultResponse = null;
    }

    @Override
    protected void onPostExecute(Object s) {
        mRemoteRecordingReservationJsonParserCallback.
                onRemoteRecordingReservationJsonParsed(mRemoteRecordingReservationResultResponse);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        RemoteRecordingReservationResultResponse response = remoteRecordingReservationResultSender(result);
        return response;
    }

    /**
     * 録画予約一覧Jsonデータを解析する
     *
     * @param jsonStr 録画予約一覧Jsonデータ
     * @return 録画予約一覧取得：正常時レスポンスデータ
     */
    public RemoteRecordingReservationResultResponse remoteRecordingReservationResultSender(String jsonStr) {

        mRemoteRecordingReservationResultResponse = null;

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj != null) {
                    sendStatus(jsonObj);
                    return mRemoteRecordingReservationResultResponse;
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(CLASS_NAME + JSON_OBJECT, e);
        } catch (Exception e) {
            DTVTLogger.debug(CLASS_NAME + RESPONSE, e);
        }
        return null;
    }

    /**
     * statusとerrornoの値をリモート録画予約：レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendStatus(JSONObject jsonObj) {
        try {
            String status = null;
            String errorNo = null;
            // statusの値を取得
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);

            }
            //errornoの値を取得
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_NG_ERROR_NO)) {
                errorNo = jsonObj.getString(JsonContents.META_RESPONSE_NG_ERROR_NO);
            }
            // レスポンスデータに格納（errornoはnullの場合もある）
            mRemoteRecordingReservationResultResponse
                    = new RemoteRecordingReservationResultResponse(status, errorNo);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            DTVTLogger.debug(CLASS_NAME + SEND_STATUS, e);
        }
    }
}
