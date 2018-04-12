/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.content.Context;
import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChildContentListGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChildContentListGetWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * レンタル一覧（Jsonパーサー）.
 */
public class ChildContentListJsonParser extends AsyncTask<String, Object, Object> {

    private Context mContext = null;
    private ChildContentListGetWebClient.JsonParserCallback mJsonParserCallback = null;
    private ChildContentListGetResponse mChildContentListGetResponse = null;

    /**
     * コンストラクタ
     * @param context
     * @param callback
     */
    public ChildContentListJsonParser(final Context context, final ChildContentListGetWebClient.JsonParserCallback callback) {
        mContext = context;
        mJsonParserCallback = callback;
        mChildContentListGetResponse = new ChildContentListGetResponse();
    }

    @Override
    protected Object doInBackground(final String... strings) {
        String result = strings[0];
        if (result == null) {
            mChildContentListGetResponse.setStatus(JsonConstants.META_RESPONSE_STATUS_NG);
            return mChildContentListGetResponse;
        }

        try {
            JSONObject jsonObj = new JSONObject(result);
            mChildContentListGetResponse.setStatus(jsonObj.getString(JsonConstants.META_RESPONSE_STATUS));
            parseMetaList(jsonObj);
        } catch (JSONException e) {
            mChildContentListGetResponse.setStatus(JsonConstants.META_RESPONSE_STATUS_NG);
            DTVTLogger.debug(e);
        } catch (RuntimeException e) {
            mChildContentListGetResponse.setStatus(JsonConstants.META_RESPONSE_STATUS_NG);
            DTVTLogger.debug(e);
        }
        return mChildContentListGetResponse;
    }

    @Override
    protected void onPostExecute(final Object s) {
        mJsonParserCallback.onJsonParsed(mChildContentListGetResponse);
    }

    /**
     * メタリストをパースする（user情報を見てclip状態も反映する）
     *
     * @param jsonObj APIレスポンス Jsonデータ
     */
    public void parseMetaList(final JSONObject jsonObj) {
        try {
            ArrayList<VodMetaFullData> vodMetaFullDataList = new ArrayList<VodMetaFullData>();
            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_STATUS)) {
                mChildContentListGetResponse.setStatus(jsonObj.getString(JsonConstants.META_RESPONSE_STATUS));
            }

            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_PAGER)) {
                JSONObject pager = jsonObj.getJSONObject(JsonConstants.META_RESPONSE_PAGER);
                int limit = 0, offset = 0, count, total;
                if (!pager.isNull(JsonConstants.META_RESPONSE_PAGER_LIMIT)) {
                    limit = pager.getInt(JsonConstants.META_RESPONSE_PAGER_LIMIT);
                }
                if (!pager.isNull(JsonConstants.META_RESPONSE_OFFSET)) {
                    offset = pager.getInt(JsonConstants.META_RESPONSE_OFFSET);
                }
                count = pager.getInt(JsonConstants.META_RESPONSE_COUNT);
                total = pager.getInt(JsonConstants.META_RESPONSE_TOTAL);
                mChildContentListGetResponse.setPager(limit, offset, count, total);
            }

            if (!jsonObj.isNull(JsonConstants.META_RESPONSE_LIST)) {
                JSONArray metaList = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_LIST);
                UserState userState = UserInfoUtils.getUserState(mContext);
                for (int i = 0; i < metaList.length(); i++) {
                    VodMetaFullData vodMetaFullData = new VodMetaFullData();
                    vodMetaFullData.setData(userState, metaList.getJSONObject(i));
                    vodMetaFullDataList.add(vodMetaFullData);
                }
                mChildContentListGetResponse.setVodMetaFullData(vodMetaFullDataList);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}