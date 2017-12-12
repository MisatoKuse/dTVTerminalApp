/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TvScheduleJsonParser extends AsyncTask<Object, Object, Object> {

    private TvScheduleWebClient.TvScheduleJsonParserCallback mTvScheduleJsonParserCallback;

    // オブジェクトクラスの定義
    private TvScheduleList mTvScheduleList;

    /**
     * コンストラクタ
     *
     * @param mTvScheduleJsonParserCallback コールバック
     */
    public TvScheduleJsonParser(TvScheduleWebClient.TvScheduleJsonParserCallback mTvScheduleJsonParserCallback) {
        this.mTvScheduleJsonParserCallback = mTvScheduleJsonParserCallback;
    }

    @Override
    protected void onPostExecute(Object s) {
        mTvScheduleJsonParserCallback.onTvScheduleJsonParsed((List<TvScheduleList>) s);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String) strings[0];
        List<TvScheduleList> resultList = TvScheduleListListSender(result);
        return resultList;
    }

    /**
     * CH毎番組Jsonデータを解析する
     *
     * @param jsonStr 元のJSONデータ
     * @return パース後のJSONデータ
     */
    public List<TvScheduleList> TvScheduleListListSender(String jsonStr) {

        mTvScheduleList = new TvScheduleList();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                sendStatus(jsonObj);

                if (!jsonObj.isNull(JsonContents.META_RESPONSE_LIST)) {
                    JSONArray arrayList = jsonObj.getJSONArray(JsonContents.META_RESPONSE_LIST);
                    sendTsList(arrayList);
                }
                List<TvScheduleList> tvScheduleList = Arrays.asList(mTvScheduleList);
                return tvScheduleList;
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * statsの値をMapでオブジェクトクラスに渡す
     *
     * @param jsonObj 元のJSONデータ
     */
    public void sendStatus(JSONObject jsonObj) {
        try {
            // statusの値を取得し、Mapに格納
            HashMap<String, String> map = new HashMap<>();
            if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                String status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);
                map.put(JsonContents.META_RESPONSE_STATUS, status);
            }
            mTvScheduleList.setTvsMap(map);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * コンテンツのList<HashMap>をオブジェクトクラスに格納
     *
     * @param arrayList 元のJSONデータ
     */
    public void sendTsList(JSONArray arrayList) {
        try {
            List<HashMap<String, String>> tsList = new ArrayList<>();
            for (int i = 0; i < arrayList.length(); i++) {
                HashMap<String, String> tsListMap = new HashMap<>();
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
                                    tsListMap.put(stringBuffer.toString(), dateBuffer);
                                } else {
                                    //日付ではないのでそのまま格納する
                                    String para = puinfObj.getString(puinfBuffer);
                                    tsListMap.put(stringBuffer.toString(), para);
                                }
                            }
                        } else if (DBUtils.isDateItem(listBuffer)) {
                            // DATE_PARAに含まれるのは日付なので、エポック秒となる。変換して格納する
                            String dateBuffer = DateUtils.formatEpochToString(
                                    StringUtil.changeString2Long(jsonObject.getString(listBuffer)));
                            tsListMap.put(listBuffer, dateBuffer);
                        } else {
                            String para = jsonObject.getString(listBuffer);
                            tsListMap.put(listBuffer, para);
                        }
                    }
                }
                tsList.add(tsListMap);
            }
            if (mTvScheduleList != null) {
                mTvScheduleList.setTvsList(tsList);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
    }
}
