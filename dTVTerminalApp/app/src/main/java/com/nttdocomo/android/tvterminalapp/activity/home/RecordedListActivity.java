/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.RecordedContentsDetailData;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;
import com.nttdocomo.android.tvterminalapp.utils.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class RecordedListActivity extends BaseActivity implements View.OnClickListener,
        RecordedBaseFragmentScrollListener, DlnaRecVideoListener {
    private LinearLayout mTabLinearLayout;
    private String[] mTabNames;
    private ImageView mMenuImageView;
    private ViewPager mViewPager;
    private SearchView mSearchView;
    private HorizontalScrollView mTabScrollView;
    private DlnaProvRecVideo mDlnaProvRecVideo;
    private RecordedFragmentFactory mRecordedFragmentFactory = null;

    private final static long SEARCH_INTERVAL = 1000;

    private final static int TEXT_SIZE = 15;

    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private static final int MARGIN_ZERO = 0;
    private static final int MARGIN_LEFT_TAB = 5;
    private static final int MARGIN_LEFT_NOT_INDEX = 15;

    // タブTYPE：すべて
    private static final int RECORDED_MODE_NO_OF_ALL = 0;
    // タブTYPE：持ち出し
    private static final int RECORDED_MODE_NO_OF_TAKE_OUT = 1;

    public static final String RECORD_LIST_KEY = "recordListKey";

    //設定するマージンのピクセル数
    private static final String DATE_FORMAT = "yyyy/MM/ddHH:mm:ss";
    private String date[] = {"日", "月", "火", "水", "木", "金", "土"};
    public ArrayList<DlnaRecVideoItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.record_list_main_layout);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.nav_menu_item_recorder_program));

        initView();
        getData();
        initTabVIew();
        setPagerAdapter();
        setSearchViewState();
        DTVTLogger.end();
    }

    /**
     * TODO 検索バーの内部処理はSprint7では実装しない
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
//                            initSearchedResultView();
                            setSearchData(mInputText);
                            mSearchView.clearFocus();
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            // 検索フォームに文字が入力された時
                            //一時検索画面が表示される
//                            initSearchedResultView();
                            if (s.length() > 0) {
                                mInputText = s;
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
                                                    DTVTLogger.debug("1 sencond passed");
                                                    mSearchTime = System.currentTimeMillis();
                                                    if (mInputText != mBeforeText) {
                                                        // 文字列に変化があった場合
                                                        DTVTLogger.debug("Start IncrementalSearch:" + mInputText);
//                                                        initSearchedResultView();
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

    /**
     * 検索処理実行
     * @param searchText
     */
    private void setSearchData(String searchText) {
        // TODO 検索バーの内部処理はSprint7では実装しない
    }

    /**
     * 画面描画
     */
    private void initView() {
        DTVTLogger.start();
//        if (null != mViewPager) {
//            return;
//        }
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getStbStatus()) {
                    getRemoteControllerView().startRemoteUI();
                }
            }
        });

        mViewPager = findViewById(R.id.record_list_main_layout_viewpagger);
        mTabScrollView = findViewById(R.id.record_list_main_layout_scroll);
        mTabNames = getResources().getStringArray(R.array.record_list_tab_names);
        mRecordedFragmentFactory = new RecordedFragmentFactory();
        mSearchView = findViewById(R.id.record_list_main_layout_searchview);
    }

    /**
     * タブの指定が持ち出しリストの時、持ち出しリストの生成を行う
     */
    private void setPagerAdapter() {
        DTVTLogger.start();
        mViewPager.setAdapter(new RecordedListActivity.MainAdpater(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);

                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        setRecordedAllContents();
                        break;
                    case 1:
                        //setRecordedTakeOutContents();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 機能
     * タブの設定
     */
    private void initTabVIew() {
        DTVTLogger.start();
        int screenWidth = getWidthDensity();
        mTabScrollView.removeAllViews();
        mTabLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                screenWidth / SCREEN_TIME_WIDTH_PERCENT + (int) getDensity() * MARGIN_LEFT_TAB);
        mTabLinearLayout.setLayoutParams(layoutParams);
        mTabLinearLayout.setBackgroundResource(R.drawable.rectangele_all);
        mTabScrollView.addView(mTabLinearLayout);
        for (int i = 0; i < mTabNames.length; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if (i != 0) {
                params.setMargins((int) getDensity() * MARGIN_LEFT_NOT_INDEX, MARGIN_ZERO, MARGIN_ZERO, MARGIN_ZERO);
            } else {
                params.setMargins((int) getResources().getDimension(R.dimen.recorded_searchview_left_margin), MARGIN_ZERO, MARGIN_ZERO, MARGIN_ZERO);
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setTag(i);
            tabTextView.setBackgroundResource(0);
            tabTextView.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.rectangele);
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
        DTVTLogger.start();
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

    private void setRecordedAllContents() {
        DTVTLogger.start();
        // NOP
    }

    /**
     * 持ち出しリスト生成
     */
    private void setRecordedTakeOutContents() {
        DTVTLogger.start();
        RecordedBaseFragment baseFragment = getCurrentRecordedBaseFragment();
        List<ContentsData> curInfo = baseFragment.getContentsData();
        List<ContentsData> list = baseFragment.getContentsData();
        if (null != list) {
            list.clear();
            ContentsData contentsData = new ContentsData();
            list.add(contentsData);
            baseFragment.notifyDataSetChanged();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDlnaProvRecVideo != null) {
            mDlnaProvRecVideo.stopListen();
        }
    }

    /**
     * DMSデバイスを取り始める
     */
    private void getData() {
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (mDlnaProvRecVideo == null) {
            mDlnaProvRecVideo = new DlnaProvRecVideo();
        }
        if (mDlnaProvRecVideo.start(dlnaDmsItem.mUdn, this)) {
            mDlnaProvRecVideo.browseRecVideoDms(dlnaDmsItem.mControlUrl);
        }
    }

    /**
     * フラグメント生成
     */
    private RecordedBaseFragment getCurrentRecordedBaseFragment() {
        DTVTLogger.start();
        int currentPageNo = mViewPager.getCurrentItem();
        RecordedBaseFragment baseFragment = mRecordedFragmentFactory.createFragment(currentPageNo, this);
        return baseFragment;
    }

    /**
     * フラグメントをクリア
     */
    public void clearAllFragment() {
        DTVTLogger.start();
        if (null != mViewPager) {
            int sum = mRecordedFragmentFactory.getFragmentCount();
            for (int i = 0; i < sum; ++i) {
                RecordedBaseFragment baseFragment = mRecordedFragmentFactory.createFragment(i, this);
                baseFragment.clear();
            }
        }
    }

    @Override
    public void onClick(View view) {
        DTVTLogger.start();
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    @Override
    public void onScroll(RecordedBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        DTVTLogger.start();
    }

    @Override
    public void onVideoBrows(DlnaRecVideoInfo curInfo) {
        if (curInfo != null && curInfo.getRecordVideoLists() != null) {
            final RecordedBaseFragment baseFrgament = getCurrentRecordedBaseFragment();
            baseFrgament.mContentsList = new ArrayList<RecordedContentsDetailData>();
            for (int i = 0; i < curInfo.getRecordVideoLists().size(); ++i) {
                RecordedContentsDetailData detailData = new RecordedContentsDetailData();
                detailData.setUpnpIcon(curInfo.getRecordVideoLists().get(i).mUpnpIcon);
                detailData.setSize(curInfo.getRecordVideoLists().get(i).mSize);
                detailData.setResUrl(curInfo.getRecordVideoLists().get(i).mResUrl);
                detailData.setResolution(curInfo.getRecordVideoLists().get(i).mResolution);
                detailData.setBitrate(curInfo.getRecordVideoLists().get(i).mBitrate);
                detailData.setDuration(curInfo.getRecordVideoLists().get(i).mDuration);
                baseFrgament.mContentsList.add(detailData);
            }
            List<ContentsData> listData = baseFrgament.getContentsData();
            listData.clear();
            mList = getTakeOutContents(curInfo.getRecordVideoLists());
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);
            for (int i = 0; i < mList.size(); i++) {
                // TODO 年齢取得未実装の為、固定値を返却
                boolean isAge = true;
                DlnaRecVideoItem dlnaRecVideoItem = mList.get(i);
                ContentsData contentsData = new ContentsData();
                int currentPageNo = mViewPager.getCurrentItem();
                if (isAge) {
                    contentsData.setTitle(dlnaRecVideoItem.mTitle);
                    contentsData.setAllowedUse(dlnaRecVideoItem.mAllowedUse);
                    String time = dlnaRecVideoItem.mDate.replaceAll("-", "/").replace("T", "");
                    try {
                        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
                        calendar.setTime(sdf.parse(time));
                        StringUtil util = new StringUtil(this);
                        String[] strings = {String.valueOf(calendar.get(Calendar.MONTH)),"/",
                                String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))," (",
                                date[calendar.get(Calendar.DAY_OF_WEEK) - 1],") ",
                                String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),":",
                                String.valueOf(calendar.get(Calendar.MINUTE))};
                        String selectDate = util.getConnectString(strings);
                        contentsData.setTime(selectDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    listData.add(contentsData);
                } else {
                    // NOP
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    baseFrgament.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onDeviceLeave(DlnaDMSInfo curInfo, String leaveDmsUdn) {
        DTVTLogger.start();
    }

    @Override
    public void onError(String msg) {
        DTVTLogger.start(msg);
    }

    @Override
    public String getCurrentDmsUdn() {
        DTVTLogger.start();
        return null;
    }

    /**
     * 検索結果タブ専用アダプター
     */
    private class MainAdpater extends FragmentStatePagerAdapter {

        MainAdpater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            synchronized (this) {
                DTVTLogger.start();
                return mRecordedFragmentFactory.createFragment(position, RecordedListActivity.this);
            }
        }

        @Override
        public int getCount() {
            DTVTLogger.start();
            return mTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            DTVTLogger.start();
            return mTabNames[position];
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        DTVTLogger.start();
        View currentView = getCurrentFocus();
        if (currentView != null && currentView instanceof SearchView) {
        } else {
            //検索ボックス以外タッチならキーボードを消す
            mSearchView.clearFocus();
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * [すべて]のListと[持ち出し]Listをマージ
     *
     * @return list　生成した[すべて]一覧に表示するリスト
     */
    private ArrayList<DlnaRecVideoItem> getTakeOutContents(ArrayList<DlnaRecVideoItem> list) {
        // 持ち出しLIST
        ArrayList<DlnaRecVideoItem> takeoutList = new ArrayList<>();
        // 返却するリスト
        ArrayList<DlnaRecVideoItem> allList = list;
        if (allList != null && takeoutList != null) {
            for (int i = 0; i < takeoutList.size(); i++) {
                int getPosition = -1;
                boolean isExist = false;
                for (int j = 0; j < allList.size(); j++) {
                    if (takeoutList.get(i).equals(allList.get(j))) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    allList.add(takeoutList.get(i));
                }
            }
        }
        return allList;
    }
}