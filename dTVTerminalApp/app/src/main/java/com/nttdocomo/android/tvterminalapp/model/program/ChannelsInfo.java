/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.program;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import java.util.ArrayList;

/*
 * 複数チャンネルクラス
 * 　　機能： 複数チャンネルを管理するクラスである
 */
public class ChannelsInfo {

    //チャンネルの配列
    private ArrayList<Channel> mChannels=null;

    //番組UIにて、時間パーツの高さ
    private int mTimeLineHeight=0;

    /**
     * クラス構造
     */
    public ChannelsInfo(){
        mChannels=new ArrayList<>();
    }

    /**
     * チャンネルの配列を取得
     * @return mChannels
     */
    public ArrayList<Channel> getChannels() {
        return mChannels;
    }

    /**
     * チャンネル数を取得
     * @return チャンネル数
     */
    public int getChannelCount(){
        if(null==mChannels){
            return 0;
        }
        return mChannels.size();
    }

    /**
     * チャンネル名前の配列を取得
     * @return チャンネル名前
     */
    public ArrayList<String> getChannelNames(){
        if(null==mChannels){
            return null;
        }
        ArrayList<String> ret=new ArrayList<>();
        for(int i=0;i<mChannels.size();++i){
            Channel ch= mChannels.get(i);
            if(null==ch){
                DTVTLogger.debug("ChannelsInfo::getChannelNames, bad data");
                continue;
            }
            String name= ch.getChannelName();
            ret.add(name);
        }
        return ret;
    }

    /**
     * チャンネルの配列を設定
     * @param channels チャンネル配列
     */
    public void setChannels(ArrayList<Channel> channels){
        mChannels.clear();
        mChannels = channels;
    }

    /**
     * チャンネルを追加
     * @param ch チャンネル
     */
    public void addChannel(Channel ch){
        mChannels.add(ch);
    }

    /**
     * RecyclerView用配列を取得
     * @param startChanellIndex 開始index
     * @param displayChannelCount チャンネルcnt
     * @return Schedule配列
     */
    public ArrayList<Schedule> getScheduleInfosForRecyclerView(int startChanellIndex, final int displayChannelCount){
        ArrayList<Schedule> ret= new ArrayList<>();

        for(int i=startChanellIndex;i<displayChannelCount;++i){
            Schedule bottomPadding = createBlankSchedule(getBottomPaddingHeight(0));
            ret.add(bottomPadding);
        }
        return ret;
    }

    /**
     * 空内容のScheduleを作成し、戻す
     * @param height 高さ
     * @return Schedule
     */
    private Schedule createBlankSchedule(int height){
        Schedule ret= new Schedule();
        ret.rejustScheduleInfo(1, 0);
        ret.setTitle("");
        ret.setImageUrl("");
        ret.setStartTime("");
        ret.setEndTime("");
        ret.setHeight(height);
        return ret;
    }

    /**
     * startHeightから最大高さを戻す
     * @param startHeight 開始高さ
     * @return  bom　padding
     */
    private int getBottomPaddingHeight(int startHeight){
        return mTimeLineHeight - startHeight;
    }

    /**
     * チャンネルごと開始時間でソート
     * @param baseTimeHour　開始時間
     */
    public void sortByTime(final int baseTimeHour){
        for(Channel ch: mChannels){
            ch.sort(baseTimeHour);
        }
    }

    /**
     * ScheduleはDataProviderにて生成したので、UIはScheduleを使う前に、UIのラインタイム情報をScheduleに設定
     * @param timeLineTotalHeight　タイムライン高さ
     * @param baseTime　開始タイム
     */
    public void rejustSchedulesForUi(int timeLineTotalHeight, int baseTime){
        for(Channel ch:mChannels){
            ch.rejustScheduleInfo(timeLineTotalHeight, baseTime);
        }
    }
}
