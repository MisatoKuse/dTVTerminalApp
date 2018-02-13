/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 縮小番組表用データプロバイダークラス
 * 機能：　縮小番組表画面で使うデータを提供するクラスである.
 */
public class ScaledDownProgramListDataProvider extends ClipKeyListDataProvider implements DbThread.DbOperation,
        ChannelWebClient.ChannelJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback {

    private ApiDataProviderCallback mApiDataProviderCallback = null;
    private Context mContext = null;

    private ChannelList mChannelList = null;
    private TvScheduleList mTvScheduleList = null;
    private int mChannelDisplayType = 0;
    private int mProgramDisplayType = 0;
    private String mProgramSelectDate = null;

    //共通スレッド使う
    private static final int CHANNEL_UPDATE = 1;//チャンネル更新
    private static final int SCHEDULE_UPDATE = 2;//番組更新
    private static final int CHANNEL_SELECT = 3;//チャンネル検索
    private static final int SCHEDULE_SELECT = 4;//番組検索

    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String SELECT_TIME_FORMAT = "yyyy-MM-ddHH:mm:ss";

    //CH毎番組取得のフィルター
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE = "release";
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_TESTA = "testa";
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_DEMO = "demo";

    // クリップキーリスト取得済み判定
    private boolean tvClipKeyListResponse = false;
    private boolean vodClipKeyListResponse = false;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean isStop = false;
    /**
     * チャンネルリスト取得WebClient.
     */
    private ChannelWebClient mChannelWebClient = null;
    /**
     * 番組リスト取得WebClient.
     */
    private TvScheduleWebClient mTvScheduleWebClient = null;

    /**
     * コンストラクタ.
     *
     * @param mContext TvProgramListActivity
     */
    public ScaledDownProgramListDataProvider(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    @Override
    public void onDbOperationFinished(boolean isSuccessful, List<Map<String, String>> resultSet, int operationId) {
        if (isSuccessful) {
            switch (operationId) {
                case CHANNEL_SELECT:
                    ArrayList<ChannelInfo> channels = new ArrayList<>();
                    for (int i = 0; i < resultSet.size(); i++) {
                        ArrayList<ScheduleInfo> mScheduleList;
                        Map<String, String> hashMap = resultSet.get(i);
                        String chNo = hashMap.get(JsonConstants.META_RESPONSE_CHNO);
                        String title = hashMap.get(JsonConstants.META_RESPONSE_TITLE);
                        String thumb = hashMap.get(JsonConstants.META_RESPONSE_DEFAULT_THUMB);
                        String serviceId = hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID);
                        String rValue = hashMap.get(JsonConstants.META_RESPONSE_R_VALUE);
                        String dispType = hashMap.get(JsonConstants.META_RESPONSE_DISP_TYPE);
                        String searchOk = hashMap.get(JsonConstants.META_RESPONSE_SEARCH_OK);
                        String dtv = hashMap.get(JsonConstants.META_RESPONSE_DTV);
                        String dtvType = hashMap.get(JsonConstants.META_RESPONSE_DTV_TYPE);

                        ScheduleInfo mSchedule = new ScheduleInfo();
                        String startDate = hashMap.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE);
                        String endDate = hashMap.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE);
                        mSchedule.setStartTime(startDate);
                        mSchedule.setEndTime(endDate);
                        mSchedule.setImageUrl(thumb);
                        mSchedule.setTitle(title);
                        mSchedule.setChNo(chNo);
                        mSchedule.setContentType(hashMap.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
                        mSchedule.setDtv(dtv);
                        mSchedule.setDispType(dispType);
                        mSchedule.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
                        mSchedule.setClipRequestData(setClipData((HashMap<String, String>) hashMap));
                        mSchedule.setContentsId(hashMap.get(JsonConstants.META_RESPONSE_CID));

                        if (!TextUtils.isEmpty(chNo)) {
                            ChannelInfo channel = new ChannelInfo();
                            channel.setChNo(Integer.parseInt(chNo));
                            channel.setTitle(title);
                            channel.setThumbnail(thumb);
                            channel.setServiceId(serviceId);
                            mScheduleList = new ArrayList<>();
                            mScheduleList.add(mSchedule);
                            channel.setSchedules(mScheduleList);
                            channels.add(channel);
                        }
                    }
                    if (null != mApiDataProviderCallback) {
                        mApiDataProviderCallback.channelListCallback(channels);
                    }
                    break;
                case SCHEDULE_SELECT:
                    ChannelInfoList channelsInfo = null;
                    if (resultSet != null && resultSet.size() > 0) {
                        channelsInfo = new ChannelInfoList();
                        for (int i = 0; i < resultSet.size(); i++) {//CH毎番組データ取得して、整形する
                            HashMap<String, String> hashMap =  (HashMap<String, String>)resultSet.get(i);
                            setScheduleInfo(hashMap, channelsInfo);
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
                channelInsertDataManager.insertChannelInsertList(mChannelList, JsonConstants.DISPLAY_TYPE[mChannelDisplayType]);
                break;
            case SCHEDULE_UPDATE://サーバーから取得した番組データをDBに保存する
                TvScheduleInsertDataManager scheduleInsertDataManager = new TvScheduleInsertDataManager(mContext);
                scheduleInsertDataManager.insertTvScheduleInsertList(mTvScheduleList, JsonConstants.DISPLAY_TYPE[mProgramDisplayType]);
                break;
            case CHANNEL_SELECT://DBからチャンネルデータを取得して、画面に返却する
                ProgramDataManager channelDataManager = new ProgramDataManager(mContext);
                resultSet = channelDataManager.selectChannelListProgramData(JsonConstants.DISPLAY_TYPE[mChannelDisplayType]);
                break;
            case SCHEDULE_SELECT://DBから番組データを取得して、画面に返却する
                ProgramDataManager scheduleDataManager = new ProgramDataManager(mContext);
                resultSet = scheduleDataManager.selectTvScheduleListProgramData(JsonConstants.DISPLAY_TYPE[mProgramDisplayType], mProgramSelectDate);
                break;
            default:
                break;
        }
        return resultSet;
    }

    @Override
    public void onChannelJsonParsed(List<ChannelList> channelLists) {
        ArrayList<ChannelInfo> channels = null;
        if (channelLists != null) {
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastProgramDate(DateUtils.TVSCHEDULE_LAST_UPDATE);
            mChannelList = channelLists.get(0);
            List<HashMap<String, String>> channelList = mChannelList.getChannelList();
            if (channelList != null) {
                channels = new ArrayList<>();
                setChannelData(channels, channelList);
                Handler handler = new Handler();//チャンネル情報更新
                try {
                    DbThread t = new DbThread(handler, this, CHANNEL_UPDATE);
                    t.start();
                } catch (Exception e) {
                    DTVTLogger.debug(e);
                }
            }
        }

        if (null != mApiDataProviderCallback) {
            mApiDataProviderCallback.channelListCallback(channels);
        }
    }

    @Override
    public void onTvScheduleJsonParsed(List<TvScheduleList> tvScheduleList) {
        if (tvScheduleList != null) {
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastProgramDate(DateUtils.CHANNEL_LAST_UPDATE);
            //チャンネルデータ
            mTvScheduleList = tvScheduleList.get(0);
            if (mRequiredClipKeyList) {
                // クリップキーリストを取得
                tvClipKeyListResponse = false;
                vodClipKeyListResponse = false;
                getClipKeyList();
            } else {
                if (null != mApiDataProviderCallback) {
                    mApiDataProviderCallback.channelInfoCallback(setProgramListContentData());
                }
            }
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            List<Map<String, String>> scheduleList = new ArrayList<>();
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            scheduleList = homeDataManager.selectTvScheduleListHomeData();
            mTvScheduleList.setTvsList(scheduleList);
        }

    }

    @Override
    public void onTvClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        super.onTvClipKeyListJsonParsed(clipKeyListResponse);
        if (vodClipKeyListResponse) {
            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.channelInfoCallback(setProgramListContentData());
            }
        } else {
            tvClipKeyListResponse = true;
        }
    }

    @Override
    public void onVodClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        if (tvClipKeyListResponse) {
            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.channelInfoCallback(setProgramListContentData());
            }
        } else {
            vodClipKeyListResponse = true;
        }
    }

    /**
     * 番組表をチャンネルに入れる
     */
    private void setScheduleInfo(HashMap<String, String> hashMap, ChannelInfoList channelsInfo){
        ScheduleInfo mSchedule = new ScheduleInfo();
        String startDate = hashMap.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE);
        String endDate = hashMap.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE);
        String thumb = hashMap.get(JsonConstants.META_RESPONSE_THUMB_448);
        String title = hashMap.get(JsonConstants.META_RESPONSE_TITLE);
        String detail = hashMap.get(JsonConstants.META_RESPONSE_EPITITLE);
        String chNo = hashMap.get(JsonConstants.META_RESPONSE_CHNO);
        String rValue = hashMap.get(JsonConstants.META_RESPONSE_R_VALUE);
        String dispType = hashMap.get(JsonConstants.META_RESPONSE_DISP_TYPE);
        String contentType = hashMap.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
        String searchOk = hashMap.get(JsonConstants.META_RESPONSE_SEARCH_OK);
        String dtv = hashMap.get(JsonConstants.META_RESPONSE_DTV);
        String dtvType = hashMap.get(JsonConstants.META_RESPONSE_DTV_TYPE);
        mSchedule.setStartTime(startDate);
        mSchedule.setEndTime(endDate);
        mSchedule.setImageUrl(thumb);
        mSchedule.setTitle(title);
        mSchedule.setDetail(detail);
        mSchedule.setChNo(chNo);
        mSchedule.setRValue(rValue);
        mSchedule.setContentType(hashMap.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
        mSchedule.setDtv(dtv);
        mSchedule.setDispType(dispType);
        mSchedule.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
        mSchedule.setClipRequestData(setClipData(hashMap));
        mSchedule.setClipStatus(getClipStatus(dispType, contentType, dtv,
                hashMap.get(JsonConstants.META_RESPONSE_CRID),
                hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID),
                hashMap.get(JsonConstants.META_RESPONSE_EVENT_ID),
                hashMap.get(JsonConstants.META_RESPONSE_TITLE_ID)));
        mSchedule.setContentsId(hashMap.get(JsonConstants.META_RESPONSE_CID));

        if (!TextUtils.isEmpty(chNo)) {//CH毎番組データ取得して、整形する
            ArrayList<ChannelInfo> oldChannelList = channelsInfo.getChannels();
            boolean isExist = false;
            if (oldChannelList.size() > 0) {//番組ID存在するのをチェックする
                for (int j = 0; j < oldChannelList.size(); j++) {
                    ChannelInfo oldChannel = oldChannelList.get(j);
                    if (oldChannel.getChNo() == Integer.valueOf(chNo)) {//番組ID存在する場合
                        ArrayList<ScheduleInfo> oldSchedule = oldChannel.getSchedules();
                        oldSchedule.add(mSchedule);
                        isExist = true;
                        break;
                    }
                }
            }
            if (!isExist) {//番組ID存在しない場合
                ArrayList<ScheduleInfo> mScheduleList = new ArrayList<>();
                mScheduleList.add(mSchedule);
                ChannelInfo channel = new ChannelInfo();
                channel.setChNo(Integer.valueOf(chNo));
                channel.setTitle(title);
                channel.setSchedules(mScheduleList);
                channelsInfo.addChannel(channel);
            }
        }
    }

    /**
     * 番組表用に番組のメタデータを整形.
     */
    private ChannelInfoList setProgramListContentData() {
        ChannelInfoList channelsInfo = null;
        List<HashMap<String, String>> mChannelProgramList = mTvScheduleList.geTvsList();
        if (mChannelProgramList != null) {
            channelsInfo = new ChannelInfoList();
            ArrayList<ScheduleInfo> mScheduleList = null;
            for (int i = 0; i < mChannelProgramList.size(); i++) {
                //CH毎番組データ取得して、整形する
                HashMap<String, String> hashMap = mChannelProgramList.get(i);
                setScheduleInfo(hashMap, channelsInfo);
            }
            Handler handler = new Handler();
            //番組情報更新
            try {
                DbThread t = new DbThread(handler, this, SCHEDULE_UPDATE);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        }
        return channelsInfo;
    }

    /**
     * クリップリクエストに必要なデータを作成する(番組表用).
     *
     * @param hashMap 番組表データ
     * @return Clipリクエストに必要なデータ
     */
    private static ClipRequestData setClipData(final HashMap<String, String> hashMap) {
        String dispType = hashMap.get(JsonConstants.META_RESPONSE_DISP_TYPE);
        String contentsType = hashMap.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
        ClipRequestData requestData = new ClipRequestData();
        requestData.setCrid(hashMap.get(JsonConstants.META_RESPONSE_CRID));
        requestData.setServiceId(hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID));
        requestData.setEventId(hashMap.get(JsonConstants.META_RESPONSE_EVENT_ID));
        requestData.setTitleId(hashMap.get(JsonConstants.META_RESPONSE_TITLE_ID));
        requestData.setTitle(hashMap.get(JsonConstants.META_RESPONSE_TITLE));
        requestData.setRValue(hashMap.get(JsonConstants.META_RESPONSE_R_VALUE));
        requestData.setLinearStartDate(String.valueOf(hashMap.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE)));
        requestData.setLinearEndDate(String.valueOf(hashMap.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)));
        requestData.setSearchOk(hashMap.get(JsonConstants.META_RESPONSE_SEARCH_OK));
        requestData.setIsNotify(dispType, contentsType,
                String.valueOf(hashMap.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)),
                hashMap.get(JsonConstants.META_RESPONSE_TV_SERVICE), hashMap.get(JsonConstants.META_RESPONSE_DTV));
        requestData.setDispType(dispType);
        requestData.setContentType(contentsType);
//        requestData.setTableType(decisionTableType(contentsType, contentsType));
        return requestData;
    }

    /**
     * チャンネルデータの整形.
     *
     * @param channels    channels
     * @param channelList channelList
     */
    private void setChannelData(final ArrayList<ChannelInfo> channels,
                                final List<HashMap<String, String>> channelList) {
        for (int i = 0; i < channelList.size(); i++) {
            HashMap<String, String> hashMap = channelList.get(i);
            String chNo = hashMap.get(JsonConstants.META_RESPONSE_CHNO);
            String title = hashMap.get(JsonConstants.META_RESPONSE_TITLE);
            String thumbnail = hashMap.get(JsonConstants.META_RESPONSE_DEFAULT_THUMB);
            String serviceId = hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID);
            String chType = hashMap.get(JsonConstants.META_RESPONSE_CH_TYPE);
            String puId = hashMap.get(JsonConstants.META_RESPONSE_PUID);
            String subPuId = hashMap.get(JsonConstants.META_RESPONSE_SUB_PUID);
            String chPackPuId = hashMap.get(JsonConstants.META_RESPONSE_CHPACK
                    + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_PUID);
            String chPackSubPuId = hashMap.get(JsonConstants.META_RESPONSE_CHPACK
                    + JsonConstants.UNDER_LINE + JsonConstants.META_RESPONSE_SUB_PUID);
            if (!TextUtils.isEmpty(chNo)) {
                ChannelInfo channel = new ChannelInfo();
                channel.setTitle(title);
                channel.setChNo(Integer.parseInt(chNo));
                channel.setThumbnail(thumbnail);
                channel.setServiceId(serviceId);
                channel.setChType(chType);
                channel.setPuId(puId);
                channel.setSubPuId(subPuId);
                channel.setChPackPuId(chPackPuId);
                channel.setChPackSubPuId(chPackSubPuId);
                channels.add(channel);
            }
        }
    }

    /**
     * 画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {

        /**
         * 縮小番組表多チャンネル情報を戻すコールバック.
         *
         * @param channelsInfo 画面に渡すチャンネル番組情報
         */
        void channelInfoCallback(ChannelInfoList channelsInfo);

        /**
         * チャンネルリストを戻す.
         *
         * @param channels 　画面に渡すチャンネル情報
         */
        void channelListCallback(ArrayList<ChannelInfo> channels);
    }

    /**
     * CH一覧取得.
     *
     * @param limit  レスポンスの最大件数
     * @param offset 取得位置(1～)
     * @param filter release、testa、demo ※指定なしの場合release
     * @param type   dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    public void getChannelList(final int limit, final int offset, final String filter, final int type) {
        this.mChannelDisplayType = type;
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.CHANNEL_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler();
            //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, CHANNEL_SELECT);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        } else {
            if(!isStop){
                mChannelWebClient = new ChannelWebClient(mContext);
                mChannelWebClient.getChannelApi(limit, offset, filter, JsonConstants.DISPLAY_TYPE[type], this);
            } else {
                DTVTLogger.error("ScaledDownProgramListDataProvider is stopping connect");
            }
        }
    }

    /**
     * 通信/DBで、チャンネル毎番組取得.
     *
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     * @param display_type displayType
     */
    public void getProgram(final int[] chList, final String[] dateList, final int display_type) {
        getProgram(chList, dateList, PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE, display_type);
    }

    /**
     * 通信/DBで、チャンネル毎番組取得.
     *
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     * @param filter   文字列、Filter
     * @param type displayType
     */
    private void getProgram(final int[] chList, final String[] dateList, final String filter, final int type) {
        this.mProgramDisplayType = type;
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.TVSCHEDULE_LAST_UPDATE);
        mProgramSelectDate = dateList[0];
        String[] list = dateList;
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler();
            //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, SCHEDULE_SELECT);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        } else {
            //日付の比較
            int startPosition;
            if (type == 2) {
                startPosition = -7;
                list = new String[15];
            } else {
                startPosition = 0;
                list = new String[8];
            }
            int j = 0;
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);
            //取得日付範囲を作る
            for (int i = startPosition; i < 8; i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, i);
                list[j] = sdf.format(calendar.getTime());
                j++;
            }
            if(!isStop){
                mTvScheduleWebClient = new TvScheduleWebClient(mContext);
                mTvScheduleWebClient.getTvScheduleApi(chList, list, filter, this);
            } else {
                DTVTLogger.error("ScaledDownProgramListDataProvider is stopping connect");
            }
        }
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        isStop = true;
        if (mChannelWebClient != null) {
            mChannelWebClient.stopConnection();
        }
        if (mTvScheduleWebClient != null) {
            mTvScheduleWebClient.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        isStop = false;
        if (mChannelWebClient != null) {
            mChannelWebClient.enableConnection();
        }
        if (mTvScheduleWebClient != null) {
            mTvScheduleWebClient.enableConnection();
        }
    }
}