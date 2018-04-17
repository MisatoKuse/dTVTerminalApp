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

/**
 * ジャンル毎コンテンツ一覧メタデータ.
 */
public class GenreListMetaData implements Serializable {
    private static final long serialVersionUID = -4466388996087422463L;
    /** NODジャンルIDキー名.*/
    public static final String VIDEO_LIST_GENRE_ID_NOD = "genre_id_nod";
    /** VODジャンルIDキー名.*/
    public static final String VIDEO_LIST_GENRE_ID_VOD = "genre_id_vod";
    /**DTVジャンルIDキー名.*/
    public static final String VIDEO_LIST_GENRE_ID_DTV = "genre_id_dtv";
    /**全コンテンツ.*/
    public static final String VIDEO_LIST_GENRE_ID_ALL_CONTENTS = "genre_id_all_contents";
    /**id.*/
    private String mId;
    /**タイトル.*/
    private String mTitle;
    /**パレンタル情報.*/
    private String mRValue;
    /**サブコンテンツ.*/
    private ArrayList<GenreListMetaData> mSubContent = null;
    /**サブコンテンツ初期化.*/
    public GenreListMetaData() {
        mSubContent = new ArrayList<GenreListMetaData>();
    }

    /**
     * IDを取得.
     * @return ID
     */
    public String getId() {
        return mId;
    }

    /**
     * IDを設定.
     * @param mId ID
     */
    public void setId(final String mId) {
        this.mId = mId;
    }
    /**
     * タイトルを取得する.
     * @return  mTitle タイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * タイトルを設定する.
     * @param mTitle タイトル
     */
    public void setTitle(final String mTitle) {
        this.mTitle = mTitle;
    }
    /**
     * パレンタル情報を取得する.
     * @return  パレンタル情報
     */
    public String getRValue() {
        return mRValue;
    }

    /**
     * パレンタル情報を設定する.
     * @param mRValue パレンタル情報
     */
    public void setRValue(final String mRValue) {
        this.mRValue = mRValue;
    }
    /**
     * サブコンテンツ取得.
     * @return サブコンテンツ
     */
    public ArrayList<GenreListMetaData> getSubContent() {
        return this.mSubContent;
    }

    /**
     *  サブコンテンツ設定.
     * @param metaDataList サブコンテンツ
     */
    public void setSubContentAll(final ArrayList<GenreListMetaData> metaDataList) {
        mSubContent.addAll(metaDataList);
    }

    /**idキー名.*/
    private static final String GENRE_LIST_META_DATA_ID = "id";
    /**titleキー名.*/
    private static final String GENRE_LIST_META_DATA_TITLE = "title";
    /**パレンタル情報キー名.*/
    private static final String GENRE_LIST_META_DATA_R_VALUE = "r_value";
    /**サブキー名.*/
    private static final String GENRE_LIST_META_DATA_SUB = "sub";
    /**共通コンテンツキー.*/
    private static final String[] mCommonData = {GENRE_LIST_META_DATA_ID,
            GENRE_LIST_META_DATA_TITLE, GENRE_LIST_META_DATA_R_VALUE};

    /**
     * キーとキーの値をメンバーにセットする.
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(final String key, final Object data) {
        if (!key.isEmpty()) {
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
     * サーバから取得したデータをセット.
     *
     * @param jsonObj Jsonオブジェクト
     */
    public void setData(final JSONObject jsonObj) {
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