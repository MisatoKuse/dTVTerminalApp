/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nttdocomo.android.tvterminalapp.R;
import com.nttdocomo.android.tvterminalapp.activity.BaseActivity;
import com.nttdocomo.android.tvterminalapp.activity.player.TvPlayerActivity;
import com.nttdocomo.android.tvterminalapp.adapter.WatchListenVideoBaseAdapter;
import com.nttdocomo.android.tvterminalapp.common.DTVTConstants;
import com.nttdocomo.android.tvterminalapp.dataprovider.WatchListenVideoListDataProvider;
import com.nttdocomo.android.tvterminalapp.dataprovider.data.WatchListenVideoContentInfo;

import java.util.ArrayList;
import java.util.List;

public class WatchingVideoListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, WatchListenVideoListDataProvider.WatchListenVideoListProviderCallback, AbsListView.OnScrollListener {

    private ListView mListView;

    private ImageView mMenuImageView = null;

    private WatchListenVideoBaseAdapter mWatchListenVideoBaseAdapter;
    private List mData = new ArrayList<>();

    private View mLoadMoreView;
    //private boolean mPagingStatus = false;
    private WatchListenVideoListDataProvider mWatchListenVideoListDataProvider=null;
    private final int NUM_PER_PAGE=10;
    private boolean mIsCommunicating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watching_video_list_main_layout);

        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);
        setTitleText(getString(R.string.str_watching_video_activity_title));

        resetPaging();

        initView();
        mWatchListenVideoListDataProvider= new WatchListenVideoListDataProvider(this);
        mWatchListenVideoListDataProvider.getWatchListenVideoData(1);
    }

    private void initView() {

        mListView = findViewById(R.id.video_watching_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        mWatchListenVideoBaseAdapter
                = new WatchListenVideoBaseAdapter(this, mData, R.layout.item_watching_video);
        mListView.setAdapter(mWatchListenVideoBaseAdapter);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }

    }

    private void setCommunicatingStatus(boolean b){
        synchronized (this){
            mIsCommunicating = b;
        }
    }

    private int getCurrentNumber(){
        if(null==mData){
            return 0;
        }
        return mData.size()/NUM_PER_PAGE;
    }

    private boolean isSkip(WatchListenVideoContentInfo watchListenVideoContentInfo){
        if(null==mData || 0==mData.size()){
            return false;
        }

        if(null==watchListenVideoContentInfo || 0==watchListenVideoContentInfo.size() ){
            return true;
        }

        WatchListenVideoContentInfo.WatchListenVideoContentInfoItem item1= (WatchListenVideoContentInfo.WatchListenVideoContentInfoItem)mData.get(mData.size() -1 );
        WatchListenVideoContentInfo.WatchListenVideoContentInfoItem item2 = watchListenVideoContentInfo.get(watchListenVideoContentInfo.size() -1 );
        return watchListenVideoContentInfo.isContentEqual(item1, item2);
    }

    @Override
    public void watchListenVideoListCallback(WatchListenVideoContentInfo watchListenVideoContentInfo) {
        if(null == watchListenVideoContentInfo){
            //通信とJSON Parseに関してerror処理
            Log.d(DTVTConstants.LOG_DEF_TAG, "ClipListActivity::VodClipListCallback, クリップデータ取得失敗");
            Toast.makeText(this, "クリップデータ取得失敗", Toast.LENGTH_SHORT);
            resetPaging();
            resetCommunication();
            return;
        }

        if(0==watchListenVideoContentInfo.size()){
            //doing
            resetCommunication();
            return;
        }

        /*
        WatchListenVideoContentInfo tmp = new WatchListenVideoContentInfo();
        Log.d("----", "WatchListenVideoCallback");
        //boolean clipFlag, String contentPictureUrl, String title, String rating
        WatchListenVideoContentInfo.WatchListenVideoContentInfoItem i1 = tmp.new WatchListenVideoContentInfoItem(
                "https://image5-a.beetv.jp/basic/img/beetv_image/1014/top_hd_org/10142461_top_hd_org.jpg",
                "千本桜", "4.0");
        WatchListenVideoContentInfo.WatchListenVideoContentInfoItem i2 = tmp.new WatchListenVideoContentInfoItem(
                "https://image5-a.beetv.jp/basic/img/title/10014265_top_hd_org.jpg",
                "サクラ大戦～桜華絢爛～", "5.0");

        for(int i=0;i<22;++i){
            String rating = String.valueOf((1.0f + 0.2f*i)%5.0);
            WatchListenVideoContentInfo.WatchListenVideoContentInfoItem iii = tmp.new WatchListenVideoContentInfoItem(
                    i1.mContentPictureUrl,
                    "千本桜" + (i + 1),  rating);
            watchListenVideoContentInfo.add(iii);
        }
        */

        if(isSkip(watchListenVideoContentInfo)){
            resetCommunication();
            return;
        }

        int pageNumber =  getCurrentNumber();
        for(int i= pageNumber*NUM_PER_PAGE;i<(pageNumber + 1)*NUM_PER_PAGE  && i<watchListenVideoContentInfo.size();++i){ //mPageNumber
            mData.add(watchListenVideoContentInfo.get(i ));
        }

        Log.d(DTVTConstants.LOG_DEF_TAG, "WatchListenVideoCallback, mData.size=="+ mData.size());

        resetCommunication();
        mWatchListenVideoBaseAdapter.notifyDataSetChanged();
    }

    private void resetCommunication(){
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(mLoadMoreView == view){
            return;
        }
        startActivity(TvPlayerActivity.class, null);
    }

    private void resetPaging(){
        synchronized (this){
            setCommunicatingStatus(false);
            if(null != mData){
                mData.clear();
                if(null!=mWatchListenVideoBaseAdapter){
                    mWatchListenVideoBaseAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private static final int sLoadPageDelayTime = 1000;

    private void displayMoreData(boolean b) {
        if(null!=mListView && null!=mLoadMoreView){
            if(b){
                mListView.addFooterView(mLoadMoreView);
            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        //int page = getCurrentNumber();
        synchronized (this) {
            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                    //&& 0 != firstVisibleItem
                    //&& page <= mMaxPage
                    //&& !mPagingStatus
                    ) {
                Log.d(DTVTConstants.LOG_DEF_TAG, "onScroll, paging, firstVisibleItem=" + firstVisibleItem + ", totalItemCount=" + totalItemCount+ ", visibleItemCount=" + visibleItemCount);
                //setSetPagingStatus(true);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

        synchronized (this) {
            //if (mPagingStatus && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //if (absListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1
            //        && scrollState == SCROLL_STATE_IDLE && !mIsLoadingOrComplete && !mIsAllVisible) {
            if (/*mPagingStatus && */scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1) {
                /*
                if(mIsFirstScroll){
                    setFirstScroll(false);
                    return;
                }
                */

                if(mIsCommunicating){
                    return;
                }

                Log.d(DTVTConstants.LOG_DEF_TAG, "onScrollStateChanged, do paging");

                displayMoreData(true);
                setCommunicatingStatus(true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int offset = 0;
                        if(null!=mData ){
                            offset = mData.size();
                        }
                        mWatchListenVideoListDataProvider.getWatchListenVideoData(offset);

                    }
                }, sLoadPageDelayTime);
            }
        }

    }
}