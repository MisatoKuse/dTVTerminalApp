/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;

//response
public class TotalSearchErrorData {
    public String status;
    public ErrorResultData error;

    public static class ErrorResultData {
        public String id;
        public String param;
    }

    public TotalSearchErrorData() {
        error = new ErrorResultData();
    }

    public TotalSearchErrorData(final String id, final String param) {
        error = new ErrorResultData();
        error.id = id;
        error.param = param;
        status = DTVTConstants.SEARCH_STATUS_NG;
    }

}
