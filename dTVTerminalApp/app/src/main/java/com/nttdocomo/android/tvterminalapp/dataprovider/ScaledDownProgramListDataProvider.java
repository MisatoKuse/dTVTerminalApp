/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ScaledDownProgramChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ScaledDownProgramList;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.ChannelsInfo;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ScaledDownProgramListChannelListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ScaledDownProgramListWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 縮小番組表用データプロバイダークラス
 *      機能：　縮小番組表画面で使うデータを提供するクラスである
 */
public class ScaledDownProgramListDataProvider implements DbThread.DbOperation,
        ScaledDownProgramListWebClient.ScaledDownProgramListJsonParserCallback,
        ScaledDownProgramListChannelListWebClient.ScaledDownProgramChannelListJsonParserCallback{

    //CH毎番組取得のフィルター
    public static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE = "release";
    public static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_TESTA = "testa";
    public static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_DEMO = "demo";

    private ApiDataProviderCallback mApiDataProviderCallback=null;

    @Override
    public void onDbOperationFinished(boolean isSuccessful, List<Map<String, String>> resultSet, int operationId) {
        /* 2017/10/30日実装予定 */
    }

    @Override
    public List<Map<String, String>> dbOperation() throws Exception {
        /* 2017/10/30日実装予定 */
        return null;
    }

    @Override
    public void onScaledDownProgramListJsonParsed(List<ScaledDownProgramList> scaledDownProgramLists) {
        ChannelsInfo channelsInfo=null;
        /* 2017/10/30日実装予定 */

        if(null!=mApiDataProviderCallback){
            mApiDataProviderCallback.channelInfoCallback(channelsInfo);
        }
    }

    @Override
    public void onScaledDownProgramChannelListJsonParsed(List<ScaledDownProgramChannelList> scaledDownProgramLists) {
        ArrayList<Channel> channels=null;
        /* 2017/10/30日実装予定 */

        if(null!=mApiDataProviderCallback){
            mApiDataProviderCallback.channelListCallback(channels);
        }
    }

    /**
     * 画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {
        /**
         * 縮小番組表多チャンネル情報を戻すコールバック
         *
         * @param channelsInfo
         */
        public void channelInfoCallback(ChannelsInfo channelsInfo);

        /**
         * チャンネルリストを戻す
         * @param channels
         */
        public void channelListCallback(ArrayList<Channel> channels);
    }

    /**
     * CH一覧取得
     * @param limit     レスポンスの最大件数
     * @param offset    取得位置(1～)
     * @param filter    release、testa、demo ※指定なしの場合release
     * @param type      dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void getChannelList(int limit, int offset, String filter, String type) {
        /* 2017/10/30日実装予定 */
    }

    /**
     * 通信/DBで、チャンネル毎番組取得
     * @param chList    配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     */
    public void getProgram(ArrayList<String> chList, ArrayList<String> dateList){
        getProgram(chList, dateList, PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE);
    }

    /**
     *
     * @param chList       配列, 中身は整数値
     * @param dateList     配列, 中身は整数値 YYYYMMDD
     * @param filter       文字列、Filter
     */
    public void getProgram(ArrayList<String> chList, ArrayList<String> dateList, final String filter){
        /* 2017/10/30日実装予定 */
    }

}
