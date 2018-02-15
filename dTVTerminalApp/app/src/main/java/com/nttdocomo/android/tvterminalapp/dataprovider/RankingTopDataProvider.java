/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.dataprovider;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.nttdocomo.android.tvterminalapp.activity.ranking.DailyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.VideoRankingActivity;
import com.nttdocomo.android.tvterminalapp.activity.ranking.WeeklyTvRankingActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.DailyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.VideoRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.insert.WeeklyRankInsertDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.HomeDataManager;
import com.nttdocomo.android.tvterminalapp.datamanager.select.RankingTopDataManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.callback.VideoRankingApiDataProviderCallback;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListRequest;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipKeyListResponse;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.ClipRequestData;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.DailyRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VideoRankList;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WeeklyRankList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ClipUtils;
import com.nttdocomo.android.tvterminalapp.utils.DateUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.ContentsListPerGenreWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.DailyRankWebClient;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WebApiBasePlala;
import com.nttdocomo.android.tvterminalapp.webapiclient.hikari.WeeklyRankWebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.DAILY_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.VIDEO_RANK_LAST_INSERT;
import static com.nttdocomo.android.tvterminalapp.utils.DateUtils.WEEKLY_RANK_LAST_INSERT;

/**
 * ランキングTop画面用データプロバイダ.
 */
public class RankingTopDataProvider extends ClipKeyListDataProvider implements
        DailyRankWebClient.DailyRankJsonParserCallback,
        WeeklyRankWebClient.WeeklyRankJsonParserCallback,
        ContentsListPerGenreWebClient.ContentsListPerGenreJsonParserCallback {

    /**
     * コンテキスト.
     */
    private Context mContext;
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

    @Override
    public void onDailyRankJsonParsed(final List<DailyRankList> dailyRankLists) {
        if (dailyRankLists != null && dailyRankLists.size() > 0) {
            DailyRankList list = dailyRankLists.get(0);
            setStructDB(list);
            if (!mRequiredClipKeyList || mResponseEndFlag) {
                sendDailyRankListData(list.getDrList());
            } else {
                mDailyRankList = list;
            }
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            List<Map<String, String>> dailyRankList = homeDataManager.selectDailyRankListHomeData();
            sendDailyRankListData(dailyRankList);
        }
    }


    @Override
    public void onWeeklyRankJsonParsed(final List<WeeklyRankList> weeklyRankLists) {
        DTVTLogger.start();
        if (weeklyRankLists != null && weeklyRankLists.size() > 0) {
            WeeklyRankList list = weeklyRankLists.get(0);

            setStructDB(list);
            // コールバック判定
            if (!mRequiredClipKeyList || mResponseEndFlag) {
                if (mApiDataProviderCallback != null) {
                    DTVTLogger.debug("WeeklyRankList Callback");
                    sendWeeklyRankListData(list.getWrList());
                } else {
                    sendWeeklyGenreRankListData(setRankingContentData(list.getWrList()));
                }
            } else {
                mWeeklyRankList = list;
            }
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            List<Map<String, String>> weeklyRankList = homeDataManager.selectWeeklyRankListHomeData();
            sendWeeklyRankListData(weeklyRankList);
        }
        DTVTLogger.end();
    }

    @Override
    public void onContentsListPerGenreJsonParsed(final List<VideoRankList> contentsListPerGenre) {
        if (contentsListPerGenre != null && contentsListPerGenre.size() > 0) {
            VideoRankList list = contentsListPerGenre.get(0);
            setStructDB(list);
            if (!mRequiredClipKeyList || mResponseEndFlag) {
                sendVideoRankListData(list.getVrList());
            } else {
                mVideoRankList = list;
            }
        } else {
            //WEBAPIを取得できなかった時はDBのデータを使用
            List<Map<String, String>> videoRankList;
            RankingTopDataManager rankingTopDataManager = new RankingTopDataManager(mContext);
            videoRankList = rankingTopDataManager.selectVideoRankListData();
            sendVideoRankListData(videoRankList);
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
                sendDailyRankListData(mDailyRankList.getDrList());
            }
        }
        // 週間テレビランキング
        if (mContext instanceof WeeklyTvRankingActivity) {
            if (mWeeklyRankList != null) {
                if (mApiDataProviderCallback != null) {
                    DTVTLogger.debug("WeeklyRankList Callback");
                    sendWeeklyRankListData(mWeeklyRankList.getWrList());
                } else {
                    sendWeeklyGenreRankListData(setRankingContentData(mWeeklyRankList.getWrList()));
                }
            }
        }
        // ビデオランキング
        if (mContext instanceof VideoRankingActivity) {
            if (mVideoRankList != null) {
                if (mApiDataProviderCallback != null) {
                    DTVTLogger.debug("WeeklyRankList Callback");
                    sendVideoRankListData(mVideoRankList.getVrList());
                } else {
                    sendVideoGenreRankListData(setRankingContentData(mVideoRankList.getVrList()));
                }
            }
        }

        DTVTLogger.end();
    }

    @Override
    public void onVodClipKeyListJsonParsed(final ClipKeyListResponse clipKeyListResponse) {
        DTVTLogger.start();
        super.onVodClipKeyListJsonParsed(clipKeyListResponse);
        // コールバック判定
        // 今日のテレビランキング
        if (mContext instanceof DailyTvRankingActivity) {
            if (mDailyRankList != null) {
                sendDailyRankListData(mDailyRankList.getDrList());
            }
        }
        // 週間テレビランキング
        if (mWeeklyRankList != null) {
            if (mApiDataProviderCallback != null) {
                DTVTLogger.debug("WeeklyRankList Callback");
                sendWeeklyRankListData(mWeeklyRankList.getWrList());
            } else {
                sendWeeklyGenreRankListData(setRankingContentData(mWeeklyRankList.getWrList()));
            }
        }
        // ビデオランキング
        if (mContext instanceof VideoRankingActivity) {
            if (mVideoRankList != null) {
                if (mApiDataProviderCallback != null) {
                    DTVTLogger.debug("VideoRankList Callback");
                    sendVideoRankListData(mVideoRankList.getVrList());
                } else {
                    sendVideoGenreRankListData(setRankingContentData(mVideoRankList.getVrList()));
                }
            }
        }
        DTVTLogger.end();
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
            List<Map<String, String>> weeklyRankList = getWeeklyRankListData("");
            if (weeklyRankList != null && weeklyRankList.size() > 0) {
                // 週間テレビランキング取得
                sendWeeklyRankListData(weeklyRankList);
            }
            //ビデオのランキング
            List<Map<String, String>> videoRankList = getVideoRankListData("");
            if (videoRankList != null && videoRankList.size() > 0) {
                sendVideoRankListData(videoRankList);
            }
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
            List<Map<String, String>> dailyRankList = getDailyRankListData();
            if (dailyRankList != null && dailyRankList.size() > 0) {
                if (!mRequiredClipKeyList || mResponseEndFlag) {
                    sendDailyRankListData(dailyRankList);
                } else {
                    mDailyRankList = new DailyRankList();
                    mDailyRankList.setDrList(dailyRankList);
                }
            }
        } else {
            DTVTLogger.error("RankingTopDataProvider is stopping connect");
            mApiDataProviderCallback.dailyRankListCallback(null);
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
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.TV));
            // データを取得
            List<Map<String, String>> weeklyRankList = getWeeklyRankListData(genreId);

            if (weeklyRankList != null && weeklyRankList.size() > 0) {
                if (!mRequiredClipKeyList || mResponseEndFlag) {
                    List<ContentsData> contentsDataList = setRankingContentData(weeklyRankList);
                    sendWeeklyGenreRankListData(contentsDataList);
                } else {
                    mWeeklyRankList = new WeeklyRankList();
                    Bundle bundle = new Bundle();
                    bundle.putString(WeeklyRankWebClient.WEEKLY_RANK_CLIENT_BUNDLE_KEY, genreId);
                    mWeeklyRankList.setExtraData(bundle);
                    mWeeklyRankList.setWrList(weeklyRankList);
                }
            }
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
                getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.VOD));
            }

            List<Map<String, String>> videoRankList = getVideoRankListData(genreId);
            if (videoRankList != null && videoRankList.size() > 0) {
                if (!mRequiredClipKeyList || mResponseEndFlag) {
                    List<ContentsData> contentsDataList = setRankingContentData(videoRankList);
                    sendVideoGenreRankListData(contentsDataList);
                } else {
                    mVideoRankList = new VideoRankList();
                    Bundle bundle = new Bundle();
                    bundle.putString(WeeklyRankWebClient.WEEKLY_RANK_CLIENT_BUNDLE_KEY, genreId);
                    mVideoRankList.setExtraData(bundle);
                    mVideoRankList.setVrList(videoRankList);
                }
            }
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
        List<ContentsData> contentsDataList = setRankingContentData(list);
        mApiDataProviderCallback.dailyRankListCallback(contentsDataList);
    }

    /**
     * 週間ランキングリストをRankingTopActivityに送る.
     *
     * @param list 週間ランキング
     */
    private void sendWeeklyRankListData(final List<Map<String, String>> list) {
        List<ContentsData> contentsDataList = setRankingContentData(list);
        mApiDataProviderCallback.weeklyRankCallback(contentsDataList);
    }

    /**
     * ビデオランキングリストをRankingTopActivityに送る.
     *
     * @param list ビデオランキング
     */
    private void sendVideoRankListData(final List<Map<String, String>> list) {
        List<ContentsData> contentsDataList = setRankingContentData(list);
        if (mApiDataProviderCallback != null) {
            mApiDataProviderCallback.videoRankCallback(contentsDataList);
        }
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる.
     *
     * @param dailyRankMapList コンテンツリストデータ
     * @return ListView表示用データ
     */
    private List<ContentsData> setRankingContentData(final List<Map<String, String>> dailyRankMapList) {
        DTVTLogger.start();
        List<ContentsData> rankingContentsDataList = new ArrayList<>();

        ContentsData rankingContentInfo;

        for (int i = 0; i < dailyRankMapList.size(); i++) {
            rankingContentInfo = new ContentsData();

            Map<String, String> map = dailyRankMapList.get(i);

            String title = map.get(JsonConstants.META_RESPONSE_TITLE);
            String searchOk = map.get(JsonConstants.META_RESPONSE_SEARCH_OK);
            String linearEndDate = map.get(JsonConstants.META_RESPONSE_AVAIL_END_DATE);
            String dispType = map.get(JsonConstants.META_RESPONSE_DISP_TYPE);
            String dtv = map.get(JsonConstants.META_RESPONSE_DTV);
            String dtvType = map.get(JsonConstants.META_RESPONSE_DTV_TYPE);

            rankingContentInfo.setRank(String.valueOf(i + 1));
            rankingContentInfo.setThumURL(map.get(JsonConstants.META_RESPONSE_THUMB_448));
            rankingContentInfo.setTitle(title);
            rankingContentInfo.setTime(map.get(JsonConstants.META_RESPONSE_DISPLAY_START_DATE));
            rankingContentInfo.setSearchOk(searchOk);
            rankingContentInfo.setRank(String.valueOf(i + 1));
            rankingContentInfo.setRatStar(map.get(JsonConstants.META_RESPONSE_RATING));
            rankingContentInfo.setContentsType(map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE));
            rankingContentInfo.setDtv(dtv);
            rankingContentInfo.setDtvType(dtvType);
            rankingContentInfo.setDispType(dispType);
            rankingContentInfo.setClipExec(ClipUtils.isCanClip(dispType, searchOk, dtv, dtvType));
            rankingContentInfo.setContentsId(map.get(JsonConstants.META_RESPONSE_CID));

            //クリップリクエストデータ作成
            ClipRequestData requestData = new ClipRequestData();
            requestData.setCrid(map.get(JsonConstants.META_RESPONSE_CRID));
            requestData.setServiceId(map.get(JsonConstants.META_RESPONSE_SERVICE_ID));
            requestData.setEventId(map.get(JsonConstants.META_RESPONSE_EVENT_ID));
            requestData.setTitleId(map.get(JsonConstants.META_RESPONSE_TITLE_ID));
            requestData.setTitle(title);
            requestData.setRValue(map.get(JsonConstants.META_RESPONSE_R_VALUE));
            requestData.setLinearStartDate(map.get(JsonConstants.META_RESPONSE_AVAIL_START_DATE));
            requestData.setLinearEndDate(linearEndDate);
            requestData.setSearchOk(searchOk);

            //視聴通知判定生成
            String contentsType = map.get(JsonConstants.META_RESPONSE_CONTENT_TYPE);
            String tvService = map.get(JsonConstants.META_RESPONSE_TV_SERVICE);
            String dTv = map.get(JsonConstants.META_RESPONSE_DTV);
            requestData.setIsNotify(dispType, contentsType, linearEndDate, tvService, dTv);
            requestData.setDispType(dispType);
            requestData.setContentType(contentsType);
            requestData.setTableType(decisionTableType(contentsType, contentsType));

            if (mRequiredClipKeyList) {
                boolean clipStatus;
                // クリップ状態をコンテンツリストに格納
                clipStatus = getClipStatus(dispType, contentsType, dTv,
                        requestData.getCrid(), requestData.getServiceId(),
                        requestData.getEventId(), requestData.getTitleId());
                rankingContentInfo.setClipStatus(clipStatus);
                requestData.setClipStatus(clipStatus);
            }

            rankingContentInfo.setRequestData(requestData);
            rankingContentsDataList.add(rankingContentInfo);
            DTVTLogger.info("RankingContentInfo " + rankingContentInfo.getRank());
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
     * ビデオランキングリストをVideoRankingActivityに送る.
     *
     * @param list ビデオランキングリスト
     */
    private void sendVideoGenreRankListData(final List<ContentsData> list) {
        if (list != null) {
            mVideoRankingApiDataProviderCallback.onVideoRankListCallback(list);
        }
        DTVTLogger.end();
    }

    /**
     * 今日のランキングデータを取得する.
     *
     * @return 今日のランキングデータ
     */
    private List<Map<String, String>> getDailyRankListData() {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(DAILY_RANK_LAST_INSERT);
        mDailyRankList = null;
        // クリップキー一覧取得
        if (mRequiredClipKeyList) {
            getClipKeyList(new ClipKeyListRequest(ClipKeyListRequest.REQUEST_PARAM_TYPE.TV));
        }

        List<Map<String, String>> list = new ArrayList<>();
        //今日のランキング一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectDailyRankListHomeData();
        } else {
            if (!mIsCancel) {
                //通信クラスにデータ取得要求を出す
                mDailyRankWebClient = new DailyRankWebClient(mContext);
                UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
                int ageReq = userInfoDataProvider.getUserAge();
                int upperPageLimit = 100;
                mDailyRankWebClient.getDailyRankApi(upperPageLimit, 1, WebApiBasePlala.FILTER_RELEASE, ageReq, this);
            } else {
                DTVTLogger.error("RankingTopDataProvider is stopping connect");
            }
        }
        return list;
    }

    /**
     * 週間ランキングのデータ取得要求を行う.
     *
     * @param genreId ジャンルID
     * @return 週間ランキングリスト
     */
    private List<Map<String, String>> getWeeklyRankListData(final String genreId) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(WEEKLY_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            HomeDataManager homeDataManager = new HomeDataManager(mContext);
            list = homeDataManager.selectWeeklyRankListHomeData();
        } else {
            if (!mIsCancel) {
                //通信クラスにデータ取得要求を出す
                mWeeklyRankWebClient = new WeeklyRankWebClient(mContext);
                int upperPageLimit = 100;
                UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
                int ageReq = userInfoDataProvider.getUserAge();
                mWeeklyRankWebClient.getWeeklyRankApi(upperPageLimit, 1, WebApiBasePlala.FILTER_RELEASE, ageReq, genreId, this);
            } else {
                DTVTLogger.error("RankingTopDataProvider is stopping connect");
            }
        }
        return list;
    }

    /**
     * ビデオランキングのデータ取得要求を行う.
     *
     * @param genreId ジャンルID
     * @return ビデオランキングリスト
     */
    private List<Map<String, String>> getVideoRankListData(final String genreId) {
        DateUtils dateUtils = new DateUtils(mContext);
        String lastDate = dateUtils.getLastDate(VIDEO_RANK_LAST_INSERT);

        List<Map<String, String>> list = new ArrayList<>();
        //Vodクリップ一覧のDB保存履歴と、有効期間を確認
        if (!TextUtils.isEmpty(lastDate) && !dateUtils.isBeforeLimitDate(lastDate)) {
            //データをDBから取得する
            RankingTopDataManager rankingTopDataManager = new RankingTopDataManager(mContext);
            list = rankingTopDataManager.selectVideoRankListData();
        } else {
            if (!mIsCancel) {
                //通信クラスにデータ取得要求を出す
                mContentsListPerGenreWebClient = new ContentsListPerGenreWebClient(mContext);

                int upperPageLimit = 100;
                UserInfoDataProvider userInfoDataProvider = new UserInfoDataProvider(mContext);
                int ageReq = userInfoDataProvider.getUserAge();
                //人気順でソートする
                String sort = JsonConstants.GENRE_PER_CONTENTS_SORT_PLAY_COUNT_DESC;
                mContentsListPerGenreWebClient.getContentsListPerGenreApi(
                        upperPageLimit, 1, WebApiBasePlala.FILTER_RELEASE, ageReq, genreId, sort, this);
            } else {
                DTVTLogger.error("RankingTopDataProvider is stopping connect");
            }
        }
        return list;
    }

    /**
     * デイリーランキングデータをDBに格納する.
     *
     * @param dailyRankList デイリーランキングデータ
     */
    private void setStructDB(final DailyRankList dailyRankList) {
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(DAILY_RANK_LAST_INSERT);
        DailyRankInsertDataManager dataManager = new DailyRankInsertDataManager(mContext);
        dataManager.insertDailyRankInsertList(dailyRankList);
    }

    /**
     * 週間ランキングリストをDBに格納する.
     * @param weeklyRankList 週間ランキングリスト
     */
    private void setStructDB(final WeeklyRankList weeklyRankList) {
        // TODO ジャンル毎のキャッシュ登録について検討
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(WEEKLY_RANK_LAST_INSERT);
        WeeklyRankInsertDataManager dataManager = new WeeklyRankInsertDataManager(mContext);
        dataManager.insertWeeklyRankInsertList(weeklyRankList);
    }

    /**
     * ビデオランキングリストをDBに格納する.
     * @param videoRankList ビデオランキングリスト
     */
    private void setStructDB(final VideoRankList videoRankList) {
        // TODO ジャンル毎のキャッシュ登録について検討
        DateUtils dateUtils = new DateUtils(mContext);
        dateUtils.addLastDate(VIDEO_RANK_LAST_INSERT);
        VideoRankInsertDataManager dataManager = new VideoRankInsertDataManager(mContext);
        dataManager.insertVideoRankInsertList(videoRankList);
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
    }
}