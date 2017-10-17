package com.nttdocomo.android.tvterminalapp.webapiclient.recommend.search;


import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

    private boolean mError=false;

    private void setError(boolean b){
        synchronized (this) {
            mError = b;
        }
    }

    public HttpThread(String url, Handler handler, HttpThreadFinish httpThreadFinish) {
        mHandler = handler;
        mUrl = url;
        mHttpThreadFinish=httpThreadFinish;
        clearStatus();
    }

    private void clearStatus(){
        mXmlStr = "";
        setError(false);
    }

    @Override
    public void run() {

        clearStatus();
        HttpURLConnection httpUrlConn = null;
        final StringBuffer sb = new StringBuffer();
        String str = null;

        try {
            URL url = new URL(this.mUrl);
            httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setReadTimeout(DTVTConstants.SEARCH_SERVER_TIMEOUT);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("Accept-Charset", "utf-8");
            httpUrlConn.setRequestProperty("contentType", "utf-8");

            if(httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream is = httpUrlConn.getInputStream();
                if(null==is){
                    throw new Exception("HttpThread::run, is==null");
                }

                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                if(null==isr){
                    throw new Exception("HttpThread::run, isr==null");
                }
                BufferedReader br = new BufferedReader(isr);
                if (null == br) {
                    throw new Exception("HttpThread::run, br==null");
                }

                while ((str = br.readLine()) != null) {
                    sb.append(str);
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            setError(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            setError(true);
        }

        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if(null!=mHttpThreadFinish){
                    if(mError){
                        mXmlStr = "";
                    } else {
                        mXmlStr = sb.toString();
                    }
                    mHttpThreadFinish.onHttpThreadFinish(mXmlStr);
                }
            }
        });

    }
}
