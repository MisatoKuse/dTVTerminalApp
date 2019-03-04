/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */
package com.nttdocomo.android.tvterminalapp.activity.search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.common.DtvtConstants;
import com.nttdocomo.android.tvterminalapp.common.ErrorState;
import com.nttdocomo.android.tvterminalapp.common.JsonConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.SearchDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.stop.StopSearchDataConnect;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.search.SearchFragmentFactory;
import com.nttdocomo.android.tvterminalapp.struct.ChannelInfo;
import com.nttdocomo.android.tvterminalapp.struct.ContentsData;
import com.nttdocomo.android.tvterminalapp.struct.ResultType;
import com.nttdocomo.android.tvterminalapp.struct.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.struct.SearchSortKind;
import com.nttdocomo.android.tvterminalapp.utils.ContentUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtils;
import com.nttdocomo.android.tvterminalapp.utils.UserInfoUtils;
import com.nttdocomo.android.tvterminalapp.view.TabItemLayout;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchDubbedType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchFilterTypeMappable;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchGenreType;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchResultError;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.SearchState;
import com.nttdocomo.android.tvterminalapp.webapiclient.recommend_search.TotalSearchContentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 検索画面.
 */
public class SearchTopActivity extends BaseActivity
        implements SearchDataProvider.SearchDataProviderListener, ScaledDownProgramListDataProvider.ApiDataProviderCallback,
        View.OnClickListener, SearchBaseFragmentScrollListener, TabItemLayout.OnClickTabTextListener {

    /** 検索結果件数.*/
    private int mSearchTotalCount = 0;
    /** 検索結果ページ数.*/
    private int mPageNumber = 0;
    /** ページング中判定フラグ.*/
    private boolean mIsPaging = false;
    /** 検索中判定フラグ.*/
    private boolean mIsSearching = false;
    /** タブ名.*/
    private String[] mTabNames = null;
    /** 現在の検索文字列.*/
    private String sCurrentSearchText = "";
    /** tabのレイアウト.*/
    private TabItemLayout mTabLayout = null;
    /** ViewPager.*/
    private ViewPager mSearchViewPager = null;
    /** 検索窓.*/
    private SearchView mSearchView = null;
    /** グローバルメニューからの表示かどうか.*/
    private Boolean mIsMenuLaunch = false;
    /** 検索用データプロパイダ.*/
    private SearchDataProvider mSearchDataProvider = null;
    /** 検索条件.*/
    private SearchNarrowCondition mSearchNarrowCondition = null;
    /** 検索時のソート種別.*/
    private final SearchSortKind mSearchSortKind = new SearchSortKind(SearchSortKind.SearchSortKindEnum.SEARCH_SORT_KIND_NONE);
    /** fragment factory.*/
    private SearchFragmentFactory mFragmentFactory = null;
    /** テレビ.*/
    public static final int PAGE_NO_OF_SERVICE_TELEVISION = 0;
    /** dTVチャンネル.*/
    public static final int PAGE_NO_OF_SERVICE_DTV_CHANNEL = PAGE_NO_OF_SERVICE_TELEVISION + 3;
    /** クリア用.*/
    public static final int PAGE_NO_OF_SERVICE_CLEAR = PAGE_NO_OF_SERVICE_TELEVISION - 1;
    /** 検索インターバル.*/
    private static final long SEARCH_INTERVAL = 1000;
    /** 検索窓内の文字の大きさ.*/
    private static final int TEXT_SIZE = 14;
    /** タブインデックス　テレビ.*/
    private static final int TAB_INDEX_TV = 0;
    /** タブインデックス　ビデオ.*/
    private static final int TAB_INDEX_VIDEO = 1;
    /** タブインデックス　dTV.*/
    private static final int TAB_INDEX_DTV = 2;
    /** タブインデックス　dTVチャンネル.*/
    private static final int TAB_INDEX_DTV_CHANNEL = 3;
    /** タブインデックス　DAZN.*/
    private static final int TAB_INDEX_DAZN = 4;
    /** タブインデックス　dアニメストア.*/
    private static final int TAB_INDEX_DANIME = 5;
    /** 最後に表示したタブindex.*/
    private int mTabIndex = 0;
    /** 最後に表示したタブindex.*/
    private static final String TAB_INDEX = "tabIndex";
    /** 最後に表示した検索キーワード.*/
    private CharSequence mCharSequence = null;
    /** 最後に表示した検索キーワード.*/
    private static final String CHAR_SEQUENCE = "query";
    /** フォーカス状態.*/
    private boolean mIsFocus = false;
    /** タイマー.*/
    private Timer mTimer = null;
    /** 前回文字列.*/
    private String mBeforeText = null;
    /** アダプター.*/
    private MainAdapter mMainAdapter = null;
    /** スクロール取得中. */
    private boolean mIsLoading = false;
    /** ロード終了. */
    private boolean mIsEndPage = false;
    /** チャンネルプロバイダー.*/
    private ScaledDownProgramListDataProvider mScaledDownProgramListDataProvider = null;
    /** チャンネル一覧.*/
    private ArrayList<ChannelInfo> mChannels = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_top_main_layout);
        if (savedInstanceState != null) {
            mTabIndex = savedInstanceState.getInt(TAB_INDEX);
            mCharSequence = savedInstanceState.getCharSequence(CHAR_SEQUENCE);
            savedInstanceState.clear();
        }
        //Headerの設定
        setTitleText(getString(R.string.keyword_search_title));
        Intent intent = getIntent();
        mIsMenuLaunch = intent.getBooleanExtra(DtvtConstants.GLOBAL_MENU_LAUNCH, false);
        if (mIsMenuLaunch) {
            enableHeaderBackIcon(true);
        }
        enableGlobalMenuIcon(true);

        initData();
        initView();
        setSearchViewState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableStbStatusIcon(true);
        if (mIsFromBgFlg) {
            super.sendScreenView(getString(R.string.google_analytics_screen_name_search),
                    ContentUtils.getParingAndLoginCustomDimensions(SearchTopActivity.this));
        } else {
            if (sCurrentSearchText.isEmpty()) {
                super.sendScreenView(getString(R.string.google_analytics_screen_name_search), null);
            } else {
                sendScreenViewForPosition(mTabIndex, false);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_INDEX, mTabIndex);
        CharSequence query = mSearchView.getQuery();
        outState.putCharSequence(CHAR_SEQUENCE, query);
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
        getChannelListData();
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

            @Override
            public void onFocusChange(final View view, final boolean isFocus) {
                mIsFocus = isFocus;
                if (isFocus) {
                    DTVTLogger.debug("SearchView Focus");
                    setEditTextFocus();
                    // フォーカスが当たった時
                    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        final Handler handler = new Handler();
                        // 次回検索日時
                        long searchTime = 0;
                        // 入力文字列
                        String inputText = null;

                        @Override
                        public boolean onQueryTextSubmit(final String str) {
                            // 決定ボタンがタップされた時
                            long submit_time = System.currentTimeMillis();
                            inputText = str;
                            DTVTLogger.debug("onQueryTextSubmit");
                            if ((searchTime + SEARCH_INTERVAL) > submit_time) {
                                mTimer.cancel();
                                mTimer = null;
                            }
                            // 検索処理実行
                            initSearchedResultView();
                            mBeforeText = inputText;
                            setSearchData(inputText);
                            mSearchView.clearFocus();
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(final String searchText) {
                            // 検索フォームに文字が入力された時
                            if (mSearchDataProvider.getSearchState() == SearchState.finished) {
                                mSearchDataProvider.setSearchState(SearchState.inital);
                            }
                            setEditTextUnFocus();
                            // 空白でインクリメンタル検索を行うと、以後の検索で0件と表示され続けるため除外
                            if (searchText.trim().length() == 0 && searchText.length() > 0) {
                                return false;
                            }
                            //一時検索画面が表示される
                            initSearchedResultView();
                            if (searchText.length() > 0) {
                                inputText = searchText;
                                if (inputText.isEmpty()) {
                                    DTVTLogger.debug("isEmpty");
                                }
                                searchTime = System.currentTimeMillis();
                                // result用画面に切り替え
                                if (mTimer == null) {
                                    mTimer = new Timer();
                                    mTimer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    DTVTLogger.debug("1 second passed");
                                                    searchTime = System.currentTimeMillis();
                                                    if (!inputText.equals(mBeforeText)) {
                                                        // 文字列に変化があった場合
                                                        DTVTLogger.debug("Start IncrementalSearch:" + inputText);
                                                        initSearchedResultView();
                                                        if (!mIsFocus && mTimer != null) {
                                                            mTimer.cancel();
                                                            mTimer = null;
                                                        }
                                                        // コールバックが来る前にリクエストする場合もあるので、リクエスト前にキャンセルする
                                                        cancelDataProvider();
                                                        setSearchStart(false);
                                                        mBeforeText = inputText;
                                                        setSearchData(inputText);
                                                    } else {
                                                        // nop.
                                                        DTVTLogger.debug("Don't Start IncrementalSearch I=" + inputText + ":B=" + mBeforeText);
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
                                mSearchTotalCount = 0;
                                //前回と同一文字を入力しても検索実行するために前回キーワードを消去
                                mBeforeText = null;
                                findViewById(R.id.fl_search_result).setVisibility(View.GONE);
                                // tabViewの非表示
                                findViewById(R.id.rl_search_tab).setVisibility(View.GONE);
                                clearAllFragment();
                                mSearchViewPager = null;

                                SearchTopActivity.super.sendScreenView(getString(R.string.google_analytics_screen_name_search), null);
                            }
                            //setSearchData(s);
                            return false;
                        }
                    });
                } else {
                    // フォーカスが外れた時
                    if (mTimer != null && mSearchDataProvider.getSearchState() == SearchState.finished) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                    setEditTextUnFocus();
                    mSearchView.clearFocus();
                }
            }
        });
        mSearchView.setFocusable(false);
        //ユーザー操作の簡略化のため、遷移時キーボードを表示する
        searchAutoComplete.requestFocus();
        searchAutoComplete.setHint(R.string.keyword_search_hint);
        if (mCharSequence != null && mCharSequence.length() > 0) {
            searchAutoComplete.setText(mCharSequence.toString());
        }
        searchAutoComplete.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE);
    }

    /**
     * CH一覧情報取得.
     */
    private void getChannelListData() {
        DTVTLogger.start();
        if (mChannels != null && mChannels.size() > 0) {
            return;
        }
        if (mScaledDownProgramListDataProvider == null) {
            mScaledDownProgramListDataProvider = new ScaledDownProgramListDataProvider(SearchTopActivity.this, this);
        }
        mScaledDownProgramListDataProvider.setAreaCode(UserInfoUtils.getAreaCode(this));
        mScaledDownProgramListDataProvider.getChannelList(0, 0, "", JsonConstants.CH_SERVICE_TYPE_INDEX_ALL);
        DTVTLogger.end();
    }

    @Override
    public void channelListCallback(final ArrayList<ChannelInfo> channels) {
        //チャンネル情報を受け取る
        DTVTLogger.start();
        if (null == channels) {
            String message = mScaledDownProgramListDataProvider.getChannelError().getErrorMessage();
            showGetDataFailedToast(message);
            DTVTLogger.end();
            return;
        }
        //後で使用する為に控えておく
        mChannels = channels;
        if (channels.size() > 0) {
            SearchBaseFragment baseFragment = mFragmentFactory.createFragment(mTabIndex, this);
            if (null == baseFragment) {
                return;
            }
            if (baseFragment.getContentDataSize() > 0) {
                List<ContentsData> contentsDatas = baseFragment.getContentsData();
                for (int i = 0; i < contentsDatas.size(); i++) {
                    ContentsData contentsData = contentsDatas.get(i);
                    contentsData.setChannelName(searchChannelName(contentsData.getChannelId(), contentsData.getServiceId(), contentsData.getCategoryId()));
                }
                baseFragment.setNotifyDataSetChanged();
            }
        }
        DTVTLogger.end();
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
                baseFragment.clear(this);
                //連続検索を行うと一瞬0件と表示される対策として、前回の検索結果件数を持たせる
                String totalCountText = getResultString();
                baseFragment.notifyDataSetChanged(totalCountText, mTabIndex);
                if (!baseFragment.isProgressBarShowing()) {
                    if (!baseFragment.showProgressBar(true)) {
                        showProgress(View.VISIBLE);
                    }
                }
                setPageNumber(0);
                setPagingStatus(false);
            }
            sCurrentSearchText = searchText;
        }
        if (mPageNumber == 0) {
            mIsEndPage = false;
        }
        mSearchDataProvider.startSearchWith(sCurrentSearchText, mSearchNarrowCondition, mTabIndex, mSearchSortKind, mPageNumber, this);
    }

    /**
     * 進捗バーを表示.
     * @param visible 表示
     */
    private void showProgress(final int visible) {
        findViewById(R.id.fl_searched_progress).setVisibility(visible);
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
        mTabLayout =  initTabData(mTabLayout, mTabNames);
        // tabを表示
        findViewById(R.id.rl_search_tab).setVisibility(View.VISIBLE);
        if (mMainAdapter == null) {
            mMainAdapter = new MainAdapter(getSupportFragmentManager(), this);
            mSearchViewPager.setAdapter(mMainAdapter);
            mSearchViewPager.addOnPageChangeListener(new ViewPager
                    .SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(final int position) {
                    super.onPageSelected(position);
                    mTabLayout.setTab(position);
                    mTabIndex = position;
                }
            });
        }
        mSearchViewPager.setCurrentItem(mTabIndex);
        // 最後のタブ位置を復旧
        mTabLayout.setTab(mTabIndex);
        DTVTLogger.end();
    }

    /**
     * DataProviderキャンセル処理.
     */
    private void cancelDataProvider() {
        DTVTLogger.start();
        if (mSearchDataProvider != null) {
            mSearchDataProvider.stopConnect();
            mSearchDataProvider.setSearchDataProviderListener(null);
            //キャンセル後に mSearchDataProvider の使いまわしを防ぐため初期化する
            mSearchDataProvider = null;
            mSearchDataProvider = new SearchDataProvider();
        }
        DTVTLogger.end();
    }

    /**
     * 表示中タブの内容によってスクリーン情報を送信する.
     *
     * @param position タブ位置
     * @param isFromServerSuccess true:サーバ取得成功 false:その他
     */
    private void sendScreenViewForPosition(final int position, final boolean isFromServerSuccess) {
        String screenName = "";
        SparseArray<String> customDimensions = null;
        if (isFromServerSuccess) {
            customDimensions = new SparseArray<>();
            customDimensions.put(ContentUtils.CUSTOMDIMENSION_KEYWORD, sCurrentSearchText);
        }
        switch (position) {
            case TAB_INDEX_TV:
                if (isFromServerSuccess) {
                    customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_h4d));
                }
                screenName = getString(R.string.google_analytics_screen_name_search_result_tv);
                break;
            case TAB_INDEX_VIDEO:
                if (isFromServerSuccess) {
                    customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_h4d));
                }
                screenName = getString(R.string.google_analytics_screen_name_search_result_video);
                break;
            case TAB_INDEX_DTV:
                if (isFromServerSuccess) {
                    customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_dtv));
                }
                screenName = getString(R.string.google_analytics_screen_name_search_result_dtv);
                break;
            case TAB_INDEX_DTV_CHANNEL:
                if (isFromServerSuccess) {
                    customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_dtv_ch));
                }
                screenName = getString(R.string.google_analytics_screen_name_search_result_dtv_channel);
                break;
            case TAB_INDEX_DAZN:
                if (isFromServerSuccess) {
                    customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_dazn));
                }
                screenName = getString(R.string.google_analytics_screen_name_search_result_dazn);
                break;
            case TAB_INDEX_DANIME:
                if (isFromServerSuccess) {
                    customDimensions.put(ContentUtils.CUSTOMDIMENSION_SERVICE, getString(R.string.google_analytics_custom_dimension_service_danime));
                }
                screenName = getString(R.string.google_analytics_screen_name_search_result_danime);
                break;
            default:
                break;
        }
        if (!TextUtils.isEmpty(screenName)) {
            super.sendScreenView(screenName, customDimensions);
        }
    }

    @Override
    public void onClickTab(final int position) {
        DTVTLogger.start("position = " + position);
        mTabIndex = position;
        if (null != mSearchViewPager) {
            if (mSearchViewPager.getCurrentItem() != position) {
                DTVTLogger.debug("viewpager not null");
                SearchBaseFragment baseFragment = mFragmentFactory.createFragment(mTabIndex, this);
                baseFragment.showProgressBar(true);
                mSearchViewPager.setCurrentItem(position);
            }
        }
        DTVTLogger.end();
    }

    @Override
    public void onSearchDataProviderFinishOk(final ResultType<TotalSearchContentInfo> resultType) {
        mIsLoading = false;
        TotalSearchContentInfo content = resultType.getResultType();
        mSearchTotalCount = content.getTotalCount();

        SearchBaseFragment baseFragment = mFragmentFactory.createFragment(mTabIndex, this);
        if (null == baseFragment) {
            showProgress(View.GONE);
            return;
        }

        sendScreenViewForPosition(mTabIndex, true);

        synchronized (this) {
            if (mIsPaging) {
                baseFragment.displayLoadMore(false);
                setPagingStatus(false);
            } else {
                baseFragment.clear(this);
            }
        }
        baseFragment.setResultTextVisibility(true);
        if (0 < mSearchTotalCount) {

            baseFragment.setNoDataMessageVisibility(false);
            //画面表示用のデータセット
            for (int i = 0; i < content.getContentsDataList().size(); ++i) {
                ContentsData contentsData = content.getContentsDataList().get(i);
                contentsData.setChannelName(searchChannelName(contentsData.getChannelId(), contentsData.getServiceId(), contentsData.getCategoryId()));
                baseFragment.addContentData(contentsData);
            }
            if (content.getContentsDataList().size() < SearchConstants.Search.requestMaxResultCount) {
                mIsEndPage = true;
            }

            DTVTLogger.debug("baseFragment.mData.size = " + baseFragment.getContentDataSize());

            baseFragment.notifyDataSetChanged(getResultString(), mTabIndex);
            baseFragment.invalidateViews();
        } else {
            //表示件数0件の場合は"タブ名+検索結果:0件"を表示する
            baseFragment.notifyDataSetChanged(getResultString(), mTabIndex);
            baseFragment.setNoDataMessageVisibility(true);
            mIsEndPage = true;
        }
        baseFragment.showProgressBar(false);
        showProgress(View.GONE);
        baseFragment.displayLoadMore(false);
        setSearchStart(false);
    }

    /**
     * 指定されたIDを持つチャンネル名を見つける.
     *
     * @param channelId チャンネルID
     * @param serviceId サービスID
     * @param categoryId カテゴリID
     * @return 見つかったチャンネル名
     */
    private String searchChannelName(final String channelId, final String serviceId, final String categoryId) {
        getChannelListData();
        if (mSearchViewPager == null) {
            return "";
        }
        if (mSearchViewPager.getCurrentItem() != TAB_INDEX_TV
                && mSearchViewPager.getCurrentItem() != TAB_INDEX_DTV_CHANNEL) {
            return "";
        }
        return ContentUtils.getChannelName(channelId, serviceId, categoryId, mChannels);
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
                baseFragment.clear(this);
            }
        }
    }

    @Override
    public void onSearchDataProviderFinishNg(final ResultType<SearchResultError> resultType) {
        //エラーの場合もプログレスバーを消す
        mIsLoading = false;
        SearchBaseFragment baseFragment = mFragmentFactory.createFragment(mTabIndex, this);
        baseFragment.showProgressBar(false);
        showProgress(View.GONE);
        baseFragment.displayLoadMore(false);
        if (mPageNumber == 0 && baseFragment.getContentDataSize() == 0) {
            baseFragment.setResultTextVisibility(false);
        }
        if (mPageNumber > 0) {
            mPageNumber--;
        }
        baseFragment.notifyDataSetChanged(getResultString(), mTabIndex);
        sendScreenViewForPosition(mTabIndex, false);

        setSearchStart(false);
        showErrorMessage();
    }

    /**
     * おすすめテレビ用コールバック.
     */
    private void showErrorMessage() {
        ErrorState errorState = mSearchDataProvider.getError();
        if (errorState != null && errorState.getErrorType() != DtvtConstants.ErrorType.SUCCESS) {
            String message = errorState.getErrorMessage();
            showGetDataFailedToast(message);
        } else {
            showGetDataFailedToast();
        }
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
    public void onScrollChanged(final SearchBaseFragment fragment, final AbsListView absListView, final int scrollState) {
        if (mIsEndPage) {
            return;
        }
        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && absListView.getLastVisiblePosition() == fragment.getContentDataSize() - 1
                    && !mIsLoading) {
                setPageNumber(mPageNumber + 1);
                setPagingStatus(true);
                fragment.displayLoadMore(true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsLoading = true;
                        setSearchData(null);
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    @Override
    public void onUserVisibleHint(final boolean isVisibleToUser, final SearchBaseFragment searchBaseFragment) {
        if (!isVisibleToUser) {
            cancelDataProvider();
            //検索中フラグをクリア
            setSearchStart(false);
            clearAllFragment();
            setPagingStatus(false);
        } else {
            if (null != mSearchView) {
                setPageNumber(0);
                CharSequence searchText = mSearchView.getQuery();
                if (0 < searchText.length()) {
                    searchBaseFragment.showProgressBar(true);
                    mBeforeText = searchText.toString();
                    setSearchData(searchText.toString());
                }
            }
        }
    }

    /**
     * 検索結果タブ専用アダプター.
     */
    private class MainAdapter extends FragmentStatePagerAdapter {
        /**
         * 自身のActivity.
         */
        private SearchTopActivity mSearchTopActivity;

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
        String tabName = mTabNames[mTabIndex];
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
                if (closeDrawerMenu()) {
                    return false;
                }
                if (mIsMenuLaunch) {
                    //メニューから起動の場合ホーム画面に戻る
                    contentsDetailBackKey(null);
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
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        //検索の通信を止める
        StopSearchDataConnect stopSearchDataConnect = new StopSearchDataConnect();
        stopSearchDataConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mSearchDataProvider);

        //FragmentにContentsAdapterの通信を止めるように通知する
        SearchBaseFragment baseFragment = mFragmentFactory.createFragment(mTabIndex, this);
        if (baseFragment != null) {
            baseFragment.stopContentsAdapterCommunication();
        }
    }
}
