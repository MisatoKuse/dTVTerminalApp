package com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser;

import android.util.Xml;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class RecommendChannelXmlParser  {

    String xmlResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<Object>\n" +
            " <Result>0</Result>\n" +
            " <RecommendContentsList>\n" +
            "  <RecommendContent>\n" +
            "   <recommendOrder>1</recommendOrder>\n" +
            "   <serviceId>15</serviceId>\n" +
            "   <categoryId>01</categoryId>\n" +
            "   <channelId></channelId>\n" +
            "   <contentsId>10010045</contentsId>\n" +
            "   <title>24－TWENTY　FOUR－　リブ・アナザー・デイ</title>\n" +
            "   <ctPicURL1>https://image5-a.beetv.jp/basic/img/title/10010045_top_hd_org.jpg</ctPicURL1>\n" +
            "   <ctPicURL2></ctPicURL2>\n" +
            "   <startViewing>20150401000000</startViewing>\n" +
            "   <endViewing>20170930235900</endViewing>\n" +
            "  </RecommendContent>\n" +
            "  <RecommendContent>\n" +
            "   <recommendOrder>2</recommendOrder>\n" +
            "   <serviceId>15</serviceId>\n" +
            "   <categoryId>01</categoryId>\n" +
            "   <channelId></channelId>\n" +
            "   <contentsId>10238503</contentsId>\n" +
            "   <title>24　－TWENTY　FOUR－　レガシー</title>\n" +
            "   <ctPicURL1>http://image5-a.beetv.jp/basic/img/title/10018486_v.jpg</ctPicURL1>\n" +
            "   <ctPicURL2></ctPicURL2>\n" +
            "   <startViewing>20150501000000</startViewing>\n" +
            "   <endViewing>20180430235900</endViewing>\n" +
            "  </RecommendContent>\n" +
            " </RecommendContentsList>\n" +
            "</Object>";
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

    public RecommendChList getRecommendchannelList() {
        RecommendChList redChContents = null;
        List<HashMap<String, String>> redChContentList = null;
        HashMap<String, String> redChHashMap = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(xmlResult));
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
                            redChHashMap.put(parser.getName(),parser.getText()==null?"":parser.getText());
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
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return redChContents;
    }
}