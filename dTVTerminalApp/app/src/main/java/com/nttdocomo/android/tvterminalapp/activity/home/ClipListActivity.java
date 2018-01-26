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
import com.nttdocomo.android.tvterminalapp.adapter.ClipMainAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.TvClipDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.VodClipDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListFragmentFactory;
import com.nttdocomo.android.tvterminalapp.model.TabItemLayout;

import java.util.List;

public class ClipListActivity extends BaseActivity implements
        VodClipDataProvider.ApiDataProviderCallback,
        TvClipDataProvider.TvClipDataProviderCallback,
        ClipListBaseFragmentScrollListener,
        TabItemLayout.OnClickTabTextListener {

    private String[] mTabNames = null;
    private boolean mIsCommunicating = false;

    private LinearLayout mLinearLayout = null;
    private TabItemLayout mTabLayout = null;
    private ViewPager mViewPager = null;
    private Boolean mIsMenuLaunch = false;

    private VodClipDataProvider mVodClipDataProvider = null;
    private TvClipDataProvider mTvClipDataProvider = null;
    private ClipListFragmentFactory mClipListFragmentFactory = null;

    private final int NUM_PER_PAGE = 10;

    private static final int CLIP_LIST_PAGE_NO_OF_TV = 0;
    private static final int CLIP_LIST_PAGE_NO_OF_VOD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clip_list_main);

        //Headerの設定
        setTitleText(getString(R.string.str_clip_activity_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

        initData();
        initView();
        resetPaging();
        setTv();
    }

    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            ClipListBaseFragment baseFragment = getCurrentFragment();
            if (null != baseFragment && null != baseFragment.mData) {
                baseFragment.mData.clear();
                baseFragment.noticeRefresh();
            }
        }
    }

    private void setCommunicatingStatus(boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }

    private int getCurrentNumber() {
        ClipListBaseFragment baseFragment = getCurrentFragment();
        if (null == baseFragment || null == baseFragment.mData || 0 == baseFragment.mData.size()) {
            return 0;
        }
        return baseFragment.mData.size() / NUM_PER_PAGE;
    }

    /**
     * 取得結果が更新前と同じなら更新しない
     *
     * @param tvClipContentInfo 取得コンテンツデータ
     * @return
     */
    private boolean isSkipTv(List<ContentsData> tvClipContentInfo) {
        ClipListBaseFragment baseFragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);
        if (null == baseFragment || null == baseFragment.mData || 0 == baseFragment.mData.size()) {
            return false;
        }

        if (null == tvClipContentInfo || 0 == tvClipContentInfo.size()) {
            return true;
        }

        ContentsData item1 = baseFragment.mData.get(baseFragment.mData.size() - 1);
        ContentsData item2 = tvClipContentInfo.get(tvClipContentInfo.size() - 1);
        return isContentEqual(item1, item2);
    }

    /**
     * 取得結果が更新前と同じなら更新しない
     *
     * @param vodClipContentInfo 取得コンテンツデータ
     * @return
     */
    private boolean isSkipVod(List<ContentsData> vodClipContentInfo) {
        ClipListBaseFragment baseFragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);
        if (null == baseFragment || null == baseFragment.mData || 0 == baseFragment.mData.size()) {
            return false;
        }

        if (null == vodClipContentInfo || 0 == vodClipContentInfo.size()) {
            return true;
        }

        ContentsData item1 = baseFragment.mData.get(baseFragment.mData.size() - 1);
        ContentsData item2 = vodClipContentInfo.get(vodClipContentInfo.size() - 1);
        return isContentEqual(item1, item2);
    }

    /**
     * 同じコンテンツデータか判定
     *
     * @param item1 取得済みデータ
     * @param item2 更新データ
     * @return 判定結果
     */
    private boolean isContentEqual(ContentsData item1, ContentsData item2) {
        if (null == item1 || null == item2) {
            return false;
        }
        return item1.getThumURL().equals(item2.getThumURL())
                && item1.getRatStar().equals(item2.getRatStar())
                && item1.getTitle().equals(item2.getTitle());
    }

    private void initData() {
        mTabNames = getResources().getStringArray(R.array.tab_clip_names);
        mVodClipDataProvider = new VodClipDataProvider(this);
        mTvClipDataProvider = new TvClipDataProvider(this);
        mClipListFragmentFactory = new ClipListFragmentFactory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < 2; ++i) {
            ClipListBaseFragment b = mClipListFragmentFactory.createFragment(i, this);
            if (null != b) {
                b.mData.clear();
            }
        }
    }

    @Override
    public void tvClipListCallback(List<ContentsData> clipContentInfo) {
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
            if (null != fragment.mData) {
                fragment.mData.add(clipContentInfo.get(i));
            }
        }

        DTVTLogger.debug("tvClipListCallback");

        resetCommunication();
        fragment.noticeRefresh();
    }

    @Override
    public void vodClipListCallback(List<ContentsData> clipContentInfo) {
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
            if (null != fragment.mData) {
                fragment.mData.add(clipContentInfo.get(i));
            }
        }

        DTVTLogger.debug("vodClipListCallback");

        resetCommunication();
        fragment.noticeRefresh();
    }

    private void resetCommunication() {
        ClipListBaseFragment baseFragment = getCurrentFragment();
        if (null == baseFragment) {
            return;
        }
        baseFragment.displayMoreData(false);
        setCommunicatingStatus(false);
    }

    @Override
    public void onScroll(ClipListBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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
    public void onScrollStateChanged(ClipListBaseFragment fragment, AbsListView absListView, int scrollState) {
        synchronized (this) {

            ClipListBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getClipMainAdapter()) {
                return;
            }
            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() == fragment.getClipMainAdapter().getCount() - 1) {

                if (mIsCommunicating) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");


                fragment.displayMoreData(true);
                setCommunicatingStatus(true);

                final ClipListBaseFragment finalFragment = fragment;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int offset = 0;
                        if (null != finalFragment.mData) {
                            offset = finalFragment.mData.size();
                        }
                        switch (mViewPager.getCurrentItem()) {
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

    private ClipListActivity getClipListActivity() {
        return this;
    }

    /**
     * 検索結果タブ専用アダプター
     */
    class ClipPagerAdapter extends FragmentStatePagerAdapter {
        public ClipPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mClipListFragmentFactory.createFragment(position, getClipListActivity());
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
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTabLayout.setTab(position);

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

    private void setVod() {

        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);
        //fragment.clearAllDatas();
        fragment.setMode(ClipMainAdapter.Mode.CLIP_LIST_MODE_VIDEO);
        mVodClipDataProvider.getClipData(1);
    }

    private void setTv() {
        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);
        //fragment.clearAllDatas();
        fragment.setMode(ClipMainAdapter.Mode.CLIP_LIST_MODE_TV);
        mTvClipDataProvider.getClipData(1);
    }

    /**
     * tabに関連Viewの初期化
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
    public void onClickTab(int position) {
        DTVTLogger.start("position = " + position);
        if (null != mViewPager) {
            DTVTLogger.debug("viewpager not null");
            mViewPager.setCurrentItem(position);
        }
        DTVTLogger.end();
    }

    /**
     * インジケーター設置
     * @param position
     */
    public void setTab(int position) {
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

    private ClipListBaseFragment getCurrentFragment() {

        int i = mViewPager.getCurrentItem();
        return mClipListFragmentFactory.createFragment(i, this);
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