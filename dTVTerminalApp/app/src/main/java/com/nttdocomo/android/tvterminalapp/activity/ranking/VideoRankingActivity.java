/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.RankingPagerAdapter;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRankingTopDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopGenreListDataConnect;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.GenreListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * ビデオランキング.
 */
public class VideoRankingActivity extends BaseActivity implements
        TabItemLayout.OnClickTabTextListener,

        RankingTopDataProvider.VideoRankingApiDataProviderCallback,
        GenreListDataProvider.RankGenreListCallback {
     // region variable
    /** 標準タブ数. */
    private static final int DEFAULT_TAB_MAX = 4;

    /** FragmentFactory. */
    private RankingFragmentFactory mRankingFragmentFactory = null;
    /** タブ用レイアウト. */
    private TabItemLayout mTabLayout;
    /** ViewPager. */
    private ViewPager mViewPager;
    /** リスト0件メッセージ. */
    private TextView mNoDataMessage;

    /** DataProvider. */
    private RankingTopDataProvider mRankingDataProvider;
    /** ビデオジャンル取得用プロパイダ. */
    private GenreListDataProvider mVideoGenreProvider = null;
    /** ジャンルリスト. */
    private ArrayList<GenreListMetaData> mGenreMetaDataList;
    /** タブ名. */
    private String[] mTabNames;
    // endregion

    // region Activity LifeCycle
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
        resetPaging(mViewPager, mRankingFragmentFactory);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
        RankingBaseFragment baseFragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
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

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        StopGenreListDataConnect stopVideoGenreConnect = new StopGenreListDataConnect();
        stopVideoGenreConnect.execute(mVideoGenreProvider);
        StopRankingTopDataConnect stopRankingTopDataConnect = new StopRankingTopDataConnect();
        stopRankingTopDataConnect.execute(mRankingDataProvider);

        //FragmentにContentsAdapterの通信を止めるように通知する
        RankingBaseFragment baseFragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
        if (baseFragment != null) {
            baseFragment.stopContentsAdapterCommunication();
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
                        ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK, i);
                if (null != baseFragment) {
                    baseFragment.mData.clear();
                }
            }
        }
    }
    // endregion

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
            mVideoGenreProvider = new GenreListDataProvider(this, this,
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
            if (mGenreMetaDataList != null && mGenreMetaDataList.size() > 0) {
                mRankingDataProvider.getVideoRankingData(mGenreMetaDataList.get(mViewPager.getCurrentItem()).getId());
            } else {
                //ジャンルデータ or ViewPagerが存在しない場合はジャンルデータから取得しなおす
                mVideoGenreProvider = new GenreListDataProvider(this, this,
                        ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
                mVideoGenreProvider.getGenreListDataRequest();
                return;
            }
        }

        RankingBaseFragment baseFragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
        if (baseFragment != null) {
            if (baseFragment.mData.size() == 0) {
                //Fragmentがデータを保持していない場合は再取得を行う
                mVideoGenreProvider = new GenreListDataProvider(this, this,
                        ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
                mVideoGenreProvider.getGenreListDataRequest();
                return;
            }
            if (baseFragment.getRankingAdapter() != null) {
                baseFragment.enableContentsAdapterCommunication();
                baseFragment.noticeRefresh();
            }
        }
    }

    // region implement
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
     * 取得条件"総合"用コールバック.
     *
     * @param videoRankList ビデオランクリスト
     */
    @Override
    public void onVideoRankListCallback(final List<ContentsData> videoRankList) {
        DTVTLogger.start();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (videoRankList != null) {
                    DTVTLogger.debug("videoRankList.size :" + videoRankList.size());
                    setShowVideoRanking(videoRankList);
                } else {
                    mNoDataMessage.setVisibility(View.VISIBLE);
                    //エラーメッセージを取得する
                    ErrorState errorState = mRankingDataProvider.getContentsListPerGenreWebApiErrorState();
                    if (errorState != null) {
                        String message = errorState.getErrorMessage();
                        //有無で処理を分ける
                        if (!TextUtils.isEmpty(message)) {
                            showGetDataFailedToast(message);
                            return;
                        }
                    }
                    showGetDataFailedToast();
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
            mGenreMetaDataList = genreMetaDataList;
            int totalSize = mGenreMetaDataList.size();
            mTabNames = new String[totalSize];
            for (int i = 0; i < totalSize; i++) {
                mTabNames[i] = mGenreMetaDataList.get(i).getTitle();
            }
            initTab();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //エラーメッセージを取得する
                    ErrorState errorState = mVideoGenreProvider.getGenreListError();
                    if (errorState != null) {
                        String message = errorState.getErrorMessage();
                        //有無で処理を分ける
                        if (!TextUtils.isEmpty(message)) {
                            showGetDataFailedToast(message);
                            return;
                        }
                    }
                    showGetDataFailedToast();
                }
            });
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
        rankingPagerAdapter.setTabNames(mTabNames);
        rankingPagerAdapter.setRankingFragmentFactory(mRankingFragmentFactory);
        mViewPager.setAdapter(rankingPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                resetPaging(mViewPager, mRankingFragmentFactory);
                mTabLayout.setTab(position);
                getGenreData();
            }
        });
        mTabLayout = initTabData(mTabLayout, mTabNames);
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
        if (mGenreMetaDataList != null && mGenreMetaDataList.size() > 0) {
            mNoDataMessage.setVisibility(View.GONE);
            mRankingDataProvider.getVideoRankingData(mGenreMetaDataList.get(mViewPager.getCurrentItem()).getId());
        }
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
                mViewPager.getCurrentItem());

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
}
