/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.util.SparseArray;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationMetaData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RecordingReservationListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RemoteRecordingReservationListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordingReservationListDataProvider implements
        RemoteRecordingReservationListWebClient.RemoteRecordingReservationListJsonParserCallback,
        RecordingReservationListWebClient.RecordingReservationListJsonParserCallback,
        ChannelWebClient.ChannelJsonParserCallback {

    private Context mContext;
    private ApiDataProviderCallback apiDataProviderCallback;
    // ソート完了リスト
    private List<ContentsData> mRecordingReservationList = null;
    // dリモートレスポンス　
    private RecordingReservationListResponse mDRemoteResponse = null;
    // STBレスポンス
    private RemoteRecordingReservationListResponse mStbResponse = null;
    // レスポンス突合後リストマップ
    private SparseArray<List<RecordingReservationContentInfo>> mBuffMatchListMap = null;
    // チャンネル一覧
    private List<Map<String, String>> mTvScheduleList = null;

    // 録画予約ステータスの固定値
    public static final int RECORD_RESERVATION_SYNC_STATUS_REFLECTS_WAITING = 1; // チューナー反映待ち
    public static final int RECORD_RESERVATION_SYNC_STATUS_DURING_REFLECT = 2; // チューナー反映中
    public static final int RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT = 3; // チューナー反映済み
    public static final int RECORD_RESERVATION_SYNC_STATUS_REFLECT_FAILURE = 4; // チューナー反映失敗

    // 定期予約判定用定数
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_SINGLE = 0; // 単発
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_MONDAY = 1; // 毎月曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_TUESDAY = 2; // 毎火曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_WEDNESDAY = 3; // 毎水曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_THURSDAY = 4; // 毎木曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_FRIDAY = 5; // 毎金曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SATURDAY = 6; // 毎土曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SUNDAY = 7; // 毎日曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_MON_FRI = 8; // 月曜～金曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_MON_SAT = 9; // 月曜～土曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EVERYDAY = 10; // 毎日
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_THU = 11; // 日曜～木曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_FRI = 12; // 日曜～金曜
    private static final int RECORD_RESERVATION_LOOP_TYPE_NUM_EV_MONTH = 13; // 毎月

    // 現在時刻と比較した次回録画予約日時の定数
    private static final int RECORD_RESERVATION_MAP_INDEX_TODAY = 0;
    private static final int RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER = 1;
    private static final int RECORD_RESERVATION_MAP_INDEX_TWO_DAY_LATER = 2;
    private static final int RECORD_RESERVATION_MAP_INDEX_THREE_DAY_LATER = 3;
    private static final int RECORD_RESERVATION_MAP_INDEX_FOUR_DAY_LATER = 4;
    private static final int RECORD_RESERVATION_MAP_INDEX_FIVE_DAY_LATER = 5;
    private static final int RECORD_RESERVATION_MAP_INDEX_SIX_DAY_LATER = 6;
    private static final int RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER = 7;

    // 1日→秒
    private static int CONVERSION_ONE_DAY_TO_SEC = 86400;

    private enum ReservationType {
        // STB/dリモート 定期予約タイプ対応定数
        TYPE_SINGLE(0, "0x00"), // 単発録画
        TYPE_EV_MONDAY(1, "0x02"), // 毎月曜
        TYPE_EV_TUESDAY(2, "0x04"), // 毎火曜
        TYPE_EV_WEDNESDAY(3, "0x08"), // 毎水曜
        TYPE_EV_THURSDAY(4, "0x10"), // 毎木曜
        TYPE_EV_FRIDAY(5, "0x20"), // 毎金曜
        TYPE_EV_SATURDAY(6, "0x40"), // 毎土曜
        TYPE_EV_SUNDAY(7, "0x01"), // 毎日曜
        TYPE_MON_FRI(8, "0x3e"), // 月曜～金曜
        TYPE_SUN_THU(11, "0x1f"), // 日曜～木曜
        TYPE_SUN_FRI(12, "0x3f"), // 日曜～金曜
        TYPE_EV_MONTH(13, "0x7e"),; // 毎月
        private final int id;
        private final String dRemote;

        private ReservationType(final int id, final String dRemote) {
            this.id = id;
            this.dRemote = dRemote;
        }

        public static int getInt(String dRemote) {
            ReservationType[] types = ReservationType.values();
            for (ReservationType type : types) {
                if (type.dRemote.equals(dRemote)) {
                    return type.id;
                }
            }
            return 99;
        }
    }

    @Override
    public void onRemoteRecordingReservationListJsonParsed(RemoteRecordingReservationListResponse response) {
        DTVTLogger.start();
        if (response != null) {
            mStbResponse = response;
            // CH一覧取得,リモート側との同期
            if (mDRemoteResponse != null && mTvScheduleList != null && mTvScheduleList.size() != 0) {
                buttRecordingReservationListData();
                sortRecordingReservationListData();
                apiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
            }
        } else {
            DTVTLogger.error("response is null");
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
        DTVTLogger.end();
    }

    @Override
    public void onRecordingReservationListJsonParsed(RecordingReservationListResponse response) {
        DTVTLogger.start();
        if (response != null) {
            mDRemoteResponse = response;
            // CH一覧取得,STB側との同期
            if (mStbResponse != null && mTvScheduleList != null && mTvScheduleList.size() != 0) {
                buttRecordingReservationListData();
                sortRecordingReservationListData();
                apiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
            }
        } else {
            DTVTLogger.error("response is null");
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
        DTVTLogger.end();
    }

    @Override
    public void onChannelJsonParsed(List<ChannelList> channelLists) {
        DTVTLogger.start();
        if (channelLists != null && channelLists.size() > 0) {
            ChannelList list = channelLists.get(0);
            mTvScheduleList = list.getClList();
            // 録画予約一覧取得との同期
            if (mStbResponse != null && mDRemoteResponse != null) {
                buttRecordingReservationListData();
                sortRecordingReservationListData();
                apiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
            }
        } else {
            DTVTLogger.error("response is null");
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
        DTVTLogger.end();
    }

    /**
     * WebApiからのコールバックデータを返却するためのActivity実装用コールバック
     */
    public interface ApiDataProviderCallback {

        /**
         * 録画予約一覧返却用コールバック
         *
         * @param list コンテンツデータリスト
         */
        void recordingReservationListCallback(List<ContentsData> list);
    }

    /**
     * コンストラクタ
     *
     * @param mContext コンテキスト
     */
    public RecordingReservationListDataProvider(Context mContext) {
        this.mContext = mContext;
        this.apiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * Activityからのデータ取得要求受付
     */
    public void requestRecordingReservationListData() {
        DTVTLogger.start();
        initDataList();

        // STB側録画予約一覧取得要求
        RemoteRecordingReservationListWebClient stbWebClient = new RemoteRecordingReservationListWebClient();
        stbWebClient.getRemoteRecordingReservationListApi(this);

        // dリモート側録画予約一覧取得要求
        RecordingReservationListWebClient dRemoteWebClient= new RecordingReservationListWebClient();
        int limit = 0;
        int offset = 0;
        dRemoteWebClient.getRecordingReservationListApi(limit, offset, this);

        // チャンネル一覧取得
        getTvScheduleListData();
        DTVTLogger.end();
    }

    /**
     * 取得リストを初期化
     */
    private void initDataList() {
        DTVTLogger.debug("Init ListData");
        mStbResponse = null;
        mDRemoteResponse = null;
        mTvScheduleList = null;
        mRecordingReservationList = null;
        mBuffMatchListMap = null;
    }

    /**
     * CH一覧情報取得
     */
    private void getTvScheduleListData() {
        DTVTLogger.start();
        List<HashMap<String, String>> list = new ArrayList<>();
        //通信クラスにデータ取得要求を出す
        ChannelWebClient webClient = new ChannelWebClient();
        int pagetLimit = 1;
        int pagerOffset = 1;
        String filter = "";
        String type = "";
        webClient.getChannelApi(pagetLimit, pagerOffset, filter, type, this);
        DTVTLogger.end();
    }

    /**
     * ContentsDataを生成(STB)
     *
     * @param data レスポンスデータ
     * @return コンテンツデータ
     */
    private ContentsData createContentsData(RemoteRecordingReservationMetaData data) {
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
     * ContentsDataを生成(dリモート)
     *
     * @param data レスポンスデータ
     * @return コンテンツデータ
     */
    private ContentsData createContentsData(RecordingReservationMetaData data) {
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
     * STB側とdリモート側のレスポンスを突合してSparseArrayに格納する
     */
    private void buttRecordingReservationListData() {
        DTVTLogger.start();
        DTVTLogger.debug("start buff");
        mBuffMatchListMap = new SparseArray<List<RecordingReservationContentInfo>>();
        List<RemoteRecordingReservationMetaData> stbList = mStbResponse.getRemoteRecordingReservationMetaData();
        List<RecordingReservationMetaData> dRemoteList = mDRemoteResponse.getRecordingReservationMetaData();
        for (RemoteRecordingReservationMetaData stbData : stbList) {
            int j = 0;
            for (RecordingReservationMetaData dRemoteData : dRemoteList) {
                if (stbData.getServiceId().equals(dRemoteData.getServiceId())
                        && stbData.getEventId().equals(dRemoteData.getEventId())) {
                    setRecordingReservationContentInfo(dRemoteData);
                    DTVTLogger.debug("Match Data");
                    break;
                }
            }
        }
        DTVTLogger.debug("end buff");
        SparseArray<List<RecordingReservationContentInfo>> sparseArray = mBuffMatchListMap.clone();
        DTVTLogger.debug("sparseArray.size() = " + sparseArray.size());
        boolean matchFlag = false;
        // STB側データを格納
        for (RemoteRecordingReservationMetaData stbData : stbList) {
            matchFlag = false;
            for (int i = 0; i < sparseArray.size(); i++) {
                int key = sparseArray.keyAt(i);
                List<RecordingReservationContentInfo> list = sparseArray.get(key, new ArrayList<RecordingReservationContentInfo>());
                DTVTLogger.debug("sparseArray.get(" + i + ").size =" + list.size());
                for (RecordingReservationContentInfo info : list) {
                    if (stbData.getServiceId().equals(info.getServiceId())
                            && stbData.getEventId().equals(info.getEventId())) {
                        matchFlag = true;
                        DTVTLogger.debug("Match Data");
                        break;
                    }
                }
            }
            if (!matchFlag) {
                setRecordingReservationContentInfo(stbData);
                matchFlag = false;
            }
        }
        // dリモート側データを格納
        for (RecordingReservationMetaData dRemoteData : dRemoteList) {
            matchFlag = false;
            for (int i = 0; i < sparseArray.size(); i++) {
                int key = sparseArray.keyAt(i);
                List<RecordingReservationContentInfo> list = sparseArray.get(key, new ArrayList<RecordingReservationContentInfo>());
                for (RecordingReservationContentInfo info : list) {
                    if (dRemoteData.getServiceId().equals(info.getServiceId())
                            && dRemoteData.getEventId().equals(info.getEventId())) {
                        matchFlag = true;
                        DTVTLogger.debug("Match Data");
                    }
                }
            }
            if (!matchFlag) {
                setRecordingReservationContentInfo(dRemoteData);
                matchFlag = false;
            }
        }
        DTVTLogger.end();
    }

    /**
     * STB側レスポンスデータをRecordingReservationContentInfoに設定
     *
     * @param data レスポンスデータ
     */
    private void setRecordingReservationContentInfo(RemoteRecordingReservationMetaData data) {
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
     * dリモート側レスポンスデータをRecordingReservationContentInfoに設定
     *
     * @param data レスポンスデータ
     */
    private void setRecordingReservationContentInfo(RecordingReservationMetaData data) {
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
     * 日付差毎のkey値を算出してMapにputする
     *
     * @param startTime 開始時間（エポック秒：秒単位）
     * @param info      　コンテンツデータ
     */
    private void putBuffMatchListMapSingle(long startTime, RecordingReservationContentInfo info) {
        DTVTLogger.start();
        // 本日の曜日
        int todayDayOfWeek = DateUtils.getTodayDayOfWeek();
        // レスポンスデータの曜日
        int dataDayOfWeek = DateUtils.getDayOfWeek(startTime);
        // mBuffMatchListMapのkey値
        int key;
        // 曜日比較
        if (todayDayOfWeek == dataDayOfWeek) {
            // 時間比較：現在日時より値が小さい場合リストには表示しない
            if (DateUtils.getNowTimeFormatEpoch() > startTime) {
                return;
            }
        }
        // mBuffMatchListMapのkey値を算出
        if (todayDayOfWeek > dataDayOfWeek) {
            key = (dataDayOfWeek + 7) - todayDayOfWeek;
        } else {
            key = dataDayOfWeek - todayDayOfWeek;
        }
        info.setStartTimeEpoch(startTime);
        List<RecordingReservationContentInfo> infoList = getRecordingReservationContentInfoList(key);
        infoList.add(info);
        mBuffMatchListMap.put(key, infoList);
        DTVTLogger.end("key = " + key);
    }

    /**
     * 単発録画予約以外のレスポンスに対して日付差毎のkey値を算出してMapにputする
     *
     * @param loopTypeNum 録画予約パターン
     * @param startTime   開始時間（0時00分00秒からの秒数）
     * @param info        コンテンツデータ
     */
    private void putBuffMatchListMap(int loopTypeNum, long startTime, RecordingReservationContentInfo info) {
        DTVTLogger.start("loopTypeNum = " + loopTypeNum);
        // 現在日の曜日
        int todayDayOfWeek = DateUtils.getTodayDayOfWeek();
        // 現在日時（エポック秒：秒単位）
        long nowTimeEpoch = DateUtils.getNowTimeFormatEpoch();
        // レスポンスデータの日時（エポック秒：秒単位）
        long dataTimeEpoch = DateUtils.getTodayStartTimeFormatEpoch() + startTime;
        int key = 99;
        DTVTLogger.debug("toDayOfWeek = " + todayDayOfWeek);
        switch (loopTypeNum) {
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_MONDAY: // 毎週月曜日
                DTVTLogger.debug("case 1:dayOfweek 2");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_MONDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_TUESDAY: // 毎週火曜日
                DTVTLogger.debug("case 2:dayOfweek 3");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_TUESDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_WEDNESDAY: // 毎週水曜日
                DTVTLogger.debug("case 3:dayOfweek 4");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_WEDNESDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_THURSDAY: // 毎週木曜日
                DTVTLogger.debug("case 4:dayOfweek 5");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_THURSDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_FRIDAY: // 毎週金曜日
                DTVTLogger.debug("case 5:dayOfweek 6");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SATURDAY: // 毎週土曜日
                DTVTLogger.debug("case 6:dayOfweek 7");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SUNDAY: // 毎週日曜日
                DTVTLogger.debug("case 7:dayOfweek 1");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SUNDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_MON_FRI: // 月曜日～金曜日
                DTVTLogger.debug("case 8");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_THREE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY
                        || todayDayOfWeek == DateUtils.DAY_OF_WEEK_SUNDAY) {
                    key = DateUtils.getNumberOfDaysUntilMonday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_MON_SAT: // 月曜日～土曜日
                DTVTLogger.debug("case 9");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_THREE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SUNDAY) {
                    key = DateUtils.getNumberOfDaysUntilMonday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EVERYDAY: // 毎日
                DTVTLogger.debug("case 10");
                if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                    key = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                } else {
                    key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                }
            case RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_THU: // 日曜日～木曜日
                DTVTLogger.debug("case 11");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_THURSDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_THREE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY
                        || todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    key = DateUtils.getNumberOfDaysUntilSunday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_FRI: // 日曜日～金曜日
                DTVTLogger.debug("case 12");
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_TWO_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    key = DateUtils.getNumberOfDaysUntilSunday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_MONTH: // 毎月
                DTVTLogger.debug("case 13");
                // TODO QA中 DREM-602　毎月の場合の開始時間のフォーマット
                break;
            default:
                DTVTLogger.debug("default");
                return;
        }
        if (key == 99) {
            int dataDayOfWeek = loopTypeNum + 1;
            DTVTLogger.debug("dataDayOfWeek = " + dataDayOfWeek);
            if (todayDayOfWeek < dataDayOfWeek) {
                key = dataDayOfWeek - todayDayOfWeek;
            } else {
                key = (dataDayOfWeek + 7) - todayDayOfWeek;
            }
        }
        dataTimeEpoch = dataTimeEpoch + (CONVERSION_ONE_DAY_TO_SEC * key);
        info.setStartTimeEpoch(dataTimeEpoch);
        List<RecordingReservationContentInfo> infoList = getRecordingReservationContentInfoList(key);
        infoList.add(info);
        mBuffMatchListMap.put(key, infoList);
        DTVTLogger.end("key = " + key + " todayDayOfWeek = " + todayDayOfWeek + "表示:"
                + DateUtils.getRecordShowListItem(info.getStartTimeEpoch()) + "mBuffMatchListMap.size() = " + mBuffMatchListMap.size());
    }

    /**
     * key値を元にSparseArrayからリストデータを取得
     *
     * @param key Mapのkey値
     * @return keyの値で格納したRecordingReservationContentInfoリスト
     */
    private List<RecordingReservationContentInfo> getRecordingReservationContentInfoList(int key) {
        return mBuffMatchListMap.get(key, new ArrayList<RecordingReservationContentInfo>());
    }

    /**
     * エポック秒による時間の比較
     *
     * @param dataTimeA 比較時間A
     * @param dataTimeB 比較時間B
     * @return true:Aが大  false:Bが大
     */
    private boolean buffStartTime(long dataTimeA, long dataTimeB) {
        if (dataTimeA > dataTimeB) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * リストデータを開始時間の昇順にソートを実施
     */
    private void sortRecordingReservationListData() {
        DTVTLogger.start();
        mRecordingReservationList = new ArrayList<ContentsData>();
        for (int i = 0; i < mBuffMatchListMap.size(); i++) {
            int key = mBuffMatchListMap.keyAt(i);
            List<RecordingReservationContentInfo> infoList = getRecordingReservationContentInfoList(key);
            Collections.sort(infoList, new ContentInfoComparator());
            for (int j = 0; j < infoList.size(); j++) {
                mRecordingReservationList.add(infoList.get(j).returnContentsData());
            }
        }
        DTVTLogger.end("mRecordingReservationList.size() = " + mRecordingReservationList.size());
    }

    /**
     * CH一覧のレスポンスからチャンネル名を取得
     *
     * @param serviceId サービスID
     * @return チャンネル名
     */
    private String getChannelName(String serviceId) {
        DTVTLogger.start("serviceId = " + serviceId + " TvScheduleList.size = " + mTvScheduleList.size());
        String channelName = null;
        for (Map<String, String> map : mTvScheduleList) {
            if (map != null && serviceId.equals(map.get(TvScheduleJsonParser.TV_SCHEDULE_LIST_SERVICE_ID))) {
                channelName = map.get(TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE);
                DTVTLogger.debug("channel = " + map.get(TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE));
                break;
            }
            DTVTLogger.debug("map.serviceId = " + map.get(TvScheduleJsonParser.TV_SCHEDULE_LIST_SERVICE_ID));
        }
        DTVTLogger.end();
        return channelName;
    }

    /**
     * ソート用レスポンス格納クラス
     */
    private class RecordingReservationContentInfo {
        // コンテンツデータ
        private ContentsData contentsData = null;
        // 開始日時（エポック秒:秒単位）
        private long startTimeEpoch = 0;
        // サービスID
        private String serviceId = null;
        // イベントID
        private String eventId = null;

        private void setContentsData(ContentsData contentsData) {
            this.contentsData = contentsData;
        }

        private void setStartTimeEpoch(long epochTime) {
            this.startTimeEpoch = epochTime;
        }

        private void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        private void setEventId(String eventId) {
            this.eventId = eventId;
        }

        private long getStartTimeEpoch() {
            return this.startTimeEpoch;
        }

        private String getServiceId() {
            return this.serviceId;
        }

        private String getEventId() {
            return this.eventId;
        }

        /**
         * 開始時間をエポック秒からFormatしてString値としてコンテンツデータに設定して返す
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
     * ソート条件定義クラス
     */
    private class ContentInfoComparator implements Comparator<RecordingReservationContentInfo> {
        @Override
        public int compare(RecordingReservationContentInfo infoA, RecordingReservationContentInfo infoB) {
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
}
