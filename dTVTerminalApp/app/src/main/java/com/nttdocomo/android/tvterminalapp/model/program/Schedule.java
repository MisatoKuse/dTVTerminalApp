/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.program;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* 作成中 */
/*
 * 番組クラス
 * 　　機能： 番組の属性を纏めるクラスである
 */
public class Schedule {

    //タイトル
    private String title;
    //開始時間
    private String startTime;
    //終了時間
    private String endTime;
    //サムネイル url
    private String imageUrl;
    //チャンネル ID
    private String chNo;
    //時間単価換算
    private static final int FORMAT = 1000 * 60 * 60;
    //日付format
    private static final String DATE_FORMAT = "yyyy-MM-ddHH:mm:ss";

    /*
     * タイトルを取得する
     */
    public String getTitle() {
        return title;
    }

    /*
     * タイトルを設定する
     */
    public void setTitle(String title) {
        this.title = title;
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
    public void setStartTime(String startTime) {
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
    public void setEndTime(String endTime) {
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
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
    public void setChNo(String chNo) {
        this.chNo = chNo;
    }


    /**
     * 開始時間よりmarginの取得
     *
     * @return 前の間隔
     */
    public float getMarginTop(String lastEndDate) {
        String standardTime = "";
        if ("".equals(lastEndDate)) {
            if (startTime != null) {
                String curStartDay = startTime.substring(0, 10);
                standardTime = curStartDay + "04:00:00";
            }
        } else {
            standardTime = getFormatDate(lastEndDate);
        }
        Date startTime = stringToDate(standardTime);
        Date endTime = stringToDate(getFormatDate(this.startTime));
        float diffHours = (endTime.getTime() - startTime.getTime()) / (float) (FORMAT);
        if (diffHours < 0) {
            diffHours = 0;
        }
        return diffHours;
    }

    /**
     * StringをDateに変換
     *
     * @return date
     */
    private Date stringToDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);  //2017-10-12T08:00:00+09:00
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Stringを切り取る
     *
     * @return 変換後のデータ
     */
    private String getFormatDate(String date) {
        return date.substring(0, 10) + date.substring(11, 19);
    }

    /**
     * 番組高さ取得
     *
     * @return 高さ
     */
    public float getMyHeight() {
        Date startTime = stringToDate(getFormatDate(this.startTime));
        Date endTime = stringToDate(getFormatDate(this.endTime));
        float diffHours = (endTime.getTime() - startTime.getTime()) / (float) (FORMAT);
        if (diffHours < 0) {
            diffHours = 0;
        }
        return diffHours;
    }
}
