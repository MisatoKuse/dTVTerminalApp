/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * クリップキー一覧用JsonParserクラス.
 */
public class ClipKeyListJsonParser {

    /**オブジェクトクラスの定義.*/
    private ClipKeyListResponse mClipKeyListResponse;
    /**クリップキー一覧パラメータキー.*/
    private static final String[] LIST_PARAM = {JsonConstants.META_RESPONSE_CRID,
            JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
            JsonConstants.META_RESPONSE_TITLE_ID};

    /**
     * クリップ一覧Jsonデータを解析する.
     * @param jsonStr クリップ一覧Jsonデータ
     * @return  正常時レスポンスデータ
     */
    public ClipKeyListResponse clipKeyListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mClipKeyListResponse = new ClipKeyListResponse();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            sendStatus(jsonObj);
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                sendVcList(arrayList);
            }
            return mClipKeyListResponse;
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     *  statusの値を正常時レスポンスデータオブジェクトに格納.
     * @param jsonObj 解析前ステータス
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                if (mClipKeyListResponse != null) {
                    mClipKeyListResponse.setStatus(status);
                }
            }
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_IS_UPDATE)) {
                boolean isUpdate = jsonObj.getBoolean(JsonConstants.META_RESPONSE_IS_UPDATE);
                mClipKeyListResponse.setIsUpdate(isUpdate);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納.
     * @param arrayList JSONArray
     */
    private void sendVcList(final JSONArray arrayList) {
        try {
            List<HashMap<String, String>> list = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> listMap = new HashMap<>();
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : LIST_PARAM) {
                    if (!jsonObject.isNull(listBuffer)) {
                        listMap.put(listBuffer, jsonObject.getString(listBuffer));
                    }
                }
                list.add(listMap);
            }

            if (mClipKeyListResponse != null) {
                mClipKeyListResponse.setCkList(list);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}