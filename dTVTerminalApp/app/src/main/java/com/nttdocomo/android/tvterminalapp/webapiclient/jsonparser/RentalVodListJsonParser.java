/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
// 購入済みVOD一覧取得：正常時レスポンスデータの定数
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse.VOD_META_FULL_RESPONSE_LIST;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse.VOD_META_FULL_RESPONSE_STATUS;

/**
 * レンタル一覧（Jsonパーサー）
 */

public class RentalVodListJsonParser extends AsyncTask<Object, Object, Object> {
    private RentalVodListWebClient.RentalVodListJsonParserCallback mRentalVodListJsonParserCallback;
    // オブジェクトクラスの定義
    private PurchasedVodListResponse mPurchasedVodListResponse;

    /**
     * コンストラクタ
     *
     * @param rentalVodListJsonParserCallback
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
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendPurchasedVodListResponse(jsonObj);

                return mPurchasedVodListResponse;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            if (!jsonObj.isNull(VOD_META_FULL_RESPONSE_STATUS)) {
                String status = jsonObj.getString(VOD_META_FULL_RESPONSE_STATUS);
                mPurchasedVodListResponse.setStatus(status);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            if (!jsonObj.isNull(VOD_META_FULL_RESPONSE_LIST)) {
                // 購入済みVOD一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(VOD_META_FULL_RESPONSE_LIST);
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
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
