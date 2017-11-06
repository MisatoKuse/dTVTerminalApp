/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.video;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoContentProvider;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.VideoRankJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideoContentListActivity extends BaseActivity implements View.OnClickListener,
        VideoContentProvider.videoContentApiDataProviderCallback,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    // 最大表示件数
    private final static int NUM_PER_PAGE = 10;
    // タイムアウト時間
    private final static int LOAD_PAGE_DELAY_TIME = 1000;

    private ImageView mMenuImageView;
    private VideoContentProvider mVideoContentProvider;
    private ContentsAdapter mContentsAdapter;

    private View mLoadMoreView;
    private ListView mListView;
    private List mContentsList;

    private boolean mIsCommunicating = false;

    // ダミーデーター
    public static final String GENRE_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_tv_ranking_main_layout);
        mContentsList = new ArrayList();
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.video_content_sub_genre_title));
        resetPaging();

        initView();
        mVideoContentProvider = new VideoContentProvider(this);
        mVideoContentProvider.getRankingTopData(GENRE_ID); //TODO　仮データ
    }

    /**
     * ListViewの表示
     */
    private void initView(){
        if (mContentsList == null) {
            mContentsList = new ArrayList();
        }
        mListView = findViewById(R.id.tv_rank_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mContentsAdapter = new ContentsAdapter(
                this,
                mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_LIST
        );
        mListView.setAdapter(mContentsAdapter);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
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

    /**
     * 取得結果の設定・表示
     */
    private void setShowVideoContent(List<Map<String, String>> videoContentMapList) {
        if (null == videoContentMapList || 0 == videoContentMapList.size()) {
            return;
        }
        List<ContentsData> rankingContentInfo = setVideoContentContentData(videoContentMapList);

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

    /*
    * 取得したリストマップをContentsDataクラスへ入れる
     */
    private List<ContentsData> setVideoContentContentData(
            List<Map<String, String>> videoContentMapList) {
        List<ContentsData> rankingContentsDataList = new ArrayList<>();

        ContentsData videoContentInfo;

        for (int i = 0; i < videoContentMapList.size(); i++) {
            videoContentInfo = new ContentsData();
            videoContentInfo.setRank(String.valueOf(i + 1));
            videoContentInfo.setThumURL(videoContentMapList.get(i)
                    .get(VideoRankJsonParser.VIDEORANK_LIST_THUMB));
            videoContentInfo.setTitle(videoContentMapList.get(i)
                    .get(VideoRankJsonParser.VIDEORANK_LIST_TITLE));
            videoContentInfo.setRatStar(videoContentMapList.get(i)
                    .get(VideoRankJsonParser.VIDEORANK_LIST_RATING));

            rankingContentsDataList.add(videoContentInfo);
            DTVTLogger.info("videoContentInfo " + videoContentInfo.getRank());
        }

        return rankingContentsDataList;
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

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        synchronized (this) {
            if (null == mContentsAdapter) {
                return;
            }
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    view.getLastVisiblePosition() == mContentsAdapter.getCount() - 1) {
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
                        mVideoContentProvider.getRankingTopData(GENRE_ID);//TODO　仮データ
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mLoadMoreView == view) {
            return;
        }
        startActivity(TvPlayerActivity.class, null);
    }

    @Override
    public void videoContentCallback(List<Map<String, String>> videoContentMapList) {
        setShowVideoContent(videoContentMapList);
    }
}
