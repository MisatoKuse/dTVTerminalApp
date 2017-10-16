package com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search;


import com.nttdocomo.android.tvterminalapp.common.Constants.UrlConstants;
import com.nttdocomo.android.tvterminalapp.WebApiClient.WebApiBase;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class TotalSearchWebApi extends WebApiBase implements WebApiCallback, XMLParser.XMLParserFinishListener {

    private TotalSearchWebApiDelegate delegate;

    String genreFilterString = "";
    String dubbedFilterString = "";
    String chargeFilterString = "";
    String otherFilterString = "";

    public void setDelegate(TotalSearchWebApiDelegate de){
        synchronized (this) {
            delegate = de;
        }
    }

    // 1.request
    public void request(TotalSearchRequestData requestData){

        System.out.println("request");

        TotalSearchRequestData data = requestData;
        data.userId = "1234567890"; //KARI

        LinkedHashMap queryItems=new LinkedHashMap();
        queryItems.put(SearchRequestKey.kUserId, data.userId);
        queryItems.put(SearchRequestKey.kFunction, data.function.value()+"");   //ok
        //queryItems.put(SearchRequestKey.kFunction, new String(2+""));   //ng for test
        queryItems.put(SearchRequestKey.kResponseType, String.valueOf(data.responseType.ordinal() + 1));
        queryItems.put(SearchRequestKey.kQuery, data.query);
        queryItems.put(SearchRequestKey.kStartIndex, String.valueOf(data.startIndex));
        queryItems.put(SearchRequestKey.kMaxResult, String.valueOf(data.maxResult));

        String serviceId= data.serviceId;
        queryItems.put(SearchRequestKey.kServiceId, serviceId);

        int sortKind = data.sortKind;
        queryItems.put(SearchRequestKey.kSortKind, sortKind+"");

        ArrayList<SearchFilterType> filterList =requestData.filterTypeList;
        for( SearchFilterType type : filterList) {
            appendString(type);
        }
        queryItems.put(SearchRequestKey.kCondition, concatFilterString());

        get(UrlConstants.WebApiUrl.totalSearchUrl, queryItems, this);
    }

    // MARK : - private method
    private String concatFilterString() {
        String resultString = "";
        if(genreFilterString.isEmpty() == false) {
            resultString +=genreFilterString;
        }
        if(dubbedFilterString.isEmpty() == false) {
            if(resultString.isEmpty()){
                resultString += dubbedFilterString;
            } else {
                resultString += "+";
                resultString +=dubbedFilterString;
            }
        }
        if(chargeFilterString.isEmpty()== false) {
            if(resultString.isEmpty()){
                resultString +=chargeFilterString;
            } else {
                resultString +="+";
                resultString +="chargeFilterString";
            }
        }
        if(otherFilterString.isEmpty() == false){
            if(resultString.isEmpty()){
                resultString+=otherFilterString;
            } else {
                resultString+="+";
                resultString+=otherFilterString;
            }
        }
        genreFilterString = "";
        dubbedFilterString = "";
        chargeFilterString = "";
        otherFilterString = "";
        return resultString;
    }

    private void appendString(SearchFilterType addType){
        switch (addType) {
        case genreMovie:
            genreFilterString = "genre:" + getValueFromFilterType(addType);
            break;
        case dubbedText:
        case dubbedTextAndDubbing:
        case dubbedDubbed:
            if(dubbedFilterString.isEmpty()) {
                dubbedFilterString = "dubbed:" + getValueFromFilterType(addType);
            } else {
                dubbedFilterString+=",";
                dubbedFilterString +=(getValueFromFilterType(addType));
            }
            break;
        case chargeUnlimited:
        case chargeRental:
            if(chargeFilterString.isEmpty()) {
                chargeFilterString = "charge:" + getValueFromFilterType(addType);
            } else {
                chargeFilterString +=",";
                chargeFilterString +=(getValueFromFilterType(addType));
            }
            break;
        case otherHdWork:
            otherFilterString = "other:" + getValueFromFilterType(addType);
        }

    }

    private String getValueFromFilterType(SearchFilterType type) {
        switch (type) {
        case genreMovie:
            return "active_v001";
        case dubbedText:
            return "1";
        case dubbedDubbed:
            return "2";
        case dubbedTextAndDubbing:
            return "3";
        case chargeUnlimited:
            return "0";
        case chargeRental:
            return "1";
        case otherHdWork:
            return "1";
        }
        return "1"; //test
    }

    @Override
    public void onFinish(String responseData) {
        new XMLParser(this).execute(responseData);
    }

    @Override
    public void onXMLParserFinish(TotalSearchResponseData responseData) {
        if(null!=delegate) {
            delegate.onSuccess(responseData);
        }
    }

    @Override
    public void onXMLParserError(TotalSearchErrorData errorData) {
        if(null!=delegate) {
            delegate.onFailure(errorData);
        }
    }
}


