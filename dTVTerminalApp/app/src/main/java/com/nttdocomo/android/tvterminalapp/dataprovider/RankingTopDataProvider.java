package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VideoRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.WeeklyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RankingTopDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.callback.VideoRankingApiDataProviderCallback;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingConstants;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WeeklyRankWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.DAILY_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VIDEO_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.WEEKLY_RANK_LAST_INSERT;

public class RankingTopDataProvider implements
        DailyRankWebClient.DailyRankJsonParserCallback,
        WeeklyRankWebClient.WeeklyRankJsonParserCallback,
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback {

    private Context mContext;
    // RakingTop画面用コールバック
    private ApiDataProviderCallback apiDataProviderCallback = null;
    // WeeklyRanking用コールバック
    private WeeklyRankingApiDataProviderCallback weeklyRankingApiCallback = null;
    // VideoRanking用コールバック
    private VideoRankingApiDataProviderCallback videoRankingApiDataProviderCallback = null;


    @Override
    public void onDailyRankJsonParsed(List<DailyRankList> dailyRankLists) {
        if (dailyRankLists != null && dailyRankLists.size() > 0) {
            DailyRankList list = dailyRankLists.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }


    @Override
    public void onWeeklyRankJsonParsed(List<WeeklyRankList> weeklyRankLists) {
        if (weeklyRankLists != null && weeklyRankLists.size() > 0) {
            WeeklyRankList list = weeklyRankLists.get(0);

            setStructDB(list);

        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    @Override
    public void onContentsListPerGenreJsonParsed(List<VideoRankList> contentsListPerGenre) {
        if (contentsListPerGenre != null && contentsListPerGenre.size() > 0) {
            VideoRankList list = contentsListPerGenre.get(0);
            setStructDB(list);
        } else {
            //TODO:WEBAPIを取得できなかった時の処理を記載予定
        }
    }

    /**
     * Ranking Top画面用データを返却するためのコールバック
     */
    public interface ApiDataProviderCallback {
        /**
         * デイリーランキング用コールバック
         */
        void dailyRankListCallback(List<Map<String, String>> dailyHashMap);

        /**
         * 週間ランキング用コールバック
         */
        void weeklyRankCallback(List<Map<String, String>> weeklyHashMap);

        /**
         * ビデオランキング用コールバック
         */
        void videoRankCallback(List<Map<String, String>> videoHashMap);
    }

    /**
     * WeeklyRanking用のコールバック
     */
    public interface WeeklyRankingApiDataProviderCallback {
        /**
         * 取得条件"総合"用コールバック
         */
        void weeklyRankSynthesisCallback(List<Map<String, String>> weeklyHashMap);

        /**
         * 取得条件"海外映画"用コールバック
         */
        void weeklyRankOverseasMovieCallback(List<Map<String, String>> weeklyHashMap);

        /**
         * 取得条件"国内映画"用コールバック
         */
        void weeklyRankDomesticMovieCallback(List<Map<String, String>> weeklyHashMap);

        /**
         * 取得条件"海外TV番組・ドラマ"用コールバック
         */
        void weeklyRankOverseasChannelCallback(List<Map<String, String>> weeklyHashMap);
    }

    /**
     * コンストラクタ
     *
     * @param context
     */
    public RankingTopDataProvider(Context context) {
        this.mContext = context;
        this.apiDataProviderCallback = (ApiDataProviderCallback) context;
    }

    /**
     * 週間・ビデオ用コンストラクタ
     */
    public RankingTopDataProvider(Context mContext, int mMode) {
        this.mContext = mContext;
        if (mMode == RankingConstants.RANKING_MODE_NO_OF_WEEKLY) {
            this.weeklyRankingApiCallback = (WeeklyRankingApiDataProviderCallback) mContext;
        } else if (mMode == RankingConstants.RANKING_MODE_NO_OF_VIDEO) {
            this.videoRankingApiDataProviderCallback = (VideoRankingApiDataProviderCallback) mContext;
        }
    }


    /**
     * RankingTopActivityからのデータ取得要求受付
     */
    public void getRankingTopData() {
        //今日のランキング
        List<Map<String, String>> dailyRankList = getDailyRankListData();
        if (dailyRankList != null && dailyRankList.size() > 0) {
            sendDailyRankListData(dailyRankList);
        }
        //週刊のランキング
        List<Map<String, String>> weeklyRankList
                = getWeeklyRankListData(RankingConstants.RANKING_GENRE_ID_SYNTHESIS);
        if (weeklyRankList != null && weeklyRankList.size() > 0) {
            sendWeeklyRankListData(weeklyRankList);
        }
        //ビデオのランキング
        List<Map<String, String>> videoRankList = getVideoRankListData(RankingConstants.RANKING_GENRE_ID_SYNTHESIS);
        if (videoRankList != null && videoRankList.size() > 0) {
            sendVideoRankListData(videoRankList);
        }
    }

    /**
     * WeeklyTvRankingActivityからのデータ取得要求
     */
    public void getWeeklyRankingData(int tabPageNo) {
        // TODO ジャンルIDを設定する
        String genreId = null;
        switch (tabPageNo) {
            case RankingConstants.RANKING_PAGE_NO_OF_SYNTHESIS: //総合
                // ジャンルID 指定なし
                genreId = RankingConstants.RANKING_GENRE_ID_SYNTHESIS;
                break;
            case RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_MOVIE: // 海外映画
                genreId = RankingConstants.RANKING_GENRE_ID_OVERSEAS_MOVIE;
                break;
            case RankingConstants.RANKING_PAGE_NO_OF_DOMESTIC_MOVIE: // 国内映画
                genreId = RankingConstants.RANKING_GENRE_ID_DOMESTIC_MOVIE;
                break;
            case RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_CHANNEL: // 海外TV番組・ドラマ
                genreId = RankingConstants.RANKING_GENRE_ID_OVERSEAS_CHANNEL;
                break;
            default:
                break;
        }
        // データを取得
        List<Map<String, String>> weeklyRankList = getWeeklyRankListData(genreId);
        if (weeklyRankList != null && weeklyRankList.size() > 0) {
            sendWeeklyGenreRankListData(weeklyRankList, genreId);
        }
    }

    /**
     * VideoRankingActivityからのデータ取得要求
     */
    public void getVideoRankingData(int tabPageNo) {
        // TODO ジャンルIDを設定する
        String genreId = null;
        switch (tabPageNo) {
            case RankingConstants.RANKING_PAGE_NO_OF_SYNTHESIS: //総合
                // ジャンルID 指定なし
                genreId = RankingConstants.RANKING_GENRE_ID_SYNTHESIS;
                break;
            case RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_MOVIE: // 海外映画
                genreId = RankingConstants.RANKING_GENRE_ID_OVERSEAS_MOVIE;
                break;
            case RankingConstants.RANKING_PAGE_NO_OF_DOMESTIC_MOVIE: // 国内映画
                genreId = RankingConstants.RANKING_GENRE_ID_DOMESTIC_MOVIE;
                break;
            case RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_CHANNEL: // 海外TV番組・ドラマ
                genreId = RankingConstants.RANKING_GENRE_ID_OVERSEAS_CHANNEL;
                break;
            default:
                break;
        }
        // データを取得
        List<Map<String, String>> videoRankList = getVideoRankListData(genreId);
        if (videoRankList != null && videoRankList.size() > 0) {
            sendVideoGenreRankListData(videoRankList, genreId);
        }
    }

    /**
     * 今日のランキングをRankingTopActivityに送る
     */
    public void sendDailyRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.dailyRankListCallback(list);
    }

    /**
     * 週間ランキングリストをRankingTopActivityに送る
     */
    public void sendWeeklyRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.weeklyRankCallback(list);
    }

    /**
     * ビデオランキングリストをRankingTopActivityに送る
     */
    public void sendVideoRankListData(List<Map<String, String>> list) {
        apiDataProviderCallback.videoRankCallback(list);
    }

    /**
     * 週間ランキングリストをWeeklyRankingActivityに送る
     */
    public void sendWeeklyGenreRankListData(List<Map<String, String>> list, String genreId) {
        DTVTLogger.start("response genreId : " + genreId);
        // TODO ジャンルID毎にコールバックを返す
        switch (genreId) {
            case "":
                weeklyRankingApiCallback.weeklyRankSynthesisCallback(list);
                break;
            default:
                break;
        }
    }

    /**
     * ビデオランキングリストをVideoRankingActivityに送る
     */
    public void sendVideoGenreRankListData(List<Map<String, String>> list, String genreId) {
        DTVTLogger.start("response genreId : " + genreId);
        // TODO ジャンルID毎にコールバックを返す
        switch (genreId) {
            case "":
                videoRankingApiDataProviderCallback.videoRankSynthesisCallback(list);
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * 今日のランキングデータを取得する
     */
    private List<Map<String, String>> getDailyRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DAILY_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //今日のランキング一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectDailyRankListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            DailyRankWebClient webClient = new DailyRankWebClient();
            int ageReq = 1;
            int upperPageLimit = 1;
            String lowerPageLimit = "";
            int pagerOffset = 1;
            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getDailyRankApi(ageReq, upperPageLimit,
                    lowerPageLimit, pagerOffset, this);
        }
        return list;
    }

    /**
     * 週間ランキングのデータ取得要求を行う
     *
     * @param genreId
     * @return 週間ランキングリスト
     */
    private List<Map<String, String>> getWeeklyRankListData(String genreId) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(WEEKLY_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectWeeklyRankListHomeData();
        } else {
            //通信クラスにデータ取得要求を出す
            WeeklyRankWebClient webClient = new WeeklyRankWebClient();
            int limit = 1;
            int offset = 1;
            String filter = "";
            int ageReq = 1;

            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getWeeklyRankApi(limit, offset,
                    filter, ageReq, genreId, this);
        }
        return list;
    }

    /**
     * ビデオランキングのデータ取得要求を行う
     *
     * @return ビデオランキングリスト
     */
    private List<Map<String, String>> getVideoRankListData(String genreId) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(VIDEO_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            RankingTopDataManager rankingTopDataManager = new RankingTopDataManager(mContext);
            list = rankingTopDataManager.selectVideoRankListData();
        } else {
            //通信クラスにデータ取得要求を出す
            ContentsListPerGenreWebClient webClient = new ContentsListPerGenreWebClient();
            int limit = 1;
            int offset = 1;
            String filter = "";
            int ageReq = 1;
            String type = "";
            String sort = "";

            //TODO: コールバック対応でエラーが出るようになってしまったのでコメント化
            webClient.getContentsListPerGenreApi(limit, offset,
                    filter, ageReq, genreId, type, sort, this);
        }
        return list;
    }

    /**
     * デイリーランキングデータをDBに格納する
     */
    public void setStructDB(DailyRankList dailyRankList) {

        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DAILY_RANK_LAST_INSERT);
        DailyRankInsertDataManager dataManager = new DailyRankInsertDataManager(mContext);
        dataManager.insertDailyRankInsertList(dailyRankList);
        sendDailyRankListData(dailyRankList.getDrList());
    }

    /**
     * 週間ランキングリストをDBに格納する
     */
    public void setStructDB(WeeklyRankList weeklyRankList) {
        // TODO ジャンル毎のキャッシュ登録について検討
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
        WeeklyRankInsertDataManager dataManager = new WeeklyRankInsertDataManager(mContext);
        dataManager.insertWeeklyRankInsertList(weeklyRankList);
        if (apiDataProviderCallback != null) {
            sendWeeklyRankListData(weeklyRankList.getWrList());
        } else {
            sendWeeklyGenreRankListData(weeklyRankList.getWrList(), weeklyRankList.getExtraData().getString("genreId"));
        }
    }

    /**
     * ビデオランキングリストをDBに格納する
     */
    public void setStructDB(VideoRankList videoRankList) {
        // TODO ジャンル毎のキャッシュ登録について検討
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(VIDEO_RANK_LAST_INSERT);
        VideoRankInsertDataManager dataManager = new VideoRankInsertDataManager(mContext);
        dataManager.insertVideoRankInsertList(videoRankList);
        sendVideoRankListData(videoRankList.getVrList());
    }
}
