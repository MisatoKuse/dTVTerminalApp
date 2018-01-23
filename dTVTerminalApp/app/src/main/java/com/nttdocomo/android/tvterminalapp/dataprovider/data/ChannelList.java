/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * チャンネルリスト情報.
 */
public class ChannelList {

    // TODO:メンバを固定でnewしているが、結局setter関数でまるごと置き換えているので無駄にnewしている.
    // TODO:コンストラクタで必要な情報をすべて受け取り初期化するか、初期値をnullにして利用箇所は全てnull判定するなりすべき.
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
    public void setResponseInfoMap(HashMap<String, String> responseInfoMap) {
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
    public void setChannelList(List<HashMap<String, String>> clList) {

        this.mChannelList = clList;
    }

}
