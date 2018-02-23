/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class GenreCountGetMetaData implements Serializable {
    private static final long serialVersionUID = -2522621200651636807L;
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SET_DATA = ".setData";
    private String mGenreId;//ジャンルID
    private int mCount;//コンテンツ数

    private static final String GENRE_COUNT_GET_META_DATA_GENRE_ID = "genre_id";
    private static final String GENRE_COUNT_GET_META_DATA_COUNT = "count";
    private static final String[] mRootPara = {GENRE_COUNT_GET_META_DATA_GENRE_ID, GENRE_COUNT_GET_META_DATA_COUNT};

    public String getGenreId() {
        return mGenreId;
    }

    public void setGenreId(String mGenreId) {
        this.mGenreId = mGenreId;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

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
                case GENRE_COUNT_GET_META_DATA_GENRE_ID:
                    mGenreId = (String) data;
                    break;
                case GENRE_COUNT_GET_META_DATA_COUNT:
                    mCount = DBUtils.getNumeric(data);
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
                case GENRE_COUNT_GET_META_DATA_GENRE_ID:
                    return mGenreId;
                case GENRE_COUNT_GET_META_DATA_COUNT:
                    return mCount;
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
        if (jsonObj != null) {
            //ジャンル毎コンテンツ数
            for (String item : mRootPara) {
                if (!jsonObj.isNull(item)) {
                    try {
                        setMember(item, jsonObj.get(item));
                    } catch (JSONException e) {
                        DTVTLogger.debug(CLASS_NAME + SET_DATA, e);
                    }
                }
            }
        }
    }
}
