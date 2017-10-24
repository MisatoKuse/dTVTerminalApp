/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VodClipJsonParser2 {

    // オブジェクトクラスの定義
    private VodClipList mVodClipList;

    public static final String VODCLIP_LIST_STATUS = "status";

    public static final String VODCLIP_LIST_PAGER = "pager";
    public static final String VODCLIP_LIST_PAGER_UPPER_LIMIT = "upper_limit";
    public static final String VODCLIP_LIST_PAGER_LOWER_LIMIT = "lower_limit";
    public static final String VODCLIP_LIST_PAGER_OFFSET = "offset";
    public static final String VODCLIP_LIST_PAGER_COUNT = "count";

    public static final String VODCLIP_LIST = "list";
    public static final String VODCLIP_LIST_CRID = "crid";
    public static final String VODCLIP_LIST_CID = "cid";
    public static final String VODCLIP_LIST_TITLE_ID = "title_id";
    public static final String VODCLIP_LIST_EPISODE_ID = "episode_id";
    public static final String VODCLIP_LIST_TITLE = "title";
    public static final String VODCLIP_LIST_EPITITLE = "epititle";
    public static final String VODCLIP_LIST_DISP_TYPE = "disp_type";
    public static final String VODCLIP_LIST_DISPLAY_START_DATE = "display_start_date";
    public static final String VODCLIP_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String VODCLIP_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String VODCLIP_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String VODCLIP_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String VODCLIP_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String VODCLIP_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String VODCLIP_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String VODCLIP_LIST_COPYRIGHT = "copyright";
    public static final String VODCLIP_LIST_THUMB = "thumb";
    public static final String VODCLIP_LIST_DUR = "dur";
    public static final String VODCLIP_LIST_DEMONG = "demong";
    public static final String VODCLIP_LIST_BVFLG = "bvflg";
    public static final String VODCLIP_LIST_4KFLG = "4kflg";
    public static final String VODCLIP_LIST_HDRFLG = "hdrflg";
    public static final String VODCLIP_LIST_AVAIL_STATUS = "avail_status";
    public static final String VODCLIP_LIST_DELIVERY = "delivery";
    public static final String VODCLIP_LIST_R_VALUE = "r_value";
    public static final String VODCLIP_LIST_ADULT = "adult";
    public static final String VODCLIP_LIST_MS = "ms";
    public static final String VODCLIP_LIST_NG_FUNC = "ng_func";
    public static final String VODCLIP_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String VODCLIP_LIST_DTV = "dtv";

    public static final String[] pagerPara = {VODCLIP_LIST_PAGER_UPPER_LIMIT, VODCLIP_LIST_PAGER_LOWER_LIMIT,
            VODCLIP_LIST_PAGER_OFFSET, VODCLIP_LIST_PAGER_COUNT};

    public static final String[] listPara = {VODCLIP_LIST_CRID, VODCLIP_LIST_CID, VODCLIP_LIST_TITLE_ID,
            VODCLIP_LIST_EPISODE_ID, VODCLIP_LIST_TITLE, VODCLIP_LIST_EPITITLE, VODCLIP_LIST_DISP_TYPE,
            VODCLIP_LIST_DISPLAY_START_DATE, VODCLIP_LIST_DISPLAY_END_DATE, VODCLIP_LIST_AVAIL_START_DATE,
            VODCLIP_LIST_AVAIL_END_DATE, VODCLIP_LIST_PUBLISH_START_DATE, VODCLIP_LIST_PUBLISH_END_DATE,
            VODCLIP_LIST_NEWA_START_DATE, VODCLIP_LIST_NEWA_END_DATE, VODCLIP_LIST_COPYRIGHT,
            VODCLIP_LIST_THUMB, VODCLIP_LIST_DUR, VODCLIP_LIST_DEMONG, VODCLIP_LIST_BVFLG, VODCLIP_LIST_4KFLG,
            VODCLIP_LIST_HDRFLG, VODCLIP_LIST_AVAIL_STATUS, VODCLIP_LIST_DELIVERY, VODCLIP_LIST_R_VALUE,
            VODCLIP_LIST_ADULT, VODCLIP_LIST_MS, VODCLIP_LIST_NG_FUNC, VODCLIP_LIST_GENRE_ID_ARRAY, VODCLIP_LIST_DTV};

    public List<VodClipList> VodClipListSender(String jsonStr) {

        mVodClipList = new VodClipList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<VodClipList> vodClipList = Arrays.asList(mVodClipList);

                return vodClipList;
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
            if (!jsonObj.isNull(VODCLIP_LIST_STATUS)) {
                String status = jsonObj.getString(VODCLIP_LIST_STATUS);
                map.put(VODCLIP_LIST_STATUS, status);
            }

            if (!jsonObj.isNull(VODCLIP_LIST_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(VODCLIP_LIST_PAGER);

                for (int i = 0; i < pagerPara.length; i++){
                    if (!pager.isNull(pagerPara[i])) {
                        String para = pager.getString(pagerPara[i]);
                        map.put(pagerPara[i], para);
                    }
                }
            }

            mVodClipList.setVcMap(map);

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
            if (!jsonObj.isNull(VODCLIP_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(VODCLIP_LIST);

                // リストの数だけまわす
                for (int i = 0; i<jsonArr.length(); i++){
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<String, String>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    for (int j = 0; j < listPara.length; j++){
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j] == VODCLIP_LIST_GENRE_ID_ARRAY) {
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
                mVodClipList.setVcList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}