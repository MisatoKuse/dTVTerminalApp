/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
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
     * CH一覧Jsonデータを解析する.
     *
     * @param jsonStr 元のJSONデータ
     * @return リスト化データ
     */
    private List<ChannelList> CHANNELListSender(final String jsonStr) {

        mChannelList = new ChannelList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            sendStatus(jsonObj);
            sendVcList(jsonObj);
            return Collections.singletonList(mChannelList);
        } catch (JSONException e) {
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

                for (String PAGER_PARAMETER : PAGER_PARAMETERS) {
                    if (!pager.isNull(PAGER_PARAMETER)) {
                        String para = pager.getString(PAGER_PARAMETER);
                        map.put(PAGER_PARAMETER, para);
                    }
                }
            }

            mChannelList.setResponseInfoMap(map);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
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
        mChannelJsonParserCallback.onChannelJsonParsed((List<ChannelList>) object);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        return CHANNELListSender(result);
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
                                            if (DBUtils.isDateItem(JsonConstants.CHPACK_PARA[c])) {
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
                            } else if (DBUtils.isDateItem(JsonConstants.METADATA_LIST_PARA[j])) {
                                // DATE_PARAに含まれるのは日付なので、エポック秒となる。変換して格納する
                                String dateBuffer = DateUtils.formatEpochToString(
                                        StringUtils.changeString2Long(jsonObject.getString(
                                                JsonConstants.METADATA_LIST_PARA[j])));
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
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}
