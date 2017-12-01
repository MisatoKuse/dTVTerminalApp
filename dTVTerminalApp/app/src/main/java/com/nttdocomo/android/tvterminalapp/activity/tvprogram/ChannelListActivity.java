/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;

import java.util.ArrayList;

public class ChannelListActivity extends BaseActivity implements View.OnClickListener {

    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private LinearLayout mTabsLayout;
    private String[] mTabNames;
    private int screenWidth;
    private ArrayList hikariList;
    private ArrayList terrestrialList;
    private ArrayList bSList;
    private ArrayList dtvChannelList;
    private ViewPager mChannelBodyViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_list_main_layout);
        screenWidth = getWidthDensity();
        initData();
        initView();
    }

    private void initData() {
        hikariList = new ArrayList();
        terrestrialList = new ArrayList();
        bSList = new ArrayList();
        dtvChannelList = new ArrayList();
        for (int i = 0; i < 50; i++) {
            View channelItemView = View.inflate(this, R.layout.channel_list_item, null);
            hikariList.add(channelItemView);
        }
        for (int i = 0; i < 50; i++) {
            View channelItemView = View.inflate(this, R.layout.channel_list_item, null);
            terrestrialList.add(channelItemView);
        }
        for (int i = 0; i < 50; i++) {
            View channelItemView = View.inflate(this, R.layout.channel_list_item, null);
            bSList.add(channelItemView);
        }
        for (int i = 0; i < 50; i++) {
            View channelItemView = View.inflate(this, R.layout.channel_list_item, null);
            dtvChannelList.add(channelItemView);
        }
    }


    private void initView() {
        HorizontalScrollView channelList = findViewById(R.id.channel_list_main_layout_channel_titles_hs);
        mChannelBodyViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
        initChannelListTab(channelList);
        mChannelBodyViewPager.setAdapter(new ChannelListAdapter(getSupportFragmentManager()));
        mChannelBodyViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);
            }
        });
    }

    private void initChannelListTab(HorizontalScrollView channelList) {
        mTabNames = getResources().getStringArray(R.array.channel_list_tab_names);
        channelList.removeAllViews();
        mTabsLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                screenWidth / SCREEN_TIME_WIDTH_PERCENT + (int) getDensity() * 5);
        mTabsLayout.setLayoutParams(layoutParams);
        mTabsLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTabsLayout.setGravity(Gravity.CENTER);
        mTabsLayout.setBackgroundColor(Color.BLACK);
        mTabsLayout.setBackgroundResource(R.drawable.rectangele_all);
        channelList.addView(mTabsLayout);
        for (int i = 0; i < mTabNames.length; i++) {
            TextView tabTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins((int) getDensity() * 30, 0, 0, 0);
            tabTextView.setLayoutParams(params);
            tabTextView.setText(mTabNames[i]);
            tabTextView.setBackgroundColor(Color.BLACK);
            tabTextView.setTextColor(Color.WHITE);
            tabTextView.setGravity(Gravity.CENTER_VERTICAL);
            tabTextView.setTag(i);
            tabTextView.setBackgroundResource(0);
            tabTextView.setTextColor(Color.WHITE);
            if (i == 0) {
                tabTextView.setBackgroundResource(R.drawable.rectangele);
                tabTextView.setTextColor(Color.GRAY);
            }
            tabTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tab_index = (int) view.getTag();
                    setTab(tab_index);
                    mChannelBodyViewPager.setCurrentItem(tab_index);
                    // TODO: 2017/11/30 clear,getデータ
                    //clearData();
                    //getChannelData();
                }
            });
            mTabsLayout.addView(tabTextView);
        }
    }

    private void setTab(int position) {
        if (mTabsLayout != null) {
            for (int i = 0; i < mTabNames.length; i++) {
                TextView mTextView = (TextView) mTabsLayout.getChildAt(i);
                if (position == i) {
                    mTextView.setBackgroundResource(R.drawable.rectangele);
                    mTextView.setTextColor(Color.GRAY);
                } else {
                    mTextView.setBackgroundResource(0);
                    mTextView.setTextColor(Color.WHITE);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    /*チャンネルリストアダプター*/
    private class ChannelListAdapter extends FragmentStatePagerAdapter {

        ChannelListAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ChannelListFragment channelListFragment1 = new ChannelListFragment(hikariList);
            ChannelListFragment channelListFragment2 = new ChannelListFragment(terrestrialList);
            ChannelListFragment channelListFragment3 = new ChannelListFragment(bSList);
            ChannelListFragment channelListFragment4 = new ChannelListFragment(dtvChannelList);
            if (position == 0) {
                return channelListFragment1;
            }
            if (position == 1) {
                return channelListFragment2;
            }
            if (position == 2) {
                return channelListFragment3;
            }
            if (position == 3) {
                return channelListFragment4;
            }
            return null;
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
