/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.common.UserState;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvClipWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * クリップ(TV)用データプロバイダ.
 */
public class TvClipDataProvider extends ClipKeyListDataProvider implements TvClipWebClient.TvClipJsonParserCallback {
    /**
     * コンテキスト.
     */
    private Context mContext;
    /**
     * クリップリスト.
     */
    private TvClipList mClipList = null;

    /**
     * callback.
     */
    private TvClipDataProviderCallback apiDataProviderCallback;

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * 視聴中ビデオリスト取得WebClient.
     */
    private TvClipWebClient mWebClient = null;
    /**
     * クリップキー一覧取得プロバイダ.
     */
    private ClipKeyListDataProvider mClipKeyListDataProvider = null;
    /**
     * ネットワークエラーの控え.
     */
    private ErrorState mNetworkError = null;

    @Override
    public void onTvClipJsonParsed(final List<TvClipList> tvClipLists) {
        if (tvClipLists != null) {
            List vclist = tvClipLists.get(0).getVcList();
            if (vclist != null) {
                    TvClipList list = tvClipLists.get(0);
                    if (!mRequiredClipKeyList
                            || mResponseEndFlag) {
                        sendTvClipListData(list.getVcList());
                    } else {
                        mClipList = list;
                        sendTvClipListData(list.getVcList());
                    }
            } else {
                if (null != apiDataProviderCallback) {
                    //ヌルなので、ネットワークエラーを取得する
                    mNetworkError = mWebClient.getError();

                    apiDataProviderCallback.tvClipListCallback(null);
                }
            }
        } else {
            //ヌルなので、ネットワークエラーを取得する
            mNetworkError = mWebClient.getError();

            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.tvClipListCallback(null);
            }
        }
    }

    @Override
    public void onTvClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onTvClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        if (mClipList != null) {
            sendTvClipListData(mClipList.getVcList());
        }
        DTVTLogger.end();
    }

    /**
     * 画面用データを返却するためのコールバック.
     */
    public interface TvClipDataProviderCallback {
        /**
         * クリップリスト用コールバック.
         *
         * @param clipContentInfo クリップ表示情報
         */
        void tvClipListCallback(List<ContentsData> clipContentInfo);
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテクスト
     */
    public TvClipDataProvider(final Context context) {
        super(context);
        this.mContext = context;
        this.apiDataProviderCallback = (TvClipDataProviderCallback) context;
    }

    /**
     * Activityからのデータ取得要求受付.
     *
     * @param pagerOffset ページオフセット
     */
    public void getClipData(final int pagerOffset) {
        mClipList = null;
        if (!mIsCancel) {
            // クリップキー一覧を取得
            if (mRequiredClipKeyList) {
                mClipKeyListDataProvider = new ClipKeyListDataProvider(mContext);
                mClipKeyListDataProvider.getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.TV));
            }
            getTvClipListData(pagerOffset);
        } else {
            DTVTLogger.error("TvClipDataProvider is stopping connection");
            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.tvClipListCallback(null);
            }
        }
    }

    /**
     * TvクリップリストをActivityに送る.
     *
     * @param list Tvクリップリスト
     */
    private void sendTvClipListData(final List<Map<String, String>> list) {
        apiDataProviderCallback.tvClipListCallback(setClipContentData(list));
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     *
     * @param clipMapList コンテンツリストデータ
     * @return ListView表示用データ
     */
    @SuppressWarnings("OverlyLongMethod")
    private List<ContentsData> setClipContentData(final List<Map<String, String>> clipMapList) {
        List<ContentsData> contentsDataList = new ArrayList<>();

        ContentsData contentInfo;

        UserState userState = UserInfoUtils.getUserState(mContext);
        for (int i = 0; i < clipMapList.size(); i++) {
            contentInfo = new ContentsData();

            Map<String, String> map = clipMapList.get(i);
            String title = map.get(JsonConstants.META_RESPONSE_TITLE);
            String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
            String linearEndDate = map.get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE);
            String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
            String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
            String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);

            contentInfo.setRank(String.valueOf(i + 1));
            contentInfo.setThumURL(map.get(JsonConstants.META_RESPONSE_THUMB_448));
            contentInfo.setThumDetailURL(map.get(JsonConstants.META_RESPONSE_THUMB_640));
            contentInfo.setTitle(title);
            contentInfo.setTime(map.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE));
            contentInfo.setSearchOk(searchOk);
            contentInfo.setRatStar(map.get(JsonConstants.META_RESPONSE_RATING));
            contentInfo.setContentsType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
            contentInfo.setDtv(dtv);
            contentInfo.setDtvType(dtvType);
            contentInfo.setDispType(dispType);
            contentInfo.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
            contentInfo.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
            contentInfo.setClipExec(ClipUtils.isCanClip(userState, dispType, searchOk, dtv, dtvType));
            contentInfo.setContentsId(map.get(JsonConstants.META_RESPONSE_CRID));
            contentInfo.setAvailStartDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE)));
            contentInfo.setAvailEndDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)));
            contentInfo.setVodStartDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_VOD_START_DATE)));
            contentInfo.setVodEndDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_VOD_END_DATE)));
            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();
            requestData.setCrid(map.get(JsonConstants.META_RESPONSE_CRID));
            requestData.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
            requestData.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
            requestData.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
            requestData.setTitle(title);
            requestData.setRValue(map.get(JsonConstants.META_RESPONSE_R_VALUE));
            requestData.setLinearStartDate(map.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE));
            requestData.setLinearEndDate(linearEndDate);
            requestData.setSearchOk(searchOk);

            //視聴通知判定生成
            String contentsType = map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
            String tvService = map.get(JsonConstants.META_RESPONSE_TV_SERVICE);
            String dTv = map.get(JsonConstants.META_RESPONSE_DTV);
            requestData.setIsNotify(dispType, contentsType, linearEndDate, tvService, dTv);
            requestData.setDispType(dispType);
            requestData.setContentType(contentsType);
            contentInfo.setRequestData(requestData);

            DTVTLogger.debug("RankingContentInfo " + contentInfo.getRank());

            if (mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                contentInfo.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        requestData.getCrid(), requestData.getServiceId(),
                        requestData.getEventId(), requestData.getTitleId(), tvService));
            }
            //生成した contentInfo をリストに格納する
            contentsDataList.add(contentInfo);
        }

        return contentsDataList;
    }

    /**
     * Tvクリップリストデータ取得開始.
     *
     * @param pagerOffset ページオフセット
     */
    private void getTvClipListData(final int pagerOffset) {
        if (!mIsCancel) {
            //通信クラスにデータ取得要求を出す
            mWebClient = new TvClipWebClient(mContext);

            String pagerDirection = "";
            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
            if (!mWebClient.getTvClipApi(userInfoDataProvider.getUserAge(), DTVTConstants.REQUEST_LIMIT_50,
                    DTVTConstants.REQUEST_LIMIT_50, pagerOffset, pagerDirection, this)) {
                apiDataProviderCallback.tvClipListCallback(null);
            }
        } else {
            DTVTLogger.error("TvClipDataProvider is stopping connection");
            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.tvClipListCallback(null);
            }
        }
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        if (mClipKeyListDataProvider != null) {
            mClipKeyListDataProvider.stopConnection();
        }
        if (mWebClient != null) {
            mWebClient.stopConnection();
        }
    }

    /**
     * 通信許可状態にする.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
        if (mClipKeyListDataProvider != null) {
            mClipKeyListDataProvider.enableConnection();
        }
        if (mWebClient != null) {
            mWebClient.enableConnection();
        }
    }

    /**
     * ネットワークエラーのゲッター.
     *
     * @return ネットワークエラーのクラス
     */
    public ErrorState getNetworkError() {
        return mNetworkError;
    }
}
