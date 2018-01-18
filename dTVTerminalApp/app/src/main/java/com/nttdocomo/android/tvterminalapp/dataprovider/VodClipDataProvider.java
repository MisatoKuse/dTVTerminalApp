/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * クリップ(ビデオ)データプロバイダ.
 */
public class VodClipDataProvider extends ClipKeyListDataProvider implements VodClipWebClient.VodClipJsonParserCallback {

    private Context mContext;
    private VodClipList mClipList = null;

    @Override
    public void onVodClipJsonParsed(List<VodClipList> vodClipLists) {
        if (vodClipLists != null && vodClipLists.size() > 0) {
            VodClipList list = vodClipLists.get(0);
//            setStructDB(list);
            if (!mRequiredClipKeyList
                    || mResponseEndFlag) {
                sendVodClipListData(list.getVcList());
            } else {
                mClipList = list;
            }
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.vodClipListCallback(null);
            }
        }
    }

    @Override
    public void onVodClipKeyListJsonParsed(ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        if(mClipList != null) {
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

    private ApiDataProviderCallback apiDataProviderCallback;

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
        // クリップキー一覧を取得
        if(mRequiredClipKeyList) {
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
        }
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//        List<Map<String, String>> vodClipList = getVodClipListData(pagerOffset);
        getVodClipListData(pagerOffset);
//        if(vodClipList != null && vodClipList.size() > 0){
//            sendVodClipListData(vodClipList);
//        }
    }

    /**
     * VodクリップリストをActivityに送る.
     *
     * @param list Vodクリップリスト
     */
    public void sendVodClipListData(final List<Map<String, String>> list) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
        apiDataProviderCallback.vodClipListCallback(setVodClipContentData(list));
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     *
     * @param clipMapList コンテンツリストデータ
     * @return ListView表示用データ
     */
    private List<ContentsData> setVodClipContentData(final List<Map<String, String>> clipMapList) {
        List<ContentsData> clipDataList = new ArrayList<>();

        ContentsData clipContentInfo;

        for (int i = 0; i < clipMapList.size(); i++) {
            clipContentInfo = new ContentsData();

            Map<String, String> map = clipMapList.get(i);

            String title = map.get(JsonContents.META_RESPONSE_TITLE);
            String searchOk = map.get(JsonContents.META_RESPONSE_SEARCH_OK);
            String linearEndDate = map.get(JsonContents.META_RESPONSE_AVAIL_END_DATE);
            String dispType = map.get(JsonContents.META_RESPONSE_DISP_TYPE);
            String dtv = map.get(JsonContents.META_RESPONSE_DTV);
            String dtvType = map.get(JsonContents.META_RESPONSE_DTV_TYPE);

            clipContentInfo.setRank(String.valueOf(i + 1));
            clipContentInfo.setThumURL(map.get(JsonContents.META_RESPONSE_THUMB_448));
            clipContentInfo.setTitle(title);
            clipContentInfo.setTime(map.get(JsonContents.META_RESPONSE_DISPLAY_START_DATE));
            clipContentInfo.setSearchOk(searchOk);
            clipContentInfo.setRatStar(map.get(JsonContents.META_RESPONSE_RATING));
            clipContentInfo.setContentsType(map.get(JsonContents.META_RESPONSE_CONTENT_TYPE));
            clipContentInfo.setDtv(dtv);
            clipContentInfo.setDtvType(dtvType);
            clipContentInfo.setDispType(dispType);
            clipContentInfo.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();
            requestData.setCrid(map.get(JsonContents.META_RESPONSE_CRID));
            requestData.setServiceId(map.get(JsonContents.META_RESPONSE_SERVICE_ID));
            requestData.setEventId(map.get(JsonContents.META_RESPONSE_EVENT_ID));
            requestData.setTitleId(map.get(JsonContents.META_RESPONSE_TITLE_ID));
            requestData.setTitle(title);
            requestData.setRValue(map.get(JsonContents.META_RESPONSE_R_VALUE));
            requestData.setLinearStartDate(map.get(JsonContents.META_RESPONSE_AVAIL_START_DATE));
            requestData.setLinearEndDate(linearEndDate);
            requestData.setSearchOk(searchOk);

            //視聴通知判定生成
            String contentsType = map.get(JsonContents.META_RESPONSE_CONTENT_TYPE);
            String tvService = map.get(JsonContents.META_RESPONSE_TV_SERVICE);
            String dTv = map.get(JsonContents.META_RESPONSE_DTV);
            requestData.setIsNotify(dispType, contentsType, linearEndDate, tvService, dTv);
            clipContentInfo.setRequestData(requestData);

            if(mRequiredClipKeyList) {
                // クリップ状態をコンテンツリストに格納
                clipContentInfo.setClipStatus(getClipStatus(dispType, contentsType, dTv,
                        clipContentInfo.getCrid(), clipContentInfo.getServiceId(),
                        clipContentInfo.getEventId(), clipContentInfo.getTitleId()));
            }

            clipDataList.add(clipContentInfo);
            DTVTLogger.info("RankingContentInfo " + clipContentInfo.getRank());
        }

        return clipDataList;
    }

    /**
     * Vodクリップリストデータ取得開始.
     *
     * @param pagerOffset ページオフセット
     */
    private void getVodClipListData(final int pagerOffset) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//    private List<Map<String, String>> getVodClipListData(int pagerOffset) {
/*        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(VOD_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        boolean fromDb = lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate);
        if (fromDb) {
            //データをDBから取得する
            VodClipDataManager vodClipDataManager = new VodClipDataManager(mContext);
            list = vodClipDataManager.selectVodClipData();
            if (null == list || 0 == list.size()) {
                fromDb = false;
            }
        }
*/
//        if(!fromDb){
        //通信クラスにデータ取得要求を出す
        VodClipWebClient webClient = new VodClipWebClient(mContext);
        int ageReq = 1;
        int upperPageLimit = 1;
        int lowerPageLimit = 1;
        //int pagerOffset = 1;
        String direction = "";

        webClient.getVodClipApi(ageReq, upperPageLimit,
                lowerPageLimit, pagerOffset, direction, this);
//        }
//        return list;
    }

    /**
     * Vodクリップ一覧データをDBに格納する.
     *
     * @param vodClipList
     */
    private void setStructDB(VodClipList vodClipList) {
        //TODO:Sprint10において、一旦クリップ一覧をキャッシュする処理を消去することになった
//        DateUtils dateUtils = new DateUtils(mContext);
//        dateUtils.addLastDate(VOD_LAST_INSERT);
//        VodClipInsertDataManager dataManager = new VodClipInsertDataManager(mContext);
//        dataManager.insertVodClipInsertList(vodClipList);
    }
}
