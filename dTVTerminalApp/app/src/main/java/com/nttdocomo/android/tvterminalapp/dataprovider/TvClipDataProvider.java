/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvClipWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TvClipDataProvider extends ClipKeyListDataProvider implements TvClipWebClient.TvClipJsonParserCallback {
    private Context mContext;
    private TvClipList mClipList = null;

    @Override
    public void onTvClipJsonParsed(List<TvClipList> tvClipLists) {
        if (tvClipLists != null && tvClipLists.size() > 0) {
            TvClipList list = tvClipLists.get(0);
//            setStructDB(list);
            if (!mRequiredClipKeyList
                    || mResponseEndFlag) {
                sendTvClipListData(list.getVcList());
            } else {
                mClipList = list;
            }
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.tvClipListCallback(null);
            }
        }
    }

    @Override
    public void onTvClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onTvClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        if(mClipList != null) {
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

    private TvClipDataProviderCallback apiDataProviderCallback;

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
        // クリップキー一覧を取得
        if(mRequiredClipKeyList) {
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.TV));
        }
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//        List<Map<String, String>> tvClipList = getTvClipListData(pagerOffset);
        getTvClipListData(pagerOffset);

//        if (tvClipList != null && tvClipList.size() > 0) {
//            sendTvClipListData(tvClipList);
//        }
    }

    /**
     * TvクリップリストをActivityに送る.
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
    private List<ContentsData> setClipContentData(final List<Map<String, String>> clipMapList) {
        List<ContentsData> contentsDataList = new ArrayList<>();

        ContentsData contentInfo;

        for (int i = 0; i < clipMapList.size(); i++) {
            contentInfo = new ContentsData();

            Map<String, String> map = clipMapList.get(i);
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
            contentInfo.setRatStar(map.get(JsonConstants.META_RESPONSE_RATING));
            contentInfo.setContentsType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
            contentInfo.setDtv(dtv);
            contentInfo.setDtvType(dtvType);
            contentInfo.setDispType(dispType);
            contentInfo.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
            contentInfo.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
            contentInfo.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));

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

            contentsDataList.add(contentInfo);
            DTVTLogger.info("RankingContentInfo " + contentInfo.getRank());

            if(mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                contentInfo.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        contentInfo.getCrid(), contentInfo.getServiceId(),
                        contentInfo.getEventId(), contentInfo.getTitleId()));
            }
        }

        return contentsDataList;
    }

    /**
     * Tvクリップリストデータ取得開始.
     *
     * @param pagerOffset ページオフセット
     */
    private void getTvClipListData(final int pagerOffset) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//    private List<Map<String, String>> getTvClipListData(int pagerOffset) {
//        DateUtils dateUtils = new DateUtils(mContext);
//        String lastDate = dateUtils.getLastDate(TV_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        //if (true) { //test
/*        boolean fromDb = lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate);
        if (fromDb) {
            //データをDBから取得する
            TvClipDataManager tvClipDataManager = new TvClipDataManager(mContext);
            list = tvClipDataManager.selectTvClipData();
            if (null == list || 0 == list.size()) {
                fromDb = false;
            }
        }*/

//        if (!fromDb) {
        //通信クラスにデータ取得要求を出す
        TvClipWebClient webClient = new TvClipWebClient(mContext);
        int ageReq = 1;
        int upperPageLimit = 1;
        int lowerPageLimit = 1;
        //int pagerOffset = 1;
        String pagerDirection = "";
        webClient.getTvClipApi(ageReq, upperPageLimit,
                lowerPageLimit, pagerOffset, pagerDirection, this);
//        }
//        return list;
    }

    /**
     * Vodクリップ一覧データをDBに格納する
     */
    private void setStructDB(final TvClipList tvClipList) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//        DateUtils dateUtils = new DateUtils(mContext);
//        dateUtils.addLastDate(TV_LAST_INSERT);
//        TvClipInsertDataManager dataManager = new TvClipInsertDataManager(mContext);
//        dataManager.insertTvClipInsertList(tvClipList);
    }
}
