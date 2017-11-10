/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.player;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.OtherContentsDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.OtherContentsDetailData;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.player.DtvContentsDetailFragmentFactory;

public class DtvContentsDetailActivity extends BaseActivity implements
        OtherContentsDataProvider.ApiDataProviderCallback {
    private ImageView mMenuImageView;
    private String[] mTabNames;
    private OtherContentsDataProvider mContentsDetailDataProvider;
    private DtvContentsDetailFragmentFactory mContentsDetailFragmentFactory = null;
    private HorizontalScrollView mTabScrollView;
    private LinearLayout mLinearLayout;
    private ViewPager mViewPager;
    private OtherContentsDetailData mDetailData;

    public static final String DTV_INFO_BUNDLE_KEY = "dTVInfoKey";
    public static final int DTV_CONTENTS_SERVICE_ID = 15;
    public static final int D_ANIMATION_CONTENTS_SERVICE_ID = 17;
    public static final int DTV_CHANNEL_CONTENTS_SERVICE_ID = 43;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtv_contents_detail_main_layout);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mDetailData = getIntent().getParcelableExtra(DTV_INFO_BUNDLE_KEY);
        DTVTLogger.start();
        setNoTitle();
        initData();
        initView();
        getGenreData(mViewPager.getCurrentItem());
    }

    /**
     * データの初期化
     */
    private void initData() {
        mTabNames = getResources().getStringArray(R.array.other_contents_detail_tabs);
        mContentsDetailDataProvider =
                new OtherContentsDataProvider(this);
        mContentsDetailFragmentFactory = new DtvContentsDetailFragmentFactory();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < 4; ++i) { // タブの数だけ処理を行う
            DtvContentsDetailBaseFragment b = mContentsDetailFragmentFactory.createFragment(i);
            if (null != b) {
                b.mContentsDetailData.clear();
            }
        }
    }

    /**
     * Viewの初期化
     */
    private void initView() {
        TextView headerTitle = findViewById(R.id.header_layout_title);
        headerTitle.setText(mDetailData.getTitle());
        mTabScrollView = findViewById(R.id.contents_detail_tab_strip_scroll);
        mViewPager = findViewById(R.id.contents_detail_result);
        ContentsDetailPagerAdapter contentsDetailPagerAdapter
                = new ContentsDetailPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(contentsDetailPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // スクロールによるタブ切り替え
                super.onPageSelected(position);
                setTab(position);
                getGenreData(mViewPager.getCurrentItem());
            }
        });
        initTabData();
    }

    /**
     * タブ毎にリクエストを行う
     *
     * @param tabPageNo
     */
    private void getGenreData(int tabPageNo) {
        mContentsDetailDataProvider.getContentsDetailData(tabPageNo);
    }

    /**
     * tabのレイアウトを設定
     */
    private void initTabData() {
        mTabScrollView.removeAllViews();
        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(layoutParams);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setBackgroundColor(Color.BLACK);
        mLinearLayout.setGravity(Gravity.LEFT);
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
                    // タップによるタブ切り替え
                    int position = (int) view.getTag();
                    mViewPager.setCurrentItem(position);
                    setTab(position);
                }
            });
            mLinearLayout.addView(tabTextView);
        }
    }

    /**
     * 取得結果の設定・表示
     */
    private void setShowContentsDetail() {
        //TODO：WebApiからのデータを受け取ったデータを画面に設定する
    }

    /*インジケーター設置*/
    public void setTab(int position) {
        if (mLinearLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) mLinearLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.indicating);
                } else {
                    mTextView.setBackgroundResource(R.drawable.indicating_no);
                }
            }
        }
    }

    /**
     * Fragmentの取得
     *
     * @return
     */
    private DtvContentsDetailBaseFragment getCurrentFragment() {

        int i = mViewPager.getCurrentItem();
        return mContentsDetailFragmentFactory.createFragment(i);
    }

    @Override
    public void otherContentsDetailCallback() {

    }

    /*検索結果タブ専用アダプター*/
    class ContentsDetailPagerAdapter extends FragmentStatePagerAdapter {
        public ContentsDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DtvContentsDetailBaseFragment fragment =
                    mContentsDetailFragmentFactory.createFragment(position);
            //Fragmentへデータを渡す
            Bundle args = new Bundle();
            args.putParcelable(DTV_INFO_BUNDLE_KEY, mDetailData);
            fragment.setArguments(args);
            return fragment;
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
