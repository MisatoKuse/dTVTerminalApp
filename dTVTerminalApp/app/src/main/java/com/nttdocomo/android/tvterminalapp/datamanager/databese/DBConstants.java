/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;

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

    //SQL共通項目
    private static final String CREATE_TABLE_TEXT = "create table ";
    private static final String CREATE_TABLE_PRIMARY_TEXT = " integer primary key autoincrement,";
    private static final String TEXT_WITH_COMMA_TEXT = " text, ";
    private static final String TEXT_WITHOUT_COMMA_TEXT = " text ";
    private static final String OPEN_BRACKETS_TEXT = " (";
    private static final String CLOSE_BRACKETS_TEXT = ")";

    /**
     * チャンネルリストDB作成SQL文.
     */
    public static final String CREATE_TABLE_CHANNEL_SQL = StringUtil.getConnectStrings(
            CREATE_TABLE_TEXT, CHANNEL_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CH_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DEFAULT_THUMB, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_STATUS, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NG_FUNC, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SUB_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_SUB_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHPACK, JsonContents.UNDER_LINE,
            JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            DATE_TYPE, TEXT_WITH_COMMA_TEXT, UPDATE_DATE, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * デイリーランキングテーブル名.
     */
    public static final String DAILYRANK_LIST_TABLE_NAME = "daily_rank_list";

    /**
     * デイリーランキングテーブル作成用SQL文.
     */
    public static final String CREATE_TABLE_DAILY_RANK_SQL = StringUtil.getConnectStrings(
            CREATE_TABLE_TEXT, DAILYRANK_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    //Homeキャッシュデータ格納用テーブル
    /**
     * TV番組表テーブル名.
     */
    public static final String TV_SCHEDULE_LIST_TABLE_NAME = "tv_schedule_list";

    /**
     * TV番組表テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_TV_SCHEDULE_SQL = StringUtil.getConnectStrings(
            CREATE_TABLE_TEXT, TV_SCHEDULE_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITH_COMMA_TEXT,
            DATE_TYPE, TEXT_WITH_COMMA_TEXT,
            UPDATE_DATE, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    //Homeキャッシュデータ格納用テーブル
    public static final String USER_INFO_LIST_TABLE_NAME = "user_info_list";
    public static final String CREATE_TABLE_USER_INFO_SQL = StringUtil.getConnectStrings(
            "create table " , USER_INFO_LIST_TABLE_NAME , " (" ,
            ID_COLUMN , " integer primary key autoincrement, " ,
            UserInfoJsonParser.USER_INFO_LIST_LOGGEDIN_ACCOUNT , " text, " ,
            UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS , " text, " ,
            UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ , " text," ,
            UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ , " text," ,
            UserInfoJsonParser.USER_INFO_LIST_UPDATE_TIME , " integer" ,
            ")");

    //Homeキャッシュデータ格納用テーブル
    /**
     * クリップリスト表示テーブル.
     */
    public static final String VODCLIP_LIST_TABLE_NAME = "vod_clip_list";

    /**
     * クリップリスト表示テーブル作成SQL.
     */
    public static final String CREATE_TABLE_VODCLIP_LIST_SQL = StringUtil.getConnectStrings(
            CREATE_TABLE_TEXT, VODCLIP_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * 週間ランキングリストテーブル名.
     */
    public static final String WEEKLYRANK_LIST_TABLE_NAME = "weekly_rank_list";

    /**
     * 週間ランキングリストテーブル作成SQL文.
     */
    public static final String CREATE_TABLE_WEEKLYRANK_SQL = StringUtil.getConnectStrings(
            CREATE_TABLE_TEXT, WEEKLYRANK_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

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
    /**
     * ビデオランキングテーブル名.
     */
    public static final String RANKING_VIDEO_LIST_TABLE_NAME = "ranking_video_list";

    /**
     * ビデオランキングテーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RANKING_VIDEO_SQL = StringUtil.getConnectStrings(
            CREATE_TABLE_TEXT, RANKING_VIDEO_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

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
    /**
     * TVクリップ一覧テーブル.
     */
    public static final String TVCLIP_LIST_TABLE_NAME = "tv_clip_list";

    /**
     * TVクリップ一覧テーブル作成SQL.
     */
    public static final String CREATE_TABLE_TVCLIP_LIST_SQL = StringUtil.getConnectStrings(
            CREATE_TABLE_TEXT, TVCLIP_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUINF, JsonContents.UNDER_LINE, JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    // TABLE、録画持ち出しリスト用
    public static final String DOWNLOAD_LIST_COLUM_ITEM_ID = "item_id";
    public static final String DOWNLOAD_LIST_COLUM_URL = "url";
    public static final String DOWNLOAD_LIST_COLUM_SAVE_DIDL = "didl";
    public static final String DOWNLOAD_LIST_COLUM_SAVE_HOST = "host";
    public static final String DOWNLOAD_LIST_COLUM_SAVE_PORT = "port";
    public static final String DOWNLOAD_LIST_COLUM_SAVE_URL = "save_file";
    public static final String DOWNLOAD_LIST_COLUM_TYPE = "mimetype";
    public static final String DOWNLOAD_LIST_COLUM_DOWNLOAD_SIZE = "download_size";
    public static final String DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS = "download_status";
    public static final String DOWNLOAD_LIST_COLUM_SIZE = "total_size";
    public static final String DOWNLOAD_LIST_COLUM_DURATION = "duration";
    public static final String DOWNLOAD_LIST_COLUM_RESOLUTION = "resolution";
    public static final String DOWNLOAD_LIST_COLUM_UPNP_ICON = "upnp_icon";
    public static final String DOWNLOAD_LIST_COLUM_BITRATE = "bitrate";
    public static final String DOWNLOAD_LIST_COLUM_IS_SUPPORTED_BYTE_SEEK = "is_supported_byte_seek";
    public static final String DOWNLOAD_LIST_COLUM_IS_SUPPORTED_TIME_SEEK = "is_supported_time_seek";
    public static final String DOWNLOAD_LIST_COLUM_IS_AVAILABLE_CONNECTION_STALLING = "is_available_connection_stalling";
    public static final String DOWNLOAD_LIST_COLUM_IS_LIVE_MODE = "is_live_mode";
    public static final String DOWNLOAD_LIST_COLUM_IS_REMOTE = "is_remote";
    public static final String DOWNLOAD_LIST_COLUM_TITLE = "title";
    public static final String DOWNLOAD_LIST_COLUM_CONTENTFORMAT = "contentFormat";
    public static final String DOWNLOAD_LIST_TABLE_NAME = "download_list";
    public static final String CREATE_TABLE_DOWNLOAD_LIST_SQL_NAMES[] = {
            CREATE_TABLE_TEXT,
            DOWNLOAD_LIST_TABLE_NAME,
            OPEN_BRACKETS_TEXT,
            ID_COLUMN,
            CREATE_TABLE_PRIMARY_TEXT,
            DOWNLOAD_LIST_COLUM_ITEM_ID,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_URL,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SAVE_DIDL,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SAVE_HOST,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SAVE_PORT,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SAVE_URL,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_TYPE,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_DOWNLOAD_SIZE,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SIZE,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_DURATION,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_RESOLUTION,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_UPNP_ICON,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_BITRATE,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_SUPPORTED_BYTE_SEEK,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_SUPPORTED_TIME_SEEK,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_AVAILABLE_CONNECTION_STALLING,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_LIVE_MODE,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_REMOTE,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_TITLE,TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_CONTENTFORMAT,TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT
    };

    /**
     * レンタル一覧テーブル.
     */
    public static final String RENTAL_LIST_TABLE_NAME = "rental_list";
    /**
     * 購入済みCH一覧テーブル.
     */
    public static final String RENTAL_CHANNEL_LIST_TABLE_NAME = "rental_cnannel_list";

    /**
     * レンタル一覧テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RENTAL_LIST_SQL = StringUtil.getConnectStrings(
            CREATE_TABLE_TEXT, RENTAL_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonContents.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_640 + TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_THUMB_448 + TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_640 + TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_THUMB_448 + TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_AVAIL_STATUS, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            //VOD&番組マージでは廃止されたデータ
            //JsonContents.MSDATA_VOD_META_FULL_DATA_MS , TEXT_WITH_COMMA_TEXT ,
            //JsonContents.META_RESPONSE_NG_FUNC , CREATE_TEXT_WITH_COMMA_TEXT ,
            JsonContents.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_DTV, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * TABLE、ロールリスト用.
     */
    public static final String ROLE_LIST_TABLE_NAME = "role_list";

    /**
     * ロールリストテーブル.
     */
    public static final String CREATE_TABLE_ROLE_LIST_SQL = StringUtil.getConnectStrings(
            CREATE_TABLE_TEXT,
            ROLE_LIST_TABLE_NAME,
            OPEN_BRACKETS_TEXT,
            ID_COLUMN,
            CREATE_TABLE_PRIMARY_TEXT,
            JsonContents.META_RESPONSE_CONTENTS_ID, TEXT_WITH_COMMA_TEXT,
            JsonContents.META_RESPONSE_CONTENTS_NAME, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);
    public static final String CREATE_TABLE_DOWNLOAD_LIST_SQL = StringUtil.getConnectString(
            CREATE_TABLE_DOWNLOAD_LIST_SQL_NAMES);

    //My番組表キャッシュデータ格納用テーブル
    public static final String MY_CHANNEL_LIST_TABLE_NAME = "my_channel_list";
    public static final String CREATE_TABLE_MY_CHANNEL_SQL = "" +
            "create table " + MY_CHANNEL_LIST_TABLE_NAME + " (" +
            ID_COLUMN + " integer primary key autoincrement, " +
            JsonContents.META_RESPONSE_CRID + " text, " +
            JsonContents.META_RESPONSE_SERVICE_ID  + " text, " +
            JsonContents.META_RESPONSE_TITLE + " text, " +
            JsonContents.META_RESPONSE_R_VALUE  + " text, " +
            JsonContents.META_RESPONSE_ADULT_TYPE  + " text, " +
            JsonContents.META_RESPONSE_INDEX  + " text " +
            ")";


}
