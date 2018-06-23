/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreCountGetWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ジャンル毎コンテンツ数取得JsonParser.
 */
public class GenreCountGetJsonParser extends AsyncTask<Object, Object, Object> {
    /**コールバック.*/
    private final GenreCountGetWebClient.GenreCountGetJsonParserCallback
            mGenreCountGetJsonParserCallback;
    /**オブジェクトクラスの定義　.*/
    private GenreCountGetResponse mGenreCountGetResponse;


    /**
     * コンストラクタ.
     * <p>
     *@param genreCountGetJsonParserCallback コールバック
     */
    public GenreCountGetJsonParser(final GenreCountGetWebClient.GenreCountGetJsonParserCallback genreCountGetJsonParserCallback) {
        mGenreCountGetJsonParserCallback =
                genreCountGetJsonParserCallback;
        mGenreCountGetResponse = new GenreCountGetResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        mGenreCountGetJsonParserCallback.
                onGenreCountGetJsonParsed(mGenreCountGetResponse);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        GenreCountGetResponse response = genreCountGetSender(result);
        return response;
    }

    /**
     * ジャンル毎コンテンツ数取得一覧Jsonデータを解析する.
     *
     * @param jsonStr ジャンル毎コンテンツ数取得一覧Jsonデータ
     * @return ジャンル毎コンテンツ数取得一覧取得：正常時レスポンスデータ
     */
    private GenreCountGetResponse genreCountGetSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mGenreCountGetResponse = new GenreCountGetResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj != null) {
                    sendStatus(jsonObj);
                    sendGenreCountGetResponse(jsonObj);
                    return mGenreCountGetResponse;
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をジャンルコンテンツ数取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                if (mGenreCountGetResponse != null) {
                    mGenreCountGetResponse.setStatus(status);
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * ジャンルコンテンツ数のListをジャンルコンテンツ数取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendGenreCountGetResponse(final JSONObject jsonObj) {
        try {
            ArrayList<GenreCountGetMetaData> genreCountGetMetaDataList =
                    new ArrayList<GenreCountGetMetaData>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                // ジャンル毎コンテンツ数取得一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                if (lists.length() == 0) {
                    return;
                }
                //ジャンル毎コンテンツ数取得一覧のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    GenreCountGetMetaData genreCountGetMetaData =
                            new GenreCountGetMetaData();
                    genreCountGetMetaData.setData(lists.getJSONObject(i));
                    genreCountGetMetaDataList.add(genreCountGetMetaData);
                }
                if (mGenreCountGetResponse != null) {
                    // ジャンル毎コンテンツ数取得リストをセットする
                    mGenreCountGetResponse.setGenreCountGetMetaData(
                            genreCountGetMetaDataList);
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}
