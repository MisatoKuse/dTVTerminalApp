/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelRegisterResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelRegisterWebClient;

import org.json.JSONException;
import org.json.JSONObject;

public class MyChannelRegisterJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_STATUS = ".sendStatus";
    private static final String RESPONSE = ".MyChannelRegisterResponse";
    private static final String JSON_OBJECT = ".JSONObject";

    private MyChannelRegisterWebClient.MyChannelRegisterJsonParserCallback
            myChannelRegisterJsonParserCallback;
    // オブジェクトクラスの定義　
    private MyChannelRegisterResponse myChannelRegisterResponse;

    /**
     * コンストラクタ.
     * <p>
     * //     * @param myChannelRegisterJsonParserCallback
     */
    public MyChannelRegisterJsonParser(final MyChannelRegisterWebClient.MyChannelRegisterJsonParserCallback myChannelRegisterJsonParserCallback) {
        this.myChannelRegisterJsonParserCallback = myChannelRegisterJsonParserCallback;
        myChannelRegisterResponse = new MyChannelRegisterResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        myChannelRegisterJsonParserCallback.onMyChannelRegisterJsonParsed(myChannelRegisterResponse);
    }
    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        MyChannelRegisterResponse response = myChannelRegisterSender(result);
        return response;
    }

    /**
     * マイチャンネル登録Jsonデータを解析する.
     *
     * @param jsonStr マイチャンネル登録Jsonデータ
     * @return マイチャンネル登録取得：正常時レスポンスデータ
     */
    private MyChannelRegisterResponse myChannelRegisterSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        myChannelRegisterResponse = new MyChannelRegisterResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                sendStatus(jsonObj);
                return myChannelRegisterResponse;
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
     * statusの値をマイチャンネル登録取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                if (myChannelRegisterResponse != null) {
                    myChannelRegisterResponse.setStatus(status);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + SEND_STATUS, e);
        }
    }
}
