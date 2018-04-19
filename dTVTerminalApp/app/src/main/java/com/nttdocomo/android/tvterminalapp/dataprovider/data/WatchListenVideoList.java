/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 視聴中ビデオ一覧データ管理クラス.
 */
public class WatchListenVideoList implements Serializable {

    private static final long serialVersionUID = 9013746905348284666L;
    /** 視聴中ビデオ一覧データマップ.*/
    private HashMap<String, String> mVcMap = new HashMap<>();
    /** 視聴中ビデオ一覧データ.*/
    private List<HashMap<String, String>> mVcList = new ArrayList<>();

    /**
     * 視聴中ビデオ一覧データマップ取得.
     * @return 視聴中ビデオ一覧データマップ
     */
    public HashMap getVcMap() {
        return mVcMap;
    }

    /**
     * 視聴中ビデオ一覧データマップ設定.
     * @param vcMap 視聴中ビデオ一覧データマップ
     */
    public void setVcMap(final HashMap<String, String> vcMap) {
        this.mVcMap = vcMap;
    }

    /**
     * 視聴中ビデオ一覧データ取得.
     * @return 視聴中ビデオ一覧データ
     */
    public List getVcList() {
        return mVcList;
    }

    /**
     * 視聴中ビデオ一覧データ設定.
     * @param vcList 視聴中ビデオ一覧データ
     */
    public void setVcList(final List<HashMap<String, String>> vcList) {
        this.mVcList = vcList;
    }
}
