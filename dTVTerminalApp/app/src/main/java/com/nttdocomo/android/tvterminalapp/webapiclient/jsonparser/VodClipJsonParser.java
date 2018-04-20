/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Vodクリップ一覧用JsonParserクラス.
 */
public class VodClipJsonParser {

    /**オブジェクトクラスの定義.*/
    private VodClipList mVodClipList;
    /**ページャーパラメータキー.*/
    public static final String[] PAGER_PARA = {JsonConstants.META_RESPONSE_UPPER_LIMIT,
            JsonConstants.META_RESPONSE_LOWER_LIMIT, JsonConstants.META_RESPONSE_OFFSET,
            JsonConstants.META_RESPONSE_COUNT};

    /**
     * Vodクリップ一覧Jsonデータ解析.
     * @param jsonStr 元のJsonデータ
     * @return 解析後のデータリスト
     */
    public List<VodClipList> VodClipListSender(final String jsonStr) {

        DTVTLogger.debugHttp(jsonStr);
        mVodClipList = new VodClipList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            sendStatus(jsonObj);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                sendVcList(arrayList);
            }
            List<VodClipList> vodClipList = Arrays.asList(mVodClipList);
            return vodClipList;
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapに格納.
     * @param jsonObj 解析前のJsonデータ
     */
    public void sendStatus(final JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<>();
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
            if (mVodClipList != null) {
                mVodClipList.setVcMap(map);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納.
     * @param arrayList ArrayList
     */
    public void sendVcList(final JSONArray arrayList) {
        try {
            List<HashMap<String, String>> vcList = new ArrayList<>();
            // リストの数だけまわす
            for (int i = 0; i < arrayList.length(); i++) {
                // 最初にHashMapを生成＆初期化
                HashMap<String, String> vcListMap = new HashMap<>();
                // i番目のJSONArrayをJSONObjectに変換する
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
                                vcListMap.put(JsonConstants.META_RESPONSE_PUINF + JsonConstants.UNDER_LINE + puinfBuffer, para);
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
            if (mVodClipList != null) {
                mVodClipList.setVcList(vcList);
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}
