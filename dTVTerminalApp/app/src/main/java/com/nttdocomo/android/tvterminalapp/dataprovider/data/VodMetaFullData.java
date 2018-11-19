/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;


import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.vodmetafulldata.Puinf;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

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
    /**crid.*/
    private String mCrid = null;
    /**コンテンツID.*/
    private String mCid = null;
    /**タイトルID（dTV）.*/
    private String mTitle_id = null;
    /**エピソードID（dTV）.*/
    private String mEpisode_id = null;
    /**タイトル.*/
    private String mTitle = null;
    /**エピソードタイトル.*/
    private String mEpititle = null;
    /**タイトルルビ.*/
    private String mTitleruby = null;
    /**表示タイプ.*/
    private String mDisp_type = null;
    /**表示開始日時(EPOC秒なので数値).*/
    private long mDisplay_start_date = 0;
    /**表示終了日時(EPOC秒なので数値).*/
    private long mDisplay_end_date = 0;
    /**コンテンツ自体の有効開始日時(PITのみ)(EPOC秒なので数値).*/
    private long mAvail_start_date = 0;
    /**コンテンツ自体の有効期限日時(PITのみ)(EPOC秒なので数値).*/
    private long mAvail_end_date = 0;
    /**有効開始日時(EPOC秒なので数値).*/
    private long mPublish_start_date = 0;
    /**有効期限日時(EPOC秒なので数値).*/
    private long mPublish_end_date = 0;
    /**新着期間開始(EPOC秒なので数値).*/
    private long mNewa_start_date = 0;
    /**新着期間終了(EPOC秒なので数値).*/
    private long mNewa_end_date = 0;
    /**サムネイル（640＊360）.*/
    private String mThumb_640_360 = null;
    /**サムネイル（448＊252）.*/
    private String mThumb_448_252 = null;
    /**dtvサムネイル（640＊360）.*/
    private String mDtv_thumb_640_360 = null;
    /**dtvサムネイル（448＊252）.*/
    private String mDtv_thumb_448_252 = null;
    /**コピーライト.*/
    private String mCopyright = null;
    /**尺長.*/
    private int mDur = 0;
    /**デモフラグ.*/
    private String mDemong = null;
    /**見放題フラグ.*/
    private String mBvflg = null;
    /**4Kフラグ.*/
    private int m4kflg = 0;
    /**HDRフラグ.*/
    private String mHdrflg = null;
    /**配信ステータス.*/
    private String mDelivery = null;
    /**配信ステータス.*/
    private String mR_value = null;
    /**アダルトフラグ.*/
    private int mAdult = 0;
    /**ジャンル.*/
    private String[] mGenre_array = null;
    /**あらすじ(long)(数値ではなく長いの意味の方).*/
    private String mSynop = null;
    /**パーチャスID.*/
    private String mPuid = null;
    /**価格(税込).*/
    private String mPrice = null;
    /**購入単位の期間(3日の3).*/
    private String mQrange = null;
    /**購入単位の単位(3日の「日」).*/
    private String mQunit = null;
    /**販売開始日時(EPOC秒なので数値).*/
    private long mPu_start_date = 0;
    /**販売終了日時(EPOC秒なので数値).*/
    private long mPu_end_date = 0;
    /**出演者情報（ロール|出演者名）.*/
    private String[] mCredit_array = null;
    /**レーティング値.*/
    private double mRating = 0;
    /** dTVフラグ.*/
    private String mDtv = null;
    /**dTVタイプ..*/
    private String mDtvType = null;
    /** CHSVOD.*/
    private String mChsvod = null;
    /**クリップ判定に利用(※一部コンテンツはこれだけでは判定不可).*/
    private String mSearch_ok = null;
    /**ライセンス情報リスト.*/
    private String[] mLiinf_array = null;
    /**販売情報リスト(原文が全て大文字なので、こちらも大文字).*/
    private List<Puinf> mPUINF = new ArrayList<>();
    /**字幕.*/
    private String mCapl = null;
    /**二ヶ国語.*/
    private String mBilingal = null;
    /**コンテンツID（見逃し、関連VOD用）.*/
    private String mTv_cid = null;
    /**サービスID.*/
    private String mService_id = null;
    /**イベントID.*/
    private String mEvent_id = null;
    /**チャンネル番号.*/
    private String mChno = null;
    /**放送種別（多ｃｈ、dch）.*/
    private String mTv_service = null;
    /**見逃しタイプ（見逃し：切り出し、見逃し：完パケ、関連VOD）.*/
    private String mContent_type = null;
    /**VOD配信開始日時.*/
    private long mVod_start_date = 0;
    /**VOD配信終了日時.*/
    private long mVod_end_date = 0;
    /**主ジャンル（ARIB）.*/
    private String mMain_genre = null;
    /**副ジャンル（ARIB）.*/
    private String[] mSecond_genre_array = null;
    /**コピー制御.*/
    private String mCopy = null;
    /**音声情報.*/
    private String[] mAdinfo_array = null;
    /**関連VODのcrid.*/
    private String[] mRelational_id_array = null;
    /**クリップ可否.*/
    private boolean mClipExec = false;
    /**クリップ未/済.*/
    private boolean mClipStatus = false;
    /**ESTフラグ.*/
    private String mEstFlag = null;
    /** コンテンツタイプ. **/
    private ContentUtils.ContentsType mContentsType = null;

    private static final long serialVersionUID = 3855428172716406303L;

    /**
     * 単一データキー取得.
     * @return 単一データキー
     */
    public String[] getRootPara() {
        return mRootPara.clone();
    }

    /**
     * crid取得.
     * @return crid
     */
    public String getCrid() {
        return mCrid;
    }

    /**
     * crid設定.
     * @param crid crid
     */
    public void setCrid(final String crid) {
        mCrid = crid;
    }

    /**
     * コンテンツID取得.
     * @return コンテンツID
     */
    public String getCid() {
        return mCid;
    }

    /**
     * コンテンツID設定.
     * @param cid コンテンツID
     */
    public void setCid(final String cid) {
        mCid = cid;
    }

    /**
     *タイトルID（dTV）取得.
     * @return タイトルID（dTV）
     */
    public String getTitle_id() {
        return mTitle_id;
    }

    /**
     * タイトルID（dTV）設定.
     * @param title_id タイトルID（dTV）
     */
    public void setTitle_id(final String title_id) {
        mTitle_id = title_id;
    }

    /**
     * エピソードID（dTV）取得.
     * @return エピソードID（dTV）
     */
    public String getEpisode_id() {
        return mEpisode_id;
    }

    /**
     * エピソードID（dTV）設定.
     * @param episode_id エピソードID（dTV）
     */
    public void setEpisode_id(final String episode_id) {
        mEpisode_id = episode_id;
    }

    /**
     * タイトル取得.
     * @return  タイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * タイトル設定.
     * @param title タイトル
     */
    public void setTitle(final String title) {
        mTitle = title;
    }

    /**
     * エピソードタイトル取得.
     * @return エピソードタイトル
     */
    public String getEpititle() {
        return mEpititle;
    }

    /**
     * エピソードタイトル設定.
     * @param epititle エピソードタイトル
     */
    public void setEpititle(final String epititle) {
        mEpititle = epititle;
    }

    /**
     * タイトルルビ取得.
     * @return タイトルルビ
     */
    public String getTitleruby() {
        return mTitleruby;
    }

    /**
     *タイトルルビ 設定.
     * @param titleruby タイトルルビ
     */
    public void setTitleruby(final String titleruby) {
        mTitleruby = titleruby;
    }

    /**
     * 表示タイプ取得.
     * @return 表示タイプ
     */
    public String getDisp_type() {
        return mDisp_type;
    }

    /**
     * 表示タイプ設定.
     * @param disp_type 表示タイプ
     */
    public void setDisp_type(final String disp_type) {
        mDisp_type = disp_type;
    }

    /**
     * 表示開始日時(EPOC秒なので数値)取得.
     * @return 表示開始日時(EPOC秒なので数値)
     */
    public long getDisplay_start_date() {
        return mDisplay_start_date;
    }

    /**
     * 表示開始日時(EPOC秒なので数値)設定.
     * @param display_start_date 表示開始日時(EPOC秒なので数値)
     */
    public void setDisplay_start_date(final long display_start_date) {
        mDisplay_start_date = display_start_date;
    }

    /**
     * 表示終了日時(EPOC秒なので数値)取得.
     * @return 表示終了日時(EPOC秒なので数値)
     */
    public long getDisplay_end_date() {
        return mDisplay_end_date;
    }

    /**
     * 表示終了日時(EPOC秒なので数値)設定.
     * @param display_end_date 表示終了日時(EPOC秒なので数値)
     */
    public void setDisplay_end_date(final long display_end_date) {
        mDisplay_end_date = display_end_date;
    }

    /**
     * コンテンツ自体の有効開始日時(PITのみ)(EPOC秒なので数値)取得.
     * @return コンテンツ自体の有効開始日時(PITのみ)(EPOC秒なので数値)
     */
    public long getAvail_start_date() {
        return mAvail_start_date;
    }

    /**
     * コンテンツ自体の有効開始日時(PITのみ)(EPOC秒なので数値)設定.
     * @param avail_start_date コンテンツ自体の有効開始日時(PITのみ)(EPOC秒なので数値)
     */
    public void setAvail_start_date(final long avail_start_date) {
        mAvail_start_date = avail_start_date;
    }

    /**
     *コンテンツ自体の有効期限日時(PITのみ)(EPOC秒なので数値) 取得.
     * @return コンテンツ自体の有効期限日時(PITのみ)(EPOC秒なので数値)
     */
    public long getAvail_end_date() {
        return mAvail_end_date;
    }

    /**
     * コンテンツ自体の有効期限日時(PITのみ)(EPOC秒なので数値)設定.
     * @param avail_end_date コンテンツ自体の有効期限日時(PITのみ)(EPOC秒なので数値)
     */
    public void setAvail_end_date(final long avail_end_date) {
        mAvail_end_date = avail_end_date;
    }

    /**
     * 有効開始日時(EPOC秒なので数値)取得.
     * @return 有効開始日時(EPOC秒なので数値)
     */
    public long getPublish_start_date() {
        return mPublish_start_date;
    }

    /**
     * 有効開始日時(EPOC秒なので数値)設定.
     * @param publish_start_date 有効開始日時(EPOC秒なので数値)
     */
    public void setPublish_start_date(final long publish_start_date) {
        mPublish_start_date = publish_start_date;
    }

    /**
     * 有効期限日時(EPOC秒なので数値)取得.
     * @return 有効期限日時(EPOC秒なので数値)
     */
    public long getPublish_end_date() {
        return mPublish_end_date;
    }

    /**
     *有効期限日時(EPOC秒なので数値) 設定.
     * @param publish_end_date 有効期限日時(EPOC秒なので数値)
     */
    public void setPublish_end_date(final long publish_end_date) {
        mPublish_end_date = publish_end_date;
    }

    /**
     * 新着期間開始(EPOC秒なので数値)取得.
     * @return 新着期間開始(EPOC秒なので数値)
     */
    public long getNewa_start_date() {
        return mNewa_start_date;
    }

    /**
     * 新着期間開始(EPOC秒なので数値)設定.
     * @param newa_start_date 新着期間開始(EPOC秒なので数値)
     */
    public void setNewa_start_date(final long newa_start_date) {
        mNewa_start_date = newa_start_date;
    }

    /**
     * 新着期間終了(EPOC秒なので数値)取得.
     * @return 新着期間終了(EPOC秒なので数値)
     */
    public long getNewa_end_date() {
        return mNewa_end_date;
    }

    /**
     * 新着期間終了(EPOC秒なので数値)設定.
     * @param newa_end_date 新着期間終了(EPOC秒なので数値)
     */
    public void setNewa_end_date(final long newa_end_date) {
        mNewa_end_date = newa_end_date;
    }

    /**
     * サムネイル（640＊360）取得.
     * @return サムネイル（640＊360）
     */
    public String getmThumb_640_360() {
        return mThumb_640_360;
    }

    /**
     * サムネイル（640＊360）設定.
     * @param mThumb_640_360 サムネイル（640＊360）
     */
    public void setmThumb_640_360(final String mThumb_640_360) {
        this.mThumb_640_360 = mThumb_640_360;
    }

    /**
     * サムネイル（448＊252）取得.
     * @return サムネイル（448＊252）
     */
    public String getmThumb_448_252() {
        return mThumb_448_252;
    }

    /**
     * サムネイル（448＊252）設定.
     * @param mThumb_448_252 サムネイル（448＊252）
     */
    public void setmThumb_448_252(final String mThumb_448_252) {
        this.mThumb_448_252 = mThumb_448_252;
    }

    /**
     * dtvサムネイル（640＊360）取得.
     * @return dtvサムネイル（640＊360）
     */
    public String getmDtv_thumb_640_360() {
        return mDtv_thumb_640_360;
    }

    /**
     * dtvサムネイル（640＊360）設定.
     * @param mDtv_thumb_640_360 dtvサムネイル（640＊360）
     */
    public void setmDtv_thumb_640_360(final String mDtv_thumb_640_360) {
        this.mDtv_thumb_640_360 = mDtv_thumb_640_360;
    }

    /**
     * dtvサムネイル（448＊252）取得.
     * @return dtvサムネイル（448＊252）
     */
    public String getmDtv_thumb_448_252() {
        return mDtv_thumb_448_252;
    }

    /**
     * dtvサムネイル（448＊252）設定.
     * @param mDtv_thumb_448_252 dtvサムネイル（448＊252）
     */
    public void setmDtv_thumb_448_252(final String mDtv_thumb_448_252) {
        this.mDtv_thumb_448_252 = mDtv_thumb_448_252;
    }

    /**
     * コピーライト取得.
     * @return コピーライト
     */
    public String getCopyright() {
        return mCopyright;
    }

    /**
     * コピーライト設定.
     * @param copyright コピーライト
     */
    public void setCopyright(final String copyright) {
        mCopyright = copyright;
    }

    /**
     * 尺長取得.
     * @return 尺長
     */
    public int getDur() {
        return mDur;
    }

    /**
     * 尺長設定.
     * @param dur 尺長
     */
    public void setDur(final int dur) {
        mDur = dur;
    }

    /**
     * デモフラグ取得.
     * @return デモフラグ
     */
    public String getDemong() {
        return mDemong;
    }

    /**
     * デモフラグ 設定.
     * @param demong デモフラグ
     */
    public void setDemong(final String demong) {
        mDemong = demong;
    }

    /**
     * 見放題フラグ取得.
     * @return 見放題フラグ
     */
    public String getBvflg() {
        return mBvflg;
    }

    /**
     * 見放題フラグ設定.
     * @param bvflg 見放題フラグ
     */
    public void setBvflg(final String bvflg) {
        mBvflg = bvflg;
    }

    /**
     * 4Kフラグ取得.
     * @return 4Kフラグ
     */
    public int getM4kflg() {
        return m4kflg;
    }

    /**
     * 4Kフラグ設定.
     * @param m4kflg 4Kフラグ
     */
    public void setM4kflg(final int m4kflg) {
        this.m4kflg = m4kflg;
    }

    /**
     * HDRフラグ取得.
     * @return HDRフラグ
     */
    public String getHdrflg() {
        return mHdrflg;
    }

    /**
     *HDRフラグ 設定.
     * @param hdrflg HDRフラグ
     */
    public void setHdrflg(final String hdrflg) {
        mHdrflg = hdrflg;
    }

    /**
     * 配信ステータス取得.
     * @return 配信ステータス
     */
    public String getDelivery() {
        return mDelivery;
    }

    /**
     * 配信ステータス設定.
     * @param delivery 配信ステータス
     */
    public void setDelivery(final String delivery) {
        mDelivery = delivery;
    }

    /**
     * 配信ステータス取得.
     * @return 配信ステータス
     */
    public String getR_value() {
        return mR_value;
    }

    /**
     * 配信ステータス設定.
     * @param r_value 配信ステータス
     */
    public void setR_value(final String r_value) {
        mR_value = r_value;
    }

    /**
     * アダルトフラグ取得.
     * @return アダルトフラグ
     */
    public int getAdult() {
        return mAdult;
    }

    /**
     * アダルトフラグ設定.
     * @param adult vアダルトフラグ
     */
    public void setAdult(final int adult) {
        mAdult = adult;
    }

    /**
     *ジャンル取得.
     * @return ジャンル
     */
    public String[] getGenre_array() {
        return mGenre_array.clone();
    }

    /**
     * ジャンル設定.
     * @param genre_array ジャンル
     */
    public void setGenre_array(final String[] genre_array) {
        mGenre_array = genre_array.clone();
    }

    /**
     * あらすじ(long)(数値ではなく長いの意味の方)取得.
     * @return あらすじ(long)(数値ではなく長いの意味の方)
     */
    public String getSynop() {
        return mSynop;
    }

    /**
     * あらすじ(long)(数値ではなく長いの意味の方)設定.
     * @param synop あらすじ(long)(数値ではなく長いの意味の方)
     */
    public void setSynop(final String synop) {
        mSynop = synop;
    }

    /**
     * パーチャスID取得.
     * @return パーチャスID
     */
    public String getPuid() {
        return mPuid;
    }

    /**
     * パーチャスID設定.
     * @param puid パーチャスID
     */
    public void setPuid(final String puid) {
        mPuid = puid;
    }

    /**
     * 価格(税込)取得.
     * @return 価格(税込)
     */
    public String getPrice() {
        return mPrice;
    }

    /**
     *価格(税込) 設定.
     * @param price 価格(税込)
     */
    public void setPrice(final String price) {
        mPrice = price;
    }

    /**
     * 購入単位の期間(3日の3)取得.
     * @return 購入単位の期間(3日の3)
     */
    public String getQrange() {
        return mQrange;
    }

    /**
     * 購入単位の期間(3日の3)設定.
     * @param qrange 購入単位の期間(3日の3)
     */
    public void setQrange(final String qrange) {
        mQrange = qrange;
    }

    /**
     * 購入単位の単位(3日の「日」)取得.
     * @return 購入単位の単位(3日の「日」)
     */
    public String getQunit() {
        return mQunit;
    }

    /**
     * 購入単位の単位(3日の「日」)設定.
     * @param qunit 購入単位の単位(3日の「日」)
     */
    public void setQunit(final String qunit) {
        mQunit = qunit;
    }

    /**
     * 販売開始日時(EPOC秒なので数値)取得.
     * @return 販売開始日時(EPOC秒なので数値)
     */
    public long getmPu_start_date() {
        return mPu_start_date;
    }

    /**
     * 販売開始日時(EPOC秒なので数値)設定.
     * @param mPu_start_date 販売開始日時(EPOC秒なので数値)
     */
    public void setmPu_start_date(final long mPu_start_date) {
        this.mPu_start_date = mPu_start_date;
    }

    /**
     * 販売終了日時(EPOC秒なので数値)取得.
     * @return 販売終了日時(EPOC秒なので数値)
     */
    public long getmPu_end_date() {
        return mPu_end_date;
    }

    /**
     * 販売終了日時(EPOC秒なので数値)設定.
     * @param mPu_end_date 販売終了日時(EPOC秒なので数値)
     */
    public void setmPu_end_date(final long mPu_end_date) {
        this.mPu_end_date = mPu_end_date;
    }

    /**
     * 出演者情報（ロール|出演者名）取得.
     * @return 出演者情報（ロール|出演者名）
     */
    public String[] getmCredit_array() {
        //コピーを作成して返す
        return mCredit_array.clone();
    }

    /**
     * 出演者情報（ロール|出演者名）設定.
     * @param credit_array 出演者情報（ロール|出演者名）
     */
    public void setmCredit_array(final String[] credit_array) {
        //コピーして蓄積する
        this.mCredit_array = credit_array.clone();
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
     * @param rating レーティング値
     */
    public void setRating(final double rating) {
        mRating = rating;
    }

    /**
     * dTVフラグ取得.
     * @return dTVフラグ
     */
    public String getDtv() {
        return mDtv;
    }

    /**
     * dTVフラグ設定..
     * @param dtv dTVフラグ
     */
    public void setDtv(final String dtv) {
        mDtv = dtv;
    }

    /**
     * CHSVOD取得.
     * @return CHSVOD
     */
    public String getmChsvod() {
        return mChsvod;
    }

    /**
     * CHSVOD設定.
     * @param mChsvod CHSVOD
     */
    public void setmChsvod(final String mChsvod) {
        this.mChsvod = mChsvod;
    }

    /**
     * クリップ判定に利用(※一部コンテンツはこれだけでは判定不可)取得.
     * @return クリップ判定に利用(一部コンテンツはこれだけでは判定不可)
     */
    public String getmSearch_ok() {
        return mSearch_ok;
    }

    /**
     * クリップ判定に利用(※一部コンテンツはこれだけでは判定不可)設定.
     * @param mSearch_ok クリップ判定に利用(※一部コンテンツはこれだけでは判定不可)
     */
    public void setmSearch_ok(final String mSearch_ok) {
        this.mSearch_ok = mSearch_ok;
    }

    /**
     * ライセンス情報リスト取得.
     * @return ライセンス情報リスト
     */
    public String[] getmLiinf_array() {
        return mLiinf_array.clone();
    }

    /**
     * ライセンス情報リスト設定.
     * @param liinfArray ライセンス情報リスト
     */
    public void setmLiinf_array(final String[] liinfArray) {
        this.mLiinf_array = liinfArray.clone();
    }

    /**
     * 販売情報リスト(原文が全て大文字なので、こちらも大文字)取得.
     * @return 販売情報リスト(原文が全て大文字なので、こちらも大文字)
     */
    public List<Puinf> getmPUINF() {
        return mPUINF;
    }

    /**
     * 販売情報リスト(原文が全て大文字なので、こちらも大文字)設定.
     * @param mPUINF 販売情報リスト(原文が全て大文字なので、こちらも大文字)
     */
    public void setmPUINF(final List<Puinf> mPUINF) {
        this.mPUINF = mPUINF;
    }

    /**
     * 字幕取得.
     * @return 字幕
     */
    public String getmCapl() {
        return mCapl;
    }

    /**
     *字幕設定.
     * @param mCapl 字幕
     */
    public void setmCapl(final String mCapl) {
        this.mCapl = mCapl;
    }

    /**
     * 二ヶ国語取得.
     * @return 二ヶ国語
     */
    public String getmBilingal() {
        return mBilingal;
    }

    /**
     * 二ヶ国語設定.
     * @param mBilingal 二ヶ国語
     */
    public void setmBilingal(final String mBilingal) {
        this.mBilingal = mBilingal;
    }

    /**
     * コンテンツID（見逃し、関連VOD用）取得.
     * @return コンテンツID（見逃し、関連VOD用）
     */
    public String getmTv_cid() {
        return mTv_cid;
    }

    /**
     * コンテンツID（見逃し、関連VOD用）設定.
     * @param mTv_cid コンテンツID（見逃し、関連VOD用）
     */
    public void setmTv_cid(final String mTv_cid) {
        this.mTv_cid = mTv_cid;
    }

    /**
     *サービスID 取得.
     * @return サービスID
     */
    public String getmService_id() {
        return mService_id;
    }

    /**
     * サービスID設定.
     * @param mService_id サービスID
     */
    public void setmService_id(final String mService_id) {
        this.mService_id = mService_id;
    }

    /**
     * イベントID取得.
     * @return イベントID
     */
    public String getmEvent_id() {
        return mEvent_id;
    }

    /**
     *イベントID 設定.
     * @param mEvent_id イベントID
     */
    public void setmEvent_id(final String mEvent_id) {
        this.mEvent_id = mEvent_id;
    }

    /**
     * チャンネル番号取得.
     * @return チャンネル番号
     */
    public String getmChno() {
        return mChno;
    }

    /**
     * チャンネル番号設定.
     * @param mChno チャンネル番号
     */
    public void setmChno(final String mChno) {
        this.mChno = mChno;
    }

    /**
     * 放送種別（多ｃｈ、dch）取得.
     * @return 放送種別（多ｃｈ、dch）
     */
    public String getmTv_service() {
        return mTv_service;
    }

    /**
     * 放送種別（多ｃｈ、dch）設定.
     * @param mTv_service 放送種別（多ｃｈ、dch）
     */
    public void setmTv_service(final String mTv_service) {
        this.mTv_service = mTv_service;
    }

    /**
     * 見逃しタイプ（見逃し：切り出し、見逃し：完パケ、関連VOD）取得.
     * @return 見逃しタイプ（見逃し：切り出し、見逃し：完パケ、関連VOD）
     */
    public String getmContent_type() {
        return mContent_type;
    }

    /**
     *見逃しタイプ（見逃し：切り出し、見逃し：完パケ、関連VOD） 設定.
     * @param mContent_type 見逃しタイプ（見逃し：切り出し、見逃し：完パケ、関連VOD）
     */
    public void setmContent_type(final String mContent_type) {
        this.mContent_type = mContent_type;
    }

    /**
     * VOD配信開始日時取得.
     * @return VOD配信開始日時
     */
    public long getmVod_start_date() {
        return mVod_start_date;
    }

    /**
     * VOD配信開始日時設定.
     * @param mVod_start_date VOD配信開始日時
     */
    public void setmVod_start_date(final long mVod_start_date) {
        this.mVod_start_date = mVod_start_date;
    }

    /**
     * VOD配信終了日時取得.
     * @return VOD配信終了日時
     */
    public long getmVod_end_date() {
        return mVod_end_date;
    }

    /**
     * VOD配信終了日時設定.
     * @param mVod_end_date VOD配信終了日時
     */
    public void setmVod_end_date(final long mVod_end_date) {
        this.mVod_end_date = mVod_end_date;
    }

    /**
     * 主ジャンル（ARIB）取得.
     * @return 主ジャンル（ARIB）
     */
    public String getmMain_genre() {
        return mMain_genre;
    }

    /**
     * 主ジャンル（ARIB）設定.
     * @param mMain_genre 主ジャンル（ARIB）
     */
    public void setmMain_genre(final String mMain_genre) {
        this.mMain_genre = mMain_genre;
    }

    /**
     * 副ジャンル（ARIB）取得.
     * @return 副ジャンル（ARIB）
     */
    public String[] getmSecond_genre_array() {
        return mSecond_genre_array.clone();
    }

    /**
     * 副ジャンル（ARIB）設定.
     * @param mSecond_genre_array 副ジャンル（ARIB）
     */
    public void setmSecond_genre_array(final String[] mSecond_genre_array) {
        this.mSecond_genre_array = mSecond_genre_array.clone();
    }

    /**
     * コピー制御取得.
     * @return コピー制御
     */
    public String getmCopy() {
        return mCopy;
    }

    /**
     * コピー制御 設定.
     * @param mCopy コピー制御
     */
    public void setmCopy(final String mCopy) {
        this.mCopy = mCopy;
    }

    /**
     *音声情報 取得.
     * @return 音声情報
     */
    public String[] getmAdinfo_array() {
        return mAdinfo_array.clone();
    }

    /**
     * 音声情報設定.
     * @param adinfo_array 音声情報
     */
    public void setmAdinfo_array(final String[] adinfo_array) {
        this.mAdinfo_array = adinfo_array.clone();
    }

    /**
     *関連VODのcrid 取得.
     * @return 関連VODのcrid
     */
    public String[] getmRelational_id_array() {
        return mRelational_id_array.clone();
    }

    /**
     *関連VODのcrid 設定.
     * @param mRelational_id_array 関連VODのcrid
     */
    public void setmRelational_id_array(final String[] mRelational_id_array) {
        this.mRelational_id_array = mRelational_id_array.clone();
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
    private void setClipExec(final boolean mClipExec) {
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

    /**キー名：単一データ(順番が分かりやすいように配列とリストの情報も残してある).*/
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
            JsonConstants.META_RESPONSE_LIINF_ARRAY,//JsonConstants.META_RESPONSE_PUINF,
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

    /**キー名：配列データ.*/
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
                        setClipExec(ClipUtils.isCanClip(getDisp_type(), getmSearch_ok(), getDtv(), getDtvType()));
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
                    mRating = DataBaseUtils.getDecimal(data);
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
     * @return キー
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
     * ESTフラグ取得.
     * @return ESTフラグ
     */
    public String getEstFlag() {
        return mEstFlag;
    }

    /**
     * ESTフラグ設定.
     * @param mEstFlag ESTフラグ
     */
    public void setEstFlag(final String mEstFlag) {
        this.mEstFlag = mEstFlag;
    }

    /**
     * コンテンツタイプ設定.
     * @return コンテンツタイプ
     */
    public ContentUtils.ContentsType getContentsType() {
        return mContentsType;
    }

    /**
     * コンテンツタイプ設定.
     * @param mContentsType コンテンツタイプ
     */
    public void setContentsType(final ContentUtils.ContentsType mContentsType) {
        this.mContentsType = mContentsType;
    }
}