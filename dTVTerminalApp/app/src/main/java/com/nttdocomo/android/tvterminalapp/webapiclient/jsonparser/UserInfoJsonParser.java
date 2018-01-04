/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserInfoJsonParser extends AsyncTask<String, Object, Integer> {

    private static final String USER_INFO_LIST_STATUS = "status";

    public static final String USER_INFO_LIST_LOGGEDIN_ACCOUNT = "loggedin_account";
    public static final String USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT = "h4d_contracted_account";
    public static final String USER_INFO_LIST_CONTRACT_STATUS = "contract_status";
    public static final String USER_INFO_LIST_DCH_AGE_REQ = "dch_age_req";
    public static final String USER_INFO_LIST_H4D_AGE_REQ = "h4d_age_req";
    public static final int USE_NONE_AGE_REQ = 8;
    private UserInfoWebClient.UserInfoJsonParserCallback userInfoJsonParserCallback = null;

    private static final String[] listPara = {USER_INFO_LIST_CONTRACT_STATUS, USER_INFO_LIST_DCH_AGE_REQ,
            USER_INFO_LIST_H4D_AGE_REQ};

    public UserInfoJsonParser(UserInfoWebClient.UserInfoJsonParserCallback callback) {
        userInfoJsonParserCallback = callback;
    }

    /**
     * ユーザ情報Jsonデータ解析
     *
     * @param jsonStr ユーザ情報情報一覧
     * @return userInfoList
     */
    public int userInfoListSender(String jsonStr) {

        // オブジェクトクラスの定義
        List<HashMap<String, String>> userStateMapList = new ArrayList<>();

        //Jsonデータがないときはデフォルト値(PG12)を返却する
        if(jsonStr == null || jsonStr.length() < 1){
            return USE_NONE_AGE_REQ;
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.length() > 0) {
                if (!jsonObj.isNull(USER_INFO_LIST_STATUS)) {
                    String status = jsonObj.getString(USER_INFO_LIST_STATUS);
                    if(!status.equals(JsonContents.META_RESPONSE_STATUS_OK)){
                        getUserInfoFailure();
                        return USE_NONE_AGE_REQ;
                    }
                }

                if (!jsonObj.isNull(USER_INFO_LIST_LOGGEDIN_ACCOUNT)) {
                    JSONObject loggedInObj = jsonObj.getJSONObject(USER_INFO_LIST_LOGGEDIN_ACCOUNT);
                    userStateMapList.add(sendUiList(loggedInObj));
                } else {
                    return USE_NONE_AGE_REQ;
                }

                if (!jsonObj.isNull(USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT)) {
                    JSONObject h4dObj = jsonObj.getJSONObject(USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT);
                    userStateMapList.add(sendUiList(h4dObj));
                } else {
                    return USE_NONE_AGE_REQ;
                }

                return getUserInfo(userStateMapList);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }

        return USE_NONE_AGE_REQ;
    }

    /**
     * ユーザ情報取得失敗
     */
    private void getUserInfoFailure(){
        userInfoJsonParserCallback.getUserInfoFailure();
    }

    /**
     * ユーザ情報をList<HashMap>でオブジェクトクラスに格納
     *
     * @param jsonObj
     * @return ユーザ情報List
     */
    private HashMap<String, String> sendUiList(JSONObject jsonObj) {
        HashMap<String, String> map = new HashMap<>();
        try {
            for (String aListPara : listPara) {
                if (!jsonObj.isNull(aListPara)) {
                    if (!jsonObj.isNull(aListPara)) {
                        String para = jsonObj.getString(aListPara);
                        map.put(aListPara, para);
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return map;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String jsonStr = null;
        if (strings != null && strings[0] != null) {
            jsonStr = strings[0];
        }
        return userInfoListSender(jsonStr);
    }

    @Override
    protected void onPostExecute(Integer age) {
        userInfoJsonParserCallback.getUserInfoResult(age);
    }

    /**
     * ユーザ年齢情報を取得する
     *
     * @param userInfoMapList ユーザアカウント情報
     * @return 年齢情報
     */
    private int getUserInfo(List<HashMap<String, String>> userInfoMapList) {
        final int INT_LOGGED_IN_ACCOUNT_LIST = 0;
        final String USE_H4D_AGE_REQ = "001";
        final String USE_DCH_AGE_REQ = "002";
        String age = null;
        String contractStatus = null;
        int intAge = 8;
        HashMap<String, String> mLoggedInAccount;
//        UserInfoList infoList = userInfoList.get(INT_LIST_HEAD);
//        List<HashMap<String, String>> mLoggedInAccountList = infoList.getLoggedinAccountList();

        if (userInfoMapList != null && userInfoMapList.size() > 0) {
            mLoggedInAccount = userInfoMapList.get(INT_LOGGED_IN_ACCOUNT_LIST);
        } else {
            return intAge;
        }

        if (mLoggedInAccount != null && mLoggedInAccount.size() > 0) {
            contractStatus = mLoggedInAccount.get(UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS);
        }

        //contractStatusがないときはPG12制限を設定
        if (contractStatus != null) {
            if (contractStatus.equals(USE_H4D_AGE_REQ)) {
                //H4Dの制限情報がないときはDCH側を使用
                age = mLoggedInAccount.get(UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ);
                if (age == null || age.length() < 1) {
                    age = mLoggedInAccount.get(UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ);
                }
            } else if (contractStatus.equals(USE_DCH_AGE_REQ)) {
                //DCHの制限情報がないときはH4DDCH側を使用
                age = mLoggedInAccount.get(UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ);
                if (age == null || age.length() < 1) {
                    age = mLoggedInAccount.get(UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ);
                }
            }
        }
        if(DBUtils.isNumber(age)){
            intAge = Integer.parseInt(age);
        }
        return intAge;
    }
}