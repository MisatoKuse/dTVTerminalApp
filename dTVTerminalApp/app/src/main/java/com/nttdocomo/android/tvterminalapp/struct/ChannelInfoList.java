/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * 複数チャンネルクラス
 * 　　機能： 複数チャンネルを管理するクラスである
 */
public class ChannelInfoList {

    //チャンネルの配列
    private List<ChannelInfo> mChannels = null;

    /**
     * クラス構造.
     */
    public ChannelInfoList() {
        mChannels = Collections.synchronizedList(new ArrayList<ChannelInfo>());
    }

    /**
     * チャンネルの配列を取得.
     * @return mChannels
     */
    public List<ChannelInfo> getChannels() {
        return mChannels;
    }

    /**
     * チャンネルを追加.
     * @param ch チャンネル
     */
    public void addChannel(final ChannelInfo ch) {
        mChannels.add(ch);
    }
}
