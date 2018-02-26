/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DBUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendRequestData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * レコメンド用データプロバイダ
 */
public class RecommendDataProvider implements RecommendWebClient.RecommendCallback, DbThread.DbOperation {

    private Context mContext = null;
    private static final String COMMA = ",";
    private static final String SEPARATOR = ":";

    //テレビのレコメンド情報のタブ番号
    static final int TV_NO = 0;

    //ビデオのレコメンド情報のタブ番号
    static final int VIDEO_NO = 1;

    // ページング判定
    private boolean mIsPaging = false;

    //コールバック
    private RecommendApiDataProviderCallback mApiDataProviderCallback = null;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsStop = false;

    //タブ番号の控え
    private int mRequestPageNo;

    /**
     * RecommendWebClient.
     */
    private RecommendWebClient mRecommendWebClient = null;
    /**
     * テレビカテゴリー一覧（dTVチャンネル　VOD（見逃し）が無くなった等の新情報を反映）.
     */
    private final String[] RECOMMEND_CATEGORY_ID_TELEVI = {
            RecommendRequestId.DTVCHANNEL_BLOADCAST.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_IPTV.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_DTV_BLOADCAST.getRequestSCId(),
    };

    /**
     * ビデオカテゴリー一覧（dTVチャンネル　VOD（見逃し）が追加された等の新情報を反映）.
     */
    private final String[] RECOMMEND_CATEGORY_ID_VIDEO = {
            RecommendRequestId.HIKARITV_DOCOMO_DTV_MISS.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_DTV_RELATION.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_DTV_SVOD.getRequestSCId(),
            RecommendRequestId.DTV_SVOD.getRequestSCId(),
            RecommendRequestId.DTV_TVOD.getRequestSCId(),
            RecommendRequestId.DTVCHANNEL_MISS.getRequestSCId(),
            RecommendRequestId.DTVCHANNEL_RELATION.getRequestSCId(),
            RecommendRequestId.DANIME.getRequestSCId(),
    };

    /**
     * dTVカテゴリー一覧.
     */
    private final String[] RECOMMEND_CATEGORY_ID_DTV = {
            RecommendRequestId.DTV_SVOD.getRequestSCId(),
            RecommendRequestId.DTV_TVOD.getRequestSCId(),
    };

    /**
     * dTVチャンネルカテゴリー一覧.
     */
    private final String[] RECOMMEND_CATEGORY_ID_DTV_CHANNEL = {
            RecommendRequestId.DTVCHANNEL_BLOADCAST.getRequestSCId(),
            RecommendRequestId.DTVCHANNEL_MISS.getRequestSCId(),
            RecommendRequestId.DTVCHANNEL_RELATION.getRequestSCId(),
    };
    /**
     * 取得対象サービスID:カテゴリーID.
     */
    public enum RecommendRequestId {
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

        /**
         * 定数をENUMで蓄積するメソッド.
         *
         * @param serviceIdSource  元になるサービスID
         * @param categoryIdSource 元になるカテゴリーID
         */
        RecommendRequestId(final String serviceIdSource, final String categoryIdSource) {
            this.serviceId = serviceIdSource;
            this.categoryId = categoryIdSource;
        }

        /**
         * サービスIDのゲッター.
         * @return サービスID
         */
        public String getServiceId() {
            return this.serviceId;
        }
        /**
         * カテゴリーIDのゲッター.
         * @return カテゴリーID
         */
        public String getCategoryId() {
            return this.categoryId;
        }

        /**
         * "サービスID:カテゴリID"の形式の文字列を返す.
         *
         * @return 文字列
         */
        public String getRequestSCId() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.serviceId)
                    .append(SEPARATOR)
                    .append(this.categoryId);
            return stringBuilder.toString();
        }
    }

    @Override
    public void recommendCallback(final RecommendChList mRecommendChList) {
        if (mRecommendChList != null
                && mRecommendChList.getmRcList() != null
                && mRecommendChList.getmRcList().size() > 0) {
            sendRecommendChListData(mRecommendChList);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
            mApiDataProviderCallback.recommendNGCallback();
        }
    }

    /**
     * Home画面用データを返却するためのコールバック.
     */
    public interface RecommendApiDataProviderCallback {

        /**
         * おすすめ番組用コールバック.
         *
         * @param recommendContentInfoList コンテンツ情報リスト
         */
        void recommendChannelCallback(List<ContentsData> recommendContentInfoList);

        /**
         * おすすめビデオ用コールバック.
         *
         * @param recommendContentInfoList コンテンツ情報リスト
         */
        void recommendVideoCallback(List<ContentsData> recommendContentInfoList);

        /**
         * おすすめdTV用コールバック.
         *
         * @param recommendContentInfoList コンテンツ情報リスト
         */
        void recommendDTVCallback(List<ContentsData> recommendContentInfoList);

        /**
         * おすすめdアニメ用コールバック.
         *
         * @param recommendContentInfoList コンテンツ情報リスト
         */
        void recommendDAnimeCallback(List<ContentsData> recommendContentInfoList);

        /**
         * おすすめdチャンネル用コールバック.
         *
         * @param recommendContentInfoList コンテンツ情報リスト
         */
        void recommendDChannelCallback(List<ContentsData> recommendContentInfoList);

        /**
         * 0件取得時のコールバック.
         */
        void recommendNGCallback();
    }

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public RecommendDataProvider(final Context mContext) {
        this.mContext = mContext;
        this.mApiDataProviderCallback = (RecommendApiDataProviderCallback) mContext;
    }

    /**
     * コンストラクタ.
     */
    public RecommendDataProvider() {
    }

    /**
     * コンストラクタ.
     *
     * @param mContext コンテキスト
     */
    public void setmContext(final Context mContext) {
        this.mContext = mContext;
    }

    /**
     * コンストラクタ. コールバックを明示指定する場合.
     *
     * @param context                          コンテキスト
     * @param recommendApiDataProviderCallback コールバック
     */
    RecommendDataProvider(final Context context,
                          final RecommendApiDataProviderCallback recommendApiDataProviderCallback) {
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
     * @return レコメンドデータ
     */
    List<ContentsData> getRecommendListDataCache(
            final String cacheDateKey, final int requestPageNo, final int startIndex, final int maxResult) {

        //データ側の情報だけでは、どのタブ向きのデータ化判別しにくくなったので、控えておく
        mRequestPageNo = requestPageNo;

        List<ContentsData> resultList = new ArrayList<>();

        //データ存在チェック
        if (!DBUtils.isCachingRecord(mContext, DBUtils.getRecommendTableName(requestPageNo))) {
            return resultList;
        }

        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(cacheDateKey);

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
     *
     * @param requestPageNo  使用するタブ番号
     * @param startIndex     読み込み初期位置
     * @param maxResult      読み込み最大件数
     * @param hasReturnValue DBにデータがある場合それをすぐ使用するならばtrue。データは常にコールバック経由ならばfalse
     * @return hasReturnValueがtrueでDBにデータがあるならばそれを返す。それ以外はヌルとなる
     */
    public List<ContentsData> startGetRecommendData(final int requestPageNo, final int startIndex,
                                                    final int maxResult, final boolean hasReturnValue) {
        DTVTLogger.debug("requestPageNo:" + requestPageNo);

        //データ側の情報だけでは、どのタブ向きのデータ化判別しにくくなったので、控えておく
        mRequestPageNo = requestPageNo;

        // RequestDataのインスタンス生成
        RecommendRequestData requestData = new RecommendRequestData();

        List<ContentsData> resultList;
        switch (requestPageNo) {
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV: //テレビ
                resultList = getRecommendListDataCache(
                        DateUtils.RECOMMEND_CH_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult && NetWorkUtils.isOnline(mContext)) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getTerebiRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if (!hasReturnValue) {
                        mApiDataProviderCallback.recommendChannelCallback(resultList);
                    }
                    return resultList;
                }
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
                resultList = getRecommendListDataCache(
                        DateUtils.RECOMMEND_VD_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult && NetWorkUtils.isOnline(mContext)) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getVideoRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if (!hasReturnValue) {
                        mApiDataProviderCallback.recommendVideoCallback(resultList);
                    }
                    return resultList;
                }
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
                resultList = getRecommendListDataCache(
                        DateUtils.RECOMMEND_DCHANNEL_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult && NetWorkUtils.isOnline(mContext)) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getDCHRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if (!hasReturnValue) {
                        mApiDataProviderCallback.recommendDChannelCallback(resultList);
                    }
                    return resultList;
                }

                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV: //dTV
                resultList = getRecommendListDataCache(
                        DateUtils.RECOMMEND_DTV_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult && NetWorkUtils.isOnline(mContext)) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getDTVRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if (!hasReturnValue) {
                        mApiDataProviderCallback.recommendDTVCallback(resultList);
                    }
                    return resultList;
                }
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                resultList = getRecommendListDataCache(
                        DateUtils.RECOMMEND_DANIME_LAST_INSERT, requestPageNo, startIndex, maxResult);
                if (resultList.size() < maxResult && NetWorkUtils.isOnline(mContext)) { // キャッシュ内のデータ数が20件未満の場合
                    requestData.serviceCategoryId = getDAnimeRequestSCIdStr();
                } else {
                    //戻り値を使用する指定が無い場合は、コールバックに値を渡す
                    if (!hasReturnValue) {
                        mApiDataProviderCallback.recommendDAnimeCallback(resultList);
                    }
                    return resultList;
                }
                break;
            default:
                break;
        }
        requestData.startIndex = String.valueOf(startIndex);
        requestData.maxResult = String.valueOf(maxResult);
        if (!mIsStop) {
            // サーバへリクエストを送信
            mRecommendWebClient = new RecommendWebClient(this, mContext);
            mRecommendWebClient.getRecommendApi(requestData);
        } else {
            DTVTLogger.error("RecommendDataProvider is stopping connect");
        }
        //戻り値はコールバック任せとなるので、こちらはヌルを返す
        return null;
    }

    /**
     * おすすめ番組データをServiceIdとCategoryIdを元にキャッシュし、Activityに送る.
     *
     * @param recChList おすすめ番組データ
     */
    private void sendRecommendChListData(final RecommendChList recChList) {
        List<Map<String, String>> recList = recChList.getmRcList();
        List<ContentsData> recommendContentInfoList = new ArrayList<>();

        for (Map<String, String> map : recList) {
            recommendContentInfoList.add(setContentsData(map));
        }

        //タブ番号を判定して処理を分ける（ビデオカテゴリーの数が一気に増加したので、データのカテゴリー情報などでは判別しにくくなった）
        switch (mRequestPageNo) {
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV:
                //テレビ用データ
                setStructDB(recChList, DateUtils.RECOMMEND_CH_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV);
                mApiDataProviderCallback.recommendChannelCallback(recommendContentInfoList);
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO:
                //ビデオ用データ
                setStructDB(recChList, DateUtils.RECOMMEND_VD_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO);
                mApiDataProviderCallback.recommendVideoCallback(recommendContentInfoList);
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL:
                //Dチャンネル用データ
                setStructDB(recChList, DateUtils.RECOMMEND_DCHANNEL_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL);
                mApiDataProviderCallback.recommendDChannelCallback(recommendContentInfoList);
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV:
                //dTV用データ
                setStructDB(recChList, DateUtils.RECOMMEND_DTV_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV);
                mApiDataProviderCallback.recommendDTVCallback(recommendContentInfoList);
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME:
            default:
                //dアニメ用データ
                setStructDB(recChList, DateUtils.RECOMMEND_DANIME_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME);
                mApiDataProviderCallback.recommendDAnimeCallback(recommendContentInfoList);
        }
    }

    /**
     * 1行分のコンテンツデータを作成(メソッドの行数が多すぎる警告の回避用).
     *
     * @param map データの作成元
     * @return 作成したデータ
     */
    private ContentsData setContentsData(final Map<String, String> map) {
        ContentsData contentsData = new ContentsData();

        contentsData.setContentsId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID));
        contentsData.setCategoryId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID));
        contentsData.setServiceId(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID));
        contentsData.setThumURL(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1));
        contentsData.setTitle(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_TITLE));
        contentsData.setStartViewing(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING));
        contentsData.setEndViewing(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING));
        contentsData.setReserved1(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED1));
        contentsData.setReserved2(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED2));
        contentsData.setReserved4(map.get(RecommendChannelXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4));
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
    private ClipRequestData setClipResponse(final Map<String, String> map) {
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
     * @param recommendChList レコメンド(TV)データ
     * @param cacheDateKey    データ更新日付
     * @param tagPageNo       ページ番号
     */
    private void setStructDB(final RecommendChList recommendChList, final String cacheDateKey, final int tagPageNo) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(cacheDateKey);
        RecommendListDataManager dataManager = new RecommendListDataManager(mContext);
        dataManager.insertRecommendInsertList(recommendChList, mIsPaging, tagPageNo);
    }

    /**
     * おすすめテレビの取得対象サービスID:カテゴリーID文字列生成.
     *
     * @return 取得対象サービスID:カテゴリーID文字列
     */
    private String getTerebiRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();

        //テレビカテゴリー一覧の数だけループ
        for (int counter = 0; counter < RECOMMEND_CATEGORY_ID_TELEVI.length; counter++) {
            //カテゴリーIDとサービスIDを蓄積
            stringBuilder.append(RECOMMEND_CATEGORY_ID_TELEVI[counter]);

            if (counter != RECOMMEND_CATEGORY_ID_TELEVI.length - 1) {
                //最後のデータ以外はカンマを入れる
                stringBuilder.append(COMMA);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * おすすめビデオの取得対象サービスID:カテゴリーID文字列生成.
     *
     * @return 取得対象サービスID:カテゴリーID文字列
     */
    private String getVideoRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();

        //ビデオカテゴリー一覧の数だけループ
        for (int counter = 0; counter < RECOMMEND_CATEGORY_ID_VIDEO.length; counter++) {
            //カテゴリーIDとサービスIDを蓄積
            stringBuilder.append(RECOMMEND_CATEGORY_ID_VIDEO[counter]);

            if (counter != RECOMMEND_CATEGORY_ID_VIDEO.length - 1) {
                //最後のデータ以外はカンマを入れる
                stringBuilder.append(COMMA);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * おすすめdチャンネルの取得対象サービスID:カテゴリーID文字列生成.
     *
     * @return 取得対象サービスID:カテゴリーID文字列
     */
    private String getDCHRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();

        //ビデオカテゴリー一覧の数だけループ
        for (int counter = 0; counter < RECOMMEND_CATEGORY_ID_DTV_CHANNEL.length; counter++) {
            //カテゴリーIDとサービスIDを蓄積
            stringBuilder.append(RECOMMEND_CATEGORY_ID_DTV_CHANNEL[counter]);

            if (counter != RECOMMEND_CATEGORY_ID_DTV_CHANNEL.length - 1) {
                //最後のデータ以外はカンマを入れる
                stringBuilder.append(COMMA);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * おすすめdTVの取得対象サービスID:カテゴリーID文字列生成.
     *
     * @return 取得対象サービスID:カテゴリーID文字列
     */
    private String getDTVRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();

        //ビデオカテゴリー一覧の数だけループ
        for (int counter = 0; counter < RECOMMEND_CATEGORY_ID_DTV.length; counter++) {
            //カテゴリーIDとサービスIDを蓄積
            stringBuilder.append(RECOMMEND_CATEGORY_ID_DTV[counter]);

            if (counter != RECOMMEND_CATEGORY_ID_DTV.length - 1) {
                //最後のデータ以外はカンマを入れる
                stringBuilder.append(COMMA);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * おすすめdアニメの取得対象サービスID:カテゴリーID文字列生成.
     *
     * @return 取得対象サービスID:カテゴリーID文字列
     */
    private String getDAnimeRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RecommendRequestId.DANIME.getRequestSCId());

        return stringBuilder.toString();
    }
    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsStop = true;
        if (mRecommendWebClient != null) {
            mRecommendWebClient.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsStop = false;
        if (mRecommendWebClient != null) {
            mRecommendWebClient.enableConnection();
        }
    }

    @Override
    public void onDbOperationFinished(final boolean isSuccessful, final List<Map<String, String>> resultSet, final int operationId) {
        //強制オーバーライド
    }

    @Override
    public List<Map<String, String>> dbOperation(final int operationId) {
        return null;
    }
}
