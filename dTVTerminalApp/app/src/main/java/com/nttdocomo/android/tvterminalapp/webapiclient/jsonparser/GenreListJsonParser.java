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

import static com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse.GENRE_LIST_RESPONSE_ARIB_LIST;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse.GENRE_LIST_RESPONSE_NOD_LIST;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse.GENRE_LIST_RESPONSE_PLALA_LIST;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse.GENRE_LIST_RESPONSE_UPDATE_DATE;

public class GenreListJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_RESPONSE = ".sendGenreListResponse";
    private static final String SEND_UPDATE_DATE = ".sendUpdateDate";
    private static final String RESPONSE = ". GenreListResponse";
    private static final String JSON_OBJECT = ".JSONObject";

    private GenreListWebClient.GenreListJsonParserCallback
            mGenreListJsonParserCallback;
    // オブジェクトクラスの定義　
    private GenreListResponse mGenreListResponse;

    /**
     * コンストラクタ
     * <p>
     * //     * @param genreListJsonParserCallback
     */
    public GenreListJsonParser(GenreListWebClient.
                                       GenreListJsonParserCallback genreListJsonParserCallback) {
        mGenreListJsonParserCallback =
                genreListJsonParserCallback;
        mGenreListResponse = new GenreListResponse();
    }

//    @Override
//    protected void onPostExecute(Object s) {
//        mGenreListJsonParserCallback.
//                onGenreListJsonParsed(mGenreListResponse);
//    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        GenreListResponse response = genreListSender(result);
        return response;
    }


    /**
     * ジャンル一覧Jsonデータを解析する
     *
     * @param jsonStr ジジャンル一覧Jsonデータ
     * @return ジャンル一覧取得：正常時レスポンスデータ
     */
    public GenreListResponse genreListSender(String jsonStr) {

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
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + JSON_OBJECT, e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + RESPONSE, e);
        }
        return null;
    }

    /**
     * UpdateDateの値をジャンルコンテンツ数取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendUpdateDate(JSONObject jsonObj) {
        try {
            // UpdateDateの値を取得しセットする
            if (!jsonObj.isNull(GENRE_LIST_RESPONSE_UPDATE_DATE)) {
                String UpdateDate = jsonObj.getString(GENRE_LIST_RESPONSE_UPDATE_DATE);
                if (mGenreListResponse != null) {
                    mGenreListResponse.setUpdateDate(UpdateDate);
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + SEND_UPDATE_DATE, e);
        }
    }

    /**
     * ジャンル一覧のデータをジャンル一覧取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendGenreListResponse(JSONObject jsonObj) {
        try {
            ArrayList<GenreListMetaData> genreListMetaDataList;
            Iterator<String> iterator_key = jsonObj.keys();
            while (iterator_key.hasNext()) {
                String item = iterator_key.next();
                if (item.equals(GENRE_LIST_RESPONSE_UPDATE_DATE)) {
                    continue;
                }
                JSONArray lists = jsonObj.getJSONArray(item);
                if (lists.length() == 0) {
                    return;
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
                    // ジャンル毎コンテンツをセットする
                    if (item.equals(GENRE_LIST_RESPONSE_PLALA_LIST)) {
                        mGenreListResponse.setmPLALA(genreListMetaDataList);
                    } else if (item.equals(GENRE_LIST_RESPONSE_NOD_LIST)) {
                        mGenreListResponse.setmNOD(genreListMetaDataList);
                    } else if (item.equals(GENRE_LIST_RESPONSE_ARIB_LIST)) {
                        mGenreListResponse.setmARIB(genreListMetaDataList);
                    }
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
