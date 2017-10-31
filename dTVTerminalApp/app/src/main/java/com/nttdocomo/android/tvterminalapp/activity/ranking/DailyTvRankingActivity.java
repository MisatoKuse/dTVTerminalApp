/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DailyTvRankingActivity extends BaseActivity implements View.OnClickListener,
        RankingTopDataProvider.ApiDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    // 最大表示件数
    private final static int NUM_PER_PAGE = 10;
    // タイムアウト時間
    private final static int LOAD_PAGE_DELAY_TIME = 1000;

    private ImageView mMenuImageView;
    private RankingTopDataProvider mRankingTopDataProvider;
    private ContentsAdapter mContentsAdapter;

    private View mLoadMoreView;
    private ListView mListView;
    private List mContentsList;

    private boolean mIsCommunicating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_tv_ranking_main_layout);
        mContentsList = new ArrayList();
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.daily_tv_ranking_title));
        resetPaging();

        initView();
        mRankingTopDataProvider = new RankingTopDataProvider(this);
        mRankingTopDataProvider.getRankingTopData();
    }

    /**
     * ListViewの表示
     */
    private void initView() {
        if (mContentsList == null) {
            mContentsList = new ArrayList();
        }
        mListView = findViewById(R.id.tv_rank_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mContentsAdapter = new ContentsAdapter(
                this,
                mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_DAILY_RANK
        );
        mListView.setAdapter(mContentsAdapter);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
    }

    /**
     * 再読み込み時の処理
     */
    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * 読み込み表示を行う
     *
     * @param bool 読み込み表示フラグ
     */
    private void displayMoreData(boolean bool) {
        if (null != mListView) {
            if (bool) {
                mListView.addFooterView(mLoadMoreView);
            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * ページングリセット
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            if (0 != getCurrentNumber() && null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    /**
     * 再読み込み実施フラグ設定
     *
     * @param bool 読み込み表示フラグ
     */
    private void setCommunicatingStatus(boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }


    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    /**
     * ページングを行った回数を取得
     *
     * @return ページング回数
     */
    private int getCurrentNumber() {
        if (null == mContentsList || 0 == mContentsList.size()) {
            return 0;
        } else if (mContentsList.size() < NUM_PER_PAGE) {
            return 1;
        }
        return mContentsList.size() / NUM_PER_PAGE;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount && 0 != totalItemCount) {
                DTVTLogger.debug(
                        "Activity::onScroll, paging, firstVisibleItem=" + firstVisibleItem
                                + ", totalItemCount=" + totalItemCount
                                + ", visibleItemCount=" + visibleItemCount);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() == mContentsAdapter.getCount() - 1) {
                if (mIsCommunicating) {
                    return;
                }
                DTVTLogger.debug("onScrollStateChanged, do paging");
                displayMoreData(true);
                setCommunicatingStatus(true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRankingTopDataProvider.getRankingTopData();
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mLoadMoreView == view) {
            return;
        }
        startActivity(TvPlayerActivity.class, null);
    }

    @Override
    public void dailyRankListCallback(List<Map<String, String>> dailyRankMapList) {
        setShowDailyRanking(dailyRankMapList);
    }

    /**
     * 取得結果の設定・表示
     */
    private void setShowDailyRanking(List<Map<String, String>> dailyRankMapList) {
        if (null == dailyRankMapList || 0 == dailyRankMapList.size()) {
            return;
        }
        List<ContentsData> rankingContentInfo = setDailyRankContentData(dailyRankMapList);

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1)
                * NUM_PER_PAGE && i < rankingContentInfo.size(); ++i) {
            DTVTLogger.debug("i = " + i);
            if (null != mContentsList) {
                mContentsList.add(rankingContentInfo.get(i));
            }
        }
        resetCommunication();
        mContentsAdapter.notifyDataSetChanged();
    }

    /**
     * 取得したリストマップをContentsDataクラスへ入れる
     *
     * @param dailyRankMapList コンテンツリストデータ
     * @return dataList 読み込み表示フラグ
     */
    private List<ContentsData> setDailyRankContentData(
            List<Map<String, String>> dailyRankMapList) {
        List<ContentsData> rankingContentsDataList = new ArrayList<>();

        ContentsData rankingContentInfo;

        for (int i = 0; i < dailyRankMapList.size(); i++) {
            rankingContentInfo = new ContentsData();
            rankingContentInfo.setRank(String.valueOf(i + 1));
            rankingContentInfo.setThumURL(dailyRankMapList.get(i)
                    .get(VideoRankJsonParser.VIDEORANK_LIST_THUMB));
            rankingContentInfo.setTitle(dailyRankMapList.get(i)
                    .get(VideoRankJsonParser.VIDEORANK_LIST_TITLE));
            rankingContentInfo.setRatStar(dailyRankMapList.get(i)
                    .get(VideoRankJsonParser.VIDEORANK_LIST_RATING));

            rankingContentsDataList.add(rankingContentInfo);
            DTVTLogger.info("RankingContentInfo " + rankingContentInfo.getRank());
        }

        return rankingContentsDataList;
    }

    @Override
    public void weeklyRankCallback(List<Map<String, String>> weeklyHashMap) {
        // NOP
    }

    @Override
    public void videoRankCallback(List<Map<String, String>> videoHashMap) {
        // NOP
    }
}
