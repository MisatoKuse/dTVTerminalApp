/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;


import android.content.Context;

import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendChInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendVdInsertDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendVdList;
import com.nttdocomo.android.tvterminalapp.model.recommend.RecommendContentInfo;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.WEEKLY_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING;
import static com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE;

public class RecommendDataProvider implements
        RecommendWebClient.RecommendCallback{

    private Context mContext;
    private static final String COMMA = ",";
    private static final String SEPARATOR = ":";

    // 取得対象サービスID
    private static final String SERVICE_ID = null;
    // レコメンド取得ページ
    private static final String GET_PAGE = "1";
    // レコメンド取得開始位置
    private static final String START_INDEX = "1";
    // レコメンド取得最大件数
    private static final String MAX_RESULT = "100";
    // 画面ID
    //TODO ダミーデータ
    private static final String PAGE_ID = null;
    // 放送時間考慮
    private static final String AIRTIME = "1";

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
            stringBuilder.append(this.categoryId)
                    .append(SEPARATOR)
                    .append(this.serviceId);
            return stringBuilder.toString();
        }
    }

    private int RECOMMEND_SERVICE_ID_TELEVI_VIDEO
            = Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getServiceId());
    private int RECOMMEND_SERVICE_ID_DCHANNEL
            = Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getServiceId());
    private int RECOMMEND_SERVICE_ID_DTV
            = Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getServiceId());
    private int RECOMMEND_SERVICE_ID_DANIME
            = Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getServiceId());

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




    // TODO レスポンス処理
    @Override
    public void RecommendCallback(RecommendChList mRecommendChList) {
        if (mRecommendChList != null &&
                mRecommendChList.getmRcList() != null &&
                mRecommendChList.getmRcList().size() > 0) {
//            setStructDB(mRecommendChList);
            sendRecommendChListData(mRecommendChList.getmRcList());
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

//    検討予定のコールバックをコメントアウト
//    @Override
//    public void RecommendVideoCallback(RecommendVdList mRecommendVdList) {
//        if (mRecommendVdList != null && mRecommendVdList.getmRvList() != null &&
//                mRecommendVdList.getmRvList().size() > 0) {
////            setStructDB(mRecommendVdList);
//            sendRecommendVdListData(mRecommendVdList.getmRvList());
//        } else {
//            //TODO:WEBAPIを取得できなかった時の処理を記載予定
//        }
//    }
//
//    @Override
//    public void RecommendChannelCallback(RecommendChList mRecommendChList) {
//        if (mRecommendChList != null && mRecommendChList.getmRcList() != null &&
//                mRecommendChList.getmRcList().size() > 0) {
////            setStructDB(mRecommendChList);
//            sendRecommendChListData(mRecommendChList.getmRcList());
//        } else {
//            //TODO:WEBAPIを取得できなかった時の処理を記載予定
//        }
//    }
//
//    @Override
//    public void RecommenddTVCallback(RecommenddTVList mRecommenddTVList) {
//        if (mRecommenddTVList != null && mRecommenddTVList.getmRcList() != null &&
//                mRecommenddTVList.getmRcList().size() > 0) {
////            setStructDB(mRecommenddTVList);
//            sendRecommenddTVListData(mRecommenddTVList.getmRcList());
//        } else {
//            //TODO:WEBAPIを取得できなかった時の処理を記載予定
//        }
//    }
//
//    @Override
//    public void RecommenddAnimeCallback(RecommenddAnimeList mRecommenddAnimeList) {
//        if (mRecommenddAnimeList != null && mRecommenddAnimeList.getmRcList() != null &&
//                mRecommenddAnimeList.getmRcList().size() > 0) {
////            setStructDB(mRecommenddAnimeList);
//            sendRecommenddAnimeListData(mRecommenddAnimeList.getmRcList());
//        } else {
//            //TODO:WEBAPIを取得できなかった時の処理を記載予定
//        }
//    }
//
//    @Override
//    public void RecommenddChCallback(RecommenddCHList mRecommenddCHList) {
//        if (mRecommenddCHList != null && mRecommenddCHList.getmRcList() != null &&
//                mRecommenddCHList.getmRcList().size() > 0) {
////            setStructDB(mRecommenddCHList);
//            sendRecommenddChListData(mRecommenddCHList.getmRcList());
//        } else {
//            //TODO:WEBAPIを取得できなかった時の処理を記載予定
//        }
//    }

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
        void RecommenddTVCallback(List<RecommendContentInfo> recommendContentInfoList);

        /**
         * おすすめdアニメ用コールバック
         *
         * @param recommendContentInfoList
         */
        void RecommenddAnimeCallback(List<RecommendContentInfo> recommendContentInfoList);

        /**
         * おすすめdチャンネル用コールバック
         *
         * @param recommendContentInfoList
         */
        void RecommenddChannelCallback(List<RecommendContentInfo> recommendContentInfoList);
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
     * Activityからのデータ取得要求受付
     * @param requestPageNo タブ種別 ,startIndex 取得開始位置, maxResult 最大取得件数
     */
    public void startGetRecommendData(int requestPageNo,int startIndex,int maxResult) {

        StringBuilder stringBuilder = new StringBuilder();
        switch (requestPageNo) {
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TEREBI: //テレビ
                getTerebiRequestSCIdStr();
                // TODO リクエストデータクラスのインスタンス生成
                // TODO WebClient.Api呼び出し
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
                getVideoRequestSCIdStr();
                // TODO WebClient.Api呼び出し
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
                getdCHRequestSCIdStr();
                // TODO WebClient.Api呼び出し
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV: //dTV
                getdTVRequestSCIdStr();
                // TODO WebClient.Api呼び出し
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                getdAnimeRequestSCIdStr();
                // TODO WebClient.Api呼び出し
                break;
            default:
                // TODO 全件呼び出し
                break;
        }
        try {
//
//            RecommendChWebClient mRecommendChWebClient = new RecommendChWebClient(this);
//            mRecommendChWebClient.getRecommendChannelApi();
//
//            RecommendVdWebClient mRecommendChWebClient2 = new RecommendVdWebClient(this);
//            mRecommendChWebClient2.getRecommendChannelApi();
        } catch (Exception e) {
        }
    }

    // TODO レスポンス処理
    /**
     * おすすめ番組をHomeActivityに送る
     *
     * @param list
     */
    public void sendRecommendChListData(List<Map<String, String>> list) {

        List<RecommendContentInfo> recommendContentInfoList = new ArrayList<RecommendContentInfo>();

        for (Map<String, String> map : list) {
            RecommendContentInfo info = new RecommendContentInfo(
                    map.get(RECOMMENDCHANNEL_LIST_CONTENTSID),
                    Integer.parseInt(map.get(RECOMMENDCHANNEL_LIST_CATEGORYID)),
                    Integer.parseInt(map.get(RECOMMENDCHANNEL_LIST_SERVICEID)),
                    map.get(RECOMMENDCHANNEL_LIST_CTPICURL1),
                    map.get(RECOMMENDCHANNEL_LIST_TITLE),
                    map.get(RECOMMENDCHANNEL_LIST_STARTVIEWING)
            );
            recommendContentInfoList.add(info);
//            for(Map.Entry<String, String> entry : map.entrySet()) {
//
//            }
        }
        int serviceId = recommendContentInfoList.get(0).serviceId;
        int televiServiceId = Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getServiceId());

        if (serviceId == Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getServiceId())) {

            apiDataProviderCallback.RecommendChannelCallback(recommendContentInfoList);
        } else if ((serviceId == Integer.parseInt(recommendRequestId.DTVCHANNEL_BLOADCAST.getServiceId()))) {
            apiDataProviderCallback.RecommenddChannelCallback(recommendContentInfoList);
        } else if (serviceId == Integer.parseInt(recommendRequestId.DTV_SVOD.getServiceId())) {
            apiDataProviderCallback.RecommenddTVCallback(recommendContentInfoList);
        } else if (serviceId == Integer.parseInt(recommendRequestId.DANIME.getServiceId())) {
            apiDataProviderCallback.RecommenddAnimeCallback(recommendContentInfoList);
        }
//        switch (serviceId) {
//            case 0:
//                apiDataProviderCallback.RecommendChannelCallback(recommendContentInfoList);
//                break;
//            case 1:
//                apiDataProviderCallback.RecommendVideoCallback(recommendContentInfoList);
//                break;
//            case 2:
//                apiDataProviderCallback.RecommenddChannelCallback(recommendContentInfoList);
//                break;
//            case 3:
//                apiDataProviderCallback.RecommenddTVCallback(recommendContentInfoList);
//                break;
//            case 4:
//                apiDataProviderCallback.RecommenddAnimeCallback(recommendContentInfoList);
//                break;
//        }


        apiDataProviderCallback.RecommendChannelCallback(recommendContentInfoList);
    }

//    private List<Map<String, String>> getChannelListData() {
//        DateUtils dateUtils = new DateUtils(mContext);
//        String lastDate = dateUtils.getLastDate(CHANNEL_LAST_INSERT);
//
//        List<Map<String, String>> list = new ArrayList<>();
//        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
//        if (lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate)) {
//            //データをDBから取得する
//            HomeDataManager homeDataManager = new HomeDataManager(mContext);
//            list = homeDataManager.selectChannelListHomeData();
//        } else {
//            //通信クラスにデータ取得要求を出す
//            ChannelWebClient webClient = new ChannelWebClient();
//            int ageReq = 1;
//            int upperPageLimit = 1;
//            String lowerPageLimit = "";
//            String pagerOffset = "";
//            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
//            webClient.getChannelApi(ageReq, upperPageLimit,
//                    lowerPageLimit, pagerOffset, this);
//        }
//        return list;
//    }

    // TODO キャッシュ処理
    /**
     * おすすめ番組をDBに保存する
     *
     * @param recommendChList
     */
    public void setStructDB(RecommendChList recommendChList) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
        RecommendChInsertDataManager dataManager = new RecommendChInsertDataManager(mContext);
        dataManager.insertRecommendChInsertList(recommendChList);
    }

    /**
     * おすすめビデオをDBに保存する
     *
     * @param recommendVdList
     */
    public void setStructDB(RecommendVdList recommendVdList) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
        RecommendVdInsertDataManager dataManager = new RecommendVdInsertDataManager(mContext);
        dataManager.insertRecommendVdInsertList(recommendVdList);
    }

//    /**
//     * おすすめdTVをDBに保存する
//     *
//     * @param recommenddTVList
//     */
//    public void setStructDB(RecommendChList recommenddTVList) {
//        DateUtils dateUtils = new DateUtils(mContext);
//        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
//        RecommendVdInsertDataManager dataManager = new RecommendVdInsertDataManager(mContext);
//        dataManager.insertVodClipInsertList(recommenddTVList);
//    }
//
//    /**
//     * おすすめdアニメをDBに保存する
//     *
//     * @param recommenddAnimeList
//     */
//    public void setStructDB(RecommendChList recommenddAnimeList) {
//        DateUtils dateUtils = new DateUtils(mContext);
//        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
//        RecommendVdInsertDataManager dataManager = new RecommendVdInsertDataManager(mContext);
//        dataManager.insertVodClipInsertList(recommenddAnimeList);
//    }
//
//    /**
//     * おすすめdチャンネルをDBに保存する
//     *
//     * @param recommenddCHList
//     */
//    public void setStructDB(RecommendChList recommenddCHList) {
//        DateUtils dateUtils = new DateUtils(mContext);
//        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
//        RecommendVdInsertDataManager dataManager = new RecommendVdInsertDataManager(mContext);
//        dataManager.insertVodClipInsertList(recommenddCHList);
//    }

    /**
     * おすすめテレビの取得対象サービスID:カテゴリーID文字列生成
     * @return
     */
    private String getTerebiRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(recommendRequestId.HIKARITV_DOCOMO_IPTV.getRequestSCId())
                .append(COMMA)
                .append(recommendRequestId.HIKARITV_DOCOMO_DTV_BLOADCAST)
                .append(COMMA)
                .append(recommendRequestId.HIKARITV_DOCOMO_DTV_MISS);

        return stringBuilder.toString();
    }

    /**
     * おすすめビデオの取得対象サービスID:カテゴリーID文字列生成
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
     * @return
     */
    private String getdCHRequestSCIdStr() {
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
     * @return
     */
    private String getdTVRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(recommendRequestId.DTV_SVOD.getRequestSCId())
                .append(COMMA)
                .append(recommendRequestId.DTV_TVOD.getRequestSCId());

        return stringBuilder.toString();
    }

    /**
     * おすすめdアニメの取得対象サービスID:カテゴリーID文字列生成
     * @return
     */
    private String getdAnimeRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(recommendRequestId.DANIME.getRequestSCId());

        return stringBuilder.toString();
    }
}
