package com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search;


public interface TotalSearchWebApiDelegate {

    public void onSuccess(TotalSearchResponseData result);
    public void  onFailure(TotalSearchErrorData result);
}
