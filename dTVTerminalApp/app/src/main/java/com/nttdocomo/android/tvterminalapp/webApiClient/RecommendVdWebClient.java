package com.nttdocomo.android.tvterminalapp.webApiClient;

import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVdList;
import com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendVideoXmlParser;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 * チャンネル一覧取得処理
 */
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
        RecommendVdList mRecommendVdList = recommendVideoXmlParser.getRecommendVideoList("");
        mRecommendVideoCallback.RecommendVideoCallback(mRecommendVdList);
    }
}
