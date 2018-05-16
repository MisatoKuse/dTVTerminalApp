/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser;

import android.os.AsyncTask;
import android.util.Xml;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVideoList;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendVdWebClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * おすすめビデオパーサー.
 */
public class RecommendVideoXmlParser extends AsyncTask<Object, Object, Object> {

    /**
     * おすすめビデオパーサーコールバック.
     */
    private RecommendVdWebClient.RecommendVideoCallback mRecommendVideoCallback;
    /**レコメンドコンテンツリスト.*/
    public static final String RECOMMENDVIDEO_LIST_RECOMMENDCONTENT = "RecommendContent";
    /**おすすめ順.*/
    public static final String RECOMMENDVIDEO_LIST_RECOMMENDORDER = "recommendOrder";
    /**サービスID.*/
    public static final String RECOMMENDVIDEO_LIST_SERVICEID = "serviceId";
    /**カテゴリーID.*/
    public static final String RECOMMENDVIDEO_LIST_CATEGORYID = "categoryId";
    /**チャンネルID.*/
    public static final String RECOMMENDVIDEO_LIST_CHANNELID = "channelId";
    /**コンテンツID.*/
    public static final String RECOMMENDVIDEO_LIST_CONTENTSID = "contentsId";
    /**タイトル.*/
    public static final String RECOMMENDVIDEO_LIST_TITLE = "title";
    /**サムネイル用画像URL１.*/
    public static final String RECOMMENDVIDEO_LIST_CTPICURL1 = "ctPicURL1";
    /**サムネイル用画像URL２.*/
    public static final String RECOMMENDVIDEO_LIST_CTPICURL2 = "ctPicURL2";
    /**視聴可能期間開始日時.*/
    public static final String RECOMMENDVIDEO_LIST_STARTVIEWING = "startViewing";
    /**視聴可能期間終了日時.*/
    public static final String RECOMMENDVIDEO_LIST_ENDVIEWING = "endViewing";
    /**予備1.*/
    public static final String RECOMMENDVIDEO_LIST_RESERVED1 = "reserved1";
    /**予備2.*/
    public static final String RECOMMENDVIDEO_LIST_RESERVED2 = "reserved2";
    /**予備3.*/
    public static final String RECOMMENDVIDEO_LIST_RESERVED3 = "reserved3";
    /**予備4.*/
    public static final String RECOMMENDVIDEO_LIST_RESERVED4 = "reserved4";
    /**予備5.*/
    public static final String RECOMMENDVIDEO_LIST_RESERVED5 = "reserved5";
    /**サービス契約有無.*/
    public static final String RECOMMENDVIDEO_LIST_AGREEMENT = "agreement";
    /**契約なし動画再生可否.*/
    public static final String RECOMMENDVIDEO_LIST_VIEWABLE = "viewable";
    /**画面ID.*/
    public static final String RECOMMENDVIDEO_LIST_PAGEID = "pageId";
    /**ユーザグループID.*/
    public static final String RECOMMENDVIDEO_LIST_GROUPID = "groupId";
    /**レコメンド手法ID.*/
    public static final String RECOMMENDVIDEO_LIST_RECOMMENDMETHODID = "recommendMethodId";

    /**
     * コンテキスト.
     *
     * @param mRecommendVideoCallback コールバック
     */
    public RecommendVideoXmlParser(final RecommendVdWebClient.RecommendVideoCallback mRecommendVideoCallback) {
        this.mRecommendVideoCallback = mRecommendVideoCallback;
    }

    @Override
    protected void onPostExecute(final Object s) {
        mRecommendVideoCallback.RecommendVideoCallback((RecommendVideoList) s);
    }

    @Override
    protected Object doInBackground(final Object... strings) {
        String result = String.valueOf(strings[0]);
        return getRecommendVideoList(result);
    }

    /**
     * 受け取ったレスポンスデータからXMLをパースする.
     *
     * @param responseData レスポンスデータ
     * @return パース後のデータ
     */
    private RecommendVideoList getRecommendVideoList(final String responseData) {
        DTVTLogger.debugHttp(responseData);
        RecommendVideoList redVdContents = null;
        List<Map<String, String>> redVdContentList = null;
        HashMap<String, String> redVdHashMap = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(responseData));
            int eventType = parser.getEventType();
            boolean endFlg = false;
            while (!endFlg) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        redVdContents = new RecommendVideoList();
                        redVdContentList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        switch (parser.getName()) {
                            case RECOMMENDVIDEO_LIST_RECOMMENDCONTENT:
                                redVdHashMap = new HashMap<>();
                                break;
                            case RECOMMENDVIDEO_LIST_RECOMMENDORDER:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_RECOMMENDORDER, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_SERVICEID:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_SERVICEID, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_CATEGORYID:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_CATEGORYID, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_CHANNELID:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_CHANNELID, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_CONTENTSID:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_CONTENTSID, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_TITLE:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_TITLE, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_CTPICURL1:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_CTPICURL1, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_CTPICURL2:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_CTPICURL2, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_STARTVIEWING:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_STARTVIEWING, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_ENDVIEWING:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_ENDVIEWING, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_RESERVED1:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_RESERVED1, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_RESERVED2:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_RESERVED2, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_RESERVED3:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_RESERVED3, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_RESERVED4:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_RESERVED4, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_RESERVED5:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_RESERVED5, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_AGREEMENT:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_AGREEMENT, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_VIEWABLE:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_VIEWABLE, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_PAGEID:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_PAGEID, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_GROUPID:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_GROUPID, parser.getText());
                                }
                                break;
                            case RECOMMENDVIDEO_LIST_RECOMMENDMETHODID:
                                parser.next();
                                if (null != redVdHashMap) {
                                    redVdHashMap.put(RECOMMENDVIDEO_LIST_RECOMMENDMETHODID, parser.getText());
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (RECOMMENDVIDEO_LIST_RECOMMENDCONTENT.equals(parser.getName())) {
                            if (null != redVdContentList) {
                                redVdContentList.add(redVdHashMap);
                            }
                            redVdHashMap = null;
                        }
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        if (null != redVdContents) {
                            redVdContents.setmRvList(redVdContentList);
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
        return redVdContents;
    }
}