/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInfoJsonParser extends AsyncTask<Object, Object, Object> {

    private static final String USER_INFO_LIST_STATUS = "status";

    public static final String USER_INFO_LIST_LOGGEDIN_ACCOUNT = "loggedin_account";
    public static final String USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT = "h4d_contracted_account";
    public static final String USER_INFO_LIST_CONTRACT_STATUS = "contract_status";
    public static final String USER_INFO_LIST_DCH_AGE_REQ = "dch_age_req";
    public static final String USER_INFO_LIST_H4D_AGE_REQ = "h4d_age_req";
    public static final String USER_INFO_LIST_UPDATE_TIME = "update_time";
    private static final String BRACKET_LEFT = "[";
    private static final String BRACKET_RIGHT = "]";

    private UserInfoWebClient.UserInfoJsonParserCallback mUserInfoJsonParserCallback;

    private List<UserInfoList> mUserInfoListResponse;

    /**
     * コンストラクタ
     */
    public UserInfoJsonParser(UserInfoWebClient.UserInfoJsonParserCallback userInfoJsonParserCallback) {
        mUserInfoJsonParserCallback = userInfoJsonParserCallback;
        mUserInfoListResponse = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(Object s) {
        if(s != null && s instanceof List) {
            mUserInfoListResponse = (List<UserInfoList>) s;
            mUserInfoJsonParserCallback.onUserInfoJsonParsed(
                    mUserInfoListResponse);
        }
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];

        //TODO:仮データ・後で消す事
        result = "{\n" +
                "  \"status\": \"OK\",\n" +
                "  \"loggedin_account\": {\n" +
                "    \"contract_status\": \"002\",\n" +
                "    \"dch_age_req\": \"9\",\n" +
                "    \"h4d_age_req\": \"17\"\n" +
                "  },\n" +
                "  \"h4d_contracted_account\": {\n" +
                "    \"contract_status\": \"002\",\n" +
                "    \"dch_age_req\": \"12\",\n" +
                "    \"h4d_age_req\": \"17\"\n" +
                "  }\n" +
                "}";

        return userInfoListSender(result);
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
                //ステータスの取得
                if (!jsonObj.isNull(USER_INFO_LIST_STATUS)) {
                    String status = jsonObj.getString(USER_INFO_LIST_STATUS);
                    infoList.setmStatus(status);
                }

                //リクエストユーザデータの取得
                ArrayList<UserInfoList.AccountList> loggedinAccount = new ArrayList<>();

                if (!jsonObj.isNull(USER_INFO_LIST_LOGGEDIN_ACCOUNT)) {
                    Object arryCheck = jsonObj.getJSONObject(USER_INFO_LIST_LOGGEDIN_ACCOUNT);
                    JSONArray sendData;
                    if (arryCheck instanceof JSONArray) {
                        //配列だったので、配列にする
                        sendData = jsonObj.getJSONArray(USER_INFO_LIST_LOGGEDIN_ACCOUNT);
                    } else {
                        //前後に大かっこを足して配列化
                        StringBuilder tempBuffer = new StringBuilder();
                        tempBuffer.append(BRACKET_LEFT);
                        tempBuffer.append(((JSONObject) arryCheck).toString());
                        tempBuffer.append(BRACKET_RIGHT);
                        sendData = new JSONArray(tempBuffer.toString());
                    }

                    getDataArray(loggedinAccount, sendData);
                }

                infoList.setmLoggedinAccount(loggedinAccount);


                //H4D契約ユーザデータの取得
                ArrayList<UserInfoList.AccountList> h4dContractedAccount = new ArrayList<>();

                if (!jsonObj.isNull(USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT)) {
                    Object arryCheck = jsonObj.getJSONObject(
                            USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT);
                    JSONArray sendData;
                    if (arryCheck instanceof JSONArray) {
                        //配列だったので、配列にする
                        sendData = jsonObj.getJSONArray(USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT);
                    } else {
                        //前後に大かっこを足して配列化
                        StringBuilder tempBuffer = new StringBuilder();
                        tempBuffer.append(BRACKET_LEFT);
                        tempBuffer.append(((JSONObject) arryCheck).toString());
                        tempBuffer.append(BRACKET_RIGHT);
                        sendData = new JSONArray(tempBuffer.toString());
                    }

                    getDataArray(h4dContractedAccount, sendData);
                }

                infoList.setmH4dContractedAccount(h4dContractedAccount);

                return Arrays.asList(infoList);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }

        return null;
    }

    /**
     * 取得した契約情報を蓄積する
     *
     * @param loggedinAccount 蓄積用データリスト
     * @param loggedinArray   蓄積対象のリクエストユーザデータ又はH4D契約ユーザデータ
     */
    void getDataArray(List<UserInfoList.AccountList> loggedinAccount, JSONArray loggedinArray) {

        UserInfoList.AccountList tempList = new UserInfoList.AccountList();
        String temp;
        for (int count = 0; count < loggedinArray.length(); count++) {
            try {
                JSONObject loggedinObj = loggedinArray.getJSONObject(count);

                temp = loggedinObj.getString(USER_INFO_LIST_CONTRACT_STATUS);
                tempList.setmContractStatus(temp);
                temp = loggedinObj.getString(USER_INFO_LIST_DCH_AGE_REQ);
                tempList.setmDchAgeReq(temp);

                //この項目は省略される場合がある
                if(loggedinObj.has(USER_INFO_LIST_H4D_AGE_REQ)) {
                    temp = loggedinObj.getString(USER_INFO_LIST_H4D_AGE_REQ);
                    tempList.setmH4dAgeReq(temp);
                } else {
                    //省略された場合は空文字
                    tempList.setmH4dAgeReq("");
                }
                loggedinAccount.add(tempList);
            } catch (JSONException e) {
                //パースに失敗した場合は次のデータに行くので何もしない
            }
        }
    }
}