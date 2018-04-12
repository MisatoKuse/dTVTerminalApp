/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
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
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
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
    private int mChannelServiceType = JsonConstants.CH_SERVICE_TYPE_INDEX_ALL;
    /**
     * 取得要求日付.
     */
    private String mProgramSelectDate = null;

    //共通スレッド使う
    /**
     * チャンネル更新(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int CHANNEL_UPDATE = 5;
    /**
     * 番組更新(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int SCHEDULE_UPDATE = 6;
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
     * DBから取得するチャンネル番号を格納するリスト.
     */
    private List<String> mFromDB = new ArrayList<>();
    /**
     * DBから取得した複数チャンネルの情報を格納するリスト.
     */
    private List<List<Map<String, String>>> mResultSets = null;

    /**
     * チャンネルリスト用エラー情報バッファ.
     */
    private ErrorState mChannelError = null;

    /**
     * 番組リスト用エラー情報バッファ.
     */
    private ErrorState mTvScheduleError = null;

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

    /**
     * コンストラクタ.
     *
     * @param mContext コンテスト
     * @param mApiDataProviderCallback リスナー
     */
    public ScaledDownProgramListDataProvider(final Context mContext, final ApiDataProviderCallback mApiDataProviderCallback) {
        super(mContext);
        this.mContext = mContext;
        this.mApiDataProviderCallback = mApiDataProviderCallback;
    }

    /**
     * チャンネル情報取得エラーのクラスを返すゲッター.
     *
     * @return チャンネル情報取得エラーのクラス
     */
    public ErrorState getChannelError() {
        return mChannelError;
    }

    /**
     * 番組情報取得エラーのクラスを返すゲッター.
     *
     * @return 番組情報取得エラーのクラス
     */
    public ErrorState getmTvScheduleError() {
        return mTvScheduleError;
    }


    @SuppressWarnings("OverlyLongMethod")
    @Override
    public void onDbOperationFinished(final boolean isSuccessful,
                                      final List<Map<String, String>> resultSet,
                                      final int operationId) {
        if (isSuccessful) {
            UserState userState = UserInfoUtils.getUserState(mContext);
            switch (operationId) {
                case CHANNEL_SELECT:
                    ArrayList<ChannelInfo> channels = new ArrayList<>();
                    for (int i = 0; i < resultSet.size(); i++) {
                        ArrayList<ScheduleInfo> mScheduleList;
                        Map<String, String> map = resultSet.get(i);
                        String chNo = map.get(JsonConstants.META_RESPONSE_CHNO);
                        String title = map.get(JsonConstants.META_RESPONSE_TITLE);
                        String thumb = map.get(JsonConstants.META_RESPONSE_THUMB_448);
                        String serviceId = map.get(JsonConstants.META_RESPONSE_SERVICE_ID);
                        String rValue = map.get(JsonConstants.META_RESPONSE_R_VALUE);
                        String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
                        String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
                        String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
                        String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);

                        ScheduleInfo mSchedule = new ScheduleInfo();
                        String startDate = map.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE);
                        String endDate = map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE);
                        mSchedule.setStartTime(startDate);
                        mSchedule.setEndTime(endDate);
                        mSchedule.setImageUrl(thumb);
                        mSchedule.setTitle(title);
                        mSchedule.setChNo(chNo);
                        mSchedule.setContentType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
                        mSchedule.setDtv(dtv);
                        mSchedule.setRValue(rValue);
                        mSchedule.setDispType(dispType);
                        mSchedule.setClipExec(ClipUtils.isCanClip(userState, dispType, searchOk, dtv, dtvType));
                        mSchedule.setClipRequestData(setClipData(map));
                        mSchedule.setContentsId(map.get(JsonConstants.META_RESPONSE_CRID));
                        mSchedule.setTvService(map.get(JsonConstants.META_RESPONSE_TV_SERVICE));
                        mSchedule.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
                        mSchedule.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
                        mSchedule.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
                        mSchedule.setCrId(map.get(JsonConstants.META_RESPONSE_CRID));

                        if (!TextUtils.isEmpty(chNo)) {
                            ChannelInfo channel = new ChannelInfo();
                            channel.setChNo(Integer.parseInt(chNo));
                            channel.setTitle(title);
                            channel.setThumbnail(thumb);
                            channel.setServiceId(serviceId);
                            mScheduleList = new ArrayList<>();
                            mScheduleList.add(mSchedule);
//                            channel.setSchedules(mScheduleList);
                            channels.add(channel);
                        }
                    }
                    if (null != mApiDataProviderCallback) {
                        mApiDataProviderCallback.channelListCallback(channels);
                    }
                    break;
                case SCHEDULE_SELECT:
                    ChannelInfoList channelsInfo = new ChannelInfoList();
                    for (List<Map<String, String>> channelInfos : mResultSets) {
                        if (channelInfos != null && channelInfos.size() > 0) {
                            ArrayList<ScheduleInfo> scheduleInfoList = new ArrayList<>();
                            for (int i = 0; i < channelInfos.size(); i++) { //番組データ取得して整形する
                                HashMap<String, String> hashMap =  (HashMap<String, String>) channelInfos.get(i);
                                ScheduleInfo mSchedule = convertScheduleInfo(hashMap, userState);
                                scheduleInfoList.add(mSchedule);
                            }
                            //setScheduleInfoのやり方を踏襲.
                            ChannelInfo channel = new ChannelInfo();
                            channel.setChNo(Integer.parseInt(scheduleInfoList.get(0).getChNo()));
//                            channel.setTitle(scheduleInfoList.get(0).getTitle());
                            channel.setSchedules(scheduleInfoList);
                            channelsInfo.addChannel(channel);
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
    public List<Map<String, String>> dbOperation(final int mOperationId) {
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
                resultSet = channelDataManager.selectChannelListProgramData(mChannelServiceType);
                break;
            case SCHEDULE_SELECT://DBから番組データを取得して、画面に返却する
                ProgramDataManager scheduleDataManager = new ProgramDataManager(mContext);
                mResultSets = scheduleDataManager.selectTvScheduleListProgramData(mFromDB, mProgramSelectDate);
                mFromDB = new ArrayList<>();
                resultSet = new ArrayList<>();
                // 番組データがある場合はダミーで1件の結果セットを返す
                if (mResultSets != null && mResultSets.size() > 0) {
                    resultSet.add(new HashMap<String, String>(){{put("","");}});
                }
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

            channels = new ArrayList<>();
            setChannelData(channels, channelList);
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, CHANNEL_UPDATE);
                t.start();
            } catch (IllegalThreadStateException e) {
                DTVTLogger.debug(e);
                channels = null;
            }
        } else {
            //データが取得できなかったので、エラーを取得する
            mChannelError = mChannelWebClient.getError();
        }

        //WebApi上は必ずひかり・dCH共に取得するが、呼び出し元によってはどちらか一方のみ用いるのでフィルタ.
        //せいぜい250チャンネル程度であれば高負荷にならないのでmain スレッドで回す.
        ArrayList<ChannelInfo> dstChannels = null;
        if (channels != null) {
            dstChannels = new ArrayList<>();
            if (mChannelServiceType == JsonConstants.CH_SERVICE_TYPE_INDEX_DCH) {
                // DCHのみ
                for (int i = 0; i < channels.size(); ++i) {
                    String service = channels.get(i).getService();
                    if (service != null && service.equals(ProgramDataManager.CH_SERVICE_DCH)) {
                        dstChannels.add(channels.get(i));
                    }
                }
            } else if (mChannelServiceType == JsonConstants.CH_SERVICE_TYPE_INDEX_HIKARI) {
                // ひかりのみ
                for (int i = 0; i < channels.size(); ++i) {
                    String service = channels.get(i).getService();
                    if (service != null && service.equals(ProgramDataManager.CH_SERVICE_HIKARI)) {
                        dstChannels.add(channels.get(i));
                    }
                }
            } else {
                // どちらも
                dstChannels = channels;
            }
        }

        if (null != mApiDataProviderCallback) {
            mApiDataProviderCallback.channelListCallback(dstChannels);
        }
    }

    @Override
    public void onTvScheduleJsonParsed(final List<TvScheduleList> tvScheduleList) {
        if (tvScheduleList != null) {
            //チャンネルデータ
            mTvScheduleList = tvScheduleList.get(0);
//            if (mRequiredClipKeyList) {
//                // クリップキーリストを取得
//                tvClipKeyListResponse = false;
//                vodClipKeyListResponse = false;
//                getClipKeyList();
//            } else {
//                if (null != mApiDataProviderCallback) {
//                    mApiDataProviderCallback.channelInfoCallback(setProgramListContentData());
//                }
//            }
            mApiDataProviderCallback.channelInfoCallback(setProgramListContentData());
        } else {
            //データが取得できなかったので、エラーを取得する
            mTvScheduleError = mTvScheduleWebClient.getError();

            //Data取得時に、DBから取得するチャンネル番号とWebAPIから取得するチャンネル番号を分けて
            //データを取っているため、ここで改めてDBからデータを取得は行わない.
            if (null != mApiDataProviderCallback) {
                mApiDataProviderCallback.channelInfoCallback(null);
            }
        }
    }

    @Override
    public void onTvClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        super.onTvClipKeyListJsonParsed(clipKeyListResponse);
        if (vodClipKeyListResponse) {
            if (null != mApiDataProviderCallback) {
//                mApiDataProviderCallback.channelInfoCallback(setProgramListContentData());
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
//                mApiDataProviderCallback.channelInfoCallback(setProgramListContentData());
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
    private void setScheduleInfo(final Map<String, String> hashMap, final ChannelInfoList channelsInfo, final UserState userState) {
        ScheduleInfo mSchedule = convertScheduleInfo(hashMap, userState);

        if (!TextUtils.isEmpty(mSchedule.getChNo())) { //CH毎番組データ取得して、整形する
            List<ChannelInfo> oldChannelList = channelsInfo.getChannels();
            boolean isExist = false;
            if (oldChannelList.size() > 0) { //番組ID存在するのをチェックする
                for (int j = 0; j < oldChannelList.size(); j++) {
                    ChannelInfo oldChannel = oldChannelList.get(j);
                    if (oldChannel.getChNo() == Integer.parseInt(mSchedule.getChNo())) { //番組ID存在する場合
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
                channel.setChNo(Integer.parseInt(mSchedule.getChNo()));
//                channel.setTitle(mSchedule.getTitle());
                channel.setSchedules(mScheduleList);
                channelsInfo.addChannel(channel);
            }
        }
    }

    /**
     * hashMap情報からScheduleInfo情報を組み立てる.
     *
     * @param map マップ
     * @param userState ユーザ情報
     * @return ScheduleInfo情報
     */
    private ScheduleInfo convertScheduleInfo(final Map<String, String> map, final UserState userState) {
        ScheduleInfo mSchedule = new ScheduleInfo();
        String startDate = map.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE);
        String endDate = map.get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE);
        String thumb = map.get(JsonConstants.META_RESPONSE_THUMB_448);
        String thumbDetail = map.get(JsonConstants.META_RESPONSE_THUMB_640);
        String title = map.get(JsonConstants.META_RESPONSE_TITLE);
        String detail = map.get(JsonConstants.META_RESPONSE_EPITITLE);
        String chNo = map.get(JsonConstants.META_RESPONSE_CHNO);
        String rValue = map.get(JsonConstants.META_RESPONSE_R_VALUE);
        String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
        String contentType = map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
        String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
        String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
        String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);
        mSchedule.setStartTime(startDate);
        mSchedule.setEndTime(endDate);
        mSchedule.setImageUrl(thumb);
        mSchedule.setImageDetailUrl(thumbDetail);
        mSchedule.setTitle(title);
        mSchedule.setDetail(detail);
        mSchedule.setChNo(chNo);
        mSchedule.setRValue(rValue);
        mSchedule.setContentType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
        mSchedule.setDtv(dtv);
        mSchedule.setDispType(dispType);
        mSchedule.setContentsId(map.get(JsonConstants.META_RESPONSE_CRID));
        mSchedule.setTvService(map.get(JsonConstants.META_RESPONSE_TV_SERVICE));
        mSchedule.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
        mSchedule.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
        mSchedule.setCrId(map.get(JsonConstants.META_RESPONSE_CRID));
//        mSchedule.setClipExec(ClipUtils.isCanClip(userState, dispType, searchOk, dtv, dtvType));
//        mSchedule.setClipRequestData(setClipData(map));
//        mSchedule.setClipStatus(getClipStatus(dispType, contentType, dtv,
//                map.get(JsonConstants.META_RESPONSE_CRID),
//                map.get(JsonConstants.META_RESPONSE_SERVICE_ID),
//                map.get(JsonConstants.META_RESPONSE_EVENT_ID),
//                map.get(JsonConstants.META_RESPONSE_TITLE_ID),
//                map.get(JsonConstants.META_RESPONSE_TV_SERVICE)));
//        mSchedule.setContentsId(map.get(JsonConstants.META_RESPONSE_CRID));
        return mSchedule;
    }

    /**
     * 番組表用に番組のメタデータを整形.
     *
     * @return 番組情報
     */
    private ChannelInfoList setProgramListContentData() {
        ChannelInfoList channelsInfo = null;
        List<Map<String, String>> mChannelProgramList = mTvScheduleList.geTvsList();
        if (mChannelProgramList != null) {
            channelsInfo = new ChannelInfoList();
            UserState userState = UserInfoUtils.getUserState(mContext);
            for (int i = 0; i < mChannelProgramList.size(); i++) {
                //CH毎番組データ取得して、整形する
                Map<String, String> hashMap = mChannelProgramList.get(i);
                setScheduleInfo(hashMap, channelsInfo, userState);
            }
//            mChannelsInfoList = channelsInfo;
//            Handler handler = new Handler();
//            //番組情報更新
//            try {
//                DbThread t = new DbThread(handler, this, SCHEDULE_UPDATE);
//                t.start();
//            } catch (Exception e) {
//                DTVTLogger.debug(e);
//            }
        }
        return channelsInfo;
    }

    /**
     * クリップリクエストに必要なデータを作成する(番組表用).
     *
     * @param map 番組表データ
     * @return Clipリクエストに必要なデータ
     */
    private static ClipRequestData setClipData(final Map<String, String> map) {
        String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
        String contentsType = map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
        ClipRequestData requestData = new ClipRequestData();
        requestData.setCrid(map.get(JsonConstants.META_RESPONSE_CRID));
        requestData.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
        requestData.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
        requestData.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
        requestData.setTitle(map.get(JsonConstants.META_RESPONSE_TITLE));
        requestData.setRValue(map.get(JsonConstants.META_RESPONSE_R_VALUE));
        requestData.setLinearStartDate(String.valueOf(map.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE)));
        requestData.setLinearEndDate(String.valueOf(map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)));
        requestData.setSearchOk(map.get(JsonConstants.META_RESPONSE_SEARCH_OK));
        requestData.setIsNotify(dispType, contentsType,
                String.valueOf(map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)),
                map.get(JsonConstants.META_RESPONSE_TV_SERVICE), map.get(JsonConstants.META_RESPONSE_DTV));
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
            String thumbnail = hashMap.get(JsonConstants.META_RESPONSE_THUMB_448);
            String serviceId = hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID);
            String chType = hashMap.get(JsonConstants.META_RESPONSE_CH_TYPE);
            String puId = hashMap.get(JsonConstants.META_RESPONSE_PUID);
            String subPuId = hashMap.get(JsonConstants.META_RESPONSE_SUB_PUID);
            String service = hashMap.get(JsonConstants.META_RESPONSE_SERVICE);
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
                channel.setService(service);
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
        mChannelServiceType = type;

        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.CHANNEL_LAST_UPDATE);
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate)) {
            //データをDBから取得する
            Handler handler = new Handler(mContext.getMainLooper());
            //チャンネル情報更新
            try {
                DbThread t = new DbThread(handler, this, CHANNEL_SELECT);
                t.start();
            } catch (IllegalThreadStateException e) {
                DTVTLogger.debug(e);
                //TODO:エラー返却した上でUI上に通知が必要
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
     */
    public void getProgram(final int[] chList, final String[] dateList) {
        getProgram(chList, dateList, PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE);
    }

    /**
     * 通信/DBで、チャンネル毎番組取得.
     *
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     * @param filter   文字列、Filter
     */
    private void getProgram(final int[] chList, final String[] dateList, final String filter) {
//        DateUtils dateUtils = new DateUtils(mContext);
//        //dateListのサイズは1.
//        mProgramSelectDate = dateList[0];
//        //前回のデータ取得日時を取得
//        String[] lastDate = dateUtils.getChLastDate(chList, mProgramSelectDate);
//        //DBから取得するチャンネル情報とWebAPiから取得するチャンネル番号を分ける.
//        List<Integer> fromWebAPI = new ArrayList<>();
//
//        for (int i = 0; i < lastDate.length; i++) {
//            if (dateUtils.isBeforeLimitChDate(lastDate[i])) {
//                fromWebAPI.add(chList[i]);
//            } else {
//                mFromDB.add(String.valueOf(chList[i]));
//            }
//        }
//
//        //データをDBから取得する
//        if (mFromDB.size() > 0) {
//            Handler handler = new Handler();
//            //チャンネル情報更新
//            try {
//                DbThread t = new DbThread(handler, this, SCHEDULE_SELECT);
//                t.start();
//            } catch (Exception e) {
//                DTVTLogger.debug(e);
//            }
//        }
//
//        //データをWebAPIから取得する
//        if (!isStop) {
//            mTvScheduleWebClient = new TvScheduleWebClient(mContext);
//            int[] chNos = new int[fromWebAPI.size()];
//            for (int i = 0; i < fromWebAPI.size(); i++) {
//                chNos[i] = fromWebAPI.get(i);
//            }
//            mTvScheduleWebClient.getTvScheduleApi(chNos, dateList, filter, this);
//        } else {
//            DTVTLogger.error("ScaledDownProgramListDataProvider is stopping connect");
//        }
        mTvScheduleWebClient = new TvScheduleWebClient(mContext);
//        int[] chNos = new int[fromWebAPI.size()];
//        for (int i = 0; i < fromWebAPI.size(); i++) {
//            chNos[i] = fromWebAPI.get(i);
//        }
        mTvScheduleWebClient.getTvScheduleApi(chList, dateList, filter, this);
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