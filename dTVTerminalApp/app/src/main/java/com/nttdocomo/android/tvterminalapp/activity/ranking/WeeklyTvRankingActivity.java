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
import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RankingTopDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingConstants;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.ranking.RankingFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.webapiclient.jsonparser.WeeklyRankJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeeklyTvRankingActivity extends BaseActivity implements View.OnClickListener, RankingTopDataProvider.WeeklyRankingApiDataProviderCallback, RankingFragmentScrollListener {

    private ImageView mMenuImageView;
    private boolean mIsCommunicating = false;
    private int NUM_PER_PAGE = 2;
    private String[] mTabNames;
    private RankingTopDataProvider mRankingDataProvider;
    private RankingFragmentFactory mRankingFragmentFactory = null;
    private HorizontalScrollView mTabScrollView;
    private LinearLayout mLinearLayout;
    private ViewPager mViewPager;
    private static final int sLoadPageDelayTime = 1000;

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
            RankingBaseFragment b = getCurrentFragment();
            if (null != b && null != b.mData) {
                b.mData.clear();
                b.noticeRefresh();
            }
        }
    }

    /**
     * mIsCommunicationを変更
     * @param b
     */
    private void setCommunicatingStatus(boolean b) {
        synchronized (this) {
            mIsCommunicating = b;
        }
    }

    /**
     * ページングを行った回数を取得
     *
     * @return
     */
    private int getCurrentNumber() {
        RankingBaseFragment b = getCurrentFragment();
        if (null == b || null == b.mData || 0 == b.mData.size()) {
            return 0;
        }
        return b.mData.size() / NUM_PER_PAGE;
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
        for (int i = 0; i < 4; ++i) { // タブの数だけ処理を行う
            RankingBaseFragment b = mRankingFragmentFactory.createFragment
                    (i, this);
            if (null != b) {
                b.mData.clear();
            }
        }
    }

    /**
     * Viewの初期化
     */
    private void initView() {
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
     *
     * @param tabPageNo
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
    private void setShowWeeklyRanking(List<Map<String, String>> weeklyRankMapList) {
        if (null == weeklyRankMapList || 0 == weeklyRankMapList.size()) {
            //通信とJSON Parseに関してerror処理
            Toast.makeText(this, "ランキングデータ取得失敗", Toast.LENGTH_SHORT);
            return;
        }
        List<ContentsData> rankingContentInfo = setWeeklyRankingContentData(weeklyRankMapList);


        RankingBaseFragment fragment = mRankingFragmentFactory.createFragment
                (mViewPager.getCurrentItem(), this);

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE && i < rankingContentInfo.size(); ++i) {
            if (null != fragment.mData) {
                fragment.mData.add(rankingContentInfo.get(i));
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
     * 取得したリストマップをContentsDataクラスへ入れる
     */
    private List<ContentsData> setWeeklyRankingContentData(List<Map<String, String>> weeklyRankMapList) {
        List<ContentsData> rankingContentsDataList = new ArrayList<ContentsData>();

        ContentsData rankingContentInfo = new ContentsData();

        for (int i = 0; i < weeklyRankMapList.size(); i++) {

            rankingContentInfo.setRank(String.valueOf(i + 1));
            rankingContentInfo.setThumURL(weeklyRankMapList.get(i).get(WeeklyRankJsonParser.WEEKLYRANK_LIST_THUMB));
            rankingContentInfo.setTime(weeklyRankMapList.get(i).get(WeeklyRankJsonParser.WEEKLYRANK_LIST_LINEAR_START_DATE));
            rankingContentInfo.setTitle(weeklyRankMapList.get(i).get(WeeklyRankJsonParser.WEEKLYRANK_LIST_TITLE));

            rankingContentsDataList.add(rankingContentInfo);
        }

        return rankingContentsDataList;
    }

    /**
     * コンテンツ詳細への遷移
     *
     * @param view
     */
    public void contentsDetailButton(View view) {
        startActivity(TvPlayerActivity.class, null);
    }

    /**
     * クリップボタン
     *
     * @param view
     */
    public void clipButton(View view) {
        Toast.makeText(this, "クリップしました", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    @Override
    public void onScroll(RankingBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        synchronized (this) {
            RankingBaseFragment b = getCurrentFragment();
            if (null == b || null == fragment.getRankingAdapter()) {
                return;
            }
            if (fragment != b) {
                return;
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                    ) {
                DTVTLogger.debug("Activity::onScroll, paging, firstVisibleItem=" + firstVisibleItem + ", totalItemCount=" + totalItemCount + ", visibleItemCount=" + visibleItemCount);

            }
        }
    }

    @Override
    public void onScrollStateChanged(RankingBaseFragment fragment, AbsListView absListView, int scrollState) {
        synchronized (this) {

            RankingBaseFragment b = getCurrentFragment();
            if (null == b || null == fragment.getRankingAdapter()) {
                return;
            }
            if (fragment != b) {
                return;
            }

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() == fragment.getRankingAdapter().getCount() - 1) {

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
                }, sLoadPageDelayTime);
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
     * @return
     */
    private RankingBaseFragment getCurrentFragment() {

        int i = mViewPager.getCurrentItem();
        return mRankingFragmentFactory.createFragment(i, this);
    }

    public WeeklyTvRankingActivity getWeeklyTvRankingActivity() {
        return this;
    }


    /**
     * 取得条件"総合"用コールバック
     *
     * @param weeklyRankMapList
     */
    @Override
    public void weeklyRankSynthesisCallback(List<Map<String, String>> weeklyRankMapList) {
        DTVTLogger.start("ResponseDataSize :" + weeklyRankMapList.size());
        if (mViewPager.getCurrentItem() == RankingConstants.RANKING_PAGE_NO_OF_SYNTHESIS) {
            setShowWeeklyRanking(weeklyRankMapList);
        } else {
            // nop.
        }
        DTVTLogger.end();
    }

    /**
     * 取得条件"海外映画"用コールバック
     *
     * @param weeklyRankMapList
     */
    @Override
    public void weeklyRankOverseasMovieCallback(List<Map<String, String>> weeklyRankMapList) {
        DTVTLogger.start("ResponseDataSize :" + weeklyRankMapList.size());
        if (mViewPager.getCurrentItem() == RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_MOVIE) {
            setShowWeeklyRanking(weeklyRankMapList);
        } else {
            // nop.
        }
        DTVTLogger.end();
    }

    /**
     * 取得条件"国内映画"用コールバック
     *
     * @param weeklyRankMapList
     */
    @Override
    public void weeklyRankDomesticMovieCallback(List<Map<String, String>> weeklyRankMapList) {
        DTVTLogger.start("ResponseDataSize :" + weeklyRankMapList.size());
        if (mViewPager.getCurrentItem() == RankingConstants.RANKING_PAGE_NO_OF_DOMESTIC_MOVIE) {
            setShowWeeklyRanking(weeklyRankMapList);
        } else {
            // nop.
        }
        DTVTLogger.end();
    }

    /**
     * 取得条件"海外TV番組・ドラマ"用コールバック
     *
     * @param weeklyRankMapList
     */
    public void weeklyRankOverseasChannelCallback(List<Map<String, String>> weeklyRankMapList) {
        DTVTLogger.start("ResponseDataSize :" + weeklyRankMapList.size());
        if (mViewPager.getCurrentItem() == RankingConstants.RANKING_PAGE_NO_OF_OVERSEAS_CHANNEL) {
            setShowWeeklyRanking(weeklyRankMapList);
        } else {
            // nop.
        }
        DTVTLogger.end();
    }

    /*検索結果タブ専用アダプター*/
    class RankingPagerAdapter extends FragmentStatePagerAdapter {
        public RankingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mRankingFragmentFactory.createFragment
                    (position, getWeeklyTvRankingActivity());
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
