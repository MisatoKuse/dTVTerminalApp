/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;

/**
 * レンタル一覧（Jsonパーサー）
 */
public class RentalVodListJsonParser extends AsyncTask<Object, Object, Object> {

    private RentalVodListWebClient.RentalVodListJsonParserCallback
            mRentalVodListJsonParserCallback = null;

    // オブジェクトクラスの定義　
    private PurchasedVodListResponse mPurchasedVodListResponse = null;

    /**
     * コンストラクタ
     *
     * @param rentalVodListJsonParserCallback コールバックの飛び先
     */
    public RentalVodListJsonParser(RentalVodListWebClient.RentalVodListJsonParserCallback rentalVodListJsonParserCallback) {
        mRentalVodListJsonParserCallback = rentalVodListJsonParserCallback;
        mPurchasedVodListResponse = new PurchasedVodListResponse();
    }

    @Override
    protected void onPostExecute(Object s) {
        mRentalVodListJsonParserCallback.onRentalVodListJsonParsed(mPurchasedVodListResponse);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        PurchasedVodListResponse response = PurchasedVodListSender(result);
        return response;
    }

    /**
     * レンタル一覧Jsonデータを解析する
     *
     * @param jsonStr レンタル一覧Jsonデータ
     * @return 購入済みVOD一覧取得：正常時レスポンスデータ
     */
    public PurchasedVodListResponse PurchasedVodListSender(String jsonStr) {

        mPurchasedVodListResponse = new PurchasedVodListResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj != null) {
                    sendStatus(jsonObj);
                    sendPurchasedVodListResponse(jsonObj);
                    sendActiveListResponse(jsonObj);

                    return mPurchasedVodListResponse;
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値を購入済みVOD一覧取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(
                        JsonContents.META_RESPONSE_STATUS);
                mPurchasedVodListResponse.setStatus(status);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * レンタル一覧コンテンツのListを購入済みVOD一覧取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendPurchasedVodListResponse(JSONObject jsonObj) {
        try {
            ArrayList<VodMetaFullData> vodMetaFullDataList = new ArrayList<VodMetaFullData>();
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_METADATE_LIST)) {
                // 購入済みVOD一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(
                        JsonContents.META_RESPONSE_METADATE_LIST);
                if (lists.length() == 0) {
                    return;
                }
                // VODメタレスポンス（フル版）のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    VodMetaFullData vodMetaFullData = new VodMetaFullData();
                    vodMetaFullData.setData(lists.getJSONObject(i));
                    vodMetaFullDataList.add(vodMetaFullData);
                }
                // 購入済みVOD一覧リストをセットする
                mPurchasedVodListResponse.setVodMetaFullData(vodMetaFullDataList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 有効期限一覧の取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendActiveListResponse(JSONObject jsonObj) {
        try {
            ArrayList<ActiveData> vodActiveDataList =
                    new ArrayList<ActiveData>();
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_ACTIVE_LIST)) {
                // 購入済みVOD一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(
                        JsonContents.META_RESPONSE_ACTIVE_LIST);
                if (lists.length() == 0) {
                    return;
                }
                // VODメタレスポンス（フル版）のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject listData = (JSONObject) lists.get(i);

                    ActiveData activeData = new ActiveData();

                    //データを取得する
                    activeData.setLicenseId(listData.getString(
                            JsonContents.META_RESPONSE_LICENSE_ID));
                    activeData.setValidEndDate(StringUtil.changeString2Long(listData.getLong(
                            JsonContents.META_RESPONSE_VAILD_END_DATE)));

                    vodActiveDataList.add(activeData);
                }

                // 有効期限一覧リストをセットする
                mPurchasedVodListResponse.setVodActiveData(vodActiveDataList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}