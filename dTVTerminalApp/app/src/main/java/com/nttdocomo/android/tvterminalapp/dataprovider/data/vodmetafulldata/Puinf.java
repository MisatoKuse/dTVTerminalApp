/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider.data.vodmetafulldata;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * VOD&番組メタレスポンス（フル版）.
 * 販売情報リストの内容
 */
public class Puinf implements Serializable {

    // ライセンスID
    private String mPuid = null;
    // ライセンスIDのCRID
    private String mCrid = null;
    // ライセンスIDのタイトル
    private String mTitle = null;
    // ライセンスIDのエピソードタイトル
    private String mEpititle = null;
    // 表示タイプ
    private String mDisp_type = null;
    // CHSVOD
    private String mChsvod = null;
    // ライセンス価格
    private String mPrice = null;
    // ライセンス購入単位の期間(3日の3)
    private String mQunit = null;
    // ライセンス購入単位の単位(3日の「日」)
    private String mQrange = null;
    // ライセンス販売可能期間（開始）(EPOC秒なので数値)
    private long mPu_start_date = 0;
    // ライセンス販売可能期間（終了）(EPOC秒なので数値)
    private long mPu_end_date = 0;

    private static final long serialVersionUID = 147899028985473046L;

    public String getmPuid() {
        return mPuid;
    }

    public void setmPuid(final String mPuid) {
        this.mPuid = mPuid;
    }

    public String getmCrid() {
        return mCrid;
    }

    public void setmCrid(final String mCrid) {
        this.mCrid = mCrid;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(final String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmEpititle() {
        return mEpititle;
    }

    public void setmEpititle(final String mEpititle) {
        this.mEpititle = mEpititle;
    }

    public String getmDisp_type() {
        return mDisp_type;
    }

    public void setmDisp_type(final String mDisp_typ) {
        this.mDisp_type = mDisp_typ;
    }

    public String getmChsvod() {
        return mChsvod;
    }

    public void setmChsvod(final String mChsvod) {
        this.mChsvod = mChsvod;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(final String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmQunit() {
        return mQunit;
    }

    public void setmQunit(final String mQunit) {
        this.mQunit = mQunit;
    }

    public String getmQrange() {
        return mQrange;
    }

    public void setmQrange(String mQrange) {
        this.mQrange = mQrange;
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

    //キー名：販売情報リスト
    private static final String[] mPUINF_ListPara = {
            JsonConstants.META_RESPONSE_PUID, JsonConstants.META_RESPONSE_CRID,
            JsonConstants.META_RESPONSE_TITLE, JsonConstants.META_RESPONSE_EPITITLE,
            JsonConstants.META_RESPONSE_DISP_TYPE, JsonConstants.META_RESPONSE_CHSVOD,
            JsonConstants.META_RESPONSE_PRICE, JsonConstants.META_RESPONSE_QUNIT,
            JsonConstants.META_RESPONSE_QRANGE, JsonConstants.META_RESPONSE_PU_START_DATE,
            JsonConstants.META_RESPONSE_PU_END_DATE,
    };

    /**
     * PUINFの値をメンバーにセットする.
     *
     * @param puInf PUINFの値(JSONObject)
     */
    public void setPuinfData(final JSONObject puInf) {
        try {
            if (puInf != null) {
                //項目の数だけ回り、データを蓄積する
                for (String item : mPUINF_ListPara) {
                    if (!puInf.isNull(item)) {
                        setMember(item, puInf.get(item));
                    }
                }
            }
        } catch (JSONException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * キーとキーの値をメンバーにセットする
     * (メソッドが長すぎると警告が出るが、分割するとむしろわかりにくくなるのでこのままとする)
     *
     * @param key  キー
     * @param data キーの値
     */
    private void setMember(final String key, final Object data) {
        //キーに値があれば、それを元に値を格納する
        if (key != null && !key.isEmpty()) {
            switch (key) {
                case JsonConstants.META_RESPONSE_PUID:
                    // ライセンスID
                    mPuid = (String) data;
                    break;
                case JsonConstants.META_RESPONSE_CRID:
                    // ライセンスIDのCRID
                    mCrid = (String) data;
                    break;
                case JsonConstants.META_RESPONSE_TITLE:
                    // ライセンスIDのタイトル
                    mTitle = (String) data;
                    break;
                case JsonConstants.META_RESPONSE_EPITITLE:
                    // ライセンスIDのエピソードタイトル
                    mEpititle = (String) data;
                    break;
                case JsonConstants.META_RESPONSE_CHSVOD:
                    //CHSVOD
                    mChsvod = (String) data;
                    break;
                case JsonConstants.META_RESPONSE_PRICE:
                    // 価格(税込)
                    mPrice = (String) data;
                    break;
                case JsonConstants.META_RESPONSE_QUNIT:
                    // 購入単位の期間(3日の3)
                    mQunit = (String) data;
                    break;
                case JsonConstants.META_RESPONSE_QRANGE:
                    // 購入単位の単位(3日の「日」)
                    mQrange = (String) data;
                    break;
                case JsonConstants.META_RESPONSE_PU_START_DATE:
                    // 表示タイプ
                    mPu_start_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_PU_END_DATE:
                    // 表示タイプ
                    mPu_end_date = StringUtils.changeString2Long(data);
                    break;
                case JsonConstants.META_RESPONSE_DISP_TYPE:
                    // 表示タイプ
                    mDisp_type = (String) data;
                    break;
                default:
                    //何もしない
            }
        }
    }

    /**
     * キーの値を取得する.
     *
     * @param key 取得したい値のキー
     */
    public Object getMember(final String key) {

        if (key == null || key.isEmpty()) {
            //キーが無いので、空文字を返す
            return "";
        } else {
            // 指定された項目名の値を返却する
            switch (key) {
                case JsonConstants.META_RESPONSE_PUID:
                    return mPuid;
                case JsonConstants.META_RESPONSE_CRID:
                    return mCrid;
                case JsonConstants.META_RESPONSE_TITLE:
                    return mTitle;
                case JsonConstants.META_RESPONSE_EPITITLE:
                    return mEpititle;
                case JsonConstants.META_RESPONSE_DISP_TYPE:
                    return mDisp_type;
                case JsonConstants.META_RESPONSE_CHSVOD:
                    return mChsvod;
                case JsonConstants.META_RESPONSE_PRICE:
                    return mPrice;
                case JsonConstants.META_RESPONSE_QUNIT:
                    return mQunit;
                case JsonConstants.META_RESPONSE_QRANGE:
                    return mQrange;
                case JsonConstants.META_RESPONSE_PU_START_DATE:
                    return mPu_start_date;
                case JsonConstants.META_RESPONSE_PU_END_DATE:
                    return mPu_end_date;
                default:
                    return "";
            }
        }
    }
}
