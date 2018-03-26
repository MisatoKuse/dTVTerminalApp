package com.nttdocomo.android.tvterminalapp.activity.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.SearchDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopSearchDataConnect;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchFragmentFactory;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.struct.ResultType;
import com.nttdocomo.android.tvterminalapp.struct.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.struct.SearchSortKind;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchDubbedType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchFilterTypeMappable;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchGenreType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchResultError;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchContentInfo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 検索画面.
 */
public class SearchTopActivity extends BaseActivity
        implements SearchDataProvider.SearchDataProviderListener,
        View.OnClickListener, SearchBaseFragmentScrollListener, TabItemLayout.OnClickTabTextListener {

    /**
     * 検索結果件数.
     */
    private int mSearchTotalCount = 0;
    /**
     * 検索結果ページ数.
     */
    private int mPageNumber = 0;
    /**
     * ページング中判定フラグ.
     */
    private boolean mIsPaging = false;
    /**
     * 検索中判定フラグ.
     */
    private boolean mIsSearching = false;
    /**
     * タブ名.
     */
    private String[] mTabNames = null;
    /**
     * 現在の検索文字列.
     */
    private String sCurrentSearchText = "";
    /**
     * tabのレイアウト.
     */
    private TabItemLayout mTabLayout = null;
    /**
     * ViewPager.
     */
    private ViewPager mSearchViewPager = null;
    /**
     * 検索窓.
     */
    private SearchView mSearchView = null;
    /**
     * グローバルメニューからの表示かどうか.
     */
    private Boolean mIsMenuLaunch = false;
    /**
     * スクロールによるデータ取得中フラグ.
     */
    private Boolean mIsScroll = false;

    /**
     * 検索用データプロパイダ.
     */
    private SearchDataProvider mSearchDataProvider = null;
    /**
     * 検索条件.
     */
    private SearchNarrowCondition mSearchNarrowCondition = null;
    /**
     * 検索時のソート種別.
     */
    private SearchSortKind mSearchSortKind = new SearchSortKind(SearchSortKind.SearchSortKindEnum.SEARCH_SORT_KIND_NONE);
    /**
     * fragment factory.
     */
    private SearchFragmentFactory mFragmentFactory = null;

    /**
     * テレビ.
     */
    public static final int PAGE_NO_OF_SERVICE_TELEVISION = 0;
    /**
     * ビデオ.
     */
    private static final int PAGE_NO_OF_SERVICE_VIDEO = PAGE_NO_OF_SERVICE_TELEVISION + 1;
    /**
     * dTVチャンネル.
     */
    private static final int PAGE_NO_OF_SERVICE_DTV_CHANNEL = PAGE_NO_OF_SERVICE_TELEVISION + 2;
    /**
     * dTV.
     */
    private static final int PAGE_NO_OF_SERVICE_DTV = PAGE_NO_OF_SERVICE_TELEVISION + 3;
    /**
     * dアニメ.
     */
    private static final int PAGE_NO_OF_SERVICE_DANIME = PAGE_NO_OF_SERVICE_TELEVISION + 4;
    /**
     * クリア用.
     */
    public static final int PAGE_NO_OF_SERVICE_CLEAR = PAGE_NO_OF_SERVICE_TELEVISION - 1;
    /**
     * 検索インターバル.
     */
    private static final long SEARCH_INTERVAL = 1000;
    /**
     * 検索窓内の文字の大きさ.
     */
    private static final int TEXT_SIZE = 14;
    /**
     * 最後に表示したタブindex.
     */
    public static int mTabIndex = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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
        setStatusBarColor(true);

        initData();
        initView();
        setSearchViewState();
    }

    /**
     * 初期化処理.
     */
    private void initData() {
        mTabNames = getResources().getStringArray(R.array.tab_names);
        setSearchNarrowCondition();

        mFragmentFactory = new SearchFragmentFactory();
        mSearchDataProvider = new SearchDataProvider();
    }

    /**
     * 検索トップ画面初期化.
     */
    private void initView() {
        mSearchView = findViewById(R.id.keyword_search_form);
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);
    }

    /**
     * 検索フォームの設定.
     */
    private void setSearchViewState() {
        mSearchView.setIconifiedByDefault(false);
        SearchView.SearchAutoComplete searchAutoComplete
                = findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //mSearchView.set
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            Timer mTimer = null;

            @Override
            public void onFocusChange(final View view, final boolean isFocus) {
                if (isFocus) {
                    DTVTLogger.debug("SearchView Focus");
                    setEditTextFocus();
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
                        public boolean onQueryTextSubmit(final String str) {
                            // 決定ボタンがタップされた時
                            long submit_time = System.currentTimeMillis();
                            mInputText = str;
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
                        public boolean onQueryTextChange(final String searchText) {
                            // 検索フォームに文字が入力された時
                            setEditTextUnFocus();
                            // 空白でインクリメンタル検索を行うと、以後の検索で0件と表示され続けるため除外
                            if (searchText.trim().length() == 0 && searchText.length() > 0) {
                                return false;
                            }
                            //一時検索画面が表示される
                            initSearchedResultView();
                            if (searchText.length() > 0) {
                                mInputText = searchText;
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
                                }
                            } else {
                                if (mTimer != null) {
                                    mTimer.cancel();
                                    mTimer = null;
                                }
                                mSearchView.setSubmitButtonEnabled(false);
                                //検索文字が1文字以上から0文字になった場合、Tabを非表示にする
                                mSearchViewPager = null;
                                //前回と同一文字を入力しても検索実行するために前回キーワードを消去
                                mBeforeText = null;
                                findViewById(R.id.fl_search_result).setVisibility(View.GONE);
                                // tabViewの非表示
                                findViewById(R.id.rl_search_tab).setVisibility(View.GONE);
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
                    setEditTextUnFocus();
                    mSearchView.clearFocus();
                }
            }
        });
        mSearchView.setFocusable(false);
        setEditTextUnFocus();
        searchAutoComplete.setHint(R.string.keyword_search_hint);
        searchAutoComplete.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE);
    }

    /**
     * 入力エリア非Focus設定.
     */
    private void setEditTextUnFocus() {
        SearchView.SearchAutoComplete searchAutoComplete
                = findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ImageView searchIcon = findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        CharSequence query = mSearchView.getQuery();
        DTVTLogger.debug("" + searchAutoComplete.isFocused());
        // 入力文字があれば白背景基調、またはテキスト入力にFocusがあれば白背景基調
        if ((query != null && query.length() > 0) || searchAutoComplete.isFocused()) {
            searchIcon.setImageResource(R.mipmap.icon_graylight_search);
            mSearchView.setBackgroundColor(ContextCompat.getColor(this, R.color.keyword_search_background_focus));
            searchAutoComplete.setBackgroundColor(ContextCompat.getColor(this, R.color.keyword_search_background_focus));
            searchAutoComplete.setTextColor(ContextCompat.getColor(this, R.color.keyword_search_text_focus));
            searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.keyword_search_text_focus));
        } else {
            // 入力文字がなければグレー背景基調
            searchIcon.setImageResource(R.mipmap.icon_normal_search);
            mSearchView.setBackgroundColor(ContextCompat.getColor(this, R.color.keyword_search_background_unfocus));
            searchAutoComplete.setBackgroundColor(ContextCompat.getColor(this, R.color.keyword_search_background_unfocus));
            searchAutoComplete.setTextColor(ContextCompat.getColor(this, R.color.keyword_search_text_unfocus));
            searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.keyword_search_text_unfocus));
        }
    }

    /**
     * 入力エリアFocus設定.
     */
    private void setEditTextFocus() {
        SearchView.SearchAutoComplete searchAutoComplete
                = findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ImageView searchIcon = findViewById(android.support.v7.appcompat.R.id.search_mag_icon);

        // Focus時は固定で白背景基調
        searchIcon.setImageResource(R.mipmap.icon_graylight_search);
        mSearchView.setBackgroundColor(ContextCompat.getColor(this, R.color.keyword_search_background_focus));
        searchAutoComplete.setBackgroundColor(ContextCompat.getColor(this, R.color.keyword_search_background_focus));
        searchAutoComplete.setTextColor(ContextCompat.getColor(this, R.color.keyword_search_text_focus));
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.keyword_search_text_focus));
    }

    /**
     * ページ数を設定.
     *
     * @param pageNumber ページ数
     */
    private void setPageNumber(final int pageNumber) {
        mPageNumber = pageNumber;
        mPageNumber = (mPageNumber < 0 ? 0 : mPageNumber);
    }

    /**
     * 検索フィルタを設定.
     */
    private void setSearchNarrowCondition() {
        if (null == mSearchNarrowCondition) {
            ArrayList<SearchFilterTypeMappable> arr = new ArrayList<>();
            SearchGenreType genreType = new SearchGenreType("SearchGenreTypeActive_001");   //映画
            SearchDubbedType dubbedType = new SearchDubbedType("SearchDubbedTypeBoth");     //字幕
            //SearchChargeType
            //SearchOtherType
            arr.add(genreType);
            arr.add(dubbedType);
            mSearchNarrowCondition = new SearchNarrowCondition(arr);
        }
    }

    /**
     * 検索中フラグの設定.
     *
     * @param searchingFlag 検索中フラグ
     */
    private void setSearchStart(final boolean searchingFlag) {
        mIsSearching = searchingFlag;
    }

    /**
     * 検索データを設定.
     *
     * @param searchText 検索文字列
     */
    private void setSearchData(final String searchText) {
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
        if (!TextUtils.isEmpty(searchText)) {
            SearchBaseFragment baseFragment = mFragmentFactory.createFragment(mTabIndex, this);
            if (null != baseFragment) {
                baseFragment.clear();
                //連続検索を行うと一瞬0件と表示される対策として、前回の検索結果件数を持たせる
                String totalCountText = getResultString();
                baseFragment.notifyDataSetChanged(totalCountText, mTabIndex);
                baseFragment.showProgressBar(true);
                setPageNumber(0);
                setPagingStatus(false);
            }
            sCurrentSearchText = searchText;
        }

        mSearchDataProvider.startSearchWith(
                sCurrentSearchText,
                mSearchNarrowCondition,
                mTabIndex,
                mSearchSortKind,
                mPageNumber,
                this
        );
    }

    /**
     * 検索結果表示Viewを初期化.
     */
    private void initSearchedResultView() {
        DTVTLogger.start();
        if (null != mSearchViewPager) {
            DTVTLogger.debug("mSearchViewPager is allready create");
            mSearchViewPager.setCurrentItem(mTabIndex);
            return;
        }
        findViewById(R.id.fl_search_result).setVisibility(View.VISIBLE);

        mSearchViewPager = findViewById(R.id.vp_search_result);
        mSearchViewPager.setCurrentItem(mTabIndex);
        initTabView();
        // tabを表示
        findViewById(R.id.rl_search_tab).setVisibility(View.VISIBLE);

        mSearchViewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), this));
        mSearchViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(final int position) {
                super.onPageSelected(position);
                mTabLayout.setTab(position);
                mTabIndex = position;

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

        DTVTLogger.end();
    }

    /**
     * tab関連Viewの初期化.
     */
    private void initTabView() {
        DTVTLogger.start();
        if (mTabLayout == null) {
            mTabLayout = new TabItemLayout(this);
            mTabLayout.setTabClickListener(this);
            mTabLayout.initTabView(mTabNames, TabItemLayout.ActivityType.SEARCH_ACTIVITY);
            RelativeLayout tabRelativeLayout = findViewById(R.id.rl_search_tab);
            tabRelativeLayout.addView(mTabLayout);
        } else {
            mTabLayout.resetTabView(mTabNames);
        }
        // 最後のタブ位置を復旧
        mTabLayout.setTab(mTabIndex);
        DTVTLogger.end();
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        if (null != mSearchViewPager) {
            DTVTLogger.debug("viewpager not null");
            mSearchViewPager.setCurrentItem(position);
        }
        mTabIndex = position;
        DTVTLogger.end();
    }

    @Override
    public void onSearchDataProviderFinishOk(final ResultType<TotalSearchContentInfo> resultType) {
        TotalSearchContentInfo content = resultType.getResultType();
        mSearchTotalCount = content.totalCount;
        mIsScroll = false;

        SearchBaseFragment baseFragment = mFragmentFactory.createFragment(mTabIndex, this);
        if (null == baseFragment) {
            return;
        }

        synchronized (this) {
            if (mIsPaging) {
                baseFragment.displayLoadMore(false);
                setPagingStatus(false);
            } else {
                baseFragment.clear();
            }
        }
        baseFragment.setResultTextVisibility(true);
        if (0 < mSearchTotalCount) {

            baseFragment.setNoDataMessageVisibility(false);
            //画面表示用のデータセット
            for (int i = 0; i < content.getContentsDataList().size(); ++i) {
                baseFragment.mData.add(content.getContentsDataList().get(i));
            }

            DTVTLogger.debug("baseFragment.mData.size = " + baseFragment.mData.size());

            baseFragment.notifyDataSetChanged(getResultString(), mTabIndex);
            baseFragment.invalidateViews();
        } else {
            //表示件数0件の場合は"タブ名+検索結果:0件"を表示する
            baseFragment.notifyDataSetChanged(getResultString(), mTabIndex);
            baseFragment.setNoDataMessageVisibility(true);
        }
        baseFragment.showProgressBar(false);
        baseFragment.displayLoadMore(false);
        setSearchStart(false);
    }

    /**
     * 全fragmentを消去する.
     */
    private void clearAllFragment() {
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
    public void onSearchDataProviderFinishNg(final ResultType<SearchResultError> resultType) {
        if (mIsScroll) {
            //TODO ページング中のデータ取得に失敗した場合のエラー処理
            DTVTLogger.debug("error while paging");
        } else {
            clearAllFragment();
            DTVTLogger.debug("onSearchDataProviderFinishNg");
        }
        mIsScroll = false;
        setSearchStart(false);
    }

    /**
     * ページング状態を設定する.
     *
     * @param pagingFlag ページングフラグ
     */
    private void setPagingStatus(final boolean pagingFlag) {
        DTVTLogger.start("" + pagingFlag);
        synchronized (this) {
            mIsPaging = pagingFlag;
        }
        DTVTLogger.end();
    }

    @Override
    public void onScroll(final SearchBaseFragment fragment, final AbsListView absListView,
                         final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        int pageMax = (mPageNumber + 1) * SearchConstants.Search.requestMaxResultCount;
        int maxPage = mSearchTotalCount / SearchConstants.Search.requestMaxResultCount;
        mIsScroll = true;
        if (firstVisibleItem + visibleItemCount >= pageMax && maxPage >= 1 + mPageNumber) {
            setPageNumber(mPageNumber + 1);
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
     * 検索結果タブ専用アダプター.
     */
    private class MainAdapter extends FragmentStatePagerAdapter {
        /**
         * 自身のActivity.
         */
        private SearchTopActivity mSearchTopActivity = null;

        /**
         * コンストラクタ.
         * @param fm FragmentManager
         * @param top SearchTopActivity
         */
        MainAdapter(final FragmentManager fm, final SearchTopActivity top) {
            super(fm);
            mSearchTopActivity = top;
        }

        @Override
        public Fragment getItem(final int position) {
            synchronized (this) {
                return mFragmentFactory.createFragment(position, mSearchTopActivity);
            }
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

    @Override
    public boolean dispatchTouchEvent(final MotionEvent event) {
        View currentView = getCurrentFocus();
        if (currentView == null || !(currentView instanceof SearchView)) {
            //検索ボックス以外タッチならキーボードを消す
            mSearchView.clearFocus();
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 検索結果件数を表示する文字列を返す.
     *
     * @return 検索結果件数の文字列
     */
    private String getResultString() {
        String[] tabNames = getResources().getStringArray(R.array.tab_names);
        String tabName = tabNames[mTabIndex];
        String[] strings = {tabName,
                getString(R.string.keyword_search_result_no),
                getString(R.string.keyword_search_result),
                Integer.toString(mSearchTotalCount),
                getString(R.string.keyword_search_result_num)};
        return StringUtils.getConnectString(strings);
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

    @Override
    public void onStartCommunication() {
        super.onStartCommunication();
        DTVTLogger.start();
        if (mIsSearching) {
            //検索中にバックグラウンドに遷移した場合はページ数を補正する
            setPageNumber(mPageNumber - 1);
            setSearchStart(false);
        }
        if (mSearchDataProvider != null) {
            mSearchDataProvider.enableConnect();
        }
        SearchBaseFragment baseFragment = mFragmentFactory.createFragment(mTabIndex, this);
        if (baseFragment != null) {
            baseFragment.enableContentsAdapterCommunication();
            baseFragment.displayLoadMore(false);
            baseFragment.invalidateViews();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        DTVTLogger.start();

        //検索の通信を止める
        StopSearchDataConnect stopSearchDataConnect = new StopSearchDataConnect();
        stopSearchDataConnect.execute(mSearchDataProvider);

        //FragmentにContentsAdapterの通信を止めるように通知する
        SearchBaseFragment baseFragment = mFragmentFactory.createFragment(mTabIndex, this);
        if (baseFragment != null) {
            baseFragment.stopContentsAdapterCommunication();
        }
    }
}
