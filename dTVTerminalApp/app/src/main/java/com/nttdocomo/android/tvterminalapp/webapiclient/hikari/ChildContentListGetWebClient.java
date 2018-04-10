/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.hikari;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChildContentListGetResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ContentsDetailGetResponse;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChildContentListJsonParser;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ContentsDetailJsonParser;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.RentalVodListJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChildContentListGetWebClient extends WebApiBasePlala implements
        WebApiBasePlala.WebApiBasePlalaCallback
{

    // declaration
    /** コールバック. */
    public interface JsonParserCallback {
        /**
         * コールバック
         * errorの場合はnullを返却する
         * @param response
         */
        void onJsonParsed(@Nullable ChildContentListGetResponse response);
    }

    // region variable
    private Context mContext = null;
    private JsonParserCallback mJsonParserCallback;
    /** 通信禁止判定フラグ. */
    private boolean mIsCancel = false;

    /**
     * コンテキストを継承元のコンストラクタに送る.
     *
     * @param context コンテキスト
     */
    public ChildContentListGetWebClient(final Context context) {
        super(context);
        mContext = context;
    }

    /**
     * 通信成功時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onAnswer(final ReturnCode returnCode) {
        new ChildContentListJsonParser(mContext, mJsonParserCallback).execute(returnCode.bodyData);
    }

    /**
     * 通信失敗時のコールバック.
     *
     * @param returnCode 戻り値構造体
     */
    @Override
    public void onError(final ReturnCode returnCode) {
        if (mJsonParserCallback != null) {
            mJsonParserCallback.onJsonParsed(null);
        }
    }

    /**
     * APIリクエスト
     * @return
     */
    public boolean requestChildContentListGetApi(final String crid, final int offset, final String filter, final int ageReq, final JsonParserCallback callback) {
        mJsonParserCallback = callback;

        final String receivedParameter = makeSendParameter(crid, offset, filter, ageReq);
        if (receivedParameter.isEmpty()) {
            return false;
        }

        openUrl(UrlConstants.WebApiUrl.CHILD_CONTENTS_GET_WEB_CLIENT, receivedParameter, this);
        return true;
    }

    private String makeSendParameter(final String crid, final int offset, final String filter, final int ageReq) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject();
            int intAge = ageReq;
            //数字がゼロの場合は無指定と判断して1にする.また17より大きい場合は17に丸める.
            if (ageReq < WebApiBasePlala.AGE_LOW_VALUE) {
                intAge = WebApiBasePlala.AGE_LOW_VALUE;
            } else if (ageReq > WebApiBasePlala.AGE_HIGH_VALUE) {
                intAge = WebApiBasePlala.AGE_HIGH_VALUE;
            }
            jsonObject.put(JsonConstants.META_RESPONSE_AGE_REQ, intAge);

            jsonObject.put(JsonConstants.META_RESPONSE_CRID, crid);
            jsonObject.put(JsonConstants.META_RESPONSE_FILTER, filter);

            JSONObject jsonPagerObject = new JSONObject();
            jsonPagerObject.put(JsonConstants.META_RESPONSE_OFFSET, offset);
            jsonPagerObject.put(JsonConstants.META_RESPONSE_PAGER_LIMIT, DTVTConstants.REQUEST_LIMIT_50);
            jsonObject.put(JsonConstants.META_RESPONSE_PAGER, jsonPagerObject);

            result = jsonObject.toString().replace("\\", "");

        } catch (JSONException e) {
            DTVTLogger.warning(e.getMessage());
        }
        DTVTLogger.debugHttp(result);
        return result;
    }
    /**
     * 通信を止める.
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopAllConnections();
    }

    /**
     * 通信可能状態にする.
     */
    public void enableConnection() {
        DTVTLogger.start();
        mIsCancel = false;
    }
}
