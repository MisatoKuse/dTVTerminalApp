/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelDeleteResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelDeleteWebClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * マイチャンネル削除用JsonParserクラス.
 */
public class MyChannelDeleteJsonParser extends AsyncTask<Object, Object, Object> {
    /**マイチャンネル削除JsonParser コールバック.*/
    private final MyChannelDeleteWebClient.MyChannelDeleteJsonParserCallback
            myChannelDeleteJsonParserCallback;
    /**オブジェクトクラスの定義.*/
    private MyChannelDeleteResponse myChannelDeleteResponse;

    /**
     * コンストラクタ.
     * <p>
     * @param myChannelDeleteJsonParserCallback  コールバック
     */
    public MyChannelDeleteJsonParser(final MyChannelDeleteWebClient.MyChannelDeleteJsonParserCallback myChannelDeleteJsonParserCallback) {
        this.myChannelDeleteJsonParserCallback = myChannelDeleteJsonParserCallback;
        myChannelDeleteResponse = new MyChannelDeleteResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        myChannelDeleteJsonParserCallback.onMyChannelDeleteJsonParsed(myChannelDeleteResponse);
    }
    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        MyChannelDeleteResponse response = myChannelDeleteSender(result);
        return response;
    }

    /**
     * マイチャンネル解除Jsonデータを解析する.
     *
     * @param jsonStr マイチャンネル解除Jsonデータ
     * @return マイチャンネル解除取得：正常時レスポンスデータ
     */
    private MyChannelDeleteResponse myChannelDeleteSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        myChannelDeleteResponse = new MyChannelDeleteResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                sendStatus(jsonObj);
                return myChannelDeleteResponse;
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をマイチャンネル解除取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                if (myChannelDeleteResponse != null) {
                    myChannelDeleteResponse.setStatus(status);
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}
