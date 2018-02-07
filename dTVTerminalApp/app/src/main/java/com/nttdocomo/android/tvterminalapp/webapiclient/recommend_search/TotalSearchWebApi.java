/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 検索WebApi.
 */
public class TotalSearchWebApi extends WebApiBase implements WebApiCallback, SearchXmlParser.XMLParserFinishListener {

    /**
     * XMLのparse結果をコールバックする.
     */
    private TotalSearchWebApiDelegate mDelegate;

    /**
     * genreFilter文字列.
     */
    private String genreFilterString = "";
    /**
     * dubbedFilter文字列.
     */
    private String dubbedFilterString = "";
    /**
     * chargeFilter文字列.
     */
    private String chargeFilterString = "";
    /**
     * otherFilter文字列.
     */
    private String otherFilterString = "";

    /**
     * SSLチェック用コンテキスト.
     */
    private Context mContext;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public TotalSearchWebApi(final Context context) {
        //コンテキストの退避
        mContext = context;
    }

    /**
     * delegateの設定.
     *
     * @param delegate デリゲート
     */
    public void setDelegate(final TotalSearchWebApiDelegate delegate) {
        synchronized (this) {
            mDelegate = delegate;
        }
    }

    /**
     * 検索リクエスト.
     *
     * @param requestData リクエストデータ
     */
    public void request(final TotalSearchRequestData requestData) {
        DTVTLogger.debug("request");

        if (!mIsCancel) {
            TotalSearchRequestData data = requestData;
            data.userId = "1234567890"; //KARI

            LinkedHashMap<String, String> queryItems = new LinkedHashMap<>();
            queryItems.put(SearchRequestKey.kUserId, data.userId);
            queryItems.put(SearchRequestKey.kFunction, data.function.value() + ""); //ok
            //queryItems.put(SearchRequestKey.kFunction, new String(2+"")); //ng for test
            queryItems.put(SearchRequestKey.kResponseType, String.valueOf(data.responseType.ordinal() + 1));
            queryItems.put(SearchRequestKey.kQuery, data.query);
            queryItems.put(SearchRequestKey.kStartIndex, String.valueOf(data.startIndex));
            queryItems.put(SearchRequestKey.kMaxResult, String.valueOf(data.maxResult));

            String serviceId = data.serviceId;
            queryItems.put(SearchRequestKey.kServiceId, serviceId);

            String categoryId = data.categoryId;
            queryItems.put(SearchRequestKey.kCategoryId, categoryId);

            int sortKind = data.sortKind;
            queryItems.put(SearchRequestKey.kSortKind, sortKind + "");

            ArrayList<SearchFilterType> filterList = requestData.filterTypeList;
            for (SearchFilterType type : filterList) {
                appendString(type);
            }
            queryItems.put(SearchRequestKey.kCondition, concatFilterString());

            //ユーザ情報が設定されている時のみパラメータを追加する
            String filterViewableAge = data.filterViewableAge;
            if (filterViewableAge != null) {
                queryItems.put(SearchRequestKey.kFilterViewableAge, filterViewableAge);
            }

            get(UrlConstants.WebApiUrl.TOTAL_SEARCH_URL, queryItems, this, mContext);
        } else {
            DTVTLogger.error("TotalSearchWebApi is stopping connection");
        }
    }

    /**
     * フィルタ文字列の連結.
     *
     * @return 連結した文字列.
     */
    private String concatFilterString() {
        String resultString = "";
        if (!genreFilterString.isEmpty()) {
            resultString += genreFilterString;
        }
        if (!dubbedFilterString.isEmpty()) {
            if (resultString.isEmpty()) {
                resultString += dubbedFilterString;
            } else {
                resultString += "+";
                resultString += dubbedFilterString;
            }
        }
        if (!chargeFilterString.isEmpty()) {
            if (resultString.isEmpty()) {
                resultString += chargeFilterString;
            } else {
                resultString += "+";
                resultString += "chargeFilterString";
            }
        }
        if (!otherFilterString.isEmpty()) {
            if (resultString.isEmpty()) {
                resultString += otherFilterString;
            } else {
                resultString += "+";
                resultString += otherFilterString;
            }
        }
        genreFilterString = "";
        dubbedFilterString = "";
        chargeFilterString = "";
        otherFilterString = "";
        return resultString;
    }

    /**
     * 文字の追加.
     *
     * @param addType 追加する文字
     */
    private void appendString(final SearchFilterType addType) {
        switch (addType) {
        case genreMovie:
            genreFilterString = "genre:" + getValueFromFilterType(addType);
            break;
        case dubbedText:
        case dubbedTextAndDubbing:
        case dubbedDubbed:
            if (dubbedFilterString.isEmpty()) {
                dubbedFilterString = "dubbed:" + getValueFromFilterType(addType);
            } else {
                dubbedFilterString += ",";
                dubbedFilterString += (getValueFromFilterType(addType));
            }
            break;
        case chargeUnlimited:
        case chargeRental:
            if (chargeFilterString.isEmpty()) {
                chargeFilterString = "charge:" + getValueFromFilterType(addType);
            } else {
                chargeFilterString += ",";
                chargeFilterString += (getValueFromFilterType(addType));
            }
            break;
        case otherHdWork:
            otherFilterString = "other:" + getValueFromFilterType(addType);
        }
    }

    /**
     * FilterTypeを基に文字列を返却する.
     *
     * @param type FilterType
     * @return 文字列
     */
    private String getValueFromFilterType(final SearchFilterType type) {
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
    public void onFinish(final String responseData) {
        String str = responseData;
        if (null == responseData || 0 == responseData.length()) {
            str = "";
        }
        new SearchXmlParser(this).execute(str);
    }

    @Override
    public void onXMLParserFinish(final TotalSearchResponseData responseData) {
        if (null != mDelegate) {
            mDelegate.onSuccess(responseData);
        }
    }

    @Override
    public void onXMLParserError(final TotalSearchErrorData errorData) {
        if (null != mDelegate) {
            mDelegate.onFailure(errorData);
        }
    }

    /**
     * 通信を止める.
     * TotalSearchWebApiは検索の度にnewされているので通信可能状態にする処理は不要
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        stopHTTPConnection();
    }
}