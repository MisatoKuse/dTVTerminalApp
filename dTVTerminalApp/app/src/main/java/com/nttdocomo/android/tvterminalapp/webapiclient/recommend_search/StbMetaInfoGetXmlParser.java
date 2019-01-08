/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Xml;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

/**
 * STBメタデータParser.
 */
class StbMetaInfoGetXmlParser extends AsyncTask<String, Integer, String> {

    /**
     * STBメタデータ取得成功時返却用データ.
     */
    private StbMetaInfoResponseData searchResponse = null;
    /**
     * STBメタデータ取得失敗時の返却用データ.
     */
    private StbMetaInfoGetErrorData searchError = null;
    /**
     * XMLParserリスナー.
     */
    private XMLParserFinishListener mXMLParserFinishListener;
    /**
     * エピソード情報処理フラグ.
     */
    private boolean mIsEpisodeInfoProcessing = false;

    /**
     * 処理結果.
     */
    private static final String STATUS = "status";
    /**
     * 処理結果メッセージ ok.
     */
    private static final String STATUS_MSG_OK = "ok";
    /**
     * 処理結果メッセージ ng.
     */
    private static final String STATUS_MSG_NG = "ng";
    /**
     * 検索結果合計件数.
     */
    private static final String TOTAL_COUNT = "totalCount";
    /**
     * id.
     */
    private static final String ID = "id";
    /**
     * param.
     */
    private static final String PARAM = "param";
    /**
     * content.
     */
    private static final String CONTENT = "content";
    /**
     * serviceId.
     */
    private static final String SERVICE_ID = "serviceId";
    /**
     * categoryId.
     */
    private static final String CATEGORY_ID = "categoryId";
    /**
     * channelId.
     */
    private static final String CHANNEL_ID = "channelId";
    /**
     * contentsId.
     */
    private static final String CONTENTS_ID = "contentsId";
    /**
     * title.
     */
    private static final String TITLE = "title";
    /**
     * channelName.
     */
    private static final String CHANNEL_NAME = "channelName";
    /**
     * genreName.
     */
    private static final String GENRE_NAME = "genreName";
    /**
     * ctPicURL1.
     */
    private static final String CTPICURL1 = "ctPicURL1";
    /**
     * ctPicURL2.
     */
    private static final String CTPICURL2 = "ctPicURL2";
    /**
     * startViewing.
     */
    private static final String START_VIEWING = "startViewing";
    /**
     * endViewing.
     */
    private static final String END_VIEWING = "endViewing";
    /**
     * paymentFlg.
     */
    private static final String PAYMENT_FLG = "paymentFlg";
    /**
     * mobileViewingFlg.
     */
    private static final String MOBILE_VIEWING_FLG = "mobileViewingFlg";
    /**
     * viewableAge.
     */
    private static final String VIEWABLE_AGE = "viewableAge";
    /**
     * titleKind.
     */
    private static final String TITLE_KIND = "titleKind";
    /**
     * director.
     */
    private static final String DIRECTOR = "director";
    /**
     * cast.
     */
    private static final String CAST = "cast";
    /**
     * artist.
     */
    private static final String ARTIST = "artist";
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
     * productionYear.
     */
    private static final String PRODUCTION_YEAR = "productionYear";
    /**
     * copyright.
     */
    private static final String COPYRIGHT = "copyright";
    /**
     * contentsLength.
     */
    private static final String CONTENTS_LENGTH = "contentsLength";
    /**
     * area.
     */
    private static final String AREA = "area";
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
     * reserved6.
     */
    private static final String RESERVED6 = "reserved6";
    /**
     * reserved7.
     */
    private static final String RESERVED7 = "reserved7";
    /**
     * reserved8.
     */
    private static final String RESERVED8 = "reserved8";
    /**
     * reserved9.
     */
    private static final String RESERVED9 = "reserved9";
    /**
     * reserved10.
     */
    private static final String RESERVED10 = "reserved10";
    /**
     * totalEpisodeCount.
     */
    private static final String TOTAL_EPISODE_COUNT = "totalEpisodeCount";
    /**
     * episode.
     */
    private static final String EPISODE = "episode";
    /**
     * episodeId.
     */
    private static final String EPISODE_ID = "episodeId";
    /**
     * subTitle.
     */
    private static final String SUB_TITLE = "subTitle";
    /**
     * summary.
     */
    private static final String SUMMARY = "summary";
    /**
     * episodeNumber.
     */
    private static final String EPISODE_NUMBER = "episodeNumber";
    /**
     * episodeNumberNotation.
     */
    private static final String EPISODE_NUMBER_NOTATION = "episodeNumberNotation";


    /**
     * Parserのリスナー.
     */
    public interface XMLParserFinishListener {
        /**
         * Parseが正常終了した場合のコールバック.
         *
         * @param responseData 返却用データ
         */
        void onXMLParserFinish(StbMetaInfoResponseData responseData);

        /**
         * Parseが異常終了した場合のコールバック.
         *
         * @param errorData 返却用データ
         */
        void onXMLParserError(StbMetaInfoGetErrorData errorData);
    }

    /**
     * Parse終了時のコールバックリスナーを設定する.
     *
     * @param xMLParserFinishListener Parse終了時のコールバックリスナー
     */
    StbMetaInfoGetXmlParser(final XMLParserFinishListener xMLParserFinishListener) {
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
        searchError = new StbMetaInfoGetErrorData(DtvtConstants.SEARCH_ERROR_ID_1,
                DtvtConstants.SEARCH_ERROR_PARAM_NULL_REQ);
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
        int event;
        try {
            event = parser.getEventType();
        } catch (XmlPullParserException e) {
            DTVTLogger.debug(e);
            setOtherError();
            return;
        }

        boolean loop = true;
        String xmlValue = null;
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
                        xmlValue = null;
                        try {
                            xmlValue = parser.nextText();
                        } catch (XmlPullParserException | IOException e) {
                            DTVTLogger.debug(e);
                        }

                        if (name2 != null && 0 < name2.length() && xmlValue != null) {
                            parseProc(name2, xmlValue);
                        }

                        break;
                    case XmlPullParser.TEXT:
                        xmlValue = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        String name = parser.getName();
                        if (name != null && 0 < name.length() && xmlValue != null) {
                            parseProc(name, xmlValue);
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

        searchResponse = new StbMetaInfoResponseData();
    }

    /**
     * parse中に異常が発生した時に返却するデータの初期処理.
     */
    private void ifNullCreate() {
        if (null == searchError) {
            searchError = new StbMetaInfoGetErrorData();
        }
    }

    /**
     * parse終了時の処理.
     */
    private void parserDidEndDocument() {
        DTVTLogger.debug("xml parse finished");
    }

    /**
     * parse実処理.
     *
     * @param tagName parseするJSONのtag
     * @param value parseする文字列
     */
    private void parseProc(final String tagName, final String value) {

        try {
            if (!TextUtils.isEmpty(tagName)) {
                switch (tagName) {
                    case STATUS:
                        int status = Integer.parseInt(value);
                        if (DtvtConstants.SEARCH_STATUS_OK == status) {
                            DTVTLogger.debug("parseProc, " + STATUS_MSG_OK);
                            searchResponse.setStatus(DtvtConstants.SEARCH_STATUS_OK);
                        } else if (DtvtConstants.SEARCH_STATUS_NG == status) {
                            DTVTLogger.debug("parseProc, " + STATUS_MSG_NG);
                            ifNullCreate();
                            searchError.status = DtvtConstants.SEARCH_STATUS_NG;
                        }
                        break;
                    case TOTAL_COUNT:
                        searchResponse.setTotalCount(Integer.parseInt(value));
                    case CONTENT:
                        break;
                    case SERVICE_ID:
                        searchResponse.getContent().mServiceId = value;
                        break;
                    case CATEGORY_ID:
                        searchResponse.getContent().mCategoryId = value;
                        break;
                    case CHANNEL_ID:
                        searchResponse.getContent().mChannelId = value;
                        break;
                    case CONTENTS_ID:
                        searchResponse.getContent().mContentsId = value;
                        break;
                    case TITLE:
                        searchResponse.getContent().mTitle = value;
                        break;
                    case CHANNEL_NAME:
                        searchResponse.getContent().mChannelName = value;
                        break;
                    case GENRE_NAME:
                        searchResponse.getContent().mGenreName = value;
                        break;
                    case CTPICURL1:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mCtPicURL1 = value;
                        } else {
                            searchResponse.getContent().mCtPicURL1 = value;
                        }
                        break;
                    case CTPICURL2:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mCtPicURL2 = value;
                        } else {
                            searchResponse.getContent().mCtPicURL2 = value;
                        }
                        break;
                    case START_VIEWING:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mStartViewing = value;
                        } else {
                            searchResponse.getContent().mStartViewing = value;
                        }
                        break;
                    case END_VIEWING:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mEndViewing = value;
                        } else {
                            searchResponse.getContent().mEndViewing = value;
                        }
                        break;
                    case PAYMENT_FLG:
                        searchResponse.getContent().mPaymentFlg = value;
                        break;
                    case MOBILE_VIEWING_FLG:
                        searchResponse.getContent().mMobileViewingFlg = value;
                        break;
                    case VIEWABLE_AGE:
                        searchResponse.getContent().mViewableAge = value;
                        break;
                    case TITLE_KIND:
                        searchResponse.getContent().mTitleKind = value;
                        break;
                    case DIRECTOR:
                        searchResponse.getContent().mDirector = value;
                        break;
                    case CAST:
                        searchResponse.getContent().mCast = value;
                        break;
                    case ARTIST:
                        searchResponse.getContent().mArtist = value;
                        break;
                    case DESCRIPTION1:
                        searchResponse.getContent().mDescription1 = value;
                        break;
                    case DESCRIPTION2:
                        searchResponse.getContent().mDescription2 = value;
                        break;
                    case DESCRIPTION3:
                        searchResponse.getContent().mDescription3 = value;
                        break;
                    case PRODUCTION_YEAR:
                        searchResponse.getContent().mProductionYear = value;
                        break;
                    case COPYRIGHT:
                        searchResponse.getContent().mCopyright = value;
                        break;
                    case CONTENTS_LENGTH:

                        int contentsLength = TextUtils.isEmpty(value) ? 0 : Integer.parseInt(value);

                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mContentsLength = contentsLength;
                        } else {
                            searchResponse.getContent().mContentsLength = contentsLength;
                        }
                        break;
                    case AREA:
                        searchResponse.getContent().mArea = value;
                        break;
                    case RESERVED1:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved1 = value;
                        } else {
                            searchResponse.getContent().mReserved1 = value;
                        }
                        break;
                    case RESERVED2:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved2 = value;
                        } else {
                            searchResponse.getContent().mReserved2 = value;
                        }
                        break;
                    case RESERVED3:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved3 = value;
                        } else {
                            searchResponse.getContent().mReserved3 = value;
                        }
                        break;
                    case RESERVED4:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved4 = value;
                        } else {
                            searchResponse.getContent().mReserved4 = value;
                        }
                        break;
                    case RESERVED5:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved5 = value;
                        } else {
                            searchResponse.getContent().mReserved5 = value;
                        }
                        break;
                    case RESERVED6:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved6 = value;
                        } else {
                            searchResponse.getContent().mReserved6 = value;
                        }
                        break;
                    case RESERVED7:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved7 = value;
                        } else {
                            searchResponse.getContent().mReserved7 = value;
                        }
                        break;
                    case RESERVED8:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved8 = value;
                        } else {
                            searchResponse.getContent().mReserved8 = value;
                        }
                        break;
                    case RESERVED9:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved9 = value;
                        } else {
                            searchResponse.getContent().mReserved9 = value;
                        }
                        break;
                    case RESERVED10:
                        if (mIsEpisodeInfoProcessing) {
                            searchResponse.getLastEpisode().mReserved10 = value;
                        } else {
                            searchResponse.getContent().mReserved10 = value;
                        }
                        break;
                    case TOTAL_EPISODE_COUNT:
                        searchResponse.getContent().mTotalEpisodeCount = Integer.parseInt(value);
                        break;
                    case EPISODE:
                        mIsEpisodeInfoProcessing = true;
                        searchResponse.appendEpisode();
                        break;
                    case EPISODE_ID:
                        searchResponse.getLastEpisode().mEpisodeId = value;
                        break;
                    case SUB_TITLE:
                        searchResponse.getLastEpisode().mSubTitle = value;
                        break;
                    case SUMMARY:
                        searchResponse.getLastEpisode().mSummary = value;
                        break;
                    case EPISODE_NUMBER:
                        searchResponse.getLastEpisode().mEpisodeNumber = value;
                        break;
                    case EPISODE_NUMBER_NOTATION:
                        searchResponse.getLastEpisode().mEpisodeNumberNotation = value;
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
        } catch (NumberFormatException e) {
            DTVTLogger.debug(e);
        }
    }
}
