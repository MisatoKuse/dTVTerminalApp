/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

/**
 * 検索結果詳細クラス.
 */
@SuppressWarnings("PublicField")
public class SearchContentInfo {
    //単純な構造体としての役割のクラスの為、メンバはpublicとする
    /** クリップフラグ.*/
    private final boolean mClipFlag;
    /** コンテンツID.*/
    public final String mContentsId;
    /** サービスID.*/
    public final int mServiceId;
    /** カテゴリーID.*/
    public final String mCategoryId;
    /** サムネイルURL1.*/
    public final String mContentPictureUrl1;
    /** サムネイルURL2.*/
    private final String mContentPictureUrl2;
    /** タイトル.*/
    public final String mTitle;
    /** ランク.*/
    public final String mRank;
    /** mobileViewingFlg.*/
    public final String mMobileViewingFlg;
    /** 開始時刻.*/
    public final String mStartViewing;
    /** 終了時刻.*/
    public final String mEndViewing;
    /** チャンネル名.*/
    public final String mChannelName;
    /** チャンネルId.*/
    public final String mChannelId;
    /** 種類名.*/
    private final String mGenreName;
    /** cast.*/
    public String mCast;
    /** description1.*/
    public final String mDescription1;
    /** description2.*/
    public final String mDescription2;
    /** description3.*/
    public final String mDescription3;
    /** viewAbleAge.*/
    private final String mViewAbleAge;
    /** mobileViewingFlg.*/
    public final String mTitleKind;
    /** reserved1.*/
    public final String mReserved1;
    /** reserved2.*/
    public final String mReserved2;
    /** mReserved3.*/
    public final String mReserved3;
    /** mReserved4.*/
    public final String mReserved4;
    /** mReserved5.*/
    public final String mReserved5;
    /** mReserved6.*/
    public final String mReserved6;
    /** mReserved7.*/
    public final String mReserved7;
    /** mReserved8.*/
    public final String mReserved8;
    /** mReserved9.*/
    public final String mReserved9;
    /** mReserved10.*/
    public final String mReserved10;
    /** 制作年.*/
    public final String mProductionYear;
    /** コピーライト.*/
    public final String mCopyright;
    /** コンテンツ長.*/
    public final String mContentsLength;
    /** 視聴エリアコード.*/
    public final String mArea;

    /**
     * コンストラクタ.
     *  @param clipFlag  クリップフラグ
     * @param contentsId コンテンツID
     * @param serviceId サービスID
     * @param categoryId カテゴリーID
     * @param contentPictureUrl1    サムネイルURL1
     * @param contentPictureUrl2    サムネイルURL2
     * @param title     タイトル
     * @param rank     ランク
     * @param mobileViewingFlg  モバイルビューフラグ
     * @param titleKind 作品種別
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
     * @param reserved6   予備6
     * @param reserved7   予備7
     * @param reserved8   予備8
     * @param reserved9   予備9
     * @param reserved10  予備10
     * @param productionYear  制作年
     * @param copyright  コピーライト
     * @param contentsLength  コンテンツ長
     * @param area  視聴エリアコード
     */
    public SearchContentInfo(final boolean clipFlag, final String contentsId, final int serviceId,
                             final String categoryId, final String contentPictureUrl1, final String contentPictureUrl2,
                             final String title, final int rank, final String mobileViewingFlg,
                             final String titleKind, final String startViewing, final String endViewing, final String channelName,
                             final String channelId, final String genreName, final String description1,
                             final String description2, final String description3, final String viewAbleAge,
                             final String reserved1, final String reserved2, final String reserved3,
                             final String reserved4, final String reserved5, final String reserved6,
                             final String reserved7, final String reserved8, final String reserved9,
                             final String reserved10, final String productionYear, final String copyright,
                             final String contentsLength, final String area) {
        this.mClipFlag = clipFlag;
        this.mContentsId = contentsId;
        this.mServiceId = serviceId;
        this.mCategoryId = categoryId;
        this.mContentPictureUrl1 = contentPictureUrl1;
        this.mContentPictureUrl2 = contentPictureUrl2;
        this.mTitle = title;
        this.mRank = String.valueOf(rank);
        this.mMobileViewingFlg = mobileViewingFlg;
        this.mTitleKind = titleKind;
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
        this.mReserved6 = reserved6;
        this.mReserved7 = reserved7;
        this.mReserved8 = reserved8;
        this.mReserved9 = reserved9;
        this.mReserved10 = reserved10;
        this.mProductionYear = productionYear;
        this.mCopyright = copyright;
        this.mContentsLength = contentsLength;
        this.mArea = area;
    }
}
