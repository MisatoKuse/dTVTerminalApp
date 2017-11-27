/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;


import android.os.Bundle;
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
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;

import java.util.ArrayList;
import java.util.List;

public class RecordedListActivity extends BaseActivity implements View.OnClickListener, RecordedBaseFragmentScrollListener
        , DlnaRecVideoListener {
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
    private int screenWidth;

    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private static final int MARGIN_ZERO = 0;
    private static final int MARGIN_LEFT_TAB = 5;
    private static final int MARGIN_LEFT_NOT_INDEX = 15;

    // タブTYPE：すべて
    private static final int RECORDED_MODE_NO_OF_ALL = 0;
    // タブTYPE：持ち出し
    private static final int RECORDED_MODE_NO_OF_TAKE_OUT = 2;

    // TODO タブUI共通の値であれば、styleに定義
    //設定するマージンのピクセル数
    private static final int LEFT_MARGIN = 30;
    private static final int ZERO_MARGIN = 0;
    private static final int TAB_NAME_DENSITY = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DTVTLogger.start();
        setContentView(R.layout.record_list_main_layout);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.nav_menu_item_recorder_program));
        // TODO DPクラスをここでnewする

        initView();
        initTabVIew();
        setPagerAdapter();
//        setSearchViewState();
        DTVTLogger.end();
    }

    /**
     * TODO 検索バーの内部処理はSprint7では実装しない
     */
    private void setSearchViewState() {
    }

    /**
     * ページ数を変数に格納
     *
     * @param pageNumber
     */
    private void setPageNumber(int pageNumber) {
        DTVTLogger.start();
        mPageNumber = pageNumber;
        mPageNumber = (mPageNumber < 0 ? 0 : mPageNumber);
    }

//    /**
//     * タブのタイプ別にList一覧を出しわける
//     *
//     * @return
//     */
//    private ArrayList<SearchServiceType> getTypeArray() {
//        // TODO タイプclass検討する
//        ArrayList<SearchServiceType> ret = new ArrayList<SearchServiceType>();
//        if (null == mViewPager) {
//            return ret;
//        }
//        int pageIndex = mViewPager.getCurrentItem();
//        switch (pageIndex) {
//            case RECORDED_MODE_NO_OF_ALL: // すべて
////                ret.add(new SearchServiceType(SearchServiceType.ServiceId.hikariTVForDocomo));
////                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTVChannel));
//                break;
//            case RECORDED_MODE_NO_OF_TAKE_OUT: // 持ち出し
////                ret.add(new SearchServiceType(SearchServiceType.ServiceId.hikariTVForDocomo));
////                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dTV));
////                ret.add(new SearchServiceType(SearchServiceType.ServiceId.dAnime));
//                break;
//            default:
//                break;
//        }
//        return ret;
//    }

    /**
     * TODO 検索バーの内部処理はSprint7では実装しない
     *
     * @param searchText
     */
    private void setSearchData(String searchText) {
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
        DlnaProvRecVideo dlnaProvRecVideo = new DlnaProvRecVideo();
//        boolean startFlag =  dlnaProvRecVideo.start("uuid:aad079ae-0ea4-431a-b88f-a47174cd5601",this);
//        boolean startFlag =  dlnaProvRecVideo.start("uuid:aad079ae-0ea4-431a-b88f-020000000000",this);
        //boolean startFlag =  dlnaProvRecVideo.start("uuid:4f154baf-7827-24b2-ffff-ffff97fdca80",this);
        boolean startFlag = dlnaProvRecVideo.start("uuid:da769f7c-3650-dabc-ffff-ffffb853d273", this); //b002

        if (startFlag) {
            //dlnaProvRecVideo.browseRecVideoDms("http://192.168.11.10:58645/dev/4f154baf-7827-24b2-ffff-ffff97fdca80/svc/upnp-org/ContentDirectory/action"); //bubule 1
            dlnaProvRecVideo.browseRecVideoDms("http://192.168.11.15:58645/dev/da769f7c-3650-dabc-ffff-ffffb853d273/svc/upnp-org/ContentDirectory/action"); //b0021
//            dlnaProvRecVideo.browseRecVideoDms("http://192.168.11.10:58645/dev/4f154baf-7827-24b2-ffff-ffff97fdca80/svc/upnp-org/ContentDirectory/action");

//            dlnaProvRecVideo.browseRecVideoDms("http://192.168.11.8:50001/ContentDirectory/control");
            //dlnaProvRecVideo.browseRecVideoDms("http://192.168.11.15:50000/ContentDirectory/control");
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
        screenWidth = getWidthDensity();
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

    /**
     * フラグメント生成
     *
     * @return
     */
    private RecordedBaseFrgament getCurrentRecordedBaseFrgament() {
        DTVTLogger.start();
        int currentPageNo = mViewPager.getCurrentItem();
        RecordedBaseFrgament baseFragment = (RecordedBaseFrgament) mRecordedFragmentFactory.createFragment(currentPageNo, this);
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

    /**
     * ページング判定の変更
     *
     * @param bool
     */
    private void setPagingStatus(boolean bool) {
        DTVTLogger.start();
        synchronized (this) {
            mIsPaging = bool;
        }
    }

    @Override
    public void onScroll(RecordedBaseFrgament fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        DTVTLogger.start();
    }

    @Override
    public void onVideoBrows(DlnaRecVideoInfo curInfo) {
        DTVTLogger.start();
        DTVTLogger.debug("curInfo = " + curInfo);
        if (curInfo != null && curInfo.getRecordVideoLists() != null) {
            final RecordedBaseFrgament baseFrgament = getCurrentRecordedBaseFrgament();
            List<ContentsData> listData = baseFrgament.getContentsData();
            ArrayList<DlnaRecVideoItem> list = curInfo.getRecordVideoLists();
            for (int i = 0; i < list.size(); i++) {
                DlnaRecVideoItem dlnaRecVideoItem = list.get(i);
                ContentsData data = new ContentsData();
                data.setTitle(dlnaRecVideoItem.mTitle);
                listData.add(data);
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
}