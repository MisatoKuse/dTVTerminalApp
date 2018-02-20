/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.HikariTvChDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopHikariTvChDataConnect;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopRecommendDataConnect;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendFragmentFactory;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecommendActivity extends BaseActivity implements
        RecommendBaseFragmentScrollListener,
        RecommendDataProvider.RecommendApiDataProviderCallback,
        HikariTvChDataProvider.ApiDataProviderCallback,
        TabItemLayout.OnClickTabTextListener {

    private String[] mTabNames = null;
    private boolean mIsSearching = false;
    private Boolean mIsMenuLaunch = false;

    //チャンネル情報
    private List<Map<String, String>> mChannelMap;

    private LinearLayout mLinearLayout = null;
    private TabItemLayout mTabLayout = null;
    //ページャーのクラス(staticにしないと前回の値が維持され、データの更新に失敗する場合がある)
    private static ViewPager sRecommendViewPager = null;

    private RecommendDataProvider mRecommendDataProvider = null;
    private HikariTvChDataProvider mHikariTvChDataProvider = null;

    private int mPagingOffset = 1;

    private static final int LOAD_PAGE_DELAY_TIME = 500;
    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private static final int MARGIN_ZERO = 0;
    private static final int MARGIN_LEFT_TAB = 5;

    //チャンネルを指定
    private static final int SEARCH_CHANNEL = 3;

    private final static int TEXT_SIZE = 15;

    // レコメンドコンテンツ最大件数（システム制約）
    private final int MAX_SHOW_LIST_SIZE = 100;
    // 表示中レコメンドコンテンツ件数(staticにしないと前回の値が維持され、データの更新に失敗する場合がある)
    private static int sShowListSize = 0;
    // 表示中の最後の行を保持(staticにしないと前回の値が維持され、データの更新に失敗する場合がある)
    private static int sSearchLastItem = 0;
    // ページングの回数(staticにしないと前回の値が維持され、データの更新に失敗する場合がある)
    private static int sCntPaging = 0;
    // ページング判定
    private boolean mIsPaging = false;
    //アクティビティ初回起動フラグ
    private boolean mIsFirst = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_main_layout);

        //チャンネルリスト取得
        getChannelList();

        //Headerの設定
        setTitleText(getString(R.string.recommend_list_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        initData();
        initRecommendListView();
        setSearchStart(false);

        //初回起動フラグをONにする
        mIsFirst = true;
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //フォーカスを得て、初回起動だった場合の判定
        if (hasFocus && mIsFirst) {
            //画面の初期表示処理は、onCreateでは実行が早すぎて画面に表示されないので、こちらに移動
            requestRecommendData();

            //初回起動の処理が終了したので、falseとする
            mIsFirst = false;
        }
    }

    /**
     * データの初期化.
     */
    private void initData() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mTabNames = getResources().getStringArray(R.array.recommend_list_tab_names);
        mRecommendDataProvider = new RecommendDataProvider(this);

    }

    /**
     * チャンネルリストの取得.
     */
    private void getChannelList() {
        //チャンネルリストプロバイダーでデータを取得する
        mHikariTvChDataProvider = new HikariTvChDataProvider(this);
        mHikariTvChDataProvider.getChannelList(0, 0, "");

        try {
            mChannelMap = mHikariTvChDataProvider.dbOperation(SEARCH_CHANNEL);
        } catch (Exception e) {
            //発生する例外がExceptionのみの模様
            mChannelMap = new ArrayList();
        }
    }

    /**
     * 検索中フラグの変更.
     *
     * @param searchingFlag 検索中フラグ
     */
    private void setSearchStart(final boolean searchingFlag) {
        synchronized (this) {
            mIsSearching = searchingFlag;
        }
    }

    /**
     * データプロバイダへデータ取得要求.
     */
    private void requestRecommendData() {
        if (null == mRecommendDataProvider) {
            DTVTLogger.debug("RecommendActivity::setRecommendData, mRecommendDataProvider is null");
            return;
        }
        synchronized (this) {
            if (!mIsSearching) {
                setSearchStart(true);
            } else {
                return;
            }
        }
        if (null == sRecommendViewPager) {
            return;
        }

        int requestService = sRecommendViewPager.getCurrentItem();
        int startIndex = sShowListSize + 1;

        //戻り値を使用せず、データは必ずコールバック経由なので、falseを指定する
        mRecommendDataProvider.startGetRecommendData(
                requestService, startIndex,
                SearchConstants.RecommendList.requestMaxCount_Recommend, false);
    }

    /**
     * レコメンドのリストを初期化.
     */
    private void initRecommendListView() {
        //ページカウンターの初期化が必要になった
        sCntPaging = 0;

        if (null != sRecommendViewPager) {
            return;
        }
        sRecommendViewPager = findViewById(R.id.vp_recommend_list_items);
        initTabVIew();

        sRecommendViewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), this));
        // フリックによるtab切り替え
        sRecommendViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                mTabLayout.setTab(position);
                clearAllFragment();
                setPagingStatus(false);
                sShowListSize = 0;
                sCntPaging = 0;
                sSearchLastItem = 0;
                //ここでフラグをクリアしないと、以後の更新が行われなくなる場合がある
                setSearchStart(false);
                requestRecommendData();
                getChannelList();
            }
        });
    }

    /**
     * tabの関連Viewを初期化.
     */
    private void initTabVIew() {
        DTVTLogger.start();
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.RECOMMEND_LIST_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_recommend_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mTabNames);
        }
        DTVTLogger.end();
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (null != sRecommendViewPager) {
            DTVTLogger.debug("viewpager not null");
            sRecommendViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * フラグメントの取得.
     *
     * @return 現在のフラグメント
     */
    private RecommendBaseFragment getCurrentRecommendBaseFragment() {
        int currentPageNo = sRecommendViewPager.getCurrentItem();
        return RecommendFragmentFactory.createFragment(currentPageNo, this);
    }

    /**
     * レコメンド取得完了時の表示処理.
     *
     * @param resultInfoList レコメンド情報
     */
    public void recommendDataProviderSuccess(final List<ContentsData> resultInfoList) {
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();

        synchronized (this) {
            if (mIsPaging) {
                baseFragment.displayLoadMore(false);
                setPagingStatus(false);
            } else {
                baseFragment.clear();
            }
        }

        if (0 < resultInfoList.size()) {
            for (ContentsData info : resultInfoList) {
                baseFragment.mData.add(info);
                sShowListSize += 1;
            }

            DTVTLogger.debug("baseFragment.mData.size = " + baseFragment.mData.size());

            // フラグメントの更新
            baseFragment.notifyDataSetChanged(sRecommendViewPager.getCurrentItem());
            //ゼロ以下ならばゼロにする
            if (sSearchLastItem < 0) {
                DTVTLogger.debug("sSearchLastItem = " + sSearchLastItem);
                sSearchLastItem = 0;
            }

            //フラグメントにチャンネルリストを送信する
            baseFragment.setChannelData(mChannelMap);

            baseFragment.setSelection(sSearchLastItem);
            baseFragment.displayLoadMore(false);
            setSearchStart(false);
        }
    }

    /**
     * フラグメントクリア.
     */
    public void clearAllFragment() {

        if (null != sRecommendViewPager) {
            int sum = RecommendFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                RecommendBaseFragment baseFragment = RecommendFragmentFactory.createFragment(i, this);
                baseFragment.clear();
            }
        }
    }

    /**
     * データ取得失敗時の処理.
     */
    private void recommendDataProviderFinishNg() {
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        synchronized (this) {
            // ページング処理判定
            if (mIsPaging) {
                baseFragment.displayLoadMore(false);
                setPagingStatus(false);
            } else {
                baseFragment.clear();
                clearAllFragment();
            }
        }
        setSearchStart(false);
        DTVTLogger.debug("onSearchDataProviderFinishNg");
    }

    /**
     * ページング判定の変更.
     *
     * @param bool ページングの有無
     */
    private void setPagingStatus(final boolean bool) {
        synchronized (this) {
            mIsPaging = bool;
        }
    }

    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {
        //チャンネル情報取得後のコールバック
        try {
            mChannelMap = mHikariTvChDataProvider.dbOperation(SEARCH_CHANNEL);

            getCurrentRecommendBaseFragment().setChannelData(mChannelMap);
        } catch (Exception e) {
            //一般例外しか出ない模様となる
            DTVTLogger.debug(e);
        }
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        //チャンネル情報取得後のコールバック
        try {
            mChannelMap = mHikariTvChDataProvider.dbOperation(SEARCH_CHANNEL);

            getCurrentRecommendBaseFragment().setChannelData(mChannelMap);
        } catch (Exception e) {
            //一般例外しか出ない模様となる
            DTVTLogger.debug(e);
        }
    }

    /**
     * タブ専用アダプター.
     */
    private class TabAdapter extends FragmentStatePagerAdapter {
        /**
         * RecommendActivity.
         */
        private RecommendActivity mRecommendActivity = null;

        /**
         * コンストラクタ.
         *
         * @param fm FragmentManager
         * @param top RecommendActivity
         */
        TabAdapter(final FragmentManager fm, final RecommendActivity top) {
            super(fm);
            mRecommendActivity = top;
        }

        @Override
        public Fragment getItem(final int position) {
            synchronized (this) {
                return RecommendFragmentFactory.createFragment(position, mRecommendActivity);
            }
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            return mTabNames[position];
        }
    }

    @Override
    public void onScroll(final RecommendBaseFragment fragment, final AbsListView absListView,
                         final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        sSearchLastItem = firstVisibleItem + visibleItemCount - 1;

        int pageMax = (sCntPaging + 1) * SearchConstants.RecommendList.requestMaxCount_Recommend;
        DTVTLogger.debug("onScroll.first:" + firstVisibleItem
                + " .visible:" + visibleItemCount + " .total:" + totalItemCount
                + " dataSize:" + fragment.mData.size());
        if (MAX_SHOW_LIST_SIZE > fragment.mData.size() && // システム制約最大値 100件
                fragment.mData.size() != 0 && // 取得結果0件以外
                firstVisibleItem + visibleItemCount >= pageMax) { // 表示中の最下まで行ったかの判定
            sCntPaging += 1;
            setPagingStatus(true);
            fragment.displayLoadMore(true);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestRecommendData();
                }
            }, LOAD_PAGE_DELAY_TIME);
        }
    }

    /**
     * おすすめテレビ用コールバック.
     *
     * @param recommendContentInfoList テレビタブ用情報
     */
    @Override
    public void recommendChannelCallback(final List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("Chan Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
        if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }
    }

    /**
     * おすすめビデオ用コールバック.
     *
     * @param recommendContentInfoList ビデオタブ用情報
     */
    @Override
    public void recommendVideoCallback(final List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("vid Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
        if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }
    }

    /**
     * おすすめdTV用コールバック.
     *
     * @param recommendContentInfoList dTV用情報
     */
    @Override
    public void recommendDTVCallback(final List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("dtv Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
        if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }

    }

    /**
     * おすすめdアニメ用コールバック.
     *
     * @param recommendContentInfoList dアニメ用情報
     */
    @Override
    public void recommendDAnimeCallback(final List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("ani Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
        if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }
    }

    /**
     * おすすめdチャンネル用コールバック.
     *
     * @param recommendContentInfoList dTVチャンネル用情報
     */
    @Override
    public void recommendDChannelCallback(final List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("dCH Callback DataSize:"
                + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + sRecommendViewPager.getCurrentItem());
        if (sRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }
    }

    /**
     * 0件コールバック.
     */
    @Override
    public void recommendNGCallback() {
        recommendDataProviderFinishNg();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsMenuLaunch) {
                    //メニューから起動の場合はアプリ終了ダイアログを表示
                    showTips();
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //終了する場合はフラグをクリアする
        if (isFinishing()) {
            mIsFirst = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ヌルを入れて解放されやすくする
        sRecommendViewPager = null;

        //この組み合わせは、Androidのソースでも行われている正当な方法です。
        System.gc();
        System.runFinalization();
        System.gc();
    }


    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        if (mRecommendDataProvider != null) {
            mRecommendDataProvider.enableConnect();
        }
        if (mHikariTvChDataProvider != null) {
            mHikariTvChDataProvider.enableConnect();
        }
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment != null) {
            baseFragment.enableContentsAdapterCommunication();
            baseFragment.displayLoadMore(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();

        //おすすめ番組・ビデオの通信を止める
        StopRecommendDataConnect stopRecommendDataConnect = new StopRecommendDataConnect();
        StopHikariTvChDataConnect stopHikariTvChDataConnect = new StopHikariTvChDataConnect();
        stopRecommendDataConnect.execute(mRecommendDataProvider);
        stopHikariTvChDataConnect.execute(mHikariTvChDataProvider);

        //FragmentにContentsAdapterの通信を止めるように通知する
        RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
        if (baseFragment != null) {
            baseFragment.stopContentsAdapterCommunication();
        }
    }

}