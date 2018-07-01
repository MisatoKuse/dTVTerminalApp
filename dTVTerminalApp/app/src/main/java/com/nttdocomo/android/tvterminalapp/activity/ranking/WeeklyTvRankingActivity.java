/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.RankingPagerAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.dataprovider.GenreListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.GenreListMetaData;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopGenreListDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRankingTopDataConnect;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 週間番組ランキング一覧表示画面.
 */
public class WeeklyTvRankingActivity extends BaseActivity implements
        TabItemLayout.OnClickTabTextListener,

        RankingTopDataProvider.WeeklyRankingApiDataProviderCallback,
        GenreListDataProvider.RankGenreListCallback {
     // region variable
    /** 標準タブ数. */
    private static final int DEFAULT_TAB_MAX = 4;
    /** 最後に表示したタブindex. */
    private static final String TAB_INDEX = "tabIndex";
    /** 最後に表示したタブindex. */
    private int mTabIndex = 0;
    /** フラグメント作成クラス. */
    private RankingFragmentFactory mRankingFragmentFactory = null;
    /** タブ用レイアウト. */
    private TabItemLayout mTabLayout;
    /** ViewPager. */
    private ViewPager mViewPager;

    /** ランキングデータ取得用データプロパイダ. */
    private RankingTopDataProvider mRankingDataProvider;
    /** ビデオジャンル取得用プロパイダ. */
    private GenreListDataProvider mVideoGenreProvider = null;
    /** ジャンルデータリスト. */
    private ArrayList<GenreListMetaData> genreMetaDataList;
    /** タブ名. */
    private String[] mTabNames;
    // endregion

    // region Activity LifeCycle
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_tv_ranking_main_layout);
        ImageView menuImageView = findViewById(R.id.header_layout_menu);
        menuImageView.setVisibility(View.VISIBLE);
        menuImageView.setOnClickListener(this);
        if (savedInstanceState != null) {
            mTabIndex = savedInstanceState.getInt(TAB_INDEX);
            savedInstanceState.clear();
        }

        //Headerの設定
        setTitleText(getString(R.string.weekly_tv_ranking_title));
        enableHeaderBackIcon(true);
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        initView();
        resetPaging(mViewPager, mRankingFragmentFactory);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_INDEX, mViewPager.getCurrentItem());
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
        RankingBaseFragment baseFragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
        if (baseFragment != null && baseFragment.isContentsDetailDisplay()) {
            baseFragment.setContentsDetailDisplay(false);
            if (null != baseFragment.getContentsAdapter()) {
                List<ContentsData> list;
                list = mRankingDataProvider.checkClipStatus(baseFragment.getData());
                baseFragment.updateContentsList(list);
                DTVTLogger.debug("WeeklyRankingActivity::Clip Status Update");
            }
        }
        super.sendScreenView(getString(R.string.google_analytics_screen_name_weekly_ranking));
        DTVTLogger.end();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();
        //通信を止める
        StopGenreListDataConnect stopVideoGenreConnect = new StopGenreListDataConnect();
        stopVideoGenreConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mVideoGenreProvider);
        StopRankingTopDataConnect stopRankingTopDataConnect = new StopRankingTopDataConnect();
        stopRankingTopDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mRankingDataProvider);

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
            tabCount = mTabNames.length;
        } else {
            tabCount = DEFAULT_TAB_MAX;
        }

        for (int i = 0; i < tabCount; ++i) { // タブの数だけ処理を行う
            if (mRankingFragmentFactory != null) {
                RankingBaseFragment b = mRankingFragmentFactory.createFragment(
                        ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK, i);
                if (null != b) {
                    b.clearData();
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
    // region implement
    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            //タブ移動時にそれまでのデータ取得要求はキャンセルする
            mViewPager.setCurrentItem(position);
            RankingBaseFragment fragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
            if (fragment != null) {
                fragment.showNoDataMessage(false, null);
            }
        }
        DTVTLogger.end();
    }

    /**
     * 取得条件用コールバック.
     */
    @Override
    public void onWeeklyRankListCallback(final List<ContentsData> contentsDataList) {
        //DbThreadからのコールバックではUIスレッドとして扱われないため
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setShowWeeklyRanking(contentsDataList);
            }
        });
    }

    @Override
    public void onRankGenreListCallback(final ArrayList<GenreListMetaData> genreMetaDataList) {
        DTVTLogger.start();
        if (genreMetaDataList == null) {
            DTVTLogger.warning("genreMetaDataList is null");
            showErrorMessage();
            return;
        }

        this.genreMetaDataList = genreMetaDataList;

        int totalSize = genreMetaDataList.size();
        DTVTLogger.debug("totalSize = " + totalSize);
        mTabNames = new String[totalSize];
        for (int i = 0; i < totalSize; i++) {
            mTabNames[i] = genreMetaDataList.get(i).getTitle();
        }
        initTab();
        DTVTLogger.end();
    }
    // endregion

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();

        //ジャンル取得のデータプロパイダがあれば通信を許可し、無ければ取得する
        if (mVideoGenreProvider != null) {
            mVideoGenreProvider.enableConnect();
        } else { //全てのデータが取得できていないケース
            requestgetGenreList();
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
            //ジャンルデータ取得済み、各Tabも設定済みの状態
            mRankingDataProvider = new RankingTopDataProvider(this,
                    ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK);
            //現在表示中のTabのデータ取得を行う
            if (genreMetaDataList != null && mViewPager != null) {
                mRankingDataProvider.getWeeklyRankingData(genreMetaDataList.get(mViewPager.getCurrentItem()).getId());
            } else { //ジャンルデータ or ViewPagerが存在しない場合はジャンルデータから取得しなおす
                requestgetGenreList();
                return;
            }
        }

        RankingBaseFragment baseFragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
        if (baseFragment != null) {
            if (baseFragment.getDataSize() == 0) { //Fragmentがデータを保持していない場合は再取得を行う
                baseFragment.showProgressBar(true);
                requestgetGenreList();
                return;
            }
            if (baseFragment.getRankingAdapter() != null) {
                baseFragment.enableContentsAdapterCommunication();
                baseFragment.noticeRefresh();
            }
        }
    }

    /**
     * Viewの初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mViewPager = findViewById(R.id.vp_weekly_ranking_result);
    }

    /**
     * タブの初期化.
     */
    private void initTab() {
        if (mRankingFragmentFactory == null) {
            mRankingFragmentFactory = new RankingFragmentFactory();
        }
        RankingPagerAdapter rankingPagerAdapter = new RankingPagerAdapter(getSupportFragmentManager(),
                ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK);
        rankingPagerAdapter.setTabNames(mTabNames);
        rankingPagerAdapter.setRankingFragmentFactory(mRankingFragmentFactory);
        mViewPager.setAdapter(rankingPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                //タブ移動時にデータ取得要求をキャンセルする
                cancelDataProvider();
                RankingBaseFragment fragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
                if (fragment != null) {
                    fragment.showProgressBar(false);
                    if(fragment.getDataSize() < 1) {
                        resetPaging(mViewPager, mRankingFragmentFactory);
                    }
                }
                mTabLayout.setTab(position);
                getGenreData();
            }
        });
        mTabLayout = initTabData(mTabLayout, mTabNames);
        mViewPager.setCurrentItem(mTabIndex);
        mTabLayout.setTab(mTabIndex);
        getGenreData();
    }

    /**
     * DataProviderキャンセル処理.
     */
    private void cancelDataProvider() {
        DTVTLogger.start();
        if (mRankingDataProvider != null) {
            mRankingDataProvider.stopConnect();
            mRankingDataProvider.setWeeklyRankingApiCallback(null);
            //キャンセル後に mRankingDataProvider の使いまわしを防ぐため null を設定
            mRankingDataProvider = null;
        }
        if (mVideoGenreProvider != null) {
            mVideoGenreProvider.stopConnect();
            mVideoGenreProvider.setRankGenreListCallback(null);
            //キャンセル後に mVideoGenreProvider の使いまわしを防ぐため null を設定
            mVideoGenreProvider = null;
        }
        DTVTLogger.end();
    }

    /**
     * タブ毎にリクエストを行う.
     */
    private void getGenreData() {
        DTVTLogger.start();
        if (mRankingDataProvider == null) {
            mRankingDataProvider = new RankingTopDataProvider(
                    this, ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK);
        }
        if (genreMetaDataList != null
                && genreMetaDataList.size() > 0) {
            RankingBaseFragment fragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
            if (fragment != null) {
                if (fragment.getDataSize() < 1) {
                    fragment.showProgressBar(true);
                    mRankingDataProvider.getWeeklyRankingData(genreMetaDataList.get(mViewPager.getCurrentItem()).getId());
                }
            } else {
                mRankingDataProvider.getWeeklyRankingData(genreMetaDataList.get(mViewPager.getCurrentItem()).getId());
            }
        }
        DTVTLogger.end();
    }

    /**
     * 取得結果の設定・表示.
     *
     * @param contentsDataList コンテンツデータ詳細.
     */
    private void setShowWeeklyRanking(final List<ContentsData> contentsDataList) {
        DTVTLogger.start();
        RankingBaseFragment fragment = mRankingFragmentFactory.createFragment(
                ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK, mViewPager.getCurrentItem());
        if (fragment != null) {
            fragment.showProgressBar(false);
        }
        if (null == contentsDataList) {
            //通信とJSON Parseに関してerror処理
            //エラーメッセージを取得する
            ErrorState errorState = mRankingDataProvider.getWeeklyRankWebApiErrorState();
            if (errorState != null) {
                String message = errorState.getErrorMessage();
                if (fragment != null) {
                    fragment.showNoDataMessage(true, getString(R.string.common_get_data_failed_message));
                }

               //有無で処理を分ける
                if (!TextUtils.isEmpty(message)) {
                    showGetDataFailedToast(message);
                    return;
                }
            }
            showGetDataFailedToast();
            return;
        }

        if (0 == contentsDataList.size()) {
            if (fragment != null) {
                fragment.showNoDataMessage(true, getString(R.string.common_empty_data_message));
            }
            return;
        }

        //既に元のデータ以上の件数があれば足す物は無いので、更新せずに帰る
        if (fragment.getDataSize() >= contentsDataList.size()) {
            return;
        }

        for (ContentsData info : contentsDataList) {
            fragment.addData(info);
        }
        DTVTLogger.end("Fragment.mData.size :" + String.valueOf(fragment.getDataSize()));
        fragment.noticeRefresh();
        DTVTLogger.end();
    }
    /**
     * データ取得要求.
     */
    private void requestgetGenreList() {
        mVideoGenreProvider = new GenreListDataProvider(this, this,
                ContentsAdapter.ActivityTypeItem.TYPE_WEEKLY_RANK);
        mVideoGenreProvider.getGenreListDataRequest();
    }

    /**
     * エラーメッセージを表示する.
     */
    private void showErrorMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //エラーメッセージを取得する
                ErrorState errorState = mVideoGenreProvider.getGenreListError();
                RankingBaseFragment fragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
                if (fragment != null) {
                    fragment.showProgressBar(false);
                    if (fragment.getDataSize() < 1) {
                        fragment.showNoDataMessage(true, getString(R.string.get_contents_data_error_message));
                    }
                }
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
    // endregion
}