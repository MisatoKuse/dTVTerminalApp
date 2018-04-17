/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * マイチャンネルメタデータ.
 */
public class MyChannelMetaData implements Serializable {
    private static final long serialVersionUID = -5113796911971449210L;
    //仕様書の名前に合わせているので、フルスペルではない変数名もあります
    /**サービスID.*/
    private String mServiceId;
    /**チャンネル名.*/
    private String mTitle;
    /**パレンタル情報.*/
    private String mRValue;
    /**チャンネルのアダルトタイプ.*/
    private String mAdultType;
    /**マイチャンネル登録位置.*/
    private String mIndex;

    /**
     * サービスID取得.
     * @return サービスID
     */
    public String getServiceId() {
        return mServiceId;
    }

    /**
     * サービスID設定.
     * @param mServiceId サービスID
     */
    public void setServiceId(final String mServiceId) {
        this.mServiceId = mServiceId;
    }

    /**
     * チャンネル名取得.
     * @return チャンネル名
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * チャンネル名設定.
     * @param mTitle チャンネル名
     */
    public void setTitle(final String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * パレンタル情報取得.
     * @return パレンタル情報
     */
    public String getRValue() {
        return mRValue;
    }
    /**
     * パレンタル情報設定する.
     * @param mRValue パレンタル情報
     */
    public void setRValue(final String mRValue) {
        this.mRValue = mRValue;
    }

    /**
     * アダルトタイプ取得.
     * @return アダルトタイプ
     */
    public String getAdultType() {
        return mAdultType;
    }

    /**
     * アダルトタイプ設定する.
     * @param mAdultType アダルトタイプ
     */
    public void setAdultType(final String mAdultType) {
        this.mAdultType = mAdultType;
    }

    /**
     * マイチャンネル登録位置取得.
     * @return マイチャンネル登録位置
     */
    public String getIndex() {
        return mIndex;
    }

    /**
     * マイチャンネル登録位置設定.
     * @param mIndex マイチャンネル登録位置
     */
    public void setIndex(final String mIndex) {
        this.mIndex = mIndex;
    }

    /**サービスIDキー名.*/
    private static final String MY_CHANNEL_META_DATA_SERVICE_ID = "service_id";
    /**チャンネル名キー名.*/
    private static final String MY_CHANNEL_META_DATA_TITLE = "title";
    /**番組のパレンタル設定値キー名.*/
    private static final String MY_CHANNEL_META_DATA_R_VALUE = "r_value";
    /**チャンネルのアダルトタイプキー名.*/
    private static final String MY_CHANNEL_META_DATA_ADULT_TYPE = "adult_type";
    /**マイチャンネル登録位置キー名.*/
    private static final String MY_CHANNEL_META_DATA_ADULT_INDEX = "index"; //
    /**チャンネル情報キー名.*/
    private static final String[] mRootPara = {MY_CHANNEL_META_DATA_SERVICE_ID,
            MY_CHANNEL_META_DATA_TITLE, MY_CHANNEL_META_DATA_R_VALUE,
            MY_CHANNEL_META_DATA_ADULT_TYPE, MY_CHANNEL_META_DATA_ADULT_INDEX};

    /**
     * キーとキーの値をメンバーにセットする.
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(final String key, final Object data) {
        DTVTLogger.start();
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
        DTVTLogger.end();
    }

    /**
     * キーの値を取得する.
     *
     * @param key キー
     * @return key
     */
    public Object getMember(final String key) {
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
     * サーバから取得したデータをセット.
     *
     * @param jsonObj Jsonオブジェクト
     */
    public void setData(final JSONObject jsonObj) {
        DTVTLogger.start();
        if (jsonObj != null) {
            // 単一データ
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
