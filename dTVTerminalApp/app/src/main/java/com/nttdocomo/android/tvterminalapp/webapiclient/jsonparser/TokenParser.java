/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ServiceTokenData;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ServiceTokenClient;

import org.json.JSONObject;

import java.util.List;

/**
 * サービストークンの解析
 */
public class TokenParser extends AsyncTask<Object, Object, Object> {
    //戻り先のコールバック
    private ServiceTokenClient.TokenJsonParserCallback mTokenParserCallback;

    // オブジェクトクラスの定義
    private ServiceTokenData mServiceTokenData;

    /**
     * サービストークンのデータを解析する.
     *
     * @param dataStr 元のJSONデータ
     * @return リスト化データ
     */
    public List<ServiceTokenData> TokenDataSender(String dataStr) {
        DTVTLogger.start(dataStr);
        mServiceTokenData = new ServiceTokenData();


        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに渡す.
     *
     * @param jsonObj 元のJSONデータ
     */
    public void sendStatus(JSONObject jsonObj) {
        DTVTLogger.start();

        //TODO:正常データの受信にまだ成功しておらず、データ形式不詳の為コメント化

//        try {
//            // statusの値を取得し、Mapに格納
//            HashMap<String, String> map = new HashMap<>();
//            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
//                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
//                map.put(JsonConstants.META_RESPONSE_STATUS, status);
//            }
//
//            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_PAGER)) {
//                JSONObject pager = jsonObj.getJSONObject(JsonConstants.META_RESPONSE_PAGER);
//
////                for (int i = 0; i < PAGER_PARAMETERS.length; i++) {
////                    if (!pager.isNull(PAGER_PARAMETERS[i])) {
////                        String para = pager.getString(PAGER_PARAMETERS[i]);
////                        map.put(PAGER_PARAMETERS[i], para);
////                    }
////                }
//            }
//
//            mServiceTokenData.setResponseInfoMap(map);
//
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     * コンストラクタ.
     *
     * @param mTokenParserCallback コールバック
     */
    public TokenParser(ServiceTokenClient.TokenJsonParserCallback mTokenParserCallback) {
        this.mTokenParserCallback = mTokenParserCallback;
    }

    @Override
    protected void onPostExecute(Object object) {
        mTokenParserCallback.onTokenJsonParsed((List<ServiceTokenData>) object);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        List<ServiceTokenData> resultList = TokenDataSender(result);
        return resultList;
    }


    //TODO:正常データの受信にまだ成功しておらず、データ形式不詳の為コメント化
//    /**
//     * コンテンツのList<HashMap>をオブジェクトクラスに格納
//     *
//     * @param jsonObj 元のJSONデータ
//     */
//    public void sendVcList(JSONObject jsonObj) {
//        try {
//            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
//                // コンテンツリストのList<HashMap>を用意
//                List<HashMap<String, String>> tokenData = new ArrayList<>();
//
//                // コンテンツリストをJSONArrayにパースする
//                JSONArray jsonArr = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
//
//                // リストの数だけまわす
//                for (int i = 0; i < jsonArr.length(); i++) {
//                    // 最初にHashMapを生成＆初期化
//                    HashMap<String, String> vcListMap = new HashMap<>();
//
//                    // i番目のJSONArrayをJSONObjectに変換する
//                    JSONObject jsonObject = jsonArr.getJSONObject(i);
//
//                    for (int j = 0; j < CONTENT_META_PARAMETERS.length; j++) {
//                        if (!jsonObject.isNull(CONTENT_META_PARAMETERS[j])) {
//                            if (CONTENT_META_PARAMETERS[j].equals(JsonConstants.META_RESPONSE_GENRE_ARRAY)) {
//                                JSONArray para = jsonObject.getJSONArray(CONTENT_META_PARAMETERS[j]);
//                                vcListMap.put(CONTENT_META_PARAMETERS[j], para.toString());
//                            } else if (CONTENT_META_PARAMETERS[j].equals(JsonConstants.META_RESPONSE_CHPACK)) {
//                                JSONObject para = jsonObject.getJSONObject(CONTENT_META_PARAMETERS[j]);
//                                for (int c = 0; c < CHANNEL_META_PARAMETERS.length; c++) {
//                                    if (!jsonObject.isNull(CHANNEL_META_PARAMETERS[c])) {
//                                        String value = para.getString(CHANNEL_META_PARAMETERS[c]);
//                                        //書き込み用項目名の作成
//                                        StringBuilder stringBuffer = new StringBuilder();
//                                        stringBuffer.append(CONTENT_META_PARAMETERS[j]);
//                                        stringBuffer.append(JsonConstants.UNDER_LINE);
//                                        stringBuffer.append(CHANNEL_META_PARAMETERS[c]);
//
//                                        //日付項目チェック
//                                        if (DBUtils.isDateItem(CHANNEL_META_PARAMETERS[c])) {
//                                            //日付なので変換して格納する
//                                            String dateBuffer = DateUtils.formatEpochToString(
//                                                    StringUtils.changeString2Long(value));
//                                            vcListMap.put(stringBuffer.toString(), dateBuffer);
//                                        } else {
//                                            //日付ではないのでそのまま格納する
//                                            vcListMap.put(stringBuffer.toString(), value);
//                                        }
//                                    }
//                                }
//                            } else if (DBUtils.isDateItem(CONTENT_META_PARAMETERS[j])) {
//                                // DATE_PARAに含まれるのは日付なので、エポック秒となる。変換して格納する
//                                String dateBuffer = DateUtils.formatEpochToString(
//                                        StringUtils.changeString2Long(
//                                                jsonObject.getString(
//                                                        CONTENT_META_PARAMETERS[j])));
//                                vcListMap.put(CONTENT_META_PARAMETERS[j], dateBuffer);
//                            } else {
//                                String para = jsonObject.getString(CONTENT_META_PARAMETERS[j]);
//                                vcListMap.put(CONTENT_META_PARAMETERS[j], para);
//                            }
//                        }
//                    }
//                    // i番目のMapをListにadd
//                    tokenData.add(vcListMap);
//                }
//                // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
//                mServiceTokenData.setToken(tokenData);
//            }
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
