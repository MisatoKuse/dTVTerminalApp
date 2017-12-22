/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelDeleteResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelDeleteWebClient;

import org.json.JSONException;
import org.json.JSONObject;


public class MyChannelDeleteJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_STATUS = ".sendStatus";
    private static final String RESPONSE = ".MyChannelDeleteResponse";
    private static final String JSON_OBJECT = ".JSONObject";

    private MyChannelDeleteWebClient.MyChannelDeleteJsonParserCallback
            myChannelDeleteJsonParserCallback;
    // オブジェクトクラスの定義　
    private MyChannelDeleteResponse myChannelDeleteResponse;

    /**
     * コンストラクタ
     * <p>
     * //     * @param myChannelDeleteJsonParserCallback
     */
    public MyChannelDeleteJsonParser(MyChannelDeleteWebClient.MyChannelDeleteJsonParserCallback
                                             myChannelDeleteJsonParserCallback) {
        this.myChannelDeleteJsonParserCallback = myChannelDeleteJsonParserCallback;
        myChannelDeleteResponse = new MyChannelDeleteResponse();
    }

    @Override
    protected void onPostExecute(Object s) {
        myChannelDeleteJsonParserCallback.onMyChannelDeleteJsonParsed(myChannelDeleteResponse);
    }
    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        MyChannelDeleteResponse response =
                myChannelDeleteSender(result);
        return response;
    }

    /**
     * マイチャンネル解除Jsonデータを解析する
     *
     * @param jsonStr マイチャンネル解除Jsonデータ
     * @return マイチャンネル解除取得：正常時レスポンスデータ
     */
    private MyChannelDeleteResponse myChannelDeleteSender(String jsonStr) {

        myChannelDeleteResponse = new MyChannelDeleteResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                sendStatus(jsonObj);
                return myChannelDeleteResponse;
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
     * statusの値をマイチャンネル解除取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);
                if (myChannelDeleteResponse != null) {
                    myChannelDeleteResponse.setStatus(status);
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
