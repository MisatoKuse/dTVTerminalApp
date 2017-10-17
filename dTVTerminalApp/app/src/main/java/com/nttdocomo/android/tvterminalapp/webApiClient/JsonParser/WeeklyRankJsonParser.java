package com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;

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

public class WeeklyRankJsonParser {
    // オブジェクトクラスの定義
    private WeeklyRankList mWeeklyRankList;

    public static final String WEEKLYRANK_LIST_STATUS = "status";

    public static final String WEEKLYRANK_LIST_PAGER = "pager";
    public static final String WEEKLYRANK_LIST_PAGER_UPPER_LIMIT = "upper_limit";
    public static final String WEEKLYRANK_LIST_PAGER_LOWER_LIMIT = "lower_limit";
    public static final String WEEKLYRANK_LIST_PAGER_OFFSET = "offset";
    public static final String WEEKLYRANK_LIST_PAGER_COUNT = "count";

    public static final String WEEKLYRANK_LIST = "list";
    public static final String WEEKLYRANK_LIST_CRID = "crid";
    public static final String WEEKLYRANK_LIST_CID = "cid";
    public static final String WEEKLYRANK_LIST_TITLE_ID = "title_id";
    public static final String WEEKLYRANK_LIST_EPISODE_ID = "episode_id";
    public static final String WEEKLYRANK_LIST_TITLE = "title";
    public static final String WEEKLYRANK_LIST_EPITITLE = "epititle";
    public static final String WEEKLYRANK_LIST_DISP_TYPE = "disp_type";
    public static final String WEEKLYRANK_LIST_DISPLAY_START_DATE = "display_start_date";
    public static final String WEEKLYRANK_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String WEEKLYRANK_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String WEEKLYRANK_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String WEEKLYRANK_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String WEEKLYRANK_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String WEEKLYRANK_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String WEEKLYRANK_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String WEEKLYRANK_LIST_COPYRIGHT = "copyright";
    public static final String WEEKLYRANK_LIST_THUMB = "thumb";
    public static final String WEEKLYRANK_LIST_DUR = "dur";
    public static final String WEEKLYRANK_LIST_DEMONG = "demong";
    public static final String WEEKLYRANK_LIST_BVFLG = "bvflg";
    public static final String WEEKLYRANK_LIST_4KFLG = "4kflg";
    public static final String WEEKLYRANK_LIST_HDRFLG = "hdrflg";
    public static final String WEEKLYRANK_LIST_AVAIL_STATUS = "avail_status";
    public static final String WEEKLYRANK_LIST_DELIVERY = "delivery";
    public static final String WEEKLYRANK_LIST_R_VALUE = "r_value";
    public static final String WEEKLYRANK_LIST_ADULT = "adult";
    public static final String WEEKLYRANK_LIST_MS = "ms";
    public static final String WEEKLYRANK_LIST_NG_FUNC = "ng_func";
    public static final String WEEKLYRANK_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String WEEKLYRANK_LIST_DTV = "dtv";

    public static final String[] pagerPara = {WEEKLYRANK_LIST_PAGER_UPPER_LIMIT, WEEKLYRANK_LIST_PAGER_LOWER_LIMIT,
            WEEKLYRANK_LIST_PAGER_OFFSET, WEEKLYRANK_LIST_PAGER_COUNT};

    public static final String[] listPara = {WEEKLYRANK_LIST_CRID, WEEKLYRANK_LIST_CID, WEEKLYRANK_LIST_TITLE_ID,
            WEEKLYRANK_LIST_EPISODE_ID, WEEKLYRANK_LIST_TITLE, WEEKLYRANK_LIST_EPITITLE, WEEKLYRANK_LIST_DISP_TYPE,
            WEEKLYRANK_LIST_DISPLAY_START_DATE, WEEKLYRANK_LIST_DISPLAY_END_DATE, WEEKLYRANK_LIST_AVAIL_START_DATE,
            WEEKLYRANK_LIST_AVAIL_END_DATE, WEEKLYRANK_LIST_PUBLISH_START_DATE, WEEKLYRANK_LIST_PUBLISH_END_DATE,
            WEEKLYRANK_LIST_NEWA_START_DATE, WEEKLYRANK_LIST_NEWA_END_DATE, WEEKLYRANK_LIST_COPYRIGHT,
            WEEKLYRANK_LIST_THUMB, WEEKLYRANK_LIST_DUR, WEEKLYRANK_LIST_DEMONG, WEEKLYRANK_LIST_BVFLG, WEEKLYRANK_LIST_4KFLG,
            WEEKLYRANK_LIST_HDRFLG, WEEKLYRANK_LIST_AVAIL_STATUS, WEEKLYRANK_LIST_DELIVERY, WEEKLYRANK_LIST_R_VALUE,
            WEEKLYRANK_LIST_ADULT, WEEKLYRANK_LIST_MS, WEEKLYRANK_LIST_NG_FUNC, WEEKLYRANK_LIST_GENRE_ID_ARRAY, WEEKLYRANK_LIST_DTV};

    public List<WeeklyRankList> WEEKLYRANKListSender(String jsonStr) {

        mWeeklyRankList = new WeeklyRankList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<WeeklyRankList> WEEKLYRANKList = Arrays.asList(mWeeklyRankList);

                return WEEKLYRANKList;
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
            if (!jsonObj.isNull(WEEKLYRANK_LIST_STATUS)) {
                String status = jsonObj.getString(WEEKLYRANK_LIST_STATUS);
                map.put(WEEKLYRANK_LIST_STATUS, status);
            }

            if (!jsonObj.isNull(WEEKLYRANK_LIST_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(WEEKLYRANK_LIST_PAGER);

                for (int i = 0; i < pagerPara.length; i++){
                    if (!pager.isNull(pagerPara[i])) {
                        String para = pager.getString(pagerPara[i]);
                        map.put(pagerPara[i], para);
                    }
                }
            }

            mWeeklyRankList.setWrMap(map);

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
            if (!jsonObj.isNull(WEEKLYRANK_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(WEEKLYRANK_LIST);

                // リストの数だけまわす
                for (int i = 0; i<jsonArr.length(); i++){
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<String, String>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    for (int j = 0; j < listPara.length; j++){
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j] == WEEKLYRANK_LIST_GENRE_ID_ARRAY) {
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
                mWeeklyRankList.setWrList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}