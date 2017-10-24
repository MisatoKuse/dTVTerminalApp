
package com.nttdocomo.android.tvterminalapp.activity.home;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.RecommendDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.recommend.RecommendFragmentFactory;
import com.nttdocomo.android.tvterminalapp.model.recommend.RecommendContentInfo;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;

import java.util.List;

public class RecommendActivity extends BaseActivity implements View.OnClickListener,
        RecommendBaseFragmentScrollListener, RecommendDataProvider.RecommendApiDataProviderCallback {


    private String[] mTabNames;
    private LinearLayout mLinearLayout;
    private HorizontalScrollView mTabScrollView;
    private ViewPager mRecommendViewPager = null;


    private ImageView mMenuImageView = null;
    private boolean mIsSearching = false;

    RecommendDataProvider mRecommendDataProvider = null;

    private static final int mLoadPageDelayTime = 500;

    private int mRequestService = 99;
    // レコメンドコンテンツ最大件数（システム制約）
    private int maxShowListSize = 100;
    // 表示中レコメンドコンテンツ件数
    private int showListSize = 0;
    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_main_layout);
        setTitleText(getString(R.string.recommend_list_title));

        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);


        initData();
        initView();
        initRecommendListView();
        requestRecommendData();
    }

    /**
     * おすすめ番組・ビデオ画面初期化
     */
    private void initView() {

    }

    private void initData() {
        mTabNames = getResources().getStringArray(R.array.recommend_list_tab_names);

        mRecommendDataProvider = new RecommendDataProvider(this);

    }

    private void setSearchStart(boolean b) {
        synchronized (this) {
            mIsSearching = b;
        }
    }

    /**
     * データプロバイダへデータ取得要求
     */
    private void requestRecommendData() {
        if (null == mRecommendDataProvider) {
            Log.e(DTVTConstants.LOG_DEF_TAG, "RecommendActivity::setRecommendData, mRecommendDataProvider is null");
            return;
        }

        synchronized (this) {
            if (!mIsSearching) {
                setSearchStart(true);
            } else {
                return;
            }
        }
//        RecommendBaseFragment b = getCurrentRecommendBaseFragment();
//        if (null != b) {
//
//            b.clear();
//            setPagingStatus(false);
//        }

        if (null == mRecommendViewPager) {
            return;
        }

        int requestService = mRecommendViewPager.getCurrentItem();
        int startIndex = showListSize + 1;
        showListSize += SearchConstants.RecommendList.requestMaxCount_Recommend;
        mRecommendDataProvider.startGetRecommendData(
                requestService, startIndex, SearchConstants.RecommendList.requestMaxCount_Recommend);
    }

    private void initRecommendListView() {
        if (null != mRecommendViewPager) {
            return;
        }
        mRecommendViewPager = findViewById(R.id.vp_recommend_list_items);
        mTabScrollView = findViewById(R.id.recommend_tab_strip_scroll);
        initTabVIew();

        mRecommendViewPager.setAdapter(new RecommendActivity.TabAdpater(getSupportFragmentManager(), this));
        mRecommendViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            // タブ切り替え時
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);
                clearAllFragment();
                setPagingStatus(false);
                showListSize = 0;
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
                    int position = (int) view.getTag();
                    mRecommendViewPager.setCurrentItem(position);
                    setTab(position);
                    requestRecommendData();
                }
            });
            mLinearLayout.addView(tabTextView);
        }
    }

    /*インジケーター設置*/
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

    private RecommendBaseFragment getCurrentRecommendBaseFragment() {
        int currentPageNo = mRecommendViewPager.getCurrentItem();
        RecommendBaseFragment baseFragment = RecommendFragmentFactory.createFragment(currentPageNo, this);
        return baseFragment;
    }


    public void recommendDataProviderSuccess(List<RecommendContentInfo> resultInfoList) {
        ;

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
            for (RecommendContentInfo info : resultInfoList) {
                baseFragment.mData.add(info);
                showListSize += 1;
            }

            Log.d(DTVTConstants.LOG_DEF_TAG, "baseFragment.mData.size = " + baseFragment.mData.size());

            // フラグメントの更新


//             RecommendBaseFragment baseFragment = getCurrentRecommendBaseFragment();
            baseFragment.notifyDataSetChanged();
            baseFragment.setSelection(mSearchLastItem);
            baseFragment.displayLoadMore(false);

        }
        setSearchStart(false);
    }

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
        clearAllFragment();
        Log.e(DTVTConstants.LOG_DEF_TAG, "onSearchDataProviderFinishNg");
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    private int mSearchLastItem = 0;
    private boolean mIsPaging = false;

    private void setPagingStatus(boolean b) {
        synchronized (this) {
            mIsPaging = b;
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

//        int pageMax = (mPageNumber+ 1)* SearchConstants.RecommendList.requestMaxCount_Recommend;
//        int maxPage = mSearchTotalCount/SearchConstants.RecommendList.requestMaxCount_Recommend;
//        if(firstVisibleItem + visibleItemCount>=pageMax && maxPage >=1+ mPageNumber ){
        Log.i(DTVTConstants.LOG_DEF_TAG, "onScroll.first:" + firstVisibleItem + " .visible:" + visibleItemCount + " .total:" + totalItemCount + " dataSize:" + fragment.mData.size());
        if (maxShowListSize > fragment.mData.size() &&
                fragment.mData.size() % SearchConstants.RecommendList.requestMaxCount_Recommend == 0 &&
                firstVisibleItem + visibleItemCount >= fragment.mData.size()) {
            setPagingStatus(true);
            fragment.displayLoadMore(true);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestRecommendData();
                }
            }, mLoadPageDelayTime);
        }
    }

    /**
     * おすすめテレビ用コールバック
     *
     * @param recommendContentInfoList
     */
    @Override
    public void RecommendChannelCallback(List<RecommendContentInfo> recommendContentInfoList) {
        Log.d(DTVTConstants.LOG_DEF_TAG, "Chan Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
        if (mRecommendViewPager.getCurrentItem() == SearchConstants.RecommendTabPageNo.RECOMMEND_PAGE_NO_OF_SERVICE_TELEBI) {
            recommendDataProviderSuccess(recommendContentInfoList);
        }
    }

    /**
     * おすすめビデオ用コールバック
     *
     * @param recommendContentInfoList
     */
    @Override
    public void RecommendVideoCallback(List<RecommendContentInfo> recommendContentInfoList) {
        Log.d(DTVTConstants.LOG_DEF_TAG, "vid Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
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
    public void RecommendDTVCallback(List<RecommendContentInfo> recommendContentInfoList) {
        Log.d(DTVTConstants.LOG_DEF_TAG, "dtv Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
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
    public void RecommendDAnimeCallback(List<RecommendContentInfo> recommendContentInfoList) {
        Log.d(DTVTConstants.LOG_DEF_TAG, "ani Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
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
    public void RecommendDChannelCallback(List<RecommendContentInfo> recommendContentInfoList) {
        Log.d(DTVTConstants.LOG_DEF_TAG, "dCH Callback DataSize:" + recommendContentInfoList.size() + "ViewPager.getCurrentItem:" + mRecommendViewPager.getCurrentItem());
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
}

