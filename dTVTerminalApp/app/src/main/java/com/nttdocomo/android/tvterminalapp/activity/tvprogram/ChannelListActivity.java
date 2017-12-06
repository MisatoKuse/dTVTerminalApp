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
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ChannelListAdapter;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragment;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

public class ChannelListActivity extends BaseActivity implements View.OnClickListener, ChannelListFragment.ChannelListFragmentListener, DlnaRecVideoListener {

    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private LinearLayout mTabsLayout;
    private String[] mTabNames;
    private int screenWidth;
    private ViewPager mViewPager;
    private ChannelListFragmentFactory mFactory;
    private HorizontalScrollView mHorizontalScrollView;

    private DlnaProvRecVideo mDlnaProvRecVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_list_main_layout);
        screenWidth = getWidthDensity();
        initData();
        initView();

        //getBsData();
    }

    @Override
    protected void onResume(){
        if(null==mDlnaProvRecVideo) {
            mDlnaProvRecVideo = new DlnaProvRecVideo();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDlnaProvRecVideo != null) {
            mDlnaProvRecVideo.stopListen();
        }
    }

    private void initData() {
        mFactory= new ChannelListFragmentFactory();
    }


    private void initView() {
        mHorizontalScrollView = findViewById(R.id.channel_list_main_layout_channel_titles_hs);
        mViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
        initChannelListTab(mHorizontalScrollView);
        ChannelListPagerAdapter adp= new ChannelListPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adp);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);
            }
        });
        enableGlobalMenuIcon(true);
        enableStbStatusIcon(true);
    }

    private void getBsData(){
        DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(this);
        if (mDlnaProvRecVideo == null) {
            mDlnaProvRecVideo = new DlnaProvRecVideo();
        }
        if (mDlnaProvRecVideo.start(dlnaDmsItem.mUdn, this)) {
            mDlnaProvRecVideo.browseRecVideoDms(dlnaDmsItem.mControlUrl);
        }
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
                    mViewPager.setCurrentItem(tab_index);
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

    ChannelListActivity getActivity(){
        return this;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public void onScroll(ChannelListFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onScrollStateChanged(ChannelListFragment fragment, AbsListView absListView, int scrollState) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser, ChannelListFragment fragment){
        if(!isVisibleToUser){
            fragment.clearDatas();
            fragment.noticeRefresh();
            return;
        }

        ChannelListAdapter.ChListDataType type= fragment.getChListDataType();
        switch (type){
            case CH_LIST_DATA_TYPE_BS:
                getBsData();
                break;
            case CH_LIST_DATA_TYPE_TER:

                break;
            case CH_LIST_DATA_TYPE_HIKARI:

                break;
            case CH_LIST_DATA_TYPE_DTV:

                break;
            case CH_LIST_DATA_TYPE_INVALID:
                break;
        }
    }

    @Override
    public void onVideoBrows(DlnaRecVideoInfo curInfo) {
        int pos=mViewPager.getCurrentItem();
        final ChannelListFragment fragment= mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        for(int i=0;i<curInfo.size();++i){
            fragment.addData(curInfo.get(i));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.noticeRefresh();
            }
        });
    }

    @Override
    public String getCurrentDmsUdn() {
        return null;
    }

    private ChannelListAdapter.ChListDataType getTypeFromViewPagerIndex(int viewPagerIndex){
        ChannelListAdapter.ChListDataType ret=ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_INVALID;
        switch (viewPagerIndex){
            case 0:
                ret= ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI;
                break;
            case 1:
                ret = ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER;
                break;
            case 2:
                ret = ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS;
                break;
            case 3:
                ret = ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV;
                break;
        }
        return ret;
    }

    /*チャンネルリストアダプター*/
    private class ChannelListPagerAdapter extends FragmentStatePagerAdapter {

        ChannelListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(null==mFactory){
                return null;
            }
            return mFactory.createFragment(position, getActivity(), getTypeFromViewPagerIndex(position));
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
