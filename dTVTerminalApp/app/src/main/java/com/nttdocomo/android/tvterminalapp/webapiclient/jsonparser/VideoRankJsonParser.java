/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VideoRankJsonParser {
    // オブジェクトクラスの定義
    private VideoRankList mVideoRankList;

    public static final String VIDEORANK_LIST_STATUS = "status";

    public static final String VIDEORANK_LIST_PAGER = "pager";
    public static final String VIDEORANK_LIST_PAGER_LIMIT = "limit";
    public static final String VIDEORANK_LIST_PAGER_OFFSET= "offset";
    public static final String VIDEORANK_LIST_PAGER_COUNT = "count";
    public static final String VIDEORANK_LIST_PAGER_TOTAL = "total";

    public static final String VIDEORANK_LIST = "list";
    public static final String VIDEORANK_LIST_CRID = "crid";
    public static final String VIDEORANK_LIST_CID = "cid";
    public static final String VIDEORANK_LIST_TITLE_ID = "title_id";
    public static final String VIDEORANK_LIST_EPISODE_ID = "episode_id";
    public static final String VIDEORANK_LIST_TITLE = "title";
    public static final String VIDEORANK_LIST_EPITITLE = "epititle";
    public static final String VIDEORANK_LIST_TITLERUBY = "titleruby";
    public static final String VIDEORANK_LIST_DISP_TYPE = "disp_type";
    public static final String VIDEORANK_LIST_START_DATE = "display_start_date";
    public static final String VIDEORANK_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String VIDEORANK_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String VIDEORANK_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String VIDEORANK_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String VIDEORANK_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String VIDEORANK_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String VIDEORANK_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String VIDEORANK_LIST_COPYRIGTHT = "copyright";
    public static final String VIDEORANK_LIST_THUMB = "thumb";
    public static final String VIDEORANK_LIST_DUR = "dur";
    public static final String VIDEORANK_LIST_DEMONG = "demong";
    public static final String VIDEORANK_LIST_DVFLG = "bvflg";
    public static final String VIDEORANK_LIST_4KFLG = "4kflg";
    public static final String VIDEORANK_LIST_HDRFLG = "hdrflg";
    public static final String VIDEORANK_LIST_AVAIL_STATUS = "avail_status";
    public static final String VIDEORANK_LIST_DELIVERY = "delivery";
    public static final String VIDEORANK_LIST_R_VALUE = "r_value";
    public static final String VIDEORANK_LIST_ADULT = "adult";
    public static final String VIDEORANK_LIST_MS = "ms";
    public static final String VIDEORANK_LIST_NG_FUNC = "ng_func";
    public static final String VIDEORANK_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String VIDEORANK_LIST_SYNOP = "synop";
    public static final String VIDEORANK_LIST_PUID = "puid";
    public static final String VIDEORANK_LIST_PRICE = "price";
    public static final String VIDEORANK_LIST_QUNIT = "qunit";
    public static final String VIDEORANK_LIST_PU_S = "pu_s";
    public static final String VIDEORANK_LIST_PU_E = "pu_e";
    public static final String VIDEORANK_LIST_CREDITS = "credits";
    public static final String VIDEORANK_LIST_RATING = "rating";
    public static final String VIDEORANK_LIST_DTV = "dtv";

    public static final String VIDEORANK_LIST_PLIT = "PLIT";
    public static final String VIDEORANK_LIST_PLI_VIS = "pli_vis";
    public static final String VIDEORANK_LIST_PLI_VIE = "pli_vie";

    public static final String VIDEORANK_LIST_PLICENSS = "plicense";
    public static final String VIDEORANK_LIST_PLI_PUID = "pli_puid";
    public static final String VIDEORANK_LIST_PLI_CRID = "pli_crid";
    public static final String VIDEORANK_LIST_PLI_TITLE = "pli_title";
    public static final String VIDEORANK_LIST_PLI_EPITITLE = "pli_epititle";
    public static final String VIDEORANK_LIST_PLI_DISP_TYPE = "pli_disp_type";
    public static final String VIDEORANK_LIST_PLI_PRICE = "pli_price";
    public static final String VIDEORANK_LIST_PLI_QUNIT = "pli_qunit";
    public static final String VIDEORANK_LIST_PLI_GRANGE = "pli_qrange";
    public static final String VIDEORANK_LIST_PLI_PU_S = "pli_pu_s";
    public static final String VIDEORANK_LIST_PLI_PU_E = "pli_pu_e";

    public static final String[] pagerPara = {VIDEORANK_LIST_PAGER_LIMIT, VIDEORANK_LIST_PAGER_OFFSET,
            VIDEORANK_LIST_PAGER_COUNT, VIDEORANK_LIST_PAGER_TOTAL};

    public static final String[] listPara = {VIDEORANK_LIST_CRID, VIDEORANK_LIST_CID, VIDEORANK_LIST_TITLE_ID,
            VIDEORANK_LIST_EPISODE_ID, VIDEORANK_LIST_TITLE, VIDEORANK_LIST_EPITITLE, VIDEORANK_LIST_TITLERUBY,
            VIDEORANK_LIST_DISP_TYPE, VIDEORANK_LIST_START_DATE, VIDEORANK_LIST_DISPLAY_END_DATE,
            VIDEORANK_LIST_AVAIL_START_DATE, VIDEORANK_LIST_AVAIL_END_DATE, VIDEORANK_LIST_PUBLISH_START_DATE,
            VIDEORANK_LIST_PUBLISH_END_DATE, VIDEORANK_LIST_NEWA_START_DATE, VIDEORANK_LIST_NEWA_END_DATE,
            VIDEORANK_LIST_COPYRIGTHT, VIDEORANK_LIST_THUMB, VIDEORANK_LIST_DUR, VIDEORANK_LIST_DEMONG, VIDEORANK_LIST_DVFLG,
            VIDEORANK_LIST_4KFLG, VIDEORANK_LIST_HDRFLG, VIDEORANK_LIST_AVAIL_STATUS, VIDEORANK_LIST_DELIVERY,
            VIDEORANK_LIST_R_VALUE, VIDEORANK_LIST_ADULT, VIDEORANK_LIST_MS, VIDEORANK_LIST_NG_FUNC, VIDEORANK_LIST_GENRE_ID_ARRAY,
            VIDEORANK_LIST_SYNOP, VIDEORANK_LIST_PUID, VIDEORANK_LIST_PRICE, VIDEORANK_LIST_QUNIT, VIDEORANK_LIST_PU_S,
            VIDEORANK_LIST_PU_E, VIDEORANK_LIST_CREDITS, VIDEORANK_LIST_RATING, VIDEORANK_LIST_DTV, VIDEORANK_LIST_PLIT};

//    public static final String[] listPritPara = {VIDEORANK_LIST_PLI_VIS, VIDEORANK_LIST_PLI_VIE, VIDEORANK_LIST_PLICENSS};
//
//    public static final String[] listPritPlicensePara = {VIDEORANK_LIST_PLI_PUID, VIDEORANK_LIST_PLI_CRID, VIDEORANK_LIST_PLI_TITLE,
//            VIDEORANK_LIST_PLI_EPITITLE, VIDEORANK_LIST_PLI_DISP_TYPE, VIDEORANK_LIST_PLI_PRICE, VIDEORANK_LIST_PLI_QUNIT,
//            VIDEORANK_LIST_PLI_GRANGE, VIDEORANK_LIST_PLI_PU_S, VIDEORANK_LIST_PLI_PU_E};

    public List<VideoRankList> VideoRankListSender(String jsonStr) {

        mVideoRankList = new VideoRankList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {

                // statusとpagerの値を取得し、Mapに格納
                HashMap<String, String> map = new HashMap<String, String>();
                if (!jsonObj.isNull(VIDEORANK_LIST_STATUS)) {
                    String status = jsonObj.getString(VIDEORANK_LIST_STATUS);
                    map.put(VIDEORANK_LIST_STATUS, status);
                }
                if (!jsonObj.isNull(VIDEORANK_LIST_PAGER)) {
                    JSONObject pager = jsonObj.getJSONObject(VIDEORANK_LIST_PAGER);

                    for (int i = 0; i < pagerPara.length; i++){
                        if (!pager.isNull(pagerPara[i])) {
                            String para = pager.getString(pagerPara[i]);
                            map.put(pagerPara[i], para);
                        }
                    }
                }
                mVideoRankList.setVrMap(map);

                if (!jsonObj.isNull(VIDEORANK_LIST)) {
                    JSONArray listArr = jsonObj.getJSONArray(VIDEORANK_LIST);
                    sendList(listArr);
                }

                List<VideoRankList> videoRankList = Arrays.asList(mVideoRankList);

                return videoRankList;
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

    public void sendList(JSONArray jsonArr) {
        try {
            // コンテンツリストのList<HashMap>を用意
            List<HashMap<String, String>> wrList = new ArrayList<>();

            for (int i = 0; i<jsonArr.length(); i++) {
                // statusの値を取得し、Mapに格納
                HashMap<String, String> map = new HashMap<String, String>();
                // i番目のJSONArrayをJSONObjectに変換する
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                for (int j = 0; j < listPara.length; j++) {
                    if (!jsonObject.isNull(listPara[j])) {
                        String para = jsonObject.getString(listPara[j]);
                        map.put(listPara[j], para);
                    }
                }
                // i番目のMapをListにadd
                wrList.add(map);
            }
            mVideoRankList.setVrList(wrList);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    public void sendPLITList(JSONArray jsonArr) {
//        try {
//            // コンテンツリストのList<HashMap>を用意
//            List<HashMap<String, String>> plitList = new ArrayList<>();
//
//            for (int i = 0; i<jsonArr.length(); i++) {
//                // statusの値を取得し、Mapに格納
//                HashMap<String, String> map = new HashMap<String, String>();
//                // i番目のJSONArrayをJSONObjectに変換する
//                JSONObject jsonObject = jsonArr.getJSONObject(i);
//                for (int j = 0; j < listPritPara.length; j++) {
//                    if (!jsonObject.isNull(listPritPara[j])) {
//                        if (listPara[j] == VIDEORANK_LIST_PLICENSS) {
//                            sendPlicenseList(jsonObject.getJSONArray(VIDEORANK_LIST_PLICENSS));
//                        } else {
//                            String para = jsonObject.getString(listPritPara[j]);
//                            map.put(listPritPara[j], para);
//                        }
//                    }
//                }
//                // i番目のMapをListにadd
//                plitList.add(map);
//            }
//            mVideoRankList.setVrPLITList(plitList);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    public void sendPlicenseList(JSONArray jsonArr) {
//        try {
//            // コンテンツリストのList<HashMap>を用意
//            List<HashMap<String, String>> plicenseList = new ArrayList<>();
//
//            for (int i = 0; i<jsonArr.length(); i++) {
//                // statusの値を取得し、Mapに格納
//                HashMap<String, String> map = new HashMap<String, String>();
//                // i番目のJSONArrayをJSONObjectに変換する
//                JSONObject jsonObject = jsonArr.getJSONObject(i);
//                for (int j = 0; j < listPritPlicensePara.length; j++) {
//                    if (!jsonObject.isNull(listPara[j])) {
//                        String para = jsonObject.getString(listPritPlicensePara[j]);
//                        map.put(listPritPlicensePara[j], para);
//                    }
//                }
//                // i番目のMapをListにadd
//                plicenseList.add(map);
//            }
//            mVideoRankList.setVrPlicenseList(plicenseList);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}