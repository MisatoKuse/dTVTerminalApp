/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;


import java.util.ArrayList;

/*
 * 複数チャンネルクラス
 * 　　機能： 複数チャンネルを管理するクラスである
 */
public class ChannelsInfo {

    //チャンネルの配列
    private ArrayList<Channel> mChannels=null;

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
     * チャンネルを追加
     * @param ch チャンネル
     */
    public void addChannel(Channel ch){
        mChannels.add(ch);
    }
}
