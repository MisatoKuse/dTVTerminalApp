/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.ChannelsInfo;
import com.nttdocomo.android.tvterminalapp.model.program.Schedule;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_CHNO;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.ChannelJsonParser.CHANNEL_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_CHNO;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_THUMB;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_LINEAR_START_DATE;
import static com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser.TV_SCHEDULE_LIST_LINEAR_END_DATE;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.CHANNEL_LAST_UPDATE;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.TVSCHEDULE_LAST_UPDATE;

/**
 * 縮小番組表用データプロバイダークラス
 * 機能：　縮小番組表画面で使うデータを提供するクラスである
 */
public class ScaledDownProgramListDataProvider implements DbThread.DbOperation,
        ChannelWebClient.ChannelJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback {

    //CH毎番組取得のフィルター
    public static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE = "release";
    public static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_TESTA = "testa";
    public static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_DEMO = "demo";

    private ApiDataProviderCallback mApiDataProviderCallback = null;
    private Context mContext;
    //共通スレッド使う
    private static final int CHANNEL_UPDATE = 1;//チャンネル更新
    private static final int SCHEDULE_UPDATE = 2;//番組更新
    private static final int CHANNEL_SELECT = 3;//チャンネル検索
    private static final int SCHEDULE_SELECT = 4;//番組検索

    private ChannelList mChannelList;
    private TvScheduleList mTvScheduleList;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public ScaledDownProgramListDataProvider(Context mContext) {
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ScaledDownProgramListDataProvider.ApiDataProviderCallback) mContext;
    }

    @Override
    public void onDbOperationFinished(boolean isSuccessful, List<Map<String, String>> resultSet, int operationId) {
        if(isSuccessful){
            DateUtils dateUtils;
            switch (operationId){
                case CHANNEL_UPDATE:
                    dateUtils = new DateUtils(mContext);
                    dateUtils.addLastDate(CHANNEL_LAST_UPDATE);
                    break;
                case SCHEDULE_UPDATE:
                    dateUtils = new DateUtils(mContext);
                    dateUtils.addLastDate(TVSCHEDULE_LAST_UPDATE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(int mOperationId) throws Exception {
        switch (mOperationId){
            case CHANNEL_UPDATE://サーバーから取得したチャンネルデータをDBに保存する
                ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(mContext);
                channelInsertDataManager.insertChannelInsertList(mChannelList);
                break;
            case SCHEDULE_UPDATE://サーバーから取得した番組データをDBに保存する
                TvScheduleInsertDataManager scheduleInsertDataManager = new TvScheduleInsertDataManager(mContext);
                scheduleInsertDataManager.insertTvScheduleInsertList(mTvScheduleList);
                break;
            case CHANNEL_SELECT://DBからチャンネルデータを取得して、画面に返却する
                HomeDataManager channelDataManager = new HomeDataManager(mContext);
                List<Map<String, String>> channelList = channelDataManager.selectChannelListHomeData();
                ArrayList<Channel> channels = new ArrayList<>();
                for (int i = 0; i < channelList.size(); i++) {
                    Map<String, String> hashMap = channelList.get(i);
                    String chNo = hashMap.get(CHANNEL_LIST_CHNO);
                    String title = hashMap.get(CHANNEL_LIST_TITLE);
                    if (!TextUtils.isEmpty(chNo)) {
                        channels.add(new Channel(title, Integer.valueOf(chNo), null));
                    }
                }
                if (null != mApiDataProviderCallback) {
                    mApiDataProviderCallback.channelListCallback(channels);
                }
                break;
            case SCHEDULE_SELECT://DBから番組データを取得して、画面に返却する
                HomeDataManager scheduleDataManager = new HomeDataManager(mContext);
                List<Map<String, String>> scheduleList = scheduleDataManager.selectTvScheduleListHomeData();
                ChannelsInfo channelsInfo = null;
                if (scheduleList!=null && scheduleList.size() > 0){
                    channelsInfo = new ChannelsInfo();
                    ArrayList<Schedule> mScheduleList;
                    for(int i=0; i < scheduleList.size();i++ ){//CH毎番組データ取得して、整形する
                        Map<String, String> hashMap = scheduleList.get(i);
                        Schedule mSchedule = new Schedule();
                        String startDate = hashMap.get(TV_SCHEDULE_LIST_LINEAR_START_DATE);
                        String endDate = hashMap.get(TV_SCHEDULE_LIST_LINEAR_END_DATE);
                        String thumb = hashMap.get(TV_SCHEDULE_LIST_THUMB);
                        String title = hashMap.get(TV_SCHEDULE_LIST_TITLE);
                        String chno = hashMap.get(TV_SCHEDULE_LIST_CHNO);
                        mSchedule.setStartTime(startDate);
                        mSchedule.setEndTime(endDate);
                        mSchedule.setImageUrl(thumb);
                        mSchedule.setTitle(title);
                        mSchedule.setmChno(chno);
                        if(!TextUtils.isEmpty(chno)){//CH毎番組データ取得して、整形する
                            ArrayList<Channel> oldChannelList = channelsInfo.getChannels();
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
                if (null != mApiDataProviderCallback) {
                    mApiDataProviderCallback.channelInfoCallback(channelsInfo);
                }
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public void onChannelJsonParsed(List<ChannelList> channelLists) {
        ArrayList<Channel> channels = null;
        if (channelLists != null) {
            mChannelList = channelLists.get(0);
            List<HashMap<String, String>> channelList = mChannelList.getClList();
            if (channelList != null) {
                channels = new ArrayList<>();
                setChannelData(channels, channelList);
                Handler handler =new Handler();//チャンネル情報更新
                try {
                    DbThread t = new DbThread(handler,this, CHANNEL_UPDATE);
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (null != mApiDataProviderCallback) {
            mApiDataProviderCallback.channelListCallback(channels);
        }
    }

    @Override
    public void onTvScheduleJsonParsed(List<TvScheduleList> tvScheduleList) {
        ChannelsInfo channelsInfo = null;
        if (tvScheduleList != null) {
            //チャンネルデータ
            mTvScheduleList = tvScheduleList.get(0);
            List<HashMap<String, String>> mChannelProgramList = mTvScheduleList.geTvsList();
            if(mChannelProgramList != null){
                channelsInfo = new ChannelsInfo();
                ArrayList<Schedule> mScheduleList;
                for(int i=0; i < mChannelProgramList.size();i++ ){//CH毎番組データ取得して、整形する
                    HashMap<String, String> hashMap = mChannelProgramList.get(i);
                    Schedule mSchedule = new Schedule();
                    String startDate = hashMap.get(TV_SCHEDULE_LIST_LINEAR_START_DATE);
                    String endDate = hashMap.get(TV_SCHEDULE_LIST_LINEAR_END_DATE);
                    String thumb = hashMap.get(TV_SCHEDULE_LIST_THUMB);
                    String title = hashMap.get(TV_SCHEDULE_LIST_TITLE);
                    String chno = hashMap.get(TV_SCHEDULE_LIST_CHNO);
                    mSchedule.setStartTime(startDate);
                    mSchedule.setEndTime(endDate);
                    mSchedule.setImageUrl(thumb);
                    mSchedule.setTitle(title);
                    mSchedule.setmChno(chno);
                    if(!TextUtils.isEmpty(chno)){//CH毎番組データ取得して、整形する
                        ArrayList<Channel> oldChannelList = channelsInfo.getChannels();
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
                Handler handler =new Handler();//番組情報更新
                try {
                    DbThread t = new DbThread(handler,this, SCHEDULE_UPDATE);
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (null != mApiDataProviderCallback) {
            mApiDataProviderCallback.channelInfoCallback(channelsInfo);
        }
    }

    /**
     * チャンネルデータの整形
     */
    private void setChannelData(ArrayList<Channel> channels, List<HashMap<String, String>> channelList){
        for (int i = 0; i < channelList.size(); i++) {
            HashMap<String, String> hashMap = channelList.get(i);
            String chNo = hashMap.get(CHANNEL_LIST_CHNO);
            String title = hashMap.get(CHANNEL_LIST_TITLE);
            if (!TextUtils.isEmpty(chNo)) {
                channels.add(new Channel(title, Integer.valueOf(chNo), null));
            }
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
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(CHANNEL_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler =new Handler();//チャンネル情報更新
            try {
                DbThread t = new DbThread(handler,this, CHANNEL_SELECT);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ChannelWebClient mChannelList = new ChannelWebClient();
            mChannelList.getChannelApi(limit, offset, filter, type, this);
        }
    }

    /**
     * 通信/DBで、チャンネル毎番組取得
     *
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     */
    public void getProgram(int[] chList, String[] dateList) {
        getProgram(chList, dateList, PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE);
    }

    /**
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     * @param filter   文字列、Filter
     */
    private void getProgram(int[] chList, String[] dateList, final String filter) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(TVSCHEDULE_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler =new Handler();//チャンネル情報更新
            try {
                DbThread t = new DbThread(handler,this, SCHEDULE_SELECT);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            TvScheduleWebClient mChannelProgramList = new TvScheduleWebClient();
            mChannelProgramList.getTvScheduleApi(chList, dateList, filter, this);
        }
    }

}
