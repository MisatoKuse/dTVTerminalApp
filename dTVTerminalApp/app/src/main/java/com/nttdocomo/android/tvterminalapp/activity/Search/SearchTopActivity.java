package com.nttdocomo.android.tvterminalapp.activity.Search;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.common.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.DCommon;
import com.nttdocomo.android.tvterminalapp.DataProvider.SearchDataProvider;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.SearchResultError;
import com.nttdocomo.android.tvterminalapp.Model.ResultType;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.SearchConstants;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchContentInfo;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.SearchDubbedType;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.SearchFilterTypeMappable;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.SearchGenreType;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchNarrowCondition;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchServiceType;
import com.nttdocomo.android.tvterminalapp.Model.Search.SearchSortKind;
import com.nttdocomo.android.tvterminalapp.WebApiClient.Recommend.Search.TotalSearchContentInfo;
import com.nttdocomo.android.tvterminalapp.Fragment.Search.FragmentFactory;
import com.nttdocomo.android.tvterminalapp.Fragment.Search.SearchBaseFragment;
import com.nttdocomo.android.tvterminalapp.Fragment.Search.SearchBaseFragmentScrollListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchTopActivity extends BaseActivity implements SearchDataProvider.SearchDataProviderListener, View.OnClickListener, SearchBaseFragmentScrollListener {

    private static final int PAGE_NO_OF_SERVICE_TEREBI =0;                                         //テレビ
    private static final int PAGE_NO_OF_SERVICE_VIDEO = PAGE_NO_OF_SERVICE_TEREBI + 1;         //ビデオ
    private static final int PAGE_NO_OF_SERVICE_DTV_CHANNEL = PAGE_NO_OF_SERVICE_TEREBI + 2;  //dTVチャンネル
    private static final int PAGE_NO_OF_SERVICE_DTV =PAGE_NO_OF_SERVICE_TEREBI + 3;            //dTV
    private static final int PAGE_NO_OF_SERVICE_DANIME =PAGE_NO_OF_SERVICE_TEREBI + 4;        //dアニメ

    public final static int sSearchDisplayCountOnce = 20;
    private final static long SEARCH_INTERVAL = 1000;
    public final static String sSearchCountDefault ="検索結果:0件";

    private SearchView mSearchView;
    private final static int TEXT_SIZE = 14;
    private String[] mTabNames;
    private LinearLayout mLinearLayout;
    private HorizontalScrollView mTabScrollView;
    private ViewPager mSearchViewPager=null;
    private TextView mSearchCount;//,mTvDetail;
    //private ListView mSearchData;
    //private boolean mIsSearchViewPager=
    SearchNarrowCondition mSearchNarrowCondition=null;
    SearchSortKind mSearchSortKind= new SearchSortKind("SearchSortKindNone");

    private TextView mMenuTextView=null;
    private int mPageNumber=0;

    //private SearchBaseFragment mSearchBaseFragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_top_main_layout);
        setTitleText(getString(R.string.keyword_search_hint));

        mMenuTextView= findViewById(R.id.header_layout_menu);
        mMenuTextView.setVisibility(View.VISIBLE);
        mMenuTextView.setOnClickListener(this);

        initData();
        initView();
        setSearchViewState();
    }

    private void initData() {
        mTabNames = getResources().getStringArray(R.array.tab_names);
        setSearchNarrowCondition();
    }

    /*検索トップ画面初期化*/
    private void initView() {
        mSearchView = findViewById(R.id.keyword_search_form);
    }

    private static String sKeywordText="";

    /*検索フォームの設定*/
    private void setSearchViewState() {
        mSearchView.setIconifiedByDefault(false);
        SearchView.SearchAutoComplete searchAutoComplete
                = findViewById(android.support.v7.appcompat.R.id.search_src_text);

        //mSearchView.set

        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (isFocus) {
                    // フォーカスが当たった時
                    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                        Timer mTimer = null;
                        long mSearchTime = 0;
                        private Handler mHandler = new Handler();

                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            long submit_time = System.currentTimeMillis();
                            Log.i("SearchTopActivity","onQueryTextSubmit");
                            if((mSearchTime+SEARCH_INTERVAL) > submit_time) {
                                mTimer.cancel();
                            }
                            // 検索処理実行
                            initSearchedResultView();
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            sKeywordText=s;
                            //一時検索画面が表示される
                            initSearchedResultView();

                            if (s.length() > 0) {
                                // result用画面に切り替え

                                if (mTimer == null) {
                                    mTimer = new Timer();
                                } else {
                                    mTimer.cancel();
                                    mTimer = null;
                                    mTimer = new Timer();
                                }
                                mSearchTime = System.currentTimeMillis();
                                mTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.d(DCommon.LOG_DEF_TAG, "1秒経過");
                                                initSearchedResultView();
                                                setSearchData(sKeywordText);
                                            }
                                        });
                                    }
                                },SEARCH_INTERVAL);

                                mSearchView.setSubmitButtonEnabled(true);

                                //setSearchData(s);
                            } else {
                                mSearchView.setSubmitButtonEnabled(false);
                                clearAllFragment();
                            }

                            //setSearchData(s);

                            return false;
                        }

                    });
                } else {
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

    private void setPageNumber(int pageNumber){
        mPageNumber = pageNumber;
        mPageNumber = (mPageNumber <0? 0: mPageNumber);
    }

    private ArrayList<SearchServiceType> getCurrentSearchServiceTypeArray(){

        ArrayList<SearchServiceType> ret=new ArrayList<SearchServiceType>();
        if(null==mSearchViewPager){
            return ret;
        }
        int pageIndex= mSearchViewPager.getCurrentItem();

        switch (pageIndex){
            case PAGE_NO_OF_SERVICE_TEREBI: //テレビ
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.hikariTVForDocomo));
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTVChannel));
                break;
            case PAGE_NO_OF_SERVICE_VIDEO: //ビデオ
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.hikariTVForDocomo));
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTV));
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dAnime));
                break;
            case PAGE_NO_OF_SERVICE_DTV_CHANNEL: //dTVチャンネル
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTVChannel));
                break;
            case PAGE_NO_OF_SERVICE_DTV: //dTV
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTV));
            case PAGE_NO_OF_SERVICE_DANIME: //dアニメ
                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dAnime));
        }

        return ret;
    }

    private void setSearchNarrowCondition(){
        if(null==mSearchNarrowCondition){

            ArrayList<SearchFilterTypeMappable> arr=new ArrayList<SearchFilterTypeMappable>();

            SearchGenreType genreType = new SearchGenreType("SearchGenreTypeActive_001");      //映画
            SearchDubbedType dubbedType = new SearchDubbedType("SearchDubbedTypeBoth");         //字幕
            //SearchChargeType
            //SearchOtherType

            arr.add(genreType);
            arr.add(dubbedType);
            mSearchNarrowCondition=new SearchNarrowCondition(arr);
        }
    }

    private static String mCurrentSearchText="";
    private void setSearchData(String searchText) {
        if(null!=searchText){
            mCurrentSearchText = searchText;
        }
        if(mCurrentSearchText.length()==0){
            return;
        }

        SearchDataProvider dp=new SearchDataProvider();

        ArrayList<SearchServiceType> serviceTypeArray=getCurrentSearchServiceTypeArray();
        dp.startSearchWith(
                mCurrentSearchText,
                serviceTypeArray,
                mSearchNarrowCondition,
                mSearchSortKind,
                mPageNumber,
                this
        );
    }

    private void initSearchedResultView() {
        if(null!=mSearchViewPager){
            return;
        }

        findViewById(R.id.search_history).setVisibility(View.GONE);
        findViewById(R.id.fl_search_result).setVisibility(View.VISIBLE);


        mSearchViewPager = findViewById(R.id.vp_search_result);
        mTabScrollView = findViewById(R.id.hs_tab_strip_scroll);
        initTabVIew();

        mSearchViewPager.setAdapter(new MainAdpater(getSupportFragmentManager(), this));
        mSearchViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);

                clearAllFragment();

                if(null!=mSearchView){
                    setPageNumber(0);
                    CharSequence searchText= mSearchView.getQuery();
                    if(0 < searchText.length()){
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
                    int position = (int) view.getTag();
                    mSearchViewPager.setCurrentItem(position);
                    setTab(position);
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

    @Override
    public void onSearchDataProviderFinishOk(ResultType<TotalSearchContentInfo> resultType) {
        TotalSearchContentInfo content= resultType.getResultType();

        mSearchCount = findViewById(R.id.tv_searched_result);
        int totalCount = content.totalCount;

        String totalCountText=sSearchCountDefault;

        int currentPageNo = mSearchViewPager.getCurrentItem();
        SearchBaseFragment baseFragment = (SearchBaseFragment)FragmentFactory.createFragment(currentPageNo, this);

        if(0<totalCount){

            totalCountText="検索結果:"+ totalCount +"件";

            int thisTimeTotal = content.searchContentInfo.size();
            //int sum= (thisTimeTotal<sSearchDisplayCountOnce? thisTimeTotal: sSearchDisplayCountOnce);
            //baseFragment.mData.clear();
            for(int i=0; i<thisTimeTotal; ++i){
                SearchContentInfo ci = content.searchContentInfo.get(i);

                SearchContentInfo searchContentInfo=new SearchContentInfo(false, ci.contentId, ci. serviceId, ci.contentPictureUrl, ci.title);
                baseFragment.mData.add(searchContentInfo);
            }
            Log.d(DCommon.LOG_DEF_TAG, "baseFragment.mData.size = " + baseFragment.mData.size());

            baseFragment.refresh(totalCountText);
            //baseFragment.pagingFinish();
            return;
        }
        baseFragment.refresh(totalCountText);
    }

    public void clearAllFragment() {

        if(null!=mSearchViewPager){
            int sum=FragmentFactory.getFragmentCount();
            for(int i=0;i<sum;++i){
                SearchBaseFragment baseFragment = FragmentFactory.createFragment(i, this);
                baseFragment.clear();
            }
        }
    }

    @Override
    public void onSearchDataProviderFinishNg(ResultType<SearchResultError> resultType) {

    }

    @Override
    public void onClick(View view) {
        if(view ==mMenuTextView){
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    @Override
    public void onScrollStateChanged(SearchBaseFragment fragment, AbsListView absListView, int scrollState) {
    }

    @Override
    public void onScroll(SearchBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int pageMin = mPageNumber* SearchConstants.Search.requestMaxResultCount;
        int pageMax = (mPageNumber+ 1)* SearchConstants.Search.requestMaxResultCount;
        if(firstVisibleItem<=pageMin && 0!=firstVisibleItem){

        }else if(firstVisibleItem + visibleItemCount>=pageMax){
            setPageNumber(mPageNumber + 1);
            setSearchData(null);
        }
    }


    /*検索結果タブ専用アダプター*/
    private class MainAdpater extends FragmentStatePagerAdapter {

        private SearchTopActivity mSearchTopActivity = null;
        MainAdpater(FragmentManager fm, SearchTopActivity top) {
            super(fm);
            mSearchTopActivity=top;
        }

        @Override
        public Fragment getItem(int position) {
            synchronized (this) {
                return FragmentFactory.createFragment(position, mSearchTopActivity);
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
}


