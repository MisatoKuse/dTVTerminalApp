/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.adapter.ClipMainAdapter;
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedBaseFrgament;
import com.nttdocomo.android.tvterminalapp.fragment.recorded.RecordedFragmentFactory;

public class RecordedListActivity extends BaseActivity implements View.OnClickListener, RecordedBaseFragmentScrollListener {

    private HorizontalScrollView mTabView;
    private LinearLayout mTabLinearLayout;
    private String[] mRecordTabNames;
    private ImageView mMenuImageView;
    private ViewPager mViewPager;

    private RecordedFragmentFactory mRecordedFragmentFactory = null;

    private ContentsAdapter mContentsAdapter;

    public static final int RECORDED_MODE_NO_OF_ALL = 0; // すべて
    public static final int RECORDED_MODE_NO_OF_TAKE_OUT = 1; // 持ち出し

    // TODO この辺りはタブUI共通の値であれば、styleに定義しましょう。 コード上で動的にstyle適用させるにはコツがいるようですが。
    //設定するマージンのピクセル数
    private static final int LEFT_MARGIN = 30;
    private static final int ZERO_MARGIN = 0;
    private static final int TAB_LAYOUT_DENSITY = 5;
    private static final int TAB_NAME_DENSITY = 15;
    private static final int TAB_LAYOUT_WIDTH_DENSITY = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list_main_layout);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.nav_menu_item_recorder_program));
        initView();
        setTabView();
        setPagerAdapter();
    }

    private void initView() {
        mRecordedFragmentFactory = new RecordedFragmentFactory();
        mTabView = findViewById(R.id.record_list_main_layout_scroll);
        mViewPager = findViewById(R.id.record_list_main_layout_viewpagger);
    }

    private void setPagerAdapter() {
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

    private void setRecordedAllContents() {
        RecordedBaseFrgament recordedBaseFrgament = mRecordedFragmentFactory.createFragment(RECORDED_MODE_NO_OF_ALL, this);
//        setMode(ContentsAdapter.Mode.CLIP_LIST_MODE_VIDEO);
        // TODO DPからデータを取得
    }

    private void setRecordedTakeOutContents() {
        RecordedBaseFrgament recordedBaseFrgament = mRecordedFragmentFactory.createFragment(RECORDED_MODE_NO_OF_TAKE_OUT, this);
//        setMode(ContentsAdapter.Mode.CLIP_LIST_MODE_TV);
        // TODO DPからデータを取得
    }

    public void setMode(ClipMainAdapter.Mode mode){
        if(null!=mContentsAdapter){
//            mContentsAdapter.setMode(mode);
//            mData.clear();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    /**
     * 機能
     * タブの設定
     */
    private void setTabView() {
        mRecordTabNames = getResources().getStringArray(R.array.record_list_tab_names);
        mTabView.removeAllViews();
        mTabLinearLayout = new LinearLayout(this);
        // TODO このあたりもアプリ共通であれば、処理で算出しなくてよい部分だけでもstyleで定義しましょう。
        // 画面ごとに同じ処理があったり最悪バラバラな設定になるのはNGかと。
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                getWidthDensity() / TAB_LAYOUT_WIDTH_DENSITY + (int) getDensity() * TAB_LAYOUT_DENSITY);
        mTabLinearLayout.setLayoutParams(layoutParams);
        mTabLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTabLinearLayout.setBackgroundColor(Color.BLACK);
        mTabLinearLayout.setBackgroundResource(R.drawable.rectangele_all);
        mTabView.addView(mTabLinearLayout);
        for (int i = 0; i < mRecordTabNames.length; i++) {
            // TODO この辺りも極力style化すべきかと。
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if (i != 0) {
                params.setMargins((int) getDensity() * TAB_NAME_DENSITY, ZERO_MARGIN,
                        ZERO_MARGIN, ZERO_MARGIN);
            } else {
                params.setMargins((int) LEFT_MARGIN, ZERO_MARGIN, ZERO_MARGIN, ZERO_MARGIN);
            }
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mRecordTabNames[i]);
            tabTextView.setTag(i);
            tabTextView.setBackgroundResource(0);

            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.rectangele);
                tabTextView.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
            }

            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTab((int) view.getTag());
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
    public void setTab(int position) {
        if (mTabLinearLayout != null) {
            for (int i = 0; i < mRecordTabNames.length; i++) {
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

    /*検索結果タブ専用アダプター*/
    private class MainAdpater extends FragmentStatePagerAdapter {

        MainAdpater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            synchronized (this) {
                return mRecordedFragmentFactory.createFragment(position, RecordedListActivity.this);
            }
        }

        @Override
        public int getCount() {
            return mRecordTabNames.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mRecordTabNames[position];
        }
    }

    @Override
    public void onScroll(RecordedBaseFrgament fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // TODO loading処理
    }
}