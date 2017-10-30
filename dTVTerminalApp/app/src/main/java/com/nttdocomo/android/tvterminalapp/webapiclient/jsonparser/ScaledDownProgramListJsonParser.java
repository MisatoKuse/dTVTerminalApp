/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.ScaledDownProgramList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ScaledDownProgramListJsonParser {

    // オブジェクトクラスの定義
    private ScaledDownProgramList mScaledDownProgramList;

    public static final String SCALEDDOWN_PROGRAM_LIST_STATUS = "status";
    public static final String SCALEDDOWN_PROGRAM_LIST = "list";


    public static final String SCALEDDOWN_PROGRAM_LIST_CRID = "crid";
    public static final String SCALEDDOWN_PROGRAM_LIST_TITLE = "title";
    public static final String SCALEDDOWN_PROGRAM_LIST_TITLERUBY = "titleruby";
    public static final String SCALEDDOWN_PROGRAM_LIST_CID = "cid";
    public static final String SCALEDDOWN_PROGRAM_LIST_SERVICE_ID = "service_id";
    public static final String SCALEDDOWN_PROGRAM_LIST_EVENT_ID = "event_id";
    public static final String SCALEDDOWN_PROGRAM_LIST_CHNO = "chno";
    public static final String SCALEDDOWN_PROGRAM_LIST_DISP_TYPE = "disp_type";
    public static final String SCALEDDOWN_PROGRAM_LIST_LINEAR_START_DATE = "linear_start_date";
    public static final String SCALEDDOWN_PROGRAM_LIST_LINEAR_END_DATE = "linear_end_date";
    public static final String SCALEDDOWN_PROGRAM_LIST_VOD_START_DATE = "vod_start_date";
    public static final String SCALEDDOWN_PROGRAM_LIST_VOD_END_DATE = "vod_end_date";
    public static final String SCALEDDOWN_PROGRAM_LIST_THUMB = "thumb";
    public static final String SCALEDDOWN_PROGRAM_LIST_COPYRIGHT = "copyright";
    public static final String SCALEDDOWN_PROGRAM_LIST_DUR = "dur";
    public static final String SCALEDDOWN_PROGRAM_LIST_DEMONG = "demong";
    public static final String SCALEDDOWN_PROGRAM_LIST_AVAIL_STATUS = "avail_status";
    public static final String SCALEDDOWN_PROGRAM_LIST_DELIVERY = "delivery";
    public static final String SCALEDDOWN_PROGRAM_LIST_R_VALUE = "r_value";
    public static final String SCALEDDOWN_PROGRAM_LIST_MAIN_GENRE = "main_genre";
    public static final String SCALEDDOWN_PROGRAM_LIST_SECOND_GEBRE_ARRAY = "second_genre_array";
    public static final String SCALEDDOWN_PROGRAM_LIST_SYNOP = "synop";
    public static final String SCALEDDOWN_PROGRAM_LIST_CREDITS = "credits";
    public static final String SCALEDDOWN_PROGRAM_LIST_CAPL = "capl";
    public static final String SCALEDDOWN_PROGRAM_LIST_COPY = "copy";
    public static final String SCALEDDOWN_PROGRAM_LIST_ADINFO = "adinfo";
    public static final String SCALEDDOWN_PROGRAM_LIST_BILINGAL = "bilingal";
    public static final String SCALEDDOWN_PROGRAM_LIST_LIVE  = "live";
    public static final String SCALEDDOWN_PROGRAM_LIST_FIRST_JAPAN = "first_japan";
    public static final String SCALEDDOWN_PROGRAM_LIST_FIRST_TV = "first_tv";
    public static final String SCALEDDOWN_PROGRAM_LIST_EXCLUSIVE = "exclusive";
    public static final String SCALEDDOWN_PROGRAM_LIST_PRE = "pre";
    public static final String SCALEDDOWN_PROGRAM_LIST_FIRST_CH = "first_ch";
    public static final String SCALEDDOWN_PROGRAM_LIST_ORIGINAL = "original";
    public static final String SCALEDDOWN_PROGRAM_LIST_MASK = "mask";
    public static final String SCALEDDOWN_PROGRAM_LIST_NONSCRAMBLE = "nonscramble";
    public static final String SCALEDDOWN_PROGRAM_LIST_DOWNLOAD = "download";
    public static final String SCALEDDOWN_PROGRAM_LIST_STARTOVER = "startover";
    public static final String SCALEDDOWN_PROGRAM_LIST_STAMP = "stamp";
    public static final String SCALEDDOWN_PROGRAM_LIST_RELATIONAL_ID_ARRAY = "relational_id_array";

    public static final String[] listPara = {SCALEDDOWN_PROGRAM_LIST_CRID,SCALEDDOWN_PROGRAM_LIST_TITLE,SCALEDDOWN_PROGRAM_LIST_TITLERUBY,SCALEDDOWN_PROGRAM_LIST_CID, SCALEDDOWN_PROGRAM_LIST_SERVICE_ID,
            SCALEDDOWN_PROGRAM_LIST_EVENT_ID, SCALEDDOWN_PROGRAM_LIST_CHNO,SCALEDDOWN_PROGRAM_LIST_DISP_TYPE,SCALEDDOWN_PROGRAM_LIST_LINEAR_START_DATE,SCALEDDOWN_PROGRAM_LIST_LINEAR_END_DATE,SCALEDDOWN_PROGRAM_LIST_VOD_START_DATE,
            SCALEDDOWN_PROGRAM_LIST_VOD_END_DATE,SCALEDDOWN_PROGRAM_LIST_THUMB, SCALEDDOWN_PROGRAM_LIST_COPYRIGHT,SCALEDDOWN_PROGRAM_LIST_DUR,SCALEDDOWN_PROGRAM_LIST_DEMONG,SCALEDDOWN_PROGRAM_LIST_AVAIL_STATUS,SCALEDDOWN_PROGRAM_LIST_DELIVERY,
            SCALEDDOWN_PROGRAM_LIST_R_VALUE,SCALEDDOWN_PROGRAM_LIST_MAIN_GENRE,SCALEDDOWN_PROGRAM_LIST_SECOND_GEBRE_ARRAY,SCALEDDOWN_PROGRAM_LIST_SYNOP,SCALEDDOWN_PROGRAM_LIST_CREDITS,SCALEDDOWN_PROGRAM_LIST_CAPL,
            SCALEDDOWN_PROGRAM_LIST_COPY,SCALEDDOWN_PROGRAM_LIST_ADINFO,SCALEDDOWN_PROGRAM_LIST_BILINGAL,SCALEDDOWN_PROGRAM_LIST_LIVE,SCALEDDOWN_PROGRAM_LIST_FIRST_JAPAN,SCALEDDOWN_PROGRAM_LIST_FIRST_TV,SCALEDDOWN_PROGRAM_LIST_EXCLUSIVE,
            SCALEDDOWN_PROGRAM_LIST_PRE,SCALEDDOWN_PROGRAM_LIST_FIRST_CH,SCALEDDOWN_PROGRAM_LIST_ORIGINAL,SCALEDDOWN_PROGRAM_LIST_MASK,SCALEDDOWN_PROGRAM_LIST_NONSCRAMBLE,SCALEDDOWN_PROGRAM_LIST_DOWNLOAD,SCALEDDOWN_PROGRAM_LIST_STARTOVER,
            SCALEDDOWN_PROGRAM_LIST_STAMP,SCALEDDOWN_PROGRAM_LIST_RELATIONAL_ID_ARRAY};


    public List<ScaledDownProgramList> ScaledDownProgramListSender(String jsonStr) {

        mScaledDownProgramList = new ScaledDownProgramList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            sendStatus(jsonObj);
            sendVcList(jsonObj);

            List<ScaledDownProgramList> vodClipList = Arrays.asList(mScaledDownProgramList);

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
            if (!jsonObj.isNull(SCALEDDOWN_PROGRAM_LIST_STATUS)) {
                String status = jsonObj.getString(SCALEDDOWN_PROGRAM_LIST_STATUS);
                map.put(SCALEDDOWN_PROGRAM_LIST_STATUS, status);
            }

            /* 2017/10/30日実装予定 */

            mScaledDownProgramList.setVcMap(map);

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
            if (!jsonObj.isNull(SCALEDDOWN_PROGRAM_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(SCALEDDOWN_PROGRAM_LIST);

                // リストの数だけまわす
                for (int i = 0; i<jsonArr.length(); i++){
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    /* 2017/10/30日実装予定 */

                    for (int j = 0; j < listPara.length; j++){
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j].equals(SCALEDDOWN_PROGRAM_LIST_SECOND_GEBRE_ARRAY)) {
                                JSONArray para = jsonObject.getJSONArray(listPara[j]);
                                vcListMap.put(listPara[j], para.toString());
                            }else if(listPara[j].equals(SCALEDDOWN_PROGRAM_LIST_CREDITS)){
                                JSONArray para = jsonObject.getJSONArray(listPara[j]);
                                vcListMap.put(listPara[j], para.toString());
                            }else if(listPara[j].equals(SCALEDDOWN_PROGRAM_LIST_RELATIONAL_ID_ARRAY)){
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
                mScaledDownProgramList.setVcList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}