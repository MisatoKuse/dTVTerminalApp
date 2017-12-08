/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TvClipJsonParser {

    // オブジェクトクラスの定義
    private TvClipList mTvClipList;

    public static final String[] PAGER_PARA = {JsonContents.META_RESPONSE_UPPER_LIMIT,
            JsonContents.META_RESPONSE_LOWER_LIMIT, JsonContents.META_RESPONSE_OFFSET,
            JsonContents.META_RESPONSE_COUNT};

    public List<TvClipList> tvClipListSender(String jsonStr) {
        mTvClipList = new TvClipList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            sendStatus(jsonObj);
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonContents.META_RESPONSE_LIST);
                sendVcList(arrayList);
            }
            List<TvClipList> tvClipList = Arrays.asList(mTvClipList);
            return tvClipList;
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
            HashMap<String, String> map = new HashMap<String, String>();
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);
                map.put(JsonContents.META_RESPONSE_STATUS, status);
            }
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonContents.META_RESPONSE_PAGER);

                for (String pagerBuffer : PAGER_PARA) {
                    if (!pager.isNull(pagerBuffer)) {
                        String para = pager.getString(pagerBuffer);
                        map.put(pagerBuffer, para);
                    }
                }
            }
            mTvClipList.setVcMap(map);
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
            List<HashMap<String, String>> tcList = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> tcListMap = new HashMap<>();
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : JsonContents.LIST_PARA) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonContents.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(listBuffer);
                            for (String puinfBuffer : JsonContents.PUINF_PARA) {
                                String para = puinfObj.getString(puinfBuffer);
                                tcListMap.put(JsonContents.META_RESPONSE_PUINF + JsonContents.UNDER_LINE + puinfBuffer, para);
                            }
                        } else {
                            String para = jsonObject.getString(listBuffer);
                            tcListMap.put(listBuffer, para);
                        }
                    }
                }
                tcList.add(tcListMap);
            }

            if (mTvClipList != null) {
                mTvClipList.setVcList(tcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}