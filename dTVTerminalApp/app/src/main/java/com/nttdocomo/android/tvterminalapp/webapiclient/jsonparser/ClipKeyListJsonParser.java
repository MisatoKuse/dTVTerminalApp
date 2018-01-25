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

public class ClipKeyListJsonParser {

    // オブジェクトクラスの定義
    private ClipKeyListResponse mClipKeyListResponse;

    public static final String[] LIST_PARAM = {JsonConstants.META_RESPONSE_CRID,
            JsonConstants.META_RESPONSE_SERVICE_ID, JsonConstants.META_RESPONSE_EVENT_ID,
            JsonConstants.META_RESPONSE_TYPE, JsonConstants.META_RESPONSE_TITLE_ID};

    public ClipKeyListResponse clipKeyListSender(String jsonStr) {
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
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return null;
    }

    public void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                mClipKeyListResponse.setStatus(status);
            }
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_IS_UPDATE)) {
                boolean isUpdate = jsonObj.getBoolean(JsonConstants.META_RESPONSE_IS_UPDATE);
                mClipKeyListResponse.setIsUpdate(isUpdate);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納
     */
    public void sendVcList(JSONArray arrayList) {
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
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}