/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.detail.ContentDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.commonmanager.StbConnectionManager;
import com.nttdocomo.android.tvterminalapp.dataprovider.DtvChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.HikariTvChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.dataprovider.dlna.DlnaContentBsChannelDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.dlna.DlnaContentTerChennelDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragment;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaObject;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.DataConverter;
import com.nttdocomo.android.tvterminalapp.utils.DlnaUtils;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.util.ArrayList;

/**
 * チャンネルリスト.
 */
public class ChannelListActivity extends BaseActivity implements
        View.OnClickListener,

        ChannelListFragment.ChannelListFragmentListener,
        ChannelListFragment.OnClickChannelItemListener,
        TabItemLayout.OnClickTabTextListener,

        ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        HikariTvChannelDataProvider.ContentsDataCallback,
        DlnaContentBsChannelDataProvider.ContentsDataCallback,
        DlnaContentTerChennelDataProvider.ContentsDataCallback {

    // region declaration
    /** チャンネルリストのタイプ. */
    public enum ChannelListDataType {
        /** タイプ:ひかりTV. */
        CH_LIST_DATA_TYPE_HIKARI,
        /** タイプ:地上波. */
        CH_LIST_DATA_TYPE_TDB,
        /** タイプ:BS. */
        CH_LIST_DATA_TYPE_BS,
        /** タイプ:dTVチャンネル. */
        CH_LIST_DATA_TYPE_DCH
    }
    // endregion declaration

    // region variable
    /** STB接続時タブリスト. */
    private static final ChannelListDataType[] CHANNEL_LIST_TAB_NO_STB_LIST =
            {
                    ChannelListDataType.CH_LIST_DATA_TYPE_HIKARI,
                    ChannelListDataType.CH_LIST_DATA_TYPE_DCH
            };
    /** STB未接続時タブリスト.*/
    private static final ChannelListDataType[] CHANNEL_LIST_TAB_CONNECT_STB_LIST =
            {
                    ChannelListDataType.CH_LIST_DATA_TYPE_HIKARI,
                    ChannelListDataType.CH_LIST_DATA_TYPE_TDB,
                    ChannelListDataType.CH_LIST_DATA_TYPE_BS,
                    ChannelListDataType.CH_LIST_DATA_TYPE_DCH
            };

    /** タブインデックス　ひかりTV for docomo.*/
    private static final int TAB_INDEX_HIKARI = 0;
    /** タブインデックス　地上波.*/
    private static final int TAB_INDEX_TER = 1;
    /** タブインデックス　BS.*/
    private static final int TAB_INDEX_BS = 2;
    /** タブインデックス　dTVチャンネル.*/
    private static final int TAB_INDEX_DTV = 3;
    /**
     * ひかりTV for docomoタブの連続更新防止用実行間隔.
     * これより短い間隔で呼ばれた場合は、スキップする
     */
    private static final long GET_HIKARI_DATA_INTERVAL = 100L;

    /** ビューページャ. */
    private ViewPager mViewPager = null;
    /** 共通タブ. */
    private TabItemLayout mTabLayout = null;
    /** チャンネルリストフラグメントファクトリー. */
    private ChannelListFragmentFactory mFactory = null;
    /** リスト0件メッセージ. */
    private TextView mNoDataMessage;

    /** hikariTVチャンネルプロバイダー. */
    private HikariTvChannelDataProvider mHikariTvChannelDataProvider = null;
    /** dTVチャンネルプロバイダー. */
    private DtvChannelDataProvider mDtvChannelDataProvider = null;
    /** 地上波一覧プロバイダー. */
    private DlnaContentTerChennelDataProvider mDlnaContentTerChennelDataProvider = null;
    /** BSデジタル一覧プロバイダー. */
    private DlnaContentBsChannelDataProvider mDlnaContentBsChannelDataProvider = null;

    /** ハンドラー(DataProvider). */
    private final Handler mDataProviderHandler = new Handler();
    /** ハンドラー(ScrollView関連). */
    private final Handler mHandler = new Handler();

    /**ひかりTVチャンネル一覧.*/
    private ArrayList<ChannelInfo> mHikariTvChannelList;
    /**dTvチャンネル一覧.*/
    private ArrayList<ChannelInfo> mdTvChannelList;

    /** タブリスト. */
    private String[] mTabNames = null;
    /** LastY. */
    private float mLastY = 0;
    /** しきい値 20.0f. */
    private static final float sScrollThreshold = 20.0f;
    /** 上にスクロール. */
    private boolean mIsScrollUp = false;
    /** 現在タイプ. */
    private ChannelListDataType mCurrentType = ChannelListDataType.CH_LIST_DATA_TYPE_HIKARI;
    /** メニュー表示フラグ.*/
    private Boolean mIsMenuLaunch = false;
    /** ページングインデックス.*/
    private int mPageIndex = 0;
    /** ロード終了. */
    private boolean mIsEndPage = false;
    /** ひかりTV for docomoタブの連続更新防止用. */
    private long beforeGetHikariData;
    /** 前回のタブ位置.*/
    private static final int DEFAULT_TAB_INDEX = -1;
    /** 前回のタブ位置.*/
    private int mTabIndex = DEFAULT_TAB_INDEX;
    /** 前回のタブ位置.*/
    private static final String TAB_INDEX_BEFORE = "tabIndex";
    /** 前回リモートに接続したか.*/
    private boolean mIsShowedBefore = false;
    /** 前回リモートに接続したか.*/
    private static final String IS_SHOWED_BEFORE = "isShowedBefore";
    // endregion variable

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.channel_list_main_layout);
        if (savedInstanceState != null) {
            mIsShowedBefore = savedInstanceState.getBoolean(IS_SHOWED_BEFORE);
            mTabIndex = savedInstanceState.getInt(TAB_INDEX_BEFORE);
            savedInstanceState.clear();
        }
        setTitleText(getString(R.string.channel_list_activity_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        enableHeaderBackIcon(true);
        enableGlobalMenuIcon(true);

        initView();
        initData();
        DTVTLogger.end();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        sendScreenViewForPosition(mViewPager.getCurrentItem());
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SHOWED_BEFORE, mIsShowedBefore);
        outState.putInt(TAB_INDEX_BEFORE, mViewPager.getCurrentItem());
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        if (null == mHikariTvChannelDataProvider) {
            mHikariTvChannelDataProvider = new HikariTvChannelDataProvider(this, this);
        }
        mHikariTvChannelDataProvider.enableConnect();
        if (null == mDtvChannelDataProvider) {
            mDtvChannelDataProvider = new DtvChannelDataProvider(this);
        }
        mDtvChannelDataProvider.enableConnect();
    }

    /**
     * CHリストデータ取得.
     */
    private void getChListData() {
        mNoDataMessage.setVisibility(View.GONE);
        switch (mCurrentType) {
            case CH_LIST_DATA_TYPE_BS:
                getBsData();
                break;
            case CH_LIST_DATA_TYPE_TDB:
                getTerData();
                break;
            case CH_LIST_DATA_TYPE_HIKARI:
                getHikariData();
                break;
            case CH_LIST_DATA_TYPE_DCH:
                getDtvData();
                break;
            default:
                break;
        }
    }

    /**
     * onPause処理.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (null != mHikariTvChannelDataProvider) {
            mHikariTvChannelDataProvider.stopConnect();
        }
        if (null != mDtvChannelDataProvider) {
            mDtvChannelDataProvider.stopConnect();
        }
        // bg→fgになる場合、再取得しない、再取得必要があれば、コメントアウトを外す
//        clearAllFrames();
    }

    /**
     * Clear for all frames data.
     */
    private void clearAllFrames() {
        if (null == mTabNames || null == mFactory) {
            return;
        }
        for (int i = 0; i < mTabNames.length; ++i) {
            ChannelListDataType type = ChannelListDataType.values()[i];
            ChannelListFragment fragment = mFactory.createFragment(i, this, type, this);
            fragment.clearDatas();
            fragment.noticeRefresh();
            fragment.showProgressBar(false);
        }
    }

    /**
     * データ初期化.
     */
    private void initData() {
        mFactory = new ChannelListFragmentFactory();
        //初回は必ず実行させるために、最小値を入れる
        beforeGetHikariData = Long.MIN_VALUE;
        if (null == mHikariTvChannelDataProvider) {
            mHikariTvChannelDataProvider = new HikariTvChannelDataProvider(this, this);
        }
        if (null == mDtvChannelDataProvider) {
            mDtvChannelDataProvider = new DtvChannelDataProvider(this);
        }
        if (null == mDlnaContentBsChannelDataProvider) {
            mDlnaContentBsChannelDataProvider = new DlnaContentBsChannelDataProvider(this);
        }
        if (null == mDlnaContentTerChennelDataProvider) {
            mDlnaContentTerChennelDataProvider = new DlnaContentTerChennelDataProvider(this);
        }
        synchronized (mCurrentType) {
            getChListData();
        }
    }

    /**
     * view初期化.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        mNoDataMessage = findViewById(R.id.channel_list_no_items);
        initChannelListTab();
        ChannelListPagerAdapter adp = new ChannelListPagerAdapter(getSupportFragmentManager());
        final ChannelListFragment.ChannelListFragmentListener lis = this;
        mViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
        mViewPager.setAdapter(adp);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(final int position) {
                        super.onPageSelected(position);
                        mPageIndex = 0;
                        mIsEndPage = false;
                        mTabLayout.setTab(position);
                        sendScreenViewForPosition(position);
                        mFactory.createFragment(position, lis, ChannelListDataType.values()[position], getActivity()).showProgressBar(true);
                    }
                });
    }

    /** 地上波データ取得. */
    private void getTerData() {
        mDlnaContentTerChennelDataProvider.browseContentWithContainerId(
                DlnaUtils.getContainerIdByImageQuality(getApplicationContext(), DlnaUtils.DLNA_DMS_TER_CHANNEL), mPageIndex);
    }

    /** Bsデータを取得. */
    private void getBsData() {
        mDlnaContentBsChannelDataProvider.browseContentWithContainerId(
                DlnaUtils.getContainerIdByImageQuality(getApplicationContext(), DlnaUtils.DLNA_DMS_BS_CHANNEL), mPageIndex);
    }

    /**
     * Hikariデータ取得.
     */
    private void getHikariData() {
        //アクティビティ起動時は、アクティビティ表示の初期化用とタブ表示更新用の2回ここが呼ばれてしまう。
        //ネットワークエラーの表示がおかしくなるので、連続して呼ばれた場合は、後者をスキップする
        if (beforeGetHikariData + GET_HIKARI_DATA_INTERVAL > System.currentTimeMillis()) {
            DTVTLogger.end("getHikariData skip");
            return;
        } else {
            DTVTLogger.end("getHikariData exec");
        }
        //現在時刻を取得する
        beforeGetHikariData = System.currentTimeMillis();
        mHikariTvChannelDataProvider.getChannelList(0, 0, "");
    }

    /**
     * DTvデータ取得.
     */
    private void getDtvData() {
        mDtvChannelDataProvider.getChannelList(0, 0, "");
    }

    /**
     * tab関連Viewの初期化.
     */
    private void initChannelListTab() {
        Resources res = getResources();
        DTVTLogger.warning("StbConnectionManager.shared().getConnectionStatus() = " + StbConnectionManager.shared().getConnectionStatus());

        String contractInfo = UserInfoUtils.getUserContractInfo(SharedPreferencesUtils.getSharedPreferencesUserInfo(ChannelListActivity.this));
        if (!UserInfoUtils.CONTRACT_INFO_H4D.equals(contractInfo)) {
            mTabNames = res.getStringArray(R.array.channel_list_tab_names_no_paring);
        } else {
            switch (StbConnectionManager.shared().getConnectionStatus()) {
                case NONE_PAIRING:
                case NONE_LOCAL_REGISTRATION:
                    if (mIsShowedBefore) {
                        mTabNames = res.getStringArray(R.array.channel_list_tab_names);
                    } else {
                        mTabNames = res.getStringArray(R.array.channel_list_tab_names_no_paring);
                    }
                    break;
                case HOME_OUT:
                case HOME_OUT_CONNECT:
                case HOME_IN:
                default:
                    mTabNames = res.getStringArray(R.array.channel_list_tab_names);
                    mIsShowedBefore = true;
                    break;
            }
        }

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

    /**
     * 送信containerIdと表示中のタブ一致のチェック.
     * @param pos タブポジション
     * @param containerId パス
     * @return  一致のチェック結果
     */
    private boolean checkRequestContainerId(final int pos, final String containerId) {
        boolean result = true;
        if (pos == TAB_INDEX_TER) {
            if (!containerId.endsWith(DlnaUtils.DLNA_DMS_TER_CHANNEL)) {
                result = false;
            }
        } else if (pos == TAB_INDEX_BS) {
            if (!containerId.endsWith(DlnaUtils.DLNA_DMS_BS_CHANNEL)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public void onContentBrowseCallback(final DlnaObject[] objs, final String containerId) {
        DTVTLogger.start();
        int pos = mViewPager.getCurrentItem();
        if (!checkRequestContainerId(pos, containerId)) {
            return;
        }
        final ChannelListFragment fragment = mFactory.createFragment(pos, this, mCurrentType, null);
        ArrayList<Object> tmp = new ArrayList<>();
        for (int i = 0; i < objs.length; ++i) {
            Object item = objs[i];
            tmp.add(item);
        }
        paging(fragment, tmp, true);
        updateUi(fragment);
        DTVTLogger.end();
    }

    @Override
    public void onContentBrowseErrorCallback(final String containerId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int pos = mViewPager.getCurrentItem();
                if (!checkRequestContainerId(pos, containerId)) {
                    return;
                }
                ChannelListFragment fragment = mFactory.createFragment(pos, ChannelListActivity.this, mCurrentType, null);
                updateUi(fragment);
                showGetDataFailedToast();
                mNoDataMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onConnectErrorCallback(final int errorCode) {
        int pos = mViewPager.getCurrentItem();
        final ChannelListFragment fragment = mFactory.createFragment(pos, this, mCurrentType, null);
        updateUi(fragment);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showGetDataFailedToast();
                mNoDataMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.debug("position = " + position);
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            mPageIndex = 0;
            mIsEndPage = false;
            mViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * 表示中タブの内容によってスクリーン情報を送信する.
     * @param position ポジション
     */
    private void sendScreenViewForPosition(final int position) {
        switch (position) {
            case TAB_INDEX_HIKARI:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_channel_list_h4d));
                break;
            case TAB_INDEX_TER:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_channel_list_ter));
                break;
            case TAB_INDEX_BS:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_channel_list_bs));
                break;
            case TAB_INDEX_DTV:
                super.sendScreenView(getString(R.string.google_analytics_screen_name_channel_list_dtv));
                break;
        }
    }

    /**
     * Activityを取得.
     *
     * @return this this
     */
    private ChannelListActivity getActivity() {
        return this;
    }

    @Override
    public void onScroll(final ChannelListFragment fragment, final AbsListView absListView, final
    int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
    }

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

    @Override
    public void onScrollStateChanged(final ChannelListFragment fragment, final AbsListView absListView, final int scrollState) {
        if (mIsEndPage) {
            return;
        }
        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == fragment.getDataCount() - 1
                    && mIsScrollUp) {
                mIsScrollUp = false;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mNoDataMessage.setVisibility(View.GONE);
                        switch (fragment.getChListDataType()) {
                            case CH_LIST_DATA_TYPE_BS:
                                if (fragment.getConnectionStatus() == fragment.isRemote()) {
                                    fragment.loadStart();
                                } else {
                                    mPageIndex = 0;
                                    fragment.showProgressBar(true);
                                    fragment.clearDatas();
                                }
                                getBsData();
                                break;
                            case CH_LIST_DATA_TYPE_TDB:
                                if (fragment.getConnectionStatus() == fragment.isRemote()) {
                                    fragment.loadStart();
                                } else {
                                    mPageIndex = 0;
                                    fragment.showProgressBar(true);
                                    fragment.clearDatas();
                                }
                                getTerData();
                                break;
                            case CH_LIST_DATA_TYPE_HIKARI:
                            case CH_LIST_DATA_TYPE_DCH:
                            default:
                                break;
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClickChannelItem(final int pos, final ChannelListDataType type,
                                   final ChannelListFragment fragment) {
        ChannelInfo channelInfo;
            switch (type) {
                case CH_LIST_DATA_TYPE_BS:
                case CH_LIST_DATA_TYPE_TDB:
                    DTVTLogger.error(" >>>");
                    return;
                case CH_LIST_DATA_TYPE_HIKARI:
                    if (mHikariTvChannelList.size() < pos) {
                        DTVTLogger.error("pos = " + pos + " is invalid mHikariTvChannelList.size() = " + mHikariTvChannelList.size());
                        return;
                    }
                    channelInfo = mHikariTvChannelList.get(pos);
                    mHikariTvChannelDataProvider.getNowOnAirProgram(channelInfo.getChannelNo());
                    break;
                case CH_LIST_DATA_TYPE_DCH:
                    if (mdTvChannelList.size() < pos) {
                        DTVTLogger.error("pos = " + pos + " is invalid mdTvChannelList.size() = " + mdTvChannelList.size());
                        return;
                    }
                    channelInfo = mdTvChannelList.get(pos);
                    mHikariTvChannelDataProvider.getNowOnAirProgram(channelInfo.getChannelNo());
                    break;
            }
    }

    @Override
    public void onContentDataGet(final ContentsData data) {
        if (data == null) {
            //Now On Airコンテンツがない場合はダイアログ表示
            showErrorDialog(getString(R.string.contents_detail_now_on_air_contents_nothing));
            return;
        }
        Intent intent = new Intent(this, ContentDetailActivity.class);
        ComponentName componentName = this.getComponentName();
        intent.putExtra(DtvtConstants.SOURCE_SCREEN, componentName.getClassName());
        OtherContentsDetailData detailData = DataConverter.getOtherContentsDetailData(data, ContentUtils.PLALA_INFO_BUNDLE_KEY);
        intent.putExtra(detailData.getRecommendFlg(), detailData);
        startActivity(intent);
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser, final ChannelListFragment fragment) {
        DTVTLogger.start();
        mNoDataMessage.setVisibility(View.GONE);
        if (!isVisibleToUser) {
            fragment.clearDatas();
            noticeRefresh(fragment);
            fragment.showProgressBar(true);
            DTVTLogger.end();
            return;
        }

        ChannelListDataType type = fragment.getChListDataType();
        setCurrentType(type);
        switch (type) {
            case CH_LIST_DATA_TYPE_HIKARI:
                getHikariData();
                break;
            case CH_LIST_DATA_TYPE_TDB:
                getTerData();
                break;
            case CH_LIST_DATA_TYPE_BS:
                getBsData();
                break;
            case CH_LIST_DATA_TYPE_DCH:
                getDtvData();
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
                fragment.showProgressBar(false);
            }
        });
    }

    /**
     * データタイプ取得.
     *
     * @param viewPagerIndex viewPagerIndex
     * @return ChannelListDataType
     */
    private ChannelListDataType getTypeFromViewPagerIndex(final int viewPagerIndex) {
        DTVTLogger.start();
        ChannelListDataType ret;
        if (mTabNames.length == CHANNEL_LIST_TAB_CONNECT_STB_LIST.length) {
            ret = CHANNEL_LIST_TAB_CONNECT_STB_LIST[viewPagerIndex];
        } else {
            ret = CHANNEL_LIST_TAB_NO_STB_LIST[viewPagerIndex];
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
         *
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
            return mFactory.createFragment(position, getActivity(), getTypeFromViewPagerIndex(position), getActivity());
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
    public void channelInfoCallback(final ChannelInfoList channelsInfo, final int[] chNo) {
        DTVTLogger.error(" >>>");
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        DTVTLogger.start();
        if (mTabIndex >= 0) {
            mViewPager.setCurrentItem(mTabIndex);
            mTabIndex = DEFAULT_TAB_INDEX;
        }
        int pos = mViewPager.getCurrentItem();
        ChannelListDataType chType = getTypeFromViewPagerIndex(pos);
        ChannelListFragment fragment = mFactory.createFragment(pos, this, chType, this);
        fragment.showProgressBar(false);

        if (null == channels) {
            mNoDataMessage.setVisibility(View.VISIBLE);

            //エラーメッセージを取得する
            String message = mHikariTvChannelDataProvider.getChannelError().getErrorMessage();

            //有無で処理を分ける
            if (TextUtils.isEmpty(message)) {
                showGetDataFailedToast();
            } else {
                showGetDataFailedToast(message);
            }

            DTVTLogger.end();
            return;
        }
        int size = channels.size();

        if (0 == size) {
            mNoDataMessage.setVisibility(View.VISIBLE);
            return;
        }
        switch (chType) {
            case CH_LIST_DATA_TYPE_HIKARI:
                fragment = mFactory.createFragment(pos, this, chType, this);
                mHikariTvChannelList = channels;
                break;
            case CH_LIST_DATA_TYPE_DCH:
                fragment = mFactory.createFragment(pos, this, chType, this);
                mdTvChannelList = channels;
                break;
            case CH_LIST_DATA_TYPE_TDB:
            case CH_LIST_DATA_TYPE_BS:
            default:
                fragment = null;
                break;
        }
        if (null == fragment) {
            return;
        }
        paging(fragment, channels, false);
        updateUi(fragment);
        DTVTLogger.end();
    }

    /**
     * ページング処理.
     * @param fragment フラグメント
     * @param list     リスト
     * @param isFromDLNA  true:dlna false:hikari
     */
    private void paging(final ChannelListFragment fragment, final ArrayList list, final boolean isFromDLNA) {
        DTVTLogger.start();
        mDataProviderHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null == fragment || null == list) {
                    return;
                }
                if (!isFromDLNA || mPageIndex == 0) {
                    fragment.clearDatas();
                } else {
                    if (mPageIndex > 0) {
                        fragment.loadComplete();
                    }
                }
                for (Object item : list) {
                    fragment.addData(item);
                }
                noticeRefresh(fragment);
                if (mPageIndex == 0 && list.size() == 0) {
                    mNoDataMessage.setVisibility(View.VISIBLE);
                }
                mPageIndex++;
                fragment.showProgressBar(false);
                if (list.size() < DtvtConstants.REQUEST_DLNA_LIMIT_50) {
                    mIsEndPage = true;
                }
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
        mDataProviderHandler.post(new Runnable() {
            @Override
            public void run() {
                fragment.noticeRefresh();
            }
        });
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        DTVTLogger.start();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mIsMenuLaunch) {
                    //メニューから起動の場合ホーム画面に戻る
                    startHomeActivity();
                    return false;
                }
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void contentsDetailBackKey(final View view) {
        if (mIsMenuLaunch) {
            //メニューから起動の場合ホーム画面に戻る
            startHomeActivity();
        } else {
            //ランチャーから起動の場合
            finish();
        }
    }

    /**
     * type値設定.
     *
     * @param type type
     */
    private synchronized void setCurrentType(final ChannelListDataType type) {
        mCurrentType = type;
        mLastY = 0;
        mIsScrollUp = false;
    }

    @Override
    public void clipKeyResult() {
        //Nop 仕様により実装のみ
    }
}
