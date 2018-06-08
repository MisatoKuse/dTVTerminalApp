/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * チャンネルリスト情報.
 */
public class ChannelList implements Parcelable {

    // TODO :メンバを固定でnewしているが、結局setter関数でまるごと置き換えているので無駄にnewしている.
    // TODO :コンストラクタで必要な情報をすべて受け取り初期化するか、初期値をnullにして利用箇所は全てnull判定するなりすべき.
    /**
     * WebAPIレスポンス情報(チャンネルリスト情報以外).
     */
    private HashMap<String, String> mResponseInfoMap = new HashMap<>();

    /**
     * チャンネルリスト情報.
     */
    private List<HashMap<String, String>> mChannelList = new ArrayList<>();

    /**
     * WebAPIレスポンス情報(チャンネルリスト情報以外)取得.
     * @return レスポンス情報
     */
    public HashMap getResponseInfoMap() {
       return mResponseInfoMap;
    }

    /**
     * WebAPIレスポンス情報(チャンネルリスト情報以外)設定.
     * @param responseInfoMap  レスポンス情報
     */
    public void setResponseInfoMap(final HashMap<String, String> responseInfoMap) {
        this.mResponseInfoMap = responseInfoMap;
    }

    /**
     * WebAPIレスポンス情報(チャンネルリスト情報以外)情報取得.
     * @return チャンネルリスト情報
     */
    public List<HashMap<String, String>> getChannelList() {
        return mChannelList;
    }

    /**
     * チャンネルリスト情報設定.
     * @param clList  チャンネルリスト情報
     */
    public void setChannelList(final List clList) {
        this.mChannelList = clList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.mResponseInfoMap);
        dest.writeList(this.mChannelList);
    }

    public ChannelList() {
    }

    protected ChannelList(Parcel in) {
        this.mResponseInfoMap = (HashMap<String, String>) in.readSerializable();
        this.mChannelList = new ArrayList<HashMap<String, String>>();
        //TODO 自動生成したのにビルドエラーが出るので一時的にコメントアウト(対策は後回し)
//        in.readList(this.mChannelList, HashMap<String, String>.class.getClassLoader());
    }

    public static final Parcelable.Creator<ChannelList> CREATOR = new Parcelable.Creator<ChannelList>() {
        @Override
        public ChannelList createFromParcel(Parcel source) {
            return new ChannelList(source);
        }

        @Override
        public ChannelList[] newArray(int size) {
            return new ChannelList[size];
        }
    };
}
