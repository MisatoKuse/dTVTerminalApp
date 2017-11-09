/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.util.SparseArray;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordingReservationMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RemoteRecordingReservationMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RecordingReservationListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RemoteRecordingReservationListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.TvScheduleJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.TvSchedule_LAST_INSERT;

// TODO implement WebApiコールバック実装
public class RecordingReservationListDataProvider implements
        RemoteRecordingReservationListWebClient.RemoteRecordingReservationListJsonParserCallback,
        RecordingReservationListWebClient.RecordingReservationListJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback {

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

    public enum ReservationType {
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
        if (response != null) {
            mStbResponse = response;
            // CH一覧取得,リモート側との同期
            if (mStbResponse != null && mTvScheduleList != null && mTvScheduleList.size() != 0) {
                apiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
            }
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onRecordingReservationListJsonParsed(RecordingReservationListResponse response) {
        if (response != null) {
            mDRemoteResponse = response;
            // CH一覧取得,STB側との同期
            if (mStbResponse != null && mTvScheduleList != null && mTvScheduleList.size() != 0) {
                apiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
            }
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onTvScheduleJsonParsed(List<TvScheduleList> tvScheduleList) {
        if (tvScheduleList != null && tvScheduleList.size() > 0) {
            TvScheduleList list = tvScheduleList.get(0);
            mTvScheduleList = list.geTvsList();
            // 録画予約一覧取得との同期
            if (mStbResponse != null && mStbResponse != null) {
                apiDataProviderCallback.recordingReservationListCallback(mRecordingReservationList);
                setStructDB(list);
            }
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    /**
     * WebApiからのコールバックデータを返却するためのActivity実装用コールバック
     */
    public interface ApiDataProviderCallback {

        /**
         * 録画予約一覧返却用コールバック
         *
         * @param list
         */
        void recordingReservationListCallback(List<ContentsData> list);
    }

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public RecordingReservationListDataProvider(Context mContext) {
        this.mContext = mContext;
        this.apiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * Activityからのデータ取得要求受付
     */
    public void requestRecordingReservationListData() {
        getRecordingReservationListData();
    }

    /**
     * 録画予約一覧を取得する
     */
    private void getRecordingReservationListData() {
        // STB側録画予約一覧取得要求
        RemoteRecordingReservationListWebClient remoteWebClient = new RemoteRecordingReservationListWebClient();
        remoteWebClient.getRemoteRecordingReservationListApi(this);

        // dリモート側録画予約一覧取得要求
        RecordingReservationListWebClient stbWebClient = new RecordingReservationListWebClient();
        int limit = 0;
        int offset = 0;
        stbWebClient.getRecordingReservationListApi(limit, offset, this);

        // チャンネル一覧取得
        getTvScheduleListData();

    }

    /**
     * CH一覧情報取得
     */
    private void getTvScheduleListData() {
        List<Map<String, String>> list = new ArrayList<>();
        // チャンネル一覧（NO ON AIR一覧）のDB保存履歴と、有効期間を確認
        //データをDBから取得する
        HomeDataManager homeDataManager = new HomeDataManager(mContext);
        list = homeDataManager.selectTvScheduleListHomeData();
        if (list == null || list.size() <= 0) {
            //通信クラスにデータ取得要求を出す
            TvScheduleWebClient webClient = new TvScheduleWebClient();
            int[] ageReq = {1};
            String[] upperPageLimit = {WebApiBasePlala.DATE_NOW};
            String lowerPageLimit = "";
            webClient.getTvScheduleApi(ageReq, upperPageLimit,
                    lowerPageLimit, this);
        } else {
            mTvScheduleList = list;
        }

    }

    /**
     * ContentsDataを生成(STB)
     *
     * @param data
     * @return
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
     * @param data
     * @return
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
        mBuffMatchListMap = new SparseArray<List<RecordingReservationContentInfo>>();
        List<RemoteRecordingReservationMetaData> stbList = mStbResponse.getRemoteRecordingReservationMetaData();
        List<RecordingReservationMetaData> dRemoteList = mDRemoteResponse.getRecordingReservationMetaData();
        for (RemoteRecordingReservationMetaData stbData : stbList) {
            int j = 0;
            for (RecordingReservationMetaData dRemoteData : dRemoteList) {
                if (stbData.getServiceId().equals(dRemoteData.getServiceId())
                        && stbData.getEventId().equals(dRemoteData.getEventId())) {
                    setRecordingReservationContentInfo(dRemoteData);
                    break;
                }
            }
        }
        SparseArray<List<RecordingReservationContentInfo>> sparseArray = mBuffMatchListMap;
        boolean matchFlag = false;
        // STBが側データを格納
        for (RemoteRecordingReservationMetaData stbData : stbList) {
            matchFlag = false;
            for (int i = 0; i < sparseArray.size(); i++) {
                List<RecordingReservationContentInfo> list = sparseArray.get(i, null);
                if (list != null && list.size() != 0) {
                    for (RecordingReservationContentInfo info : list) {
                        if (stbData.getServiceId().equals(info.getServiceId())
                                && stbData.getEventId().equals(info.getEventId())) {
                            matchFlag = true;
                        }
                    }
                }
            }
            if (!matchFlag) {
                setRecordingReservationContentInfo(stbData);
            }
        }
        // dリモート側データを格納
        for (RecordingReservationMetaData dRemoteData : dRemoteList) {
            matchFlag = false;
            for (int i = 0; i < sparseArray.size(); i++) {
                List<RecordingReservationContentInfo> list = sparseArray.get(i, null);
                if (list != null && list.size() != 0) {
                    for (RecordingReservationContentInfo info : list) {
                        if (dRemoteData.getServiceId().equals(info.getServiceId())
                                && dRemoteData.getEventId().equals(info.getEventId())) {
                            matchFlag = true;
                        }
                    }
                }
            }
            if (!matchFlag) {
                setRecordingReservationContentInfo(dRemoteData);
            }
        }
    }

    /**
     * STB側レスポンスデータをRecordingReservationContentInfoに設定
     *
     * @param data
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
     * @param data
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
     * @param startTime
     * @param info
     */
    private void putBuffMatchListMapSingle(long startTime, RecordingReservationContentInfo info) {
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
    }

    /**
     * 単発録画予約以外のレスポンスに対して日付差毎のkey値を算出してMapにputする
     *
     * @param loopTypeNum
     * @param startTime
     * @param info
     */
    private void putBuffMatchListMap(int loopTypeNum, long startTime, RecordingReservationContentInfo info) {
        // 現在日の曜日
        int todayDayOfWeek = DateUtils.getTodayDayOfWeek();
        // 現在日時（エポック秒：秒単位）
        long nowTimeEpoch = DateUtils.getNowTimeFormatEpoch();
        // レスポンスデータの日時（エポック秒：秒単位）
        long dataTimeEpoch = DateUtils.getTodayStartTimeFormatEpoch() + startTime;
        int key = 0;
        switch (loopTypeNum) {
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_MONDAY: // 毎週月曜日
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_MONDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_TUESDAY: // 毎週火曜日
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_TUESDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_WEDNESDAY: // 毎週水曜日
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_WEDNESDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_THURSDAY: // 毎週木曜日
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_THURSDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_FRIDAY: // 毎週金曜日
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SATURDAY: // 毎週土曜日
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_SUNDAY: // 毎週日曜日
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SUNDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_SEVEN_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_MON_FRI: // 月曜日～金曜日
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
                if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                    key = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                } else {
                    key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                }
            case RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_THU: // 日曜日～木曜日
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_THURSDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_THREE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY
                        || todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    key = DateUtils.getNumberOfDaysUntilMonday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_SUN_FRI: // 日曜日～金曜日
                if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_FRIDAY) {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_TWO_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                } else if (todayDayOfWeek == DateUtils.DAY_OF_WEEK_SATURDAY) {
                    key = DateUtils.getNumberOfDaysUntilMonday(todayDayOfWeek);
                } else {
                    if (buffStartTime(nowTimeEpoch, dataTimeEpoch)) {
                        key = RECORD_RESERVATION_MAP_INDEX_ONE_DAY_LATER;
                    } else {
                        key = RECORD_RESERVATION_MAP_INDEX_TODAY;
                    }
                }
                break;
            case RECORD_RESERVATION_LOOP_TYPE_NUM_EV_MONTH: // 毎月
                // TODO QA中 DREM-602　毎月の場合の開始時間のフォーマット
                break;
            default:
                return;
        }
        dataTimeEpoch = dataTimeEpoch + (CONVERSION_ONE_DAY_TO_SEC * key);
        info.setStartTimeEpoch(dataTimeEpoch);
        List<RecordingReservationContentInfo> infoList = getRecordingReservationContentInfoList(key);
        infoList.add(info);
        mBuffMatchListMap.put(key, infoList);
    }

    /**
     * key値を元にSparseArrayからリストデータを取得
     *
     * @param key
     * @return
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

    // TODO データのソート（開始時刻順）
    private void sortRecordingReservationListData(List<ContentsData> beforeList) {
        mRecordingReservationList = new ArrayList<ContentsData>();
        mRecordingReservationList.add(beforeList.get(0));
        int i = 0;
        for (ContentsData data : beforeList) {
//            if(data.getTime() < mRecordingReservationList.get(i).getTime()) {
//
//            }
        }
    }

    /**
     *  次回予約時間を エポック秒 に変換かつString値に変換
     */
    // TODO Utilに実装の可能性あり
//    public static String getNextRecordingTime() {
//        //現在日時を取得
//        Calendar now = Calendar.getInstance();
//
//        return null;
//    }

    /**
     * CH一覧のレスポンスからチャンネル名を取得
     *
     * @param serviceId
     * @return
     */
    private String getChannelName(String serviceId) {
        String channelName = null;
        for (Map<String, String> map : mTvScheduleList) {
            if (map.get(TvScheduleJsonParser.TV_SCHEDULE_LIST_SERVICE_ID).equals(serviceId)) {
                channelName = map.get(TvScheduleJsonParser.TV_SCHEDULE_LIST_TITLE);
                break;
            }
        }
        return channelName;
    }

    /**
     * チャンネル一覧データをDBに格納する
     *
     * @param tvScheduleList
     */
    public void setStructDB(TvScheduleList tvScheduleList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(TvSchedule_LAST_INSERT);
        TvScheduleInsertDataManager dataManager = new TvScheduleInsertDataManager(mContext);
        dataManager.insertTvScheduleInsertList(tvScheduleList);

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

        public void setContentsData(ContentsData contentsData) {
            this.contentsData = contentsData;
        }

        public void setStartTimeEpoch(long epochTime) {
            this.startTimeEpoch = epochTime;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public long getStartTimeEpoch() {
            return this.startTimeEpoch;
        }

        public String getServiceId() {
            return this.serviceId;
        }

        public String getEventId() {
            return this.eventId;
        }

        public ContentsData returnContentsData() {
            ContentsData contentsData = new ContentsData();
            contentsData = this.contentsData;
            contentsData.setTime(DateUtils.formatEpochToString(this.startTimeEpoch));
            return contentsData;
        }
    }
}
