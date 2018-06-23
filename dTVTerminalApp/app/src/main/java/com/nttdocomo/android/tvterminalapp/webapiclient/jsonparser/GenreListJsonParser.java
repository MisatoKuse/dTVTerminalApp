/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreListWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * ジャンル一覧JsonParser.
 */
public class GenreListJsonParser extends AsyncTask<String, Object, Object> {
    /**GenreListJsonParserCallback.*/
    private final GenreListWebClient.GenreListJsonParserCallback
            mGenreListJsonParserCallback;
    /**オブジェクトクラスの定義　.*/
    private GenreListResponse mGenreListResponse;

    /**
     * コンストラクタ.
     * <p>
     * @param genreListJsonParserCallback  genreListJsonParserCallback
     */
    public GenreListJsonParser(final GenreListWebClient.GenreListJsonParserCallback genreListJsonParserCallback) {
        mGenreListJsonParserCallback =
                genreListJsonParserCallback;
        mGenreListResponse = new GenreListResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        mGenreListJsonParserCallback.
                onGenreListJsonParsed(mGenreListResponse);
    }

    @Override
    protected Object doInBackground(final String... strings) {
        String result = strings[0];
        GenreListResponse response = genreListSender(result);
        return response;
    }

    /**
     * ジャンル一覧Jsonデータを解析する.
     *
     * @param jsonStr ジジャンル一覧Jsonデータ
     * @return ジャンル一覧取得：正常時レスポンスデータ
     */
    private GenreListResponse genreListSender(final String jsonStr) {

//        DTVTLogger.debugHttp(jsonStr); ログを確認したい場合に使用
        mGenreListResponse = new GenreListResponse();
        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj != null) {
                    sendUpdateDate(jsonObj);
                    sendGenreListResponse(jsonObj);
                    return mGenreListResponse;
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * UpdateDateの値をジャンルコンテンツ数取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendUpdateDate(final JSONObject jsonObj) {
        try {
            // UpdateDateの値を取得しセットする
            if (!jsonObj.isNull(GenreListResponse.GENRE_LIST_RESPONSE_UPDATE_DATE)) {
                String UpdateDate = jsonObj.getString(
                        GenreListResponse.GENRE_LIST_RESPONSE_UPDATE_DATE);
                if (mGenreListResponse != null) {
                    mGenreListResponse.setUpdateDate(UpdateDate);
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * ジャンル一覧のデータをジャンル一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendGenreListResponse(final JSONObject jsonObj) {
        try {
            ArrayList<GenreListMetaData> genreListMetaDataList;
            Iterator<String> iterator_key = jsonObj.keys();
            while (iterator_key.hasNext()) {
                String item = iterator_key.next();
                if (item.equals(GenreListResponse.GENRE_LIST_RESPONSE_UPDATE_DATE)) {
                    continue;
                }
                JSONArray lists = jsonObj.getJSONArray(item);
                if (lists.length() == 0) {
                    continue;
                }
                genreListMetaDataList =
                        new ArrayList<GenreListMetaData>();
                //ジャンル一覧のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    GenreListMetaData genreListMetaData =
                            new GenreListMetaData();
                    genreListMetaData.setData(lists.getJSONObject(i));
                    genreListMetaDataList.add(genreListMetaData);
                }
                if (mGenreListResponse != null) {
                    //種別名をキーにしてジャンル毎コンテンツを蓄積する
                    mGenreListResponse.addTypeList(item, genreListMetaDataList);
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}
