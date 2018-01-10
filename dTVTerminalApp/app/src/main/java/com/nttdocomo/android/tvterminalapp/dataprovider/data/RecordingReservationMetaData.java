/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RecordingReservationMetaData implements Serializable {
    private static final long serialVersionUID = -930749879941574984L;
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SET_DATA = ".setData";
    private int mPriority = 0;
    private String mPlatformType = null;
    private String mDayOfTheWeek = null;
    private String mAdultAttributeOfChannel = null;
    private String mServiceId = null;
    private long mStartScheduleTime = 0;
    private long mEndScheduleTime = 0;
    private String mEventId = null;
    private String mTitle = null;
    private int mParentalAgeAttributeOfProgram = 0;

    public static final String[] mPagerPara = {JsonContents.META_RESPONSE_PAGER_LIMIT,
            JsonContents.META_RESPONSE_OFFSET, JsonContents.META_RESPONSE_COUNT,
            JsonContents.META_RESPONSE_TOTAL};

    public static final String[] mReservationListPara = {JsonContents.META_RESPONSE_PRIORITY,
            JsonContents.META_RESPONSE_PLATFORM_TYPE, JsonContents.META_RESPONSE_DAY_OF_THE_WEEK,
            JsonContents.META_RESPONSE_ADULT_ATTRIBUTE_OF_CHANNEL, JsonContents.META_RESPONSE_SERVICE_ID,
            JsonContents.META_RESPONSE_START_SCHEDULE_TIME, JsonContents.META_RESPONSE_END_SCHEDULE_TIME,
            JsonContents.META_RESPONSE_EVENT_ID, JsonContents.META_RESPONSE_TITLE,
            JsonContents.META_RESPONSE_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM};

    /**
     * キーとキーの値をメンバーにセットする
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(String key, Object data) {
        if (key.isEmpty()) {
            return;
        } else {
            switch (key) {
                case JsonContents.META_RESPONSE_PRIORITY:
                    mPriority = DBUtils.getNumeric(data);        //priority
                    break;
                case JsonContents.META_RESPONSE_PLATFORM_TYPE:
                    mPlatformType = (String) data;        //platform_type
                    break;
                case JsonContents.META_RESPONSE_DAY_OF_THE_WEEK:
                    mDayOfTheWeek = (String) data;        //day_of_the_week
                    break;
                case JsonContents.META_RESPONSE_ADULT_ATTRIBUTE_OF_CHANNEL:
                    mAdultAttributeOfChannel = (String) data;//adult_attribute_of_channel
                    break;
                case JsonContents.META_RESPONSE_SERVICE_ID:
                    mServiceId = (String) data;           //service_id
                    break;
                case JsonContents.META_RESPONSE_START_SCHEDULE_TIME:
                    mStartScheduleTime = DBUtils.getLong(data);  //start_schedule_time
                    break;
                case JsonContents.META_RESPONSE_END_SCHEDULE_TIME:
                    mEndScheduleTime = DBUtils.getLong(data);     //end_schedule_time
                    break;
                case JsonContents.META_RESPONSE_EVENT_ID:
                    mEventId = (String) data;             //event_id
                    break;
                case JsonContents.META_RESPONSE_TITLE:
                    mTitle = (String) data;               //title
                    break;
                case JsonContents.META_RESPONSE_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM:
                    mParentalAgeAttributeOfProgram = DBUtils.getNumeric(data); //parental_age_attribute_of_program
                    break;
                default:
            }
        }
    }

    /**
     * キーの値を取得する
     *
     * @param key キー
     */
    public Object getMember(String key) {
        if (key.isEmpty()) {
            return "";
        } else {
            switch (key) {
                case JsonContents.META_RESPONSE_PRIORITY:
                    return mPriority;            //priority
                case JsonContents.META_RESPONSE_PLATFORM_TYPE:
                    return mPlatformType;        //platform_type
                case JsonContents.META_RESPONSE_DAY_OF_THE_WEEK:
                    return mDayOfTheWeek;        //day_of_the_week
                case JsonContents.META_RESPONSE_ADULT_ATTRIBUTE_OF_CHANNEL:
                    return mAdultAttributeOfChannel;//adult_attribute_of_channel
                case JsonContents.META_RESPONSE_SERVICE_ID:
                    return mServiceId;           //service_id
                case JsonContents.META_RESPONSE_START_SCHEDULE_TIME:
                    return mStartScheduleTime;   //start_schedule_time
                case JsonContents.META_RESPONSE_END_SCHEDULE_TIME:
                    return mEndScheduleTime;     //end_schedule_time
                case JsonContents.META_RESPONSE_EVENT_ID:
                    return mEventId;             //event_id
                case JsonContents.META_RESPONSE_TITLE:
                    return mTitle;               //title
                case JsonContents.META_RESPONSE_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM:
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

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int mPriority) {
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

    public long getStartScheduleTime() {
        return mStartScheduleTime;
    }

    public void setStartScheduleTime(int mStartScheduleTime) {
        this.mStartScheduleTime = mStartScheduleTime;
    }

    public long getEndScheduleTime() {
        return mEndScheduleTime;
    }

    public void setEndScheduleTime(int mEndScheduleTime) {
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

    public int getParentalAgeAttributeOfProgram() {
        return mParentalAgeAttributeOfProgram;
    }

    public void setParentalAgeAttributeOfProgram(int mParentalAgeAttributeOfProgram) {
        this.mParentalAgeAttributeOfProgram = mParentalAgeAttributeOfProgram;
    }

    /**
     * サーバから取得したデータをセット
     *
     * @param jsonObj Jsonオブジェクト
     */
    public void setData(JSONObject jsonObj) {
        if (jsonObj != null) {
            //録画予約一覧データ
            for (String item : mReservationListPara) {
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