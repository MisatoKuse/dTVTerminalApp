/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

/**
 * ClipRequestData .
 */
public class ClipRequestData {
    /**タイプ.*/
    private String mType = null;
    /**コンテンツ識別子.*/
    private String mCrid = null;
    /**サービスID.*/
    private String mServiceId = null;
    /**イベントID.*/
    private String mEventId = null;
    /**タイトルID.*/
    private String mTitleId = null;
    /**コンテンツタイトル.*/
    private String mTitle = null;
    /**番組のパレンタル設定値.*/
    private String mRValue = null;
    /**放送開始日時.*/
    private String mLinearStartDate = null;
    /**放送終了日時.*/
    private String mLinearEndDate = null;
    /**クリップ状態.*/
    private String mSearchOk = null;
    /**表示タイプ.*/
    private String mDispType = null;
    /**コンテンツタイプ.*/
    private String mContentType = null;
    /**テーブルタイプ.*/
    private ClipKeyListDao.TABLE_TYPE mTableType = null;
    /**視聴通知判定.*/
    private boolean mIsNotify = false;
    /**クリップ未/済.*/
    private boolean mClipStatus = false;
    /**EPG判定用.*/
    private static final String TV_PROGRAM_CHECK = "tv_program";
    /**h4d_iptv.*/
    private static final String H4D_IPTV_SERVICE_CONTENTS = "0";
    /**dch.*/
    private static final String DCH_SERVICE_CONTENTS = "1";
    /**h4d_vod.*/
    private String DTV_SERVICE_CONTENTS_FALSE = "0";
    /**dtv_vod.*/
    private static final String DTV_SERVICE_CONTENTS_TRUE = "1";

    /**
     * コンテンツタイトルを取得する.
     * @return コンテンツタイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * コンテンツタイトルを設定する.
     * @param mTitle コンテンツタイトル
     */
    public void setTitle(final String mTitle) {
        this.mTitle = mTitle;
    }
    /**
     * タイプを取得する.
     * @return タイプ
     */
    public String getType() {
        return mType;
    }

    /**
     * コンテンツ識別子取得する.
     * @return コンテンツ識別子
     */
    public String getCrid() {
        return mCrid;
    }

    /**
     * コンテンツ識別子設定する.
     * @param mCrid コンテンツ識別子
     */
    public void setCrid(final String mCrid) {
        this.mCrid = mCrid;
    }

    /**
     * サービスID取得する.
     * @return サービスID
     */
    public String getServiceId() {
        return mServiceId;
    }
    /**
     * サービスID設定する.
     * @param mServiceId サービスID
     */
    public void setServiceId(final String mServiceId) {
        this.mServiceId = mServiceId;
    }
    /**
     * イベントID取得する.
     * @return イベントID
     */
    public String getEventId() {
        return mEventId;
    }

    /**
     * イベントID設定する.
     * @param mEventId イベントID
     */
    public void setEventId(final String mEventId) {
        this.mEventId = mEventId;
    }

    /**
     * タイトルID取得する.
     * @return タイトルID
     */
    public String getTitleId() {
        return mTitleId;
    }
    /**
     * タイトルID設定する.
     * @param mTitleId タイトルID
     */
    public void setTitleId(final String mTitleId) {
        this.mTitleId = mTitleId;
    }

    /**
     * 番組のパレンタル設定値取得する.
     * @return 番組のパレンタル設定値
     */
    public String getRValue() {
        return mRValue;
    }

    /**
     * 番組のパレンタル設定値設定する.
     * @param mRValue 番組のパレンタル設定値
     */
    public void setRValue(final String mRValue) {
        this.mRValue = mRValue;
    }

    /**
     * 放送開始日時取得する.
      * @return 放送開始日時
     */
    public String getLinearStartDate() {
        return mLinearStartDate;
    }

    /**
     * 仕様により avail_start_date を設定する.
     *
     * @param mLinearStartDate 番組開始時間
     */
    public void setLinearStartDate(final String mLinearStartDate) {
        this.mLinearStartDate = mLinearStartDate;
    }

    /**
     * 放送終了日時取得する.
     * @return 放送終了日時
     */
    public String getLinearEndDate() {
        return mLinearEndDate;
    }

    /**
     * 仕様により avail_end_date を設定する.
     *
     * @param mLinearEndDate 番組終了時間
     */
    public void setLinearEndDate(final String mLinearEndDate) {
        this.mLinearEndDate = mLinearEndDate;
    }

    /**
     * クリップ状態取得する.
     * @return クリップ状態
     */
    public String getSearchOk() {
        return mSearchOk;
    }

    /**
     * クリップ状態設定する.
     * @param mSearchOk クリップ状態
     */
    public void setSearchOk(final String mSearchOk) {
        this.mSearchOk = mSearchOk;
    }
    /**
     * 視聴通知判定取得する.
     * @return 視聴通知判定
     */
    public boolean getIsNotify() {
        return mIsNotify;
    }

    /**
     * 表示タイプ取得する.
     * @return 表示タイプ
     */
    public String getDispType() {
        return mDispType;
    }

    /**
     * 表示タイプ設定する.
     * @param mDispType 表示タイプ
     */
    public void setDispType(final String mDispType) {
        this.mDispType = mDispType;
    }

    /**
     * コンテンツタイプ取得する.
     * @return コンテンツタイプ
     */
    public String getContentType() {
        return mContentType;
    }

    /**
     * コンテンツタイプ設定する.
     * @param mContentType コンテンツタイプ
     */
    public void setContentType(final String mContentType) {
        this.mContentType = mContentType;
    }

    /**
     * テーブルタイプ取得する.
     * @return テーブルタイプ
     */
    public ClipKeyListDao.TABLE_TYPE getTableType() {
        return mTableType;
    }

    /**
     * テーブルタイプ設定する.
     * @param mTableType テーブルタイプ
     */

    public void setTableType(final ClipKeyListDao.TABLE_TYPE mTableType) {
        this.mTableType = mTableType;
    }
    /**
     * クリップ状態取得する.
     * @return クリップ状態済み/未
     */
    public boolean isClipStatus() {
        return mClipStatus;
    }

    /**
     * クリップ状態設定する.
     * @param mClipStatus クリップ
     */
    public void setClipStatus(final boolean mClipStatus) {
        this.mClipStatus = mClipStatus;
    }
    /**
     * 視聴通知とコンテンツタイプを指定する.
     *
     * @param dispType      　番組種別
     * @param contentsType  　コンテンツ種別
     * @param linearEndDate 　放送終了日時
     * @param tvService     　サービス種別
     * @param dTV           dTVフラグ
     */
    public void setIsNotify(final String dispType, final String contentsType,
                            final String linearEndDate, final String tvService, final String dTV) {

        //yyyy/MM/dd HH:mm:ss形式の時はエポック秒に変換する
        String epocLinearEndDate = linearEndDate;
        if (!DBUtils.isNumber(epocLinearEndDate)) {
            epocLinearEndDate = String.valueOf(DateUtils.getEpochTime(epocLinearEndDate));
        }
        //EPG/DTVはdispType,contentsTypeの内容で判定する
        if (dispType != null && dispType.equals(TV_PROGRAM_CHECK)
                && contentsType != null && contentsType.length() > 0
                && Long.parseLong(epocLinearEndDate) < DateUtils.getNowTimeFormatEpoch()) {
            //dispTypeがtv_programかつcontentsTypeにデータが存在かつ番組終了時間が現在時刻未満であればEPGと判断
            mIsNotify = true;
            setTvType(tvService);
        } else {
            mIsNotify = false;
            setDtvType(dTV);
        }
    }

    /**
     * TV種別設定.
     *
     * @param tvService TV種別
     */
    private void setTvType(final String tvService) {
        if (tvService != null) {
            switch (tvService) {
                case H4D_IPTV_SERVICE_CONTENTS:
                    mType = WebApiBasePlala.CLIP_TYPE_H4D_IPTV;
                    break;
                case DCH_SERVICE_CONTENTS:
                    mType = WebApiBasePlala.CLIP_TYPE_DCH;
                    break;
                //TODO：上記二つ以外の仕様が未定のため暫定対応
                default:
                    mType = "";
                    break;
            }
        }
    }

    /**
     * DTV種別設定.
     *
     * @param dtv dTV種別
     */
    private void setDtvType(final String dtv) {
        if (dtv != null && dtv.equals(DTV_SERVICE_CONTENTS_TRUE)) {
            //dtv_vod
            mType = WebApiBasePlala.CLIP_TYPE_DTV_VOD;
        } else {
            //h4d_vod
            mType = WebApiBasePlala.CLIP_TYPE_H4D_VOD;
        }
    }
}
