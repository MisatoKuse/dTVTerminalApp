/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

/**
 * ダウンロードデータ管理クラス.
 */
public class DownloadData {
    /**アイテムID.*/
    private String mItemId;
    /**メディアのURI.*/
    private String mUrl;
    /**Didl.*/
    private String mDidl;
    /**Host.*/
    private String mHost;
    /**Port.*/
    private String mPort;
    /**SaveFile.*/
    private String mSaveFile;
    /**メディアのMimeType.*/
    private String mMimeType;
    /**ダウンロードサイズ.*/
    private String mDownLoadSize;
    /**ダウンロードステータス.*/
    private String mDownLoadStatus;
    /**トータルサイズ.*/
    private String mTotalSize;
    /**メディアの総再生時間(ms).*/
    private String mDuration;
    /**解像度.*/
    private String mResolution;
    /**UpnpIcon.*/
    private String mUpnpIcon;
    /**メディアのビットレート(Byte/sec).*/
    private String mBitrate;
    /**DLNAのByteシークをサポートしているか.*/
    private String mIsSupportedByteSeek;
    /**DLNAのTimeシークをサポートしているか.*/
    private String mIsSupportedTimeSeek;
    /**DLNAのAvailableConnectionStallingかどうか.*/
    private String mIsAvailableConnectionStalling;
    /**放送中か.*/
    private String mIsLiveMode;
    /**リモートアクセスコンテンツかどうか.*/
    private String mIsRemote;
    /**メディアのタイトル.*/
    private String mTitle;
    /**DIDLのres protocolInfoの3番目のフィールド.*/
    private String mContentFormat;
    /**ビデオタイプ.*/
    private String mVideoType;
    /**パーセント.*/
    private String mPercentToNotify;
    /**ダウンロードパラメータ.*/
    private String mXmlToDownLoad;
    /**チャンネル名.*/
    private String mChannelName;
    /**コンテンツ日付.*/
    private String mDate;

    /**
     *Getter.
     * @return アイテムID
     */
    public String getItemId() {
        return mItemId;
    }

    /**
     * Setter.
     * @param mItemId アイテムID
     */
    public void setItemId(final String mItemId) {
        this.mItemId = mItemId;
    }

    /**
     *Getter.
     * @return メディアのURI
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     *Setter.
     * @param mUrl メディアのURI
     */
    public void setUrl(final String mUrl) {
        this.mUrl = mUrl;
    }

    /**
     *Getter.
     * @return mDidl
     */
    public String getDidl() {
        return mDidl;
    }

    /**
     *Setter.
     * @param mDidl Didl
     */
    public void setDidl(final String mDidl) {
        this.mDidl = mDidl;
    }

    /**
     * Getter.
     * @return Host
     */
    public String getHost() {
        return mHost;
    }

    /**
     * Setter.
     * @param mHost Host
     */
    public void setHost(final String mHost) {
        this.mHost = mHost;
    }

    /**
     *Getter.
     * @return Port
     */
    public String getPort() {
        return mPort;
    }

    /**
     * Setter.
     * @param mPort Port
     */
    public void setPort(final String mPort) {
        this.mPort = mPort;
    }

    /**
     * Getter.
     * @return SaveFile
     */
    public String getSaveFile() {
        return mSaveFile;
    }

    /**
     * Setter.
     * @param mSaveFile SaveFile
     */
    public void setSaveFile(final String mSaveFile) {
        this.mSaveFile = mSaveFile;
    }

    /**
     * Getter.
     * @return メディアのMimeType
     */
    public String getMimeType() {
        return mMimeType;
    }

    /**
     * Setter.
     * @param mMimeType メディアのMimeType
     */
    public void setMimeType(final String mMimeType) {
        this.mMimeType = mMimeType;
    }

    /**
     * Getter.
     * @return ダウンロードサイズ
     */
    public String getDownLoadSize() {
        return mDownLoadSize;
    }

    /**
     * Setter.
     * @param mDownLoadSize ダウンロードサイズ
     */
    public void setDownLoadSize(final String mDownLoadSize) {
        this.mDownLoadSize = mDownLoadSize;
    }

    /**
     * Getter.
     * @return ダウンロードステータス
     */
    public String getDownLoadStatus() {
        return mDownLoadStatus;
    }

    /**
     * Setter.
     * @param mDownLoadStatus ダウンロードステータス
     */
    public void setDownLoadStatus(final String mDownLoadStatus) {
        this.mDownLoadStatus = mDownLoadStatus;
    }

    /**
     * Getter.
     * @return トータルサイズ
     */
    public String getTotalSize() {
        return mTotalSize;
    }

    /**
     * Setter.
     * @param mTotalSize トータルサイズ
     */
    public void setTotalSize(final String mTotalSize) {
        this.mTotalSize = mTotalSize;
    }

    /**
     * Getter.
     * @return メディアの総再生時間(ms)
     */
    public String getDuration() {
        return mDuration;
    }

    /**
     * Setter.
     * @param mDuration メディアの総再生時間(ms)
     */
    public void setDuration(final String mDuration) {
        this.mDuration = mDuration;
    }

    /**
     * Getter.
     * @return 解像度
     */
    public String getResolution() {
        return mResolution;
    }

    /**
     * Setter.
     * @param mResolution 解像度
     */
    public void setResolution(final String mResolution) {
        this.mResolution = mResolution;
    }

    /**
     * Getter.
     * @return UpnpIcon
     */
    public String getUpnpIcon() {
        return mUpnpIcon;
    }

    /**
     * Setter.
     * @param mUpnpIcon UpnpIcon
     */
    public void setUpnpIcon(final String mUpnpIcon) {
        this.mUpnpIcon = mUpnpIcon;
    }

    /**
     * Getter.
     * @return メディアのビットレート;(Byte/sec)
     */
    public String getBitrate() {
        return mBitrate;
    }

    /**
     * Setter.
     * @param mBitrate メディアのビットレート;(Byte/sec)
     */
    public void setBitrate(final String mBitrate) {
        this.mBitrate = mBitrate;
    }

    /**
     * Getter.
     * @return DLNAのByteシークをサポートしているか
     */
    public String getIsSupportedByteSeek() {
        return mIsSupportedByteSeek;
    }

    /**
     * Setter.
     * @param mIsSupportedByteSeek DLNAのByteシークをサポートしているか
     */
    public void setIsSupportedByteSeek(final String mIsSupportedByteSeek) {
        this.mIsSupportedByteSeek = mIsSupportedByteSeek;
    }

    /**
     * Getter.
     * @return DLNAのTimeシークをサポートしているか
     */
    public String getIsSupportedTimeSeek() {
        return mIsSupportedTimeSeek;
    }

    /**
     * Setter.
     * @param mIsSupportedTimeSeek DLNAのTimeシークをサポートしているか
     */
    public void setIsSupportedTimeSeek(final String mIsSupportedTimeSeek) {
        this.mIsSupportedTimeSeek = mIsSupportedTimeSeek;
    }

    /**
     * Getter.
     * @return DLNAのAvailableConnectionStallingかどうか
     */
    public String getIsAvailableConnectionStalling() {
        return mIsAvailableConnectionStalling;
    }

    /**
     * Setter.
     * @param mIsAvailableConnectionStalling DLNAのAvailableConnectionStallingかどうか
     */
    public void setIsAvailableConnectionStalling(final String mIsAvailableConnectionStalling) {
        this.mIsAvailableConnectionStalling = mIsAvailableConnectionStalling;
    }

    /**
     * Getter.
     * @return 放送中か
     */
    public String getIsLiveMode() {
        return mIsLiveMode;
    }

    /**
     * Setter.
     * @param mIsLiveMode 放送中か
     */
    public void setIsLiveMode(final String mIsLiveMode) {
        this.mIsLiveMode = mIsLiveMode;
    }

    /**
     * Getter.
     * @return リモートアクセスコンテンツかどうか
     */
    public String getIsRemote() {
        return mIsRemote;
    }

    /**
     * Setter.
     * @param mIsRemote リモートアクセスコンテンツかどうか
     */
    public void setIsRemote(final String mIsRemote) {
        this.mIsRemote = mIsRemote;
    }

    /**
     * Getter.
     * @return メディアのタイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Setter.
     * @param mTitle メディアのタイトル
     */
    public void setTitle(final String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * Getter.
     * @return DIDLのres protocolInfoの3番目のフィールド
     */
    public String getContentFormat() {
        return mContentFormat;
    }

    /**
     * Setter.
     * @param mContentFormat DIDLのres protocolInfoの3番目のフィールド
     */
    public void setContentFormat(final String mContentFormat) {
        this.mContentFormat = mContentFormat;
    }

    /**
     * Getter.
     * @return  ビデオタイプ
     */
    public String getVideoType() {
        return mVideoType;
    }

    /**
     * Setter.
     * @param mVideoType ビデオタイプ
     */
    public void setVideoType(final String mVideoType) {
        this.mVideoType = mVideoType;
    }

    /**
     * Getter.
     * @return パーセント
     */
    public String getPercentToNotify() {
        return mPercentToNotify;
    }

    /**
     * Setter.
     * @param mPercentToNotify パーセント
     */
    public void setPercentToNotify(final String mPercentToNotify) {
        this.mPercentToNotify = mPercentToNotify;
    }

    /**
     * Setter.
     * @param xml ダウンロードパラメータ
     */
    public void setXmlToDownLoad(final String xml) {
        mXmlToDownLoad = xml;
    }

    /**
     * Getter.
     * @return ダウンロードパラメータ
     */
    public String getXmlToDownLoad() {
        return mXmlToDownLoad;
    }

    /**
     * Getter.
     * @return チャンネル名
     */
    public String getChannelName() {
        return mChannelName;
    }

    /**
     * Setter.
     * @param mChannelName チャンネル名
     */
    public void setChannelName(final String mChannelName) {
        this.mChannelName = mChannelName;
    }

    /**
     * Getter.
     * @return 日付
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Setter.
     * @param mDate 日付
     */
    public void setDate(final String mDate) {
        this.mDate = mDate;
    }
}
