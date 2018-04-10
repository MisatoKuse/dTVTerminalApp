/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.struct;

import android.widget.ImageView;

import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecordingReservationListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

public class ContentsData {

    //ランキング順位
    private String mRank = null;
    //時間
    private String mTime = null;
    //メインタイトル
    private String mTitle = null;
    //評価ポイント
    private String mRatStar = null;
    //サムネイルURL(リスト)
    private String mThumListURL = null;
    //サムネイルURL(詳細画面)
    private String mThumDetailURL = null;
    // 録画予約ステータス
    private int mRecordingReservationStatus = RecordingReservationListDataProvider.RECORD_RESERVATION_SYNC_STATUS_ALREADY_REFLECT;
    // チャンネル名
    private String mChannelName = null;
    // 録画番組用 チャンネル名
    private String recordedChannelName = null;
    // 録画番組用 コピー残り回数
    private int mAllowedUse = 0;
    // クリップ状態
    private String mSearchOk = null;
    // コンテンツ識別子
    private String mCrid = null;
    // サービスID
    private String mServiceId = null;
    // イベントID
    private String mEventId = null;
    // タイトルID
    private String mTitleId = null;
    // 番組種別
    private String mDispType = null;
    // 番組のパレンタル設定値
    private String mRValue = null;
    // 放送開始日時
    private String mLinearStartDate = null;
    // 放送終了日時
    private String mLinearEndDate = null;
    // 視聴通知判定
    private String mIsNotify = null;
    // コンテンツタイプ
    private String mContentsType = null;
    // サービス種別
    private String mTvService = null;
    // dTVフラグ
    private String mDtv = null;
    // コンテンツID
    private String mContentsId = null;
    // あらすじ
    private String mSynop = null;
    // カテゴリID
    private String mCategoryId = null;
    // 開始時刻
    private String mStartViewing = null;
    //終了時刻
    private String mEndViewing = null;
    // reserved1
    private String mReserved1 = null;
    // reserved2
    private String mReserved2 = null;
    // reserved3
    private String mReserved3 = null;
    // reserved4
    private String mReserved4 = null;
    // reserved5
    private String mReserved5 = null;
    // mobileViewingFlg
    private String  mobileViewingFlg = null;
    // クリップボタン
    private ImageView mClipButton = null;
    // クリップリクエストデータ
    private ClipRequestData mRequestData = new ClipRequestData();


    //ダウンロードフラグ
    private int mDownloadFlg = 0;
    //ダウンロードステータス（進捗）
    private String mDownloadStatus = null;
    //ダウンロードパス
    private String mDlFileFullPath = null;

    // チャンネルID
    private String mChannelId = null;
    /**
     * チャンネルNo
     */
    private String mChannelNo = null;
    // おすすめ順
    private String mRecommendOrder = null;
    // 画面ID
    private String mPageId = null;
    // ユーザグループID
    private String mGroupId = null;
    // レコメンド手法ID
    private String mRecommendMethodId = null;
    // dTVタイプ
    private String mDtvType = null;
    // クリップ可否
    private boolean mClipExec = false;
    // クリップ未/済
    private boolean mClipStatus = false;
    //クリップ状態に変更があった場合に真
    private boolean mIsClipStatusUpdate = false;
    // サブタイトル
    private String mSubTitle = null;
    // description1
    private String mDescription1 = null;
    // description2
    private String mDescription2 = null;
    // description3
    private String mDescription3 = null;

    public String getDescription1() {
        return mDescription1;
    }

    public void setDescription1(String mDescription1) {
        this.mDescription1 = mDescription1;
    }

    public String getDescription2() {
        return mDescription2;
    }

    public void setDescription2(String mDescription2) {
        this.mDescription2 = mDescription2;
    }

    public String getDescription3() {
        return mDescription3;
    }

    public void setDescription3(String mDescription3) {
        this.mDescription3 = mDescription3;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String mSubTitle) {
        this.mSubTitle = mSubTitle;
    }

    public String getRank() {
        return mRank;
    }

    public void setRank(String rank) {
        this.mRank = rank;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getRatStar() {
        return mRatStar;
    }

    public void setRatStar(String ratStar) {
        //不正な値が入った場合のためUtilメソッドを通してから格納
        this.mRatStar = StringUtils.toRatString(ratStar);
    }

    public String getThumURL() {
        return mThumListURL;
    }

    public void setThumURL(String thumURL) {
        this.mThumListURL = thumURL;
    }

    public String getThumDetailURL() {
        return mThumDetailURL;
    }

    public void setThumDetailURL(String mThumDetailURL) {
        this.mThumDetailURL = mThumDetailURL;
    }

    public void setRecordingReservationStatus(int status) {
        mRecordingReservationStatus = status;
    }

    public int getRecordingReservationStatus() {
        return mRecordingReservationStatus;
    }

    public String getChannelName() {
        return mChannelName;
    }

    public void setChannelName(String channelName) {
        this.mChannelName = channelName;
    }

    public String getRecordedChannelName() {
        return recordedChannelName;
    }

    public void setRecordedChannelName(String channelName) {
        this.recordedChannelName = channelName;
    }
    public int getAllowedUse() {
        return mAllowedUse;
    }

    public void setAllowedUse(int allowedUse) {
        this.mAllowedUse = allowedUse;
    }

    public String getSearchOk() {
        return mSearchOk;
    }

    public void setSearchOk(String mSearchOk) {
        this.mSearchOk = mSearchOk;
    }

    public String getCrid() {
        return mCrid;
    }

    public void setCrid(String mCrid) {
        this.mCrid = mCrid;
    }

    public String getServiceId() {
        return mServiceId;
    }

    public void setServiceId(String mServiceId) {
        this.mServiceId = mServiceId;
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(String mEventId) {
        this.mEventId = mEventId;
    }

    public String getDispType() {
        return mDispType;
    }

    public void setDispType(String mDispType) {
        this.mDispType = mDispType;
    }

    public String getTitleId() {
        return mTitleId;
    }

    public void setTitleId(String mTitleId) {
        this.mTitleId = mTitleId;
    }

    public String getRValue() {
        return mRValue;
    }

    public void setRValue(String mRValue) {
        this.mRValue = mRValue;
    }

    public String getLinearStartDate() {
        return mLinearStartDate;
    }

    public void setLinearStartDate(String linearStartDate) {
        this.mLinearStartDate = linearStartDate;
    }

    public String getLinearEndDate() {
        return mLinearEndDate;
    }

    public void setLinearEndDate(String mLinearEndDate) {
        this.mLinearEndDate = mLinearEndDate;
    }

    public String getContentsType() {
        return mContentsType;
    }

    public void setContentsType(String mContentsType) {
        this.mContentsType = mContentsType;
    }

    public String isIsNotify() {
        return mIsNotify;
    }

    public void setIsNotify(String mIsNotify) {
        this.mIsNotify = mIsNotify;
    }

    public ImageView getClipButton() {
        return mClipButton;
    }

    public void setClipButton(ImageView mClipButton) {
        this.mClipButton = mClipButton;
    }

    public String getTvService() {
        return mTvService;
    }

    public void setTvService(String mTvService) {
        this.mTvService = mTvService;
    }

    public String getDtv() {
        return mDtv;
    }

    public void setDtv(String mDtv) {
        this.mDtv = mDtv;
    }

    public String getContentsId() {
        return mContentsId;
    }

    public void setContentsId(String mContentsId) {
        this.mContentsId = mContentsId;
    }

    public String getSynop() {
        return mSynop;
    }

    public void setSynop(String mSynop) {
        //TODO:レスポンスがないためダミー
//        this.mSynop = mSynop;
        this.mSynop = "";
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(String mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public String getStartViewing() {
        return mStartViewing;
    }

    public void setStartViewing(String mStartViewing) {
        this.mStartViewing = mStartViewing;
    }

    public String getEndViewing() {
        return mEndViewing;
    }

    public void setEndViewing(String mEndViewing) {
        this.mEndViewing = mEndViewing;
    }

    public String getReserved1() {
        return mReserved1;
    }

    public void setReserved1(String mReserved1) {
        this.mReserved1 = mReserved1;
    }

    public String getReserved2() {
        return mReserved2;
    }

    public void setReserved2(String mReserved2) {
        this.mReserved2 = mReserved2;
    }

    public String getReserved3() {
        return mReserved3;
    }

    public void setReserved3(String mReserved3) {
        this.mReserved3 = mReserved3;
    }

    public String getReserved4() {
        return mReserved4;
    }

    public void setReserved4(String mReserved4) {
        this.mReserved4 = mReserved4;
    }

    public String getReserved5() {
        return mReserved5;
    }

    public void setReserved5(String mReserved5) {
        this.mReserved5 = mReserved5;
    }

    public ClipRequestData getRequestData() {
        return mRequestData;
    }

    public void setRequestData(ClipRequestData ｍRequestData) {
        this.mRequestData = ｍRequestData;
    }

    public int getDownloadFlg() {
        return mDownloadFlg;
    }

    public void setDownloadFlg(int mDownloadFlg) {
        this.mDownloadFlg = mDownloadFlg;
    }

    public String getDownloadStatus() {
        return mDownloadStatus;
    }

    public void setDownloadStatus(String mDownloadStatus) {
        this.mDownloadStatus = mDownloadStatus;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String mChannelId) {
        this.mChannelId = mChannelId;
    }

    /**
     * チャンネルNoを取得.
     *
     * @return チャンネルNo
     */
    public String getChannelNo() {
        return mChannelNo;
    }

    /**
     * チャンネルNoを設定.
     *
     * @param channelNo チャンネルNo
     */
    public void setChannelNo(final String channelNo) {
        this.mChannelNo = channelNo;
    }

    public String getRecommendOrder() {
        return mRecommendOrder;
    }

    public void setRecommendOrder(String mRecommendOrder) {
        this.mRecommendOrder = mRecommendOrder;
    }

    public String getPageId() {
        return mPageId;
    }

    public void setPageId(String mPageId) {
        this.mPageId = mPageId;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public void setGroupId(String mGroupId) {
        this.mGroupId = mGroupId;
    }

    public String getRecommendMethodId() {
        return mRecommendMethodId;
    }

    public void setRecommendMethodId(String mRecommendMethodId) {
        this.mRecommendMethodId = mRecommendMethodId;
    }

    public String getDtvType() {
        return mDtvType;
    }

    public void setDtvType(String mDtvType) {
        this.mDtvType = mDtvType;
    }

    public boolean isClipExec() {
        return mClipExec;
    }

    public void setClipExec(boolean mClipExec) {
        this.mClipExec = mClipExec;
    }

    public boolean isClipStatus() {
        return mClipStatus;
    }

    public void setClipStatus(boolean mClipStatus) {
        this.mClipStatus = mClipStatus;
    }

    public void setDlFileFullPath(String path){
        mDlFileFullPath=path;
    }

    public String getDlFileFullPath() {
        return mDlFileFullPath;
    }

    public String getMobileViewingFlg() {
        return mobileViewingFlg;
    }

    public void setMobileViewingFlg(String mobileViewingFlg) {
        this.mobileViewingFlg = mobileViewingFlg;
    }

    public boolean isClipStatusUpdate() {
        return mIsClipStatusUpdate;
    }

    public void setClipStatusUpdate(boolean mClipStatusUpdate) {
        this.mIsClipStatusUpdate = mClipStatusUpdate;
    }

    /**
     * 子コンテンツを持っているか.
     * @return 子コンテンツ持ちのコンテンツの場合、true
     */
    public boolean hasChildContentList() {
        boolean result = false;
        do {
            if (mDispType == null) {
                break;
            }
            if (mDispType.equals(DTVTConstants.DISP_TYPE_SERIES_SVOD)
                    || mDispType.equals(DTVTConstants.DISP_TYPE_WIZARD)
                    || mDispType.equals(DTVTConstants.DISP_TYPE_VIDEO_PACKAGE)
                    || mDispType.equals(DTVTConstants.DISP_TYPE_SUBSCRIPTION_PACKAGE)) {
                result = true;
                break;
            }
        } while (false);
        return result;
    }
}
