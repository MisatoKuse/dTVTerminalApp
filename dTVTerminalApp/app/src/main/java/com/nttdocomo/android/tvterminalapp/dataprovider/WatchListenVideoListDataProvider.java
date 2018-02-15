/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.WatchListenVideoDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.WatchListenVideoListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WatchListenVideoWebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 視聴中ビデオ一覧.
 */
public class WatchListenVideoListDataProvider extends ClipKeyListDataProvider implements WatchListenVideoWebClient.WatchListenVideoJsonParserCallback {

    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * 視聴中ビデオ一覧データ.
     */
    private WatchListenVideoList mWatchListenVideoList = null;

    /**
     * デフォルトのページ取得位置.
     */
    public static final int DEFAULT_PAGE_OFFSET = 1;

    /**
     * callback.
     */
    private WatchListenVideoListProviderCallback mApiDataProviderCallback;

    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * 視聴中ビデオリスト取得WebClient.
     */
    private WatchListenVideoWebClient mWebClient = null;
    /**
     * クリップキー一覧取得プロバイダ.
     */
    private ClipKeyListDataProvider mClipKeyListDataProvider = null;

    @Override
    public void onWatchListenVideoJsonParsed(final List<WatchListenVideoList> watchListenVideoList) {
        WatchListenVideoListDataManager videoListDataManager = new WatchListenVideoListDataManager(mContext);
        if (watchListenVideoList != null && watchListenVideoList.size() > 0) {
            WatchListenVideoList list = watchListenVideoList.get(0);
            setStructDB(list);
            if (!mRequiredClipKeyList
                    || mResponseEndFlag) {
                List<HashMap<String, String>> vcList = list.getVcList();
                if (vcList != null && vcList.get(0).size() > 0 && !vcList.get(0).isEmpty()) {
                    sendWatchListenVideoListData(list.getVcList());
                } else {
                    //通信でデータ取得できないときはDBから取得
                    sendWatchListenVideoListData(videoListDataManager.selectWatchListenVideoData());
                }
            } else {
                mWatchListenVideoList = list;
            }
        } else {
            if (null != mApiDataProviderCallback) {
                //通信でデータ取得できないときはDBから取得
                sendWatchListenVideoListData(videoListDataManager.selectWatchListenVideoData());
            }
        }
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        if (mWatchListenVideoList != null) {
            sendWatchListenVideoListData(mWatchListenVideoList.getVcList());
        }
        DTVTLogger.end();
    }

    /**
     * 画面用データを返却するためのコールバック.
     */
    public interface WatchListenVideoListProviderCallback {
        /**
         * クリップリスト用コールバック.
         *
         * @param clipContentInfo コンテンツ情報
         */
        void watchListenVideoListCallback(List<ContentsData> clipContentInfo);
    }

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     */
    public WatchListenVideoListDataProvider(final Context context) {
        super(context);
        this.mContext = context;
        this.mApiDataProviderCallback = (WatchListenVideoListProviderCallback) context;
    }

    /**
     * Activityからのデータ取得要求受付.
     *
     * @param pagerOffset 取得位置
     */
    public void getWatchListenVideoData(final int pagerOffset) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.WATCHING_VIDEO_LIST_LAST_INSERT);

        // クリップキー一覧を取得
        if (!mIsCancel && mRequiredClipKeyList) {
                mClipKeyListDataProvider = new ClipKeyListDataProvider(mContext);
                mClipKeyListDataProvider.getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
        }

        //視聴中ビデオ一覧のDB保存履歴と、有効期間を確認
        if (!mIsCancel && (lastDate == null || lastDate.length() < 1 || dateUtils.isBeforeLimitDate(lastDate))) {
            mWebClient = new WatchListenVideoWebClient(mContext);

            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
            int ageReq = userInfoDataProvider.getUserAge();
            int upperPageLimit = 20;
            int lowerPageLimit = 1;
            String pagerDirection = "next";

            mWebClient.getWatchListenVideoApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, pagerDirection, this);
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            List<Map<String, String>> watchListenList = new ArrayList<>();
            WatchListenVideoListDataManager watchListenVideoDataManager = new WatchListenVideoListDataManager(mContext);
            watchListenList = watchListenVideoDataManager.selectWatchListenVideoData();
            sendWatchListenVideoListData(watchListenList);
        }
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     *
     * @param mapList コンテンツリストデータ
     */
    private void sendWatchListenVideoListData(final List<Map<String, String>> mapList) {
        //情報が取得できなかった時はActivityにnullを返却
        if (mapList == null || mapList.size() < 1 || mapList.get(0).isEmpty()) {
            mApiDataProviderCallback.watchListenVideoListCallback(null);
            return;
        }

        List<ContentsData> rankingContentsDataList = new ArrayList<>();

        ContentsData contentInfo;

        for (int i = 0; i < mapList.size(); i++) {
            contentInfo = new ContentsData();

            Map<String, String> map = mapList.get(i);
            String title = map.get(JsonConstants.META_RESPONSE_TITLE);
            String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
            String linearEndDate = map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE);
            String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
            String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
            String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);

            contentInfo.setRank(String.valueOf(i + 1));
            contentInfo.setThumURL(map.get(JsonConstants.META_RESPONSE_THUMB_448));
            contentInfo.setTitle(title);
            contentInfo.setTime(map.get(JsonConstants.META_RESPONSE_DISPLAY_START_DATE));
            contentInfo.setSearchOk(searchOk);
            contentInfo.setRank(String.valueOf(i + 1));
            contentInfo.setRatStar(map.get(JsonConstants.META_RESPONSE_RATING));
            contentInfo.setContentsType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
            contentInfo.setDtv(dtv);
            contentInfo.setDtvType(dtvType);
            contentInfo.setDispType(dispType);
            contentInfo.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
            contentInfo.setContentsId(map.get(JsonConstants.META_RESPONSE_CONTENTS_ID));
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
            requestData.setDispType(dispType);
            requestData.setContentType(contentsType);
//            requestData.setTableType(decisionTableType(contentsType, contentsType));
            contentInfo.setRequestData(requestData);

            if (mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                contentInfo.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        requestData.getCrid(), requestData.getServiceId(),
                        requestData.getEventId(), requestData.getTitleId()));
            }

            rankingContentsDataList.add(contentInfo);
            DTVTLogger.info("RankingContentInfo " + contentInfo.getRank());
        }

        mApiDataProviderCallback.watchListenVideoListCallback(rankingContentsDataList);
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
     * 視聴中ビデオ一覧データをDBに格納する.
     *
     * @param watchListenVideoList 視聴中ビデオ一覧用データ
     */
    private void setStructDB(final WatchListenVideoList watchListenVideoList) {
        mWatchListenVideoList = watchListenVideoList;
        //DB保存
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DateUtils.WATCHING_VIDEO_LIST_LAST_INSERT);
        WatchListenVideoDataManager watchListenVideoDataManager = new WatchListenVideoDataManager(mContext);
        watchListenVideoDataManager.insertWatchListenVideoInsertList(mWatchListenVideoList);
    }
}
