/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DbThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendRequestData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendChannelXmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * レコメンド用データプロバイダ.
 */
public class RecommendDataProvider implements RecommendWebClient.RecommendCallback, DbThread.DbOperation {

    /**
     * コンテキスト.
     */
    private Context mContext = null;
    /**
     * カンマ区切り用.
     */
    private static final String COMMA = ",";
    /**
     * コロン区切り用.
     */
    private static final String SEPARATOR = ":";

    /**
     * ページング判定.
     */
    private boolean mIsPaging = false;

    /**
     * コールバック.
     */
    private RecommendApiDataProviderCallback mApiDataProviderCallback = null;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsStop = false;

    //TODO 検討中
//    private String mCacheDateKey = null;
//    private int mTagPageNo = -1;
//    private RecommendChList mRecommendChList = null;

    /**
     * CHレコメンドデータキャッシュ取得用.
     */
    private static final int SELECT_RECOMMEND_CHANNEL_LIST = 0;
    /**
     * Vodレコメンドデータキャッシュ取得用.
     */
    private static final int SELECT_RECOMMEND_VOD_LIST = 1;
    /**
     * Dtvチャンネルレコメンドデータキャッシュ取得用.
     */
    private static final int SELECT_RECOMMEND_DTV_CHANNEL_LIST = 2;
    /**
     * Dtvレコメンドデータキャッシュ取得用.
     */
    private static final int SELECT_RECOMMEND_DTV_LIST = 3;
    /**
     * Dアニメデータキャッシュ取得用.
     */
    private static final int SELECT_RECOMMEND_D_ANIMATION_LIST = 4;
    /**
     * レコメンドコンテンツ最大件数（システム制約）.
     */
    private static final int MAX_SHOW_LIST_SIZE = 100;
    /**
     * レコメンドコンテンツ取得位置（システム制約）.
     */
    private static final int RECOMMEND_START_INDEX = 1;

    /**
     * RecommendWebClient.
     */
    private RecommendWebClient mRecommendWebClient = null;
    /**
     * RecommendWebClient(番組).
     */
    private RecommendWebClient mVodWebClient = null;
    /**
     * RecommendWebClient(テレビ).
     */
    private RecommendWebClient mTvWebClient = null;
    /**
     * API_INDEX_TV(ホーム).
     */
    public static final int API_INDEX_TV_HOME = -1;
    /**
     * API_INDEX_TV.
     */
    public static final int API_INDEX_TV = 1;
    /**
     * API_INDEX(その他).
     */
    public static final int API_INDEX_OTHER = 2;
    /**
     * テレビカテゴリー一覧（dTVチャンネル　VOD（見逃し）が無くなった等の新情報を反映）.
     */
    private final String[] RECOMMEND_CATEGORY_ID_TELEVI = {
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
         *
         * @return サービスID
         */
        public String getServiceId() {
            return this.serviceId;
        }

        /**
         * カテゴリーIDのゲッター.
         *
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
            return StringUtils.getConnectString(new String[] {this.serviceId, SEPARATOR, this.categoryId});
        }
    }

    /**
     * 検レコ取得パラメータ サービス分類.
     */
    private enum RESP_DATA_SERVICE_TYPE {
        /**
         * ひかりTV_テレビ.
         */
        HIKARI_TV,
        /**
         * ひかりTV_ビデオ.
         */
        HIKARI_VOD,
        /**
         * dTV.
         */
        DTV,
        /**
         * dチャンネル.
         */
        DCHANNEL,
        /**
         * dアニメ.
         */
        DANIME,
        /**
         * 判別不能.
         * ひかりTV_テレビ と dチャンネル は一部同じリクエストを送信するため判定できない
         */
        UNKNOWN,
    }

    @Override
    public void recommendCallback(final RecommendChList mRecommendChList) {
        if (mRecommendChList != null
                && mRecommendChList.getmRcList() != null) {
            sendRecommendChListData(mRecommendChList);
        } else {
            //TODO WEBAPIを取得できなかった時の処理を記載予定
            if (!mIsStop) {
                mApiDataProviderCallback.recommendNGCallback();
            }
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
     * 通信エラーメッセージを取得する.
     * @param apiIndex API区別インデックス
     */
    public ErrorState getError(final int apiIndex) {
        ErrorState errorState = null;
        //テレビ(ホーム用)
        if (apiIndex == API_INDEX_TV_HOME) {
            if (mTvWebClient != null) {
                errorState = mTvWebClient.getError();
            }
        //ビデオ
        } else if (apiIndex == API_INDEX_TV) {
            if (mVodWebClient != null) {
                errorState = mVodWebClient.getError();
            }
        //テレビ、dTV、dTVチャンネル、dアニメストア
        } else {
            if (mRecommendWebClient != null) {
                errorState = mRecommendWebClient.getError();
            }
        }
        return errorState;
    }

    /**
     * おすすめテレビ取得.
     */
    void getHomeTvRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_CH_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.maxResult = String.valueOf(MAX_SHOW_LIST_SIZE);
                requestData.startIndex = String.valueOf(RECOMMEND_START_INDEX);
                requestData.serviceCategoryId = getTerebiRequestSCIdStr();
                // サーバへリクエストを送信
                mTvWebClient = new RecommendWebClient(this, mContext);
                mTvWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DbThread t = new DbThread(handler, this, SELECT_RECOMMEND_CHANNEL_LIST);
            t.start();
            //キャッシュ取得時はおすすめビデオ取得が呼び出されないため、ここで呼び出す
            getVodRecommend();
        }
    }

    /**
     * おすすめテレビ取得.
     */
    private void getTvRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_CH_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.maxResult = String.valueOf(MAX_SHOW_LIST_SIZE);
                requestData.startIndex = String.valueOf(RECOMMEND_START_INDEX);
                requestData.serviceCategoryId = getTerebiRequestSCIdStr();
                // サーバへリクエストを送信
                mRecommendWebClient = new RecommendWebClient(this, mContext);
                mRecommendWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DbThread t = new DbThread(handler, this, SELECT_RECOMMEND_CHANNEL_LIST);
            t.start();

        }
    }

    /**
     * おすすめビデオ取得.
     */
    void getVodRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_VD_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.maxResult = String.valueOf(MAX_SHOW_LIST_SIZE);
                requestData.startIndex = String.valueOf(RECOMMEND_START_INDEX);
                requestData.serviceCategoryId = getVideoRequestSCIdStr();
                // サーバへリクエストを送信
                mVodWebClient = new RecommendWebClient(this, mContext);
                mVodWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DbThread t = new DbThread(handler, this, SELECT_RECOMMEND_VOD_LIST);
            t.start();
        }
    }

    /**
     * おすすめDTVチャンネル取得.
     */
    void getDtvChRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_DCHANNEL_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.maxResult = String.valueOf(MAX_SHOW_LIST_SIZE);
                requestData.startIndex = String.valueOf(RECOMMEND_START_INDEX);
                requestData.serviceCategoryId = getDCHRequestSCIdStr();
                // サーバへリクエストを送信
                mRecommendWebClient = new RecommendWebClient(this, mContext);
                mRecommendWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DbThread t = new DbThread(handler, this, SELECT_RECOMMEND_DTV_CHANNEL_LIST);
            t.start();
        }
    }

    /**
     * おすすめDTV取得.
     */
    void getDtvRecommend() {

        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_DTV_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.maxResult = String.valueOf(MAX_SHOW_LIST_SIZE);
                requestData.startIndex = String.valueOf(RECOMMEND_START_INDEX);
                requestData.serviceCategoryId = getDTVRequestSCIdStr();
                // サーバへリクエストを送信
                mRecommendWebClient = new RecommendWebClient(this, mContext);
                mRecommendWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DbThread t = new DbThread(handler, this, SELECT_RECOMMEND_DTV_LIST);
            t.start();
        }
    }

    /**
     * おすすめDアニメ取得.
     */
    void getDAnimationRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_DANIME_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.maxResult = String.valueOf(MAX_SHOW_LIST_SIZE);
                requestData.startIndex = String.valueOf(RECOMMEND_START_INDEX);
                requestData.serviceCategoryId = getDAnimeRequestSCIdStr();
                // サーバへリクエストを送信
                mRecommendWebClient = new RecommendWebClient(this, mContext);
                mRecommendWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DbThread t = new DbThread(handler, this, SELECT_RECOMMEND_D_ANIMATION_LIST);
            t.start();
        }
    }

    /**
     * Activityからのデータ取得要求受付.
     *
     * @param requestPageNo  使用するタブ番号
     */
    public void startGetRecommendData(final int requestPageNo) {
        DTVTLogger.debug("requestPageNo:" + requestPageNo);

        switch (requestPageNo) {
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV: //テレビ
                getTvRecommend();
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
                getVodRecommend();
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
                getDtvChRecommend();
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV: //dTV
                getDtvRecommend();
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                getDAnimationRecommend();
                break;
            default:
                break;
        }
    }

    /**
     * おすすめ番組データをServiceIdとCategoryIdを元にキャッシュし、Activityに送る.
     *
     * @param recChList おすすめ番組データ
     */
    @SuppressWarnings("EnumSwitchStatementWhichMissesCases")
    private void sendRecommendChListData(final RecommendChList recChList) {
        List<Map<String, String>> recList = recChList.getmRcList();
        List<ContentsData> recommendContentInfoList = new ArrayList<>();
        RESP_DATA_SERVICE_TYPE respDataServiceType = RESP_DATA_SERVICE_TYPE.UNKNOWN;

        for (Map<String, String> map : recList) {
            ContentsData data;
            data = setContentsData(map);
            recommendContentInfoList.add(data);
            if (respDataServiceType == RESP_DATA_SERVICE_TYPE.UNKNOWN) {
                // レスポンスデータのサービスが確定するまで判定を続ける
                respDataServiceType = decisionResponseDataType(data.getServiceId(), data.getCategoryId());
            }
        }

        switch (respDataServiceType) {
            case HIKARI_TV:
                //テレビ用データ
                setStructDB(recChList, DateUtils.RECOMMEND_CH_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV);
                mApiDataProviderCallback.recommendChannelCallback(recommendContentInfoList);
                break;
            case HIKARI_VOD:
                //ビデオ用データ
                setStructDB(recChList, DateUtils.RECOMMEND_VD_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO);
                mApiDataProviderCallback.recommendVideoCallback(recommendContentInfoList);
                break;
            case DCHANNEL:
                //Dチャンネル用データ
                setStructDB(recChList, DateUtils.RECOMMEND_DCHANNEL_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL);
                mApiDataProviderCallback.recommendDChannelCallback(recommendContentInfoList);
                break;
            case DTV:
                //dTV用データ
                setStructDB(recChList, DateUtils.RECOMMEND_DTV_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV);
                mApiDataProviderCallback.recommendDTVCallback(recommendContentInfoList);
                break;
            case DANIME:
                //dアニメ用データ
                setStructDB(recChList, DateUtils.RECOMMEND_DANIME_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME);
                mApiDataProviderCallback.recommendDAnimeCallback(recommendContentInfoList);
                break;
            default:
                // 判定不能データ
                if (!mIsStop) {
                    mApiDataProviderCallback.recommendNGCallback();
                }
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
        //TODO レスポンスがないため、ダミーデータを設定
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
        RecommendListDataManager dataManager = new RecommendListDataManager(mContext);
        dataManager.insertRecommendInsertList(recommendChList, mIsPaging, tagPageNo, cacheDateKey);
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
        return RecommendRequestId.DANIME.getRequestSCId();
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
        if (mVodWebClient != null) {
            mVodWebClient.stopConnection();
        }
        if (mTvWebClient != null) {
            mTvWebClient.stopConnection();
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
        if (mVodWebClient != null) {
            mVodWebClient.enableConnection();
        }
        if (mTvWebClient != null) {
            mTvWebClient.enableConnection();
        }
    }

    /**
     * レスポンスデータからサービスの種別を取得する.
     * @param serviceId サービスID
     * @param categoryId カテゴリID
     * @return サービス種別
     */
    private RESP_DATA_SERVICE_TYPE decisionResponseDataType(final String serviceId, final String categoryId) {
        DTVTLogger.start("ServiceId : " + serviceId + " CategoryId : " + categoryId);
        RESP_DATA_SERVICE_TYPE result;

        if (RecommendRequestId.HIKARITV_DOCOMO_IPTV.getServiceId().equals(serviceId)) {
            // ServiceId == 44
            if (RecommendRequestId.HIKARITV_DOCOMO_IPTV.getCategoryId().equals(categoryId)
                    || RecommendRequestId.HIKARITV_DOCOMO_DTV_BLOADCAST.getCategoryId().equals(categoryId)) {
                // CategoryId == 03 or 04
                result = RESP_DATA_SERVICE_TYPE.HIKARI_TV;
            } else {
                result = RESP_DATA_SERVICE_TYPE.HIKARI_VOD;
            }
        } else if (RecommendRequestId.DANIME.getServiceId().equals(serviceId)) {
            // ServiceId == 17
            result = RESP_DATA_SERVICE_TYPE.DANIME;
        } else if (RecommendRequestId.DTV_SVOD.getServiceId().equals(serviceId)) {
            // ServiceId == 15
            result = RESP_DATA_SERVICE_TYPE.DTV;
        } else if (RecommendRequestId.DTVCHANNEL_RELATION.getServiceId().equals(serviceId)) {
            // ServiceId == 43
            result = RESP_DATA_SERVICE_TYPE.DCHANNEL;
        } else {
            // DAZNの判定を行う予定
            result = RESP_DATA_SERVICE_TYPE.UNKNOWN;
        }

        DTVTLogger.end();
        return result;
    }

    @Override
    public void onDbOperationFinished(final boolean isSuccessful, final List<Map<String, String>> resultSet, final int operationId) {
        //強制オーバーライド
    }

    @Override
    public List<Map<String, String>> dbOperation(final int operationId) {
        RecommendListDataManager recommendDataManager;
        List<ContentsData> resultList;
        switch (operationId) {
            case SELECT_RECOMMEND_CHANNEL_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV);
                mApiDataProviderCallback.recommendChannelCallback(resultList);
                break;
            case SELECT_RECOMMEND_VOD_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO);
                mApiDataProviderCallback.recommendVideoCallback(resultList);
                break;
            case SELECT_RECOMMEND_DTV_CHANNEL_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL);
                mApiDataProviderCallback.recommendDChannelCallback(resultList);
                break;
            case SELECT_RECOMMEND_DTV_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV);
                mApiDataProviderCallback.recommendDTVCallback(resultList);
                break;
            case SELECT_RECOMMEND_D_ANIMATION_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME);
                mApiDataProviderCallback.recommendDAnimeCallback(resultList);
                break;
            default:
                break;
        }
        return null;
    }
}
