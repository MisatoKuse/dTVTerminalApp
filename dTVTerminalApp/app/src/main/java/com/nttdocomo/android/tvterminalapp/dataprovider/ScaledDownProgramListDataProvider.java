/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ScaledDownProgramChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ScaledDownProgramList;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.ChannelsInfo;
import com.nttdocomo.android.tvterminalapp.model.program.Schedule;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ScaledDownProgramListChannelListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ScaledDownProgramListWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListChannelListJsonParser.SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CHNO;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListChannelListJsonParser.SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListJsonParser.SCALEDDOWN_PROGRAM_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListJsonParser.SCALEDDOWN_PROGRAM_LIST_CHNO;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListJsonParser.SCALEDDOWN_PROGRAM_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListJsonParser.SCALEDDOWN_PROGRAM_LIST_LINEAR_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ScaledDownProgramListJsonParser.SCALEDDOWN_PROGRAM_LIST_LINEAR_END_DATE;


/**
 * 縮小番組表用データプロバイダークラス
 * 機能：　縮小番組表画面で使うデータを提供するクラスである
 */
public class ScaledDownProgramListDataProvider implements DbThread.DbOperation,
        ScaledDownProgramListWebClient.ScaledDownProgramListJsonParserCallback,
        ScaledDownProgramListChannelListWebClient.ScaledDownProgramChannelListJsonParserCallback {

    //CH毎番組取得のフィルター
    public static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE = "release";
    public static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_TESTA = "testa";
    public static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_DEMO = "demo";

    private ApiDataProviderCallback mApiDataProviderCallback = null;

    @Override
    public void onDbOperationFinished(boolean isSuccessful, List<Map<String, String>> resultSet, int operationId) {
        /* 2017/10/31日実装予定 */
    }

    @Override
    public List<Map<String, String>> dbOperation() throws Exception {
        /* 2017/10/31日実装予定 */
        return null;
    }

    @Override
    public void onScaledDownProgramListJsonParsed(List<ScaledDownProgramList> scaledDownProgramLists) {
        ChannelsInfo channelsInfo = null;
        if (scaledDownProgramLists != null) {
            //チャンネルデータ
            ScaledDownProgramList scaledDownProgramList = scaledDownProgramLists.get(0);
            List<HashMap<String, String>> mChannelProgramList = scaledDownProgramList.getVcList();
            if(mChannelProgramList != null){
                channelsInfo = new ChannelsInfo();
                ArrayList<Schedule> mScheduleList;
                for(int i=0; i < mChannelProgramList.size();i++ ){//CH毎番組データ取得して、整形する
                    HashMap<String, String> hashMap = mChannelProgramList.get(i);
                    Schedule mSchedule = new Schedule();
                    String startDate = hashMap.get(SCALEDDOWN_PROGRAM_LIST_LINEAR_START_DATE);
                    String endDate = hashMap.get(SCALEDDOWN_PROGRAM_LIST_LINEAR_END_DATE);
                    String thumb = hashMap.get(SCALEDDOWN_PROGRAM_LIST_THUMB);
                    String title = hashMap.get(SCALEDDOWN_PROGRAM_LIST_TITLE);
                    String chno = hashMap.get(SCALEDDOWN_PROGRAM_LIST_CHNO);
                    mSchedule.setStartTime(startDate);
                    mSchedule.setEndTime(endDate);
                    mSchedule.setImageUrl(thumb);
                    mSchedule.setContent(title);
                    mSchedule.setmChno(chno);
                    if(!TextUtils.isEmpty(chno)){//CH毎番組データ取得して、整形する
                        ArrayList<Channel> oldChannelList = channelsInfo.getmChannels();
                        boolean isExist = false;
                        if(oldChannelList.size() > 0){//番組ID存在するのをチェックする
                            for(int j=0; j<oldChannelList.size(); j++){
                                Channel oldChannel = oldChannelList.get(j);
                                if(oldChannel.getChno() == Integer.valueOf(chno)){//番組ID存在する場合
                                    ArrayList<Schedule> oldSchedule = oldChannel.getmSchedules();
                                    oldSchedule.add(mSchedule);
                                    isExist = true;
                                    break;
                                }
                            }
                        }
                        if(!isExist){//番組ID存在しない場合
                            mScheduleList = new ArrayList<>();
                            mScheduleList.add(mSchedule);
                            channelsInfo.addChannel(new Channel(title, Integer.valueOf(chno), mScheduleList));
                        }
                    }
                }

            }
        }

        if (null != mApiDataProviderCallback) {
            mApiDataProviderCallback.channelInfoCallback(channelsInfo);
        }
    }

    @Override
    public void onScaledDownProgramChannelListJsonParsed(List<ScaledDownProgramChannelList> scaledDownProgramLists) {
        ArrayList<Channel> channels = null;
        if (scaledDownProgramLists != null) {
            ScaledDownProgramChannelList scaledDownProgramChannelList = scaledDownProgramLists.get(0);
            List<HashMap<String, String>> channelList = scaledDownProgramChannelList.getVcList();
            if (channelList != null) {
                channels = new ArrayList<>();
                for (int i = 0; i < channelList.size(); i++) {
                    HashMap<String, String> hashMap = channelList.get(i);
                    String chNo = hashMap.get(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_CHNO);
                    String title = hashMap.get(SCALEDDOWN_PROGRAM_LIST_CHANNEL_LIST_TITLE);
                    if (!TextUtils.isEmpty(chNo)) {
                        channels.add(new Channel(title, Integer.valueOf(chNo), null));
                    }
                }
            }
        }
        if (null != mApiDataProviderCallback) {
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
         * @param channelsInfo 画面に渡すチャンネル番組情報
         */
        void channelInfoCallback(ChannelsInfo channelsInfo);

        /**
         * チャンネルリストを戻す
         *
         * @param channels　画面に渡すチャンネル情報
         */
        void channelListCallback(ArrayList<Channel> channels);
    }

    /**
     * CH一覧取得
     *
     * @param limit  レスポンスの最大件数
     * @param offset 取得位置(1～)
     * @param filter release、testa、demo ※指定なしの場合release
     * @param type   dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void getChannelList(int limit, int offset, String filter, String type) {
        /*if(){

        } else {*/
        ScaledDownProgramListChannelListWebClient mChannelList = new ScaledDownProgramListChannelListWebClient();
        mChannelList.getScaledDownProgramListApi(limit, offset, filter, type, this);
        /*}*/
    }

    /**
     * 通信/DBで、チャンネル毎番組取得
     *
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     */
    public void getProgram(List<String> chList, List<String> dateList) {
        getProgram(chList, dateList, PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE);
    }

    /**
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     * @param filter   文字列、Filter
     */
    private void getProgram(List<String> chList, List<String> dateList, final String filter) {
        /*if(){

        } else {*/
        ScaledDownProgramListWebClient mChannelProgramList = new ScaledDownProgramListWebClient();
        mChannelProgramList.getScaledDownProgramListApi(chList, dateList, filter, this);
        /*}*/
    }

}
