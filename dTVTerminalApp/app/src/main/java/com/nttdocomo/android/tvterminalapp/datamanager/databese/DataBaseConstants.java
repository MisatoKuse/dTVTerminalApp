/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.datamanager.databese;

import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.UserInfoJsonParser;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendWebXmlParser;

/**
 * DB定数定義クラス.
 */
public class DataBaseConstants {

    /**
     * DB名.
     */
    public static final String DATABASE_NAME = "db_data";

    /**
     * DB名.
     */
    public static final String DOWNLOAD_DATABASE_NAME = "download_db_data";

    /**
     * 4kflg.
     */
    public static final String FOUR_K_FLG = "4kflg";
    /**
     * Jsonのキー名が数字から始まってる時の対策用定数.
     */
    public static final String UNDER_BAR_FOUR_K_FLG = "_4kflg";

    //Homeキャッシュデータ格納用テーブル
    /**
     * channel list.
     */
    public static final String CHANNEL_LIST_TABLE_NAME = "channel_list";
    /**
     * row id.
     */
    private static final String ID_COLUMN = "row_id";
    /**
     * update date.
     */
    public static final String UPDATE_DATE = "update_date";
    /**
     * date type.
     */
    private static final String DATE_TYPE = "date_type";

    //SQL共通項目
    /**
     * create table.
     */
    private static final String CREATE_TABLE_TEXT = "create table ";
    /**
     * table primary text.
     */
    private static final String CREATE_TABLE_PRIMARY_TEXT = " integer primary key autoincrement,";
    /**
     * text with comma.
     */
    private static final String TEXT_WITH_COMMA_TEXT = " text, ";
    /**
     * text without comma.
     */
    private static final String TEXT_WITHOUT_COMMA_TEXT = " text ";
    /**
     * brackets.
     */
    private static final String OPEN_BRACKETS_TEXT = " (";
    /**
     * closing brackets.
     */
    private static final String CLOSE_BRACKETS_TEXT = ")";
    /**
     * closing brackets.
     */
    public static final String DROP_TABLE_TEXT = "drop table ";

    /**
     * チャンネルリストDB作成SQL文.
     */
    public static final String CREATE_TABLE_CHANNEL_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, CHANNEL_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CH_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_STATUS, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NG_FUNC, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SUB_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_SUB_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            DATE_TYPE, TEXT_WITH_COMMA_TEXT, UPDATE_DATE, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * デイリーランキングテーブル名.
     */
    public static final String DAILYRANK_LIST_TABLE_NAME = "daily_rank_list";

    /**
     * デイリーランキングテーブル作成用SQL文.
     */
    public static final String CREATE_TABLE_DAILY_RANK_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, DAILYRANK_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    //Homeキャッシュデータ格納用テーブル
    /**
     * TV番組表テーブル名.
     */
    public static final String TV_SCHEDULE_LIST_TABLE_NAME = "tv_schedule_list";

    /**
     * TV番組表テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_TV_SCHEDULE_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, TV_SCHEDULE_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITH_COMMA_TEXT,
            UPDATE_DATE, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * ユーザー情報格納用テーブル.
     */
    public static final String USER_INFO_LIST_TABLE_NAME = "user_info_list";
    /**
     * ユーザー情報取得テーブル作成SQL.
     */
    public static final String CREATE_TABLE_USER_INFO_SQL = StringUtils.getConnectStrings(
            "create table ", USER_INFO_LIST_TABLE_NAME, " (",
            ID_COLUMN, " integer primary key autoincrement, ",
            UserInfoJsonParser.USER_INFO_LIST_LOGGEDIN_ACCOUNT, " text, ",
            UserInfoJsonParser.USER_INFO_LIST_CONTRACT_STATUS, " text, ",
            UserInfoJsonParser.USER_INFO_LIST_DCH_AGE_REQ, " text,",
            UserInfoJsonParser.USER_INFO_LIST_H4D_AGE_REQ, " text,",
            UserInfoJsonParser.USER_INFO_LIST_UPDATE_TIME, " integer",
            ")");

    //Homeキャッシュデータ格納用テーブル

    /**
     * 週間ランキングリストテーブル名.
     */
    public static final String WEEKLYRANK_LIST_TABLE_NAME = "weekly_rank_list";

    /**
     * 週間ランキングリストテーブル作成SQL文.
     */
    public static final String CREATE_TABLE_WEEKLYRANK_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, WEEKLYRANK_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * ホームのおすすめ番組データ格納用テーブルカラム.
     */
    public static final String CREATE_TABLE_RECOMMEND_DATA_COLUMN_SQL = " ("
            + ID_COLUMN + " integer primary key autoincrement, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_TITLE + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL2 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED1 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED2 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED3 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED5 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED6 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED7 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED8 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED9 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED10 + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_AGREEMENT + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_VIEWABLE + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_PAGEID + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_GROUPID + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_GENRE_NAME + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_AREA + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_R_RATED + " text, "
            + RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_COPYRIGHT + " text "
            + ")";

    /**
     * ホームのおすすめ番組データ格納用テーブル.
     */
    public static final String RECOMMEND_HOME_CHANNEL_LIST_TABLE_NAME = "recommend_home_channel_list";
    /**
     * ホームのおすすめ番組情報テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RECOMMEND_HOME_CHANNEL_SQL = ""
            + "create table " + RECOMMEND_HOME_CHANNEL_LIST_TABLE_NAME + CREATE_TABLE_RECOMMEND_DATA_COLUMN_SQL;

    /**
     * おすすめチャンネルデータ格納用テーブル.
     */
    public static final String RECOMMEND_CHANNEL_LIST_TABLE_NAME = "recommend_channel_list";
    /**
     * おすすめチャンネル情報テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RECOMMEND_CHANNEL_SQL = ""
            + "create table " + RECOMMEND_CHANNEL_LIST_TABLE_NAME + CREATE_TABLE_RECOMMEND_DATA_COLUMN_SQL;

    /**
     * ホームのおすすめビデオデータ格納用テーブル.
     */
    public static final String RECOMMEND_HOME_VIDEO_LIST_TABLE_NAME = "recommend_home_video_list";
    /**
     * ホームのおすすめビデオ情報テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RECOMMEND_HOME_VIDEO_SQL = ""
            + "create table " + RECOMMEND_HOME_VIDEO_LIST_TABLE_NAME + CREATE_TABLE_RECOMMEND_DATA_COLUMN_SQL;

    /**
     * おすすめビデオデータ格納用テーブル.
     */
    public static final String RECOMMEND_VIDEO_LIST_TABLE_NAME = "recommend_video_list";
    /**
     * おすすめビデオ情報テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RECOMMEND_VIDEO_SQL = ""
            + "create table " + RECOMMEND_VIDEO_LIST_TABLE_NAME + CREATE_TABLE_RECOMMEND_DATA_COLUMN_SQL;

    //ランキングキャッシュデータ格納用テーブル
    /**
     * ビデオランキングテーブル名.
     */
    public static final String RANKING_VIDEO_LIST_TABLE_NAME = "ranking_video_list";

    /**
     * ビデオランキングテーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RANKING_VIDEO_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, RANKING_VIDEO_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * レコメンド（dTV）データ格納用テーブル.
     */
    public static final String RECOMMEND_LIST_DTV_TABLE_NAME = "recommend_dtv_list";
    /**
     * レコメンド（dTV）テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RECOMMEND_DTV_SQL = ""
            + "create table " + RECOMMEND_LIST_DTV_TABLE_NAME + CREATE_TABLE_RECOMMEND_DATA_COLUMN_SQL;

    /**
     * レコメンド（dチャンネル）データ格納用テーブル.
     */
    public static final String RECOMMEND_LIST_DCHANNEL_TABLE_NAME = "recommend_list_dchannel";
    /**
     * レコメンド（dチャンネル）テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RECOMMEND_DCHANNEL_SQL = ""
            + "create table " + RECOMMEND_LIST_DCHANNEL_TABLE_NAME + CREATE_TABLE_RECOMMEND_DATA_COLUMN_SQL;


    /**
     * レコメンド（dアニメ）データ格納用テーブル.
     */
    public static final String RECOMMEND_LIST_DANIME_TABLE_NAME = "recommend_danime_list";
    /**
     * レコメンド（dアニメ）テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RECOMMEND_DANIME_SQL = ""
            + "create table " + RECOMMEND_LIST_DANIME_TABLE_NAME + CREATE_TABLE_RECOMMEND_DATA_COLUMN_SQL;

    // TABLE、録画持ち出しリスト用
    /**
     * item id.
     */
    public static final String DOWNLOAD_LIST_COLUM_ITEM_ID = "item_id";
    /**
     * url.
     */
    public static final String DOWNLOAD_LIST_COLUM_URL = "url";
    /**
     * didl.
     */
    public static final String DOWNLOAD_LIST_COLUM_SAVE_DIDL = "didl";
    /**
     * host.
     */
    public static final String DOWNLOAD_LIST_COLUM_SAVE_HOST = "host";
    /**
     * host.
     */
    public static final String DOWNLOAD_LIST_COLUM_SAVE_PORT = "port";
    /**
     * save file.
     */
    public static final String DOWNLOAD_LIST_COLUM_SAVE_URL = "save_file";
    /**
     * mimetype.
     */
    public static final String DOWNLOAD_LIST_COLUM_TYPE = "mimetype";
    /**
     * channelName.
     */
    public static final String DOWNLOAD_LIST_COLUM_CHANNELNAME = "channelName";
    /**
     * date.
     */
    public static final String DOWNLOAD_LIST_COLUM_DATE = "date";
    /**
     * download size.
     */
    public static final String DOWNLOAD_LIST_COLUM_DOWNLOAD_SIZE = "download_size";
    /**
     * download status.
     */
    public static final String DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS = "download_status";
    /**
     * total size.
     */
    public static final String DOWNLOAD_LIST_COLUM_SIZE = "total_size";
    /**
     * duration.
     */
    public static final String DOWNLOAD_LIST_COLUM_DURATION = "duration";
    /**
     * resolution.
     */
    public static final String DOWNLOAD_LIST_COLUM_RESOLUTION = "resolution";
    /**
     * upnp icon.
     */
    public static final String DOWNLOAD_LIST_COLUM_UPNP_ICON = "upnp_icon";
    /**
     * bitrate.
     */
    public static final String DOWNLOAD_LIST_COLUM_BITRATE = "bitrate";
    /**
     * is supported byte seek.
     */
    public static final String DOWNLOAD_LIST_COLUM_IS_SUPPORTED_BYTE_SEEK = "is_supported_byte_seek";
    /**
     * is supported time seek.
     */
    public static final String DOWNLOAD_LIST_COLUM_IS_SUPPORTED_TIME_SEEK = "is_supported_time_seek";
    /**
     * is available connection stalling.
     */
    public static final String DOWNLOAD_LIST_COLUM_IS_AVAILABLE_CONNECTION_STALLING = "is_available_connection_stalling";
    /**
     * is live mode.
     */
    public static final String DOWNLOAD_LIST_COLUM_IS_LIVE_MODE = "is_live_mode";
    /**
     * is remote.
     */
    public static final String DOWNLOAD_LIST_COLUM_IS_REMOTE = "is_remote";
    /**
     * title.
     */
    public static final String DOWNLOAD_LIST_COLUM_TITLE = "title";
    /**
     * contentFormat.
     */
    public static final String DOWNLOAD_LIST_COLUM_CONTENTFORMAT = "contentFormat";
    /**
     * download list.
     */
    public static final String DOWNLOAD_LIST_TABLE_NAME = "download_list";
    /**
     * ダウンロードリストテーブル作成SQL.
     */
    private static final String[] CREATE_TABLE_DOWNLOAD_LIST_SQL_NAMES = {
            CREATE_TABLE_TEXT,
            DOWNLOAD_LIST_TABLE_NAME,
            OPEN_BRACKETS_TEXT,
            ID_COLUMN,
            CREATE_TABLE_PRIMARY_TEXT,
            DOWNLOAD_LIST_COLUM_ITEM_ID, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_URL, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SAVE_DIDL, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SAVE_HOST, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SAVE_PORT, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SAVE_URL, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_TYPE, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_CHANNELNAME, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_DATE, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_DOWNLOAD_SIZE, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_DOWNLOAD_STATUS, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_SIZE, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_DURATION, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_RESOLUTION, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_UPNP_ICON, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_BITRATE, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_SUPPORTED_BYTE_SEEK, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_SUPPORTED_TIME_SEEK, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_AVAILABLE_CONNECTION_STALLING, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_LIVE_MODE, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_IS_REMOTE, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_TITLE, TEXT_WITH_COMMA_TEXT,
            DOWNLOAD_LIST_COLUM_CONTENTFORMAT, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT
    };

    /**
     * レンタル一覧テーブル.
     */
    public static final String RENTAL_LIST_TABLE_NAME = "rental_list";

    /**
     * レンタル一覧テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RENTAL_LIST_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, RENTAL_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_640 + TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_448 + TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_640 + TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_448 + TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EST_FLAG, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_STATUS, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            //VOD&番組マージでは廃止されたデータ
            //JsonConstants.MSDATA_VOD_META_FULL_DATA_MS , TEXT_WITH_COMMA_TEXT ,
            //JsonConstants.META_RESPONSE_NG_FUNC , CREATE_TEXT_WITH_COMMA_TEXT ,
            JsonConstants.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * レンタルのactive_list一覧テーブル.
     */
    public static final String RENTAL_ACTIVE_LIST_TABLE_NAME = "rental_active_list";

    /**
     * レンタルのactive_list一覧テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RENTAL_ACTIVE_LIST_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, RENTAL_ACTIVE_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_ACTIVE_LIST, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_LICENSE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ACTIVE_LIST, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_VAILD_END_DATE, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * 購入済みCH一覧テーブル.
     */
    public static final String RENTAL_CHANNEL_LIST_TABLE_NAME = "rental_cnannel_list";

    /**
     * レンタル一覧テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RENTAL_CHANNEL_LIST_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, RENTAL_CHANNEL_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CH_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_STATUS, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NG_FUNC, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SUB_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_SUB_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            DATE_TYPE, TEXT_WITH_COMMA_TEXT, UPDATE_DATE, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * 購入済みCHのactive_list一覧テーブル.
     */
    public static final String RENTAL_CHANNEL_ACTIVE_LIST_TABLE_NAME = "rental_cnannel_active_list";

    /**
     * 購入済みCHのactive_list一覧テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_RENTAL_CHANNEL_ACTIVE_LIST_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, RENTAL_CHANNEL_ACTIVE_LIST_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_ACTIVE_LIST, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_LICENSE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ACTIVE_LIST, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_VAILD_END_DATE, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * TABLE、ロールリスト用.
     */
    public static final String ROLE_LIST_TABLE_NAME = "role_list";

    /**
     * ロールリストテーブル.
     */
    public static final String CREATE_TABLE_ROLE_LIST_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT,
            ROLE_LIST_TABLE_NAME,
            OPEN_BRACKETS_TEXT,
            ID_COLUMN,
            CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CONTENTS_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CONTENTS_NAME, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);
    /**
     * ロールリストテーブル作成SQL文.
     */
    public static final String CREATE_TABLE_DOWNLOAD_LIST_SQL = StringUtils.getConnectString(
            CREATE_TABLE_DOWNLOAD_LIST_SQL_NAMES);

    /**
     * クリップキー一覧 キャッシュデータ格納用テーブル.
     */
    public static final String TV_CLIP_KEY_LIST_TABLE_NAME = "tv_clip_key_list";
    /**
     * クリップキー一覧テーブル用 colum ("vod"or"tv").
     */
    private static final String[] CREATE_TABLE_TV_CLIP_KEY_LIST_SQL_NAMES = {
            CREATE_TABLE_TEXT,
            TV_CLIP_KEY_LIST_TABLE_NAME,
            OPEN_BRACKETS_TEXT,
            ID_COLUMN,
            CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE_ID, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT
    };
    /**
     * クリップキー一覧テーブル作成SQL文.
     */
    public static final String CREATE_TABLE_TV_CLIP_KEY_LIST_SQL = StringUtils.getConnectString(
            CREATE_TABLE_TV_CLIP_KEY_LIST_SQL_NAMES);

    /**
     * クリップキー一覧 キャッシュデータ格納用テーブル.
     */
    public static final String VOD_CLIP_KEY_LIST_TABLE_NAME = "vod_clip_key_list";
    /**
     * クリップキー一覧テーブル用 colum ("vod"or"tv").
     */
    private static final String[] CREATE_TABLE_VOD_CLIP_KEY_LIST_SQL_NAMES = {
            CREATE_TABLE_TEXT,
            VOD_CLIP_KEY_LIST_TABLE_NAME,
            OPEN_BRACKETS_TEXT,
            ID_COLUMN,
            CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE_ID, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT
    };
    /**
     * VODクリップ一覧テーブル作成用SQL文.
     */
    public static final String CREATE_TABLE_VOD_CLIP_KEY_LIST_SQL = StringUtils.getConnectString(
            CREATE_TABLE_VOD_CLIP_KEY_LIST_SQL_NAMES);

    /**
     * 視聴中ビデオ一覧テーブル名.
     */
    public static final String WATCH_LISTEN_VIDEO_TABLE_NAME = "watch_listen_video_table_name";

    /**
     * 視聴中ビデオ一覧テーブル用 column.
     */
    public static final String CREATE_TABLE_WATCH_LISTEN_VIDEO_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT,
            WATCH_LISTEN_VIDEO_TABLE_NAME,
            OPEN_BRACKETS_TEXT,
            ID_COLUMN,
            CREATE_TABLE_PRIMARY_TEXT,
            JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPISODE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TITLERUBY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DISPLAY_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUBLISH_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_NEWA_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_640, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_THUMB_448, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPYRIGHT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DUR, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DEMONG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BVFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV_TYPE, TEXT_WITH_COMMA_TEXT,
            UNDER_BAR_FOUR_K_FLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_HDRFLG, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DELIVERY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_R_VALUE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADULT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SYNOP_SHORT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CREDIT_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RATING, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_DTV, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SEARCH_OK, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_LIINF_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PUID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CRID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_TITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_EPITITLE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_DISP_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_CHSVOD, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PRICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QUNIT, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_QRANGE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_PUINF, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PU_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CAPL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_BILINGAL, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_CID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SERVICE_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_EVENT_ID, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CHNO, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_TV_SERVICE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_CONTENT_TYPE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_START_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_VOD_END_DATE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_MAIN_GENRE, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_COPY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_ADINFO_ARRAY, TEXT_WITH_COMMA_TEXT,
            JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);

    /**
     * DLNAコンテンツブラウズテーブルカラム名.
     */
    public static final String DLNA_BROWSE_COLUM_NO = "no";
    public static final String DLNA_BROWSE_COLUM_BITRATE = "bitrate";
    public static final String DLNA_BROWSE_COLUM_CHANNEL_NAME = "channelName";
    public static final String DLNA_BROWSE_COLUM_DURATION = "duration";
    public static final String DLNA_BROWSE_COLUM_RESOLUTION = "resolution";
    public static final String DLNA_BROWSE_COLUM_RES_URL = "resUrl";
    public static final String DLNA_BROWSE_COLUM_SIZE = "size";
    public static final String DLNA_BROWSE_COLUM_VIDEO_TYPE = "videoType";
    public static final String DLNA_BROWSE_COLUM_CONTAINER_ID = "containerId";

    /**
     * DLNAコンテンツブラウズテーブル名.
     */
    public static final String DLNA_BROWSE_TABLE_NAME = "dlna_browse_table_name";

    /**
     * コンテンツブラウズテーブル作成SQL文.
     */
    public static final String CREATE_TABLE_DLNA_BROWSE_SQL = StringUtils.getConnectStrings(
            CREATE_TABLE_TEXT, DLNA_BROWSE_TABLE_NAME, OPEN_BRACKETS_TEXT,
            ID_COLUMN, CREATE_TABLE_PRIMARY_TEXT,
            DLNA_BROWSE_COLUM_BITRATE, TEXT_WITH_COMMA_TEXT,
            DLNA_BROWSE_COLUM_CHANNEL_NAME, TEXT_WITH_COMMA_TEXT,
            DLNA_BROWSE_COLUM_DURATION, TEXT_WITH_COMMA_TEXT,
            DLNA_BROWSE_COLUM_RESOLUTION, TEXT_WITH_COMMA_TEXT,
            DLNA_BROWSE_COLUM_RES_URL, TEXT_WITH_COMMA_TEXT,
            DLNA_BROWSE_COLUM_SIZE, TEXT_WITH_COMMA_TEXT,
            DLNA_BROWSE_COLUM_VIDEO_TYPE, TEXT_WITH_COMMA_TEXT,
            DLNA_BROWSE_COLUM_CONTAINER_ID, TEXT_WITH_COMMA_TEXT,
            UPDATE_DATE, TEXT_WITHOUT_COMMA_TEXT,
            CLOSE_BRACKETS_TEXT);
}
