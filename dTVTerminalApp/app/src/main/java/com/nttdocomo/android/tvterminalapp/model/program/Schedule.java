/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.program;

import java.text.SimpleDateFormat;
import java.util.Date;

/* 作成中 */
/*
 * 番組クラス
 * 　　機能： 番組の属性を纏めるクラスである
 */
public class Schedule {

    //タイトル
    private String mTitle = "";
    //開始時間
    private String mStartTime = "";
    //終了時間
    private String mEndTime = "";
    //thumbnail url
    private String mImageUrl = "";
    //channel no
    private String mChno = "";
    //episode info
    private String mEpisode;
    //高さ
    private int mHeight = 0;
    //一日の秒数
    private static final int secondsOneDay=24*60*60;
    //タイムラインの高さ
    private int mTimeLineTotalHeight=1;
    //タイムラインは何時から
    private int mBaseTime=0;

    /**
     * クラス構造
     */
    public Schedule(){
    }

    /**
     * ScheduleはDataProviderにて生成したので、UIはScheduleを使う前に、UIのラインタイム情報をScheduleに設定
     * @param timeLineTotalHeight タイムライン高さ
     * @param baseTime　開始タイム
     */
    public void rejustScheduleInfo(int timeLineTotalHeight, int baseTime){
        mTimeLineTotalHeight = timeLineTotalHeight;
        mBaseTime = baseTime;
    }

    /**
     * mEpisodeを取得
     * @return episode情報
     */
    public String getEpisode() {
        return mEpisode;
    }

    /**
     * mEpisodeを設定
     * @param mEpisode episode情報
     */
    public void setEpisode(String mEpisode) {
        this.mEpisode = mEpisode;
    }

    /*
     * タイトルを取得する
     */
    public String getTitle() {
        return mTitle;
    }

    /*
     * タイトルを設定する
     */
    public void setTitle(String content) {
        mTitle = content;
    }

    /*
     * 開始時間を取得する
     */
    public String getStartTime() {
        return mStartTime;
    }

    /*
     * 開始時間を設定する
     */
    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    /*
     * 終了時間を取得する
     */
    public String getEndTime() {
        return mEndTime;
    }

    /*
     * 終了時間を設定する
     */
    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    /*
     * Thumbnailを取得する
     */
    public String getImageUrl() {
        return mImageUrl;
    }

    /*
     * Thumbnailを設定する
     */
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    /*
     * チャンネルNOを取得する
     */
    public String getmChno() {
        return mChno;
    }

    /*
     * チャンネルNOを設定する
     */
    public void setmChno(String mChno) {
        this.mChno = mChno;
    }

    /*
         * 高さを取得する
         */
    public int getHeight() {
        return mHeight;
    }

    /*
     * 高さを設定する
     */
    public void setHeight(int height) {
        mHeight = height;
    }

    /**
     * 0時から開始時間までの秒数を取得
     * @return 開始タイムの秒数
     */
    private int getStartTimeSeconds(){
        Date time = strToDate(mStartTime);
        if(null==time){
            return 0;
        }
        int hourSub = time.getHours() - mBaseTime;
        if(0>hourSub){
            hourSub+=24;
        }
        return hourSub*60*60 + time.getMinutes()*60 + time.getSeconds();
    }

    /**
     * 文字列の日付を日付へ変換し、戻す
     * @param str　変換情報
     * @return 日付
     */
    private static Date strToDate(String str) {
        if(null==str || str.length()<20){
            return null;
        }
        str= str.substring(0, 10) + str.substring(11, 19);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");  //2017-10-12T08:00:00+09:00
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * カレントSchduleの上のpaddingを取得
     * @return top padding
     */
    public int getTopPadding(){
        if(0== mTimeLineTotalHeight){
            return 0;
        }
        int seconds = getStartTimeSeconds();
        if(0>seconds){
            seconds=0;
        }
        return (int) ((double)(seconds)/(double)(secondsOneDay) * mTimeLineTotalHeight);
    }
}
