/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.program;

import java.util.ArrayList;

/*
 * チャンネルクラス
 * 　　機能： チャンネルクラスである
 */
public class Channel {

    //Scheduleの配列を保存
    private ArrayList<Schedule> schedules =null;
    //チャンネルのタイトル
    private String title;
    //チャンネルのチャンネルID
    private int chNo;
    //チャンネルのサービスID
    private String serviceId;
    //チャンネルの開始時間
    private String startDate;
    //チャンネルの終了時間
    private String endDate;

    /**
     * Channelタイトルを取得
     * @return title タイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * Channelタイトルを設定
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * chNoを取得
     * @return chNo
     */
    public int getChNo() {
        return chNo;
    }

    /**
     * ｍChnを設定
     * @param chNo チャンネルID
     */
    public void setChNo(int chNo) {
        this.chNo = chNo;
    }

    /**
     * serviceIdを取得
     * @return serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * serviceIdを設定
     * @param serviceId チャンネルID
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * startDateを取得
     * @return startDate
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * startDateを設定
     * @param startDate チャンネルID
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * endDateを取得
     * @return endDate
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * endDateを設定
     * @param endDate チャンネルID
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * チャンネル番組を取得
     * @return schedules　チャンネル番組
     */
    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    /**
     * チャンネル番組を設定
     * @param schedules チャンネル
     */
    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }
}
