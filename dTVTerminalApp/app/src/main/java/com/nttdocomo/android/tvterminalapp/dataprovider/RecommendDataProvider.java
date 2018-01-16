/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendRequestData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecommendDataProvider implements RecommendWebClient.RecommendCallback {

    private Context mContext = null;
    private static final String COMMA = ",";
    private static final String SEPARATOR = ":";

    //テレビのレコメンド情報のタブ番号
    static final int TV_NO = 1;

    //ビデオのレコメンド情報のタブ番号
    static final int VIDEO_NO = 2;

    // ページング判定
    private boolean mIsPaging = false;

    private RecommendApiDataProviderCallback mApiDataProviderCallback = null;

    /**
     * テレビカテゴリー一覧（dTVチャンネル　VOD（見逃し）が無くなった等の新情報を反映）.
     * カテゴリー判定方法の変更のため、int型からInteger型に変更
     */
    private final Integer[] RECOMMEND_CATEGORY_ID_TELEVI = {
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getCategoryId()),
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_DTV_BLOADCAST.getCategoryId()),
            Integer.parseInt(recommendRequestId.DTVCHANNEL_BLOADCAST.getCategoryId()),
    };

    /**
     * ビデオカテゴリー一覧（dTVチャンネル　VOD（見逃し）が追加された等の新情報を反映）.
     * カテゴリー判定方法の変更のため、int型からInteger型に変更
     */
    private final Integer[] RECOMMEND_CATEGORY_ID_VIDEO = {
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_DTV_MISS.getCategoryId()),
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_DTV_RELATION.getCategoryId()),
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getCategoryId()),
            Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_DTV_SVOD.getCategoryId()),
            Integer.parseInt(recommendRequestId.DTV_SVOD.getCategoryId()),
            Integer.parseInt(recommendRequestId.DTV_TVOD.getCategoryId()),
            Integer.parseInt(recommendRequestId.DTVCHANNEL_MISS.getCategoryId()),
            Integer.parseInt(recommendRequestId.DTVCHANNEL_RELATION.getCategoryId()),
            Integer.parseInt(recommendRequestId.DANIME.getCategoryId()),
    };

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

    @Override
    public void RecommendCallback(RecommendChList mRecommendChList) {
        if (mRecommendChList != null &&
                mRecommendChList.getmRcList() != null &&
                mRecommendChList.getmRcList().size() > 0) {
            sendRecommendChListData(mRecommendChList);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
            mApiDataProviderCallback.RecommendNGCallback();
        }
    }

    /**
     * Home画面用データを返却するためのコールバック.
     */
    public interface RecommendApiDataProviderCallback {

        /**
         * おすすめ番組用コールバック.
         *
         * @param recommendContentInfoList
         */
        void RecommendChannelCallback(List<ContentsData> recommendContentInfoList);

        /**
         * おすすめビデオ用コールバック.
         *
         * @param recommendContentInfoList
         */
        void RecommendVideoCallback(List<ContentsData> recommendContentInfoList);

        /**
         * おすすめdTV用コールバック.
         *
         * @param recommendContentInfoList
         */
        void RecommendDTVCallback(List<ContentsData> recommendContentInfoList);

        /**
         * おすすめdアニメ用コールバック.
         *
         * @param recommendContentInfoList
         */
        void RecommendDAnimeCallback(List<ContentsData> recommendContentInfoList);

        /**
         * おすすめdチャンネル用コールバック.
         *
         * @param recommendContentInfoList
         */
        void RecommendDChannelCallback(List<ContentsData> recommendContentInfoList);

        /**
         * 0件取得時のコールバック.
         */
        void RecommendNGCallback();
    }

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public RecommendDataProvider(Context mContext) {
        this.mContext = mContext;
        this.mApiDataProviderCallback = (RecommendApiDataProviderCallback) mContext;
    }

    /**
     * コンストラクタ. コールバックを明示指定する場合.
     *
     * @param context                          コンテキスト
     * @param recommendApiDataProviderCallback コールバック
     */
    public RecommendDataProvider(Context context,
                                 RecommendApiDataProviderCallback recommendApiDataProviderCallback) {
        mContext = context;
        mApiDataProviderCallback = recommendApiDataProviderCallback;
    }

    /**
     * キャッシュからデータを取得.
     *
     * @param cacheDateKey  日付キー
     * @param requestPageNo タブNo
     * @param startIndex    取得開始位置
     * @param maxResult     最大取得件数
     * @return
     */
    private List<ContentsData> getRecommendListDataCache(
            String cacheDateKey, int requestPageNo, int startIndex, int maxResult) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(cacheDateKey);

        List<ContentsData> resultList = new ArrayList<>();
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
     * Activityからのデータ取得要求受付.
     * @param requestPageNo 使用するタブ番号
     * @param startIndex 読み込み初期位置
     * @param maxResult 読み込み最大件数
     * @param hasReturnValue DBにデータがある場合それをすぐ使用するならばtrue。データは常にコールバック経由ならばfalse
     * @return hasReturnValueがtrueでDBにデータがあるならばそれを返す。それ以外はヌルとなる
     */
    public List<ContentsData> startGetRecommendData(int requestPageNo, int startIndex,
                                      int maxResult,boolean hasReturnValue) {
        DTVTLogger.debug("requestPageNo:" + requestPageNo);
        // RequestDataのインスタンス生成
        RecommendRequestData requestData = new RecommendRequestData();

        List<ContentsData> resultList = null;
        switch (requestPageNo) {
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV: //テレビ
                resultList = getRecommendListDataCache
                        (DateUtils.RECOMMEND_CH_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getTerebiRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if(!hasReturnValue) {
                        mApiDataProviderCallback.RecommendChannelCallback(resultList);
                    }
                    return resultList;
                }
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
                resultList = getRecommendListDataCache
                        (DateUtils.RECOMMEND_VD_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getVideoRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if(!hasReturnValue) {
                        mApiDataProviderCallback.RecommendVideoCallback(resultList);
                    }
                    return resultList;
                }
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
                resultList = getRecommendListDataCache
                        (DateUtils.RECOMMEND_DCHANNEL_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getDCHRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if(!hasReturnValue) {
                        mApiDataProviderCallback.RecommendDChannelCallback(resultList);
                    }
                    return resultList;
                }

                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV: //dTV
                resultList = getRecommendListDataCache
                        (DateUtils.RECOMMEND_DTV_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getDTVRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if(!hasReturnValue) {
                        mApiDataProviderCallback.RecommendDTVCallback(resultList);
                    }
                    return resultList;
                }
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                resultList = getRecommendListDataCache
                        (DateUtils.RECOMMEND_DANIME_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getDAnimeRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if(!hasReturnValue) {
                        mApiDataProviderCallback.RecommendDAnimeCallback(resultList);
                    }
                    return resultList;
                }
                break;
            default:
                break;
        }
        requestData.startIndex = String.valueOf(startIndex);
        requestData.maxResult = String.valueOf(maxResult);
        // サーバへリクエストを送信
        RecommendWebClient webClient = new RecommendWebClient(this, mContext);
        webClient.getRecommendApi(requestData);

        //戻り値はコールバック任せとなるので、こちらはヌルを返す
        return null;
    }

    /**
     * おすすめ番組データをServiceIdとCategoryIdを元にキャッシュし、Activityに送る.
     *
     * @param recChList
     */
    public void sendRecommendChListData(RecommendChList recChList) {
        List<Map<String, String>> recList = recChList.getmRcList();
        List<ContentsData> recommendContentInfoList = new ArrayList<>();

        for (Map<String, String> map : recList) {
            recommendContentInfoList.add(setContentsData(map));
        }
        int serviceId = Integer.parseInt(recommendContentInfoList.get(0).getServiceId());
        int categoryId = Integer.parseInt(recommendContentInfoList.get(0).getCategoryId());

        if (serviceId == Integer.parseInt(recommendRequestId.HIKARITV_DOCOMO_IPTV.getServiceId())) {
            //カテゴリーを判定して処理を分ける（ビデオカテゴリーの数が一気に増加したので、今後に備えて配列の判定を変更した。）
            if (Arrays.asList(RECOMMEND_CATEGORY_ID_TELEVI).contains(categoryId)) {
                //RECOMMEND_CATEGORY_ID_TELEVIの中のどれかにcategoryIdが一致した場合
                setStructDB(recChList, DateUtils.RECOMMEND_CH_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV);
                mApiDataProviderCallback.RecommendChannelCallback(recommendContentInfoList);
                return;
            } else if (Arrays.asList(RECOMMEND_CATEGORY_ID_VIDEO).contains(categoryId)) {
                //RECOMMEND_CATEGORY_ID_VIDEOの中のどれかにcategoryIdが一致した場合
                setStructDB(recChList, DateUtils.RECOMMEND_VD_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO);
                mApiDataProviderCallback.RecommendVideoCallback(recommendContentInfoList);
                return;
            }
        } else if ((serviceId == Integer.parseInt(recommendRequestId.DTVCHANNEL_BLOADCAST.getServiceId()))) {
            setStructDB(recChList, DateUtils.RECOMMEND_DCHANNEL_LAST_INSERT,
                    SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL);
            mApiDataProviderCallback.RecommendDChannelCallback(recommendContentInfoList);
            return;
        } else if (serviceId == Integer.parseInt(recommendRequestId.DTV_SVOD.getServiceId())) {
            setStructDB(recChList, DateUtils.RECOMMEND_DTV_LAST_INSERT,
                    SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV);
            mApiDataProviderCallback.RecommendDTVCallback(recommendContentInfoList);
            return;
        } else if (serviceId == Integer.parseInt(recommendRequestId.DANIME.getServiceId())) {
            setStructDB(recChList, DateUtils.RECOMMEND_DANIME_LAST_INSERT,
                    SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME);
            mApiDataProviderCallback.RecommendDAnimeCallback(recommendContentInfoList);
            return;
        }
    }

    /**
     * 1行分のコンテンツデータを作成(メソッドの行数が多すぎる警告の回避用)
     *
     * @param map データの作成元
     * @return 作成したデータ
     */
    private ContentsData setContentsData(Map<String, String> map) {
        ContentsData contentsData = new ContentsData();

        contentsData.setContentsId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID));
        contentsData.setCategoryId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID));
        contentsData.setServiceId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID));
        contentsData.setThumURL(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1));
        contentsData.setTitle(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE));
        contentsData.setStartViewing(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING));
        contentsData.setReserved(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4));
        contentsData.setChannelId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID));
        contentsData.setRecommendOrder(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER));
        contentsData.setPageId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_PAGEID));
        contentsData.setGroupId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_GROUPID));
        contentsData.setRecommendMethodId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID));
        contentsData.setRequestData(setClipResponse(map));

        return contentsData;
    }

    /**
     * クリップ操作用レスポンスを作成.
     *
     * @param map サーバレスポンスデータ
     * @return クリップ用リクエストデータ
     */
    private ClipRequestData setClipResponse(Map<String, String> map) {
        ClipRequestData requestData = new ClipRequestData();
        String title = map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE);

        //クリップ用データ設定
        //TODO:レスポンスがないため、ダミーデータを設定
        requestData.setCrid("672017101601");
        requestData.setServiceId("672017101601");
        requestData.setEventId("14c2");
        requestData.setTitleId("");
        requestData.setTitle(title);
        requestData.setRValue("G");
        requestData.setLinearStartDate("1513071135");
        requestData.setLinearEndDate("1513306982");
        requestData.setSearchOk("0");
        requestData.setIsNotify("disp_type", "content_type",
                "1513306982", "tv_service", "dtv");
        return requestData;
    }

    /**
     * おすすめ番組をDBに保存する.
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
     * おすすめテレビの取得対象サービスID:カテゴリーID文字列生成.
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
     * おすすめビデオの取得対象サービスID:カテゴリーID文字列生成.
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
     * おすすめdチャンネルの取得対象サービスID:カテゴリーID文字列生成.
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
     * おすすめdTVの取得対象サービスID:カテゴリーID文字列生成.
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
     * おすすめdアニメの取得対象サービスID:カテゴリーID文字列生成.
     *
     * @return
     */
    private String getDAnimeRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(recommendRequestId.DANIME.getRequestSCId());

        return stringBuilder.toString();
    }
}