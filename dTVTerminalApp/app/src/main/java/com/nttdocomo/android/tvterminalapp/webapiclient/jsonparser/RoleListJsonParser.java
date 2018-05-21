/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;


import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListResponse;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RoleListWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ロール一覧用JsonParserクラス.
 */
public class RoleListJsonParser extends AsyncTask<String, Object, Object> {
    /**callback.*/
    private RoleListWebClient.RoleListJsonParserCallback
            mRoleListJsonParserCallback;
    /**オブジェクトクラスの定義　.*/
    private RoleListResponse mRoleListResponse;

    /**
     * コンストラクタ.
     * <p>
     * @param roleListJsonParserCallback  genreListJsonParserCallback
     */
    public RoleListJsonParser(final RoleListWebClient.RoleListJsonParserCallback roleListJsonParserCallback) {
        mRoleListJsonParserCallback = roleListJsonParserCallback;
        mRoleListResponse = new RoleListResponse();
    }

    @Override
    protected void onPostExecute(final Object s) {
        mRoleListJsonParserCallback.
                onRoleListJsonParsed(mRoleListResponse);
    }

    @Override
    protected Object doInBackground(final String... strings) {
        String result = strings[0];
        RoleListResponse response = roleListSender(result);
        return response;
    }


    /**
     * ジャンル一覧Jsonデータを解析する.
     *
     * @param jsonStr ジジャンル一覧Jsonデータ
     * @return ジャンル一覧取得：正常時レスポンスデータ
     */
    private RoleListResponse roleListSender(final String jsonStr) {

//        DTVTLogger.debugHttp(jsonStr); //jsonを把握したい場合
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
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * ジャンル一覧のデータをジャンル一覧取得：正常時レスポンスデータオブジェクトに格納.
     *
     * @param jsonArray APIレスポンス Jsonデータ
     */
    private void sendRoleListResponse(final JSONArray jsonArray) {
        String id = "";
        String name = "";
        try {
            ArrayList<RoleListMetaData> roleListMetaData = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                RoleListMetaData roleMetaData = new RoleListMetaData();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (!jsonObject.isNull(JsonConstants.META_RESPONSE_CONTENTS_ID)) {
                    id = jsonObject.getString(JsonConstants.META_RESPONSE_CONTENTS_ID);
                }
                if (!jsonObject.isNull(JsonConstants.META_RESPONSE_CONTENTS_NAME)) {
                    name = jsonObject.getString(JsonConstants.META_RESPONSE_CONTENTS_NAME);
                }
                roleMetaData.setId(id);
                roleMetaData.setName(name);
                roleListMetaData.add(roleMetaData);
            }
            mRoleListResponse.setRoleList(roleListMetaData);
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}