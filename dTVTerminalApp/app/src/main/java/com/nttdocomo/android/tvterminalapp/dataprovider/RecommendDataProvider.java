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
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecommendChannelList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendRequestData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.RecommendWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.xmlparser.RecommendWebXmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * レコメンド用データプロバイダ.
 */
public class RecommendDataProvider implements RecommendWebClient.RecommendCallback, DataBaseThread.DataBaseOperation {

    /** コンテキスト. */
    private Context mContext = null;
    /** カンマ区切り用. */
    private static final String COMMA = ",";
    /** コロン区切り用. */
    private static final String SEPARATOR = ":";

    /** コールバック. */
    private RecommendApiDataProviderCallback mApiDataProviderCallback = null;
    /** 通信禁止判定フラグ. */
    private boolean mIsStop = false;

    /** CHレコメンドデータキャッシュ取得用. */
    private static final int SELECT_RECOMMEND_CHANNEL_LIST = 0;
    /** Vodレコメンドデータキャッシュ取得用. */
    private static final int SELECT_RECOMMEND_VOD_LIST = 1;
    /** Dtvチャンネルレコメンドデータキャッシュ取得用. */
    private static final int SELECT_RECOMMEND_DTV_CHANNEL_LIST = 2;
    /** Dtvレコメンドデータキャッシュ取得用. */
    private static final int SELECT_RECOMMEND_DTV_LIST = 3;
    /** Dアニメデータキャッシュ取得用. */
    private static final int SELECT_RECOMMEND_D_ANIMATION_LIST = 4;
    /** ホームのおすすめ番組レコメンドデータキャッシュ取得用. */
    private static final int SELECT_RECOMMEND_HOME_CHANNEL_LIST = 5;
    /** ホームのおすすめビデオレコメンドデータキャッシュ取得用. */
    private static final int SELECT_RECOMMEND_HOME_VOD_LIST = 6;
    /** ホームのおすすめ番組レコメンドデータキャッシュ保存用. */
    private static final int INSERT_RECOMMEND_HOME_TV_LIST = 7;
    /** ホームのおすすめビデオレコメンドデータキャッシュ保存用. */
    private static final int INSERT_RECOMMEND_HOME_VOD_LIST = 8;
    /** CHレコメンドデータキャッシュ保存用. */
    private static final int INSERT_RECOMMEND_CHANNEL_LIST = 9;
    /** Vodレコメンドデータキャッシュ保存用. */
    private static final int INSERT_RECOMMEND_VOD_LIST = 10;
    /** Dtvチャンネルレコメンドデータキャッシュ保存用. */
    private static final int INSERT_RECOMMEND_DTV_CHANNEL_LIST = 11;
    /** Dtvレコメンドデータキャッシュ保存用. */
    private static final int INSERT_RECOMMEND_DTV_LIST = 12;
    /** Dアニメデータキャッシュ保存用. */
    private static final int INSERT_RECOMMEND_D_ANIMATION_LIST = 13;
    /** DAZNデータキャッシュ取得用. */
    private static final int SELECT_RECOMMEND_DAZN_LIST = 14;
    /** DAZNデータキャッシュ保存用. */
    private static final int INSERT_RECOMMEND_DAZN_LIST = 15;
    /** レコメンドコンテンツ最大件数（システム制約）. */
    private static final int MAX_SHOW_LIST_SIZE = 100;
    /** レコメンドコンテンツ取得位置（システム制約）. */
    private static final int RECOMMEND_START_INDEX = 1;
    /** ページID ホーム 番組. */
    private static final String RECOMMEND_PAGE_ID_HOME_PROGRAM = "107";
    /** ページID ホーム VOD. */
    private static final String RECOMMEND_PAGE_ID_HOME_VOD = "108";
    /** ページID ホーム ひかりTV番組. */
    private static final String RECOMMEND_PAGE_ID_HIKARI_PROGRAM = "109";
    /** ページID ホーム ひかりVOD. */
    private static final String RECOMMEND_PAGE_ID_HIKARI_VOD = "110";
    /** ページID ホーム DTV. */
    private static final String RECOMMEND_PAGE_ID_DTV = "111";
    /** ページID ホーム DCH. */
    private static final String RECOMMEND_PAGE_ID_DTVCHANNEL = "112";
    /** ページID ホーム Dアニメ. */
    private static final String RECOMMEND_PAGE_ID_DANIME = "113";
    /** ページID ホーム DAZN. */
    private static final String RECOMMEND_PAGE_ID_DAZN = "115";

    /** RecommendWebClient. */
    private RecommendWebClient mRecommendWebClient = null;
    /** RecommendWebClient(番組). */
    private RecommendWebClient mVodWebClient = null;
    /** RecommendWebClient(テレビ). */
    private RecommendWebClient mTvWebClient = null;
    /** API_INDEX_TV(ホーム). */
    public static final int API_INDEX_TV_HOME = -1;
    /** API_INDEX_TV. */
    public static final int API_INDEX_TV = 1;
    /** API_INDEX(その他). */
    public static final int API_INDEX_OTHER = 2;
    /** ホームおすすめ番組カテゴリー一覧. */
    private final String[] RECOMMEND_CATEGORY_ID_HOME_TELEVI = {
            RecommendRequestId.HIKARITV_DOCOMO_TTB.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_BS.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_IPTV.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_DTVCHANNEL_BLOADCAST.getRequestSCId(),
            RecommendRequestId.DTVCHANNEL_BLOADCAST.getRequestSCId(),
            RecommendRequestId.DAZN.getRequestSCId()
    };
    /** テレビカテゴリー一覧（dTVチャンネル　VOD（見逃し）が無くなった等の新情報を反映）. */
    private final String[] RECOMMEND_CATEGORY_ID_TELEVI = {
            RecommendRequestId.HIKARITV_DOCOMO_TTB.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_BS.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_IPTV.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_DTVCHANNEL_BLOADCAST.getRequestSCId(),
    };

    /** ホームおすすめビデオカテゴリー一覧. */
    private final String[] RECOMMEND_CATEGORY_ID_HOME_VIDEO = {
            RecommendRequestId.HIKARITV_DOCOMO_DTVCHANNEL_MISS.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_DTVCHANNEL_RELATION.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_DTV_SVOD.getRequestSCId(),
            RecommendRequestId.DTV_SVOD.getRequestSCId(),
            RecommendRequestId.DTV_TVOD.getRequestSCId(),
            RecommendRequestId.DTVCHANNEL_MISS.getRequestSCId(),
            RecommendRequestId.DTVCHANNEL_RELATION.getRequestSCId(),
            RecommendRequestId.DANIME.getRequestSCId(),
    };
    /** ビデオカテゴリー一覧（dTVチャンネル　VOD（見逃し）が追加された等の新情報を反映）. */
    private final String[] RECOMMEND_CATEGORY_ID_VIDEO = {
            RecommendRequestId.HIKARITV_DOCOMO_DTVCHANNEL_MISS.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_DTVCHANNEL_RELATION.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_HIKARITV_VOD.getRequestSCId(),
            RecommendRequestId.HIKARITV_DOCOMO_DTV_SVOD.getRequestSCId(),
    };

    /** dTVカテゴリー一覧. */
    private final String[] RECOMMEND_CATEGORY_ID_DTV = {
            RecommendRequestId.DTV_SVOD.getRequestSCId(),
            RecommendRequestId.DTV_TVOD.getRequestSCId(),
    };

    /** dTVチャンネルカテゴリー一覧. */
    private final String[] RECOMMEND_CATEGORY_ID_DTV_CHANNEL = {
            RecommendRequestId.DTVCHANNEL_BLOADCAST.getRequestSCId(),
            RecommendRequestId.DTVCHANNEL_MISS.getRequestSCId(),
            RecommendRequestId.DTVCHANNEL_RELATION.getRequestSCId(),
    };

    /**
     * 取得対象サービスID:カテゴリーID.
     */
    public enum RecommendRequestId {
        /**dTV SVOD.*/
        DTV_SVOD("15", "01"),
        /**dTV TVOD.*/
        DTV_TVOD("15", "02"),
        /**dアニメストア.*/
        DANIME("17", "01"),
        /**DAZN.*/
        DAZN("35", "02"),
        /**dTVチャンネル　放送.*/
        DTVCHANNEL_BLOADCAST("43", "01"),
        /**dTVチャンネル　VOD（見逃し）.*/
        DTVCHANNEL_MISS("43", "02"),
        /**dTVチャンネル　VOD（関連番組）.*/
        DTVCHANNEL_RELATION("43", "03"),
        /**地デジ.*/
        HIKARITV_DOCOMO_TTB("44", "01"),
        /**BS.*/
        HIKARITV_DOCOMO_BS("44", "02"),
        /**IPTV.*/
        HIKARITV_DOCOMO_IPTV("44", "03"),
        /**dTVチャンネル　放送.*/
        HIKARITV_DOCOMO_DTVCHANNEL_BLOADCAST("44", "04"),
        /**dTVチャンネル　VOD（見逃し）.*/
        HIKARITV_DOCOMO_DTVCHANNEL_MISS("44", "05"),
        /**dTVチャンネル　VOD（関連番組）.*/
        HIKARITV_DOCOMO_DTVCHANNEL_RELATION("44", "06"),
        /**ひかりTV VOD.*/
        HIKARITV_DOCOMO_HIKARITV_VOD("44", "08"),
        /**dTV SVOD.*/
        HIKARITV_DOCOMO_DTV_SVOD("44", "10"),;
        /**サービスID.*/
        private final String mServiceId;
        /**カテゴリーID.*/
        private final String mCategoryId;

        /**
         * 定数をENUMで蓄積するメソッド.
         *
         * @param serviceIdSource  元になるサービスID
         * @param categoryIdSource 元になるカテゴリーID
         */
        RecommendRequestId(final String serviceIdSource, final String categoryIdSource) {
            this.mServiceId = serviceIdSource;
            this.mCategoryId = categoryIdSource;
        }

        /**
         * サービスIDのゲッター.
         *
         * @return サービスID
         */
        public String getServiceId() {
            return this.mServiceId;
        }

        /**
         * カテゴリーIDのゲッター.
         *
         * @return カテゴリーID
         */
        public String getCategoryId() {
            return this.mCategoryId;
        }

        /**
         * "サービスID:カテゴリID"の形式の文字列を返す.
         *
         * @return 文字列
         */
        public String getRequestSCId() {
            return StringUtils.getConnectString(new String[] {this.mServiceId, SEPARATOR, this.mCategoryId});
        }
    }

    /**
     * 検レコ取得パラメータ サービス分類.
     */
    private enum RESP_DATA_SERVICE_TYPE {
        /** ホーム_おすすめ番組. */
        HOME_TV,
        /** ホームおすすめビデオ. */
        HOME_VOD,
        /** ひかりTV_テレビ. */
        HIKARI_TV,
        /** ひかりTV_ビデオ. */
        HIKARI_VOD,
        /** dTV. */
        DTV,
        /** dチャンネル. */
        DCHANNEL,
        /** dアニメ. */
        DANIME,
        /** DAZN. */
        DAZN,
        /** 判別不能 ひかりTV_テレビ と dチャンネル は一部同じリクエストを送信するため判定できない. */
        UNKNOWN,
    }

    @Override
    public void recommendCallback(final RecommendChannelList recommendChannelList) {
        if (recommendChannelList != null
                && recommendChannelList.getmRcList() != null) {
            sendRecommendChListData(recommendChannelList);
        } else {
            if (!mIsStop && mApiDataProviderCallback != null) {
                mApiDataProviderCallback.recommendNGCallback();
            }
        }
    }

    /**
     * Home画面用データを返却するためのコールバック.
     */
    public interface RecommendApiDataProviderCallback {
        /**
         * ホームのおすすめ番組用コールバック.
         *
         * @param recommendContentInfoList コンテンツ情報リスト
         */
        void recommendHomeChannelCallback(List<ContentsData> recommendContentInfoList);

        /**
         * ホームのおすすめビデオ用コールバック.
         *
         * @param recommendContentInfoList コンテンツ情報リスト
         */
        void recommendHomeVideoCallback(List<ContentsData> recommendContentInfoList);

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
         * おすすめDAZN用コールバック.
         *
         * @param recommendContentInfoList コンテンツ情報リスト
         */
        void recommendDAZNCallback(List<ContentsData> recommendContentInfoList);

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
        this.mContext = mContext.getApplicationContext();
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
     * @return エラーステータス
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
     * ホームのおすすめ番組取得.
     */
    void getHomeTvRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_HOME_CH_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.setMaxResult(String.valueOf(MAX_SHOW_LIST_SIZE));
                requestData.setStartIndex(String.valueOf(RECOMMEND_START_INDEX));
                requestData.setServiceCategoryId(getHomeTerebiRequestSCIdStr());
                requestData.setPageId(RECOMMEND_PAGE_ID_HOME_PROGRAM);
                // サーバへリクエストを送信
                mTvWebClient = new RecommendWebClient(this, mContext, SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_TV);
                mTvWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread t = new DataBaseThread(handler, this, SELECT_RECOMMEND_HOME_CHANNEL_LIST);
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
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.setMaxResult(String.valueOf(MAX_SHOW_LIST_SIZE));
                requestData.setStartIndex(String.valueOf(RECOMMEND_START_INDEX));
                requestData.setServiceCategoryId(getTerebiRequestSCIdStr());
                requestData.setPageId(RECOMMEND_PAGE_ID_HIKARI_PROGRAM);
                // サーバへリクエストを送信
                mRecommendWebClient = new RecommendWebClient(this, mContext, SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV);
                mRecommendWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread t = new DataBaseThread(handler, this, SELECT_RECOMMEND_CHANNEL_LIST);
            t.start();

        }
    }

    /**
     * ホームのおすすめビデオ取得.
     */
    void getHomeVodRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_HOME_VD_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.setMaxResult(String.valueOf(MAX_SHOW_LIST_SIZE));
                requestData.setStartIndex(String.valueOf(RECOMMEND_START_INDEX));
                requestData.setServiceCategoryId(getHomeVideoRequestSCIdStr());
                requestData.setPageId(RECOMMEND_PAGE_ID_HOME_VOD);
                // サーバへリクエストを送信
                mVodWebClient = new RecommendWebClient(this, mContext, SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_VIDEO);
                mVodWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread t = new DataBaseThread(handler, this, SELECT_RECOMMEND_HOME_VOD_LIST);
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
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.setMaxResult(String.valueOf(MAX_SHOW_LIST_SIZE));
                requestData.setStartIndex(String.valueOf(RECOMMEND_START_INDEX));
                requestData.setServiceCategoryId(getVideoRequestSCIdStr());
                requestData.setPageId(RECOMMEND_PAGE_ID_HIKARI_VOD);
                // サーバへリクエストを送信
                mVodWebClient = new RecommendWebClient(this, mContext, SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO);
                mVodWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread t = new DataBaseThread(handler, this, SELECT_RECOMMEND_VOD_LIST);
            t.start();
        }
    }

    /**
     * おすすめDTVチャンネル取得.
     */
    private void getDtvChRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_DCHANNEL_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.setMaxResult(String.valueOf(MAX_SHOW_LIST_SIZE));
                requestData.setStartIndex(String.valueOf(RECOMMEND_START_INDEX));
                requestData.setServiceCategoryId(getDCHRequestSCIdStr());
                requestData.setPageId(RECOMMEND_PAGE_ID_DTVCHANNEL);
                // サーバへリクエストを送信
                mRecommendWebClient = new RecommendWebClient(this, mContext, SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL);
                mRecommendWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread t = new DataBaseThread(handler, this, SELECT_RECOMMEND_DTV_CHANNEL_LIST);
            t.start();
        }
    }

    /**
     * おすすめDTV取得.
     */
    private void getDtvRecommend() {

        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_DTV_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.setMaxResult(String.valueOf(MAX_SHOW_LIST_SIZE));
                requestData.setStartIndex(String.valueOf(RECOMMEND_START_INDEX));
                requestData.setServiceCategoryId(getDTVRequestSCIdStr());
                requestData.setPageId(RECOMMEND_PAGE_ID_DTV);
                // サーバへリクエストを送信
                mRecommendWebClient = new RecommendWebClient(this, mContext, SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV);
                mRecommendWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread t = new DataBaseThread(handler, this, SELECT_RECOMMEND_DTV_LIST);
            t.start();
        }
    }

    /**
     * おすすめDアニメ取得.
     */
    private void getDAnimationRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_DANIME_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.setMaxResult(String.valueOf(MAX_SHOW_LIST_SIZE));
                requestData.setStartIndex(String.valueOf(RECOMMEND_START_INDEX));
                requestData.setServiceCategoryId(getDAnimeRequestSCIdStr());
                requestData.setPageId(RECOMMEND_PAGE_ID_DANIME);
                // サーバへリクエストを送信
                mRecommendWebClient = new RecommendWebClient(this, mContext, SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME);
                mRecommendWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread t = new DataBaseThread(handler, this, SELECT_RECOMMEND_D_ANIMATION_LIST);
            t.start();
        }
    }

    /**
     * おすすめDAZN取得.
     */
    private void getDAZNRecommend() {
        //DB保存履歴と、有効期間を確認
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RECOMMEND_DAZN_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeLimitDate(lastDate))) {
            if (!mIsStop) {
                // RequestDataのインスタンス生成
                RecommendRequestData requestData = new RecommendRequestData();
                requestData.setMaxResult(String.valueOf(MAX_SHOW_LIST_SIZE));
                requestData.setStartIndex(String.valueOf(RECOMMEND_START_INDEX));
                requestData.setServiceCategoryId(getDAZNRequestSCIdStr());
                requestData.setPageId(RECOMMEND_PAGE_ID_DAZN);
                // サーバへリクエストを送信
                mRecommendWebClient = new RecommendWebClient(this, mContext, SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DAZN);
                mRecommendWebClient.getRecommendApi(requestData);
            } else {
                DTVTLogger.error("RecommendDataProvider is stopping connect");
            }
        } else {
            //DBキャッシュ取得
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread t = new DataBaseThread(handler, this, SELECT_RECOMMEND_DAZN_LIST);
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
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_TV: //テレビ
                getHomeTvRecommend();
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_VIDEO: //ビデオ
                getHomeVodRecommend();
                break;
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
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DAZN: //DAZN
                getDAZNRecommend();
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
    @SuppressWarnings({"EnumSwitchStatementWhichMissesCases", "OverlyLongMethod"})
    private void sendRecommendChListData(final RecommendChannelList recChList) {
        List<Map<String, String>> recList = recChList.getmRcList();
        List<ContentsData> recommendContentInfoList = new ArrayList<>();
        RESP_DATA_SERVICE_TYPE respDataServiceType = decisionResponseDataType(recChList);

        for (Map<String, String> map : recList) {
            ContentsData data;
            data = setContentsData(map);
            recommendContentInfoList.add(data);
        }

        Handler handler;
        DataBaseThread thread;
        switch (respDataServiceType) {
            case HOME_TV:
                //ホームのおすすめ番組用データ
                //DBキャッシュ取得
                handler = new Handler(Looper.getMainLooper());
                thread = new DataBaseThread(handler, this, INSERT_RECOMMEND_HOME_TV_LIST);
                thread.setRecommendChannelList(recChList);
                thread.setContentsDataList(recommendContentInfoList);
                thread.start();
                break;
            case HOME_VOD:
                //ホームのおすすめビデオ用データ
                //DBキャッシュ取得
                handler = new Handler(Looper.getMainLooper());
                thread = new DataBaseThread(handler, this, INSERT_RECOMMEND_HOME_VOD_LIST);
                thread.setRecommendChannelList(recChList);
                thread.setContentsDataList(recommendContentInfoList);
                thread.start();
                break;
            case HIKARI_TV:
                //テレビ用データ
                //DBキャッシュ取得
                handler = new Handler(Looper.getMainLooper());
                thread = new DataBaseThread(handler, this, INSERT_RECOMMEND_CHANNEL_LIST);
                thread.setRecommendChannelList(recChList);
                thread.setContentsDataList(recommendContentInfoList);
                thread.start();
                break;
            case HIKARI_VOD:
                //ビデオ用データ
                //DBキャッシュ取得
                handler = new Handler(Looper.getMainLooper());
                thread = new DataBaseThread(handler, this, INSERT_RECOMMEND_VOD_LIST);
                thread.setRecommendChannelList(recChList);
                thread.setContentsDataList(recommendContentInfoList);
                thread.start();
                break;
            case DCHANNEL:
                //Dチャンネル用データ
                //DBキャッシュ取得
                handler = new Handler(Looper.getMainLooper());
                thread = new DataBaseThread(handler, this, INSERT_RECOMMEND_DTV_CHANNEL_LIST);
                thread.setRecommendChannelList(recChList);
                thread.setContentsDataList(recommendContentInfoList);
                thread.start();
                break;
            case DTV:
                //dTV用データ
                //DBキャッシュ取得
                handler = new Handler(Looper.getMainLooper());
                thread = new DataBaseThread(handler, this, INSERT_RECOMMEND_DTV_LIST);
                thread.setRecommendChannelList(recChList);
                thread.setContentsDataList(recommendContentInfoList);
                thread.start();
                break;
            case DANIME:
                //dアニメ用データ
                //DBキャッシュ取得
                handler = new Handler(Looper.getMainLooper());
                thread = new DataBaseThread(handler, this, INSERT_RECOMMEND_D_ANIMATION_LIST);
                thread.setRecommendChannelList(recChList);
                thread.setContentsDataList(recommendContentInfoList);
                thread.start();
                break;
            case DAZN:
                //DAZN用データ
                //DBキャッシュ取得
                handler = new Handler(Looper.getMainLooper());
                thread = new DataBaseThread(handler, this, INSERT_RECOMMEND_DAZN_LIST);
                thread.setRecommendChannelList(recChList);
                thread.setContentsDataList(recommendContentInfoList);
                thread.start();
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

        contentsData.setContentsId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CONTENTSID));
        contentsData.setCategoryId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CATEGORYID));
        contentsData.setServiceId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_SERVICEID));
        contentsData.setThumURL(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CTPICURL1));
        contentsData.setTitle(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_TITLE));
        contentsData.setStartViewing(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_STARTVIEWING));
        contentsData.setEndViewing(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_ENDVIEWING));
        contentsData.setReserved1(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED1));
        contentsData.setReserved2(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED2));
        contentsData.setReserved4(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RESERVED4));
        contentsData.setChannelId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_CHANNELID));
        contentsData.setRecommendOrder(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDORDER));
        contentsData.setPageId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_PAGEID));
        contentsData.setGroupId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_GROUPID));
        contentsData.setRecommendMethodId(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_RECOMMENDMETHODID));
        contentsData.setCopyright(map.get(RecommendWebXmlParser.RECOMMENDCHANNEL_LIST_COPYRIGHT));

        return contentsData;
    }

    /**
     * おすすめ番組をDBに保存する.
     *
     * @param recommendChannelList レコメンド(TV)データ
     * @param cacheDateKey    データ更新日付
     * @param tagPageNo       ページ番号
     */
    private void setStructDB(final RecommendChannelList recommendChannelList, final String cacheDateKey, final int tagPageNo) {
        RecommendListDataManager dataManager = new RecommendListDataManager(mContext);
        dataManager.insertRecommendInsertList(recommendChannelList, false, tagPageNo, cacheDateKey);
    }

    /**
     * ホームのおすすめ番組の取得対象サービスID:カテゴリーID文字列生成.
     *
     * @return 取得対象サービスID:カテゴリーID文字列
     */
    private String getHomeTerebiRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();

        //番組カテゴリー一覧の数だけループ
        for (int counter = 0; counter < RECOMMEND_CATEGORY_ID_HOME_TELEVI.length; counter++) {
            //カテゴリーIDとサービスIDを蓄積
            stringBuilder.append(RECOMMEND_CATEGORY_ID_HOME_TELEVI[counter]);

            if (counter != RECOMMEND_CATEGORY_ID_HOME_TELEVI.length - 1) {
                //最後のデータ以外はカンマを入れる
                stringBuilder.append(COMMA);
            }
        }
        return stringBuilder.toString();
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
     * ホームのおすすめビデオの取得対象サービスID:カテゴリーID文字列生成.
     *
     * @return 取得対象サービスID:カテゴリーID文字列
     */
    private String getHomeVideoRequestSCIdStr() {
        StringBuilder stringBuilder = new StringBuilder();

        //ビデオカテゴリー一覧の数だけループ
        for (int counter = 0; counter < RECOMMEND_CATEGORY_ID_HOME_VIDEO.length; counter++) {
            //カテゴリーIDとサービスIDを蓄積
            stringBuilder.append(RECOMMEND_CATEGORY_ID_HOME_VIDEO[counter]);

            if (counter != RECOMMEND_CATEGORY_ID_HOME_VIDEO.length - 1) {
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
     * おすすめDAZNの取得対象サービスID:カテゴリーID文字列生成.
     *
     * @return 取得対象サービスID:カテゴリーID文字列
     */
    private String getDAZNRequestSCIdStr() {
        return RecommendRequestId.DAZN.getRequestSCId();
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
     * @param recChList レコメンドデータ
     * @return サービス種別
     */
    private RESP_DATA_SERVICE_TYPE decisionResponseDataType(final RecommendChannelList recChList) {
        DTVTLogger.start();
        RESP_DATA_SERVICE_TYPE result;
        switch (recChList.getmRequestPageNo()) {
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_TV:
                result = RESP_DATA_SERVICE_TYPE.HOME_TV;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_VIDEO:
                result = RESP_DATA_SERVICE_TYPE.HOME_VOD;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV:
                result = RESP_DATA_SERVICE_TYPE.HIKARI_TV;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO:
                result = RESP_DATA_SERVICE_TYPE.HIKARI_VOD;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV:
                result = RESP_DATA_SERVICE_TYPE.DTV;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL:
                result = RESP_DATA_SERVICE_TYPE.DCHANNEL;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME:
                result = RESP_DATA_SERVICE_TYPE.DANIME;
                break;
            case SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DAZN:
                result = RESP_DATA_SERVICE_TYPE.DAZN;
                break;
            default:
                result = RESP_DATA_SERVICE_TYPE.UNKNOWN;
                break;
        }

        DTVTLogger.debug(result.name());
        DTVTLogger.end();
        return result;
    }

    @Override
    public void onDbOperationFinished(final boolean isSuccessful, final List<Map<String, String>> resultSet, final int operationId) {
        //強制オーバーライド
    }

    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    @Override
    public List<Map<String, String>> dbOperation(final DataBaseThread dataBaseThread, final int operationId) {
        RecommendListDataManager recommendDataManager;
        List<ContentsData> resultList;
        switch (operationId) {
            case SELECT_RECOMMEND_HOME_CHANNEL_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_TV);
                mApiDataProviderCallback.recommendHomeChannelCallback(resultList);
                break;
            case SELECT_RECOMMEND_HOME_VOD_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_VIDEO);
                mApiDataProviderCallback.recommendHomeVideoCallback(resultList);
                break;
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
            case SELECT_RECOMMEND_DAZN_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DAZN);
                mApiDataProviderCallback.recommendDAZNCallback(resultList);
                break;
            case INSERT_RECOMMEND_HOME_TV_LIST:
                //ホームのおすすめ番組用データ
                setStructDB(dataBaseThread.getRecommendChannelList(), DateUtils.RECOMMEND_HOME_CH_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_TV);
                mApiDataProviderCallback.recommendHomeChannelCallback(dataBaseThread.getContentsDataList());
                break;
            case INSERT_RECOMMEND_HOME_VOD_LIST:
                //ホームのおすすめビデオ用データ
                setStructDB(dataBaseThread.getRecommendChannelList(), DateUtils.RECOMMEND_HOME_VD_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_HOME_VIDEO);
                mApiDataProviderCallback.recommendHomeVideoCallback(dataBaseThread.getContentsDataList());
                break;
            case INSERT_RECOMMEND_CHANNEL_LIST:
                //テレビ用データ
                setStructDB(dataBaseThread.getRecommendChannelList(), DateUtils.RECOMMEND_CH_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV);
                mApiDataProviderCallback.recommendChannelCallback(dataBaseThread.getContentsDataList());
                break;
            case INSERT_RECOMMEND_VOD_LIST:
                //ビデオ用データ
                setStructDB(dataBaseThread.getRecommendChannelList(), DateUtils.RECOMMEND_VD_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO);
                mApiDataProviderCallback.recommendVideoCallback(dataBaseThread.getContentsDataList());
                break;
            case INSERT_RECOMMEND_DTV_CHANNEL_LIST:
                //Dチャンネル用データ
                setStructDB(dataBaseThread.getRecommendChannelList(), DateUtils.RECOMMEND_DCHANNEL_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL);
                mApiDataProviderCallback.recommendDChannelCallback(dataBaseThread.getContentsDataList());
                break;
            case INSERT_RECOMMEND_DTV_LIST:
                //dTV用データ
                setStructDB(dataBaseThread.getRecommendChannelList(), DateUtils.RECOMMEND_DTV_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV);
                mApiDataProviderCallback.recommendDTVCallback(dataBaseThread.getContentsDataList());
                break;
            case INSERT_RECOMMEND_D_ANIMATION_LIST:
                //dアニメ用データ
                setStructDB(dataBaseThread.getRecommendChannelList(), DateUtils.RECOMMEND_DANIME_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME);
                mApiDataProviderCallback.recommendDAnimeCallback(dataBaseThread.getContentsDataList());
                break;
            case INSERT_RECOMMEND_DAZN_LIST:
                //DAZN用データ
                setStructDB(dataBaseThread.getRecommendChannelList(), DateUtils.RECOMMEND_DAZN_LAST_INSERT,
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DAZN);
                mApiDataProviderCallback.recommendDAZNCallback(dataBaseThread.getContentsDataList());
                break;
            default:
                break;
        }
        return null;
    }
}
