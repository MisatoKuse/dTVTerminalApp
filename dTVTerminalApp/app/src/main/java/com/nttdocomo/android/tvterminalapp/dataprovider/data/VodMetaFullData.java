/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * VODメタレスポンス（フル版）
 */

public class VodMetaFullData implements Serializable {

    private static final long serialVersionUID = 2793910456218850277L;

    public static String[] getRootPara() {
        return mRootPara;
    }

    public String getCrid() {
        return mCrid;
    }

    public void setCrid(String crid) {
        mCrid = crid;
    }

    public String getCid() {
        return mCid;
    }

    public void setCid(String cid) {
        mCid = cid;
    }

    public String getTitle_id() {
        return mTitle_id;
    }

    public void setTitle_id(String title_id) {
        mTitle_id = title_id;
    }

    public String getEpisode_id() {
        return mEpisode_id;
    }

    public void setEpisode_id(String episode_id) {
        mEpisode_id = episode_id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getEpititle() {
        return mEpititle;
    }

    public void setEpititle(String epititle) {
        mEpititle = epititle;
    }

    public String getTitleruby() {
        return mTitleruby;
    }

    public void setTitleruby(String titleruby) {
        mTitleruby = titleruby;
    }

    public String getDisp_type() {
        return mDisp_type;
    }

    public void setDisp_type(String disp_type) {
        mDisp_type = disp_type;
    }

    public String getDisplay_start_date() {
        return mDisplay_start_date;
    }

    public void setDisplay_start_date(String display_start_date) {
        mDisplay_start_date = display_start_date;
    }

    public String getDisplay_end_date() {
        return mDisplay_end_date;
    }

    public void setDisplay_end_date(String display_end_date) {
        mDisplay_end_date = display_end_date;
    }

    public String getAvail_start_date() {
        return mAvail_start_date;
    }

    public void setAvail_start_date(String avail_start_date) {
        mAvail_start_date = avail_start_date;
    }

    public String getAvail_end_date() {
        return mAvail_end_date;
    }

    public void setAvail_end_date(String avail_end_date) {
        mAvail_end_date = avail_end_date;
    }

    public String getPublish_start_date() {
        return mPublish_start_date;
    }

    public void setPublish_start_date(String publish_start_date) {
        mPublish_start_date = publish_start_date;
    }

    public String getPublish_end_date() {
        return mPublish_end_date;
    }

    public void setPublish_end_date(String publish_end_date) {
        mPublish_end_date = publish_end_date;
    }

    public String getNewa_start_date() {
        return mNewa_start_date;
    }

    public void setNewa_start_date(String newa_start_date) {
        mNewa_start_date = newa_start_date;
    }

    public String getNewa_end_date() {
        return mNewa_end_date;
    }

    public void setNewa_end_date(String newa_end_date) {
        mNewa_end_date = newa_end_date;
    }

    public String getCopyright() {
        return mCopyright;
    }

    public void setCopyright(String copyright) {
        mCopyright = copyright;
    }

    public String getThumb() {
        return mThumb;
    }

    public void setThumb(String thumb) {
        mThumb = thumb;
    }

    public String getDur() {
        return mDur;
    }

    public void setDur(String dur) {
        mDur = dur;
    }

    public String getDemong() {
        return mDemong;
    }

    public void setDemong(String demong) {
        mDemong = demong;
    }

    public String getBvflg() {
        return mBvflg;
    }

    public void setBvflg(String bvflg) {
        mBvflg = bvflg;
    }

    public String getM4kflg() {
        return m4kflg;
    }

    public void setM4kflg(String m4kflg) {
        this.m4kflg = m4kflg;
    }

    public String getHdrflg() {
        return mHdrflg;
    }

    public void setHdrflg(String hdrflg) {
        mHdrflg = hdrflg;
    }

    public String getAvail_status() {
        return mAvail_status;
    }

    public void setAvail_status(String avail_status) {
        mAvail_status = avail_status;
    }

    public String getDelivery() {
        return mDelivery;
    }

    public void setDelivery(String delivery) {
        mDelivery = delivery;
    }

    public String getR_value() {
        return mR_value;
    }

    public void setR_value(String r_value) {
        mR_value = r_value;
    }

    public String getAdult() {
        return mAdult;
    }

    public void setAdult(String adult) {
        mAdult = adult;
    }

    public String getMs() {
        return mMs;
    }

    public void setMs(String ms) {
        mMs = ms;
    }

    public String getNg_func() {
        return mNg_func;
    }

    public void setNg_func(String ng_func) {
        mNg_func = ng_func;
    }

    public String[] getGenre_id_array() {
        return mGenre_id_array;
    }

    public void setGenre_id_array(String[] genre_id_array) {
        mGenre_id_array = genre_id_array;
    }

    public String getSynop() {
        return mSynop;
    }

    public void setSynop(String synop) {
        mSynop = synop;
    }

    public String getPuid() {
        return mPuid;
    }

    public void setPuid(String puid) {
        mPuid = puid;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getQrange() {
        return mQrange;
    }

    public void setQrange(String qrange) {
        mQrange = qrange;
    }

    public String getQunit() {
        return mQunit;
    }

    public void setQunit(String qunit) {
        mQunit = qunit;
    }

    public String getPu_s() {
        return mPu_s;
    }

    public void setPu_s(String pu_s) {
        mPu_s = pu_s;
    }

    public String getPu_e() {
        return mPu_e;
    }

    public void setPu_e(String pu_e) {
        mPu_e = pu_e;
    }

    public String getCredits() {
        return mCredits;
    }

    public void setCredits(String credits) {
        mCredits = credits;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        mRating = rating;
    }

    public String getDtv() {
        return mDtv;
    }

    public void setDtv(String dtv) {
        mDtv = dtv;
    }

    private String mCrid;                 // crid
    private String mCid;                  // コンテンツID
    private String mTitle_id;             // タイトルID（dTV）
    private String mEpisode_id;           // エピソードID（dTV）
    private String mTitle;                // タイトル
    private String mEpititle;             // エピソードタイトル
    private String mTitleruby;            // タイトルルビ
    private String mDisp_type;            // 表示タイプ
    private String mDisplay_start_date;   // 表示開始日時
    private String mDisplay_end_date;     // 表示終了日時
    private String mAvail_start_date;     // コンテンツ自体の有効開始日時(PITのみ)
    private String mAvail_end_date;       // コンテンツ自体の有効期限日時(PITのみ)
    private String mPublish_start_date;   // 有効開始日時
    private String mPublish_end_date;     // 有効期限日時
    private String mNewa_start_date;      // 新着期間開始
    private String mNewa_end_date;        // 新着期間終了
    private String mCopyright;            // コピーライト
    private String mThumb;                // サムネイル
    private String mDur;                  // 尺長
    private String mDemong;               // デモフラグ
    private String mBvflg;                // 見放題フラグ
    private String m4kflg;                // ４Kフラグ
    private String mHdrflg;               // HDRフラグ
    private String mAvail_status;         // 配信ステータス
    private String mDelivery;             // deliveryStatus
    private String mR_value;              // パレンタル情報
    private String mAdult;                // アダルトフラグ
    private String mMs;                   // MS_OK/NGフラグ
    private String mNg_func;              // NGファンク
    private String[] mGenre_id_array;       // ジャンル
    private String mSynop;                // あらすじ
    private String mPuid;                 // パーチャスID
    private String mPrice;                // 価格(税込)
    private String mQrange;               // 購入単位の期間(3日の3)
    private String mQunit;                // 購入単位の単位(3日の「日」)
    private String mPu_s;                 // 販売開始日時
    private String mPu_e;                 // 販売終了日時
    private String mCredits;              // 出演者情報（ロール|出演者名）
    private String mRating;               // レーティング値
    private String mDtv;                  // dTVフラグ

    public static final String VOD_META_FULL_DATA_CRID = "crid";
    public static final String VOD_META_FULL_DATA_CID = "cid";
    public static final String VOD_META_FULL_DATA_TITLE_ID = "title_id";
    public static final String VOD_META_FULL_DATA_EPISODE_ID = "episode_id";
    public static final String VOD_META_FULL_DATA_TITLE = "title";
    public static final String VOD_META_FULL_DATA_EPITITLE = "epititle";
    public static final String VOD_META_FULL_DATA_TITLERUBY = "titleruby";
    public static final String VOD_META_FULL_DATA_DISP_TYPE = "disp_type";
    public static final String VOD_META_FULL_DATA_DISPLAY_START_DATE = "display_start_date";
    public static final String VOD_META_FULL_DATA_DISPLAY_END_DATE = "display_end_date";
    public static final String VOD_META_FULL_DATA_AVAIL_START_DATE = "avail_start_date";
    public static final String VOD_META_FULL_DATA_AVAIL_END_DATE = "avail_end_date";
    public static final String VOD_META_FULL_DATA_PUBLISH_START_DATE = "publish_start_date";
    public static final String VOD_META_FULL_DATA_PUBLISH_END_DATE = "publish_end_date";
    public static final String VOD_META_FULL_DATA_NEWA_START_DATE = "newa_start_date";
    public static final String VOD_META_FULL_DATA_NEWA_END_DATE = "newa_end_date";
    public static final String VOD_META_FULL_DATA_COPYRIGHT = "copyright";
    public static final String VOD_META_FULL_DATA_THUMB = "thumb";
    public static final String VOD_META_FULL_DATA_DUR = "dur";
    public static final String VOD_META_FULL_DATA_DEMONG = "demong";
    public static final String VOD_META_FULL_DATA_BVFLG = "bvflg";
    public static final String VOD_META_FULL_DATA_4KFLG = "4kflg";
    public static final String VOD_META_FULL_DATA_HDRFLG = "hdrflg";
    public static final String VOD_META_FULL_DATA_AVAIL_STATUS = "avail_status";
    public static final String VOD_META_FULL_DATA_DELIVERY = "delivery";
    public static final String VOD_META_FULL_DATA_R_VALUE = "r_value";
    public static final String VOD_META_FULL_DATA_ADULT = "adult";
    public static final String VOD_META_FULL_DATA_MS = "ms";
    public static final String VOD_META_FULL_DATA_NG_FUNC = "ng_func";
    public static final String VOD_META_FULL_DATA_GENRE_ID_ARRAY = "genre_id_array";
    public static final String VOD_META_FULL_DATA_SYNOP = "synop";
    public static final String VOD_META_FULL_DATA_PUID = "puid";
    public static final String VOD_META_FULL_DATA_PRICE = "price";
    public static final String VOD_META_FULL_DATA_QRANGE = "qrange";
    public static final String VOD_META_FULL_DATA_QUNIT = "qunit";
    public static final String VOD_META_FULL_DATA_PU_S = "pu_s";
    public static final String VOD_META_FULL_DATA_PU_E = "pu_e";
    public static final String VOD_META_FULL_DATA_CREDITS = "credits";
    public static final String VOD_META_FULL_DATA_RATING = "rating";
    public static final String VOD_META_FULL_DATA_DTV = "dtv";
    // ライセンス/販売情報リスト：キー名
    public static final String VOD_META_FULL_DATA_PLIT = "PLIT";
    public static final String VOD_META_FULL_DATA_PLIT_PLI_VIS = "pli_vis";
    public static final String VOD_META_FULL_DATA_PLIT_PLI_VIE = "pli_vie";
    // ライセンス詳細リスト：キー名
    public static final String VOD_META_FULL_DATA_PLICENSE = "plicense";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_PUID = "pli_puid";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_CRID = "pli_crid";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_TITLE = "pli_title";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_EPITITLE = "pli_epititle";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_DISP_TYPE = "pli_disp_type";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_PRICE = "pli_price";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_QUNIT = "pli_qunit";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_QRANGE = "pli_qrange";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_PU_S = "pli_pu_s";
    public static final String VOD_META_FULL_DATA_PLICENSE_PLI_PU_E = "pli_pu_e";

    // キー名：単一データ
    public static final String[] mRootPara = {VOD_META_FULL_DATA_CRID, VOD_META_FULL_DATA_CID, VOD_META_FULL_DATA_TITLE_ID,
            VOD_META_FULL_DATA_EPISODE_ID, VOD_META_FULL_DATA_TITLE, VOD_META_FULL_DATA_EPITITLE, VOD_META_FULL_DATA_TITLERUBY,
            VOD_META_FULL_DATA_DISP_TYPE, VOD_META_FULL_DATA_DISPLAY_START_DATE, VOD_META_FULL_DATA_DISPLAY_END_DATE,
            VOD_META_FULL_DATA_AVAIL_START_DATE, VOD_META_FULL_DATA_AVAIL_END_DATE, VOD_META_FULL_DATA_PUBLISH_START_DATE,
            VOD_META_FULL_DATA_PUBLISH_END_DATE, VOD_META_FULL_DATA_NEWA_START_DATE, VOD_META_FULL_DATA_NEWA_END_DATE,
            VOD_META_FULL_DATA_COPYRIGHT, VOD_META_FULL_DATA_THUMB, VOD_META_FULL_DATA_DUR, VOD_META_FULL_DATA_DEMONG,
            VOD_META_FULL_DATA_BVFLG, VOD_META_FULL_DATA_4KFLG, VOD_META_FULL_DATA_HDRFLG, VOD_META_FULL_DATA_AVAIL_STATUS,
            VOD_META_FULL_DATA_DELIVERY, VOD_META_FULL_DATA_R_VALUE, VOD_META_FULL_DATA_ADULT, VOD_META_FULL_DATA_MS,
            VOD_META_FULL_DATA_NG_FUNC, VOD_META_FULL_DATA_SYNOP, VOD_META_FULL_DATA_PUID,
            VOD_META_FULL_DATA_PRICE, VOD_META_FULL_DATA_QRANGE, VOD_META_FULL_DATA_QUNIT, VOD_META_FULL_DATA_PU_S,
            VOD_META_FULL_DATA_PU_E, VOD_META_FULL_DATA_CREDITS, VOD_META_FULL_DATA_RATING, VOD_META_FULL_DATA_DTV};
    // キー名：配列データ
    public static final String[] mRootArrayPara = {VOD_META_FULL_DATA_GENRE_ID_ARRAY};
    // ライセンス/販売情報リスト：キー名
    public static final String[] mPlitPara = {VOD_META_FULL_DATA_PLIT_PLI_VIS, VOD_META_FULL_DATA_PLIT_PLI_VIE};
    // ライセンス詳細リスト：キー名
    public static final String[] mPlicensePara = {VOD_META_FULL_DATA_PLICENSE_PLI_PUID, VOD_META_FULL_DATA_PLICENSE_PLI_CRID,
            VOD_META_FULL_DATA_PLICENSE_PLI_TITLE, VOD_META_FULL_DATA_PLICENSE_PLI_EPITITLE,
            VOD_META_FULL_DATA_PLICENSE_PLI_DISP_TYPE, VOD_META_FULL_DATA_PLICENSE_PLI_PRICE,
            VOD_META_FULL_DATA_PLICENSE_PLI_QUNIT, VOD_META_FULL_DATA_PLICENSE_PLI_QRANGE,
            VOD_META_FULL_DATA_PLICENSE_PLI_PU_S, VOD_META_FULL_DATA_PLICENSE_PLI_PU_E};


    // ライセンス/販売情報リスト
    private ArrayList<Plit> mPlits;

    /**
     * VODメタレスポンス（フル版）
     */
    public VodMetaFullData() {
        mGenre_id_array = new String[0];
        mPlits = new ArrayList<Plit>();
    }

    /**
     * VODメタレスポンス（フル版）の json データをデータオブジェクトに変換
     *
     * @param jsonObj 　json データ
     */
    public void setData(JSONObject jsonObj) {
        // ライセンス/販売情報リスト
        Plit plit;
        try {
            // 単一データ
            for (String item : mRootPara) {
                setMember(item, jsonObj.get(item));
            }
            // 配列データ
            for (String item : mRootArrayPara) {
                setMember(item, jsonObj.getJSONArray(item));
            }
            JSONArray plits = jsonObj.getJSONArray(VOD_META_FULL_DATA_PLIT);
            if (plits.length() == 0) {
                return;
            }
            for (int i = 0; i < plits.length(); i++) {
                plit = new Plit();
                plit.setPlitData(plits.getJSONObject(i));
                mPlits.add(plit);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * ライセンス/販売情報リスト：オブジェクト配列を返す
     *
     * @return ライセンス/販売情報リスト：オブジェクト配列
     */
    public Plit[] getPlits() {
        return mPlits.toArray(new Plit[mPlits.size()]);
    }

    /**
     * キーとキーの値をメンバーにセットする
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(String key, Object data) {

        if (key.isEmpty()) {
            return;
        } else switch (key) {
            case VOD_META_FULL_DATA_CRID:
                mCrid = (String) data;                // crid
                break;
            case VOD_META_FULL_DATA_CID:
                mCid = (String) data;                 // コンテンツID
                break;
            case VOD_META_FULL_DATA_TITLE_ID:
                mTitle_id = (String) data;            // タイトルID（dTV）
                break;
            case VOD_META_FULL_DATA_EPISODE_ID:
                mEpisode_id = (String) data;          // エピソードID（dTV）
                break;
            case VOD_META_FULL_DATA_TITLE:
                mTitle = (String) data;               // タイトル
                break;
            case VOD_META_FULL_DATA_EPITITLE:
                mEpititle = (String) data;            // エピソードタイトル
                break;
            case VOD_META_FULL_DATA_TITLERUBY:
                mTitleruby = (String) data;           // タイトルルビ
                break;
            case VOD_META_FULL_DATA_DISP_TYPE:
                mDisp_type = (String) data;           // 表示タイプ
                break;
            case VOD_META_FULL_DATA_DISPLAY_START_DATE:
                mDisplay_start_date = (String) data;  // 表示開始日時
                break;
            case VOD_META_FULL_DATA_DISPLAY_END_DATE:
                mDisplay_end_date = (String) data;    // 表示終了日時
                break;
            case VOD_META_FULL_DATA_AVAIL_START_DATE:
                mAvail_start_date = (String) data;    // コンテンツ自体の有効開始日時(PITのみ)
                break;
            case VOD_META_FULL_DATA_AVAIL_END_DATE:
                mAvail_end_date = (String) data;      // コンテンツ自体の有効期限日時(PITのみ)
                break;
            case VOD_META_FULL_DATA_PUBLISH_START_DATE:
                mPublish_start_date = (String) data;  // 有効開始日時
                break;
            case VOD_META_FULL_DATA_PUBLISH_END_DATE:
                mPublish_end_date = (String) data;    // 有効期限日時
                break;
            case VOD_META_FULL_DATA_NEWA_START_DATE:
                mNewa_start_date = (String) data;     // 新着期間開始
                break;
            case VOD_META_FULL_DATA_NEWA_END_DATE:
                mNewa_end_date = (String) data;       // 新着期間終了
                break;
            case VOD_META_FULL_DATA_COPYRIGHT:
                mCopyright = (String) data;           // コピーライト
                break;
            case VOD_META_FULL_DATA_THUMB:
                mThumb = (String) data;               // サムネイル
                break;
            case VOD_META_FULL_DATA_DUR:
                mDur = (String) data;                 // 尺長
                break;
            case VOD_META_FULL_DATA_DEMONG:
                mDemong = (String) data;              // デモフラグ
                break;
            case VOD_META_FULL_DATA_BVFLG:
                mBvflg = (String) data;               // 見放題フラグ
                break;
            case VOD_META_FULL_DATA_4KFLG:
                m4kflg = (String) data;               // ４Kフラグ
                break;
            case VOD_META_FULL_DATA_HDRFLG:
                mHdrflg = (String) data;              // HDRフラグ
                break;
            case VOD_META_FULL_DATA_AVAIL_STATUS:
                mAvail_status = (String) data;        // 配信ステータス
                break;
            case VOD_META_FULL_DATA_DELIVERY:
                mDelivery = (String) data;            // deliveryStatus
                break;
            case VOD_META_FULL_DATA_R_VALUE:
                mR_value = (String) data;             // パレンタル情報
                break;
            case VOD_META_FULL_DATA_ADULT:
                mAdult = (String) data;               // アダルトフラグ
                break;
            case VOD_META_FULL_DATA_MS:
                mMs = (String) data;                  // MS_OK/NGフラグ
                break;
            case VOD_META_FULL_DATA_NG_FUNC:
                mNg_func = (String) data;             // NGファンク
                break;
            case VOD_META_FULL_DATA_GENRE_ID_ARRAY:
                mGenre_id_array = toStringArray((JSONArray) data);
                break;
            case VOD_META_FULL_DATA_SYNOP:
                mSynop = (String) data;               // あらすじ
                break;
            case VOD_META_FULL_DATA_PUID:
                mPuid = (String) data;                // パーチャスID
                break;
            case VOD_META_FULL_DATA_PRICE:
                mPrice = (String) data;               // 価格(税込)
                break;
            case VOD_META_FULL_DATA_QRANGE:
                mQrange = (String) data;              // 購入単位の期間(3日の3)
                break;
            case VOD_META_FULL_DATA_QUNIT:
                mQunit = (String) data;               // 購入単位の単位(3日の「日」)
                break;
            case VOD_META_FULL_DATA_PU_S:
                mPu_s = (String) data;                // 販売開始日時
                break;
            case VOD_META_FULL_DATA_PU_E:
                mPu_e = (String) data;                // 販売終了日時
                break;
            case VOD_META_FULL_DATA_CREDITS:
                mCredits = (String) data;             // 出演者情報（ロール|出演者名）
                break;
            case VOD_META_FULL_DATA_RATING:
                mRating = (String) data;              // レーティング値
                break;
            case VOD_META_FULL_DATA_DTV:
                mDtv = (String) data;                 // dTVフラグ
                break;
            default:
        }
    }

    /**
     * キーの値を取得する
     *
     * @param key  キー
     */
    public Object getMember(String key) {

        if (key.isEmpty()) {
            return "";
        } else switch (key) {
            case VOD_META_FULL_DATA_CRID:
                return mCrid;                // crid
            case VOD_META_FULL_DATA_CID:
                return mCid;                 // コンテンツID
            case VOD_META_FULL_DATA_TITLE_ID:
                return mTitle_id;            // タイトルID（dTV）
            case VOD_META_FULL_DATA_EPISODE_ID:
                return mEpisode_id;          // エピソードID（dTV）
            case VOD_META_FULL_DATA_TITLE:
                return mTitle;               // タイトル
            case VOD_META_FULL_DATA_EPITITLE:
                return mEpititle;            // エピソードタイトル
            case VOD_META_FULL_DATA_TITLERUBY:
                return mTitleruby;           // タイトルルビ
            case VOD_META_FULL_DATA_DISP_TYPE:
                return mDisp_type;           // 表示タイプ
            case VOD_META_FULL_DATA_DISPLAY_START_DATE:
                return mDisplay_start_date;  // 表示開始日時
            case VOD_META_FULL_DATA_DISPLAY_END_DATE:
                return mDisplay_end_date;    // 表示終了日時
            case VOD_META_FULL_DATA_AVAIL_START_DATE:
                return mAvail_start_date;    // コンテンツ自体の有効開始日時(PITのみ)
            case VOD_META_FULL_DATA_AVAIL_END_DATE:
                return mAvail_end_date;      // コンテンツ自体の有効期限日時(PITのみ)
            case VOD_META_FULL_DATA_PUBLISH_START_DATE:
                return mPublish_start_date;  // 有効開始日時
            case VOD_META_FULL_DATA_PUBLISH_END_DATE:
                return mPublish_end_date;    // 有効期限日時
            case VOD_META_FULL_DATA_NEWA_START_DATE:
                return mNewa_start_date;     // 新着期間開始
            case VOD_META_FULL_DATA_NEWA_END_DATE:
                return mNewa_end_date;       // 新着期間終了
            case VOD_META_FULL_DATA_COPYRIGHT:
                return mCopyright;           // コピーライト
            case VOD_META_FULL_DATA_THUMB:
                return mThumb;               // サムネイル
            case VOD_META_FULL_DATA_DUR:
                return mDur;                 // 尺長
            case VOD_META_FULL_DATA_DEMONG:
                return mDemong;              // デモフラグ
            case VOD_META_FULL_DATA_BVFLG:
                return mBvflg;               // 見放題フラグ
            case VOD_META_FULL_DATA_4KFLG:
                return m4kflg;               // ４Kフラグ
            case VOD_META_FULL_DATA_HDRFLG:
                return mHdrflg;              // HDRフラグ
            case VOD_META_FULL_DATA_AVAIL_STATUS:
                return mAvail_status;        // 配信ステータス
            case VOD_META_FULL_DATA_DELIVERY:
                return mDelivery;            // deliveryStatus
            case VOD_META_FULL_DATA_R_VALUE:
                return mR_value;             // パレンタル情報
            case VOD_META_FULL_DATA_ADULT:
                return mAdult;               // アダルトフラグ
            case VOD_META_FULL_DATA_MS:
                return mMs;                  // MS_OK/NGフラグ
            case VOD_META_FULL_DATA_NG_FUNC:
                return mNg_func;             // NGファンク
            case VOD_META_FULL_DATA_GENRE_ID_ARRAY:
                return mGenre_id_array.toString();
            case VOD_META_FULL_DATA_SYNOP:
                return mSynop;               // あらすじ
            case VOD_META_FULL_DATA_PUID:
                return mPuid;                // パーチャスID
            case VOD_META_FULL_DATA_PRICE:
                return mPrice;               // 価格(税込)
            case VOD_META_FULL_DATA_QRANGE:
                return mQrange;              // 購入単位の期間(3日の3)
            case VOD_META_FULL_DATA_QUNIT:
                return mQunit;               // 購入単位の単位(3日の「日」)
            case VOD_META_FULL_DATA_PU_S:
                return mPu_s;                // 販売開始日時
            case VOD_META_FULL_DATA_PU_E:
                return mPu_e;                // 販売終了日時
            case VOD_META_FULL_DATA_CREDITS:
                return mCredits;             // 出演者情報（ロール|出演者名）
            case VOD_META_FULL_DATA_RATING:
                return mRating;              // レーティング値
            case VOD_META_FULL_DATA_DTV:
                return mDtv;                 // dTVフラグ
            default:
                return "";
        }
    }

    // ライセンス/販売情報リスト
    public class Plit {
        public String getPli_vis() {
            return mPli_vis;
        }

        public void setPli_vis(String pli_vis) {
            mPli_vis = pli_vis;
        }

        public String getPli_vie() {
            return mPli_vie;
        }

        public void setPli_vie(String pli_vie) {
            mPli_vie = pli_vie;
        }

        private String mPli_vis;    // ライセンスID有効期間（開始）
        private String mPli_vie;    // ライセンスID有効期間（終了）


        private ArrayList<Plicense> mPlicenses;

        private Plit() {
            mPlicenses = new ArrayList<Plicense>();
        }

        /**
         * PLITの値をメンバーにセットする
         *
         * @param plit PLITの値(JSONObject)
         */
        void setPlitData(JSONObject plit) {
            // ライセンス詳細リスト
            Plicense plicense;

            try {
                for (String item : mPlitPara) {
                    setPlitMember(item, plit.get(item));
                }
                JSONArray plicenses = plit.getJSONArray(VOD_META_FULL_DATA_PLICENSE);
                if (plicenses.length() == 0) {
                    return;
                }
                for (int i = 0; i < plicenses.length(); i++) {
                    plicense = new Plicense();
                    plicense.setPlicenseData(plicenses.getJSONObject(i));
                    mPlicenses.add(plicense);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * キーとキーの値をメンバーにセットする
         *
         * @param key  キー
         * @param data キーの値
         */
        private void setPlitMember(String key, Object data) {
            if (key.isEmpty()) {
                return;
            } else switch (key) {
                case VOD_META_FULL_DATA_PLIT_PLI_VIS:
                    mPli_vis = (String) data;
                    break;
                case VOD_META_FULL_DATA_PLIT_PLI_VIE:
                    mPli_vie = (String) data;
                    break;
                default:
            }
        }

        /**
         * ライセンス詳細リスト：オブジェクト配列を返す
         *
         * @return ライセンス詳細リスト：オブジェクト配列
         */
        public Plicense[] getPlicenses() {
            return mPlicenses.toArray(new Plicense[mPlicenses.size()]);
        }

        /**
         * ライセンス詳細リスト
         */
        public class Plicense {
            private String mPli_puid;       // ライセンスID
            private String mPli_crid;       // ライセンスIDのCRID
            private String mPli_title;      // ライセンスIDのタイトル
            private String mPli_epititle;   // ライセンスIDのエピソードタイトル
            private String mPli_disp_type;  // 表示タイプ
            private String mPli_price;      // ライセンス価格
            private String mPli_qunit;      // ライセンス購入単位の期間(3日の3)
            private String mPli_qrange;     // ライセンス購入単位の単位(3日の「日」)
            private String mPli_pu_s;       // ライセンス販売可能期間（開始）
            private String mPli_pu_e;       // ライセンス販売可能期間（終了）

            public String getPli_puid() {
                return mPli_puid;
            }

            public void setPli_puid(String pli_puid) {
                mPli_puid = pli_puid;
            }

            public String getPli_crid() {
                return mPli_crid;
            }

            public void setPli_crid(String pli_crid) {
                mPli_crid = pli_crid;
            }

            public String getPli_title() {
                return mPli_title;
            }

            public void setPli_title(String pli_title) {
                mPli_title = pli_title;
            }

            public String getPli_epititle() {
                return mPli_epititle;
            }

            public void setPli_epititle(String pli_epititle) {
                mPli_epititle = pli_epititle;
            }

            public String getPli_disp_type() {
                return mPli_disp_type;
            }

            public void setPli_disp_type(String pli_disp_type) {
                mPli_disp_type = pli_disp_type;
            }

            public String getPli_price() {
                return mPli_price;
            }

            public void setPli_price(String pli_price) {
                mPli_price = pli_price;
            }

            public String getPli_qunit() {
                return mPli_qunit;
            }

            public void setPli_qunit(String pli_qunit) {
                mPli_qunit = pli_qunit;
            }

            public String getPli_qrange() {
                return mPli_qrange;
            }

            public void setPli_qrange(String pli_qrange) {
                mPli_qrange = pli_qrange;
            }

            public String getPli_pu_s() {
                return mPli_pu_s;
            }

            public void setPli_pu_s(String pli_pu_s) {
                mPli_pu_s = pli_pu_s;
            }

            public String getPli_pu_e() {
                return mPli_pu_e;
            }

            public void setPli_pu_e(String pli_pu_e) {
                mPli_pu_e = pli_pu_e;
            }

            /**
             * Plicense の値をメンバーにセットする
             *
             * @param plicense Plicense の値(JSONObject)
             */
            void setPlicenseData(JSONObject plicense) {
                try {
                    for (String item : mPlicensePara) {
                        setPlicenseMember(item, plicense.get(item));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /**
             * キーとキーの値をメンバーにセットする
             *
             * @param key  キー
             * @param data キーの値
             */
            private void setPlicenseMember(String key, Object data) {
                if (key.isEmpty()) {
                    return;
                } else switch (key) {
                    case VOD_META_FULL_DATA_PLICENSE_PLI_PUID:
                        mPli_puid = (String) data;
                        break;
                    case VOD_META_FULL_DATA_PLICENSE_PLI_CRID:
                        mPli_crid = (String) data;
                        break;
                    case VOD_META_FULL_DATA_PLICENSE_PLI_TITLE:
                        mPli_title = (String) data;
                        break;
                    case VOD_META_FULL_DATA_PLICENSE_PLI_EPITITLE:
                        mPli_epititle = (String) data;
                        break;
                    case VOD_META_FULL_DATA_PLICENSE_PLI_DISP_TYPE:
                        mPli_disp_type = (String) data;
                        break;
                    case VOD_META_FULL_DATA_PLICENSE_PLI_PRICE:
                        mPli_price = (String) data;
                        break;
                    case VOD_META_FULL_DATA_PLICENSE_PLI_QUNIT:
                        mPli_qunit = (String) data;
                        break;
                    case VOD_META_FULL_DATA_PLICENSE_PLI_QRANGE:
                        mPli_qrange = (String) data;
                        break;
                    case VOD_META_FULL_DATA_PLICENSE_PLI_PU_S:
                        mPli_pu_s = (String) data;
                        break;
                    case VOD_META_FULL_DATA_PLICENSE_PLI_PU_E:
                        mPli_pu_e = (String) data;
                        break;
                    default:
                }
            }

        }

    }

    /**
     * JSONArray to String[]
     *
     * @param array
     * @return
     */
    public static String[] toStringArray(JSONArray array) {

        String[] arr = null;
        if (array == null) {
            return null;
        }
        try {
            arr = new String[array.length()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = array.optString(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return arr;
    }
}
