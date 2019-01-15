/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * チャンネル情報JsonのParser.
 */
public class ChannelJsonParser extends AsyncTask<Object, Object, Object> {

    /**
     * Parse結果を返すコールバック.
     */
    private final ChannelWebClient.ChannelJsonParserCallback mChannelJsonParserCallback;
    /**
     * オブジェクトクラスの定義.
     */
    private ChannelList mChannelList;
    /**
     * pagerのparse用パラメータ.
     */
    private static final String[] PAGER_PARAMETERS = {JsonConstants.META_RESPONSE_PAGER_LIMIT,
            JsonConstants.META_RESPONSE_OFFSET, JsonConstants.META_RESPONSE_COUNT,
            JsonConstants.META_RESPONSE_TOTAL};
    /**
     * JSONパースエラー
     */
    private ErrorState mJsonParseError = null;

    /**
     * CH一覧Jsonデータを解析する.
     *
     * @param jsonStr 元のJSONデータ
     * @return リスト化データ
     */
    private List<ChannelList> channelListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mChannelList = new ChannelList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            sendStatus(jsonObj);
            sendVcList(jsonObj);
            return Collections.singletonList(mChannelList);
        } catch (JSONException e) {
            mJsonParseError = new ErrorState();
            mJsonParseError.setErrorType(DtvtConstants.ErrorType.PARSE_ERROR);
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに渡す.
     *
     * @param jsonObj 元のJSONデータ
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                map.put(JsonConstants.META_RESPONSE_STATUS, status);
            }

            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonConstants.META_RESPONSE_PAGER);

                for (String pagerParameter : PAGER_PARAMETERS) {
                    if (!pager.isNull(pagerParameter)) {
                        String para = pager.getString(pagerParameter);
                        map.put(pagerParameter, para);
                    }
                }
            }

            mChannelList.setResponseInfoMap(map);
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンストラクタ.
     *
     * @param mChannelJsonParserCallback コールバック
     */
    public ChannelJsonParser(final ChannelWebClient.ChannelJsonParserCallback mChannelJsonParserCallback) {
        this.mChannelJsonParserCallback = mChannelJsonParserCallback;
    }

    @Override
    protected void onPostExecute(final Object object) {
        mChannelJsonParserCallback.onChannelJsonParsed((List<ChannelList>) object, mJsonParseError);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        return channelListSender(result);
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納.
     *
     * @param jsonObj 元のJSONデータ
     */
    private void sendVcList(final JSONObject jsonObj) {
        try {
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);

                // リストの数だけまわす
                for (int i = 0; i < jsonArr.length(); i++) {
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);
                    for (String strings : JsonConstants.METADATA_LIST_PARA) {
                        if (!jsonObject.isNull(strings)) {
                            if (strings.equals(JsonConstants.META_RESPONSE_GENRE_ARRAY)) {
                                JSONArray para = jsonObject.getJSONArray(strings);
                                vcListMap.put(strings, para.toString());
                            } else if (strings.equals(JsonConstants.META_RESPONSE_CHPACK)) {
                                JSONArray jsonArrayCHPACK = jsonObject.getJSONArray(strings);
                                for (int k = 0; k < jsonArrayCHPACK.length(); k++) {
                                    JSONObject jsonObjectChPack = jsonArrayCHPACK.getJSONObject(k);
                                    for (String chpackPara : JsonConstants.CHPACK_PARA) {
                                        if (!jsonObjectChPack.isNull(chpackPara)) {
                                            String value = jsonObjectChPack.getString(chpackPara);
                                            //書き込み用項目名の作成
                                            StringBuilder stringBuffer = new StringBuilder();
                                            stringBuffer.append(strings);
                                            stringBuffer.append(JsonConstants.UNDER_LINE);
                                            stringBuffer.append(chpackPara);

                                            //日付項目チェック
                                            if (DataBaseUtils.isDateItem(chpackPara)) {
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
                            } else if (DataBaseUtils.isDateItem(strings)) {
                                // DATE_PARAに含まれるのは日付なので、エポック秒となる。変換して格納する
                                String dateBuffer = DateUtils.formatEpochToString(
                                        StringUtils.changeString2Long(jsonObject.getString(
                                                strings)));
                                vcListMap.put(strings, dateBuffer);
                            } else {
                                String para = jsonObject.getString(strings);
                                vcListMap.put(strings, para);
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
            DTVTLogger.debug(e);
        }
    }
}
