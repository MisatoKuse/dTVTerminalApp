/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser.USER_INFO_LIST_LOGGEDIN_ACCOUNT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_ADULT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_BVFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_CID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_CRID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DEMONG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DTV;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_DUR;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_MS;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VodClipJsonParser.VODCLIP_LIST_TITLE_ID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_AGREEMENT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_GROUPID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_PAGEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED3;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED5;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_VIEWABLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_AGREEMENT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CATEGORYID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CHANNELID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CONTENTSID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_CTPICURL2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_ENDVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_GROUPID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_PAGEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RECOMMENDMETHODID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RECOMMENDORDER;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED2;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED3;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED4;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_RESERVED5;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_SERVICEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_STARTVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser.RECOMMENDVIDEO_LIST_VIEWABLE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_CRID;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_CID;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_TITLE_ID;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_EPISODE_ID;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_TITLE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_EPITITLE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_TITLERUBY;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_DISP_TYPE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_DISPLAY_START_DATE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_DISPLAY_END_DATE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_AVAIL_START_DATE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_AVAIL_END_DATE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_PUBLISH_START_DATE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_PUBLISH_END_DATE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_NEWA_START_DATE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_NEWA_END_DATE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_COPYRIGHT;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_THUMB;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_DUR;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_DEMONG;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_BVFLG;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_HDRFLG;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_AVAIL_STATUS;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_DELIVERY;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_R_VALUE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_ADULT;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_MS;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_NG_FUNC;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_GENRE_ID_ARRAY;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_SYNOP;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_PUID;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_PRICE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_QRANGE;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_QUNIT;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_PU_S;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_PU_E;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_CREDITS;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_RATING;
import static com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData.VOD_META_FULL_DATA_DTV;

public class DBConstants {

    /**
     * DB吁E
     */
    public static final String DATABASE_NAME = "db_data";

    /**
     * DBVersion
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * Jsonのキー名が数字から始まってる時の対策用定数
     */
    public static final String FOUR_K_FLG = "4kflg";
    public static final String UNDER_BAR_FOUR_K_FLG = "_4kflg";

    //Homeキャッシュデータ格納用テーブル
    public static final String CHANNEL_LIST_TABLE_NAME = "channel_list";
    private static final String ID_COLUMN = "row_id";
    public static final String UPDATE_DATE = "update_date";
    public static final String DATE_TYPE = "date_type";

    public static final String CREATE_TABLE_CHANNEL_SQL = "" +
            "create table " + CHANNEL_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_SERVICE_ID  + " text, " +
            JsonContents.META_RESPONSE_CHNO  + " text, " +
            JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_TITLERUBY   + " text, " +
            JsonContents.META_RESPONSE_DISP_TYPE  + " text, " +
            JsonContents.META_RESPONSE_SERVICE  + " text, " +
            JsonContents.META_RESPONSE_CH_TYPE  + " text, " +
            JsonContents.META_RESPONSE_AVAIL_START_DATE  + " text, " +
            JsonContents.META_RESPONSE_AVAIL_END_DATE  + " text, " +
            JsonContents.META_RESPONSE_DEFAULT_THUMB  + " text, " +
            JsonContents.META_RESPONSE_THUMB_640  + " text, " +
            JsonContents.META_RESPONSE_THUMB_448  + " text, " +
            JsonContents.META_RESPONSE_DEMONG  + " text, " +
            UNDER_BAR_FOUR_K_FLG  + " text, " +
            JsonContents.META_RESPONSE_AVAIL_STATUS  + " text, " +
            JsonContents.META_RESPONSE_DELIVERY  + " text, " +
            JsonContents.META_RESPONSE_R_VALUE  + " text, " +
            JsonContents.META_RESPONSE_ADULT  + " text, " +
            JsonContents.META_RESPONSE_NG_FUNC  + " text, " +
            JsonContents.META_RESPONSE_GENRE_ARRAY  + " text, " +
            JsonContents.META_RESPONSE_SYNOP  + " text, " +
            JsonContents.META_RESPONSE_CHSVOD  + " text, " +
            JsonContents.META_RESPONSE_PUID  + " text, " +
            JsonContents.META_RESPONSE_SUB_PUID  + " text, " +
            JsonContents.META_RESPONSE_PRICE  + " text, " +
            JsonContents.META_RESPONSE_QRANGE  + " text, " +
            JsonContents.META_RESPONSE_QUNIT  + " text, " +
            JsonContents.META_RESPONSE_PU_START_DATE  + " text, " +
            JsonContents.META_RESPONSE_PU_END_DATE  + " text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_TITLE +" text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_DISP_TYPE +" text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PUID +" text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_SUB_PUID +" text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PRICE +" text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QRANGE +" text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QUNIT +" text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_START_DATE +" text, " +
            JsonContents.META_RESPONSE_CHPACK  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_END_DATE +" text, " +
            DATE_TYPE + " text, " +
            UPDATE_DATE + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String DAILYRANK_LIST_TABLE_NAME = "daily_rank_list";
    public static final String CREATE_TABLE_DAILY_RANK_SQL = "" +
            "create table " + DAILYRANK_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_CID + " text, " +
            JsonContents.META_RESPONSE_TITLE_ID + " text, " +
            JsonContents.META_RESPONSE_EPISODE_ID + " text, " +
            JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_TITLERUBY + " text, " +
            JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_START_DATE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_END_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_START_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_END_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_END_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_START_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_END_DATE + " text, " +
            JsonContents.META_RESPONSE_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_COPYRIGHT + " text, " +
            JsonContents.META_RESPONSE_DUR + " text, " +
            JsonContents.META_RESPONSE_DEMONG + " text, " +
            JsonContents.META_RESPONSE_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            JsonContents.META_RESPONSE_HDRFLG + " text, " +
            JsonContents.META_RESPONSE_DELIVERY + " text, " +
            JsonContents.META_RESPONSE_R_VALUE + " text, " +
            JsonContents.META_RESPONSE_ADULT + " text, " +
            JsonContents.META_RESPONSE_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_SYNOP + " text, " +
            JsonContents.META_RESPONSE_SYNOP_SHORT + " text, " +
            JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CREDIT_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RATING + " text, " +
            JsonContents.META_RESPONSE_DTV + " text, " +
            JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_SEARCH_OK + " text, " +
            JsonContents.META_RESPONSE_LIINF_ARRAY + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CAPL + " text, " +
            JsonContents.META_RESPONSE_BILINGAL + " text, " +
            JsonContents.META_RESPONSE_TV_CID + " text, " +
            JsonContents.META_RESPONSE_SERVICE_ID + " text, " +
            JsonContents.META_RESPONSE_EVENT_ID + " text, " +
            JsonContents.META_RESPONSE_CHNO + " text, " +
            JsonContents.META_RESPONSE_TV_SERVICE + " text, " +
            JsonContents.META_RESPONSE_CONTENT_TYPE + " text, " +
            JsonContents.META_RESPONSE_VOD_START_DATE + " text, " +
            JsonContents.META_RESPONSE_VOD_END_DATE + " text, " +
            JsonContents.META_RESPONSE_MAIN_GENRE + " text, " +
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_COPY + " text, " +
            JsonContents.META_RESPONSE_ADINFO_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String TV_SCHEDULE_LIST_TABLE_NAME = "tv_schedule_list";
    public static final String CREATE_TABLE_TV_SCHEDULE_SQL = "" +
            "create table " + TV_SCHEDULE_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_CID + " text, " +
            JsonContents.META_RESPONSE_TITLE_ID + " text, " +
            JsonContents.META_RESPONSE_EPISODE_ID + " text, " +
            JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_TITLERUBY + " text, " +
            JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_START_DATE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_END_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_START_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_END_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_END_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_START_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_END_DATE + " text, " +
            JsonContents.META_RESPONSE_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_COPYRIGHT + " text, " +
            JsonContents.META_RESPONSE_DUR + " text, " +
            JsonContents.META_RESPONSE_DEMONG + " text, " +
            JsonContents.META_RESPONSE_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            JsonContents.META_RESPONSE_HDRFLG + " text, " +
            JsonContents.META_RESPONSE_DELIVERY + " text, " +
            JsonContents.META_RESPONSE_R_VALUE + " text, " +
            JsonContents.META_RESPONSE_ADULT + " text, " +
            JsonContents.META_RESPONSE_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_SYNOP + " text, " +
            JsonContents.META_RESPONSE_SYNOP_SHORT + " text, " +
            JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CREDIT_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RATING + " text, " +
            JsonContents.META_RESPONSE_DTV + " text, " +
            JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_SEARCH_OK + " text, " +
            JsonContents.META_RESPONSE_LIINF_ARRAY + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CAPL + " text, " +
            JsonContents.META_RESPONSE_BILINGAL + " text, " +
            JsonContents.META_RESPONSE_TV_CID + " text, " +
            JsonContents.META_RESPONSE_SERVICE_ID + " text, " +
            JsonContents.META_RESPONSE_EVENT_ID + " text, " +
            JsonContents.META_RESPONSE_CHNO + " text, " +
            JsonContents.META_RESPONSE_TV_SERVICE + " text, " +
            JsonContents.META_RESPONSE_CONTENT_TYPE + " text, " +
            JsonContents.META_RESPONSE_VOD_START_DATE + " text, " +
            JsonContents.META_RESPONSE_VOD_END_DATE + " text, " +
            JsonContents.META_RESPONSE_MAIN_GENRE + " text, " +
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_COPY + " text, " +
            JsonContents.META_RESPONSE_ADINFO_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String USER_INFO_LIST_TABLE_NAME = "user_info_list";
    public static final String CREATE_TABLE_USER_INFO_SQL = "" +
            "create table " + USER_INFO_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            USER_INFO_LIST_LOGGEDIN_ACCOUNT + " text, " +
            USER_INFO_LIST_CONTRACT_STATUS + " text, " +
            USER_INFO_LIST_DCH_AGE_REQ +" text," +
            USER_INFO_LIST_H4D_AGE_REQ + " text" +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String VODCLIP_LIST_TABLE_NAME = "vod_clip_list";
    public static final String CREATE_TABLE_VODCLIP_LIST_SQL = "" +
            "create table " + VODCLIP_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            VODCLIP_LIST_CRID + " text, " +
            VODCLIP_LIST_CID + " text, " +
            VODCLIP_LIST_TITLE_ID + " text, " +
            VODCLIP_LIST_EPISODE_ID + " text, " +
            VODCLIP_LIST_TITLE + " text, " +
            VODCLIP_LIST_EPITITLE + " text, " +
            VODCLIP_LIST_DISP_TYPE + " text, " +
            VODCLIP_LIST_DISPLAY_START_DATE + " text, " +
            VODCLIP_LIST_DISPLAY_END_DATE + " text, " +
            VODCLIP_LIST_AVAIL_START_DATE + " text, " +
            VODCLIP_LIST_AVAIL_END_DATE + " text, " +
            VODCLIP_LIST_PUBLISH_START_DATE + " text, " +
            VODCLIP_LIST_PUBLISH_END_DATE + " text, " +
            VODCLIP_LIST_NEWA_START_DATE + " text, " +
            VODCLIP_LIST_NEWA_END_DATE + " text, " +
            VODCLIP_LIST_COPYRIGHT + " text, " +
            VODCLIP_LIST_THUMB + " text, " +
            VODCLIP_LIST_DUR + " text, " +
            VODCLIP_LIST_DEMONG + " text, " +
            VODCLIP_LIST_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            VODCLIP_LIST_HDRFLG + " text, " +
            VODCLIP_LIST_AVAIL_STATUS + " text, " +
            VODCLIP_LIST_DELIVERY + " text, " +
            VODCLIP_LIST_R_VALUE + " text, " +
            VODCLIP_LIST_ADULT + " text, " +
            VODCLIP_LIST_MS + " text, " +
            VODCLIP_LIST_NG_FUNC + " text, " +
            VODCLIP_LIST_GENRE_ID_ARRAY + " text, " +
            VODCLIP_LIST_DTV + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String WEEKLYRANK_LIST_TABLE_NAME = "weekly_rank_list";
    public static final String CREATE_TABLE_WEEKLYRANK_SQL = "" +
            "create table " + WEEKLYRANK_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_CID + " text, " +
            JsonContents.META_RESPONSE_TITLE_ID + " text, " +
            JsonContents.META_RESPONSE_EPISODE_ID + " text, " +
            JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_TITLERUBY + " text, " +
            JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_START_DATE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_END_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_START_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_END_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_END_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_START_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_END_DATE + " text, " +
            JsonContents.META_RESPONSE_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_COPYRIGHT + " text, " +
            JsonContents.META_RESPONSE_DUR + " text, " +
            JsonContents.META_RESPONSE_DEMONG + " text, " +
            JsonContents.META_RESPONSE_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            JsonContents.META_RESPONSE_HDRFLG + " text, " +
            JsonContents.META_RESPONSE_DELIVERY + " text, " +
            JsonContents.META_RESPONSE_R_VALUE + " text, " +
            JsonContents.META_RESPONSE_ADULT + " text, " +
            JsonContents.META_RESPONSE_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_SYNOP + " text, " +
            JsonContents.META_RESPONSE_SYNOP_SHORT + " text, " +
            JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CREDIT_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RATING + " text, " +
            JsonContents.META_RESPONSE_DTV + " text, " +
            JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_SEARCH_OK + " text, " +
            JsonContents.META_RESPONSE_LIINF_ARRAY + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CAPL + " text, " +
            JsonContents.META_RESPONSE_BILINGAL + " text, " +
            JsonContents.META_RESPONSE_TV_CID + " text, " +
            JsonContents.META_RESPONSE_SERVICE_ID + " text, " +
            JsonContents.META_RESPONSE_EVENT_ID + " text, " +
            JsonContents.META_RESPONSE_CHNO + " text, " +
            JsonContents.META_RESPONSE_TV_SERVICE + " text, " +
            JsonContents.META_RESPONSE_CONTENT_TYPE + " text, " +
            JsonContents.META_RESPONSE_VOD_START_DATE + " text, " +
            JsonContents.META_RESPONSE_VOD_END_DATE + " text, " +
            JsonContents.META_RESPONSE_MAIN_GENRE + " text, " +
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_COPY + " text, " +
            JsonContents.META_RESPONSE_ADINFO_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String RECOMMEND_CHANNEL_LIST_TABLE_NAME = "recommend_channel_list";
    public static final String CREATE_TABLE_RECOMMEND_CHANNEL_SQL = "" +
            "create table " + RECOMMEND_CHANNEL_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDORDER + " text, " +
            RECOMMENDCHANNEL_LIST_SERVICEID + " text, " +
            RECOMMENDCHANNEL_LIST_CATEGORYID + " text, " +
            RECOMMENDCHANNEL_LIST_CHANNELID + " text, " +
            RECOMMENDCHANNEL_LIST_CONTENTSID + " text, " +
            RECOMMENDCHANNEL_LIST_TITLE + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL1 + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL2 + " text, " +
            RECOMMENDCHANNEL_LIST_STARTVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_ENDVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED1 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED2 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED3 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED4 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED5 + " text, " +
            RECOMMENDCHANNEL_LIST_AGREEMENT + " text, " +
            RECOMMENDCHANNEL_LIST_VIEWABLE + " text, " +
            RECOMMENDCHANNEL_LIST_PAGEID + " text, " +
            RECOMMENDCHANNEL_LIST_GROUPID + " text, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID + " text " +
            ")";

    //Homeキャッシュデータ格納用テーブル
    public static final String RECOMMEND_VIDEO_LIST_TABLE_NAME = "recommend_video_list";
    public static final String CREATE_TABLE_RECOMMEND_VIDEO_SQL = "" +
            "create table " + RECOMMEND_VIDEO_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            RECOMMENDVIDEO_LIST_RECOMMENDORDER + " text, " +
            RECOMMENDVIDEO_LIST_SERVICEID + " text, " +
            RECOMMENDVIDEO_LIST_CATEGORYID + " text, " +
            RECOMMENDVIDEO_LIST_CHANNELID + " text, " +
            RECOMMENDVIDEO_LIST_CONTENTSID + " text, " +
            RECOMMENDVIDEO_LIST_TITLE + " text, " +
            RECOMMENDVIDEO_LIST_CTPICURL1 + " text, " +
            RECOMMENDVIDEO_LIST_CTPICURL2 + " text, " +
            RECOMMENDVIDEO_LIST_STARTVIEWING + " text, " +
            RECOMMENDVIDEO_LIST_ENDVIEWING + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED1 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED2 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED3 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED4 + " text, " +
            RECOMMENDVIDEO_LIST_RESERVED5 + " text, " +
            RECOMMENDVIDEO_LIST_AGREEMENT + " text, " +
            RECOMMENDVIDEO_LIST_VIEWABLE + " text, " +
            RECOMMENDVIDEO_LIST_PAGEID + " text, " +
            RECOMMENDVIDEO_LIST_GROUPID + " text, " +
            RECOMMENDVIDEO_LIST_RECOMMENDMETHODID + " text " +
            ")";

    //ランキングキャッシュデータ格納用テーブル
    public static final String RANKING_VIDEO_LIST_TABLE_NAME = "ranking_video_list";
    public static final String CREATE_TABLE_RANKING_VIDEO_SQL = "" +
            "create table " + RANKING_VIDEO_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_CID + " text, " +
            JsonContents.META_RESPONSE_TITLE_ID + " text, " +
            JsonContents.META_RESPONSE_EPISODE_ID + " text, " +
            JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_TITLERUBY + " text, " +
            JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_START_DATE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_END_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_START_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_END_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_END_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_START_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_END_DATE + " text, " +
            JsonContents.META_RESPONSE_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_COPYRIGHT + " text, " +
            JsonContents.META_RESPONSE_DUR + " text, " +
            JsonContents.META_RESPONSE_DEMONG + " text, " +
            JsonContents.META_RESPONSE_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            JsonContents.META_RESPONSE_HDRFLG + " text, " +
            JsonContents.META_RESPONSE_DELIVERY + " text, " +
            JsonContents.META_RESPONSE_R_VALUE + " text, " +
            JsonContents.META_RESPONSE_ADULT + " text, " +
            JsonContents.META_RESPONSE_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_SYNOP + " text, " +
            JsonContents.META_RESPONSE_SYNOP_SHORT + " text, " +
            JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CREDIT_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RATING + " text, " +
            JsonContents.META_RESPONSE_DTV + " text, " +
            JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_SEARCH_OK + " text, " +
            JsonContents.META_RESPONSE_LIINF_ARRAY + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CAPL + " text, " +
            JsonContents.META_RESPONSE_BILINGAL + " text, " +
            JsonContents.META_RESPONSE_TV_CID + " text, " +
            JsonContents.META_RESPONSE_SERVICE_ID + " text, " +
            JsonContents.META_RESPONSE_EVENT_ID + " text, " +
            JsonContents.META_RESPONSE_CHNO + " text, " +
            JsonContents.META_RESPONSE_TV_SERVICE + " text, " +
            JsonContents.META_RESPONSE_CONTENT_TYPE + " text, " +
            JsonContents.META_RESPONSE_VOD_START_DATE + " text, " +
            JsonContents.META_RESPONSE_VOD_END_DATE + " text, " +
            JsonContents.META_RESPONSE_MAIN_GENRE + " text, " +
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_COPY + " text, " +
            JsonContents.META_RESPONSE_ADINFO_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY + " text " +
            ")";

    // レコメンド（dTV）データ格納用テーブル
    public static final String RECOMMEND_LIST_DTV_TABLE_NAME = "recommend_dtv_list";
    public static final String CREATE_TABLE_RECOMMEND_DTV_SQL = "" +
            "create table " + RECOMMEND_LIST_DTV_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDORDER + " text, " +
            RECOMMENDCHANNEL_LIST_SERVICEID + " text, " +
            RECOMMENDCHANNEL_LIST_CATEGORYID + " text, " +
            RECOMMENDCHANNEL_LIST_CHANNELID + " text, " +
            RECOMMENDCHANNEL_LIST_CONTENTSID + " text, " +
            RECOMMENDCHANNEL_LIST_TITLE + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL1 + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL2 + " text, " +
            RECOMMENDCHANNEL_LIST_STARTVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_ENDVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED1 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED2 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED3 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED4 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED5 + " text, " +
            RECOMMENDCHANNEL_LIST_AGREEMENT + " text, " +
            RECOMMENDCHANNEL_LIST_VIEWABLE + " text, " +
            RECOMMENDCHANNEL_LIST_PAGEID + " text ," +
            RECOMMENDCHANNEL_LIST_GROUPID + " text, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID + " text " +
            ")";

    // レコメンド（dチャンネル）データ格納用テーブル
    public static final String RECOMMEND_LIST_DCHANNEL_TABLE_NAME = "recommend_list_dchannel";
    public static final String CREATE_TABLE_RECOMMEND_DCHANNEL_SQL = "" +
            "create table " + RECOMMEND_LIST_DCHANNEL_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDORDER + " text, " +
            RECOMMENDCHANNEL_LIST_SERVICEID + " text, " +
            RECOMMENDCHANNEL_LIST_CATEGORYID + " text, " +
            RECOMMENDCHANNEL_LIST_CHANNELID + " text, " +
            RECOMMENDCHANNEL_LIST_CONTENTSID + " text, " +
            RECOMMENDCHANNEL_LIST_TITLE + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL1 + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL2 + " text, " +
            RECOMMENDCHANNEL_LIST_STARTVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_ENDVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED1 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED2 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED3 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED4 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED5 + " text, " +
            RECOMMENDCHANNEL_LIST_AGREEMENT + " text, " +
            RECOMMENDCHANNEL_LIST_VIEWABLE + " text, " +
            RECOMMENDCHANNEL_LIST_PAGEID + " text, " +
            RECOMMENDCHANNEL_LIST_GROUPID + " text, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID + " text " +
            ")";


    // レコメンド（dアニメ）データ格納用テーブル
    public static final String RECOMMEND_LIST_DANIME_TABLE_NAME = "recommend_danime_list";
    public static final String CREATE_TABLE_RECOMMEND_DANIME_SQL = "" +
            "create table " + RECOMMEND_LIST_DANIME_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDORDER + " text, " +
            RECOMMENDCHANNEL_LIST_SERVICEID + " text, " +
            RECOMMENDCHANNEL_LIST_CATEGORYID + " text, " +
            RECOMMENDCHANNEL_LIST_CHANNELID + " text, " +
            RECOMMENDCHANNEL_LIST_CONTENTSID + " text, " +
            RECOMMENDCHANNEL_LIST_TITLE + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL1 + " text, " +
            RECOMMENDCHANNEL_LIST_CTPICURL2 + " text, " +
            RECOMMENDCHANNEL_LIST_STARTVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_ENDVIEWING + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED1 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED2 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED3 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED4 + " text, " +
            RECOMMENDCHANNEL_LIST_RESERVED5 + " text, " +
            RECOMMENDCHANNEL_LIST_AGREEMENT + " text, " +
            RECOMMENDCHANNEL_LIST_VIEWABLE + " text, " +
            RECOMMENDCHANNEL_LIST_PAGEID + " text, " +
            RECOMMENDCHANNEL_LIST_GROUPID + " text, " +
            RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID + " text " +
            ")";

    //TV CLIP TABLE、クリップ一覧用
    public static final String TVCLIP_LIST_TABLE_NAME = "tv_clip_list";
    public static final String CREATE_TABLE_TVCLIP_LIST_SQL = "" +
            "create table " + TVCLIP_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_CID + " text, " +
            JsonContents.META_RESPONSE_TITLE_ID + " text, " +
            JsonContents.META_RESPONSE_EPISODE_ID + " text, " +
            JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_TITLERUBY + " text, " +
            JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_START_DATE + " text, " +
            JsonContents.META_RESPONSE_DISPLAY_END_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_START_DATE + " text, " +
            JsonContents.META_RESPONSE_AVAIL_END_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUBLISH_END_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_START_DATE + " text, " +
            JsonContents.META_RESPONSE_NEWA_END_DATE + " text, " +
            JsonContents.META_RESPONSE_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_640 + " text, " +
            JsonContents.META_RESPONSE_DTV_THUMB_448 + " text, " +
            JsonContents.META_RESPONSE_COPYRIGHT + " text, " +
            JsonContents.META_RESPONSE_DUR + " text, " +
            JsonContents.META_RESPONSE_DEMONG + " text, " +
            JsonContents.META_RESPONSE_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            JsonContents.META_RESPONSE_HDRFLG + " text, " +
            JsonContents.META_RESPONSE_DELIVERY + " text, " +
            JsonContents.META_RESPONSE_R_VALUE + " text, " +
            JsonContents.META_RESPONSE_ADULT + " text, " +
            JsonContents.META_RESPONSE_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_SYNOP + " text, " +
            JsonContents.META_RESPONSE_SYNOP_SHORT + " text, " +
            JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CREDIT_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RATING + " text, " +
            JsonContents.META_RESPONSE_DTV + " text, " +
            JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_SEARCH_OK + " text, " +
            JsonContents.META_RESPONSE_LIINF_ARRAY + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PUID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_EPITITLE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_DISP_TYPE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_CHSVOD + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PRICE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QUNIT + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_QRANGE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_START_DATE + " text, " +
            JsonContents.META_RESPONSE_PUINF  + JsonContents.UNDER_LINE + JsonContents.META_RESPONSE_PU_END_DATE + " text, " +
            JsonContents.META_RESPONSE_CAPL + " text, " +
            JsonContents.META_RESPONSE_BILINGAL + " text, " +
            JsonContents.META_RESPONSE_TV_CID + " text, " +
            JsonContents.META_RESPONSE_SERVICE_ID + " text, " +
            JsonContents.META_RESPONSE_EVENT_ID + " text, " +
            JsonContents.META_RESPONSE_CHNO + " text, " +
            JsonContents.META_RESPONSE_TV_SERVICE + " text, " +
            JsonContents.META_RESPONSE_CONTENT_TYPE + " text, " +
            JsonContents.META_RESPONSE_VOD_START_DATE + " text, " +
            JsonContents.META_RESPONSE_VOD_END_DATE + " text, " +
            JsonContents.META_RESPONSE_MAIN_GENRE + " text, " +
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY + " text, " +
            JsonContents.META_RESPONSE_COPY + " text, " +
            JsonContents.META_RESPONSE_ADINFO_ARRAY + " text, " +
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY + " text " +
            ")";

    // TABLE、レンタル一覧用
    public static final String RENTAL_LIST_TABLE_NAME = "rental_list";
    public static final String CREATE_TABLE_RENTAL_LIST_SQL = "" +
            "create table " + RENTAL_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            VOD_META_FULL_DATA_CRID + " text, " +
            VOD_META_FULL_DATA_CID + " text, " +
            VOD_META_FULL_DATA_TITLE_ID + " text, " +
            VOD_META_FULL_DATA_EPISODE_ID + " text, " +
            VOD_META_FULL_DATA_TITLE + " text, " +
            VOD_META_FULL_DATA_EPITITLE + " text, " +
            VOD_META_FULL_DATA_TITLERUBY + " text, " +
            VOD_META_FULL_DATA_DISP_TYPE + " text, " +
            VOD_META_FULL_DATA_DISPLAY_START_DATE + " text, " +
            VOD_META_FULL_DATA_DISPLAY_END_DATE + " text, " +
            VOD_META_FULL_DATA_AVAIL_START_DATE + " text, " +
            VOD_META_FULL_DATA_AVAIL_END_DATE + " text, " +
            VOD_META_FULL_DATA_PUBLISH_START_DATE + " text, " +
            VOD_META_FULL_DATA_PUBLISH_END_DATE + " text, " +
            VOD_META_FULL_DATA_NEWA_START_DATE + " text, " +
            VOD_META_FULL_DATA_NEWA_END_DATE + " text, " +
            VOD_META_FULL_DATA_COPYRIGHT + " text, " +
            VOD_META_FULL_DATA_THUMB + " text, " +
            VOD_META_FULL_DATA_DUR + " text, " +
            VOD_META_FULL_DATA_DEMONG + " text, " +
            VOD_META_FULL_DATA_BVFLG + " text, " +
            UNDER_BAR_FOUR_K_FLG + " text, " +
            VOD_META_FULL_DATA_HDRFLG + " text, " +
            VOD_META_FULL_DATA_AVAIL_STATUS + " text, " +
            VOD_META_FULL_DATA_DELIVERY + " text, " +
            VOD_META_FULL_DATA_R_VALUE + " text, " +
            VOD_META_FULL_DATA_ADULT + " text, " +
            VOD_META_FULL_DATA_MS + " text, " +
            VOD_META_FULL_DATA_NG_FUNC + " text, " +
            VOD_META_FULL_DATA_GENRE_ID_ARRAY + " text, " +
            VOD_META_FULL_DATA_SYNOP + " text, " +
            VOD_META_FULL_DATA_PUID + " text, " +
            VOD_META_FULL_DATA_PRICE + " text, " +
            VOD_META_FULL_DATA_QRANGE + " text, " +
            VOD_META_FULL_DATA_QUNIT + " text, " +
            VOD_META_FULL_DATA_PU_S + " text, " +
            VOD_META_FULL_DATA_PU_E + " text, " +
            VOD_META_FULL_DATA_CREDITS + " text, " +
            VOD_META_FULL_DATA_RATING + " text, " +
            VOD_META_FULL_DATA_DTV + " text, " +
            ")";
}
