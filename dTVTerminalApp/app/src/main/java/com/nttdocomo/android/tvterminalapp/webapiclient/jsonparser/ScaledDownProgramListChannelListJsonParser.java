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

    public static final String SCALEDDOWN_PROGRAM_LIST_STATUS = "status";
    public static final String SCALEDDOWN_PROGRAM_LIST = "list";


    public List<ScaledDownProgramChannelList> ScaledDownProgramListSender(String jsonStr) {

        mScaledDownProgramChannelList = new ScaledDownProgramChannelList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<ScaledDownProgramChannelList> vodClipList = Arrays.asList(mScaledDownProgramChannelList);

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
            if (!jsonObj.isNull(SCALEDDOWN_PROGRAM_LIST_STATUS)) {
                String status = jsonObj.getString(SCALEDDOWN_PROGRAM_LIST_STATUS);
                map.put(SCALEDDOWN_PROGRAM_LIST_STATUS, status);
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
    public void sendVcList(JSONObject jsonObj) {
        try {
            if (!jsonObj.isNull(SCALEDDOWN_PROGRAM_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(SCALEDDOWN_PROGRAM_LIST);

                // リストの数だけまわす
                for (int i = 0; i<jsonArr.length(); i++){
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<String, String>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

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