/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.adapter.ClipMainAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.TvClipDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.VodClipDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.TvClipContentInfo;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.VodClipContentInfo;
import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragment;
import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListBaseFragmentScrollListener;
import com.nttdocomo.android.tvterminalapp.fragment.ClipList.ClipListFragmentFactory;


public class ClipListActivity extends BaseActivity implements View.OnClickListener, VodClipDataProvider.ApiDataProviderCallback, TvClipDataProvider.TvClipDataProviderCallback, ClipListBaseFragmentScrollListener {
    private ImageView mMenuImageView;
    private HorizontalScrollView mTabScrollView;
    private ViewPager mViewPager;
    private String[] mTabNames;
    private LinearLayout mLinearLayout;
    private VodClipDataProvider mVodClipDataProvider;
    private TvClipDataProvider mTvClipDataProvider;
    private final int NUM_PER_PAGE=10;
    private boolean mIsCommunicating = false;
    private ClipListFragmentFactory mClipListFragmentFactory=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clip_list_main);
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.str_clip_activity_title));
        initData();

        initView();
        resetPaging();
        setTv();
    }

    private void resetPaging() {
        synchronized (this){
            setCommunicatingStatus(false);
            ClipListBaseFragment b=getCurrentFragment();
            if(null!=b && null != b.mData){
                b.mData.clear();
                b.noticeRefresh();
            }
        }
    }

    private void setCommunicatingStatus(boolean b){
        synchronized (this){
            mIsCommunicating = b;
        }
    }

    private int getCurrentNumber(){
        ClipListBaseFragment b=getCurrentFragment();
        if(null==b || null==b.mData || 0==b.mData.size()){
            return 0;
        }
        return b.mData.size()/NUM_PER_PAGE;
    }

    private boolean isSkipTv(TvClipContentInfo tvClipContentInfo){
        ClipListBaseFragment b= mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);
        if(null==b || null == b.mData || 0==b.mData.size()){
            return false;
        }

        if(null==tvClipContentInfo || 0==tvClipContentInfo.size() ){
            return true;
        }

        TvClipContentInfo.TvClipContentInfoItem item1= (TvClipContentInfo.TvClipContentInfoItem)b.mData.get(b.mData.size() -1 );
        TvClipContentInfo.TvClipContentInfoItem item2 = tvClipContentInfo.get(tvClipContentInfo.size() -1 );
        return tvClipContentInfo.isContentEqual(item1, item2);
    }

    private static final int CLIP_LIST_PAGE_NO_OF_TV = 0;
    private static final int CLIP_LIST_PAGE_NO_OF_VOD = 1;

    private boolean isSkipVod(VodClipContentInfo vodClipContentInfo){
        ClipListBaseFragment b= mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);
        if(null==b || null == b.mData || 0==b.mData.size()){
            return false;
        }

        if(null==vodClipContentInfo || 0==vodClipContentInfo.size() ){
            return true;
        }

        VodClipContentInfo.VodClipContentInfoItem item1= (VodClipContentInfo.VodClipContentInfoItem)b.mData.get(b.mData.size() -1 );
        VodClipContentInfo.VodClipContentInfoItem item2 = vodClipContentInfo.get(vodClipContentInfo.size() -1 );
        return vodClipContentInfo.isContentEqual(item1, item2);
    }

    private void initData() {
        mTabNames = getResources().getStringArray(R.array.tab_clip_names);
        mVodClipDataProvider = new VodClipDataProvider(this);
        mTvClipDataProvider = new TvClipDataProvider(this);
        mClipListFragmentFactory = new ClipListFragmentFactory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(int i=0;i<2; ++i){
            ClipListBaseFragment b =mClipListFragmentFactory.createFragment(i, this);
            if(null!=b){
                b.mData.clear();
            }
        }
    }

    @Override
    public void tvClipListCallback(TvClipContentInfo clipContentInfo) {
        if(null == clipContentInfo || 0== clipContentInfo.size()){
            //通信とJSON Parseに関してerror処理
            DTVTLogger.debug("ClipListActivity::TvClipListCallback, get data failed.");
            // TODO:エラーメッセージ表示はリスト画面上に表示する
            Toast.makeText(this, "クリップデータ取得失敗", Toast.LENGTH_SHORT);
            return;
        }

        if(0==clipContentInfo.size()){
            //doing
            resetCommunication();
            return;
        }

        if(isSkipTv(clipContentInfo)){
            resetCommunication();
            return;
        }

        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);

        int pageNumber =  getCurrentNumber();
        for(int i= pageNumber*NUM_PER_PAGE;i<(pageNumber + 1)*NUM_PER_PAGE  && i<clipContentInfo.size();++i){
            if(null!=fragment.mData) {
                fragment.mData.add(clipContentInfo.get(i));
            }
        }

        DTVTLogger.debug("tvClipListCallback");

        resetCommunication();
        fragment.noticeRefresh();
    }

    @Override
    public void vodClipListCallback(VodClipContentInfo clipContentInfo) {
        if(null == clipContentInfo || 0== clipContentInfo.size()){
            //通信とJSON Parseに関してerror処理
            DTVTLogger.debug("ClipListActivity::VodClipListCallback, get data failed");
            // TODO:エラーメッセージ表示はリスト画面上に表示する
            Toast.makeText(this, "クリップデータ取得失敗", Toast.LENGTH_SHORT);
            return;
        }

        if(0==clipContentInfo.size()){
            //doing
            resetCommunication();
            return;
        }

        if(isSkipVod(clipContentInfo)){
            resetCommunication();
            return;
        }

        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);

        int pageNumber =  getCurrentNumber();
        for(int i= pageNumber*NUM_PER_PAGE;i<(pageNumber + 1)*NUM_PER_PAGE  && i<clipContentInfo.size();++i){
            if(null!=fragment.mData) {
                fragment.mData.add(clipContentInfo.get(i));
            }
        }

        DTVTLogger.debug("vodClipListCallback");

        resetCommunication();
        fragment.noticeRefresh();
    }

    private void resetCommunication(){
        ClipListBaseFragment b = getCurrentFragment();
        if(null==b){
            return;
        }
        b.displayMoreData(false);
        setCommunicatingStatus(false);
    }

    @Override
    public void onScroll(ClipListBaseFragment fragment, AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        synchronized (this) {
            ClipListBaseFragment b= getCurrentFragment();
            if(null==b || null==fragment.getClipMainAdapter()){
                return;
            }
            if(fragment!=b){
                return;
            }

            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                    ) {
                DTVTLogger.debug("ClipListActivity::onScroll, paging, firstVisibleItem=" + firstVisibleItem + ", totalItemCount=" + totalItemCount+ ", visibleItemCount=" + visibleItemCount);
            }
        }
    }

    @Override
    public void onScrollStateChanged(ClipListBaseFragment fragment, AbsListView absListView, int scrollState) {
        synchronized (this) {

            ClipListBaseFragment b= getCurrentFragment();
            if(null==b || null==fragment.getClipMainAdapter()){
                return;
            }
            if(fragment!=b){
                return;
            }

            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() == fragment.getClipMainAdapter().getCount() - 1) {

                if(mIsCommunicating){
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");


                fragment.displayMoreData(true);
                setCommunicatingStatus(true);

                final ClipListBaseFragment finalFragment = fragment;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int offset = 0;
                        if(null!=finalFragment.mData ){
                            offset = finalFragment.mData.size();
                        }
                        switch (mViewPager.getCurrentItem()){
                            case CLIP_LIST_PAGE_NO_OF_TV:
                                mTvClipDataProvider.getClipData(offset);
                                break;
                            case CLIP_LIST_PAGE_NO_OF_VOD:
                                mVodClipDataProvider.getClipData(offset);
                                break;
                        }
                    }
                }, LOAD_PAGE_DELAY_TIME);
            }
        }
    }

    private ClipListActivity getClipListActivity(){
        return this;
    }

    /*検索結果タブ専用アダプター*/
    class ClipPagerAdapter extends FragmentStatePagerAdapter {
        public ClipPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mClipListFragmentFactory.createFragment(position, getClipListActivity());
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

    private void initView() {
        //テレビアイコンをタップされたらリモコンを起動する
        findViewById(R.id.header_stb_status_icon).setOnClickListener(mRemoteControllerOnClickListener);

        mTabScrollView = findViewById(R.id.clip_hs_tab_strip_scroll);
        mViewPager = findViewById(R.id.vp_clip_result);
        ClipPagerAdapter clipPagerAdapter
                = new ClipPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(clipPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager
                .SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setTab(position);

                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        setTv();
                        break;
                    case 1:
                        setVod();
                        break;
                }
            }
        });
        initTabVIew();
    }

    private void setVod(){

        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_VOD, this);
        //fragment.clearAllDatas();
        fragment.setMode(ClipMainAdapter.Mode.CLIP_LIST_MODE_VIDEO);
        mVodClipDataProvider.getClipData(1);
    }

    private void setTv(){
        ClipListBaseFragment fragment = mClipListFragmentFactory.createFragment(CLIP_LIST_PAGE_NO_OF_TV, this);
        //fragment.clearAllDatas();
        fragment.setMode(ClipMainAdapter.Mode.CLIP_LIST_MODE_TV);
        mTvClipDataProvider.getClipData(1);
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
                    mViewPager.setCurrentItem(position);
                    setTab(position);
                }
            });
            mLinearLayout.addView(tabTextView);
        }
    }

    /*インジケーター設置*/
    public void setTab(int position) {
        //mCurrentPageNum = position;
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

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    private ClipListBaseFragment getCurrentFragment(){

        int i= mViewPager.getCurrentItem();
        return mClipListFragmentFactory.createFragment(i, this);
    }
}
