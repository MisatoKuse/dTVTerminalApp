/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.os.Parcel;

import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;

import java.util.List;


/**
 * 値渡しクラス.
 */
public class OtherContentsDetailData extends RecordedContentsDetailData {

    /**thumb.*/
    private String mThumb;
    /**日付.*/
    private String mChannelDate;
    /**チャンネル名.*/
    private String mChannelName;
    /**サービスID.*/
    private int mServiceId;
    /**detail.*/
    private String mDetail;
    /**comment.*/
    private String mComment;
    /**highlight.*/
    private String mHighlight;
    /**コンテンツタイプ.*/
    private String mContentsType;
    /**年齢.*/
    private int mAge;
    /**ロール情報.*/
    private String[] mRoleList;
    /**開始時間.*/
    private String mStartDate;
    /**終了時間.*/
    private String mEndDate;
    /**表示タイプ.*/
    private String mDisplayType;
    /**カテゴリーID.*/
    private String mCategoryId;
    /**商品詳細1.*/
    private String mDescription1;
    /**商品詳細2.*/
    private String mDescription2;
    /**商品詳細3.*/
    private String mDescription3;
    /**予備1.*/
    private String mReserved1;
    /**予備2.*/
    private String mReserved2;
    /**予備3.*/
    private String mReserved3;
    /**予備4.*/
    private String mReserved4;
    /**予備5.*/
    private String mReserved5;
    /**モバイル視聴可否フラグ.*/
    private String mobileViewingFlg;
    /**スタッフ情報.*/
    private List<String> staffList;
    /**クリップ情報取得用.*/
    private VodMetaFullData mVodMetaFullData = null;
    /**コンテンツIDを追加.*/
    private String mContentsId;
    /**チャンネルID.*/
    private String mChannelId = "";
    /**おすすめ順.*/
    private String mRecommendOrder = "";
    /**画面ID.*/
    private String mPageId = "";
    /**ユーザグループID.*/
    private String mGroupId = "";
    /**レコメンド手法ID.*/
    private String mRecommendMethodId = "";
    /**レコメンドフラグ.*/
    private String mRecommendFlg = "";
    /**表示タイプ.*/
    private String mDispType = "";
    /**クリップ情報.*/
    private String mSearchOk = "";
    /**dTVフラグ.*/
    private String mDtv = "";
    /**dTVタイプ.*/
    private String mDtvType = "";
    /**クリップ可否.*/
    private boolean mClipExec = false;
    /**クリップ未/済.*/
    private boolean mClipStatus = false;
    /**レーティング値.*/
    private double mRating = 0;
    /**シーズン名.*/
    private String mEpititle = "";
    /**Tvサービス.*/
    private String mTvService = "";
    /**crId.*/
    private String mCrId = null;
    /**イベントID.*/
    private String mEventId = null;
    /**タイトルID.*/
    private String mTitleId = null;
    /**作品種別.*/
    private String mTitleKind = null;
    /**4kflg.*/
    private int m4kflg;
    /**(音声、言語).*/
    private String[] mAdinfoArray;
    /**コピー制限.*/
    private String mCopy = "";
    /**視聴制限.*/
    private String mRvalue = "";
    /**コンテンツ種別.*/
    private ContentUtils.ContentsType mContentCategory = null;

    /**
     * 日付取得.
     * @return 日付
     */
    public String getChannelDate() {
        return mChannelDate;
    }

    /**
     * 日付設定.
     * @param mChannelDate 日付
     */
    public void setChannelDate(final String mChannelDate) {
        this.mChannelDate = mChannelDate;
    }

    /**
     * チャンネル名取得.
     * @return チャンネル名
     */
    public String getChannelName() {
        return mChannelName;
    }

    /**
     * チャンネル名設定.
     * @param mChannelName チャンネル名
     */
    public void setChannelName(final String mChannelName) {
        this.mChannelName = mChannelName;
    }

    /**
     * サムネイル取得.
     * @return サムネイル
     */
    public String getThumb() {
        return mThumb;
    }

    /**
     * サムネイル設定.
     * @param mThumb サムネイル
     */
    public void setThumb(final String mThumb) {
        this.mThumb = mThumb;
    }
    /**
     * サービスID取得.
     * @return サービスID
     */
    public int getServiceId() {
        return mServiceId;
    }

    /**
     * サービスID設定.
     * @param serviceId サービスID
     */
    public void setServiceId(final int serviceId) {
        this.mServiceId = serviceId;
    }

    /**
     * モバイル視聴可否取得.
     * @return モバイル視聴可否フラグ
     */
    public String getMobileViewingFlg() {
        return mobileViewingFlg;
    }

    /**
     * モバイル視聴可否設定.
     * @param mobileViewingFlg モバイル視聴可否フラグ
     */
    public void setMobileViewingFlg(final String mobileViewingFlg) {
        this.mobileViewingFlg = mobileViewingFlg;
    }

    /**
     * 詳細取得.
     * @return  詳細
     */
    public String getDetail() {
        return mDetail;
    }

    /**
     * 詳細設定.
     * @param detail 詳細
     */
    public void setDetail(final String detail) {
        this.mDetail = detail;
    }

    /**
     * comment取得.
     * @return comment
     */
    public String getComment() {
        return mComment;
    }

    /**
     * comment設定.
     * @param mComment comment
     */
    public void setComment(final String mComment) {
        this.mComment = mComment;
    }

    /**
     * highlight取得.
     * @return highlight
     */
    public String getHighlight() {
        return mHighlight;
    }

    /**
     * highlight設定.
     * @param mHighlight highlight
     */
    public void setHighlight(final String mHighlight) {
        this.mHighlight = mHighlight;
    }
    /**
     * コンテンツタイプ取得.
     * @return コンテンツタイプ
     */
    public String getContentsType() {
        return mContentsType;
    }

    /**
     * コンテンツタイプ設定.
     * @param mContentsType コンテンツタイプ
     */
    public void setContentsType(final String mContentsType) {
        this.mContentsType = mContentsType;
    }

    /**
     * 年齢取得.
     * @return 年齢
     */
    public int getAge() {
        return mAge;
    }

    /**
     * 年齢設定.
     * @param Age 年齢
     */
    public void setAge(final int Age) {
        this.mAge = Age;
    }

    /**
     * スタッフ情報取得.
     * @return スタッフ情報
     */
    public List<String> getStaffList() {
        return staffList;
    }

    /**
     * スタッフ情報設定.
     * @param staffList スタッフ情報
     */
    public void setStaffList(final List<String> staffList) {
        this.staffList = staffList;
    }

    /**
     * ロール情報取得.
     * @return ロール情報
     */
    public String[] getmRoleList() {
        return mRoleList.clone();
    }

    /**
     * ロール情報設定.
     * @param mRoleList ロール情報
     */
    public void setmRoleList(final String[] mRoleList) {
        this.mRoleList = mRoleList.clone();
    }

    /**
     * 開始時間取得.
     * @return 開始時間
     */
    public String getmStartDate() {
        return mStartDate;
    }

    /**
     * 開始時間設定.
     * @param mStartDate 開始時間
     */
    public void setmStartDate(final String mStartDate) {
        this.mStartDate = mStartDate;
    }

    /**
     * 終了時間取得.
     * @return 終了時間
     */
    public String getmEndDate() {
        return mEndDate;
    }

    /**
     * 終了時間設定.
     * @param mEndDate 終了時間
     */
    public void setmEndDate(final String mEndDate) {
        this.mEndDate = mEndDate;
    }

    /**
     * 表示タイプ取得.
     * @return 表示タイプ
     */
    public String getmDisplayType() {
        return mDisplayType;
    }

    /**
     * 表示タイプ設定.
     * @param mDisplayType 表示タイプ
     */
    public void setmDisplayType(final String mDisplayType) {
        this.mDisplayType = mDisplayType;
    }

    /**
     * カテゴリーID取得.
     * @return カテゴリーID
     */
    public String getCategoryId() {
        return mCategoryId;
    }

    /**
     * カテゴリーID設定.
     * @param categoryId カテゴリーID
     */
    public void setCategoryId(final String categoryId) {
        this.mCategoryId = categoryId;
    }

    /**
     * コンテンツIDを取得.
     * @return コンテンツID
     */
    public String getContentsId() {
        return mContentsId;
    }

    /**
     * コンテンツIDを設定.
     * @param mContentsId コンテンツID
     */
    public void setContentsId(final String mContentsId) {
        this.mContentsId = mContentsId;
    }

    /**
     * 予備1取得.
     * @return 予備1
     */
    public String getReserved1() {
        return mReserved1;
    }

    /**
     * 予備1設定.
     * @param mReserved1 予備1
     */
    public void setReserved1(final String mReserved1) {
        this.mReserved1 = mReserved1;
    }

    /**
     * 予備2取得.
     * @return 予備2
     */
    public String getReserved2() {
        return mReserved2;
    }

    /**
     * 予備2設定.
     * @param mReserved2 予備2
     */
    public void setReserved2(final String mReserved2) {
        this.mReserved2 = mReserved2;
    }

    /**
     * 予備4取得.
     * @return 予備4
     */
    public String getReserved4() {
        return mReserved4;
    }

    /**
     * 予備4設定.
     * @param mReserved4 予備4
     */
    public void setReserved4(final String mReserved4) {
        this.mReserved4 = mReserved4;
    }

    /**
     * 商品詳細1取得.
     * @return 商品詳細1
     */
    public String getDescription1() {
        return mDescription1;
    }

    /**
     * 商品詳細1設定.
     * @param mDescription1 商品詳細1
     */
    public void setDescription1(final String mDescription1) {
        this.mDescription1 = mDescription1;
    }

    /**
     * 商品詳細2取得.
     * @return 商品詳細2
     */
    public String getDescription2() {
        return mDescription2;
    }

    /**
     * 商品詳細2設定.
     * @param mDescription2 商品詳細2
     */
    public void setDescription2(final String mDescription2) {
        this.mDescription2 = mDescription2;
    }

    /**
     * 商品詳細3取得.
     * @return 商品詳細3
     */
    public String getDescription3() {
        return mDescription3;
    }

    /**
     * 商品詳細3設定.
     * @param mDescription3 商品詳細3
     */
    public void setDescription3(final String mDescription3) {
        this.mDescription3 = mDescription3;
    }

    /**
     * 予備3取得.
     * @return 予備3
     */
    public String getReserved3() {
        return mReserved3;
    }

    /**
     * 予備3設定.
     * @param mReserved3 予備3
     */
    public void setReserved3(final String mReserved3) {
        this.mReserved3 = mReserved3;
    }

    /**
     * 予備5取得.
     * @return 予備5
     */
    public String getReserved5() {
        return mReserved5;
    }

    /**
     * 予備5設定.
     * @param mReserved5 予備5
     */
    public void setReserved5(final String mReserved5) {
        this.mReserved5 = mReserved5;
    }

    /**
     * クリップ情報取得用.
     * @return クリップ情報
     */
    public VodMetaFullData getVodMetaFullData() {
        return mVodMetaFullData;
    }

    /**
     * クリップ情報取得用設定.
     * @param mVodMetaFullData クリップ情報取得用
     */
    public void setVodMetaFullData(final VodMetaFullData mVodMetaFullData) {
        this.mVodMetaFullData = mVodMetaFullData;
    }

    /**
     * チャンネルID取得.
     * @return チャンネルID
     */
    public String getChannelId() {
        return mChannelId;
    }

    /**
     * チャンネルID設定.
     * @param mChannelId チャンネルID
     */
    public void setChannelId(final String mChannelId) {
        this.mChannelId = mChannelId;
    }

    /**
     * おすすめ順取得.
     * @return おすすめ順
     */
    public String getRecommendOrder() {
        return mRecommendOrder;
    }

    /**
     * おすすめ順設定.
     * @param mRecommendOrder おすすめ順
     */
    public void setRecommendOrder(final String mRecommendOrder) {
        this.mRecommendOrder = mRecommendOrder;
    }

    /**
     * 画面ID取得.
     * @return 画面ID
     */
    public String getPageId() {
        return mPageId;
    }

    /**
     * 画面ID設定 .
     * @param mPageId 画面ID
     */
    public void setPageId(final String mPageId) {
        this.mPageId = mPageId;
    }

    /**
     * ユーザグループID取得.
     * @return ユーザグループID
     */
    public String getGroupId() {
        return mGroupId;
    }

    /**
     * ユーザグループID設定.
     * @param mGroupId ユーザグループID
     */
    public void setGroupId(final String mGroupId) {
        this.mGroupId = mGroupId;
    }

    /**
     * レコメンド手法ID取得.
     * @return レコメンド手法ID
     */
    public String getRecommendMethodId() {
        return mRecommendMethodId;
    }

    /**
     * レコメンド手法ID設定.
     * @param mRecommendMethodId レコメンド手法ID
     */
    public void setRecommendMethodId(final String mRecommendMethodId) {
        this.mRecommendMethodId = mRecommendMethodId;
    }

    /**
     * レコメンドフラグ取得.
     * @return レコメンドフラグ
     */
    public String getRecommendFlg() {
        return mRecommendFlg;
    }

    /**
     * レコメンドフラグ設定.
     * @param mRecommendFlg レコメンドフラグ
     */
    public void setRecommendFlg(final String mRecommendFlg) {
        this.mRecommendFlg = mRecommendFlg;
    }

    /**
     * 表示タイプ取得.
     * @return 表示タイプ
     */
    public String getDispType() {
        return mDispType;
    }

    /**
     * 表示タイプ設定.
     * @param mDispType 表示タイプ
     */
    public void setDispType(final String mDispType) {
        this.mDispType = mDispType;
    }

    /**
     * クリップ情報取得.
     * @return クリップ情報
     */
    public String getSearchOk() {
        return mSearchOk;
    }

    /**
     * クリップ情報設定.
     * @param mSearchOk クリップ情報
     */
    public void setSearchOk(final String mSearchOk) {
        this.mSearchOk = mSearchOk;
    }

    /**
     * dTVフラグ取得.
     * @return dTVフラグ
     */
    public String getDtv() {
        return mDtv;
    }

    /**
     * dTVフラグ設定.
     * @param mDtv dTVフラグ
     */
    public void setDtv(final String mDtv) {
        this.mDtv = mDtv;
    }

    /**
     * dTVタイプ取得.
     * @return dTVタイプ
     */
    public String getDtvType() {
        return mDtvType;
    }

    /**
     * dTVタイプ設定.
     * @param mDtvType dTVタイプ
     */
    public void setDtvType(final String mDtvType) {
        this.mDtvType = mDtvType;
    }

    /**
     * クリップ可否取得.
     * @return クリップ可否
     */
    public boolean isClipExec() {
        return mClipExec;
    }

    /**
     * クリップ可否設定.
     * @param mClipExec クリップ可否
     */
    public void setClipExec(final boolean mClipExec) {
        this.mClipExec = mClipExec;
    }

    /**
     * クリップ未/済取得.
     * @return クリップ未/済
     */
    public boolean isClipStatus() {
        return mClipStatus;
    }

    /**
     * クリップ未/済設定.
     * @param mClipStatus クリップ未/済
     */
    public void setClipStatus(final boolean mClipStatus) {
        this.mClipStatus = mClipStatus;
    }

    /**
     * レーティング値取得.
     * @return レーティング値
     */
    public double getRating() {
        return mRating;
    }

    /**
     * レーティング値設定.
     * @param mRating レーティング値
     */
    public void setRating(final double mRating) {
        this.mRating = mRating;
    }

    /**
     * シーズン名取得.
     * @return シーズン名
     */
    public String getEpititle() {
        return mEpititle;
    }

    /**
     * シーズン名設定.
     * @param mEpititle シーズン名
     */
    public void setEpititle(final String mEpititle) {
        this.mEpititle = mEpititle;
    }

    /**
     * Tvサービス取得.
     * @return Tvサービス
     */
    public String getTvService() {
        return mTvService;
    }

    /**
     * Tvサービス設定.
     * @param mTvService Tvサービス
     */
    public void setTvService(final String mTvService) {
        this.mTvService = mTvService;
    }

    /**
     * crId取得.
     * @return crId
     */
    public String getCrId() {
        return mCrId;
    }

    /**
     * crId設定.
     * @param mCrId crId
     */
    public void setCrId(final String mCrId) {
        this.mCrId = mCrId;
    }

    /**
     * イベントID取得.
     * @return イベントID
     */
    public String getEventId() {
        return mEventId;
    }

    /**
     * イベントID設定.
     * @param mEventId イベントID
     */
    public void setEventId(final String mEventId) {
        this.mEventId = mEventId;
    }

    /**
     * タイトルID取得.
     * @return タイトルID
     */
    public String getTitleId() {
        return mTitleId;
    }

    /**
     * タイトルID設定.
     * @param mTitleId タイトルID
     */
    public void setTitleId(final String mTitleId) {
        this.mTitleId = mTitleId;
    }

    /**
     * 作品種別取得.
     * @return 作品種別
     */
    public String getTitleKind() {
        return mTitleKind;
    }

    /**
     * 作品種別設定.
     * @param titleKind 作品種別
     */
    public void setTitleKind(final String titleKind) {
        this.mTitleKind = titleKind;
    }

    /**
     * 4kflg取得.
     * @return 4kflg
     */
    public int getM4kflg() {
        return m4kflg;
    }

    /**
     * 4kflg設定.
     * @param m4kflg 4kflg
     */
    public void setM4kflg(final int m4kflg) {
        this.m4kflg = m4kflg;
    }

    /**
     * (音声、言語)取得.
     * @return (音声,言語)
     */
    public String[] getAdinfoArray() {
        return mAdinfoArray;
    }

    /**
     * (音声、言語)設定.
     * @param mAdinfoArray (音声、言語)
     */
    public void setAdinfoArray(final String[] mAdinfoArray) {
        this.mAdinfoArray = mAdinfoArray;
    }

    /**
     * コピー制限取得.
     * @return コピー制限
     */
    public String getCopy() {
        return mCopy;
    }

    /**
     * コピー制限設定.
     * @param mCopy コピー制限
     */
    public void setCopy(final String mCopy) {
        this.mCopy = mCopy;
    }

    /**
     * 視聴制限取得.
     * @return 視聴制限
     */
    public String getRvalue() {
        return mRvalue;
    }

    /**
     * 視聴制限設定.
     * @param mRvalue 視聴制限
     */
    public void setRvalue(final String mRvalue) {
        this.mRvalue = mRvalue;
    }

    /**
     * コンテンツ種別取得.
     * @return コンテンツ種別
     */
    public ContentUtils.ContentsType getContentCategory() {
        return mContentCategory;
    }

    /**
     * コンテンツ種別設定.
     * @param mContentCategory コンテンツ種別
     */
    public void setContentCategory(final ContentUtils.ContentsType mContentCategory) {
        this.mContentCategory = mContentCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("OverlyLongMethod")
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mThumb);
        dest.writeString(this.mChannelDate);
        dest.writeString(this.mChannelName);
        dest.writeInt(this.mServiceId);
        dest.writeString(this.mDetail);
        dest.writeString(this.mComment);
        dest.writeString(this.mHighlight);
        dest.writeString(this.mContentsType);
        dest.writeInt(this.mAge);
        dest.writeStringArray(this.mRoleList);
        dest.writeString(this.mStartDate);
        dest.writeString(this.mEndDate);
        dest.writeString(this.mDisplayType);
        dest.writeString(this.mCategoryId);
        dest.writeString(this.mDescription1);
        dest.writeString(this.mDescription2);
        dest.writeString(this.mDescription3);
        dest.writeString(this.mReserved1);
        dest.writeString(this.mReserved2);
        dest.writeString(this.mReserved3);
        dest.writeString(this.mReserved4);
        dest.writeString(this.mReserved5);
        dest.writeString(this.mobileViewingFlg);
        dest.writeStringList(this.staffList);
        dest.writeSerializable(this.mVodMetaFullData);
        dest.writeString(this.mContentsId);
        dest.writeString(this.mChannelId);
        dest.writeString(this.mRecommendOrder);
        dest.writeString(this.mPageId);
        dest.writeString(this.mGroupId);
        dest.writeString(this.mRecommendMethodId);
        dest.writeString(this.mRecommendFlg);
        dest.writeString(this.mDispType);
        dest.writeString(this.mSearchOk);
        dest.writeString(this.mDtv);
        dest.writeString(this.mDtvType);
        dest.writeByte(this.mClipExec ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mClipStatus ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.mRating);
        dest.writeString(this.mEpititle);
        dest.writeString(this.mTvService);
        dest.writeString(this.mCrId);
        dest.writeString(this.mEventId);
        dest.writeString(this.mTitleId);
        dest.writeString(this.mTitleKind);
        dest.writeInt(this.m4kflg);
        dest.writeStringArray(this.mAdinfoArray);
        dest.writeString(this.mCopy);
        dest.writeString(this.mRvalue);
        dest.writeInt(this.mContentCategory == null ? -1 : this.mContentCategory.ordinal());
    }

    /**コンストラクタ.*/
    public OtherContentsDetailData() {
    }

    /**
     * コンストラクタ.
     * @param in source
     */
    @SuppressWarnings("OverlyLongMethod")
    private OtherContentsDetailData(final Parcel in) {
        super(in);
        this.mThumb = in.readString();
        this.mChannelDate = in.readString();
        this.mChannelName = in.readString();
        this.mServiceId = in.readInt();
        this.mDetail = in.readString();
        this.mComment = in.readString();
        this.mHighlight = in.readString();
        this.mContentsType = in.readString();
        this.mAge = in.readInt();
        this.mRoleList = in.createStringArray();
        this.mStartDate = in.readString();
        this.mEndDate = in.readString();
        this.mDisplayType = in.readString();
        this.mCategoryId = in.readString();
        this.mDescription1 = in.readString();
        this.mDescription2 = in.readString();
        this.mDescription3 = in.readString();
        this.mReserved1 = in.readString();
        this.mReserved2 = in.readString();
        this.mReserved3 = in.readString();
        this.mReserved4 = in.readString();
        this.mReserved5 = in.readString();
        this.mobileViewingFlg = in.readString();
        this.staffList = in.createStringArrayList();
        this.mVodMetaFullData = (VodMetaFullData) in.readSerializable();
        this.mContentsId = in.readString();
        this.mChannelId = in.readString();
        this.mRecommendOrder = in.readString();
        this.mPageId = in.readString();
        this.mGroupId = in.readString();
        this.mRecommendMethodId = in.readString();
        this.mRecommendFlg = in.readString();
        this.mDispType = in.readString();
        this.mSearchOk = in.readString();
        this.mDtv = in.readString();
        this.mDtvType = in.readString();
        this.mClipExec = in.readByte() != 0;
        this.mClipStatus = in.readByte() != 0;
        this.mRating = in.readDouble();
        this.mEpititle = in.readString();
        this.mTvService = in.readString();
        this.mCrId = in.readString();
        this.mEventId = in.readString();
        this.mTitleId = in.readString();
        this.mTitleKind = in.readString();
        this.m4kflg = in.readInt();
        this.mAdinfoArray = in.createStringArray();
        this.mCopy = in.readString();
        this.mRvalue = in.readString();
        int tmpMContentCategory = in.readInt();
        this.mContentCategory = tmpMContentCategory == -1 ? null : ContentUtils.ContentsType.values()[tmpMContentCategory];
    }

    /**CREATOR構造初期化.*/
    public static final Creator<OtherContentsDetailData> CREATOR = new Creator<OtherContentsDetailData>() {
        @Override
        public OtherContentsDetailData createFromParcel(final Parcel source) {
            return new OtherContentsDetailData(source);
        }

        @Override
        public OtherContentsDetailData[] newArray(final int size) {
            return new OtherContentsDetailData[size];
        }
    };
}
