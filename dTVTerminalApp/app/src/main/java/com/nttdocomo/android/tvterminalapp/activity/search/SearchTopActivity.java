/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.search;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.SearchDataProvider;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchResultError;
import com.nttdocomo.android.tvterminalapp.model.ResultType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.model.search.SearchContentInfo;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchDubbedType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchFilterTypeMappable;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchGenreType;
import com.nttdocomo.android.tvterminalapp.model.search.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.model.search.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.model.search.SearchSortKind;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchContentInfo;
import com.nttdocomo.android.tvterminalapp.fragment.search.FragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragmentScrollListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchTopActivity extends BaseActivity implements SearchDataProvider.SearchDataProviderListener, View.OnClickListener, SearchBaseFragmentScrollListener {

    private static final int PAGE_NO_OF_SERVICE_TEREBI = 0;                                         //テレビ
    private static final int PAGE_NO_OF_SERVICE_VIDEO = PAGE_NO_OF_SERVICE_TEREBI + 1;         //ビデオ
    private static final int PAGE_NO_OF_SERVICE_DTV_CHANNEL = PAGE_NO_OF_SERVICE_TEREBI + 2;  //dTVチャンネル
    private static final int PAGE_NO_OF_SERVICE_DTV = PAGE_NO_OF_SERVICE_TEREBI + 3;            //dTV
    private static final int PAGE_NO_OF_SERVICE_DANIME = PAGE_NO_OF_SERVICE_TEREBI + 4;        //dアニメ

    public final static int sSearchDisplayCountOnce = 20;
    private final static long SEARCH_INTERVAL = 1000;
    public final static String sSearchCountDefault = "検索結果:0件";

    private SearchView mSearchView;
    private final static int TEXT_SIZE = 15;
    private String[] mTabNames;
    private LinearLayout mLinearLayout;
    private HorizontalScrollView mTabScrollView;
    private ViewPager mSearchViewPager = null;

    SearchNarrowCondition mSearchNarrowCondition = null;
    SearchSortKind mSearchSortKind = new SearchSortKind("SearchSortKindNone");

    private ImageView mMenuImageView = null;
    private int mPageNumber = 0;
    private boolean mIsSearching = false;
    private FragmentFactory mFragmentFactory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_top_main_layout);
        setTitleText(getString(R.string.keyword_search_title));

        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);

        initData();
        initView();
        setSearchViewState();
    }

    private void initData() {
        mTabNames = getResources().getStringArray(R.array.tab_names);
        setSearchNarrowCondition();

        mFragmentFactory = new FragmentFactory();
        mSearchDataProvider = new SearchDataProvider();
    }

    /*検索トップ画面初期化*/
    private void initView() {
        mSearchView = findViewById(R.id.keyword_search_form);
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
    }

    /*検索フォームの設定*/
    private void setSearchViewState() {
        mSearchView.setIconifiedByDefault(false);
        SearchView.SearchAutoComplete searchAutoComplete
                = findViewById(android.support.v7.appcompat.R.id.search_src_text);

        //mSearchView.set

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            Timer mTimer = null;

            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    DTVTLogger.debug("SearchView Focus");
                    // フォーカスが当たった時
                    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                        private Handler mHandler = new Handler();
                        // 次回検索日時
                        long mSearchTime = 0;
                        // 前回文字列
                        private String mBeforeText = null;
                        // 入力文字列
                        private String mInputText = null;

                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            // 決定ボタンがタップされた時
                            long submit_time = System.currentTimeMillis();
                            mInputText = s;
                            DTVTLogger.debug("onQueryTextSubmit");
                            if ((mSearchTime + SEARCH_INTERVAL) > submit_time) {
                                mTimer.cancel();
                                mTimer = null;
                            }
                            // 検索処理実行
                            initSearchedResultView();
                            setSearchData(mInputText);
                            mSearchView.clearFocus();
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            // 検索フォームに文字が入力された時
                            // 空白でインクリメンタル検索を行うと、以後の検索で0件と表示され続けるため除外
                            if (s.trim().length() == 0 && s.length() > 0) {
                                return false;
                            }
                            //一時検索画面が表示される
                            initSearchedResultView();
                            if (s.length() > 0) {
                                mInputText = s;
                                if (mInputText.isEmpty()) {
                                    DTVTLogger.debug("isEmpty");
                                }
                                mSearchTime = System.currentTimeMillis();
                                // result用画面に切り替え
                                if (mTimer == null) {
                                    mTimer = new Timer();
                                    mTimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    DTVTLogger.debug("1 second passed");
                                                    mSearchTime = System.currentTimeMillis();
                                                    if (!mInputText.equals(mBeforeText)) {
                                                        // 文字列に変化があった場合
                                                        DTVTLogger.debug("Start IncrementalSearch:" + mInputText);
                                                        initSearchedResultView();
                                                        setSearchData(mInputText);
                                                        mBeforeText = mInputText;
                                                    } else {
                                                        // nop.
                                                        DTVTLogger.debug("Don't Start IncrementalSearch I=" + mInputText + ":B=" + mBeforeText);
                                                    }
                                                }
                                            });
                                        }
                                    }, SEARCH_INTERVAL, SEARCH_INTERVAL);
                                    mSearchView.setSubmitButtonEnabled(true);
                                    //setSearchData(s);
                                } else {
                                    // nop.
                                }
                            } else {
                                if (mTimer != null) {
                                    mTimer.cancel();
                                    mTimer = null;
                                }
                                mSearchView.setSubmitButtonEnabled(false);
                                //検索文字が1文字以上から0文字になった場合、Tabを非表示にする
                                mSearchViewPager = null;
                                findViewById(R.id.fl_search_result).setVisibility(View.GONE);
                                clearAllFragment();
                            }
                            //setSearchData(s);
                            return false;
                        }
                    });
                } else {
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                    // フォーカスが外れた時
                    mSearchView.clearFocus();
                }
            }
        });
        mSearchView.setFocusable(false);
        searchAutoComplete.setTextColor(ContextCompat.getColor(this, R.color.keyword_search_text));
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.keyword_search_hint));
        searchAutoComplete.setHint(R.string.keyword_search_hint);
        searchAutoComplete.setTextSize(TEXT_SIZE);
    }

    private void setPageNumber(int pageNumber) {
        mPageNumber = pageNumber;
        mPageNumber = (mPageNumber < 0 ? 0 : mPageNumber);
    }

    private ArrayList<SearchServiceType> getCurrentSearchServiceTypeArray() {

        ArrayList<SearchServiceType> ret = new ArrayList<>();
        if (null == mSearchViewPager) {
            return ret;
        }
        int pageIndex = mSearchViewPager.getCurrentItem();

        switch (pageIndex) {
            case PAGE_NO_OF_SERVICE_TEREBI: //テレビ
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.hikariTVForDocomo));
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTVChannel));
                break;
            case PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
                //ret.add(new SearchServiceType(SearchServiceType.ServiceId.hikariTVForDocomo));
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTV));
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dAnime));
                break;
            case PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTVChannel));
                break;
            case PAGE_NO_OF_SERVICE_DTV: //dTV
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTV));
                break;
            case PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dAnime));
                break;
            default:
                break;
        }

        return ret;
    }

    private void setSearchNarrowCondition() {
        if (null == mSearchNarrowCondition) {

            ArrayList<SearchFilterTypeMappable> arr = new ArrayList<>();

            SearchGenreType genreType = new SearchGenreType("SearchGenreTypeActive_001");      //映画
            SearchDubbedType dubbedType = new SearchDubbedType("SearchDubbedTypeBoth");         //字幕
            //SearchChargeType
            //SearchOtherType

            arr.add(genreType);
            arr.add(dubbedType);
            mSearchNarrowCondition = new SearchNarrowCondition(arr);
        }
    }

    private void setSearchStart(boolean b) {
        synchronized (this) {
            mIsSearching = b;
        }
    }

    private static String mCurrentSearchText = "";
    SearchDataProvider mSearchDataProvider = null;

    private void setSearchData(String searchText) {
        if (null == mSearchDataProvider) {
            DTVTLogger.debug("SearchTopActivity::setSearchData, mSearchDataProvider is null");
            return;
        }

        synchronized (this) {
            if (!mIsSearching) {
                setSearchStart(true);
            } else {
                return;
            }
        }
        if (null != searchText) {
            /*
            if(false ==mCurrentSearchText.equals(searchText)){
                SearchBaseFragment b=getCurrentSearchBaseFragment();
                if(null!=b){
                    b.clear();
                    setPageNumber(0);
                    setPagingStatus(false);
                }
            }
            */
            SearchBaseFragment b = getCurrentSearchBaseFragment();
            if (null != b) {
                b.clear();
                setPageNumber(0);
                setPagingStatus(false);
            }
            mCurrentSearchText = searchText;
        }

        if (mCurrentSearchText.length() == 0) {
            return;
        }

        //SearchDataProvider dp=new SearchDataProvider();
        ArrayList<SearchServiceType> serviceTypeArray = getCurrentSearchServiceTypeArray();
        mSearchDataProvider.startSearchWith(
                mCurrentSearchText,
                serviceTypeArray,
                mSearchNarrowCondition,
                mSearchSortKind,
                mPageNumber,
                this
        );
        /* //for test
        try {
            Thread.sleep(1000);
            dp.cancelSearch();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }

    private void initSearchedResultView() {
        if (null != mSearchViewPager) {
            return;
        }

        findViewById(R.id.fl_search_result).setVisibility(View.VISIBLE);


        mSearchViewPager = findViewById(R.id.vp_search_result);
        mTabScrollView = findViewById(R.id.hs_tab_strip_scroll);
        initTabVIew();

        mSearchViewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), this));
        mSearchViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
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

    /*tabに関連Viewの初期化*/
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
                    if (null != mSearchViewPager) {
                        int position = (int) view.getTag();
                        mSearchViewPager.setCurrentItem(position);
                        setTab(position);
                    }
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

    private int mSearchTotalCount = 0;

    private SearchBaseFragment getCurrentSearchBaseFragment() {
        if (null != mSearchViewPager) {
            int currentPageNo = mSearchViewPager.getCurrentItem();
            return mFragmentFactory.createFragment(currentPageNo, this);
        }
        return null;
    }

    @Override
    public void onSearchDataProviderFinishOk(ResultType<TotalSearchContentInfo> resultType) {
        TotalSearchContentInfo content = resultType.getResultType();

        mSearchTotalCount = content.totalCount;

        String totalCountText = sSearchCountDefault;

        SearchBaseFragment baseFragment = getCurrentSearchBaseFragment();//(SearchBaseFragment)mFragmentFactory.createFragment(currentPageNo, this);
        if (null == baseFragment) {
            return;
        }

        synchronized (this) {
            if (mIsPaging) {
                baseFragment.displayLoadMore(false);
                setPagingStatus(false);
            } else {
                //if(0==mPageNumber) {
                baseFragment.clear();
                //}
            }
        }

        if (0 < mSearchTotalCount) {

            totalCountText = "検索結果:" + mSearchTotalCount + "件";

            int thisTimeTotal = content.searchContentInfo.size();
            for (int i = 0; i < thisTimeTotal; ++i) {
                SearchContentInfo ci = content.searchContentInfo.get(i);

                SearchContentInfo searchContentInfo = new SearchContentInfo(false, ci.contentId, ci.serviceId, ci.contentPictureUrl, ci.title);
                baseFragment.mData.add(searchContentInfo);
            }
            DTVTLogger.debug("baseFragment.mData.size = " + baseFragment.mData.size());

            baseFragment.notifyDataSetChanged(totalCountText);
            baseFragment.setSelection(mSearchLastItem);
        }

        baseFragment.displayLoadMore(false);

        setSearchStart(false);
    }

    public void clearAllFragment() {

        if (null != mSearchViewPager) {
            int sum = mFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                SearchBaseFragment baseFragment = mFragmentFactory.createFragment(i, this);
                baseFragment.clear();
            }
        }
    }

    @Override
    public void onSearchDataProviderFinishNg(ResultType<SearchResultError> resultType) {
        clearAllFragment();
        DTVTLogger.debug("onSearchDataProviderFinishNg");
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

    private static final int sLoadPageDelayTime = 500;

    @Override
    public void onScroll(SearchBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        mSearchLastItem = firstVisibleItem + visibleItemCount - 1;

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
            }, sLoadPageDelayTime);
        }
    }


    /*検索結果タブ専用アダプター*/
    private class MainAdapter extends FragmentStatePagerAdapter {

        private SearchTopActivity mSearchTopActivity = null;

        MainAdapter(FragmentManager fm, SearchTopActivity top) {
            super(fm);
            mSearchTopActivity = top;
        }

        @Override
        public Fragment getItem(int position) {
            synchronized (this) {
                return mFragmentFactory.createFragment(position, mSearchTopActivity);
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
}


