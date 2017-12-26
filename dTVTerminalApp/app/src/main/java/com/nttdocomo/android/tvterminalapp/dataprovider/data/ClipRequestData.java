/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;

public class ClipRequestData {
    // タイプ
    private String mType = null;
    // コンテンツ識別子
    private String mCrid = null;
    // サービスID
    private String mServiceId = null;
    // イベントID
    private String mEventId = null;
    // タイトルID
    private String mTitleId = null;
    // コンテンツタイトル
    private String mTitle = null;
    // 番組のパレンタル設定値
    private String mRValue = null;
    // 放送開始日時
    private String mLinearStartDate = null;
    // 放送終了日時
    private String mLinearEndDate = null;
    // クリップ状態
    private String mSearchOk = null;
    // クリップ対象
    private String mClipTarget = null;
    // 視聴通知判定
    private boolean mIsNotify = false;
    //EPG判定用
    private String TV_PROGRAM_CHECK = "tv_program";
    //h4d_iptv
    private String H4D_IPTV_SERVICE_CONTENTS = "0";
    //dch
    private String DCH_SERVICE_CONTENTS = "1";
    //h4d_vod
    private String DTV_SERVICE_CONTENTS_FALSE = "0";
    //dtv_vod
    private String DTV_SERVICE_CONTENTS_TRUE = "1";

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getType() {
        return mType;
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

    /**
     * 仕様により avail_start_date を設定する
     *
     * @param mLinearStartDate 番組開始時間
     */
    public void setLinearStartDate(String mLinearStartDate) {
        this.mLinearStartDate = mLinearStartDate;
    }

    public String getLinearEndDate() {
        return mLinearEndDate;
    }

    /**
     * 仕様により avail_end_date を設定する
     *
     * @param mLinearEndDate 番組終了時間
     */
    public void setLinearEndDate(String mLinearEndDate) {
        this.mLinearEndDate = mLinearEndDate;
    }

    public String getSearchOk() {
        return mSearchOk;
    }

    public void setSearchOk(String mSearchOk) {
        this.mSearchOk = mSearchOk;
    }

    public boolean getIsNotify() {
        return mIsNotify;
    }

    /**
     * 視聴通知とコンテンツタイプを指定する
     *
     * @param dispType      　番組種別
     * @param contentsType  　コンテンツ種別
     * @param linearEndDate 　放送終了日時
     * @param tvService     　サービス種別
     * @param dTV           dTVフラグ
     */
    public void setIsNotify(String dispType, String contentsType, String linearEndDate, String tvService, String dTV) {

        //yyyy/MM/dd HH:mm:ss形式の時はエポック秒に変換する
        String epocLinearEndDate = linearEndDate;
        if (!DBUtils.isNumber(epocLinearEndDate)) {
            epocLinearEndDate = String.valueOf(DateUtils.getEpochTime(epocLinearEndDate));
        }
        //EPG/DTVはdispType,contentsTypeの内容で判定する
        if (dispType != null && dispType.equals(TV_PROGRAM_CHECK) &&
                contentsType != null && contentsType.length() > 0 &&
                Long.parseLong(epocLinearEndDate) < DateUtils.getNowTimeFormatEpoch()) {
            //dispTypeがtv_programかつcontentsTypeにデータが存在かつ番組終了時間が現在時刻未満であればEPGと判断
            mIsNotify = true;
            setTvType(tvService);
        } else {
            mIsNotify = false;
            setDtvType(dTV);
        }
    }

    /**
     * TV種別設定
     *
     * @param tvService TV種別
     */
    private void setTvType(String tvService) {
        if (tvService != null) {
            if (tvService.equals(H4D_IPTV_SERVICE_CONTENTS)) {
                //h4d_iptv
                mType = WebApiBasePlala.CLIP_TYPE_H4D_IPTV;
            } else if (tvService.equals(DCH_SERVICE_CONTENTS)) {
                //dch
                mType = WebApiBasePlala.CLIP_TYPE_DCH;
            } else {
                //TODO：上記二つ以外の仕様が未定のため暫定対応
                mType = "";
            }
        }
    }

    /**
     * DTV種別設定
     *
     * @param dtv dTV種別
     */
    private void setDtvType(String dtv) {
        if (dtv != null && dtv.equals(DTV_SERVICE_CONTENTS_TRUE)) {
            //dtv_vod
            mType = WebApiBasePlala.CLIP_TYPE_DTV_VOD;
        } else {
            //h4d_vod
            mType = WebApiBasePlala.CLIP_TYPE_H4D_VOD;
        }
    }
}
