/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.program;


import java.util.ArrayList;

/* 作成中 */
/*
 * 複数チャンネルクラス
 * 　　機能： 複数チャンネルを管理するクラスである
 */
public class ChannelsInfo {
    private ArrayList<Channel> mChannels=null;

    public ChannelsInfo(){
        mChannels=new ArrayList<Channel>();
    }

    public int getChannelCount(){
        if(null==mChannels){
            return 0;
        }
        return mChannels.size();
    }

    public ArrayList<String> getChannelNames(){
        if(null==mChannels){
            return null;
        }
        ArrayList<String> ret=new ArrayList<String>();
        for(int i=0;i<mChannels.size();++i){
            ret.add(mChannels.get(i).getChannelName());
        }
        return ret;
    }

    public void setScheduleInfos(ArrayList<Channel> channels){
        mChannels.clear();
        mChannels = channels;
    }

    public void addChannel(Channel ch){
        mChannels.add(ch);
    }

    private static final int SCHEDULE_ACTIVITY_CHANNEL_COUNT=2;

    private int getMaxScheduleCount(int startChanellIndex, final int displayChannelCount){
        int ret=0;
        for(int i=startChanellIndex; i<displayChannelCount; ++i) {
            Channel channel = mChannels.get(i);
            int cnt=channel.getScheduleCount();
            ret = (ret<cnt? cnt: ret);
        }
        return ret;
    }

    private ArrayList<Integer> mChannelOverCount;

    private ArrayList<Integer> mChannelHeight;
    private int getLowest(){
        int ret=Integer.MAX_VALUE;
        for(int i=0;i<mChannelHeight.size();++i){
            if(ret>mChannelHeight.get(i)){
                ret=mChannelHeight.get(i);
            }
        }
        return ret;
    }
    private int getLowestChannelNo(){
        int lowestValue= getLowest();
        for(int i=0;i<mChannelHeight.size();++i){
            if(lowestValue == mChannelHeight.get(i)){
                return i;
            }
        }
        return 0;
    }

    private int getAllScheduleCount(int startChanellIndex, final int displayChannelCount){
        int ret=0;
        for(int i=startChanellIndex; i<displayChannelCount; ++i) {
            Channel channel = mChannels.get(i);
            ret += ( channel.getScheduleCount()  );
        }
        return ret;
    }

    public ArrayList<Schedule> getScheduleInfosForRecyclerView(int startChanellIndex, final int displayChannelCount){
        ArrayList<Schedule> ret= new ArrayList<Schedule>();

        mChannelHeight = new ArrayList<Integer>();
        for(int i=0;i<displayChannelCount;++i){
            mChannelHeight.add(0);
        }

        mChannelOverCount= new ArrayList<Integer>();
        for(int i=0;i<displayChannelCount;++i){
            mChannelOverCount.add(0);
        }

        int sum = getAllScheduleCount(startChanellIndex, displayChannelCount);
        for(int i=0; i<sum; ++i){
            int lowestChannelNo = getLowestChannelNo();
            Channel channel = mChannels.get(lowestChannelNo + startChanellIndex);

            int currentHeight = mChannelHeight.get(lowestChannelNo);

            int channelOverCount = mChannelOverCount.get(lowestChannelNo);
            if(channelOverCount==channel.getScheduleCount()){
                //pad this channel to
                Schedule bottomPadding = createBlankSchedule(getBottomPaddingHeight(currentHeight));
                ret.add(bottomPadding);
                mChannelOverCount.set(lowestChannelNo, channelOverCount + 1);
                mChannelHeight.set(lowestChannelNo, bottomPadding.getAllHeight() + currentHeight);
                ++sum;
                continue;
            }
            if(channelOverCount>channel.getScheduleCount()){
                continue;
            }

            Schedule sche= channel.getSchedule(channelOverCount);
            int topPadding= getScheduleTopPadding(sche, currentHeight);
            sche.setTopPadding(topPadding);

            ret.add(sche);

            //update mChannelOverCount
            mChannelOverCount.set(lowestChannelNo, channelOverCount + 1);

            //update mChannelHeight
            int scheHeight= sche.getHeightWithTopPadding() + currentHeight;
            mChannelHeight.set(lowestChannelNo, scheHeight);
        }

        int maxScheduleCnt= getMaxScheduleCount(startChanellIndex, displayChannelCount);


        return ret;
    }

    private Schedule createBlankSchedule(int height){
        Schedule ret= new Schedule();
        ret = new Schedule();
        ret.setContent("");
        ret.setImageUrl("");
        ret.setStartTime("");
        ret.setEndTime("");
        ret.setHeight(height);
        return ret;
    }

    private int getScheduleTopPadding(Schedule sche, int topHeight){
        //to do
        return 50;
    }

    private int getBottomPaddingHeight(int startHeight){
        //to do
        return 10000;
    }


}
