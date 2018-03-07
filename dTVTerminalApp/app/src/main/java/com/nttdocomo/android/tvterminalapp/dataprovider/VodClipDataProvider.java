/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * クリップ(ビデオ)データプロバイダ.
 */
public class VodClipDataProvider extends ClipKeyListDataProvider implements VodClipWebClient.VodClipJsonParserCallback {

    /**
     * コンテキスト.
     */
    private Context mContext;
    /**
     * クリップ一覧データ.
     */
    private VodClipList mClipList = null;

    /**
     * callback.
     */
    private ApiDataProviderCallback apiDataProviderCallback;

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * 視聴中ビデオリスト取得WebClient.
     */
    private VodClipWebClient mWebClient = null;
    /**
     * クリップキー一覧取得プロバイダ.
     */
    private ClipKeyListDataProvider mClipKeyListDataProvider = null;

    @Override
    public void onVodClipJsonParsed(final List<VodClipList> vodClipLists) {
        if (vodClipLists != null && vodClipLists.size() > 0) {
            List vclist = vodClipLists.get(0).getVcList();
            if (vclist != null && vclist.size() > 0) {
                HashMap hashMap = (HashMap) vclist.get(0);
                if (!hashMap.isEmpty()) {
                    VodClipList list = vodClipLists.get(0);
                    //            setStructDB(list);
                    if (!mRequiredClipKeyList
                            || mResponseEndFlag) {
                        sendVodClipListData(list.getVcList());
                    } else {
                        mClipList = list;
                        sendVodClipListData(list.getVcList());
                    }
                } else {
                    if (null != apiDataProviderCallback) {
                        apiDataProviderCallback.vodClipListCallback(null);
                    }
                }
            }
        } else {
            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.vodClipListCallback(null);
            }
        }
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        if (mClipList != null) {
            sendVodClipListData(mClipList.getVcList());
        }
        DTVTLogger.end();
    }

    /**
     * 画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {
        /**
         * クリップリスト用コールバック.
         *
         * @param clipContentInfo クリップ表示用データ
         */
        void vodClipListCallback(List<ContentsData> clipContentInfo);
    }

    /**
     * コンストラクタ.
     *
     * @param mContext コンテクスト
     */
    public VodClipDataProvider(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.apiDataProviderCallback = (ApiDataProviderCallback) mContext;
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
                mClipKeyListDataProvider.getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
            }
            getVodClipListData(pagerOffset);
        } else {
            DTVTLogger.error("VodClipDataProvider is stopping connection");
            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.vodClipListCallback(null);
            }
        }
    }

    /**
     * VodクリップリストをActivityに送る.
     *
     * @param list Vodクリップリスト
     */
    private void sendVodClipListData(final List<Map<String, String>> list) {
        apiDataProviderCallback.vodClipListCallback(setVodClipContentData(list));
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     *
     * @param clipMapList コンテンツリストデータ
     * @return ListView表示用データ
     */
    @SuppressWarnings("OverlyLongMethod")
    private List<ContentsData> setVodClipContentData(final List<Map<String, String>> clipMapList) {
        List<ContentsData> clipDataList = new ArrayList<>();

        ContentsData clipContentInfo;

        for (int i = 0; i < clipMapList.size(); i++) {
            clipContentInfo = new ContentsData();

            Map<String, String> map = clipMapList.get(i);

            String title = map.get(JsonConstants.META_RESPONSE_TITLE);
            String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
            String linearEndDate = map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE);
            String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
            String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
            String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);

            clipContentInfo.setRank(String.valueOf(i + 1));
            clipContentInfo.setThumURL(map.get(JsonConstants.META_RESPONSE_THUMB_448));
            clipContentInfo.setThumDetailURL(map.get(JsonConstants.META_RESPONSE_THUMB_640));
            clipContentInfo.setTitle(title);
            clipContentInfo.setTime(map.get(JsonConstants.META_RESPONSE_DISPLAY_START_DATE));
            clipContentInfo.setSearchOk(searchOk);
            clipContentInfo.setRatStar(map.get(JsonConstants.META_RESPONSE_RATING));
            clipContentInfo.setContentsType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
            clipContentInfo.setDtv(dtv);
            clipContentInfo.setDtvType(dtvType);
            clipContentInfo.setDispType(dispType);
            clipContentInfo.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
            clipContentInfo.setContentsId(map.get(JsonConstants.META_RESPONSE_CRID));
            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();
            requestData.setCrid(map.get(JsonConstants.META_RESPONSE_CRID));
            requestData.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
            requestData.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
            requestData.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
            requestData.setTitle(title);
            requestData.setRValue(map.get(JsonConstants.META_RESPONSE_R_VALUE));
            requestData.setLinearStartDate(map.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE));
            requestData.setLinearEndDate(linearEndDate);
            requestData.setSearchOk(searchOk);

            //視聴通知判定生成
            String contentsType = map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
            String tvService = map.get(JsonConstants.META_RESPONSE_TV_SERVICE);
            String dTv = map.get(JsonConstants.META_RESPONSE_DTV);
            requestData.setIsNotify(dispType, contentsType, linearEndDate, tvService, dTv);
            clipContentInfo.setRequestData(requestData);

            if (mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                clipContentInfo.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        requestData.getCrid(), requestData.getServiceId(),
                        requestData.getEventId(), requestData.getTitleId(), tvService));
            }

            clipDataList.add(clipContentInfo);
            DTVTLogger.debug("RankingContentInfo " + clipContentInfo.getRank());
        }

        return clipDataList;
    }

    /**
     * Vodクリップリストデータ取得開始.
     *
     * @param pagerOffset ページオフセット
     */
    private void getVodClipListData(final int pagerOffset) {
        if (!mIsCancel) {
            //通信クラスにデータ取得要求を出す
            mWebClient = new VodClipWebClient(mContext);
            int ageReq = 1;
            int upperPageLimit = 1;
            int lowerPageLimit = 1;
            //int pagerOffset = 1;
            String direction = "";

            mWebClient.getVodClipApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, direction, this);
        } else {
            DTVTLogger.error("VodClipDataProvider is stopping connection");
            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.vodClipListCallback(null);
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
}
