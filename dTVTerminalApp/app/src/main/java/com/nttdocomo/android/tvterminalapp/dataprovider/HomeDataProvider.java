/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.activity.home.HomeActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.ChannelInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RecommendListDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RentalListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.RoleListInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.TvScheduleInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VideoRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.WatchListenVideoDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RankingTopDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RentalListDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ActiveData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ChannelList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedChannelListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.PurchasedVodListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RoleListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvScheduleList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodMetaFullData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.utils.NetWorkUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ChannelWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.GenreListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalChListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RentalVodListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.RoleListWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.TvScheduleWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.VodClipWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WatchListenVideoWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider.UPPER_PAGE_LIMIT;

/**
 * ホーム画面用データプロバイダ.
 */
public class HomeDataProvider extends ClipKeyListDataProvider implements
        TvClipWebClient.TvClipJsonParserCallback,
        VodClipWebClient.VodClipJsonParserCallback,
        TvScheduleWebClient.TvScheduleJsonParserCallback,
        DailyRankWebClient.DailyRankJsonParserCallback,
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback,
        WatchListenVideoWebClient.WatchListenVideoJsonParserCallback,
        RentalVodListWebClient.RentalVodListJsonParserCallback,
        RentalChListWebClient.RentalChListJsonParserCallback,
        ChannelWebClient.ChannelJsonParserCallback,
        RoleListWebClient.RoleListJsonParserCallback,
        GenreListWebClient.GenreListJsonParserCallback,
        RecommendDataProvider.RecommendApiDataProviderCallback {

    /**
     * Context.
     */
    private Context mContext = null;

    /**
     * Home画面用データを返却するためのコールバック.
     */
    private ApiDataProviderCallback mApiDataProviderCallback = null;

    /**
     * Home画面ではチャンネルリストを使用しないため"全て"を]設定する.
     */
    private static final int DEFAULT_CHANNEL_DISPLAY_TYPE = 0;

    /**
     * 今日のテレビランキングリストID.
     */
    private static final int DAILY_RANK_LIST = 3;
    /**
     * ビデオランキングリストID.
     */
    private static final int VIDEO_RANK_LIST = 4;
    /**
     * 番組表取得.
     */
    private static final int SELECT_TV_SCHEDULE_LIST = 5;
    /**
     * デイリーランク取得.
     */
    private static final int SELECT_DAILY_RANK_LIST = 6;
    /**
     * 視聴中ビデオ一覧ID.
     */
    private static final int WATCH_LISTEN_VIDEO_LIST = 7;
    /**
     * 購入済みCh一覧ID.
     */
    private static final int RENTAL_CH_LIST = 8;
    /**
     * 購入済みVod一覧ID.
     */
    private static final int RENTAL_VOD_LIST = 9;
    /**
     * ビデオランク取得.
     */
    private static final int SELECT_VIDEO_RANK_LIST = 10;
    /**
     * チャンネル一覧取得.
     */
    private static final int SELECT_CHANNEL_LIST = 11;
    /**
     * 視聴中ビデオ一覧取得.
     */
    private static final int SELECT_WATCH_LISTEN_VIDEO_LIST = 12;
    /**
     * おすすめCh一覧取得.
     */
    private static final int SELECT_RECOMMEND_CHANNEL_LIST = 13;
    /**
     * おすすめVod一覧取得.
     */
    private static final int SELECT_RECOMMEND_VOD_LIST = 14;
    /**
     * チャンネルリストID(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int CHANNEL_LIST = 15;
    /**
     * 番組一覧ID(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int TV_SCHEDULE_LIST = 16;
    /**
     * ロールリストID(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int ROLE_ID_LIST = 17;
    /**
     * レンタル一覧取得.
     */
    private static final int SELECT_RENTAL_VIDEO_LIST = 18;
    /**
     * レンタル、プレミアムビデオ一覧取得.
     */
    private static final int SELECT_PREMIUM_VIDEO_LIST = 1;
    /**
     * NOW ON AIR対象チャンネル最大件数.
     */
    private static final int NOW_ON_AIR_CHANNEL_LIMIT = 30;
    /**
     * 表示するコンテンツの最大件数.
     */
    private static final int MAX_CONTENTS_COUNT_FOR_SHOW = 10;

    /**
     * 購入済みCh一覧取得クラス.
     */
    private PurchasedChannelListResponse mPurchasedChannelListResponse = null;

    /**
     * ロールリスト.
     */
    private ArrayList<RoleListMetaData> mRoleListMetaDataList = null;

    /**
     * 購入済みVOD一覧取得クラス.
     */
    private PurchasedVodListResponse mPurchasedVodListResponse = null;

    /**
     * 視聴中ビデオListクラス.
     */
    private WatchListenVideoList mWatchListenVideoList = null;

    /**
     * チャンネルリストListクラス.
     */
    private ChannelList mChannelList = null;

    /**
     * ビデオリスト Listクラス.
     */
    private VideoRankList mVideoRankList = null;

    /**
     * 今日テレビランキングListクラス.
     */
    private DailyRankList mDailyRankList = null;

    /**
     * 番組Listクラス.
     */
    private TvScheduleList mTvScheduleList = null;
    //ここから
    /**
     * クリップ(TV)WebClient.
     */
    private TvClipWebClient mTvClipWebClient = null;
    /**
     * クリップ(TV)WebClient.
     */
    private VodClipWebClient mVodClipWebClient = null;
    /**
     * 番組表WebClient.
     */
    private TvScheduleWebClient mTvScheduleWebClient = null;
    /**
     * デイリーランキングWebClient.
     */
    private DailyRankWebClient mDailyRankWebClient = null;
    /**
     * ジャンル毎コンテンツ一覧WebClient.
     */
    private ContentsListPerGenreWebClient mContentsListPerGenreWebClient = null;
    /**
     * 視聴中ビデオ一覧WebClient.
     */
    private WatchListenVideoWebClient mWatchListenVideoWebClient = null;
    /**
     * レンタルVOD一覧取得WebClient.
     */
    private RentalVodListWebClient mRentalVodListWebClient = null;
    /**
     * レンタルチャンネル一覧取得WebClient.
     */
    private RentalChListWebClient mRentalChListWebClient = null;
    /**
     * チャンネルWebClient.
     */
    private ChannelWebClient mChannelWebClient = null;
    /**
     * ロールリスト取得WebClient.
     */
    private RoleListWebClient mRoleListWebClient = null;
    /**
     * ジャンル一覧取得WebClient.
     */
    private GenreListWebClient mGenreListWebClient = null;
    /**
     * RecommendDataProvider.
     */
    private RecommendDataProvider mRecommendDataProvider = null;
    /**
     * おすすめ番組データ.
     */
    private List<ContentsData> mRecommendChData = null;
    /**
     * 今日の番組データ.
     */
    private List<Map<String, String>> mDailyRankListData = null;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsStop = false;

    @Override
    public void onTvClipJsonParsed(final List<TvClipList> tvClipLists) {
        if (tvClipLists != null && tvClipLists.size() > 0) {
            TvClipList list = tvClipLists.get(0);
            setStructDB(list);
        } else {
            //ここに来てもヌルではない場合があるので、確認する
            if (tvClipLists == null) {
                //タイムアウトならばウェイト表示を止める
                ifTimeoutStopProgess(HomeActivity.HOME_CONTENTS_SORT_TV_CLIP);
            }
        }
    }

    @Override
    public void onVodClipJsonParsed(final List<VodClipList> vodClipLists) {
        if (vodClipLists != null && vodClipLists.size() > 0) {
            VodClipList list = vodClipLists.get(0);
            setStructDB(list);
        } else {
            //ここに来てもヌルではない場合があるので、確認する
            if (vodClipLists == null) {
                //タイムアウトならばウェイト表示を止める
                ifTimeoutStopProgess(HomeActivity.HOME_CONTENTS_SORT_VOD_CLIP);
            }
        }
    }

    @Override
    public void onTvScheduleJsonParsed(final List<TvScheduleList> tvScheduleList) {
        DTVTLogger.start();
        if (tvScheduleList != null && tvScheduleList.size() > 0) {
            TvScheduleList list = tvScheduleList.get(0);

            mTvScheduleList = list;
            if (mTvScheduleList != null && mTvScheduleList.geTvsList() != null) {
                List<Map<String, String>> map = mTvScheduleList.geTvsList();
                List<ContentsData> contentsData = setHomeContentData(map, false);
                sendTvScheduleListData(setChannelName(contentsData, mChannelList, true));
            }
        } else {
            //ここに来てもヌルではない場合があるので、確認する
            if (tvScheduleList == null) {
                //タイムアウトならばウェイト表示を止める
                ifTimeoutStopProgess(HomeActivity.HOME_CONTENTS_TV_SCHEDULE);
            }

            //WEBAPIを取得できなかった時はDBのデータを使用
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_TV_SCHEDULE_LIST);
            dataBaseThread.start();
        }
        DTVTLogger.end();
    }

    @Override
    public void onDailyRankJsonParsed(final List<DailyRankList> dailyRankLists) {
        DTVTLogger.start();
        if (dailyRankLists != null && dailyRankLists.size() > 0) {
            DailyRankList list = dailyRankLists.get(0);
            setStructDB(list);
        } else {
            //ここに来てもヌルではない場合があるので、確認する
            if (dailyRankLists == null) {
                //タイムアウトならばウェイト表示を止める
                ifTimeoutStopProgess(HomeActivity.HOME_CONTENTS_DAILY_RANK_LIST);
            }

            //WEBAPIを取得できなかった時はDBのデータを使用
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread t = new DataBaseThread(handler, this, SELECT_DAILY_RANK_LIST);
            t.start();
        }
        DTVTLogger.end();
    }

    @Override
    public void onContentsListPerGenreJsonParsed(final List<VideoRankList> contentsListPerGenre,
                                                 final String genreId) {
        DTVTLogger.start();
        if (contentsListPerGenre != null && contentsListPerGenre.size() > 0) {
            VideoRankList list = contentsListPerGenre.get(0);
            setStructDB(list);
        } else {
            //ここに来てもヌルではない場合があるので、確認する
            if (contentsListPerGenre == null) {
                //タイムアウトならばウェイト表示を止める
                ifTimeoutStopProgess(HomeActivity.HOME_CONTENTS_LIST_PER_GENRE);
            }

            //WEBAPIを取得できなかった時はDBのデータを使用
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_VIDEO_RANK_LIST);
            dataBaseThread.start();
        }
        DTVTLogger.end();
    }

    @Override
    public void onWatchListenVideoJsonParsed(final List<WatchListenVideoList> watchListenVideoList) {
        if (watchListenVideoList != null && watchListenVideoList.size() > 0) {
            WatchListenVideoList list = watchListenVideoList.get(0);
            setStructDB(list);
            List<Map<String, String>> vcList = list.getVcList();
            sendWatchingVideoListData(vcList);
        } else {
            //ここに来てもヌルではない場合があるので、確認する
            if (watchListenVideoList == null) {
                //タイムアウトならばウェイト表示を止める
                ifTimeoutStopProgess(HomeActivity.HOME_CONTENTS_SORT_WATCHING_VIDEO);
            }

            //WEBAPIを取得できなかった時はDBのデータを使用
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_WATCH_LISTEN_VIDEO_LIST);
            dataBaseThread.start();
        }
    }

    @Override
    public void onRentalVodListJsonParsed(final PurchasedVodListResponse RentalVodListResponse) {
        if (RentalVodListResponse != null) {
            setStructDB(RentalVodListResponse);
            sendRentalListData(RentalVodListResponse);
            sendPremiumListData(RentalVodListResponse);
        } else {
            //タイムアウトならばウェイト表示を止める
            ifTimeoutStopProgess(HomeActivity.HOME_CONTENTS_SORT_RENTAL);

            //WEBAPIを取得できなかった時はDBのデータを使用
            Handler rentalHandler = new Handler(Looper.getMainLooper());
            DataBaseThread rentalThread = new DataBaseThread(rentalHandler, this, SELECT_RENTAL_VIDEO_LIST);
            rentalThread.start();
            Handler premiumHandler = new Handler(Looper.getMainLooper());
            DataBaseThread premiumThread = new DataBaseThread(premiumHandler, this, SELECT_PREMIUM_VIDEO_LIST);
            premiumThread.start();
        }
    }

    @Override
    public void onRentalChListJsonParsed(final PurchasedChannelListResponse RentalChListResponse) {
        if (RentalChListResponse != null) {
            setStructDB(RentalChListResponse);
        } else {
            //タイムアウトならばウェイト表示を止める
            ifTimeoutStopProgess(HomeActivity.HOME_CONTENTS_SORT_RENTAL);
        }
    }

    @Override
    public void onChannelJsonParsed(final List<ChannelList> channelLists) {
        DTVTLogger.start();

        //ヌルならば、エラー判定を行う
        if (channelLists == null) {
            //タイムアウトならばウェイト表示を止める
            ifTimeoutStopProgess(HomeActivity.HOME_CONTENTS_SORT_CHANNEL);
        }

        if (channelLists != null && channelLists.size() > 0) {
            ChannelList list = channelLists.get(0);
            setStructDB(list);
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_CHANNEL_LIST);
            dataBaseThread.start();
        }
        //おすすめ番組は、チャンネル取得してから、再設定する
        if (mRecommendChData != null) {
            sendRecommendChListData(mRecommendChData);
        }
        if (mDailyRankListData != null) {
            sendDailyRankListData(mDailyRankListData);
        }
        DTVTLogger.end();
    }

    /**
     * タイムアウトならばウェイト表示の停止を行う.
     *
     * @param selectGetError WebClientを示す固定値。getErrorメソッドに指定する。
     */
    private void ifTimeoutStopProgess(final int selectGetError) {
        //エラー情報の取得
        ErrorState errorState = getError(selectGetError);

        //原因がタイムアウトならばこの場でヌルを指定したコールバックを返して、ウェイト表示を終わらせる
        if (errorState != null && errorState.isTimeout()) {
            mApiDataProviderCallback.tvScheduleListCallback(null);
        }
    }

    /**
     * コンテンツのServiceIDとServiceIDが一致するチャンネル名を追加する.
     *
     * @param scheduleList 番組表コンテンツリスト
     * @param channelList  チャンネルリスト
     * @param isPlala  true ぷららサーバ  flase レコメンドサーバ
     * @return チャンネル名
     */
    private List<ContentsData> setChannelName(final List<ContentsData> scheduleList, final ChannelList channelList, final boolean isPlala) {
        if (channelList != null && scheduleList != null) {
            List<HashMap<String, String>> list = channelList.getChannelList();
            for (int i = 0; i < scheduleList.size(); i++) {
                String scheduleId = "";
                if (!isPlala) {
                    scheduleId = scheduleList.get(i).getChannelId();
                } else {
                    scheduleId = scheduleList.get(i).getServiceId();
                }
                if (!TextUtils.isEmpty(scheduleId)) {
                    for (HashMap<String, String> hashMap : list) {
                        String channelId = (hashMap.get(JsonConstants.META_RESPONSE_SERVICE_ID));
                        //番組表と
                        if (!TextUtils.isEmpty(channelId) && scheduleId.equals(channelId)) {
                            scheduleList.get(i).setChannelName(hashMap.get(JsonConstants.META_RESPONSE_TITLE));
                        }
                    }
                }
            }
        }
        return scheduleList;
    }

    @Override
    public void onRoleListJsonParsed(final RoleListResponse roleListResponse) {
        if (roleListResponse != null) {
            //Homeではデータを使用しないためDB保存処理のみ実施
            setStructDB(roleListResponse);
        }
    }

    @Override
    public void onGenreListJsonParsed(final GenreListResponse genreListResponse) {
        if (genreListResponse != null && !genreListResponse.getTypeList().isEmpty()) {
            //取得した情報を保存する
            DateUtils dateUtils = new DateUtils(mContext);
            String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
            if (TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate)) {
                dateUtils.addLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
                dateUtils.addLastProgramDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
                SharedPreferencesUtils.setSharedPreferencesVideoGenreData(mContext,
                        StringUtils.toGenreListResponseBase64(genreListResponse));
            }
        } else {
            //取得したデータが空の場合は保存しないで取得日付をクリアする
            DateUtils.clearLastProgramDate(mContext, DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
        }
    }

    /**
     * Home画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {
        /**
         * チャンネル一覧用コールバック.
         *
         * @param channelList チャンネルリスト
         */
        void tvScheduleListCallback(List<ContentsData> channelList);

        /**
         * デイリーランキング用コールバック.
         *
         * @param dailyList デイリーランクリスト
         */
        void dailyRankListCallback(List<ContentsData> dailyList);

        /**
         * クリップ[テレビ]リスト用コールバック.
         *
         * @param tvClipList クリップ(ビデオ)リスト
         */
        void tvClipListCallback(List<ContentsData> tvClipList);

        /**
         * クリップ[ビデオ]リスト用コールバック.
         *
         * @param vodClipList クリップ(ビデオ)リスト
         */
        void vodClipListCallback(List<ContentsData> vodClipList);

        /**
         * ビデオランキング用コールバック.
         *
         * @param videoRankList ビデオランクリスト
         */
        void videoRankCallback(List<ContentsData> videoRankList);

        /**
         * 視聴中ビデオ用コールバック.
         *
         * @param watchingVideoList 視聴中ビデオリスト
         */
        void watchingVideoCallback(List<ContentsData> watchingVideoList);

        /**
         * レンタルリスト用コールバック.
         *
         * @param rentalList 視聴中ビデオリスト
         */
        void rentalListCallback(List<ContentsData> rentalList);

        /**
         * レンタルリスト用コールバック.
         *
         * @param premiumVideoList 視聴中ビデオリスト
         */
        void premiumListCallback(List<ContentsData> premiumVideoList);

        /**
         * おすすめ番組用コールバック.
         *
         * @param recChList おすすめ番組リスト
         */
        void recommendChannelCallback(List<ContentsData> recChList);

        /**
         * おすすめビデオ用コールバック.
         *
         * @param recVdList おすすめビデオリスト
         */
        void recommendVideoCallback(List<ContentsData> recVdList);
    }

    /**
     * コンストラクタ.
     *
     * @param mContext コンテクスト
     */
    public HomeDataProvider(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) mContext;
    }

    /**
     * Activityからのデータ取得要求受付.
     */
    public void getHomeData() {
        DTVTLogger.start();
        //NOW ON AIRを取得するためにまずはチャンネルリスト取得
        getChannelList(0, 0, "", DEFAULT_CHANNEL_DISPLAY_TYPE);

        //今日のテレビランキング
        getDailyRankListData();

        UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);

        //ビデオランキング
        int ageReq = userInfoDataProvider.getUserAge();
        getVideoRankListData(UPPER_PAGE_LIMIT, 1, WebApiBasePlala.FILTER_RELEASE, ageReq, "",
                JsonConstants.GENRE_PER_CONTENTS_SORT_PLAY_COUNT_DESC);

        //TODO :生データ保存のみ(DB保存までの処理を新設するか検討中)
        //ジャンルID(ビデオ一覧)一覧取得
        getGenreListDataRequest();

        //ロールID一覧取得
        getRoleListData();

        //おすすめ番組・レコメンド情報は最初からContentsDataのリストなので、そのまま使用する
        getRecommendChListData();

        //おすすめビデオ・ワンタイムパスワードの取得で重複しないようにしたので、連続呼び出しが可能になった
        getRecommendVdListData();

        if (userInfoDataProvider.isH4dUser()) {
            //H4dユーザに必要なデータ取得開始
            //クリップキー一覧(今日のテレビランキングにまとめられてるので省略するか検討中)
            //TVクリップ一覧
            getTvClipListData();

            //VODクリップ一覧
            getVodClipListData();

            //視聴中ビデオ一覧
            getWatchListenVideoData(WatchListenVideoListDataProvider.DEFAULT_PAGE_OFFSET);

            //購入済チャンネル一覧(レンタルCh)
            getChListData();

            //購入済VOD一覧
            getRentalData();
        }
        DTVTLogger.end();
    }

    /**
     * NOW ON AIRをHomeActivityに送る.
     *
     * @param list 番組リスト
     */
    private void sendTvScheduleListData(final List<ContentsData> list) {
        //契約ありの場合のみ「NOW ON AIR」を表示する(番組表表示のためデータをDB保存する必要があるためActivityへのデータ送信抑制のみ実施)
        if (UserInfoUtils.isContract(mContext)) {
            List<ContentsData> matchDataList = getContentsDataListMatchAgeInfo(list);
            mApiDataProviderCallback.tvScheduleListCallback(matchDataList);
        }
    }

    /**
     * 年齢情報に該当し、且つ画面表示に必要件数分の情報を戻す.
     *
     * @param list 番組リスト
     * @return 条件に該当する番組リスト
     */
    private List<ContentsData> getContentsDataListMatchAgeInfo(final List<ContentsData> list) {
        List<ContentsData> baseList = new ArrayList();
        int userAge = UserInfoUtils.getUserAgeInfoWrapper(SharedPreferencesUtils.getSharedPreferencesUserInfo(mContext));

        for (ContentsData data : list) {
            if (!StringUtils.isParental(userAge, data.getRValue())) {
                baseList.add(data);
            }

            if (baseList.size() >= MAX_CONTENTS_COUNT_FOR_SHOW) {
                break;
            }
        }

        return baseList;
    }

    /**
     * おすすめ番組をHomeActivityに送る.
     *
     * @param list おすすめ番組のコンテンツ情報
     */
    private void sendRecommendChListData(final List<ContentsData> list) {
        if (mChannelList != null) {
            mApiDataProviderCallback.recommendChannelCallback(setChannelName(list, mChannelList, false));
        } else {
            mRecommendChData = list;
        }
    }

    /**
     * おすすめビデオをHomeActivityに送る.
     *
     * @param list おすすめビデオのコンテンツ情報
     */
    private void sendRecommendVdListData(final List<ContentsData> list) {
        mApiDataProviderCallback.recommendVideoCallback(list);
    }

    /**
     * 今日のランキングをHomeActivityに送る.
     *
     * @param list デイリーランクリスト
     */
    private void sendDailyRankListData(final List<Map<String, String>> list) {
        if (mChannelList != null) {
            mApiDataProviderCallback.dailyRankListCallback(setChannelName(setHomeContentData(list, true), mChannelList, true));
        } else {
            mDailyRankListData = list;
        }
    }

    /**
     * ビデオランキングをHomeActivityに送る.
     *
     * @param list ビデオランキング
     */
    private void sendVideoRankListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.videoRankCallback(setHomeContentData(list, true));
    }

    /**
     * 視聴中ビデオをHomeActivityに送る.
     *
     * @param list 視聴中ビデオ
     */
    private void sendWatchingVideoListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.watchingVideoCallback(setHomeContentData(list, false));
    }

    /**
     * レンタル一覧データをRentalListActivityに送る.
     *
     * @param response レンタル一覧データ
     */
    private void sendRentalListData(final PurchasedVodListResponse response) {

        RentalDataProvider rentalDataProvider = new RentalDataProvider(mContext, RentalDataProvider.RentalType.RENTAL_LIST);
        //ContentsList生成
        List<ContentsData> list = rentalDataProvider.makeContentsData(response);

        //レンタル一覧を送る
        mApiDataProviderCallback.rentalListCallback(list);
    }

    /**
     * レンタル一覧データをRentalListActivityに送る.
     *
     * @param response レンタル一覧データ
     */
    private void sendPremiumListData(final PurchasedVodListResponse response) {

        RentalDataProvider rentalDataProvider = new RentalDataProvider(mContext, RentalDataProvider.RentalType.PREMIUM_VIDEO);
        //ContentsList生成
        List<ContentsData> list = rentalDataProvider.makeContentsData(response);

        //レンタル一覧を送る
        mApiDataProviderCallback.premiumListCallback(list);
    }

    /**
     * クリップ[テレビ]リストをHomeActivityに送る.
     *
     * @param list クリップ[テレビ]リスト
     */
    private void sendTvClipListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.tvClipListCallback(setChannelName(setHomeContentData(list, false), mChannelList, true));
    }

    /**
     * クリップ[ビデオ]リストをHomeActivityに送る.
     *
     * @param list クリップ[ビデオ]リスト
     */
    private void sendVodClipListData(final List<Map<String, String>> list) {
        mApiDataProviderCallback.vodClipListCallback(setHomeContentData(list, false));
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     *
     * @param mapList  コンテンツリストデータ
     * @param rankFlag ランキングのコンテンツか否か
     * @return dataList ListView表示用データ
     */
    private List<ContentsData> setHomeContentData(
            final List<Map<String, String>> mapList, final boolean rankFlag) {
        List<ContentsData> contentsDataList = new ArrayList<>();

        ContentsData contentInfo;

        for (int i = 0; i < mapList.size(); i++) {
            contentInfo = new ContentsData();
            if (rankFlag) {
                contentInfo.setRank(String.valueOf(i + 1));
            }
            contentInfo.setTime(mapList.get(i).get(JsonConstants.META_RESPONSE_DISPLAY_START_DATE));
            contentInfo.setTitle(mapList.get(i).get(JsonConstants.META_RESPONSE_TITLE));
            contentInfo.setDtv(mapList.get(i).get(JsonConstants.META_RESPONSE_DTV));
            if (ContentUtils.IS_DTV_FLAG.equals(contentInfo.getDtv())) {
                contentInfo.setThumURL(mapList.get(i).get(JsonConstants.META_RESPONSE_DTV_THUMB_448));
                contentInfo.setThumDetailURL(mapList.get(i).get(JsonConstants.META_RESPONSE_DTV_THUMB_640));
            } else {
                contentInfo.setThumURL(mapList.get(i).get(JsonConstants.META_RESPONSE_THUMB_448));
                contentInfo.setThumDetailURL(mapList.get(i).get(JsonConstants.META_RESPONSE_THUMB_640));
            }
            contentInfo.setPublishStartDate(mapList.get(i).get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE));
            contentInfo.setPublishEndDate(mapList.get(i).get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE));
            contentInfo.setServiceId(mapList.get(i).get(JsonConstants.META_RESPONSE_SERVICE_ID));
            contentInfo.setTvService(mapList.get(i).get(JsonConstants.META_RESPONSE_TV_SERVICE));
            contentInfo.setChannelNo(mapList.get(i).get(JsonConstants.META_RESPONSE_CHNO));
            contentInfo.setRValue(mapList.get(i).get(JsonConstants.META_RESPONSE_R_VALUE));
            contentInfo.setDispType(mapList.get(i).get(JsonConstants.META_RESPONSE_DISP_TYPE));
            contentInfo.setContentsType(mapList.get(i).get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
            contentInfo.setContentsId(mapList.get(i).get(JsonConstants.META_RESPONSE_CRID));
            contentInfo.setCrid(mapList.get(i).get(JsonConstants.META_RESPONSE_CRID));
            contentInfo.setRatStar(mapList.get(i).get(JsonConstants.META_RESPONSE_RATING));
            contentInfo.setSynop(mapList.get(i).get(JsonConstants.META_RESPONSE_SYNOP));
            contentInfo.setEventId(mapList.get(i).get(JsonConstants.META_RESPONSE_EVENT_ID));
            contentInfo.setAvailStartDate(DateUtils.getSecondEpochTime(mapList.get(i).get(JsonConstants.META_RESPONSE_AVAIL_START_DATE)));
            contentInfo.setAvailEndDate(DateUtils.getSecondEpochTime(mapList.get(i).get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)));
            contentInfo.setVodStartDate(DateUtils.getSecondEpochTime(mapList.get(i).get(JsonConstants.META_RESPONSE_VOD_START_DATE)));
            contentInfo.setVodEndDate(DateUtils.getSecondEpochTime(mapList.get(i).get(JsonConstants.META_RESPONSE_VOD_END_DATE)));
            contentsDataList.add(contentInfo);
        }

        return contentsDataList;
    }

    /**
     * 取得したリストマップをChannelListクラスへ入れる.
     *
     * @param list チャンネル一覧データ
     * @return ChannelListデータ
     */
    private ChannelList setHomeChannelData(final List<Map<String, String>> list) {
        List<Map<String, String>> hashMapList = new ArrayList<>();
        for (Map<String, String> map : list) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.putAll(map);
            hashMapList.add(hashMap);
        }
        ChannelList channelList = new ChannelList();
        channelList.setChannelList(hashMapList);
        return channelList;
    }

    /**
     * チャンネル情報を元にNowOnAir情報の取得を行う.
     * 複数件のcalleeのchannelListは異なる場所でも同じinputを保証しなければならない
     * @param channelList チャンネル情報
     */
    private void getTvScheduleFromChInfo(final ChannelList channelList) {
        List<HashMap<String, String>> channelInfoList;

        // 最大30チャンネルを対象にする
        if (channelList.getChannelList().size() >= NOW_ON_AIR_CHANNEL_LIMIT) {
            channelInfoList = channelList.getChannelList().subList(0, NOW_ON_AIR_CHANNEL_LIMIT);
        } else {
            channelInfoList = channelList.getChannelList();
        }

        List<String> chNoList = new ArrayList<>();
        for (HashMap<String, String> hashMap : channelInfoList) {
            String chNo = hashMap.get(JsonConstants.META_RESPONSE_CHNO);
            if (!TextUtils.isEmpty(chNo)) {
                chNoList.add(chNo);
            }
        }
        String[] chNos = new String[chNoList.size()];
        for (int i = 0; i < chNoList.size(); i++) {
            chNos[i] = chNoList.get(i);
        }
        getTvScheduleListData(chNos);
    }

    /**
     * NOW ON AIR 情報取得.
     * @param chNo チャンネル番号 dbからとる場合は同じチャンネルリストAPIから取ってキャッシュされてる
     */
    private void getTvScheduleListData(final String[] chNo) {
        DTVTLogger.start();
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.TV_SCHEDULE_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        if (!mIsStop) {
            //通信クラスにデータ取得要求を出す
            mTvScheduleWebClient = new TvScheduleWebClient(mContext);
            int[] chNos = new int[chNo.length];
            for (int i = 0; i < chNo.length; i++) {
                chNos[i] = Integer.parseInt(chNo[i]);
            }
            String[] channelInfoDate = new String[]{WebApiBasePlala.DATE_NOW};
            String lowerPageLimit = "";
            mTvScheduleWebClient.getTvScheduleApi(chNos, channelInfoDate, lowerPageLimit, this);
        } else {
            DTVTLogger.error("TvScheduleWebClient is stopping connect");
        }
        DTVTLogger.end();
    }

    /**
     * おすすめ番組情報取得.
     * 認証処理が必要になったので、レコメンド情報はレコメンドデータプロバイダ経由で取得するように変更
     */
    private void getRecommendChListData() {
        DTVTLogger.start();
        if (mRecommendDataProvider == null) {
            mRecommendDataProvider = new RecommendDataProvider(
                    mContext.getApplicationContext(), this);
        }

        mRecommendDataProvider.getHomeTvRecommend();
        DTVTLogger.end();
    }

    /**
     * おすすめビデオ情報取得.
     * 認証処理が必要になったので、レコメンド情報はレコメンドデータプロバイダ経由で取得するように変更
     */
    private void getRecommendVdListData() {
        DTVTLogger.start();
        if (mRecommendDataProvider == null) {
            mRecommendDataProvider = new RecommendDataProvider(
                    mContext.getApplicationContext(), this);
        }
        mRecommendDataProvider.getVodRecommend();
        DTVTLogger.end();
    }

    /**
     * クリップ[テレビ]リストデータ取得開始.
     */
    private void getTvClipListData() {
        if (!mIsStop) {
            //通信クラスにデータ取得要求を出す
            mTvClipWebClient = new TvClipWebClient(mContext);
            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
            String pagerDirection = "";

            mTvClipWebClient.getTvClipApi(userInfoDataProvider.getUserAge(), DtvtConstants.REQUEST_LIMIT_50,
                    DtvtConstants.REQUEST_LIMIT_50, 1, pagerDirection, this);
        } else {
            DTVTLogger.error("TvClipWebClient is stopping connect");
        }
    }

    /**
     * クリップ[ビデオ]リストデータ取得開始.
     */
    private void getVodClipListData() {
        if (!mIsStop) {
            //通信クラスにデータ取得要求を出す
            mVodClipWebClient = new VodClipWebClient(mContext);
            UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
            String pagerDirection = "";

            mVodClipWebClient.getVodClipApi(userInfoDataProvider.getUserAge(), DtvtConstants.REQUEST_LIMIT_50,
                    DtvtConstants.REQUEST_LIMIT_50, 1, pagerDirection, this);
        } else {
            DTVTLogger.error("VodClipWebClient is stopping connect");
        }
    }

    /**
     * 今日のテレビランキング情報取得.
     */
    private void getDailyRankListData() {
        DTVTLogger.start();
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.DAILY_RANK_LAST_INSERT);
        //今日のテレビランキング一覧のDB保存履歴と、有効期間を確認
        if ((lastDate != null && lastDate.length() > 0 && !dateUtils.isBeforeLimitDate(lastDate))
                || !NetWorkUtils.isOnline(mContext)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_DAILY_RANK_LIST);
            dataBaseThread.start();
        } else {
            if (!mIsStop) {
                //通信クラスにデータ取得要求を出す
                mDailyRankWebClient = new DailyRankWebClient(mContext);
                // 年齢情報取得(取得済み情報より)
                UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
                int ageReq = userInfoDataProvider.getUserAge();
                mDailyRankWebClient.getDailyRankApi(UPPER_PAGE_LIMIT, 1, WebApiBasePlala.FILTER_RELEASE, ageReq, this);
            } else {
                DTVTLogger.error("DailyRankWebClient is stopping connect");
            }
        }
        DTVTLogger.end();
    }

    /**
     * ビデオランキング情報取得.
     *
     * @param limit   レスポンス最大件数
     * @param offset  取得位置
     * @param filter  フィルター
     * @param ageReq  年齢設定値
     * @param genreId ジャンルID
     * @param sort    ソート指定
     */
    private void getVideoRankListData(
            final int limit, final int offset, final String filter, final int ageReq,
            final String genreId, final String sort) {
        DTVTLogger.start();
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_RANK_LAST_INSERT);
        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if ((!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate))
                || !NetWorkUtils.isOnline(mContext)) {
            //データをDBから取得する
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_VIDEO_RANK_LIST);
            dataBaseThread.start();
        } else {
            if (!mIsStop) {
                //通信クラスにデータ取得要求を出す
                mContentsListPerGenreWebClient = new ContentsListPerGenreWebClient(mContext);
                mContentsListPerGenreWebClient.getContentsListPerGenreApi(limit, offset, filter, ageReq, genreId, sort, this);
            } else {
                DTVTLogger.error("ContentsListPerGenreWebClient is stopping connect");
            }
        }
        DTVTLogger.end();
    }

    /**
     * CH一覧取得.
     *
     * @param limit  レスポンスの最大件数
     * @param offset 取得位置(1～)
     * @param filter release、testa、demo ※指定なしの場合release
     * @param type   dch：dチャンネル, hikaritv：ひかりTVの多ch, 指定なしの場合：すべて
     */
    private void getChannelList(final int limit, final int offset, final String filter, final int type) {
        DTVTLogger.start();
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.CHANNEL_LAST_UPDATE);
        if ((!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeProgramLimitDate(lastDate))
                || !NetWorkUtils.isOnline(mContext)) {
            //データをDBから取得する
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_CHANNEL_LIST);
            dataBaseThread.start();
        } else {
            //通信クラスにデータ取得要求を出す
            mChannelWebClient = new ChannelWebClient(mContext);
            mChannelWebClient.getChannelApi(limit, offset, filter, JsonConstants.DISPLAY_TYPE[type], this);
        }
        DTVTLogger.end();
    }

    /**
     * 視聴中ビデオ一覧取得.
     *
     * @param pagerOffset 取得位置
     */
    private void getWatchListenVideoData(final int pagerOffset) {
        DTVTLogger.start();
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.WATCHING_VIDEO_LIST_LAST_INSERT);

        // クリップキー一覧を取得
        if (mRequiredClipKeyList) {
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.VOD));
        }

        //視聴中ビデオ一覧のDB保存履歴と、有効期間を確認(DBにデータが不在の時もデータ再取得)
        if ((lastDate == null || lastDate.length() < 1 || dateUtils.isBeforeLimitDate(lastDate)
                || !DataBaseUtils.isCachingRecord(mContext, DataBaseConstants.WATCH_LISTEN_VIDEO_TABLE_NAME))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                mWatchListenVideoWebClient = new WatchListenVideoWebClient(mContext);

                UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
                int ageReq = userInfoDataProvider.getUserAge();
                int lowerPageLimit = 1;
                String pagerDirection = "next";

                mWatchListenVideoWebClient.getWatchListenVideoApi(ageReq, UPPER_PAGE_LIMIT,
                        lowerPageLimit, pagerOffset, pagerDirection, this);
            } else {
                DTVTLogger.error("WatchListenVideoWebClient is stopping connect");
            }
        } else {
            //キャッシュ期限内ならDBから取得
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_WATCH_LISTEN_VIDEO_LIST);
            dataBaseThread.start();
        }
        DTVTLogger.end();
    }

    /**
     * 購入済みVod一覧取得.
     */
    private void getRentalData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RENTAL_VOD_LAST_UPDATE);
        // クリップキー一覧を取得
        if (mRequiredClipKeyList) {
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.VOD));
        }
        if ((lastDate == null || lastDate.length() < 1 || dateUtils.isBeforeLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                //レンタル一覧取得
                mRentalVodListWebClient = new RentalVodListWebClient(mContext);
                mRentalVodListWebClient.getRentalVodListApi(this);
            } else {
                DTVTLogger.error("RentalVodListWebClient is stopping connect");
            }
        } else {
            Handler rentalHandler = new Handler(Looper.getMainLooper());
            DataBaseThread rentalThread = new DataBaseThread(rentalHandler, this, SELECT_RENTAL_VIDEO_LIST);
            rentalThread.start();
            Handler premiumHandler = new Handler(Looper.getMainLooper());
            DataBaseThread premiumThread = new DataBaseThread(premiumHandler, this, SELECT_PREMIUM_VIDEO_LIST);
            premiumThread.start();
        }
    }

    /**
     * 購入済みCH一覧取得.
     */
    private void getChListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.RENTAL_CHANNEL_LAST_UPDATE);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                mRentalChListWebClient = new RentalChListWebClient(mContext);
                mRentalChListWebClient.getRentalChListApi(this);
            } else {
                DTVTLogger.error("RentalChListWebClient is stopping connect");
            }
        }
        //TODO :Homeでこのデータを使用する場合はオフライン時等にキャッシュ取得等の対応が必要
    }

    /**
     * ロールリスト取得.
     */
    private void getRoleListData() {
        DTVTLogger.start();
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.ROLELIST_LAST_UPDATE);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                dateUtils.addLastProgramDate(DateUtils.ROLELIST_LAST_UPDATE);
                mRoleListWebClient = new RoleListWebClient(mContext);
                mRoleListWebClient.getRoleListApi(this);
            } else {
                DTVTLogger.error("RoleListWebClient is stopping connect");
            }
        }
        //TODO :Homeでこのデータを使用する場合はオフライン時等にキャッシュ取得等の対応が必要
        DTVTLogger.end();
    }

    /**
     * ジャンル一覧取得.
     */
    private void getGenreListDataRequest() {
        DTVTLogger.start();
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_GENRE_LIST_LAST_INSERT);
        if ((TextUtils.isEmpty(lastDate) || dateUtils.isBeforeProgramLimitDate(lastDate))
                && NetWorkUtils.isOnline(mContext)) {
            if (!mIsStop) {
                //データの有効期限切れなら通信で取得
                mGenreListWebClient = new GenreListWebClient(mContext);
                mGenreListWebClient.getGenreListApi(this);
            } else {
                DTVTLogger.error("GenreListWebClient is stopping connect");
            }
        }
        // TODO :Homeでこのデータを使用する場合はオフライン時等にキャッシュ取得等の対応が必要
        // ビデオ系は使ってない番組(IPTV)でつかうかな？
        DTVTLogger.end();
    }

    /**
     * デイリーランキングデータをDBに格納する.
     *
     * @param dailyRankList デイリーランキングデータ
     */
    private void setStructDB(final DailyRankList dailyRankList) {
        DTVTLogger.start();
        mDailyRankList = dailyRankList;
        List<Map<String, String>> drList = mDailyRankList.getDrList();
        if (drList != null && !drList.isEmpty()) {
            sendDailyRankListData(drList);
            //DB保存
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, DAILY_RANK_LIST);
            dataBaseThread.start();
        }
        DTVTLogger.end();
    }

    /**
     * ビデオランキングデータをDBに格納する.
     *
     * @param videoRankList ビデオランキングデータ
     */
    private void setStructDB(final VideoRankList videoRankList) {
        DTVTLogger.start();
        mVideoRankList = videoRankList;
        List<Map<String, String>> vrList = mVideoRankList.getVrList();
        if (vrList != null && !vrList.isEmpty()) {
            sendVideoRankListData(vrList);
            //DB保存
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, VIDEO_RANK_LIST);
            dataBaseThread.start();
        }
        DTVTLogger.end();
    }

    /**
     * チャンネルリストデータをDBに格納する.
     *
     * @param channelList チャンネルリストデータ
     */
    private void setStructDB(final ChannelList channelList) {
        DTVTLogger.start();
        mChannelList = channelList;
        List<HashMap<String, String>> chList = mChannelList.getChannelList();
        if (chList != null && !chList.isEmpty()) {
            //DB保存
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, CHANNEL_LIST);
            dataBaseThread.start();
        }
        //チャンネル情報を元にNowOnAir情報の取得を行う.
        getTvScheduleFromChInfo(mChannelList);
        DTVTLogger.end();
    }

    /**
     * クリップ[テレビ]一覧データをDBに格納する.
     *
     * @param tvClipList クリップ[テレビ]一覧データ
     */
    private void setStructDB(final TvClipList tvClipList) {
        DTVTLogger.start();
        List<Map<String, String>> vcList = tvClipList.getVcList();
        sendTvClipListData(vcList);
        DTVTLogger.end();
    }

    /**
     * クリップ[ビデオ]一覧データをDBに格納する.
     *
     * @param vodClipList クリップ[ビデオ]一覧データ
     */
    private void setStructDB(final VodClipList vodClipList) {
        DTVTLogger.start();
        List<Map<String, String>> vcList = vodClipList.getVcList();
        sendVodClipListData(vcList);
        DTVTLogger.end();
    }

    /**
     * 視聴中ビデオ一覧データをDBに格納する.
     *
     * @param watchListenVideoList 視聴中ビデオ一覧用データ
     */
    private void setStructDB(final WatchListenVideoList watchListenVideoList) {
        DTVTLogger.start();
        mWatchListenVideoList = watchListenVideoList;
        List<HashMap<String, String>> vcList = mWatchListenVideoList.getVcList();
        if (vcList != null && !vcList.isEmpty()) {
            //DB保存
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, WATCH_LISTEN_VIDEO_LIST);
            dataBaseThread.start();
        }
        DTVTLogger.end();
    }

    /**
     * 購入済みVod一覧データをDBに格納する.
     *
     * @param purchasedVodListResponse 購入済みVod一覧データ一覧用データ
     */
    private void setStructDB(final PurchasedVodListResponse purchasedVodListResponse) {
        DTVTLogger.start();
        if (purchasedVodListResponse != null) {
            mPurchasedVodListResponse = purchasedVodListResponse;
            ArrayList<ActiveData> actionData = mPurchasedVodListResponse.getVodActiveData();
            ArrayList<VodMetaFullData> metaData = mPurchasedVodListResponse.getVodMetaFullData();
            if (actionData != null && actionData.size() > 0 && metaData != null && metaData.size() > 0) {
                //DB保存
                Handler handler = new Handler(Looper.getMainLooper());
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_VOD_LIST);
                dataBaseThread.start();
            }
        }
        DTVTLogger.end();
    }

    /**
     * 購入済みCh一覧データをDBに格納する.
     *
     * @param purchasedChannelListResponse 購入済みCh一覧用データ
     */
    private void setStructDB(final PurchasedChannelListResponse purchasedChannelListResponse) {
        DTVTLogger.start();
        if (purchasedChannelListResponse != null) {
            mPurchasedChannelListResponse = purchasedChannelListResponse;
            ArrayList<ActiveData> actionData = mPurchasedChannelListResponse.getChActiveData();
            List<HashMap<String, String>> channelList = mPurchasedChannelListResponse.getChannelListData().getChannelList();
            if (actionData != null && actionData.size() > 0 && channelList != null && !channelList.isEmpty()) {
                //DB保存
                Handler handler = new Handler(Looper.getMainLooper());
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, RENTAL_CH_LIST);
                dataBaseThread.start();
            }
        }
        DTVTLogger.end();
    }

    /**
     * ロールリストをDBに格納する.
     *
     * @param roleListResponse ロールリスト
     */
    private void setStructDB(final RoleListResponse roleListResponse) {
        DTVTLogger.start();
        mRoleListMetaDataList = roleListResponse.getRoleList();
        if (mRoleListMetaDataList != null && mRoleListMetaDataList.size() > 0) {
            //DB保存
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, ROLE_ID_LIST);
            dataBaseThread.start();
        }
        DTVTLogger.end();
    }

    /**
     * レコメンドのテレビ情報のコールバック.
     *
     * @param recommendContentInfoList テレビレコメンド情報
     */
    @Override
    public void recommendChannelCallback(final List<ContentsData> recommendContentInfoList) {
        DTVTLogger.start();
        if (recommendContentInfoList != null && recommendContentInfoList.size() > 0) {
            //送られてきたデータをアクティビティに渡す
            sendRecommendChListData(recommendContentInfoList);
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_RECOMMEND_CHANNEL_LIST);
            dataBaseThread.start();
        }

        DTVTLogger.end();
    }

    /**
     * レコメンドのビデオ情報のコールバック.
     *
     * @param recommendContentInfoList ビデオレコメンド情報
     */
    @Override
    public void recommendVideoCallback(final List<ContentsData> recommendContentInfoList) {
        DTVTLogger.start();
        //ワンタイムパスワードが競合しないように、おすすめVOD取得後に動作を開始する
        //TODO :　競合は解消したので、ここでの動作は必要なくなった。最終的には上記コメントとログ出力も消去する
        DTVTLogger.debug("mRecommendDataProvider.getDtvRecommend() executed before");

        if (recommendContentInfoList != null && recommendContentInfoList.size() > 0) {
            //送られてきたデータをアクティビティに渡す
            sendRecommendVdListData(recommendContentInfoList);
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            Handler handler = new Handler(Looper.getMainLooper());
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_RECOMMEND_VOD_LIST);
            dataBaseThread.start();
        }
        DTVTLogger.end();
    }

    /**
     * レコメンドのDTV情報のコールバック.
     *
     * @param recommendContentInfoList DTVレコメンド情報
     */
    @Override
    public void recommendDTVCallback(final List<ContentsData> recommendContentInfoList) {
        //ワンタイムパスワードが競合しないように、おすすめDTV取得後に動作を開始する
        //TODO :　競合は解消したので、ここでの動作は必要なくなった。最終的には上記コメントとログ出力も消去する
        DTVTLogger.debug("mRecommendDataProvider.getDtvChRecommend() executed before");

        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    /**
     * レコメンドのdアニメ情報のコールバック.
     *
     * @param recommendContentInfoList dアニメレコメンド情報
     */
    @Override
    public void recommendDAnimeCallback(final List<ContentsData> recommendContentInfoList) {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    /**
     * レコメンドのDチャンネル情報のコールバック.
     *
     * @param recommendContentInfoList Dチャンネルのレコメンド情報
     */
    @Override
    public void recommendDChannelCallback(final List<ContentsData> recommendContentInfoList) {
        //ワンタイムパスワードが競合しないように、おすすめDチャンネル取得後に動作を開始する
        //TODO :　競合は解消したので、ここでの動作は必要なくなった。最終的には上記コメントとログ出力も消去する
        DTVTLogger.debug("mRecommendDataProvider.getDAnimationRecommend() executed before");

        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    /**
     * レコメンドのエラーのコールバック.
     */
    @Override
    public void recommendNGCallback() {
        //現状では不使用・インタフェースの仕様で宣言を強要されているだけとなる
    }

    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    //Db処理を一括でまとめたメソッドのため60行以上の処理としています
    @Override
    public List<Map<String, String>> dbOperation(final int operationId) {
        super.dbOperation(operationId);
        HomeDataManager homeDataManager;
        List<ContentsData> resultList;
        RecommendListDataManager recommendDataManager;
        RentalDataProvider rentalDataProvider;
        List<Map<String, String>> vodMetaList;
        List<Map<String, String>> activeList;
        RentalListDataManager rentalListDataManager;
        PurchasedVodListResponse purchasedVodListResponse;
        switch (operationId) {
            case ROLE_ID_LIST:
                RoleListInsertDataManager roleListInsertDataManager = new RoleListInsertDataManager(mContext);
                roleListInsertDataManager.insertRoleList(mRoleListMetaDataList);
                break;
            case CHANNEL_LIST:
                ChannelInsertDataManager channelInsertDataManager = new ChannelInsertDataManager(mContext);
                channelInsertDataManager.insertChannelInsertList(mChannelList);
                break;
            case TV_SCHEDULE_LIST:
                TvScheduleInsertDataManager tvScheduleInsertDataManager = new TvScheduleInsertDataManager(mContext);
                tvScheduleInsertDataManager.insertTvScheduleInsertList(mTvScheduleList);
                break;
            case DAILY_RANK_LIST:
                DailyRankInsertDataManager dailyRankInsertDataManager = new DailyRankInsertDataManager(mContext);
                dailyRankInsertDataManager.insertDailyRankInsertList(mDailyRankList);
                break;
            case VIDEO_RANK_LIST:
                VideoRankInsertDataManager videoRankInsertDataManager = new VideoRankInsertDataManager(mContext);
                videoRankInsertDataManager.insertVideoRankInsertList(mVideoRankList);
                break;
            case WATCH_LISTEN_VIDEO_LIST:
                WatchListenVideoDataManager watchListenVideoDataManager = new WatchListenVideoDataManager(mContext);
                watchListenVideoDataManager.insertWatchListenVideoInsertList(mWatchListenVideoList);
                break;
            case RENTAL_CH_LIST:
                RentalListInsertDataManager rentalChListInsertDataManager = new RentalListInsertDataManager(mContext);
                rentalChListInsertDataManager.insertChRentalListInsertList(mPurchasedChannelListResponse);
                break;
            case RENTAL_VOD_LIST:
                RentalListInsertDataManager rentalListInsertDataManager = new RentalListInsertDataManager(mContext);
                rentalListInsertDataManager.insertRentalListInsertList(mPurchasedVodListResponse);
                break;
            case SELECT_TV_SCHEDULE_LIST:
                List<Map<String, String>> scheduleList;
                homeDataManager = new HomeDataManager(mContext);
                scheduleList = homeDataManager.selectTvScheduleListHomeData();
                sendTvScheduleListData(setChannelName(setHomeContentData(scheduleList, false), mChannelList, true));
                break;
            case SELECT_DAILY_RANK_LIST:
                List<Map<String, String>> dailyRankList;
                homeDataManager = new HomeDataManager(mContext);
                dailyRankList = homeDataManager.selectDailyRankListHomeData();
                sendDailyRankListData(dailyRankList);
                break;
            case SELECT_VIDEO_RANK_LIST:
                List<Map<String, String>> videoRankList;
                RankingTopDataManager rankingTopDataManager = new RankingTopDataManager(mContext);
                videoRankList = rankingTopDataManager.selectVideoRankListData();
                sendVideoRankListData(videoRankList);
                break;
            case SELECT_CHANNEL_LIST:
                homeDataManager = new HomeDataManager(mContext);
                List<Map<String, String>> channelList = homeDataManager.selectChannelListHomeData();
                ChannelList chList = setHomeChannelData(channelList);
                //チャンネル情報を元にNowOnAir情報の取得を行う.
                mChannelList = chList;
                getTvScheduleFromChInfo(chList);
                //おすすめ番組は、前回チャンネル取得できなければ、再設定する
                if (mRecommendChData != null) {
                    sendRecommendChListData(mRecommendChData);
                }
                if (mDailyRankListData != null) {
                    sendDailyRankListData(mDailyRankListData);
                }
                break;
            case SELECT_WATCH_LISTEN_VIDEO_LIST:
                homeDataManager = new HomeDataManager(mContext);
                List<Map<String, String>> list = homeDataManager.selectWatchingVideoHomeData();
                mApiDataProviderCallback.watchingVideoCallback(setHomeContentData(list, false));
                break;
            case SELECT_RECOMMEND_CHANNEL_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV);
                mApiDataProviderCallback.recommendChannelCallback(setChannelName(resultList, mChannelList, false));
                break;
            case SELECT_RECOMMEND_VOD_LIST:
                recommendDataManager = new RecommendListDataManager(mContext);
                resultList = recommendDataManager.selectRecommendList(
                        SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO);
                mApiDataProviderCallback.recommendVideoCallback(resultList);
                break;
            case SELECT_RENTAL_VIDEO_LIST:
                rentalDataProvider = new RentalDataProvider(mContext, RentalDataProvider.RentalType.RENTAL_LIST);
                rentalListDataManager = new RentalListDataManager(mContext);
                vodMetaList = rentalListDataManager.selectRentalListData();
                activeList = rentalListDataManager.selectRentalActiveListData();
                purchasedVodListResponse = rentalDataProvider.makeVodMetaData(vodMetaList, activeList);
                sendRentalListData(purchasedVodListResponse);
                break;
            case SELECT_PREMIUM_VIDEO_LIST:
                rentalDataProvider = new RentalDataProvider(mContext, RentalDataProvider.RentalType.PREMIUM_VIDEO);
                rentalListDataManager = new RentalListDataManager(mContext);
                vodMetaList = rentalListDataManager.selectRentalListData();
                activeList = rentalListDataManager.selectRentalActiveListData();
                purchasedVodListResponse = rentalDataProvider.makeVodMetaData(vodMetaList, activeList);
                sendPremiumListData(purchasedVodListResponse);
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsStop = true;
        stopConnection();
        if (mTvClipWebClient != null) {
            mTvClipWebClient.stopConnection();
        }
        if (mVodClipWebClient != null) {
            mVodClipWebClient.stopConnection();
        }
        if (mTvScheduleWebClient != null) {
            mTvScheduleWebClient.stopConnection();
        }
        if (mDailyRankWebClient != null) {
            mDailyRankWebClient.stopConnection();
        }
        if (mContentsListPerGenreWebClient != null) {
            mContentsListPerGenreWebClient.stopConnect();
        }
        if (mWatchListenVideoWebClient != null) {
            mWatchListenVideoWebClient.stopConnection();
        }
        if (mChannelWebClient != null) {
            mChannelWebClient.stopConnection();
        }
        if (mGenreListWebClient != null) {
            mGenreListWebClient.stopConnect();
        }
        if (mRecommendDataProvider != null) {
            mRecommendDataProvider.stopConnect();
        }
        if (mRoleListWebClient != null) {
            mRoleListWebClient.stopConnection();
        }
        if (mRentalChListWebClient != null) {
            mRentalChListWebClient.stopConnection();
        }
        if (mRentalVodListWebClient != null) {
            mRentalVodListWebClient.stopConnection();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsStop = false;
        enableConnection();
        if (mTvClipWebClient != null) {
            mTvClipWebClient.enableConnection();
        }
        if (mVodClipWebClient != null) {
            mVodClipWebClient.enableConnection();
        }
        if (mTvScheduleWebClient != null) {
            mTvScheduleWebClient.enableConnection();
        }
        if (mDailyRankWebClient != null) {
            mDailyRankWebClient.enableConnect();
        }
        if (mContentsListPerGenreWebClient != null) {
            mContentsListPerGenreWebClient.enableConnect();
        }
        if (mWatchListenVideoWebClient != null) {
            mWatchListenVideoWebClient.enableConnection();
        }
        if (mChannelWebClient != null) {
            mChannelWebClient.enableConnection();
        }
        if (mGenreListWebClient != null) {
            mGenreListWebClient.enableConnect();
        }
        if (mRecommendDataProvider != null) {
            mRecommendDataProvider.enableConnect();
        }
        if (mRoleListWebClient != null) {
            mRoleListWebClient.enableConnection();
        }
        if (mRentalChListWebClient != null) {
            mRentalChListWebClient.enableConnection();
        }
        if (mRentalVodListWebClient != null) {
            mRentalVodListWebClient.enableConnection();
        }
    }

    /**
     * 通信エラーメッセージを取得する.
     * @param apiIndex API区別インデックス
     * @return エラーメッセージ
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public ErrorState getError(final int apiIndex) {
        ErrorState errorState = null;
        switch (apiIndex) {
            case HomeActivity.HOME_CONTENTS_SORT_CHANNEL:
                if (mChannelWebClient != null) {
                    errorState = mChannelWebClient.getError();
                }
                break;
            case HomeActivity.HOME_CONTENTS_SORT_RECOMMEND_PROGRAM:
                if (mRecommendDataProvider != null) {
                    errorState = mRecommendDataProvider.getError(RecommendDataProvider.API_INDEX_TV_HOME);
                }
                break;
            case HomeActivity.HOME_CONTENTS_SORT_RECOMMEND_VOD:
                if (mRecommendDataProvider != null) {
                    errorState = mRecommendDataProvider.getError(RecommendDataProvider.API_INDEX_TV);
                }
                break;
            case HomeActivity.HOME_CONTENTS_SORT_TODAY:
                if (mDailyRankWebClient != null) {
                    errorState = mDailyRankWebClient.getError();
                }
                break;
            case HomeActivity.HOME_CONTENTS_SORT_VIDEO:
                if (mContentsListPerGenreWebClient != null) {
                    errorState = mContentsListPerGenreWebClient.getError();
                }
                break;
            case HomeActivity.HOME_CONTENTS_SORT_WATCHING_VIDEO:
                if (mWatchListenVideoWebClient != null) {
                    errorState = mWatchListenVideoWebClient.getError();
                }
                break;
            case HomeActivity.HOME_CONTENTS_SORT_TV_CLIP:
                if (mTvClipWebClient != null) {
                    errorState = mTvClipWebClient.getError();
                }
                break;
            case HomeActivity.HOME_CONTENTS_SORT_VOD_CLIP:
                if (mVodClipWebClient != null) {
                    errorState = mVodClipWebClient.getError();
                }
                break;
            case HomeActivity.HOME_CONTENTS_SORT_PREMIUM:
            case HomeActivity.HOME_CONTENTS_SORT_RENTAL:
                if (mRentalVodListWebClient != null) {
                    errorState = mRentalVodListWebClient.getError();
                }
                break;
            case HomeActivity.HOME_CONTENTS_LIST_PER_GENRE:
                //ジャンル一覧のエラー情報の取得
                if (mContentsListPerGenreWebClient != null) {
                    errorState = mContentsListPerGenreWebClient.getError();
                }
                break;
            case HomeActivity.HOME_CONTENTS_DAILY_RANK_LIST:
                //デイリーランキングのエラー情報の取得
                if (mDailyRankWebClient != null) {
                    errorState = mDailyRankWebClient.getError();
                }
                break;
            case  HomeActivity.HOME_CONTENTS_TV_SCHEDULE:
                //番組表のエラー情報取得
                if (mTvScheduleWebClient != null) {
                    errorState = mTvScheduleWebClient.getError();
                }
                break;
            default:
                break;
        }
        return errorState;
    }
}
