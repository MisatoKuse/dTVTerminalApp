/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOTT;

public class SendOperateLog extends WebApiBase {

    private StringBuffer mUrl = new StringBuffer("https://ve.m.service.smt.docomo.ne.jp/srermd/operateLog/index.do");
    private String mCategoryId = "";

    //SSLチェック用コンテキスト
    private Context mContext;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * HTTP通信スレッド.
     */
    private HttpThread mHttpThread = null;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public SendOperateLog(Context context) {
        //コンテキストの退避
        mContext = context;
    }

    public void sendOpeLog(final OtherContentsDetailData mDetailData, VodMetaFullData mDetailFullData) {
        if (!mIsCancel && mDetailData != null) {
            if (OtherContentsDetailData.DTV_HIKARI_CONTENTS_SERVICE_ID == mDetailData.getServiceId()) {
                mCategoryId = getCategoryId(mDetailFullData);
            } else {
                mCategoryId = mDetailData.getCategoryId();
            }
            if (!TextUtils.isEmpty(mCategoryId)) {
                //dアカウントのワンタイムパスワードの取得を行う
                DaccountGetOTT getOtt = new DaccountGetOTT();
                getOtt.execDaccountGetOTT(mContext, new DaccountGetOTT.DaccountGetOttCallBack() {
                    @Override
                    public void getOttCallBack(int result, String id, String oneTimePassword) {
                        //ワンタイムパスワードの取得後に呼び出す
                        mHttpThread = new HttpThread(getUrl(mDetailData), null,
                                mContext,oneTimePassword);
                        mHttpThread.start();
                    }
                });
            }
        }
    }

    /**
     * Urlを設定.
     */
    private String getUrl(OtherContentsDetailData mDetailData) {
        mUrl.append("?serviceId=");
        mUrl.append(String.valueOf(mDetailData.getServiceId()));
        mUrl.append("&categoryId=");
        mUrl.append(mCategoryId);
        if (!TextUtils.isEmpty(mDetailData.getChannelId())) {
            mUrl.append("&channelId=");
            mUrl.append(mDetailData.getChannelId());
        }
        mUrl.append("&cid=");
        mUrl.append(mDetailData.getContentId());
        mUrl.append("&operateKind=");
        if (ContentDetailActivity.RECOMMEND_INFO_BUNDLE_KEY.equals(mDetailData.getRecommendFlg())) {
            mUrl.append("412");
        } else {
            mUrl.append("411");
        }
        mUrl.append("&operateDate=");
        mUrl.append(DateUtils.formatEpochToStringOpeLog(DateUtils.getNowTimeFormatEpoch()));
        if (!TextUtils.isEmpty(mDetailData.getRecommendOrder())) {
            mUrl.append("&rank=");
            mUrl.append(mDetailData.getRecommendOrder());
        }
        if (!TextUtils.isEmpty(mDetailData.getPageId())) {
            mUrl.append("&pageId=");
            mUrl.append(mDetailData.getPageId());
        }
        if (!TextUtils.isEmpty(mDetailData.getGroupId())) {
            mUrl.append("&groupId=");
            mUrl.append(mDetailData.getGroupId());
        }
        if (!TextUtils.isEmpty(mDetailData.getRecommendMethodId())) {
            mUrl.append("&recommendMethodId=");
            mUrl.append(mDetailData.getRecommendMethodId());
        }
        return mUrl.toString();
    }

    /**
     * カテゴリーIDを設定.
     */
    private String getCategoryId(VodMetaFullData mDetailFullData) {
        if (mDetailFullData != null) {
            switch (mDetailFullData.getDisp_type()) {
                case "tv_program":
                    switch (mDetailFullData.getmTv_service()) {
                        case "0":
                            return RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_IPTV.getCategoryId();
                        case "1":
                            switch (mDetailFullData.getmContent_type()) {
                                case "0":
                                    return RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_DTV_BLOADCAST.getCategoryId();
                                case "1":
                                case "2":
                                    return RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_DTV_MISS.getCategoryId();
                                case "3":
                                    return RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_DTV_RELATION.getCategoryId();
                                default:
                                    break;
                            }
                        default:
                            break;
                    }
                default:
                    switch (mDetailFullData.getDtv()) {
                        case "0":
                            return RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getCategoryId();
                        case "1":
                            return RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_DTV_SVOD.getCategoryId();
                        default:
                            break;
                    }
            }
        }
        return "";
    }

    /**
     * 通信を止める.
     */
    public void stopConnection() {
        DTVTLogger.start();
        mIsCancel = true;
        if (mHttpThread != null) {
            mHttpThread.disconnect();
        }
    }

    /**
     * 通信可能状態にする.
     */
    public void enableConnection() {
        DTVTLogger.start();
        mIsCancel = false;
    }
}
