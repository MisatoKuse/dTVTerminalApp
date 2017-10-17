/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.plala;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendChInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

public class RecommendChWebClient {

    private RecommendChannelCallback mRecommendChannelCallback;
    private Context context;

    public interface RecommendChannelCallback {
        void RecommendChannelCallback(RecommendChList mRecommendChList);
    }

    public RecommendChWebClient(Context context, RecommendChannelCallback mRecommendChannelCallback){
        this.mRecommendChannelCallback = mRecommendChannelCallback;
        this.context = context;
    }

    public void getRecommendChannelApi() {
        //TODO: レコメンドサーバ処理
        RecommendChannelXmlParser recommendChannelXmlParser = new RecommendChannelXmlParser();
        //TODO: dummy data
        RecommendChList mRecommendChList = recommendChannelXmlParser.getRecommendchannelList();
        RecommendChInsertDataManager mRecommendChInsertDataManager = new RecommendChInsertDataManager(context);
        mRecommendChInsertDataManager.insertVodClipInsertList(mRecommendChList);
        mRecommendChannelCallback.RecommendChannelCallback(mRecommendChList);
    }

}
