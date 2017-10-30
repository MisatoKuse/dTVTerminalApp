/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.ScaledDownProgramChannelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ScaledDownProgramListChannelListJsonParser {

    // オブジェクトクラスの定義
    private ScaledDownProgramChannelList mScaledDownProgramChannelList;

    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_STATUS = "status";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST = "list";

    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER = "pager";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER_LIMIT = "limit";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER_OFFSET = "offset";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER_COUNT = "count";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER_TOTAL = "total";


    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CRID = "crid";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CID = "cid";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_SERVER_ID = "service_id";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CHNO = "chno";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_TITLE = "title";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_TITLERUBY = "titleruby";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_DISP_TYPE = "disp_type";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_SERVICE = "service";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CH_TYPE = "ch_type";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_THUMB = "thumb";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_DEMONG = "demong";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_4KFLG = "4kflg";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_AVAIL_STATUS = "avail_status";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_DELIVERY = "delivery";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_R_VALUE = "r_value";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_ADULT = "adult";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_NG_FUNC = "ng_func";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_GENRE_ARRAY = "genre_array";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_SYNOP = "synop";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_STAMP = "stamp";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CHSVOD = "chsvod";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PUID = "puid";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_SUB_PUID = "sub_puid";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PRICE = "price";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_QRANGE = "qrange";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_QUINT = "qunit";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PU_S = "pu_s";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PU_E = "pu_e";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CHPACK = "CHPACK";
    private static final String UNDER_LINE = "_";


    public static final String[] pagerPara = {SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER_LIMIT, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER_OFFSET,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER_COUNT, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER_TOTAL};

    public static final String[] listPara = {SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CRID, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CID,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_SERVER_ID, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CHNO, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_TITLE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_TITLERUBY,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_DISP_TYPE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_SERVICE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CH_TYPE,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_AVAIL_START_DATE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_AVAIL_END_DATE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_THUMB,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_DEMONG, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_4KFLG, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_AVAIL_STATUS,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_DELIVERY, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_R_VALUE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_ADULT, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_NG_FUNC, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_GENRE_ARRAY,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_SYNOP, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_STAMP, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CHSVOD, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PUID,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_SUB_PUID, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PRICE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_QRANGE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_QUINT,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PU_S, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PU_E, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CHPACK};
    public static final String[] chpackList = {SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CRID, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_TITLE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_DISP_TYPE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PUID,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_SUB_PUID, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PRICE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_QRANGE, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_QUINT,
            SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PU_S, SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PU_E};


    public List<ScaledDownProgramChannelList> ScaledDownProgramListSender(String jsonStr) {

        mScaledDownProgramChannelList = new ScaledDownProgramChannelList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<ScaledDownProgramChannelList> vodClipList = Arrays.asList(mScaledDownProgramChannelList);

                return vodClipList;
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
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_STATUS)) {
                String status = jsonObj.getString(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_STATUS);
                map.put(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_STATUS, status);
            }


            if (!jsonObj.isNull(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_PAGER);

                for (int i = 0; i < pagerPara.length; i++) {
                    if (!pager.isNull(pagerPara[i])) {
                        String para = pager.getString(pagerPara[i]);
                        map.put(pagerPara[i], para);
                    }
                }
            }

            /* 2017/10/30日実装予定 */

            mScaledDownProgramChannelList.setVcMap(map);

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
    private void sendVcList(JSONObject jsonObj) {
        try {
            if (!jsonObj.isNull(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST);

                // リストの数だけまわす
                for (int i = 0; i < jsonArr.length(); i++) {
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);


                    for (int j = 0; j < listPara.length; j++) {
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j].equals(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_GENRE_ARRAY)) {
                                JSONArray para = jsonObject.getJSONArray(listPara[j]);
                                vcListMap.put(listPara[j], para.toString());
                            } else if (listPara[j].equals(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CHPACK)) {
                                JSONObject para = jsonObject.getJSONObject(listPara[j]);
                                for (int c = 0; c < chpackList.length; c++) {
                                    if (!jsonObject.isNull(chpackList[c])) {
                                        String value = para.getString(chpackList[c]);
                                        vcListMap.put(listPara[j] + UNDER_LINE + chpackList[c], value);
                                    }
                                }
                            } else {
                                String para = jsonObject.getString(listPara[j]);
                                vcListMap.put(listPara[j], para);
                            }
                        }
                    }

                    /* 2017/10/30日実装予定 */

                    // i番目のMapをListにadd
                    vcList.add(vcListMap);
                }
                // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
                mScaledDownProgramChannelList.setVcList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}