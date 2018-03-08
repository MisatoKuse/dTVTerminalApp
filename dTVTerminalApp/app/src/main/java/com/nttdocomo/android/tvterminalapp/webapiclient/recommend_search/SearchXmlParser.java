/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Xml;

import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

/**
 * 検索結果Parser.
 */
public class SearchXmlParser extends AsyncTask<String, Integer, String> {

    /**
     * 検索結果返却用データ.
     */
    private TotalSearchResponseData searchResponse = null;
    /**
     * 検索結果NG時の返却用データ.
     */
    private TotalSearchErrorData searchError = null;
    /**
     * XMLParserリスナー.
     */
    private XMLParserFinishListener mXMLParserFinishListener = null;

    /**
     * status.
     */
    private static final String STATUS = "status";
    /**
     * status ok.
     */
    private static final String STATUS_OK = "ok";
    /**
     * status ng.
     */
    private static final String STATUS_NG = "ng";
    /**
     * totalCount.
     */
    private static final String TOTAL_COUNT = "totalCount";
    /**
     * query.
     */
    private static final String QUERY = "query";
    /**
     * startIndex.
     */
    private static final String START_INDEX = "startIndex";
    /**
     * resultCount.
     */
    private static final String RESULT_COUNT = "resultCount";
    /**
     * id.
     */
    private static final String ID = "id";
    /**
     * param.
     */
    private static final String PARAM = "param";
    /**
     * serviceCount.
     */
    private static final String SERVICE_COUNT = "serviceCount";
    /**
     * content.
     */
    private static final String CONTENT = "content";
    /**
     * contentCount.
     */
    private static final String CONTENT_COUNT = "contentCount";
    /**
     * rank.
     */
    private static final String RANK = "rank";
    /**
     * contentsId.
     */
    private static final String COUNTENTS_ID = "contentsId";
    /**
     * ctPicURL1.
     */
    private static final String CTPICURL1 = "ctPicURL1";
    /**
     * ctPicURL2.
     */
    private static final String CTPICURL2 = "ctPicURL2";
    /**
     * title.
     */
    private static final String TITLE = "title";
    /**
     * SERVICE_ID.
     */
    private static final String SERVICE_ID = "serviceId";
    /**
     * mobileViewingFlg.
     */
    private static final String MOBILEVIEWINGFLG = "mobileViewingFlg";
    /**
     * 開始時刻.
     */
    private static final String STARTVIEWING = "startViewing";
    /**
     * 終了時刻.
     */
    private static final String ENDVIEWING = "endViewing";
    /**
     * チャンネル名.
     */
    private static final String CHANNEL_NAME = "channelName";
    /**
     * チャンネルID.
     */
    private static final String CHANNEL_ID = "channelId";
    /**
     * categoryId.
     */
    private static final String CATEGORY_ID = "categoryId";
    /**
     * チャンネルID.
     */
    private static final String VIEWABLE_AGE = "viewableAge";
    /**
     * description1.
     */
    private static final String DESCRIPTION1 = "description1";
    /**
     * description2.
     */
    private static final String DESCRIPTION2 = "description2";
    /**
     * description3.
     */
    private static final String DESCRIPTION3 = "description3";
    /**
     * reserved1.
     */
    private static final String RESERVED1 = "reserved1";
    /**
     * reserved2.
     */
    private static final String RESERVED2 = "reserved2";
    /**
     * reserved3.
     */
    private static final String RESERVED3 = "reserved3";
    /**
     * reserved4.
     */
    private static final String RESERVED4 = "reserved4";
    /**
     * reserved5.
     */
    private static final String RESERVED5 = "reserved5";
    /**
     * genreName.
     */
    private static final String GENRE_NAME = "genreName";
    /**
     * paymentFlg.
     */
    private static final String PAYMENT_FLG = "paymentFlg";
    /**
     * cast.
     */
    private static final String CAST = "cast";

    /**
     * Parserのリスナー.
     */
    public interface XMLParserFinishListener {
        /**
         * Parseが正常終了した場合のコールバック.
         *
         * @param responseData 返却用データ
         */
        void onXMLParserFinish(TotalSearchResponseData responseData);

        /**
         * Parseが異常終了した場合のコールバック.
         *
         * @param errorData 返却用データ
         */
        void onXMLParserError(TotalSearchErrorData errorData);
    }

    /**
     * Parse終了時のコールバックリスナーを設定する.
     *
     * @param xMLParserFinishListener Parse終了時のコールバックリスナー
     */
    public SearchXmlParser(final XMLParserFinishListener xMLParserFinishListener) {
        mXMLParserFinishListener = xMLParserFinishListener;
    }

    @Override
    protected String doInBackground(final String... strings) {

        searchResponse = null;
        searchError = null;

        if (null == strings || 1 > strings.length) {
            setOtherError();
            return null;
        }

        if (0 == strings[0].length()) {
            DTVTLogger.debug("SearchXmlParser::doInBackground, str.length=0");
            setOtherError();
        } else {
            parse(strings[0]);
        }

        return null;
    }

    /**
     * 異常時に返却するデータを設定する.
     */
    private void setOtherError() {
        searchError = new TotalSearchErrorData(DTVTConstants.SEARCH_ERROR_ID_1,
                DTVTConstants.SEARCH_ERROR_PARAM_NULL_REQ);
    }

    @Override
    protected void onPostExecute(final String aVoid) {
        super.onPostExecute(aVoid);
        DTVTLogger.debug("MyAsyncTask.onPostExecute aVoid=" + aVoid);

        if (null != mXMLParserFinishListener) {
            if (null == searchError) {
                mXMLParserFinishListener.onXMLParserFinish(searchResponse);
            } else {
                mXMLParserFinishListener.onXMLParserError(searchError);
            }
        }
    }

    /**
     * parse処理.
     *
     * @param responseData JSONデータ
     */
    private void parse(final String responseData) {
        DTVTLogger.debug("parse");

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(responseData));
        } catch (XmlPullParserException e) {
            DTVTLogger.debug(e);
            setOtherError();
            return;
        }
        int event = 0;
        try {
            event = parser.getEventType();
        } catch (XmlPullParserException e) {
            DTVTLogger.debug(e);
            setOtherError();
            return;
        }

        boolean loop = true;
        String text = "";
        try {
            while (loop) {

                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        parserDidStartDocument();
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        loop = false;
                        parserDidEndDocument();
                        break;

                    case XmlPullParser.START_TAG:

                        String name2 = parser.getName();
                        String xmlValue = null;

                        try {
                            xmlValue = parser.nextText();
                        } catch (XmlPullParserException | IOException e) {
                            DTVTLogger.debug(e);
                        }

                        if (name2 != null && 0 < name2.length()) {
                            parseProc(parser, name2, xmlValue);
                        }

                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        String name = parser.getName();
                       // String xmlValue = null;

                        //xmlValue = parser.nextText();
                        if (name != null && 0 < name.length()) {
                            parseProc(parser, name, text);
                        }

                        break;
                    default:
                }

                event = parser.next();

            }   //while end
        } catch (Exception e) {
            DTVTLogger.debug(e);
            if (null == searchError) {
                setOtherError();
            }
        }

    }

    /**
     * parse処理の準備.
     */
    private void parserDidStartDocument() {
        DTVTLogger.debug("xml parse start");

        searchResponse = new TotalSearchResponseData();
    }

    /**
     * parse中に異常が発生した時に返却するデータの初期処理.
     */
    private void ifNullCreate() {
        if (null == searchError) {
            searchError = new TotalSearchErrorData();
        }
    }

    /**
     * parse終了時の処理.
     */
    public void parserDidEndDocument() {
        DTVTLogger.debug("xml parse finished");
    }

    /**
     * parse実処理.
     *
     * @param parser parser
     * @param tagName parseするJSONのtag
     * @param value parseする文字列
     * @throws Exception parseエラー
     */
    private void parseProc(final XmlPullParser parser, final String tagName, final String value)
            throws Exception {

        if (!TextUtils.isEmpty(tagName)) {
            switch (tagName) {
                case STATUS:
                    if (STATUS_OK.equals(value)) {
                        DTVTLogger.debug("parseProc, " + STATUS_OK);
                        searchResponse.status = STATUS_OK;
                    } else if (STATUS_NG.equals(value)) {
                        DTVTLogger.debug("parseProc, " + STATUS_NG);
                        ifNullCreate();
                        searchError.status = value;
                    }
                    break;
                case TOTAL_COUNT:
                    searchResponse.totalCount = Integer.parseInt(value);
                    break;
                case QUERY:
                    searchResponse.query = value;
                    break;
                case START_INDEX:
                    searchResponse.startIndex = Integer.parseInt(value);
                    break;
                case RESULT_COUNT:
                    searchResponse.resultCount = Integer.parseInt(value);
                    break;
                case SERVICE_COUNT:
                    if (null == value) {
                        searchResponse.appendServiceCount();
                    }
                    break;
                case CONTENT:
                    if (null == value) {
                        searchResponse.appendContent();
                    }
                    break;
                case SERVICE_ID:
                    if (searchResponse.contentList.size() > 0) { //parse content serviceId
                        int currentContentListIndex = searchResponse.contentList.size() - 1;
                        TotalSearchResponseData.Content content = searchResponse.contentList.get(currentContentListIndex);
                        content.mServiceId = Integer.parseInt(value);
                        searchResponse.contentList.set(currentContentListIndex, content);
                    } else {
                        int currentServiceCountListIndex = searchResponse.serviceCountList.size() - 1;
                        TotalSearchResponseData.ServiceCount serviceCount = searchResponse.serviceCountList.get(currentServiceCountListIndex);
                        serviceCount.mServiceId = Integer.parseInt(value);
                        searchResponse.serviceCountList.set(currentServiceCountListIndex, serviceCount);
                    }
                    break;
                case CONTENT_COUNT:
                    int currentServiceCountListIndex = searchResponse.serviceCountList.size() - 1;
                    TotalSearchResponseData.ServiceCount serviceCount = searchResponse.serviceCountList.get(currentServiceCountListIndex);
                    serviceCount.mContentCount = Integer.parseInt(value);
                    searchResponse.serviceCountList.set(currentServiceCountListIndex, serviceCount);
                    break;
                case RANK:
                    int currentRankIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content rankContent = searchResponse.contentList.get(currentRankIndex);
                    rankContent.mRank = Integer.parseInt(value);
                    searchResponse.contentList.set(currentRankIndex, rankContent);
                    break;
                case COUNTENTS_ID:
                    int currentContentIdIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentIdContent = searchResponse.contentList.get(currentContentIdIndex);
                    contentIdContent.mContentsId = value;
                    searchResponse.contentList.set(currentContentIdIndex, contentIdContent);
                    break;
                case CTPICURL1:
                    int currentCtPicURL1Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentCtPicURL1 = searchResponse.contentList.get(currentCtPicURL1Index);
                    contentCtPicURL1.mCtPicURL1 = value;
                    searchResponse.contentList.set(currentCtPicURL1Index, contentCtPicURL1);
                    break;
                case CTPICURL2:
                    int currentCtPicURL2Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentCtPicURL2 = searchResponse.contentList.get(currentCtPicURL2Index);
                    contentCtPicURL2.mCtPicURL2 = value;
                    searchResponse.contentList.set(currentCtPicURL2Index, contentCtPicURL2);
                    break;
                case TITLE:
                    int currentTitleIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentTitle = searchResponse.contentList.get(currentTitleIndex);
                    contentTitle.mTitle = value;
                    searchResponse.contentList.set(currentTitleIndex, contentTitle);
                    break;
                case MOBILEVIEWINGFLG:
                    int currentMobileIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentMoble = searchResponse.contentList.get(currentMobileIndex);
                    contentMoble.mMobileViewingFlg = value;
                    break;
                case STARTVIEWING:
                    int currentStartViewingIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentStartViewing = searchResponse.contentList.get(currentStartViewingIndex);
                    contentStartViewing.mStartViewing = value;
                    break;
                case ENDVIEWING:
                    int currentEndViewingIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentEndViewing = searchResponse.contentList.get(currentEndViewingIndex);
                    contentEndViewing.mEndViewing = value;
                    break;
                case CHANNEL_NAME:
                    int currentChannelNameIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentChannelName = searchResponse.contentList.get(currentChannelNameIndex);
                    contentChannelName.mChannelName = value;
                    break;
                case CHANNEL_ID:
                    int currentChannelIdIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentChannelId = searchResponse.contentList.get(currentChannelIdIndex);
                    contentChannelId.mChannelId = value;
                    break;
                case VIEWABLE_AGE:
                    int currentAgeIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentAge = searchResponse.contentList.get(currentAgeIndex);
                    contentAge.mViewableAge = value;
                    break;
                case CATEGORY_ID:
                    int currentCategoryIdIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentCategoryId = searchResponse.contentList.get(currentCategoryIdIndex);
                    contentCategoryId.mCategoryId = value;
                    break;
                case DESCRIPTION1:
                    int currentDescription1Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentDescription1 = searchResponse.contentList.get(currentDescription1Index);
                    contentDescription1.mDescription1 = value;
                    break;
                case DESCRIPTION2:
                    int currentDescription2Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentDescription2 = searchResponse.contentList.get(currentDescription2Index);
                    contentDescription2.mDescription2 = value;
                    break;
                case DESCRIPTION3:
                    int currentDescription3Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentDescription3 = searchResponse.contentList.get(currentDescription3Index);
                    contentDescription3.mDescription3 = value;
                    break;
                case RESERVED1:
                    int currentReserved1Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentReserved1 = searchResponse.contentList.get(currentReserved1Index);
                    contentReserved1.mReserved1 = value;
                    break;
                case RESERVED2:
                    int currentReserved2Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentReserved2 = searchResponse.contentList.get(currentReserved2Index);
                    contentReserved2.mReserved2 = value;
                    break;
                case RESERVED3:
                    int currentReserved3Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentReserved3 = searchResponse.contentList.get(currentReserved3Index);
                    contentReserved3.mReserved3 = value;
                    break;
                case RESERVED4:
                    int currentReserved4Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentReserved4 = searchResponse.contentList.get(currentReserved4Index);
                    contentReserved4.mReserved4 = value;
                    break;
                case RESERVED5:
                    int currentReserved5Index = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentReserved5 = searchResponse.contentList.get(currentReserved5Index);
                    contentReserved5.mReserved5 = value;
                    break;
                case GENRE_NAME:
                    int currentGenreNameIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentGenreName = searchResponse.contentList.get(currentGenreNameIndex);
                    contentGenreName.mGenreName = value;
                    break;
                case PAYMENT_FLG:
                    int currentPaymentFlgIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentPaymentFlg = searchResponse.contentList.get(currentPaymentFlgIndex);
                    contentPaymentFlg.mPaymentFlg = value;
                    break;
                case CAST:
                    int currentCastIndex = searchResponse.contentList.size() - 1;
                    TotalSearchResponseData.Content contentCast = searchResponse.contentList.get(currentCastIndex);
                    contentCast.mCast = value;
                    break;
                case ID:
                    ifNullCreate();
                    searchError.error.id = value;
                    break;
                case PARAM:
                    ifNullCreate();
                    searchError.error.param = value;
                    break;
                default:
                    break;
            }
        }
    }
}
