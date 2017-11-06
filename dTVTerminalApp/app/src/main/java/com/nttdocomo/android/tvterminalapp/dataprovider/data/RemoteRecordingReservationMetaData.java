/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class RemoteRecordingReservationMetaData implements Serializable {

    private static final long serialVersionUID = -5113796911971449210L;
    //仕様書の名前に合わせているので、フルスペルではない変数名もあります
    //mResvId,mResvType,mLoopTypeNum,mRValue,mSyncStatus,mSyncErrorReason
    private String mResvId;
    private String mResvType;
    private String mPlatformType;
    private String mServiceId;
    private String mEventId;
    private String mTitle;
    private String mStartTime;
    private String mDuration;
    private String mLoopTypeNum;
    private String mRValue;
    private String mSyncStatus;
    private String mSyncErrorReason;

    public static String[] getRootPara() {
        return mRootPara;
    }

    public String getResvId() {
        return mResvId;
    }

    public void setResvId(String mResvId) {
        this.mResvId = mResvId;
    }

    public String getResvType() {
        return mResvType;
    }

    public void setResvType(String mResvType) {
        this.mResvType = mResvType;
    }

    public String getPlatformType() {
        return mPlatformType;
    }

    public void setPlatformType(String mPlatformType) {
        this.mPlatformType = mPlatformType;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(String mServiceId) {
        this.mServiceId = mServiceId;
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(String mEventId) {
        this.mEventId = mEventId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getLoopTypeNum() {
        return mLoopTypeNum;
    }

    public void setLoopTypeNum(String mLoopTypeNum) {
        this.mLoopTypeNum = mLoopTypeNum;
    }

    public String getRValue() {
        return mRValue;
    }

    public void setRValue(String mRValue) {
        this.mRValue = mRValue;
    }

    public String getSyncStatus() {
        return mSyncStatus;
    }

    public void setSyncStatus(String mSyncStatus) {
        this.mSyncStatus = mSyncStatus;
    }

    public String getSyncErrorReason() {
        return mSyncErrorReason;
    }

    public void setSyncErrorReason(String mSyncErrorReason) {
        this.mSyncErrorReason = mSyncErrorReason;
    }

    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID = "resv_id"; //予約ID
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE = "resv_type"; //１：番組指定予約
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE = "platform_type"; //プラットフォームタイプ
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID = "service_id"; //サービスID
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID = "event_id"; //イベントID
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_TITLE = "title"; //番組名
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME = "start_time"; //開始予定エポック秒
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_DURATION = "duration"; // 予約時間の長さ（秒）
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM = "loop_type_num"; //定期予約指定値、0~10
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE = "r_value"; //番組のパレンタル設定値
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS = "sync_status"; //同期状態
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON = "sync_error_reason"; //同期失敗理由


    public static final String[] mRootPara = {REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID,
            REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE, REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE,
            REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID, REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID,
            REMOTE_RECORDING_RESERVATION_META_DATA_TITLE, REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME,
            REMOTE_RECORDING_RESERVATION_META_DATA_DURATION, REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM,
            REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE, REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS,
            REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON};

    private void setMember(String key, Object data) {
        if (key.isEmpty()) {
            return;
        } else {
            switch (key) {
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID:
                    mResvId = (String) data;                //resv_id
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE:
                    mResvType = (String) data;              //resv_type
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE:
                    mPlatformType = (String) data;          //platform_type
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID:
                    mServiceId = (String) data;             //service_id
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID:
                    mEventId = (String) data;               //event_id
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_TITLE:
                    mTitle = (String) data;                 //title
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME:
                    mStartTime = (String) data;             //start_time
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_DURATION:
                    mDuration = (String) data;              //duration
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM:
                    mLoopTypeNum = (String) data;           //loop_type_num
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE:
                    mRValue = (String) data;                //r_value
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS:
                    mSyncStatus = (String) data;            //sync_status
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON:
                    mSyncErrorReason = (String) data;       //sync_error_reason
                    break;
                default:

            }
        }
    }

    public Object getMember(String key) {
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

    public void setData(JSONObject jsonObj) {
        if (jsonObj != null) {
            // 単一データ
            for (String item : mRootPara) {
                if (!jsonObj.isNull(item)) {
                    try {
                        setMember(item, jsonObj.get(item));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
