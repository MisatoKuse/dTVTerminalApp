package com.nttdocomo.android.tvterminalapp.datamanager.databese;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

public class DBTestEntity {
    private int rowId;

    private String value;

    HashMap<String, String> map = new HashMap<>();
    List<HashMap<String, String>> hashMaps = new ArrayList<HashMap<String, String>>();

    /**
     * テスト用getter
     *
     * @return
     */
    public HashMap<String, String> getMap() {
        return map;
    }

    /**
     * テスト用getter
     *
     * @return
     */
    public List<HashMap<String, String>> getHashMaps() {
        return hashMaps;
    }

    /**
     * テスト用setter
     *
     * @param hashMap
     */
    public void setHashMap(HashMap<String, String> hashMap) {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("title", "ＭＬＢ　アメリカン・リーグ　地区シリーズ　第１戦「レッドソックス×アストロズ」");
        map.put("thumb", "\"//www.nhk.or.jp/prog/img/639/639.jpg");
        map = hashMap;
    }

    /**
     * テスト用setter
     *
     * @param hashMapList
     */
    public void setHashMapList(List<HashMap<String, String>> hashMapList) {
        hashMaps = hashMapList;
    }
    /*
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getRowId() {
        return rowId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    */
}
