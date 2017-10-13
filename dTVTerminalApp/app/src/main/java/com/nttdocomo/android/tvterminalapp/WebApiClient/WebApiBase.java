package com.nttdocomo.android.tvterminalapp.WebApiClient;


import android.os.Handler;
import android.util.Log;

import com.nttdocomo.android.tvterminalapp.common.DCommon;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.HttpThread;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.WebApiCallback;

import java.util.LinkedHashMap;
import java.util.Map;


public class WebApiBase implements HttpThread.HttpThreadFinish {

    private WebApiCallback mWebApiCallback=null;

    public void get(String urlString, LinkedHashMap<String, String> queryItems, WebApiCallback callback){
        Handler handler = new Handler();
        String url=createUrlComponents(urlString, queryItems);
        mWebApiCallback=callback;
        //Log.d(DCommon.LOG_DEF_TAG, "WebApiBase::get, url= " + url);
        new HttpThread(url, handler, this).start();
    }

    private String createUrlComponents(String url, Map<String, String> queryItems) {
        StringBuffer u=new StringBuffer(url);
        if(null!=url){

            for(String key : queryItems.keySet()){
                if(null!=key && 0<key.length()){
                    if(queryItems.containsKey(key)){
                        String v="";
                        try {
                            v = queryItems.get(key);
                        }catch (Exception e){
                            e.printStackTrace();
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

            }
            Log.d(DCommon.LOG_DEF_TAG, "WebApiBase::createUrlComponents, url=" + u.toString());
        }
        return u.toString();
    }

    @Override
    public void onHttpThreadFinish(String str) {
        if(null!=mWebApiCallback){
            mWebApiCallback.onFinish(str);
        }
    }

}
