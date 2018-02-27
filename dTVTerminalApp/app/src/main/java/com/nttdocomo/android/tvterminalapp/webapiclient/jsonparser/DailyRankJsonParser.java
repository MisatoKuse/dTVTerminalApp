/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DailyRankJsonParser extends AsyncTask<Object, Object, Object> {

    final private DailyRankWebClient.DailyRankJsonParserCallback mDailyRankJsonParserCallback;

    // オブジェクトクラスの定義
    private DailyRankList mDailyRankList = null;

    public static final String[] PAGER_PARA = {JsonConstants.META_RESPONSE_PAGER_LIMIT,
            JsonConstants.META_RESPONSE_OFFSET, JsonConstants.META_RESPONSE_COUNT,
            JsonConstants.META_RESPONSE_TOTAL};

    /**
     * コンストラクタ
     *
     * @param mDailyRankJsonParserCallback コールバック
     */
    public DailyRankJsonParser(final DailyRankWebClient.DailyRankJsonParserCallback mDailyRankJsonParserCallback) {
        this.mDailyRankJsonParserCallback = mDailyRankJsonParserCallback;
    }

    @Override
    protected void onPostExecute(final Object dailyLankListData) {
        mDailyRankJsonParserCallback.onDailyRankJsonParsed((List<DailyRankList>) dailyLankListData);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        List<DailyRankList> resultList = dailyRankListSender(result);
        return resultList;
    }

    /**
     * デイリーランキングJsonデータを解析する.
     *
     * @param jsonStr JSONデータ文字列
     * @return パース後のJSONデータ
     */
    private List<DailyRankList> dailyRankListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mDailyRankList = new DailyRankList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            sendStatus(jsonObj);
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                sendDrList(arrayList);
            }
            List<DailyRankList> drList = Arrays.asList(mDailyRankList);
            return drList;
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに渡す.
     *
     * @param jsonObj 解析前ステータス
     */
    private void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                map.put(JsonConstants.META_RESPONSE_STATUS, status);
            }

            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonConstants.META_RESPONSE_PAGER);

                for (String pagerBuffer : PAGER_PARA) {
                    if (!pager.isNull(pagerBuffer)) {
                        String para = pager.getString(pagerBuffer);
                        map.put(pagerBuffer, para);
                    }
                }
            }

            if (mDailyRankList != null) {
                mDailyRankList.setDrMap(map);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納.
     *
     * @param arrayList JSONArray
     */
    private void sendDrList(final JSONArray arrayList) {
        try {
            List<HashMap<String, String>> drList = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> drListMap = new HashMap<>();
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : JsonConstants.LIST_PARA) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonConstants.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(listBuffer);
                            for (String puinfBuffer : JsonConstants.PUINF_PARA) {
                                String para = puinfObj.getString(puinfBuffer);
                                drListMap.put(JsonConstants.META_RESPONSE_PUINF + JsonConstants.UNDER_LINE + puinfBuffer, para);
                            }
                        } else {
                            String para = jsonObject.getString(listBuffer);
                            drListMap.put(listBuffer, para);
                        }
                    }
                }
                drList.add(drListMap);
            }

            if (mDailyRankList != null) {
                mDailyRankList.setDrList(drList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}