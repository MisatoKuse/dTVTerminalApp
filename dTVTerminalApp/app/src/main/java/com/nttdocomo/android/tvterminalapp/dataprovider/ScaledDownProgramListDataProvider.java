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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 縮小番組表用データプロバイダークラス
 * 機能：　縮小番組表画面で使うデータを提供するクラスである.
 */
public class ScaledDownProgramListDataProvider extends ClipKeyListDataProvider implements
        ChannelWebClient.ChannelJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback {
    /**
     * データ取得結果コールバック.
     */
    private ApiDataProviderCallback mApiDataProviderCallback = null;
    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * チャンネルデータ.
     */
    private ChannelList mChannelList = null;
    /**
     * 番組データ.
     */
    private TvScheduleList mTvScheduleList = null;
    /**
     * チャンネル番号で仕分けした番組データ.
     */
    private ChannelInfoList mChannelsInfoList = null;
    /**
     * チャンネルタイプ(dチャンネル、ひかりTV多ch、全て).
     */
    private String mChannelDisplayType = "";
    /**
     * 取得要求日付.
     */
    private String mProgramSelectDate = null;

    //共通スレッド使う
    /**
     * チャンネル更新.
     */
    private static final int CHANNEL_UPDATE = 1;
    /**
     * 番組更新.
     */
    private static final int SCHEDULE_UPDATE = 2;
    /**
     * チャンネル検索.
     */
    private static final int CHANNEL_SELECT = 3;
    /**
     * 番組検索.
     */
    private static final int SCHEDULE_SELECT = 4;
    /**
     * 日付フォーマット.
     */
    private static final String DATE_FORMAT = "yyyyMMdd";

    //CH毎番組取得のフィルター
    /**
     * release.
     */
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE = "release";
    /**
     * testa.
     */
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_TESTA = "testa";
    /**
     * demo.
     */
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_DEMO = "demo";

    /**
     * tvコンテンツのクリップキーリスト取得済み判定.
     */
    private boolean tvClipKeyListResponse = false;
    /**
     * vodコンテンツのクリップキーリスト取得済み判定.
     */
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
     * 取得するチャンネル情報のチャンネル番号.
     */
    private List<String> mChNo = new ArrayList<>();
    /**
     * DBから取得するチャンネル番号を格納するリスト.
     */
    private List<String> mFromDB = new ArrayList<>();
    /**
     * DBから取得した複数チャンネルの情報を格納するリスト.
     */
    private List<List<Map<String, String>>> mResultSets = null;

    /**
     * コンストラクタ.
     *
     * @param mContext TvProgramListActivity
     */
    public ScaledDownProgramListDataProvider(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    @Override
    public void onDbOperationFinished(final boolean isSuccessful,
                                      final List<Map<String, String>> resultSet,
                                      final int operationId) {
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
                        mSchedule.setRValue(rValue);
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
                    for (List<Map<String, String>> channelInfos : mResultSets) {
                        ChannelInfoList channelsInfo;
                        if (channelInfos != null && channelInfos.size() > 0) {
                            channelsInfo = new ChannelInfoList();
                            for (int i = 0; i < channelInfos.size(); i++) { //CH毎番組データ取得して、整形する
                                HashMap<String, String> hashMap =  (HashMap<String, String>) channelInfos.get(i);
                                setScheduleInfo(hashMap, channelsInfo);
                            }
                            if (null != mApiDataProviderCallback) {
                                mApiDataProviderCallback.channelInfoCallback(channelsInfo);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(final int mOperationId) throws Exception {
        List<Map<String, String>> resultSet = null;
        switch (mOperationId) {
            case CHANNEL_UPDATE://サーバーから取得したチャンネルデータをDBに保存する
                ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(mContext);
                channelInsertDataManager.insertChannelInsertList(mChannelList);
                break;
            case SCHEDULE_UPDATE://サーバーから取得した番組データをDBに保存する
                TvScheduleInsertDataManager scheduleInsertDataManager = new TvScheduleInsertDataManager(mContext);
                scheduleInsertDataManager.insertTvScheduleInsertList(mChannelsInfoList, mProgramSelectDate);
                break;
            case CHANNEL_SELECT://DBからチャンネルデータを取得して、画面に返却する
                ProgramDataManager channelDataManager = new ProgramDataManager(mContext);
                resultSet = channelDataManager.selectChannelListProgramData(mChannelDisplayType);
                break;
            case SCHEDULE_SELECT://DBから番組データを取得して、画面に返却する
                ProgramDataManager scheduleDataManager = new ProgramDataManager(mContext);
                mResultSets = scheduleDataManager.selectTvScheduleListProgramData(mFromDB, mProgramSelectDate);
                mFromDB = new ArrayList<>();
                resultSet = new ArrayList<>();
                break;
            default:
                break;
        }
        return resultSet;
    }

    @Override
    public void onChannelJsonParsed(final List<ChannelList> channelLists) {
        ArrayList<ChannelInfo> channels = null;
        if (channelLists != null) {
            DateUtils dateUtils = new DateUtils(mContext);
            dateUtils.addLastProgramDate(DateUtils.TVSCHEDULE_LAST_UPDATE);
            mChannelList = channelLists.get(0);
            List<HashMap<String, String>> channelList = mChannelList.getChannelList();

            //チャンネル番号を保存する.
            for (HashMap<String, String> hashMap : channelList) {
                mChNo.add(hashMap.get(JsonConstants.META_RESPONSE_CHNO));
            }

            channels = new ArrayList<>();
            setChannelData(channels, channelList);
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, CHANNEL_UPDATE);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        }

        if (null != mApiDataProviderCallback) {
            mApiDataProviderCallback.channelListCallback(channels);
        }
    }

    @Override
    public void onTvScheduleJsonParsed(final List<TvScheduleList> tvScheduleList) {
        if (tvScheduleList != null) {
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
            List<List<Map<String, String>>> scheduleList;
            ProgramDataManager programDataManager = new ProgramDataManager(mContext);
            scheduleList = programDataManager.selectTvScheduleListProgramData(mChNo, mProgramSelectDate);
            if (mTvScheduleList == null) {
                mTvScheduleList = new TvScheduleList();
            }
            mTvScheduleList.setTvsList(scheduleList);
        }
    }

    @Override
    public void onTvClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
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
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
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
     * 番組表をチャンネルに入れる.
     *
     * @param hashMap 番組情報
     * @param channelsInfo チャンネル情報
     */
    private void setScheduleInfo(final HashMap<String, String> hashMap, final ChannelInfoList channelsInfo) {
        ScheduleInfo mSchedule = new ScheduleInfo();
        String startDate = hashMap.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE);
        String endDate = hashMap.get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE);
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

        if (!TextUtils.isEmpty(chNo)) { //CH毎番組データ取得して、整形する
            ArrayList<ChannelInfo> oldChannelList = channelsInfo.getChannels();
            boolean isExist = false;
            if (oldChannelList.size() > 0) { //番組ID存在するのをチェックする
                for (int j = 0; j < oldChannelList.size(); j++) {
                    ChannelInfo oldChannel = oldChannelList.get(j);
                    if (oldChannel.getChNo() == Integer.parseInt(chNo)) { //番組ID存在する場合
                        ArrayList<ScheduleInfo> oldSchedule = oldChannel.getSchedules();
                        oldSchedule.add(mSchedule);
                        isExist = true;
                        break;
                    }
                }
            }
            if (!isExist) { //番組ID存在しない場合
                ArrayList<ScheduleInfo> mScheduleList = new ArrayList<>();
                mScheduleList.add(mSchedule);
                ChannelInfo channel = new ChannelInfo();
                channel.setChNo(Integer.parseInt(chNo));
                channel.setTitle(title);
                channel.setSchedules(mScheduleList);
                channelsInfo.addChannel(channel);
            }
        }
    }

    /**
     * 番組表用に番組のメタデータを整形.
     *
     * @return 番組情報
     */
    private ChannelInfoList setProgramListContentData() {
        ChannelInfoList channelsInfo = null;
        List<HashMap<String, String>> mChannelProgramList = mTvScheduleList.geTvsList();
        if (mChannelProgramList != null) {
            channelsInfo = new ChannelInfoList();
            for (int i = 0; i < mChannelProgramList.size(); i++) {
                //CH毎番組データ取得して、整形する
                HashMap<String, String> hashMap = mChannelProgramList.get(i);
                setScheduleInfo(hashMap, channelsInfo);
            }
            mChannelsInfoList = channelsInfo;
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
        if (type == JsonConstants.DISPLAY_TYPE_INDEX_ALL) {
            mChannelDisplayType = "";
        } else if (type == JsonConstants.DISPLAY_TYPE_INDEX_DCH) {
            mChannelDisplayType = "1";
        } else {
            mChannelDisplayType = "0";
        }

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
            if (!isStop) {
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
        DateUtils dateUtils = new DateUtils(mContext);
        //dateListのサイズは1.
        mProgramSelectDate = dateList[0];
        //前回のデータ取得日時を取得
        String[] lastDate = dateUtils.getChLastDate(chList, mProgramSelectDate);
        //DBから取得するチャンネル情報とWebAPiから取得するチャンネル番号を分ける.
        List<Integer> fromWebAPI = new ArrayList<>();

        for (int i = 0; i < lastDate.length; i++) {
            if (dateUtils.isBeforeLimitChDate(lastDate[i])) {
                fromWebAPI.add(chList[i]);
            } else {
                mFromDB.add(String.valueOf(chList[i]));
            }
        }

        //データをDBから取得する
        if (mFromDB.size() > 0) {
            Handler handler = new Handler();
            //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, SCHEDULE_SELECT);
                t.start();
            } catch (Exception e) {
                DTVTLogger.debug(e);
            }
        }

        //データをWebAPIから取得する
        if (!isStop) {
            mTvScheduleWebClient = new TvScheduleWebClient(mContext);
            int[] chNos = new int[fromWebAPI.size()];
            for (int i = 0; i < fromWebAPI.size(); i++) {
                chNos[i] = fromWebAPI.get(i);
            }
            mTvScheduleWebClient.getTvScheduleApi(chNos, dateList, filter, this);
        } else {
            DTVTLogger.error("ScaledDownProgramListDataProvider is stopping connect");
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