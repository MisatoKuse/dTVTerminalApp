/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserInfoJsonParser {
    // オブジェクトクラスの定義
    private UserInfoList mUserInfoList;

    private static final String USER_INFO_LIST_STATUS = "status";

    public static final String USER_INFO_LIST_LOGGEDIN_ACCOUNT = "loggedin_account";
    public static final String USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT = "h4d_contracted_account";
    public static final String USER_INFO_LIST_CONTRACT_STATUS = "contract_status";
    public static final String USER_INFO_LIST_DCH_AGE_REQ = "dch_age_req";
    public static final String USER_INFO_LIST_H4D_AGE_REQ = "h4d_age_req";

    private static final String[] listPara = {USER_INFO_LIST_CONTRACT_STATUS, USER_INFO_LIST_DCH_AGE_REQ,
            USER_INFO_LIST_H4D_AGE_REQ};

    /**
     * ユーザ情報Jsonデータ解析
     *
     * @param jsonStr ユーザ情報情報一覧
     * @return userInfoList
     */
    public List<UserInfoList> userInfoListSender(String jsonStr) {

        mUserInfoList = new UserInfoList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj != null) {
                HashMap<String, String> map = new HashMap<>();
                if (!jsonObj.isNull(USER_INFO_LIST_STATUS)) {
                    String status = jsonObj.getString(USER_INFO_LIST_STATUS);
                    map.put(USER_INFO_LIST_STATUS, status);
                }
                mUserInfoList.setUiMap(map);

                if (!jsonObj.isNull(USER_INFO_LIST_LOGGEDIN_ACCOUNT)) {
                    JSONObject loggedinObj = jsonObj.getJSONObject(USER_INFO_LIST_LOGGEDIN_ACCOUNT);
                    mUserInfoList.setLoggedinAccountList(sendUiList(loggedinObj));

                    if (!loggedinObj.isNull(USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT)) {
                        JSONObject h4dObj = loggedinObj.getJSONObject(USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT);
                        mUserInfoList.setH4dAccountList(sendUiList(h4dObj));
                    }
                }

                List<UserInfoList> userInfoList = Arrays.asList(mUserInfoList);

                return userInfoList;
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * ユーザ情報をList<HashMap>でオブジェクトクラスに格納
     *
     * @param jsonObj
     * @return ユーザ情報List
     */
    private List sendUiList(JSONObject jsonObj) {
        List<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        try {
            for (int i = 0; i < listPara.length; i++) {
                if (!jsonObj.isNull(listPara[i])) {
                    if (!jsonObj.isNull(listPara[i])) {
                        String para = jsonObj.getString(listPara[i]);
                        map.put(listPara[i], para);
                    }
                }
                list.add(map);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return list;
    }
}
