package com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class ChannelJsonParser {

    // オブジェクトクラスの定義
    private ChannelList mChannelList;

    public static final String CHANNEL_LIST_STATUS = "status";

    public static final String CHANNEL_LIST = "list";
    public static final String CHANNEL_LIST_CRID = "crid";
    public static final String CHANNEL_LIST_TITLE = "title";
    public static final String CHANNEL_LIST_TITLERUBY = "titleruby";
    public static final String CHANNEL_LIST_CID = "cid";
    public static final String CHANNEL_LIST_SERVICE_ID = "service_id";
    public static final String CHANNEL_LIST_EVENT_ID = "event_id";
    public static final String CHANNEL_LIST_CHNO = "chno";
    public static final String CHANNEL_LIST_DISP_TYPE = "disp_type";
    public static final String CHANNEL_LIST_LINEAR_START_DATE = "linear_start_date";
    public static final String CHANNEL_LIST_LINEAR_END_DATE = "linear_end_date";
    public static final String CHANNEL_LIST_VOD_START_DATE = "vod_start_date";
    public static final String CHANNEL_LIST_VOD_END_DATE = "vod_end_date";
    public static final String CHANNEL_LIST_THUMB = "thumb";
    public static final String CHANNEL_LIST_COPYRIGHT = "copyright";
    public static final String CHANNEL_LIST_DUR = "dur";
    public static final String CHANNEL_LIST_DEMONG = "demong";
    public static final String CHANNEL_LIST_AVAIL_STATUS = "avail_status";
    public static final String CHANNEL_LIST_DELIVERY = "delivery";
    public static final String CHANNEL_LIST_R_VALUE = "r_value";
    public static final String CHANNEL_LIST_MAIN_GENRE = "main_genre";
    public static final String CHANNEL_LIST_SECOND_GENRE_ARRAY = "second_genre_array";
    public static final String CHANNEL_LIST_SYNOP = "synop";
    public static final String CHANNEL_LIST_CREDITS = "credits";
    public static final String CHANNEL_LIST_CAPL = "capl";
    public static final String CHANNEL_LIST_COPY = "copy";
    public static final String CHANNEL_LIST_ADINFO = "adinfo";
    public static final String CHANNEL_LIST_BILINGAL = "bilingal";
    public static final String CHANNEL_LIST_LIVE = "live";
    public static final String CHANNEL_LIST_FIRST_JAPAN = "first_japan";
    public static final String CHANNEL_LIST_FIRST_TV = "first_tv";
    public static final String CHANNEL_LIST_EXCLUSIVE = "exclusive";
    public static final String CHANNEL_LIST_PRE = "pre";
    public static final String CHANNEL_LIST_FIRST_CH = "first_ch";
    public static final String CHANNEL_LIST_ORIGINAL = "original";
    public static final String CHANNEL_LIST_MASK = "mask";
    public static final String CHANNEL_LIST_NONSCRAMBLE = "nonscramble";
    public static final String CHANNEL_LIST_DOWNLOAD = "download";
    public static final String CHANNEL_LIST_STARTOVER = "startover";
    public static final String CHANNEL_LIST_STAMP = "stamp";
    public static final String CHANNEL_LIST_RELATIONAL_ID_ARRAY = "relational_id_array";

}
