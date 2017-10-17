package com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class DailyRankJsonParser  {

    // オブジェクトクラスの定義
    private DailyRankList mDailyRankList;

    public static final String DAILYRANK_LIST_STATUS = "status";

    public static final String DAILYRANK_LIST_PAGER = "pager";
    public static final String DAILYRANK_LIST_PAGER_UPPER_LIMIT = "upper_limit";
    public static final String DAILYRANK_LIST_PAGER_LOWER_LIMIT = "lower_limit";
    public static final String DAILYRANK_LIST_PAGER_OFFSET = "offset";
    public static final String DAILYRANK_LIST_PAGER_COUNT = "count";

    public static final String DAILYRANK_LIST = "list";
    public static final String DAILYRANK_LIST_CRID = "crid";
    public static final String DAILYRANK_LIST_CID = "cid";
    public static final String DAILYRANK_LIST_TITLE_ID = "title_id";
    public static final String DAILYRANK_LIST_EPISODE_ID = "episode_id";
    public static final String DAILYRANK_LIST_TITLE = "title";
    public static final String DAILYRANK_LIST_EPITITLE = "epititle";
    public static final String DAILYRANK_LIST_DISP_TYPE = "disp_type";
    public static final String DAILYRANK_LIST_DISPLAY_START_DATE = "display_start_date";
    public static final String DAILYRANK_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String DAILYRANK_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String DAILYRANK_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String DAILYRANK_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String DAILYRANK_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String DAILYRANK_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String DAILYRANK_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String DAILYRANK_LIST_COPYRIGHT = "copyright";
    public static final String DAILYRANK_LIST_THUMB = "thumb";
    public static final String DAILYRANK_LIST_DUR = "dur";
    public static final String DAILYRANK_LIST_DEMONG = "demong";
    public static final String DAILYRANK_LIST_BVFLG = "bvflg";
    public static final String DAILYRANK_LIST_4KFLG = "4kflg";
    public static final String DAILYRANK_LIST_HDRFLG = "hdrflg";
    public static final String DAILYRANK_LIST_AVAIL_STATUS = "avail_status";
    public static final String DAILYRANK_LIST_DELIVERY = "delivery";
    public static final String DAILYRANK_LIST_R_VALUE = "r_value";
    public static final String DAILYRANK_LIST_ADULT = "adult";
    public static final String DAILYRANK_LIST_MS = "ms";
    public static final String DAILYRANK_LIST_NG_FUNC = "ng_func";
    public static final String DAILYRANK_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String DAILYRANK_LIST_DTV = "dtv";

    public static final String[] pagerPara = {DAILYRANK_LIST_PAGER_UPPER_LIMIT, DAILYRANK_LIST_PAGER_LOWER_LIMIT,
            DAILYRANK_LIST_PAGER_OFFSET, DAILYRANK_LIST_PAGER_COUNT};

    public static final String[] listPara = {DAILYRANK_LIST_CRID, DAILYRANK_LIST_CID, DAILYRANK_LIST_TITLE_ID,
            DAILYRANK_LIST_EPISODE_ID, DAILYRANK_LIST_TITLE, DAILYRANK_LIST_EPITITLE, DAILYRANK_LIST_DISP_TYPE,
            DAILYRANK_LIST_DISPLAY_START_DATE, DAILYRANK_LIST_DISPLAY_END_DATE, DAILYRANK_LIST_AVAIL_START_DATE,
            DAILYRANK_LIST_AVAIL_END_DATE, DAILYRANK_LIST_PUBLISH_START_DATE, DAILYRANK_LIST_PUBLISH_END_DATE,
            DAILYRANK_LIST_NEWA_START_DATE, DAILYRANK_LIST_NEWA_END_DATE, DAILYRANK_LIST_COPYRIGHT,
            DAILYRANK_LIST_THUMB, DAILYRANK_LIST_DUR, DAILYRANK_LIST_DEMONG, DAILYRANK_LIST_BVFLG, DAILYRANK_LIST_4KFLG,
            DAILYRANK_LIST_HDRFLG, DAILYRANK_LIST_AVAIL_STATUS, DAILYRANK_LIST_DELIVERY, DAILYRANK_LIST_R_VALUE,
            DAILYRANK_LIST_ADULT, DAILYRANK_LIST_MS, DAILYRANK_LIST_NG_FUNC, DAILYRANK_LIST_GENRE_ID_ARRAY, DAILYRANK_LIST_DTV};

    public List<DailyRankList> DAILYRANKListSender(String jsonStr) {

        mDailyRankList = new DailyRankList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<DailyRankList> dRList = Arrays.asList(mDailyRankList);

                return dRList;
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

    public void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<String, String>();
            if (!jsonObj.isNull(DAILYRANK_LIST_STATUS)) {
                String status = jsonObj.getString(DAILYRANK_LIST_STATUS);
                map.put(DAILYRANK_LIST_STATUS, status);
            }

            if (!jsonObj.isNull(DAILYRANK_LIST_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(DAILYRANK_LIST_PAGER);

                for (int i = 0; i < pagerPara.length; i++){
                    if (!pager.isNull(pagerPara[i])) {
                        String para = pager.getString(pagerPara[i]);
                        map.put(pagerPara[i], para);
                    }
                }
            }

            mDailyRankList.setDrMap(map);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
    * コンテンツのList<HashMap>をオブジェクトクラスに格納
     */
    public void sendVcList(JSONObject jsonObj) {
        try {
            if (!jsonObj.isNull(DAILYRANK_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(DAILYRANK_LIST);

                // リストの数だけまわす
                for (int i = 0; i<jsonArr.length(); i++){
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<String, String>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    for (int j = 0; j < listPara.length; j++){
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j] == DAILYRANK_LIST_GENRE_ID_ARRAY) {
                                String para = jsonObject.getString(listPara[j]);
                                vcListMap.put(listPara[j], para.substring(1, (para.length() -1)));
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
                mDailyRankList.setDrList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    // オブジェクトクラスの定義
//    private DailyRankList mDAILYRANKList;
//
//    public static final String DAILYRANK_LIST_STATUS = "status";
//
//    public static final String DAILYRANK_LIST_PAGER = "pager";
//    public static final String DAILYRANK_LIST_PAGER_LIMIT = "limit";
//    public static final String DAILYRANK_LIST_PAGER_OFFSET = "offset";
//    public static final String DAILYRANK_LIST_PAGER_COUNT = "count";
//    public static final String DAILYRANK_LIST_PAGER_TOTAL = "total";
//
//    public static final String DAILYRANK_LIST = "list";
//    public static final String DAILYRANK_LIST_CRID = "crid";
//    public static final String DAILYRANK_LIST_TITLE = "title";
//    public static final String DAILYRANK_LIST_CID = "cid";
//    public static final String DAILYRANK_LIST_SERVICE_ID = "service_id";
//    public static final String DAILYRANK_LIST_EVENT_ID = "event_id";
//    public static final String DAILYRANK_LIST_CHNO = "chno";
//    public static final String DAILYRANK_LIST_DISP_TYPE = "disp_type";
//    public static final String DAILYRANK_LIST_MISSED_VOD = "missed_vod";
//    public static final String DAILYRANK_LIST_LINER_START_DATE = "linear_start_date";
//    public static final String DAILYRANK_LIST_LINER_START_END = "linear_start_end";
//    public static final String DAILYRANK_LIST_VOD_START_DATE = "vod_start_date";
//    public static final String DAILYRANK_LIST_VOD_END_DATE = "vod_end_date";
//    public static final String DAILYRANK_LIST_THUMB = "thumb";
//    public static final String DAILYRANK_LIST_COPYRIGHT = "copyright";
//    public static final String DAILYRANK_LIST_DUR = "dur";
//    public static final String DAILYRANK_LIST_DEMONG = "demong";
//    public static final String DAILYRANK_LIST_AVAIL_STATUS = "avail_status";
//    public static final String DAILYRANK_LIST_DELIVERY = "delivery";
//    public static final String DAILYRANK_LIST_R_VALUE = "r_value";
//
//    public static final String[] pagerPara = {DAILYRANK_LIST_PAGER_LIMIT, DAILYRANK_LIST_PAGER_OFFSET,
//            DAILYRANK_LIST_PAGER_COUNT, DAILYRANK_LIST_PAGER_TOTAL};
//
//    public static final String[] listPara = {DAILYRANK_LIST_CRID, DAILYRANK_LIST_TITLE, DAILYRANK_LIST_CID,
//            DAILYRANK_LIST_SERVICE_ID, DAILYRANK_LIST_EVENT_ID, DAILYRANK_LIST_CHNO, DAILYRANK_LIST_DISP_TYPE,
//            DAILYRANK_LIST_MISSED_VOD, DAILYRANK_LIST_LINER_START_DATE, DAILYRANK_LIST_LINER_START_END,
//            DAILYRANK_LIST_VOD_START_DATE, DAILYRANK_LIST_VOD_END_DATE, DAILYRANK_LIST_THUMB,
//            DAILYRANK_LIST_COPYRIGHT, DAILYRANK_LIST_DUR, DAILYRANK_LIST_DEMONG,
//            DAILYRANK_LIST_AVAIL_STATUS, DAILYRANK_LIST_DELIVERY, DAILYRANK_LIST_R_VALUE};
//
//    public List DailyRankListSender(String jsonStr) {
//
//        mDAILYRANKList = new DailyRankList();
//
//        try {
//            JSONObject jsonObj = new JSONObject(jsonStr);
//            if (jsonObj != null) {
//                sendStatus(jsonObj);
//                sendDailyRank(jsonObj);
//
//                List<DailyRankList> DAILYRANKList = Arrays.asList(mDAILYRANKList);
//
//                return DAILYRANKList;
//            }
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public void sendStatus(JSONObject jsonObj) {
//        try {
//            // statusの値を取得し、Mapに格納
//            HashMap<String, String> map = new HashMap<String, String>();
//
//            if (!jsonObj.isNull(DAILYRANK_LIST_STATUS)) {
//                String status = jsonObj.getString(DAILYRANK_LIST_STATUS);
//                Log.d("hino", " status: " + status);
//                map.put(DAILYRANK_LIST_STATUS, status);
//            }
//
//            if (!jsonObj.isNull(DAILYRANK_LIST_PAGER)) {
//                JSONObject pager = jsonObj.getJSONObject(DAILYRANK_LIST_PAGER);
//
//                for (int i = 0; i < pagerPara.length; i++){
//                    if (!pager.isNull(pagerPara[i])) {
//                        String para = pager.getString(pagerPara[i]);
//                        map.put(pagerPara[i], para);
//                    }
//                }
//            }
//
//            for (String key : map.keySet()) {
//                Log.d("hino DailyRankPS", " key: " + key + " map.get(key): " + map.get(key));
//            }
//
//            mDAILYRANKList.setVcMap(map);
//
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    /*
//    * コンテンツのList<HashMap>をオブジェクトクラスに格納
//     */
//    public void sendDailyRank(JSONObject jsonObj) {
//        try {
//            if (!jsonObj.isNull(DAILYRANK_LIST)) {
//                // コンテンツリストのList<HashMap>を用意
//                List<HashMap<String, String>> drList = new ArrayList<>();
//
//                // コンテンツリストをJSONArrayにパースする
//                JSONArray jsonArr = jsonObj.getJSONArray(DAILYRANK_LIST);
//
//                // リストの数だけまわす
//                for (int i = 0; i<jsonArr.length(); i++){
//                    Log.d("hino", "jsonArr.length(): " + jsonArr.length());
//                    // 最初にHashMapを生成＆初期化
//                    HashMap<String, String> drListMap = new HashMap<String, String>();
//
//                    // i番目のJSONArrayをJSONObjectに変換する
//                    JSONObject jsonObject = jsonArr.getJSONObject(i);
//
//                    for (int j = 0; j < listPara.length; j++){
//                        if (!jsonObject.isNull(listPara[j])) {
//                            String para = jsonObject.getString(listPara[j]);
//                            drListMap.put(listPara[j], para);
//                            Log.d("hino", listPara[j] + " : " + para);
//                        }
//                    }
//
//                    // i番目のMapをListにadd
//                    drList.add(drListMap);
//                }
//                // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
//                mDAILYRANKList.setDrList(drList);
//            }
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

}