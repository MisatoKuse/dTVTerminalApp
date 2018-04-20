/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * マイチャンネル一覧用JsonParserクラス.
 */
public class MyChannelListJsonParser extends AsyncTask<Object, Object, Object> {
    /**callback.*/
    private MyChannelWebClient.MyChannelListJsonParserCallback
            myChannelListJsonParserCallback;
    /**オブジェクトクラスの定義　.*/
    private MyChannelListResponse myChannelListResponse;

    /**
     * コンストラクタ.
     * <p>
     *@param myChannelListJsonParserCallback callback
     */
    public MyChannelListJsonParser(final MyChannelWebClient.MyChannelListJsonParserCallback myChannelListJsonParserCallback) {
        this.myChannelListJsonParserCallback = myChannelListJsonParserCallback;
        myChannelListResponse = new MyChannelListResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        myChannelListJsonParserCallback.onMyChannelListJsonParsed(myChannelListResponse);
    }
    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        MyChannelListResponse response = myChannelListSender(result);
        return response;
    }

    /**
     * マイチャンネル一覧Jsonデータを解析する.
     *
     * @param jsonStr マイチャンネル一覧Jsonデータ
     * @return マイチャンネル一覧取得：正常時レスポンスデータ
     */
    private MyChannelListResponse myChannelListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        myChannelListResponse = new MyChannelListResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                sendStatus(jsonObj);
                sendMyChannelListResponse(jsonObj);
                return myChannelListResponse;
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusとcountの値をマイチャンネル一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                if (myChannelListResponse != null) {
                    myChannelListResponse.setStatus(status);
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
                if (myChannelListResponse != null) {
                    myChannelListResponse.setCount(count);
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * マイチャンネル一覧コンテンツのListをマイチャンネル一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendMyChannelListResponse(final JSONObject jsonObj) {
        try {
            ArrayList<MyChannelMetaData> myChannelMetaDataList = new ArrayList<>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                // マイチャンネル一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                if (lists.length() == 0) {
                    return;
                }
                //マイチャンネル一覧のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    MyChannelMetaData myChannelMetaData = new MyChannelMetaData();
                    myChannelMetaData.setData(lists.getJSONObject(i));
                    myChannelMetaDataList.add(myChannelMetaData);
                }
                if (myChannelListResponse != null) {
                    // マイチャンネル一覧リストをセットする
                    myChannelListResponse.setMyChannelMetaData(myChannelMetaDataList);
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}
