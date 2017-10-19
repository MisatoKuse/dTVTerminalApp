/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVdList;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendVideoXmlParser;

public class RecommendVdWebClient{

    private RecommendVideoCallback mRecommendVideoCallback;

    public interface RecommendVideoCallback {
        void RecommendVideoCallback(RecommendVdList mRecommendChList);
    }

    public RecommendVdWebClient(RecommendVideoCallback mRecommendVideoCallback){
        this.mRecommendVideoCallback = mRecommendVideoCallback;
    }

    public void getRecommendChannelApi() {
        //TODO: レコメンドサーバ処理
        RecommendVideoXmlParser recommendVideoXmlParser = new RecommendVideoXmlParser();
        //TODO: dummy data
        RecommendVdList mRecommendVdList = recommendVideoXmlParser.getRecommendVideoList();
        mRecommendVideoCallback.RecommendVideoCallback(mRecommendVdList);
    }
}
