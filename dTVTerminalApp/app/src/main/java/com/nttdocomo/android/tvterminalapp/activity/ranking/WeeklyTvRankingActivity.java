/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.ranking;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.DtvContentsDetailActivity;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.JsonContents;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingConstants;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeeklyTvRankingActivity extends BaseActivity implements View.OnClickListener,
        RankingTopDataProvider.WeeklyRankingApiDataProviderCallback, RankingFragmentScrollListener {

    private ImageView mMenuImageView;
    private boolean mIsCommunicating = false;
    private final int NUM_PER_PAGE = 10;
    private String[] mTabNames;
    private RankingTopDataProvider mRankingDataProvider;
    private RankingFragmentFactory mRankingFragmentFactory = null;
    private HorizontalScrollView mTabScrollView;
    private LinearLayout mLinearLayout;
    private ViewPager mViewPager;

    //標準タブ数
    private static final int DEFAULT_TAB_MAX = 4;

    //設定するマージンのピクセル数
    private static final int LEFT_MARGIN = 30;
    //設定する文字サイズ
    private static final int TEXT_SIZE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_tv_ranking_main_layout);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.weekly_tv_ranking_title));
        initData();
        initView();
        resetPaging();
        getGenreData(mViewPager.getCurrentItem());
    }

    // ページのリセット
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            RankingBaseFragment baseFragment = getCurrentFragment();
            if (null != baseFragment && null != baseFragment.mData) {
                baseFragment.mData.clear();
                baseFragment.noticeRefresh();
            }
        }
    }

    /**
     * mIsCommunicationを変更
     */
    private void setCommunicatingStatus(boolean bool) {
        synchronized (this) {
            mIsCommunicating = bool;
        }
    }

    /**
     * ページングを行った回数を取得
     *
     * @return ページング回数
     */
    private int getCurrentNumber() {
        RankingBaseFragment baseFragment = getCurrentFragment();
        if (null == baseFragment || null == baseFragment.mData || 0 == baseFragment.mData.size()) {
            return 0;
        } else if (baseFragment.mData.size() < NUM_PER_PAGE) {
            return 1;
        }
        return baseFragment.mData.size() / NUM_PER_PAGE;
    }

    /**
     * データの初期化
     */
    private void initData() {
        mTabNames = getResources().getStringArray(R.array.ranking_tab_names);
        mRankingDataProvider =
                new RankingTopDataProvider(this, RankingConstants.RANKING_MODE_NO_OF_WEEKLY);
        mRankingFragmentFactory = new RankingFragmentFactory();
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
            RankingBaseFragment b = mRankingFragmentFactory.createFragment
                    (RankingConstants.RANKING_MODE_NO_OF_WEEKLY, i, this);
            if (null != b) {
                b.mData.clear();
            }
        }
    }

    /**
     * Viewの初期化
     */
    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
        mTabScrollView = findViewById(R.id.weekly_ranking_tab_strip_scroll);
        mViewPager = findViewById(R.id.vp_weekly_ranking_result);
        RankingPagerAdapter rankingPagerAdapter
                = new RankingPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(rankingPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                setTab(position);
                getGenreData(mViewPager.getCurrentItem());
            }
        });
        initTabData();
    }

    /**
     * タブ毎にリクエストを行う
     */
    private void getGenreData(int tabPageNo) {
        switch (tabPageNo) {
            case RankingConstants.RANKING_PAGE_NO_OF_SYNTHESIS:
                mRankingDataProvider.getWeeklyRankingData(tabPageNo);
                break;
            case RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_MOVIE:
                mRankingDataProvider.getWeeklyRankingData(tabPageNo);
                break;
            case RankingConstants.RANKING_PAGE_NO_OF_DOMESTIC_MOVIE:
                mRankingDataProvider.getWeeklyRankingData(tabPageNo);
                break;
            case RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_CHANNEL:
                mRankingDataProvider.getWeeklyRankingData(tabPageNo);
                break;
            default:
                break;
        }
    }

    /**
     * tabのレイアウトを設定
     */
    private void initTabData() {
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

        for (int i = 0; i < mTabNames.length; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if (i != 0) {
                params.setMargins(LEFT_MARGIN, 0, 0, 0);
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTextSize(TEXT_SIZE);
            tabTextView.setBackgroundColor(Color.BLACK);
            tabTextView.setTextColor(Color.WHITE);
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            tabTextView.setTag(i);
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.indicating);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // タップによるタブ切り替え
                    int position = (int) view.getTag();
                    mViewPager.setCurrentItem(position);
                    setTab(position);
                }
            });
            mLinearLayout.addView(tabTextView);
        }
    }

    /**
     * 取得結果の設定・表示
     */
    private void setShowWeeklyRanking(List<ContentsData> contentsDataList) {
        if (null == contentsDataList || 0 == contentsDataList.size()) {
            //通信とJSON Parseに関してerror処理
            //TODO: メッセージは仕様検討の必要あり
            Toast.makeText(this, "ランキングデータ取得失敗", Toast.LENGTH_SHORT).show();
            return;
        }

        RankingBaseFragment fragment = mRankingFragmentFactory.createFragment
                (RankingConstants.RANKING_MODE_NO_OF_WEEKLY, mViewPager.getCurrentItem(), this);

        //既に元のデータ以上の件数があれば足す物は無いので、更新せずに帰る
        if (null != fragment.mData && fragment.mData.size() >= contentsDataList.size()) {
            fragment.displayMoreData(false);
            return;
        }

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE
                && i < contentsDataList.size(); ++i) {
            if (null != fragment.mData) {
                fragment.mData.add(contentsDataList.get(i));
            }
        }
        DTVTLogger.end("Fragment.mData.size :" + String.valueOf(fragment.mData.size()));
        resetCommunication();
        fragment.noticeRefresh();
    }


    /**
     * 読み込み中表示を非表示に変更
     */
    private void resetCommunication() {
        RankingBaseFragment b = getCurrentFragment();
        if (null == b) {
            return;
        }
        b.displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * コンテンツ詳細への遷移
     */
    public void contentsDetailButton(View view) {
        startActivity(DtvContentsDetailActivity.class, null);
    }

    @Override
    public void onClick(View view) {
        if (mMenuImageView.equals(view)) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    @Override
    public void onScroll(RankingBaseFragment fragment, AbsListView absListView,
                         int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        synchronized (this) {
            RankingBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getRankingAdapter()) {
                return;
            }

            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                    ) {
                DTVTLogger.debug("Activity::onScroll, paging, firstVisibleItem="
                        + firstVisibleItem + ", totalItemCount=" + totalItemCount
                        + ", visibleItemCount=" + visibleItemCount);

            }
        }
    }

    @Override
    public void onScrollStateChanged(RankingBaseFragment fragment, AbsListView absListView,
                                     int scrollState) {
        synchronized (this) {

            RankingBaseFragment baseFragment = getCurrentFragment();
            if (null == baseFragment || null == fragment.getRankingAdapter()) {
                return;
            }
            if (!fragment.equals(baseFragment)) {
                return;
            }

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() ==
                            fragment.getRankingAdapter().getCount() - 1) {

                if (mIsCommunicating) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");

                fragment.displayMoreData(true);
                setCommunicatingStatus(true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getGenreData(mViewPager.getCurrentItem());
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }

    }

    /*インジケーター設置*/
    public void setTab(int position) {
        //mCurrentPageNum = position;
        if (mLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) mLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.indicating);
                } else {
                    mTextView.setBackgroundResource(R.drawable.indicating_no);
                }
            }
        }
    }

    /**
     * Fragmentの取得
     *
     * @return Fragment
     */
    private RankingBaseFragment getCurrentFragment() {

        int i = mViewPager.getCurrentItem();
        return mRankingFragmentFactory.createFragment
                (RankingConstants.RANKING_MODE_NO_OF_WEEKLY, i, this);
    }

    public WeeklyTvRankingActivity getWeeklyTvRankingActivity() {
        return this;
    }


    /**
     * 取得条件"総合"用コールバック
     */
    @Override
    public void weeklyRankSynthesisCallback(List<ContentsData> contentsDataList) {
        DTVTLogger.start("ResponseDataSize :" + contentsDataList.size());
        if (mViewPager.getCurrentItem() == RankingConstants.RANKING_PAGE_NO_OF_SYNTHESIS) {
            setShowWeeklyRanking(contentsDataList);
        }
    }

    /**
     * 取得条件"海外映画"用コールバック
     */
    @Override
    public void weeklyRankOverseasMovieCallback(List<ContentsData> contentsDataList) {
        DTVTLogger.start("ResponseDataSize :" + contentsDataList.size());
        if (mViewPager.getCurrentItem() ==
                RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_MOVIE) {
            setShowWeeklyRanking(contentsDataList);
        }
    }

    /**
     * 取得条件"国内映画"用コールバック
     */
    @Override
    public void weeklyRankDomesticMovieCallback(List<ContentsData> contentsDataList) {
        DTVTLogger.start("ResponseDataSize :" + contentsDataList.size());
        if (mViewPager.getCurrentItem() ==
                RankingConstants.RANKING_PAGE_NO_OF_DOMESTIC_MOVIE) {
            setShowWeeklyRanking(contentsDataList);
        }
    }

    /**
     * 取得条件"海外TV番組・ドラマ"用コールバック
     */
    @Override
    public void weeklyRankOverseasChannelCallback(List<ContentsData> contentsDataList) {
        DTVTLogger.start("ResponseDataSize :" + contentsDataList.size());
        if (mViewPager.getCurrentItem() ==
                RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_CHANNEL) {
            setShowWeeklyRanking(contentsDataList);
        }
    }

    /*検索結果タブ専用アダプター*/
    class RankingPagerAdapter extends FragmentStatePagerAdapter {
        public RankingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mRankingFragmentFactory.createFragment
                    (RankingConstants.RANKING_MODE_NO_OF_WEEKLY,
                            position, getWeeklyTvRankingActivity());
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
}

