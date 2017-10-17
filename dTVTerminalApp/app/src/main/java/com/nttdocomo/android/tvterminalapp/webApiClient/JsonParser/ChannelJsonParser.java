package com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class ChannelJsonParser {
    // オブジェクトクラスの定義
    private ChannelList mChannelList;

    public static final String CHANNEL_LIST_STATUS = "status";

    public static final String CHANNEL_LIST_PAGER = "pager";
    public static final String CHANNEL_LIST_PAGER_UPPER_LIMIT = "upper_limit";
    public static final String CHANNEL_LIST_PAGER_LOWER_LIMIT = "lower_limit";
    public static final String CHANNEL_LIST_PAGER_OFFSET = "offset";
    public static final String CHANNEL_LIST_PAGER_COUNT = "count";

    public static final String CHANNEL_LIST = "list";
    public static final String CHANNEL_LIST_CRID = "crid";
    public static final String CHANNEL_LIST_CID = "cid";
    public static final String CHANNEL_LIST_TITLE_ID = "title_id";
    public static final String CHANNEL_LIST_EPISODE_ID = "episode_id";
    public static final String CHANNEL_LIST_TITLE = "title";
    public static final String CHANNEL_LIST_EPITITLE = "epititle";
    public static final String CHANNEL_LIST_DISP_TYPE = "disp_type";
    public static final String CHANNEL_LIST_DISPLAY_START_DATE = "display_start_date";
    public static final String CHANNEL_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String CHANNEL_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String CHANNEL_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String CHANNEL_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String CHANNEL_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String CHANNEL_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String CHANNEL_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String CHANNEL_LIST_COPYRIGHT = "copyright";
    public static final String CHANNEL_LIST_THUMB = "thumb";
    public static final String CHANNEL_LIST_DUR = "dur";
    public static final String CHANNEL_LIST_DEMONG = "demong";
    public static final String CHANNEL_LIST_BVFLG = "bvflg";
    public static final String CHANNEL_LIST_4KFLG = "4kflg";
    public static final String CHANNEL_LIST_HDRFLG = "hdrflg";
    public static final String CHANNEL_LIST_AVAIL_STATUS = "avail_status";
    public static final String CHANNEL_LIST_DELIVERY = "delivery";
    public static final String CHANNEL_LIST_R_VALUE = "r_value";
    public static final String CHANNEL_LIST_ADULT = "adult";
    public static final String CHANNEL_LIST_MS = "ms";
    public static final String CHANNEL_LIST_NG_FUNC = "ng_func";
    public static final String CHANNEL_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String CHANNEL_LIST_DTV = "dtv";

    public static final String[] pagerPara = {CHANNEL_LIST_PAGER_UPPER_LIMIT, CHANNEL_LIST_PAGER_LOWER_LIMIT,
            CHANNEL_LIST_PAGER_OFFSET, CHANNEL_LIST_PAGER_COUNT};

    public static final String[] listPara = {CHANNEL_LIST_CRID, CHANNEL_LIST_CID, CHANNEL_LIST_TITLE_ID,
            CHANNEL_LIST_EPISODE_ID, CHANNEL_LIST_TITLE, CHANNEL_LIST_EPITITLE, CHANNEL_LIST_DISP_TYPE,
            CHANNEL_LIST_DISPLAY_START_DATE, CHANNEL_LIST_DISPLAY_END_DATE, CHANNEL_LIST_AVAIL_START_DATE,
            CHANNEL_LIST_AVAIL_END_DATE, CHANNEL_LIST_PUBLISH_START_DATE, CHANNEL_LIST_PUBLISH_END_DATE,
            CHANNEL_LIST_NEWA_START_DATE, CHANNEL_LIST_NEWA_END_DATE, CHANNEL_LIST_COPYRIGHT,
            CHANNEL_LIST_THUMB, CHANNEL_LIST_DUR, CHANNEL_LIST_DEMONG, CHANNEL_LIST_BVFLG, CHANNEL_LIST_4KFLG,
            CHANNEL_LIST_HDRFLG, CHANNEL_LIST_AVAIL_STATUS, CHANNEL_LIST_DELIVERY, CHANNEL_LIST_R_VALUE,
            CHANNEL_LIST_ADULT, CHANNEL_LIST_MS, CHANNEL_LIST_NG_FUNC, CHANNEL_LIST_GENRE_ID_ARRAY, CHANNEL_LIST_DTV};

    public List<ChannelList> CHANNELListSender(String jsonStr) {

        mChannelList = new ChannelList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<ChannelList> clList = Arrays.asList(mChannelList);

                return clList;
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
            if (!jsonObj.isNull(CHANNEL_LIST_STATUS)) {
                String status = jsonObj.getString(CHANNEL_LIST_STATUS);
                map.put(CHANNEL_LIST_STATUS, status);
            }

            if (!jsonObj.isNull(CHANNEL_LIST_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(CHANNEL_LIST_PAGER);

                for (int i = 0; i < pagerPara.length; i++){
                    if (!pager.isNull(pagerPara[i])) {
                        String para = pager.getString(pagerPara[i]);
                        map.put(pagerPara[i], para);
                    }
                }
            }

            mChannelList.setClMap(map);

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
            if (!jsonObj.isNull(CHANNEL_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(CHANNEL_LIST);

                // リストの数だけまわす
                for (int i = 0; i<jsonArr.length(); i++){
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<String, String>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    for (int j = 0; j < listPara.length; j++){
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j] == CHANNEL_LIST_GENRE_ID_ARRAY) {
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
                mChannelList.setClList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



//
//    // オブジェクトクラスの定義
//    private ChannelList mChannelList;
//
//    public static final String CHANNEL_LIST_STATUS = "status";
//
//    public static final String CHANNEL_LIST = "list";
//    public static final String CHANNEL_LIST_CRID = "crid";
//    public static final String CHANNEL_LIST_TITLE = "title";
//    public static final String CHANNEL_LIST_TITLERUBY = "titleruby";
//    public static final String CHANNEL_LIST_CID = "cid";
//    public static final String CHANNEL_LIST_SERVICE_ID = "service_id";
//    public static final String CHANNEL_LIST_EVENT_ID = "event_id";
//    public static final String CHANNEL_LIST_CHNO = "chno";
//    public static final String CHANNEL_LIST_DISP_TYPE = "disp_type";
//    public static final String CHANNEL_LIST_LINEAR_START_DATE = "linear_start_date";
//    public static final String CHANNEL_LIST_LINEAR_END_DATE = "linear_end_date";
//    public static final String CHANNEL_LIST_VOD_START_DATE = "vod_start_date";
//    public static final String CHANNEL_LIST_VOD_END_DATE = "vod_end_date";
//    public static final String CHANNEL_LIST_THUMB = "thumb";
//    public static final String CHANNEL_LIST_COPYRIGHT = "copyright";
//    public static final String CHANNEL_LIST_DUR = "dur";
//    public static final String CHANNEL_LIST_DEMONG = "demong";
//    public static final String CHANNEL_LIST_AVAIL_STATUS = "avail_status";
//    public static final String CHANNEL_LIST_DELIVERY = "delivery";
//    public static final String CHANNEL_LIST_R_VALUE = "r_value";
//    public static final String CHANNEL_LIST_MAIN_GENRE = "main_genre";
//    public static final String CHANNEL_LIST_SECOND_GENRE_ARRAY = "second_genre_array";
//    public static final String CHANNEL_LIST_SYNOP = "synop";
//    public static final String CHANNEL_LIST_CREDITS = "credits";
//    public static final String CHANNEL_LIST_CAPL = "capl";
//    public static final String CHANNEL_LIST_COPY = "copy";
//    public static final String CHANNEL_LIST_ADINFO = "adinfo";
//    public static final String CHANNEL_LIST_BILINGAL = "bilingal";
//    public static final String CHANNEL_LIST_LIVE = "live";
//    public static final String CHANNEL_LIST_FIRST_JAPAN = "first_japan";
//    public static final String CHANNEL_LIST_FIRST_TV = "first_tv";
//    public static final String CHANNEL_LIST_EXCLUSIVE = "exclusive";
//    public static final String CHANNEL_LIST_PRE = "pre";
//    public static final String CHANNEL_LIST_FIRST_CH = "first_ch";
//    public static final String CHANNEL_LIST_ORIGINAL = "original";
//    public static final String CHANNEL_LIST_MASK = "mask";
//    public static final String CHANNEL_LIST_NONSCRAMBLE = "nonscramble";
//    public static final String CHANNEL_LIST_DOWNLOAD = "download";
//    public static final String CHANNEL_LIST_STARTOVER = "startover";
//    public static final String CHANNEL_LIST_STAMP = "stamp";
//    public static final String CHANNEL_LIST_RELATIONAL_ID_ARRAY = "relational_id_array";
//
//    public static final String[] listPara = {CHANNEL_LIST_CRID, CHANNEL_LIST_TITLE, CHANNEL_LIST_TITLERUBY,
//            CHANNEL_LIST_CID, CHANNEL_LIST_SERVICE_ID, CHANNEL_LIST_EVENT_ID, CHANNEL_LIST_CHNO,
//            CHANNEL_LIST_DISP_TYPE, CHANNEL_LIST_LINEAR_START_DATE, CHANNEL_LIST_LINEAR_END_DATE,
//            CHANNEL_LIST_VOD_START_DATE, CHANNEL_LIST_VOD_END_DATE, CHANNEL_LIST_THUMB,
//            CHANNEL_LIST_COPYRIGHT, CHANNEL_LIST_DUR, CHANNEL_LIST_DEMONG, CHANNEL_LIST_AVAIL_STATUS,
//            CHANNEL_LIST_DELIVERY, CHANNEL_LIST_R_VALUE, CHANNEL_LIST_MAIN_GENRE, CHANNEL_LIST_SECOND_GENRE_ARRAY,
//            CHANNEL_LIST_SYNOP, CHANNEL_LIST_CREDITS, CHANNEL_LIST_CAPL, CHANNEL_LIST_COPY,
//            CHANNEL_LIST_ADINFO, CHANNEL_LIST_BILINGAL, CHANNEL_LIST_LIVE, CHANNEL_LIST_FIRST_JAPAN, CHANNEL_LIST_FIRST_TV,
//            CHANNEL_LIST_EXCLUSIVE, CHANNEL_LIST_PRE, CHANNEL_LIST_FIRST_CH, CHANNEL_LIST_ORIGINAL, CHANNEL_LIST_MASK,
//            CHANNEL_LIST_NONSCRAMBLE, CHANNEL_LIST_DOWNLOAD, CHANNEL_LIST_STARTOVER, CHANNEL_LIST_STAMP, CHANNEL_LIST_RELATIONAL_ID_ARRAY
//    };
//
//
//    public List sendChannelListStatus(String jsonStr) {
//
//        mChannelList = new ChannelList();
//
//        try {
//            // 受け取ったStringをJSONにパースする
//            JSONObject jsonObj = new JSONObject(jsonStr);
//
//            // JSONのnullチェック
//            if (jsonObj != null) {
//                HashMap<String, String> map = new HashMap<String, String>();
//                if (jsonObj.isNull(CHANNEL_LIST_STATUS)) {
//                    String status = jsonObj.getString(CHANNEL_LIST_STATUS);
//                    map.put(CHANNEL_LIST_STATUS, status);
//                }
//                mChannelList.setClMap(map);
//
//                sendChannelList(jsonObj);
//
//                List<ChannelList> CLList = Arrays.asList(mChannelList);
//
//                return CLList;
//
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
//    /*
//    * コンテンツのList<HashMap>をオブジェクトクラスに格納
//     */
//    public void sendChannelList(JSONObject jsonObj) {
//        try {
//            if (!jsonObj.isNull(CHANNEL_LIST)) {
//                // コンテンツリストのList<HashMap>を用意
//                List<HashMap<String, String>> clList = new ArrayList<>();
//
//                // コンテンツリストをJSONArrayにパースする
//                JSONArray jsonArr = jsonObj.getJSONArray(CHANNEL_LIST);
//
//                // リストの数だけまわす
//                for (int i = 0; i<jsonArr.length(); i++){
//                    // 最初にHashMapを生成＆初期化
//                    HashMap<String, String> clListMap = new HashMap<String, String>();
//
//                    // i番目のJSONArrayをJSONObjectに変換する
//                    JSONObject jsonObject = jsonArr.getJSONObject(i);
//
//                    for (int j = 0; j < listPara.length; j++) {
//                        if (!jsonObject.isNull(listPara[j])) {
//                            if (listPara[j] == CHANNEL_LIST_RELATIONAL_ID_ARRAY
//                                    || listPara[j] == CHANNEL_LIST_CREDITS
//                                    || listPara[j] == CHANNEL_LIST_SECOND_GENRE_ARRAY) {
//                                String para = jsonObject.getString(listPara[j]);
//                                clListMap.put(listPara[j], para.substring(1, (para.length() -1)));
//                            } else {
//                                String para = jsonObject.getString(listPara[j]);
//                                clListMap.put(listPara[j], para);
//                            }
//                        }
//                    }
//                    // i番目のMapをListにadd
//                    clList.add(clListMap);
//                }
//                // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
//                mChannelList.setClList(clList);
//            }
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

}
