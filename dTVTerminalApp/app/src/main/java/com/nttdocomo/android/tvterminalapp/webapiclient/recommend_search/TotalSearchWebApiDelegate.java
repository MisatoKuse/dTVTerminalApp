/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


public interface TotalSearchWebApiDelegate {

    void onSuccess(TotalSearchResponseData result);
    void onFailure(TotalSearchErrorData result);
}
