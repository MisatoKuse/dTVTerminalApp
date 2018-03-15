/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.vodmetafulldata.Puinf;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * VOD&番組メタレスポンス（フル版）.
 */
public class VodMetaFullData implements Serializable {

    // crid
    private String mCrid = null;
    // コンテンツID
    private String mCid = null;
    // タイトルID（dTV）
    private String mTitle_id = null;
    // エピソードID（dTV）
    private String mEpisode_id = null;
    // タイトル
    private String mTitle = null;
    // エピソードタイトル
    private String mEpititle = null;
    // タイトルルビ
    private String mTitleruby = null;
    // 表示タイプ
    private String mDisp_type = null;
    // 表示開始日時(EPOC秒なので数値)
    private long mDisplay_start_date = 0;
    // 表示終了日時(EPOC秒なので数値)
    private long mDisplay_end_date = 0;
    // コンテンツ自体の有効開始日時(PITのみ)(EPOC秒なので数値)
    private long mAvail_start_date = 0;
    // コンテンツ自体の有効期限日時(PITのみ)(EPOC秒なので数値)
    private long mAvail_end_date = 0;
    // 有効開始日時(EPOC秒なので数値)
    private long mPublish_start_date = 0;
    // 有効期限日時(EPOC秒なので数値)
    private long mPublish_end_date = 0;
    // 新着期間開始(EPOC秒なので数値)
    private long mNewa_start_date = 0;
    // 新着期間終了(EPOC秒なので数値)
    private long mNewa_end_date = 0;
    // サムネイル（640＊360）
    private String mThumb_640_360 = null;
    // サムネイル（448＊252）
    private String mThumb_448_252 = null;
    // dtvサムネイル（640＊360）
    private String mDtv_thumb_640_360 = null;
    // dtvサムネイル（448＊252）
    private String mDtv_thumb_448_252 = null;
    // コピーライト
    private String mCopyright = null;
    // 尺長
    private int mDur = 0;
    // デモフラグ
    private String mDemong = null;
    // 見放題フラグ
    private String mBvflg = null;
    // 4Kフラグ
    private int m4kflg = 0;
    // HDRフラグ
    private String mHdrflg = null;
    // 配信ステータス
    private String mDelivery = null;
    // 配信ステータス
    private String mR_value = null;
    // アダルトフラグ
    private int mAdult = 0;
    // ジャンル
    private String[] mGenre_array = null;
    // あらすじ(long)(数値ではなく長いの意味の方)
    private String mSynop = null;
    // パーチャスID
    private String mPuid = null;
    // 価格(税込)
    private String mPrice = null;
    // 購入単位の期間(3日の3)
    private String mQrange = null;
    // 購入単位の単位(3日の「日」)
    private String mQunit = null;
    // 販売開始日時(EPOC秒なので数値)
    private long mPu_start_date = 0;
    // 販売終了日時(EPOC秒なので数値)
    private long mPu_end_date = 0;
    // 出演者情報（ロール|出演者名）
    private String[] mCredit_array = null;
    // レーティング値
    private double mRating = 0;
    // dTVフラグ
    private String mDtv = null;
    // dTVタイプ.
    private String mDtvType = null;
    // CHSVOD
    private String mChsvod = null;
    // クリップ判定に利用(※一部コンテンツはこれだけでは判定不可)
    private String mSearch_ok = null;
    // ライセンス情報リスト
    private String[] mLiinf_array = null;
    // 販売情報リスト(原文が全て大文字なので、こちらも大文字)
    private List<Puinf> mPUINF = new ArrayList<>();
    // 字幕
    private String mCapl = null;
    // 二ヶ国語
    private String mBilingal = null;
    // コンテンツID（見逃し、関連VOD用）
    private String mTv_cid = null;
    // サービスID
    private String mService_id = null;
    // イベントID
    private String mEvent_id = null;
    // チャンネル番号
    private String mChno = null;
    // 放送種別（多ｃｈ、dch）
    private String mTv_service = null;
    // 見逃しタイプ（見逃し：切り出し、見逃し：完パケ、関連VOD）
    private String mContent_type = null;
    // VOD配信開始日時
    private long mVod_start_date = 0;
    // VOD配信終了日時
    private long mVod_end_date = 0;
    // 主ジャンル（ARIB）
    private String mMain_genre = null;
    // 副ジャンル（ARIB）
    private String[] mSecond_genre_array = null;
    // コピー制御
    private String mCopy = null;
    // 音声情報
    private String[] mAdinfo_array = null;
    // 関連VODのcrid
    private String[] mRelational_id_array = null;
    // クリップ可否
    private boolean mClipExec = false;
    // クリップ未/済
    private boolean mClipStatus = false;
    // ESTフラグ
    private String mEstFlag = null;

    private static final long serialVersionUID = 3855428172716406303L;

    public String[] getRootPara() {
        return mRootPara.clone();
    }

    public String getCrid() {
        return mCrid;
    }

    public void setCrid(final String crid) {
        mCrid = crid;
    }

    public String getCid() {
        return mCid;
    }

    public void setCid(final String cid) {
        mCid = cid;
    }

    public String getTitle_id() {
        return mTitle_id;
    }

    public void setTitle_id(final String title_id) {
        mTitle_id = title_id;
    }

    public String getEpisode_id() {
        return mEpisode_id;
    }

    public void setEpisode_id(final String episode_id) {
        mEpisode_id = episode_id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(final String title) {
        mTitle = title;
    }

    public String getEpititle() {
        return mEpititle;
    }

    public void setEpititle(final String epititle) {
        mEpititle = epititle;
    }

    public String getTitleruby() {
        return mTitleruby;
    }

    public void setTitleruby(final String titleruby) {
        mTitleruby = titleruby;
    }

    public String getDisp_type() {
        return mDisp_type;
    }

    public void setDisp_type(final String disp_type) {
        mDisp_type = disp_type;
    }

    public long getDisplay_start_date() {
        return mDisplay_start_date;
    }

    public void setDisplay_start_date(final long display_start_date) {
        mDisplay_start_date = display_start_date;
    }

    public long getDisplay_end_date() {
        return mDisplay_end_date;
    }

    public void setDisplay_end_date(final long display_end_date) {
        mDisplay_end_date = display_end_date;
    }

    public long getAvail_start_date() {
        return mAvail_start_date;
    }

    public void setAvail_start_date(final long avail_start_date) {
        mAvail_start_date = avail_start_date;
    }

    public long getAvail_end_date() {
        return mAvail_end_date;
    }

    public void setAvail_end_date(final long avail_end_date) {
        mAvail_end_date = avail_end_date;
    }

    public long getPublish_start_date() {
        return mPublish_start_date;
    }

    public void setPublish_start_date(final long publish_start_date) {
        mPublish_start_date = publish_start_date;
    }

    public long getPublish_end_date() {
        return mPublish_end_date;
    }

    public void setPublish_end_date(final long publish_end_date) {
        mPublish_end_date = publish_end_date;
    }

    public long getNewa_start_date() {
        return mNewa_start_date;
    }

    public void setNewa_start_date(final long newa_start_date) {
        mNewa_start_date = newa_start_date;
    }

    public long getNewa_end_date() {
        return mNewa_end_date;
    }

    public void setNewa_end_date(final long newa_end_date) {
        mNewa_end_date = newa_end_date;
    }

    public String getmThumb_640_360() {
        return mThumb_640_360;
    }

    public void setmThumb_640_360(final String mThumb_640_360) {
        this.mThumb_640_360 = mThumb_640_360;
    }

    public String getmThumb_448_252() {
        return mThumb_448_252;
    }

    public void setmThumb_448_252(final String mThumb_448_252) {
        this.mThumb_448_252 = mThumb_448_252;
    }

    public String getmDtv_thumb_640_360() {
        return mDtv_thumb_640_360;
    }

    public void setmDtv_thumb_640_360(final String mDtv_thumb_640_360) {
        this.mDtv_thumb_640_360 = mDtv_thumb_640_360;
    }

    public String getmDtv_thumb_448_252() {
        return mDtv_thumb_448_252;
    }

    public void setmDtv_thumb_448_252(final String mDtv_thumb_448_252) {
        this.mDtv_thumb_448_252 = mDtv_thumb_448_252;
    }

    public String getCopyright() {
        return mCopyright;
    }

    public void setCopyright(final String copyright) {
        mCopyright = copyright;
    }

    public int getDur() {
        return mDur;
    }

    public void setDur(final int dur) {
        mDur = dur;
    }

    public String getDemong() {
        return mDemong;
    }

    public void setDemong(final String demong) {
        mDemong = demong;
    }

    public String getBvflg() {
        return mBvflg;
    }

    public void setBvflg(final String bvflg) {
        mBvflg = bvflg;
    }

    public int getM4kflg() {
        return m4kflg;
    }

    public void setM4kflg(final int m4kflg) {
        this.m4kflg = m4kflg;
    }

    public String getHdrflg() {
        return mHdrflg;
    }

    public void setHdrflg(final String hdrflg) {
        mHdrflg = hdrflg;
    }

    public String getDelivery() {
        return mDelivery;
    }

    public void setDelivery(final String delivery) {
        mDelivery = delivery;
    }

    public String getR_value() {
        return mR_value;
    }

    public void setR_value(final String r_value) {
        mR_value = r_value;
    }

    public int getAdult() {
        return mAdult;
    }

    public void setAdult(final int adult) {
        mAdult = adult;
    }

    public String[] getGenre_array() {
        return mGenre_array.clone();
    }

    public void setGenre_array(final String[] genre_array) {
        mGenre_array = genre_array.clone();
    }

    public String getSynop() {
        return mSynop;
    }

    public void setSynop(final String synop) {
        mSynop = synop;
    }

    public String getPuid() {
        return mPuid;
    }

    public void setPuid(final String puid) {
        mPuid = puid;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(final String price) {
        mPrice = price;
    }

    public String getQrange() {
        return mQrange;
    }

    public void setQrange(final String qrange) {
        mQrange = qrange;
    }

    public String getQunit() {
        return mQunit;
    }

    public void setQunit(final String qunit) {
        mQunit = qunit;
    }

    public long getmPu_start_date() {
        return mPu_start_date;
    }

    public void setmPu_start_date(final long mPu_start_date) {
        this.mPu_start_date = mPu_start_date;
    }

    public long getmPu_end_date() {
        return mPu_end_date;
    }

    public void setmPu_end_date(final long mPu_end_date) {
        this.mPu_end_date = mPu_end_date;
    }

    public String[] getmCredit_array() {
        //コピーを作成して返す
        return mCredit_array.clone();
    }

    public void setmCredit_array(final String[] credit_array) {
        //コピーして蓄積する
        this.mCredit_array = credit_array.clone();
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(final double rating) {
        mRating = rating;
    }

    public String getDtv() {
        return mDtv;
    }

    public void setDtv(final String dtv) {
        mDtv = dtv;
    }

    public String getmChsvod() {
        return mChsvod;
    }

    public void setmChsvod(final String mChsvod) {
        this.mChsvod = mChsvod;
    }

    public String getmSearch_ok() {
        return mSearch_ok;
    }

    public void setmSearch_ok(final String mSearch_ok) {
        this.mSearch_ok = mSearch_ok;
    }

    public String[] getmLiinf_array() {
        return mLiinf_array.clone();
    }

    public void setmLiinf_array(final String[] liinfArray) {
        this.mLiinf_array = liinfArray.clone();
    }

    public List<Puinf> getmPUINF() {
        return mPUINF;
    }

    public void setmPUINF(final List<Puinf> mPUINF) {
        this.mPUINF = mPUINF;
    }

    public String getmCapl() {
        return mCapl;
    }

    public void setmCapl(final String mCapl) {
        this.mCapl = mCapl;
    }

    public String getmBilingal() {
        return mBilingal;
    }

    public void setmBilingal(final String mBilingal) {
        this.mBilingal = mBilingal;
    }

    public String getmTv_cid() {
        return mTv_cid;
    }

    public void setmTv_cid(final String mTv_cid) {
        this.mTv_cid = mTv_cid;
    }

    public String getmService_id() {
        return mService_id;
    }

    public void setmService_id(final String mService_id) {
        this.mService_id = mService_id;
    }

    public String getmEvent_id() {
        return mEvent_id;
    }

    public void setmEvent_id(final String mEvent_id) {
        this.mEvent_id = mEvent_id;
    }

    public String getmChno() {
        return mChno;
    }

    public void setmChno(final String mChno) {
        this.mChno = mChno;
    }

    public String getmTv_service() {
        return mTv_service;
    }

    public void setmTv_service(final String mTv_service) {
        this.mTv_service = mTv_service;
    }

    public String getmContent_type() {
        return mContent_type;
    }

    public void setmContent_type(final String mContent_type) {
        this.mContent_type = mContent_type;
    }

    public long getmVod_start_date() {
        return mVod_start_date;
    }

    public void setmVod_start_date(final long mVod_start_date) {
        this.mVod_start_date = mVod_start_date;
    }

    public long getmVod_end_date() {
        return mVod_end_date;
    }

    public void setmVod_end_date(final long mVod_end_date) {
        this.mVod_end_date = mVod_end_date;
    }

    public String getmMain_genre() {
        return mMain_genre;
    }

    public void setmMain_genre(final String mMain_genre) {
        this.mMain_genre = mMain_genre;
    }

    public String[] getmSecond_genre_array() {
        return mSecond_genre_array.clone();
    }

    public void setmSecond_genre_array(final String[] mSecond_genre_array) {
        this.mSecond_genre_array = mSecond_genre_array.clone();
    }

    public String getmCopy() {
        return mCopy;
    }

    public void setmCopy(final String mCopy) {
        this.mCopy = mCopy;
    }

    public String[] getmAdinfo_array() {
        return mAdinfo_array.clone();
    }

    public void setmAdinfo_array(final String[] adinfo_array) {
        this.mAdinfo_array = adinfo_array.clone();
    }

    public String[] getmRelational_id_array() {
        return mRelational_id_array.clone();
    }

    public void setmRelational_id_array(final String[] mRelational_id_array) {
        this.mRelational_id_array = mRelational_id_array.clone();
    }

    public boolean isClipExec() {
        return mClipExec;
    }

    private void setClipExec(final boolean mClipExec) {
        this.mClipExec = mClipExec;
    }

    public boolean isClipStatus() {
        return mClipStatus;
    }

    public void setClipStatus(final boolean mClipStatus) {
        this.mClipStatus = mClipStatus;
    }

    // キー名：単一データ(順番が分かりやすいように配列とリストの情報も残してある)
    private static final String[] mRootPara = {
            JsonConstants.META_RESPONSE_CRID, JsonConstants.META_RESPONSE_CID,
            JsonConstants.META_RESPONSE_TITLE_ID, JsonConstants.META_RESPONSE_EPISODE_ID,
            JsonConstants.META_RESPONSE_TITLE, JsonConstants.META_RESPONSE_EPITITLE,
            JsonConstants.META_RESPONSE_TITLERUBY, JsonConstants.META_RESPONSE_DISP_TYPE,
            JsonConstants.META_RESPONSE_DISPLAY_START_DATE,
            JsonConstants.META_RESPONSE_DISPLAY_END_DATE,
            JsonConstants.META_RESPONSE_AVAIL_START_DATE,
            JsonConstants.META_RESPONSE_AVAIL_END_DATE,
            JsonConstants.META_RESPONSE_PUBLISH_START_DATE,
            JsonConstants.META_RESPONSE_PUBLISH_END_DATE,
            JsonConstants.META_RESPONSE_NEWA_START_DATE,
            JsonConstants.META_RESPONSE_NEWA_END_DATE,
            JsonConstants.META_RESPONSE_THUMB_640, JsonConstants.META_RESPONSE_THUMB_448,
            JsonConstants.META_RESPONSE_DTV_THUMB_640, JsonConstants.META_RESPONSE_DTV_THUMB_448,
            JsonConstants.META_RESPONSE_COPYRIGHT, JsonConstants.META_RESPONSE_DUR,
            JsonConstants.META_RESPONSE_DEMONG, JsonConstants.META_RESPONSE_BVFLG,
            JsonConstants.META_RESPONSE_4KFLG, JsonConstants.META_RESPONSE_HDRFLG,
            JsonConstants.META_RESPONSE_DELIVERY, JsonConstants.META_RESPONSE_R_VALUE,
            JsonConstants.META_RESPONSE_ADULT, //JsonConstants.META_RESPONSE_GENRE_ARRAY,
            JsonConstants.META_RESPONSE_SYNOP, JsonConstants.META_RESPONSE_PUID,
            JsonConstants.META_RESPONSE_PRICE, JsonConstants.META_RESPONSE_QRANGE,
            JsonConstants.META_RESPONSE_QUNIT, JsonConstants.META_RESPONSE_PU_START_DATE,
            JsonConstants.META_RESPONSE_PU_END_DATE, //JsonConstants.META_RESPONSE_CREDIT_ARRAY,
            JsonConstants.META_RESPONSE_RATING, JsonConstants.META_RESPONSE_DTV,
            JsonConstants.META_RESPONSE_CHSVOD, JsonConstants.META_RESPONSE_SEARCH_OK,
            //JsonConstants.META_RESPONSE_LIINF_ARRAY,JsonConstants.META_RESPONSE_PUINF,
            JsonConstants.META_RESPONSE_CAPL, JsonConstants.META_RESPONSE_BILINGAL,
            JsonConstants.META_RESPONSE_TV_CID, JsonConstants.META_RESPONSE_SERVICE_ID,
            JsonConstants.META_RESPONSE_EVENT_ID, JsonConstants.META_RESPONSE_CHNO,
            JsonConstants.META_RESPONSE_TV_SERVICE, JsonConstants.META_RESPONSE_CONTENT_TYPE,
            JsonConstants.META_RESPONSE_VOD_START_DATE, JsonConstants.META_RESPONSE_VOD_END_DATE,
            JsonConstants.META_RESPONSE_MAIN_GENRE, JsonConstants.META_RESPONSE_DTV_TYPE,
            //JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY,
            JsonConstants.META_RESPONSE_COPY,
            //JsonConstants.META_RESPONSE_ADINFO_ARRAY,
            //JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY,
    };

    // キー名：配列データ
    private static final String[] mRootArrayPara = {
            JsonConstants.META_RESPONSE_GENRE_ARRAY, JsonConstants.META_RESPONSE_CREDIT_ARRAY,
            JsonConstants.META_RESPONSE_LIINF_ARRAY, JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY,
            JsonConstants.META_RESPONSE_ADINFO_ARRAY, JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY,
    };

    /**
     * コンストラクタ.
     */
    public VodMetaFullData() {
        //各配列の初期化
        mCredit_array = new String[0];
        mLiinf_array = new String[0];
        mSecond_genre_array = new String[0];
        mAdinfo_array = new String[0];
        mRelational_id_array = new String[0];
        mGenre_array = new String[0];

        //リストの初期化
        mPUINF = new ArrayList<>();
    }

    /**
     * VOD&番組メタレスポンス（フル版）の json データをデータオブジェクトに変換.
     *
     * @param userState 　ユーザ情報
     * @param jsonObj 　json データ
     */
    public void setData(final UserState userState, final JSONObject jsonObj) {
        // ライセンス/販売情報リスト
        Puinf puinf;
        try {
            if (jsonObj != null) {
                // 単一データ
                for (String item : mRootPara) {
                    if (!jsonObj.isNull(item)) {
                        setMember(item, jsonObj.get(item));
                        setClipExec(ClipUtils.isCanClip(userState, getDisp_type(), getmSearch_ok(), getDtv(), getDtvType()));
                    }
                }

                // 配列データ
                for (String item : mRootArrayPara) {
                    if (!jsonObj.isNull(item)) {
                        setMember(item, jsonObj.getJSONArray(item));
                    }
                }

                // 販売情報リストデータ
                if (!jsonObj.isNull(JsonConstants.META_RESPONSE_PUINF)) {
                    JSONArray puinfs = jsonObj.getJSONArray(JsonConstants.META_RESPONSE_PUINF);
                    if (puinfs.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < puinfs.length(); i++) {
                        puinf = new Puinf();
                        puinf.setPuinfData(puinfs.getJSONObject(i));
                        mPUINF.add(puinf);
                    }
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * キーとキーの値をメンバーにセットする.
     * (メソッドが長くて複雑と警告が出るが、分割するとむしろわかりにくくなるのでこのままとする)
     *
     * @param key  キー
     * @param data キーの値
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    private void setMember(final String key, final Object data) {
        //キーに値があれば、それを元に値を格納する
        if (key != null && !key.isEmpty()) {
            switch (key) {
                case JsonConstants.META_RESPONSE_CRID:
                    // crid
                    mCrid = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_CID:
                    // コンテンツID
                    mCid = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_TITLE_ID:
                    // タイトルID（dTV）
                    mTitle_id = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_EPISODE_ID:
                    // エピソードID（dTV）
                    mEpisode_id = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_TITLE:
                    // タイトル
                    mTitle = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_EPITITLE:
                    // エピソードタイトル
                    mEpititle = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_TITLERUBY:
                    mTitleruby = String.valueOf(data);           // タイトルルビ
                    break;
                case JsonConstants.META_RESPONSE_DISP_TYPE:
                    // 表示タイプ
                    mDisp_type = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_DISPLAY_START_DATE:
                    // 表示開始日時
                    mDisplay_start_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_DISPLAY_END_DATE:
                    // 表示終了日時
                    mDisplay_end_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_AVAIL_START_DATE:
                    // コンテンツ自体の有効開始日時(PITのみ)
                    mAvail_start_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_AVAIL_END_DATE:
                    // コンテンツ自体の有効期限日時(PITのみ)
                    mAvail_end_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_PUBLISH_START_DATE:
                    // 有効開始日時
                    mPublish_start_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_PUBLISH_END_DATE:
                    // 有効期限日時
                    mPublish_end_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_NEWA_START_DATE:
                    // 新着期間開始
                    mNewa_start_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_NEWA_END_DATE:
                    // 新着期間終了
                    mNewa_end_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_THUMB_640:
                    //サムネイル（640＊360）
                    mThumb_640_360 = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_THUMB_448:
                    //サムネイル（448＊252）
                    mThumb_448_252 = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_DTV_THUMB_640:
                    //dtvサムネイル（640＊360）
                    mDtv_thumb_640_360 = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_DTV_THUMB_448:
                    //dtvサムネイル（448＊252）
                    mDtv_thumb_448_252 = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_COPYRIGHT:
                    // コピーライト
                    mCopyright = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_DUR:
                    // 尺長
                    mDur = StringUtils.changeString2Int(data);
                    break;
                case JsonConstants.META_RESPONSE_DEMONG:
                    // デモフラグ
                    mDemong = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_BVFLG:
                    // 見放題フラグ
                    mBvflg = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_4KFLG:
                    // ４Kフラグ
                    m4kflg = StringUtils.changeString2Int(data);
                    break;
                case JsonConstants.META_RESPONSE_HDRFLG:
                    // HDRフラグ
                    mHdrflg = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_DELIVERY:
                    // 配信ステータス
                    mDelivery = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_R_VALUE:
                    // パレンタル情報
                    mR_value = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_ADULT:
                    // アダルトフラグ
                    mAdult = StringUtils.changeString2Int(data);
                    break;
                case JsonConstants.META_RESPONSE_GENRE_ARRAY:
                    // ジャンル情報
                    mGenre_array = StringUtils.JSonArray2StringArray((JSONArray) data);
                    break;
                case JsonConstants.META_RESPONSE_SYNOP:
                    // あらすじ
                    mSynop = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_PUID:
                    // パーチャスID
                    mPuid = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_PRICE:
                    // 価格(税込)
                    mPrice = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_QRANGE:
                    // 購入単位の期間(3日の3)
                    mQrange = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_QUNIT:
                    // 購入単位の単位(3日の「日」)
                    mQunit = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_PU_START_DATE:
                    // 販売開始日時
                    mPu_start_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_PU_END_DATE:
                    // 販売終了日時
                    mPu_end_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_CREDIT_ARRAY:
                    // 出演者情報（ロール|出演者名）
                    mCredit_array = StringUtils.JSonArray2StringArray((JSONArray) data);
                    break;
                case JsonConstants.META_RESPONSE_RATING:
                    // レーティング値
                    mRating = DBUtils.getDecimal(data);
                    break;
                case JsonConstants.META_RESPONSE_DTV:
                    // dTVフラグ
                    mDtv = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_DTV_TYPE:
                    // dTVフラグ
                    mDtvType = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_EST_FLAG:
                    // ESTフラグ
                    mEstFlag = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_CHSVOD:
                    //CHSVOD
                    mChsvod = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_SEARCH_OK:
                    //クリップ判定に利用
                    mSearch_ok = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_LIINF_ARRAY:
                    //ライセンス情報リスト
                    mLiinf_array = StringUtils.JSonArray2StringArray((JSONArray) data);
                    break;
                case JsonConstants.META_RESPONSE_CAPL:
                    //字幕
                    mCapl = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_BILINGAL:
                    //二ヶ国語
                    mBilingal = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_TV_CID:
                    //コンテンツID（見逃し、関連VOD用）
                    mTv_cid = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_SERVICE_ID:
                    //サービスID
                    mService_id = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_EVENT_ID:
                    //イベントID
                    mEvent_id = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_CHNO:
                    //チャンネル番号
                    mChno = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_TV_SERVICE:
                    //放送種別（多ch、dch）
                    mTv_service = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_CONTENT_TYPE:
                    //見逃しタイプ
                    mContent_type = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_VOD_START_DATE:
                    //VOD配信開始日時
                    mVod_start_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_VOD_END_DATE:
                    //VOD配信終了日時
                    mVod_end_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_MAIN_GENRE:
                    //主ジャンル（ARIB）
                    mMain_genre = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY:
                    //副ジャンル（ARIB）
                    mSecond_genre_array = StringUtils.JSonArray2StringArray((JSONArray) data);
                    break;
                case JsonConstants.META_RESPONSE_COPY:
                    //コピー制御
                    mCopy = String.valueOf(data);
                    break;
                case JsonConstants.META_RESPONSE_ADINFO_ARRAY:
                    //音声情報
                    mAdinfo_array = StringUtils.JSonArray2StringArray((JSONArray) data);
                    break;
                case JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY:
                    //関連VODのcrid
                    mRelational_id_array = StringUtils.JSonArray2StringArray((JSONArray) data);
                    break;
                default:
            }
        }
    }

    /**
     * キーの値を取得する.
     * (メソッドが長くて複雑と警告が出るが、分割するとむしろわかりにくくなるのでこのままとする)
     *
     * @param key 取得したい値のキー
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public Object getMember(final String key) {
        if (key == null || key.isEmpty()) {
            //キーが無いので、空文字を返す
            return "";
        } else {
            switch (key) {
                case JsonConstants.META_RESPONSE_CRID:
                    // crid
                    return mCrid;
                case JsonConstants.META_RESPONSE_CID:
                    // コンテンツID
                    return mCid;
                case JsonConstants.META_RESPONSE_TITLE_ID:
                    // タイトルID（dTV）
                    return mTitle_id;
                case JsonConstants.META_RESPONSE_EPISODE_ID:
                    // エピソードID（dTV）
                    return mEpisode_id;
                case JsonConstants.META_RESPONSE_TITLE:
                    // タイトル
                    return mTitle;
                case JsonConstants.META_RESPONSE_EPITITLE:
                    // エピソードタイトル
                    return mEpititle;
                case JsonConstants.META_RESPONSE_TITLERUBY:
                    // タイトルルビ
                    return mTitleruby;
                case JsonConstants.META_RESPONSE_DISP_TYPE:
                    // 表示タイプ
                    return mDisp_type;
                case JsonConstants.META_RESPONSE_DISPLAY_START_DATE:
                    // 表示開始日時
                    return mDisplay_start_date;
                case JsonConstants.META_RESPONSE_DISPLAY_END_DATE:
                    // 表示終了日時
                    return mDisplay_end_date;
                case JsonConstants.META_RESPONSE_AVAIL_START_DATE:
                    // コンテンツ自体の有効開始日時(PITのみ)
                    return mAvail_start_date;
                case JsonConstants.META_RESPONSE_AVAIL_END_DATE:
                    // コンテンツ自体の有効期限日時(PITのみ)
                    return mAvail_end_date;
                case JsonConstants.META_RESPONSE_PUBLISH_START_DATE:
                    // 有効開始日時
                    return mPublish_start_date;
                case JsonConstants.META_RESPONSE_PUBLISH_END_DATE:
                    // 有効期限日時
                    return mPublish_end_date;
                case JsonConstants.META_RESPONSE_NEWA_START_DATE:
                    // 新着期間開始
                    return mNewa_start_date;
                case JsonConstants.META_RESPONSE_NEWA_END_DATE:
                    // 新着期間終了
                    return mNewa_end_date;
                case JsonConstants.META_RESPONSE_THUMB_640:
                    //サムネイル（640＊360）
                    return mThumb_640_360;
                case JsonConstants.META_RESPONSE_THUMB_448:
                    //サムネイル（448＊252）
                    return mThumb_448_252;
                case JsonConstants.META_RESPONSE_DTV_THUMB_640:
                    //dtvサムネイル（640＊360）
                    return mDtv_thumb_640_360;
                case JsonConstants.META_RESPONSE_DTV_THUMB_448:
                    //dｔｖサムネイル（448＊252）
                    return mDtv_thumb_448_252;
                case JsonConstants.META_RESPONSE_COPYRIGHT:
                    // コピーライト
                    return mCopyright;
                case JsonConstants.META_RESPONSE_DUR:
                    // 尺長
                    return mDur;
                case JsonConstants.META_RESPONSE_DEMONG:
                    // デモフラグ
                    return mDemong;
                case JsonConstants.META_RESPONSE_BVFLG:
                    // 見放題フラグ
                    return mBvflg;
                case JsonConstants.META_RESPONSE_4KFLG:
                    // ４Kフラグ
                    return m4kflg;
                case JsonConstants.META_RESPONSE_HDRFLG:
                    // HDRフラグ
                    return mHdrflg;
                case JsonConstants.META_RESPONSE_DELIVERY:
                    // 配信ステータス
                    return mDelivery;
                case JsonConstants.META_RESPONSE_R_VALUE:
                    // パレンタル情報
                    return mR_value;
                case JsonConstants.META_RESPONSE_ADULT:
                    return mAdult;               // アダルトフラグ
                case JsonConstants.META_RESPONSE_GENRE_ARRAY:
                    //ジャンル情報
                    return mGenre_array.clone();
                case JsonConstants.META_RESPONSE_SYNOP:
                    // あらすじ
                    return mSynop;
                case JsonConstants.META_RESPONSE_PUID:
                    // パーチャスID
                    return mPuid;
                case JsonConstants.META_RESPONSE_PRICE:
                    // 価格(税込)
                    return mPrice;
                case JsonConstants.META_RESPONSE_QRANGE:
                    // 購入単位の期間(3日の3)
                    return mQrange;
                case JsonConstants.META_RESPONSE_QUNIT:
                    // 購入単位の単位(3日の「日」)
                    return mQunit;
                case JsonConstants.META_RESPONSE_PU_START_DATE:
                    // 販売開始日時
                    return mPu_start_date;
                case JsonConstants.META_RESPONSE_PU_END_DATE:
                    // 販売終了日時
                    return mPu_end_date;
                case JsonConstants.META_RESPONSE_CREDIT_ARRAY:
                    // 出演者情報（ロール|出演者名）
                    return mCredit_array.clone();
                case JsonConstants.META_RESPONSE_RATING:
                    // レーティング値
                    return mRating;
                case JsonConstants.META_RESPONSE_DTV:
                    // dTVフラグ
                    return mDtv;

                case JsonConstants.META_RESPONSE_CHSVOD:
                    //CHSVOD
                    return mChsvod;
                case JsonConstants.META_RESPONSE_SEARCH_OK:
                    //クリップ判定に利用
                    return mSearch_ok;
                case JsonConstants.META_RESPONSE_LIINF_ARRAY:
                    //ライセンス情報リスト
                    return mLiinf_array.clone();
                case JsonConstants.META_RESPONSE_PUINF:
                    //販売情報リスト
                    return mPUINF;
                case JsonConstants.META_RESPONSE_CAPL:
                    //字幕
                    return mCapl;
                case JsonConstants.META_RESPONSE_BILINGAL:
                    //二ヶ国語
                    return mBilingal;
                case JsonConstants.META_RESPONSE_TV_CID:
                    //コンテンツID（見逃し、関連VOD用）
                    return mTv_cid;
                case JsonConstants.META_RESPONSE_SERVICE_ID:
                    //サービスID
                    return mService_id;
                case JsonConstants.META_RESPONSE_EVENT_ID:
                    //イベントID
                    return mEvent_id;
                case JsonConstants.META_RESPONSE_CHNO:
                    //チャンネル番号
                    return mChno;
                case JsonConstants.META_RESPONSE_TV_SERVICE:
                    //放送種別（多ch、dch）
                    return mTv_service;
                case JsonConstants.META_RESPONSE_CONTENT_TYPE:
                    //見逃しタイプ
                    return mContent_type;
                case JsonConstants.META_RESPONSE_VOD_START_DATE:
                    //VOD配信開始日時
                    return mVod_start_date;
                case JsonConstants.META_RESPONSE_VOD_END_DATE:
                    //VOD配信終了日時
                    return mVod_end_date;
                case JsonConstants.META_RESPONSE_MAIN_GENRE:
                    //主ジャンル（ARIB）
                    return mMain_genre;
                case JsonConstants.META_RESPONSE_SECOND_GENRE_ARRAY:
                    //副ジャンル（ARIB）
                    return mSecond_genre_array.clone();
                case JsonConstants.META_RESPONSE_COPY:
                    //コピー制御
                    return mCopy;
                case JsonConstants.META_RESPONSE_ADINFO_ARRAY:
                    //音声情報
                    return mAdinfo_array.clone();
                case JsonConstants.META_RESPONSE_RELATIONAL_ID_ARRAY:
                    //関連VODのcrid
                    return mRelational_id_array.clone();
                default:
                    return "";
            }
        }
    }

    public String getDtvType() {
        return mDtvType;
    }

    public void setDtvType(final String mDtvType) {
        this.mDtvType = mDtvType;
    }

    public String getEstFlag() {
        return mEstFlag;
    }

    public void setEstFlag(final String mEstFlag) {
        this.mEstFlag = mEstFlag;
    }
}