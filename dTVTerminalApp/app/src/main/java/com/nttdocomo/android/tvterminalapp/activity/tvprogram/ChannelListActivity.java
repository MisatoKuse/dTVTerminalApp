/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListListener;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfoList;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.util.ArrayList;

public class ChannelListActivity extends BaseActivity implements
        View.OnClickListener, ChannelListFragment.ChannelListFragmentListener,
        DlnaRecVideoListener, DlnaTerChListListener, DlnaBsChListListener,
        ScaledDownProgramListDataProvider.ApiDataProviderCallback {

    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private static final float TAB_TEXT_SIZE = 14.0f;

    private String[] mTabNames = null;
    private int screenWidth = 0;
    private float mLastY = 0;
    private static final float sScrollThreshold = 20.0f;
    private boolean mIsScrollUp = false;
    private Boolean mIsMenuLaunch = false;

    private LinearLayout mTabsLayout = null;
    private ViewPager mViewPager = null;

    private ChannelListFragmentFactory mFactory = null;
    private DlnaProvRecVideo mDlnaProvRecVideo = null;
    private DlnaProvBsChList mDlnaProvBsChList = null;
    private DlnaProvTerChList mDlnaProvTerChList = null;
    private HikariTvChDataProvider mHikariTvChDataProvider = null;
    private DTvChDataProvider mDTvChDataProvider = null;
    private int mPagingOffset = 1;
    //ToDo: 開発中テストデータは不足なので、ページング数は一時に「10」に設定していますが、
    //       リリースの時、仕様より、再設定する必要がある。
    private final int CH_LIST_ACTI_PAGING_NUMBER = 10;
    private final int CHANNEL_LIST_TAB_HIKARI = 0;
    private final int CHANNEL_LIST_TAB_TER = 1;
    private final int CHANNEL_LIST_TAB_BS = 2;
    private final int CHANNEL_LIST_TAB_DTV = 3;
    private final int CHANNEL_LIST_TAB_DELAY_TIME = 1300;

    private Handler mHandle = new Handler();

    /**
     * Constructor
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        screenWidth = getWidthDensity();
        initView();
        initData();
        firstMore();
        DTVTLogger.end();
    }

    /**
     * 初回の場合、Loading...を表示
     */
    private void firstMore() {
        DTVTLogger.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayMore(true, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
            }
        }, 200);
        DTVTLogger.end();
    }

    /**
     * onResume処理
     */
    @Override
    protected void onResume() {
        DTVTLogger.start();
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
        super.onResume();
        DTVTLogger.end();
    }

    /**
     * onPause処理
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
        onPageChange(CHANNEL_LIST_TAB_HIKARI);
        onPageChange(CHANNEL_LIST_TAB_BS);
        DTVTLogger.end();
    }

    /**
     * データ初期化
     */
    private void initData() {
        DTVTLogger.start();
        mFactory = new ChannelListFragmentFactory();
        DTVTLogger.end();
    }

    /**
     * view初期化
     */
    private void initView() {
        DTVTLogger.start();
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        HorizontalScrollView horizontalScrollView = findViewById(R.id.channel_list_main_layout_channel_titles_hs);
        //mViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
        initChannelListTab(horizontalScrollView);
        ChannelListPagerAdapter adp = new ChannelListPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
        mViewPager.setAdapter(adp);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);
            }
        });
        DTVTLogger.end();
    }

    /**
     * Loading...を表示
     *
     * @param yn
     * @param tabNo
     * @param type
     */
    private void displayMore(boolean yn, int tabNo, ChannelListAdapter.ChListDataType type) {
        DTVTLogger.start();
        final ChannelListFragment fragment = mFactory.createFragment(tabNo, this, type);
        fragment.displayMoreData(yn);
        DTVTLogger.end();
    }

    /**
     * Bsデータスレッド
     */
    private Runnable mRunableBs = new Runnable() {
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
            //本番ソース end
            //仮ソース begin
//            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
//            if (mDlnaProvRecVideo.start(dlnaDmsItem, getActivity())) {
//                boolean ret=mDlnaProvRecVideo.browseRecVideoDms();
//                if(!ret){
//                    onError("Get recoreded video datas failed");
//                }
//            }
//            testType=ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS;   //test
            //仮ソース end
            DTVTLogger.end();
        }
    };

    /**
     * Bsデータを取得
     */
    private void getBsData() {
        DTVTLogger.start();
        displayMore(true, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        mHandle.postDelayed(mRunableBs, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Terデータスレッド
     */
    private Runnable mRunableTer = new Runnable() {
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
            //本番ソース end
            //仮ソース begin
//            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
//            if (mDlnaProvRecVideo.start(dlnaDmsItem, getActivity())) {
//                mDlnaProvRecVideo.browseRecVideoDms();
//            }
//            testType=ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER;   //test
            //仮ソース end
            DTVTLogger.end();
        }
    };

    /**
     * Terデータ取得
     */
    private void getTerData() {
        DTVTLogger.start();
        displayMore(true, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
        mHandle.postDelayed(mRunableTer, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Hikariデータ取得
     */
    private void getHikariData() {
        DTVTLogger.start();
        //test b
        displayMore(true, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
        //test e
        mHandle.postDelayed(mRunableHikari, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Hikariデータスレッド
     */
    private Runnable mRunableHikari = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
//            //test b
//            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
//            if (mDlnaProvRecVideo.start(dlnaDmsItem, getActivity())) {
//                mDlnaProvRecVideo.browseRecVideoDms();
//            }
//            testType=ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI;   //test
            mHikariTvChDataProvider.getChannelList(1, mPagingOffset, "");
            DTVTLogger.end();
        }
    };

    /**
     * DTvデータスレッド
     */
    private Runnable mRunableDtv = new Runnable() {
        @Override
        public void run() {
            DTVTLogger.start();
//            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
//            if (mDlnaProvRecVideo.start(dlnaDmsItem, getActivity())) {
//                mDlnaProvRecVideo.browseRecVideoDms();
//            }
//            testType=ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV;   //test
            mDTvChDataProvider.getChannelList(1, mPagingOffset, "");
            DTVTLogger.end();
        }
    };

    /**
     * DTvデータ取得
     */
    private void getDtvData() {
        DTVTLogger.start();
        //test b
        displayMore(true, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
        //test e
        mHandle.postDelayed(mRunableDtv, CHANNEL_LIST_TAB_DELAY_TIME);
        DTVTLogger.end();
    }

    /**
     * Paging処理
     *
     * @param tabPosition
     */
    private void onPageChange(int tabPosition) {
        DTVTLogger.start();
        if (null == mHandle) {
            return;
        }
        switch (tabPosition) {
            case CHANNEL_LIST_TAB_HIKARI:
                mHandle.removeCallbacks(mRunableBs);
                mHandle.removeCallbacks(mRunableDtv);
                mHandle.removeCallbacks(mRunableTer);
                displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                break;
            case CHANNEL_LIST_TAB_DTV:
                mHandle.removeCallbacks(mRunableBs);
                mHandle.removeCallbacks(mRunableHikari);
                mHandle.removeCallbacks(mRunableTer);
                displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                break;
            case CHANNEL_LIST_TAB_TER:
                mHandle.removeCallbacks(mRunableBs);
                mHandle.removeCallbacks(mRunableDtv);
                mHandle.removeCallbacks(mRunableHikari);
                displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                break;
            case CHANNEL_LIST_TAB_BS:
                mHandle.removeCallbacks(mRunableDtv);
                mHandle.removeCallbacks(mRunableHikari);
                mHandle.removeCallbacks(mRunableTer);
                displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                break;
            default:
                break;
        }
        mPagingOffset = 1;
        mLastY = 0;
        mIsScrollUp = false;
        DTVTLogger.end();
    }

    /**
     * Tab初期化
     *
     * @param channelList channelList
     */
    private void initChannelListTab(HorizontalScrollView channelList) {
        DTVTLogger.start();
        mTabNames = getResources().getStringArray(R.array.channel_list_tab_names);
        channelList.removeAllViews();
        mTabsLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                screenWidth / SCREEN_TIME_WIDTH_PERCENT + (int) getDensity() * 5);
        mTabsLayout.setLayoutParams(layoutParams);
        mTabsLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTabsLayout.setGravity(Gravity.CENTER);
        mTabsLayout.setBackgroundColor(Color.BLACK);
        mTabsLayout.setBackgroundResource(R.drawable.rectangele_all);
        channelList.addView(mTabsLayout);
        for (int i = 0; i < mTabNames.length; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins((int) getDensity() * 30, 0, 0, 0);
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setBackgroundColor(Color.BLACK);
            tabTextView.setTextColor(Color.WHITE);
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            tabTextView.setTag(i);
            tabTextView.setBackgroundResource(0);
            tabTextView.setTextColor(Color.WHITE);
            tabTextView.setTextSize(TAB_TEXT_SIZE);
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.rectangele);
                tabTextView.setTextColor(Color.GRAY);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tab_index = (int) view.getTag();
                    setTab(tab_index);
                    mViewPager.setCurrentItem(tab_index);
                    //onPageChange(tab_index);
                }
            });
            mTabsLayout.addView(tabTextView);
        }
        DTVTLogger.end();
    }

    /**
     * Tab内容を設定
     *
     * @param position position
     */
    private void setTab(int position) {
        DTVTLogger.start();
        //onPageChange(position);
        if (mTabsLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) mTabsLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.rectangele);
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setBackgroundResource(0);
                    mTextView.setTextColor(Color.WHITE);
                }
            }
        }
        DTVTLogger.end();
    }

    /**
     * Activityを取得
     *
     * @return
     */
    ChannelListActivity getActivity() {
        return this;
    }

    /**
     * Scroll処理
     *
     * @param fragment
     * @param absListView
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(ChannelListFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    /**
     * MotionEvent処理
     *
     * @param event event
     * @return super.dispatchTouchEvent
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
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

    private Handler mHandler = new Handler();

    /**
     * Event処理
     *
     * @param fragment    fragment
     * @param absListView absListView
     * @param scrollState scrollState
     */
    @Override
    public void onScrollStateChanged(final ChannelListFragment fragment, AbsListView absListView, int scrollState) {
        DTVTLogger.start();
        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() == fragment.getDataCount() - 1 &&
                    mIsScrollUp) {
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
     * データ取得
     *
     * @param isVisibleToUser isVisibleToUser
     * @param fragment        fragment
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser, ChannelListFragment fragment) {
        DTVTLogger.start();
        if (!isVisibleToUser) {
            fragment.clearDatas();
            //fragment.noticeRefresh();
            noticeRefresh(fragment);
            DTVTLogger.end();
            return;
        }

        ChannelListAdapter.ChListDataType type = fragment.getChListDataType();
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

    //test begin
    private ChannelListAdapter.ChListDataType testType;
    //test end

    //仮データ関数 begin
    @Override
    public void onVideoBrows(DlnaRecVideoInfo curInfo) {
        DTVTLogger.start();
        int pos = mViewPager.getCurrentItem();
        //STBにチャンネル機能を提供していないで、仮データを使うために、testデータを一時コメントしている。
        //final ChannelListFragment fragment= mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        //test b
        DlnaRecVideoInfo curInfo2 = new DlnaRecVideoInfo();
        if (testType == ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS) {
            for (int i = 0; i < curInfo.size(); ++i) {
                curInfo2.addItemByValue(curInfo.get(i));
                curInfo2.get(i).mTitle = "BSチャンネル " + i;
            }
            pos = 2;
        } else if (testType == ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER) {
            for (int i = 0; i < curInfo.size(); ++i) {
                curInfo2.addItemByValue(curInfo.get(i));
                curInfo2.get(i).mTitle = "地上波チャンネル " + i;
            }
            pos = 1;
        } else if (testType == ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI) {
            pos = 0;
        } else if (testType == ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV) {
            pos = 3;
        }
        //test e
        final ChannelListFragment fragment = mFactory.createFragment(pos, this, testType); //test
//        for(int i=0;i<curInfo.size();++i){
////            fragment.addData(curInfo.get(i));   //本番
//            fragment.addData(curInfo2.get(i));      //test
//        }
        ArrayList<Object> tmp = new ArrayList();
        for (int i = 0; i < curInfo2.size(); ++i) {
            tmp.add(curInfo2.get(i));
        }
        paging(fragment, tmp);
        updateUi(fragment);
        DTVTLogger.end();
    }
    //仮データ関数 end

    /**
     * UI更新
     *
     * @param fragment
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
     * Terデータcallback
     *
     * @param curInfo current recorded video list
     */
    @Override
    public void onListUpdate(DlnaTerChListInfo curInfo) {
        DTVTLogger.start();
        int pos = mViewPager.getCurrentItem();
        final ChannelListFragment fragment = mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
        ArrayList<Object> tmp = new ArrayList();
        for (int i = 0; i < curInfo.size(); ++i) {
            Object item = curInfo.get(i);
            tmp.add(item);
        }
        paging(fragment, tmp);
        updateUi(fragment);
        DTVTLogger.end();
    }

    /**
     * Bsデータcallback
     *
     * @param curInfo current recorded video list
     */
    @Override
    public void onListUpdate(DlnaBsChListInfo curInfo) {
        DTVTLogger.start();
        int pos = mViewPager.getCurrentItem();
        final ChannelListFragment fragment = mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        ArrayList<Object> tmp = new ArrayList();
        for (int i = 0; i < curInfo.size(); ++i) {
            Object item = curInfo.get(i);
            tmp.add(item);
        }
        paging(fragment, tmp);
        updateUi(fragment);
        DTVTLogger.end();
    }

    /**
     * 実現しない
     *
     * @return
     */
    @Override
    public String getCurrentDmsUdn() {
        return null;
    }

    /**
     * データタイプ取得
     *
     * @param viewPagerIndex
     * @return
     */
    private ChannelListAdapter.ChListDataType getTypeFromViewPagerIndex(int viewPagerIndex) {
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
     * チャンネルリストアダプター
     */
    private class ChannelListPagerAdapter extends FragmentStatePagerAdapter {

        ChannelListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
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
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }

    /**
     * 縮小番組表多チャンネル情報を戻すコールバック
     *
     * @param channelsInfo 画面に渡すチャンネル番組情報
     */
    @Override
    public void channelInfoCallback(ChannelInfoList channelsInfo) {
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
     * チャンネルリストを戻す
     *
     * @param channels 　画面に渡すチャンネル情報
     */
    @Override
    public void channelListCallback(ArrayList<ChannelInfo> channels) {
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
        ArrayList<Object> tmp = new ArrayList();
        for (ChannelInfo ch : channels) {
            tmp.add(ch);
        }
        paging(fragment, tmp);
        updateUi(fragment);
        DTVTLogger.end();
    }

    /**
     * Paging処理
     *
     * @param fragment fragment
     * @param list     list
     */
    private void paging(ChannelListFragment fragment, ArrayList<Object> list) {
        DTVTLogger.start();
        final ChannelListFragment fragment2 = fragment;
        final ArrayList<Object> list2 = list;
        mHandle.post(new Runnable() {
            @Override
            public void run() {
                if (null == fragment2 || null == list2) {
                    return;
                }
                int addedCnt = 0;
                for (Object objBs : list2) {
                    if (addedCnt >= CH_LIST_ACTI_PAGING_NUMBER) {
                        break;
                    }
                    boolean has = fragment2.hasData(objBs);
                    if (!has) {
                        fragment2.addData(objBs);
                        ++addedCnt;
                    }
                }
                noticeRefresh(fragment2);
            }
        });

        DTVTLogger.end();
    }

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
     * Error処理
     *
     * @param msg エラー情報
     */
    @Override
    public void onError(String msg) {
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
                        showMessage("BSチャンネル取得できなせんでした");
                        break;
                    case CHANNEL_LIST_TAB_TER:
                        displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                        showMessage("地上波チャンネル取得できなせんでした");
                        break;
                    default:
                        break;
                }
            }
        });
        DTVTLogger.end();
    }

    /**
     * showMessage
     *
     * @param msg
     */
    private void showMessage(String msg) {
        DTVTLogger.start();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        DTVTLogger.end();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
}