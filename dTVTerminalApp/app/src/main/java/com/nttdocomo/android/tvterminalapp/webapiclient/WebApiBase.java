/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient;

import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.HttpThread;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.WebApiCallback;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebApiBase implements HttpThread.HttpThreadFinish {

    private WebApiCallback mWebApiCallback = null;

    public void get(String urlString, LinkedHashMap<String, String> queryItems, WebApiCallback callback) {
        Handler handler = new Handler();
        String url = createUrlComponents(urlString, queryItems);
        mWebApiCallback = callback;
        //Log.d(DCommon.LOG_DEF_TAG, "WebApiBase::get, url= " + url);
        new HttpThread(url, handler, this).start();
    }

    /**
     * Handlerが使用できないASyncTaskの処理内で使用する
     */
    public void getReccomendInfo(String urlString, LinkedHashMap<String, String> queryItems, WebApiCallback callback) {
        String url = createUrlComponents(urlString, queryItems);
        mWebApiCallback = callback;
        //Log.d(DCommon.LOG_DEF_TAG, "WebApiBase::get, url= " + url);
        new HttpThread(url, this).start();
    }

    /**
     * get呼び出し用に、URLとパラメータを統合する
     *
     * @param url 呼び出し用URL
     * @param queryItems 呼び出し用パラメータ
     * @return 統合後文字列
     */
    protected String createUrlComponents(String url, Map<String, String> queryItems) {
        StringBuffer stringBuffer = new StringBuffer("");
        if (null != url) {

            stringBuffer = new StringBuffer(url);

            for (String key : queryItems.keySet()) {
                if (null != key && 0 < key.length()) {
                    if (queryItems.containsKey(key)) {
                        String v = "";
                        try {
                            v = queryItems.get(key);
                        } catch (Exception e) {
                            DTVTLogger.debug(e);
                        }
                        if (null != v) {
                            stringBuffer.append(key);
                            stringBuffer.append("=");
                            stringBuffer.append(queryItems.get(key));
                        } else {
                            DTVTLogger.debug("WebApiBase::createUrlComponents, queryItems.get(key) is NULL");
                        }
                    } else {
                        DTVTLogger.debug("WebApiBase::createUrlComponents, queryItems has no key " + key);
                    }
                }

            }
            //findbug 対応 begin
            /*
            for(Iterator value = queryItems.entrySet().iterator(), key = queryItems.keySet().iterator(); value.hasNext() && key.hasNext();  ){
                if(null!=key && 0<key.length()){
                    if(queryItems.containsKey(key)){
                        String v="";
                        try {
                            v = queryItems.get(key);
                        }catch (Exception e){
                            DTVTLogger.debug(e);
                        }
                        if(null!=v){
                            u.append(key+ "=") ;
                            u.append(queryItems.get(key));
                        }else {
                            Log.d(DCommon.LOG_DEF_TAG, "WebApiBase::createUrlComponents, queryItems.get(key) is NULL");
                        }
                    } else {
                        Log.d(DCommon.LOG_DEF_TAG, "WebApiBase::createUrlComponents, queryItems has no key "+ key);
                    }
                }

            }*/
            //findbug 対応 end
            DTVTLogger.debug("WebApiBase::createUrlComponents, url=" + stringBuffer.toString());
        }
        return stringBuffer.toString();
    }

    @Override
    public void onHttpThreadFinish(String str) {
        if (null != mWebApiCallback) {
            mWebApiCallback.onFinish(str);
        }
    }
}