/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RemoteRecordingReservationMetaData implements Serializable {
    private static final long serialVersionUID = -5113796911971449210L;
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SET_DATA = ".setData";
    //仕様書の名前に合わせているので、フルスペルではない変数名もあります
    //mResvId,mResvType,mLoopTypeNum,mRValue,mSyncStatus,mSyncErrorReason
    private String mResvId;
    private int mResvType;
    private int mPlatformType;
    private String mServiceId;
    private String mEventId;
    private String mTitle;
    private long mStartTime;
    private int mDuration;
    private int mLoopTypeNum;
    private int mRValue;
    private int mSyncStatus;
    private int mSyncErrorReason;

    public static String[] getRootPara() {
        return mRootPara.clone();
    }

    public String getResvId() {
        return mResvId;
    }

    public void setResvId(final String mResvId) {
        this.mResvId = mResvId;
    }

    public int getResvType() {
        return mResvType;
    }

    public void setResvType(final int mResvType) {
        this.mResvType = mResvType;
    }

    public int getPlatformType() {
        return mPlatformType;
    }

    public void setPlatformType(final int mPlatformType) {
        this.mPlatformType = mPlatformType;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(final String mServiceId) {
        this.mServiceId = mServiceId;
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(final String mEventId) {
        this.mEventId = mEventId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(final String mTitle) {
        this.mTitle = mTitle;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(final int mStartTime) {
        this.mStartTime = mStartTime;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(final int mDuration) {
        this.mDuration = mDuration;
    }

    public int getLoopTypeNum() {
        return mLoopTypeNum;
    }

    public void setLoopTypeNum(final int mLoopTypeNum) {
        this.mLoopTypeNum = mLoopTypeNum;
    }

    public int getRValue() {
        return mRValue;
    }

    public void setRValue(final int mRValue) {
        this.mRValue = mRValue;
    }

    public int getSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(final int mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }

    public int getSyncErrorReason() {
        return mSyncErrorReason;
    }

    public void setSyncErrorReason(final int mSyncErrorReason) {
        this.mSyncErrorReason = mSyncErrorReason;
    }

    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID = "resv_id"; //予約ID
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE = "resv_type"; //１：番組指定予約
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE = "platform_type"; //プラットフォームタイプ
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID = "service_id"; //サービスID
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID = "event_id"; //イベントID
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_TITLE = "title"; //番組名
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME = "start_time"; //開始予定エポック秒
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_DURATION = "duration"; // 予約時間の長さ（秒）
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM = "loop_type_num"; //定期予約指定値、0~10
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE = "r_value"; //番組のパレンタル設定値
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS = "sync_status"; //同期状態
    private static final String REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON = "sync_error_reason"; //同期失敗理由


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
                    mResvType = DBUtils.getNumeric(data);              //resv_type
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE:
                    mPlatformType = DBUtils.getNumeric(data);          //platform_type
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
                    mStartTime = DBUtils.getLong(data);             //start_time
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_DURATION:
                    mDuration = DBUtils.getNumeric(data);              //duration
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM:
                    mLoopTypeNum = DBUtils.getNumeric(data);           //loop_type_num
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE:
                    mRValue = DBUtils.getNumeric(data);                //r_value
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS:
                    mSyncStatus = DBUtils.getNumeric(data);            //sync_status
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON:
                    mSyncErrorReason = DBUtils.getNumeric(data);       //sync_error_reason
                    break;
                default:

            }
        }
    }

    /**
     * キーの値を取得する.
     *
     * @param key キー
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
        if (jsonObj != null) {
            // 単一データ
            for (String item : mRootPara) {
                if (!jsonObj.isNull(item)) {
                    try {
                        setMember(item, jsonObj.get(item));
                    } catch (JSONException e) {
                        DTVTLogger.debug(CLASS_NAME + SET_DATA, e);
                    }
                }
            }
        }
    }
}
