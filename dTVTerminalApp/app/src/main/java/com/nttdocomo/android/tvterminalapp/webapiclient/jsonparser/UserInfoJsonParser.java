/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserInfoJsonParser extends AsyncTask<String, Object, List<UserInfoList>> {

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
    public List<UserInfoList> userInfoListSender(String jsonStr) {

        // オブジェクトクラスの定義
        UserInfoList infoList;

        infoList = new UserInfoList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj != null) {
                HashMap<String, String> map = new HashMap<>();
                if (!jsonObj.isNull(USER_INFO_LIST_STATUS)) {
                    String status = jsonObj.getString(USER_INFO_LIST_STATUS);
                    map.put(USER_INFO_LIST_STATUS, status);
                }
                infoList.setUiMap(map);

                if (!jsonObj.isNull(USER_INFO_LIST_LOGGEDIN_ACCOUNT)) {
                    JSONObject loggedinObj = jsonObj.getJSONObject(USER_INFO_LIST_LOGGEDIN_ACCOUNT);
                    infoList.setLoggedinAccountList(sendUiList(loggedinObj));
                }

                if (!jsonObj.isNull(USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT)) {
                    JSONObject h4dObj = jsonObj.getJSONObject(USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT);
                    infoList.setH4dAccountList(sendUiList(h4dObj));
                }

                List<UserInfoList> userInfoList = Arrays.asList(infoList);

                return userInfoList;
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
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
            }
            list.add(map);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return list;
    }

    @Override
    protected List<UserInfoList> doInBackground(String... strings) {
        String jsonStr = null;
        if (strings != null && strings[0] != null) {
            jsonStr = strings[0];
        }
        return userInfoListSender(jsonStr);
    }

    @Override
    protected void onPostExecute(List<UserInfoList> list) {
        if (list != null && list.size() > 0) {
            int age = getUserInfo(list);
            userInfoJsonParserCallback.getUserInfoResult(age);
        } else {
            userInfoJsonParserCallback.getUserInfoFailure();
        }
    }

    /**
     * ユーザ年齢情報を取得する
     *
     * @param userInfoList ユーザアカウント情報
     * @return 年齢情報
     */
    private int getUserInfo(List<UserInfoList> userInfoList){
        final int INT_LIST_HEAD = 0;
        final String USE_H4D_AGE_REQ = "001";
        final String USE_DCH_AGE_REQ = "002";
        String age = null;
        String contractStatus = null;
        UserInfoList infoList = userInfoList.get(INT_LIST_HEAD);
        List<HashMap<String, String>> mLoggedInAccountList = infoList.getLoggedinAccountList();
        HashMap<String, String> mLoggedInAccount = mLoggedInAccountList.get(INT_LIST_HEAD);
        if (mLoggedInAccount != null && mLoggedInAccount.size() > 0) {
            contractStatus = mLoggedInAccount.get(UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS);
        }

        //contractStatusがないときはPG12制限を設定
        int intAge = 8;
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