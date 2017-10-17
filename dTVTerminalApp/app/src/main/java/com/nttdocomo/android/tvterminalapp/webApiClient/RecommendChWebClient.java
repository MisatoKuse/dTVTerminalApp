package com.nttdocomo.android.tvterminalapp.webApiClient;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendChInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.webApiClient.xmlparser.RecommendChannelXmlParser;

import java.util.Iterator;
import java.util.Map;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 * おすすめ番組一覧取得処理
 */
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
        RecommendChList mRecommendChList = recommendChannelXmlParser.getRecommendchannelList("");
        for (int i = 0; i < mRecommendChList.getmRcList().size(); i++) {
            Iterator entries = mRecommendChList.getmRcList().get(i).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyName = (String) entry.getKey();
                String valName = (String) entry.getValue();
                System.out.println("keyName----->"+keyName+"---valName--->"+valName);
            }
        }
        RecommendChInsertDataManager mRecommendChInsertDataManager = new RecommendChInsertDataManager(context);
        mRecommendChInsertDataManager.insertVodClipInsertList(mRecommendChList);
        mRecommendChannelCallback.RecommendChannelCallback(mRecommendChList);
    }

}
