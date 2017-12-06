/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WatchListenVideoListJsonParser {

    // オブジェクトクラスの定義
    private WatchListenVideoList mWatchListenVideoList;

    private static final String[] PAGER_PARA = {JsonContents.META_RESPONSE_UPPER_LIMIT,
            JsonContents.META_RESPONSE_LOWER_LIMIT, JsonContents.META_RESPONSE_OFFSET,
            JsonContents.META_RESPONSE_COUNT};

    public List<WatchListenVideoList> watchListenVideoListSender(String jsonStr) {
        mWatchListenVideoList = new WatchListenVideoList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            sendStatus(jsonObj);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_METADATE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonContents.META_RESPONSE_METADATE_LIST);
                sendVcList(arrayList);
            }
            return Arrays.asList(mWatchListenVideoList);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
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
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);
                map.put(JsonContents.META_RESPONSE_STATUS, status);
            }

            if (!jsonObj.isNull(JsonContents.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonContents.META_RESPONSE_PAGER);

                for (String pagerBuffer : PAGER_PARA){
                    if (!pager.isNull(pagerBuffer)) {
                        String para = pager.getString(pagerBuffer);
                        map.put(pagerBuffer, para);
                    }
                }
            }
            if (mWatchListenVideoList != null) {
                mWatchListenVideoList.setVcMap(map);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }

    /*
    * コンテンツのList<HashMap>をオブジェクトクラスに格納
     */
    public void sendVcList(JSONArray arrayList) {
        try {
            List<HashMap<String, String>> vcList = new ArrayList<>();
            // リストの数だけまわす
            for (int i = 0; i<arrayList.length(); i++) {
                // 最初にHashMapを生成＆初期化
                HashMap<String, String> vcListMap = new HashMap<>();
                // i番目のJSONArrayをJSONObjectに変換する
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : JsonContents.LIST_PARA) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonContents.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(listBuffer);
                            for (String puinfBuffer : JsonContents.PUINF_PARA) {
                                String para = puinfObj.getString(puinfBuffer);
                                vcListMap.put(JsonContents.META_RESPONSE_PUINF + JsonContents.UNDER_LINE + puinfBuffer, para);
                            }
                        } else {
                            String para = jsonObject.getString(listBuffer);
                            vcListMap.put(listBuffer, para);
                        }
                    }
                }
                // i番目のMapをListにadd
                vcList.add(vcListMap);
            }
            // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
            if (mWatchListenVideoList != null) {
                mWatchListenVideoList.setVcList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }

}