/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RoleListWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoleListJsonParser extends AsyncTask<Object, Object, Object> {
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SEND_RESPONSE = ".sendRoleListResponse";
    private static final String RESPONSE = ". RoleListResponse";
    private static final String JSON_OBJECT = ".JSONObject";

    private RoleListWebClient.RoleListJsonParserCallback
            mRoleListJsonParserCallback;
    // オブジェクトクラスの定義　
    private RoleListResponse mRoleListResponse;

    /**
     * コンストラクタ
     * <p>
     * //     * @param genreListJsonParserCallback
     */
    public RoleListJsonParser(RoleListWebClient.RoleListJsonParserCallback roleListJsonParserCallback) {
        mRoleListJsonParserCallback = roleListJsonParserCallback;
        mRoleListResponse = new RoleListResponse();
    }

    @Override
    protected void onPostExecute(Object s) {
        mRoleListJsonParserCallback.
                onRoleListJsonParsed(mRoleListResponse);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        RoleListResponse response = roleListSender(result);
        return response;
    }


    /**
     * ジャンル一覧Jsonデータを解析する
     *
     * @param jsonStr ジジャンル一覧Jsonデータ
     * @return ジャンル一覧取得：正常時レスポンスデータ
     */
    public RoleListResponse roleListSender(String jsonStr) {
        mRoleListResponse = new RoleListResponse();
        try {
            if (jsonStr != null) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                if (jsonArray != null) {
                    sendRoleListResponse(jsonArray);
                    return mRoleListResponse;
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + JSON_OBJECT, e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + RESPONSE, e);
        }
        return null;
    }

    /**
     * ジャンル一覧のデータをジャンル一覧取得：正常時レスポンスデータオブジェクトに格納
     *
     * @param jsonArray APIレスポンス Jsonデータ
     */
    public void sendRoleListResponse(JSONArray jsonArray) {
        String id = "";
        String name = "";
        try {
            ArrayList<RoleListMetaData> roleListMetaData = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                RoleListMetaData roleMetaData = new RoleListMetaData();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (!jsonObject.isNull(JsonContents.META_RESPONSE_CONTENTS_ID)) {
                    id = jsonObject.getString(JsonContents.META_RESPONSE_CONTENTS_ID);
                }
                if (!jsonObject.isNull(JsonContents.META_RESPONSE_CONTENTS_NAME)) {
                    name = jsonObject.getString(JsonContents.META_RESPONSE_CONTENTS_NAME);
                }
                roleMetaData.setId(id);
                roleMetaData.setName(name);
                roleListMetaData.add(roleMetaData);
            }
            mRoleListResponse.setRoleList(roleListMetaData);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(CLASS_NAME + SEND_RESPONSE, e);
        }
    }
}