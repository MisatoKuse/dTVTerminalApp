/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.program;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * チャンネルクラス
 * 　　機能： チャンネルクラスである
 */
public class Channel {

    //Scheduleの配列を保存
    private ArrayList<Schedule> mSchedules=null;
    //チャンネルのタイトル
    private String mTitle ="";
    //チャンネルのｍChno
    private int ｍChno=0;

    /**
     * Channelを構造する
     * @param title タイトル情報
     * @param chno　チャンネルID
     *
     */
    public Channel(String title, int chno, ArrayList<Schedule> scheduleInfos){
        this.mTitle =title;
        this.ｍChno = chno;
        mSchedules = scheduleInfos;
    }

    /**
     * Channelタイトルを取得
     * @return タイトル
     */
    public String getChannelName(){
        return mTitle;
    }

    /**
     * ｍChnoを取得
     * @return chno
     */
    public int getChno() {
        return ｍChno;
    }

    /**
     * ｍChnを設定
     * @param chno チャンネルID
     */
    public void setChno(int chno) {
        this.ｍChno = chno;
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

    /**
     * baseTimeHourを基準とし、番組の開始時間でソート
     * @param baseTimeHour 開始タイム
     */
    public void sort(final int baseTimeHour){
        if(null==mSchedules || 2>mSchedules.size()){
            return;
        }

        SortByStartTime sort= new SortByStartTime();
        Collections.sort(mSchedules, sort);

        ArrayList<Schedule> tmp=new ArrayList<>();
        int i=0;
        for(;i<mSchedules.size();++i){
            Schedule sch= mSchedules.get(i);
            String strHour = sch.getStartTime();  //format is 2017-10-17T06:30:00+09:00
            if(12>strHour.length()){
                DTVTLogger.debug("Schedule::sort, data format is wrong, skip");
                continue;
            }
            strHour= strHour.substring(11, 13);
            int hour=0;
            try{
                hour = Integer.parseInt(strHour);
            }catch (Exception e){
                DTVTLogger.debug(e);
                continue;
            }
            if(hour>baseTimeHour){
                break;
            }
        }

        for(int j=i;j<mSchedules.size();++j){
            tmp.add(mSchedules.get(j));
        }

        for(int j=0;j<i;++j){
            tmp.add(mSchedules.get(j));
        }
        mSchedules=tmp;
    }

    /**
     * 番組の開始時間でソートを実現する内部クラス
     */
    static private class SortByStartTime implements Comparator {
        /**
         * 番組の開始時間でソート
         * @param o1 比較対象
         * @param o2 比較対象
         * @return 比較結果
         */
        @Override
        public int compare(Object o1, Object o2) {
            //the format of Schedule.getStartTime() is 2017-10-17T06:30:00+09:00
            Schedule s1 = (Schedule) o1;
            Schedule s2 = (Schedule) o2;
            String time1 = s1.getStartTime();
            String time2= s2.getStartTime();
            return time1.compareTo(time2);
        }
    }

    /**
     * ScheduleはDataProviderにて生成したので、UIはScheduleを使う前に、UIのラインタイム情報をScheduleに設定
     * @param timeLineTotalHeight タイムライン高さ
     * @param baseTime　開始タイム
     */
    public void rejustScheduleInfo(int timeLineTotalHeight, int baseTime){
        for(Schedule sch:mSchedules){
            sch.rejustScheduleInfo(timeLineTotalHeight, baseTime);
        }
    }
}
