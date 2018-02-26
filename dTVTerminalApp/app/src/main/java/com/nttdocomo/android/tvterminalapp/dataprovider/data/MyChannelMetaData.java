/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MyChannelMetaData implements Serializable {
    private static final long serialVersionUID = -5113796911971449210L;
    private final String CLASS_NAME = getClass().getSimpleName();
    private static final String SET_DATA = ".setData";
    //仕様書の名前に合わせているので、フルスペルではない変数名もあります

    private String mServiceId;
    private String mTitle;
    private String mRValue;
    private String mAdultType;
    private String mIndex;

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(String mServiceId) {
        this.mServiceId = mServiceId;
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

    public String getAdultType() {
        return mAdultType;
    }

    public void setAdultType(String mAdultType) {
        this.mAdultType = mAdultType;
    }

    public String getIndex() {
        return mIndex;
    }

    public void setIndex(String mIndex) {
        this.mIndex = mIndex;
    }

    private static final String MY_CHANNEL_META_DATA_SERVICE_ID = "service_id"; //サービスID
    private static final String MY_CHANNEL_META_DATA_TITLE = "title"; //チャンネル名
    private static final String MY_CHANNEL_META_DATA_R_VALUE = "r_value"; //番組のパレンタル設定値
    private static final String MY_CHANNEL_META_DATA_ADULT_TYPE = "adult_type"; //チャンネルのアダルトタイプ
    private static final String MY_CHANNEL_META_DATA_ADULT_INDEX = "index"; //マイチャンネル登録位置

    private static final String[] mRootPara = {MY_CHANNEL_META_DATA_SERVICE_ID,
            MY_CHANNEL_META_DATA_TITLE, MY_CHANNEL_META_DATA_R_VALUE,
            MY_CHANNEL_META_DATA_ADULT_TYPE, MY_CHANNEL_META_DATA_ADULT_INDEX};

    /**
     * キーとキーの値をメンバーにセットする
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(String key, Object data) {
        if (!key.isEmpty()) {
            switch (key) {
                case MY_CHANNEL_META_DATA_SERVICE_ID:
                    mServiceId = String.valueOf(data);             //service_id
                    break;
                case MY_CHANNEL_META_DATA_TITLE:
                    mTitle = String.valueOf(data);             //title
                    break;
                case MY_CHANNEL_META_DATA_R_VALUE:
                    mRValue = String.valueOf(data);             //r_value
                    break;
                case MY_CHANNEL_META_DATA_ADULT_TYPE:
                    mAdultType = String.valueOf(data);             //adult_type
                    break;
                case MY_CHANNEL_META_DATA_ADULT_INDEX:
                    mIndex = String.valueOf(data);              //index
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
                case MY_CHANNEL_META_DATA_SERVICE_ID:
                    return mServiceId;             //service_id
                case MY_CHANNEL_META_DATA_TITLE:
                    return mTitle;                 //title
                case MY_CHANNEL_META_DATA_R_VALUE:
                    return mRValue;                //r_value
                case MY_CHANNEL_META_DATA_ADULT_TYPE:
                    return mAdultType;            //adult_type
                case MY_CHANNEL_META_DATA_ADULT_INDEX:
                    return mIndex;                //index
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
            // 単一データ
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
