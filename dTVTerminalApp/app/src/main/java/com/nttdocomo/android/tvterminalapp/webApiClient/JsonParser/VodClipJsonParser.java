package com.nttdocomo.android.tvterminalapp.webApiClient.JsonParser;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class VodClipJsonParser {

    // オブジェクトクラスの定義
    private VodClipList mVodClipList;

    public static final String VODCLIP_LIST_STATUS = "status";

    public static final String VODCLIP_LIST_PAGER = "pager";
    public static final String VODCLIP_LIST_PAGER_UPPER_LIMIT = "upper_limit";
    public static final String VODCLIP_LIST_PAGER_LOWER_LIMIT = "lower_limit";
    public static final String VODCLIP_LIST_PAGER_OFFSET = "offset";
    public static final String VODCLIP_LIST_PAGER_COUNT = "count";

    public static final String VODCLIP_LIST = "list";
    public static final String VODCLIP_LIST_CRID = "crid";
    public static final String VODCLIP_LIST_CID = "cid";
    public static final String VODCLIP_LIST_TITLE_ID = "title_id";
    public static final String VODCLIP_LIST_EPISODE_ID = "episode_id";
    public static final String VODCLIP_LIST_TITLE = "title";
    public static final String VODCLIP_LIST_EPITITLE = "epititle";
    public static final String VODCLIP_LIST_DISP_TYPE = "disp_type";
    public static final String VODCLIP_LIST_DISPLAY_START_DATE = "display_start_date";
    public static final String VODCLIP_LIST_DISPLAY_END_DATE = "display_end_date";
    public static final String VODCLIP_LIST_AVAIL_START_DATE = "avail_start_date";
    public static final String VODCLIP_LIST_AVAIL_END_DATE = "avail_end_date";
    public static final String VODCLIP_LIST_PUBLISH_START_DATE = "publish_start_date";
    public static final String VODCLIP_LIST_PUBLISH_END_DATE = "publish_end_date";
    public static final String VODCLIP_LIST_NEWA_START_DATE = "newa_start_date";
    public static final String VODCLIP_LIST_NEWA_END_DATE = "newa_end_date";
    public static final String VODCLIP_LIST_COPYRIGHT = "copyright";
    public static final String VODCLIP_LIST_THUMB = "thumb";
    public static final String VODCLIP_LIST_DUR = "dur";
    public static final String VODCLIP_LIST_DEMONG = "demong";
    public static final String VODCLIP_LIST_BVFLG = "bvflg";
    public static final String VODCLIP_LIST_4KFLG = "4kflg";
    public static final String VODCLIP_LIST_HDRFLG = "hdrflg";
    public static final String VODCLIP_LIST_AVAIL_STATUS = "avail_status";
    public static final String VODCLIP_LIST_DELIVERY = "delivery";
    public static final String VODCLIP_LIST_R_VALUE = "r_value";
    public static final String VODCLIP_LIST_ADULT = "adult";
    public static final String VODCLIP_LIST_MS = "ms";
    public static final String VODCLIP_LIST_NG_FUNC = "ng_func";
    public static final String VODCLIP_LIST_GENRE_ID_ARRAY = "genre_id_array";
    public static final String VODCLIP_LIST_DTV = "dtv";

    public static final String[] pagerPara = {VODCLIP_LIST_PAGER_UPPER_LIMIT, VODCLIP_LIST_PAGER_LOWER_LIMIT,
            VODCLIP_LIST_PAGER_OFFSET, VODCLIP_LIST_PAGER_COUNT};

    public static final String[] listPara = {VODCLIP_LIST_CRID, VODCLIP_LIST_CID, VODCLIP_LIST_TITLE_ID,
            VODCLIP_LIST_EPISODE_ID, VODCLIP_LIST_TITLE, VODCLIP_LIST_EPITITLE, VODCLIP_LIST_DISP_TYPE,
            VODCLIP_LIST_DISPLAY_START_DATE, VODCLIP_LIST_DISPLAY_END_DATE, VODCLIP_LIST_AVAIL_START_DATE,
            VODCLIP_LIST_AVAIL_END_DATE, VODCLIP_LIST_PUBLISH_START_DATE, VODCLIP_LIST_PUBLISH_END_DATE,
            VODCLIP_LIST_NEWA_START_DATE, VODCLIP_LIST_NEWA_END_DATE, VODCLIP_LIST_COPYRIGHT,
            VODCLIP_LIST_THUMB, VODCLIP_LIST_DUR, VODCLIP_LIST_DEMONG, VODCLIP_LIST_BVFLG, VODCLIP_LIST_4KFLG,
            VODCLIP_LIST_HDRFLG, VODCLIP_LIST_AVAIL_STATUS, VODCLIP_LIST_DELIVERY, VODCLIP_LIST_R_VALUE,
            VODCLIP_LIST_ADULT, VODCLIP_LIST_MS, VODCLIP_LIST_NG_FUNC, VODCLIP_LIST_GENRE_ID_ARRAY, VODCLIP_LIST_DTV};

}
