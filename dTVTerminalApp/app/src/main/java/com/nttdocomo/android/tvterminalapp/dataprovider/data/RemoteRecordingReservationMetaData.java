/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class RemoteRecordingReservationMetaData implements Serializable {

    private static final long serialVersionUID = -5113796911971449210L;
    private String mResv_id;
    private String mResv_type;
    private String mPlatform_type;
    private String mService_id;
    private String mEvent_id;
    private String mTitle;
    private String mStart_time;
    private String mDuration;
    private String mLoop_type_num;
    private String mR_value;
    private String mSync_status;
    private String mSync_error_reason;

    public String getmResv_id() {
        return mResv_id;
    }

    public void setmResv_id(String mResv_id) {
        this.mResv_id = mResv_id;
    }

    public String getmResv_type() {
        return mResv_type;
    }

    public void setmResv_type(String mResv_type) {
        this.mResv_type = mResv_type;
    }

    public String getmPlatform_type() {
        return mPlatform_type;
    }

    public void setmPlatform_type(String mPlatform_type) {
        this.mPlatform_type = mPlatform_type;
    }

    public String getmService_id() {
        return mService_id;
    }

    public void setmService_id(String mService_id) {
        this.mService_id = mService_id;
    }

    public String getmEvent_id() {
        return mEvent_id;
    }

    public void setmEvent_id(String mEvent_id) {
        this.mEvent_id = mEvent_id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmStart_time() {
        return mStart_time;
    }

    public void setmStart_time(String mStart_time) {
        this.mStart_time = mStart_time;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmLoop_type_num() {
        return mLoop_type_num;
    }

    public void setmLoop_type_num(String mLoop_type_num) {
        this.mLoop_type_num = mLoop_type_num;
    }

    public String getmR_value() {
        return mR_value;
    }

    public void setmR_value(String mR_value) {
        this.mR_value = mR_value;
    }

    public String getmSync_status() {
        return mSync_status;
    }

    public void setmSync_status(String mSync_status) {
        this.mSync_status = mSync_status;
    }

    public String getmSync_error_reason() {
        return mSync_error_reason;
    }

    public void setmSync_error_reason(String mSync_error_reason) {
        this.mSync_error_reason = mSync_error_reason;
    }

    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID = "resv_id"; //予約ID
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE = "resv_type"; //１：番組指定予約
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE = "platform_type"; //プラットフォームタイプ 1：多チャンネル放送
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID = "service_id";
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID = "event_id";
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_TITLE = "title";
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME = "start_time";
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_DURATION = "duration";
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM = "loop_type_num";
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE = "r_value";
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS = "sync_status";
    public static final String REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON = "sync_error_reason";


    public static final String[] mRootPara = {REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID, REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE,
            REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE, REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID, REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID,
            REMOTE_RECORDING_RESERVATION_META_DATA_TITLE, REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME, REMOTE_RECORDING_RESERVATION_META_DATA_DURATION,
            REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM, REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE, REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS, REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON};

    private void setMember(String key, Object data) {
        if (key.isEmpty()) {
            return;
        } else {
            switch (key) {
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_ID:
                    mResv_id = (String) data;                //resv_id
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_TYPE:
                    mResv_type = (String) data;             //resv_type
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_RESV_PLATFORM_TYPE:
                    mPlatform_type = (String) data;             //platform_type
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SERVICE_ID:
                    mService_id = (String) data;
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_EVENT_ID:
                    mEvent_id = (String) data;
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_TITLE:
                    mTitle = (String) data;
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_START_TIME:
                    mStart_time = (String) data;
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_DURATION:
                    mDuration = (String) data;
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_LOOP_TYPE_NUM:
                    mLoop_type_num = (String) data;
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_R_VALUE:
                    mR_value = (String) data;
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_STATUS:
                    mSync_status = (String) data;
                    break;
                case REMOTE_RECORDING_RESERVATION_META_DATA_SYNC_ERROR_REASON:
                    mSync_error_reason = (String) data;
                    break;
                default:
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
