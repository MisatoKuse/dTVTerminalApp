/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * レンタル一覧（Jsonパーサー）.
 */
public class RentalVodListJsonParser extends AsyncTask<Object, Object, Object> {
    /**コンテキスト.*/
    private Context mContext = null;
    /**callback.*/
    private RentalVodListWebClient.RentalVodListJsonParserCallback
            mRentalVodListJsonParserCallback = null;

    /**オブジェクトクラスの定義.*/
    private PurchasedVodListResponse mPurchasedVodListResponse = null;
    /**JSONパースエラー*/
    private ErrorState mJsonParseError = null;

    /**
     * コンストラクタ.
     * @param context コンテキスト
     * @param rentalVodListJsonParserCallback コールバックの飛び先
     */
    public RentalVodListJsonParser(final Context context, final RentalVodListWebClient.RentalVodListJsonParserCallback rentalVodListJsonParserCallback) {
        mContext = context;
        mRentalVodListJsonParserCallback = rentalVodListJsonParserCallback;
        mPurchasedVodListResponse = new PurchasedVodListResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        mRentalVodListJsonParserCallback.onRentalVodListJsonParsed(mPurchasedVodListResponse, mJsonParseError);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        PurchasedVodListResponse response = PurchasedVodListSender(result);
        return response;
    }

    /**
     * レンタル一覧Jsonデータを解析する.
     *
     * @param jsonStr レンタル一覧Jsonデータ
     * @return 購入済みVOD一覧取得：正常時レスポンスデータ
     */
    public PurchasedVodListResponse PurchasedVodListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mPurchasedVodListResponse = new PurchasedVodListResponse();

        try {
            if (jsonStr != null) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj != null) {
                    sendStatus(jsonObj);
                    sendPurchasedVodListResponse(jsonObj);
                    sendActiveListResponse(jsonObj);
                }
            }
        } catch (JSONException e) {
            mPurchasedVodListResponse.setStatus(JsonConstants.META_RESPONSE_STATUS_NG);
            mJsonParseError = new ErrorState();
            mJsonParseError.setErrorType(DtvtConstants.ErrorType.PARSE_ERROR);
            DTVTLogger.debug(e);
        }
        return mPurchasedVodListResponse;
    }

    /**
     * statusの値を購入済みVOD一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(
                        JsonConstants.META_RESPONSE_STATUS);
                mPurchasedVodListResponse.setStatus(status);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * レンタル一覧コンテンツのListを購入済みVOD一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendPurchasedVodListResponse(final JSONObject jsonObj) {
        try {
            ArrayList<VodMetaFullData> vodMetaFullDataList = new ArrayList<VodMetaFullData>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_METADATE_LIST)) {
                // 購入済みVOD一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(
                        JsonConstants.META_RESPONSE_METADATE_LIST);
                if (lists.length() == 0) {
                    return;
                }
                UserState userState = UserInfoUtils.getUserState(mContext);
                // VODメタレスポンス（フル版）のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    VodMetaFullData vodMetaFullData = new VodMetaFullData();
                    vodMetaFullData.setData(userState, lists.getJSONObject(i));
                    vodMetaFullDataList.add(vodMetaFullData);
                }
                // 購入済みVOD一覧リストをセットする
                mPurchasedVodListResponse.setVodMetaFullData(vodMetaFullDataList);
            }
        } catch (JSONException e) {
           DTVTLogger.debug(e);
        }
    }

    /**
     * 有効期限一覧の取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendActiveListResponse(final JSONObject jsonObj) {
        try {
            ArrayList<ActiveData> vodActiveDataList =
                    new ArrayList<ActiveData>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_ACTIVE_LIST)) {
                // 購入済みVOD一覧をJSONArrayにパースする
                JSONArray lists = jsonObj.getJSONArray(
                        JsonConstants.META_RESPONSE_ACTIVE_LIST);
                if (lists.length() == 0) {
                    return;
                }
                // VODメタレスポンス（フル版）のデータオブジェクトArrayListを生成する
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject listData = (JSONObject) lists.get(i);

                    ActiveData activeData = new ActiveData();

                    //データを取得する
                    if (!listData.isNull(JsonConstants.META_RESPONSE_LICENSE_ID)) {
                    activeData.setLicenseId(listData.getString(
                            JsonConstants.META_RESPONSE_LICENSE_ID));
                    }
                    if (!listData.isNull(JsonConstants.META_RESPONSE_VAILD_END_DATE)) {
                        // active_list の valid_end_date は yyyy/MM/dd HH:mm:ss 形式で返却されるためエポック秒変換
                        activeData.setValidEndDate(DateUtils.getHyphenEpochTime(listData.getString(
                                JsonConstants.META_RESPONSE_VAILD_END_DATE)));
                    }

                    vodActiveDataList.add(activeData);
                }

                // 有効期限一覧リストをセットする
                mPurchasedVodListResponse.setVodActiveData(vodActiveDataList);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}