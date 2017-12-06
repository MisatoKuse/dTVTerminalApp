/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

import java.util.List;

public class JsonParserThread extends Thread {

    public interface JsonParser {
        public void onParserFinished(Object parsedData);

        public Object parse(String body) throws Exception;
    }

    private Handler mHandle = null;
    private String mJson = "";
    private JsonParser mJsonParser = null;
    private boolean mError = false;

    public JsonParserThread(String json, Handler handle, JsonParser lis) throws Exception {
        if (null == json || 0 == json.length() || null == handle) {
            throw new Exception("JsonParserThread Exception, cause=(null==json || 0==json.length() || null==handle)");
        }

        mJson = json;
        mHandle = handle;
        mJsonParser = lis;
    }

    @Override
    public void run() {
        mError = false;
        Object ret = null;

        if (null != mJsonParser) {
            try {
                ret = mJsonParser.parse(mJson);
            } catch (Exception e) {
                DTVTLogger.debug(e);
                mError = true;
            }
        }

        final Object finalRet = ret;
        mHandle.post(new Runnable() {

            @Override
            public void run() {
                if (null != mJsonParser) {
                    if (mError) {
                        mJsonParser.onParserFinished("");
                    } else {
                        mJsonParser.onParserFinished(finalRet);
                    }
                }
            }
        });
    }
}
