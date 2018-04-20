/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser;

import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;


/**
 *　JsonParserThread.
 */
public class JsonParserThread extends Thread {
    /**ハンドラー.*/
    private Handler mHandle = null;
    /**データ.*/
    private String mJson = "";
    /**JsonParser インタフェース.*/
    private JsonParser mJsonParser = null;
    /**エラー発生したか.*/
    private boolean mError = false;

    /**
     * JsonParser インタフェース.
     */
    public interface JsonParser {
        /**
         * パース後のコールバック.
         * @param parsedData パース後のデータ
         */
        void onParserFinished(Object parsedData);

        /**
         * パースする.
         * @param body パース対象
         * @return パース後のデータ
         * @throws Exception Exception
         */
        Object parse(String body) throws Exception;
    }

    /**
     * JsonParserThread コンストラクタ.
     * @param json データ
     * @param handle ハンドラー
     * @param lis  JsonParserインタフェース
     * @throws  Exception Exception
     */
    public JsonParserThread(final String json, final Handler handle, final JsonParser lis) throws Exception {
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