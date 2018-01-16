/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;


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
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendFragmentFactory;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.List;

public class RecommendActivity extends BaseActivity implements
        RecommendBaseFragmentScrollListener, RecommendDataProvider.RecommendApiDataProviderCallback {

    private String[] mTabNames = null;
    private boolean mIsSearching = false;
    private Boolean mIsMenuLaunch = false;

    private LinearLayout mLinearLayout = null;
    private HorizontalScrollView mTabScrollView = null;
    private ViewPager mRecommendViewPager = null;

    private RecommendDataProvider mRecommendDataProvider = null;

    private static final int LOAD_PAGE_DELAY_TIME = 500;

    // レコメンドコンテンツ最大件数（システム制約）
    private int maxShowListSize = 100;
    // 表示中レコメンドコンテンツ件数(staticにしないと前回の値が維持され、データの更新に失敗する場合がある)
    private static int sShowListSize = 0;
    // 表示中の最後の行を保持
    private int mSearchLastItem = 0;
    // ページングの回数(staticにしないと前回の値が維持され、データの更新に失敗する場合がある)
    private static int sCntPageing = 0;
    // ページング判定
    private boolean mIsPaging = false;
    //アクティビティ初回起動フラグ
    private boolean mIsFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.recommend_list_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

        //初回起動フラグをONにする
        mIsFirst = true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //フォーカスを得て、初回起動だった場合の判定
        if(hasFocus && mIsFirst) {
            //画面の初期表示処理は、onCreateでは実行が早すぎて画面に表示されないので、こちらに移動
            initData();
            initRecommendListView();
            setSearchStart(false);
            requestRecommendData();

            //初回起動の処理が終了したので、falseとする
            mIsFirst = false;
        }
    }

    /**
     * データの初期化
     */
    private void initData() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mTabNames = getResources().getStringArray(R.array.recommend_list_tab_names);
        mRecommendDataProvider = new RecommendDataProvider(this);
    }

    /**
     * 検索中フラグの変更
     *
     * @param seachingFlag
     */
    private void setSearchStart(boolean seachingFlag) {
        synchronized (this) {
            mIsSearching = seachingFlag;
        }
    }

    /**
     * データプロバイダへデータ取得要求
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
        if (null == mRecommendViewPager) {
            return;
        }

        int requestService = mRecommendViewPager.getCurrentItem();
        int startIndex = sShowListSize + 1;

        //戻り値を使用せず、データは必ずコールバック経由なので、falseを指定する
        mRecommendDataProvider.startGetRecommendData(
                requestService, startIndex,
                SearchConstants.RecommendList.requestMaxCount_Recommend,false);
    }

    /**
     * レコメンドのリストを初期化
     */
    private void initRecommendListView() {
        if (null != mRecommendViewPager) {
            return;
        }
        mRecommendViewPager = findViewById(R.id.vp_recommend_list_items);
        mTabScrollView = findViewById(R.id.recommend_tab_strip_scroll);
        initTabVIew();

        mRecommendViewPager.setAdapter(new RecommendActivity.TabAdpater(getSupportFragmentManager(), this));
        // フリックによるtab切り替え
        mRecommendViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);
                clearAllFragment();
                setPagingStatus(false);
                sShowListSize = 0;
                sCntPageing = 0;
                //ここでフラグをクリアしないと、以後の更新が行われなくなる場合がある
                setSearchStart(false);
                requestRecommendData();
            }
        });
    }

    /**
     * tabの関連Viewを初期化
     */
    private void initTabVIew() {
        mTabScrollView.removeAllViews();
        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(layoutParams);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setBackgroundColor(Color.BLACK);
        mLinearLayout.setGravity(Gravity.CENTER);
        mTabScrollView.addView(mLinearLayout);

        // tabの設定
        for (int i = 0; i < mTabNames.length; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if (i != 0) {
                params.setMargins(30, 0, 0, 0);
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTextSize(15);
            tabTextView.setBackgroundColor(Color.BLACK);
            tabTextView.setTextColor(Color.WHITE);
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            tabTextView.setTag(i);
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.indicating);
            }
            // tabタップ時の処理
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    mRecommendViewPager.setCurrentItem(position);
                    setTab(position);
                    sShowListSize = 0;
                    sCntPageing = 0;
                    //ここでフラグをクリアしないと、以後の更新が行われなくなる場合がある
                    setSearchStart(false);
                    requestRecommendData();
                }
            });
            mLinearLayout.addView(tabTextView);
        }
    }

    /**
     * インジケーター設置
     *
     * @param position
     */
    public void setTab(int position) {
        if (mLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView textView = (TextView) mLinearLayout.getChildAt(i);
                if (position == i) {
                    textView.setBackgroundResource(R.drawable.indicating);
                } else {
                    textView.setBackgroundResource(R.drawable.indicating_no);
                }
            }
        }
    }

    /**
     * フラグメントの取得
     *
     * @return
     */
    private RecommendBaseFragment getCurrentRecommendBaseFragment() {
        int currentPageNo = mRecommendViewPager.getCurrentItem();
        RecommendBaseFragment baseFragment = RecommendFragmentFactory.createFragment(currentPageNo, this);
        return baseFragment;
    }

    /**
     * レコメンド取得完了時の表示処理
     *
     * @param resultInfoList
     */
    public void recommendDataProviderSuccess(List<ContentsData> resultInfoList) {
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
            baseFragment.notifyDataSetChanged();
            baseFragment.setSelection(mSearchLastItem);
            baseFragment.displayLoadMore(false);
            setSearchStart(false);
        }
    }

    /**
     * フラグメントクリア
     */
    public void clearAllFragment() {

        if (null != mRecommendViewPager) {
            int sum = RecommendFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                RecommendBaseFragment baseFragment = RecommendFragmentFactory.createFragment(i, this);
                baseFragment.clear();
            }
        }
    }

    /**
     * データ取得失敗時の処理
     */
    public void recommendDataProviderFinishNg() {
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
     * ページング判定の変更
     *
     * @param bool
     */
    private void setPagingStatus(boolean bool) {
        synchronized (this) {
            mIsPaging = bool;
        }
    }

    /*タブ専用アダプター*/
    private class TabAdpater extends FragmentStatePagerAdapter {

        private RecommendActivity mRecommendActivity = null;

        TabAdpater(FragmentManager fm, RecommendActivity top) {
            super(fm);
            mRecommendActivity = top;
        }

        @Override
        public Fragment getItem(int position) {
            synchronized (this) {
                return RecommendFragmentFactory.createFragment(position, mRecommendActivity);
            }
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

    @Override
    public void onScroll(RecommendBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mSearchLastItem = firstVisibleItem + visibleItemCount - 1;

        int pageMax = (sCntPageing + 1) * SearchConstants.RecommendList.requestMaxCount_Recommend;
        DTVTLogger.debug("onScroll.first:" + firstVisibleItem +
                " .visible:" + visibleItemCount + " .total:" + totalItemCount +
                " dataSize:" + fragment.mData.size());
        if (maxShowListSize > fragment.mData.size() && // システム制約最大値 100件
                fragment.mData.size() != 0 && // 取得結果0件以外
                firstVisibleItem + visibleItemCount >= pageMax) { // 表示中の最下まで行ったかの判定
            sCntPageing += 1;
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
     * おすすめテレビ用コールバック
     *
     * @param recommendContentInfoList
     */
    @Override
    public void RecommendChannelCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("Chan Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
        if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TV) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }
    }

    /**
     * おすすめビデオ用コールバック
     *
     * @param recommendContentInfoList
     */
    @Override
    public void RecommendVideoCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("vid Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
        if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_VIDEO) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }
    }

    /**
     * おすすめdTV用コールバック
     *
     * @param recommendContentInfoList
     */
    @Override
    public void RecommendDTVCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("dtv Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
        if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }

    }

    /**
     * おすすめdアニメ用コールバック
     *
     * @param recommendContentInfoList
     */
    @Override
    public void RecommendDAnimeCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("ani Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
        if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DANIME) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }
    }

    /**
     * おすすめdチャンネル用コールバック
     *
     * @param recommendContentInfoList
     */
    @Override
    public void RecommendDChannelCallback(List<ContentsData> recommendContentInfoList) {
        DTVTLogger.debug("dCH Callback DataSize:"
                + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
        if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_DTV_CHANNEL) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }
    }

    /**
     * 0件コールバック
     */
    @Override
    public void RecommendNGCallback() {
        recommendDataProviderFinishNg();
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