/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 録画コンテンツ詳細データ.
 */
public class RecordedContentsDetailData implements Parcelable {
    /**
     * パラメータタイプ.
     */
    public enum DetailParamFromWhere {
        /**地上波.*/
        DetailParamFromWhere_ChList_TabTer,
        /**BS.*/
        DetailParamFromWhere_ChList_TabBs,
        /**他.*/
        DetailParamFromWhere_Other,
    }
    /**アイテムID.*/
    private String mItemId;
    /**コンテンツサイズ.*/
    private String mSize;
    /**コンテンツの総再生時間(ms).*/
    private String mDuration;
    /**解像度.*/
    private String mResolution;
    /**ビット毎秒.*/
    private String mBitrate;
    /**Url.*/
    private String mResUrl;
    /**.*/
    private String mUpnpIcon;
    /**タイトル.*/
    private String mTitle;
    /**.*/
    private DetailParamFromWhere mDetailParamFromWhere = DetailParamFromWhere.DetailParamFromWhere_Other;
    /**ビデオタイプ.*/
    private String mVideoType;
    /**clearTextSize.*/
    private String mClearTextSize;
    /**ダウンロードステータス.*/
    private int mDownLoadStatus;
    /**ダウンロードコンテンツ格納パスー.*/
    private String mDlFileFullPath;
    /**isLive.*/
    private boolean mIsLive = false;

    /**
     *isLive取得.
     * @return isLive
     */
    public boolean isIsLive() {
        return mIsLive;
    }

    /**
     * isLive設定.
     * @param mIsLive isLive
     */
    public void setIsLive(final boolean mIsLive) {
        this.mIsLive = mIsLive;
    }

    /**
     * パラメータタイプ設定.
     * @param from パラメータタイプ
     */
    public void setDetailParamFromWhere(final DetailParamFromWhere from) {
        mDetailParamFromWhere = from;
    }

    /**
     * パラメータタイプ取得.
     * @return パラメータタイプ
     */
    public DetailParamFromWhere getDetailParamFromWhere() {
        return mDetailParamFromWhere;
    }

    /**
     *アイテムID取得.
     * @return アイテムID
     */
    public String getItemId() {
        return mItemId;
    }

    /**
     * アイテムID設定 .
     * @param mItemId アイテムID
     */
    public void setItemId(final String mItemId) {
        this.mItemId = mItemId;
    }

    /**
     *タイトル取得.
     * @return タイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     *タイトル設定.
     * @param mTitle タイトル
     */
    public void setTitle(final String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     *コンテンツサイズ設定.
     * @param mSize コンテンツサイズ
     */
    public void setSize(final String mSize) {
        this.mSize = mSize;
    }

    /**
     *コンテンツサイズ取得.
     * @return コンテンツサイズ
     */
    public String getSize() {
        return mSize;
    }

    /**
     * 総再生時間(ms)を設定 .
     * @param mDuration 総再生時間(ms)
     */
    public void setDuration(final String mDuration) {
        this.mDuration = mDuration;
    }

    /**
     *総再生時間(ms)取得.
     * @return 総再生時間(ms)
     */
    public String getDuration() {
        return mDuration;
    }

    /**
     * 解像度設定.
     * @param mResolution 解像度
     */
    public void setResolution(final String mResolution) {
        this.mResolution = mResolution;
    }

    /**
     * 解像度取得.
     * @return 解像度
     */
    public String getResolution() {
        return mResolution;
    }

    /**
     *ビット毎秒設定.
     * @param mBitrate ビット毎秒
     */
    public void setBitrate(final String mBitrate) {
        this.mBitrate = mBitrate;
    }

    /**
     * ビット毎秒取得.
     * @return ビット毎秒
     */
    public String getBitrate() {
        return mBitrate;
    }

    /**
     * ダウンロードUrl取得.
     * @return ダウンロードUrl
     */
    public String getResUrl() {
        return mResUrl;
    }

    /**
     * ダウンロードUrl設定.
     * @param mResUrl ダウンロードUrl
     */
    public void setResUrl(final String mResUrl) {
        this.mResUrl = mResUrl;
    }

    /**
     * upnpアイコン取得.
     * @return upnpアイコン
     */
    public String getUpnpIcon() {
        return mUpnpIcon;
    }

    /**
     *upnpアイコン設定.
     * @param mUpnpIcon upnpアイコン
     */
    public void setUpnpIcon(final String mUpnpIcon) {
        this.mUpnpIcon = mUpnpIcon;
    }

    /**
     *ビデオタイプ取得.
     * @return ビデオタイプ
     */
    public String getVideoType() {
        return mVideoType;
    }

    /**
     *ビデオタイプ設定.
     * @param type ビデオタイプ
     */
    public void setVideoType(final String type) {
        this.mVideoType = type;
    }

    /**
     *ダウンロードサイズ取得.
     * @return ダウンロードサイズ
     */
    public String getClearTextSize() {
        return mClearTextSize;
    }

    /**
     * ダウンロードサイズ.
     * @param clearTextSize ダウンロードサイズ
     */
    public void setClearTextSize(final String clearTextSize) {
        this.mClearTextSize = clearTextSize;
    }

    /**
     * ダウンロードステータス取得.
     * @return ダウンロードステータス
     */
    public int getDownLoadStatus() {
        return mDownLoadStatus;
    }

    /**
     *ダウンロードステータス設定.
     * @param mDownLoadStatus ダウンロードステータス
     */
    public void setDownLoadStatus(final int mDownLoadStatus) {
        this.mDownLoadStatus = mDownLoadStatus;
    }

    /**
     * ダウンロードコンテンツ格納パスーを取得.
     * @return 格納パスー
     */
    public String getDlFileFullPath() {
        return mDlFileFullPath;
    }

    /**
     * ダウンロードコンテンツ格納パスーを設定.
     * @param dlFileFullPath 格納パスー
     */
    public void setDlFileFullPath(final String dlFileFullPath) {
        this.mDlFileFullPath = dlFileFullPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.mItemId);
        dest.writeString(this.mSize);
        dest.writeString(this.mDuration);
        dest.writeString(this.mResolution);
        dest.writeString(this.mBitrate);
        dest.writeString(this.mResUrl);
        dest.writeString(this.mUpnpIcon);
        dest.writeString(this.mTitle);
        dest.writeInt(this.mDetailParamFromWhere == null ? -1 : this.mDetailParamFromWhere.ordinal());
        dest.writeString(this.mVideoType);
        dest.writeString(this.mClearTextSize);
        dest.writeInt(this.mDownLoadStatus);
        dest.writeString(this.mDlFileFullPath);
        dest.writeByte(this.mIsLive ? (byte) 1 : (byte) 0);
    }
    /**コンストラクタ.*/
    public RecordedContentsDetailData() {
    }

    /**
     * レコメンドコンテンツ詳細データ.
     * @param in source
     */
    RecordedContentsDetailData(final Parcel in) {
        this.mItemId = in.readString();
        this.mSize = in.readString();
        this.mDuration = in.readString();
        this.mResolution = in.readString();
        this.mBitrate = in.readString();
        this.mResUrl = in.readString();
        this.mUpnpIcon = in.readString();
        this.mTitle = in.readString();
        int tmpMDetailParamFromWhere = in.readInt();
        this.mDetailParamFromWhere = tmpMDetailParamFromWhere == -1 ? null : DetailParamFromWhere.values()[tmpMDetailParamFromWhere];
        this.mVideoType = in.readString();
        this.mClearTextSize = in.readString();
        this.mDownLoadStatus = in.readInt();
        this.mDlFileFullPath = in.readString();
        this.mIsLive = in.readByte() != 0;
    }
    /**コンストラクタ.*/
    public static final Creator<RecordedContentsDetailData> CREATOR = new Creator<RecordedContentsDetailData>() {
        @Override
        public RecordedContentsDetailData createFromParcel(final Parcel source) {
            return new RecordedContentsDetailData(source);
        }

        @Override
        public RecordedContentsDetailData[] newArray(final int size) {
            return new RecordedContentsDetailData[size];
        }
    };
}