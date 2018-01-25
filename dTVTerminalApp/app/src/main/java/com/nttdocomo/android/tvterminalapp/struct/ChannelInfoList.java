/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;


import java.util.ArrayList;

/*
 * 複数チャンネルクラス
 * 　　機能： 複数チャンネルを管理するクラスである
 */
public class ChannelInfoList {

    //チャンネルの配列
    private ArrayList<ChannelInfo> mChannels=null;

    /**
     * クラス構造
     */
    public ChannelInfoList(){
        mChannels=new ArrayList<>();
    }

    /**
     * チャンネルの配列を取得
     * @return mChannels
     */
    public ArrayList<ChannelInfo> getChannels() {
        return mChannels;
    }

    /**
     * チャンネルを追加
     * @param ch チャンネル
     */
    public void addChannel(ChannelInfo ch){
        mChannels.add(ch);
    }
}
