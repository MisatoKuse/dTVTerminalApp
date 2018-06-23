/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationResultResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RemoteRecordingReservationWebClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * リモート録画予約登録用JsonParserクラス.
 */
public class RemoteRecordingReservationJsonParser extends AsyncTask<Object, Object, Object> {
    /**callback.*/
    private final RemoteRecordingReservationWebClient.RemoteRecordingReservationJsonParserCallback
            mRemoteRecordingReservationJsonParserCallback;
    /**オブジェクトクラスの定義.*/
    private RemoteRecordingReservationResultResponse mRemoteRecordingReservationResultResponse;

    /**
     * コンストラクタ.
     * @param recordingReservationListJsonParserCallback callback
     */
    public RemoteRecordingReservationJsonParser(final RemoteRecordingReservationWebClient.
                  RemoteRecordingReservationJsonParserCallback recordingReservationListJsonParserCallback) {
        mRemoteRecordingReservationJsonParserCallback =
                recordingReservationListJsonParserCallback;
        mRemoteRecordingReservationResultResponse = null;
    }

    @Override
    protected void onPostExecute(final Object s) {
        mRemoteRecordingReservationJsonParserCallback.
                onRemoteRecordingReservationJsonParsed(mRemoteRecordingReservationResultResponse);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        RemoteRecordingReservationResultResponse response = remoteRecordingReservationResultSender(result);
        return response;
    }

    /**
     * 録画予約登録リザルトJsonデータを解析する.
     *
     * @param jsonStr 録画予約一覧Jsonデータ
     * @return 録画予約一覧取得：正常時レスポンスデータ
     */
    private RemoteRecordingReservationResultResponse remoteRecordingReservationResultSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mRemoteRecordingReservationResultResponse = null;

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj != null) {
                    sendStatus(jsonObj);
                    return mRemoteRecordingReservationResultResponse;
                }
                DTVTLogger.debug("JsonStr is Null");
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusとerrornoの値をリモート録画予約：レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            String status = null;
            String errorNo = null;
            // statusの値を取得
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);

            }
            //errornoの値を取得
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_NG_ERROR_NO)) {
                errorNo = jsonObj.getString(JsonConstants.META_RESPONSE_NG_ERROR_NO);
            }
            // レスポンスデータに格納（errornoはnullの場合もある）
            mRemoteRecordingReservationResultResponse
                    = new RemoteRecordingReservationResultResponse(status, errorNo);
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}
