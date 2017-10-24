/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;


import android.content.Context;
import android.util.Log;

import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.model.recommend.RecommendContentInfo;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendRequestData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RECOMMEND_CH_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RECOMMEND_DANIME_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RECOMMEND_DCHANNEL_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RECOMMEND_DTV_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.RECOMMEND_VD_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE;

public class RecommendDataProvider implements
        RecommendWebClient.RecommendCallback {

    private Context mContext;
    private static final String COMMA = ",";
    private static final String SEPARATOR = ":";

    // ページング判定
    private boolean mIsPaging = false;


    // 取得対象サービスID:カテゴリーID
    public enum recommendRequestId {
        DTV_SVOD("15", "01"),
        DTV_TVOD("15", "02"),
        DANIME("17", "01"),
        DTVCHANNEL_BLOADCAST("43", "01"),
        DTVCHANNEL_MISS("43", "02"),
        DTVCHANNEL_RELATION("43", "03"),
        HIKARITV_DOCOMO_IPTV("44", "03"),
        HIKARITV_DOCOMO_DTV_BLOADCAST("44", "04"),
        HIKARITV_DOCOMO_DTV_MISS("44", "05"),
        HIKARITV_DOCOMO_DTV_RELATION("44", "06"),
        HIKARITV_DOCOMO_HIKARITV_VOD("44", "08"),
        HIKARITV_DOCOMO_DTV_SVOD("44", "10"),;
        private final String serviceId;
        private final String categoryId;

        private recommendRequestId(final String sId, final String cId) {
            this.serviceId = sId;
            this.categoryId = cId;
        }

        // サービスID
        public String getServiceId() {
            return this.serviceId;
        }

        // カテゴリID
        public String getCategoryId() {
            return this.categoryId;
        }

        // サービスID:カテゴリID
        public String getRequestSCId() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.serviceId)
                    .append(SEPARATOR)
                    .append(this.categoryId);
            return stringBuilder.toString();
        }
    }

    private int[] RECOMMEND_CATEGORY_ID_TELEVI
            = {Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getCategoryId()),
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_DTV_BLOADCAST.getCategoryId()),
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_DTV_MISS.getCategoryId())
    };
    private int[] RECOMMEND_CATEGORY_ID_VIDEO
            = {Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_DTV_RELATION.getCategoryId()),
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getCategoryId()),
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_DTV_SVOD.getCategoryId())
    };


    @Override
    public void RecommendCallback(RecommendChList mRecommendChList) {
        if (mRecommendChList != null &&
                mRecommendChList.getmRcList() != null &&
                mRecommendChList.getmRcList().size() > 0) {
            sendRecommendChListData(mRecommendChList);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
            apiDataProviderCallback.RecommendNGCallback();
        }
    }


    /**
     * Home画面用データを返却するためのコールバック
     */
    public interface RecommendApiDataProviderCallback {

        /**
         * おすすめ番組用コールバック
         *
         * @param recommendContentInfoList
         */
        void RecommendChannelCallback(List<RecommendContentInfo> recommendContentInfoList);

        /**
         * おすすめビデオ用コールバック
         *
         * @param recommendContentInfoList
         */
        void RecommendVideoCallback(List<RecommendContentInfo> recommendContentInfoList);

        /**
         * おすすめdTV用コールバック
         *
         * @param recommendContentInfoList
         */
        void RecommendDTVCallback(List<RecommendContentInfo> recommendContentInfoList);

        /**
         * おすすめdアニメ用コールバック
         *
         * @param recommendContentInfoList
         */
        void RecommendDAnimeCallback(List<RecommendContentInfo> recommendContentInfoList);

        /**
         * おすすめdチャンネル用コールバック
         *
         * @param recommendContentInfoList
         */
        void RecommendDChannelCallback(List<RecommendContentInfo> recommendContentInfoList);

        /**
         * 0件取得時のコールバック
         */
        void RecommendNGCallback();
    }

    private RecommendApiDataProviderCallback apiDataProviderCallback;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public RecommendDataProvider(Context mContext) {
        this.mContext = mContext;
        this.apiDataProviderCallback = (RecommendApiDataProviderCallback) mContext;
    }

    /**
     * キャッシュからデータを取得
     *
     * @param cacheDateKey  日付キー
     * @param requestPageNo タブNo
     * @param startIndex    取得開始位置
     * @param maxResult     最大取得件数
     * @return
     */
    private List<RecommendContentInfo> getRecommendListDataCache(
            String cacheDateKey, int requestPageNo, int startIndex, int maxResult) {

        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(cacheDateKey);

        List<RecommendContentInfo> resultList = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            RecommendListDataManager recommendDataManager = new RecommendListDataManager(mContext);
            resultList = recommendDataManager.selectRecommendList(requestPageNo, startIndex, maxResult);
        } else {
            // nop.
        }
        return resultList;
    }

    /**
     * Activityからのデータ取得要求受付
     *
     * @param requestPageNo タブ種別 ,startIndex 取得開始位置, maxResult 最大取得件数
     */
    public void startGetRecommendData(int requestPageNo, int startIndex, int maxResult) {
        Log.i(DTVTConstants.LOG_DEF_TAG, "requestPageNo:" + requestPageNo);
        // RequestDataのインスタンス生成
        RecommendRequestData requestData = new RecommendRequestData();

        List<RecommendContentInfo> resultList = null;
        switch (requestPageNo) {
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV: //テレビ
                resultList = getRecommendListDataCache
                        (RECOMMEND_CH_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getTerebiRequestSCIdStr();
                } else {
                    apiDataProviderCallback.RecommendChannelCallback(resultList);
                    return;
                }
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
                resultList = getRecommendListDataCache
                        (RECOMMEND_VD_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getVideoRequestSCIdStr();
                } else {
                    apiDataProviderCallback.RecommendVideoCallback(resultList);
                    return;
                }
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
                resultList = getRecommendListDataCache
                        (RECOMMEND_DCHANNEL_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getDCHRequestSCIdStr();
                } else {
                    apiDataProviderCallback.RecommendDChannelCallback(resultList);
                    return;
                }

                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV: //dTV
                resultList = getRecommendListDataCache
                        (RECOMMEND_DTV_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getDTVRequestSCIdStr();
                } else {
                    apiDataProviderCallback.RecommendDTVCallback(resultList);
                    return;
                }
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                resultList = getRecommendListDataCache
                        (RECOMMEND_DANIME_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getDAnimeRequestSCIdStr();
                } else {
                    apiDataProviderCallback.RecommendDAnimeCallback(resultList);
                    return;
                }
                break;
            default:
                break;
        }
        requestData.startIndex = String.valueOf(startIndex);
        requestData.maxResult = String.valueOf(maxResult);
        // サーバへリクエストを送信
        RecommendWebClient webClient = new RecommendWebClient(this);
        webClient.getRecommendApi(requestData);
    }

    /**
     * おすすめ番組データをServiceIdとCategoryIdを元にキャッシュし、Activityに送る
     *
     * @param recChList
     */
    public void sendRecommendChListData(RecommendChList recChList) {

        List<Map<String, String>> recList = recChList.getmRcList();
        List<RecommendContentInfo> recommendContentInfoList = new ArrayList<RecommendContentInfo>();

        for (Map<String, String> map : recList) {
            RecommendContentInfo info = new RecommendContentInfo(
                    map.get(RECOMMENDCHANNEL_LIST_CONTENTSID),
                    Integer.parseInt(map.get(RECOMMENDCHANNEL_LIST_CATEGORYID)),
                    Integer.parseInt(map.get(RECOMMENDCHANNEL_LIST_SERVICEID)),
                    map.get(RECOMMENDCHANNEL_LIST_CTPICURL1),
                    map.get(RECOMMENDCHANNEL_LIST_TITLE),
                    map.get(RECOMMENDCHANNEL_LIST_STARTVIEWING)
            );
            recommendContentInfoList.add(info);
        }
        int serviceId = recommendContentInfoList.get(0).serviceId;
        int categoryId = recommendContentInfoList.get(0).categoryId;

        if (serviceId == Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getServiceId())) {
            if (categoryId == RECOMMEND_CATEGORY_ID_TELEVI[0]
                    || categoryId == RECOMMEND_CATEGORY_ID_TELEVI[1]
                    || categoryId == RECOMMEND_CATEGORY_ID_TELEVI[2]) {

                setStructDB(recChList, RECOMMEND_CH_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV);
                apiDataProviderCallback.RecommendChannelCallback(recommendContentInfoList);
                return;
            } else if (categoryId == RECOMMEND_CATEGORY_ID_VIDEO[0]
                    || categoryId == RECOMMEND_CATEGORY_ID_VIDEO[1]
                    || categoryId == RECOMMEND_CATEGORY_ID_VIDEO[2]) {
                setStructDB(recChList, RECOMMEND_VD_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO);
                apiDataProviderCallback.RecommendVideoCallback(recommendContentInfoList);
                return;
            }
        } else if ((serviceId == Integer.parseInt(recommendRequestId.DTVCHANNEL_BLOADCAST.getServiceId()))) {
            setStructDB(recChList, RECOMMEND_DCHANNEL_LAST_INSERT,
                    SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL);
            apiDataProviderCallback.RecommendDChannelCallback(recommendContentInfoList);
            return;
        } else if (serviceId == Integer.parseInt(recommendRequestId.DTV_SVOD.getServiceId())) {
            setStructDB(recChList, RECOMMEND_DTV_LAST_INSERT,
                    SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV);
            apiDataProviderCallback.RecommendDTVCallback(recommendContentInfoList);
            return;
        } else if (serviceId == Integer.parseInt(recommendRequestId.DANIME.getServiceId())) {
            setStructDB(recChList, RECOMMEND_DANIME_LAST_INSERT,
                    SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME);
            apiDataProviderCallback.RecommendDAnimeCallback(recommendContentInfoList);
            return;
        }
    }


    /**
     * おすすめ番組をDBに保存する
     *
     * @param recommendChList
     */
    public void setStructDB(RecommendChList recommendChList, String cacheDateKey, int tagPageNo) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(cacheDateKey);
        RecommendListDataManager dataManager = new RecommendListDataManager(mContext);
        dataManager.insertRecommendInsertList(recommendChList, mIsPaging, tagPageNo);
    }


    /**
     * おすすめテレビの取得対象サービスID:カテゴリーID文字列生成
     *
     * @return
     */
    private String getTerebiRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(recommendRequestId.HIKARITV_DOCOMO_IPTV.getRequestSCId())
                .append(COMMA)
                .append(recommendRequestId.HIKARITV_DOCOMO_DTV_BLOADCAST.getRequestSCId())
                .append(COMMA)
                .append(recommendRequestId.HIKARITV_DOCOMO_DTV_MISS.getRequestSCId());

        return stringBuilder.toString();
    }

    /**
     * おすすめビデオの取得対象サービスID:カテゴリーID文字列生成
     *
     * @return
     */
    private String getVideoRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(recommendRequestId.HIKARITV_DOCOMO_DTV_RELATION.getRequestSCId())
                .append(COMMA)
                .append(recommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getRequestSCId())
                .append(COMMA)
                .append(recommendRequestId.HIKARITV_DOCOMO_DTV_SVOD.getRequestSCId());

        return stringBuilder.toString();
    }

    /**
     * おすすめdチャンネルの取得対象サービスID:カテゴリーID文字列生成
     *
     * @return
     */
    private String getDCHRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(recommendRequestId.DTVCHANNEL_BLOADCAST.getRequestSCId())
                .append(COMMA)
                .append(recommendRequestId.DTVCHANNEL_MISS.getRequestSCId())
                .append(COMMA)
                .append(recommendRequestId.DTVCHANNEL_RELATION.getRequestSCId());

        return stringBuilder.toString();
    }

    /**
     * おすすめdTVの取得対象サービスID:カテゴリーID文字列生成
     *
     * @return
     */
    private String getDTVRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(recommendRequestId.DTV_SVOD.getRequestSCId())
                .append(COMMA)
                .append(recommendRequestId.DTV_TVOD.getRequestSCId());

        return stringBuilder.toString();
    }

    /**
     * おすすめdアニメの取得対象サービスID:カテゴリーID文字列生成
     *
     * @return
     */
    private String getDAnimeRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(recommendRequestId.DANIME.getRequestSCId());

        return stringBuilder.toString();
    }
}