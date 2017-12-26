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
    private String mId;//id
    private String mTitle;//title
    private String mRValue;//r_value
    private ArrayList<SubContent> mSubContent;

    public GenreListMetaData() {
        mSubContent = new ArrayList<SubContent>();
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

    public ArrayList<SubContent> getSubContent() {
        return this.mSubContent;
    }

    public static final String GENRE_LIST_META_DATA_ID = "id";
    public static final String GENRE_LIST_META_DATA_TITLE = "title";
    public static final String GENRE_LIST_META_DATA_R_VALUE = "r_value";
    public static final String GENRE_LIST_META_DATA_SUB = "sub";
    //サブコンテンツキー名
    public static final String mCommonData[] = {GENRE_LIST_META_DATA_ID,
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
        SubContent subContent;
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
                    for (int i = 0; i < sub.length(); i++) {
                        subContent = new SubContent();
                        subContent.setSubData(sub.getJSONObject(i));
                        mSubContent.add(subContent);
                    }
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    //サブジャンルコンテンツ
    public static class SubContent implements Serializable {

        private static final long serialVersionUID = 9019948926533001525L;
        private String mId;
        private String mTitle;
        private String mRValue;

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

        /**
         * サーバから取得したデータをセット
         *
         * @param jsonObj Jsonオブジェクト
         */
        void setSubData(JSONObject jsonObj) {
            //サブジャンルデータ
            try {
                if (jsonObj != null) {
                    for (String item : mCommonData) {
                        if (!jsonObj.isNull(item)) {
                            setSubMember(item, jsonObj.get(item));
                        }
                    }
                }
            } catch (JSONException e) {
                DTVTLogger.debug(e);
            }
        }

        /**
         * キーとキーの値をメンバーにセットする
         *
         * @param key  キー
         * @param data キーの値
         */
        private void setSubMember(String key, Object data) {
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
    }
}