/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class GenreListMetaData implements Serializable {
    private static final long serialVersionUID = -4466388996087422463L;
    public static final String VIDEO_LIST_GENRE_ID_NOD = "genre_id_nod";
    public static final String VIDEO_LIST_GENRE_ID_ALL_CONTENTS = "genre_id_all_contents";

    private String mId;//id
    private String mTitle;//title
    private String mRValue;//r_value
    private ArrayList<GenreListMetaData> mSubContent = null;

    public GenreListMetaData() {
        mSubContent = new ArrayList<GenreListMetaData>();
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getRValue() {
        return mRValue;
    }

    public void setRValue(String mRValue) {
        this.mRValue = mRValue;
    }

    public ArrayList<GenreListMetaData> getSubContent() {
        return this.mSubContent;
    }

    public void setSubContentAll(ArrayList<GenreListMetaData> metaDataList) {
        mSubContent.addAll(metaDataList);
    }

    private static final String GENRE_LIST_META_DATA_ID = "id";
    private static final String GENRE_LIST_META_DATA_TITLE = "title";
    private static final String GENRE_LIST_META_DATA_R_VALUE = "r_value";
    private static final String GENRE_LIST_META_DATA_SUB = "sub";
    //サブコンテンツキー名
    private static final String mCommonData[] = {GENRE_LIST_META_DATA_ID,
            GENRE_LIST_META_DATA_TITLE, GENRE_LIST_META_DATA_R_VALUE};

    /**
     * キーとキーの値をメンバーにセットする
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(String key, Object data) {
        if (key.isEmpty()) {
            return;
        } else {
            switch (key) {
                case GENRE_LIST_META_DATA_ID:
                    mId = (String) data;
                    break;
                case GENRE_LIST_META_DATA_TITLE:
                    mTitle = (String) data;
                    break;
                case GENRE_LIST_META_DATA_R_VALUE:
                    mRValue = (String) data;
                    break;
                default:
            }
        }
    }

    /**
     * キーの値を取得する
     *
     * @param key キー
     */
    public Object getMember(String key) {
        if (key.isEmpty()) {
            return "";
        } else {
            switch (key) {
                case GENRE_LIST_META_DATA_ID:
                    return mId;
                case GENRE_LIST_META_DATA_TITLE:
                    return mTitle;
                case GENRE_LIST_META_DATA_R_VALUE:
                    return mRValue;
                default:
                    return "";
            }
        }
    }

    /**
     * サーバから取得したデータをセット
     *
     * @param jsonObj Jsonオブジェクト
     */
    public void setData(JSONObject jsonObj) {
        GenreListMetaData metaData;
        try {
            if (jsonObj != null) {
                // 単一データ
                for (String item : mCommonData) {
                    if (!jsonObj.isNull(item)) {
                        setMember(item, jsonObj.get(item));
                    }
                }
                // サブジャンルデータ
                if (!jsonObj.isNull(GENRE_LIST_META_DATA_SUB)) {
                    JSONArray sub = jsonObj.getJSONArray(GENRE_LIST_META_DATA_SUB);
                    if (sub.length() == 0) {
                        return;
                    }
                    mSubContent = new ArrayList<GenreListMetaData>();
                    for (int i = 0; i < sub.length(); i++) {
                        metaData = new GenreListMetaData();
                        metaData.setData(sub.getJSONObject(i));
                        mSubContent.add(metaData);
                    }
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }
}