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
    private String mName="";
    private int mChannelId;

    /**
     * Channelを構造する
     * @param channelName
     * @param scheduleInfos
     */
    public Channel(String channelName, int channelId, ArrayList<Schedule> scheduleInfos){
        mName=channelName;
        mChannelId = channelId;
        mSchedules = scheduleInfos;
    }

    /**
     * Channelタイトルを取得
     * @return
     */
    public String getChannelName(){
        return mName;
    }

    /**
     * Channel idを取得
     * @return
     */
    public int getChannelId() {
        return mChannelId;
    }

    /**
     *
     * @param mChannelId
     */
    public void setChannelId(int mChannelId) {
        this.mChannelId = mChannelId;
    }

    /**
     * 番組数を取得
     * @return
     */
    public int getScheduleCount(){
        if(null==mSchedules){
            return 0;
        }

        return mSchedules.size();
    }

    /**
     * 番組を取得
     * @param index
     * @return
     */
    public Schedule getSchedule(int index){
        if(null==mSchedules || 0>index || index>=mSchedules.size()){
            return null;
        }
        return mSchedules.get(index);
    }


}
