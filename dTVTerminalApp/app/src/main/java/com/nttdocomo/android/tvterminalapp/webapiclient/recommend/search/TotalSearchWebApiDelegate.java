package com.nttdocomo.android.tvterminalapp.webapiclient.recommend.search;


public interface TotalSearchWebApiDelegate {

    public void onSuccess(TotalSearchResponseData result);
    public void  onFailure(TotalSearchErrorData result);
}
