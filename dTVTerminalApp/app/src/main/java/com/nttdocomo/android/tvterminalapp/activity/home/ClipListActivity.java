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
import android.widget.TextView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.fragment.cliplist.ClipListBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.cliplist.ClipListBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.cliplist.ClipListFragmentFactory;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.TvClipDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.VodClipDataProvider;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;

import java.util.List;

/**
 * クリップ画面.
 */
public class ClipListActivity extends BaseActivity implements
        VodClipDataProvider.ApiDataProviderCallback,
        TvClipDataProvider.TvClipDataProviderCallback,
        ClipListBaseFragmentScrollListener,
        TabItemLayout.OnClickTabTextListener {

    /**
     * タブ名.
     */
    private String[] mTabNames = null;
    /**
     * 接続中フラグ.
     */
    private boolean mIsCommunicating = false;

    /**
     * レイアウト.
     */
    private LinearLayout mLinearLayout = null;
    /**
     * タブアイテムレイアウト.
     */
    private TabItemLayout mTabLayout = null;
    /**
     * ViewPager.
     */
    private ViewPager mViewPager = null;
    /**
     * メニュー表示フラグ.
     */
    private Boolean mIsMenuLaunch = false;

    /**
     * VodClipデータプロバイダ.
     */
    private VodClipDataProvider mVodClipDataProvider = null;
    /**
     * TvClipデータプロバイダ.
     */
    private TvClipDataProvider mTvClipDataProvider = null;
    /**
     * FragmentFactory.
     */
    private ClipListFragmentFactory mClipListFragmentFactory = null;
    /**
     * ページング単位設定値.
     */
    private final int NUM_PER_PAGE = 20;

    /**
     * タブポジション(Tv).
     */
    private static final int CLIP_LIST_PAGE_NO_OF_TV = 0;
    /**
     * タブポジション(ビデオ).
     */
    public static final int CLIP_LIST_PAGE_NO_OF_VOD = 1;
    /**
     * 表示開始タブ指定キー.
     */
    public static final String CLIP_LIST_START_PAGE = "clipListStartPage";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clip_list_main);

        //Headerの設定
        setTitleText(getString(R.string.str_clip_activity_title));
        Intent intent = getIntent();
        int startPageNo = intent.getIntExtra(CLIP_LIST_START_PAGE, CLIP_LIST_PAGE_NO_OF_TV);
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);
        setStatusBarColor(true);

        initData();
        initView();
        resetPaging();
        //前画面からのタブ指定起動反映
        if (startPageNo == CLIP_LIST_PAGE_NO_OF_VOD) {
            setVod();
        } else {
            setTv();
        }
        //初回表示のみ前画面からのタブ指定を反映する
        mViewPager.setCurrentItem(startPageNo);
        mTabLayout.setTab(startPageNo);
    }

    /**
     * リセットページ開始.
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            ClipListBaseFragment baseFragment = getCurrentFragment();
            if (null != baseFragment && null != baseFragment.mClipListData) {
                baseFragment.mClipListData.clear();
                baseFragment.noticeRefresh();
            }
        }
    }

    /**
     * 接続中フラグ設定.
     *
     * @param bool 接続中フラグ
     */
    private void setCommunicatingStatus(final boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }

    /**
     * ページング単位値取得.
     *
     * @return ページング単位
     */
    private int getCurrentNumber() {
        ClipListBaseFragment baseFragment = getCurrentFragment();
        if (null == baseFragment || null == baseFragment.mClipListData || 0 == baseFragment.mClipListData.size()) {
            return 0;
        }
        return baseFragment.mClipListData.size() / NUM_PER_PAGE;
    }

    /**
     * 取得結果が更新前と同じなら更新しない.
     *
     * @param tvClipContentInfo 取得コンテンツデータ
     * @return コンテンツデータ更新フラグ
     */
    private boolean isSkipTv(final List<ContentsData> tvClipContentInfo) {
        ClipListBaseFragment baseFragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);
        if (null == baseFragment || null == baseFragment.mClipListData || 0 == baseFragment.mClipListData.size()) {
            return false;
        }

        if (null == tvClipContentInfo || 0 == tvClipContentInfo.size()) {
            return true;
        }

        ContentsData item1 = baseFragment.mClipListData.get(baseFragment.mClipListData.size() - 1);
        ContentsData item2 = tvClipContentInfo.get(tvClipContentInfo.size() - 1);
        return isContentEqual(item1, item2);
    }

    /**
     * 取得結果が更新前と同じなら更新しない.
     *
     * @param vodClipContentInfo 取得コンテンツデータ
     * @return コンテンツデータ更新フラグ
     */
    private boolean isSkipVod(final List<ContentsData> vodClipContentInfo) {
        ClipListBaseFragment baseFragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);
        if (null == baseFragment || null == baseFragment.mClipListData || 0 == baseFragment.mClipListData.size()) {
            return false;
        }

        if (null == vodClipContentInfo || 0 == vodClipContentInfo.size()) {
            return true;
        }

        ContentsData item1 = baseFragment.mClipListData.get(baseFragment.mClipListData.size() - 1);
        ContentsData item2 = vodClipContentInfo.get(vodClipContentInfo.size() - 1);
        return isContentEqual(item1, item2);
    }

    /**
     * 同じコンテンツデータか判定.
     *
     * @param item1 取得済みデータ
     * @param item2 更新データ
     * @return 判定結果
     */
    private boolean isContentEqual(final ContentsData item1, final ContentsData item2) {
        if (null == item1 || null == item2) {
            return false;
        }
        return item1.getThumURL().equals(item2.getThumURL())
                && item1.getRatStar().equals(item2.getRatStar())
                && item1.getTitle().equals(item2.getTitle());
    }

    /**
     * データ初期化.
     */
    private void initData() {
        mTabNames = getResources().getStringArray(R.array.tab_clip_names);
        mVodClipDataProvider = new VodClipDataProvider(this);
        mTvClipDataProvider = new TvClipDataProvider(this);
        mClipListFragmentFactory = new ClipListFragmentFactory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //全フラグメントを削除
        if (null != mViewPager) {
            int sum = mClipListFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                ClipListBaseFragment b = mClipListFragmentFactory.createFragment(i, this);
                if (null != b) {
                    b.mClipListData.clear();
                }
            }
        }
    }

    @Override
    public void tvClipListCallback(final List<ContentsData> clipContentInfo) {
        if (null == clipContentInfo || 0 == clipContentInfo.size()) {
            //通信とJSON Parseに関してerror処理
            DTVTLogger.debug("ClipListActivity::TvClipListCallback, get data failed.");
            // TODO:エラーメッセージ表示はリスト画面上に表示する
            Toast.makeText(this, "クリップデータ取得失敗", Toast.LENGTH_SHORT).show();
            return;
        }

        if (0 == clipContentInfo.size()) {
            //doing
            resetCommunication();
            return;
        }

        if (isSkipTv(clipContentInfo)) {
            resetCommunication();
            return;
        }

        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE && i < clipContentInfo.size(); ++i) {
            if (null != fragment.mClipListData) {
                fragment.mClipListData.add(clipContentInfo.get(i));
            }
        }

        DTVTLogger.debug("tvClipListCallback");

        resetCommunication();
        fragment.noticeRefresh();
    }

    @Override
    public void vodClipListCallback(final List<ContentsData> clipContentInfo) {
        if (null == clipContentInfo || 0 == clipContentInfo.size()) {
            //通信とJSON Parseに関してerror処理
            DTVTLogger.debug("ClipListActivity::VodClipListCallback, get data failed");
            // TODO:エラーメッセージ表示はリスト画面上に表示する
            Toast.makeText(this, "クリップデータ取得失敗", Toast.LENGTH_SHORT).show();
            return;
        }

        if (0 == clipContentInfo.size()) {
            //doing
            resetCommunication();
            return;
        }

        if (isSkipVod(clipContentInfo)) {
            resetCommunication();
            return;
        }

        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE && i < clipContentInfo.size(); ++i) {
            if (null != fragment.mClipListData) {
                fragment.mClipListData.add(clipContentInfo.get(i));
            }
        }

        DTVTLogger.debug("vodClipListCallback");

        resetCommunication();
        fragment.noticeRefresh();
    }

    /**
     * 接続中止.
     */
    private void resetCommunication() {
        ClipListBaseFragment baseFragment = getCurrentFragment();
        if (null == baseFragment) {
            return;
        }
        baseFragment.displayMoreData(false);
        setCommunicatingStatus(false);
    }

    @Override
    public void onScroll(
            final ClipListBaseFragment fragment, final AbsListView absListView,
            final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        synchronized (this) {
            ClipListBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getClipMainAdapter()) {
                return;
            }
            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                    ) {
                DTVTLogger.debug("ClipListActivity::onScroll, paging, firstVisibleItem=" + firstVisibleItem + ", totalItemCount=" + totalItemCount + ", visibleItemCount=" + visibleItemCount);
            }
        }
    }

    @Override
    public void onScrollStateChanged(final ClipListBaseFragment fragment,
                                     final AbsListView absListView, final int scrollState) {
        synchronized (this) {

            ClipListBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getClipMainAdapter()) {
                return;
            }
            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == fragment.getClipMainAdapter().getCount() - 1) {

                if (mIsCommunicating) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");


                fragment.displayMoreData(true);
                setCommunicatingStatus(true);

                //非同期中にタブ移動することがあるため、ここでスクロール開始タブ位置を保持しておく
                final int TAB_POSITION = mViewPager.getCurrentItem();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int offset = 0;
                        if (null != fragment.mClipListData) {
                            offset = fragment.mClipListData.size();
                        }
                        switch (TAB_POSITION) {
                            case CLIP_LIST_PAGE_NO_OF_TV:
                                mTvClipDataProvider.getClipData(offset);
                                break;
                            case CLIP_LIST_PAGE_NO_OF_VOD:
                                mVodClipDataProvider.getClipData(offset);
                                break;
                            default:
                                break;
                        }
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    /**
     * Activity取得.
     *
     * @return this
     */
    private ClipListActivity getClipListActivity() {
        return this;
    }

    /**
     * 検索結果タブ専用アダプター.
     */
    private class ClipPagerAdapter extends FragmentStatePagerAdapter {
        /**
         * コンストラクタ.
         *
         * @param fm FragmentManager
         */
        private ClipPagerAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            return mClipListFragmentFactory.createFragment(position, getClipListActivity());
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
     * 画面初期設定.
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        mViewPager = findViewById(R.id.vp_clip_result);
        ClipPagerAdapter clipPagerAdapter
                = new ClipPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(clipPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                mTabLayout.setTab(position);
                //タブ移動時にページリセット
                resetPaging();

                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        setTv();
                        break;
                    case 1:
                        setVod();
                        break;
                    default:
                        break;
                }
            }
        });
        initTabVIew();
    }

    /**
     * クリップ(ビデオ)画面設定.
     */
    private void setVod() {

        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);
        //スワイプ時にページング中のプログレスバーを非表示にする
        resetCommunication();
        fragment.setMode(ContentsAdapter.ActivityTypeItem.TYPE_CLIP_LIST_MODE_VIDEO);
        mVodClipDataProvider.getClipData(1);
    }

    /**
     * クリップ(テレビ)画面設定.
     */
    private void setTv() {
        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);
        //スワイプ時にページング中のプログレスバーを非表示にする
        resetCommunication();
        fragment.setMode(ContentsAdapter.ActivityTypeItem.TYPE_CLIP_LIST_MODE_TV);
        mTvClipDataProvider.getClipData(1);
    }

    /**
     * tabに関連Viewの初期化.
     */
    private void initTabVIew() {
        DTVTLogger.start();
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.CLIP_LIST_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_clip_list_tab);
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
        }
        DTVTLogger.end();
    }

    /**
     * インジケーター設置.
     *
     * @param position インジケータ位置
     */
    public void setTab(final int position) {
        //mCurrentPageNum = position;
        if (mLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) mLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.indicating_common);
                } else {
                    mTextView.setBackgroundResource(R.drawable.indicating_no_common);
                }
            }
        }
    }

    /**
     * Fragment生成.
     *
     * @return Fragment
     */
    private ClipListBaseFragment getCurrentFragment() {

        int i = mViewPager.getCurrentItem();
        return mClipListFragmentFactory.createFragment(i, this);
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
}