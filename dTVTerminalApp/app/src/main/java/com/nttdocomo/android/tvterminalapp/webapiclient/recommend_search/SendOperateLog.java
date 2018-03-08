/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOTT;

/**
 * ログ送信クラス.
 */
public class SendOperateLog extends WebApiBase {

    /**
     * Url.
     */
    private final StringBuffer mUrl = new StringBuffer(UrlConstants.WebApiUrl.RECOMMEND_SEND_OPERATE_LOG_URL);
    /**
     * カテゴリID.
     */
    private String mCategoryId = "";
    /**
     * SSLチェック用コンテキスト.
     */
    final private Context mContext;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * HTTP通信スレッド.
     */
    private HttpThread mHttpThread = null;

    /**
     * OTT取得クラス.
     */
    private DaccountGetOTT mGetOtt;

    /**
     * サービスID.
     */
    private static final String URL_TEXT_SERVICE_ID = "?serviceId=";
    /**
     * カテゴリーID.
     */
    private static final String URL_TEXT_CATEGORY_ID = "&categoryId=";
    /**
     * チャンネルID.
     */
    private static final String URL_TEXT_CHANNEL_ID = "&channelId=";
    /**
     * コンテンツID.
     */
    private static final String URL_TEXT_CID = "&cid=";
    /**
     * 操作種別.
     */
    private static final String URL_TEXT_OPERATE_KIND = "&operateKind=";
    /**
     * 操作種別ページ訪問（レコメンド）.
     */
    private static final String URL_TEXT_OPERATE_KIND_RECOMMEND = "412";
    /**
     * ページ訪問（非レコメンド）.
     */
    private static final String URL_TEXT_OPERATE_KIND_OTHERS = "411";
    /**
     * 操作日時.
     */
    private static final String URL_TEXT_OPERATE_DATE = "&operateDate=";
    /**
     * おすすめ順.
     */
    private static final String URL_TEXT_RANK = "&rank=";
    /**
     * 画面ID.
     */
    private static final String URL_TEXT_PAGE_ID = "&pageId=";
    /**
     * ユーザグループID.
     */
    private static final String URL_TEXT_GROUP_ID = "&groupId=";
    /**
     * レコメンド手法ID.
     */
    private static final String URL_TEXT_RECOMMEND_METHOD_ID = "&recommendMethodId=";

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public SendOperateLog(final Context context) {
        //コンテキストの退避
        mContext = context;
    }

    /**
     * ログの送信.
     *
     * @param mDetailData     コンテンツ詳細データ
     * @param mDetailFullData コンテンツフルデータ
     */
    public void sendOpeLog(final OtherContentsDetailData mDetailData, final VodMetaFullData mDetailFullData) {
        DTVTLogger.start();
        if (!mIsCancel && mDetailData != null) {
            if (OtherContentsDetailData.DTV_HIKARI_CONTENTS_SERVICE_ID == mDetailData.getServiceId() || mDetailFullData != null) {
                mCategoryId = getCategoryId(mDetailFullData);
            } else {
                mCategoryId = mDetailData.getCategoryId();
            }
            if (!TextUtils.isEmpty(mCategoryId)) {
                //dアカウントのワンタイムパスワードの取得を行う
                mGetOtt = new DaccountGetOTT();
                mGetOtt.execDaccountGetOTT(mContext, new DaccountGetOTT.DaccountGetOttCallBack() {
                    @Override
                    public void getOttCallBack(final int result, final String id, final String oneTimePassword) {
                        //ワンタイムパスワードの取得後に呼び出す
                        mHttpThread = new HttpThread(getUrl(mDetailData), null,
                                mContext, oneTimePassword, mGetOtt);
                        mHttpThread.start();
                    }
                });
            }
        }
        DTVTLogger.end();
    }

    /**
     * Urlを設定.
     *
     * @param mDetailData 詳細データ
     * @return Url
     */
    private String getUrl(final OtherContentsDetailData mDetailData) {
        mUrl.append(URL_TEXT_SERVICE_ID);
        mUrl.append(String.valueOf(mDetailData.getServiceId()));
        mUrl.append(URL_TEXT_CATEGORY_ID);
        mUrl.append(mCategoryId);
        if (!TextUtils.isEmpty(mDetailData.getChannelId())) {
            mUrl.append(URL_TEXT_CHANNEL_ID);
            mUrl.append(mDetailData.getChannelId());
        }
        mUrl.append(URL_TEXT_CID);
        mUrl.append(mDetailData.getContentsId());
        mUrl.append(URL_TEXT_OPERATE_KIND);
        if (ContentDetailActivity.RECOMMEND_INFO_BUNDLE_KEY.equals(mDetailData.getRecommendFlg())) {
            mUrl.append(URL_TEXT_OPERATE_KIND_RECOMMEND);
        } else {
            mUrl.append(URL_TEXT_OPERATE_KIND_OTHERS);
        }
        mUrl.append(URL_TEXT_OPERATE_DATE);
        mUrl.append(DateUtils.formatEpochToStringOpeLog(DateUtils.getNowTimeFormatEpoch()));
        if (!TextUtils.isEmpty(mDetailData.getRecommendOrder())) {
            mUrl.append(URL_TEXT_RANK);
            mUrl.append(mDetailData.getRecommendOrder());
        }
        if (!TextUtils.isEmpty(mDetailData.getPageId())) {
            mUrl.append(URL_TEXT_PAGE_ID);
            mUrl.append(mDetailData.getPageId());
        }
        if (!TextUtils.isEmpty(mDetailData.getGroupId())) {
            mUrl.append(URL_TEXT_GROUP_ID);
            mUrl.append(mDetailData.getGroupId());
        }
        if (!TextUtils.isEmpty(mDetailData.getRecommendMethodId())) {
            mUrl.append(URL_TEXT_RECOMMEND_METHOD_ID);
            mUrl.append(mDetailData.getRecommendMethodId());
        }
        return mUrl.toString();
    }

    /**
     * カテゴリーIDを設定.
     *
     * @param mDetailFullData 詳細フルデータ
     * @return ID
     */
    private String getCategoryId(final VodMetaFullData mDetailFullData) {
        final String valueBlank = "";
        if (mDetailFullData != null) {
            switch (mDetailFullData.getDisp_type() == null ? valueBlank : mDetailFullData.getDisp_type()) {
                case ContentDetailActivity.TV_PROGRAM:
                    switch (mDetailFullData.getmTv_service() == null ? valueBlank : mDetailFullData.getmTv_service()) {
                        case ContentDetailActivity.TV_SERVICE_FLAG_ZERO:
                            return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_IPTV.getCategoryId();
                        case ContentDetailActivity.TV_SERVICE_FLAG_ONE:
                            switch (mDetailFullData.getmContent_type() == null ? valueBlank : mDetailFullData.getmContent_type()) {
                                case valueBlank:
                                case ContentDetailActivity.CONTENT_TYPE_FLAG_ZERO:
                                    return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_DTV_BLOADCAST.getCategoryId();
                                case ContentDetailActivity.CONTENT_TYPE_FLAG_ONE:
                                case ContentDetailActivity.CONTENT_TYPE_FLAG_TWO:
                                    return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_DTV_MISS.getCategoryId();
                                case ContentDetailActivity.CONTENT_TYPE_FLAG_THREE:
                                    return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_DTV_RELATION.getCategoryId();
                                default:
                                    break;
                            }
                        default:
                            break;
                    }
                default:
                    switch (mDetailFullData.getDtv() == null ? valueBlank : mDetailFullData.getDtv()) {
                        case valueBlank:
                        case ContentDetailActivity.DTV_FLAG_ZERO:
                            return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getCategoryId();
                        case ContentDetailActivity.DTV_FLAG_ONE:
                            return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_DTV_SVOD.getCategoryId();
                        default:
                            break;
                    }
            }
        }
        return valueBlank;
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
