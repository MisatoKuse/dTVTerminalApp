/*
 * Copyright (c) 2018 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.nttdocomo.android.tvterminalapp.activity.home;

import android.os.Bundle;
import android.os.Handler;
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
import com.nttdocomo.android.tvterminalapp.adapter.ContentsAdapter;
import com.nttdocomo.android.tvterminalapp.common.ContentsData;
import com.nttdocomo.android.tvterminalapp.common.DTVTLogger;
import com.nttdocomo.android.tvterminalapp.dataprovider.RentalDataProvider;

import java.util.ArrayList;
import java.util.List;

public class RentalListActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, AbsListView.OnScrollListener,
        RentalDataProvider.ApiDataProviderCallback {

    private ImageView mMenuImageView;
    private RentalDataProvider mRentalDataProvider;
    private ListView mListView;
    private View mLoadMoreView;
    private List mContentsList;
    private boolean mIsCommunicating = false;
    private ContentsAdapter mContentsAdapter;
    private final int NUM_PER_PAGE = 20;
    private final int LOAD_PAGE_DELEY_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rental_list_main_layout);
        mContentsList = new ArrayList();
        mMenuImageView = findViewById(R.id.header_layout_menu);
        mMenuImageView.setVisibility(View.VISIBLE);
        mMenuImageView.setOnClickListener(this);

        setTitleText(getString(R.string.rental_title));
        resetPaging();

        initView();
        mRentalDataProvider = new RentalDataProvider(this);
        mRentalDataProvider.getRentalData(1);
    }

    /**
     * アダプタを設定
     */
    private void initView() {

        mListView = findViewById(R.id.rental_list);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        if (mContentsList == null) {
            mContentsList = new ArrayList();
        }
        mContentsAdapter = new ContentsAdapter(this, mContentsList,
                ContentsAdapter.ActivityTypeItem.TYPE_RENTAL_RANK);
        mListView.setAdapter(mContentsAdapter);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.search_load_more, null);
    }

    /**
     * 再読み込み時のダイアログ表示処理
     *
     * @param b
     */
    private void displayMoreData(boolean b) {
        if (null != mListView && null != mLoadMoreView) {
            if (b) {
                mListView.addFooterView(mLoadMoreView);
            } else {
                mListView.removeFooterView(mLoadMoreView);
            }
        }
    }

    /**
     * 再読み込み時の処理
     */
    private void resetCommunication() {
        displayMoreData(false);
        setCommunicatingStatus(false);
    }

    /**
     * 再読み込み実施フラグ設定
     *
     * @param b
     */
    private void setCommunicatingStatus(boolean b) {
        synchronized (this) {
            mIsCommunicating = b;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mMenuImageView) {
            onSampleGlobalMenuButton_PairLoginOk();
        }
    }

    @Override
    public void rentalListCallback(List<ContentsData> contentsDataList) {
        if (null == contentsDataList) {
            //通信とJSON Parseに関してerror処理
            DTVTLogger.debug("ClipListActivity::VodClipListCallback, クリップデータ取得失敗");
            Toast.makeText(this, "クリップデータ取得失敗", Toast.LENGTH_SHORT);
            resetPaging();
            resetCommunication();
            return;
        }

        if (0 == contentsDataList.size()) {
            resetCommunication();
            return;
        }

        int pageNumber = getCurrentNumber();
        for (int i = pageNumber * NUM_PER_PAGE; i < (pageNumber + 1) * NUM_PER_PAGE
                && i < contentsDataList.size(); i++) { //mPageNumber
            mContentsList.add(contentsDataList.get(i));
        }

        DTVTLogger.debug("WatchListenVideoCallback, mData.size==" + mContentsList.size());

        resetCommunication();
        mContentsAdapter.notifyDataSetChanged();
    }

    /**
     * ページングリセット
     */
    private void resetPaging() {
        synchronized (this) {
            setCommunicatingStatus(false);
            if (null != mContentsList) {
                mContentsList.clear();
                if (null != mContentsAdapter) {
                    mContentsAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * ページング数取得
     *
     * @return
     */
    private int getCurrentNumber() {
        if (null == mContentsList) {
            return 0;
        }
        return mContentsList.size() / NUM_PER_PAGE;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mLoadMoreView == view) {
            return;
        }
        startActivity(TvPlayerActivity.class, null);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

        synchronized (this) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    absListView.getLastVisiblePosition() == mListView.getAdapter().getCount() - 1) {

                if (mIsCommunicating) {
                    return;
                }

                DTVTLogger.debug("onScrollStateChanged, do paging");

                displayMoreData(true);
                setCommunicatingStatus(true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        int offset = 0;
                        if (null != mContentsList) {
                            offset = mContentsList.size();
                        }
                        mRentalDataProvider.getRentalData(offset);

                    }
                }, LOAD_PAGE_DELEY_TIME);
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        synchronized (this) {
            if (firstVisibleItem + visibleItemCount == totalItemCount
                    && 0 != totalItemCount
                    ) {
                DTVTLogger.debug("onScroll, paging, firstVisibleItem=" + firstVisibleItem
                        + ", totalItemCount=" + totalItemCount + ", visibleItemCount="
                        + visibleItemCount);
            }
        }
    }
}
