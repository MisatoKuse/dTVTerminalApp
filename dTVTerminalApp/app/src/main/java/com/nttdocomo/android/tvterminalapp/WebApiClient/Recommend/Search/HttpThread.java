package com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search;


import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DCommon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpThread extends Thread {

    public interface HttpThreadFinish{
        public void onHttpThreadFinish(String str);
    }

    private String mUrl = null;
    private Handler mHandler = null;
    private String mXmlStr="";
    private HttpThreadFinish mHttpThreadFinish=null;

    public HttpThread(String url, Handler handler, HttpThreadFinish httpThreadFinish) {
        mHandler = handler;
        mUrl = url;
        mHttpThreadFinish=httpThreadFinish;
    }

    @Override
    public void run() {
        HttpURLConnection httpUrlConn = null;
        try {
            URL url = new URL(this.mUrl);
            httpUrlConn = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            httpUrlConn.setReadTimeout(DCommon.SEARCH_SERVER_TIMEOUT);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("Accept-Charset", "utf-8");
            httpUrlConn.setRequestProperty("contentType", "utf-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final StringBuffer sb = new StringBuffer();
        String str = null;
        try {
            if(httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream()));

                    try {
                        while ((str = br.readLine()) != null) {
                            sb.append(str);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mHandler.post(new Runnable() {

            public void run() {
                if(null!=mHttpThreadFinish){
                    mXmlStr = sb.toString();
                    mHttpThreadFinish.onHttpThreadFinish(mXmlStr);
                }
            }
        });

    }

}
