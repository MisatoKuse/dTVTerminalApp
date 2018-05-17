/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * リモート録画予約メタデータ.
 */
public class RemoteRecordingReservationMetaData implements Serializable {
    private static final long serialVersionUID = -5113796911971449210L;
    //仕様書の名前に合わせているので、フルスペルではない変数名もあります
    //mResvId,mResvType,mLoopTypeNum,mRValue,mSyncStatus,mSyncErrorReason
    /**予約ID.*/
    private String mResvId;
    /**番組指定予約.*/
    private int mResvType;
    /**プラットフォームタイプ.*/
    private int mPlatformType;
    /**サービスID.*/
    private String mServiceId;
    /**イベントID.*/
    private String mEventId;
    /**タイトル.*/
    private String mTitle;
    /**開始予定エポック秒.*/
    private long mStartTime;
    /**予約時間の長さ（秒）.*/
    private int mDuration;
    /**定期予約指定値、0~10.*/
    private int mLoopTypeNum;
    /**番組のパレンタル設定値.*/
    private int mRValue;
    /**同期状態.*/
    private int mSyncStatus;
    /**同期失敗理由.*/
    private int mSyncErrorReason;
    /**
     * 単一データパラメータ取得.
     * @return 単一データパラメータ
     */
    public static String[] getRootPara() {
        return mRootPara.clone();
    }
    /**
     * 予約ID取得.
     * @return 予約ID
     */
    public String getResvId() {
        return mResvId;
    }

    /**
     * 予約ID設定.
     * @param mResvId 予約ID
     */
    public void setResvId(final String mResvId) {
        this.mResvId = mResvId;
    }

    /**
     * 番組指定予約取得.
     * @return  番組指定予約
     */
    public int getResvType() {
        return mResvType;
    }

    /**
     * 番組指定予約設定.
     * @param mResvType 番組指定予約
     */
    public void setResvType(final int mResvType) {
        this.mResvType = mResvType;
    }

    /**
     * プラットフォームタイプ取得.
     * @return プラットフォームタイプ
     */
    public int getPlatformType() {
        return mPlatformType;
    }

    /**
     * プラットフォームタイプ設定.
     * @param mPlatformType プラットフォームタイプ
     */
    public void setPlatformType(final int mPlatformType) {
        this.mPlatformType = mPlatformType;
    }

    /**
     * サービスID取得.
     * @return サービスID
     */
    public String getServiceId() {
        return mServiceId;
    }

    /**
     * サービスID設定.
     * @param mServiceId サービスID
     */
    public void setServiceId(final String mServiceId) {
        this.mServiceId = mServiceId;
    }

    /**
     * イベントID取得.
     * @return イベントID
     */
    public String getEventId() {
        return mEventId;
    }

    /**
     * イベントID設定.
     * @param mEventId イベントID
     */
    public void setEventId(final String mEventId) {
        this.mEventId = mEventId;
    }

    /**
     * タイトル取得.
     * @return タイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * タイトル設定.
     * @param mTitle タイトル
     */
    public void setTitle(final String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * 開始予定エポック秒取得.
     * @return 開始予定エポック秒
     */
    public long getStartTime() {
        return mStartTime;
    }

    /**
     * 開始予定エポック秒設定.
     * @param mStartTime 開始予定エポック秒
     */
    public void setStartTime(final int mStartTime) {
        this.mStartTime = mStartTime;
    }

    /**
     * 予約時間の長さ（秒）取得.
     * @return 予約時間の長さ（秒）
     */
    public int getDuration() {
        return mDuration;
    }

    /**
     * 予約時間の長さ（秒）設定.
     * @param mDuration 予約時間の長さ（秒）
     */
    public void setDuration(final int mDuration) {
        this.mDuration = mDuration;
    }

    /**
     * 定期予約指定値、0~10取得.
     * @return 期予約指定値、0~10
     */
    public int getLoopTypeNum() {
        return mLoopTypeNum;
    }

    /**
     * 期予約指定値、0~10設定.
     * @param mLoopTypeNum 期予約指定値、0~10
     */
    public void setLoopTypeNum(final int mLoopTypeNum) {
        this.mLoopTypeNum = mLoopTypeNum;
    }

    /**
     * 番組のパレンタル設定値取得.
     * @return 番組のパレンタル設定値
     */
    public int getRValue() {
        return mRValue;
    }

    /**
     * 番組のパレンタル設定値設定.
     * @param mRValue 番組のパレンタル設定値
     */
    public void setRValue(final int mRValue) {
        this.mRValue = mRValue;
    }

    /**
     * 同期状態取得.
     * @return 同期状態
     */
    public int getSyncStatus() {
        return mSyncStatus;
    }

    /**
     * 同期状態設定.
     * @param mSyncStatus 同期状態
     */
    public void setSyncStatus(final int mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }

    /**
     * 同期失敗理由取得.
     * @return 同期失敗理由
     */
    public int getSyncErrorReason() {
        return mSyncErrorReason;
    }

    /**
     * 同期失敗理由設定.
     * @param mSyncErrorReason 同期失敗理由
     */
    public void setSyncErrorReason(final int mSyncErrorReason) {
        this.mSyncErrorReason = mSyncErrorReason;
    }
    /**予約IDキー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID = "resv_id"; //予約ID
    /**番組指定予約キー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE = "resv_type"; //１：番組指定予約
    /**プラットフォームタイプ.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE = "platform_type"; //プラットフォームタイプ
    /**サービスIDキー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID = "service_id"; //サービスID
    /**イベントIDキー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID = "event_id"; //イベントID
    /**番組名キー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_TITLE = "title"; //番組名
    /**開始予定エポック秒キー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME = "start_time"; //開始予定エポック秒
    /**予約時間の長さ（秒）キー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_DURATION = "duration"; // 予約時間の長さ（秒）
    /**定期予約指定値、0~10キー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM = "loop_type_num"; //定期予約指定値、0~10
    /**番組のパレンタル設定値キー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE = "r_value"; //番組のパレンタル設定値
    /**同期状態キー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS = "sync_status"; //同期状態
    /**同期失敗理由キー.*/
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON = "sync_error_reason"; //同期失敗理由

    /**単一データキー.*/
    private static final String[] mRootPara = {REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID,
            REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE, REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE,
            REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID, REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID,
            REMOTE_RECORDING_RESERVATION_META_DATA_TITLE, REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME,
            REMOTE_RECORDING_RESERVATION_META_DATA_DURATION, REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM,
            REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE, REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS,
            REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON};

    /**
     * キーとキーの値をメンバーにセットする.
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(final String key, final Object data) {
        if (!key.isEmpty()) {
            switch (key) {
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID:
                    mResvId = (String) data;                //resv_id
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE:
                    mResvType = DataBaseUtils.getNumeric(data);              //resv_type
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE:
                    mPlatformType = DataBaseUtils.getNumeric(data);          //platform_type
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID:
                    mServiceId = String.valueOf(data);             //service_id
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID:
                    mEventId = (String) data;               //event_id
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_TITLE:
                    mTitle = (String) data;                 //title
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME:
                    mStartTime = DataBaseUtils.getLong(data);             //start_time
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_DURATION:
                    mDuration = DataBaseUtils.getNumeric(data);              //duration
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM:
                    mLoopTypeNum = DataBaseUtils.getNumeric(data);           //loop_type_num
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE:
                    mRValue = DataBaseUtils.getNumeric(data);                //r_value
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS:
                    mSyncStatus = DataBaseUtils.getNumeric(data);            //sync_status
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON:
                    mSyncErrorReason = DataBaseUtils.getNumeric(data);       //sync_error_reason
                    break;
                default:

            }
        }
    }

    /**
     * キーの値を取得する.
     *
     * @param key キー
     * @return キー
     */
    public Object getMember(final String key) {
        if (key.isEmpty()) {
            return "";
        } else {
            switch (key) {
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID:
                    return mResvId;                //resv_id
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE:
                    return mResvType;              //resv_type
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE:
                    return mPlatformType;          //platform_type
                case REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID:
                    return mServiceId;             //service_id
                case REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID:
                    return mEventId;               //event_id
                case REMOTE_RECORDING_RESERVATION_META_DATA_TITLE:
                    return mTitle;                 //title
                case REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME:
                    return mStartTime;             //start_time
                case REMOTE_RECORDING_RESERVATION_META_DATA_DURATION:
                    return mDuration;              //duration
                case REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM:
                    return mLoopTypeNum;           //loop_type_num
                case REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE:
                    return mRValue;                //r_value
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS:
                    return mSyncStatus;            //sync_status
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON:
                    return mSyncErrorReason;       //sync_error_reason
                default:
                    return "";
            }
        }
    }

    /**
     * サーバから取得したデータをセット.
     *
     * @param jsonObj Jsonオブジェクト
     */
    public void setData(final JSONObject jsonObj) {
        DTVTLogger.start();
        if (jsonObj != null) {
            // 単一データ
            for (String item : mRootPara) {
                if (!jsonObj.isNull(item)) {
                    try {
                        setMember(item, jsonObj.get(item));
                    } catch (JSONException e) {
                        DTVTLogger.end();
                    }
                }
            }
        }
    }
}
