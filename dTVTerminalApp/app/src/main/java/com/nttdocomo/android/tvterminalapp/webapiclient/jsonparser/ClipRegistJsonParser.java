/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.AsyncTask;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ClipRegistWebClient;

import org.json.JSONException;
import org.json.JSONObject;

public class ClipRegistJsonParser extends AsyncTask<Object, Object, String> {

    // callback
    private ClipRegistWebClient.ClipRegistJsonParserCallback mClipJsonParserCallback;

    // クリップ登録/削除成功判定用
    private static final String CLIP_RESULT_STATUS_OK = "OK";

    /**
     * CH一覧Jsonデータを解析する
     *
     * @param jsonStr 元のJSONデータ
     * @return リスト化データ
     */
    private String getClipStatus(String jsonStr) {

        String status = "";

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj != null) {
                if (!jsonObj.isNull(JsonContents.META_RESPONSE_STATUS)) {
                    status = jsonObj.getString(JsonContents.META_RESPONSE_STATUS);
                }

                return status;
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DTVTLogger.debug(e);
        }
        return null;
    }

    /**
     * コンストラクタ
     *
     * @param mClipJsonParserCallback コールバック
     */
    public ClipRegistJsonParser(ClipRegistWebClient.ClipRegistJsonParserCallback mClipJsonParserCallback) {
        this.mClipJsonParserCallback = mClipJsonParserCallback;
    }

    @Override
    protected void onPostExecute(String status) {
        if (status != null && status.equals(CLIP_RESULT_STATUS_OK)) {
            //成功時のcallback
            mClipJsonParserCallback.onClipRegistResult();
        } else {
            //失敗時のcallback
            mClipJsonParserCallback.onClipRegistFailure();
        }
    }

    @Override
    protected String doInBackground(Object... strings) {
        String result = (String) strings[0];
        return getClipStatus(result);
    }
}
