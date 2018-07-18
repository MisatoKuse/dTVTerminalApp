/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ClipKeyListDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.ProgramDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.service.TvProgramIntentService;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ScheduleInfo;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.ServiceUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 縮小番組表用データプロバイダークラス
 * 機能：　縮小番組表画面で使うデータを提供するクラスである.
 */
public class ScaledDownProgramListDataProvider extends ClipKeyListDataProvider implements
        ChannelWebClient.ChannelJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback {
    /** データ取得結果コールバック. */
    private ApiDataProviderCallback mApiDataProviderCallback = null;
    /** コンテキスト. */
    private Context mContext = null;
    /** チャンネルデータ. */
    private ChannelList mChannelList = null;
    /** 番組データ. */
    private TvScheduleList mTvScheduleList = null;
    /** チャンネル番号で仕分けした番組データ. */
    private ChannelInfoList mChannelsInfoList = null;
    /** チャンネルタイプ(dチャンネル、ひかりTV多ch、全て). */
    private int mChannelServiceType = JsonConstants.CH_SERVICE_TYPE_INDEX_ALL;
    /** 取得要求日付. */
    private String mProgramSelectDate = null;

    //共通スレッド使う
    /** チャンネル更新(親クラスのDbThreadで"0","1","2"を使用しているため使用しない). */
    private static final int CHANNEL_UPDATE = 5;
    /** 番組更新(親クラスのDbThreadで"0","1","2"を使用しているため使用しない). */
    private static final int SCHEDULE_UPDATE = 6;
    /** チャンネル検索. */
    private static final int CHANNEL_SELECT = 3;
    /** 番組検索. */
    private static final int SCHEDULE_SELECT = 4;
    /** 日付フォーマット. */
    private static final String DATE_FORMAT = "yyyyMMdd";

    //CH毎番組取得のフィルター
    /** release. */
    private static final String PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE = "release";
    /** チャンネルリスト送受信用キー. */
    public static final String SEND_CHANNEL_LIST = "com.nttdocomo.android.idmanager.action.SEND_CHANNEL_LIST";
    /** 番組表リスト送受信用キー. */
    public static final String SEND_SCHEDULE_LIST = "com.nttdocomo.android.idmanager.action.SEND_SCHEDULE_LIST";
    /** 通信エラー時ウェイト処理. */
    private static final long SLEEP_TIME = 100L;

    /** tvコンテンツのクリップキーリスト取得済み判定. */
    private boolean mTvClipKeyListResponse = false;
    /** vodコンテンツのクリップキーリスト取得済み判定. */
    private boolean mVodClipKeyListResponse = false;
    /** 通信禁止判定フラグ. */
    private boolean mIsStop = false;
    /** チャンネルリスト取得WebClient. */
    private ChannelWebClient mChannelWebClient = null;
    /** 番組リスト取得WebClient. */
    private TvScheduleWebClient mTvScheduleWebClient = null;
    /** DBから取得するチャンネル番号を格納するリスト. */
    private List<String> mFromDB = new ArrayList<>();
    /** DBから取得した複数チャンネルの情報を格納するリスト. */
    private List<List<Map<String, String>>> mResultSets = null;
    /** TvScheduleWebClientのキューリスト. */
    private LinkedList<TvScheduleWebClient> mTvScheduleWebClientLinkedList =null;

    /** チャンネルリスト用エラー情報バッファ. */
    private ErrorState mChannelError = null;

    /** 番組リスト用エラー情報バッファ. */
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
            switch (operationId) {
                case CHANNEL_SELECT:
                    ArrayList<ChannelInfo> channels = new ArrayList<>();
                    for (int i = 0; i < resultSet.size(); i++) {
                        ArrayList<ScheduleInfo> mScheduleList;
                        Map<String, String> map = resultSet.get(i);
                        String adult = map.get(JsonConstants.META_RESPONSE_ADULT);
                        String chNo = map.get(JsonConstants.META_RESPONSE_CHNO);
                        String title = map.get(JsonConstants.META_RESPONSE_TITLE);
                        String thumb = map.get(JsonConstants.META_RESPONSE_THUMB_448);
                        String serviceId = map.get(JsonConstants.META_RESPONSE_SERVICE_ID);
                        String rValue = map.get(JsonConstants.META_RESPONSE_R_VALUE);
                        String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
                        String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
                        String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
                        String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);
                        String startDate = map.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE);
                        String endDate = map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE);

                        ScheduleInfo mSchedule = new ScheduleInfo();
                        mSchedule.setStartTime(startDate);
                        mSchedule.setEndTime(endDate);
                        mSchedule.setImageUrl(thumb);
                        mSchedule.setTitle(title);
                        mSchedule.setChNo(chNo);
                        mSchedule.setContentType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
                        mSchedule.setDtv(dtv);
                        mSchedule.setRValue(rValue);
                        mSchedule.setDispType(dispType);
                        mSchedule.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
                        mSchedule.setClipRequestData(setClipData(map));
                        mSchedule.setContentsId(map.get(JsonConstants.META_RESPONSE_CRID));
                        mSchedule.setTvService(map.get(JsonConstants.META_RESPONSE_TV_SERVICE));
                        mSchedule.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
                        mSchedule.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
                        mSchedule.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
                        mSchedule.setCrId(map.get(JsonConstants.META_RESPONSE_CRID));

                        if (!TextUtils.isEmpty(chNo)) {
                            ChannelInfo channel = new ChannelInfo();
                            channel.setAdult(adult);
                            channel.setChannelNo(Integer.parseInt(chNo));
                            channel.setTitle(title);
                            channel.setThumbnail(thumb);
                            channel.setServiceId(serviceId);
                            channel.setPurchaseId(map.get(JsonConstants.META_RESPONSE_PUID));
                            channel.setSubPurchaseId(map.get(JsonConstants.META_RESPONSE_SUB_PUID));
                            channel.setChannelPackPurchaseId(map.get(StringUtils.getConnectStrings(
                                    JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_PUID)));
                            channel.setChannelPackSubPurchaseId(map.get(StringUtils.getConnectStrings(
                                    JsonConstants.META_RESPONSE_CHPACK, JsonConstants.UNDER_LINE, JsonConstants.META_RESPONSE_SUB_PUID)));
                            channel.setChannelType(map.get(JsonConstants.META_RESPONSE_CH_TYPE));
                            mScheduleList = new ArrayList<>();
                            mScheduleList.add(mSchedule);
//                            channel.setSchedules(mScheduleList);
                            channels.add(channel);
                        }
                    }
                    sendChannelInfoArray(channels);
                    break;
                case SCHEDULE_SELECT:
                    //非同期なので、タブ切替時にこの処理に入ってしまわないようにNullチェックを追加
                    if (mResultSets != null) {
                        ChannelInfoList channelsInfo = new ChannelInfoList();
                        ClipKeyListDataManager keyListDataManager = new ClipKeyListDataManager(mContext);
                        List<Map<String, String>> clipKeyList = keyListDataManager.selectClipAllList();
                        for (List<Map<String, String>> channelInfos : mResultSets) {
                            if (channelInfos != null && channelInfos.size() > 0) {
                                ArrayList<ScheduleInfo> scheduleInfoList = new ArrayList<>();
                                for (int i = 0; i < channelInfos.size(); i++) { //番組データ取得して整形する
                                    HashMap<String, String> hashMap = (HashMap<String, String>) channelInfos.get(i);
                                    ScheduleInfo mSchedule = DataConverter.convertScheduleInfo(hashMap, clipKeyList);
                                    scheduleInfoList.add(mSchedule);
                                }
                                //setScheduleInfoのやり方を踏襲.
                                ChannelInfo channel = new ChannelInfo();
                                channel.setChannelNo(Integer.parseInt(scheduleInfoList.get(0).getChNo()));
                                channel.setTitle(scheduleInfoList.get(0).getTitle());
                                channel.setSchedules(scheduleInfoList);
                                channelsInfo.addChannel(channel);
                            }
                        }
                        sendChannelInfoList(channelsInfo, new int[0]);
                    } else {
                        sendChannelInfoList(null, new int[0]);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<Map<String, String>> dbOperation(final DataBaseThread dataBaseThread, final int operationId) {
        super.dbOperation(dataBaseThread, operationId);
        List<Map<String, String>> resultSet = null;
        switch (operationId) {
            case CHANNEL_UPDATE://サーバーから取得したチャンネルデータをDBに保存する
                ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(mContext);
                channelInsertDataManager.insertChannelInsertList(dataBaseThread.getChannelList());
                break;
            case SCHEDULE_UPDATE://サーバーから取得した番組データをDBに保存する
                TvScheduleInsertDataManager scheduleInsertDataManager = new TvScheduleInsertDataManager(mContext);
                scheduleInsertDataManager.insertTvScheduleInsertList(dataBaseThread.getChannelsInfoList(), mProgramSelectDate);
                //番組表の保存と番組表描画を並行して実行するとフリーズするため、DBへのInsertが終了してから描画を開始する
                sendChannelInfoList(mChannelsInfoList, new int[0]);
                break;
            case CHANNEL_SELECT://DBからチャンネルデータを取得して、画面に返却する
                ProgramDataManager channelDataManager = new ProgramDataManager(mContext);
                resultSet = channelDataManager.selectChannelListProgramData(mChannelServiceType);
                break;
            case SCHEDULE_SELECT://DBから番組データを取得して、画面に返却する
                ProgramDataManager scheduleDataManager = new ProgramDataManager(mContext);
                dataBaseThread.getFromDB();
                mResultSets = scheduleDataManager.selectTvScheduleListProgramData(dataBaseThread.getFromDB(), mProgramSelectDate);
                mFromDB = new ArrayList<>();
                resultSet = new ArrayList<>();
                // 番組データがある場合はダミーで1件の結果セットを返す
                if (mResultSets != null && mResultSets.size() > 0) {
                    resultSet.add(new HashMap<String, String>() {{ put("", ""); }});
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
            mChannelList = channelLists.get(0);
            List<HashMap<String, String>> channelList = mChannelList.getChannelList();

            channels = new ArrayList<>();
            setChannelData(channels, channelList);
            Handler handler = new Handler(); //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, CHANNEL_UPDATE);
                dataBaseThread.setChannelList(mChannelList);
                dataBaseThread.start();
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

        sendChannelInfoArray(dstChannels);
    }

    @Override
    public void onTvScheduleJsonParsed(final List<TvScheduleList> tvScheduleList, final int[] chNo) {
        //WebClientキューが残っていたら実行する
        pollTvScheduleWebClient();
        if (tvScheduleList != null) {
            int[] chNoList = chNo;
            DTVTLogger.debug("onTvScheduleJsonParsed chNo = " + chNoList);
            //チャンネルデータ
            mTvScheduleList = tvScheduleList.get(0);

            if (mTvScheduleList.geTvsList().size() < 1) {
                mTvScheduleList.setTvsList(DataConverter.getDummyContentMap(mContext,chNo, false));
            }
            if (mRequiredClipKeyList) {
                // クリップキーリストを取得
                mTvClipKeyListResponse = false;
                mVodClipKeyListResponse = false;
                getClipKeyList();
            } else {
                if (null != mApiDataProviderCallback) {
                    setProgramListContentData();
                }
            }
        } else {
            //データが取得できなかったので、エラーを取得する
            if (mTvScheduleWebClient != null) {
                mTvScheduleError = mTvScheduleWebClient.getError();
            }

            //Data取得時に、DBから取得するチャンネル番号とWebAPIから取得するチャンネル番号を分けて
            //データを取っているため、ここで改めてDBからデータを取得は行わない.
            sendChannelInfoList(null, chNo);
        }
    }

    @Override
    public void onTvClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse
        ,final ErrorState errorState) {
        mTvScheduleError = errorState;
        super.onTvClipKeyListJsonParsed(clipKeyListResponse,errorState);
        if (mVodClipKeyListResponse) {
            sendClipKeyResult();
            sendChannelInfoList(setProgramListContentData(), new int[0]);
        } else {
            mTvClipKeyListResponse = true;
        }
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse
            ,final ErrorState errorState) {
        super.onVodClipKeyListJsonParsed(clipKeyListResponse, errorState);
        if (mTvClipKeyListResponse) {
            sendClipKeyResult();
            sendChannelInfoList(setProgramListContentData(), new int[0]);
        } else {
            mVodClipKeyListResponse = true;
        }
    }

    /**
     * 番組表をチャンネルに入れる.
     *
     * @param hashMap 番組情報
     * @param channelsInfo チャンネル情報
     * @param clipKeyList クリップキーリスト
     */
    private void setScheduleInfo(final Map<String, String> hashMap, final ChannelInfoList channelsInfo, final List<Map<String, String>> clipKeyList) {
        ScheduleInfo mSchedule = DataConverter.convertScheduleInfo(hashMap, clipKeyList);

        if (!TextUtils.isEmpty(mSchedule.getChNo())) { //CH毎番組データ取得して、整形する
            List<ChannelInfo> oldChannelList = channelsInfo.getChannels();
            boolean isExist = false;
            if (oldChannelList.size() > 0) { //番組ID存在するのをチェックする
                for (int j = 0; j < oldChannelList.size(); j++) {
                    ChannelInfo oldChannel = oldChannelList.get(j);
                    if (oldChannel.getChannelNo() == Integer.parseInt(mSchedule.getChNo())) { //番組ID存在する場合
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
                channel.setChannelNo(Integer.parseInt(mSchedule.getChNo()));
//                channel.setTitle(mSchedule.getTitle());
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

        if (mTvScheduleList == null) {
            //解放処理の強化によりヌルの場合が発生したので、ヌルならば帰る
            return null;
        }

        List<Map<String, String>> mChannelProgramList = mTvScheduleList.geTvsList();
        if (mChannelProgramList != null) {
            channelsInfo = new ChannelInfoList();
            ClipKeyListDataManager keyListDataManager = new ClipKeyListDataManager(mContext);
            List<Map<String, String>> clipKeyList = keyListDataManager.selectClipAllList();
            for (int i = 0; i < mChannelProgramList.size(); i++) {
                //CH毎番組データ取得して、整形する
                Map<String, String> hashMap = mChannelProgramList.get(i);
                setScheduleInfo(hashMap, channelsInfo, clipKeyList);
            }
            mChannelsInfoList = channelsInfo;
            Handler handler = new Handler();
            //番組情報更新
            try {
                DataBaseThread t = new DataBaseThread(handler, this, SCHEDULE_UPDATE);
                t.setChannelsInfoList(mChannelsInfoList);
                t.start();
            } catch (IllegalThreadStateException e) {
                DTVTLogger.debug(e);
            }
        }
        return channelsInfo;
    }

    /**
     * クリップリクエストに必要なデータを作成する(番組表用).
     *
     * @param map 番組表データ
     * @return Clipリクエストに必要なデータ
     */
    public static ClipRequestData setClipData(final Map<String, String> map) {
        String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
        String contentsType = map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
        ClipRequestData requestData = new ClipRequestData();
        requestData.setCrid(map.get(JsonConstants.META_RESPONSE_CRID));
        requestData.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
        requestData.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
        requestData.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
        requestData.setTitle(map.get(JsonConstants.META_RESPONSE_TITLE));
        requestData.setRValue(map.get(JsonConstants.META_RESPONSE_R_VALUE));
        requestData.setLinearStartDate(String.valueOf(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE))));
        requestData.setLinearEndDate(String.valueOf(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE))));
        requestData.setSearchOk(map.get(JsonConstants.META_RESPONSE_SEARCH_OK));
        requestData.setIsNotify(dispType, contentsType,
                DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_VOD_START_DATE)),
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
            String adult =  hashMap.get(JsonConstants.META_RESPONSE_ADULT);
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
                channel.setAdult(adult);
                channel.setTitle(title);
                channel.setChannelNo(Integer.parseInt(chNo));
                channel.setThumbnail(thumbnail);
                channel.setServiceId(serviceId);
                channel.setChannelType(chType);
                channel.setPurchaseId(puId);
                channel.setSubPurchaseId(subPuId);
                channel.setChannelPackPurchaseId(chPackPuId);
                channel.setChannelPackSubPurchaseId(chPackSubPuId);
                channel.setService(service);
                channels.add(channel);
            }
        }
    }

    /**
     * 後始末を行い、ガベージコレクションされやすくする.
     */
    public void clearData() {
        if (mTvScheduleList != null) {
            mTvScheduleList.clearData();
            mTvScheduleList = null;
        }

        if (mChannelsInfoList != null) {
            mChannelsInfoList.clearData();
            mChannelsInfoList = null;
        }
        if (mChannelWebClient != null) {
            mChannelWebClient = null;
        }
        if (mTvScheduleWebClient != null) {
            mTvScheduleWebClient = null;
        }
        if (mFromDB != null) {
            mFromDB.clear();
            mFromDB = null;
        }
        if (mResultSets != null) {
            mResultSets.clear();
            mResultSets = null;
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
         * @param chNo 画面に渡すチャンネル番号(番組表でのみ使用)
         */
        void channelInfoCallback(ChannelInfoList channelsInfo, int[] chNo);

        /**
         * チャンネルリストを戻す.
         *
         * @param channels 　画面に渡すチャンネル情報
         */
        void channelListCallback(ArrayList<ChannelInfo> channels);

        /**
         * クリップキー取得終了callback.
         */
        void clipKeyResult();
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
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate) || !NetWorkUtils.isOnline(mContext)) {
            //データをDBから取得する
            Handler handler = new Handler(mContext.getMainLooper());
            //チャンネル情報更新
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, CHANNEL_SELECT);
                dataBaseThread.start();
            } catch (IllegalThreadStateException e) {
                DTVTLogger.debug(e);
                //TODO　:エラー返却した上でUI上に通知が必要
            }
        } else {
            if (!mIsStop) {
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
     * @param isTvProgramList 番組表からの取得フラグ
     */
    public void getProgram(final int[] chList, final String[] dateList, final boolean isTvProgramList) {
        getProgram(chList, dateList, PROGRAM_LIST_CHANNEL_PROGRAM_FILTER_RELEASE, isTvProgramList);
    }

    /**
     * 通信/DBで、チャンネル毎番組取得.
     *
     * @param chList   配列, 中身は整数値
     * @param dateList 配列, 中身は整数値 YYYYMMDD
     * @param filter   文字列、Filter
     * @param isTvProgramList 番組表からの取得フラグ
     */
    private void getProgram(final int[] chList, final String[] dateList, final String filter, final boolean isTvProgramList) {
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
                DataBaseThread t = new DataBaseThread(handler, this, SCHEDULE_SELECT);
                t.setFromDB(mFromDB);
                t.start();
            } catch (IllegalThreadStateException e) {
                DTVTLogger.debug(e);
            }
        }

        //データをWebAPIから取得する
        if (!mIsStop) {
            if (NetWorkUtils.isOnline(mContext)) {
                int[] chNos = new int[fromWebAPI.size()];
                for (int i = 0; i < fromWebAPI.size(); i++) {
                    chNos[i] = fromWebAPI.get(i);
                }
                if (isTvProgramList) {
                    mTvScheduleWebClientLinkedList = new LinkedList<>();
                    for (int chNo : chNos) {
                        mTvScheduleWebClient = new TvScheduleWebClient(mContext);
                        mTvScheduleWebClient.setChannelNoList(new int[]{chNo}, dateList, filter);
                        DTVTLogger.debug("TvScheduleWebClient offer execute date " + chNo);
                        mTvScheduleWebClientLinkedList.offer(mTvScheduleWebClient);
                    }
                    //キュー生成後に実行する
                    pollTvScheduleWebClient();
                } else {
                    mTvScheduleWebClient = new TvScheduleWebClient(mContext);
                    mTvScheduleWebClient.getTvScheduleApi(chNos, dateList, filter, this);
                }
            } else if (isTvProgramList) {
                //番組表レスポンスの時はチャンネルリスト数分のレスポンスを返却する
                for (int aChList : chList) {
                    mApiDataProviderCallback.channelInfoCallback(null, new int[]{aChList});
                }
            } else {
                mApiDataProviderCallback.channelInfoCallback(null, chList);
            }
        } else {
            DTVTLogger.error("ScaledDownProgramListDataProvider is stopping connect");
        }
    }

    /**
     * キューに入れたWebClientを実行する.
     */
    private void pollTvScheduleWebClient() {
        if (mTvScheduleWebClientLinkedList != null && mTvScheduleWebClientLinkedList.size() > 0) {
            mTvScheduleWebClientLinkedList.poll().getTvScheduleApi(this);
        }
    }

    /**
     * TvProgramIntentServiceを開始する.
     */
    public void startTvProgramIntentService() {
        //TvProgramIntentServiceが実行していない場合のみ開始する
        if (!ServiceUtils.isRunningService(mContext, TvProgramIntentService.class.getName())) {
            TvProgramIntentService.startTvProgramService(mContext);
            mContext.registerReceiver(receiver, new IntentFilter(SEND_CHANNEL_LIST));
            mContext.registerReceiver(receiver, new IntentFilter(SEND_SCHEDULE_LIST));
        }
    }

    /**
     * TvProgramIntentServiceを終了する.
     */
    public void stopTvProgramIntentService() {
        //TvProgramIntentServiceが実行している場合のみ終了する
        if (ServiceUtils.isRunningService(mContext, TvProgramIntentService.class.getName())) {
            Intent intent = new Intent(mContext, TvProgramIntentService.class);
            mContext.unregisterReceiver(receiver);
            mContext.stopService(intent);
        }
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsStop = true;
        if (mChannelWebClient != null) {
            mChannelWebClient.stopConnection();
        }
        if (mTvScheduleWebClient != null) {
            mTvScheduleWebClient.stopConnection();
        }
        if (mTvScheduleWebClientLinkedList != null) {
            for (int i = 0; i < mTvScheduleWebClientLinkedList.size(); i++) {
                mTvScheduleWebClientLinkedList.poll().stopConnection();
            }
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsStop = false;
        if (mChannelWebClient != null) {
            mChannelWebClient.enableConnection();
        }
        if (mTvScheduleWebClient != null) {
            mTvScheduleWebClient.enableConnection();
        }
        if (mTvScheduleWebClientLinkedList != null) {
            for (int i = 0; i < mTvScheduleWebClientLinkedList.size(); i++) {
                mTvScheduleWebClientLinkedList.poll().enableConnection();
            }
        }
    }

    /**
     * TvProgramIntentServiceからのBroadcastレシーバー.
     */
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            DTVTLogger.start();
            String key = intent.getAction();
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread thread;
            switch (key) {
                case SEND_CHANNEL_LIST:
                    DTVTLogger.debug("ScaledDownProgramListDataProvider BroadcastReceiver CHANNEL_UPDATE");
                    mChannelList = intent.getParcelableExtra(SEND_CHANNEL_LIST);
                    thread = new DataBaseThread(handler, ScaledDownProgramListDataProvider.this, CHANNEL_UPDATE);
                    thread.setChannelList(mChannelList);
                    thread.start();
                    break;
                case SEND_SCHEDULE_LIST:
                    DTVTLogger.debug("ScaledDownProgramListDataProvider BroadcastReceiver SCHEDULE_UPDATE");
                    mChannelsInfoList = intent.getParcelableExtra(SEND_SCHEDULE_LIST);
                    thread = new DataBaseThread(handler, ScaledDownProgramListDataProvider.this, SCHEDULE_UPDATE);
                    thread.setChannelsInfoList(mChannelsInfoList);
                    thread.start();
                    break;
                default:
                    break;
            }
            mContext.unregisterReceiver(receiver);
            DTVTLogger.end();
        }
    };

    /**
     * 複数チャンネルクラス送信.
     *
     * @param channelsInfo 複数チャンネルクラス
     * @param chNo チャンネル番号
     */
    private void sendChannelInfoList(final ChannelInfoList channelsInfo, final int[] chNo) {
        DTVTLogger.start();
        if (null != mApiDataProviderCallback) {
            DTVTLogger.debug("null != mApiDataProviderCallback");
            mApiDataProviderCallback.channelInfoCallback(channelsInfo, chNo);
        }
        DTVTLogger.end();
    }

    /**
     * チャンネルクラス送信.
     * @param channels チャンネルクラス
     */
    private void sendChannelInfoArray(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        if (null != mApiDataProviderCallback) {
            DTVTLogger.debug("null != mApiDataProviderCallback");

            if (channels == null) {
                //channelsがヌルの場合は通信エラーになっている。実際の通信の前に通信エラーとなった場合、アクティビティ側の初期処理が終了するよりも早く
                //結果が返る場合があり、動作に不整合が発生する場合があった。そこで、ウェイト時間を設ける事で対処する。アクティビティ側での対処では、
                //例外処理が膨れ上がった上に信頼性も低かったので、これが最もシンプルな解決方法となる。
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    //割り込みがあっても特に何も行わない
                }
            }

            mApiDataProviderCallback.channelListCallback(channels);
        }
        DTVTLogger.end();
    }

    /**
     * クリップキー取得終了イベント送信.
     */
    private void sendClipKeyResult() {
        DTVTLogger.start();
        if (null != mApiDataProviderCallback) {
            DTVTLogger.debug("get clip key result");
            mApiDataProviderCallback.clipKeyResult();
        }
        DTVTLogger.end();
    }

    /**
     * callbackキャンセル用.
     *
     * @param providerCallback callback(nullを設定)
     */
    public void setApiDataProviderCallback(final ApiDataProviderCallback providerCallback) {
        this.mApiDataProviderCallback = providerCallback;
    }
}