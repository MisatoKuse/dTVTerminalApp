/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.service.download;

public class DlData {
    private String mItemId;
    private String mUrl;
    private String mDidl;
    private String mHost;
    private String mPort;
    private String mSaveFile;
    private String mMimeType;
    private String mDownLoadSize;
    private String mDownLoadStatus;
    private String mTotalSize;
    private String mDuration;
    private String mResolution;
    private String mUpnpIcon;
    private String mBitrate;
    private String mIsSupportedByteSeek;
    private String mIsSupportedTimeSeek;
    private String mIsAvailableConnectionStalling;
    private String mIsLiveMode;
    private String mIsRemote;
    private String mTitle;
    private String mContentFormat;
    private String mVideoType;
    private String mPercentToNotify;
    private String mXmlToDl;

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(final String mItemId) {
        this.mItemId = mItemId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(final String mUrl) {
        this.mUrl = mUrl;
    }

    public String getDidl() {
        return mDidl;
    }

    public void setDidl(final String mDidl) {
        this.mDidl = mDidl;
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(final String mHost) {
        this.mHost = mHost;
    }

    public String getPort() {
        return mPort;
    }

    public void setPort(final String mPort) {
        this.mPort = mPort;
    }

    public String getSaveFile() {
        return mSaveFile;
    }

    public void setSaveFile(final String mSaveFile) {
        this.mSaveFile = mSaveFile;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public void setMimeType(final String mMimeType) {
        this.mMimeType = mMimeType;
    }

    public String getDownLoadSize() {
        return mDownLoadSize;
    }

    public void setDownLoadSize(final String mDownLoadSize) {
        this.mDownLoadSize = mDownLoadSize;
    }

    public String getDownLoadStatus() {
        return mDownLoadStatus;
    }

    public void setDownLoadStatus(final String mDownLoadStatus) {
        this.mDownLoadStatus = mDownLoadStatus;
    }

    public String getTotalSize() {
        return mTotalSize;
    }

    public void setTotalSize(final String mTotalSize) {
        this.mTotalSize = mTotalSize;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(final String mDuration) {
        this.mDuration = mDuration;
    }

    public String getResolution() {
        return mResolution;
    }

    public void setResolution(final String mResolution) {
        this.mResolution = mResolution;
    }

    public String getUpnpIcon() {
        return mUpnpIcon;
    }

    public void setUpnpIcon(final String mUpnpIcon) {
        this.mUpnpIcon = mUpnpIcon;
    }

    public String getBitrate() {
        return mBitrate;
    }

    public void setBitrate(final String mBitrate) {
        this.mBitrate = mBitrate;
    }

    public String getIsSupportedByteSeek() {
        return mIsSupportedByteSeek;
    }

    public void setIsSupportedByteSeek(final String mIsSupportedByteSeek) {
        this.mIsSupportedByteSeek = mIsSupportedByteSeek;
    }

    public String getIsSupportedTimeSeek() {
        return mIsSupportedTimeSeek;
    }

    public void setIsSupportedTimeSeek(final String mIsSupportedTimeSeek) {
        this.mIsSupportedTimeSeek = mIsSupportedTimeSeek;
    }

    public String getIsAvailableConnectionStalling() {
        return mIsAvailableConnectionStalling;
    }

    public void setIsAvailableConnectionStalling(final String mIsAvailableConnectionStalling) {
        this.mIsAvailableConnectionStalling = mIsAvailableConnectionStalling;
    }

    public String getIsLiveMode() {
        return mIsLiveMode;
    }

    public void setIsLiveMode(final String mIsLiveMode) {
        this.mIsLiveMode = mIsLiveMode;
    }

    public String getIsRemote() {
        return mIsRemote;
    }

    public void setIsRemote(final String mIsRemote) {
        this.mIsRemote = mIsRemote;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(final String mTitle) {
        this.mTitle = mTitle;
    }

    public String getContentFormat() {
        return mContentFormat;
    }

    public void setContentFormat(final String mContentFormat) {
        this.mContentFormat = mContentFormat;
    }

    public String getVideoType() {
        return mVideoType;
    }

    public void setVideoType(final String mVideoType) {
        this.mVideoType = mVideoType;
    }

    public String getPercentToNotify() {
        return mPercentToNotify;
    }

    public void setPercentToNotify(final String mPercentToNotify) {
        this.mPercentToNotify = mPercentToNotify;
    }

    public void setXmlToDl(final String xml) {
        mXmlToDl = xml;
    }

    public String getXmlToDl() {
        return mXmlToDl;
    }
}
