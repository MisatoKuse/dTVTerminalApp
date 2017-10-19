/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendChInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

public class RecommendChWebClient {

    private RecommendChannelCallback mRecommendChannelCallback;

    public interface RecommendChannelCallback {
        void RecommendChannelCallback(RecommendChList mRecommendChList);
    }

    public RecommendChWebClient(RecommendChannelCallback mRecommendChannelCallback){
        this.mRecommendChannelCallback = mRecommendChannelCallback;
    }

    public void getRecommendChannelApi() {
        //TODO: レコメンドサーバ処理
        RecommendChannelXmlParser recommendChannelXmlParser = new RecommendChannelXmlParser();
        //TODO: dummy data
        RecommendChList mRecommendChList = recommendChannelXmlParser.getRecommendchannelList();
        mRecommendChannelCallback.RecommendChannelCallback(mRecommendChList);
    }

}
