/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WeeklyRankWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 週間ランキングデータ用JsonParserクラス.
 */
public class WeeklyRankJsonParser extends AsyncTask<String, Object, Object> {
    /**callback.*/
    final private WeeklyRankWebClient.WeeklyRankJsonParserCallback mWeeklyRankJsonParserCallback;

    /**オブジェクトクラスの定義.*/
    private WeeklyRankList mWeeklyRankList;

    /**
     * 拡張情報.
     **/
    private Bundle mExtraData = null;

    /**
     * リクエストジャンル.
     */
    private String mGenreId = "";

    // **FindBugs** Bad practice FindBugは、"PAGER_PARAMETERS"と"CONTENT_META_PARAMETERS"はpublicを外せと言うが、対外的なパラメータなので、対応は行わない。
    /**ページャーパラメータキー.*/
    private static final String[] PAGER_PARA = {JsonConstants.META_RESPONSE_PAGER_LIMIT,
            JsonConstants.META_RESPONSE_OFFSET, JsonConstants.META_RESPONSE_COUNT,
            JsonConstants.META_RESPONSE_TOTAL};

    /**
     * コンストラクタ.
     *
     * @param mWeeklyRankJsonParserCallback 値を返すコールバック
     */
    public WeeklyRankJsonParser(final WeeklyRankWebClient.WeeklyRankJsonParserCallback mWeeklyRankJsonParserCallback) {
        this.mWeeklyRankJsonParserCallback = mWeeklyRankJsonParserCallback;
    }

    /**
     * 拡張情報付きコンストラクタ.
     *
     * @param mWeeklyRankJsonParserCallback コールバック用
     * @param extraDataSrc                  拡張情報
     * @param genreId リクエストしたジャンルID
     */
    public WeeklyRankJsonParser(final WeeklyRankWebClient.WeeklyRankJsonParserCallback
                                        mWeeklyRankJsonParserCallback, final Bundle extraDataSrc, final String genreId) {
        this.mWeeklyRankJsonParserCallback = mWeeklyRankJsonParserCallback;

        //拡張情報の追加
        mExtraData = extraDataSrc;
        mGenreId = genreId;
    }

    @Override
    protected void onPostExecute(final Object object) {
        //拡張情報が存在すれば、入れ込む
        List<WeeklyRankList> rankLists = (List<WeeklyRankList>) object;
        if (mExtraData != null) {
            for (WeeklyRankList rankList : rankLists) {
                rankList.setExtraData(mExtraData);
            }
        }
        mWeeklyRankJsonParserCallback.onWeeklyRankJsonParsed(rankLists, mGenreId);
    }

    @Override
    protected Object doInBackground(final String... strings) {
        String result = strings[0];
        List<WeeklyRankList> resultList = weeklyRankListSender(result);
        return resultList;
    }

    /**
     * 週間ランキングJsonデータを解析する.
     *
     * @param jsonStr String形式のJSONデータ
     * @return List<WeeklyRankList> ObjectクラスをList形式で返却
     */
    private List<WeeklyRankList> weeklyRankListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mWeeklyRankList = new WeeklyRankList();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            sendStatus(jsonObj);
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                sendWrList(arrayList);
            }
            List<WeeklyRankList> wrList = Arrays.asList(mWeeklyRankList);
            return wrList;
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに格納.
     *
     * @param jsonObj ステータスを含んだJSONオブジェクト
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

                for (String pagerBuffer : PAGER_PARA) {
                    if (!pager.isNull(pagerBuffer)) {
                        String para = pager.getString(pagerBuffer);
                        map.put(pagerBuffer, para);
                    }
                }
            }
            if (mWeeklyRankList != null) {
                mWeeklyRankList.setWeeklyRankMap(map);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツリストをList<HashMap>の形式でObjectクラスへ格納する.
     *
     * @param arrayList JSONArray
     */
    private void sendWrList(final JSONArray arrayList) {
        try {
            List<HashMap<String, String>> wrList = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> wrListMap = new HashMap<>();
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : JsonConstants.LIST_PARA) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonConstants.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(listBuffer);
                            for (String puinfBuffer : JsonConstants.PUINF_PARA) {
                                String para = puinfObj.getString(puinfBuffer);
                                wrListMap.put(JsonConstants.META_RESPONSE_PUINF + JsonConstants.UNDER_LINE + puinfBuffer, para);
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
                mWeeklyRankList.setWeeklyRankList(wrList);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}