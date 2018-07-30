/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationMetaData;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RecordingReservationListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RemoteRecordingReservationListWebClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 録画予約データ取得用プロパイダ.
 */
public class RecordingReservationListDataProvider implements
        RemoteRecordingReservationListWebClient.RemoteRecordingReservationListJsonParserCallback,
        RecordingReservationListWebClient.RecordingReservationListJsonParserCallback,
        ChannelWebClient.ChannelJsonParserCallback {

    /**
     * コンテキスト.
     */
    private final Context mContext;
    /**
     * コールバック.
     */
    private ApiDataProviderCallback mApiDataProviderCallback = null;
    /**
     * ソート完了リスト.
     */
    private List<ContentsData> mRecordingReservationList = null;
    /**
     * dリモートレスポンス.
     */
    private RecordingReservationListResponse mDRemoteResponse = null;
    /**
     * STBレスポンス.
     */
    private RemoteRecordingReservationListResponse mStbResponse = null;
    /**
     * レスポンス突合後リスト.
     */
    private List<RecordingReservationContentInfo> mBuffMatchList = null;
    /**
     * チャンネル一覧.
     */
    private List<HashMap<String, String>> mTvScheduleList = null;

    /**
     * 録画予約情報受信時刻.
     */
    private String mReservationTime = null;

    // 録画予約ステータスの固定値
    /**
     * チューナー反映待ち.
     */
    public static final int RECORD_RESERVATION_SYNC_STATUS_REFLECTS_WAITING = 1;
    /**
     * チューナー反映中.
     */
    public static final int RECORD_RESERVATION_SYNC_STATUS_DURING_REFLECT = 2;
    /**
     * チューナー反映済み.
     */
    public static final int RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT = 3;
    /**
     * チューナー反映失敗.
     */
    public static final int RECORD_RESERVATION_SYNC_STATUS_REFLECT_FAILURE = 4;

    // 定期予約判定用定数
    /**
     * 単発.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_SINGLE = 0;
    /**
     * 毎月曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_MONDAY = 1;
    /**
     * 毎火曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_TUESDAY = 2;
    /**
     * 毎水曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_WEDNESDAY = 3;
    /**
     * 毎木曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_THURSDAY = 4;
    /**
     * 毎金曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_FRIDAY = 5;
    /**
     * 毎土曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SATURDAY = 6;
    /**
     * 毎日曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SUNDAY = 7;
    /**
     * 月曜～金曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_MON_FRI = 8;
    /**
     * 月曜～土曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_MON_SAT = 9;
    /**
     * 毎日.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EVERYDAY = 10;
    /**
     * 日曜～木曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_THU = 11;
    /**
     * 日曜～金曜.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_FRI = 12;
    /**
     * 定期予約判定Default値.
     */
    private static final int RECORD_RESERVATION_LOOP_TYPE_DEFAULT = 99;

    // 現在時刻と比較した次回録画予約日時の定数(MapのKey値)
    /**
     * 0日後.
     */
    private static final int RECORD_RESERVATION_MAP_INDEX_TODAY = 0;
    /**
     * 1日後.
     */
    private static final int RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER = 1;
    /**
     * 2日後.
     */
    private static final int RECORD_RESERVATION_MAP_INDEX_TWO_DAY_LATER = 2;
    /**
     * 3日後.
     */
    private static final int RECORD_RESERVATION_MAP_INDEX_THREE_DAY_LATER = 3;
    /**
     * 4日後.
     */
    private static final int RECORD_RESERVATION_MAP_INDEX_FOUR_DAY_LATER = 4;
    /**
     * 5日後.
     */
    private static final int RECORD_RESERVATION_MAP_INDEX_FIVE_DAY_LATER = 5;
    /**
     * 6日後.
     */
    private static final int RECORD_RESERVATION_MAP_INDEX_SIX_DAY_LATER = 6;
    /**
     * 7日後.
     */
    private static final int RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER = 7;
    /**
     * 単発予約格納用Key.
     */
    private static final int RECORD_RESERVATION_MAP_INDEX_SINGLE_RECORD = 8;
    /**
     * 多チャンネル放送の区分.
     */
    private static final int RECORD_RESERVATION_MULTI_CHANNEL = 1;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * STB側録画予約一覧取得用Webクライアント.
     */
    private RemoteRecordingReservationListWebClient mStbWebClient = null;
    /**
     * dリモート側録画予約一覧取得用Webクライアント.
     */
    private RecordingReservationListWebClient mDRemoteWebClient = null;
    /**
     * チャンネルリスト取得用Webクライアント.
     */
    private ChannelWebClient mWebClient = null;
    /**
     * 録画予約一覧用エラー情報バッファ.
     */
    private ErrorState mError = null;

    /**
     * STB/dリモート 定期予約タイプ対応定数.
     */
    private enum ReservationType {
        /**
         * 単発録画.
         */
        TYPE_SINGLE(0, "0x00"),
        /**
         * 毎月曜.
         */
        TYPE_EV_MONDAY(1, "0x02"),
        /**
         * 毎火曜.
         */
        TYPE_EV_TUESDAY(2, "0x04"),
        /**
         * 毎水曜.
         */
        TYPE_EV_WEDNESDAY(3, "0x08"),
        /**
         * 毎木曜.
         */
        TYPE_EV_THURSDAY(4, "0x10"),
        /**
         * 毎金曜.
         */
        TYPE_EV_FRIDAY(5, "0x20"),
        /**
         * 毎土曜.
         */
        TYPE_EV_SATURDAY(6, "0x40"),
        /**
         * 毎日曜.
         */
        TYPE_EV_SUNDAY(7, "0x01"),
        /**
         * 月曜～金曜.
         */
        TYPE_MON_FRI(8, "0x3e"),
        /**
         * 日曜～木曜.
         */
        TYPE_SUN_THU(11, "0x1f"),
        /**
         * 日曜～金曜.
         */
        TYPE_SUN_FRI(12, "0x3f"),
        /**
         * 毎月.
         */
        TYPE_EV_MONTH(13, "0x7e"),;
        /**
         * ID.
         */
        private final int mId;
        /**
         * 予約タイプ.
         */
        private final String mDocomoRemote;

        /**
         * コンストラクタ.
         *
         * @param id      ID
         * @param dRemote 予約タイプ
         */
        ReservationType(final int id, final String dRemote) {
            this.mId = id;
            this.mDocomoRemote = dRemote;
        }

        /**
         * 予約タイプに対応するIDを取得する.
         *
         * @param dRemote 予約タイプ
         * @return 予約タイプのID
         */
        public static int getInt(final String dRemote) {
            ReservationType[] types = ReservationType.values();
            for (ReservationType type : types) {
                if (type.mDocomoRemote.equals(dRemote)) {
                    return type.mId;
                }
            }
            return 99;
        }
    }

    @Override
    public void onRemoteRecordingReservationListJsonParsed(final RemoteRecordingReservationListResponse response) {
        DTVTLogger.start();
        if (response != null) {
            mStbResponse = response;
            // CH一覧取得,リモート側との同期
            if (mDRemoteResponse != null && mTvScheduleList != null && mTvScheduleList.size() != 0) {
                buttRecordingReservationListData();
                sortRecordingReservationListData();
                mApiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
            }
        } else {
            DTVTLogger.error("response is null");
            //データが取得できなかったので、エラーを取得する
            if (mStbWebClient != null) {
                mError = mStbWebClient.getError();
            }
            mApiDataProviderCallback.recordingReservationListCallback(null);
        }
        DTVTLogger.end();
    }

    @Override
    public void onRecordingReservationListJsonParsed(final RecordingReservationListResponse response) {
        DTVTLogger.start();
        if (response != null) {
            mDRemoteResponse = response;
            if (null != response.getReservation()) {
                mReservationTime = DateUtils.getRecordShowListItem(Long.parseLong(response.getReservation()));
            }
            // CH一覧取得,STB側との同期
            if (mStbResponse != null && mTvScheduleList != null && mTvScheduleList.size() != 0) {
                buttRecordingReservationListData();
                sortRecordingReservationListData();
                mApiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
            }
        } else {
            DTVTLogger.error("response is null");
            //データが取得できなかったので、エラーを取得する
            if (mDRemoteWebClient != null) {
                mError = mDRemoteWebClient.getError();
            }
            mApiDataProviderCallback.recordingReservationListCallback(null);
        }
        DTVTLogger.end();
    }

    @Override
    public void onChannelJsonParsed(final List<ChannelList> channelLists) {
        DTVTLogger.start();
        if (channelLists != null && channelLists.size() > 0) {
            ChannelList list = channelLists.get(0);
            mTvScheduleList = list.getChannelList();
            // 録画予約一覧取得との同期
            if (mStbResponse != null && mDRemoteResponse != null) {
                buttRecordingReservationListData();
                sortRecordingReservationListData();
                mApiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
            }
        } else {
            DTVTLogger.error("response is null");
            //データが取得できなかったので、エラーを取得する
            if (mWebClient != null) {
                mError = mWebClient.getError();
            }
            mApiDataProviderCallback.recordingReservationListCallback(null);
        }
        DTVTLogger.end();
    }

    /**
     * WebApiからのコールバックデータを返却するためのActivity実装用コールバック.
     */
    public interface ApiDataProviderCallback {

        /**
         * 録画予約一覧返却用コールバック.
         *
         * @param list コンテンツデータリスト
         */
        void recordingReservationListCallback(List<ContentsData> list);
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public RecordingReservationListDataProvider(final Context context) {
        this.mContext = context;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) context;
    }

    /**
     * Activityからのデータ取得要求受付.
     */
    public void requestRecordingReservationListData() {
        DTVTLogger.start();
        initDataList();

        if (!mIsCancel) {
            // STB側録画予約一覧取得要求 現状プラットフォームタイプのパラメータは仕様書には1のみ記載
            mStbWebClient = new RemoteRecordingReservationListWebClient(mContext);
            mStbWebClient.getRemoteRecordingReservationListApi(
                    RECORD_RESERVATION_MULTI_CHANNEL, this);

            // dリモート側録画予約一覧取得要求
            mDRemoteWebClient = new RecordingReservationListWebClient(mContext);
            int limit = 0;
            int offset = 0;
            mDRemoteWebClient.getRecordingReservationListApi(limit, offset, this);

            // チャンネル一覧取得
            getTvScheduleListData();
        } else {
            DTVTLogger.error("RecordingReservationListDataProvider is stop connection.");
        }
        DTVTLogger.end();
    }

    /**
     * 取得リストを初期化.
     */
    private void initDataList() {
        DTVTLogger.debug("Init ListData");
        mStbResponse = null;
        mDRemoteResponse = null;
        mTvScheduleList = null;
        mRecordingReservationList = null;
        mBuffMatchList = null;
    }

    /**
     * CH一覧情報取得.
     */
    private void getTvScheduleListData() {
        DTVTLogger.start();
        //通信クラスにデータ取得要求を出す
        mWebClient = new ChannelWebClient(mContext);
        String filter = "";
        String type = "";
        mWebClient.getChannelApi(0, 0, filter, type, this);
        DTVTLogger.end();
    }

    /**
     * ContentsDataを生成(STB).
     *
     * @param data レスポンスデータ
     * @return コンテンツデータ
     */
    private ContentsData createContentsData(final RemoteRecordingReservationMetaData data) {
        ContentsData contentsData = new ContentsData();
        // タイトル
        contentsData.setTitle(data.getTitle());
        // チャンネル名
        contentsData.setChannelName(getChannelName(data.getServiceId()));
        // 録画予約ステータス
        contentsData.setRecordingReservationStatus(data.getSyncStatus());

        return contentsData;
    }

    /**
     * ContentsDataを生成(dリモート).
     *
     * @param data レスポンスデータ
     * @return コンテンツデータ
     */
    private ContentsData createContentsData(final RecordingReservationMetaData data) {
        ContentsData contentsData = new ContentsData();
        // タイトル
        contentsData.setTitle(data.getTitle());
        // チャンネル名
        contentsData.setChannelName(getChannelName(data.getServiceId()));
        // 録画予約ステータス
        contentsData.setRecordingReservationStatus(RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT);

        return contentsData;
    }

    /**
     * STB側とdリモート側のレスポンスを突合してSparseArrayに格納する.
     */
    private void buttRecordingReservationListData() {
        DTVTLogger.start();
        mBuffMatchList = new ArrayList<>();
        List<RemoteRecordingReservationMetaData> stbList = mStbResponse.getRemoteRecordingReservationMetaData();
        List<RecordingReservationMetaData> dRemoteList = mDRemoteResponse.getRecordingReservationMetaData();
        //Stbリストをすべて格納
        for (RemoteRecordingReservationMetaData stbData : stbList) {
            setRecordingReservationContentInfo(stbData);
        }
        List<RecordingReservationContentInfo> tmpBuffMatchArray = mBuffMatchList;
        DTVTLogger.debug("tmpBuffMatchArray.size() = " + tmpBuffMatchArray.size());
        boolean matchFlag;
        // リモート側データとSTB側の重複を除外してリスト化.
        // リモート側はServiceidは"00XX"のような値が来るが、STB側は"0xXX"のような値が来る
        // ともに16進数のようだが単純文字列比較では重複とみなさなくなるので、先頭0xやその後の0を削除して比較する
        for (RecordingReservationMetaData dRemoteData : dRemoteList) {
            matchFlag = false;
            String remote_seriviceId = shapeIdString(dRemoteData.getServiceId());
            String remote_eventId = shapeIdString(dRemoteData.getEventId());
            if (remote_seriviceId != null && !remote_seriviceId.isEmpty()
                    && remote_eventId != null && !remote_eventId.isEmpty()) {
                for (RecordingReservationContentInfo info : tmpBuffMatchArray) {
                    String stb_serviceId = shapeIdString(info.getServiceId());
                    String stb_eventId = shapeIdString(info.getEventId());
                    if (stb_serviceId != null && !stb_serviceId.isEmpty()
                            && stb_eventId != null && !stb_eventId.isEmpty()) {
                        if (remote_seriviceId.equals(stb_serviceId) && remote_eventId.equals(stb_eventId)) {
                            matchFlag = true;
                            DTVTLogger.debug("Match Data");
                        }
                    }
                }
            }
            if (!matchFlag) {
                setRecordingReservationContentInfo(dRemoteData);
            }
        }
        DTVTLogger.end();
    }

    /**
     * ServiceId、EventId文字列の整形.
     */
    private String shapeIdString(final String id) {
        String retStr = null;
        if (id != null) {
            //先頭0xとその後の0連続を削除する
            retStr = id.replaceFirst("0x", "");
            retStr = retStr.replaceFirst("^0+", "");
        }
        return retStr;
    }

    /**
     * STB側レスポンスデータをRecordingReservationContentInfoに設定.
     *
     * @param data レスポンスデータ
     */
    private void setRecordingReservationContentInfo(final RemoteRecordingReservationMetaData data) {
        RecordingReservationContentInfo contentInfo = new RecordingReservationContentInfo();
        // 開始時間以外のコンテンツデータを設定
        ContentsData contentsData = createContentsData(data);
        contentInfo.setContentsData(contentsData);
        contentInfo.setServiceId(data.getServiceId());
        contentInfo.setEventId(data.getEventId());
        if (data.getLoopTypeNum() == RECORD_RESERVATION_LOOP_TYPE_NUM_SINGLE) {
            // Mapへputする
            putBuffMatchListMapSingle(data.getStartTime(), contentInfo);
        } else {
            // Mapへputする
            putBuffMatchListMap(data.getLoopTypeNum(), data.getStartTime(), contentInfo);
        }
    }

    /**
     * dリモート側レスポンスデータをRecordingReservationContentInfoに設定.
     *
     * @param data レスポンスデータ
     */
    private void setRecordingReservationContentInfo(final RecordingReservationMetaData data) {
        RecordingReservationContentInfo contentInfo = new RecordingReservationContentInfo();
        // 開始時間以外のコンテンツデータを設定
        ContentsData contentsData = createContentsData(data);
        contentInfo.setContentsData(contentsData);
        contentInfo.setServiceId(data.getServiceId());
        contentInfo.setEventId(data.getEventId());
        if (ReservationType.getInt(data.getDayOfTheWeek()) == RECORD_RESERVATION_LOOP_TYPE_NUM_SINGLE) {
            // Mapへputする
            putBuffMatchListMapSingle(data.getStartScheduleTime(), contentInfo);
        } else {
            // Mapへputする
            putBuffMatchListMap(ReservationType.getInt(data.getDayOfTheWeek()), data.getStartScheduleTime(), contentInfo);
        }
    }

    /**
     * 日付差毎のkey値を算出してMapにputする.
     *
     * @param startTime 開始時間（エポック秒：秒単位）
     * @param info      　コンテンツデータ
     */
    private void putBuffMatchListMapSingle(final long startTime, final RecordingReservationContentInfo info) {
        DTVTLogger.start();
        info.setStartTimeEpoch(startTime);
        mBuffMatchList.add(info);
        DTVTLogger.end();
    }

    /**
     * 単発録画予約以外のレスポンスに対して日付差毎のkey値を算出してMapにputする.
     *
     * @param loopTypeNum 録画予約パターン
     * @param startTime   開始時間（0時00分00秒からの秒数）
     * @param info        コンテンツデータ
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    private void putBuffMatchListMap(final int loopTypeNum, final long startTime,
                                     final RecordingReservationContentInfo info) {
        Long CONVERSION_ONE_DAY_TO_SEC = 86400L;

        DTVTLogger.start("loopTypeNum = " + loopTypeNum);
        // 現在日の曜日
        int todayDayOfWeek = DateUtils.getTodayDayOfWeek();
        // 現在日時（エポック秒：秒単位）
        long nowTimeEpoch = DateUtils.getNowTimeFormatEpoch();
        // レスポンスデータの日時（エポック秒：秒単位）
        long dataTimeEpoch = DateUtils.getTodayStartTimeFormatEpoch() + startTime;
        int dayLater = RECORD_RESERVATION_LOOP_TYPE_DEFAULT;
        DTVTLogger.debug("toDayOfWeek = " + todayDayOfWeek);
        switch (loopTypeNum) {
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_MONDAY: // 毎週月曜日
                DTVTLogger.debug("case 1:dayOfWeek 2");
                //0時0分0秒からの通算秒かどうかを判定
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_MONDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_TUESDAY: // 毎週火曜日
                DTVTLogger.debug("case 2:dayOfWeek 3");
                //0時0分0秒からの通算秒かどうかを判定
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_TUESDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_WEDNESDAY: // 毎週水曜日
                DTVTLogger.debug("case 3:dayOfWeek 4");
                //0時0分0秒からの通算秒かどうかを判定
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_WEDNESDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_THURSDAY: // 毎週木曜日
                DTVTLogger.debug("case 4:dayOfWeek 5");
                //0時0分0秒からの通算秒かどうかを判定
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_THURSDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_FRIDAY: // 毎週金曜日
                DTVTLogger.debug("case 5:dayOfWeek 6");
                //0時0分0秒からの通算秒かどうかを判定
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SATURDAY: // 毎週土曜日
                DTVTLogger.debug("case 6:dayOfWeek 7");
                //0時0分0秒からの通算秒かどうかを判定
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SUNDAY: // 毎週日曜日
                DTVTLogger.debug("case 7:dayOfWeek 1");
                //0時0分0秒からの通算秒かどうかを判定
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SUNDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_MON_FRI: // 月曜日～金曜日
                DTVTLogger.debug("case 8");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_THREE_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY
                        || todayDayOfWeek == DateUtils.DAY_OF_WEEK_SUNDAY) {
                    dayLater = DateUtils.getNumberOfDaysUntilMonday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_MON_SAT: // 月曜日～土曜日
                DTVTLogger.debug("case 9");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_THREE_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SUNDAY) {
                    dayLater = DateUtils.getNumberOfDaysUntilMonday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EVERYDAY: // 毎日
                DTVTLogger.debug("case 10");
                if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                    dayLater = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                } else {
                    dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_THU: // 日曜日～木曜日
                DTVTLogger.debug("case 11");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_THURSDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_THREE_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY
                        || todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    dayLater = DateUtils.getNumberOfDaysUntilSunday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_FRI: // 日曜日～金曜日
                DTVTLogger.debug("case 12");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TWO_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    dayLater = DateUtils.getNumberOfDaysUntilSunday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        dayLater = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            default:
                DTVTLogger.debug("default");
                break;
        }
        if (dayLater == RECORD_RESERVATION_LOOP_TYPE_DEFAULT) {
            int dataDayOfWeek = loopTypeNum + 1;
            DTVTLogger.debug("dataDayOfWeek = " + dataDayOfWeek);
            if (todayDayOfWeek < dataDayOfWeek) {
                dayLater = dataDayOfWeek - todayDayOfWeek;
            } else {
                dayLater = (dataDayOfWeek + 7) - todayDayOfWeek;
            }
        }

        dataTimeEpoch = dataTimeEpoch + (CONVERSION_ONE_DAY_TO_SEC * dayLater);
        info.setStartTimeEpoch(dataTimeEpoch);
        mBuffMatchList.add(info);
        DTVTLogger.end("dayLater = " + dayLater + " todayDayOfWeek = " + todayDayOfWeek + "表示:"
                + DateUtils.getRecordShowListItem(info.getStartTimeEpoch()) + "mBuffMatchList.size() = " + mBuffMatchList.size());
    }

    /**
     * エポック秒による時間の比較.
     *
     * @param dataTimeA 比較時間A
     * @param dataTimeB 比較時間B
     * @return true:Aが大  false:Bが大
     */
    private boolean buffStartTime(final long dataTimeA, final long dataTimeB) {
        return dataTimeA > dataTimeB;
    }

    /**
     * リストデータを開始時間の昇順にソートを実施.
     */
    private void sortRecordingReservationListData() {
        DTVTLogger.start();
        mRecordingReservationList = new ArrayList<>();
        Collections.sort(mBuffMatchList, new ContentInfoComparator());
        for (int i = 0; i < mBuffMatchList.size(); i++) {
            mRecordingReservationList.add(mBuffMatchList.get(i).returnContentsData());
        }

        DTVTLogger.end("mRecordingReservationList.size() = " + mRecordingReservationList.size());
    }

    /**
     * CH一覧のレスポンスからチャンネル名を取得.
     *
     * @param serviceId サービスID
     * @return チャンネル名
     */
    private String getChannelName(final String serviceId) {
        DTVTLogger.start("serviceId = " + serviceId + " TvScheduleList.size = " + mTvScheduleList.size());
        String channelName = null;
        for (Map<String, String> map : mTvScheduleList) {
            if (map != null && serviceId.equals(map.get(JsonConstants.META_RESPONSE_SERVICE_ID))) {
                channelName = map.get(JsonConstants.META_RESPONSE_TITLE);
                DTVTLogger.debug("channel = " + map.get(JsonConstants.META_RESPONSE_TITLE));
                break;
            }
        }
        DTVTLogger.end();
        return channelName;
    }

    /**
     * ソート用レスポンス格納クラス.
     */
    private static class RecordingReservationContentInfo {
        /**
         * コンテンツデータ.
         */
        private ContentsData contentsData = null;
        /**
         * 開始日時（エポック秒:秒単位）.
         */
        private long startTimeEpoch = 0;
        /**
         * サービスID.
         */
        private String serviceId = null;
        /**
         * イベントID.
         */
        private String eventId = null;

        /**
         * コンテンツデータを保存する.
         *
         * @param contentsData コンテンツデータ
         */
        private void setContentsData(final ContentsData contentsData) {
            this.contentsData = contentsData;
        }

        /**
         * 開始時間を保存する（エポック秒：秒単位）.
         *
         * @param epochTime 開始時間
         */
        private void setStartTimeEpoch(final long epochTime) {
            this.startTimeEpoch = epochTime;
        }

        /**
         * サービスIDを保存する.
         *
         * @param serviceId サービスID
         */
        private void setServiceId(final String serviceId) {
            this.serviceId = serviceId;
        }

        /**
         * イベントIDを保存する.
         *
         * @param eventId イベントID
         */
        private void setEventId(final String eventId) {
            this.eventId = eventId;
        }

        /**
         * 開始時間をエポック秒で取得する.
         *
         * @return 開始時間
         */
        private long getStartTimeEpoch() {
            return this.startTimeEpoch;
        }

        /**
         * サービスIDを取得する.
         *
         * @return サービスID
         */
        private String getServiceId() {
            return this.serviceId;
        }

        /**
         * イベントIDを取得する.
         *
         * @return イベントID
         */
        private String getEventId() {
            return this.eventId;
        }

        /**
         * 開始時間をエポック秒からFormatしてString値としてコンテンツデータに設定して返す.
         *
         * @return コンテンツデータクラス
         */
        private ContentsData returnContentsData() {
            ContentsData contentsData = this.contentsData;
            contentsData.setTime(DateUtils.getRecordShowListItem(this.startTimeEpoch));
            return contentsData;
        }
    }

    /**
     * ソート条件定義クラス.
     */
    private static class ContentInfoComparator implements Serializable,
            Comparator<RecordingReservationContentInfo> {

        private static final long serialVersionUID = -7546755978760372394L;

        @Override
        public int compare(final RecordingReservationContentInfo infoA, final RecordingReservationContentInfo infoB) {
            long infoA_StartTime = infoA.getStartTimeEpoch();
            long infoB_StartTime = infoB.getStartTimeEpoch();

            // RecordingReservationContentInfo.StartTimeEpochの昇順にソート
            if (infoA_StartTime > infoB_StartTime) {
                return 1;
            } else if (infoA_StartTime == infoB_StartTime) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        if (mStbWebClient != null) {
            mStbWebClient.stopConnection();
        }
        if (mDRemoteWebClient != null) {
            mDRemoteWebClient.stopConnection();
        }
        if (mWebClient != null) {
            mWebClient.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
        if (mStbWebClient != null) {
            mStbWebClient.enableConnection();
        }
        if (mDRemoteWebClient != null) {
            mDRemoteWebClient.enableConnection();
        }
        if (mWebClient != null) {
            mWebClient.enableConnection();
        }
    }

    /**
     * 録画予約リスト取得エラーのクラスを返すゲッター.
     *
     * @return 録画予約リスト取得エラーのクラス
     */
    public ErrorState getError() {
        return mError;
    }


    public String getReservationTime() {
        return mReservationTime;
    }

}