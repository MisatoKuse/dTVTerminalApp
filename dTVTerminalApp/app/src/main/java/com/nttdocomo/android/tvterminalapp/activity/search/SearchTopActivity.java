/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.SearchDataProvider;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchResultError;
import com.nttdocomo.android.tvterminalapp.struct.ResultType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchDubbedType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchFilterTypeMappable;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchGenreType;
import com.nttdocomo.android.tvterminalapp.struct.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.struct.SearchSortKind;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchContentInfo;
import com.nttdocomo.android.tvterminalapp.fragment.search.FragmentFactory;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragmentScrollListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchTopActivity extends BaseActivity
        implements SearchDataProvider.SearchDataProviderListener,
        View.OnClickListener, SearchBaseFragmentScrollListener {

    private int mSearchTotalCount = 0;
    private int mPageNumber = 0;
    private int mSearchLastItem = 0;
    private boolean mIsPaging = false;
    private boolean mIsSearching = false;
    private String[] mTabNames = null;
    private static String sCurrentSearchText = "";

    public final static String sSearchCountDefault = "検索結果:0件";

    private LinearLayout mLinearLayout = null;
    private HorizontalScrollView mTabScrollView = null;
    private ViewPager mSearchViewPager = null;
    private SearchView mSearchView = null;
    private Boolean mIsMenuLaunch = false;

    SearchDataProvider mSearchDataProvider = null;
    SearchNarrowCondition mSearchNarrowCondition = null;
    SearchSortKind mSearchSortKind = new SearchSortKind("SearchSortKindNone");

    private FragmentFactory mFragmentFactory = null;

    //テレビ
    private static final int PAGE_NO_OF_SERVICE_TELEVISION = 0;
    //ビデオ
    private static final int PAGE_NO_OF_SERVICE_VIDEO = PAGE_NO_OF_SERVICE_TELEVISION + 1;
    //dTVチャンネル
    private static final int PAGE_NO_OF_SERVICE_DTV_CHANNEL = PAGE_NO_OF_SERVICE_TELEVISION + 2;
    //dTV
    private static final int PAGE_NO_OF_SERVICE_DTV = PAGE_NO_OF_SERVICE_TELEVISION + 3;
    //dアニメ
    private static final int PAGE_NO_OF_SERVICE_DANIME = PAGE_NO_OF_SERVICE_TELEVISION + 4;

    private static final long SEARCH_INTERVAL = 1000;

    private static final int TEXT_SIZE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_top_main_layout);

        //Headerの設定
        setTitleText(getString(R.string.keyword_search_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DTVTConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(false);
        }
        enableStbStatusIcon(true);
        enableGlobalMenuIcon(true);

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

    /**
     * 検索トップ画面初期化
     */
    private void initView() {
        mSearchView = findViewById(R.id.keyword_search_form);
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
    }

    /**
     * 検索フォームの設定
     */
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
                        public boolean onQueryTextChange(String seachText) {
                            // 検索フォームに文字が入力された時
                            // 空白でインクリメンタル検索を行うと、以後の検索で0件と表示され続けるため除外
                            if (seachText.trim().length() == 0 && seachText.length() > 0) {
                                return false;
                            }
                            //一時検索画面が表示される
                            initSearchedResultView();
                            if (seachText.length() > 0) {
                                mInputText = seachText;
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

    private void setSearchStart(boolean searchingFlag) {
        synchronized (this) {
            mIsSearching = searchingFlag;
        }
    }

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
            SearchBaseFragment baseFragment = getCurrentSearchBaseFragment();
            if (null != baseFragment) {
                baseFragment.clear();
                //連続検索を行うと一瞬0件と表示される対策として、前回の検索結果件数を持たせる
                String totalCountText = getResultString();
                baseFragment.notifyDataSetChanged(totalCountText);
                setPageNumber(0);
                setPagingStatus(false);
            }
            sCurrentSearchText = searchText;
        }

        if (sCurrentSearchText.length() == 0) {
            return;
        }

        //現在のページ番号を取得
        int pageIndex = mSearchViewPager.getCurrentItem();
        //SearchDataProvider dp=new SearchDataProvider();
        mSearchDataProvider.startSearchWith(
                sCurrentSearchText,
                mSearchNarrowCondition,
                pageIndex,
                mSearchSortKind,
                mPageNumber,
                this
        );
        /* //for test
        try {
            Thread.sleep(1000);
            dp.cancelSearch();
        } catch (InterruptedException e) {
            DTVTLogger.debug(e);
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

    /**
     * tabに関連Viewの初期化
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

    /**
     * インジケーター設置
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

        baseFragment.setResultTextVisibility(true);
        if (0 < mSearchTotalCount) {

            //画面表示用のデータセット
            for (int i = 0; i < content.getContentsDataList().size(); ++i) {
                baseFragment.mData.add(content.getContentsDataList().get(i));
            }

            DTVTLogger.debug("baseFragment.mData.size = " + baseFragment.mData.size());

            baseFragment.notifyDataSetChanged(getResultString());
            baseFragment.setSelection(mSearchLastItem);
        } else {
            //表示件数0件の場合は"タブ名+検索結果:0件"を表示する
            baseFragment.notifyDataSetChanged(getResultString());
        }
        baseFragment.displayLoadMore(false);
        setSearchStart(false);
    }

    public void clearAllFragment() {
        if (null != mSearchViewPager) {
            int sum = mFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                SearchBaseFragment baseFragment = mFragmentFactory.createFragment(i, this);
                baseFragment.setResultTextVisibility(false);
                baseFragment.clear();
            }
        }
    }

    @Override
    public void onSearchDataProviderFinishNg(ResultType<SearchResultError> resultType) {
        clearAllFragment();
        DTVTLogger.debug("onSearchDataProviderFinishNg");
    }

    private void setPagingStatus(boolean pagingFlag) {
        synchronized (this) {
            mIsPaging = pagingFlag;
        }
    }

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
            }, LOAD_PAGE_DELAY_TIME);
        }
    }

    /**
     * 検索結果タブ専用アダプター
     */
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

    /**
     * 検索結果件数を表示する文字列を返す
     * @return 検索結果件数の文字列
     */
    private String getResultString() {
        int pageIndex = mSearchViewPager.getCurrentItem();
        String[] tabNames = getResources().getStringArray(R.array.tab_names);
        String tabName = tabNames[pageIndex];
        String[] strings = {tabName,
                getString(R.string.keyword_search_result_no),
                getString(R.string.keyword_search_result),
                Integer.toString(mSearchTotalCount),
                getString(R.string.keyword_search_result_num)};
        return StringUtil.getConnectString(strings);
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
