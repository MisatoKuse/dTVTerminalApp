/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.activity.ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.WeeklyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.DataBaseConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.databese.thread.DataBaseThread;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VideoRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.WeeklyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RankingTopDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataBaseUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WeeklyRankWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ランキングTop画面用データプロバイダ.
 */
public class RankingTopDataProvider extends ClipKeyListDataProvider implements
        DailyRankWebClient.DailyRankJsonParserCallback,
        WeeklyRankWebClient.WeeklyRankJsonParserCallback,
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback,

        ScaledDownProgramListDataProvider.ApiDataProviderCallback {
    /**
     * コンテキスト.
     */
    private final Context mContext;
    /**
     * RakingTop画面用コールバック.
     */
    private ApiDataProviderCallback mApiDataProviderCallback = null;
    /**
     * WeeklyRanking用コールバック.
     */
    private WeeklyRankingApiDataProviderCallback mWeeklyRankingApiCallback = null;
    /**
     * VideoRanking用コールバック.
     */
    private VideoRankingApiDataProviderCallback mVideoRankingApiDataProviderCallback = null;
    /**
     * 週間テレビランキングリストデータ.
     */
    private WeeklyRankList mWeeklyRankList = null;
    /**
     * 今日のテレビランキングリストデータ.
     */
    private DailyRankList mDailyRankList = null;
    /**
     * ビデオランキングリストデータ.
     */
    private VideoRankList mVideoRankList = null;
    /**
     * 通信禁止判定フラグ.
     */
    private boolean mIsCancel = false;
    /**
     * 今日のテレビランキング取得用Webクライアント.
     */
    private DailyRankWebClient mDailyRankWebClient = null;
    /**
     * 週間ランキング取得用Webクライアント.
     */
    private WeeklyRankWebClient mWeeklyRankWebClient = null;
    /**
     * ビデオランキング取得用Webクライアント.
     */
    private ContentsListPerGenreWebClient mContentsListPerGenreWebClient = null;
    /**
     * 今日のテレビランキング取得用Webクライアントがエラーだった場合のフラグ.
     */
    private boolean mIsDailyRankWebApiError = false;
    /**
     * 週間ランキング取得用Webクライアントがエラーだった場合のフラグ.
     */
    private boolean mIsWeeklyRankWebApiError = false;
    /**
     * ビデオランキング取得用Webクライアントがエラーだった場合のフラグ.
     */
    private boolean mIsContentsListPerGenreWebApiError = false;
    /**
     * 今日のテレビランキング取得用エラー情報.
     */
    private ErrorState mDailyRankWebApiErrorState = null;
    /**
     * 週間ランキング取得用エラー情報.
     */
    private ErrorState mWeeklyRankWebApiErrorState = null;
    /**
     * ビデオランキング取得用エラー情報.
     */
    private ErrorState mContentsListPerGenreWebApiErrorState = null;
    /**
     * DailyRankingDBThread判定用(insert)(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int INSERT_DAILY_RANKING_DATA = 6;
    /**
     * WeeklyRankingDBThread判定用(insert)(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int INSERT_WEEKLY_RANKING_DATA = 7;
    /**
     * VideoRankingDBThread判定用(insert)(親クラスのDbThreadで"0","1","2"を使用しているため使用しない).
     */
    private static final int INSERT_VIDEO_RANKING_DATA = 8;
    /**
     * DailyRankingDBThread判定用(select).
     */
    private static final int SELECT_DAILY_RANKING_DATA = 3;
    /**
     * WeeklyRankingDBThread判定用(select).
     */
    private static final int SELECT_WEEKLY_RANKING_DATA = 4;
    /**
     * VideoRankingDBThread判定用(select).
     */
    private static final int SELECT_VIDEO_RANKING_DATA = 5;
    /**
     * 最大取得件数.
     */
    public static final int UPPER_PAGE_LIMIT = 50;
    /**
     * チャンネルプロバイダー.
     */
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider = null;
    /**
     * 今日の番組ランキング.
     */
    private List<Map<String, String>> mDailyRankMapList = null;
    /**
     * 週間ランキング.
     */
    private List<Map<String, String>> mWeeklyRankMapList = null;
    /**
     * チャンネル情報.
     */
    private ArrayList<ChannelInfo> channels = null;

    @Override
    public void onDailyRankJsonParsed(final List<DailyRankList> dailyRankLists) {
        //エラー情報をクリア
        mDailyRankWebApiErrorState = null;

        if (dailyRankLists != null && dailyRankLists.size() > 0) {
            DailyRankList list = dailyRankLists.get(0);
            setStructDB(list);
            if (!mRequiredClipKeyList || mResponseEndFlag) {
                if (channels == null || channels.size() == 0) {
                    mDailyRankMapList = list.getDrList();
                    getChannelList();
                } else {
                    sendDailyRankListData(list.getDrList());
                }
            } else {
                mDailyRankList = list;
            }
        } else {
            //通信エラーなので、DBのテーブルの存在を確認する
            boolean isDailyData = DataBaseUtils.isCachingRecord(
                    mContext, DataBaseConstants.DAILYRANK_LIST_TABLE_NAME);
            DTVTLogger.debug(DataBaseConstants.DAILYRANK_LIST_TABLE_NAME + " = " + isDailyData);
            //ここにたどり着いたならば、DB上のデータは存在しないか期限切れとなる。
            if (mIsDailyRankWebApiError && !isDailyData) {
                // 通信に失敗し、DBにデータが存在しなければ、ネットワークエラーを取得する
                mDailyRankWebApiErrorState = mDailyRankWebClient.getError();
                //ヌルで帰る
                sendDailyRankListData(null);
                return;
            }

            //WEBAPIを取得できなかった時はDBのデータを使用（期限切れでも使用する）
            Handler handler = new Handler();
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_DAILY_RANKING_DATA);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        }
    }


    @Override
    public void onWeeklyRankJsonParsed(final List<WeeklyRankList> weeklyRankLists, final String genreId) {
        DTVTLogger.start();
        if (weeklyRankLists != null && weeklyRankLists.size() > 0) {
            WeeklyRankList list = weeklyRankLists.get(0);
            // ジャンル指定無しの場合のみキャッシュする.
            if (genreId == null || genreId.isEmpty()) {
                setStructDB(list);
            }
            // コールバック判定
            if (!mRequiredClipKeyList || mResponseEndFlag) {
                if (channels == null || channels.size() == 0) {
                    mWeeklyRankMapList = list.getWeeklyRankList();
                    getChannelList();
                } else {
                    sendWeeklyRankList(list);
                }
            } else {
                mWeeklyRankList = list;
            }
        } else {
            //通信エラーなので、DBのテーブルの存在を確認する
            boolean isWeeklyData = DataBaseUtils.isCachingRecord(
                    mContext, DataBaseConstants.WEEKLYRANK_LIST_TABLE_NAME);
            DTVTLogger.debug(DataBaseConstants.WEEKLYRANK_LIST_TABLE_NAME + " = " + isWeeklyData);
            //ここにたどり着いたならば、DB上のデータは存在しないか期限切れとなる。
            if (mIsWeeklyRankWebApiError && !isWeeklyData) {
                // 通信に失敗し、DBにデータが存在しなければ、ネットワークエラーを取得する
                mWeeklyRankWebApiErrorState = mWeeklyRankWebClient.getError();
                //ヌルで帰る
                sendWeeklyRankList(null);
                return;
            }

            //WEBAPIを取得できなかった時はDBのデータを使用（期限切れでも使用する）
            Handler handler = new Handler();
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_WEEKLY_RANKING_DATA);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        }
        DTVTLogger.end();
    }

    @Override
    public void onContentsListPerGenreJsonParsed(final List<VideoRankList> contentsListPerGenre, final String genreId) {
        if (contentsListPerGenre != null && contentsListPerGenre.size() > 0) {
            VideoRankList list = contentsListPerGenre.get(0);
            // ジャンル指定無しの場合のみキャッシュする.
            if (genreId == null || genreId.isEmpty()) {
                setStructDB(list);
            }
            if (!mRequiredClipKeyList || mResponseEndFlag) {
                sendVideoRankList(list);
            } else {
                mVideoRankList = list;
            }
        } else {
            //通信エラーなので、DBのテーブルの存在を確認する
            boolean isContentsListPerGenreData = DataBaseUtils.isCachingRecord(
                    mContext, DataBaseConstants.RANKING_VIDEO_LIST_TABLE_NAME);
            DTVTLogger.debug(DataBaseConstants.RANKING_VIDEO_LIST_TABLE_NAME + " = "
                    + isContentsListPerGenreData);
            //ここにたどり着いたならば、DB上のデータは存在しないか期限切れとなる。
            if (mIsContentsListPerGenreWebApiError && !isContentsListPerGenreData) {
                // 通信に失敗し、DBにデータが存在しなければ、ネットワークエラーを取得する
                mContentsListPerGenreWebApiErrorState =
                        mContentsListPerGenreWebClient.getError();
                //ヌルで帰る
                sendVideoRankList(null);
                return;
            }

            //WEBAPIを取得できなかった時はDBのデータを使用（期限切れでも使用する）
            Handler handler = new Handler();
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_VIDEO_RANKING_DATA);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        }
    }

    @Override
    public void onTvClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onTvClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        // 今日のテレビランキング
        if (mContext instanceof DailyTvRankingActivity) {
            if (mDailyRankList != null) {
                if (channels == null || channels.size() == 0) {
                    mDailyRankMapList = mDailyRankList.getDrList();
                    getChannelList();
                } else {
                    sendDailyRankListData(mDailyRankList.getDrList());
                }
            }
        }
        // 週間テレビランキング
        if (mContext instanceof WeeklyTvRankingActivity) {
            if (mWeeklyRankList != null) {
                if (channels == null || channels.size() == 0) {
                    mWeeklyRankMapList = mWeeklyRankList.getWeeklyRankList();
                    getChannelList();
                } else {
                    sendWeeklyRankList(mWeeklyRankList);
                }
            }
        }
        // ビデオランキング
        if (mContext instanceof VideoRankingActivity) {
            if (mVideoRankList != null) {
                sendVideoRankList(mVideoRankList);
            }
        }

        DTVTLogger.end();
    }

    /**
     * チャンネル情報取得.
     */
    private void getChannelList() {
        // チャンネル情報を取得
        if (mScaledDownProgramListDataProvider == null) {
            mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(mContext, this);
        }
        mScaledDownProgramListDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_ALL);
    }

    /**
     * 週間ランクリストをActivityに送る.
     *
     * @param weeklyRankList 週間ランクリスト
     */
    private void sendWeeklyRankList(final WeeklyRankList weeklyRankList) {
        DTVTLogger.start();
        boolean isError = false;
        if (weeklyRankList != null) {
            if (weeklyRankList.getWeeklyRankList() != null && weeklyRankList.getWeeklyRankList().size() > 0) {
                List<ContentsData> contentsDataList = setRankingContentData(weeklyRankList.getWeeklyRankList(), channels);
                if (mApiDataProviderCallback != null) {
                    mApiDataProviderCallback.weeklyRankCallback(contentsDataList);
                } else {
                    if (mWeeklyRankingApiCallback != null) {
                        mWeeklyRankingApiCallback.onWeeklyRankListCallback(contentsDataList);
                    }
                    DTVTLogger.end();
                }
            } else {
                isError = true;
            }
        } else {
            isError = true;
        }
        if (isError) {
            if (mApiDataProviderCallback != null) {
                mApiDataProviderCallback.weeklyRankCallback(null);
            } else {
                if (mWeeklyRankingApiCallback != null) {
                    mWeeklyRankingApiCallback.onWeeklyRankListCallback(null);
                }
                DTVTLogger.end();
            }
        }
    }

    /**
     * ビデオランクリストをActivityに送る.
     *
     * @param videoRankList ビデオランクリスト
     */
    private void sendVideoRankList(final VideoRankList videoRankList) {
        DTVTLogger.start();
        //エラーフラグ
        boolean isError = false;

        if (videoRankList != null) {
            List<ContentsData> contentsDataList = setRankingContentData(videoRankList.getVrList(), null);
            if (contentsDataList != null) {
                if (mApiDataProviderCallback != null) {
                    mApiDataProviderCallback.videoRankCallback(contentsDataList);
                } else {
                    if (mVideoRankingApiDataProviderCallback != null) {
                        mVideoRankingApiDataProviderCallback.onVideoRankListCallback(contentsDataList);
                    }
                    DTVTLogger.end();
                }
            } else {
                //データが無いので、フラグをセット
                isError = true;
            }
        } else {
            //エラーなのでフラグをセット
            isError = true;
        }

        //エラーなので、ヌルを返す
        if (isError) {
            //エラー情報を控えておく
            mContentsListPerGenreWebApiErrorState
                    = mContentsListPerGenreWebClient.getError();
            if (mApiDataProviderCallback != null) {
                //ビデオ情報が無いので、ヌルで帰る
                mApiDataProviderCallback.videoRankCallback(null);
            } else if (mVideoRankingApiDataProviderCallback != null) {
                mVideoRankingApiDataProviderCallback.onVideoRankListCallback(null);
            }
        }
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        // 今日のテレビランキング
        if (mContext instanceof DailyTvRankingActivity) {
            if (mDailyRankList != null) {
                if (channels == null || channels.size() == 0) {
                    mDailyRankMapList = mDailyRankList.getDrList();
                    getChannelList();
                } else {
                    sendDailyRankListData(mDailyRankList.getDrList());
                }
            }
        }
        // 週間テレビランキング
        if (mWeeklyRankList != null) {
            if (channels == null || channels.size() == 0) {
                mWeeklyRankMapList = mWeeklyRankList.getWeeklyRankList();
                getChannelList();
            } else {
                sendWeeklyRankList(mWeeklyRankList);
            }
        }
        // ビデオランキング
        if (mContext instanceof VideoRankingActivity && mVideoRankList != null) {
            sendVideoRankList(mVideoRankList);
        }
        DTVTLogger.end();
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {

    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        this.channels = channels;
        if (mDailyRankMapList != null) {
            List<ContentsData> contentsDataList = setRankingContentData(mDailyRankMapList, channels);
            mApiDataProviderCallback.dailyRankListCallback(contentsDataList);
        }
        if (mWeeklyRankMapList != null) {
            List<ContentsData> contentsDataList = setRankingContentData(mWeeklyRankMapList, channels);
            if (mApiDataProviderCallback != null) {
                mApiDataProviderCallback.weeklyRankCallback(contentsDataList);
            } else {
                if (mWeeklyRankingApiCallback != null) {
                    mWeeklyRankingApiCallback.onWeeklyRankListCallback(contentsDataList);
                }
                DTVTLogger.end();
            }
        }
    }

    /**
     * Ranking Top画面用データを返却するためのコールバック.
     */
    public interface ApiDataProviderCallback {
        /**
         * デイリーランキング用コールバック.
         *
         * @param contentsDataList デイリーランキングのコンテンツ一覧
         */
        void dailyRankListCallback(List<ContentsData> contentsDataList);

        /**
         * 週間ランキング用コールバック.
         *
         * @param contentsDataList 週間ランキングのコンテンツ一覧
         */
        void weeklyRankCallback(List<ContentsData> contentsDataList);

        /**
         * ビデオランキング用コールバック.
         *
         * @param contentsDataList ビデオランキングのコンテンツ一覧
         */
        void videoRankCallback(List<ContentsData> contentsDataList);
    }

    /**
     * WeeklyRanking用のコールバック.
     */
    public interface WeeklyRankingApiDataProviderCallback {
        /**
         * 取得条件"総合"用コールバック.
         *
         * @param contentsDataList 週間ランキングのコンテンツ一覧
         */
        void onWeeklyRankListCallback(List<ContentsData> contentsDataList);
    }

    /**
     * ビデオランキング用コールバック.
     */
    public interface VideoRankingApiDataProviderCallback {
        /**
         * ジャンル系のコールバック.
         *
         * @param videoRankList アダプターで使うデータ
         */
        void onVideoRankListCallback(List<ContentsData> videoRankList);
    }
    /**
     * コンストラクタ.
     *
     * @param context context
     */
    public RankingTopDataProvider(final Context context) {
        super(context);
        this.mContext = context;
        this.mApiDataProviderCallback = (ApiDataProviderCallback) context;
    }

    /**
     * 週間・ビデオ用コンストラクタ.
     *
     * @param context context
     * @param type    画面タイプ
     */
    public RankingTopDataProvider(final Context context, final ContentsAdapter.ActivityTypeItem type) {
        super(context);
        this.mContext = context;
        if (ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK.equals(type)) {
            this.mWeeklyRankingApiCallback = (WeeklyRankingApiDataProviderCallback) context;
        } else if (ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK.equals(type)) {
            this.mVideoRankingApiDataProviderCallback = (VideoRankingApiDataProviderCallback) context;
        }
    }


    /**
     * RankingTopActivityからのデータ取得要求受付.
     */
    public void getRankingTopData() {
        if (!mIsCancel) {
            //今日のランキング
            getDailyRankList();
            //週刊のランキング
            getWeeklyRankListData("");
            //ビデオのランキング
            getVideoRankListData("");
        } else {
            DTVTLogger.error("RankingTopDataProvider is stopping connect");
        }
    }

    /**
     * デイリーランキングデータ取得.
     */
    public void getDailyRankList() {
        if (!mIsCancel) {
            //今日のランキング
            getDailyRankListData();
        } else {
            DTVTLogger.error("RankingTopDataProvider is stopping connect");
        }
    }

    /**
     * WeeklyTvRankingActivityからのデータ取得要求.
     *
     * @param genreId ジャンルID
     */
    public void getWeeklyRankingData(final String genreId) {
        if (!mIsCancel) {
            mWeeklyRankList = null;
            // クリップキー一覧取得
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.TV));
            // データを取得
            getWeeklyRankListData(genreId);
        } else {
            DTVTLogger.error("RankingTopDataProvider is stopping connect");
        }
    }

    /**
     * ビデオランキングデータ取得.
     *
     * @param genreId ジャンルID
     */
    public void getVideoRankingData(final String genreId) {
        if (!mIsCancel) {
            mVideoRankList = null;
            // クリップキー一覧取得
            if (mRequiredClipKeyList) {
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.VOD));
            }

            getVideoRankListData(genreId);
        } else {
            DTVTLogger.error("RankingTopDataProvider is stopping connect");
        }
    }

    /**
     * 今日のランキングをRankingTopActivityに送る.
     *
     * @param list デイリーランキング
     */
    private void sendDailyRankListData(final List<Map<String, String>> list) {
        if (list != null && list.size() > 0) {
            List<ContentsData> contentsDataList = setRankingContentData(list, channels);
            mApiDataProviderCallback.dailyRankListCallback(contentsDataList);
        } else {
            mApiDataProviderCallback.dailyRankListCallback(null);
        }
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     * データ作成処理に行数が必要なため SuppressWarnings しています。
     *
     * @param dailyRankMapList コンテンツリストデータ
     * @param channels チャンネル情報
     *
     * @return ListView表示用データ
     */
    @SuppressWarnings("OverlyLongMethod")
    private List<ContentsData> setRankingContentData(final List<Map<String, String>> dailyRankMapList, final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        List<ContentsData> rankingContentsDataList = new ArrayList<>();
        ContentsData rankingContentInfo;
        if (dailyRankMapList != null) {
            for (int i = 0; i < dailyRankMapList.size(); i++) {
                rankingContentInfo = new ContentsData();
                Map<String, String> map = dailyRankMapList.get(i);
                String title = map.get(JsonConstants.META_RESPONSE_TITLE);
                String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
                String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
                String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
                String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);
                rankingContentInfo.setRank(String.valueOf(i + 1));
                if (ContentUtils.IS_DTV_FLAG.equals(dtv)) {
                    rankingContentInfo.setThumURL(map.get(JsonConstants.META_RESPONSE_DTV_THUMB_448));
                    rankingContentInfo.setThumDetailURL(map.get(JsonConstants.META_RESPONSE_DTV_THUMB_640));
                } else {
                    rankingContentInfo.setThumURL(map.get(JsonConstants.META_RESPONSE_THUMB_448));
                    rankingContentInfo.setThumDetailURL(map.get(JsonConstants.META_RESPONSE_THUMB_640));
                }
                rankingContentInfo.setTitle(title);
                rankingContentInfo.setSearchOk(searchOk);
                rankingContentInfo.setRank(String.valueOf(i + 1));
                rankingContentInfo.setRatStar(map.get(JsonConstants.META_RESPONSE_RATING));
                rankingContentInfo.setContentsType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
                rankingContentInfo.setDtv(dtv);
                rankingContentInfo.setDtvType(dtvType);
                rankingContentInfo.setDispType(dispType);
                rankingContentInfo.setTvService(map.get(JsonConstants.META_RESPONSE_TV_SERVICE));
                rankingContentInfo.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
                rankingContentInfo.setContentsId(map.get(JsonConstants.META_RESPONSE_CRID));
                rankingContentInfo.setChannelNo(map.get(JsonConstants.META_RESPONSE_CHNO));
                rankingContentInfo.setPublishStartDate(map.get(JsonConstants.META_RESPONSE_PUBLISH_START_DATE));
                rankingContentInfo.setPublishEndDate(map.get(JsonConstants.META_RESPONSE_PUBLISH_END_DATE));
                setChannelInfo(map, rankingContentInfo, channels);
                rankingContentInfo.setAvailStartDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE)));
                rankingContentInfo.setAvailEndDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE)));
                rankingContentInfo.setVodStartDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_VOD_START_DATE)));
                rankingContentInfo.setVodEndDate(DateUtils.getSecondEpochTime(map.get(JsonConstants.META_RESPONSE_VOD_END_DATE)));
                //クリップリクエストデータ作成
                ClipRequestData requestData = new ClipRequestData();
                setClipRequestData(map, requestData, title, searchOk);
                //視聴通知判定生成
                String contentsType = map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
                String tvService = map.get(JsonConstants.META_RESPONSE_TV_SERVICE);
                String dTv = map.get(JsonConstants.META_RESPONSE_DTV);
                requestData.setIsNotify(dispType, contentsType, rankingContentInfo.getVodStartDate(), tvService, dTv);
                requestData.setDispType(dispType);
                requestData.setContentType(contentsType);
                requestData.setTableType(decisionTableType(contentsType, contentsType));
                if (mRequiredClipKeyList) {
                    boolean clipStatus;
                    // クリップ状態をコンテンツリストに格納
                    clipStatus = getClipStatus(dispType, contentsType, dTv,
                            requestData.getCrid(), requestData.getServiceId(),
                            requestData.getEventId(), requestData.getTitleId(), tvService);
                    rankingContentInfo.setClipStatus(clipStatus);
                    requestData.setClipStatus(clipStatus);
                }
                rankingContentInfo.setRequestData(requestData);
                rankingContentsDataList.add(rankingContentInfo);
            }
        }
        DTVTLogger.end();
        return rankingContentsDataList;
    }

    /**
     * 週間ランキングリストをWeeklyRankingActivityに送る.
     *
     * @param contentsDataList 週間ランキングリスト
     */
    private void sendWeeklyGenreRankListData(final List<ContentsData> contentsDataList) {
        if (contentsDataList != null) {
            mWeeklyRankingApiCallback.onWeeklyRankListCallback(contentsDataList);
        }
    }

    /**
     * 今日のランキングデータを取得する.
     */
    private void getDailyRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.DAILY_RANK_LAST_INSERT);
        mDailyRankList = null;
        // クリップキー一覧取得
        if (mRequiredClipKeyList) {
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.RequestParamType.TV));
        }

        //今日のランキング一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //DBからデータを取得したので、通信フラグはfalse
            mIsDailyRankWebApiError = false;

            //データをDBから取得する
            Handler handler = new Handler();
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_DAILY_RANKING_DATA);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        } else {
            if (!mIsCancel) {
                //DBは存在していないか期限切れなので、通信を行った事を示すフラグを立てる
                mIsDailyRankWebApiError = true;

                //通信クラスにデータ取得要求を出す
                mDailyRankWebClient = new DailyRankWebClient(mContext);
                UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
                int ageReq = userInfoDataProvider.getUserAge();
                if (!mDailyRankWebClient.getDailyRankApi(UPPER_PAGE_LIMIT, 1, WebApiBasePlala.FILTER_RELEASE, ageReq, this)) {
                    if (mApiDataProviderCallback != null) {
                        mApiDataProviderCallback.dailyRankListCallback(null);
                    }
                }
            } else {
                DTVTLogger.error("RankingTopDataProvider is stopping connect");
            }
        }
    }

    /**
     * 週間ランキングのデータ取得要求を行う.
     *
     * @param genreId ジャンルID
     */
    private void getWeeklyRankListData(final String genreId) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.WEEKLY_RANK_LAST_INSERT);

        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        //ジャンル指定ありの場合は必ずWeb取得（キャッシュしない）
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate) && (genreId == null || genreId.isEmpty())) {
            //DBを読みに行くので、通信を行った事を示すフラグはクリア
            mIsWeeklyRankWebApiError = false;

            //データをDBから取得する
            Handler handler = new Handler();
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_WEEKLY_RANKING_DATA);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        } else {
            if (!mIsCancel) {
                //DBは存在していないか期限切れなので、通信を行った事を示すフラグを立てる
                mIsWeeklyRankWebApiError = true;

                //通信クラスにデータ取得要求を出す
                mWeeklyRankWebClient = new WeeklyRankWebClient(mContext);
                UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
                int ageReq = userInfoDataProvider.getUserAge();
                if (!mWeeklyRankWebClient.getWeeklyRankApi(UPPER_PAGE_LIMIT, 1, WebApiBasePlala.FILTER_RELEASE, ageReq, genreId, this)) {
                    if (mApiDataProviderCallback != null) {
                        mApiDataProviderCallback.weeklyRankCallback(null);
                    }
                }
            } else {
                DTVTLogger.error("RankingTopDataProvider is stopping connect");
            }
        }
    }

    /**
     * ビデオランキングのデータ取得要求を行う.
     *
     * @param genreId ジャンルID
     */
    private void getVideoRankListData(final String genreId) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DateUtils.VIDEO_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        //ジャンル指定ありの場合は必ずWeb取得（キャッシュしない）
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate) && (genreId == null || genreId.isEmpty())) {
            //通信は行わないので、フラグはクリア
            mIsContentsListPerGenreWebApiError = true;

            //データをDBから取得する
            Handler handler = new Handler();
            try {
                DataBaseThread dataBaseThread = new DataBaseThread(handler, this, SELECT_VIDEO_RANKING_DATA);
                dataBaseThread.start();
            } catch (RuntimeException e) {
                DTVTLogger.debug(e);
            }
        } else {
            if (!mIsCancel) {
                //DBは存在していないか期限切れなので、通信を行った事を示すフラグを立てる
                mIsContentsListPerGenreWebApiError = true;

                //通信クラスにデータ取得要求を出す
                mContentsListPerGenreWebClient = new ContentsListPerGenreWebClient(mContext);
                UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
                int ageReq = userInfoDataProvider.getUserAge();
                //人気順でソートする
                String sort = JsonConstants.GENRE_PER_CONTENTS_SORT_PLAY_COUNT_DESC;
                if (!mContentsListPerGenreWebClient.getContentsListPerGenreApi(
                        UPPER_PAGE_LIMIT, 1, WebApiBasePlala.FILTER_RELEASE, ageReq, genreId, sort, this)) {
                    if (mApiDataProviderCallback != null) {
                        mApiDataProviderCallback.videoRankCallback(null);
                    } else if (mVideoRankingApiDataProviderCallback != null) {
                        mVideoRankingApiDataProviderCallback.onVideoRankListCallback(null);
                    }
                }
            } else {
                DTVTLogger.error("RankingTopDataProvider is stopping connect");
            }
        }
    }

    /**
     * デイリーランキングデータをDBに格納する.
     *
     * @param dailyRankList デイリーランキングデータ
     */
    private void setStructDB(final DailyRankList dailyRankList) {
        mDailyRankList = dailyRankList;
        //DB保存
        Handler handler = new Handler();
        try {
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, INSERT_DAILY_RANKING_DATA);
            dataBaseThread.start();
        } catch (RuntimeException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * 週間ランキングリストをDBに格納する.
     *
     * @param weeklyRankList 週間ランキングリスト
     */
    private void setStructDB(final WeeklyRankList weeklyRankList) {
        mWeeklyRankList = weeklyRankList;
        //DB保存
        Handler handler = new Handler();
        try {
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, INSERT_WEEKLY_RANKING_DATA);
            dataBaseThread.start();
        } catch (RuntimeException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * ビデオランキングリストをDBに格納する.
     *
     * @param videoRankList ビデオランキングリスト
     */
    private void setStructDB(final VideoRankList videoRankList) {
        mVideoRankList = videoRankList;
        //DB保存
        Handler handler = new Handler();
        try {
            DataBaseThread dataBaseThread = new DataBaseThread(handler, this, INSERT_VIDEO_RANKING_DATA);
            dataBaseThread.start();
        } catch (RuntimeException e) {
            DTVTLogger.debug(e);
        }
    }

    /**
     * 今日のテレビランキング取得用エラー情報のゲッター.
     *
     * @return 今日のテレビランキング取得用エラー情報
     */
    public ErrorState getDailyRankWebApiErrorState() {
        return mDailyRankWebApiErrorState;
    }

    /**
     * 週間テレビランキング取得用エラー情報のゲッター.
     *
     * @return 週間テレビランキング取得用エラー情報
     */
    public ErrorState getWeeklyRankWebApiErrorState() {
        return mWeeklyRankWebApiErrorState;
    }

    /**
     * ビデオランキング取得用エラー情報のゲッター.
     *
     * @return ビデオランキング取得用エラー情報
     */
    public ErrorState getContentsListPerGenreWebApiErrorState() {
        return mContentsListPerGenreWebApiErrorState;
    }

    /**
     * 通信を止める.
     */
    public void stopConnect() {
        DTVTLogger.start();
        mIsCancel = true;
        stopConnection();
        if (mDailyRankWebClient != null) {
            mDailyRankWebClient.stopConnection();
        }
        if (mWeeklyRankWebClient != null) {
            mWeeklyRankWebClient.stopConnection();
        }
        if (mContentsListPerGenreWebClient != null) {
            mContentsListPerGenreWebClient.stopConnect();
        }
        if (mScaledDownProgramListDataProvider != null) {
            mScaledDownProgramListDataProvider.stopConnect();
        }
    }

    /**
     * 通信を許可する.
     */
    public void enableConnect() {
        DTVTLogger.start();
        mIsCancel = false;
        enableConnection();
        if (mDailyRankWebClient != null) {
            mDailyRankWebClient.enableConnect();
        }
        if (mWeeklyRankWebClient != null) {
            mWeeklyRankWebClient.enableConnect();
        }
        if (mContentsListPerGenreWebClient != null) {
            mContentsListPerGenreWebClient.enableConnect();
        }
        if (mScaledDownProgramListDataProvider != null) {
            mScaledDownProgramListDataProvider.enableConnect();
        }
    }

    @SuppressWarnings("OverlyLongMethod")
    @Override
    public List<Map<String, String>> dbOperation(final int operationId) {
        HomeDataManager homeDataManager;
        switch (operationId) {
            case INSERT_DAILY_RANKING_DATA:
                DailyRankInsertDataManager dailyRankInsertDataManager = new DailyRankInsertDataManager(mContext);
                dailyRankInsertDataManager.insertDailyRankInsertList(mDailyRankList);
                break;
            case INSERT_WEEKLY_RANKING_DATA:
                // TODO ジャンル毎のキャッシュ登録について検討
                WeeklyRankInsertDataManager dataManager = new WeeklyRankInsertDataManager(mContext);
                dataManager.insertWeeklyRankInsertList(mWeeklyRankList);
                break;
            case INSERT_VIDEO_RANKING_DATA:
                // TODO ジャンル毎のキャッシュ登録について検討
                VideoRankInsertDataManager videoRankInsertDataManager = new VideoRankInsertDataManager(mContext);
                videoRankInsertDataManager.insertVideoRankInsertList(mVideoRankList);
                break;
            case SELECT_DAILY_RANKING_DATA:
                homeDataManager = new HomeDataManager(mContext);
                List<Map<String, String>> dailyRankList = homeDataManager.selectDailyRankListHomeData();
                if (channels == null || channels.size() == 0) {
                    mDailyRankMapList = dailyRankList;
                    getChannelList();
                } else {
                    sendDailyRankListData(dailyRankList);
                }
                break;
            case SELECT_WEEKLY_RANKING_DATA:
                homeDataManager = new HomeDataManager(mContext);
                List<Map<String, String>> weeklyRankList = homeDataManager.selectWeeklyRankListHomeData();
                mWeeklyRankList = new WeeklyRankList();
                mWeeklyRankList.setWeeklyRankList(weeklyRankList);
                if (channels == null || channels.size() == 0) {
                    mWeeklyRankMapList = weeklyRankList;
                    getChannelList();
                } else {
                    sendWeeklyRankList(mWeeklyRankList);
                }
                break;
            case SELECT_VIDEO_RANKING_DATA:
                RankingTopDataManager rankingTopDataManager = new RankingTopDataManager(mContext);
                List<Map<String, String>> videoRankList = rankingTopDataManager.selectVideoRankListData();
                mVideoRankList = new VideoRankList();
                mVideoRankList.setVrList(videoRankList);
                sendVideoRankList(mVideoRankList);
                break;
            default:
                break;
        }
        return null;
    }
}
