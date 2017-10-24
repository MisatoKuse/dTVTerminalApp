/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TvScheduleJsonParser extends AsyncTask<Object, Object, Object> {

    private TvScheduleWebClient.TvScheduleJsonParserCallback mTvScheduleJsonParserCallback;

    // オブジェクトクラスの定義
    private TvScheduleList mTvScheduleList;

    public static final String TV_SCHEDULE_LIST_STATUS = "status";

    public static final String TV_SCHEDULE_LIST_PAGER = "pager";
    public static final String TV_SCHEDULE_LIST_PAGER_UPPER_LIMIT = "upper_limit";
    public static final String TV_SCHEDULE_LIST_PAGER_LOWER_LIMIT = "lower_limit";
    public static final String TV_SCHEDULE_LIST_PAGER_OFFSET = "offset";
    public static final String TV_SCHEDULE_LIST_PAGER_COUNT = "count";

    public static final String TV_SCHEDULE_LIST = "list";
    public static final String TV_SCHEDULE_LIST_CRID = "crid";
    public static final String TV_SCHEDULE_LIST_CID = "cid";
    public static final String TV_SCHEDULE_LIST_TITLE_ID = "title_id";
    public static final String TV_SCHEDULE_LIST_EPISODE_ID = "episode_id";
    public static final String TV_SCHEDULE_LIST_TITLE = "title";
    public static final String TV_SCHEDULE_LIST_EPITITLE = "epititle";
    public static final String TV_SCHEDULE_LIST_DISP_TYPE = "disp_type";
    public static final String TV_SCHEDULE_LIST_DISPLAY_START_DATE = "display_start_date";
    public static final String TV_SCHEDULE_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String TV_SCHEDULE_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String TV_SCHEDULE_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String TV_SCHEDULE_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String TV_SCHEDULE_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String TV_SCHEDULE_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String TV_SCHEDULE_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String TV_SCHEDULE_LIST_COPYRIGHT = "copyright";
    public static final String TV_SCHEDULE_LIST_THUMB = "thumb";
    public static final String TV_SCHEDULE_LIST_DUR = "dur";
    public static final String TV_SCHEDULE_LIST_DEMONG = "demong";
    public static final String TV_SCHEDULE_LIST_BVFLG = "bvflg";
    public static final String TV_SCHEDULE_LIST_4KFLG = "4kflg";
    public static final String TV_SCHEDULE_LIST_HDRFLG = "hdrflg";
    public static final String TV_SCHEDULE_LIST_AVAIL_STATUS = "avail_status";
    public static final String TV_SCHEDULE_LIST_DELIVERY = "delivery";
    public static final String TV_SCHEDULE_LIST_R_VALUE = "r_value";
    public static final String TV_SCHEDULE_LIST_ADULT = "adult";
    public static final String TV_SCHEDULE_LIST_MS = "ms";
    public static final String TV_SCHEDULE_LIST_NG_FUNC = "ng_func";
    public static final String TV_SCHEDULE_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String TV_SCHEDULE_LIST_DTV = "dtv";

    public static final String[] pagerPara = {TV_SCHEDULE_LIST_PAGER_UPPER_LIMIT, TV_SCHEDULE_LIST_PAGER_LOWER_LIMIT,
            TV_SCHEDULE_LIST_PAGER_OFFSET, TV_SCHEDULE_LIST_PAGER_COUNT};

    public static final String[] listPara = {TV_SCHEDULE_LIST_CRID, TV_SCHEDULE_LIST_CID, TV_SCHEDULE_LIST_TITLE_ID,
            TV_SCHEDULE_LIST_EPISODE_ID, TV_SCHEDULE_LIST_TITLE, TV_SCHEDULE_LIST_EPITITLE, TV_SCHEDULE_LIST_DISP_TYPE,
            TV_SCHEDULE_LIST_DISPLAY_START_DATE, TV_SCHEDULE_LIST_DISPLAY_END_DATE, TV_SCHEDULE_LIST_AVAIL_START_DATE,
            TV_SCHEDULE_LIST_AVAIL_END_DATE, TV_SCHEDULE_LIST_PUBLISH_START_DATE, TV_SCHEDULE_LIST_PUBLISH_END_DATE,
            TV_SCHEDULE_LIST_NEWA_START_DATE, TV_SCHEDULE_LIST_NEWA_END_DATE, TV_SCHEDULE_LIST_COPYRIGHT,
            TV_SCHEDULE_LIST_THUMB, TV_SCHEDULE_LIST_DUR, TV_SCHEDULE_LIST_DEMONG, TV_SCHEDULE_LIST_BVFLG, TV_SCHEDULE_LIST_4KFLG,
            TV_SCHEDULE_LIST_HDRFLG, TV_SCHEDULE_LIST_AVAIL_STATUS, TV_SCHEDULE_LIST_DELIVERY, TV_SCHEDULE_LIST_R_VALUE,
            TV_SCHEDULE_LIST_ADULT, TV_SCHEDULE_LIST_MS, TV_SCHEDULE_LIST_NG_FUNC, TV_SCHEDULE_LIST_GENRE_ID_ARRAY, TV_SCHEDULE_LIST_DTV};

    /**
     * コンストラクタ
     *
     * @param mTvScheduleJsonParserCallback
     */
    public TvScheduleJsonParser(TvScheduleWebClient.TvScheduleJsonParserCallback mTvScheduleJsonParserCallback) {
        this.mTvScheduleJsonParserCallback = mTvScheduleJsonParserCallback;
    }

    @Override
    protected void onPostExecute(Object s) {
        mTvScheduleJsonParserCallback.onTvScheduleJsonParsed((List<TvScheduleList>) s);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        List<TvScheduleList> resultList = TvScheduleListListSender(result);
        return resultList;
    }

    /**
     * CH毎番組Jsonデータを解析する
     *
     * @param jsonStr
     * @return
     */
    public List<TvScheduleList> TvScheduleListListSender(String jsonStr) {

        mTvScheduleList = new TvScheduleList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<TvScheduleList> TV_SCHEDULEList = Arrays.asList(mTvScheduleList);

                return TV_SCHEDULEList;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * statsの値をMapでオブジェクトクラスに渡す
     *
     * @param jsonObj
     */
    public void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<String, String>();
            if (!jsonObj.isNull(TV_SCHEDULE_LIST_STATUS)) {
                String status = jsonObj.getString(TV_SCHEDULE_LIST_STATUS);
                map.put(TV_SCHEDULE_LIST_STATUS, status);
            }

            if (!jsonObj.isNull(TV_SCHEDULE_LIST_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(TV_SCHEDULE_LIST_PAGER);

                for (int i = 0; i < pagerPara.length; i++) {
                    if (!pager.isNull(pagerPara[i])) {
                        String para = pager.getString(pagerPara[i]);
                        map.put(pagerPara[i], para);
                    }
                }
            }

            mTvScheduleList.setTvsMap(map);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納
     *
     * @param jsonObj
     */
    public void sendVcList(JSONObject jsonObj) {
        try {
            if (!jsonObj.isNull(TV_SCHEDULE_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(TV_SCHEDULE_LIST);

                // リストの数だけまわす
                for (int i = 0; i < jsonArr.length(); i++) {
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<String, String>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    for (int j = 0; j < listPara.length; j++) {
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j] == TV_SCHEDULE_LIST_GENRE_ID_ARRAY) {
                                String para = jsonObject.getString(listPara[j]);
                                vcListMap.put(listPara[j], para.substring(1, (para.length() - 1)));
                            } else {
                                String para = jsonObject.getString(listPara[j]);
                                vcListMap.put(listPara[j], para);
                            }
                        }
                    }

                    // i番目のMapをListにadd
                    vcList.add(vcListMap);
                }
                // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
                mTvScheduleList.setTvsList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
