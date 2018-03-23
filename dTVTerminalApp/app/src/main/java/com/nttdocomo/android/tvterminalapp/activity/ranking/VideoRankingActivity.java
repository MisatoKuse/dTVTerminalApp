/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.RankingPagerAdapter;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRankingTopDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopVideoGenreConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.VideoGenreProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.callback.VideoRankingApiDataProviderCallback;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * ビデオランキング.
 */
public class VideoRankingActivity extends BaseActivity implements VideoRankingApiDataProviderCallback, RankingFragmentScrollListener,
        VideoGenreProvider.RankGenreListCallback, TabItemLayout.OnClickTabTextListener {
    /**
     * タブ名.
     */
    private String[] mTabNames;
    /**
     * DataProvider.
     */
    private RankingTopDataProvider mRankingDataProvider;
    /**
     * FragmentFactory.
     */
    private RankingFragmentFactory mRankingFragmentFactory = null;
    /**
     * タブ用レイアウト.
     */
    private TabItemLayout mTabLayout;
    /**
     * ViewPager.
     */
    private ViewPager mViewPager;
    /**
     * ジャンルリスト.
     */
    private ArrayList<GenreListMetaData> genreMetaDataList;
    /**
     * ビデオジャンル取得用プロパイダ.
     */
    private VideoGenreProvider mVideoGenreProvider = null;

    /**
     * 標準タブ数.
     */
    private static final int DEFAULT_TAB_MAX = 4;
    /**
     * リスト0件メッセージ.
     */
    private TextView mNoDataMessage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_ranking_main_layout);
        DTVTLogger.start();

        //Headerの設定
        setTitleText(getString(R.string.video_ranking_title));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        initView();
        resetPaging();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
        RankingBaseFragment baseFragment = getCurrentFragment();
        if (baseFragment != null && baseFragment.mContentsDetailDisplay) {
            baseFragment.mContentsDetailDisplay = false;
            if (null != baseFragment.mContentsAdapter) {
                List<ContentsData> list;
                list = mRankingDataProvider.checkClipStatus(baseFragment.mData);
                baseFragment.updateContentsList(list);
                DTVTLogger.debug("VideoRankingActivity::Clip Status Update");
            }
        }
        DTVTLogger.end();
    }

    /**
     * ページのリセット.
     */
    private void resetPaging() {
        synchronized (this) {
            RankingBaseFragment baseFragment = getCurrentFragment();
            if (null != baseFragment && null != baseFragment.mData) {
                baseFragment.mData.clear();
                baseFragment.noticeRefresh();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //タブ数の取得
        int tabCount;
        if (mTabNames != null) {
            //通常はタブ名の数を取得して対処
            tabCount = mTabNames.length;
        } else {
            //取得できなかった場合はデフォルト数で指定
            tabCount = DEFAULT_TAB_MAX;
        }

        for (int i = 0; i < tabCount; ++i) { // タブの数だけ処理を行う
            if (mRankingFragmentFactory != null) {
                RankingBaseFragment baseFragment = mRankingFragmentFactory.createFragment(
                        ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK, i, this);
                if (null != baseFragment) {
                    baseFragment.mData.clear();
                }
            }
        }
    }

    /**
     * Viewの初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mViewPager = findViewById(R.id.vp_video_ranking_result);
        mNoDataMessage = findViewById(R.id.video_ranking_no_items);
    }

    /**
     * タブ初期化.
     */
    private void initTab() {
        if (mRankingFragmentFactory == null) {
            mRankingFragmentFactory = new RankingFragmentFactory();
        }
        RankingPagerAdapter rankingPagerAdapter = new RankingPagerAdapter(getSupportFragmentManager(),
                ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
        rankingPagerAdapter.setRankingFragmentScrollListener(this);
        rankingPagerAdapter.setTabNames(mTabNames);
        rankingPagerAdapter.setRankingFragmentFactory(mRankingFragmentFactory);
        mViewPager.setAdapter(rankingPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                resetPaging();
                mTabLayout.setTab(position);
                getGenreData();
            }
        });
        initTabData();
        getGenreData();
    }

    /**
     * タブ毎にリクエストを行う.
     */
    private void getGenreData() {
        if (mRankingDataProvider == null) {
            mRankingDataProvider =
                    new RankingTopDataProvider(this, ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
        }
        if (genreMetaDataList != null && genreMetaDataList.size() > 0) {
            mNoDataMessage.setVisibility(View.GONE);
            mRankingDataProvider.getVideoRankingData(genreMetaDataList.get(mViewPager.getCurrentItem()).getId());
        }
    }

    /**
     * tabのレイアウトを設定.
     */
    private void initTabData() {
        DTVTLogger.start();
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.VIDEO_RANKING_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_video_ranking_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mTabNames);
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            mViewPager.setCurrentItem(position);
            mNoDataMessage.setVisibility(View.GONE);
        }
        DTVTLogger.end();
    }

    /**
     * 取得結果の設定・表示.
     *
     * @param videoRankMapList ビデオランク情報
     */
    private void setShowVideoRanking(final List<ContentsData> videoRankMapList) {
        DTVTLogger.start();
        if (null == videoRankMapList) {
            showGetDataFailedToast();
            mNoDataMessage.setVisibility(View.VISIBLE);
            return;
        }
        if (0 == videoRankMapList.size()) {
            mNoDataMessage.setVisibility(View.VISIBLE);
            return;
        }

        RankingBaseFragment fragment = mRankingFragmentFactory.createFragment(
                ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK,
                mViewPager.getCurrentItem(), this);

        //既に元のデータ以上の件数があれば足す物は無いので、更新せずに帰る
        if (null != fragment.mData && fragment.mData.size() >= videoRankMapList.size()) {
            return;
        }

        for (ContentsData videoRankData : videoRankMapList) {
            fragment.mData.add(videoRankData);
        }
        if (fragment.mData != null) {
            DTVTLogger.debug("Fragment.mData.size :" + String.valueOf(fragment.mData.size()));
        }
        fragment.noticeRefresh();
        DTVTLogger.end();
    }

    @Override
    public void onScroll(final RankingBaseFragment fragment, final AbsListView absListView,
                         final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        synchronized (this) {
            RankingBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getRankingAdapter()) {
                return;
            }
            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount && 0 != totalItemCount) {
                DTVTLogger.debug("Activity::onScroll, paging, firstVisibleItem="
                        + firstVisibleItem + ", totalItemCount=" + totalItemCount
                        + ", visibleItemCount=" + visibleItemCount);

            }
        }
    }

    @Override
    public void onScrollStateChanged(
            final RankingBaseFragment fragment, final AbsListView absListView, final int scrollState) {
    }

    /**
     * Fragmentの取得.
     *
     * @return Fragment
     */
    private RankingBaseFragment getCurrentFragment() {

        int i = mViewPager.getCurrentItem();
        if (mRankingFragmentFactory != null) {
            return mRankingFragmentFactory.createFragment(ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK, i, this);
        }
        return null;
    }

    /**
     * 取得条件"総合"用コールバック.
     *
     * @param videoRankList ビデオランクリスト
     */
    @Override
    public void onVideoRankListCallback(final List<ContentsData> videoRankList) {
        DTVTLogger.start();
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (videoRankList != null) {
                    DTVTLogger.debug("ResponseDataSize :" + videoRankList.size());
                    setShowVideoRanking(videoRankList);
                    DTVTLogger.end();
                } else {
                    showGetDataFailedToast();
                    mNoDataMessage.setVisibility(View.VISIBLE);
                    DTVTLogger.debug("ResponseDataSize :0");
                }
            }
        });
        DTVTLogger.end();
    }

    @Override
    public void onRankGenreListCallback(final ArrayList<GenreListMetaData> genreMetaDataList) {
        DTVTLogger.start();
        if (genreMetaDataList != null) {
            this.genreMetaDataList = genreMetaDataList;
            int totalSize = genreMetaDataList.size();
            mTabNames = new String[totalSize];
            for (int i = 0; i < totalSize; i++) {
                mTabNames[i] = genreMetaDataList.get(i).getTitle();
            }
            initTab();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO:文言は仕様未確定
                    //Toast.makeText(VideoRankingActivity.this, "ジャンルデータ取得失敗しました", Toast.LENGTH_SHORT).show();
                    showGetDataFailedToast();
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        return !checkRemoteControllerView() && super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        //通信を許可する
        //ジャンル取得のデータプロパイダがあれば通信を許可し、無ければ取得する
        if (mVideoGenreProvider != null) {
            mVideoGenreProvider.enableConnect();
        } else {
            //全てのデータが取得できていないケース
            mVideoGenreProvider = new VideoGenreProvider(this, this,
                    ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
            mVideoGenreProvider.getGenreListDataRequest();
            //ジャンルデータを使用してランキングデータ取得を行うため、以降の処理を行わない
            if (mRankingDataProvider != null) {
                mRankingDataProvider.enableConnect();
            }
            return;
        }

        //ランキングデータ取得のデータプロパイダがあれば通信を許可し、無ければ取得する
        if (mRankingDataProvider != null) {
            mRankingDataProvider.enableConnect();
        } else {
            mRankingDataProvider =
                    new RankingTopDataProvider(this, ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
            if (genreMetaDataList != null && genreMetaDataList.size() > 0) {
                mRankingDataProvider.getVideoRankingData(genreMetaDataList.get(mViewPager.getCurrentItem()).getId());
            } else {
                //ジャンルデータ or ViewPagerが存在しない場合はジャンルデータから取得しなおす
                mVideoGenreProvider = new VideoGenreProvider(this, this,
                        ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
                mVideoGenreProvider.getGenreListDataRequest();
                return;
            }
        }

        RankingBaseFragment baseFragment = getCurrentFragment();
        if (baseFragment != null) {
            if (baseFragment.mData.size() == 0) {
                //Fragmentがデータを保持していない場合は再取得を行う
                mVideoGenreProvider = new VideoGenreProvider(this, this,
                        ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
                mVideoGenreProvider.getGenreListDataRequest();
                return;
            }
            if (baseFragment.getRankingAdapter() != null) {
                baseFragment.enableContentsAdapterCommunication();
                baseFragment.noticeRefresh();
                baseFragment.changeLastScrollUp(false);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        StopVideoGenreConnect stopVideoGenreConnect = new StopVideoGenreConnect();
        stopVideoGenreConnect.execute(mVideoGenreProvider);
        StopRankingTopDataConnect stopRankingTopDataConnect = new StopRankingTopDataConnect();
        stopRankingTopDataConnect.execute(mRankingDataProvider);

        //FragmentにContentsAdapterの通信を止めるように通知する
        RankingBaseFragment baseFragment = getCurrentFragment();
        if (baseFragment != null) {
            baseFragment.stopContentsAdapterCommunication();
        }
    }
}
