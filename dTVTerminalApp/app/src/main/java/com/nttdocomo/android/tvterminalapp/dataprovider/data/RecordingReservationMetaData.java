/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RecordingReservationMetaData implements Serializable {

    private static final long serialVersionUID = -930749879941574984L;
    private String mLimit;
    private String mOffset;
    private String mCount;
    private String mTotal;
    private String mPriority;
    private String mPlatformType;
    private String mDayOfTheWeek;
    private String mAdultAttributeOfChannel;
    private String mServiceId;
    private String mStartScheduleTime;
    private String mEndScheduleTime;
    private String mEventId;
    private String mTitle;
    private String mParentalAgeAttributeOfProgram;

    public static final String RECORDING_RESERVATION_META_DATA_PAGER_LIMIT = "limit"; //レスポンスの最大件数
    public static final String RECORDING_RESERVATION_META_DATA_PAGER_OFFSET = "offset"; //取得位置(0～)
    public static final String RECORDING_RESERVATION_META_DATA_PAGER_COUNT = "count"; //レスポンス(list)件数
    public static final String RECORDING_RESERVATION_META_DATA_PAGER_TOTAL = "total"; //全体の件数

    public static final String RECORDING_RESERVATION_META_DATA_PRIORITY = "priority"; //予約情報の優先度
    public static final String RECORDING_RESERVATION_META_DATA_PLATFORM_TYPE = "platform_type"; //予約されたサービス
    public static final String RECORDING_RESERVATION_META_DATA_DAY_OF_THE_WEEK = "day_of_the_week"; //予約日時タイプ
    public static final String RECORDING_RESERVATION_META_DATA_ADULT_ATTRIBUTE_OF_CHANNEL = "adult_attribute_of_channel";//アダルトタイプ
    public static final String RECORDING_RESERVATION_META_DATA_SERVICE_ID = "service_id"; //サービスID（チャンネルを一意に識別するもの）
    public static final String RECORDING_RESERVATION_META_DATA_START_SCHEDULE_TIME = "start_schedule_time";//予約の録画開始予定時刻
    public static final String RECORDING_RESERVATION_META_DATA_END_SCHEDULE_TIME = "end_schedule_time";//予約の録画終了予定時刻
    public static final String RECORDING_RESERVATION_META_DATA_EVENT_ID = "event_id"; //イベントID
    public static final String RECORDING_RESERVATION_META_DATA_TITLE = "title";//ユーザが指定した予約のタイトル
    public static final String RECORDING_RESERVATION_META_DATA_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM = "parental_age_attribute_of_program";//番組のPG値

    public static final String[] mPagerPara = {RECORDING_RESERVATION_META_DATA_PAGER_LIMIT,
            RECORDING_RESERVATION_META_DATA_PAGER_OFFSET, RECORDING_RESERVATION_META_DATA_PAGER_COUNT,
            RECORDING_RESERVATION_META_DATA_PAGER_TOTAL};
    public static final String[] mReservationListPara = {RECORDING_RESERVATION_META_DATA_PRIORITY,
            RECORDING_RESERVATION_META_DATA_PLATFORM_TYPE, RECORDING_RESERVATION_META_DATA_DAY_OF_THE_WEEK,
            RECORDING_RESERVATION_META_DATA_ADULT_ATTRIBUTE_OF_CHANNEL, RECORDING_RESERVATION_META_DATA_SERVICE_ID,
            RECORDING_RESERVATION_META_DATA_START_SCHEDULE_TIME, RECORDING_RESERVATION_META_DATA_END_SCHEDULE_TIME,
            RECORDING_RESERVATION_META_DATA_EVENT_ID, RECORDING_RESERVATION_META_DATA_TITLE,
            RECORDING_RESERVATION_META_DATA_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM};

    private void setMember(String key, Object data) {
        if (key.isEmpty()) {
            return;
        } else {
            switch (key) {
                case RECORDING_RESERVATION_META_DATA_PAGER_LIMIT:
                    mLimit = (String) data;                //limit
                    break;
                case RECORDING_RESERVATION_META_DATA_PAGER_OFFSET:
                    mOffset = (String) data;              //offset
                    break;
                case RECORDING_RESERVATION_META_DATA_PAGER_COUNT:
                    mCount = (String) data;               //count
                    break;
                case RECORDING_RESERVATION_META_DATA_PAGER_TOTAL:
                    mTotal = (String) data;               //total
                    break;
                case RECORDING_RESERVATION_META_DATA_PRIORITY:
                    mPriority = (String) data;            //priority
                    break;
                case RECORDING_RESERVATION_META_DATA_PLATFORM_TYPE:
                    mPlatformType = (String) data;        //platform_type
                    break;
                case RECORDING_RESERVATION_META_DATA_DAY_OF_THE_WEEK:
                    mDayOfTheWeek = (String) data;        //day_of_the_week
                    break;
                case RECORDING_RESERVATION_META_DATA_ADULT_ATTRIBUTE_OF_CHANNEL:
                    mAdultAttributeOfChannel = (String) data;//adult_attribute_of_channel
                    break;
                case RECORDING_RESERVATION_META_DATA_SERVICE_ID:
                    mServiceId = (String) data;           //service_id
                    break;
                case RECORDING_RESERVATION_META_DATA_START_SCHEDULE_TIME:
                    mStartScheduleTime = (String) data;   //start_schedule_time
                    break;
                case RECORDING_RESERVATION_META_DATA_END_SCHEDULE_TIME:
                    mEndScheduleTime = (String) data;     //end_schedule_time
                    break;
                case RECORDING_RESERVATION_META_DATA_EVENT_ID:
                    mEventId = (String) data;             //event_id
                    break;
                case RECORDING_RESERVATION_META_DATA_TITLE:
                    mTitle = (String) data;               //title
                    break;
                case RECORDING_RESERVATION_META_DATA_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM:
                    mParentalAgeAttributeOfProgram = (String) data; //parental_age_attribute_of_program
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
                case RECORDING_RESERVATION_META_DATA_PAGER_LIMIT:
                    return mLimit;                //limit
                case RECORDING_RESERVATION_META_DATA_PAGER_OFFSET:
                    return mOffset;              //offset
                case RECORDING_RESERVATION_META_DATA_PAGER_COUNT:
                    return mCount;              //count
                case RECORDING_RESERVATION_META_DATA_PAGER_TOTAL:
                    return mTotal;               //total
                case RECORDING_RESERVATION_META_DATA_PRIORITY:
                    return mPriority;            //priority
                case RECORDING_RESERVATION_META_DATA_PLATFORM_TYPE:
                    return mPlatformType;        //platform_type
                case RECORDING_RESERVATION_META_DATA_DAY_OF_THE_WEEK:
                    return mDayOfTheWeek;        //day_of_the_week
                case RECORDING_RESERVATION_META_DATA_ADULT_ATTRIBUTE_OF_CHANNEL:
                    return mAdultAttributeOfChannel;//adult_attribute_of_channel
                case RECORDING_RESERVATION_META_DATA_SERVICE_ID:
                    return mServiceId;           //service_id
                case RECORDING_RESERVATION_META_DATA_START_SCHEDULE_TIME:
                    return mStartScheduleTime;   //start_schedule_time
                case RECORDING_RESERVATION_META_DATA_END_SCHEDULE_TIME:
                    return mEndScheduleTime;     //end_schedule_time
                case RECORDING_RESERVATION_META_DATA_EVENT_ID:
                    return mEventId;             //event_id
                case RECORDING_RESERVATION_META_DATA_TITLE:
                    return mTitle;               //title
                case RECORDING_RESERVATION_META_DATA_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM:
                    return mParentalAgeAttributeOfProgram; //parental_age_attribute_of_program
                default:
                    return "";
            }
        }
    }

    public static String[] getPagerPara() {
        return mPagerPara;
    }

    public static String[] getReservationListPara() {
        return mReservationListPara;
    }

    public String getLimit() {
        return mLimit;
    }

    public void setLimit(String mLimit) {
        this.mLimit = mLimit;
    }

    public String getOffset() {
        return mOffset;
    }

    public void setOffset(String mOffset) {
        this.mOffset = mOffset;
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String mCount) {
        this.mCount = mCount;
    }

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(String mTotal) {
        this.mTotal = mTotal;
    }

    public String getPriority() {
        return mPriority;
    }

    public void setPriority(String mPriority) {
        this.mPriority = mPriority;
    }

    public String getPlatformType() {
        return mPlatformType;
    }

    public void setPlatformType(String mPlatformType) {
        this.mPlatformType = mPlatformType;
    }

    public String getDayOfTheWeek() {
        return mDayOfTheWeek;
    }

    public void setDayOfTheWeek(String mDayOfTheWeek) {
        this.mDayOfTheWeek = mDayOfTheWeek;
    }

    public String getAdultAttributeOfChannel() {
        return mAdultAttributeOfChannel;
    }

    public void setAdultAttributeOfChannel(String mAdultAttributeOfChannel) {
        this.mAdultAttributeOfChannel = mAdultAttributeOfChannel;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(String mServiceId) {
        this.mServiceId = mServiceId;
    }

    public String getStartScheduleTime() {
        return mStartScheduleTime;
    }

    public void setStartScheduleTime(String mStartScheduleTime) {
        this.mStartScheduleTime = mStartScheduleTime;
    }

    public String getEndScheduleTime() {
        return mEndScheduleTime;
    }

    public void setEndScheduleTime(String mEndScheduleTime) {
        this.mEndScheduleTime = mEndScheduleTime;
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

    public String getParentalAgeAttributeOfProgram() {
        return mParentalAgeAttributeOfProgram;
    }

    public void setParentalAgeAttributeOfProgram(String mParentalAgeAttributeOfProgram) {
        this.mParentalAgeAttributeOfProgram = mParentalAgeAttributeOfProgram;
    }

    public void setData(JSONObject jsonObj) {
        if (jsonObj != null) {
            // ページャデータ
            for (String item : mPagerPara) {
                if (!jsonObj.isNull(item)) {
                    try {
                        setMember(item, jsonObj.get(item));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            //
            for (String item : mReservationListPara) {
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
