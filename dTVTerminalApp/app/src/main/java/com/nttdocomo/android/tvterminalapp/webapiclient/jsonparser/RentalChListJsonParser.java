/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChannelListResponse;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalChListWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * レンタル一覧（Jsonパーサー）.
 */
public class RentalChListJsonParser extends AsyncTask<Object, Object, Object> {

    /**
     * コールバック.
     */
    private RentalChListWebClient.RentalChListJsonParserCallback
            mRentalChListJsonParserCallback = null;

    /**
     * オブジェクトクラスの定義.
     */
    private PurchasedChannelListResponse mPurchasedChannelListResponse = null;

    /**
     * コンストラクタ.
     *
     * @param rentalChListJsonParserCallback コールバックの飛び先
     */
    public RentalChListJsonParser(final RentalChListWebClient.RentalChListJsonParserCallback rentalChListJsonParserCallback) {
        mRentalChListJsonParserCallback = rentalChListJsonParserCallback;
        mPurchasedChannelListResponse = new PurchasedChannelListResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        mRentalChListJsonParserCallback.onRentalChListJsonParsed(mPurchasedChannelListResponse);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        return channelListSender(result);
    }

    /**
     * JSONを組み立ててコールバックに渡す.
     *
     * @param jsonStr JSON
     * @return コールバック
     */
    private PurchasedChannelListResponse channelListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mPurchasedChannelListResponse = new PurchasedChannelListResponse();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            sendStatus(jsonObj);
            sendVcList(jsonObj);
            sendActiveListResponse(jsonObj);

            return mPurchasedChannelListResponse;
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }

        return null;
    }

    /**
     * statusの値を取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得しセットする
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(
                        JsonConstants.META_RESPONSE_STATUS);
                mPurchasedChannelListResponse.setStatus(status);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納.
     *
     * @param jsonObj 元のJSONデータ
     */
    private void sendVcList(final JSONObject jsonObj) {
        ChannelList mChannelList = new ChannelList();
        try {
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_METADATE_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();
                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_METADATE_LIST);
                if (jsonArr.length() == 0) {
                    return;
                }
                // リストの数だけまわす
                for (int i = 0; i < jsonArr.length(); i++) {
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    for (int j = 0; j < JsonConstants.METADATA_LIST_PARA.length; j++) {
                        if (!jsonObject.isNull(JsonConstants.METADATA_LIST_PARA[j])) {
                            if (JsonConstants.METADATA_LIST_PARA[j].equals(JsonConstants.META_RESPONSE_GENRE_ARRAY)) {
                                JSONArray para = jsonObject.getJSONArray(JsonConstants.METADATA_LIST_PARA[j]);
                                vcListMap.put(JsonConstants.METADATA_LIST_PARA[j], para.toString());
                            } else if (JsonConstants.METADATA_LIST_PARA[j].equals(JsonConstants.META_RESPONSE_CHPACK)) {
                                JSONArray jsonArrayCHPACK = jsonObject.getJSONArray(JsonConstants.METADATA_LIST_PARA[j]);
                                for (int k = 0; k < jsonArrayCHPACK.length(); k++) {
                                    JSONObject jsonObjectChPack = jsonArrayCHPACK.getJSONObject(k);
                                    for (int c = 0; c < JsonConstants.CHPACK_PARA.length; c++) {
                                        if (!jsonObjectChPack.isNull(JsonConstants.CHPACK_PARA[c])) {
                                            String value = jsonObjectChPack.getString(JsonConstants.CHPACK_PARA[c]);
                                            //書き込み用項目名の作成
                                            StringBuilder stringBuffer = new StringBuilder();
                                            stringBuffer.append(JsonConstants.METADATA_LIST_PARA[j]);
                                            stringBuffer.append(JsonConstants.UNDER_LINE);
                                            stringBuffer.append(JsonConstants.CHPACK_PARA[c]);

                                            //日付項目チェック
                                            if (DataBaseUtils.isDateItem(JsonConstants.CHPACK_PARA[c])) {
                                                //日付なので変換して格納する
                                                String dateBuffer = DateUtils.formatEpochToString(
                                                        StringUtils.changeString2Long(value));
                                                vcListMap.put(stringBuffer.toString(), dateBuffer);
                                            } else {
                                                //日付ではないのでそのまま格納する
                                                vcListMap.put(stringBuffer.toString(), value);
                                            }
                                        }
                                    }
                                }
                            } else if (DataBaseUtils.isDateItem(JsonConstants.METADATA_LIST_PARA[j])) {
                                // DATE_PARAに含まれるのは日付なので、エポック秒となる。変換して格納する
                                String dateBuffer = DateUtils.formatEpochToString(
                                        StringUtils.changeString2Long(jsonObject.getString(JsonConstants.METADATA_LIST_PARA[j])));
                                vcListMap.put(JsonConstants.METADATA_LIST_PARA[j], dateBuffer);
                            } else {
                                String para = jsonObject.getString(JsonConstants.METADATA_LIST_PARA[j]);
                                vcListMap.put(JsonConstants.METADATA_LIST_PARA[j], para);
                            }
                        }
                    }
                    // i番目のMapをListにadd
                    vcList.add(vcListMap);
                }
                // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
                mChannelList.setChannelList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //購入済みチャンネルをセットする
        mPurchasedChannelListResponse.setChannelListData(mChannelList);
    }

    /**
     * 有効期限一覧の取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendActiveListResponse(final JSONObject jsonObj) {
        try {
            ArrayList<ActiveData> vodActiveDataList = new ArrayList<>();
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
                    String strDate = listData.getString(JsonConstants.META_RESPONSE_VAILD_END_DATE);
                    if (!listData.isNull(JsonConstants.META_RESPONSE_VAILD_END_DATE) && DataBaseUtils.isNumber(strDate)) {
                        activeData.setValidEndDate(StringUtils.changeString2Long(listData.getLong(
                                JsonConstants.META_RESPONSE_VAILD_END_DATE)));
                    }

                    vodActiveDataList.add(activeData);
                }

                // 有効期限一覧リストをセットする
                mPurchasedChannelListResponse.setChActiveData(vodActiveDataList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}