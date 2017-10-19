/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;


public abstract class SearchFilterTypeMappable {

    //must be overrode
    public SearchFilterType searchFilterType(){
        return null;
    }
}
