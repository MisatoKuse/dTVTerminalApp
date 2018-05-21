/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ContentsDetailGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsDetailGetWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * コンテンツ詳細JsonParser.
 */
public class ContentsDetailJsonParser extends AsyncTask<Object, Object, Object> {
    /**コンテキスト.*/
    private Context mContext;
    /**コールバック.*/
    private final ContentsDetailGetWebClient.ContentsDetailJsonParserCallback
            mContentsDetailJsonParserCallback;
    /**オブジェクトクラスの定義　.*/
    private ContentsDetailGetResponse mContentsDetailGetResponse;

    /**
     * コンストラクタ.
     * <p>
     * @param context コンテキスト.
     * @param contentsDetailJsonParserCallback コールバック
     */
    public ContentsDetailJsonParser(final Context context, final ContentsDetailGetWebClient.
                                            ContentsDetailJsonParserCallback
                                            contentsDetailJsonParserCallback) {
        mContext = context;
        mContentsDetailJsonParserCallback =
                contentsDetailJsonParserCallback;
        mContentsDetailGetResponse = new ContentsDetailGetResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        mContentsDetailJsonParserCallback.onContentsDetailJsonParsed(
                (mContentsDetailGetResponse));
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        return contentsDetailSender(result);
    }

    /**
     * ジャンル毎コンテンツ数取得一覧Jsonデータを解析する.
     *
     * @param jsonStr ジャンル毎コンテンツ数取得一覧Jsonデータ
     * @return ジャンル毎コンテンツ数取得一覧取得：正常時レスポンスデータ
     */
    private ContentsDetailGetResponse contentsDetailSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mContentsDetailGetResponse = new ContentsDetailGetResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                sendStatus(jsonObj);
                sendContentsDetailGetResponse(jsonObj);
                return mContentsDetailGetResponse;
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
            if (!jsonObj.isNull(ContentsDetailGetResponse.GENRE_COUNT_GET_RESPONSE_STATUS)) {
                String status = jsonObj.getString(ContentsDetailGetResponse.
                        GENRE_COUNT_GET_RESPONSE_STATUS);
                if (mContentsDetailGetResponse != null) {
                    mContentsDetailGetResponse.setStatus(status);
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
    private void sendContentsDetailGetResponse(final JSONObject jsonObj) {
        try {
            ArrayList<VodMetaFullData> vodMetaFullDataArrayList =
                    new ArrayList<>();
            if (!jsonObj.isNull(ContentsDetailGetResponse.GENRE_COUNT_GET_RESPONSE_LIST)) {
                // VOD＆番組マージメタデータ（フル版）をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(
                        ContentsDetailGetResponse.GENRE_COUNT_GET_RESPONSE_LIST);
                if (lists.length() == 0) {
                    return;
                }

                UserState userState = UserInfoUtils.getUserState(mContext);
                //VOD＆番組マージメタデータ（フル版）のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    VodMetaFullData fullData = new VodMetaFullData();

                    //データを個別に転送する
                    fullData.setData(userState, lists.getJSONObject(i));

                    //データを追加
                    vodMetaFullDataArrayList.add(fullData);
                }

                //レスポンスとしてVOD＆番組マージメタデータ（フル版）を返す
                if (mContentsDetailGetResponse != null) {
                    mContentsDetailGetResponse.setVodMetaFullData(
                            vodMetaFullDataArrayList);
                }

            }
        } catch (JSONException e) {
           DTVTLogger.debug(e);
        }
    }
}
