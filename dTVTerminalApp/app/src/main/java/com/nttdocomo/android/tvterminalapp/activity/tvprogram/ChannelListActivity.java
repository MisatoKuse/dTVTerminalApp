/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.tvprogram;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.nttdocomo.android.tvterminalapp.dataprovider.DTvChDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.HikariTvChDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.ScaledDownProgramListDataProvider;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragment;
import com.nttdocomo.android.tvterminalapp.fragment.channellist.ChannelListFragmentFactory;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaBsChListListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaDmsItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvBsChList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvRecVideo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaProvTerChList;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoItem;
import com.nttdocomo.android.tvterminalapp.jni.DlnaRecVideoListener;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListInfo;
import com.nttdocomo.android.tvterminalapp.jni.DlnaTerChListListener;
import com.nttdocomo.android.tvterminalapp.model.program.Channel;
import com.nttdocomo.android.tvterminalapp.model.program.ChannelsInfo;
import com.nttdocomo.android.tvterminalapp.utils.SharedPreferencesUtils;

import java.util.ArrayList;

public class ChannelListActivity extends BaseActivity implements View.OnClickListener, ChannelListFragment.ChannelListFragmentListener,
        DlnaRecVideoListener, DlnaTerChListListener, DlnaBsChListListener, ScaledDownProgramListDataProvider.ApiDataProviderCallback {

    private static final int SCREEN_TIME_WIDTH_PERCENT = 9;
    private LinearLayout mTabsLayout;
    private String[] mTabNames;
    private int screenWidth;
    private ViewPager mViewPager;
    private ChannelListFragmentFactory mFactory;
    private HorizontalScrollView mHorizontalScrollView;

    private DlnaProvRecVideo mDlnaProvRecVideo;
    private DlnaProvBsChList mDlnaProvBsChList;
    private DlnaProvTerChList mDlnaProvTerChList;
    private HikariTvChDataProvider mHikariTvChDataProvider;
    private DTvChDataProvider mDTvChDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_list_main_layout);
        screenWidth = getWidthDensity();
        initView();
        initData();
        firstMore();
    }

    private void firstMore() {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                displayMore(true, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
            }
        }, 200);
    }

    @Override
    protected void onResume(){
        if(null==mDlnaProvRecVideo) {
            mDlnaProvRecVideo = new DlnaProvRecVideo();
        }
        if(null==mDlnaProvBsChList) {
            mDlnaProvBsChList = new DlnaProvBsChList();
        }
        if(null==mDlnaProvTerChList) {
            mDlnaProvTerChList = new DlnaProvTerChList();
        }
        if(null==mHikariTvChDataProvider){
            mHikariTvChDataProvider=new HikariTvChDataProvider(this);
        }
        if(null==mDTvChDataProvider){
            mDTvChDataProvider=new DTvChDataProvider(this);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDlnaProvRecVideo != null) {
            mDlnaProvRecVideo.stopListen();
        }
        if (mDlnaProvTerChList != null) {
            mDlnaProvTerChList.stopListen();
        }
        onPageChange(CHANNEL_LIST_TAB_HIKARI);
        onPageChange(CHANNEL_LIST_TAB_BS);
    }

    private void initData() {
        mFactory= new ChannelListFragmentFactory();
    }


    private void initView() {
        mHorizontalScrollView = findViewById(R.id.channel_list_main_layout_channel_titles_hs);
        //mViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
        initChannelListTab(mHorizontalScrollView);
        ChannelListPagerAdapter adp= new ChannelListPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.channel_list_main_layout_channel_body_vp);
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

    private final int CHANNEL_LIST_TAB_HIKARI = 0;
    private final int CHANNEL_LIST_TAB_TER = 1;
    private final int CHANNEL_LIST_TAB_BS = 2;
    private final int CHANNEL_LIST_TAB_DTV = 3;

    private Handler mHandle= new Handler();

    private void displayMore(boolean yn, int tabNo, ChannelListAdapter.ChListDataType type){
        final ChannelListFragment fragment= mFactory.createFragment(tabNo, this, type);
        fragment.displayMoreData(yn);
    }

    private Runnable mRunableBs = new Runnable() {
        @Override
        public void run() {
            //STBにチャンネル機能を提供していないで、仮データを使うために、本番ソースを一時コメントしている。
            //本番ソース begin
//            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
//            if (mDlnaProvBsChList.start(dlnaDmsItem, getActivity())) {
//                boolean ret=mDlnaProvBsChList.browseChListDms();
//                if(!ret){
//                    onError("Get BS channel list datas failed");
//                }
//            }
            //本番ソース end
            //仮ソース begin
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
            if (mDlnaProvRecVideo.start(dlnaDmsItem, getActivity())) {
                boolean ret=mDlnaProvRecVideo.browseRecVideoDms();
                if(!ret){
                    onError("Get recoreded video datas failed");
                }
            }
            testType=ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS;   //test
            //仮ソース end
        }
    };

    private void getBsData(){
        displayMore(true, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        mHandle.postDelayed(mRunableBs, CHANNEL_LIST_TAB_DELAY_TIME);
    }

    private final int CHANNEL_LIST_TAB_DELAY_TIME=600;

    private Runnable mRunableTer = new Runnable() {
        @Override
        public void run() {
            //STBにチャンネル機能を提供していないで、仮データを使うために、本番ソースを一時コメントしている。
            //本番ソース begin
//            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
//            if (mDlnaProvTerChList.start(dlnaDmsItem, getActivity())) {
//                boolean ret=mDlnaProvTerChList.browseChListDms();
//                if(!ret){
//                    onError("Get BS channel list datas failed");
//                }
//            }
            //本番ソース end
            //仮ソース begin
            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
            if (mDlnaProvRecVideo.start(dlnaDmsItem, getActivity())) {
                mDlnaProvRecVideo.browseRecVideoDms();
            }
            testType=ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER;   //test
            //仮ソース end
        }
    };

    private void getTerData(){
        displayMore(true, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
        mHandle.postDelayed(mRunableTer, CHANNEL_LIST_TAB_DELAY_TIME);
    }

    private void getHikariData(){
        //test b
        displayMore(true, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
        //test e
        mHandle.postDelayed(mRunableHikari, CHANNEL_LIST_TAB_DELAY_TIME);
    }

    private Runnable mRunableHikari = new Runnable() {
        @Override
        public void run() {
//            //test b
//            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
//            if (mDlnaProvRecVideo.start(dlnaDmsItem, getActivity())) {
//                mDlnaProvRecVideo.browseRecVideoDms();
//            }
//            testType=ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI;   //test
            mHikariTvChDataProvider.getChannelList(1, 1, "");
        }
    };

    private Runnable mRunableDtv = new Runnable() {
        @Override
        public void run() {
//            DlnaDmsItem dlnaDmsItem = SharedPreferencesUtils.getSharedPreferencesStbInfo(getActivity());
//            if (mDlnaProvRecVideo.start(dlnaDmsItem, getActivity())) {
//                mDlnaProvRecVideo.browseRecVideoDms();
//            }
//            testType=ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV;   //test
            mDTvChDataProvider.getChannelList(1, 1, "");
        }
    };

    private void getDtvData(){
        //test b
        displayMore(true, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
        //test e
        mHandle.postDelayed(mRunableDtv, CHANNEL_LIST_TAB_DELAY_TIME);
    }

    private void onPageChange(int tabPosition){
        if(null==mHandle){
            return;
        }
        switch (tabPosition){
            case CHANNEL_LIST_TAB_HIKARI:
                mHandle.removeCallbacks(mRunableBs);
                mHandle.removeCallbacks(mRunableDtv);
                mHandle.removeCallbacks(mRunableTer);
                displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                break;
            case CHANNEL_LIST_TAB_DTV:
                mHandle.removeCallbacks(mRunableBs);
                mHandle.removeCallbacks(mRunableHikari);
                mHandle.removeCallbacks(mRunableTer);
                displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                break;
            case CHANNEL_LIST_TAB_TER:
                mHandle.removeCallbacks(mRunableBs);
                mHandle.removeCallbacks(mRunableDtv);
                mHandle.removeCallbacks(mRunableHikari);
                displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                displayMore(false, CHANNEL_LIST_TAB_BS, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
                break;
            case CHANNEL_LIST_TAB_BS:
                mHandle.removeCallbacks(mRunableDtv);
                mHandle.removeCallbacks(mRunableHikari);
                mHandle.removeCallbacks(mRunableTer);
                displayMore(false, CHANNEL_LIST_TAB_HIKARI, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                displayMore(false, CHANNEL_LIST_TAB_DTV, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                displayMore(false, CHANNEL_LIST_TAB_TER, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
                break;
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
                }
            });
            mTabsLayout.addView(tabTextView);
        }
    }

    private void setTab(int position) {
        //onPageChange(position);
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
                onPageChange(CHANNEL_LIST_TAB_BS);
                break;
            case CH_LIST_DATA_TYPE_TER:
                getTerData();
                onPageChange(CHANNEL_LIST_TAB_TER);
                break;
            case CH_LIST_DATA_TYPE_HIKARI:
                getHikariData();
                onPageChange(CHANNEL_LIST_TAB_HIKARI);
                break;
            case CH_LIST_DATA_TYPE_DTV:
                getDtvData();
                onPageChange(CHANNEL_LIST_TAB_DTV);
                break;
            case CH_LIST_DATA_TYPE_INVALID:
                break;
        }
    }

    //test begin
        private ChannelListAdapter.ChListDataType testType;

    //test end

    //仮データ関数 begin
    @Override
    public void onVideoBrows(DlnaRecVideoInfo curInfo) {
        int pos=mViewPager.getCurrentItem();
        //STBにチャンネル機能を提供していないで、仮データを使うために、testデータを一時コメントしている。
        //final ChannelListFragment fragment= mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        //test b
        DlnaRecVideoInfo curInfo2=new DlnaRecVideoInfo();
        if(testType==ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS){
            for(int i=0;i<curInfo.size();++i){
                curInfo2.addItem(curInfo.get(i));
                curInfo2.get(i).mTitle="BSチャンネル " + i;
            }
            pos=2;
        } else if(testType==ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER){
            for(int i=0;i<curInfo.size();++i){
                curInfo2.addItem(curInfo.get(i));
                curInfo2.get(i).mTitle="地上波チャンネル " + i;
            }
            pos=1;
        } else if(testType==ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI){
            pos=0;
        } else if(testType==ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV){
            pos=3;
        }
        //test e
        final ChannelListFragment fragment= mFactory.createFragment(pos, this, testType); //test
        for(int i=0;i<curInfo.size();++i){
//            fragment.addData(curInfo.get(i));   //本番
            fragment.addData(curInfo2.get(i));      //test
        }

        updateUi(fragment);
    }
    //仮データ関数 end

    private void updateUi(final ChannelListFragment fragment){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.noticeRefresh();
                fragment.displayMoreData(false);
            }
        });
    }

    @Override
    public void onListUpdate(DlnaTerChListInfo curInfo) {
        int pos=mViewPager.getCurrentItem();
        final ChannelListFragment fragment= mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_TER);
        for(int i=0;i<curInfo.size();++i){
            fragment.addData(curInfo.get(i));
        }
        updateUi(fragment);
    }

    @Override
    public void onListUpdate(DlnaBsChListInfo curInfo) {
        int pos=mViewPager.getCurrentItem();
        final ChannelListFragment fragment= mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_BS);
        for(int i=0;i<curInfo.size();++i){
            DlnaBsChListItem item=curInfo.get(i);

            fragment.addData(item);
        }
        updateUi(fragment);
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

    /**
     * 縮小番組表多チャンネル情報を戻すコールバック
     *
     * @param channelsInfo 画面に渡すチャンネル番組情報
     */
    @Override
    public void channelInfoCallback(ChannelsInfo channelsInfo){
        ArrayList<Channel> channels= channelsInfo.getChannels();
        if(0==channels.size()){
            return;
        }
    }

    /**
     * チャンネルリストを戻す
     *
     * @param channels 　画面に渡すチャンネル情報
     */
    @Override
    public void channelListCallback(ArrayList<Channel> channels){
        int size=channels.size();
        if(0==size){
            return;
        }
        int pos=mViewPager.getCurrentItem();
        ChannelListFragment fragment=null;
        switch (pos) {
            case CHANNEL_LIST_TAB_HIKARI:
                fragment= mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_HIKARI);
                break;
            case CHANNEL_LIST_TAB_DTV:
                fragment= mFactory.createFragment(pos, this, ChannelListAdapter.ChListDataType.CH_LIST_DATA_TYPE_DTV);
                break;
        }
        if(null==fragment){
            return;
        }
        for(int i=0;i<channels.size();++i){
            fragment.addData(channels.get(i));
        }
        updateUi(fragment);
    }
}
