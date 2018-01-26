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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChannelJsonParser extends AsyncTask<Object, Object, Object> {

    private ChannelWebClient.ChannelJsonParserCallback mChannelJsonParserCallback;

    // オブジェクトクラスの定義
    private ChannelList mChannelList;

    public static final String[] PAGER_PARAMETERS = {JsonConstants.META_RESPONSE_PAGER_LIMIT,
            JsonConstants.META_RESPONSE_OFFSET, JsonConstants.META_RESPONSE_COUNT,
            JsonConstants.META_RESPONSE_TOTAL};

    public static final String[] CONTENT_META_PARAMETERS = {JsonConstants.META_RESPONSE_CRID,
            JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_CHNO,
            JsonConstants.META_RESPONSE_TITLE, JsonConstants.META_RESPONSE_TITLERUBY,
            JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_SERVICE,
            JsonConstants.META_RESPONSE_CH_TYPE, JsonConstants.META_RESPONSE_AVAIL_START_DATE,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE, JsonConstants.META_RESPONSE_DEFAULT_THUMB,
            JsonConstants.META_RESPONSE_THUMB_640, JsonConstants.META_RESPONSE_THUMB_448,
            JsonConstants.META_RESPONSE_DEMONG, JsonConstants.META_RESPONSE_4KFLG,
            JsonConstants.META_RESPONSE_AVAIL_STATUS, JsonConstants.META_RESPONSE_DELIVERY,
            JsonConstants.META_RESPONSE_R_VALUE, JsonConstants.META_RESPONSE_ADULT,
            JsonConstants.META_RESPONSE_NG_FUNC, JsonConstants.META_RESPONSE_GENRE_ARRAY,
            JsonConstants.META_RESPONSE_SYNOP, JsonConstants.META_RESPONSE_CHSVOD,
            JsonConstants.META_RESPONSE_PUID, JsonConstants.META_RESPONSE_SUB_PUID,
            JsonConstants.META_RESPONSE_PRICE, JsonConstants.META_RESPONSE_QRANGE,
            JsonConstants.META_RESPONSE_QUNIT, JsonConstants.META_RESPONSE_PU_START_DATE,
            JsonConstants.META_RESPONSE_PU_END_DATE, JsonConstants.META_RESPONSE_CHPACK};
    public static final String[] CHANNEL_META_PARAMETERS = {JsonConstants.META_RESPONSE_CRID,
            JsonConstants.META_RESPONSE_TITLE, JsonConstants.META_RESPONSE_DISP_TYPE,
            JsonConstants.META_RESPONSE_PUID, JsonConstants.META_RESPONSE_SUB_PUID,
            JsonConstants.META_RESPONSE_PRICE, JsonConstants.META_RESPONSE_QRANGE,
            JsonConstants.META_RESPONSE_QUNIT, JsonConstants.META_RESPONSE_PU_START_DATE,
            JsonConstants.META_RESPONSE_PU_END_DATE};

    /**
     * CH一覧Jsonデータを解析する
     *
     * @param jsonStr 元のJSONデータ
     * @return リスト化データ
     */
    public List<ChannelList> CHANNELListSender(String jsonStr) {

        mChannelList = new ChannelList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<ChannelList> clList = Arrays.asList(mChannelList);

                return clList;
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに渡す
     *
     * @param jsonObj 元のJSONデータ
     */
    public void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                map.put(JsonConstants.META_RESPONSE_STATUS, status);
            }

            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonConstants.META_RESPONSE_PAGER);

                for (int i = 0; i < PAGER_PARAMETERS.length; i++) {
                    if (!pager.isNull(PAGER_PARAMETERS[i])) {
                        String para = pager.getString(PAGER_PARAMETERS[i]);
                        map.put(PAGER_PARAMETERS[i], para);
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
     * コンストラクタ
     *
     * @param mChannelJsonParserCallback コールバック
     */
    public ChannelJsonParser(ChannelWebClient.ChannelJsonParserCallback mChannelJsonParserCallback) {
        this.mChannelJsonParserCallback = mChannelJsonParserCallback;
    }

    @Override
    protected void onPostExecute(Object object) {
        mChannelJsonParserCallback.onChannelJsonParsed((List<ChannelList>) object);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        List<ChannelList> resultList = CHANNELListSender(result);
        return resultList;
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納
     *
     * @param jsonObj 元のJSONデータ
     */
    public void sendVcList(JSONObject jsonObj) {
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

                    for (int j = 0; j < CONTENT_META_PARAMETERS.length; j++) {
                        if (!jsonObject.isNull(CONTENT_META_PARAMETERS[j])) {
                            if (CONTENT_META_PARAMETERS[j].equals(JsonConstants.META_RESPONSE_GENRE_ARRAY)) {
                                JSONArray para = jsonObject.getJSONArray(CONTENT_META_PARAMETERS[j]);
                                vcListMap.put(CONTENT_META_PARAMETERS[j], para.toString());
                            } else if (CONTENT_META_PARAMETERS[j].equals(JsonConstants.META_RESPONSE_CHPACK)) {
                                JSONObject para = jsonObject.getJSONObject(CONTENT_META_PARAMETERS[j]);
                                for (int c = 0; c < CHANNEL_META_PARAMETERS.length; c++) {
                                    if (!jsonObject.isNull(CHANNEL_META_PARAMETERS[c])) {
                                        String value = para.getString(CHANNEL_META_PARAMETERS[c]);
                                        //書き込み用項目名の作成
                                        StringBuilder stringBuffer = new StringBuilder();
                                        stringBuffer.append(CONTENT_META_PARAMETERS[j]);
                                        stringBuffer.append(JsonConstants.UNDER_LINE);
                                        stringBuffer.append(CHANNEL_META_PARAMETERS[c]);

                                        //日付項目チェック
                                        if (DBUtils.isDateItem(CHANNEL_META_PARAMETERS[c])) {
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
                            } else if (DBUtils.isDateItem(CONTENT_META_PARAMETERS[j])) {
                                // DATE_PARAに含まれるのは日付なので、エポック秒となる。変換して格納する
                                String dateBuffer = DateUtils.formatEpochToString(
                                        StringUtils.changeString2Long(jsonObject.getString(CONTENT_META_PARAMETERS[j])));
                                vcListMap.put(CONTENT_META_PARAMETERS[j], dateBuffer);
                            } else {
                                String para = jsonObject.getString(CONTENT_META_PARAMETERS[j]);
                                vcListMap.put(CONTENT_META_PARAMETERS[j], para);
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
