/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WeeklyRankWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WeeklyRankJsonParser extends AsyncTask<Object, Object, Object> {

    final private WeeklyRankWebClient.WeeklyRankJsonParserCallback mWeeklyRankJsonParserCallback;
    // オブジェクトクラスの定義
    private WeeklyRankList mWeeklyRankList;

    public static final String UNDER_LINE = "_";

    // **FindBugs** Bad practice FindBugは、"pagerPara"と"listPara"はpublicを外せと言うが、対外的なパラメータなので、対応は行わない。
    public static final String[] pagerPara = {JsonContents.META_RESPONSE_PAGER_LIMIT, JsonContents.META_RESPONSE_OFFSET,
            JsonContents.META_RESPONSE_COUNT, JsonContents.META_RESPONSE_TOTAL};

    public static final String[] listPara = {JsonContents.META_RESPONSE_CRID,
            JsonContents.META_RESPONSE_CID, JsonContents.META_RESPONSE_TITLE_ID,
            JsonContents.META_RESPONSE_EPISODE_ID, JsonContents.META_RESPONSE_TITLE,
            JsonContents.META_RESPONSE_EPITITLE, JsonContents.META_RESPONSE_TITLERUBY,
            JsonContents.META_RESPONSE_DISP_TYPE, JsonContents.META_RESPONSE_DISPLAY_START_DATE,
            JsonContents.META_RESPONSE_DISPLAY_END_DATE, JsonContents.META_RESPONSE_AVAIL_START_DATE,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, JsonContents.META_RESPONSE_PUBLISH_START_DATE,
            JsonContents.META_RESPONSE_PUBLISH_END_DATE, JsonContents.META_RESPONSE_NEWA_START_DATE,
            JsonContents.META_RESPONSE_NEWA_END_DATE, JsonContents.META_RESPONSE_THUMB_640,
            JsonContents.META_RESPONSE_THUMB_448, JsonContents.META_RESPONSE_DTV_THUMB_640,
            JsonContents.META_RESPONSE_DTV_THUMB_448, JsonContents.META_RESPONSE_COPYRIGHT,
            JsonContents.META_RESPONSE_DUR, JsonContents.META_RESPONSE_DEMONG,
            JsonContents.META_RESPONSE_BVFLG, JsonContents.META_RESPONSE_4KFLG,
            JsonContents.META_RESPONSE_HDRFLG, JsonContents.META_RESPONSE_DELIVERY,
            JsonContents.META_RESPONSE_R_VALUE, JsonContents.META_RESPONSE_ADULT,
            JsonContents.META_RESPONSE_GENRE_ARRAY, JsonContents.META_RESPONSE_SYNOP,
            JsonContents.META_RESPONSE_SYNOP_SHORT, JsonContents.META_RESPONSE_PUID,
            JsonContents.META_RESPONSE_PRICE, JsonContents.META_RESPONSE_QRANGE,
            JsonContents.META_RESPONSE_QUNIT, JsonContents.META_RESPONSE_PU_START_DATE,
            JsonContents.META_RESPONSE_PU_END_DATE, JsonContents.META_RESPONSE_CREDIT_ARRAY,
            JsonContents.META_RESPONSE_RATING, JsonContents.META_RESPONSE_DTV,
            JsonContents.META_RESPONSE_CHSVOD, JsonContents.META_RESPONSE_SEARCH_OK,
            JsonContents.META_RESPONSE_LIINF_ARRAY, JsonContents.META_RESPONSE_PUINF,
            JsonContents.META_RESPONSE_CAPL, JsonContents.META_RESPONSE_BILINGAL,
            JsonContents.META_RESPONSE_TV_CID, JsonContents.META_RESPONSE_SERVICE_ID,
            JsonContents.META_RESPONSE_EVENT_ID, JsonContents.META_RESPONSE_CHNO,
            JsonContents.META_RESPONSE_TV_SERVICE, JsonContents.META_RESPONSE_CONTENT_TYPE,
            JsonContents.META_RESPONSE_VOD_START_DATE, JsonContents.META_RESPONSE_VOD_END_DATE,
            JsonContents.META_RESPONSE_MAIN_GENRE, JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY,
            JsonContents.META_RESPONSE_COPY, JsonContents.META_RESPONSE_ADINFO_ARRAY,
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY};

    public static final String[] puinfPara = {JsonContents.META_RESPONSE_PUID,
            JsonContents.META_RESPONSE_CRID, JsonContents.META_RESPONSE_TITLE,
            JsonContents.META_RESPONSE_EPITITLE, JsonContents.META_RESPONSE_DISP_TYPE,
            JsonContents.META_RESPONSE_CHSVOD, JsonContents.META_RESPONSE_PRICE,
            JsonContents.META_RESPONSE_QUNIT, JsonContents.META_RESPONSE_QRANGE,
            JsonContents.META_RESPONSE_PU_START_DATE, JsonContents.META_RESPONSE_PU_END_DATE};

    /**
     * 拡張情報
     **/
    Bundle mExtraData = null;

    /**
     * コンストラクタ
     *
     * @param mWeeklyRankJsonParserCallback 値を返すコールバック
     */
    public WeeklyRankJsonParser(WeeklyRankWebClient.WeeklyRankJsonParserCallback mWeeklyRankJsonParserCallback) {
        this.mWeeklyRankJsonParserCallback = mWeeklyRankJsonParserCallback;
    }

    /**
     * 拡張情報付きコンストラクタ
     *
     * @param mWeeklyRankJsonParserCallback コールバック用
     * @param extraDataSrc                  拡張情報
     */
    public WeeklyRankJsonParser(WeeklyRankWebClient.WeeklyRankJsonParserCallback
                                        mWeeklyRankJsonParserCallback, Bundle extraDataSrc) {
        this.mWeeklyRankJsonParserCallback = mWeeklyRankJsonParserCallback;

        //拡張情報の追加
        mExtraData = extraDataSrc;
    }

    @Override
    protected void onPostExecute(Object object) {
        //拡張情報が存在すれば、入れ込む
        List<WeeklyRankList> rankLists = (List<WeeklyRankList>) object;
        if (mExtraData != null) {
            for (WeeklyRankList rankList : rankLists) {
                rankList.setExtraData(mExtraData);
            }
        }

        mWeeklyRankJsonParserCallback.onWeeklyRankJsonParsed(rankLists);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        List<WeeklyRankList> resultList = weeklyRankListSender(result);
        return resultList;
    }

    /**
     * 週間ランキングJsonデータを解析する
     *
     * @param jsonStr String形式のJSONデータ
     * @return List<WeeklyRankList> ObjectクラスをList形式で返却
     */
    private List<WeeklyRankList> weeklyRankListSender(String jsonStr) {

        mWeeklyRankList = new WeeklyRankList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            sendStatus(jsonObj);
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonContents.META_RESPONSE_LIST);
                senWrcList(arrayList);
            }
            List<WeeklyRankList> wrList = Arrays.asList(mWeeklyRankList);
            return wrList;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに格納
     *
     * @param jsonObj ステータスを含んだJSONオブジェクト
     */
    private void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);
                map.put(JsonContents.META_RESPONSE_STATUS, status);
            }

            if (!jsonObj.isNull(JsonContents.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonContents.META_RESPONSE_PAGER);

                for (String pagerBuffer : pagerPara) {
                    if (!pager.isNull(pagerBuffer)) {
                        String para = pager.getString(pagerBuffer);
                        map.put(pagerBuffer, para);
                    }
                }
            }

            if (mWeeklyRankList != null) {
                mWeeklyRankList.setWrMap(map);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツリストをList<HashMap>の形式でObjectクラスへ格納する
     *
     * @param arrayList JSONArray
     */
    private void senWrcList(JSONArray arrayList) {
        try {
            List<HashMap<String, String>> wrList = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> wrListMap = new HashMap<>();
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : listPara) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonContents.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(JsonContents.META_RESPONSE_PUINF);
                            for (String puinfBuffer : puinfPara) {
                                String para = puinfObj.getString(puinfBuffer);
                                wrListMap.put(JsonContents.META_RESPONSE_PUINF + UNDER_LINE + puinfBuffer, para);
                            }
                        } else {
                            String para = jsonObject.getString(listBuffer);
                            wrListMap.put(listBuffer, para);
                        }
                    }
                }
                wrList.add(wrListMap);
            }

            if (mWeeklyRankList != null) {
                mWeeklyRankList.setWrList(wrList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}