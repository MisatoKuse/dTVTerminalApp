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
    public static final String CHANNEL_LIST_TITLE_ID = "title_id";
    public static final String CHANNEL_LIST_EPISODE_ID = "episode_id";
    public static final String CHANNEL_LIST_EPITITLE = "epititle";
    public static final String CHANNEL_LIST_DISPLAY_START_DATE = "display_start_date";
    public static final String CHANNEL_LIST_SERVICE_ID = "service_id";
    public static final String CHANNEL_LIST_EVENT_ID = "event_id";
    public static final String CHANNEL_LIST_CHNO = "chno";
    public static final String CHANNEL_LIST_DISP_TYPE = "disp_type";
    public static final String CHANNEL_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String CHANNEL_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String CHANNEL_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String CHANNEL_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String CHANNEL_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String CHANNEL_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String CHANNEL_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String CHANNEL_LIST_THUMB = "thumb";
    public static final String CHANNEL_LIST_COPYRIGHT = "copyright";
    public static final String CHANNEL_LIST_DUR = "dur";
    public static final String CHANNEL_LIST_DEMONG = "demong";
    public static final String CHANNEL_LIST_BVFLG = "bvflg";
    public static final String CHANNEL_LIST_4KFLG = "4kflg";
    public static final String CHANNEL_LIST_HDRFLG = "hdrflg";
    public static final String CHANNEL_LIST_AVAIL_STATUS = "avail_status";
    public static final String CHANNEL_LIST_DELIVERY = "delivery";
    public static final String CHANNEL_LIST_R_VALUE = "r_value";
    public static final String CHANNEL_LIST_ADULT = "adult";
    public static final String CHANNEL_LIST_MS = "ms";
    public static final String CHANNEL_LIST_NG_FUNC = "ng_func";
    public static final String CHANNEL_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String CHANNEL_LIST_DTV = "dtv";


}
