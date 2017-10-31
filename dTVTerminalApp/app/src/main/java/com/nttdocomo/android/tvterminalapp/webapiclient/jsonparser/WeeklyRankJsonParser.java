/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;
import android.os.Bundle;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
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

    // コミット時に警告が出るが、対外的なパラメータなので対応はしない
    public static final String WEEKLYRANK_LIST_STATUS = "status";

    public static final String WEEKLYRANK_LIST_PAGER = "pager";
    public static final String WEEKLYRANK_LIST_PAGER_LIMIT = "limit";
    public static final String WEEKLYRANK_LIST_PAGER_OFFSET = "offset";
    public static final String WEEKLYRANK_LIST_PAGER_COUNT = "count";
    public static final String WEEKLYRANK_LIST_PAGER_TOTAL = "total";

    public static final String WEEKLYRANK_LIST = "list";
    public static final String WEEKLYRANK_LIST_CRID = "crid";
    public static final String WEEKLYRANK_LIST_TITLE = "title";
    public static final String WEEKLYRANK_LIST_CID = "cid";
    public static final String WEEKLYRANK_LIST_SERVICE_ID = "service_id";
    public static final String WEEKLYRANK_LIST_EVENT_ID = "event_id";
    public static final String WEEKLYRANK_LIST_CHNO = "chno";
    public static final String WEEKLYRANK_LIST_DISP_TYPE = "disp_type";
    public static final String WEEKLYRANK_LIST_MISSED_VOD = "missed_vod";
    public static final String WEEKLYRANK_LIST_LINEAR_START_DATE = "linear_start_date";
    public static final String WEEKLYRANK_LIST_LINEAR_START_END = "linear_start_end";
    public static final String WEEKLYRANK_LIST_VOD_START_DATE = "vod_start_date";
    public static final String WEEKLYRANK_LIST_VOD_END_DATE = "vod_end_date";
    public static final String WEEKLYRANK_LIST_THUMB = "thumb";
    public static final String WEEKLYRANK_LIST_COPYRIGHT = "copyright";
    public static final String WEEKLYRANK_LIST_DUR = "dur";
    public static final String WEEKLYRANK_LIST_DEMONG = "demong";
    public static final String WEEKLYRANK_LIST_AVAII_STATUS = "avail_status";
    public static final String WEEKLYRANK_LIST_DELIVERY = "delivery";
    public static final String WEEKLYRANK_LIST_R_VALUE = "r_value";

    // **FindBugs** Bad practice FindBugは、"pagerPara"と"listPara"はpublicを外せと言うが、対外的なパラメータなので、対応は行わない。
    public static final String[] pagerPara = {WEEKLYRANK_LIST_PAGER_LIMIT, WEEKLYRANK_LIST_PAGER_OFFSET,
            WEEKLYRANK_LIST_PAGER_COUNT, WEEKLYRANK_LIST_PAGER_TOTAL};

    public static final String[] listPara = {WEEKLYRANK_LIST_CRID, WEEKLYRANK_LIST_TITLE, WEEKLYRANK_LIST_CID,
            WEEKLYRANK_LIST_SERVICE_ID, WEEKLYRANK_LIST_EVENT_ID, WEEKLYRANK_LIST_CHNO, WEEKLYRANK_LIST_DISP_TYPE,
            WEEKLYRANK_LIST_MISSED_VOD, WEEKLYRANK_LIST_LINEAR_START_DATE, WEEKLYRANK_LIST_LINEAR_START_END,
            WEEKLYRANK_LIST_VOD_START_DATE, WEEKLYRANK_LIST_VOD_END_DATE, WEEKLYRANK_LIST_THUMB,
            WEEKLYRANK_LIST_COPYRIGHT, WEEKLYRANK_LIST_DUR, WEEKLYRANK_LIST_DEMONG,
            WEEKLYRANK_LIST_AVAII_STATUS, WEEKLYRANK_LIST_DELIVERY, WEEKLYRANK_LIST_R_VALUE};

    /** 拡張情報 **/
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
     * @param mWeeklyRankJsonParserCallback コールバック用
     * @param extraDataSrc 拡張情報
     */
    public WeeklyRankJsonParser(WeeklyRankWebClient.WeeklyRankJsonParserCallback
                                        mWeeklyRankJsonParserCallback,Bundle extraDataSrc) {
        this.mWeeklyRankJsonParserCallback = mWeeklyRankJsonParserCallback;

        //拡張情報の追加
        mExtraData = extraDataSrc;
    }

    @Override
    protected void onPostExecute(Object s) {
        //拡張情報が存在すれば、入れ込む
        List<WeeklyRankList> rankLists = (List<WeeklyRankList>) s;
        if(mExtraData != null) {
            for(WeeklyRankList rankList : rankLists) {
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
            if (jsonObj != null) {
                sendStatus(jsonObj);
                if (!jsonObj.isNull(WEEKLYRANK_LIST)) {
                    JSONArray arrayLlist = jsonObj.getJSONArray(WEEKLYRANK_LIST);
                    senWrcList(arrayLlist);
                }
                List<WeeklyRankList> wrList = Arrays.asList(mWeeklyRankList);
                return wrList;
            }
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
            if (!jsonObj.isNull(WEEKLYRANK_LIST_STATUS)) {
                String status = jsonObj.getString(WEEKLYRANK_LIST_STATUS);
                map.put(WEEKLYRANK_LIST_STATUS, status);
            }

            if (!jsonObj.isNull(WEEKLYRANK_LIST_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(WEEKLYRANK_LIST_PAGER);

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
                        String para = jsonObject.getString(listBuffer);
                        wrListMap.put(listBuffer, para);
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