/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChListResponse;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
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
    private PurchasedChListResponse mPurchasedChListResponse = null;

    /**
     * コンストラクタ.
     *
     * @param rentalChListJsonParserCallback コールバックの飛び先
     */
    public RentalChListJsonParser(final RentalChListWebClient.RentalChListJsonParserCallback rentalChListJsonParserCallback) {
        mRentalChListJsonParserCallback = rentalChListJsonParserCallback;
        mPurchasedChListResponse = new PurchasedChListResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        mRentalChListJsonParserCallback.onRentalChListJsonParsed(mPurchasedChListResponse);
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
    private PurchasedChListResponse channelListSender(final String jsonStr) {

        mPurchasedChListResponse = new PurchasedChListResponse();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            sendStatus(jsonObj);
            sendVcList(jsonObj);
            sendActiveListResponse(jsonObj);

            return mPurchasedChListResponse;
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
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(
                        JsonContents.META_RESPONSE_STATUS);
                mPurchasedChListResponse.setStatus(status);
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
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_METADATE_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();
                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(JsonContents.META_RESPONSE_LIST);
                if (jsonArr.length() == 0) {
                    return;
                }
                // リストの数だけまわす
                for (int i = 0; i < jsonArr.length(); i++) {
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    for (int j = 0; j < JsonContents.METADATA_LIST_PARA.length; j++) {
                        if (!jsonObject.isNull(JsonContents.METADATA_LIST_PARA[j])) {
                            if (JsonContents.METADATA_LIST_PARA[j].equals(JsonContents.META_RESPONSE_GENRE_ARRAY)) {
                                JSONArray para = jsonObject.getJSONArray(JsonContents.METADATA_LIST_PARA[j]);
                                vcListMap.put(JsonContents.METADATA_LIST_PARA[j], para.toString());
                            } else if (JsonContents.METADATA_LIST_PARA[j].equals(JsonContents.META_RESPONSE_CHPACK)) {
                                JSONObject para = jsonObject.getJSONObject(JsonContents.METADATA_LIST_PARA[j]);
                                for (int c = 0; c < JsonContents.CHPACK_PARA.length; c++) {
                                    if (!jsonObject.isNull(JsonContents.CHPACK_PARA[c])) {
                                        String value = para.getString(JsonContents.CHPACK_PARA[c]);
                                        //書き込み用項目名の作成
                                        StringBuilder stringBuffer = new StringBuilder();
                                        stringBuffer.append(JsonContents.METADATA_LIST_PARA[j]);
                                        stringBuffer.append(JsonContents.UNDER_LINE);
                                        stringBuffer.append(JsonContents.CHPACK_PARA[c]);

                                        //日付項目チェック
                                        if (DBUtils.isDateItem(JsonContents.CHPACK_PARA[c])) {
                                            //日付なので変換して格納する
                                            String dateBuffer = DateUtils.formatEpochToString(
                                                    StringUtil.changeString2Long(value));
                                            vcListMap.put(stringBuffer.toString(), dateBuffer);
                                        } else {
                                            //日付ではないのでそのまま格納する
                                            vcListMap.put(stringBuffer.toString(), value);
                                        }
                                    }
                                }
                            } else if (DBUtils.isDateItem(JsonContents.METADATA_LIST_PARA[j])) {
                                // DATE_PARAに含まれるのは日付なので、エポック秒となる。変換して格納する
                                String dateBuffer = DateUtils.formatEpochToString(
                                        StringUtil.changeString2Long(jsonObject.getString(JsonContents.METADATA_LIST_PARA[j])));
                                vcListMap.put(JsonContents.METADATA_LIST_PARA[j], dateBuffer);
                            } else {
                                String para = jsonObject.getString(JsonContents.METADATA_LIST_PARA[j]);
                                vcListMap.put(JsonContents.METADATA_LIST_PARA[j], para);
                            }
                        }
                    }
                    // i番目のMapをListにadd
                    vcList.add(vcListMap);
                }
                // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
                mChannelList.setClList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //購入済みチャンネルをセットする
        mPurchasedChListResponse.setChannelListData(mChannelList);
    }

    /**
     * 有効期限一覧の取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendActiveListResponse(final JSONObject jsonObj) {
        try {
            ArrayList<ActiveData> vodActiveDataList = new ArrayList<>();
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
                    if (!listData.isNull(JsonContents.META_RESPONSE_LICENSE_ID)) {
                    activeData.setLicenseId(listData.getString(
                            JsonContents.META_RESPONSE_LICENSE_ID));
                    }
                    if (!listData.isNull(JsonContents.META_RESPONSE_VAILD_END_DATE)) {
                        activeData.setValidEndDate(StringUtil.changeString2Long(listData.getLong(
                                JsonContents.META_RESPONSE_VAILD_END_DATE)));
                    }

                    vodActiveDataList.add(activeData);
                }

                // 有効期限一覧リストをセットする
                mPurchasedChListResponse.setChActiveData(vodActiveDataList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}