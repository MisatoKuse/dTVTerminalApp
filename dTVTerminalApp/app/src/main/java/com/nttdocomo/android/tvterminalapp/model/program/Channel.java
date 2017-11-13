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
