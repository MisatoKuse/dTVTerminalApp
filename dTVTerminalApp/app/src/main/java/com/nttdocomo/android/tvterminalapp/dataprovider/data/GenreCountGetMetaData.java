/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * ジャンル毎コンテンツ数取得メタデータ.
 */
public class GenreCountGetMetaData implements Serializable {
    private static final long serialVersionUID = -2522621200651636807L;
    /**
     * ジャンルID.
     */
    private String mGenreId;

    /**
     * コンテンツ数.
     */
    private int mCount;
    /**
     * ジャンルIDパラメータキー名.
     */
    private static final String GENRE_COUNT_GET_META_DATA_GENRE_ID = "genre_id";
    /**
     * コンテンツ数パラメータキー名.
     */
    private static final String GENRE_COUNT_GET_META_DATA_COUNT = "count";
    /**
     * ルートパラメータキー.
     */
    private static final String[] mRootPara = {GENRE_COUNT_GET_META_DATA_GENRE_ID, GENRE_COUNT_GET_META_DATA_COUNT};
    /**
     * ジャンルID取得.
     * @return ジャンルID
     */
    public String getGenreId() {
        return mGenreId;
    }
    /**
     * ジャンルID設定.
     * @param mGenreId ジャンルID
     */
    public void setGenreId(final String mGenreId) {
        this.mGenreId = mGenreId;
    }

    /**
     * コンテンツ数取得.
     * @return コンテンツ数
     */
    public int getCount() {
        return mCount;
    }

    /**
     * コンテンツ数設定.
     * @param mCount コンテンツ数
     */
    public void setCount(final int mCount) {
        this.mCount = mCount;
    }

    /**
     * キーとキーの値をメンバーにセットする.
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(final String key, final Object data) {
        if (!key.isEmpty()) {
            switch (key) {
                case GENRE_COUNT_GET_META_DATA_GENRE_ID:
                    mGenreId = (String) data;
                    break;
                case GENRE_COUNT_GET_META_DATA_COUNT:
                    mCount = DataBaseUtils.getNumeric(data);
                    break;
                default:
            }
        }
    }

    /**
     * キーの値を取得する.
     *
     * @param key キー
     * @return キー
     */
    public Object getMember(final String key) {
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
     * サーバから取得したデータをセット.
     *
     * @param jsonObj Jsonオブジェクト
     */
    public void setData(final JSONObject jsonObj) {
        DTVTLogger.start();
        if (jsonObj != null) {
            //ジャンル毎コンテンツ数
            for (String item : mRootPara) {
                if (!jsonObj.isNull(item)) {
                    try {
                        setMember(item, jsonObj.get(item));
                    } catch (JSONException e) {
                        DTVTLogger.end();
                    }
                }
            }
        }
    }
}
