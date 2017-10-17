package com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;

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

public class UserInfoJsonParser {
    // オブジェクトクラスの定義
    private UserInfoList mUserInfoList;

    private static final String USER_INFO_LIST_STATUS = "status";

    private static final String USER_INFO_LIST_PAGER = "pager";
    private static final String USER_INFO_LIST_PAGER_UPPER_LIMIT = "upper_limit";
    private static final String USER_INFO_LIST_PAGER_LOWER_LIMIT = "lower_limit";
    private static final String USER_INFO_LIST_PAGER_OFFSET = "offset";
    private static final String USER_INFO_LIST_PAGER_COUNT = "count";

    private static final String USER_INFO_LIST = "list";
    private static final String USER_INFO_LIST_CRID = "crid";
    private static final String USER_INFO_LIST_CID = "cid";
    private static final String USER_INFO_LIST_TITLE_ID = "title_id";
    private static final String USER_INFO_LIST_EPISODE_ID = "episode_id";
    private static final String USER_INFO_LIST_TITLE = "title";
    private static final String USER_INFO_LIST_EPITITLE = "epititle";
    private static final String USER_INFO_LIST_DISP_TYPE = "disp_type";
    private static final String USER_INFO_LIST_DISPLAY_START_DATE = "display_start_date";
    private static final String USER_INFO_LIST_DISPLAY_END_DATE = "display_end_date";
    private static final String USER_INFO_LIST_AVAIL_START_DATE = "avail_start_date";
    private static final String USER_INFO_LIST_AVAIL_END_DATE = "avail_end_date";
    private static final String USER_INFO_LIST_PUBLISH_START_DATE = "publish_start_date";
    private static final String USER_INFO_LIST_PUBLISH_END_DATE = "publish_end_date";
    private static final String USER_INFO_LIST_NEWA_START_DATE = "newa_start_date";
    private static final String USER_INFO_LIST_NEWA_END_DATE = "newa_end_date";
    private static final String USER_INFO_LIST_COPYRIGHT = "copyright";
    private static final String USER_INFO_LIST_THUMB = "thumb";
    private static final String USER_INFO_LIST_DUR = "dur";
    private static final String USER_INFO_LIST_DEMONG = "demong";
    private static final String USER_INFO_LIST_BVFLG = "bvflg";
    private static final String USER_INFO_LIST_4KFLG = "4kflg";
    private static final String USER_INFO_LIST_HDRFLG = "hdrflg";
    private static final String USER_INFO_LIST_AVAIL_STATUS = "avail_status";
    private static final String USER_INFO_LIST_DELIVERY = "delivery";
    private static final String USER_INFO_LIST_R_VALUE = "r_value";
    private static final String USER_INFO_LIST_ADULT = "adult";
    private static final String USER_INFO_LIST_MS = "ms";
    private static final String USER_INFO_LIST_NG_FUNC = "ng_func";
    private static final String USER_INFO_LIST_GENRE_ID_ARRAY = "genre_id_array";
    private static final String USER_INFO_LIST_DTV = "dtv";

    private static final String[] pagerPara = {USER_INFO_LIST_PAGER_UPPER_LIMIT, USER_INFO_LIST_PAGER_LOWER_LIMIT,
            USER_INFO_LIST_PAGER_OFFSET, USER_INFO_LIST_PAGER_COUNT};

    private static final String[] listPara = {USER_INFO_LIST_CRID, USER_INFO_LIST_CID, USER_INFO_LIST_TITLE_ID,
            USER_INFO_LIST_EPISODE_ID, USER_INFO_LIST_TITLE, USER_INFO_LIST_EPITITLE, USER_INFO_LIST_DISP_TYPE,
            USER_INFO_LIST_DISPLAY_START_DATE, USER_INFO_LIST_DISPLAY_END_DATE, USER_INFO_LIST_AVAIL_START_DATE,
            USER_INFO_LIST_AVAIL_END_DATE, USER_INFO_LIST_PUBLISH_START_DATE, USER_INFO_LIST_PUBLISH_END_DATE,
            USER_INFO_LIST_NEWA_START_DATE, USER_INFO_LIST_NEWA_END_DATE, USER_INFO_LIST_COPYRIGHT,
            USER_INFO_LIST_THUMB, USER_INFO_LIST_DUR, USER_INFO_LIST_DEMONG, USER_INFO_LIST_BVFLG, USER_INFO_LIST_4KFLG,
            USER_INFO_LIST_HDRFLG, USER_INFO_LIST_AVAIL_STATUS, USER_INFO_LIST_DELIVERY, USER_INFO_LIST_R_VALUE,
            USER_INFO_LIST_ADULT, USER_INFO_LIST_MS, USER_INFO_LIST_NG_FUNC, USER_INFO_LIST_GENRE_ID_ARRAY, USER_INFO_LIST_DTV};

    /**
     * ユーザ情報Jsonデータ解析
     * @param jsonStr ユーザ情報情報一覧
     * @return userInfoList
     */
    public List<UserInfoList> userInfoListSender(String jsonStr) {

        mUserInfoList = new UserInfoList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<UserInfoList> userInfoList = Arrays.asList(mUserInfoList);

                return userInfoList;
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

    private void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(USER_INFO_LIST_STATUS)) {
                String status = jsonObj.getString(USER_INFO_LIST_STATUS);
                map.put(USER_INFO_LIST_STATUS, status);
            }

            if (!jsonObj.isNull(USER_INFO_LIST_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(USER_INFO_LIST_PAGER);

                for (int i = 0; i < pagerPara.length; i++){
                    if (!pager.isNull(pagerPara[i])) {
                        String para = pager.getString(pagerPara[i]);
                        map.put(pagerPara[i], para);
                    }
                }
            }

            mUserInfoList.setUiMap(map);

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
            if (!jsonObj.isNull(USER_INFO_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(USER_INFO_LIST);

                // リストの数だけまわす
                for (int i = 0; i<jsonArr.length(); i++){
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);

                    for (int j = 0; j < listPara.length; j++){
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j] == USER_INFO_LIST_GENRE_ID_ARRAY) {
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
                mUserInfoList.setUiList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
