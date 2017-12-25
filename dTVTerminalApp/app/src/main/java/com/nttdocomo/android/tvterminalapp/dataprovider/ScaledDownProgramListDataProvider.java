/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.ChannelsInfo;
import com.nttdocomo.android.tvterminalapp.model.program.Schedule;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE = "release";
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_TESTA = "testa";
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_DEMO = "demo";

    private ApiDataProviderCallback mApiDataProviderCallback = null;
    private Context mContext;
    //共通スレッド使う
    private static final int CHANNEL_UPDATE = 1;//チャンネル更新
    private static final int SCHEDULE_UPDATE = 2;//番組更新
    private static final int CHANNEL_SELECT = 3;//チャンネル検索
    private static final int SCHEDULE_SELECT = 4;//番組検索

    private static final String DISPLAY_TYPE[] = {"", "hikaritv", "dch"};
    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String SELECT_TIME_FORMAT = "yyyy-MM-ddHH:mm:ss";

    private ChannelList mChannelList;
    private TvScheduleList mTvScheduleList;
    private int channel_display_type;
    private int program_display_type;
    private String programSelectDate;

    /**
     * コンストラクタ
     *
     * @param mContext TvProgramListActivity
     */
    public ScaledDownProgramListDataProvider(Context mContext) {
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    @Override
    public void onDbOperationFinished(boolean isSuccessful, List<Map<String, String>> resultSet, int operationId) {
        if (isSuccessful) {
            switch (operationId) {
                case CHANNEL_SELECT:
                    ArrayList<Channel> channels = new ArrayList<>();
                    for (int i = 0; i < resultSet.size(); i++) {
                        Map<String, String> hashMap = resultSet.get(i);
                        String chNo = hashMap.get(JsonContents.META_RESPONSE_CHNO);
                        String title = hashMap.get(JsonContents.META_RESPONSE_TITLE);
                        String thumb = hashMap.get(JsonContents.META_RESPONSE_DEFAULT_THUMB);
                        String serviceId = hashMap.get(JsonContents.META_RESPONSE_SERVICE_ID);
                        if (!TextUtils.isEmpty(chNo)) {
                            Channel channel = new Channel();
                            channel.setChNo(Integer.parseInt(chNo));
                            channel.setTitle(title);
                            channel.setThumbnail(thumb);
                            channel.setServiceId(serviceId);
                            channels.add(channel);
                        }
                    }
                    if (null != mApiDataProviderCallback) {
                        mApiDataProviderCallback.channelListCallback(channels);
                    }
                    break;
                case SCHEDULE_SELECT:
                    ChannelsInfo channelsInfo = null;
                    if (resultSet != null && resultSet.size() > 0) {
                        channelsInfo = new ChannelsInfo();
                        ArrayList<Schedule> mScheduleList;
                        for (int i = 0; i < resultSet.size(); i++) {//CH毎番組データ取得して、整形する
                            Map<String, String> hashMap = resultSet.get(i);
                            Schedule mSchedule = new Schedule();
                            String startDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_START_DATE);
                            String endDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_END_DATE);
                            String thumb = hashMap.get(JsonContents.META_RESPONSE_DEFAULT_THUMB);
                            String title = hashMap.get(JsonContents.META_RESPONSE_TITLE);
                            String chNo = hashMap.get(JsonContents.META_RESPONSE_CHNO);
                            mSchedule.setStartTime(startDate);
                            mSchedule.setEndTime(endDate);
                            mSchedule.setImageUrl(thumb);
                            mSchedule.setTitle(title);
                            mSchedule.setChNo(chNo);
                            if (!TextUtils.isEmpty(chNo)) {//CH毎番組データ取得して、整形する
                                ArrayList<Channel> oldChannelList = channelsInfo.getChannels();
                                boolean isExist = false;
                                if (oldChannelList.size() > 0) {//番組ID存在するのをチェックする
                                    for (int j = 0; j < oldChannelList.size(); j++) {
                                        Channel oldChannel = oldChannelList.get(j);
                                        if (oldChannel.getChNo() == Integer.valueOf(chNo)) {//番組ID存在する場合
                                            ArrayList<Schedule> oldSchedule = oldChannel.getSchedules();
                                            oldSchedule.add(mSchedule);
                                            isExist = true;
                                            break;
                                        }
                                    }
                                }
                                if (!isExist) {//番組ID存在しない場合
                                    mScheduleList = new ArrayList<>();
                                    mScheduleList.add(mSchedule);
                                    Channel channel = new Channel();
                                    channel.setChNo(Integer.valueOf(chNo));
                                    channel.setTitle(title);
                                    channel.setSchedules(mScheduleList);
                                    channelsInfo.addChannel(channel);
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
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(int mOperationId) throws Exception {
        List<Map<String, String>> resultSet = null;
        switch (mOperationId) {
            case CHANNEL_UPDATE://サーバーから取得したチャンネルデータをDBに保存する
                ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(mContext);
                channelInsertDataManager.insertChannelInsertList(mChannelList, DISPLAY_TYPE[channel_display_type]);
                break;
            case SCHEDULE_UPDATE://サーバーから取得した番組データをDBに保存する
                TvScheduleInsertDataManager scheduleInsertDataManager = new TvScheduleInsertDataManager(mContext);
                scheduleInsertDataManager.insertTvScheduleInsertList(mTvScheduleList, DISPLAY_TYPE[program_display_type]);
                break;
            case CHANNEL_SELECT://DBからチャンネルデータを取得して、画面に返却する
                ProgramDataManager channelDataManager = new ProgramDataManager(mContext);
                resultSet = channelDataManager.selectChannelListProgramData(DISPLAY_TYPE[channel_display_type]);
                break;
            case SCHEDULE_SELECT://DBから番組データを取得して、画面に返却する
                ProgramDataManager scheduleDataManager = new ProgramDataManager(mContext);
                resultSet = scheduleDataManager.selectTvScheduleListProgramData(DISPLAY_TYPE[program_display_type], programSelectDate);
                break;
            default:
                break;
        }
        return resultSet;
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
                Handler handler = new Handler();//チャンネル情報更新
                try {
                    DbThread t = new DbThread(handler, this, CHANNEL_UPDATE);
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
            if (mChannelProgramList != null) {
                channelsInfo = new ChannelsInfo();
                ArrayList<Schedule> mScheduleList;
                StringBuilder baseStartDate = new StringBuilder();
                baseStartDate.append(programSelectDate);
                baseStartDate.append("04:00:00");
                StringBuilder baseEndDate = new StringBuilder();
                baseEndDate.append(programSelectDate);
                baseEndDate.append("03:00:00");
                Date selectStartDate = new Date();
                Date selectEndDate = new Date();
                SimpleDateFormat format = new SimpleDateFormat(SELECT_TIME_FORMAT, Locale.JAPAN);
                try {
                    selectStartDate = format.parse(baseStartDate.toString());
                    selectEndDate = format.parse(baseEndDate.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(selectEndDate);
                gc.add(Calendar.DAY_OF_MONTH, +1);
                selectEndDate = gc.getTime();
                for (int i = 0; i < mChannelProgramList.size(); i++) {//CH毎番組データ取得して、整形する
                    HashMap<String, String> hashMap = mChannelProgramList.get(i);
                    Schedule schedule = new Schedule();
                    String startDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_START_DATE);
                    StringBuilder startBuilder = new StringBuilder();
                    startBuilder.append(startDate.substring(0, 10));
                    startBuilder.append(startDate.substring(11, 19));
                    Date day = new Date();
                    try {
                        day = format.parse(startBuilder.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //TODO 番組表表示させるため、コメントアウトします
                    /*if(day.compareTo(selectStartDate) !=-1 && day.compareTo(selectEndDate)!=1){*/
                        String endDate = hashMap.get(JsonContents.META_RESPONSE_AVAIL_END_DATE);
                        String thumb = hashMap.get(JsonContents.META_RESPONSE_DEFAULT_THUMB);
                        String title = hashMap.get(JsonContents.META_RESPONSE_TITLE);
                        String chNo = hashMap.get(JsonContents.META_RESPONSE_CHNO);
                        schedule.setStartTime(startDate);
                        schedule.setEndTime(endDate);
                        schedule.setImageUrl(thumb);
                        schedule.setTitle(title);
                        schedule.setChNo(chNo);
                        schedule.setClipRequestData(setClipData(hashMap));

                        if (!TextUtils.isEmpty(chNo)) {//CH毎番組データ取得して、整形する
                            ArrayList<Channel> oldChannelList = channelsInfo.getChannels();
                            boolean isExist = false;
                            if (oldChannelList.size() > 0) {//番組ID存在するのをチェックする
                                for (int j = 0; j < oldChannelList.size(); j++) {
                                    Channel oldChannel = oldChannelList.get(j);
                                    if (oldChannel.getChNo() == Integer.valueOf(chNo)) {//番組ID存在する場合
                                        ArrayList<Schedule> oldSchedule = oldChannel.getSchedules();
                                        oldSchedule.add(schedule);
                                        isExist = true;
                                        break;
                                    }
                                }
                            }
                            if (!isExist) {//番組ID存在しない場合
                                mScheduleList = new ArrayList<>();
                                mScheduleList.add(schedule);
                                Channel channel = new Channel();
                                channel.setChNo(Integer.parseInt(chNo));
                                channel.setTitle(title);
                                channel.setSchedules(mScheduleList);
                                channelsInfo.addChannel(channel);
                            }
                        }
                    //TODO 番組表表示させるため、コメントアウトします
                    /*}*/
                }
                Handler handler = new Handler();//番組情報更新
                try {
                    DbThread t = new DbThread(handler, this, SCHEDULE_UPDATE);
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
     * クリップリクエストに必要なデータを作成する(番組表用)
     *
     * @param hashMap 番組表データ
     * @return Clipリクエストに必要なデータ
     */
    private static ClipRequestData setClipData(HashMap<String, String> hashMap){
        ClipRequestData requestData = new ClipRequestData();
        requestData.setCrid(hashMap.get(JsonContents.META_RESPONSE_CRID));
        requestData.setServiceId(hashMap.get(JsonContents.META_RESPONSE_SERVICE_ID));
        requestData.setEventId(hashMap.get(JsonContents.META_RESPONSE_EVENT_ID));
        requestData.setTitleId(hashMap.get(JsonContents.META_RESPONSE_TITLE_ID));
        requestData.setTitle(hashMap.get(JsonContents.META_RESPONSE_TITLE));
        requestData.setRValue(hashMap.get(JsonContents.META_RESPONSE_R_VALUE));
        requestData.setLinearStartDate(String.valueOf(hashMap.get(JsonContents.META_RESPONSE_AVAIL_START_DATE)));
        requestData.setLinearEndDate(String.valueOf(hashMap.get(JsonContents.META_RESPONSE_AVAIL_END_DATE)));
        requestData.setSearchOk(hashMap.get(JsonContents.META_RESPONSE_SEARCH_OK));
        requestData.setClipTarget(hashMap.get(JsonContents.META_RESPONSE_TITLE)); //TODO:仕様確認中 現在はトーストにタイトル名を表示することとしています
        requestData.setIsNotify(hashMap.get(JsonContents.META_RESPONSE_DISP_TYPE),
                hashMap.get(JsonContents.META_RESPONSE_CONTENT_TYPE),
                String.valueOf(hashMap.get(JsonContents.META_RESPONSE_AVAIL_END_DATE)),
                hashMap.get(JsonContents.META_RESPONSE_TV_SERVICE), hashMap.get(JsonContents.META_RESPONSE_DTV));
        return requestData;
    }
    /**
     * チャンネルデータの整形
     */
    private void setChannelData(ArrayList<Channel> channels, List<HashMap<String, String>> channelList) {
        for (int i = 0; i < channelList.size(); i++) {
            HashMap<String, String> hashMap = channelList.get(i);
            String chNo = hashMap.get(JsonContents.META_RESPONSE_CHNO);
            String title = hashMap.get(JsonContents.META_RESPONSE_TITLE);
            String thumbnail = hashMap.get(JsonContents.META_RESPONSE_DEFAULT_THUMB);
            String serviceId = hashMap.get(JsonContents.META_RESPONSE_SERVICE_ID);
            if (!TextUtils.isEmpty(chNo)) {
                Channel channel = new Channel();
                channel.setTitle(title);
                channel.setChNo(Integer.parseInt(chNo));
                channel.setThumbnail(thumbnail);
                channel.setServiceId(serviceId);
                channels.add(channel);
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
         * @param channels 　画面に渡すチャンネル情報
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
    public void getChannelList(int limit, int offset, String filter, int type) {
        this.channel_display_type = type;
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(CHANNEL_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler();//チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, CHANNEL_SELECT);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            dateUtils.addLastProgramDate(CHANNEL_LAST_UPDATE);
            ChannelWebClient mChannelList = new ChannelWebClient();
            mChannelList.getChannelApi(limit, offset, filter, DISPLAY_TYPE[type], this);
        }
    }

    /**
     * 通信/DBで、チャンネル毎番組取得
     *
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     */
    public void getProgram(int[] chList, String[] dateList, int display_type) {
        getProgram(chList, dateList, PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE, display_type);
    }

    /**
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     * @param filter   文字列、Filter
     */
    private void getProgram(int[] chList, String[] dateList, final String filter, int type) {
        this.program_display_type = type;
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(TVSCHEDULE_LAST_UPDATE);
        programSelectDate = dateList[0];
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler();//チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, SCHEDULE_SELECT);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //日付の比較
            int startPosition;
            if (type == 2) {
                startPosition = -7;
                dateList = new String[15];
            } else {
                startPosition = 0;
                dateList = new String[8];
            }
            int j = 0;
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);
            //取得日付範囲を作る
            for (int i = startPosition; i < 8; i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, i);
                dateList[j] = sdf.format(calendar.getTime());
                j++;
            }
            dateUtils.addLastProgramDate(TVSCHEDULE_LAST_UPDATE);
            TvScheduleWebClient mChannelProgramList = new TvScheduleWebClient();
            mChannelProgramList.getTvScheduleApi(chList, dateList, filter, this);
        }
    }

}
