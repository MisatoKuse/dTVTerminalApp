/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
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

    public static final String CHANNEL_LIST_STATUS = "status";
    public static final String CHANNEL_LIST = "list";

    public static final String CHANNEL_LIST_PAGER = "pager";
    public static final String CHANNEL_LIST_PAGER_LIMIT = "limit";
    public static final String CHANNEL_LIST_PAGER_OFFSET = "offset";
    public static final String CHANNEL_LIST_PAGER_COUNT = "count";
    public static final String CHANNEL_LIST_PAGER_TOTAL = "total";

    public static final String CHANNEL_LIST_CRID = "crid";
    public static final String CHANNEL_LIST_CID = "cid";
    public static final String CHANNEL_LIST_SERVER_ID = "service_id";
    public static final String CHANNEL_LIST_CHNO = "chno";
    public static final String CHANNEL_LIST_TITLE = "title";
    public static final String CHANNEL_LIST_TITLERUBY = "titleruby";
    public static final String CHANNEL_LIST_DISP_TYPE = "disp_type";
    public static final String CHANNEL_LIST_SERVICE = "service";
    public static final String CHANNEL_LIST_CH_TYPE = "ch_type";
    public static final String CHANNEL_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String CHANNEL_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String CHANNEL_LIST_THUMB = "thumb";
    public static final String CHANNEL_LIST_DEMONG = "demong";
    public static final String CHANNEL_LIST_4KFLG = "4kflg";
    public static final String CHANNEL_LIST_AVAIL_STATUS = "avail_status";
    public static final String CHANNEL_LIST_DELIVERY = "delivery";
    public static final String CHANNEL_LIST_R_VALUE = "r_value";
    public static final String CHANNEL_LIST_ADULT = "adult";
    public static final String CHANNEL_LIST_NG_FUNC = "ng_func";
    public static final String CHANNEL_LIST_GENRE_ARRAY = "genre_array";
    public static final String CHANNEL_LIST_SYNOP = "synop";
    public static final String CHANNEL_LIST_STAMP = "stamp";
    public static final String CHANNEL_LIST_CHSVOD = "chsvod";
    public static final String CHANNEL_LIST_PUID = "puid";
    public static final String CHANNEL_LIST_SUB_PUID = "sub_puid";
    public static final String CHANNEL_LIST_PRICE = "price";
    public static final String CHANNEL_LIST_QRANGE = "qrange";
    public static final String CHANNEL_LIST_QUINT = "qunit";
    public static final String CHANNEL_LIST_PU_S = "pu_s";
    public static final String CHANNEL_LIST_PU_E = "pu_e";
    public static final String CHANNEL_LIST_CHPACK = "CHPACK";
    public static final String UNDER_LINE = "_";

    public static final String[] pagerPara = {CHANNEL_LIST_PAGER_LIMIT, CHANNEL_LIST_PAGER_OFFSET,
            CHANNEL_LIST_PAGER_COUNT, CHANNEL_LIST_PAGER_TOTAL};

    public static final String[] listPara = {CHANNEL_LIST_CRID, CHANNEL_LIST_CID,
            CHANNEL_LIST_SERVER_ID, CHANNEL_LIST_CHNO, CHANNEL_LIST_TITLE, CHANNEL_LIST_TITLERUBY,
            CHANNEL_LIST_DISP_TYPE, CHANNEL_LIST_SERVICE, CHANNEL_LIST_CH_TYPE,
            CHANNEL_LIST_AVAIL_START_DATE, CHANNEL_LIST_AVAIL_END_DATE, CHANNEL_LIST_THUMB,
            CHANNEL_LIST_DEMONG, CHANNEL_LIST_4KFLG, CHANNEL_LIST_AVAIL_STATUS,
            CHANNEL_LIST_DELIVERY, CHANNEL_LIST_R_VALUE, CHANNEL_LIST_ADULT, CHANNEL_LIST_NG_FUNC, CHANNEL_LIST_GENRE_ARRAY,
            CHANNEL_LIST_SYNOP, CHANNEL_LIST_STAMP, CHANNEL_LIST_CHSVOD, CHANNEL_LIST_PUID,
            CHANNEL_LIST_SUB_PUID, CHANNEL_LIST_PRICE, CHANNEL_LIST_QRANGE, CHANNEL_LIST_QUINT,
            CHANNEL_LIST_PU_S, CHANNEL_LIST_PU_E, CHANNEL_LIST_CHPACK};
    public static final String[] chpackList = {CHANNEL_LIST_CRID, CHANNEL_LIST_TITLE, CHANNEL_LIST_DISP_TYPE, CHANNEL_LIST_PUID,
            CHANNEL_LIST_SUB_PUID, CHANNEL_LIST_PRICE, CHANNEL_LIST_QRANGE, CHANNEL_LIST_QUINT,
            CHANNEL_LIST_PU_S, CHANNEL_LIST_PU_E};

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
            if (!jsonObj.isNull(CHANNEL_LIST_STATUS)) {
                String status = jsonObj.getString(CHANNEL_LIST_STATUS);
                map.put(CHANNEL_LIST_STATUS, status);
            }


            if (!jsonObj.isNull(CHANNEL_LIST_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(CHANNEL_LIST_PAGER);

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
            if (!jsonObj.isNull(CHANNEL_LIST)) {
                // コンテンツリストのList<HashMap>を用意
                List<HashMap<String, String>> vcList = new ArrayList<>();

                // コンテンツリストをJSONArrayにパースする
                JSONArray jsonArr = jsonObj.getJSONArray(CHANNEL_LIST);

                // リストの数だけまわす
                for (int i = 0; i < jsonArr.length(); i++) {
                    // 最初にHashMapを生成＆初期化
                    HashMap<String, String> vcListMap = new HashMap<>();

                    // i番目のJSONArrayをJSONObjectに変換する
                    JSONObject jsonObject = jsonArr.getJSONObject(i);


                    for (int j = 0; j < listPara.length; j++) {
                        if (!jsonObject.isNull(listPara[j])) {
                            if (listPara[j].equals(CHANNEL_LIST_GENRE_ARRAY)) {
                                JSONArray para = jsonObject.getJSONArray(listPara[j]);
                                vcListMap.put(listPara[j], para.toString());
                            } else if (listPara[j].equals(CHANNEL_LIST_CHPACK)) {
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
