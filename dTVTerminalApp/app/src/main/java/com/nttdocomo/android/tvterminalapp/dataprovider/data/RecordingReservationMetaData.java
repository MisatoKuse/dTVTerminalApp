/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 録画予約メタデータ.
 */
public class RecordingReservationMetaData implements Serializable {
    private static final long serialVersionUID = -930749879941574984L;
    /**予約情報の優先度.*/
    private int mPriority = 0;
    /**プラットフォームタイプ.*/
    private String mPlatformType = null;
    /**予約日時タイプ.*/
    private String mDayOfTheWeek = null;
    /**アダルトタイプ.*/
    private String mAdultAttributeOfChannel = null;
    /**サービスID.*/
    private String mServiceId = null;
    /**予約の録画開始予定時刻.*/
    private long mStartScheduleTime = 0;
    /**予約の録画終了予定時刻.*/
    private long mEndScheduleTime = 0;
    /**イベントID.*/
    private String mEventId = null;
    /**タイトル.*/
    private String mTitle = null;
    /**番組のPG値.*/
    private int mParentalAgeAttributeOfProgram = 0;
    /**ページャーパラメータ.*/
    private static final String[] mPagerPara = {JsonConstants.META_RESPONSE_PAGER_LIMIT,
            JsonConstants.META_RESPONSE_OFFSET, JsonConstants.META_RESPONSE_COUNT,
            JsonConstants.META_RESPONSE_TOTAL};
    /**録画番組パラメータ.*/
    private static final String[] mReservationListPara = {JsonConstants.META_RESPONSE_PRIORITY,
            JsonConstants.META_RESPONSE_PLATFORM_TYPE, JsonConstants.META_RESPONSE_DAY_OF_THE_WEEK,
            JsonConstants.META_RESPONSE_ADULT_ATTRIBUTE_OF_CHANNEL, JsonConstants.META_RESPONSE_SERVICE_ID,
            JsonConstants.META_RESPONSE_START_SCHEDULE_TIME, JsonConstants.META_RESPONSE_END_SCHEDULE_TIME,
            JsonConstants.META_RESPONSE_EVENT_ID, JsonConstants.META_RESPONSE_TITLE,
            JsonConstants.META_RESPONSE_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM};

    /**
     * キーとキーの値をメンバーにセットする.
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(final String key, final Object data) {
        if (!key.isEmpty()) {
            switch (key) {
                case JsonConstants.META_RESPONSE_PRIORITY:
                    mPriority = DBUtils.getNumeric(data);        //priority
                    break;
                case JsonConstants.META_RESPONSE_PLATFORM_TYPE:
                    mPlatformType = (String) data;        //platform_type
                    break;
                case JsonConstants.META_RESPONSE_DAY_OF_THE_WEEK:
                    mDayOfTheWeek = (String) data;        //day_of_the_week
                    break;
                case JsonConstants.META_RESPONSE_ADULT_ATTRIBUTE_OF_CHANNEL:
                    mAdultAttributeOfChannel = (String) data; //adult_attribute_of_channel
                    break;
                case JsonConstants.META_RESPONSE_SERVICE_ID:
                    mServiceId = (String) data;           //service_id
                    break;
                case JsonConstants.META_RESPONSE_START_SCHEDULE_TIME:
                    mStartScheduleTime = DBUtils.getLong(data);  //start_schedule_time
                    break;
                case JsonConstants.META_RESPONSE_END_SCHEDULE_TIME:
                    mEndScheduleTime = DBUtils.getLong(data);     //end_schedule_time
                    break;
                case JsonConstants.META_RESPONSE_EVENT_ID:
                    mEventId = (String) data;             //event_id
                    break;
                case JsonConstants.META_RESPONSE_TITLE:
                    mTitle = (String) data;               //title
                    break;
                case JsonConstants.META_RESPONSE_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM:
                    mParentalAgeAttributeOfProgram = DBUtils.getNumeric(data); //parental_age_attribute_of_program
                    break;
                default:
            }
        }
    }

    /**
     * キーの値を取得する.
     *
     * @param key キー
     * @return key値
     */
    public Object getMember(final String key) {
        if (key.isEmpty()) {
            return "";
        } else {
            switch (key) {
                case JsonConstants.META_RESPONSE_PRIORITY:
                    return mPriority;            //priority
                case JsonConstants.META_RESPONSE_PLATFORM_TYPE:
                    return mPlatformType;        //platform_type
                case JsonConstants.META_RESPONSE_DAY_OF_THE_WEEK:
                    return mDayOfTheWeek;        //day_of_the_week
                case JsonConstants.META_RESPONSE_ADULT_ATTRIBUTE_OF_CHANNEL:
                    return mAdultAttributeOfChannel; //adult_attribute_of_channel
                case JsonConstants.META_RESPONSE_SERVICE_ID:
                    return mServiceId;           //service_id
                case JsonConstants.META_RESPONSE_START_SCHEDULE_TIME:
                    return mStartScheduleTime;   //start_schedule_time
                case JsonConstants.META_RESPONSE_END_SCHEDULE_TIME:
                    return mEndScheduleTime;     //end_schedule_time
                case JsonConstants.META_RESPONSE_EVENT_ID:
                    return mEventId;             //event_id
                case JsonConstants.META_RESPONSE_TITLE:
                    return mTitle;               //title
                case JsonConstants.META_RESPONSE_PARENTAL_AGE_ATTRIBUTE_OF_PROGRAM:
                    return mParentalAgeAttributeOfProgram; //parental_age_attribute_of_program
                default:
                    return "";
            }
        }
    }

    /**
     * ページャーパラメータ取得.
     * @return ページャーパラメータ
     */
    public static String[] getPagerPara() {
        return mPagerPara.clone();
    }

    /**
     * 録画番組パラメータ取得.
     * @return ページャーパラメータ
     */
    public static String[] getReservationListPara() {
        return mReservationListPara.clone();
    }

    /**
     * 予約情報の優先度取得.
     * @return  約情報の優先度
     */
    public int getPriority() {
        return mPriority;
    }

    /**
     * 約情報の優先度設定.
     * @param mPriority 約情報の優先度
     */
    public void setPriority(final int mPriority) {
        this.mPriority = mPriority;
    }

    /**
     * プラットフォームタイプ取得.
     * @return プラットフォームタイプ
     */
    public String getPlatformType() {
        return mPlatformType;
    }

    /**
     * プラットフォームタイプ設定.
     * @param mPlatformType プラットフォームタイプ
     */
    public void setPlatformType(final String mPlatformType) {
        this.mPlatformType = mPlatformType;
    }

    /**
     * 予約日時タイプ取得.
     * @return 予約日時タイプ
     */
    public String getDayOfTheWeek() {
        return mDayOfTheWeek;
    }

    /**
     * 予約日時タイプ設定.
     * @param mDayOfTheWeek 予約日時タイプ
     */
    public void setDayOfTheWeek(final String mDayOfTheWeek) {
        this.mDayOfTheWeek = mDayOfTheWeek;
    }

    /**
     * 予約日時タイプ取得.
     * @return 予約日時タイプ
     */
    public String getAdultAttributeOfChannel() {
        return mAdultAttributeOfChannel;
    }

    /**
     * アダルトタイプ設定.
     * @param mAdultAttributeOfChannel アダルトタイプ
     */
    public void setAdultAttributeOfChannel(final String mAdultAttributeOfChannel) {
        this.mAdultAttributeOfChannel = mAdultAttributeOfChannel;
    }

    /**
     * サービスID取得.
     * @return  サービスID
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
     * 予約の録画開始予定時刻取得.
     * @return 予約の録画開始予定時刻
     */
    public long getStartScheduleTime() {
        return mStartScheduleTime;
    }

    /**
     * 予約の録画開始予定時刻設定.
     * @param mStartScheduleTime 予約の録画開始予定時刻
     */
    public void setStartScheduleTime(final int mStartScheduleTime) {
        this.mStartScheduleTime = mStartScheduleTime;
    }

    /**
     * 予約の録画終了予定時刻取得.
     * @return 予約の録画終了予定時刻
     */
    public long getEndScheduleTime() {
        return mEndScheduleTime;
    }

    /**
     * 予約の録画終了予定時刻設定.
     * @param mEndScheduleTime 予約の録画終了予定時刻
     */
    public void setEndScheduleTime(final int mEndScheduleTime) {
        this.mEndScheduleTime = mEndScheduleTime;
    }

    /**
     * イベントID取得取得.
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
     * 番組のPG値取得.
     * @return 番組のPG値
     */
    public int getParentalAgeAttributeOfProgram() {
        return mParentalAgeAttributeOfProgram;
    }

    /**
     * 番組のPG値設定.
     * @param mParentalAgeAttributeOfProgram 番組のPG値
     */
    public void setParentalAgeAttributeOfProgram(final int mParentalAgeAttributeOfProgram) {
        this.mParentalAgeAttributeOfProgram = mParentalAgeAttributeOfProgram;
    }

    /**
     * サーバから取得したデータをセット.
     *
     * @param jsonObj Jsonオブジェクト
     */
    public void setData(final JSONObject jsonObj) {
        DTVTLogger.start();
        if (jsonObj != null) {
            //録画予約一覧データ
            for (String item : mReservationListPara) {
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