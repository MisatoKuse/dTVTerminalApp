/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser;

import android.os.AsyncTask;
import android.text.TextUtils;
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
    public static final String RECOMMENDCHANNEL_LIST_RECOMMENDCONTENT = "RecommendContent";
    /**おすすめ順.*/
    public static final String RECOMMENDCHANNEL_LIST_RECOMMENDORDER = "recommendOrder";
    /**サービスID.*/
    public static final String RECOMMENDCHANNEL_LIST_SERVICEID = "serviceId";
    /**カテゴリーID.*/
    public static final String RECOMMENDCHANNEL_LIST_CATEGORYID = "categoryId";
    /**チャンネルID.*/
    public static final String RECOMMENDCHANNEL_LIST_CHANNELID = "channelId";
    /**コンテンツID.*/
    public static final String RECOMMENDCHANNEL_LIST_CONTENTSID = "contentsId";
    /**タイトル.*/
    public static final String RECOMMENDCHANNEL_LIST_TITLE = "title";
    /**サムネイル用画像URL１.*/
    public static final String RECOMMENDCHANNEL_LIST_CTPICURL1 = "ctPicURL1";
    /**サムネイル用画像URL２.*/
    public static final String RECOMMENDCHANNEL_LIST_CTPICURL2 = "ctPicURL2";
    /**視聴可能期間開始日時.*/
    public static final String RECOMMENDCHANNEL_LIST_STARTVIEWING = "startViewing";
    /**視聴可能期間終了日時.*/
    public static final String RECOMMENDCHANNEL_LIST_ENDVIEWING = "endViewing";
    /**予備1.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED1 = "reserved1";
    /**予備2.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED2 = "reserved2";
    /**予備3.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED3 = "reserved3";
    /**予備4.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED4 = "reserved4";
    /**予備5.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED5 = "reserved5";
    /**予備6.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED6 = "reserved6";
    /**予備7.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED7 = "reserved7";
    /**予備8.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED8 = "reserved8";
    /**予備9.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED9 = "reserved9";
    /**予備10.*/
    public static final String RECOMMENDCHANNEL_LIST_RESERVED10 = "reserved10";
    /**サービス契約有無.*/
    public static final String RECOMMENDCHANNEL_LIST_AGREEMENT = "agreement";
    /**契約なし動画再生可否.*/
    public static final String RECOMMENDCHANNEL_LIST_VIEWABLE = "viewable";
    /**画面ID.*/
    public static final String RECOMMENDCHANNEL_LIST_PAGEID = "pageId";
    /**ユーザグループID.*/
    public static final String RECOMMENDCHANNEL_LIST_GROUPID = "groupId";
    /**レコメンド手法ID.*/
    public static final String RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID = "recommendMethodId";
    /**ジャンル名.*/
    public static final String RECOMMENDCHANNEL_LIST_GENRE_NAME = "genreName";
    /**視聴エリアコード.*/
    public static final String RECOMMENDCHANNEL_LIST_AREA = "area";
    /**視聴年齢制限.*/
    public static final String RECOMMENDCHANNEL_LIST_R_RATED = "rRated";
    /**コピーライト.*/
    public static final String RECOMMENDCHANNEL_LIST_COPYRIGHT = "copyright";

    /**
     * おすすめタブページ番号.
     */
    private int mRequestPageNo;

    /**
     * コンテキスト.
     *
     * @param mRecommendCallback コールバック
     */
    public RecommendWebXmlParser(final RecommendWebClient.RecommendCallback mRecommendCallback, final int requestPageNo) {
        this.mRecommendCallback = mRecommendCallback;
        this.mRequestPageNo = requestPageNo;
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
        if (TextUtils.isEmpty(responseData)) {
            return null;
        }
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
                        redChContents.setmRequestPageNo(this.mRequestPageNo);
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
                            case RECOMMENDCHANNEL_LIST_RESERVED6:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED6, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RESERVED7:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED7, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RESERVED8:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED8, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RESERVED9:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED9, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_RESERVED10:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_RESERVED10, parser.getText() == null ? "" : parser.getText());
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
                            case RECOMMENDCHANNEL_LIST_GENRE_NAME:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_GENRE_NAME, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_AREA:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_AREA, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_R_RATED:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_R_RATED, parser.getText() == null ? "" : parser.getText());
                                }
                                break;
                            case RECOMMENDCHANNEL_LIST_COPYRIGHT:
                                parser.next();
                                if (null != redChHashMap) {
                                    redChHashMap.put(RECOMMENDCHANNEL_LIST_COPYRIGHT, parser.getText() == null ? "" : parser.getText());
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