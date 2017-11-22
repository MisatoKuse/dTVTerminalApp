/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFrgament;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedFragmentFactory;
import com.nttdocomo.android.tvterminalapp.model.search.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.ArrayList;
import java.util.List;

public class RecordedListActivity extends BaseActivity implements View.OnClickListener, RecordedBaseFragmentScrollListener {

    private LinearLayout mTabLinearLayout;
    private String[] mTabNames;
    private ImageView mMenuImageView;
    private ViewPager mViewPager;
    private SearchView mSearchView;
    private int mPageNumber = 0;
    private int mSearchLastItem = 0;
    private int mSearchTotalCount = 0;
    private static final int mLoadPageDelayTime = 500;
    private boolean mIsPaging = false;
    private boolean mIsSearching = false;
    private HorizontalScrollView mTabScrollView;
    private RecordedFragmentFactory mRecordedFragmentFactory = null;
    private ContentsAdapter mContentsAdapter;

    private static final int RECORDED_MODE_NO_OF_ALL = 0;                                        // すべて
    private static final int RECORDED_MODE_NO_OF_TAKE_OUT = RECORDED_MODE_NO_OF_ALL + 1;    // 持ち出し

    // TODO この辺りはタブUI共通の値であれば、styleに定義しましょう。 コード上で動的にstyle適用させるにはコツがいるようですが。
    //設定するマージンのピクセル数
    private static final int LEFT_MARGIN = 30;
    private static final int ZERO_MARGIN = 0;
    private static final int TAB_NAME_DENSITY = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list_main_layout);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.nav_menu_item_recorder_program));
        mTabNames = getResources().getStringArray(R.array.record_list_tab_names);
        mRecordedFragmentFactory = new RecordedFragmentFactory();
        mSearchView = findViewById(R.id.record_list_main_layout_searchview);
        // TODO DPクラスをここでnewする

        initView();
        setSearchViewState();
    }

    /**
     * TODO 検索バーの内部処理は別タスクの為、まだ実装しない
     */
    private void setSearchViewState() {
    }

    /**
     * ページ数を変数に格納
     *
     * @param pageNumber
     */
    private void setPageNumber(int pageNumber) {
        mPageNumber = pageNumber;
        mPageNumber = (mPageNumber < 0 ? 0 : mPageNumber);
    }

    /**
     * タブの種別に沿ってListを変更
     *
     * @return
     */
    private ArrayList<SearchServiceType> getTypeArray() {
        // TODO タイプclass検討する
        ArrayList<SearchServiceType> ret = new ArrayList<SearchServiceType>();
        if (null == mViewPager) {
            return ret;
        }
        int pageIndex = mViewPager.getCurrentItem();
        switch (pageIndex) {
            case RECORDED_MODE_NO_OF_ALL: // すべて
//                ret.add(new SearchServiceType(SearchServiceType.ServiceId.hikariTVForDocomo));
//                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTVChannel));
                break;
            case RECORDED_MODE_NO_OF_TAKE_OUT: // 持ち出し
//                ret.add(new SearchServiceType(SearchServiceType.ServiceId.hikariTVForDocomo));
//                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTV));
//                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dAnime));
                break;
            default:
                break;
        }
        return ret;
    }

    /**
     * TODO 検索バーの内部処理は別タスクの為、まだ実装しない
     *
     * @param searchText
     */
    private void setSearchData(String searchText) {
    }

    /**
     * 画面描画
     */
    private void initView() {
        if (null != mViewPager) {
            return;
        }
        mViewPager = findViewById(R.id.record_list_main_layout_viewpagger);
        mTabScrollView = findViewById(R.id.record_list_main_layout_scroll);
        initTabVIew();

        mViewPager.setAdapter(new RecordedListActivity.MainAdpater(getSupportFragmentManager(), this));
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);
                setTab(position);

                clearAllFragment();
                setPagingStatus(false);

                if (null != mSearchView) {
                    setPageNumber(0);
                    CharSequence searchText = mSearchView.getQuery();
                    if (0 < searchText.length()) {
                        setSearchData(searchText.toString());
                    }
                }
            }
        });
    }

    /**
     * 機能
     * タブの設定
     */
    private void initTabVIew() {
        mTabScrollView.removeAllViews();
        mTabLinearLayout = new LinearLayout(this);
        // TODO このあたりもアプリ共通であれば、処理で算出しなくてよい部分だけでもstyleで定義しましょう。画面ごとに同じ処理があったり最悪バラバラな設定になるのはNG
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        mTabLinearLayout.setLayoutParams(layoutParams);
        mTabLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTabLinearLayout.setBackgroundColor(Color.BLACK);
        mTabLinearLayout.setGravity(Gravity.CENTER);
        mTabScrollView.addView(mTabLinearLayout);

        for (int i = 0; i < mTabNames.length; i++) {
            // TODO この辺りも極力style化すべきかと。
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            if (i != 0) {
                params.setMargins(
                        getResources().getDimensionPixelSize(R.dimen.list_tab_margin_left),
                        getResources().getDimensionPixelSize(R.dimen.list_tab_margin_top),
                        getResources().getDimensionPixelSize(R.dimen.list_tab_margin_right),
                        getResources().getDimensionPixelSize(R.dimen.list_tab_margin_bottom));
            } else {
                params.setMargins(
                        getResources().getDimensionPixelSize(R.dimen.list_tab_margin_left),
                        getResources().getDimensionPixelSize(R.dimen.list_tab_margin_top),
                        getResources().getDimensionPixelSize(R.dimen.list_tab_margin_right),
                        getResources().getDimensionPixelSize(R.dimen.list_tab_margin_bottom));
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTag(i);
            tabTextView.setBackgroundResource(0);
            tabTextView.setTextColor(ContextCompat.getColor(this, R.color.white_text));

            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.indicating);
                tabTextView.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
            }

            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    mViewPager.setCurrentItem(position);
                    setTab(position);
                }
            });
            mTabLinearLayout.addView(tabTextView);
        }
    }

    /**
     * 機能
     * タブを切り替え
     *
     * @param position タブインデックス
     */
    private void setTab(int position) {
        if (mTabLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) mTabLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.rectangele);
                    mTextView.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
                } else {
                    mTextView.setBackgroundResource(0);
                    mTextView.setTextColor(ContextCompat.getColor(this, R.color.white_text));
                }
            }
        }
    }

    private RecordedBaseFrgament getCurrentRecordedBaseFrgament() {
        int currentPageNo = mViewPager.getCurrentItem();
        RecordedBaseFrgament baseFragment = (RecordedBaseFrgament) mRecordedFragmentFactory.createFragment(currentPageNo, this);
        return baseFragment;
    }

    /**
     * フラグメントをクリア
     */
    public void clearAllFragment() {
        if (null != mViewPager) {
            int sum = mRecordedFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                RecordedBaseFrgament baseFragment = mRecordedFragmentFactory.createFragment(i, this);
                baseFragment.clear();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
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

    @Override
    public void onScroll(RecordedBaseFrgament fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // TODO loading処理
        mSearchLastItem = firstVisibleItem + visibleItemCount - 1;

        // TODO Constantsクラス検討
        int pageMax = (mPageNumber + 1) * SearchConstants.Search.requestMaxResultCount;
        int maxPage = mSearchTotalCount / SearchConstants.Search.requestMaxResultCount;
        if (firstVisibleItem + visibleItemCount >= pageMax && maxPage >= 1 + mPageNumber) {
            setPageNumber(mPageNumber + 1);
            DTVTLogger.debug("page no=" + (mPageNumber + 1));
            setPagingStatus(true);
            fragment.displayLoadMore(true);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setSearchData(null);
                }
            }, mLoadPageDelayTime);
        }
    }

    /**
     * 検索結果タブ専用アダプター
     */
    private class MainAdpater extends FragmentStatePagerAdapter {

        private RecordedListActivity mRecordedListActivity = null;

        MainAdpater(FragmentManager fm, RecordedListActivity top) {
            super(fm);
            mRecordedListActivity = top;
        }

        @Override
        public Fragment getItem(int position) {
            synchronized (this) {
                return mRecordedFragmentFactory.createFragment(position, mRecordedListActivity);
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        View currentView = getCurrentFocus();
        if (currentView != null && currentView instanceof SearchView) {
        } else {
            //検索ボックス以外タッチならキーボードを消す
            mSearchView.clearFocus();
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 年齢制限にフィルターをかける
     *
     * @param listData
     */
    private void setTab(List listData) {
        // TODO 年齢制限があるコンテンツをフィルター処理
    }

}