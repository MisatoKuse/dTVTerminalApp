/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.MyChannelMetaData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.MyChannelWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyChannelListJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_RESPONSE = ".sendMyChannelListResponse";
    private static final String SEND_STATUS = ".sendStatus";
    private static final String IS_NUMBER = ".isNumber";
    private static final String RESPONSE = ".MyChannelListResponse";
    private static final String JSON_OBJECT = ".JSONObject";

    private MyChannelWebClient.MyChannelListJsonParserCallback
            myChannelListJsonParserCallback;
    // オブジェクトクラスの定義　
    private MyChannelListResponse myChannelListResponse;

    /**
     * コンストラクタ
     * <p>
     * //     * @param myChannelListJsonParserCallback
     */
    public MyChannelListJsonParser(MyChannelWebClient.MyChannelListJsonParserCallback
                                           myChannelListJsonParserCallback) {
        this.myChannelListJsonParserCallback = myChannelListJsonParserCallback;
        myChannelListResponse = new MyChannelListResponse();
    }

    @Override
    protected void onPostExecute(Object s) {
        myChannelListJsonParserCallback.onMyChannelListJsonParsed(myChannelListResponse);
    }
    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        MyChannelListResponse response =
                myChannelListSender(result);
        return response;
    }

    /**
     * マイチャンネル一覧Jsonデータを解析する
     *
     * @param jsonStr マイチャンネル一覧Jsonデータ
     * @return マイチャンネル一覧取得：正常時レスポンスデータ
     */
    private MyChannelListResponse myChannelListSender(String jsonStr) {

        myChannelListResponse = new MyChannelListResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                sendStatus(jsonObj);
                sendMyChannelListResponse(jsonObj);
                return myChannelListResponse;
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
     * statusとcountの値をマイチャンネル一覧取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);
                if (myChannelListResponse != null) {
                    myChannelListResponse.setStatus(status);
                }
            }
            // countの値を取得しセットする
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_COUNT)) {
                int count = 0;
                try {
                    //数字の場合のみ、値を採用する
                    String stringBuffer = jsonObj.getString(JsonContents.META_RESPONSE_COUNT);
                    if(DBUtils.isNumber(stringBuffer)) {
                        count = Integer.parseInt(stringBuffer);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (JSONException e) {
                    DTVTLogger.debug(CLASS_NAME + IS_NUMBER, e);
                }
                if (myChannelListResponse != null) {
                    myChannelListResponse.setCount(count);
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
     * マイチャンネル一覧コンテンツのListをマイチャンネル一覧取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendMyChannelListResponse(JSONObject jsonObj) {
        try {
            ArrayList<MyChannelMetaData> myChannelMetaDataList = new ArrayList<>();
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_LIST)) {
                // マイチャンネル一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(JsonContents.META_RESPONSE_LIST);
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
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + SEND_RESPONSE, e);
        }
    }
}
