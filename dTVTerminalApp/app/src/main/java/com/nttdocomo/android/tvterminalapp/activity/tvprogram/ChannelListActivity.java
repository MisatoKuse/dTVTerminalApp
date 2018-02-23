/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.DTvChDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.HikariTvChDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragment;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvBsChList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvTerChList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListListener;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.util.ArrayList;

/**
 * チャンネルリスト.
 */
public class ChannelListActivity extends BaseActivity implements
        View.OnClickListener, ChannelListFragment.ChannelListFragmentListener,
        DlnaTerChListListener, DlnaBsChListListener,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback, TabItemLayout.OnClickTabTextListener {

    /**
     * タブリスト.
     */
    private String[] mTabNames = null;
    /**
     * LastY.
     */
    private float mLastY = 0;
    /**
     * しきい値 20.0f.
     */
    private static final float sScrollThreshold = 20.0f;
    /**
     * 上にスクロール.
     */
    private boolean mIsScrollUp = false;
    /**
     * メニュー起動.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * ビューページャ.
     */
    private ViewPager mViewPager = null;
    /**
     * 共通タブ.
     */
    private TabItemLayout mTabLayout = null;
    /**
     * チャンネルリストフラグメントファクトリー.
     */
    private ChannelListFragmentFactory mFactory = null;
    /**
     * 録画一覧.
     */
    private DlnaProvRecVideo mDlnaProvRecVideo = null;
    /**
     * BSデジタル一覧.
     */
    private DlnaProvBsChList mDlnaProvBsChList = null;
    /**
     * 地上波一覧.
     */
    private DlnaProvTerChList mDlnaProvTerChList = null;
    /**
     * hikariTVチャンネルプロバイダー.
     */
    private HikariTvChDataProvider mHikariTvChDataProvider = null;
    /**
     * dTVチャンネルプロバイダー.
     */
    private DTvChDataProvider mDTvChDataProvider = null;
    /**
     * hikari.
     */
    private final int CHANNEL_LIST_TAB_HIKARI = 0;
    /**
     * 地上波.
     */
    private final int CHANNEL_LIST_TAB_TER = 1;
    /**
     *　BS.
     */
    private final int CHANNEL_LIST_TAB_BS = 2;
    /**
     *　ｄTV.
     */
    private final int CHANNEL_LIST_TAB_DTV = 3;
    /**
     *　タブ延長タイム.
     */
    private final int CHANNEL_LIST_TAB_DELAY_TIME = 1300;
    /**
     *　現在タイプ.
     */
    private ChannelListAdapter.ChListDataType mCurrentType = ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_INVALID;
    /**
     * ハンドラー.
     */
    private Handler mHandle = new Handler();

    /**
     * コンストラクタ.
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        DTVTLogger.start();
        super.onCreate(savedInstanceState);

        //Headerの設定
        setTitleText(getString(R.string.channel_list_activity_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableGlobalMenuIcon(true);
        enableStbStatusIcon(true);

        setContentView(R.layout.channel_list_main_layout);
        initView();
        initData();
        firstMore();
        DTVTLogger.end();
    }

    /**
     * 初回の場合、Loading...を表示.
     */
    private void firstMore() {
        DTVTLogger.start();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                displayMore(true, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
            }
        }, 200);
        DTVTLogger.end();
    }

    @Override
    public void onStartCommunication() {
        DTVTLogger.start();
        super.onStartCommunication();
        if (null == mDlnaProvRecVideo) {
            mDlnaProvRecVideo = new DlnaProvRecVideo();
        }
        if (null == mDlnaProvBsChList) {
            mDlnaProvBsChList = new DlnaProvBsChList();
        }
        if (null == mDlnaProvTerChList) {
            mDlnaProvTerChList = new DlnaProvTerChList();
        }
        if (null == mHikariTvChDataProvider) {
            mHikariTvChDataProvider = new HikariTvChDataProvider(this);
        }
        if (null == mDTvChDataProvider) {
            mDTvChDataProvider = new DTvChDataProvider(this);
        }
        if (null != mHikariTvChDataProvider) {
            mHikariTvChDataProvider.enableConnect();
        }
        if (null != mDTvChDataProvider) {
            mDTvChDataProvider.enableConnect();
        }
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
        if (!mDlnaProvTerChList.start(dlnaDmsItem, getActivity())) {
            onError("Get Ter channel list datas failed");
        }
        if (!mDlnaProvBsChList.start(dlnaDmsItem, getActivity())) {
            onError("Get Bs channel list datas failed");
        }
        synchronized (mCurrentType) {
            getChListData();
        }
        DTVTLogger.end();
    }

    /**
     * CHリストデータ取得.
     */
    private void getChListData() {
        DTVTLogger.start();
        switch (mCurrentType) {
            case CH_LIST_DATA_TYPE_BS:
                getBsData();
                break;
            case CH_LIST_DATA_TYPE_TER:
                getTerData();
                break;
            case CH_LIST_DATA_TYPE_HIKARI:
                getHikariData();
                break;
            case CH_LIST_DATA_TYPE_DTV:
                getDtvData();
                break;
            case CH_LIST_DATA_TYPE_INVALID:
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * onPause処理.
     */
    @Override
    protected void onPause() {
        DTVTLogger.start();
        super.onPause();
        if (mDlnaProvRecVideo != null) {
            mDlnaProvRecVideo.stopListen();
        }
        if (mDlnaProvTerChList != null) {
            mDlnaProvTerChList.stopListen();
        }
        if (null != mHikariTvChDataProvider) {
            mHikariTvChDataProvider.stopConnect();
        }
        if (null != mDTvChDataProvider) {
            mDTvChDataProvider.stopConnect();
        }
        mHandle.removeCallbacks(mRunnableBs);
        mHandle.removeCallbacks(mRunnableDtv);
        mHandle.removeCallbacks(mRunnableTer);
        mHandle.removeCallbacks(mRunnableHikari);
        displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
        displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
        displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
        clearAllFrames();
        DTVTLogger.end();
    }

    /**
     * Clear datas for all frames.
     */
    private void clearAllFrames() {
        if (null == mTabNames || null == mFactory) {
            return;
        }
        for (int i = 0; i < mTabNames.length; ++i) {
            ChannelListAdapter.ChListDataType type = ChannelListAdapter
                    .ChListDataType.values()[ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_INVALID.ordinal() + i];
            ChannelListFragment fragment = mFactory.createFragment(i, this, type);
            if (null == fragment) {
                continue;
            }
            fragment.clearDatas();
            fragment.noticeRefresh();
        }
    }

    /**
     * データ初期化.
     */
    private void initData() {
        DTVTLogger.start();
        mFactory = new ChannelListFragmentFactory();
        DTVTLogger.end();
    }

    /**
     * view初期化.
     */
    private void initView() {
        DTVTLogger.start();
        //テレビアイコンをタップされたらリモコンを起動する
        View view = findViewById(R.id.header_stb_status_icon);
        if (null == view) {
            return;
        }
        view.setOnClickListener(mRemoteControllerOnClickListener);
        //mViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
        initChannelListTab();
        ChannelListPagerAdapter adp = new ChannelListPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
        mViewPager.setAdapter(adp);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                mTabLayout.setTab(position);
            }
        });
        DTVTLogger.end();
    }

    /**
     * Loading...を表示.
     *
     * @param isShow 表示するかどうか
     * @param tabNo  タブ番号
     * @param type   タイプ
     */
     private void displayMore(final boolean isShow, final int tabNo, final ChannelListAdapter.ChListDataType type) {
        DTVTLogger.start();
        final ChannelListFragment fragment = mFactory.createFragment(tabNo, this, type);
        fragment.displayMoreData(isShow);
        DTVTLogger.end();
    }

    /**
     * Bsデータスレッド.
     */
    private final Runnable mRunnableBs = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
            //STBにチャンネル機能を提供していないで、仮データを使うために、本番ソースを一時コメントしている。
            //本番ソース begin
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
            if (mDlnaProvBsChList.start(dlnaDmsItem, getActivity())) {
                boolean ret = mDlnaProvBsChList.browseChListDms();
                if (!ret) {
                    onError("Get BS channel list datas failed");
                }
            }
            DTVTLogger.end();
        }
    };

    /**
     * Bsデータを取得.
     */
    private void getBsData() {
        DTVTLogger.start();
        displayMore(true, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        mHandle.postDelayed(mRunnableBs, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Terデータスレッド.
     */
    private final Runnable mRunnableTer = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
            //STBにチャンネル機能を提供していないで、仮データを使うために、本番ソースを一時コメントしている。
            //本番ソース begin
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
            if (mDlnaProvTerChList.start(dlnaDmsItem, getActivity())) {
                boolean ret = mDlnaProvTerChList.browseChListDms();
                if (!ret) {
                    onError("Get Ter channel list datas failed");
                }
            }
            DTVTLogger.end();
        }
    };

    /**
     * Terデータ取得.
     */
    private void getTerData() {
        DTVTLogger.start();
        displayMore(true, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
        mHandle.postDelayed(mRunnableTer, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Hikariデータ取得.
     */
    private void getHikariData() {
        DTVTLogger.start();
        //test b
        displayMore(true, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
        //test e
        mHandle.postDelayed(mRunnableHikari, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Hikariデータスレッド.
     */
    private final Runnable mRunnableHikari = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
            mHikariTvChDataProvider.getChannelList(0, 0, "");
            DTVTLogger.end();
        }
    };

    /**
     * DTvデータスレッド.
     */
    private final Runnable mRunnableDtv = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
            mDTvChDataProvider.getChannelList(0, 0, "");
            DTVTLogger.end();
        }
    };

    /**
     * DTvデータ取得.
     */
    private void getDtvData() {
        DTVTLogger.start();
        displayMore(true, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
        mHandle.postDelayed(mRunnableDtv, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Paging処理.
     *
     * @param tabPosition tabPosition
     */
    private void onPageChange(final int tabPosition) {
        DTVTLogger.start();
        clearOthers(tabPosition);
        mLastY = 0;
        mIsScrollUp = false;
        DTVTLogger.end();
    }

    /**
     * 内容削除処理.
     *
     * @param tabPosition tabPosition
     */
    private void clearOthers(final int tabPosition) {
        DTVTLogger.start();
        if (null == mHandle) {
            return;
        }
        switch (tabPosition) {
            case CHANNEL_LIST_TAB_HIKARI:
                mHandle.removeCallbacks(mRunnableBs);
                mHandle.removeCallbacks(mRunnableDtv);
                mHandle.removeCallbacks(mRunnableTer);
                displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                break;
            case CHANNEL_LIST_TAB_DTV:
                mHandle.removeCallbacks(mRunnableBs);
                mHandle.removeCallbacks(mRunnableHikari);
                mHandle.removeCallbacks(mRunnableTer);
                displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                break;
            case CHANNEL_LIST_TAB_TER:
                mHandle.removeCallbacks(mRunnableBs);
                mHandle.removeCallbacks(mRunnableDtv);
                mHandle.removeCallbacks(mRunnableHikari);
                displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                break;
            case CHANNEL_LIST_TAB_BS:
                mHandle.removeCallbacks(mRunnableDtv);
                mHandle.removeCallbacks(mRunnableHikari);
                mHandle.removeCallbacks(mRunnableTer);
                displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                break;
            default:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * tab関連Viewの初期化.
     */
    private void initChannelListTab() {
        Resources res = getResources();
        mTabNames = res.getStringArray(R.array.channel_list_tab_names);
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.SEARCH_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_channel_list_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mTabNames);
        }
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.debug("position = " + position);
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            mViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * Activityを取得.
     *
     * @return this this
     */
    private ChannelListActivity getActivity() {
        return this;
    }

    /**
     * Scroll処理.
     *
     * @param fragment         fragment
     * @param absListView      absListView
     * @param firstVisibleItem firstVisibleItem
     * @param visibleItemCount visibleItemCount
     * @param totalItemCount   totalItemCount
     */
    @Override
    public void onScroll(final ChannelListFragment fragment, final AbsListView absListView, final
    int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
    }

    /**
     * MotionEvent処理.
     *
     * @param event event
     * @return super.dispatchTouchEvent
     */
    @Override
    public boolean dispatchTouchEvent(final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float y = event.getY();
                mIsScrollUp = (y - mLastY) <= sScrollThreshold;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * ハンドラー.
     */
    private final Handler mHandler = new Handler();

    /**
     * Event処理.
     *
     * @param fragment    fragment
     * @param absListView absListView
     * @param scrollState scrollState
     */
    @Override
    public void onScrollStateChanged(final ChannelListFragment fragment, final AbsListView absListView, final int scrollState) {
        DTVTLogger.start();
        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && absListView
                    .getLastVisiblePosition() == fragment.getDataCount() - 1 && mIsScrollUp) {
                mIsScrollUp = false;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        switch (fragment.getChListDataType()) {
                            case CH_LIST_DATA_TYPE_BS:
                                getBsData();
                                break;
                            case CH_LIST_DATA_TYPE_TER:
                                getTerData();
                                break;
                            case CH_LIST_DATA_TYPE_HIKARI:
                                getHikariData();
                                break;
                            case CH_LIST_DATA_TYPE_DTV:
                                getDtvData();
                                break;
                            case CH_LIST_DATA_TYPE_INVALID:
                                break;
                        }
                    }
                });
            }
        }
        DTVTLogger.end();
    }

    /**
     * データ取得.
     *
     * @param isVisibleToUser isVisibleToUser
     * @param fragment        fragment
     */
    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser, final ChannelListFragment fragment) {
        DTVTLogger.start();
        if (!isVisibleToUser) {
            fragment.clearDatas();
            //fragment.noticeRefresh();
            noticeRefresh(fragment);
            DTVTLogger.end();
            return;
        }

        ChannelListAdapter.ChListDataType type = fragment.getChListDataType();
        setCurrentType(type);
        switch (type) {
            case CH_LIST_DATA_TYPE_BS:
                getBsData();
                onPageChange(CHANNEL_LIST_TAB_BS);
                break;
            case CH_LIST_DATA_TYPE_TER:
                getTerData();
                onPageChange(CHANNEL_LIST_TAB_TER);
                break;
            case CH_LIST_DATA_TYPE_HIKARI:
                getHikariData();
                onPageChange(CHANNEL_LIST_TAB_HIKARI);
                break;
            case CH_LIST_DATA_TYPE_DTV:
                getDtvData();
                onPageChange(CHANNEL_LIST_TAB_DTV);
                break;
            case CH_LIST_DATA_TYPE_INVALID:
                break;
        }
        DTVTLogger.end();
    }

    /**
     * UI更新.
     *
     * @param fragment fragment
     */
    private void updateUi(final ChannelListFragment fragment) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.noticeRefresh();
                fragment.displayMoreData(false);
            }
        });
    }

    /**
     * Terデータcallback.
     *
     * @param curInfo current recorded video list
     */
    @Override
    public void onListUpdate(final DlnaTerChListInfo curInfo) {
        DTVTLogger.start();
        int pos = mViewPager.getCurrentItem();
        final ChannelListFragment fragment = mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
        ArrayList<Object> tmp = new ArrayList<>();
        for (int i = 0; i < curInfo.size(); ++i) {
            Object item = curInfo.get(i);
            tmp.add(item);
        }
        paging(fragment, tmp);
        updateUi(fragment);
        DTVTLogger.end();
    }

    /**
     * Bsデータcallback.
     *
     * @param curInfo current recorded video list
     */
    @Override
    public void onListUpdate(final DlnaBsChListInfo curInfo) {
        DTVTLogger.start();
        int pos = mViewPager.getCurrentItem();
        final ChannelListFragment fragment = mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        ArrayList<Object> tmp = new ArrayList<>();
        for (int i = 0; i < curInfo.size(); ++i) {
            Object item = curInfo.get(i);
            tmp.add(item);
        }
        paging(fragment, tmp);
        updateUi(fragment);
        DTVTLogger.end();
    }

    /**
     * 実現しない.
     *
     * @return null
     */
    @Override
    public String getCurrentDmsUdn() {
        return null;
    }

    /**
     * データタイプ取得.
     *
     * @param viewPagerIndex viewPagerIndex
     * @return ChListDataType
     */
    private ChannelListAdapter.ChListDataType getTypeFromViewPagerIndex(final int viewPagerIndex) {
        DTVTLogger.start();
        ChannelListAdapter.ChListDataType ret = ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_INVALID;
        switch (viewPagerIndex) {
            case 0:
                ret = ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI;
                break;
            case 1:
                ret = ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER;
                break;
            case 2:
                ret = ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS;
                break;
            case 3:
                ret = ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV;
                break;
            default:
                break;
        }
        DTVTLogger.end();
        return ret;
    }

    /**
     * チャンネルリストアダプター.
     */
    private class ChannelListPagerAdapter extends FragmentStatePagerAdapter {

        /**
         * コンストラクタ.
         * @param fm fm
         */
        ChannelListPagerAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            if (null == mFactory) {
                return null;
            }
            return mFactory.createFragment(position, getActivity(), getTypeFromViewPagerIndex(position));
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

    /**
     * 縮小番組表多チャンネル情報を戻すコールバック.
     *
     * @param channelsInfo 画面に渡すチャンネル番組情報
     */
    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {
        DTVTLogger.start();
        if (null == channelsInfo) {
            DTVTLogger.end();
            return;
        }
        ArrayList<ChannelInfo> channels = channelsInfo.getChannels();
        if (0 == channels.size()) {
            return;
        }
        DTVTLogger.end();
    }

    /**
     * チャンネルリストを戻す.
     *
     * @param channels 　画面に渡すチャンネル情報
     */
    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        if (null == channels) {
            DTVTLogger.end();
            return;
        }
        int size = channels.size();
        if (0 == size) {
            return;
        }
        int pos = mViewPager.getCurrentItem();
        ChannelListFragment fragment = null;
        switch (pos) {
            case CHANNEL_LIST_TAB_HIKARI:
                fragment = mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                break;
            case CHANNEL_LIST_TAB_DTV:
                fragment = mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                break;
            default:
                break;
        }
        if (null == fragment) {
            return;
        }
        ArrayList<Object> tmp = new ArrayList<>();
        for (ChannelInfo ch : channels) {
            tmp.add(ch);
        }
        paging(fragment, tmp);
        updateUi(fragment);
        DTVTLogger.end();
    }

    /**
     * Paging処理
     * 新しい仕様は将来チャンネルリストは一回サーバから取得するので、ページング機能は削除.
     *
     * @param fragment fragment
     * @param list     list
     */
    private void paging(final ChannelListFragment fragment, final ArrayList<Object> list) {
        DTVTLogger.start();
        mHandle.post(new Runnable() {
            @Override
            public void run() {
                if (null == fragment || null == list) {
                    return;
                }
                fragment.clearDatas();
                for (Object objBs : list) {
                    fragment.addData(objBs);
                }
                noticeRefresh(fragment);
            }
        });

        DTVTLogger.end();
    }

    /**
     * 更新を知らせ.
     *
     * @param fragment fragment
     */
    private void noticeRefresh(final ChannelListFragment fragment) {
        if (null == fragment) {
            return;
        }
        if (null == mHandle) {
            mHandle = new Handler();
        }
        mHandle.post(new Runnable() {
            @Override
            public void run() {
                fragment.noticeRefresh();
            }
        });
    }

    /**
     * Error処理.
     *
     * @param msg エラー情報
     */
    @Override
    public void onError(final String msg) {
        DTVTLogger.start();
        if (null == mViewPager) {
            DTVTLogger.end();
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int tab = mViewPager.getCurrentItem();
                switch (tab) {
                    case CHANNEL_LIST_TAB_BS:
                        displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                        showMessage("BSチャンネル取得できませんでした");
                        break;
                    case CHANNEL_LIST_TAB_TER:
                        displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                        showMessage("地上波チャンネル取得できませんでした");
                        break;
                    default:
                        break;
                }
            }
        });
        DTVTLogger.end();
    }

    /**
     * showMessage.
     *
     * @param msg msg
     */
    private void showMessage(final String msg) {
        DTVTLogger.start();
        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        t.show();
        DTVTLogger.end();
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

    /**
     * type値設定.
     *
     * @param type type
     */
    private synchronized void setCurrentType(final ChannelListAdapter.ChListDataType type) {
        mCurrentType = type;
    }
}