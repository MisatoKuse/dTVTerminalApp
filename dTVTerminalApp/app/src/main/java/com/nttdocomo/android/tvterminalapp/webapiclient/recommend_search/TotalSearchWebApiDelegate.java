/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


public interface TotalSearchWebApiDelegate {

    public void onSuccess(TotalSearchResponseData result);
    public void  onFailure(TotalSearchErrorData result);
}
