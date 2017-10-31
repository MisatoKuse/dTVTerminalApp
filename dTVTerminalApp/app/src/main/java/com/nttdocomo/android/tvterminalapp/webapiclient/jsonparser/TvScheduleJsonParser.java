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
    public static final String TV_SCHEDULE_LIST = "list";

    public static final String TV_SCHEDULE_LIST_CRID = "crid";
    public static final String TV_SCHEDULE_LIST_TITLE = "title";
    public static final String TV_SCHEDULE_LIST_TITLERUBY = "titleruby";
    public static final String TV_SCHEDULE_LIST_CID = "cid";
    public static final String TV_SCHEDULE_LIST_SERVICE_ID = "service_id";
    public static final String TV_SCHEDULE_LIST_EVENT_ID = "event_id";
    public static final String TV_SCHEDULE_LIST_CHNO = "chno";
    public static final String TV_SCHEDULE_LIST_DISP_TYPE = "disp_type";
    public static final String TV_SCHEDULE_LIST_LINEAR_START_DATE = "linear_start_date";
    public static final String TV_SCHEDULE_LIST_LINEAR_END_DATE = "linear_end_date";
    public static final String TV_SCHEDULE_LIST_VOD_START_DATE = "vod_start_date";
    public static final String TV_SCHEDULE_LIST_VOD_END_DATE = "vod_end_date";
    public static final String TV_SCHEDULE_LIST_THUMB = "thumb";
    public static final String TV_SCHEDULE_LIST_COPYRIGHT = "copyright";
    public static final String TV_SCHEDULE_LIST_DUR = "dur";
    public static final String TV_SCHEDULE_LIST_DEMONG = "demong";
    public static final String TV_SCHEDULE_LIST_AVAIL_STATUS = "avail_status";
    public static final String TV_SCHEDULE_LIST_DELIVERY = "delivery";
    public static final String TV_SCHEDULE_LIST_R_VALUE = "r_value";
    public static final String TV_SCHEDULE_LIST_MAIN_GENRE = "main_genre";
    public static final String TV_SCHEDULE_LIST_SECOND_GEBRE_ARRAY = "second_genre_array";
    public static final String TV_SCHEDULE_LIST_SYNOP = "synop";
    public static final String TV_SCHEDULE_LIST_CREDITS = "credits";
    public static final String TV_SCHEDULE_LIST_CAPL = "capl";
    public static final String TV_SCHEDULE_LIST_COPY = "copy";
    public static final String TV_SCHEDULE_LIST_ADINFO = "adinfo";
    public static final String TV_SCHEDULE_LIST_BILINGAL = "bilingal";
    public static final String TV_SCHEDULE_LIST_LIVE  = "live";
    public static final String TV_SCHEDULE_LIST_FIRST_JAPAN = "first_japan";
    public static final String TV_SCHEDULE_LIST_FIRST_TV = "first_tv";
    public static final String TV_SCHEDULE_LIST_EXCLUSIVE = "exclusive";
    public static final String TV_SCHEDULE_LIST_PRE = "pre";
    public static final String TV_SCHEDULE_LIST_FIRST_CH = "first_ch";
    public static final String TV_SCHEDULE_LIST_ORIGINAL = "original";
    public static final String TV_SCHEDULE_LIST_MASK = "mask";
    public static final String TV_SCHEDULE_LIST_NONSCRAMBLE = "nonscramble";
    public static final String TV_SCHEDULE_LIST_DOWNLOAD = "download";
    public static final String TV_SCHEDULE_LIST_STARTOVER = "startover";
    public static final String TV_SCHEDULE_LIST_STAMP = "stamp";
    public static final String TV_SCHEDULE_LIST_RELATIONAL_ID_ARRAY = "relational_id_array";

    public static final String[] listPara = {TV_SCHEDULE_LIST_CRID,TV_SCHEDULE_LIST_TITLE,TV_SCHEDULE_LIST_TITLERUBY,TV_SCHEDULE_LIST_CID, TV_SCHEDULE_LIST_SERVICE_ID,
            TV_SCHEDULE_LIST_EVENT_ID, TV_SCHEDULE_LIST_CHNO,TV_SCHEDULE_LIST_DISP_TYPE,TV_SCHEDULE_LIST_LINEAR_START_DATE,TV_SCHEDULE_LIST_LINEAR_END_DATE,TV_SCHEDULE_LIST_VOD_START_DATE,
            TV_SCHEDULE_LIST_VOD_END_DATE,TV_SCHEDULE_LIST_THUMB, TV_SCHEDULE_LIST_COPYRIGHT,TV_SCHEDULE_LIST_DUR,TV_SCHEDULE_LIST_DEMONG,TV_SCHEDULE_LIST_AVAIL_STATUS,TV_SCHEDULE_LIST_DELIVERY,
            TV_SCHEDULE_LIST_R_VALUE,TV_SCHEDULE_LIST_MAIN_GENRE,TV_SCHEDULE_LIST_SECOND_GEBRE_ARRAY,TV_SCHEDULE_LIST_SYNOP,TV_SCHEDULE_LIST_CREDITS,TV_SCHEDULE_LIST_CAPL,
            TV_SCHEDULE_LIST_COPY,TV_SCHEDULE_LIST_ADINFO,TV_SCHEDULE_LIST_BILINGAL,TV_SCHEDULE_LIST_LIVE,TV_SCHEDULE_LIST_FIRST_JAPAN,TV_SCHEDULE_LIST_FIRST_TV,TV_SCHEDULE_LIST_EXCLUSIVE,
            TV_SCHEDULE_LIST_PRE,TV_SCHEDULE_LIST_FIRST_CH,TV_SCHEDULE_LIST_ORIGINAL,TV_SCHEDULE_LIST_MASK,TV_SCHEDULE_LIST_NONSCRAMBLE,TV_SCHEDULE_LIST_DOWNLOAD,TV_SCHEDULE_LIST_STARTOVER,
            TV_SCHEDULE_LIST_STAMP,TV_SCHEDULE_LIST_RELATIONAL_ID_ARRAY};

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
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(TV_SCHEDULE_LIST_STATUS)) {
                String status = jsonObj.getString(TV_SCHEDULE_LIST_STATUS);
                map.put(TV_SCHEDULE_LIST_STATUS, status);
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
                for (int i = 0; i<jsonArr.length(); i++){
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    /* 2017/10/30日実装予定 */

                    for (int j = 0; j < listPara.length; j++){
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j].equals(TV_SCHEDULE_LIST_SECOND_GEBRE_ARRAY)) {
                                JSONArray para = jsonObject.getJSONArray(listPara[j]);
                                vcListMap.put(listPara[j], para.toString());
                            }else if(listPara[j].equals(TV_SCHEDULE_LIST_CREDITS)){
                                JSONArray para = jsonObject.getJSONArray(listPara[j]);
                                vcListMap.put(listPara[j], para.toString());
                            }else if(listPara[j].equals(TV_SCHEDULE_LIST_RELATIONAL_ID_ARRAY)){
                                JSONArray para = jsonObject.getJSONArray(listPara[j]);
                                vcListMap.put(listPara[j], para.toString());
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
