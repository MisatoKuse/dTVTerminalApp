/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

/**
 * 検索結果詳細クラス.
 */
public class SearchContentInfo {
    /**
     * クリップフラグ.
     */
    public boolean mClipFlag;
    /**
     * コンテンツID.
     */
    public String mContentId;
    /**
     * サービスID.
     */
    public int mServiceId;
    /**
     * カテゴリーID.
     */
    public String mCategoryId;
    /**
     * サムネイルURL1.
     */
    public String mContentPictureUrl1;
    /**
     * サムネイルURL2.
     */
    public String mContentPictureUrl2;
    /**
     * タイトル.
     */
    public String mTitle;
    /**
     * ランク.
     */
    public String mRank;
    /**
     *  mobileViewingFlg.
     */
    public String mMobileViewingFlg;
    /**
     *  開始時刻.
     */
    public String mStartViewing;
    /**
     *  終了時刻.
     */
    public String mEndViewing;
    /**
     *  チャンネル名.
     */
    public String mChannelName;
    /**
     *  チャンネルId.
     */
    public String mChannelId;
    /**
     *  種類名.
     */
    public String mGenreName;
    /**
     *  cast.
     */
    public String mCast;
    /**
     *  description1.
     */
    public String mDescription1;
    /**
     *  description2.
     */
    public String mDescription2;
    /**
     *  description3.
     */
    public String mDescription3;
    /**
     *  viewAbleAge.
     */
    public String mViewAbleAge;
    /**
     *  reserved1.
     */
    public String mReserved1;
    /**
     *  reserved2.
     */
    public String mReserved2;
    /**
     *  mReserved3.
     */
    public String mReserved3;
    /**
     *  mReserved4.
     */
    public String mReserved4;
    /**
     *  mReserved5.
     */
    public String mReserved5;

    /**
     * コンストラクタ.
     *
     * @param clipFlag  クリップフラグ
     * @param contentId コンテンツID
     * @param serviceId サービスID
     * @param categoryId カテゴリーID
     * @param contentPictureUrl1    サムネイルURL1
     * @param contentPictureUrl2    サムネイルURL2
     * @param title     タイトル
     * @param rank     ランク
     * @param mobileViewingFlg  モバイルビューフラグ
     * @param startViewing      開始時間
     * @param endViewing      終了時間
     * @param channelName      チャンネル名
     * @param channelId      チャンネルID
     * @param genreName      ジャンル名
     * @param description1   description1
     * @param description2   description2
     * @param description3   description3
     * @param viewAbleAge   viewAbleAge
     * @param reserved1   予備1
     * @param reserved2   予備2
     * @param reserved3   予備3
     * @param reserved4   予備4
     * @param reserved5   予備5
     */
    public SearchContentInfo(final boolean clipFlag, final String contentId, final int serviceId,
                             final String categoryId, final String contentPictureUrl1, final String contentPictureUrl2,
                             final String title, final int rank, final String mobileViewingFlg,
                             final String startViewing, final String endViewing, final String channelName,
                             final String channelId, final String genreName, final String description1,
                             final String description2, final String description3, final String viewAbleAge,
                             final String reserved1, final String reserved2, final String reserved3,
                             final String reserved4, final String reserved5) {
        this.mClipFlag = clipFlag;
        this.mContentId = contentId;
        this.mServiceId = serviceId;
        this.mCategoryId = categoryId;
        this.mContentPictureUrl1 = contentPictureUrl1;
        this.mContentPictureUrl2 = contentPictureUrl2;
        this.mTitle = title;
        this.mRank = String.valueOf(rank);
        this.mMobileViewingFlg = mobileViewingFlg;
        this.mStartViewing = startViewing;
        this.mEndViewing = endViewing;
        this.mChannelName = channelName;
        this.mChannelId = channelId;
        this.mGenreName = genreName;
        this.mDescription1 = description1;
        this.mDescription2 = description2;
        this.mDescription3 = description3;
        this.mViewAbleAge = viewAbleAge;
        this.mReserved1 = reserved1;
        this.mReserved2 = reserved2;
        this.mReserved3 = reserved3;
        this.mReserved4 = reserved4;
        this.mReserved5 = reserved5;
    }
}
