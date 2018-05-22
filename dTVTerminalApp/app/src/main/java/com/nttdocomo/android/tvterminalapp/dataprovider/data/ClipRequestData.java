/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.dao.ClipKeyListDao;
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
    private ClipKeyListDao.TableTypeEnum mTableType = null;
    /**視聴通知判定.*/
    private boolean mIsNotify = false;
    /**クリップ未/済.*/
    private boolean mClipStatus = false;
    /**disp_type値 tv_program.*/
    private static final String DISP_TYPE_TV_PROGRAM = "tv_program";
    /**tv_service値 h4d_iptv.*/
    private static final String TV_SERVICE_H4D_CONTENTS = "1";
    /**tv_service値 dch.*/
    private static final String TV_SERVICE_DCH_CONTENTS = "2";
    /**content_type値 dCh番組1.*/
    private static final String CONTENT_TYPE_DCH_PROGRAM_1 = "1";
    /**content_type値 dCh番組2.*/
    private static final String CONTENT_TYPE_DCH_PROGRAM_2 = "2";
    /**content_type値 dCh関連VOD.*/
    private static final String CONTENT_TYPE_DCH_VOD = "3";
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
     * クリップタイプを取得する.クリップに関する判定以外には用いない事.
     * @return クリップタイプ
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
     * 放送開始日時 を取得する.
      * @return 放送開始日時
     */
    public String getLinearStartDate() {
        return mLinearStartDate;
    }

    /**
     * 放送開始日時 を設定する.
     *
     * @param mLinearStartDate 番組開始時間
     */
    public void setLinearStartDate(final String mLinearStartDate) {
        this.mLinearStartDate = mLinearStartDate;
    }

    /**
     * 放送終了日時 を取得する.
     * @return 放送終了日時
     */
    public String getLinearEndDate() {
        return mLinearEndDate;
    }

    /**
     * 放送終了日時 を設定する.
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
    public ClipKeyListDao.TableTypeEnum getTableType() {
        return mTableType;
    }

    /**
     * テーブルタイプ設定する.
     * @param mTableType テーブルタイプ
     */

    public void setTableType(final ClipKeyListDao.TableTypeEnum mTableType) {
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
     * 視聴通知とクリップコンテンツタイプを判断して設定する.
     *
     * @param dispType      　番組種別
     * @param contentsType  　コンテンツ種別
     * @param vodStartDate 　VOD開始日時
     * @param tvService     　サービス種別
     * @param dTV           dTVフラグ
     */
    public void setIsNotify(final String dispType, final String contentsType,
                            final long vodStartDate, final String tvService, final String dTV) {

        //EPG/DTVはdispType,contentsTypeの内容で判定する
        if (dispType != null && dispType.equals(DISP_TYPE_TV_PROGRAM)) {
            if (tvService != null && tvService.equals(TV_SERVICE_H4D_CONTENTS)) {
                //dispTypeがtv_programかつtvServiceが1ならH4dのIPTVコンテンツ
                mType = WebApiBasePlala.CLIP_TYPE_H4D_IPTV;
                mIsNotify = true;
            } else if (tvService != null && tvService.equals(TV_SERVICE_DCH_CONTENTS)) {
                //dispTypeがtv_programかつtvServiceが2なら全てdChコンテンツ(見逃しもこちら)
                mType = WebApiBasePlala.CLIP_TYPE_DCH;

                //視聴通知フラグの設定（EPG:true、VOD:false）
                if (contentsType != null && contentsType.equals(CONTENT_TYPE_DCH_VOD)) {
                    //contents_typeが3(dCh関連VOD)なら固定でVODなので視聴通知はoffにする
                    mIsNotify = false;
                } else if (contentsType != null
                        && ((contentsType.equals(CONTENT_TYPE_DCH_PROGRAM_1)) || (contentsType.equals(CONTENT_TYPE_DCH_PROGRAM_2)))) {
                    //contents_typeが1or2(dCh番組)なら見逃し判定を行い、見逃し化している物は視聴通知はoffにする
                    mIsNotify = vodStartDate > DateUtils.getNowTimeFormatEpoch();
                } else {
                    //contents_typeが未設定やその他は番組扱いする
                    mIsNotify = true;
                }
            } else {
                //tvServiceが未設定の場合は異常.
                DTVTLogger.warning("tv_program content has not tv_service!!");
                mType = "";
                mIsNotify = false;
            }
        } else {
            //dispTypeがtv_program以外は固定でVOD.dTVフラグでdTVかどうかを判断
            if (dTV != null && dTV.equals(DTV_SERVICE_CONTENTS_TRUE)) {
                //dtv_vod
                mType = WebApiBasePlala.CLIP_TYPE_DTV_VOD;
            } else {
                //h4d_vod
                mType = WebApiBasePlala.CLIP_TYPE_H4D_VOD;
            }
            mIsNotify = false;
        }
    }
}
