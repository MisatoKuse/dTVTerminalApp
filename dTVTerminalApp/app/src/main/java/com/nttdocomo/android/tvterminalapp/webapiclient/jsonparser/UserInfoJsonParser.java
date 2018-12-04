/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.UserInfoList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.userinfolist.AccountList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.UserInfoWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ユーザー情報用JsonParserクラス.
 */
public class UserInfoJsonParser extends AsyncTask<Object, Object, Object> {
    /**ステータス.*/
    private static final String USER_INFO_LIST_STATUS = "status";
    /**r登録されてるアカウント.*/
    public static final String USER_INFO_LIST_LOGGEDIN_ACCOUNT = "loggedin_account";
    /**h4d契約アカウント.*/
    private static final String USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT = "h4d_contracted_account";
    /**契約ステータス.*/
    public static final String USER_INFO_LIST_CONTRACT_STATUS = "contract_status";
    /**dch視聴年齢値キー.*/
    public static final String USER_INFO_LIST_DCH_AGE_REQ = "dch_age_req";
    /**h4d視聴年齢値キー.*/
    public static final String USER_INFO_LIST_H4D_AGE_REQ = "h4d_age_req";
    /**ユーザー情報更新タイム.*/
    public static final String USER_INFO_LIST_UPDATE_TIME = "update_time";
    /**エリアコード.*/
    public static final String USER_INFO_LIST_AREA_CODE = "area_code";
    /**bracket_left.*/
    private static final String BRACKET_LEFT = "[";
    /**bracket_right.*/
    private static final String BRACKET_RIGHT = "]";
    /**callback.*/
    private final UserInfoWebClient.UserInfoJsonParserCallback mUserInfoJsonParserCallback;
    /**ユーザ情報.*/
    private List<UserInfoList> mUserInfoListResponse;

    /**
     * コンストラクタ.
     *@param userInfoJsonParserCallback  userInfoJsonParserCallback
     */
    public UserInfoJsonParser(final UserInfoWebClient.UserInfoJsonParserCallback userInfoJsonParserCallback) {
        mUserInfoJsonParserCallback = userInfoJsonParserCallback;
        mUserInfoListResponse = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(final Object userInfoList) {
        if (userInfoList != null && userInfoList instanceof List) {
            mUserInfoListResponse = (List<UserInfoList>) userInfoList;
            mUserInfoJsonParserCallback.onUserInfoJsonParsed(
                    mUserInfoListResponse);
        } else {
            // データが無いのでヌルを返す
            mUserInfoJsonParserCallback.onUserInfoJsonParsed(null);
        }
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];

        return userInfoListSender(result);
    }

    /**
     * ユーザ情報Jsonデータ解析.
     *
     * @param jsonStr ユーザ情報情報一覧
     * @return userInfoList
     */
    private List<UserInfoList> userInfoListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        // オブジェクトクラスの定義
        UserInfoList infoList;

        infoList = new UserInfoList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj != null) {
                //ステータスの取得
                if (!jsonObj.isNull(USER_INFO_LIST_STATUS)) {
                    String status = jsonObj.getString(USER_INFO_LIST_STATUS);
                    infoList.setStatus(status);
                }

                //リクエストユーザデータの取得
                ArrayList<AccountList> loggedinAccount = new ArrayList<>();

                if (!jsonObj.isNull(USER_INFO_LIST_LOGGEDIN_ACCOUNT)) {
                    Object arryCheck = jsonObj.getJSONObject(USER_INFO_LIST_LOGGEDIN_ACCOUNT);
                    JSONArray sendData;
                    if (arryCheck instanceof JSONArray) {
                        //配列だったので、そのまま使用する
                        sendData = (JSONArray) arryCheck;
                    } else {
                        //前後に大かっこを足して配列化
                        sendData = new JSONArray(makeJsonArray(arryCheck));
                    }

                    getDataArray(loggedinAccount, sendData);
                }

                //こちらのデータは必須なので、データーが1件もない場合は、戻り値ヌルで帰る
                if (loggedinAccount.size() == 0) {
                    return null;
                }

                infoList.setLoggedinAccount(loggedinAccount);

                //H4D契約ユーザデータの取得
                ArrayList<AccountList> h4dContractedAccount = new ArrayList<>();

                if (!jsonObj.isNull(USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT)) {
                    Object arryCheck = jsonObj.getJSONObject(
                            USER_INFO_LIST_H4D_CONTRACTED_ACCOUNT);
                    JSONArray sendData;
                    if (arryCheck instanceof JSONArray) {
                        //配列だったので、そのまま使用する
                        sendData = (JSONArray) arryCheck;
                    } else {
                        //前後に大かっこを足して配列化
                        sendData = new JSONArray(makeJsonArray(arryCheck));
                    }

                    getDataArray(h4dContractedAccount, sendData);
                }

                infoList.setH4dContractedAccount(h4dContractedAccount);

                return Arrays.asList(infoList);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }

        return null;
    }

    /**
     * JSONの前後に括弧を配置して配列に変換する.
     *
     * @param source 元のJSON
     * @return 変換後のJSON
     */
    private String makeJsonArray(final Object source) {
        //インスペクターはStringBuilderではなく+演算子での文字結合を推奨してくるが、禁止である。
        StringBuilder tempBuffer = new StringBuilder();
        tempBuffer.append(BRACKET_LEFT);
        tempBuffer.append(((JSONObject) source).toString());
        tempBuffer.append(BRACKET_RIGHT);

        return tempBuffer.toString();
    }

    /**
     * 取得した契約情報を蓄積する.
     *
     * @param loggedinAccount 蓄積用データリスト
     * @param loggedinArray   蓄積対象のリクエストユーザデータ又はH4D契約ユーザデータ
     */
    private void getDataArray(final List<AccountList> loggedinAccount, final JSONArray loggedinArray) {

        AccountList tempList = new AccountList();
        String temp;
        for (int count = 0; count < loggedinArray.length(); count++) {
            try {
                JSONObject loggedinObj = loggedinArray.getJSONObject(count);

                temp = loggedinObj.getString(USER_INFO_LIST_CONTRACT_STATUS);
                tempList.setContractStatus(temp);
                temp = loggedinObj.getString(USER_INFO_LIST_DCH_AGE_REQ);
                tempList.setDchAgeReq(temp);

                //この項目は省略される場合がある
                if (loggedinObj.has(USER_INFO_LIST_H4D_AGE_REQ)) {
                    temp = loggedinObj.getString(USER_INFO_LIST_H4D_AGE_REQ);
                    tempList.setH4dAgeReq(temp);
                } else {
                    //省略された場合は空文字
                    tempList.setH4dAgeReq("");
                }

                //エリアコードは返却されない場合がある
                if (loggedinObj.has(USER_INFO_LIST_AREA_CODE)) {
                    temp = loggedinObj.getString(USER_INFO_LIST_AREA_CODE);
                    tempList.setAreaCode(temp);
                } else {
                    //返却されない場合は空文字
                    tempList.setAreaCode("");
                }

                loggedinAccount.add(tempList);
            } catch (JSONException e) {
                //パースに失敗した場合は次のデータに行くので何もしない
                DTVTLogger.debug(e);
            }
        }
    }
}