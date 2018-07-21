/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;

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
 * ビデオランキング.
 */
public class VideoRankingActivity extends BaseActivity implements
        TabItemLayout.OnClickTabTextListener,
        RankingTopDataProvider.VideoRankingApiDataProviderCallback,
        GenreListDataProvider.RankGenreListCallback {
     // region variable
    /** 標準タブ数. */
    private static final int DEFAULT_TAB_MAX = 4;
    /** 最後に表示したタブindex. */
    private static final String TAB_INDEX = "tabIndex";
    /** 最後に表示したタブindex. */
    private int mTabIndex = DEFAULT_TAB_INDEX;
    /** 最後に表示したタブindex.*/
    private static final int DEFAULT_TAB_INDEX = -1;
    /** FragmentFactory. */
    private RankingFragmentFactory mRankingFragmentFactory = null;
    /** タブ用レイアウト. */
    private TabItemLayout mTabLayout;
    /** ViewPager. */
    private ViewPager mViewPager;

    /** DataProvider. */
    private RankingTopDataProvider mRankingDataProvider;
    /** ビデオジャンル取得用プロパイダ. */
    private GenreListDataProvider mVideoGenreProvider = null;
    /** ジャンルリスト. */
    private ArrayList<GenreListMetaData> mGenreMetaDataList;
    /** タブ名. */
    private String[] mTabNames;
    /**ビデオランキングページャーアダプター.*/
    private RankingPagerAdapter mRankingPagerAdapter = null;
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
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        initView();
        if (savedInstanceState != null) {
            mTabIndex = savedInstanceState.getInt(TAB_INDEX);
            savedInstanceState.clear();
        }
        resetPaging(mViewPager, mRankingFragmentFactory);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DTVTLogger.start();
        enableStbStatusIcon(true);
        //コンテンツ詳細から戻ってきたときのみクリップ状態をチェックする
        RankingBaseFragment baseFragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
        if (baseFragment != null && baseFragment.isContentsDetailDisplay()) {
            baseFragment.setContentsDetailDisplay(false);
            if (null != baseFragment.getContentsAdapter()) {
                List<ContentsData> list;
                list = mRankingDataProvider.checkClipStatus(baseFragment.getData());
                baseFragment.enableContentsAdapterCommunication();
                baseFragment.updateContentsList(list);
                DTVTLogger.debug("VideoRankingActivity::Clip Status Update");
            }
        }
        if (baseFragment != null && baseFragment.getData().size() <= 0) {
            baseFragment.setContentsDetailDisplay(false);
            baseFragment.showProgressBar(false);
            baseFragment.showNoDataMessage(true, getString(R.string.common_empty_data_message));
        }
        super.sendScreenView(getString(R.string.google_analytics_screen_name_video_ranking));
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
    protected void onSaveInstanceState(final Bundle outState) {
        DTVTLogger.start();
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_INDEX, mViewPager.getCurrentItem());
        DTVTLogger.end();
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
                    baseFragment.clearData();
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
            if (baseFragment.getDataSize() == 0) {
                //Fragmentがデータを保持していない場合は再取得を行う
                baseFragment.showProgressBar(true);
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
            RankingBaseFragment fragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
            if (fragment != null) {
                fragment.showNoDataMessage(false, null);
            }
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
                    RankingBaseFragment fragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
                    if (fragment != null) {
                        fragment.showNoDataMessage(true, getString(R.string.get_contents_data_error_message));
                    }
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

    @Override
    public void onClipRegistResult() {
        DTVTLogger.start();
        //コンテンツリストに登録ステータスを反映する.
        setContentsListClipStatus(getContentsList());
        super.onClipRegistResult();
        DTVTLogger.end();
    }

    @Override
    public void onClipDeleteResult() {
        DTVTLogger.start();
        //コンテンツリストに削除ステータスを反映する.
        setContentsListClipStatus(getContentsList());
        super.onClipDeleteResult();
        DTVTLogger.end();
    }

    /**
     * コンテンツリストを返す.
     * @return List<ContentsData>
     */
    private List<ContentsData> getContentsList() {
        RankingBaseFragment fragment = mRankingFragmentFactory.createFragment(
                ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK, mViewPager.getCurrentItem());
        return fragment.getData();
    }

    /**
     * Viewの初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mViewPager = findViewById(R.id.vp_video_ranking_result);
    }

    /**
     * タブ初期化.
     */
    private void initTab() {
        if (mRankingFragmentFactory == null) {
            mRankingFragmentFactory = new RankingFragmentFactory();
        }
        if (mRankingPagerAdapter == null) {
            mRankingPagerAdapter = new RankingPagerAdapter(getSupportFragmentManager(),
                    ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK);
            mRankingPagerAdapter.setTabNames(mTabNames);
            mRankingPagerAdapter.setRankingFragmentFactory(mRankingFragmentFactory);
            mViewPager.setAdapter(mRankingPagerAdapter);
        }
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
                    if (fragment.getDataSize() < 1) {
                        resetPaging(mViewPager, mRankingFragmentFactory);
                    }
                }
                mTabLayout.setTab(position);
                getGenreData();
            }
        });
        mTabLayout = initTabData(mTabLayout, mTabNames);
        if (mTabIndex >= 0) {
            mViewPager.setCurrentItem(mTabIndex);
            mTabLayout.setTab(mTabIndex);
            mTabIndex = DEFAULT_TAB_INDEX;
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem());
            mTabLayout.setTab(mViewPager.getCurrentItem());
        }
        getGenreData();
    }

    /**
     * DataProviderキャンセル処理.
     */
    private void cancelDataProvider() {
        DTVTLogger.start();
        if (mRankingDataProvider != null) {
            mRankingDataProvider.stopConnect();
            mRankingDataProvider.setVideoRankingApiDataProviderCallback(null);
            //キャンセル後に mRankingDataProvider の使いまわしを防ぐため null を設定
            mRankingDataProvider = null;
        }
        if (mVideoGenreProvider != null) {
            mVideoGenreProvider.stopConnect();
            mVideoGenreProvider.setRankGenreListCallback(null);
            //キャンセル後に mVideoGenreProvider の使いまわしを防ぐため null を設定
            mVideoGenreProvider = null;
        }
        DTVTLogger.start();
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
            RankingBaseFragment fragment = getCurrentFragment(mViewPager, mRankingFragmentFactory);
            if (fragment != null) {
                fragment.showNoDataMessage(false, null);
                if (fragment.getDataSize() < 1) {
                    fragment.showProgressBar(true);
                    mRankingDataProvider.getVideoRankingData(mGenreMetaDataList.get(mViewPager.getCurrentItem()).getId());
                }
            } else {
                mRankingDataProvider.getVideoRankingData(mGenreMetaDataList.get(mViewPager.getCurrentItem()).getId());
            }
        }
    }
    /**
     * 取得結果の設定・表示.
     *
     * @param videoRankMapList ビデオランク情報
     */
    private void setShowVideoRanking(final List<ContentsData> videoRankMapList) {
        DTVTLogger.start();
        RankingBaseFragment fragment = mRankingFragmentFactory.createFragment(
        ContentsAdapter.ActivityTypeItem.TYPE_VIDEO_RANK,
        mViewPager.getCurrentItem());
        if (fragment != null) {
            fragment.showProgressBar(false);
        }
        if (null == videoRankMapList) {
            showGetDataFailedToast();

            if (fragment != null) {
                fragment.showNoDataMessage(true, getString(R.string.get_contents_data_error_message));
            }
            return;
        }
        if (videoRankMapList.isEmpty()) {
            if (fragment != null) {
                fragment.showNoDataMessage(true, getString(R.string.common_empty_data_message));
            }
            return;
        }

        //既に元のデータ以上の件数があれば足す物は無いので、更新せずに帰る
        if (fragment.getDataSize() >= videoRankMapList.size()) {
            return;
        }

        for (ContentsData videoRankData : videoRankMapList) {
            fragment.addData(videoRankData);
        }
        DTVTLogger.debug("Fragment.mData.size :" + String.valueOf(fragment.getDataSize()));
        fragment.noticeRefresh();
        DTVTLogger.end();
    }
}
