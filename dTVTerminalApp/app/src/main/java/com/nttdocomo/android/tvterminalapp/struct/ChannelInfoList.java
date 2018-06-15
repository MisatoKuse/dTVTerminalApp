/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 複数チャンネルクラス.
 * 　　機能： 複数チャンネルを管理するクラスである
 */
public class ChannelInfoList implements Parcelable {

    /**チャンネルの配列.*/
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mChannels);
    }

    protected ChannelInfoList(Parcel in) {
        this.mChannels = in.createTypedArrayList(ChannelInfo.CREATOR);
    }

    /**
     * データのクリアを行い、ガベージコレクションされやすくする.
     */
    public void clearData() {
        if (mChannels != null) {
            mChannels.clear();
        }
    }

    public static final Parcelable.Creator<ChannelInfoList> CREATOR = new Parcelable.Creator<ChannelInfoList>() {
        @Override
        public ChannelInfoList createFromParcel(Parcel source) {
            return new ChannelInfoList(source);
        }

        @Override
        public ChannelInfoList[] newArray(int size) {
            return new ChannelInfoList[size];
        }
    };
}
