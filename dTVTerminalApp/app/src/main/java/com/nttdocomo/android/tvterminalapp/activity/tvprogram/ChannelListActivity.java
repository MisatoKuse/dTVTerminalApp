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
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.DTvChDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.HikariTvChDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragment;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvBsChList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvTerChList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListListener;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
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
        HikariTvChDataProvider.ContentsDataCallback,

        DlnaTerChListListener,
        DlnaBsChListListener {



    // region declaration
    /** チャンネルリストのタイプ. */
    public enum ChListDataType {
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
    private static final ChListDataType[] CHANNEL_LIST_TAB_NO_STB_LIST =
            {
                    ChListDataType.CH_LIST_DATA_TYPE_HIKARI,
                    ChListDataType.CH_LIST_DATA_TYPE_DCH
            };
    /**
     * 　STB未接続時タブリスト.
     */
    private static final ChListDataType[] CHANNEL_LIST_TAB_CONNECT_STB_LIST =
            {
                    ChListDataType.CH_LIST_DATA_TYPE_HIKARI,
                    ChListDataType.CH_LIST_DATA_TYPE_TDB,
                    ChListDataType.CH_LIST_DATA_TYPE_BS,
                    ChListDataType.CH_LIST_DATA_TYPE_DCH
            };

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
    private HikariTvChDataProvider mHikariTvChDataProvider = null;
    /** dTVチャンネルプロバイダー. */
    private DTvChDataProvider mDTvChDataProvider = null;

    /** 地上波一覧. */
    private DlnaProvTerChList mDlnaProvTerChList = null;
    /** BSデジタル一覧. */
    private DlnaProvBsChList mDlnaProvBsChList = null;

    /** ハンドラー(DataProvider). */
    private final Handler mDataProviderHandler = new Handler();
    /** ハンドラー(ScrollView関連). */
    private final Handler mHandler = new Handler();

    private ArrayList<ChannelInfo> mHikariTvChannelList;
    private ArrayList<ChannelInfo> mdTvChannelList;

    /** タブリスト. */
    private String[] mTabNames = null;
    /** LastY. */
    private float mLastY = 0;
    /** しきい値 20.0f. */
    private static final float sScrollThreshold = 20.0f;
    /** 上にスクロール. */
    private boolean mIsScrollUp = false;
    /** メニュー起動. */
    private Boolean mIsMenuLaunch = false;

    /** タブ延長タイム. */
    private final int CHANNEL_LIST_TAB_DELAY_TIME = 1300;
    /** 現在タイプ. */
    private ChListDataType mCurrentType = ChListDataType.CH_LIST_DATA_TYPE_HIKARI;

    /** 画面表示時のSTB接続状態. */
    private boolean mIsStbConnected;

    /** ひかりTV for docomoタブの連続更新防止用. */
    private long beforeGetHikariData;


    /**
     * Hikariデータスレッド.
     */
    private final Runnable mRunnableHikari = new Runnable() {
        @Override
        public void run() {
            mHikariTvChDataProvider.getChannelList(0, 0, "");
        }
    };

    /**
     * DTvデータスレッド.
     */
    private final Runnable mRunnableDtv = new Runnable() {
        @Override
        public void run() {
            mDTvChDataProvider.getChannelList(0, 0, "");
        }
    };

    /**
     * Terデータスレッド.
     */
    private final Runnable mRunnableTer = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
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
     * Bsデータスレッド.
     */
    private final Runnable mRunnableBs = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
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

    // endregion variable
    /**
     * コンストラクタ.
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_list_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.channel_list_activity_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(true);
        }
        enableGlobalMenuIcon(true);
        enableStbStatusIcon(true);

        mIsStbConnected = getStbStatus();
        initView();
        initData();
    }

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        if (null == mHikariTvChDataProvider) {
            mHikariTvChDataProvider = new HikariTvChDataProvider(this, this);
        }
        mHikariTvChDataProvider.enableConnect();
        if (null == mDTvChDataProvider) {
            mDTvChDataProvider = new DTvChDataProvider(this);
        }
        mDTvChDataProvider.enableConnect();

        if (null == mDlnaProvBsChList) {
            mDlnaProvBsChList = new DlnaProvBsChList();
        }
        if (null == mDlnaProvTerChList) {
            mDlnaProvTerChList = new DlnaProvTerChList();
        }

        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
        if (!mDlnaProvTerChList.start(dlnaDmsItem, getActivity())) {
            onError("Get Ter channel list data failed");
        }
        if (!mDlnaProvBsChList.start(dlnaDmsItem, getActivity())) {
            onError("Get Bs channel list data failed");
        }
        synchronized (mCurrentType) {
            getChListData();
        }
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
        if (null != mHikariTvChDataProvider) {
            mHikariTvChDataProvider.stopConnect();
        }
        if (null != mDTvChDataProvider) {
            mDTvChDataProvider.stopConnect();
        }
        if (null != mDlnaProvTerChList) {
            mDlnaProvTerChList.stopListen();
        }

        if (null != mDlnaProvTerChList) {
            mDlnaProvTerChList.stopListen();
        }
        if (null != mDlnaProvBsChList) {
            mDlnaProvBsChList.stopListen();
        }
        mDataProviderHandler.removeCallbacks(mRunnableHikari);
        mDataProviderHandler.removeCallbacks(mRunnableDtv);

        mDataProviderHandler.removeCallbacks(mRunnableTer);
        mDataProviderHandler.removeCallbacks(mRunnableBs);
        clearAllFrames();
    }

    /**
     * Clear for all frames data.
     */
    private void clearAllFrames() {
        if (null == mTabNames || null == mFactory) {
            return;
        }
        for (int i = 0; i < mTabNames.length; ++i) {
            ChListDataType type = ChListDataType.values()[i];
            ChannelListFragment fragment = mFactory.createFragment(i, this, type, this);
            if (null == fragment) {
                continue;
            }
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
        mViewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(final int position) {
                        super.onPageSelected(position);
                        mTabLayout.setTab(position);
                        mFactory.createFragment(position, lis, ChListDataType.values()[position], getActivity()).showProgressBar(true);
                    }
                });
    }

    /**
     * Bsデータを取得.
     */
    private void getBsData() {
        DTVTLogger.start();
        mDataProviderHandler.postDelayed(mRunnableBs, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Terデータ取得.
     */
    private void getTerData() {
        DTVTLogger.start();
        mDataProviderHandler.postDelayed(mRunnableTer, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Hikariデータ取得.
     */
    private void getHikariData() {
        DTVTLogger.start("time = " + beforeGetHikariData
                + " now = " + System.currentTimeMillis());

        //アクティビティ起動時は、アクティビティ表示の初期化用とタブ表示更新用の2回ここが呼ばれてしまう。
        //ネットワークエラーの表示がおかしくなるので、連続して呼ばれた場合は、後者をスキップする
        if (beforeGetHikariData + GET_HIKARI_DATA_INTERVAL > System.currentTimeMillis()) {
            //連続して呼ばれたので帰る
            DTVTLogger.end("getHikariData skip");
            return;
        }

        //連続実行ではないので、実行する
        DTVTLogger.end("getHikariData exec");

        //現在時刻を取得する
        beforeGetHikariData = System.currentTimeMillis();

        mDataProviderHandler.postDelayed(mRunnableHikari, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }


    /**
     * DTvデータ取得.
     */
    private void getDtvData() {
        DTVTLogger.start();
        mDataProviderHandler.postDelayed(mRunnableDtv, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Paging処理.
     *
     * @param chType チャンネル種別
     */
    private void onPageChange(final ChListDataType chType) {
        DTVTLogger.start();
        clearOthers(chType);
        mLastY = 0;
        mIsScrollUp = false;
        DTVTLogger.end();
    }

    /**
     * 内容削除処理.
     *
     * @param chType チャンネル種別
     */
    private void clearOthers(final ChListDataType chType) {
        DTVTLogger.start();
        switch (chType) {
            case CH_LIST_DATA_TYPE_HIKARI:
                mDataProviderHandler.removeCallbacks(mRunnableBs);
                mDataProviderHandler.removeCallbacks(mRunnableDtv);
                mDataProviderHandler.removeCallbacks(mRunnableTer);
                break;
            case CH_LIST_DATA_TYPE_DCH:
                mDataProviderHandler.removeCallbacks(mRunnableBs);
                mDataProviderHandler.removeCallbacks(mRunnableHikari);
                mDataProviderHandler.removeCallbacks(mRunnableTer);
                break;
            case CH_LIST_DATA_TYPE_TDB:
                mDataProviderHandler.removeCallbacks(mRunnableBs);
                mDataProviderHandler.removeCallbacks(mRunnableDtv);
                mDataProviderHandler.removeCallbacks(mRunnableHikari);
                break;
            case CH_LIST_DATA_TYPE_BS:
                mDataProviderHandler.removeCallbacks(mRunnableDtv);
                mDataProviderHandler.removeCallbacks(mRunnableHikari);
                mDataProviderHandler.removeCallbacks(mRunnableTer);
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
        if (mIsStbConnected) {
            mTabNames = res.getStringArray(R.array.channel_list_tab_names);
        } else {
            //未ペアリングの場合 BSと地上波は表示しない
            mTabNames = res.getStringArray(R.array.channel_list_tab_names_no_paring);
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
     * Event処理.
     *
     * @param fragment    fragment
     * @param absListView absListView
     * @param scrollState scrollState
     */
    @Override
    public void onScrollStateChanged(final ChannelListFragment fragment, final AbsListView absListView, final int scrollState) {
        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && absListView
                    .getLastVisiblePosition() == fragment.getDataCount() - 1 && mIsScrollUp) {
                mIsScrollUp = false;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mNoDataMessage.setVisibility(View.GONE);
                        switch (fragment.getChListDataType()) {
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
                });
            }
        }
    }

    @Override
    public void onClickChannelItem(int pos, ChListDataType type) {
        DTVTLogger.warning("pos = " + pos);
        ChannelInfo channelInfo = null;
            switch (type){
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
                    mHikariTvChDataProvider.getNowOnAirProgram(channelInfo.getChNo());
                    break;
                case CH_LIST_DATA_TYPE_DCH:
                    if (mdTvChannelList.size() < pos) {
                        DTVTLogger.error("pos = " + pos + " is invalid mdTvChannelList.size() = " + mdTvChannelList.size());
                        return;
                    }
                    channelInfo = mdTvChannelList.get(pos);
                    mHikariTvChDataProvider.getNowOnAirProgram(channelInfo.getChNo());
                    break;
            }
    }

    @Override
    public void onContentDataGet(ContentsData data) {
        Intent intent = new Intent(this, ContentDetailActivity.class);
        ComponentName componentName = this.getComponentName();
        intent.putExtra(DTVTConstants.SOURCE_SCREEN, componentName.getClassName());
        OtherContentsDetailData detailData = BaseActivity.getOtherContentsDetailData(data, ContentDetailActivity.PLALA_INFO_BUNDLE_KEY);
        intent.putExtra(detailData.getRecommendFlg(), detailData);
        startActivity(intent);
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
        mNoDataMessage.setVisibility(View.GONE);
        if (!isVisibleToUser) {
            fragment.clearDatas();
            //fragment.noticeRefresh();
            noticeRefresh(fragment);
            fragment.showProgressBar(true);
            DTVTLogger.end();
            return;
        }

        ChListDataType type = fragment.getChListDataType();
        setCurrentType(type);
        switch (type) {
            case CH_LIST_DATA_TYPE_BS:
                getBsData();
                onPageChange(type);
                break;
            case CH_LIST_DATA_TYPE_TDB:
                getTerData();
                onPageChange(type);
                break;
            case CH_LIST_DATA_TYPE_HIKARI:
                getHikariData();
                onPageChange(type);
                break;
            case CH_LIST_DATA_TYPE_DCH:
                getDtvData();
                onPageChange(type);
                break;
            default:
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
     * Terデータcallback.
     *
     * @param curInfo current recorded video list
     */
    @Override
    public void onListUpdate(final DlnaTerChListInfo curInfo) {
        DTVTLogger.start();
        int pos = mViewPager.getCurrentItem();
        final ChannelListFragment fragment = mFactory.createFragment(pos, this, ChListDataType.CH_LIST_DATA_TYPE_TDB, null);
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
        final ChannelListFragment fragment = mFactory.createFragment(pos, this, ChListDataType.CH_LIST_DATA_TYPE_BS, null);
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
    private ChListDataType getTypeFromViewPagerIndex(final int viewPagerIndex) {
        DTVTLogger.start();
        ChListDataType ret;
        if (mIsStbConnected) {
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

    /**
     * 縮小番組表多チャンネル情報を戻すコールバック.
     *
     * @param channelsInfo 画面に渡すチャンネル番組情報
     */
    @Override
    public void channelInfoCallback(final ChannelInfoList channelsInfo) {
        DTVTLogger.error(" >>>");
    }

    /**
     * チャンネルリストを戻す.
     *
     * @param channels 　画面に渡すチャンネル情報
     */
    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        int pos = mViewPager.getCurrentItem();
        ChListDataType chType = getTypeFromViewPagerIndex(pos);
        ChannelListFragment fragment = mFactory.createFragment(pos, this, chType, this);
        fragment.showProgressBar(false);

        if (null == channels) {
            mNoDataMessage.setVisibility(View.VISIBLE);

            //エラーメッセージを取得する
            String message = mHikariTvChDataProvider.getChannelError().getErrorMessage();

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
        paging(fragment, channels);
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
    private void paging(final ChannelListFragment fragment, final ArrayList list) {
        DTVTLogger.start();
        mDataProviderHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null == fragment || null == list) {
                    return;
                }
                fragment.clearDatas();
                for (Object item : list) {
                    fragment.addData(item);
                }
                noticeRefresh(fragment);
                fragment.showProgressBar(false);
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

    /**
     * Error処理.
     *
     * @param msg エラー情報
     */
    @Override
    public void onError(final String msg) {
        if (null == mViewPager) {
            DTVTLogger.warning(" >>>");
            return;
        }
        DTVTLogger.debug(" >>>");
        //取得エラー時はProgressDialog非表示
        final ChannelListFragment.ChannelListFragmentListener lis = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int pos = mViewPager.getCurrentItem();
                ChListDataType chType = getTypeFromViewPagerIndex(pos);
                ChannelListFragment fragment = mFactory.createFragment(pos, lis, chType, getActivity());
                fragment.showProgressBar(false);
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
                    contentsDetailBackKey(null);
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
    private synchronized void setCurrentType(final ChListDataType type) {
        mCurrentType = type;
    }
}