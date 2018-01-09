/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;

public class SendOperateLog extends WebApiBase {

    private OtherContentsDetailData mDetailData =  null;
    private VodMetaFullData mDetailFullData =  null;
    private boolean mRecommendFlg = false;
    private StringBuffer mUrl = new StringBuffer("https://ve.m.service.smt.docomo.ne.jp/srermd/operateLog/index.do");

    public SendOperateLog(OtherContentsDetailData mDetailData, VodMetaFullData mDetailFullData, boolean mRecommendFlg) {
        this.mDetailData = mDetailData;
        this.mDetailFullData = mDetailFullData;
        this.mRecommendFlg = mRecommendFlg;
    }

    public void sendOpeLog() {
        if (mDetailData != null) {
            new HttpThread(getUrl(), null).start();
        } else {
            //テスト用仮データ用
            new HttpThread(getUrlKari(), null).start();
        }
    }

    private String getUrl(){
        mUrl.append("?serciceId=");
        mUrl.append(String.valueOf(mDetailData.getServiceId()));
        if (OtherContentsDetailData.DTV_HIKARI_CONTENTS_SERVICE_ID == mDetailData.getServiceId()) {
            getCategorvId();
        } else {
            mUrl.append("&categorvId=");
            mUrl.append(mDetailData.getCategoryId());
        }
        if (!TextUtils.isEmpty(mDetailData.getChannelId())) {
            mUrl.append("&channelId=");
            mUrl.append(mDetailData.getChannelId());
        }
        mUrl.append("&cid=");
        mUrl.append(mDetailData.getContentId());
        mUrl.append("&operateKind=");
        if (mRecommendFlg){
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

    private String getUrlKari(){
        if (mDetailFullData != null) {
            mUrl.append("?serciceId=");
            mUrl.append(String.valueOf(mDetailFullData.getmService_id()));
            if (String.valueOf(OtherContentsDetailData.DTV_HIKARI_CONTENTS_SERVICE_ID).equals(mDetailFullData.getmService_id())) {
                getCategorvId();
            } else {
                mUrl.append("&categorvId=");
                mUrl.append(mDetailFullData.getCrid());
            }
            if (!TextUtils.isEmpty(mDetailFullData.getmChno())) {
                mUrl.append("&channelId=");
                mUrl.append(mDetailFullData.getmChno());
            }
            mUrl.append("&cid=");
            mUrl.append(mDetailFullData.getCid());
            mUrl.append("&operateKind=");
            if (mRecommendFlg) {
                mUrl.append("412");
            } else {
                mUrl.append("411");
            }
            mUrl.append("&operateDate=");
            mUrl.append(DateUtils.formatEpochToStringOpeLog(DateUtils.getNowTimeFormatEpoch()));
            return mUrl.toString();
        }
        return "";
    }

    private String getCategorvId(){
        String categorvId = "";
        if (mDetailFullData != null) {
            switch (mDetailFullData.getDisp_type()) {
                case "tv_program":
                    switch (mDetailFullData.getmTv_service()) {
                        case "0":
                            categorvId = RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_IPTV.getCategoryId();
                            break;
                        case "1":
                            switch (mDetailFullData.getmContent_type()) {
                                case "0":
                                    categorvId = RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_DTV_BLOADCAST.getCategoryId();
                                    break;
                                case "1":
                                case "2":
                                    categorvId = RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_DTV_MISS.getCategoryId();
                                    break;
                                case "3":
                                    categorvId = RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_DTV_RELATION.getCategoryId();
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                default:
                    switch (mDetailFullData.getDtv()) {
                        case "0":
                            categorvId = RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getCategoryId();
                            break;
                        case "1":
                            categorvId = RecommendDataProvider.recommendRequestId.HIKARITV_DOCOMO_DTV_SVOD.getCategoryId();
                            break;
                        default:
                            break;
                    }
            }
        }
        if (!TextUtils.isEmpty(categorvId)){
            return new StringBuffer("&categorvId=").append(categorvId).toString();
        }
        return "";
    }

}
