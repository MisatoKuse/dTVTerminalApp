/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * TVクリップ用JsonParserクラス.
 */
public class TvClipJsonParser {

    /**オブジェクトクラスの定義.*/
    private TvClipList mTvClipList;
    /**ページャーパラメータキー.*/
    private static final String[] PAGER_PARA = {JsonConstants.META_RESPONSE_UPPER_LIMIT,
            JsonConstants.META_RESPONSE_LOWER_LIMIT, JsonConstants.META_RESPONSE_OFFSET,
            JsonConstants.META_RESPONSE_COUNT};

    /**
     * TVクリップデータ解析.
     * @param jsonStr JSONデータ文字列
     * @return パース後のJSONデータ
     */
    public List<TvClipList> tvClipListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mTvClipList = new TvClipList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            sendStatus(jsonObj);
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                sendVcList(arrayList);
            }
            List<TvClipList> tvClipList = Arrays.asList(mTvClipList);
            return tvClipList;
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに渡す.
     * @param jsonObj APIレスポンス Jsonデータ
     */
    private void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<String, String>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonConstants.META_RESPONSE_STATUS);
                map.put(JsonConstants.META_RESPONSE_STATUS, status);
            }
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonConstants.META_RESPONSE_PAGER);

                for (String pagerBuffer : PAGER_PARA) {
                    if (!pager.isNull(pagerBuffer)) {
                        String para = pager.getString(pagerBuffer);
                        map.put(pagerBuffer, para);
                    }
                }
            }
            if (mTvClipList != null) {
                mTvClipList.setVcMap(map);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納.
     * @param arrayList ArrayList
     */
    private void sendVcList(final JSONArray arrayList) {
        try {
            List<HashMap<String, String>> tcList = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> tcListMap = new HashMap<>();
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : JsonConstants.LIST_PARA) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonConstants.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(listBuffer);
                            for (String puinfBuffer : JsonConstants.PUINF_PARA) {
                                //書き込み用項目名の作成
                                StringBuilder stringBuffer = new StringBuilder();
                                stringBuffer.append(JsonConstants.META_RESPONSE_PUINF);
                                stringBuffer.append(JsonConstants.UNDER_LINE);
                                stringBuffer.append(puinfBuffer);
                                String para = puinfObj.getString(puinfBuffer);
                                tcListMap.put(stringBuffer.toString(), para);
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
            DTVTLogger.debug(e);
        }
    }
}