/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser;

import android.os.AsyncTask;
import android.util.Xml;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChannelList;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendWebClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *おすすめWebXmlParser.
 */
public class RecommendWebXmlParser extends AsyncTask<Object, Object, Object> {
    /**callback.*/
    private final RecommendWebClient.RecommendCallback mRecommendCallback;
    /**レコメンドコンテンツ.*/
    private static final String RECOMMENDCHANNEL_LIST_RECOMMENDCONTENT = "RecommendContent";
    /**おすすめ順.*/
    private static final String RECOMMENDCHANNEL_LIST_RECOMMENDORDER = "recommendOrder";
    /**サービスID.*/
    private static final String RECOMMENDCHANNEL_LIST_SERVICEID = "serviceId";
    /**カテゴリーID.*/
    private static final String RECOMMENDCHANNEL_LIST_CATEGORYID = "categoryId";
    /**チャンネルID.*/
    private static final String RECOMMENDCHANNEL_LIST_CHANNELID = "channelId";
    /**コンテンツID.*/
    private static final String RECOMMENDCHANNEL_LIST_CONTENTSID = "contentsId";
    /**タイトル.*/
    private static final String RECOMMENDCHANNEL_LIST_TITLE = "title";
    /**サムネイル用画像URL１.*/
    private static final String RECOMMENDCHANNEL_LIST_CTPICURL1 = "ctPicURL1";
    /**サムネイル用画像URL２.*/
    private static final String RECOMMENDCHANNEL_LIST_CTPICURL2 = "ctPicURL2";
    /**視聴可能期間開始日時.*/
    private static final String RECOMMENDCHANNEL_LIST_STARTVIEWING = "startViewing";
    /**視聴可能期間終了日時.*/
    private static final String RECOMMENDCHANNEL_LIST_ENDVIEWING = "endViewing";
    /**予備1.*/
    private static final String RECOMMENDCHANNEL_LIST_RESERVED1 = "reserved1";
    /**予備2.*/
    private static final String RECOMMENDCHANNEL_LIST_RESERVED2 = "reserved2";
    /**予備3.*/
    private static final String RECOMMENDCHANNEL_LIST_RESERVED3 = "reserved3";
    /**予備4.*/
    private static final String RECOMMENDCHANNEL_LIST_RESERVED4 = "reserved4";
    /**予備5.*/
    private static final String RECOMMENDCHANNEL_LIST_RESERVED5 = "reserved5";
    /**サービス契約有無.*/
    private static final String RECOMMENDCHANNEL_LIST_AGREEMENT = "agreement";
    /**契約なし動画再生可否.*/
    private static final String RECOMMENDCHANNEL_LIST_VIEWABLE = "viewable";
    /**画面ID.*/
    private static final String RECOMMENDCHANNEL_LIST_PAGEID = "pageId";
    /**ユーザグループID.*/
    private static final String RECOMMENDCHANNEL_LIST_GROUPID = "groupId";
    /**レコメンド手法ID.*/
    private static final String RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID = "recommendMethodId";

    /**
     * コンテキスト.
     *
     * @param mRecommendCallback コールバック
     */
    public RecommendWebXmlParser(final RecommendWebClient.RecommendCallback mRecommendCallback) {
        this.mRecommendCallback = mRecommendCallback;
    }

    @Override
    protected void onPostExecute(final Object s) {
        mRecommendCallback.recommendCallback((RecommendChannelList) s);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = (String) strings[0];
        return getRecommendWebList(result);
    }

    /**
     * 受け取ったレスポンスデータからXMLをパースする.
     *
     * @param responseData レスポンスデータ
     * @return パース後のデータ
     */
    private RecommendChannelList getRecommendWebList(final String responseData) {
        DTVTLogger.debugHttp(responseData);
        RecommendChannelList redChContents = null;
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
                        redChContents = new RecommendChannelList();
                        redChContentList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        switch (parser.getName()) {
                            case RECOMMENDCHANNEL_LIST_RECOMMENDCONTENT:
                                redChHashMap = new HashMap<>();
                                break;
                            case RECOMMENDCHANNEL_LIST_RECOMMENDORDER:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RECOMMENDORDER, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_SERVICEID:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_SERVICEID, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_CATEGORYID:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_CATEGORYID, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_CHANNELID:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_CHANNELID, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_CONTENTSID:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_CONTENTSID, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_TITLE:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_TITLE, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_CTPICURL1:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_CTPICURL1, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_CTPICURL2:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_CTPICURL2, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_STARTVIEWING:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_STARTVIEWING, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_ENDVIEWING:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_ENDVIEWING, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RESERVED1:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED1, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RESERVED2:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED2, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RESERVED3:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED3, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RESERVED4:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED4, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RESERVED5:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED5, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_AGREEMENT:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_AGREEMENT, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_VIEWABLE:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_VIEWABLE, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_PAGEID:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_PAGEID, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_GROUPID:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_GROUPID, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (RECOMMENDCHANNEL_LIST_RECOMMENDCONTENT.equals(parser.getName())) {
                            if (null != redChContentList) {
                                redChContentList.add(redChHashMap);
                            }
                            redChHashMap = null;
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        if (null != redChContents) {
                            redChContents.setmRcList(redChContentList);
                        }
                        endFlg = true;
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException | NullPointerException e) {
            DTVTLogger.debug(e);
        }
        return redChContents;
    }
}