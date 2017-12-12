/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VodClipJsonParser extends AsyncTask<Object, Object, Object> {

    private VodClipWebClient.VodClipJsonParserCallback mVodClipJsonParserCallback;
    // オブジェクトクラスの定義
    private VodClipList mVodClipList;

    public static final String[] PAGER_PARA = {JsonContents.META_RESPONSE_UPPER_LIMIT,
            JsonContents.META_RESPONSE_LOWER_LIMIT, JsonContents.META_RESPONSE_OFFSET,
            JsonContents.META_RESPONSE_COUNT};

//    public static final String[] listPara = {VODCLIP_LIST_CRID, VODCLIP_LIST_CID, VODCLIP_LIST_TITLE_ID,
//            VODCLIP_LIST_EPISODE_ID, VODCLIP_LIST_TITLE, VODCLIP_LIST_EPITITLE, VODCLIP_LIST_DISP_TYPE,
//            VODCLIP_LIST_DISPLAY_START_DATE, VODCLIP_LIST_DISPLAY_END_DATE, VODCLIP_LIST_AVAIL_START_DATE,
//            VODCLIP_LIST_AVAIL_END_DATE, VODCLIP_LIST_PUBLISH_START_DATE, VODCLIP_LIST_PUBLISH_END_DATE,
//            VODCLIP_LIST_NEWA_START_DATE, VODCLIP_LIST_NEWA_END_DATE, VODCLIP_LIST_COPYRIGHT,
//            VODCLIP_LIST_THUMB, VODCLIP_LIST_DUR, VODCLIP_LIST_DEMONG, VODCLIP_LIST_BVFLG, VODCLIP_LIST_4KFLG,
//            VODCLIP_LIST_HDRFLG, VODCLIP_LIST_AVAIL_STATUS, VODCLIP_LIST_DELIVERY, VODCLIP_LIST_R_VALUE,
//            VODCLIP_LIST_ADULT, VODCLIP_LIST_MS, VODCLIP_LIST_NG_FUNC, VODCLIP_LIST_GENRE_ID_ARRAY, VODCLIP_LIST_DTV};

    /**
     * コンストラクタ
     *
     * @param mVodClipJsonParserCallback
     */
    public VodClipJsonParser(VodClipWebClient.VodClipJsonParserCallback mVodClipJsonParserCallback) {
        this.mVodClipJsonParserCallback = mVodClipJsonParserCallback;
    }

    @Override
    protected void onPostExecute(Object s) {
        mVodClipJsonParserCallback.onVodClipJsonParsed((List<VodClipList>) s);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        List<VodClipList> resultList = VodClipListSender(result);
        return resultList;
    }

    /**
     * VodクリップJsonデータを解析する
     *
     * @param jsonStr
     * @return
     */
    public List<VodClipList> VodClipListSender(String jsonStr) {

        mVodClipList = new VodClipList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            sendStatus(jsonObj);
            // **FindBugs** Bad practice FindBugはこのヌルチェックが無用と警告するが、将来的にcatch (Exception e)は消すはずなので残す
            if (jsonObj.isNull(JsonContents.META_RESPONSE_LIST)) {
                JSONArray arrayList = jsonObj.getJSONArray(JsonContents.META_RESPONSE_LIST);
                sendVcList(arrayList);
            }
            List<VodClipList> vodClipList = Arrays.asList(mVodClipList);
            return vodClipList;
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statusの値をMapでオブジェクトクラスに渡す
     *
     * @param jsonObj
     */
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

            if (mVodClipList != null) {
                mVodClipList.setVcMap(map);
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
     *
     * @param arrayList
     */
    public void sendVcList(JSONArray arrayList) {
        try {
            // コンテンツリストのList<HashMap>を用意
            List<HashMap<String, String>> vcList = new ArrayList<>();
            // リストの数だけまわす
            for (int i = 0; i < arrayList.length(); i++) {
                // 最初にHashMapを生成＆初期化
                HashMap<String, String> vcListMap = new HashMap<>();
                // i番目のJSONArrayをJSONObjectに変換する
                JSONObject jsonObject = arrayList.getJSONObject(i);
                for (String listBuffer : JsonContents.LIST_PARA) {
                    if (!jsonObject.isNull(listBuffer)) {
                        if (listBuffer.equals(JsonContents.META_RESPONSE_PUINF)) {
                            JSONObject puinfObj = jsonObject.getJSONObject(listBuffer);
                            for (String puinfBuffer : JsonContents.PUINF_PARA) {
                                //書き込み用項目名の作成
                                StringBuilder stringBuffer = new StringBuilder();
                                stringBuffer.append(JsonContents.META_RESPONSE_PUINF);
                                stringBuffer.append(JsonContents.UNDER_LINE);
                                stringBuffer.append(puinfBuffer);

                                //日付項目チェック
                                if (DBUtils.isDateItem(puinfBuffer)) {
                                    //日付なので変換して格納する
                                    String dateBuffer = DateUtils.formatEpochToString(
                                            StringUtil.changeString2Long(puinfObj.getString(
                                                    puinfBuffer)));
                                    vcListMap.put(stringBuffer.toString(), dateBuffer);
                                } else {
                                    //日付ではないのでそのまま格納する
                                    String para = puinfObj.getString(puinfBuffer);
                                    vcListMap.put(JsonContents.META_RESPONSE_PUINF + JsonContents.UNDER_LINE + puinfBuffer, para);
                                }
                            }
                        } else if (DBUtils.isDateItem(listBuffer)) {
                            // DATE_PARAに含まれるのは日付なので、エポック秒となる。変換して格納する
                            String dateBuffer = DateUtils.formatEpochToString(
                                    StringUtil.changeString2Long(jsonObject.getString(listBuffer)));
                            vcListMap.put(listBuffer, dateBuffer);
                        } else {
                            String para = jsonObject.getString(listBuffer);
                            vcListMap.put(listBuffer, para);
                        }
                    }
                }
                vcList.add(vcListMap);
            }

            // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
            if (mVodClipList != null) {
                mVodClipList.setVcList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}