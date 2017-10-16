package com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class DailyRankJsonParser  {
    // オブジェクトクラスの定義
    private DailyRankList mDAILYRANKList;

    public static final String DAILYRANK_LIST_STATUS = "status";

    public static final String DAILYRANK_LIST_PAGER = "pager";
    public static final String DAILYRANK_LIST_PAGER_LIMIT = "limit";
    public static final String DAILYRANK_LIST_PAGER_OFFSET = "offset";
    public static final String DAILYRANK_LIST_PAGER_COUNT = "count";
    public static final String DAILYRANK_LIST_PAGER_TOTAL = "total";



    public static final String DAILYRANK_LIST = "list";
    public static final String DAILYRANK_LIST_CRID = "crid";
    public static final String DAILYRANK_LIST_TITLE = "title";
    public static final String DAILYRANK_LIST_TITLE_ID = "title_id";
    public static final String DAILYRANK_LIST_EPITITLE = "epititle";
    public static final String DAILYRANK_LIST_DISPLAY_START_DATE = "display_start_date";
    public static final String DAILYRANK_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String DAILYRANK_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String DAILYRANK_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String DAILYRANK_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String DAILYRANK_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String DAILYRANK_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String DAILYRANK_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String DAILYRANK_LIST_BVFLG = "bvflg";
    public static final String DAILYRANK_LIST_4KFLG = "4kflg";
    public static final String DAILYRANK_LIST_HDRFLG = "hdrflg";
    public static final String DAILYRANK_LIST_ADULT = "adult";
    public static final String DAILYRANK_LIST_MS = "ms";
    public static final String DAILYRANK_LIST_NG_FUNC = "ng_func";
    public static final String DAILYRANK_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String DAILYRANK_LIST_DTV = "dtv";
    public static final String DAILYRANK_LIST_EPISODE_ID = "episode_id";
    public static final String DAILYRANK_LIST_CID = "cid";
    public static final String DAILYRANK_LIST_SERVICE_ID = "service_id";
    public static final String DAILYRANK_LIST_EVENT_ID = "event_id";
    public static final String DAILYRANK_LIST_CHNO = "chno";
    public static final String DAILYRANK_LIST_DISP_TYPE = "disp_type";
    public static final String DAILYRANK_LIST_MISSED_VOD = "missed_vod";
    public static final String DAILYRANK_LIST_LINER_START_DATE = "linear_start_date";
    public static final String DAILYRANK_LIST_LINER_START_END = "linear_start_end";
    public static final String DAILYRANK_LIST_VOD_START_DATE = "vod_start_date";
    public static final String DAILYRANK_LIST_VOD_END_DATE = "vod_end_date";
    public static final String DAILYRANK_LIST_THUMB = "thumb";
    public static final String DAILYRANK_LIST_COPYRIGHT = "copyright";
    public static final String DAILYRANK_LIST_DUR = "dur";
    public static final String DAILYRANK_LIST_DEMONG = "demong";
    public static final String DAILYRANK_LIST_AVAIL_STATUS = "avail_status";
    public static final String DAILYRANK_LIST_DELIVERY = "delivery";
    public static final String DAILYRANK_LIST_R_VALUE = "r_value";

    public static final String[] pagerPara = {DAILYRANK_LIST_PAGER_LIMIT, DAILYRANK_LIST_PAGER_OFFSET,
            DAILYRANK_LIST_PAGER_COUNT, DAILYRANK_LIST_PAGER_TOTAL};

    public static final String[] listPara = {DAILYRANK_LIST_CRID, DAILYRANK_LIST_TITLE, DAILYRANK_LIST_CID,
            DAILYRANK_LIST_SERVICE_ID, DAILYRANK_LIST_EVENT_ID, DAILYRANK_LIST_CHNO, DAILYRANK_LIST_DISP_TYPE,
            DAILYRANK_LIST_MISSED_VOD, DAILYRANK_LIST_LINER_START_DATE, DAILYRANK_LIST_LINER_START_END,
            DAILYRANK_LIST_VOD_START_DATE, DAILYRANK_LIST_VOD_END_DATE, DAILYRANK_LIST_THUMB,
            DAILYRANK_LIST_COPYRIGHT, DAILYRANK_LIST_DUR, DAILYRANK_LIST_DEMONG,
            DAILYRANK_LIST_AVAIL_STATUS, DAILYRANK_LIST_DELIVERY, DAILYRANK_LIST_R_VALUE};

}