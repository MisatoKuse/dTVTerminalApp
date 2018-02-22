/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 番組表情報JsonParser.
 */
public class TvScheduleJsonParser extends AsyncTask<Object, Object, Object> {
    /**
     * パース結果を伝えるコールバック.
     */
    private final TvScheduleWebClient.TvScheduleJsonParserCallback mTvScheduleJsonParserCallback;
    /**
     * オブジェクトクラスの定義.
     */
    private TvScheduleList mTvScheduleList;

    /**
     * コンストラクタ.
     *
     * @param mTvScheduleJsonParserCallback コールバック
     */
    public TvScheduleJsonParser(final TvScheduleWebClient.TvScheduleJsonParserCallback mTvScheduleJsonParserCallback) {
        this.mTvScheduleJsonParserCallback = mTvScheduleJsonParserCallback;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(final Object s) {
        mTvScheduleJsonParserCallback.onTvScheduleJsonParsed((List<TvScheduleList>) s);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        return tvScheduleListListSender(result);
    }

    /**
     * CH毎番組Jsonデータを解析する.
     *
     * @param jsonStr 元のJSONデータ
     * @return パース後のJSONデータ
     */
    private List<TvScheduleList> tvScheduleListListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mTvScheduleList = new TvScheduleList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            sendStatus(jsonObj);

            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                sendTsList(arrayList);
            }
            return Collections.singletonList(mTvScheduleList);
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statsの値をMapでオブジェクトクラスに渡す.
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
            if (mTvScheduleList != null) {
                mTvScheduleList.setTvsMap(map);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納.
     *
     * @param arrayList 元のJSONデータ
     */
    private void sendTsList(final JSONArray arrayList) {
        try {
            List<Map<String, String>> tsList = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> tsListMap = new HashMap<>();
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : JsonConstants.LIST_PARA) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonConstants.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(listBuffer);
                            for (String puinfBuffer : JsonConstants.PUINF_PARA) {
                                //書き込み用項目名の作成
                                StringBuilder stringBuffer = new StringBuilder();
                                stringBuffer.append(JsonConstants.META_RESPONSE_PUINF);
                                stringBuffer.append(JsonConstants.UNDER_LINE);
                                stringBuffer.append(puinfBuffer);

                                //日付項目チェック
                                if (DBUtils.isDateItem(puinfBuffer)) {
                                    //日付なので変換して格納する
                                    String dateBuffer = DateUtils.formatEpochToString(
                                            StringUtils.changeString2Long(puinfObj.getString(
                                                    puinfBuffer)));
                                    tsListMap.put(stringBuffer.toString(), dateBuffer);
                                } else {
                                    //日付ではないのでそのまま格納する
                                    String para = puinfObj.getString(puinfBuffer);
                                    tsListMap.put(stringBuffer.toString(), para);
                                }
                            }
                        } else if (DBUtils.isDateItem(listBuffer)) {
                            // DATE_PARAに含まれるのは日付なので、エポック秒となる。変換して格納する
                            String dateBuffer = DateUtils.formatEpochToString(
                                    StringUtils.changeString2Long(jsonObject.getString(listBuffer)));
                            tsListMap.put(listBuffer, dateBuffer);
                        } else {
                            String para = jsonObject.getString(listBuffer);
                            tsListMap.put(listBuffer, para);
                        }
                    }
                }
                tsList.add(tsListMap);
            }
            if (mTvScheduleList != null) {
                mTvScheduleList.setTvsList(tsList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}
