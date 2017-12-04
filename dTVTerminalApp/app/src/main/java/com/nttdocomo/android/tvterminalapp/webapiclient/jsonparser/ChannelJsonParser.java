/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChannelJsonParser extends AsyncTask<Object, Object, Object> {

    private ChannelWebClient.ChannelJsonParserCallback mChannelJsonParserCallback;

    // オブジェクトクラスの定義
    private ChannelList mChannelList;

    public static final String UNDER_LINE = "_";

    public static final String[] pagerPara = {JsonContents.META_RESPONSE_PAGER_LIMIT,
            JsonContents.META_RESPONSE_OFFSET, JsonContents.META_RESPONSE_COUNT,
            JsonContents.META_RESPONSE_TOTAL};

    public static final String[] listPara = {JsonContents.META_RESPONSE_CRID,
            JsonContents.META_RESPONSE_SERVICE_ID, JsonContents.META_RESPONSE_CHNO,
            JsonContents.META_RESPONSE_TITLE, JsonContents.META_RESPONSE_TITLERUBY,
            JsonContents.META_RESPONSE_DISP_TYPE, JsonContents.META_RESPONSE_SERVICE,
            JsonContents.META_RESPONSE_CH_TYPE, JsonContents.META_RESPONSE_AVAIL_START_DATE,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, JsonContents.META_RESPONSE_DEFAULT_THUMB,
            JsonContents.META_RESPONSE_THUMB_640, JsonContents.META_RESPONSE_THUMB_448,
            JsonContents.META_RESPONSE_DEMONG, JsonContents.META_RESPONSE_4KFLG,
            JsonContents.META_RESPONSE_AVAIL_STATUS,JsonContents.META_RESPONSE_DELIVERY,
            JsonContents.META_RESPONSE_R_VALUE, JsonContents.META_RESPONSE_ADULT,
            JsonContents.META_RESPONSE_NG_FUNC, JsonContents.META_RESPONSE_GENRE_ARAY,
            JsonContents.META_RESPONSE_SYNOP, JsonContents.META_RESPONSE_CHSVOD,
            JsonContents.META_RESPONSE_PUID, JsonContents.META_RESPONSE_SUB_PUID,
            JsonContents.META_RESPONSE_PRICE, JsonContents.META_RESPONSE_QRANGE,
            JsonContents.META_RESPONSE_GUNIT,JsonContents.META_RESPONSE_PU_START_DATE,
            JsonContents.META_RESPONSE_PU_END_DATE, JsonContents.META_RESPONSE_CHPACK};
    public static final String[] chpackList = {JsonContents.META_RESPONSE_CRID,
            JsonContents.META_RESPONSE_TITLE, JsonContents.META_RESPONSE_DISP_TYPE,
            JsonContents.META_RESPONSE_PUID, JsonContents.META_RESPONSE_SUB_PUID,
            JsonContents.META_RESPONSE_PRICE, JsonContents.META_RESPONSE_QRANGE,
            JsonContents.META_RESPONSE_GUNIT, JsonContents.META_RESPONSE_PU_START_DATE,
            JsonContents.META_RESPONSE_PU_END_DATE};

    /**
     * CH一覧Jsonデータを解析する
     *
     * @param jsonStr
     * @return
     */
    public List<ChannelList> CHANNELListSender(String jsonStr) {

        mChannelList = new ChannelList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);
                sendVcList(jsonObj);

                List<ChannelList> clList = Arrays.asList(mChannelList);

                return clList;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
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
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);
                map.put(JsonContents.META_RESPONSE_STATUS, status);
            }


            if (!jsonObj.isNull(JsonContents.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonContents.META_RESPONSE_PAGER);

                for (int i = 0; i < pagerPara.length; i++) {
                    if (!pager.isNull(pagerPara[i])) {
                        String para = pager.getString(pagerPara[i]);
                        map.put(pagerPara[i], para);
                    }
                }
            }

            mChannelList.setClMap(map);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンストラクタ
     *
     * @param mChannelJsonParserCallback
     */
    public ChannelJsonParser(ChannelWebClient.ChannelJsonParserCallback mChannelJsonParserCallback) {
        this.mChannelJsonParserCallback = mChannelJsonParserCallback;
    }

    @Override
    protected void onPostExecute(Object s) {
        mChannelJsonParserCallback.onChannelJsonParsed((List<ChannelList>) s);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        List<ChannelList> resultList = CHANNELListSender(result);
        return resultList;
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納
     *
     * @param jsonObj
     */
    public void sendVcList(JSONObject jsonObj) {
        try {
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(JsonContents.META_RESPONSE_LIST);

                // リストの数だけまわす
                for (int i = 0; i < jsonArr.length(); i++) {
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);


                    for (int j = 0; j < listPara.length; j++) {
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j].equals(JsonContents.META_RESPONSE_GENRE_ARAY)) {
                                JSONArray para = jsonObject.getJSONArray(listPara[j]);
                                vcListMap.put(listPara[j], para.toString());
                            } else if (listPara[j].equals(JsonContents.META_RESPONSE_CHPACK)) {
                                JSONObject para = jsonObject.getJSONObject(listPara[j]);
                                for (int c = 0; c < chpackList.length; c++) {
                                    if (!jsonObject.isNull(chpackList[c])) {
                                        String value = para.getString(chpackList[c]);
                                        vcListMap.put(listPara[j] + UNDER_LINE + chpackList[c], value);
                                    }
                                }
                            } else {
                                String para = jsonObject.getString(listPara[j]);
                                vcListMap.put(listPara[j], para);
                            }
                        }
                    }
                    // i番目のMapをListにadd
                    vcList.add(vcListMap);
                }
                // リスト数ぶんの格納が終わったらオブジェクトクラスにList<HashMap>でset
                mChannelList.setClList(vcList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}
