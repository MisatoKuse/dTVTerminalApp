/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvClipInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.TvClipDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvClipWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.TV_LAST_INSERT;


public class TvClipDataProvider implements TvClipWebClient.TvClipJsonParserCallback {
    private Context mContext;

    @Override
    public void onTvClipJsonParsed(List<TvClipList> tvClipLists) {
        if (tvClipLists != null && tvClipLists.size() > 0) {
            TvClipList list = tvClipLists.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
            if (null != apiDataProviderCallback) {
                apiDataProviderCallback.tvClipListCallback(null);
            }
        }
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
        this.mContext = context;
        this.apiDataProviderCallback = (TvClipDataProviderCallback) context;
    }

    /**
     * Activityからのデータ取得要求受付.
     *
     * @param pagerOffset ページオフセット
     */
    public void getClipData(final int pagerOffset) {
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
        List<ContentsData> rankingContentsDataList = new ArrayList<>();

        ContentsData rankingContentInfo;

        for (int i = 0; i < clipMapList.size(); i++) {
            rankingContentInfo = new ContentsData();

            Map<String, String> map = clipMapList.get(i);
            String title = map.get(JsonContents.META_RESPONSE_TITLE);
            String searchOk = map.get(JsonContents.META_RESPONSE_SEARCH_OK);
            String linearEndDate = map.get(JsonContents.META_RESPONSE_AVAIL_END_DATE);
            String dispType = map.get(JsonContents.META_RESPONSE_DISP_TYPE);
            String dtv = map.get(JsonContents.META_RESPONSE_DTV);
            String dtvType = map.get(JsonContents.META_RESPONSE_DTV_TYPE);

            rankingContentInfo.setRank(String.valueOf(i + 1));
            rankingContentInfo.setThumURL(map.get(JsonContents.META_RESPONSE_THUMB_448));
            rankingContentInfo.setTitle(title);
            rankingContentInfo.setTime(map.get(JsonContents.META_RESPONSE_DISPLAY_START_DATE));
            rankingContentInfo.setSearchOk(searchOk);
            rankingContentInfo.setRatStar(map.get(JsonContents.META_RESPONSE_RATING));
            rankingContentInfo.setContentsType(map.get(JsonContents.META_RESPONSE_CONTENT_TYPE));
            rankingContentInfo.setDtv(dtv);
            rankingContentInfo.setDispType(dispType);
            rankingContentInfo.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));

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
            requestData.setDispType(dispType);
            requestData.setContentType(contentsType);
//            requestData.setTableType(decisionTableType(contentsType, contentsType));
            rankingContentInfo.setRequestData(requestData);

            rankingContentsDataList.add(rankingContentInfo);
            DTVTLogger.info("RankingContentInfo " + rankingContentInfo.getRank());
        }

        return rankingContentsDataList;
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
        TvClipWebClient webClient = new TvClipWebClient();
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
        sendTvClipListData(tvClipList.getVcList());
    }
}
