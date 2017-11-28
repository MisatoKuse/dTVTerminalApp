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
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFrgament;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDMSInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

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

    public static final String RECORD_LIST__KEY = "recordListKey";

    private DlnaProvRecVideo dlnaProvRecVideo;

    //設定するマージンのピクセル数
    private static final String DATE_FORMAT = "yyyy/MM/ddHH:mm:ss";
    private String date[] = {"日", "月", "火", "水", "木", "金", "土"};

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
        initData();
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
        mViewPager = findViewById(R.id.record_list_main_layout_viewpagger);
        mTabScrollView = findViewById(R.id.record_list_main_layout_scroll);
        mTabNames = getResources().getStringArray(R.array.record_list_tab_names);
        mRecordedFragmentFactory = new RecordedFragmentFactory();
        mSearchView = findViewById(R.id.record_list_main_layout_searchview);
    }

    /**
     * 画面描画
     */
    private void initData() {
        DlnaProvRecVideo dlnaProvRecVideo = new DlnaProvRecVideo();
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        boolean startFlag = dlnaProvRecVideo.start(dlnaDmsItem.mUdn, this); //b002
        if (startFlag) {
            dlnaProvRecVideo.browseRecVideoDms(dlnaDmsItem.mControlUrl);
        }
    }

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
                        setRecordedTakeOutContents();
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
        RecordedBaseFrgament recordedBaseFrgament = mRecordedFragmentFactory.createFragment(RECORDED_MODE_NO_OF_ALL, this);
    }

    private void setRecordedTakeOutContents() {
        DTVTLogger.start();
        RecordedBaseFrgament recordedBaseFrgament = mRecordedFragmentFactory.createFragment(RECORDED_MODE_NO_OF_TAKE_OUT, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dlnaProvRecVideo != null) {
            dlnaProvRecVideo.stopListen();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (dlnaProvRecVideo != null) {
            dlnaProvRecVideo = new DlnaProvRecVideo();
        }
        if (dlnaProvRecVideo.start(dlnaDmsItem.mUdn, this)) {
            dlnaProvRecVideo.browseRecVideoDms(dlnaDmsItem.mControlUrl);
        }
    }

    /**
     * フラグメント生成
     */
    private RecordedBaseFrgament getCurrentRecordedBaseFrgament() {
        DTVTLogger.start();
        int currentPageNo = mViewPager.getCurrentItem();
        RecordedBaseFrgament baseFragment = mRecordedFragmentFactory.createFragment(currentPageNo, this);
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
                RecordedBaseFrgament baseFragment = mRecordedFragmentFactory.createFragment(i, this);
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
    public void onScroll(RecordedBaseFrgament fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        DTVTLogger.start();
    }

    @Override
    public void onVideoBrows(DlnaRecVideoInfo curInfo) {
        if (curInfo != null && curInfo.getRecordVideoLists() != null) {
            final RecordedBaseFrgament baseFrgament = getCurrentRecordedBaseFrgament();
            List<ContentsData> listData = baseFrgament.getContentsData();
            listData.clear();
            ArrayList<DlnaRecVideoItem> list = curInfo.getRecordVideoLists();

            getTakeOutContents(list);

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.JAPAN);
            for (int i = 0; i < list.size(); i++) {
                // TODO 年齢取得未実装の為、固定値を返却
                boolean isAge = true;
                DlnaRecVideoItem dlnaRecVideoItem = list.get(i);
                ContentsData data = new ContentsData();
                int currentPageNo = mViewPager.getCurrentItem();
                if (isAge) {
                    data.setTitle(dlnaRecVideoItem.mTitle);
                    data.setAllowedUse(dlnaRecVideoItem.mAllowedUse);
                    String time = dlnaRecVideoItem.mDate.replaceAll("-", "/").replace("T", "");
                    try {
                        Calendar calendar = Calendar.getInstance(Locale.JAPAN);
                        calendar.setTime(sdf.parse(time));
                        int week = calendar.get(Calendar.DAY_OF_WEEK);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        StringBuilder selectDate = new StringBuilder();
                        selectDate.append(month);
                        selectDate.append("/");
                        selectDate.append(day);
                        selectDate.append("  (");
                        selectDate.append(date[week - 1]);
                        selectDate.append(")  ");
                        selectDate.append(hour);
                        selectDate.append(":");
                        selectDate.append(minute);
                        data.setTime(selectDate.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    listData.add(data);
                } else {
                    // TODO 年齢制限のフィルターに引っかかった時の処理
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
     * すべてListの生成
     *
     * @return すべてList
     */
    private List<String> getTakeOutContents(ArrayList<DlnaRecVideoItem> allList) {
        // 持ち出し
        ArrayList<String> all = new ArrayList<>();
        all.add("a");
        all.add("b");
        all.add("c");
        all.add("d");
        // すべて
        ArrayList<String> take = new ArrayList<>();
        take.add("a");
        take.add("b");
        take.add("c");
        take.add("e");
        List<String> dataList = all; // allList
        if (all != null && take != null) {
            for (int i = 0; i < take.size(); i++) {
                int getPosition = -1;
                boolean isExist = false;
                for (int j = 0; j < dataList.size(); j++) {
                    if (take.get(i).equals(all.get(j))) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    all.add(take.get(i));
                }
            }
        }
        return dataList;
    }
}