/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.UrlConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.WebApiBase;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.DaccountGetOtt;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.IDimDefines;
import com.nttdocomo.android.tvterminalapp.webapiclient.daccount.OttGetAuthSwitch;

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
    private DaccountGetOtt mGetOtt;

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

        //通信手段が存在しないときはすぐに帰る
        if (mContext != null && !NetWorkUtils.isOnline(mContext)) {
            return;
        }

        if (!mIsCancel && mDetailData != null) {
            if (ContentUtils.DTV_HIKARI_CONTENTS_SERVICE_ID == mDetailData.getServiceId() || mDetailFullData != null) {
                mCategoryId = getCategoryId(mDetailFullData);
            } else {
                mCategoryId = mDetailData.getCategoryId();
            }
            DTVTLogger.debug("”execDaccountGetOTT” sendOpeLog");

            //認証画面の表示状況のインスタンスの取得
            final OttGetAuthSwitch ottGetAuthSwitch = OttGetAuthSwitch.INSTANCE;

            //dアカウントのワンタイムパスワードの取得を行う(未認証時は認証画面へ遷移するように変更)
            mGetOtt = new DaccountGetOtt();
            mGetOtt.execDaccountGetOTT(mContext, ottGetAuthSwitch.isNowAuth(), new DaccountGetOtt.DaccountGetOttCallBack() {
                @Override
                public void getOttCallBack(final int result, final String id, final String oneTimePassword) {
                    if (result == IDimDefines.RESULT_USER_CANCEL) {
                        //キャンセルならば、ログアウトのダイアログを呼び出す
                        ottGetAuthSwitch.showLogoutDialog();
                    } else {
                        //ワンタイムパスワードの取得後に呼び出す
                        mHttpThread = new HttpThread(getUrl(mDetailData), null,
                                mContext, oneTimePassword, mGetOtt);
                        mHttpThread.start();
                    }
                }
            });
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
        mUrl.append(mDetailData.getServiceId() == 0 ? ContentUtils.STR_BLANK : mDetailData.getServiceId());
        mUrl.append(URL_TEXT_CATEGORY_ID);
        if (mCategoryId == null) {
            mCategoryId = ContentUtils.STR_BLANK;
        }
        mUrl.append(mCategoryId);
        if (!TextUtils.isEmpty(mDetailData.getChannelId())) {
            mUrl.append(URL_TEXT_CHANNEL_ID);
            mUrl.append(mDetailData.getChannelId());
        }
        mUrl.append(URL_TEXT_CID);
        if (mDetailData.getContentsId() == null) {
            mUrl.append(ContentUtils.STR_BLANK);
        } else {
            mUrl.append(mDetailData.getContentsId());
        }
        mUrl.append(URL_TEXT_OPERATE_KIND);
        if (ContentUtils.RECOMMEND_INFO_BUNDLE_KEY.equals(mDetailData.getRecommendFlg())) {
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
     * @param metaFullData 詳細フルデータ
     * @return ID
     */
    private String getCategoryId(final VodMetaFullData metaFullData) {
        final String valueBlank = "";
        if (metaFullData != null) {
            switch (metaFullData.getDisp_type() == null ? valueBlank : metaFullData.getDisp_type()) {
                case ContentUtils.TV_PROGRAM:
                    switch (metaFullData.getmTv_service() == null ? valueBlank : metaFullData.getmTv_service()) {
                        case ContentUtils.TV_SERVICE_FLAG_HIKARI:
                            return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_IPTV.getCategoryId();
                        case ContentUtils.TV_SERVICE_FLAG_DCH_IN_HIKARI:
                            switch (metaFullData.getmContent_type() == null ? valueBlank : metaFullData.getmContent_type()) {
                                case valueBlank:
                                case ContentUtils.CONTENT_TYPE_FLAG_ZERO:
                                    return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_DTVCHANNEL_BLOADCAST.getCategoryId();
                                case ContentUtils.CONTENT_TYPE_FLAG_ONE:
                                case ContentUtils.CONTENT_TYPE_FLAG_TWO:
                                    return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_DTVCHANNEL_MISS.getCategoryId();
                                case ContentUtils.CONTENT_TYPE_FLAG_THREE:
                                    return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_DTVCHANNEL_RELATION.getCategoryId();
                                default:
                                    break;
                            }
                        case ContentUtils.TV_SERVICE_FLAG_TTB:
                            return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_TTB.getCategoryId();
                        case ContentUtils.TV_SERVICE_FLAG_BS:
                            return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_BS.getCategoryId();
                        default:
                            break;
                    }
                    break;
                default:
                    switch (metaFullData.getDtv() == null ? valueBlank : metaFullData.getDtv()) {
                        case valueBlank:
                        case ContentUtils.DTV_FLAG_ZERO:
                            return RecommendDataProvider.RecommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getCategoryId();
                        case ContentUtils.DTV_FLAG_ONE:
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
