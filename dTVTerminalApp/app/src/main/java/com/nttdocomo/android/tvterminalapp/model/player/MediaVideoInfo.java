/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.model.player;

import android.net.Uri;

import com.digion.dixim.android.secureplayer.MediaInfo;

/**
 * 機能：再生するヴィデオ属性クラス
 */
public class MediaVideoInfo implements MediaInfo {

	private final Uri mUri;
	private final String mMimeType;
	private final long mSize;
	private final long mDurationMs;
	private final int mBytesPerSec;
	private final boolean mIsAvailableConnectionStalling;
	private final boolean mIsSupportedByteSeek;
	private final boolean mIsSupportedTimeSeek;
	private final String mContentFormat;
	private final boolean mIsVideoBroadcast;
	private final boolean mIsRemote;
	private final String mTitle;

	/**
	 * 機能：コンストラクタ
	 * @param uri メディアのURI
	 * @param mimeType メディアのMimeType
	 * @param size メディアのサイズ(Byte)
	 * @param durationMs メディアの総再生時間(ms)
	 * @param bytesPerSec メディアのビットレート(Byte/sec)
	 * @param isSupportedByteSeek DLNAのByteシークをサポートしているか
	 * @param isSupportedTimeSeek DLNAのTimeシークをサポートしているか
	 * @param isAvailableConnectionStalling DLNAのAvailableConnectionStallingかどうか
	 * @param isVideoBroadcast 放送中コンテンツかどうか
	 * @param isRemote リモートアクセスコンテンツかどうか
	 * @param title メディアのタイトル
	 * @param contentFormat DIDLのres protocolInfoの3番目のフィールド
	 */
	public MediaVideoInfo(Uri uri,
                          String mimeType,
                          long size,
                          long durationMs,
                          int bytesPerSec,
                          boolean isSupportedByteSeek,
                          boolean isSupportedTimeSeek,
                          boolean isAvailableConnectionStalling,
                          boolean isVideoBroadcast,
                          boolean isRemote, String title,
                          String contentFormat) {
		this.mUri = uri;
		this.mMimeType = mimeType;
		this.mSize = size;
		this.mDurationMs = durationMs;
		if (size > 0 && durationMs > 0) {
			this.mBytesPerSec = (int) (size * 1000 / durationMs);
		} else {
			this.mBytesPerSec = bytesPerSec;
		}
		this.mIsSupportedByteSeek = isSupportedByteSeek;
		this.mIsSupportedTimeSeek = isSupportedTimeSeek;
		this.mIsAvailableConnectionStalling = isAvailableConnectionStalling;
		this.mContentFormat = contentFormat;
		this.mIsVideoBroadcast = isVideoBroadcast;
		this.mIsRemote = isRemote;
		this.mTitle = title;
	}

	/**
	 * 機能：「mUri」を戻す
	 * @return mUri
	 */
	@Override
	public Uri getUri() {
		return mUri;
	}

	/**
	 * 機能：「mMimeType」を戻す
	 * @return mMimeType
	 */
	@Override
	public String getMimeType() {
		return mMimeType;
	}

	/**
	 * 機能：「mSize」を戻す
	 * @return mSize
	 */
	@Override
	public long getSize() {
		return mSize;
	}

	/**
	 * 機能：「mDurationMs」を戻す
	 * @return mDurationMs
	 */
	@Override
	public long getDurationMs() {
		return mDurationMs;
	}

	/**
	 * 機能：「mBytesPerSec」を戻す
	 * @return mBytesPerSec
	 */
	@Override
	public int getBytesPerSec() {
		return mBytesPerSec;
	}

	/**
	 * 機能：「mIsSupportedByteSeek」を戻す
	 * @return mIsSupportedByteSeek
	 */
	@Override
	public boolean isSupportedByteSeek() {
		return mIsSupportedByteSeek;
	}

	/**
	 * 機能：「mIsSupportedTimeSeek」を戻す
	 * @return mIsSupportedTimeSeek
	 */
	@Override
	public boolean isSupportedTimeSeek() {
		return mIsSupportedTimeSeek;
	}

	/**
	 * 機能：「mIsAvailableConnectionStalling」を戻す
	 * @return mIsAvailableConnectionStalling
	 */
	@Override
	public boolean isAvailableConnectionStalling() {
		return mIsAvailableConnectionStalling;
	}

	/**
	 * 機能：「mContentFormat」を戻す
	 * @return mContentFormat
	 */
	@Override
	public String getContentFormat() {
		return mContentFormat;
	}

	/**
	 * 機能：「mIsVideoBroadcast」を戻す
	 * @return mIsVideoBroadcast
	 */
	@Override
	public boolean isVideoBroadcast() {
		return mIsVideoBroadcast;
	}

	/**
	 * 機能：「mIsRemote」を戻す
	 * @return mIsRemote
	 */
	@Override
	public boolean isRemote() {
		return mIsRemote;
	}

	/**
	 * 機能：「mTitle」を戻す
	 * @return mTitle
	 */
	@Override
	public String getTitle() {
		return mTitle;
	}
}
