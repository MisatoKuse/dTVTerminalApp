/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/* 作成中 */
/*
 * 番組クラス.
 * 　　機能： 番組の属性を纏めるクラスである.
 */
public class ScheduleInfo {

    //タイトル
    private String title;
    //番組詳細
    private String detail;
    //開始時間
    private String startTime;
    //終了時間
    private String endTime;
    //サムネイル url
    private String imageUrl;
    //サムネイル url(詳細画面用)
    private String imageDetailUrl;
    //チャンネル ID
    private String chNo;
    //パレンタル情報
    private String mRValue = null;
    //表示タイプ
    private String mDispType = null;
    //クリップ判定情報
    private String mSearchOk = null;
    //dTVフラグ
    private String mDtv = null;
    //dTVタイプ
    private String mDtvType = null;
    //コンテンツタイプ
    private String mContentType = null;
    //クリップリクエスト用データ
    private ClipRequestData mClipRequestData = null;
    //時間単価換算
    private static final float FORMAT = 1000 * 60 * 60;
    // クリップ可否
    private boolean mClipExec = false;
    // クリップ未/済
    private boolean mClipStatus = false;
    //コンテンツID
    private String mContentsId = null;

    /*
     * タイトルを取得する.
     */
    public String getTitle() {
        return title;
    }

    /*
     * タイトルを設定する.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /*
     * 詳細を取得する.
     */
    public String getDetail() {
        return detail;
    }

    /*
     * 詳細を設定する
     */
    public void setDetail(final String detail) {
        this.detail = detail;
    }

    /*
     * 開始時間を取得する
     */
    public String getStartTime() {
        return startTime;
    }

    /*
     * 開始時間を設定する
     */
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    /*
     * 終了時間を取得する
     */
    public String getEndTime() {
        return endTime;
    }

    /*
     * 終了時間を設定する
     */
    public void setEndTime(final String endTime) {
        this.endTime = endTime;
    }

    /*
     * Thumbnailを取得する
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /*
     * Thumbnailを設定する
     */
    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /*
     * Thumbnailを設定する(詳細画面用)
     */
    public String getImageDetailUrl() {
        return imageDetailUrl;
    }

    /*
     * Thumbnailを設定する(詳細画面用)
     */
    public void setImageDetailUrl(final String imageDetailUrl) {
        this.imageDetailUrl = imageDetailUrl;
    }

    /*
     * チャンネルNOを取得する
     */
    public String getChNo() {
        return chNo;
    }

    /*
     * チャンネルNOを設定する
     */
    public void setChNo(final String chNo) {
        this.chNo = chNo;
    }

    public ClipRequestData getClipRequestData() {
        return mClipRequestData;
    }

    public void setClipRequestData(final ClipRequestData mClipRequestData) {
        this.mClipRequestData = mClipRequestData;
    }

    public String getRValue() {
        return mRValue;
    }

    public void setRValue(final String mRValue) {
        this.mRValue = mRValue;
    }

    public String getDispType() {
        return mDispType;
    }

    public void setDispType(final String mDispType) {
        this.mDispType = mDispType;
    }

    public String getSearchOk() {
        return mSearchOk;
    }

    public void setSearchOk(final String mSearchOk) {
        this.mSearchOk = mSearchOk;
    }

    public String getDtv() {
        return mDtv;
    }

    public void setDtv(final String mDtv) {
        this.mDtv = mDtv;
    }

    public String getDtvType() {
        return mDtvType;
    }

    public void setDtvType(final String mDtvType) {
        this.mDtvType = mDtvType;
    }

    public boolean isClipExec() {
        return mClipExec;
    }

    public void setClipExec(final boolean mClipExec) {
        this.mClipExec = mClipExec;
    }

    public String getContentType() {
        return mContentType;
    }

    public void setContentType(final String mContentType) {
        this.mContentType = mContentType;
    }

    public boolean isClipStatus() {
        return mClipStatus;
    }

    public String getContentsId() {
        return mContentsId;
    }

    public void setContentsId(final String mContentsId) {
        this.mContentsId = mContentsId;
    }

    public void setClipStatus(final boolean mClipStatus) {
        this.mClipStatus = mClipStatus;
    }

    /**
     * 開始時間よりmarginの取得.
     *
     * @return 前の間隔
     */
    public float getMarginTop() {
        String standardTime = "";
        float diffHours = 0;
        if (startTime != null) {
            String curStartDay = startTime.substring(0, 10);
            int hour = Integer.parseInt(startTime.substring(11, 13));
            if (hour >= 0 && hour < 4) {
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYYMMDD, Locale.JAPAN);
                Date date = new Date();
                try {
                    curStartDay = curStartDay.replace("/", "-");
                    date = sdf.parse(curStartDay);
                } catch (ParseException e) {
                    DTVTLogger.debug(e);
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                curStartDay = sdf.format(calendar.getTime());
            }
            standardTime = curStartDay + DateUtils.DATE_STANDARD_START;
        }
        Date startTime = stringToDate(standardTime);
        Date endTime = stringToDate(getFormatDate(this.startTime));
        try {
            diffHours = (endTime.getTime() - startTime.getTime()) / FORMAT;
        } catch (Exception e) {
            DTVTLogger.error("response is null");
        }
        if (diffHours < 0) {
            diffHours = 0;
        }
        return diffHours;
    }

    /**
     * StringをDateに変換.
     *
     * @param strDate 日付
     * @return date
     */
    private Date stringToDate(final String strDate) {
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DDHHMMSS, Locale.JAPAN);  //2017-10-12T08:00:00+09:00
        Date date = null;
        try {
            String replaceString = strDate.replace("-", "/");
            date = format.parse(replaceString);
        } catch (Exception e) {
            DTVTLogger.debug(e);
        }
        return date;
    }

    /**
     * Stringを切り取る.
     *
     * @param date 日付.
     * @return 変換後のデータ
     */
    private String getFormatDate(final String date) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        return date.substring(0, 10) + date.substring(11, 19);
    }

    /**
     * 番組高さ取得.
     *
     * @return 高さ
     */
    public float getMyHeight() {
        Date startTime = stringToDate(getFormatDate(this.startTime));
        Date endTime = stringToDate(getFormatDate(this.endTime));
        if (startTime == null || endTime == null) {
            return 0L;
        }
        float diffHours = (endTime.getTime() - startTime.getTime()) / FORMAT;
        if (diffHours < 0) {
            diffHours = 0;
        }
        return diffHours;
    }
}