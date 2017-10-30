/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.program;


import java.util.ArrayList;

/* 作成中 */
/*
 * チャンネルクラス
 * 　　機能： チャンネルクラスである
 */
public class Channel {

    private ArrayList<Schedule> mSchedules=null;
    private String title="";
    private int chno;

    /**
     * Channelを構造する
     * @param title タイトル情報
     * @param chno　チャンネルID
     *
     */
    public Channel(String title, int chno, ArrayList<Schedule> scheduleInfos){
        this.title=title;
        this.chno = chno;
        mSchedules = scheduleInfos;
    }

    /**
     * Channelタイトルを取得
     *
     */
    public String getChannelName(){
        return title;
    }

    /**
     * Channel idを取得
     */
    public int getChno() {
        return chno;
    }

    /**
     *
     * @param chno チャンネルID
     */
    public void setChno(int chno) {
        this.chno = chno;
    }

    /**
     * Channel 一覧を取得
     */
    public ArrayList<Schedule> getmSchedules() {
        return mSchedules;
    }

    /**
     *
     * @param mSchedules 全チャンネル情報取得
     */
    public void setmSchedules(ArrayList<Schedule> mSchedules) {
        this.mSchedules = mSchedules;
    }

    /**
     * 番組数を取得
     */
    public int getScheduleCount(){
        if(null==mSchedules){
            return 0;
        }

        return mSchedules.size();
    }

    /**
     * 番組を取得
     * @param index　チャンネルindex
     */
    public Schedule getSchedule(int index){
        if(null==mSchedules || 0>index || index>=mSchedules.size()){
            return null;
        }
        return mSchedules.get(index);
    }


}
