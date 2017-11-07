/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;


import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RecordingReservationMetaData implements Serializable {

    private static final long serialVersionUID = -930749879941574984L;
    private int mLimit;
    private int mOffset;
    private int mCount;
    private int mTotal;
    private int mPriority;
    private String mPlatformType;
    private String mDayOfTheWeek;
    private String mAdultAttributeOfChannel;
    private String mServiceId;
    private long mStartScheduleTime;
    private long mEndScheduleTime;
    private String mEventId;
    private String mTitle;
    private int mParentalAgeAttributeOfProgram;
    //ページャ詳細：キー名
    public static final String RECORDING_RESERVATION_META_DATA_PAGER_LIMIT = "limit"; //レスポンスの最大件数
    public static final String RECORDING_RESERVATION_META_DATA_PAGER_OFFSET = "offset"; //取得位置(0～)
    public static final String RECORDING_RESERVATION_META_DATA_PAGER_COUNT = "count"; //レスポンス(list)件数
    public static final String RECORDING_RESERVATION_META_DATA_PAGER_TOTAL = "total"; //全体の件数
    //録画予約一覧リスト：キー名
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
                case RECORDING_RESERVATION_META_DATA_PAGER_LIMIT:
                    mLimit = DBUtils.getNumeric(data);           //limit
                    break;
                case RECORDING_RESERVATION_META_DATA_PAGER_OFFSET:
                    mOffset = DBUtils.getNumeric(data);         //offset
                    break;
                case RECORDING_RESERVATION_META_DATA_PAGER_COUNT:
                    mCount = DBUtils.getNumeric(data);          //count
                    break;
                case RECORDING_RESERVATION_META_DATA_PAGER_TOTAL:
                    mTotal = DBUtils.getNumeric(data);           //total
                    break;
                case RECORDING_RESERVATION_META_DATA_PRIORITY:
                    mPriority = DBUtils.getNumeric(data);        //priority
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
                    mStartScheduleTime = (long)data;   //start_schedule_time
                    break;
                case RECORDING_RESERVATION_META_DATA_END_SCHEDULE_TIME:
                    mEndScheduleTime =  (long)data;     //end_schedule_time
                    break;
                case RECORDING_RESERVATION_META_DATA_EVENT_ID:
                    mEventId = (String) data;             //event_id
                    break;
                case RECORDING_RESERVATION_META_DATA_TITLE:
                    mTitle = (String) data;               //title
                    break;
                case RECORDING_RESERVATION_META_DATA_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM:
                    mParentalAgeAttributeOfProgram = DBUtils.getNumeric(data); //parental_age_attribute_of_program
                    break;
                default:
            }

        }
    }
    /**
     * キーの値を取得する
     *
     * @param key  キー
     */
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

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int mLimit) {
        this.mLimit = mLimit;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int mOffset) {
        this.mOffset = mOffset;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    public int getTotal() {
        return mTotal;
    }

    public void setTotal(int mTotal) {
        this.mTotal = mTotal;
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
     * @param jsonObj Jsonオブジェクト
     */
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
            //録画予約一覧データ
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
