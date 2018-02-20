/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser;

import android.os.AsyncTask;
import android.util.Xml;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendChWebClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendChannelXmlParser extends AsyncTask<Object, Object, Object> {

    private RecommendChWebClient.RecommendChannelCallback mRecommendChannelCallback;
    public static final String RECOMMENDCHANNEL_LIST_RECOMMENDCONTENT = "RecommendContent";
    public static final String RECOMMENDCHANNEL_LIST_RECOMMENDORDER = "recommendOrder";
    public static final String RECOMMENDCHANNEL_LIST_SERVICEID = "serviceId";
    public static final String RECOMMENDCHANNEL_LIST_CATEGORYID = "categoryId";
    public static final String RECOMMENDCHANNEL_LIST_CHANNELID = "channelId";
    public static final String RECOMMENDCHANNEL_LIST_CONTENTSID = "contentsId";
    public static final String RECOMMENDCHANNEL_LIST_TITLE = "title";
    public static final String RECOMMENDCHANNEL_LIST_CTPICURL1 = "ctPicURL1";
    public static final String RECOMMENDCHANNEL_LIST_CTPICURL2 = "ctPicURL2";
    public static final String RECOMMENDCHANNEL_LIST_STARTVIEWING = "startViewing";
    public static final String RECOMMENDCHANNEL_LIST_ENDVIEWING = "endViewing";
    public static final String RECOMMENDCHANNEL_LIST_RESERVED1 = "reserved1";
    public static final String RECOMMENDCHANNEL_LIST_RESERVED2 = "reserved2";
    public static final String RECOMMENDCHANNEL_LIST_RESERVED3 = "reserved3";
    public static final String RECOMMENDCHANNEL_LIST_RESERVED4 = "reserved4";
    public static final String RECOMMENDCHANNEL_LIST_RESERVED5 = "reserved5";
    public static final String RECOMMENDCHANNEL_LIST_AGREEMENT = "agreement";
    public static final String RECOMMENDCHANNEL_LIST_VIEWABLE = "viewable";
    public static final String RECOMMENDCHANNEL_LIST_PAGEID = "pageId";
    public static final String RECOMMENDCHANNEL_LIST_GROUPID = "groupId";
    public static final String RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID = "recommendMethodId";

    public RecommendChannelXmlParser(RecommendChWebClient.RecommendChannelCallback mRecommendChannelCallback){
        this.mRecommendChannelCallback = mRecommendChannelCallback;
    }

    @Override
    protected void onPostExecute(Object s) {
        mRecommendChannelCallback.RecommendChannelCallback((RecommendChList)s);
    }

    @Override
    protected Object doInBackground(Object... strings) {
        String result = (String)strings[0];
        RecommendChList resultList = getRecommendchannelList(result);
        return resultList;
    }

    /**
     * 受け取ったレスポンスデータからXMLをパースする
     * @param responseData レスポンスデータ
     * @return パース後のデータ
     */
    public RecommendChList getRecommendchannelList(String responseData) {
        DTVTLogger.debugHttp(responseData);
        RecommendChList redChContents = null;
        List<Map<String, String>> redChContentList = null;
        HashMap<String, String> redChHashMap = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(responseData));
            int eventType = parser.getEventType();
            boolean endFlg = false;
            while (!endFlg) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        redChContents = new RecommendChList();
                        redChContentList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if(RECOMMENDCHANNEL_LIST_RECOMMENDCONTENT.equals(parser.getName())){
                            redChHashMap = new HashMap<>();
                        } else if(RECOMMENDCHANNEL_LIST_RECOMMENDORDER.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_RECOMMENDORDER,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_SERVICEID.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_SERVICEID,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_CATEGORYID.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_CATEGORYID,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_CHANNELID.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_CHANNELID,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_CONTENTSID.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_CONTENTSID,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_TITLE.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_TITLE,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_CTPICURL1.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_CTPICURL1,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_CTPICURL2.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_CTPICURL2,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_STARTVIEWING.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_STARTVIEWING,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_ENDVIEWING.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_ENDVIEWING,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_RESERVED1.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED1,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_RESERVED2.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED2,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_RESERVED3.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED3,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_RESERVED4.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED4,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_RESERVED5.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED5,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_AGREEMENT.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_AGREEMENT,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_VIEWABLE.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_VIEWABLE,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_PAGEID.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_PAGEID,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_GROUPID.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_GROUPID,parser.getText()==null?"":parser.getText());
                        } else if(RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID.equals(parser.getName())) {
                            eventType = parser.next();
                            redChHashMap.put(RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID,parser.getText()==null?"":parser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(RECOMMENDCHANNEL_LIST_RECOMMENDCONTENT.equals(parser.getName())){
                            redChContentList.add(redChHashMap);
                            redChHashMap = null;
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        redChContents.setmRcList(redChContentList);
                        endFlg = true;
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            DTVTLogger.debug(e);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return redChContents;
    }
}