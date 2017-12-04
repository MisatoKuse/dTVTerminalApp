/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ContentsDetailGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreCountGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodProgramMetaFullData;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsDetailGetWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentsDetailJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_RESPONSE = ".sendGenreCountGetResponse";
    private static final String SEND_STATUS = ".sendStatus";
    private static final String RESPONSE = ".GenreCountGetResponse";
    private static final String JSON_OBJECT = ".JSONObject";
    private final ContentsDetailGetWebClient.ContentsDetailJsonParserCallback
            mContentsDetailJsonParserCallback;
    // オブジェクトクラスの定義　
    private ContentsDetailGetResponse mContentsDetailGetResponse;


    /**
     * コンストラクタ
     * <p>
     * //     * @param genreCountGetJsonParserCallback
     */
    public ContentsDetailJsonParser(ContentsDetailGetWebClient.
                                            ContentsDetailJsonParserCallback
                                            contentsDetailJsonParserCallback) {
        mContentsDetailJsonParserCallback =
                contentsDetailJsonParserCallback;
        mContentsDetailGetResponse = new ContentsDetailGetResponse();
    }

    @Override
    protected void onPostExecute(Object s) {
        mContentsDetailJsonParserCallback.onContentsDetailJsonParsed(
                (mContentsDetailGetResponse));
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        return contentsDetailSender(result);
    }

    /**
     * ジャンル毎コンテンツ数取得一覧Jsonデータを解析する
     *
     * @param jsonStr ジャンル毎コンテンツ数取得一覧Jsonデータ
     * @return ジャンル毎コンテンツ数取得一覧取得：正常時レスポンスデータ
     */
    public ContentsDetailGetResponse contentsDetailSender(String jsonStr) {

        mContentsDetailGetResponse = new ContentsDetailGetResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                sendStatus(jsonObj);
                sendContentsDetailGetResponse(jsonObj);
                return mContentsDetailGetResponse;
            }
        } catch (JSONException e) {
            DTVTLogger.debug(CLASS_NAME + JSON_OBJECT, e);
        }
        return null;
    }

    /**
     * statusの値をジャンルコンテンツ数取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(GenreCountGetResponse.GENRE_COUNT_GET_RESPONSE_STATUS)) {
                String status = jsonObj.getString(GenreCountGetResponse.
                        GENRE_COUNT_GET_RESPONSE_STATUS);
                if (mContentsDetailGetResponse != null) {
                    mContentsDetailGetResponse.setStatus(status);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ジャンルコンテンツ数のListをジャンルコンテンツ数取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendContentsDetailGetResponse(JSONObject jsonObj) {
        try {
            ArrayList<VodProgramMetaFullData> vodProgramMetaFullDataArrayList =
                    new ArrayList<>();
            if (!jsonObj.isNull(GenreCountGetResponse.GENRE_COUNT_GET_RESPONSE_LIST)) {
                // VOD＆番組マージメタデータ（フル版）をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(
                        GenreCountGetResponse.GENRE_COUNT_GET_RESPONSE_LIST);
                if (lists.length() == 0) {
                    return;
                }

                //VOD＆番組マージメタデータ（フル版）のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    VodProgramMetaFullData fullData = new VodProgramMetaFullData();

                    //データを個別に転送する
                    fullData.setData(lists.getJSONObject(i));

                    //データを追加
                    vodProgramMetaFullDataArrayList.add(fullData);
                }

                //レスポンスとしてVOD＆番組マージメタデータ（フル版）を返す
                if (mContentsDetailGetResponse != null) {
                    mContentsDetailGetResponse.setVodProgramMetaFullData(
                            vodProgramMetaFullDataArrayList);
                }

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}